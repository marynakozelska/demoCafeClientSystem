FROM openjdk:17-jdk-alpine

WORKDIR /opt/cafe-client-system

ADD target/demoCafeClientSystem-0.0.1-SNAPSHOT.jar root.jar
EXPOSE 8080

CMD java -jar root.jar
