FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /
COPY . .
RUN ./gradlew clean build

CMD ./gradlew bootRun