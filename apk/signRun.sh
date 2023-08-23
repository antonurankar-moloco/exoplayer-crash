#!/bin/bash
set -e
set -x

apkFile=${@}
if [ -z "$apkFile" ]
then
  echo "Param \$apkFile is empty"
  exit 1
fi

package='com.moloco.sdk.exoplayer'
logcat='adb logcat -v color'

clear

#create ZIP. `()` are used to run in separate process and doesn't change folder
#(cd app-debug; zip -r ../app-debug-hack.apk .)

#SIGN
echo Signing
java -jar $SDK/uber-apk-signer-1.3.0.jar -debug -verbose -a $apkFile --overwrite

#Install
adb uninstall com.moloco.sdk.exoplayer || true
echo Installing
adb install -r $apkFile

$logcat -c
adb shell monkey -p $package -c android.intent.category.LAUNCHER 1
clear
$logcat |egrep -i "TransportTycoon|==tomi==|ironsource|ironsrc"

