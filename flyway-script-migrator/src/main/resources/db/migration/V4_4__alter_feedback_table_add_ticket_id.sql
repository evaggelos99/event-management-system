ALTER TABLE ems_event.feedback ADD COLUMN created_at TIMESTAMP;
ALTER TABLE ems_event.feedback ADD COLUMN ticket_id UUID CONSTRAINT fk_ticket_uuid REFERENCES ems_ticket.tickets(id);