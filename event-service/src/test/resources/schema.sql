CREATE SCHEMA ems_event;
CREATE SCHEMA ems_attendee;
CREATE SCHEMA ems_sponsor;
CREATE SCHEMA ems_organizer;

CREATE TABLE IF NOT EXISTS ems_event.events (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name VARCHAR NOT NULL,
  place VARCHAR NOT NULL,
  event_type VARCHAR NOT NULL,
  attendee_ids UUID ARRAY NOT NULL,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  sponsors_ids UUID ARRAY,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

CREATE TABLE IF NOT EXISTS ems_attendee.attendees (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  ticket_ids UUID ARRAY
);

CREATE TABLE IF NOT EXISTS ems_sponsor.sponsors (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
  name VARCHAR UNIQUE NOT NULL,
  website VARCHAR NOT NULL,
  financial_contribution INTEGER,
  email VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  physical_address VARCHAR NOT NULL
);

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


INSERT INTO ems_sponsor.sponsors
values 
  (
    '49cb5c9f-0169-44ee-a19e-c56d150e837e',
    CURRENT_TIMESTAMP(),
    CURRENT_TIMESTAMP(),
    'sint occaecat ',
    'www.adminimveniam.com', 
    95000, 
    'test@test.com', 
    '9430249302', 
    'nostrud exercitation ullamco'
  );
INSERT INTO ems_organizer.organizers
values 
  (
    'c64aee6b-bce3-4f5b-9578-0d83481b81bc', 
    CURRENT_TIMESTAMP(), 
    CURRENT_TIMESTAMP(),
    'Lorem ipsum dolor ', 
    'www.reprehenderit.com', 
    'consectetur adipiscing elit', 
    ARRAY ['NIGHTLIFE', 
    'SPORT' ], 
    'test2@test.com', 
    '9430249302', 
    'ullamco laboris nisi'
  );
INSERT INTO ems_attendee.attendees 
values 
  (
    'd7265de2-3078-403c-a884-97256c3e0ebc', 
    CURRENT_TIMESTAMP(), 
    CURRENT_TIMESTAMP(),
    'exercitation', 
    'laboris', 
    ARRAY []
  );