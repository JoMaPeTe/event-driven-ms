FROM --platform=$BUILDPLATFORM eclipse-temurin:21-jre-alpine

# Creamos un directorio de trabajo
WORKDIR /app

COPY target/inventory-service-0.0.1-SNAPSHOT.jar /app/app.jar

#  EJECUCIÓN: Llamamos a la Ruta Absoluta
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
LABEL authors="jopecode"

