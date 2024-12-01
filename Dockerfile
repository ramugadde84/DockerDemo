# Use OpenJDK 21 runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file
COPY target/DockerDemo-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 9191

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
