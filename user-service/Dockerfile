FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY ./build/libs/user-service-0.0.1-SNAPSHOT.jar user-service.jar
ENTRYPOINT ["java", "-jar", "user-service.jar"]