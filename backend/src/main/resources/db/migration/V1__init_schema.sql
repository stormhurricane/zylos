-- =========================================================================
-- 1. CLEANUP (Sequence is critical: first drop child-tables)
-- =========================================================================
DROP TABLE IF EXISTS course_materials;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS users;

-- =========================================================================
-- 2. CORE-TABELLEN (independet tables)
-- =========================================================================
CREATE TABLE courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    academic_year VARCHAR(255) NOT NULL,
    term ENUM('SUMMER', 'WINTER') NOT NULL,
    type ENUM('LECTURE', 'SEMINAR') NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_courses_title UNIQUE (title) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_type VARCHAR(31) NOT NULL, 
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    private_address VARCHAR(255),
    profile_picture LONGTEXT,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================================
-- 3. SUB-TABELLEN (Joined-Inheritance for User)
-- =========================================================================
CREATE TABLE students (
    id BIGINT NOT NULL,
    matriculation_number BIGINT,
    study_subject VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_students_matriculation UNIQUE (matriculation_number),
    CONSTRAINT fk_students_user FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE teachers (
    id BIGINT NOT NULL,
    chair VARCHAR(255),
    research_area VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_teachers_user FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================================
-- 4. RELATION-TABLES & Materials (depends on Course and Users)
-- =========================================================================
CREATE TABLE enrollments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    enrolled_at DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_enrollments_user_course UNIQUE (user_id, course_id),
    CONSTRAINT fk_enrollments_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollments_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE course_materials (
    id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(255),
    file_size BIGINT NOT NULL,
    data LONGBLOB,
    created_at DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_materials_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;