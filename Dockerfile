FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app
COPY . .
RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR secondhand
COPY --from=build /app/build/libs/*.jar secondhand.jar
ENTRYPOINT ["java", "-jar", "secondhand.jar"]