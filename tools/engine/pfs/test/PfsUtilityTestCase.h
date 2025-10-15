#pragma once

#include <cppunit/extensions/HelperMacros.h>

class CDataBlockTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CDataBlockTestCase);
	CPPUNIT_TEST( testConstruct);
	CPPUNIT_TEST( testGetData);
	CPPUNIT_TEST( testGetSize);
	CPPUNIT_TEST( testResize);
	CPPUNIT_TEST( testReserve);
	CPPUNIT_TEST( testClear);
	CPPUNIT_TEST( testAssign);
	CPPUNIT_TEST( testAppend);
	CPPUNIT_TEST( testSwap);
	CPPUNIT_TEST_SUITE_END();

public:
	CDataBlockTestCase(void);
	virtual ~CDataBlockTestCase(void);

public:
	void testConstruct();
	void testGetData();
	void testGetSize();
	void testResize();
	void testReserve();
	void testClear();
	void testAssign();
	void testAppend();
	void testSwap();
};
