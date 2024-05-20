CREATE TABLE IF NOT EXISTS organizers (
  id UUID ,
  last_updated TIMESTAMP NOT NULL,
  name VARCHAR UNIQUE NOT NULL,
  website VARCHAR,
  description VARCHAR NOT NULL,
  event_types VARCHAR ARRAY NOT NULL,
  email VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  physical_address VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS sponsors (
  id UUID ,
  last_updated TIMESTAMP NOT NULL,
  name VARCHAR UNIQUE NOT NULL,
  website VARCHAR NOT NULL,
  financial_contribution INTEGER,
  email VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  physical_address VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS attendees (
  id UUID ,
  last_updated TIMESTAMP NOT NULL,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  ticket_ids UUID ARRAY
);

CREATE TABLE IF NOT EXISTS events (
  id UUID ,
  last_updated TIMESTAMP NOT NULL,
  name VARCHAR NOT NULL,
  place VARCHAR NOT NULL,
  event_type VARCHAR NOT NULL,
  attendee_id UUID ARRAY NOT NULL,
  organizer_id UUID NOT NULL,
  limit_of_people INTEGER NOT NULL,
  sponsor_id UUID,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTERVAL DAY TO SECOND NOT NULL
);

CREATE TABLE IF NOT EXISTS tickets (
  id UUID ,
  last_updated TIMESTAMP NOT NULL,
  event_id UUID NOT NULL,
  ticket_type VARCHAR NOT NULL,
  price INTEGER NOT NULL,
  transferable BOOLEAN NOT NULL,
  seat VARCHAR NOT NULL,
  section VARCHAR NOT NULL
);

 ALTER TABLE attendees
    ADD FOREIGN KEY (ticket_ids) 
    REFERENCES tickets(id);

ALTER TABLE events
    ADD FOREIGN KEY (attendee_id) 
    REFERENCES tickets(id);

ALTER TABLE events
    ADD FOREIGN KEY (organizer_id) 
    REFERENCES organizers(id);

ALTER TABLE events
    ADD FOREIGN KEY (sponsor_id) 
    REFERENCES sponsors(id);

ALTER TABLE tickets
    ADD FOREIGN KEY (event_id) 
    REFERENCES events(id);

--ALTER TABLE attendees ADD CONSTRAINT ticket_ids FOREIGN KEY(ticket_ids) REFERENCES ticket(id) NOCHECK;

--ALTER TABLE events ADD CONSTRAINT attendee_id FOREIGN KEY(attendee_id) REFERENCES attendee(id) NOCHECK;

--ALTER TABLE events ADD CONSTRAINT organizer_id FOREIGN KEY(organizer_id) REFERENCES organizer(id) NOCHECK;

--ALTER TABLE events ADD CONSTRAINT sponsor_id FOREIGN KEY(sponsor_id) REFERENCES sponsor(id) NOCHECK;

--ALTER TABLE tickets ADD CONSTRAINT event_id FOREIGN KEY(event_id) REFERENCES event_t(id) NOCHECK;