FROM openjdk:17-ea-jdk-alpine
COPY target/server.jar app.jar
EXPOSE 8080/tcp
ENV MAX_NEW_REGISTRATIONS_PER_IP_PER_HOUR=10
ENV LOG_LEVEL=debug
ENV TZ=Europe/Berlin
RUN apk --no-cache add tzdata
ENTRYPOINT ["java","-jar","/app.jar"]
