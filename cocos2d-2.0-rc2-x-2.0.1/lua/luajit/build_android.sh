#!/bin/sh
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

SRCDIR=$DIR/LuaJit-2.0.3

NDK=$NDK_ROOT

# Android/ARM, armeabi (ARMv5TE soft-float), Android 2.2+ (Froyo)
cd "$SRCDIR"
make clean
NDKABI=8
NDKVER=$NDK/toolchains/arm-linux-androideabi-4.6
NDKP=$NDKVER/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-
NDKF="--sysroot $NDK/platforms/android-$NDKABI/arch-arm"
make HOST_CC="gcc -m32" CROSS=$NDKP TARGET_FLAGS="$NDKF" TARGET_SYS=Linux

if [ -f $SRCDIR/src/libluajit.a ]; then
	mv $SRCDIR/src/libluajit.a $DIR/android/armeabi/libluajit.a
fi

# Android/ARM, armeabi-v7a (ARMv7 VFP), Android 4.0+ (ICS)
cd "$SRCDIR"
make clean
NDKABI=14
NDKVER=$NDK/toolchains/arm-linux-androideabi-4.6
NDKP=$NDKVER/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-
NDKF="--sysroot $NDK/platforms/android-$NDKABI/arch-arm"
NDKARCH="-march=armv7-a -mfloat-abi=softfp -Wl,--fix-cortex-a8"
make HOST_CC="gcc -m32" CROSS=$NDKP TARGET_FLAGS="$NDKF $NDKARCH" TARGET_SYS=Linux

if [ -f $SRCDIR/src/libluajit.a ]; then
	mv $SRCDIR/src/libluajit.a $DIR/android/armeabi-v7a/libluajit.a
	mv $SRCDIR/src/luajit $DIR/bin/android/luajit
fi

# Android/x86, x86 (i686 SSE3), Android 4.0+ (ICS)
cd "$SRCDIR"
make clean
NDKABI=14
NDKVER=$NDK/toolchains/x86-4.6
NDKP=$NDKVER/prebuilt/darwin-x86_64/bin/i686-linux-android-
NDKF="--sysroot $NDK/platforms/android-$NDKABI/arch-x86"
make HOST_CC="gcc -m32" CROSS=$NDKP TARGET_FLAGS="$NDKF" TARGET_SYS=Linux

if [ -f $SRCDIR/src/libluajit.a ]; then
	mv $SRCDIR/src/libluajit.a $DIR/android/x86/libluajit.a
fi

cd "$SRCDIR"
make clean
