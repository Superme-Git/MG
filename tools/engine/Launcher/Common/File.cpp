#include "StdAfx.h"
#include "File.h"
#include <assert.h>
#include <algorithm>

CFile::CFile(void)
	: m_hFile(INVALID_HANDLE_VALUE)
{
}

CFile::CFile( LPCWSTR szFileName, FILE_MODE fm, FILE_ACCESS fa) 
{
	if( !Open( szFileName, fm, fa ) )
		ThrowFileIOException( ::GetLastError());
}

CFile::~CFile(void)
{
	Close();
}

bool CFile::Open( LPCWSTR szFileName, FILE_MODE fm, FILE_ACCESS fa, CFileException* pException) 
{

#ifdef _DEBUG		
	m_filename = szFileName;
#endif

	DWORD fileaccess, filecreate;
	switch ( fm) 
	{
	case FM_EXCL:  
		filecreate = OPEN_EXISTING;
		break;
	case FM_CREAT:
		filecreate = OPEN_ALWAYS;
		break;
	case FM_TRUNC:
	case FM_TRUNC | FM_EXCL:  
		filecreate = TRUNCATE_EXISTING;
		break;
	case FM_CREAT | FM_EXCL:
	case FM_CREAT | FM_TRUNC | FM_EXCL:
		filecreate = CREATE_NEW;
		break;
	case FM_CREAT | FM_TRUNC:
		filecreate = CREATE_ALWAYS;
		break;
	default:
		filecreate = OPEN_EXISTING;
		break;;
	}

	switch( fa) 
	{
	case FA_RDONLY:
		fileaccess = GENERIC_READ;
		break;
	case FA_WRONLY:  
		fileaccess = GENERIC_WRITE;
		break;
	case FA_RDWR:  
		fileaccess = GENERIC_READ | GENERIC_WRITE;
		break;
	default:   
		fileaccess = GENERIC_READ;
		break;
	}

	if( FM_CREAT == ( fm & FM_CREAT))
		CheckCreateParentDirectory( szFileName);
	m_hFile = ::CreateFileW( szFileName, 
		fileaccess, 
		FILE_SHARE_READ, // 总是允许共享读
		NULL, 
		filecreate, 
		0, 
		NULL);

	if( INVALID_HANDLE_VALUE == m_hFile)
	{
		if( pException != NULL )
		{
			pException->m_nLastError = GetLastError();
		}
		return false;
	}

	return true;
}

bool CFile::CheckCreateParentDirectory( LPCWSTR szFileName)
{
	std::wstring fn = szFileName; 
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
				//PFS::CEnv::GetOutLogInstance()->Print(L"ERROR: CheckCreateParentDirectory(path:%s)(%d)\n", substring.c_str(), ::GetLastError() );
				return false;
			}
		}
		posoffset = s + 1;
	}

	return true;
}

bool CFile::CheckCreateDirectory( LPCWSTR szDirectory )
{
	std::wstring dir = szDirectory; 
	std::replace(dir.begin(), dir.end(), L'/', L'\\');
	std::wstring::size_type	posoffset = 0;
	while( std::wstring::size_type s = dir.find( L'\\', posoffset))
	{
		std::wstring substring = dir.substr( 0, s);
		if( !IsDirectoryExisting( substring.c_str()))
		{
			if( !CreateDirectoryW( substring.c_str(), NULL) )
			{
				//PFS::CEnv::GetOutLogInstance()->Print(L"ERROR: CheckCreateDirectory(path:%s)(%d)\n", substring.c_str(), ::GetLastError() );
				return false;
			}
		}
		posoffset = s + 1;
		if( std::wstring::npos == s)
		{
			break;
		}
	}

	return true;
}

bool CFile::IsDirectoryExisting( LPCWSTR szDirectory)
{
	DWORD	dwAttribute = GetFileAttributesW( szDirectory );
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY == ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

bool CFile::IsFileExisting( LPCWSTR szFileName)
{
	DWORD	dwAttribute = GetFileAttributesW( szFileName);
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY != ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

int CFile::DeleteFile( LPCWSTR szFileName)
{
	if( ::DeleteFileW( szFileName))
		return 0;
	else
		return ::GetLastError();
}

size_t CFile::Read( void* data, size_t need)
{
	assert( INVALID_HANDLE_VALUE != m_hFile);

	DWORD	dwReaded;
	BOOL b = ReadFile( m_hFile, data, (DWORD)need, &dwReaded, NULL);
	if( !b)
		ThrowFileIOException( ::GetLastError());
	return dwReaded;
}

size_t CFile::Write( const void* data, size_t out)
{
	assert( INVALID_HANDLE_VALUE != m_hFile);

	DWORD	dwWrited;
	BOOL	b = WriteFile( m_hFile, data, (DWORD)out, &dwWrited, NULL);
	if( !b)
		ThrowFileIOException( ::GetLastError());
	return dwWrited;
}

size_t CFile::Seek( long pos, FILE_SEEK_METHOD fsm)
{
	assert( INVALID_HANDLE_VALUE != m_hFile);

	DWORD dwMethod;
	switch( fsm)
	{
	case FSM_SET:
		dwMethod = FILE_BEGIN;
		break;
	case FSM_CUR:
		dwMethod = FILE_CURRENT;
		break;
	case FSM_END:
		dwMethod = FILE_END;
		break;
	default:
		return 0;
	}

	return SetFilePointer( m_hFile, pos, NULL, dwMethod);	
}

size_t CFile::GetPos()
{
	DWORD dwResult = SetFilePointer( m_hFile, 0, NULL, FILE_CURRENT);
	return dwResult;
}

size_t CFile::GetSize()
{
	assert( INVALID_HANDLE_VALUE != m_hFile);

	DWORD dwSize = GetFileSize( m_hFile, NULL);
	return dwSize;
}

bool CFile::SetEnd()
{
	bool bRes = SetEndOfFile( m_hFile) ? true : false;
	return bRes;
}

bool CFile::Flush()
{
	assert( INVALID_HANDLE_VALUE != m_hFile);
	return !!::FlushFileBuffers( m_hFile );
}

void CFile::Close()
{
	if( INVALID_HANDLE_VALUE != m_hFile)
	{
		CloseHandle( m_hFile);
		m_hFile = INVALID_HANDLE_VALUE;
	}
}