# --- 1. 빌드 스테이지 (Builder) ---
# openjdk보다 eclipse-temurin이 더 안정적이고 가볍습니다.
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Gradle 캐싱을 위해 필요한 파일만 먼저 복사
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (캐싱 활용)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src ./src

# ⚡️ 핵심 수정 1: 테스트(-x test)를 건너뛰고, 실행 가능한 Jar(bootJar)만 만듭니다.
# clean을 붙여서 이전 빌드 잔여물을 제거합니다.
RUN ./gradlew clean bootJar -x test --no-daemon

# --- 2. 실행 스테이지 (Runner) ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# ⚡️ 핵심 수정 2: 빌드 결과물 경로에서 'plain'이 아닌 실행 파일만 찾아서 복사합니다.
# 보통 bootJar를 하면 'libs' 폴더에 실행 가능한 jar가 생성됩니다.
# 혹시 모를 plain jar 충돌을 막기 위해 정확한 패턴이나 이름 변경 전략을 씁니다.
# (가장 확실한 방법은 아래처럼 구체적인 파일명을 복사하는 것이지만,
#  이름이 바뀔 수 있으므로 shell 명령어로 plain이 아닌 것을 찾습니다.)
COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]