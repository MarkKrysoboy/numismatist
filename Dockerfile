FROM openjdk:19
COPY . .
#ADD target/numismatist-0.0.1-SNAPSHOT.jar numismatist.jar
CMD ["java", "-jar", "target/numismatist-0.0.1-SNAPSHOT.jar"]