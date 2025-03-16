#!/bin/bash

cd ../shared_module && ./gradlew build && ./gradlew publishToMavenLocal
cd ../consumer && ./gradlew build
cd ../frontend && npm run build && cd ../backend && ./gradlew build
docker-compose up --build