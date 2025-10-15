
SHARE_HOME = ../../core
TOP_SRCDIR = $(SHARE_HOME)

SINGLE_THREAD = false 
DEBUG_VERSION = false

include $(SHARE_HOME)/mk/gcc.defs.mk

#win
#C:/Program Files/Java/jdk1.8.0_45
WIN_JAVAHOME = C:\Program Files\Java\jdk1.8.0_45
#%JAVA_HOME%
WIN_JNIINCLUDES = -I"${WIN_JAVAHOME}/include/" -I"${WIN_JAVAHOME}/include/win32/"
WIN_INCLUDES = -I. ${WIN_JNIINCLUDES} -I../../ -I${SHARE_HOME} -I${SHARE_HOME}/common/
WFLAGS = "-D__int64=long long" -mthreads -O2 -DABSINC -D_FILE_OFFSET_BITS=64

#linux
JAVAHOME = /usr/local/jdk1.8.0_45
JNIINCLUDES = -I"${JAVAHOME}/include/" -I"${JAVAHOME}/include/linux/"
INCLUDES += ${JNIINCLUDES} -I$(SHARE_HOME) -I../../
CFLAGS += -O2 -DABSINC -fPIC -D_FILE_OFFSET_BITS=64
#-DMPPC_4WAY
DBTOOL_OBJS = dbtool.o
DBTEST_OBJS = testool.o
LIBDB_OBJS = mkdb_Storage.o mkdb_Logger.o 
MPPC_OBJS = mppc64.o


#security
LIBSECURITY_OBJS = mkio_security_Security.o 


all :
	@echo "please select target"
	@echo "libdb_i386.so|libdb_amd64.so|db_x86.dll|dbtool|testool|libsec_i386.so|libsec_amd64.so|sec_x86.dll"


dbtool : $(DBTOOL_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -o $@ $(DBTOOL_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)

testool : $(DBTEST_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -o $@ $(DBTEST_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)

libdb_i386.so : clean_linux $(LIBDB_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -shared -o $@ $(LIBDB_OBJS) $(SHAREOBJ) $(SHARE_SOBJ) 
	cp libdb_i386.so ../test/

libdb_amd64.so : clean_linux $(LIBDB_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -fPIC -shared -o $@ $(LIBDB_OBJS) $(SHAREOBJ) $(SHARE_SOBJ) 
	cp libdb_amd64.so ../test/

mppc64 : clean_linux $(MPPC_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -fPIC -o $@ $(MPPC_OBJS) $(SHAREOBJ) $(SHARE_SOBJ) 

mppc32 : clean_linux $(MPPC_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -o $@ $(MPPC_OBJS) $(SHAREOBJ) $(SHARE_SOBJ) 


libsec_i386.so : clean_linux $(LIBSECURITY_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -shared -o $@ $(LIBSECURITY_OBJS) $(SHAREOBJ) $(SHARE_SOBJ) 
	cp libdb_i386.so ../test/

libsec_amd64.so : clean_linux $(LIBSECURITY_OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -fPIC -shared -o $@ $(LIBSECURITY_OBJS) $(SHAREOBJ) $(SHARE_SOBJ) 
	cp libsec_amd64.so ../test/

linux:
	make libdb_amd64.so
	make libdb_i386.so

#-static-libgcc 
DLL_OBJS = ../../core/common/octets.o mkdb_Storage.o mkdb_Logger.o
db_x86.dll: clean_win mkdb_Storage.cpp mkdb_Logger.cpp mkdb_Logger.h mkdb_Storage.h jcast.h dbhelper.hpp
	g++ -Wall -O2 $(WFLAGS) ${WIN_INCLUDES} -c -o mkdb_Storage.o mkdb_Storage.cpp
	g++ -Wall -O2 $(WFLAGS) ${WIN_INCLUDES} -c -o mkdb_Logger.o mkdb_Logger.cpp
	g++ -Wall -O2 $(WFLAGS) ${WIN_INCLUDES} -c -o ../../core/common/octets.o ../../core/common/octets.cpp
	g++ -Wl,--kill-at -mthreads -shared -static-libgcc -o db_x86.dll $(DLL_OBJS)
	cp db_x86.dll ..\test\

DLL_SEC_OBJS = ../../core/common/octets.o ../../core/io/security.o mkio_security_Security.o ${SHARE_HOME}/win32/perf.lib
sec_x86.dll: clean_win jcast.h mkio_security_Security.cpp mkio_security_Security.h	
	g++ -Wall -O2 $(WFLAGS) ${WIN_INCLUDES} -c -o mkio_security_Security.o mkio_security_Security.cpp
	g++ -Wall -O2 $(WFLAGS) ${WIN_INCLUDES} -c -o ../../core/common/octets.o ../../core/common/octets.cpp
	g++ -Wall -O2 $(WFLAGS) ${WIN_INCLUDES} -c -o ../../core/io/security.o ../../core/io/security.cpp
	g++ -Wl,--kill-at -mthreads -shared -static-libgcc -o sec_x86.dll $(DLL_SEC_OBJS)
	copy sec_x86.dll ..\test\	

clean_linux :
	rm -rf $(DBTOOL_OBJS) $(LIBDB_OBJS) $(SHAREOBJ) $(MPPC_OBJS)
	rm -rf dbtool
	rm -rf *.so
	rm -rf mppc64
	
clean_win :
	rm -rf *.o
	rm -rf *.dll

include $(SHARE_HOME)/mk/gcc.rules.mk

