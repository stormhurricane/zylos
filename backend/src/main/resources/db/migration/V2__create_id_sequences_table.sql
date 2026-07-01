-- 1. Erstelle die Tabelle für die emulierten Sequenzen
-- 1. Create the table for the emulated sequences
CREATE TABLE IF NOT EXISTS id_sequences (
    sequence_name VARCHAR(50) NOT NULL,
    next_val BIGINT NOT NULL,
    PRIMARY KEY (sequence_name)
);

-- 2. Initialise the counter for matriculation numbers at 10,000,000
INSERT INTO id_sequences (sequence_name, next_val) 
VALUES ('student_mat_seq', 10000000)
ON DUPLICATE KEY UPDATE sequence_name=sequence_name;