FROM bellsoft/liberica-openjdk-alpine-musl:17
VOLUME /tmp
COPY target/ps-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080