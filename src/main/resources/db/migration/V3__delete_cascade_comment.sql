-- COMMENT
ALTER TABLE comments
    MODIFY COLUMN content VARCHAR(100) NOT NULL;

ALTER TABLE comments
    DROP FOREIGN KEY comments_ibfk_3;

ALTER TABLE comments
    ADD CONSTRAINT
        FOREIGN KEY (parent_id)
            REFERENCES comments (id)
            ON DELETE CASCADE;
