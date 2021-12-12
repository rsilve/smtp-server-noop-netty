FROM openjdk:14.0-slim

ENV DEBIAN_FRONTEND noninteractive

COPY target/appassembler/ /work/

COPY src/docker/logback.xml /work/logback.xml
COPY src/docker/entrypoint.sh /work/entrypoint.sh

WORKDIR /work

EXPOSE 2525/tcp

ENTRYPOINT ["bash", "/work/entrypoint.sh"]
