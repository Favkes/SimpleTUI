#!/bin/bash

# =======================
# Build & Run SimpleTui
# =======================

# THE NOPAUSE FLAG IS POINTLESS ON LINUX
## Check for --nopause flag
#NOPAUSE=false
#for arg in "$@"; do
#  case $arg in
#    --nopause) NOPAUSE=true ;;
#  esac
#done

echo Building project with Gradle...
./gradlew installDist

if [ $? -ne 0 ]; then
  echo Gradle build failed. Exiting. Dumbass.
fi

echo Running SimpleTui
# Change this path if the app name ever changes
./build/install/SimpleTUI/bin/SimpleTUI



