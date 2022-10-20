FROM eclipse-temurin:11

RUN apt-get update && apt-get install -y wget
RUN mkdir app
WORKDIR /app
COPY ./default-components.json ./default-components.json
COPY ./helio-rest-api-0.4.9.jar ./helio-rest-api-0.4.9.jar
RUN mkdir components
COPY ./components/helio-builder-rmlmapper-0.1.2.jar ./components/helio-builder-rmlmapper-0.1.2.jar
COPY ./components/helio-builder-siot-0.1.10.jar      ./components/helio-builder-siot-0.1.10.jar
COPY ./components/helio-handler-jayway-0.1.1.jar    ./components/helio-handler-jayway-0.1.1.jar
COPY ./components/helio-provider-files-0.1.1.jar    ./components/helio-provider-files-0.1.1.jar
COPY ./components/helio-providers-web-0.1.2.jar     ./components/helio-providers-web-0.1.2.jar
COPY ./components/helio-provider-url-0.1.0.jar      ./components/helio-provider-url-0.1.0.jar
COPY ./components/helio-builder-siot-0.1.10.jar      ./components/helio-builder-siot-0.1.10.jar
COPY ./components/helio-provider-mqtt-0.1.1.jar      ./components/helio-provider-mqtt-0.1.1.jar
COPY ./components/helio-provider-mqtt-0.1.1.jar      ./components/helio-provider-mqtt-0.1.1.jar
COPY ./components/helio-action-json-cast-0.1.0.jar   ./components/helio-action-json-cast-0.1.0.jar
EXPOSE 4567

CMD ["java", "-jar", "./helio-rest-api-0.4.9.jar"]