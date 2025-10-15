#include <windows.h>
#include <algorithm>
#include <sstream>
#include <strsafe.h>

#pragma optimize("y", off)

static HANDLE g_hLogFile = NULL;

static bool IsDirectoryExisting( const  wchar_t* dir)
{
	DWORD	dwAttribute = GetFileAttributesW( dir );
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY == ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

static bool CheckCreateParentDirectory( const wchar_t* filename )
{
	std::wstring fn = filename; 
	std::replace(fn.begin(), fn.end(), L'/', L'\\');
	std::wstring::size_type	posoffset = 0;
	std::wstring::size_type s = 0;
	while( (s = fn.find( L'\\', posoffset)) != std::wstring::npos )
	{
		std::wstring substring = fn.substr( 0, s);
		if( !IsDirectoryExisting( substring.c_str()))
		{
			if( !CreateDirectoryW( substring.c_str(), NULL) )
			{
				std::wostringstream oss;
				oss << L"ERROR: CheckCreateParentDirectory(path:" << substring<< L")( " << ::GetLastError() << L"%d)\n";
				OutputDebugStringW( oss.str().c_str() );
				return false;
			}
		}

		posoffset = s + 1;
	}

	return true;
}

void Log(...)
{
	if( g_hLogFile == NULL )
	{
		return;
	}

	TCHAR szBuf[512]={0};
	TCHAR *szUserFormat;
	va_list Parameter=0;
	DWORD dwSize=0,dwRtn=0;
	SYSTEMTIME	stTime;
	
	//获取当前时间
	GetLocalTime(&stTime);

	//构造格式化字符串
	dwSize = swprintf_s(szBuf,TEXT("%d-%02d-%02d %02d:%02d:%02d "),
		stTime.wYear,stTime.wMonth,stTime.wDay,stTime.wHour,stTime.wMinute,stTime.wSecond);

	__asm
	{
		mov	eax,dword ptr [ebp+8]
		mov	szUserFormat,eax
		lea	eax,dword ptr [ebp+0xc]
		mov	Parameter,eax
	}
	
	// 格式化字符串, 可能会被截断。
	size_t cchRemaining = 512-dwSize;
	StringCchVPrintfEx( &szBuf[dwSize], 512-dwSize, NULL, &cchRemaining,
		STRSAFE_IGNORE_NULLS,szUserFormat,Parameter);

	dwSize += 512 - dwSize - cchRemaining;
	dwSize = dwSize * (sizeof TCHAR);

	SetFilePointer( g_hLogFile,0,0,FILE_END);

	WriteFile( g_hLogFile,szBuf,dwSize,&dwRtn,0);	
}

#pragma optimize("",  on)

bool InitLog(const TCHAR *lpFileName)
{
	if( !CheckCreateParentDirectory( lpFileName ) )
	{
		return false;
	}

	HANDLE hFile = CreateFile(lpFileName,GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,
		0,OPEN_ALWAYS,FILE_ATTRIBUTE_NORMAL,0);

	if( hFile == INVALID_HANDLE_VALUE || hFile == NULL)
	{
		return false;
	}

	if( GetFileSize( hFile, NULL ) == 0 )
	{
		static const WORD wUnicodeBOM = 0xFEFF;
		DWORD dwWritten;
		WriteFile(hFile, &wUnicodeBOM, 2, &dwWritten, NULL);
	}
	else
	{
		SetFilePointer(hFile, 0, NULL, FILE_END );
	}

	g_hLogFile = hFile;

	return true;
}

void CloseLog()
{
	if( g_hLogFile != NULL )
	{
		CloseHandle( g_hLogFile );
		g_hLogFile = NULL;
	}
}