#pragma once
#include <cppunit/extensions/HelperMacros.h>

class CFileMarshalTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CFileMarshalTestCase);
	CPPUNIT_TEST( testPushByte);
	CPPUNIT_TEST( testPushString);
	CPPUNIT_TEST( testPushData);
	CPPUNIT_TEST( testPushSize);
	CPPUNIT_TEST( testPopByte);
	CPPUNIT_TEST( testPopString);
	CPPUNIT_TEST( testPopData);
	CPPUNIT_TEST( testPopSize);
	CPPUNIT_TEST( testStreamIO);
	CPPUNIT_TEST_SUITE_END();

public:
	CFileMarshalTestCase(void);
	virtual ~CFileMarshalTestCase(void);

public:
	void testPushByte();
	void testPushString();
	void testPushData();
	void testPushSize();
	void testStreamIO();

	void testPopByte();
	void testPopString();
	void testPopData();
	void testPopSize();
};
