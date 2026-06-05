# Step 1: Use an official Maven image with OpenJDK to build the project
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Use a lightweight Java runtime image to run the built application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/libraryms-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]