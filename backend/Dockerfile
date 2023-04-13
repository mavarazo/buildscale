FROM eclipse-temurin:17

RUN mkdir -p /app

COPY build/libs/buildscale-0.0.1-SNAPSHOT.jar /app/buildscale.jar

ENTRYPOINT ["java", "-jar", "/app/buildscale.jar"]
