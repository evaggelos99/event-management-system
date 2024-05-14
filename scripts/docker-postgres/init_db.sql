CREATE TABLE IF NOT EXISTS attendee (
    uuid UUID PRIMARY KEY,
    last_updated TIMESTAMP WITHOUT TIME ZONE,
    first_name TEXT,
    last_name TEXT,
    ticket_ids UUID[]
);

CREATE TABLE IF NOT EXISTS organizer (
    uuid UUID PRIMARY KEY,
    last_updated TIMESTAMP WITHOUT TIME ZONE,
    name TEXT,
    website TEXT,
    event_types TEXT[]
    -- maybe add contact_information reference? not right?
);

CREATE TABLE IF NOT EXISTS sponsor (
    uuid UUID PRIMARY KEY,
    last_updated TIMESTAMP WITHOUT TIME ZONE,
    name TEXT,
    website TEXT,
    financial_contribution INTEGER
    -- maybe add contact_information reference? not right?
);

CREATE TABLE IF NOT EXISTS event (
    uuid UUID PRIMARY KEY,
    last_updated TIMESTAMP WITHOUT TIME ZONE,
    name TEXT,
    place TEXT,
    event_type TEXT,
    attendee_ids UUID[],
    organizer_id UUID references organizer(uuid),
    limit_of_people INTEGER,
    sponsor_id UUID references sponsor(uuid),
    start_time TIMESTAMP WITH TIME ZONE,
    duration INTERVAL DAY TO SECOND
);

CREATE TABLE IF NOT EXISTS ticket (
    uuid UUID PRIMARY KEY,
    last_updated TIMESTAMP WITHOUT TIME ZONE,
    event_id UUID references event(uuid),
    ticket_type TEXT,
    price INTEGER,
    transferable BOOLEAN
    -- maybe add seating_info reference? not right?
);

CREATE TABLE IF NOT EXISTS contact_information (
    email TEXT,
    phone_number TEXT,
    physical_address TEXT,
    organizer_id uuid references organizer(uuid),
    sponsor_id uuid references sponsor(uuid)
);

CREATE TABLE IF NOT EXISTS seating_information (
    seat TEXT,
    section TEXT,
    physical_address TEXT,
    ticket_id uuid references ticket(uuid)
);