DROP TABLE IF EXISTS staging_bank_records;
CREATE TABLE staging_bank_records (
    id BIGINT PRIMARY KEY,
    txn_id VARCHAR(50),
    sender VARCHAR(100),
    receiver VARCHAR(100),
    amount DECIMAL(12,2),
    time_stamp TIMESTAMP,
    status VARCHAR(20),
    is_processed BOOLEAN DEFAULT FALSE
);

DROP TABLE IF EXISTS staging_user_records;
CREATE TABLE staging_user_records (
    id BIGINT PRIMARY KEY,
    txn_id VARCHAR(50),
    sender VARCHAR(100),
    receiver VARCHAR(100),
    amount DECIMAL(12,2),
    time_stamp TIMESTAMP,
    status VARCHAR(20),
    is_processed BOOLEAN DEFAULT FALSE
);