# Etapa de construcción
FROM gradle:8.7-jdk21 AS builder

WORKDIR /app

COPY . .

# Opcional: reduce la caché de dependencias si usas GitHub Actions o Render
RUN gradle build -x test --no-daemon

# Etapa de ejecución
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos solo el JAR generado
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
