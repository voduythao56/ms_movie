FROM openjdk:11

WORKDIR /
COPY . .
RUN ./gradlew clean build

CMD ./gradlew bootRun