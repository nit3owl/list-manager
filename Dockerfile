# Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
# Add executable permissions to gradlew and build
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the built artifact from builder stage
# Note: Update the path according to your Gradle build output directory
COPY --from=builder /app/build/libs/*.jar app.jar

# Set ownership to non-root user
RUN chown spring:spring /app/app.jar

# Switch to non-root user
USER spring:spring

# Set JVM options
ENV JAVA_OPTS="-Xms512m -Xmx512m"

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]