#!/bin/bash
mvn versions:set -DnewVersion=$"TRAVIS_TAG"
mvn versions:commit
docker build -t alex9849/mc-pluginstats-server:$TRAVIS_TAG ./web
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker push alex9849/mc-pluginstats-server