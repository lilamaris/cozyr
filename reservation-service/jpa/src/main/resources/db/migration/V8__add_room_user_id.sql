ALTER TABLE room
    ADD COLUMN user_id UUID;

-- Existing rooms have no creator information and are intentionally discarded.
-- Remove their dependent data explicitly because there are no cascading foreign keys.
DELETE FROM seat_occupancy
WHERE room_id IN (SELECT id FROM room WHERE user_id IS NULL);

DELETE FROM reservation
WHERE room_id IN (SELECT id FROM room WHERE user_id IS NULL);

DELETE FROM daily_reservation_usage
WHERE room_id IN (SELECT id FROM room WHERE user_id IS NULL);

DELETE FROM seat
WHERE room_id IN (SELECT id FROM room WHERE user_id IS NULL);

DELETE FROM room_schedule_slot
WHERE room_id IN (SELECT id FROM room WHERE user_id IS NULL);

DELETE FROM room_op_policy
WHERE room_id IN (SELECT id FROM room WHERE user_id IS NULL);

DELETE FROM room
WHERE user_id IS NULL;

ALTER TABLE room
    ALTER COLUMN user_id SET NOT NULL;
