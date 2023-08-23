#!/bin/bash
set -e
set -x

folder=${@}
if [ -z "$folder" ]
then
  echo "Param \$folder is empty"
  exit 1
fi

apktool='java -jar /Users/tomi/work/sdk/apktool_2.7.0.jar'
apkName="${folder}.apk"
apkFile="$folder/dist/$apkName"
package='com.moloco.sdk.exoplayer'
logcat='adb logcat -v color'

clear
$apktool b $folder --verbose

#SIGN
echo Signing
java -jar $SDK/uber-apk-signer-1.3.0.jar -verbose -a $apkFile --overwrite

#Install
adb uninstall com.moloco.sdk.exoplayer
echo Installing
adb install -r $apkFile

$logcat -c
adb shell monkey -p $package -c android.intent.category.LAUNCHER 1
clear
$logcat |egrep -i "TransportTycoon|==tomi==|ironsource|ironsrc"

