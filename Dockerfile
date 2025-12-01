# --- 1. 빌드 스테이지 (Builder) ---
# Java 21 JDK를 포함한 slim 이미지에서 빌드를 시작합니다.
FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

# (이하 빌드 스테이지 내용은 동일)
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x ./gradlew
RUN ./gradlew dependencies
COPY src ./src
RUN ./gradlew build --no-daemon


# --- 2. 실행 스테이지 (Runner) ---
# ⚠️ 이 부분을 수정합니다. JRE 대신 JDK의 slim 버전을 사용합니다.
FROM openjdk:21-jdk-slim

WORKDIR /app

# 'builder' 스테이지에서 빌드된 JAR 파일만 복사해옵니다.
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]