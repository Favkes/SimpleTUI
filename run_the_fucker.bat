@echo off

REM =======================
REM Build & Run SimpleTui
REM =======================

REM Check for --nopause flag
set "NOPAUSE=false"
for %%A in (%*) do (
    if "%%A"=="--nopause" set "NOPAUSE=true"
)

echo Building project with Gradle...
call gradlew.bat installDist

IF %ERRORLEVEL% NEQ 0 (
    echo Gradle build failed. Exiting. Dumbass.
    exit /b %ERRORLEVEL%
)

echo Running SimpleTui
REM Change this path if the app name ever changes
call build/install/SimpleTui/bin/SimpleTui.bat

REM Newline
echo:

if "%NOPAUSE%"=="false" (
    pause
)
