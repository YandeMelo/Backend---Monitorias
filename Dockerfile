FROM openjdk:23-jdk-slim AS build
WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y maven && apt-get clean
RUN mvn dependency:go-offline

COPY src/ ./src/
RUN mvn package -DskipTests

FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar monitorias.jar
CMD ["java", "-jar", "monitorias.jar"]