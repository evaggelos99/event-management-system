CREATE SCHEMA ems_attendee;

CREATE TABLE IF NOT EXISTS ems_attendee.attendees (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  ticket_ids UUID ARRAY
);
