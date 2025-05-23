CREATE SCHEMA IF NOT EXISTS  ems_event;

CREATE TABLE IF NOT EXISTS ems_event.events (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name VARCHAR NOT NULL,
  place VARCHAR NOT NULL,
  event_type VARCHAR NOT NULL,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

CREATE TABLE IF NOT EXISTS ems_event.attendee_mapping(
    event_id UUID,
    attendee_id UUID,
    PRIMARY KEY (event_id, attendee_id)
);

CREATE TABLE IF NOT EXISTS ems_event.sponsor_mapping(
    event_id UUID,
    sponsor_id UUID,
    PRIMARY KEY (event_id, sponsor_id)
);

ALTER TABLE ems_event.events
    ADD COLUMN streamable BOOLEAN DEFAULT FALSE;