FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 创建必要的目录结构
RUN mkdir -p /app/database/public/images/avatars
RUN mkdir -p /app/database/public/images/cars
RUN chmod -R 777 /app/database

ENTRYPOINT ["java", "-jar", "app.jar"] 