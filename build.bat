@echo off
chcp 65001 >nul
setlocal

echo ==============================
echo  mmck - build (jar + exe)
echo ==============================

set MVN=C:\Users\CosmicLatte\AppData\Local\Programs\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin\mvn.cmd
set JPACKAGE=C:\Program Files\Java\jdk-21.0.10\bin\jpackage.exe

echo [1/3] Compilando jar...
call "%MVN%" -q clean package
if errorlevel 1 (echo ERROR en maven & exit /b 1)

echo [2/3] Preparando staging...
if exist staging rmdir /s /q staging
mkdir staging
copy /y target\mmck.jar staging\mmck.jar >nul

echo [3/3] Generando mmck.exe...
if exist target\dist rmdir /s /q target\dist
"%JPACKAGE%" --type app-image --name mmck --app-version 1.0.0 --input staging --main-jar mmck.jar --main-class org.mmck.fxui.Launcher --vendor mmck --dest target\dist
if errorlevel 1 (echo ERROR en jpackage & exit /b 1)

echo.
echo Listo:
echo   Jar : target\mmck.jar
echo   Exe : target\dist\mmck\mmck.exe
echo.
endlocal