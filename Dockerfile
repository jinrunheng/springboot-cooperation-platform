FROM openjdk:8u121-jdk-alpine

RUN mkdir /app

WORKDIR /app

COPY target/springboot-cooperation-platform-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD [ "java", "-jar", "springboot-cooperation-platform-0.0.1-SNAPSHOT.jar" ]