@echo off
setlocal

set PATH_TO_JBCrypt=lib\other\jbcrypt-0.4.jar
set PATH_TO_SQLITE=lib\other\sqlite-jdbc-3.43.2.0.jar

javac -cp "%PATH_TO_JBCrypt%;%PATH_TO_SQLITE%" src\*.java

java -cp "src;%PATH_TO_JBCrypt%;%PATH_TO_SQLITE%" Main

pause