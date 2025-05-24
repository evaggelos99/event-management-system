CREATE SCHEMA IF NOT EXISTS ems_organizer;

CREATE TYPE EVENT_TYPE_ENUM AS ENUM ('NIGHTLIFE', 'WEDDING', 'CONFERENCE', 'SPORT', 'OTHER');

-- when given a string it will cast it as an Enum
CREATE CAST (text AS EVENT_TYPE_ENUM) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS ems_organizer.organizers (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name TEXT UNIQUE NOT NULL,
  website TEXT ,
  information TEXT NOT NULL,
  event_types EVENT_TYPE_ENUM[] NOT NULL,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);