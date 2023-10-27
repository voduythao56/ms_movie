FROM openjdk:11

WORKDIR /
COPY . .
RUN ./gradlew clean build -x test

CMD ./gradlew bootRun