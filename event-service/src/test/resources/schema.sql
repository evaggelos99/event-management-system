CREATE SCHEMA ems_event;

CREATE TABLE IF NOT EXISTS ems_event.events (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name VARCHAR NOT NULL,
  place VARCHAR NOT NULL,
  event_type VARCHAR NOT NULL,
  attendee_ids UUID ARRAY NOT NULL,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  sponsors_ids UUID ARRAY,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

ALTER TABLE ems_event.events
    ADD COLUMN streamable BOOLEAN DEFAULT FALSE;