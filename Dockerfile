#
# Build stage
#
FROM gradle:7.4.1-jdk11-alpine AS build
ENV GRADLE_HOME=/home/gradle/src
COPY --chown=gradle:gradle . $GRADLE_HOME
WORKDIR $GRADLE_HOME
RUN gradle build -x test

#
# Package stage
#
FROM openjdk:11-jre-slim
EXPOSE 8080 8000

#
# Copy war to root folder
#
COPY --from=build /home/gradle/src/build/libs/impact-back-end-api.war application.war
