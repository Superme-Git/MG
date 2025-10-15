#include "StdAfx.h"
#include "TranslateTestCase.h"
#include "TranslateCommon.h"

//CPPUNIT_TEST_SUITE_REGISTRATION( CPfsxAttributeTestCase);

void CPfsxAttributeTestCase::testMakeTypeIDByName()
{
	CPPUNIT_ASSERT( 0x04030201 == PFSX::MakeTypeIDByName( 1, 2, 3, 4));
}

void CPfsxAttributeTestCase::testMakeFilePfsxAttributes()
{
	CPPUNIT_ASSERT( 0x0102030405060708 == PFSX::MakeFilePfsxAttributes( 0x05060708, 0x01020304));
}

void CPfsxAttributeTestCase::testGetEncoderTypeFromFilePfsxAttributes()
{
	CPPUNIT_ASSERT( 0x05060708 == PFSX::GetEncoderTypeFromFilePfsxAttributes( 0x0102030405060708));
}

void CPfsxAttributeTestCase::testGetDecoderTypeFromFilePfsxAttributes()
{
	CPPUNIT_ASSERT( 0x01020304 == PFSX::GetDecoderTypeFromFilePfsxAttributes( 0x0102030405060708));
}

//CPPUNIT_TEST_SUITE_REGISTRATION( CTranslateManagerTestCase);

CTranslateManagerTestCase::CTranslateManagerTestCase(void)
{
}

CTranslateManagerTestCase::~CTranslateManagerTestCase(void)
{
}


void CTranslateManagerTestCase::setUp()
{
	bool b = PFS::CEnv::Mount( L"/src", L"d:/pfstest/translate", PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::Mount( L"/des", L"d:/pfstest/translate_des", PFS::FST_NATIVE, PFS::MT_WRITABLE);
}

void CTranslateManagerTestCase::tearDown()
{
	bool b = PFS::CEnv::Unmount( L"/des");
	b = PFS::CEnv::Unmount( L"/src");
}

void CTranslateManagerTestCase::testGetEncoder()
{
	PFSX::CTranslateManager& manager = PFSX::CTranslateManager::GetInstance();

	// 已存在
	PFSX::CEncoder* encoder = NULL;
	for( int i=0; i<CTranslateCommon::COUNT; i++)
	{
		if( 0 == g_TranslateId[i])
			continue;
		encoder = manager.GetEncoder( g_TranslateId[i]);
		CPPUNIT_ASSERT( NULL != encoder);
		encoder->Delete();
	}

	// 未存在
	PFSX::TSID sid = PFSX::MakeTypeIDByName( 'A', 'B', 'C');
	encoder = manager.GetEncoder( sid);
	CPPUNIT_ASSERT( NULL == encoder);
}

void CTranslateManagerTestCase::testGetDecoder()
{
	PFSX::CTranslateManager& manager = PFSX::CTranslateManager::GetInstance();

	// 已存在
	PFSX::CDecoder* decoder = NULL;
	for( int i=0; i<CTranslateCommon::COUNT; i++)
	{
		if( 0 == g_TranslateId[i])
			continue;
		decoder = manager.GetDecoder( g_TranslateId[i]);
		CPPUNIT_ASSERT( NULL != decoder);
		decoder->Delete();
	}

	// 未存在
	PFSX::TSID sid = PFSX::MakeTypeIDByName( 'A', 'B', 'C');
	decoder = manager.GetDecoder( sid);
	CPPUNIT_ASSERT( NULL == decoder);
}

void CTranslateManagerTestCase::testJpgTranslate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.jpg", g_TranslateId[CTranslateCommon::JPG]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testTgaTranslate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.tga", g_TranslateId[CTranslateCommon::TGA]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testP00Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.p00", g_TranslateId[CTranslateCommon::P00]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testP01Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.p01", g_TranslateId[CTranslateCommon::P01]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testP02Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.p02", g_TranslateId[CTranslateCommon::P02]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testP03Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.p03", g_TranslateId[CTranslateCommon::P03]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testP04Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.p04", g_TranslateId[CTranslateCommon::P04]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testP06Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.p06", g_TranslateId[CTranslateCommon::P06]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testB00Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.b00", g_TranslateId[CTranslateCommon::B00]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testB01Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.b01", g_TranslateId[CTranslateCommon::B01]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testB02Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.b02", g_TranslateId[CTranslateCommon::B02]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testB03Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.b03", g_TranslateId[CTranslateCommon::B03]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testB04Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.b04", g_TranslateId[CTranslateCommon::B04]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testB06Translate()
{
	bool b = CTranslateCommon::DoAllPictures( L"/1.b06", g_TranslateId[CTranslateCommon::B06]);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testZIPTranslate()
{
	bool b = CTranslateCommon::Do( L"/1.jpg", g_TranslateId[CTranslateCommon::ZIP], 0);
	CPPUNIT_ASSERT( b);
}

void CTranslateManagerTestCase::testX20Translate()
{
	bool b = CTranslateCommon::Do( L"/1.xml", 0, g_TranslateId[CTranslateCommon::X20]);
	CPPUNIT_ASSERT( b);
}