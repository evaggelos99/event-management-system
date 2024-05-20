DELETE FROM tickets;
DELETE FROM events;
DELETE FROM sponsors;
DELETE FROM organizers;
DELETE FROM attendees;


INSERT INTO sponsors
values 
  (
    '49cb5c9f-0169-44ee-a19e-c56d150e837e',
    CURRENT_TIMESTAMP(),
    'RANDOM_NAME_SPONSOR',
    'www.someweb.com', 
    95000, 
    'test@test.com', 
    '9430249302', 
    'ABC 153 EFG'
  );
INSERT INTO organizers 
values 
  (
    'c64aee6b-bce3-4f5b-9578-0d83481b81bc', 
    CURRENT_TIMESTAMP(), 
    'name of the organizer', 
    'www.someweborganizer.com', 
    '"information" about the organizer', 
    ARRAY ['NIGHTLIFE', 
    'SPORT' ], 
    'test2@test.com', 
    '9430249302', 
    'ABC 151 EFG'
  );
INSERT INTO attendees 
values 
  (
    'd7265de2-3078-403c-a884-97256c3e0ebc', 
    CURRENT_TIMESTAMP(), 
    'attendee name', 
    'attendee name', 
    ARRAY []
  );
INSERT INTO events 
values 
  (
    '7e5a0a82-8716-4c0c-a681-72dec6af314d', 
    CURRENT_TIMESTAMP(), 
    'name of the event', 
    'place of the event', 
    'NIGHTLIFE', 
    ARRAY [], 
    'c64aee6b-bce3-4f5b-9578-0d83481b81bc', 
    '1500', 
    null, 
    CURRENT_TIME(), 
    EXTRACT(HOUR FROM CURRENT_TIMESTAMP)
  );
INSERT INTO tickets
values 
  (
    'ee4add4c-786e-4ec4-9bbd-0f566b3812cd', 
    CURRENT_TIMESTAMP(), 
    '7e5a0a82-8716-4c0c-a681-72dec6af314d', 
    'GENERAL_ADMISSION', 
    120, 
    true, 
    '12', 
    'Stage 3'
  );
