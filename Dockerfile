# Stage 1: Build the application using Maven
# We use a specific Maven image that includes a JDK to compile our code.
# This is called a "multi-stage build". It keeps our final image small.
FROM maven:3.8.5-openjdk-11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file first. This is a Docker optimization.
# If the pom.xml doesn't change, Docker can reuse the cached dependency layer.
COPY pom.xml .

# Download all the project dependencies
RUN mvn dependency:go-offline

# Copy the rest of the source code into the container
COPY src ./src

# Package the application into a .jar file. We skip the tests during this build.
RUN mvn package -DskipTests


# Stage 2: Create the final, lightweight runtime image
# We use a slim OpenJDK image that only contains the Java Runtime Environment (JRE),
# which is all we need to run the compiled application.
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the .jar file that was created in the 'build' stage into our final image
COPY --from=build /app/target/bank-management-system-1.0.0.jar ./app.jar

# Expose the port that our Spring Boot application runs on
EXPOSE 8088

# The command to run when the container starts
# It executes our application's .jar file.
ENTRYPOINT ["java", "-jar", "app.jar"]
