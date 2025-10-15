#pragma once
#include <cppunit/extensions/HelperMacros.h>

class CPfsxAttributeTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CPfsxAttributeTestCase );
	CPPUNIT_TEST( testMakeTypeIDByName );
	CPPUNIT_TEST( testMakeFilePfsxAttributes );
	CPPUNIT_TEST( testGetEncoderTypeFromFilePfsxAttributes );
	CPPUNIT_TEST( testGetDecoderTypeFromFilePfsxAttributes );
	CPPUNIT_TEST_SUITE_END();

public:
	void testMakeTypeIDByName();
	void testMakeFilePfsxAttributes();
	void testGetEncoderTypeFromFilePfsxAttributes();
	void testGetDecoderTypeFromFilePfsxAttributes();
};


class CTranslateManagerTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CTranslateManagerTestCase);
	CPPUNIT_TEST( testGetEncoder);
	CPPUNIT_TEST( testGetDecoder);
	CPPUNIT_TEST( testJpgTranslate);
	CPPUNIT_TEST( testTgaTranslate);
	CPPUNIT_TEST( testP00Translate);
	CPPUNIT_TEST( testP01Translate);
	CPPUNIT_TEST( testP02Translate);
	CPPUNIT_TEST( testP03Translate);
	CPPUNIT_TEST( testP04Translate);
	CPPUNIT_TEST( testP06Translate);
	CPPUNIT_TEST( testB00Translate);
	CPPUNIT_TEST( testB01Translate);
	CPPUNIT_TEST( testB02Translate);
	CPPUNIT_TEST( testB03Translate);
	CPPUNIT_TEST( testB04Translate);
	CPPUNIT_TEST( testB06Translate);
	CPPUNIT_TEST( testZIPTranslate);
	CPPUNIT_TEST( testX20Translate);
	CPPUNIT_TEST_SUITE_END();

public:
	CTranslateManagerTestCase(void);
	virtual ~CTranslateManagerTestCase(void);

	void setUp();
	void tearDown();

public:
	void testGetEncoder();
	void testGetDecoder();
	void testJpgTranslate();
	void testTgaTranslate();
	void testP00Translate();
	void testP01Translate();
	void testP02Translate();
	void testP03Translate();
	void testP04Translate();
	void testP06Translate();
	void testB00Translate();
	void testB01Translate();
	void testB02Translate();
	void testB03Translate();
	void testB04Translate();
	void testB06Translate();
	void testZIPTranslate();
	void testX20Translate();
};
