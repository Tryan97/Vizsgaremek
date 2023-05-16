FROM eclipse-temurin:17
WORKDIR /opt/app
COPY target/vizsgaremek-0.0.1-SNAPSHOT.jar /opt/app/vizsgaremek.jar
CMD ["java", "-jar", "vizsgaremek.jar"]