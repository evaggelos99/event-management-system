CREATE TABLE IF NOT EXISTS ems_event.feedback(
    feedback_id BIGSERIAL PRIMARY KEY,
    event_uid UUID NOT NULL CONSTRAINT fk_event_uuid REFERENCES ems_event.events(id),
    rating INTEGER NOT NULL,
    liked_most TEXT,
    what_can_be_improved TEXT,
    other_comments TEXT
);