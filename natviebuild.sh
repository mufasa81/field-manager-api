#!/usr/bin/env bash
set -e

echo "▶ Switching to GraalVM 21.0.4"
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 21.0.4-graal

echo "▶ Java version:"
java -version

echo "▶ Starting native build"
./gradlew clean nativeCompile

echo "✅ Native build finished"

