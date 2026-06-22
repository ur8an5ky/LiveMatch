# ============================================
# Stage 1: Build the application with Maven
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom first to leverage Docker layer caching for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn package -DskipTests -B

# ============================================
# Stage 2: Runtime with JRE only
# ============================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Non-root user for security
RUN groupadd --system spring && useradd --system --gid spring spring

# Copy the executable JAR from builder stage
COPY --from=builder /build/target/*.jar app.jar
RUN chown spring:spring app.jar

USER spring

EXPOSE 8081

# JVM tuning — respect container memory limits
ENTRYPOINT ["java", \
            "-XX:MaxRAMPercentage=75.0", \
            "-XX:+UseG1GC", \
            "-jar", \
            "/app/app.jar"]