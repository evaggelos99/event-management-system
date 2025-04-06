Simple side project to experiment with different technologies.

This project is basically the title, event management system, a bit like ticketmaster.
You can manage events such as Conferences, Weddings, Night-outs and more. A user will be able to register for one. Events can also be sponsored.

TODO:
- [ ] QR code for ticket and add validation for it.
- [x] Postgres
  - [ ] Roles within the Database
- [x] Microservices
  - [ ] Some type of service discovery
  - [x] Kafka used as a transport layer
- [ ] Redis for caching front page stuff.
- [ ] UI Simple JS or Angular
- [ ] Kafka streaming for events. An attendee can subscribe on an event stream(can be a football match).
- [ ] Direct messaging between the attendees of the events(group chats) can be implemented by websockets maybe try in GOlang?
- [ ] Push notifications for attendees that can subscribe to organizers or sponsors with kafka maybe? Email, telephone?
  - [ ] Also push notifications for any changes in the event?
- [x] Authentication/authorisation (fusionauth)
  - [x] Add roles: Sponsor, Organizer, User, Admin
  - [x] Added roles for each CRUD operation on the different objects
- [ ] e commerce eshop for buying tickets to the events. Stripe for buying tickets?
- [ ] Add controllers that use web-sockets