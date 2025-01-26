# boot up

cd ..
mvn clean install -T 3 -DskipTests
cd attendee-service
mvn package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/attendee-service:latest .
cd ../event-service
mvn package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/event-service:latest .
cd ../organizer-service
mvn package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/organizer-service:latest .
cd ../sponsor-service
mvn package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/sponsor-service:latest .
cd ../ticket-service
mvn package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/ticket-service:latest .

docker compose up