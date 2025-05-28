# Etapa 1: build com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:20-jdk-alpine

RUN apk add --no-cache ghostscript tesseract-ocr

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Copia a pasta tessdata da raiz do projeto para dentro da imagem final
COPY tessdata ./tessdata

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
