-include .env

DOCKER_COMPOSE_FILE := docker-compose.yml
.DEFAULT_GOAL := run

run: build-services up

test:
	mvn clean install -T 2

build-services:
	mvn -f ./ticket-service clean compile assembly:single -DskipTests
	mvn -f ./sponsor-service clean compile assembly:single -DskipTests
	mvn -f ./organizer-service clean compile assembly:single -DskipTests
	mvn -f ./event-service clean compile assembly:single -DskipTests
	mvn -f ./attendee-service clean compile assembly:single -DskipTests

up:
	docker compose -f ${DOCKER_COMPOSE_FILE} up

down:
	docker compose -f ${DOCKER_COMPOSE_FILE} down