#!/usr/bin/env bash
storeFile=..\..\keystore\keystore.jks
storePassword=androiddemo
keyAlias=androiddemo
keyPassword=androiddemo
zipalign=%ANDROID_HOME%\build-tools\23.0.2\zipalign.exe
java -jar AndResGuard-cli-1.2.3.jar ..\build\outputs\apk\android-library-resguard-release.apk -config config.xml -out outputs -signature $storeFile $storePassword $keyAlias $keyPassword
