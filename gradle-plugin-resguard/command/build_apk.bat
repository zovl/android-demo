@rem set /p apkFile=Please enter the address of the apk file(for example: ..\build\outputs\apk\android-library-resguard-release.apk)
@rem set apkFile=..\build\outputs\apk\android-library-resguard-release.apk

set storeFile=..\..\keystore\keystore.jks
set storePassword=androiddemo
set keyAlias=androiddemo
set keyPassword=androiddemo
set zipalign=%ANDROID_HOME%\build-tools\23.0.2\zipalign.exe
java -jar AndResGuard-cli-1.2.3.jar ..\build\outputs\apk\android-library-resguard-release.apk -config config.xml -out outputs -signature "%storeFile%" "%storePassword%" "%keyAlias%" "%keyPassword%" -zipalign "%zipalign%"
pause