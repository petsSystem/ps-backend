FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
VOLUME /tmp
COPY target/ps-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080