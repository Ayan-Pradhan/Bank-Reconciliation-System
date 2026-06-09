package com.spring.projects.app.configuration;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.spring.projects.app.entity.BankRecord;
import com.spring.projects.app.entity.RecordArchive;
import com.spring.projects.app.exception.LedgerNotFoundException;
import com.spring.projects.app.service.BankRecordService;
import com.spring.projects.app.service.FileLoaderService;
import com.spring.projects.app.service.RefundService;
import com.spring.projects.app.service.ReportService;
import com.spring.projects.app.service.StagingService;
import com.spring.projects.app.service.ArchiveService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ReconciliationBatchConfig {
		
	private final StagingService stagingService;
	private final BankRecordService bankRecordService;
	private final ArchiveService archiveService;
	private final ReportService reportService;
	private final RefundService refundService;
	private final FileLoaderService fileService;
	
	@Bean
	FlatFileItemReader<BankRecord> bankRecordReader() throws LedgerNotFoundException{
		return new FlatFileItemReaderBuilder<BankRecord>()
				.name("bankRecordReader")
				.resource(fileService.getBankLedger())
				.linesToSkip(1)
				.lineMapper(bankRecordLineMapper())
				.build();
	}
	
	// extract to mapper class
	@Bean
	LineMapper<BankRecord> bankRecordLineMapper() {
		DefaultLineMapper<BankRecord> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");													
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id", "txnId", "sender", "receiver", "amount", "timeStamp", "status");			
		
		BeanWrapperFieldSetMapper<BankRecord> fieldSetMapper = new BeanWrapperFieldSetMapper<BankRecord>();										
		fieldSetMapper.setTargetType(BankRecord.class);
		fieldSetMapper.setCustomEditors(Map.of(
		        LocalDateTime.class, new PropertyEditorSupport() {
		            @Override
		            public void setAsText(String text) {
		                setValue(LocalDateTime.parse(text));
		            }
		        }
		    ));


		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
	@Bean
	JdbcBatchItemWriter<BankRecord> bankRecordWriter(DataSource dataSource){
		return new JdbcBatchItemWriterBuilder<BankRecord>()
				.dataSource(dataSource)
				.sql(stagingService.insertRawBankRecords())
				.beanMapped()
				.build();
	}
	
	
	@Bean
	FlatFileItemReader<RecordArchive> userRecordReader() throws LedgerNotFoundException  {
		return new FlatFileItemReaderBuilder<RecordArchive>()
				.name("userRecordReader")
				.resource(fileService.getUserLedger())
				.linesToSkip(1)
				.lineMapper(userRecordLineMapper())
				.build();
	}
	
	@Bean
	LineMapper<RecordArchive> userRecordLineMapper() {
		DefaultLineMapper<RecordArchive> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");													
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id", "txnId", "sender", "receiver", "amount", "timeStamp", "status");			
		
		BeanWrapperFieldSetMapper<RecordArchive> fieldSetMapper = new BeanWrapperFieldSetMapper<RecordArchive>();										
		fieldSetMapper.setTargetType(RecordArchive.class);
		fieldSetMapper.setCustomEditors(Map.of(
		        LocalDateTime.class, new PropertyEditorSupport() {
		            @Override
		            public void setAsText(String text) {
		                setValue(LocalDateTime.parse(text));
		            }
		        }
		    ));


		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
	@Bean
	JdbcBatchItemWriter<RecordArchive> userRecordWriter(DataSource dataSource){
		return new JdbcBatchItemWriterBuilder<RecordArchive>()
				.dataSource(dataSource)
				.sql(stagingService.insertRawUserRecords())
				.beanMapped()
				.build();
	}
	
	@Bean
	Step loadBankFileIntoStaging(JobRepository repository, PlatformTransactionManager transactionManager, DataSource dataSource) throws LedgerNotFoundException {
		return new StepBuilder("load-bank-file-step",repository)
				.<BankRecord, BankRecord>chunk(20)
				.reader(bankRecordReader())
				.writer(bankRecordWriter(dataSource))
				.build();
	}
	
	@Bean
	Step loadUserFileIntoStaging(JobRepository repository, PlatformTransactionManager transactionManager, DataSource dataSource) throws LedgerNotFoundException  {
		return new StepBuilder("load-user-file-step",repository)
				.<RecordArchive, RecordArchive>chunk(20)
				.reader(userRecordReader())
				.writer(userRecordWriter(dataSource))
				.build();
	}
	
	@Bean
	Tasklet compareTasklet() {
	    return (contribution, chunkContext) -> {
            bankRecordService.insertMatched();     
	        return RepeatStatus.FINISHED;
	    };
	}
	
	@Bean
	Tasklet archiveTasklet() {
	    return (contribution, chunkContext) -> {
	    	archiveService.archive();     
	        return RepeatStatus.FINISHED;
	    };
	}
	
	@Bean
	Tasklet generateReportTasklet() {
		return (contribution, chunkContext) -> {
			reportService.generateReport();
			return RepeatStatus.FINISHED;
		};
	}
	
	@Bean
	Tasklet initiateRefundTasklet() {
		return (contribution, chunkContext) -> {
			refundService.initiateBatchRefund();
			return RepeatStatus.FINISHED;
		};
	}
	
	@Bean
	Tasklet clearStagingTasklet() {
		return (contribution, chunkContext) -> {
			stagingService.clearRawRecords();
			return RepeatStatus.FINISHED;
		};
	}
	
	@Bean
	Step compare(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("compare-step", repository)
				.tasklet(compareTasklet(), transactionManager)
				.build();
	}
	
	@Bean
	Step archive(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("archive-step", repository)
				.tasklet(archiveTasklet(), transactionManager)
				.build();
	}
	
	@Bean
	Step generateReport(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("report-generation-step", repository)
				.tasklet(generateReportTasklet(), transactionManager)
				.build();
	}
	
	@Bean
	Step initiateRefund(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("refund-initialization-step", repository)
				.tasklet(initiateRefundTasklet(), transactionManager)
				.build();
	}
	
	@Bean
	Step cleanUp(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("cleanup-step", repository)
				.tasklet(clearStagingTasklet(), transactionManager)
				.build();
	}

	@Bean
	Job bankJob(JobRepository repository, Step loadBankFileIntoStaging, Step loadUserFileIntoStaging, Step compare, Step archive, Step generateReport, Step initiateRefund, Step cleanUp) {
		return new JobBuilder("bankRecord-import-job", repository)
				.start(loadBankFileIntoStaging)
				.next(loadUserFileIntoStaging)
				.next(compare)
				.next(archive)
				.next(generateReport)
				.next(initiateRefund)
				.next(cleanUp)
				.build();
	}

}
