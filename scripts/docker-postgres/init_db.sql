CREATE SCHEMA IF NOT EXISTS ems_sponsor;
CREATE SCHEMA IF NOT EXISTS ems_attendee;
CREATE SCHEMA IF NOT EXISTS ems_organizer;
CREATE SCHEMA IF NOT EXISTS ems_event;
CREATE SCHEMA IF NOT EXISTS ems_ticket;

CREATE TABLE IF NOT EXISTS ems_sponsor.sponsors (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  denomination TEXT UNIQUE NOT NULL,
  website TEXT NOT NULL,
  financial_contribution INTEGER,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);

CREATE TYPE EVENT_TYPE_ENUM AS ENUM ('NIGHTLIFE', 'WEDDING', 'CONFERENCE', 'SPORT', 'OTHER');
CREATE TYPE TICKET_TYPE_ENUM AS ENUM ('GENERAL_ADMISSION', 'VIP', 'STUDENT', 'WORK');

-- when given a string it will cast it as an Enum
CREATE CAST (text AS EVENT_TYPE_ENUM) WITH INOUT AS IMPLICIT;
CREATE CAST (text AS TICKET_TYPE_ENUM) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS ems_organizer.organizers (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  denomination TEXT UNIQUE NOT NULL,
  website TEXT ,
  information TEXT NOT NULL,
  event_types EVENT_TYPE_ENUM[] NOT NULL,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ems_attendee.attendees (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  ticket_ids UUID[]
);

CREATE TABLE IF NOT EXISTS ems_event.events (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  denomination TEXT NOT NULL,
  place TEXT NOT NULL,
  event_type EVENT_TYPE_ENUM NOT NULL,
  attendee_ids UUID[] ,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  sponsors_ids UUID[],
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

CREATE TABLE IF NOT EXISTS ems_ticket.tickets (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  event_id UUID NOT NULL,
  ticket_type TICKET_TYPE_ENUM NOT NULL,
  price INTEGER NOT NULL,
  transferable BOOLEAN NOT NULL,
  seat TEXT NOT NULL,
  section TEXT NOT NULL
);

-- functions that check if the uuid requested is in the corresponding tables
CREATE OR REPLACE FUNCTION check_uuids_exist(uuid_array UUID[])
RETURNS BOOLEAN AS $$
DECLARE
    uuid UUID;
BEGIN
    FOREACH uuid IN ARRAY uuid_array
    LOOP
        PERFORM FROM ems_ticket.tickets WHERE id = uuid;
        IF NOT FOUND THEN
            RETURN FALSE;
        END IF;
    END LOOP;
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_uuids_exist_events(uuid_array UUID[])
RETURNS BOOLEAN AS $$
DECLARE
    uuid UUID;
BEGIN
    FOREACH uuid IN ARRAY uuid_array
    LOOP
        PERFORM FROM ems_attendee.attendees WHERE id = uuid;
        IF NOT FOUND THEN
            RETURN FALSE;
        END IF;
    END LOOP;
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_uuids_exist_sponsors(uuid_array uuid[])
RETURNS BOOLEAN as $$
DECLARE
  uuid UUID;
BEGIN
    FOREACH uuid IN ARRAY uuid_array
    LOOP
        PERFORM FROM ems_sponsor.sponsors WHERE id = uuid;
        IF NOT FOUND THEN
            RETURN FALSE;
        END IF;
    END LOOP;
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

-- functions returning the triggers
CREATE OR REPLACE FUNCTION before_insert_trigger()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT check_uuids_exist(NEW.ticket_ids) THEN
        RAISE EXCEPTION 'One or more UUIDs do not exist in the tickets table';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION before_insert_trigger_events()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT check_uuids_exist_events(NEW.attendee_ids) THEN
        RAISE EXCEPTION 'One or more UUIDs do not exist in the attendees table';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION before_insert_trigger_sponsors()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT check_uuids_exist_sponsors(NEW.sponsors_ids) THEN
        RAISE EXCEPTION 'One or more UUIDs do not exist in the sponsors table';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- triggers
CREATE OR REPLACE TRIGGER check_uuids_before_insert_sponsors
  BEFORE INSERT OR UPDATE ON ems_event.events
  FOR EACH ROW 
  EXECUTE FUNCTION before_insert_trigger_sponsors();

CREATE OR REPLACE TRIGGER check_uuids_before_insert_events
  BEFORE INSERT OR UPDATE ON ems_event.events
  FOR EACH ROW
  EXECUTE FUNCTION before_insert_trigger_events();

CREATE OR REPLACE TRIGGER check_uuids_before_insert
  BEFORE INSERT OR UPDATE ON ems_attendee.attendees
  FOR EACH ROW
  EXECUTE FUNCTION before_insert_trigger();

ALTER TABLE IF EXISTS ems_event.events
    ADD CONSTRAINT fk_organizer_id
        FOREIGN KEY (organizer_id)
REFERENCES ems_organizer.organizers(id);

-- ALTER TABLE IF EXISTS events
--     ADD CONSTRAINT fk_sponsor_id
--         FOREIGN KEY (sponsor_id)
-- REFERENCES sponsors (id);

ALTER TABLE IF EXISTS ems_ticket.tickets
    ADD CONSTRAINT fk_event_id
        FOREIGN KEY (event_id)
REFERENCES ems_event.events (id);

SELECT
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'organizers';

SELECT 
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'sponsors';

SELECT 
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'attendees';

SELECT 
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE  
  table_name = 'events';

SELECT 
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'tickets';