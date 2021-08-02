FROM java:8

VOLUME /tmp

ADD hello-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8888

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]