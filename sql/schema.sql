-- ============================================================
--  login_db  –  Schema & seed data
--  Run: mysql -u root -p < sql/schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS login_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE login_db;

-- ── Users table ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id          INT          NOT NULL AUTO_INCREMENT,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(64)  NOT NULL,          -- SHA-256 hex (64 chars)
    email       VARCHAR(100) NOT NULL UNIQUE,
    full_name   VARCHAR(100) NOT NULL,
    role        ENUM('admin','user') NOT NULL DEFAULT 'user',
    is_active   TINYINT(1)   NOT NULL DEFAULT 1,
    last_login  DATETIME         NULL,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── Seed: demo admin ────────────────────────────────────────
--  plain-text password: admin123
--  SHA-256 hash:        240be518fabd2724ddb6f04eeb1da5967448d7e831d06d
--                       (full 64-char value below)
INSERT INTO users (username, password, email, full_name, role)
VALUES (
    'admin',
    '240be518fabd2724ddb6f04eeb1da5967448d7e831d06d56135bb90d27dce86',
    'admin@example.com',
    'Admin User',
    'admin'
)
ON DUPLICATE KEY UPDATE id = id;

-- ── Seed: demo user ─────────────────────────────────────────
--  plain-text password: user123
--  SHA-256 hash:        7f83b1657ff1fc53b92dc18148a1d65dfc2d4b1fa3d677284addd200126d9069
INSERT INTO users (username, password, email, full_name, role)
VALUES (
    'user',
    '7f83b1657ff1fc53b92dc18148a1d65dfc2d4b1fa3d677284addd200126d9069',
    'user@example.com',
    'Regular User',
    'user'
)
ON DUPLICATE KEY UPDATE id = id;
