# boot up
# must be run from root directory

mvn clean install -T 3 -DskipTests
cd attendee-service
mvn clean package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/attendee-service:latest .
cd ../event-service
mvn clean package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/event-service:latest .
cd ../organizer-service
mvn clean package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/organizer-service:latest .
cd ../sponsor-service
mvn clean package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/sponsor-service:latest .
cd ../ticket-service
mvn clean package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/ticket-service:latest .
cd ../flyway-script-migrator
mvn clean package spring-boot:repackage -DskipTests
docker buildx build -t evaggelosg99/flyway:latest .