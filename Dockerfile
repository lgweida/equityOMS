# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the build.gradle.kts and settings.gradle.kts files
#COPY build.gradle.kts settings.gradle.kts ./

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
# Copy the source code
COPY src ./src

RUN chmod +x ./gradlew
# Build the application
RUN ./gradlew build --stacktrace --info


# Expose port 8081 (the port your application is running on)
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "build/libs/equityOMS-0.0.1-SNAPSHOT.jar"]