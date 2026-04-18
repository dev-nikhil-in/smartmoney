-- ============================================
-- SMARTMONEY DATABASE INITIAL SCHEMA
-- Version: V1
-- ============================================

CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(150)        NOT NULL UNIQUE,
    password   VARCHAR(255)        NOT NULL,
    role       ENUM ('USER','ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP           NULL DEFAULT NULL,
    is_deleted BOOLEAN                      DEFAULT FALSE,
    deleted_at TIMESTAMP                    NULL
);


CREATE TABLE loans
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id            BIGINT         NOT NULL,
    loan_name          VARCHAR(100)   NOT NULL,
    total_amount       DECIMAL(15, 2) NOT NULL,
    outstanding_amount DECIMAL(15, 2) NOT NULL,
    interest_rate      DECIMAL(5, 2)  NOT NULL,
    emi_amount         DECIMAL(10, 2) NOT NULL,
    tenure_months      INT            NOT NULL,
    start_date         DATE           NOT NULL,
    created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP      NULL     DEFAULT NULL,
    is_deleted         BOOLEAN                 DEFAULT FALSE,
    deleted_at         TIMESTAMP               NULL,

    CONSTRAINT fk_loans_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- INDEXES
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_deleted ON users (is_deleted);
CREATE INDEX idx_loans_user_id ON loans (user_id);
CREATE INDEX idx_loans_deleted ON loans (is_deleted);