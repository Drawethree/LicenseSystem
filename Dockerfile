# Use the official OpenJDK 11 image as the base image
FROM openjdk:11

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY ./target/LicenseSystem-0.0.1-SNAPSHOT.jar /app/LicenseSystem.jar

# Expose the port that your Spring Boot app will run on
EXPOSE 8080

# Command to run your application
CMD ["java", "-jar", "LicenseSystem.jar"]