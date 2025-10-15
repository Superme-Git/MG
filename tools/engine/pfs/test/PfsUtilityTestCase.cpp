#include "StdAfx.h"
#include "PfsUtilityTestCase.h"
#include "../pfsutility.h"

CPPUNIT_TEST_SUITE_REGISTRATION( CDataBlockTestCase);

CDataBlockTestCase::CDataBlockTestCase(void)
{
}

CDataBlockTestCase::~CDataBlockTestCase(void)
{
}

const char g_buffer[] = "1234567890";
const char g_buffer2[] = "1111111111111111";

void CDataBlockTestCase::testConstruct()
{
	PFS::CDataBlock datablock1;
	CPPUNIT_ASSERT( 0 == datablock1.GetSize());

	PFS::CDataBlock datablock2( 10);
	CPPUNIT_ASSERT( 0 == datablock1.GetSize());

	PFS::CDataBlock datablock3( g_buffer, strlen( g_buffer));
	CPPUNIT_ASSERT( ( strlen( g_buffer) == datablock3.GetSize()) 
		&& ( 0 == memcmp( datablock3.GetData(), g_buffer, strlen( g_buffer))));

	PFS::CDataBlock datablock4( datablock3);
	CPPUNIT_ASSERT( ( strlen( g_buffer) == datablock4.GetSize()) 
		&& ( 0 == memcmp( datablock4.GetData(), g_buffer, strlen( g_buffer))));
}

void CDataBlockTestCase::testGetData()
{
	// 在其他函数中
}

void CDataBlockTestCase::testGetSize()
{
	// 在其他函数中
}

void CDataBlockTestCase::testResize()
{
	PFS::CDataBlock datablock;
	datablock.Resize( strlen( g_buffer));
	CPPUNIT_ASSERT( strlen( g_buffer) == datablock.GetSize());

	memcpy( datablock.GetData(), g_buffer, strlen( g_buffer));
	CPPUNIT_ASSERT( 0 == memcmp( datablock.GetData(), g_buffer, strlen( g_buffer)));
}

void CDataBlockTestCase::testReserve()
{
	// 调用Reserve接口后内存已分配但size未变
	PFS::CDataBlock datablock;
	datablock.Reserve( strlen( g_buffer));
	CPPUNIT_ASSERT( 0 == datablock.GetSize());

	memcpy( datablock.GetData(), g_buffer, strlen( g_buffer));
	CPPUNIT_ASSERT( 0 == memcmp( datablock.GetData(), g_buffer, strlen( g_buffer)));
}

void CDataBlockTestCase::testClear()
{
	PFS::CDataBlock datablock( g_buffer, strlen( g_buffer));
	datablock.Clear();
	CPPUNIT_ASSERT( 0 == datablock.GetSize());
}

void CDataBlockTestCase::testAssign()
{
	PFS::CDataBlock datablock;
	datablock.Assign( g_buffer, strlen( g_buffer));
	CPPUNIT_ASSERT( ( strlen( g_buffer) == datablock.GetSize()) 
		&& ( 0 == memcmp( datablock.GetData(), g_buffer, strlen( g_buffer))));
}

void CDataBlockTestCase::testAppend()
{
	PFS::CDataBlock datablock( g_buffer, strlen( g_buffer));
	datablock.Append( g_buffer, strlen( g_buffer));
	CPPUNIT_ASSERT( ( 2*strlen( g_buffer) == datablock.GetSize()) 
		&& ( 0 == memcmp( (char*)datablock.GetData()+strlen( g_buffer), g_buffer, strlen( g_buffer))));
}

void CDataBlockTestCase::testSwap()
{
	PFS::CDataBlock datablock1( g_buffer, strlen( g_buffer));
	PFS::CDataBlock datablock2( g_buffer2, strlen( g_buffer2));
	datablock1.Swap( datablock2);

	CPPUNIT_ASSERT( ( strlen( g_buffer2) == datablock1.GetSize()) 
		&& ( 0 == memcmp( datablock1.GetData(), g_buffer2, strlen( g_buffer2))));
	CPPUNIT_ASSERT( ( strlen( g_buffer) == datablock2.GetSize()) 
		&& ( 0 == memcmp( datablock2.GetData(), g_buffer, strlen( g_buffer))));
}