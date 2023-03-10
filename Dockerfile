FROM openjdk:19
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} numismatist.jar
ENTRYPOINT ["java", "-jar", "numismatist.jar"]