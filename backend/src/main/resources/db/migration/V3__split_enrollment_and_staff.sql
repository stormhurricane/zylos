-- 1. New relational table for course staff
CREATE TABLE course_staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_staff_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT uq_course_staff_user UNIQUE (course_id, user_id)
);

-- 2. Data Migration: Extract all instructors from old Enrollments Table
INSERT INTO course_staff (course_id, user_id, role, assigned_at)
SELECT e.course_id, e.user_id, 'OWNER', e.enrolled_at
FROM enrollments e
JOIN users u ON e.user_id = u.id
WHERE u.user_type = 'TEACHER';

-- 3. Data Cleanup: Remove instrcutors from enrollment table
DELETE FROM enrollments 
WHERE user_id IN (
    SELECT id FROM users WHERE user_type = 'TEACHER'
);