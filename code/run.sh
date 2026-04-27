#!/bin/bash
set -e

echo "========================================="
echo "Building Spring Boot Microservices..."
echo "========================================="
./mvnw clean package -DskipTests

echo "========================================="
echo "Building and Starting Docker Containers..."
echo "========================================="
docker compose -f deployment/docker-compose/apps.yml up --build -d

echo "========================================="
echo "Application Started Successfully!"
echo "API Gateway: http://localhost:8989"
echo "Web App: http://localhost:8080"
echo "========================================="
