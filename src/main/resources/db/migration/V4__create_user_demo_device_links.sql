CREATE TABLE IF NOT EXISTS user_demo_device_links (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    user_id BIGINT NOT NULL,
    farm_id BIGINT NOT NULL,
    zone_id BIGINT NOT NULL,
    physical_device_id BIGINT NOT NULL,
    serial_number VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_user_demo_device_links_user_active
    ON user_demo_device_links (user_id, active);

CREATE INDEX IF NOT EXISTS idx_user_demo_device_links_zone_active
    ON user_demo_device_links (zone_id, active);

CREATE INDEX IF NOT EXISTS idx_user_demo_device_links_device_zone_active
    ON user_demo_device_links (physical_device_id, zone_id, active);

CREATE UNIQUE INDEX IF NOT EXISTS ux_user_demo_device_links_user_farm_zone_active
    ON user_demo_device_links (user_id, farm_id, zone_id)
    WHERE active = TRUE;
