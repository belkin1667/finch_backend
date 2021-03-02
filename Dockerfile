# gradle build 
FROM gradle:4.7.0-jdk8-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon
    
# jar file execution
FROM openjdk:11
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/finch_backend-0.0.1-SNAPSHOT.jar /app/finch_backend-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/finch_backend-0.0.1-SNAPSHOT.jar"]
