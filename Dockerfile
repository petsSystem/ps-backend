FROM bellsoft/liberica-openjdk-alpine-musl:17
VOLUME /tmp
COPY target/ps-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-cp","/app.jar","br.com.petshop.Application"]

EXPOSE 8080