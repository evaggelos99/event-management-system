CREATE TABLE IF NOT EXISTS ems_attendee.email(
    email_id BIGSERIAL PRIMARY KEY,
    from_email VARCHAR(100) NOT NULL,
    to_email VARCHAR(100) NOT NULL,
    cc TEXT[],
    content TEXT NOT NULL
);

CREATE OR REPLACE FUNCTION set_from_name_default()
RETURNS VARCHAR(100) AS $$
BEGIN
    RETURN NEW.from_email;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_to_name_default()
RETURNS VARCHAR(100) AS $$
BEGIN
    RETURN NEW.to_email;
END;
$$ LANGUAGE plpgsql;

ALTER TABLE ems_attendee.email
    ADD COLUMN from_name VARCHAR(100) NOT NULL DEFAULT set_from_name_default();

ALTER TABLE ems_attendee.email
    ADD COLUMN to_name VARCHAR(100) NOT NULL DEFAULT set_to_name_default();

ALTER TABLE ems_attendee.email
    ADD COLUMN subject VARCHAR(100) NOT NULL DEFAULT 'empty';

ALTER TABLE ems_attendee.email
    RENAME COLUMN content to body;
