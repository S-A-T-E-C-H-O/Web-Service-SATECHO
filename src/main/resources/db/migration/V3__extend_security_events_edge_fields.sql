ALTER TABLE security_events
    ADD COLUMN IF NOT EXISTS zone_id BIGINT,
    ADD COLUMN IF NOT EXISTS pulse_duration_ms DOUBLE PRECISION,
    ADD COLUMN IF NOT EXISTS triggers_per_minute INTEGER;

CREATE INDEX IF NOT EXISTS idx_security_events_zone_id
    ON security_events (zone_id);

CREATE INDEX IF NOT EXISTS idx_security_events_farm_detected_at
    ON security_events (farm_id, detected_at DESC);
