FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} microsservice-01-aws.jar
ENTRYPOINT ["java", "-jar", "microsservice-01-aws.jar"]