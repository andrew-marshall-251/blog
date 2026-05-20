CREATE DATABASE IF NOT EXISTS blog;
USE blog;

-- 1. DROP TABLES (Ordered to avoid Foreign Key constraints)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS user_roles, comment, post, thread, user, role;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. CREATE TABLES
CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    bio TEXT,
    mascot ENUM('CAPYBARA', 'LEMUR', 'OCTOPUS', 'RED_PANDA', 'CHAMELEON', 'OWL', 'PENGUIN', 'ORCA')
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE thread (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE post (
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
    FOREIGN KEY (author_id) REFERENCES user(id),
    FOREIGN KEY (thread_id) REFERENCES thread(id)
);

CREATE TABLE comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    body TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    post_id BIGINT,
    user_id BIGINT,
    parent_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES comment(id)
);
-- 1. Populate Roles
INSERT INTO role (name) VALUES 
('ROLE_USER'), 
('ROLE_ADMIN'), 
('ROLE_MODERATOR');

-- 2. Populate Users
-- Assuming 'mascot' is stored as a String/Enum in the DB
INSERT INTO user (username, password, email, bio, mascot) VALUES 
('capy_bara', 'hash_pass_1', 'capy@example.com', 'Just a chill developer.', 'CAPYBARA'),
('night_owl', 'hash_pass_2', 'owl@example.com', 'I code better at 3 AM.', 'OWL'),
('red_coder', 'hash_pass_3', 'panda@example.com', 'Living for the snacks.', 'RED_PANDA');

-- 3. Map Users to Roles (Many-to-Many join table)
-- Assuming table name is user_roles
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1), (1, 2), -- capy is user and admin
(2, 1),         -- owl is user
(3, 1);         -- panda is user

-- 4. Populate Threads
INSERT INTO thread (name) VALUES 
('Java Frameworks'), 
('General Discussion'), 
('Mascot Appreciation');

-- 5. Populate Posts
-- status: DRAFT, PUBLISHED, ARCHIVED
INSERT INTO post (post_title, post_subtitle, slug, content, status, author_id, thread_id, created_at, last_updated_at, published_at) VALUES 
('Intro to Spring Boot', 'The basics', 'intro-to-spring', 'Content about Spring Boot...', 'PUBLISHED', 1, 1, NOW(), NOW(), NOW()),
('Why Capybaras are the best', 'A deep dive', 'capy-best', 'They are just so round.', 'PUBLISHED', 1, 3, NOW(), NOW(), NOW()),
('Late night thoughts', 'Sleep is optional', 'late-night', 'Does code dream of electric bugs?', 'DRAFT', 2, 2, NOW(), NOW(), NULL);

-- 6. Populate Comments
-- Handling self-referencing (parent_id)
-- First: Top-level comments
INSERT INTO comment (body, post_id, user_id, is_deleted, created_at, parent_id) VALUES 
('Great article, thanks for sharing!', 1, 2, 0, NOW(), NULL),
('I prefer Red Pandas personally.', 2, 3, 0, NOW(), NULL);

-- Second: Replies (parent_id links to previous comments)
INSERT INTO comment (body, post_id, user_id, is_deleted, created_at, parent_id) VALUES 
('Thanks! Glad you liked it.', 1, 1, 0, NOW(), 1), -- Reply to comment ID 1
('How dare you!', 2, 1, 0, NOW(), 2);              -- Reply to comment ID 2