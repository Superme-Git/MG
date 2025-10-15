#include "StdAfx.h"
#include "FileexTestCase.h"
#include <assert.h>
#include "../pfs.h"
#include "../fileex.h"

CPPUNIT_TEST_SUITE_REGISTRATION( CFileTestCase);

CFileTestCase::CFileTestCase(void)
{
}

CFileTestCase::~CFileTestCase(void)
{
}

const std::wstring RUN_NATIVE = L"/run_native";
const std::wstring PATH_NATIVE = L"d:/pfstest/native";
const std::wstring RUN_ZIP = L"/run_zip";
const std::wstring PATH_ZIP = L"d:/pfstest/1.zip";
const std::wstring FILE_EXIST = L"/1.txt";
const std::wstring FILE_UNEXIST = L"/2.txt";
const std::wstring CHILD = L"/child";

const char g_buffer1[] = "1234567890";
const char g_buffer2[] = "1111111111111111";

void CFileTestCase::testNative()
{
	// 1.txt 内容为 "1234567890"
	bool b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	PFS::CFile file;
	if( file.Open( RUN_NATIVE + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDWR))
	{
		char buffer[4] = {0};
		size_t res = file.Read( buffer, 4);
		CPPUNIT_ASSERT( ( res == 4) && ( 0 == memcmp( g_buffer1, buffer, res)));
		file.Seek( 4, PFS::FSM_CUR);
		CPPUNIT_ASSERT( 8 == file.GetPos());

		res = file.Read( buffer, 4);
		CPPUNIT_ASSERT( ( res == 2) && ( 0 == memcmp( g_buffer1+8, buffer, res)));
		CPPUNIT_ASSERT( file.GetSize() == file.GetPos());

		file.Seek( 0, PFS::FSM_SET);
		CPPUNIT_ASSERT( 0 == file.GetPos());
		res = file.Write( g_buffer2, 4);
		CPPUNIT_ASSERT( ( 4 == res) && ( 4 == file.GetPos()));

		file.Seek( 0, PFS::FSM_SET);
		res = file.Read( buffer, 4);
		CPPUNIT_ASSERT( ( res == 4) && ( 0 == memcmp( g_buffer2, buffer, res)));

		file.Seek( -(long)(file.GetSize()), PFS::FSM_END);
		CPPUNIT_ASSERT( 0 == file.GetPos());
		res = file.Write( g_buffer2, strlen( g_buffer2));
		file.Seek( 0, PFS::FSM_SET);
		res = file.Read( buffer, 4);
		CPPUNIT_ASSERT( (strlen( g_buffer2) == file.GetSize()) && ( 0 == memcmp( g_buffer2, buffer, res)));

		file.Seek( 0, PFS::FSM_SET);
		file.Write( g_buffer1, strlen( g_buffer1));
		file.SetEnd();
		CPPUNIT_ASSERT( strlen( g_buffer1) == file.GetSize());

		file.Close();
	}
	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CFileTestCase::testZip()
{
	bool b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_WRITABLE);
	PFS::CFile file;
	if( file.Open( RUN_ZIP + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDWR))
	{
		char buffer[4] = {0};
		size_t res = file.Read( buffer, 4);
		CPPUNIT_ASSERT( ( res == 4) && ( 0 == memcmp( g_buffer1, buffer, res)));
		file.Seek( 4, PFS::FSM_CUR);
		CPPUNIT_ASSERT( 8 == file.GetPos());

		res = file.Read( buffer, 4);
		CPPUNIT_ASSERT( ( res == 2) && ( 0 == memcmp( g_buffer1+8, buffer, res)));
		CPPUNIT_ASSERT( file.GetSize() == file.GetPos());

		file.Seek( 0, PFS::FSM_SET);
		CPPUNIT_ASSERT( 0 == file.GetPos());
		res = file.Write( g_buffer2, 4);
		CPPUNIT_ASSERT( ( 4 == res) && ( 4 == file.GetPos()));

		file.Close();
		if( file.Open( RUN_ZIP + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDWR))
		{
			file.Seek( 0, PFS::FSM_SET);
			res = file.Read( buffer, 4);
			CPPUNIT_ASSERT( ( res == 4) && ( 0 == memcmp( g_buffer2, buffer, res)));

			file.Seek( -(long)(file.GetSize()), PFS::FSM_END);
			CPPUNIT_ASSERT( 0 == file.GetPos());
			res = file.Write( g_buffer2, strlen( g_buffer2));
			file.Seek( 0, PFS::FSM_SET);
			res = file.Read( buffer, 4);
			CPPUNIT_ASSERT( (strlen( g_buffer2) == file.GetSize()) && ( 0 == memcmp( g_buffer2, buffer, res)));

			file.Seek( 0, PFS::FSM_SET);
			file.Write( g_buffer1, strlen( g_buffer1));
			file.SetEnd();
			CPPUNIT_ASSERT( strlen( g_buffer1) == file.GetSize());

			file.Close();
		}
	}
	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CFileTestCase::testBrief()
{
	// 暂时未测
}

CPPUNIT_TEST_SUITE_REGISTRATION( CImageFileTestCase);

void CImageFileTestCase::testImageFile()
{
	// CImageFile 不能 Write
	PFS::CDataBlock db( g_buffer1, strlen( g_buffer1));
	PFS::CImageFile file( db);

	char buffer[4] = {0};
	size_t res = file.Read( buffer, 4);
	CPPUNIT_ASSERT( ( res == 4) && ( 0 == memcmp( g_buffer1, buffer, res)));
	file.Seek( 4, PFS::FSM_CUR);
	CPPUNIT_ASSERT( 8 == file.GetPos());

	res = file.Read( buffer, 4);
	CPPUNIT_ASSERT( ( res == 2) && ( 0 == memcmp( g_buffer1+8, buffer, res)));
	CPPUNIT_ASSERT( file.GetSize() == file.GetPos());
}

CPPUNIT_TEST_SUITE_REGISTRATION( CDataFileTestCase);

void CDataFileTestCase::testDataFile()
{
	// CDataFile 不能 Read
	PFS::CDataFile file;
	size_t res = file.Write( g_buffer2, strlen( g_buffer2));
	CPPUNIT_ASSERT( ( strlen( g_buffer2) == res) && ( strlen( g_buffer2) == file.GetPos()));

	PFS::CImageData data = file.GetImage();
	CPPUNIT_ASSERT( 0 == memcmp( g_buffer2, data.GetData(), 4));

	file.Seek( -(long)(file.GetSize()), PFS::FSM_END);
	CPPUNIT_ASSERT( 0 == file.GetPos());
	file.Write( g_buffer1, strlen( g_buffer1));

	file.SetEnd();
	CPPUNIT_ASSERT( strlen( g_buffer1) == file.GetSize());

}