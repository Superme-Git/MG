#pragma once
#include <cppunit/extensions/HelperMacros.h>

class CVersionMetaFileTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CVersionMetaFileTestCase);
	CPPUNIT_TEST( testLoadFrom);
	CPPUNIT_TEST_SUITE_END();

public:
	CVersionMetaFileTestCase(void);
	virtual ~CVersionMetaFileTestCase(void);

	void testLoadFrom();
	void testGetUpdateInfoByType();
	void testAddUpdateInfo();
	void testReadNode();
	void testWriteNode();
	void testWriteTo();
	void testSaveTo();

};
