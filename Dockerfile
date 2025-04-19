FROM node:18-bullseye
RUN apt update && apt install -y openjdk-17-jdk
WORKDIR /app
COPY . .

RUN npm --prefix frontend install && npm --prefix frontend run build
RUN cd backend && ./gradlew build --no-daemon -x test

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

CMD ["java", "-jar", "backend/build/libs/my-application.jar"]
