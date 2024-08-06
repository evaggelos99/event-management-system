CREATE SCHEMA ems_organizer;

CREATE TABLE IF NOT EXISTS ems_organizer.organizers (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name VARCHAR UNIQUE NOT NULL,
  website VARCHAR,
  information VARCHAR NOT NULL,
  event_types VARCHAR ARRAY NOT NULL,
  email VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  physical_address VARCHAR NOT NULL
);