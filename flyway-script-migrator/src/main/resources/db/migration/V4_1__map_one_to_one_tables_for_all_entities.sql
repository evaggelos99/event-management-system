CREATE TABLE IF NOT EXISTS ems_admin.user_id_map_to_sponsor(
    entity_id UUID references ems_sponsor.sponsors(id),
    user_id UUID references ems_admin.users(id),
    PRIMARY KEY (entity_id, user_id)
);

CREATE TABLE IF NOT EXISTS ems_admin.user_id_map_to_attendee(
    entity_id UUID references ems_attendee.attendees(id),
    user_id UUID references ems_admin.users(id),
    PRIMARY KEY (entity_id, user_id)
);

CREATE TABLE IF NOT EXISTS ems_admin.user_id_map_to_organizer(
    entity_id UUID references ems_organizer.organizers(id),
    user_id UUID references ems_admin.users(id),
    PRIMARY KEY (entity_id, user_id)
);