FROM eclipse-temurin:11

RUN apt-get update && apt-get install -y wget
RUN mkdir app
WORKDIR /app
COPY ./default-components.json ./default-components.json
RUN wget https://github.com/helio-ecosystem/helio-rest/releases/download/v0.4.5/helio-rest-api-0.4.5.jar

EXPOSE 4567

CMD ["java", "-jar", "./helio-rest-api-0.4.5.jar"]