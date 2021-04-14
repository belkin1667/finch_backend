# gradle build 
FROM gradle:6.8.3-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# RUN gradle wrapper --gradle-version 2.13 && bash ./gradlew build
RUN gradle build --no-daemon
    
# jar file execution
FROM openjdk:11
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/finch_backend-0.0.1-SNAPSHOT.jar
COPY gcpkey.json /app/gcpkey.json
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/finch_backend-0.0.1-SNAPSHOT.jar"]
