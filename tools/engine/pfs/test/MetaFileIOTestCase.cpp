#include "StdAfx.h"
#include "MetaFileIOTestCase.h"
#include "../pfs2.h"
#include "../src/native/rawfileio.h"
#include "../src/meta/metafileio.h"

//CPPUNIT_TEST_SUITE_REGISTRATION( CCTransferTestCase);

void CCTransferTestCase::testHexToInt()
{
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(0)  ,  PFS::CTransfer::HexToInt(L'0') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(1)  ,  PFS::CTransfer::HexToInt(L'1') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(2)  ,  PFS::CTransfer::HexToInt(L'2') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(3)  ,  PFS::CTransfer::HexToInt(L'3') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(4)  ,  PFS::CTransfer::HexToInt(L'4') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(5)  ,  PFS::CTransfer::HexToInt(L'5') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(6)  ,  PFS::CTransfer::HexToInt(L'6') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(7)  ,  PFS::CTransfer::HexToInt(L'7') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(8)  ,  PFS::CTransfer::HexToInt(L'8') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(9)  ,  PFS::CTransfer::HexToInt(L'9') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(10)  ,  PFS::CTransfer::HexToInt(L'a') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(11)  ,  PFS::CTransfer::HexToInt(L'b') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(12)  ,  PFS::CTransfer::HexToInt(L'c') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(13)  ,  PFS::CTransfer::HexToInt(L'd') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(14)  ,  PFS::CTransfer::HexToInt(L'e') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(15)  ,  PFS::CTransfer::HexToInt(L'f') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(0)  ,  PFS::CTransfer::HexToInt(L'g') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(0)  ,  PFS::CTransfer::HexToInt(L'h') );
	CPPUNIT_ASSERT_EQUAL( static_cast<wchar_t>(0)  ,  PFS::CTransfer::HexToInt(L'i') );
}

void CCTransferTestCase::testString2Data()
{
	std::wstring	dt1	= L"123456789";
	std::wstring	dt2	= L"1234567890";

	unsigned char des1[]	= { 0x12, 0x34, 0x56, 0x78, 0x9};
	unsigned char des2[]	= { 0x12, 0x34, 0x56, 0x78, 0x90};

	PFS::CDataBlock	db1( des1, sizeof(des1));
	PFS::CDataBlock	db2( des2, sizeof(des2));
	CPPUNIT_ASSERT( db1 == PFS::CTransfer::String2Data(dt1) );
	CPPUNIT_ASSERT( db2 == PFS::CTransfer::String2Data(dt2) );
}

void CCTransferTestCase::testString2Int()
{
	std::wstring	dt1	= L"1234567890";
	CPPUNIT_ASSERT( 1234567890 == PFS::CTransfer::String2Int(dt1));
}

void CCTransferTestCase::testData2String()
{
	std::wstring	dt1	= L"1234567890";
	CPPUNIT_ASSERT( dt1 == PFS::CTransfer::Data2String(	PFS::CTransfer::String2Data(dt1)));
}

void CCTransferTestCase::testInt2String()
{
	std::wstring	des	= L"1234567890";
	CPPUNIT_ASSERT( des == PFS::CTransfer::Int2String( 1234567890) );
}

//CPPUNIT_TEST_SUITE_REGISTRATION( CMetaReadTestCase);
void CMetaReadTestCase::testMetaFileRead()
{
	const std::wstring metafilename = L"d:/pfstest/.files.meta";
	PFSX::CNativeFile nf;
	nf.Open( metafilename, PFS::FM_CREAT, PFS::FA_RDWR);
	PFS::CMetaInfoMap mim;
	mim.insert( std::make_pair( L"1.txt", PFS::CMetaInfo()));
	mim.insert( std::make_pair( L"2.txt", PFS::CMetaInfo()));
	bool b = PFS::CMetaFileWrite::DoWrite( nf, mim);
	CPPUNIT_ASSERT( b);

	PFS::CMetaInfoMap mim2;
	nf.Seek( 0, PFS::FSM_SET);
	b = PFS::CMetaFileRead::DoRead( nf, mim2);

	CPPUNIT_ASSERT( b && (mim.size() == mim2.size()));

	nf.Close();
	PFS::CFileIO::DeleteFile( metafilename);
}

void CMetaReadTestCase::testMetaDataRead()
{
	PFS::CDataBlock db;
	PFS::CMetaInfoMap mim;
	mim.insert( std::make_pair( L"1.txt", PFS::CMetaInfo()));
	mim.insert( std::make_pair( L"2.txt", PFS::CMetaInfo()));
	bool b = PFS::CMetaDataWrite::DoWrite( db, mim);
	CPPUNIT_ASSERT( b);

	PFS::CMetaInfoMap mim2;
	b = PFS::CMetaDataRead::DoRead( db, mim2);

	CPPUNIT_ASSERT( b && (mim.size() == mim2.size()));
}

//CPPUNIT_TEST_SUITE_REGISTRATION( CMetaWriteTestCase);
void CMetaWriteTestCase::testMetaFileWrite()
{
	// 在 CMetaReadTestCase::testMetaFileRead 中

}

void CMetaWriteTestCase::testMetaDataWrite()
{
	// 在 CMetaReadTestCase::testMetaDataRead 中

}
