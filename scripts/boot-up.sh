# boot up

# Attendee service docker build
cd ../attendee-service
mvn package spring-boot:repackage
docker build -t evaggelosg99/attendee-service:latest .

# Event service docker build
cd ../event-service
mvn package spring-boot:repackage
docker build -t evaggelosg99/event-service:latest .

# Organizer service docker build
cd ../organizer-service
mvn package spring-boot:repackage
docker build -t evaggelosg99/organizer-service:latest .

# Sponsor service docker build
cd ../sponsor-service
mvn package spring-boot:repackage
docker build -t evaggelosg99/sponsor-service:latest .

# Sponsor service docker build
cd ../eureka-server
mvn package spring-boot:repackage
docker build -t evaggelosg99/eureka-server:latest .

# Ticket service docker build
cd ../ticket-service
mvn package spring-boot:repackage
docker build -t evaggelosg99/ticket-service:latest .
cd ..

docker compose up