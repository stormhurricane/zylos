-- Flyway 9.x Initial-Skript for Zylos
-- as spring.flyway.baseline-on-migrate=true is active, 
-- does FLyway know that the base tables exist

ALTER TABLE enrollments DROP FOREIGN KEY fk_enrollments_course; 

ALTER TABLE enrollments 
ADD CONSTRAINT fk_enrollments_course 
FOREIGN KEY (course_id) REFERENCES courses(id) 
ON DELETE CASCADE;