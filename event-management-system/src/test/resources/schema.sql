CREATE TABLE IF NOT EXISTS organizers (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  denomination VARCHAR UNIQUE NOT NULL,
  website VARCHAR,
  information VARCHAR NOT NULL,
  event_types VARCHAR ARRAY NOT NULL,
  email VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  physical_address VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS sponsors (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  denomination VARCHAR UNIQUE NOT NULL,
  website VARCHAR NOT NULL,
  financial_contribution INTEGER,
  email VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  physical_address VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS attendees (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  ticket_ids UUID ARRAY
);

CREATE TABLE IF NOT EXISTS events (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  denomination VARCHAR NOT NULL,
  place VARCHAR NOT NULL,
  event_type VARCHAR NOT NULL,
  attendee_ids UUID ARRAY NOT NULL,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  sponsors_ids UUID ARRAY,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

CREATE TABLE IF NOT EXISTS tickets (
  id UUID PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  event_id UUID NOT NULL,
  ticket_type VARCHAR NOT NULL,
  price INTEGER NOT NULL,
  transferable BOOLEAN NOT NULL,
  seat VARCHAR NOT NULL,
  section VARCHAR NOT NULL
);

ALTER TABLE events
    ADD FOREIGN KEY (organizer_id) 
    REFERENCES organizers(id);

ALTER TABLE tickets
    ADD FOREIGN KEY (event_id) 
    REFERENCES events(id);