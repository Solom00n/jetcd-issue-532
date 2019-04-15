FROM maven:3.6-jdk-8
WORKDIR /app
COPY ./ ./
RUN mvn -B clean install

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=0 /app/target/jetcd-issue-532-0.0.1-SNAPSHOT.jar ./
ENTRYPOINT java -jar jetcd-issue-532-0.0.1-SNAPSHOT.jar
