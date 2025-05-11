
CREATE TABLE IF NOT EXISTS ems_sponsor.sponsors (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name TEXT UNIQUE NOT NULL,
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
  name TEXT UNIQUE NOT NULL,
  website TEXT ,
  information TEXT NOT NULL,
  event_types EVENT_TYPE_ENUM[] NOT NULL,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);

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

ALTER TABLE IF EXISTS ems_ticket.tickets
    ADD CONSTRAINT fk_event_id
        FOREIGN KEY (event_id)
REFERENCES ems_event.events (id);

ALTER TABLE IF EXISTS ems_attendee.ticket_mapping
    ADD CONSTRAINT fk_ticket_id
        FOREIGN KEY (ticket_id)
REFERENCES ems_ticket.tickets (id);

ALTER TABLE IF EXISTS ems_event.attendee_mapping
    ADD CONSTRAINT fk_event_attendee_id
        FOREIGN KEY (attendee_id)
REFERENCES ems_attendee.attendees(id);

ALTER TABLE IF EXISTS ems_event.sponsor_mapping
    ADD CONSTRAINT fk_event_sponsor_id
        FOREIGN KEY (sponsor_id)
REFERENCES ems_sponsor.sponsors(id);

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


-- used for seeding
CREATE OR REPLACE FUNCTION gen_text()
returns TEXT AS
$$
BEGIN
    RETURN gen_random_uuid()::TEXT;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION gen_interval()
returns INTERVAL AS
$$
BEGIN
    RETURN make_interval(hours => floor(random() * 10 + 1)::int);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION gen_number()
returns INTEGER AS
$$
BEGIN
    RETURN floor(random() * 150001)::int;
END;
$$ LANGUAGE plpgsql;