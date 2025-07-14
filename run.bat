@echo off
set PATH_TO_FX=javafx-sdk-24.0.1\lib
javac --module-path %PATH_TO_FX% --add-modules javafx.controls Main.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls Main
pause