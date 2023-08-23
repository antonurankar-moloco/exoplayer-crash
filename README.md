# Demonstrates ExoPlayer crash due to resources missing in APK

Demonstrates the crash observed when third party exoplayer dependency is not fully merged into APK.
https://mlc.atlassian.net/browse/SDK-847

The folder `app` contains the Android build. The folder `apk` contains the decompiled APK
with removed drawables `exo_icon_vr.png`. Then this is compiled back to `app-debug-hack.apk`
which then simulates the crash.