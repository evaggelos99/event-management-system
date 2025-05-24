CREATE SCHEMA IF NOT EXISTS ems_attendee;

CREATE TABLE IF NOT EXISTS ems_attendee.ticket_mapping(
    attendee_id UUID,
    ticket_id UUID,
    PRIMARY KEY (attendee_id, ticket_id)
);

CREATE TABLE IF NOT EXISTS ems_attendee.attendees(
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL
);