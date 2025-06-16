CREATE TABLE IF NOT EXISTS ems_attendee.email(
    email_id BIGSERIAL PRIMARY KEY,
    from_email VARCHAR(100) NOT NULL,
    to_email VARCHAR(100) NOT NULL,
    cc TEXT[],
    content TEXT NOT NULL
);