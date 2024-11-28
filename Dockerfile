FROM openjdk:8

VOLUME /tmp

COPY ./target/javasec-*.jar /app.jar

EXPOSE 8888

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

CMD ["--spring.profiles.active=docker"]
