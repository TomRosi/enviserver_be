@echo off
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5555,suspend=y -jar target\envi-server-0.0.1-SNAPSHOT.jar 

