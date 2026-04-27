-- Đảm bảo sequence tồn tại
CREATE SEQUENCE IF NOT EXISTS payment_txn_id_seq START WITH 1 INCREMENT BY 50;

-- Tạo bảng nếu chưa có (phòng trường hợp V1 bị lỗi hoặc DB bị sạch)
CREATE TABLE IF NOT EXISTS payment_transactions
(
    id              BIGINT DEFAULT NEXTVAL('payment_txn_id_seq') NOT NULL,
    transaction_id  TEXT                                         NOT NULL UNIQUE,
    created_at      TIMESTAMP                                    NOT NULL,
    status          TEXT                                         NOT NULL,
    amount          NUMERIC                                      NOT NULL,
    name_transactor TEXT                                         NOT NULL,
    paypal_payment_id TEXT,
    PRIMARY KEY (id)
);

-- Thêm cột order_number nếu chưa có
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS order_number VARCHAR(255);
