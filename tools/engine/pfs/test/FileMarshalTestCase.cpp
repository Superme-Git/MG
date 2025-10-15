#include "StdAfx.h"
#include "FileMarshalTestCase.h"
#include "../pfs2.h"
#include "../filemarshal.h"

//CPPUNIT_TEST_SUITE_REGISTRATION( CFileMarshalTestCase);

CFileMarshalTestCase::CFileMarshalTestCase(void)
{
}

CFileMarshalTestCase::~CFileMarshalTestCase(void)
{
}

void CFileMarshalTestCase::testPushByte()
{
	PFS::CDataFile df;
	PFS::CFileStream fs1( df);
	int src1 = 1234567890;
	int src2 = 987654321;
	fs1.push_byte( &src1, sizeof( src1));

	CPPUNIT_ASSERT( (sizeof( src1) == df.GetSize())
		&& ( 0 == memcmp( df.GetImage().GetData(), &src1, sizeof( src1))));

	fs1.push_byte( &src2, sizeof( src2));

	PFS::CImageFile cif( df.GetImage());
	PFS::CFileStream fs2( cif);
	int des1, des2;
	fs2.pop_byte( &des1, sizeof( des1));
	fs2.pop_byte( &des2, sizeof( des2));
}

void CFileMarshalTestCase::testPushString()
{
	PFS::CDataFile df;
	PFS::CFileStream fs1( df);
	const std::wstring src1 = L"1234567890";
	const std::wstring src2 = L"0987654321";
	fs1.push_string( src1);
	fs1.push_string( src2);

	PFS::CImageFile cif( df.GetImage());
	PFS::CFileStream fs2( cif);
	std::wstring des1, des2;
	fs2.pop_string( des1);
	fs2.pop_string( des2);

	CPPUNIT_ASSERT( src1 == des1);
	CPPUNIT_ASSERT( src2 == des2);
}

void CFileMarshalTestCase::testPushData()
{
	PFS::CDataFile df;
	PFS::CFileStream fs1( df);
	PFS::CDataBlock src1( "1234567890", 10);
	PFS::CDataBlock src2( "0987654321", 10);
	fs1.push_data( src1);
	fs1.push_data( src2);

	PFS::CImageFile cif( df.GetImage());
	PFS::CFileStream fs2( cif);
	PFS::CDataBlock des1, des2;
	fs2.pop_data( des1);
	fs2.pop_data( des2);

	CPPUNIT_ASSERT( src1 == des1);
	CPPUNIT_ASSERT( src2 == des2);
}

void CFileMarshalTestCase::testPushSize()
{
	PFS::CDataFile df;
	PFS::CFileStream fs1( df);

	size_t	datas[]	=
	{
		0x01,		0x02,
		0x7e,		0x7f,
		0x80,		0x81,
		0xfe,		0xff,
		0x3ffe,		0x3fff,
		0x4000,		0x4001,
		0xfe00,		0xff00,
		0x1ffffffe,	0x1fffffff,
		0x20000000,	0x20000001,
		0xfe000000,	0xff000000,
		0xfffffffe,	0xffffffff,
	};
	size_t count = sizeof( datas) / sizeof( datas[0]);
	for( size_t i = 0; i < count; ++i)
	{
		fs1.push_size( datas[i]);
	}

	PFS::CImageFile cif( df.GetImage());
	PFS::CFileStream fs2( cif);
	for ( size_t i = 0; i < count; ++i)
	{
		CPPUNIT_ASSERT( datas[i] == fs2.pop_size());
	}
}

void CFileMarshalTestCase::testStreamIO()
{
	char				src1	= 1;
	unsigned char		src2	= 2;
	bool				src3	= true;
	short				src4	= 4;
	unsigned short		src5	= 5;
	int					src6	= 6;
	unsigned int		src7	= 7;
	long				src8	= 8;
	unsigned long		src9	= 9;
	long long			src10	= 10;
	unsigned long long	src11	= 11;
	std::wstring		src12	= L"1234567890";
	PFS::CDataBlock		src13( src12.c_str(), src12.size() * sizeof(wchar_t) );

	PFS::CDataFile df;
	PFS::CFileStream fs1( df);
	fs1	<<	src1
		<<	src2
		<<	src3
		<<	src4
		<<	src5
		<<	src6
		<<	src7
		<<	src8
		<<	src9
		<<	src10
		<<	src11
		<<	src12
		<<	src13;

	char				des1;
	unsigned char		des2;
	bool				des3;
	short				des4;
	unsigned short		des5;
	int					des6;
	unsigned int		des7;
	long				des8;
	unsigned long		des9;
	long long			des10;
	unsigned long long	des11;
	std::wstring		des12;
	PFS::CDataBlock		des13;

	PFS::CImageFile cif( df.GetImage());
	PFS::CFileStream fs2( cif);

	fs2	>>	des1
		>>	des2
		>>	des3
		>>	des4
		>>	des5
		>>	des6
		>>	des7
		>>	des8
		>>	des9
		>>	des10
		>>	des11
		>>	des12
		>>	des13;

	CPPUNIT_ASSERT_EQUAL( src1, des1);
	CPPUNIT_ASSERT_EQUAL( src2, des2);
	CPPUNIT_ASSERT_EQUAL( src3, des3);
	CPPUNIT_ASSERT_EQUAL( src4, des4);
	CPPUNIT_ASSERT_EQUAL( src5, des5);
	CPPUNIT_ASSERT_EQUAL( src6, des6);
	CPPUNIT_ASSERT_EQUAL( src7, des7);
	CPPUNIT_ASSERT_EQUAL( src8, des8);
	CPPUNIT_ASSERT_EQUAL( src9, des9);
	CPPUNIT_ASSERT_EQUAL( src10, des10);
	CPPUNIT_ASSERT_EQUAL( src11, des11);
	CPPUNIT_ASSERT( src12 == des12);
	CPPUNIT_ASSERT( src13 == des13);
}

void CFileMarshalTestCase::testPopByte()
{
	// testPushByte 中
}

void CFileMarshalTestCase::testPopString()
{
	// testPushString 中
}

void CFileMarshalTestCase::testPopData()
{
	// testPushData 中
}

void CFileMarshalTestCase::testPopSize()
{
	// testPushSize 中
}