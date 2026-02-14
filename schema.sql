-- MDD Database Schema
-- Drop tables if they exist
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS subscription;
DROP TABLE IF EXISTS topic;
DROP TABLE IF EXISTS user;

-- USER table
CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_email (email),
  INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TOPIC table
CREATE TABLE topic (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  description TEXT,
  INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ARTICLE table
CREATE TABLE article (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  author_id BIGINT NOT NULL,
  topic_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (author_id) REFERENCES user(id) ON DELETE CASCADE,
  FOREIGN KEY (topic_id) REFERENCES topic(id) ON DELETE CASCADE,
  INDEX idx_author (author_id),
  INDEX idx_topic (topic_id),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- COMMENT table
CREATE TABLE comment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  content TEXT NOT NULL,
  author_id BIGINT NOT NULL,
  article_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (author_id) REFERENCES user(id) ON DELETE CASCADE,
  FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
  INDEX idx_article (article_id),
  INDEX idx_author (author_id),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- SUBSCRIPTION table
CREATE TABLE subscription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    UNIQUE KEY unique_user_topic (user_id, topic_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (topic_id) REFERENCES topic(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_topic (topic_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
