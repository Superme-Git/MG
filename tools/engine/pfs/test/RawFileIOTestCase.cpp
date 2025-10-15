#include "StdAfx.h"
#include "RawFileIOTestCase.h"
#include <assert.h>
#include <Windows.h>
#include "../pfs.h"
#include "../src/native/rawfileio.h"

CPPUNIT_TEST_SUITE_REGISTRATION( CFileIOTestCase);

const std::wstring FILE_EXIST = L"d:/pfstest/native/1.txt";
const std::wstring FILE_UNEXIST = L"d:/pfstest/native/2.txt";

const char g_buffer1[] = "1234567890";
const char g_buffer2[] = "1111111111111111";

CFileIOTestCase::CFileIOTestCase(void)
{
}

CFileIOTestCase::~CFileIOTestCase(void)
{
}

void CFileIOTestCase::testConstruct()
{
	PFS::CFileIO fio( FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT_THROW( PFS::CFileIO fio2( FILE_UNEXIST, PFS::FM_EXCL, PFS::FA_RDONLY), PFS::CFileIOException);
}

void CFileIOTestCase::testCheckCreateDirectory()
{
	const std::wstring temppath = L"d:/pfstest/a/b/c";
	bool b = PFS::CFileIO::CheckCreateDirectory( temppath);
	CPPUNIT_ASSERT( b);

	b = PFS::CFileIO::IsDirectoryExisting( L"d:/pfstest/a/b/c");
	CPPUNIT_ASSERT( b);
	b = PFS::CFileIO::IsDirectoryExisting( L"d:/pfstest/a/b");
	CPPUNIT_ASSERT( b);

	// remove方便下次测试
	::RemoveDirectory( L"d:/pfstest/a/b/c");
	::RemoveDirectory( L"d:/pfstest/a/b");
	::RemoveDirectory( L"d:/pfstest/a");
}

void CFileIOTestCase::testCheckCreateParentDirectory()
{
	const std::wstring temppath = L"d:/pfstest/a/b/c";
	bool b = PFS::CFileIO::CheckCreateParentDirectory( temppath);
	CPPUNIT_ASSERT( b);

	b = PFS::CFileIO::IsDirectoryExisting( L"d:/pfstest/a/b/c");
	CPPUNIT_ASSERT( !b);
	b = PFS::CFileIO::IsDirectoryExisting( L"d:/pfstest/a/b");
	CPPUNIT_ASSERT( b);

	::RemoveDirectory( L"d:/pfstest/a/b");
	::RemoveDirectory( L"d:/pfstest/a");
}

void CFileIOTestCase::testIsDirectoryExisting()
{
	// 在testCheckCreateDirectory 和 testCheckCreateParentDirectory 中测了
}

void CFileIOTestCase::testIsFileExisting()
{
	bool b = PFS::CFileIO::IsFileExisting( FILE_EXIST);
	CPPUNIT_ASSERT( b);
	b = PFS::CFileIO::IsFileExisting( FILE_UNEXIST);
	CPPUNIT_ASSERT( !b);
}

void CFileIOTestCase::testDeleteFile()
{
	{
		PFS::CFileIO fio( FILE_UNEXIST, PFS::FM_CREAT, PFS::FA_RDWR);
		// 文件被打开时不能delete
		bool b = ( 0 == PFS::CFileIO::DeleteFile( FILE_UNEXIST));
		CPPUNIT_ASSERT( !b);
	}
	bool b = ( 0 == PFS::CFileIO::DeleteFile( FILE_UNEXIST));
	CPPUNIT_ASSERT( b);
}

void CFileIOTestCase::testIO()
{
	// 测试 Read,Write,Seek,GetPos,GetSize,SetEnd 等io操作
	PFS::CFileIO fio( FILE_EXIST, PFS::FM_CREAT, PFS::FA_RDWR);

	char buffer[4] = {0};
	size_t res = fio.Read( buffer, 4);
	CPPUNIT_ASSERT( ( res == 4) && ( 0 == memcmp( g_buffer1, buffer, res)));
	fio.Seek( 4, PFS::FSM_CUR);
	CPPUNIT_ASSERT( 8 == fio.GetPos());

	res = fio.Read( buffer, 4);
	CPPUNIT_ASSERT( ( res == 2) && ( 0 == memcmp( g_buffer1+8, buffer, res)));
	CPPUNIT_ASSERT( fio.GetSize() == fio.GetPos());

	fio.Seek( 0, PFS::FSM_SET);
	CPPUNIT_ASSERT( 0 == fio.GetPos());
	res = fio.Write( g_buffer2, 4);
	CPPUNIT_ASSERT( ( 4 == res) && ( 4 == fio.GetPos()));

	fio.Seek( 0, PFS::FSM_SET);
	res = fio.Read( buffer, 4);
	CPPUNIT_ASSERT( ( res == 4) && ( 0 == memcmp( g_buffer2, buffer, res)));

	fio.Seek( -(long)(fio.GetSize()), PFS::FSM_END);
	CPPUNIT_ASSERT( 0 == fio.GetPos());
	res = fio.Write( g_buffer2, strlen( g_buffer2));
	fio.Seek( 0, PFS::FSM_SET);
	res = fio.Read( buffer, 4);
	CPPUNIT_ASSERT( (strlen( g_buffer2) == fio.GetSize()) && ( 0 == memcmp( g_buffer2, buffer, res)));

	fio.Seek( 0, PFS::FSM_SET);
	fio.Write( g_buffer1, strlen( g_buffer1));
	fio.SetEnd();
	CPPUNIT_ASSERT( strlen( g_buffer1) == fio.GetSize());
}

//CPPUNIT_TEST_SUITE_REGISTRATION( CFileImageHandleTestCase);

void CFileImageHandleTestCase::testConstruct()
{
	PFS::CFileImageHandle fih1;
	CPPUNIT_ASSERT( (0 == fih1.GetHandle()) && ( 0 == fih1.GetSize()));

	{
		PFS::CFileIO fio( FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
		PFS::CFileImageHandle fih2( fio);
		CPPUNIT_ASSERT( (0 != fih2.GetHandle()) && ( 0 != fih2.GetSize()));
	}

	PFS::CFileImageHandle fih3( FILE_EXIST.c_str(), 10);
	CPPUNIT_ASSERT( (0 != fih3.GetHandle()) && ( 0 != fih3.GetSize()));

	PFS::CFileImageHandle fih4( fih3);
	CPPUNIT_ASSERT( (0 != fih4.GetHandle()) && ( 0 != fih4.GetSize()));
}

void CFileImageHandleTestCase::testInitialize()
{
	PFS::CFileIO fio( FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	PFS::CFileImageHandle fih;
	fih.Initialize( fio);
	CPPUNIT_ASSERT( (0 != fih.GetHandle()) && ( 0 != fih.GetSize()));
}

void CFileImageHandleTestCase::testGetHandle()
{
	// 在 testContructor 和 testInitialize 中
}

void CFileImageHandleTestCase::testGetSize()
{
	// 在 testContructor 和 testInitialize 中
}

//CPPUNIT_TEST_SUITE_REGISTRATION( CFileImageTestCase);
void CFileImageTestCase::testGetData()
{
	PFS::CFileIO fio( FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	PFS::CFileImageHandle fih( fio);
	PFS::CFileImage fi( fih);
	CPPUNIT_ASSERT( 0 == memcmp( fi.GetData(), g_buffer1, strlen( g_buffer1)));
}

void CFileImageTestCase::testGetSize()
{
	PFS::CFileIO fio( FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	PFS::CFileImageHandle fih( fio);
	PFS::CFileImage fi( fih);
	CPPUNIT_ASSERT( fi.GetSize() == strlen( g_buffer1));
}

//CPPUNIT_TEST_SUITE_REGISTRATION( CPieceFileImageTestCase);
void CPieceFileImageTestCase::testGetData()
{
	PFS::CFileIO fio( FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	PFS::CFileImageHandle fih( fio);
	PFS::CPieceFileImage fi( fih, 4, 6);
	CPPUNIT_ASSERT( 0 == memcmp( fi.GetData(), g_buffer1+4, 6));
}

void CPieceFileImageTestCase::testGetSize()
{
	PFS::CFileIO fio( FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	PFS::CFileImageHandle fih( fio);
	PFS::CPieceFileImage fi( fih, 4, 6);
	CPPUNIT_ASSERT( fi.GetSize() == 6);
}