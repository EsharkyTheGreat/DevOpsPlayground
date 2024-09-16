# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-oracle

# Set the working directory
WORKDIR /app

# Copy the application's jar file to the container
COPY target/*.jar /app/api.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["sh", "-c", "java -jar /app/api.jar"]