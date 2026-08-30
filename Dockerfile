FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -B -DskipTests dependency:go-offline

COPY src/ src/
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder --chown=10001:10001 /workspace/target/z-customer-data-service-*.jar app.jar

USER 10001:10001
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
