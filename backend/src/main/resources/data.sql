CREATE SEQUENCE IF NOT EXISTS student_matriculation_seq START WITH 10000000 INCREMENT BY 1;

-- BASIC USERS
INSERT INTO users (id, first_name, last_name, email, password, private_address, user_type) VALUES
(1, 'Bruce', 'Banner', 'b.banner@zylos.com', '$2a$10$uVHDLl1nFDvP0wUS2Shfi.QZYT4pXWLPk0qc89BgsH1gxLeGPABfS', 'Australien', 'TEACHER'),
(2, 'Peter', 'Parker', 'p.parker@zylos.com', '$2a$10$uVHDLl1nFDvP0wUS2Shfi.QZYT4pXWLPk0qc89BgsH1gxLeGPABfS', 'Queens', 'STUDENT'),
(3, 'Stever', 'Rogers', 's.rogers@zylos.com', '$2a$10$uVHDLl1nFDvP0wUS2Shfi.QZYT4pXWLPk0qc89BgsH1gxLeGPABfS', 'New York', 'STUDENT');

-- TEACHER FLAGS
INSERT INTO teachers (id, chair, research_area) 
VALUES (1, 'Genetik', 'Spontan-Mutation');

-- Student Flags
INSERT INTO students (id, matriculation_number, study_subject) VALUES
(2, 9999998, 'Genetik'),
(3, 9999999, 'Sport');

-- COURSES
INSERT INTO courses (id, title, type, term, academic_year) VALUES
(1, 'Software Engineering 2', 'LECTURE', 'SUMMER', '2026'),
(2, 'Molecular Genetics', 'SEMINAR', 'WINTER', '2025');

---
INSERT INTO enrollments (id, user_id, course_id, enrolled_at) VALUES
(1, 1, 2, CURRENT_TIMESTAMP),
(2, 2, 2, CURRENT_TIMESTAMP);


-- AUTO-INCREMENTS 
-- ==========================================
ALTER TABLE users ALTER COLUMN id RESTART WITH 4;
ALTER TABLE courses ALTER COLUMN id RESTART WITH 3;
ALTER TABLE enrollments ALTER COLUMN id RESTART WITH 3;