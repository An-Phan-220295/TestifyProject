-- Run outside the script (in psql or GUI)
-- CREATE DATABASE Testify;

-- Switch to the correct database
-- \c Testify

-- ENUM Types
CREATE TYPE gender_enum AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE status_enum AS ENUM ('ACTIVE', 'INACTIVE', 'BANNED');

-- ROLES
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- USERS
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    avatar_url TEXT,
    phone_number VARCHAR(20) UNIQUE,
    dob DATE,
    gender gender_enum DEFAULT 'OTHER',
    status status_enum DEFAULT 'ACTIVE',
    email_verified BOOLEAN DEFAULT FALSE,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id INTEGER NOT NULL REFERENCES roles(id)
);

-- TESTS
CREATE TABLE tests (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    instructions TEXT,
    total_score NUMERIC(5,2),
    total_questions INT,
    time_limit INT,
    is_public BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_by INT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SECTIONS
CREATE TABLE sections (
    id SERIAL PRIMARY KEY,
    test_id INT NOT NULL REFERENCES tests(id),
    title VARCHAR(255),
    description TEXT,
    "order" INT,
    time_limit INT,
    is_randomized BOOLEAN DEFAULT FALSE,
    number_questions INT,
    total_score NUMERIC(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- QUESTIONS
CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    section_id INT NOT NULL REFERENCES sections(id),
    title TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    difficulty VARCHAR(50),
    score NUMERIC(5,2),
    explanation TEXT,
    is_publish BOOLEAN DEFAULT FALSE,
    created_by INT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ANSWERS (đã thêm question_id + FK)
CREATE TABLE answers (
    id SERIAL PRIMARY KEY,
    question_id INT NOT NULL REFERENCES questions(id),
    content TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    "order" INT,
    explanation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE answer_images (
    answer_id INT PRIMARY KEY REFERENCES answers(id),
    url TEXT NOT NULL,
    alt_text TEXT,
    caption TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TEST SUBMISSIONS
CREATE TABLE test_submissions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    test_id INT NOT NULL REFERENCES tests(id),
    score NUMERIC(5,2),
    started_at TIMESTAMP,
    submitted_at TIMESTAMP,
    duration INT,
    status VARCHAR(50),
    is_passed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SUBMISSION ANSWERS
CREATE TABLE submission_answers (
    id SERIAL PRIMARY KEY,
    submission_id INT NOT NULL REFERENCES test_submissions(id),
    question_id INT NOT NULL REFERENCES questions(id),
    is_correct BOOLEAN,
    score NUMERIC(5,2),
    duration INT,
    status VARCHAR(50),
    is_passed BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SUBMISSION ANSWER DETAILS
CREATE TABLE submission_answer_details (
    id SERIAL PRIMARY KEY,
    submission_answer_id INT NOT NULL REFERENCES submission_answers(id),
    answer_id INT NOT NULL REFERENCES answers(id),
    is_selected BOOLEAN DEFAULT FALSE
);

-------------------------------------------------
---- DATA MẪU ----
-- Insert roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'System administrator'),
('CREATOR', 'Test creator'),
('STUDENT', 'Regular student user');

-- Insert users
INSERT INTO users (email, username, password_hash, full_name, phone_number, dob, gender, status, email_verified, role_id)
VALUES
('admin@testify.com', 'admin', 'hashed_pw1', 'Admin User', '1111111111', '1980-01-01', 'MALE', 'ACTIVE', true, 1),
('creator@testify.com', 'creator', 'hashed_pw2', 'Creator User', '2222222222', '1990-02-02', 'FEMALE', 'ACTIVE', true, 2),
('student@testify.com', 'student', 'hashed_pw3', 'Student User', '3333333333', '2000-03-03', 'OTHER', 'ACTIVE', false, 3);

-- Insert test
INSERT INTO tests (title, description, instructions, total_score, total_questions, time_limit, is_public, is_active, created_by)
VALUES
('General Knowledge', 'A simple general test.', 'Answer all questions.', 5.00, 2, 15, true, true, 2);

-- Insert sections
INSERT INTO sections (test_id, title, description, "order", time_limit, number_questions, total_score)
VALUES
(1, 'Section 1', 'Basics', 1, 10, 2, 5.00);

-- Insert questions
INSERT INTO questions (section_id, title, type, difficulty, score, explanation, is_publish, created_by)
VALUES
(1, 'What is the capital of France?', 'SINGLE_CHOICE', 'EASY', 2.50, 'It is Paris.', true, 2),
(1, 'What is 5 + 3?', 'SINGLE_CHOICE', 'EASY', 2.50, 'Simple addition.', true, 2);

-- Insert answers (đã thêm question_id)
INSERT INTO answers (question_id, content, is_correct, "order", explanation)
VALUES
(1, 'Paris', true, 1, 'Correct answer'),
(1, 'Lyon', false, 2, 'Incorrect'),
(2, '8', true, 1, 'Correct answer'),
(2, '9', false, 2, 'Incorrect');

-- Insert answer_images
INSERT INTO answer_images (answer_id, url, alt_text, caption)
VALUES
(1, 'https://example.com/images/paris.png', 'Paris Image', 'Capital of France'),
(3, 'https://example.com/images/8.png', 'Number 8', 'Math Answer');

-- Insert test_submissions
INSERT INTO test_submissions (user_id, test_id, score, started_at, submitted_at, duration, status, is_passed)
VALUES
(3, 1, 5.00, now() - interval '15 minutes', now(), 15, 'COMPLETED', true);

-- Insert submission_answers
INSERT INTO submission_answers (submission_id, question_id, is_correct, score, duration, status, is_passed)
VALUES
(1, 1, true, 2.5, 7, 'ANSWERED', true),
(1, 2, true, 2.5, 8, 'ANSWERED', true);

-- Insert submission_answer_details
INSERT INTO submission_answer_details (submission_answer_id, answer_id, is_selected)
VALUES
(1, 1, true),
(2, 3, true);
