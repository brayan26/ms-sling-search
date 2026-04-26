FROM eclipse-temurin:21-jdk-jammy AS builder

RUN groupadd -r appgroup && useradd -r -g appgroup -s /bin/false appuser

WORKDIR /builder

ARG JAR_FILE=applications/app-service/build/libs/*.jar
COPY ${JAR_FILE} application.jar

RUN chown appuser:appgroup application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted
RUN chown -R appuser:appgroup extracted

# Runtime
FROM eclipse-temurin:25-jre-noble

RUN groupadd -r appgroup && useradd -r -g appgroup -s /bin/false appuser

WORKDIR /application
VOLUME /tmp

RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

ARG JAR_FILE=applications/app-service/build/libs/*.jar
COPY --chown=appuser:appgroup ${JAR_FILE} application.jar

ENV TZ=America/Bogota
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=65 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

USER appuser

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar"]