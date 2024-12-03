#!/bin/sh

JAR_FILE=$(find /app/build -type f -name '*.jar' | head -n 1)

if [ -n "$JAR_FILE" ]; then
  echo "Running JAR file: $JAR_FILE with profile: prod"
  java -Dspring.profiles.active=prod -Dfile.encoding=UTF-8 -jar "$JAR_FILE"
else
  echo "No JAR file found in /app/build"
  exit 1
fi
