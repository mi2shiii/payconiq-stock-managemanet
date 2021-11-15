#!/bin/bash
cd ..
echo 'building spring boot docker image ...'
cd backend || exit
echo 'maven is processing ...'
mvn clean install -DskipTests
echo 'project has been built. All tests were successful. ...'
mkdir -p target/dependency && (cd target/dependency || exit; jar -xf ../*.jar)
docker build -t stock/backend .
echo 'Docker image (stock/backend:latest) has been created successfully.'
cd ../prod-run || exit
docker-compose up
docker-compose rm -fsv