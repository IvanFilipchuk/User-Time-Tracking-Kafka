FROM openjdk:11-jdk-slim
WORKDIR /usr/src/app
COPY  ./target/kafka-streams-example.jar ./app.jar
CMD ["java", "-jar", "./app.jar"]
