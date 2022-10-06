FROM openjdk:8

COPY ./target/remote-service-0.0.1-SNAPSHOT.jar remote-service-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","remote-service-0.0.1-SNAPSHOT.jar"]