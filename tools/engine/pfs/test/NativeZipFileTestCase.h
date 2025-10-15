#pragma once
#include <cppunit/extensions/HelperMacros.h>

class CZipReadFileTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CZipReadFileTestCase);
	CPPUNIT_TEST( testConstruct);
	CPPUNIT_TEST( testIO);
	CPPUNIT_TEST_SUITE_END();

public:
	CZipReadFileTestCase(void);
	virtual ~CZipReadFileTestCase(void);

public:
	void testConstruct();
	void testIO();
};


class CZipWriteFileTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CZipWriteFileTestCase);
	CPPUNIT_TEST( testConstruct);
	CPPUNIT_TEST( testIO);
	CPPUNIT_TEST_SUITE_END();

public:
	CZipWriteFileTestCase(void) {}
	virtual ~CZipWriteFileTestCase(void) {}

public:
	void testConstruct();
	void testIO();
};