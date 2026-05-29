CREATE DATABASE IF NOT EXISTS blog;
USE blog;

-- 1. DROP TABLES (Ordered to avoid Foreign Key constraints)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS user_roles, comments, posts, threads, users, comment, post, thread, user, role;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. CREATE TABLES
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    bio TEXT,
    mascot ENUM('CAPYBARA', 'LEMUR', 'OCTOPUS', 'RED_PANDA', 'CHAMELEON', 'OWL', 'PENGUIN', 'ORCA')
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id),
    CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN'))
);

CREATE TABLE threads (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_title VARCHAR(255) NOT NULL,
    post_subtitle VARCHAR(255),
    slug VARCHAR(255) UNIQUE,
    content TEXT,
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    published_at DATETIME,
    author_id BIGINT,
    thread_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (thread_id) REFERENCES threads(id)
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    body TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    post_id BIGINT,
    user_id BIGINT,
    parent_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (parent_id) REFERENCES comments(id)
);
-- 1. Populate Users
-- Assuming 'mascot' is stored as a String/Enum in the DB
INSERT INTO users (username, password, email, bio, mascot) VALUES 
('capy_bara', 'hash_pass_1', 'capy@example.com', 'Just a chill developer.', 'CAPYBARA'),
('night_owl', 'hash_pass_2', 'owl@example.com', 'I code better at 3 AM.', 'OWL'),
('red_coder', 'hash_pass_3', 'panda@example.com', 'Living for the snacks.', 'RED_PANDA');

-- 2. Map Users to Roles as String values (ElementCollection)
INSERT INTO user_roles (user_id, role) VALUES 
(1, 'ROLE_USER'), (1, 'ROLE_ADMIN'), -- capy is user and admin
(2, 'ROLE_USER'),                     -- owl is user
(3, 'ROLE_USER');                     -- panda is user

-- 3. Populate Threads
INSERT INTO threads (name) VALUES 
('Java Frameworks'), 
('General Discussion'), 
('Mascot Appreciation');

-- 4. Populate Posts
-- status: DRAFT, PUBLISHED, ARCHIVED
INSERT INTO posts (post_title, post_subtitle, slug, content, status, author_id, thread_id, created_at, last_updated_at, published_at) VALUES 
('Intro to Spring Boot', 'The basics', 'intro-to-spring', 'Content about Spring Boot...', 'PUBLISHED', 1, 1, NOW(), NOW(), NOW()),
('Why Capybaras are the best', 'A deep dive', 'capy-best', 'They are just so round.', 'PUBLISHED', 1, 3, NOW(), NOW(), NOW()),
('Late night thoughts', 'Sleep is optional', 'late-night', 'Does code dream of electric bugs?', 'DRAFT', 2, 2, NOW(), NOW(), NULL);

-- 5. Populate Comments
-- Handling self-referencing (parent_id)
-- First: Top-level comments
INSERT INTO comments (body, post_id, user_id, is_deleted, created_at, parent_id) VALUES 
('Great article, thanks for sharing!', 1, 2, 0, NOW(), NULL),
('I prefer Red Pandas personally.', 2, 3, 0, NOW(), NULL);

-- Second: Replies (parent_id links to previous comments)
INSERT INTO comments (body, post_id, user_id, is_deleted, created_at, parent_id) VALUES 
('Thanks! Glad you liked it.', 1, 1, 0, NOW(), 1), -- Reply to comment ID 1
('How dare you!', 2, 1, 0, NOW(), 2);              -- Reply to comment ID 2
