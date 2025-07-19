@echo off
setlocal

set PATH_TO_JBCrypt=lib\other\jbcrypt-0.4.jar
set PATH_TO_SQLITE=lib\other\sqlite-jdbc-3.50.2.0.jar
set JAR_PATHS=%PATH_TO_JBCrypt%;%PATH_TO_SQLITE%

if exist out rmdir /s /q out
mkdir out

dir /b /s src\*.java > sources.txt

javac -cp "%JAR_PATHS%" -d out @sources.txt

java -cp "out;%JAR_PATHS%" Main

pause