# Use official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk-jammy as builder

# Set working directory
WORKDIR /app

# Copy gradle files first to leverage Docker cache
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy source code
COPY src src

# Build the application
RUN ./gradlew build --no-daemon

# Create a smaller runtime image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the built application from builder image
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port your app runs on (Spring Boot default is 8080)
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS=""

# Command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]