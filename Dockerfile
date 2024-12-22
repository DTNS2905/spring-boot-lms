# Use a lightweight JDK base image for build
FROM eclipse-temurin:17-jdk-focal AS build

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Default environment variables (override at runtime if needed)
ENV DATABASE_URL=jdbc:postgresql://localhost:5432/postgres
ENV DB_USERNAME=sangdoan
ENV DB_PASSWORD=sangdoan

CMD ["java", "-jar", "app.jar"]
