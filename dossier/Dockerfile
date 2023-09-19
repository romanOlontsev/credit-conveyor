FROM openjdk:17-slim
ARG LOGIN
ARG PASS
ENV MAIL_LOGIN=${LOGIN}
ENV MAIL_PASS=${PASS}
COPY target/dossier-0.0.1-SNAPSHOT.jar /dossier.jar
ENTRYPOINT exec java -jar /dossier.jar --MAIL_LOGIN=${MAIL_LOGIN} --MAIL_PASS=${MAIL_PASS}
