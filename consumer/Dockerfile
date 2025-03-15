FROM eclipse-temurin:17-jdk

WORKDIR /consumers
COPY . .

RUN ./gradlew build --no-daemon -x test

CMD ["java", "-jar", "build/libs/consumer.jar"]
