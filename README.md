Simple side project to experiment with different technologies.

This project is basically the title, an event management system, a bit like ticketmaster.
You can manage events such as Conferences, Weddings, Night-outs and more. A user will be able to register for one. Events can also be sponsored.

TODO:
- [ ] QR code for ticket and add validation for it.
- [x] Postgres
- [x] Microservices
  - [x] Kafka used as a transport layer
- [ ] UI Simple JS or Angular
- [x] Kafka streaming for events. An attendee can subscribe on an event stream(can be a football match).
  - [ ] Create simulator that can generate fake event data for events.
  - [ ] Implement business logic
- [ ] Direct messaging between the attendees of the events(group chats) can be implemented by websockets maybe try in GOlang?
- [ ] Push notifications for attendees that can subscribe to organizers or sponsors with kafka maybe? Email, telephone?
  - [ ] Also push notifications for any changes in the event?
- [x] Authentication/authorisation (fusionauth)
  - [x] Add roles: Sponsor, Organizer, User, Admin
  - [x] Added roles for each CRUD operation on the different objects
- [ ] e commerce eshop for buying tickets to the events. Stripe for buying tickets?
- [ ] Add controllers that use web-sockets

Email TODOs:
- [x] User signup confirmation email - configure in IDP
- [ ] Password reset flow - configure in IDP
- [x] Ticket purchase confirmation
- [x] Event reminder (email + in-app)
  - Period prior to event is 2 days 
  - every hour scheduler that checks events and find their attendees and sends email that the event is soon starting and get ready
  - but then we need a mapping that checks that the email has been send so it does not retry (solved via redis)
- [ ] Email queue system
- [x] Feedback request email
  - scheduler for every hour that checks if events that have past gets their attendees and their ticket tot that event
    - and make sure that they attended using the used flag email them for feedback and if they enjoyed blabla
- [ ] Push notification setup (optional) - ideally implemented as a scope in IDP
  - make opt in opt out functionality for:
    - reminders for events
    - feedback after event
      - if they went how it was
      - if they didnt why they didnt
    - organizer is organizing the same or similar event they attended
