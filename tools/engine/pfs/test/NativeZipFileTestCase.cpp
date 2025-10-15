#include "StdAfx.h"
#include "NativeZipFileTestCase.h"
#include <assert.h>
#include "../pfs.h"
#include "../nativezipfile.h"

CPPUNIT_TEST_SUITE_REGISTRATION( CZipReadFileTestCase);

const char g_buffer1[] = "1234567890";

CZipReadFileTestCase::CZipReadFileTestCase(void)
{
}

CZipReadFileTestCase::~CZipReadFileTestCase(void)
{
}

void CZipReadFileTestCase::testConstruct()
{
	PFS::CDataFile df;
	PFS::CImageFile cif( df.GetImage());
	PFSX::CZipReadFile zrf( cif, 10);
	CPPUNIT_ASSERT( 10 == zrf.GetSize());
}

void CZipReadFileTestCase::testIO()
{
	// CZipReadFile 不能 Write, 只能Seek到文件开始位置
	PFS::CDataFile df;
	PFSX::CZipWriteFile zwf( df, -1);
	zwf.Write( g_buffer1, strlen( g_buffer1));
	zwf.SetEnd();

	PFS::CImageFile cif( df.GetImage());
	PFSX::CZipReadFile zrf( cif, cif.GetSize());

	char buffer[16] = {0};
	size_t res = zrf.Read( buffer, 4);
	CPPUNIT_ASSERT( ( res == 4) && ( 0 == memcmp( g_buffer1, buffer, res)));

	res = zrf.Read( buffer, 16);
	CPPUNIT_ASSERT( ( res == 6) && ( 0 == memcmp( g_buffer1+4, buffer, res)));
	//CPPUNIT_ASSERT( zrf.GetSize() == zrf.GetPos());

	res = zrf.Seek( 0, PFS::FSM_SET);
	CPPUNIT_ASSERT( res == 0);

	res = zrf.Read( buffer, 16);
	CPPUNIT_ASSERT( ( res == strlen( g_buffer1)) && ( 0 == memcmp( g_buffer1, buffer, res)));
}

CPPUNIT_TEST_SUITE_REGISTRATION( CZipWriteFileTestCase);

void CZipWriteFileTestCase::testConstruct()
{
	PFS::CDataFile df;
	PFSX::CZipWriteFile zwf( df, -1);
	CPPUNIT_ASSERT( 0 == zwf.GetSize());
}

void CZipWriteFileTestCase::testIO()
{
	// CZipReadFile 不能 Read, Seek
	PFS::CDataFile df;
	PFSX::CZipWriteFile zwf( df, -1);

	size_t res = zwf.Write( g_buffer1, strlen( g_buffer1));
	CPPUNIT_ASSERT( res == strlen( g_buffer1));
	res = zwf.Write( g_buffer1, strlen( g_buffer1));
	zwf.SetEnd();
	CPPUNIT_ASSERT( zwf.GetSize() == zwf.GetPos());

	// 因为df里写的数据是zip压缩过的，只能通过 CZipReadFile 解压后检查数据
	PFS::CImageFile cif( df.GetImage());
	PFSX::CZipReadFile zrf( cif, cif.GetSize());
	char buffer[128] = {0};
	res = zrf.Read( buffer, 128);

	CPPUNIT_ASSERT( 0 == memcmp( buffer, g_buffer1, strlen( g_buffer1))
		&& ( 0 == memcmp( buffer+strlen( g_buffer1), g_buffer1, strlen( g_buffer1))));
}