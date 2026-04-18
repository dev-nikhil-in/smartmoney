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

-- INDEXES
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_deleted ON users (is_deleted);