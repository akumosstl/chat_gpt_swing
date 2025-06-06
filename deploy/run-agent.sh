#!/bin/bash
AGENT_JAR="../agent/target/agent-swing-recorder.jar"
APP_JAR="../example-app/target/example-app.jar"

echo "Starting Example App with Agent attached..."
java -javaagent:$AGENT_JAR -jar $APP_JAR
