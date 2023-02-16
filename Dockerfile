FROM openjdk:19
RUN mkdir -p /numismatist
WORKDIR . /numismatist/target
#ADD target/numismatist-0.0.1-SNAPSHOT.jar numismatist.jar
CMD ["java", "-jar", "numismatist-0.0.1-SNAPSHOT.jar"]