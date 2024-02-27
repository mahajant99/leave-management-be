# https://hub.docker.com/_/gradle
FROM gradle:7.4.1-jdk17 as build

# Copy the project files into the Docker image.
COPY . /home/gradle/src

# Build the project.
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

# https://hub.docker.com/_/openjdk
FROM openjdk:17-oracle

# Copy the jar to the production image from the builder stage.
COPY --from=build /home/gradle/src/build/libs/*.jar /usr/local/lib/app.jar

# Run the web service on container startup.
CMD ["java", "-jar", "/usr/local/lib/app.jar"]
