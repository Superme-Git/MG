#pragma once
#include <cppunit/extensions/HelperMacros.h>

class CCTransferTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CCTransferTestCase );
	CPPUNIT_TEST( testHexToInt);
	CPPUNIT_TEST( testString2Data);
	CPPUNIT_TEST( testString2Int);
	CPPUNIT_TEST( testInt2String);
	CPPUNIT_TEST_SUITE_END();

public:
	CCTransferTestCase(void) {}
	virtual ~CCTransferTestCase(void) {}

public:
	void testHexToInt();
	void testString2Data();
	void testString2Int();
	void testData2String();
	void testInt2String();
};

class CMetaReadTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CMetaReadTestCase );
	CPPUNIT_TEST( testMetaFileRead);
	CPPUNIT_TEST( testMetaDataRead);
	CPPUNIT_TEST_SUITE_END();

public:
	void testMetaFileRead();
	void testMetaDataRead();
};

class CMetaWriteTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CMetaWriteTestCase );
	CPPUNIT_TEST( testMetaFileWrite);
	CPPUNIT_TEST( testMetaDataWrite);
	CPPUNIT_TEST_SUITE_END();

public:
	void testMetaFileWrite();
	void testMetaDataWrite();
};