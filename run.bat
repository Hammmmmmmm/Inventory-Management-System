@echo off
set PATH_TO_FX=lib\javafx-sdk-24.0.1\lib
set PATH_TO_JBCrypt=lib\jbcrypt-0.4.jar
set PATH_TO_SQLITE=lib\sqlite-jdbc-3.43.2.0.jar

javac -cp "%PATH_TO_FX%\*;%PATH_TO_JBCrypt%;%PATH_TO_SQLITE%" --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml src\*.java

java -cp "src;%PATH_TO_FX%\*;%PATH_TO_JBCrypt%;%PATH_TO_SQLITE%" --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml Main
pause
