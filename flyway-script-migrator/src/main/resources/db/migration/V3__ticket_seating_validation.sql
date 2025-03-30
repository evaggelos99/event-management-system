
CREATE OR REPLACE FUNCTION ems_ticket.check_seating_fun_before_insert_tickets(seatAtr text, sectionAttr text, eventIdAttr uuid)
RETURNS BOOLEAN as $$
BEGIN
    PERFORM FROM ems_ticket.tickets WHERE seat = seatAtr AND section = sectionAttr AND event_id = eventIdAttr;
        IF NOT FOUND THEN
            RETURN TRUE;
        END IF;
    RETURN FALSE;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION ems_ticket.before_insert_trigger_tickets()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT ems_ticket.check_seating_fun_before_insert_tickets(NEW.seat, NEW.section, NEW.event_id) THEN
            RAISE EXCEPTION 'The values for seat, section, and event_id cannot be the same.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_seating_before_insert_tickets
  BEFORE INSERT OR UPDATE ON ems_ticket.tickets
  FOR EACH ROW
  EXECUTE FUNCTION ems_ticket.before_insert_trigger_tickets();

