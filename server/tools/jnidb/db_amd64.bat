
@rm -rf *.o
@rm -rf *.dll
@rm -rf ../../core/common/octets.o

set GCC_INCLUDES=-I"C:/Dev-Cpp/MinGW64/include" -I"C:/Dev-Cpp/MinGW64/x86_64-w64-mingw32/include"  -I"C:/Dev-Cpp/MinGW64/lib/gcc/x86_64-w64-mingw32/4.8.1/include" -I"C:/Dev-Cpp/MinGW64/lib/gcc/x86_64-w64-mingw32/4.8.1/include/c++"
set WIN_JNIINCLUDES=-I"c:/Program Files/Java/jdk1.8.0_45/include" -I"c:/Program Files/Java/jdk1.8.0_45/include/win32"
set WIN_INCLUDES= %GCC_INCLUDES% %WIN_JNIINCLUDES% -I"." -I"../../" -I"../../core" -I"../../core/common/"

set WFLAGS= -m64 -mthreads -D"__int64=long long" -D"BUILDING_DLL=1" -D"ABSINC -D_FILE_OFFSET_BITS=64"

C:\Dev-Cpp\MinGW64\bin\g++ -c mkdb_Logger.cpp -o mkdb_Logger.o %WIN_INCLUDES% %WFLAGS% 
C:\Dev-Cpp\MinGW64\bin\g++ -c mkdb_Storage.cpp -o mkdb_Storage.o %WIN_INCLUDES% %WFLAGS% 
C:\Dev-Cpp\MinGW64\bin\g++ -c ../../core/common/octets.cpp -o ../../core/common/octets.o %WIN_INCLUDES% %WFLAGS% 

C:\Dev-Cpp\MinGW64\bin\g++ -mthreads -mdll -static -o db_amd64.dll mkdb_Storage.o mkdb_Logger.o ../../core/common/octets.o
