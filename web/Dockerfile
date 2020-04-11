FROM openjdk:8-jdk-alpine
COPY target/web-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080/tcp
ENV MAX_NEW_REGISTRATIONS_PER_IP_PER_HOUR=10
ENV LOG_LEVEL=debug
RUN apk --no-cache add tzdata
ENTRYPOINT ["java","-jar","/app.jar","-Duser.timezone=CET"]