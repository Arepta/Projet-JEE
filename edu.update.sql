-- Step 1: Drop the foreign key constraint
ALTER TABLE programs 
DROP FOREIGN KEY fk_programs_level;

-- Step 2: Drop the 'level' column
ALTER TABLE programs 
DROP COLUMN level;

-- Step 1: Add the new 'name' column with a default value of NULL
ALTER TABLE class 
ADD COLUMN name VARCHAR(100) DEFAULT NULL AFTER title;

ALTER TABLE class 
DROP COLUMN title;

ALTER TABLE class 
DROP COLUMN size;

-- Step 2: Add the 'level' column (nullable)
ALTER TABLE class 
ADD COLUMN level BIGINT UNSIGNED NULL AFTER program;

-- Step 3: Add the foreign key constraint for the 'level' column
ALTER TABLE class 
ADD CONSTRAINT fk_class_level 
FOREIGN KEY (level) 
REFERENCES class_level(id) 
ON DELETE SET NULL 
ON UPDATE CASCADE;
