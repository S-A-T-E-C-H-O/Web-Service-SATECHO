ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_status varchar(32);
ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_token_expires_at timestamp with time zone;
ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_last_sent_at timestamp with time zone;
ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_resend_count integer NOT NULL DEFAULT 0;

UPDATE users
SET verification_status = CASE WHEN verified THEN 'VERIFIED' ELSE 'PENDING_VERIFICATION' END
WHERE verification_status IS NULL;

ALTER TABLE users ALTER COLUMN verification_status SET NOT NULL;
CREATE INDEX IF NOT EXISTS idx_users_verification_token ON users (verification_token);
