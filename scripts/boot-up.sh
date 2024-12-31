# boot up

# Attendee service docker build
#cd ../attendee-service
#mvn package spring-boot:repackage -DskipTests
#docker build -t evaggelosg99/attendee-service:latest .
#
## Event service docker build
#cd ../event-service
#mvn package spring-boot:repackage -DskipTests
#docker build -t evaggelosg99/event-service:latest .
#
## Organizer service docker build
#cd ../organizer-service
#mvn package spring-boot:repackage -DskipTests
#docker build -t evaggelosg99/organizer-service:latest .
#
## Sponsor service docker build
#cd ../sponsor-service
#mvn package spring-boot:repackage -DskipTests
#docker build -t evaggelosg99/sponsor-service:latest .
#
## Ticket service docker build
#cd ../ticket-service
#mvn package spring-boot:repackage -DskipTests
#docker build -t evaggelosg99/ticket-service:latest .
#cd ..

cd ..
mvn clean install -T 3 -DskipTests
cd attendee-service
mvn package spring-boot:repackage -DskipTests
docker build -t evaggelosg99/attendee-service:latest .
cd ../event-service
mvn package spring-boot:repackage -DskipTests
docker build -t evaggelosg99/event-service:latest .
cd ../organizer-service
mvn package spring-boot:repackage -DskipTests
docker build -t evaggelosg99/organizer-service:latest .
cd ../sponsor-service
mvn package spring-boot:repackage -DskipTests
docker build -t evaggelosg99/sponsor-service:latest .
cd ../ticket-service
mvn package spring-boot:repackage -DskipTests
docker build -t evaggelosg99/ticket-service:latest .

docker compose up