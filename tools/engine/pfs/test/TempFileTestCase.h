#pragma once
#include <cppunit/extensions/HelperMacros.h>

class CTempFileTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CTempFileTestCase);
	CPPUNIT_TEST( testOpen);
	CPPUNIT_TEST( testClose);
	CPPUNIT_TEST( testGetFileName);
	CPPUNIT_TEST_SUITE_END();

public:
	CTempFileTestCase(void);
	virtual ~CTempFileTestCase(void);

	void testOpen();
	void testClose();
	void testGetFileName();
};
