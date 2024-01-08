#!/bin/bash

# Stop and remove existing containers with volumes
docker-compose -f ./src/main/resources/docker-compose.yml down -v

# Start new containers
docker-compose -f ./src/main/resources/docker-compose.yml up -d