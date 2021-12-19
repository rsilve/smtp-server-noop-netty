FROM openjdk:14.0-slim

RUN groupadd -r smtp && useradd -r -s /bin/false -g smtp smtp
WORKDIR /work

ENV DEBIAN_FRONTEND noninteractive

COPY target/appassembler/ /work/
COPY src/docker/entrypoint.sh /work/entrypoint.sh

RUN chown -R smtp:smtp /work
USER smtp

EXPOSE 2525/tcp

ENTRYPOINT ["bash", "/work/entrypoint.sh"]
