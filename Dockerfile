# 1️⃣ Build stage
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2️⃣ Run stage
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the jar built in the first stage
COPY --from=build /app/target/*.jar app.jar

# Expose your HTTPS port
EXPOSE 8443

# Add JVM options for better performance
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Start your app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=8443"]
