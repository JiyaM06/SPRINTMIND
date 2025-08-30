@echo off
if not exist build mkdir build
dir /s /b *.java > sources.txt
javac -d build @sources.txt > compile_log.txt 2>&1 || (
  echo Compilation finished with errors. See compile_log.txt
  pause
  exit /b 1
)
echo Compilation successful. Running Main...
java -cp build Main
pause
