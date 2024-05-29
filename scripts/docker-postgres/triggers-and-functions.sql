-- functions that check if the uuid requested is in the corresponding tables
CREATE OR REPLACE FUNCTION check_uuids_exist(uuid_array UUID[])
RETURNS BOOLEAN AS $$
DECLARE
    uuid UUID;
BEGIN
    FOREACH uuid IN ARRAY uuid_array
    LOOP
        PERFORM FROM tickets WHERE id = uuid;
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
        PERFORM FROM attendees WHERE id = uuid;
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
        PERFORM FROM sponsors WHERE id = uuid;
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
  BEFORE INSERT OR UPDATE ON events
  FOR EACH ROW 
  EXECUTE FUNCTION before_insert_trigger_sponsors();

CREATE OR REPLACE TRIGGER check_uuids_before_insert_events
  BEFORE INSERT OR UPDATE ON events
  FOR EACH ROW
  EXECUTE FUNCTION before_insert_trigger_events();

CREATE OR REPLACE TRIGGER check_uuids_before_insert
  BEFORE INSERT OR UPDATE ON attendees
  FOR EACH ROW
  EXECUTE FUNCTION before_insert_trigger();