#!/bin/sh
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
LIPO="xcrun -sdk iphoneos lipo"
STRIP="xcrun -sdk iphoneos strip"

SRCDIR=$DIR/LuaJit-2.0.3
DESTDIR=$DIR/ios
IXCODE=`xcode-select -print-path`
ISDK=$IXCODE/Platforms/iPhoneOS.platform/Developer
ISDKVER=iPhoneOS7.1.sdk
ISDKP=$ISDK/usr/bin/

rm "$DESTDIR"/*.a
cd $SRCDIR

make clean
ISDKF="-arch armv7 -isysroot $ISDK/SDKs/$ISDKVER"
make HOST_CC="clang -m32 -arch i386" TARGET_FLAGS="$ISDKF" TARGET_SYS=iOS
mv "$SRCDIR"/src/libluajit.a "$DESTDIR"/libluajit-armv7.a

make clean
ISDKF="-arch armv7s -isysroot $ISDK/SDKs/$ISDKVER"
make HOST_CC="clang -m32 -arch i386" TARGET_FLAGS="$ISDKF" TARGET_SYS=iOS
mv "$SRCDIR"/src/libluajit.a "$DESTDIR"/libluajit-armv7s.a

make clean
make CC="clang -m32 -arch i386" clean all
mv "$SRCDIR"/src/libluajit.a "$DESTDIR"/libluajit-i386.a
mv "$SRCDIR"/src/luajit "$DIR"/bin/ios/luajit

$LIPO -create "$DESTDIR"/libluajit-*.a -output "$DESTDIR"/libluajit.a
$STRIP -S "$DESTDIR"/libluajit.a
$LIPO -info "$DESTDIR"/libluajit.a

rm "$DESTDIR"/libluajit-*.a

make clean
