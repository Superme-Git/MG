// VMPCounter - VMProtect Counter
//
// VMProtect begin
// VMProtect end
//
// 配对计算程序
//
// 刘东学 
//
// 2009-8-20 - V0.1
//
#include "stdafx.h"
#include "File.h"
#include <conio.h>

int action(int argc, _TCHAR* argv[])
{
	if ( argc < 2 )
	{
		printf("ERROR! Please input a binary file path as the argument!\n");
		return 1;
	}

	_tprintf(TEXT("Binary:\n%s\n"), argv[1] );
	printf("---------------------------------------------\n");

	CFile file;
	bool bRet = file.Open( argv[1], CFile::FM_EXCL, CFile::FA_RDONLY );
	if ( !bRet )
	{
		printf("Open file failed: %d\n", GetLastError() );
		return -1;
	}

	size_t len = file.GetSize();
	if ( file.GetSize() == 0 )
	{
		printf("Empty file?\n");
		return -2;
	}

	char* buffer = new char[len];
	size_t read = 0;
	size_t readAll = 0;
	do 
	{
		read = min( len-readAll, 40960 );
		read = file.Read( buffer+readAll, read );
		readAll += read;
	} while( readAll < len);

	char* pNextBegin = NULL;
	char* pNextEnd = NULL;

	int index = 1;
	char* ptr = buffer;
	do 
	{
		ptr = (char*)memchr( ptr, 'V', buffer+len-ptr );
		if( ptr != NULL )
		{
			pNextBegin = strstr( ptr, "VMProtect begin"); 
			if ( pNextBegin != NULL )
			{
				printf("[%02d] %8p: VMProtect begin\n", index ++, pNextBegin);
				ptr = pNextBegin + 1;
				do 
				{
					ptr = (char*)memchr( ptr, 'V', buffer+len-ptr );
					if( ptr != NULL )
					{
						pNextEnd = strstr( ptr, "VMProtect end");
						if( pNextEnd != NULL )
						{
							printf("     %8p: VMProtect end\n", pNextEnd );
							break;
						}
						ptr ++;
					}

				} while( ptr );
			}

			ptr ++;
		}		

	} while( ptr );

	delete[] buffer;

	return 0;
}

int _tmain(int argc, _TCHAR* argv[])
{
	setlocale( LC_ALL, "chs.936" );

	printf("------------- VMProtect Counter -------------\n" );

	int nRet = action( argc, argv );

	printf("---------------------------------------------\n");

	_getch();

	return nRet;
}
