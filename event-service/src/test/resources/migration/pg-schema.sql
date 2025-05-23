CREATE SCHEMA IF NOT EXISTS ems_event;

CREATE TYPE EVENT_TYPE_ENUM AS ENUM ('NIGHTLIFE', 'WEDDING', 'CONFERENCE', 'SPORT', 'OTHER');

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

CREATE TABLE IF NOT EXISTS ems_event.events (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name TEXT NOT NULL,
  place TEXT NOT NULL,
  event_type EVENT_TYPE_ENUM NOT NULL,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

ALTER TABLE ems_event.events
    ADD COLUMN streamable BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS ems_event.event_stream(
  uuid UUID NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  stream_type TEXT NOT NULL,
  inception_time TIMESTAMP WITH TIME ZONE NOT NULL,
  message_type TEXT NOT NULL,
  content TEXT NOT NULL,
  language TEXT NOT NULL,
  is_important BOOLEAN NOT NULL,
  metadata JSON
);

CREATE INDEX index_uuid_event_stream ON ems_event.event_stream (uuid);