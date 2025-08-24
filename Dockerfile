# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper files to the container
COPY gradlew .
COPY gradle ./gradle

# Copy the build.gradle file
COPY build.gradle .
COPY settings.gradle .

# Grant executable permissions to the Gradle wrapper
RUN chmod +x ./gradlew

# Download dependencies. This will be cached if the build.gradle file doesn't change.
RUN ./gradlew dependencies

# Copy the rest of the application's source code
COPY src ./src

# Build the application
RUN ./gradlew build

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "build/libs/field-manager-api-0.0.1-SNAPSHOT.jar"]
