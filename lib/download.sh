#!/bin/bash

#wget http://jogamp.org/deployment/jogamp-current/archive/jogamp-all-platforms.7z
#7z x jogamp-all-platforms.7z
#rm jogamp-all-platforms.7z

# Remove old jogamp files.
rm -rf jogamp-all-platforms

wget http://download.java.net/media/jogl/builds/archive/jsr-231-1.1.1a/jogl-1.1.1a-linux-amd64.zip
unzip jogl-1.1.1a-linux-amd64.zip
rm jogl-1.1.1a-linux-amd64.zip
