FROM gradle:8.8-jdk17-alpine AS build

WORKDIR /app

# Copiamos los archivos de gradle y configuración
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Descargamos dependencias (cacheable)
RUN ./gradlew dependencies --no-daemon || true

# Copiamos el código fuente
COPY src ./src

# Construimos el JAR ejecutable
RUN ./gradlew bootJar --no-daemon -x test

# Imagen ligera de runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

