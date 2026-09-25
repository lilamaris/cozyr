ALTER TABLE room_op_policy ALTER COLUMN updated_at DROP NOT NULL;
ALTER TABLE room_op_policy ADD COLUMN created_at TIMESTAMP WITH TIME ZONE;

UPDATE room_op_policy
SET created_at = updated_at
WHERE created_at IS NULL;

ALTER TABLE room_op_policy ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE room_op_policy ADD COLUMN activated_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE room_op_policy ADD COLUMN deactivated_at TIMESTAMP WITH TIME ZONE;

UPDATE room_op_policy
SET activated_at = created_at;

ALTER TABLE room_op_policy
    ADD CONSTRAINT ck_room_op_policy_activation
    CHECK (
        (activated_at IS NOT NULL) <> (deactivated_at IS NOT NULL)
    );
