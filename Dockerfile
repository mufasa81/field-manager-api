# 로컬에서 Ubuntu로 빌드했으므로, 실행 환경도 Ubuntu 계열을 사용합니다.
# (가벼운 debian-slim을 추천하지만, 안전하게 ubuntu를 씁니다)
FROM ubuntu:22.04

WORKDIR /app

# 1. 실행 파일 복사
# (로컬에 이미 만들어진 파일을 컨테이너 안으로 집어넣습니다)
COPY build/native/nativeCompile/fieldmanager app

# 2. 실행 권한 부여 (혹시 모르니 확실하게)
RUN chmod +x app

# 3. 포트 노출 및 실행
EXPOSE 8080
CMD ["./app"]