FROM --platform=$BUILDPLATFORM eclipse-temurin:21-jre-alpine

# Creamos un directorio de trabajo
WORKDIR /app

# Copiamos el JAR generado (el nombre puede variar, usamos comodín *)
COPY target/discovery-service-0.0.1-SNAPSHOT.jar /app/app.jar

# 3. EJECUCIÓN: Llamamos a la Ruta Absoluta
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
LABEL authors="jopecode"

