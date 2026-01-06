FROM --platform=$BUILDPLATFORM eclipse-temurin:21-jdk-alpine

# Creamos un directorio de trabajo
WORKDIR /app

# Copiamos el JAR generado (el nombre puede variar, usamos comodín *)
COPY target/*.jar app.jar

# Comando de inicio
ENTRYPOINT ["java", "-jar", "order-service-0.0.1-SNAPSHOT.jar"]
LABEL authors="jopecode"

