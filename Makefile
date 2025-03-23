-include .env

DOCKER_COMPOSE_FILE := docker-compose.yml
.DEFAULT_GOAL := run

run: build-services up

test:
	mvn clean install -T 2

build-services:
	$(shell ./scripts/boot-up.sh)

up:
	docker compose -f ${DOCKER_COMPOSE_FILE} up

down:
	docker compose -f ${DOCKER_COMPOSE_FILE} down