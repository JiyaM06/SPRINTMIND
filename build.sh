#!/bin/bash
set -euo pipefail
echo "Building SPRINTMIND..."
BUILD_DIR=build
mkdir -p "$BUILD_DIR"
find . -path ./build -prune -o -path ./.git -prune -o -name "*.java" -print > sources.txt
if [ ! -s sources.txt ]; then
  echo "No Java sources found." > compile_log.txt
  exit 1
fi
javac -d "$BUILD_DIR" @sources.txt > compile_log.txt 2>&1 || true
if [ -s compile_log.txt ]; then
  echo "Compilation finished with errors. See compile_log.txt"
  exit 1
fi
echo "Compilation successful. Running Main..."
java -cp "$BUILD_DIR" Main
