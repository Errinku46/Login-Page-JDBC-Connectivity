#!/bin/bash
# ============================================================
#  build.sh  –  Compile and run the Login application
#  Requires: JDK 17+, MySQL JDBC driver in ./lib/
# ============================================================

set -e

DRIVER_JAR=$(ls lib/mysql-connector-j-*.jar 2>/dev/null | head -n1)

if [ -z "$DRIVER_JAR" ]; then
  echo "❌  MySQL JDBC driver not found in ./lib/"
  echo "    Download from: https://dev.mysql.com/downloads/connector/j/"
  echo "    Place the .jar file inside ./lib/ and re-run."
  exit 1
fi

echo "✅  Using driver: $DRIVER_JAR"
echo "⚙️   Compiling..."

mkdir -p out
find src -name "*.java" > sources.txt
javac -cp "$DRIVER_JAR" -d out @sources.txt
rm sources.txt

echo "🚀  Launching application..."
java -cp "out:$DRIVER_JAR" com.login.Main
