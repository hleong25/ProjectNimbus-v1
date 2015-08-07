#!/bin/bash

cd "$(dirname "$0")"

MYJAVA="/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin"
export PATH=$MYJAVA:$PATH

java -version

OSXVER=`sw_vers -productVersion`

java -jar -Dmrj.version="$OSXVER" NimbusApp.jar
