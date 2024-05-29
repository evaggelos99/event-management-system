
CREATE TYPE EVENT_TYPE_ENUM AS ENUM ('NIGHTLIFE', 'WEDDING', 'CONFERENCE', 'SPORT', 'OTHER');
CREATE TYPE TICKET_TYPE_ENUM AS ENUM ('GENERAL_ADMISSION', 'VIP', 'STUDENT', 'WORK');
CREATE TYPE ROLE_TYPE_ENUM AS ENUM ('ATTENDEE', 'ORGANIZER', 'SPONSOR');

-- when given a string it will cast it as an Enum
CREATE CAST (varchar AS EVENT_TYPE_ENUM) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS TICKET_TYPE_ENUM) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS ROLE_TYPE_ENUM) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS organizers (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  name TEXT UNIQUE NOT NULL,
  website TEXT ,
  information TEXT NOT NULL,
  event_types EVENT_TYPE_ENUM[] NOT NULL,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS sponsors (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  name TEXT UNIQUE NOT NULL,
  website TEXT NOT NULL,
  financial_contribution INTEGER,
  email TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  physical_address TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS attendees (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  ticket_ids UUID[]
);

CREATE TABLE IF NOT EXISTS events (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  name TEXT NOT NULL,
  place TEXT NOT NULL,
  event_type EVENT_TYPE_ENUM NOT NULL,
  attendee_ids UUID[] ,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  sponsors_ids UUID[],
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

CREATE TABLE IF NOT EXISTS tickets (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  event_id UUID NOT NULL,
  ticket_type TICKET_TYPE_ENUM NOT NULL,
  price INTEGER NOT NULL,
  transferable BOOLEAN NOT NULL,
  seat TEXT NOT NULL,
  section TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  email TEXT NOT NULL,
  role_type ROLE_TYPE_ENUM NOT NULL,
  image bytea -- FIXME 
);

CREATE TABLE IF NOT EXISTS roles(
  id SERIAL,
  user_id UUID,
  role_id UUID -- must reference a uuid from sponsors,attendees,organizers
);

ALTER TABLE IF EXISTS roles
  ADD CONSTRAINT fk_user_id
    FOREIGN KEY (user_id)
REFERENCES users(id);

ALTER TABLE IF EXISTS events
    ADD CONSTRAINT fk_organizer_id
        FOREIGN KEY (organizer_id)
REFERENCES organizers(id);

ALTER TABLE IF EXISTS tickets
    ADD CONSTRAINT fk_event_id
        FOREIGN KEY (event_id)
REFERENCES events (id);

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

-- seed
INSERT INTO users(
  id,
  created_at,
  last_updated,
  first_name ,
  last_name ,
  email ,
  role_type,
  image
) VALUES(
    gen_random_uuid(),
    now(), 
    now(),
    'firstName',
    'lastName',
    'test@gmail.com',
    'ORGANIZER',
    null
);