@echo off
set AGENT_JAR=..\agent\target\agent-swing-recorder.jar
set APP_JAR=..\example-app\target\example-app.jar

echo Starting Example App with Agent attached...
java -javaagent:%AGENT_JAR% -jar %APP_JAR%
