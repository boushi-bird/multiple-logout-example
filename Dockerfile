FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/app

WORKDIR /opt/app

ENV JAR_FILE="libs/multiple-logout-example-0.1.0.jar"
ENV LOG_LEVEL="INFO"

CMD ["sh", "-c", "java -XX:TieredStopAtLevel=1 -Xverify:none -jar /opt/app/${JAR_FILE}"]
