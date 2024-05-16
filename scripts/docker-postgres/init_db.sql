CREATE TABLE IF NOT EXISTS organizer (
  uuid UUID PRIMARY KEY,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  name TEXT UNIQUE NOT NULL,
  website TEXT ,
  description TEXT NOT NULL,
  event_types TEXT[] NOT NULL,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS sponsor (
  uuid UUID PRIMARY KEY,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  name TEXT UNIQUE NOT NULL,
  website TEXT NOT NULL,
  financial_contribution INTEGER,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS attendee (
  uuid UUID PRIMARY KEY,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  ticket_ids UUID[]
);

CREATE TABLE IF NOT EXISTS event (
  uuid UUID PRIMARY KEY,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  name TEXT NOT NULL,
  place TEXT NOT NULL,
  event_type TEXT NOT NULL,
  attendee_ids UUID[] NOT NULL,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  sponsor_id UUID,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

CREATE TABLE IF NOT EXISTS ticket (
  uuid UUID PRIMARY KEY,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  event_id UUID NOT NULL,
  ticket_type TEXT NOT NULL,
  price INTEGER NOT NULL,
  transferable BOOLEAN NOT NULL,
  seat TEXT NOT NULL,
  section TEXT NOT NULL
);

ALTER TABLE IF EXISTS attendee
    ADD CONSTRAINT ticket_ids 
        FOREIGN KEY (uuid) 
REFERENCES ticket (uuid);

ALTER TABLE IF EXISTS event
    ADD CONSTRAINT attendee_ids
        FOREIGN KEY (uuid) 
REFERENCES attendee (uuid);

ALTER TABLE IF EXISTS event
    ADD CONSTRAINT organizer_id
        FOREIGN KEY (uuid) 
REFERENCES organizer (uuid);

ALTER TABLE IF EXISTS event
    ADD CONSTRAINT sponsor_id
        FOREIGN KEY (uuid) 
REFERENCES sponsor (uuid);

ALTER TABLE IF EXISTS ticket
    ADD CONSTRAINT event_id
        FOREIGN KEY (uuid) 
REFERENCES event (uuid);

SELECT
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'organizer';

SELECT 
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'sponsor';

SELECT 
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'attendee';

SELECT 
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'event';


SELECT 
  column_name, 
  data_type, 
  is_nullable,
  table_schema,
  ordinal_position,table_name
FROM 
  information_schema.columns 
WHERE 
  table_name = 'ticket';