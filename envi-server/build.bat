@echo off
set JAVA_HOME=%JAVA_8_HOME%
rem set JAVA_HOME=c:\Program Files\Java\jdk1.8.0_152
mvn clean install -e -X
pause