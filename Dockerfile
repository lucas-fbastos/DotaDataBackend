FROM bellsoft/liberica-openjdk-alpine:26
COPY build/libs/AghsLabs-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
