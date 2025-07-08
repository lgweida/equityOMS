# Use Java 21 base image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy Gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy source code
COPY src src

# Make Gradle wrapper executable
RUN chmod +x ./gradlew

# Build with verbose logging and save output to a file
RUN ./gradlew build --stacktrace --info --debug > build.log 2>&1 || true

# Print the log (even if the build fails)
RUN cat build.log

# Expose port
EXPOSE 8081

# Run the app
CMD ["java", "-jar", "build/libs/equity-oms-0.0.1-SNAPSHOT.jar"]