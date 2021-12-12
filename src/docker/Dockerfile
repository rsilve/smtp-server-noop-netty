FROM openjdk:14.0-slim

ENV DEBIAN_FRONTEND noninteractive

COPY target/appassembler/* /work

COPY src/docker/logback.xml /logback.xml
COPY src/docker/entrypoint.sh /entrypoint.sh



ENTRYPOINT ["bash", "/entrypoint.sh"]
