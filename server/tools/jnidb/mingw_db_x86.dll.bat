@rm -rf *.o
@rm -rf *.dll
@rm -rf ../../core/common/octets.o

set WIN_JNIINCLUDES=-I"c:/Program Files/Java/jdk1.8.0_45/include/" -I"c:/Program Files/Java/jdk1.8.0_45/include/win32/"
set WIN_INCLUDES=-I"." %WIN_JNIINCLUDES% -I"../../" -I"../../core" -I"../../core/common/"
set WFLAGS="-D__int64=long long" -mthreads -O2 -DABSINC -D_FILE_OFFSET_BITS=64

C:\MinGW-4.8.1\bin\mingw32-g++ -Wall -O2 %WFLAGS% %WIN_INCLUDES% -c -o mkdb_Storage.o mkdb_Storage.cpp
C:\MinGW-4.8.1\bin\mingw32-g++ -Wall -O2 %WFLAGS% %WIN_INCLUDES% -c -o mkdb_Logger.o mkdb_Logger.cpp
C:\MinGW-4.8.1\bin\mingw32-g++ -Wall -O2 %WFLAGS% %WIN_INCLUDES% -c -o ../../core/common/octets.o ../../core/common/octets.cpp
C:\MinGW-4.8.1\bin\mingw32-g++ -Wl,--kill-at -mthreads -shared -static-libgcc -static-libstdc++ -o db_x86.dll ../../core/common/octets.o mkdb_Storage.o mkdb_Logger.o
