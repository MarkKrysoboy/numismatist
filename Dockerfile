FROM java:19
COPY target/numismatist-0.0.1-SNAPSHOT.jar numismatist.jar
ENTRYPOINT ["java", "-jar", "numismatist.jar"]