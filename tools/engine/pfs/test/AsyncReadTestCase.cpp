#include "StdAfx.h"
#include "AsyncReadTestCase.h"
#include <Windows.h>

//CPPUNIT_TEST_SUITE_REGISTRATION( CAsyncReadTestCase);

const std::wstring RUN_NATIVE = L"/run_native";
const std::wstring PATH_NATIVE = L"d:/pfstest/native";
const std::wstring FILE_EXIST = L"/1.txt";
const std::wstring FILE_UNEXIST = L"/2.txt";

const char g_buffer1[] = "1234567890";

class CReadFileNotify : public PFSX::CAyncReadNotify
{
	int&				m_error;
	std::wstring&		m_file;
	PFS::CImageData&	m_data;

public:
	CReadFileNotify ( int& error, std::wstring& file, PFS::CImageData& data) 
		: m_error( error), m_file( file), m_data( data) {}

	virtual ~CReadFileNotify () {}
protected:
	virtual void OnReady( int nError, const std::wstring& filename, const PFS::CImageData& data) 
	{
		//wprintf( L"OnReady %s %d\n", filename.c_str(), nError);
		m_error = nError;
		m_file = filename;
		m_data = data;
	}
};


CAsyncReadTestCase::CAsyncReadTestCase(void)
{
}

CAsyncReadTestCase::~CAsyncReadTestCase(void)
{
}

void CAsyncReadTestCase::setUp()
{
	bool b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	PFSX::AsyncReadInitialize();

	CReadFileNotify existnotify( GetError_Exist(), GetFile_Exist(), GetData_Exist());
	PFSX::AsyncRead( RUN_NATIVE+FILE_EXIST, existnotify);

	CReadFileNotify unexistnotify( GetError_UnExist(), GetFile_UnExist(), GetData_UnExist());
	PFSX::AsyncRead( RUN_NATIVE+FILE_UNEXIST, unexistnotify);

	for( int i=0; i<3; i++)
	{
		// 循环3秒
		Sleep( 1000);
		PFSX::AsyncReadUpdate();
	}
}

void CAsyncReadTestCase::tearDown()
{
	PFSX::AsyncReadUninitialize();
	bool b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CAsyncReadTestCase::testReadExistFile()
{
	// 调用这个函数的时候数据应该已经读出
	CPPUNIT_ASSERT( (PFS::EC_SUCCESS == m_error_exist)
		&& (RUN_NATIVE+FILE_EXIST == m_file_exist)
		&& ( 0 == memcmp( g_buffer1, m_data_exist.GetData(), m_data_exist.GetSize())));
}

void CAsyncReadTestCase::testReadUnExistFile()
{
	CPPUNIT_ASSERT( (PFS::EC_FILE_NOT_FOUND == m_error_unexist)
		&& (RUN_NATIVE+FILE_UNEXIST == m_file_unexist)
		&& ( 0 == m_data_unexist.GetSize()));
}

void CAsyncReadTestCase::testSyncFile()
{
	m_syncfile.Open( RUN_NATIVE+FILE_EXIST);

	char buffer[16] = {0};
	size_t res = m_syncfile.Read( buffer, 16);
	CPPUNIT_ASSERT( (res == strlen( g_buffer1))
		&& ( 0 == memcmp( buffer, g_buffer1, strlen( g_buffer1))));
}
