# DO NOT EDIT
# This makefile makes sure all linkable targets are
# up-to-date with anything they link to
default:
	echo "Do not invoke directly"

# For each target create a dummy rule so the target does not have to exist
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Debug/libpcreposix.a:
/usr/lib/libz.dylib:
/usr/lib/libbz2.dylib:
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Debug/libpcre.a:
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/libpcreposix.a:
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/libpcre.a:
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/libpcreposix.a:
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/libpcre.a:
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/libpcreposix.a:
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/libpcre.a:
/usr/lib/libreadline.dylib:


# Rules to remove targets that are older than anything to which they
# link.  This forces Xcode to relink the targets from scratch.  It
# does not seem to check these dependencies itself.
PostBuild.pcre.Debug:
PostBuild.pcregrep.Debug:
PostBuild.pcreposix.Debug: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcregrep
PostBuild.pcre.Debug: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcregrep
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcregrep:\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Debug/libpcreposix.a\
	/usr/lib/libz.dylib\
	/usr/lib/libbz2.dylib\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Debug/libpcre.a
	/bin/rm -f /Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcregrep


PostBuild.pcreposix.Debug:
PostBuild.pcretest.Debug:
PostBuild.pcreposix.Debug: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcretest
PostBuild.pcre.Debug: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcretest
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcretest:\
	/usr/lib/libreadline.dylib\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Debug/libpcreposix.a\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Debug/libpcre.a
	/bin/rm -f /Users/magicstudio/Public/3rd/pcre-8.31/prj2/DEBUG/pcretest


PostBuild.pcre.Release:
PostBuild.pcregrep.Release:
PostBuild.pcreposix.Release: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcregrep
PostBuild.pcre.Release: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcregrep
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcregrep:\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/libpcreposix.a\
	/usr/lib/libz.dylib\
	/usr/lib/libbz2.dylib\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/libpcre.a
	/bin/rm -f /Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcregrep


PostBuild.pcreposix.Release:
PostBuild.pcretest.Release:
PostBuild.pcreposix.Release: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcretest
PostBuild.pcre.Release: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcretest
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcretest:\
	/usr/lib/libreadline.dylib\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/libpcreposix.a\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/libpcre.a
	/bin/rm -f /Users/magicstudio/Public/3rd/pcre-8.31/prj2/Release/pcretest


PostBuild.pcre.MinSizeRel:
PostBuild.pcregrep.MinSizeRel:
PostBuild.pcreposix.MinSizeRel: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcregrep
PostBuild.pcre.MinSizeRel: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcregrep
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcregrep:\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/libpcreposix.a\
	/usr/lib/libz.dylib\
	/usr/lib/libbz2.dylib\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/libpcre.a
	/bin/rm -f /Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcregrep


PostBuild.pcreposix.MinSizeRel:
PostBuild.pcretest.MinSizeRel:
PostBuild.pcreposix.MinSizeRel: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcretest
PostBuild.pcre.MinSizeRel: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcretest
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcretest:\
	/usr/lib/libreadline.dylib\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/libpcreposix.a\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/libpcre.a
	/bin/rm -f /Users/magicstudio/Public/3rd/pcre-8.31/prj2/MinSizeRel/pcretest


PostBuild.pcre.RelWithDebInfo:
PostBuild.pcregrep.RelWithDebInfo:
PostBuild.pcreposix.RelWithDebInfo: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcregrep
PostBuild.pcre.RelWithDebInfo: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcregrep
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcregrep:\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/libpcreposix.a\
	/usr/lib/libz.dylib\
	/usr/lib/libbz2.dylib\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/libpcre.a
	/bin/rm -f /Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcregrep


PostBuild.pcreposix.RelWithDebInfo:
PostBuild.pcretest.RelWithDebInfo:
PostBuild.pcreposix.RelWithDebInfo: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcretest
PostBuild.pcre.RelWithDebInfo: /Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcretest
/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcretest:\
	/usr/lib/libreadline.dylib\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/libpcreposix.a\
	/Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/libpcre.a
	/bin/rm -f /Users/magicstudio/Public/3rd/pcre-8.31/prj2/RelWithDebInfo/pcretest


