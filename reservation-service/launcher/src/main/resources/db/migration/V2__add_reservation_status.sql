ALTER TABLE reservation
    ADD COLUMN status VARCHAR(20) DEFAULT 'RESERVED';

UPDATE reservation
SET status = 'RESERVED'
WHERE status IS NULL;

ALTER TABLE reservation
    ALTER COLUMN status SET NOT NULL;

ALTER TABLE reservation
    ALTER COLUMN status DROP DEFAULT;