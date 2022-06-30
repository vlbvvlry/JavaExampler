@echo off
javac -sourcepath .\src -d .\out .\src\App\Main.java
java -classpath .\out App.Main
pause