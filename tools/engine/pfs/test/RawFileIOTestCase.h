#pragma once
#include <cppunit/extensions/HelperMacros.h>

class CFileIOTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CFileIOTestCase );
	CPPUNIT_TEST( testConstruct);
	CPPUNIT_TEST( testCheckCreateDirectory);
	CPPUNIT_TEST( testCheckCreateParentDirectory);
	CPPUNIT_TEST( testIsDirectoryExisting);
	CPPUNIT_TEST( testIsFileExisting);
	CPPUNIT_TEST( testDeleteFile);
	CPPUNIT_TEST( testIO);
	CPPUNIT_TEST_SUITE_END();

public:
	CFileIOTestCase(void);
	virtual ~CFileIOTestCase(void);

public:
	void testConstruct();
	void testCheckCreateDirectory();
	void testCheckCreateParentDirectory();
	void testIsDirectoryExisting();
	void testIsFileExisting();
	void testDeleteFile();
	void testIO();
};

class CFileImageHandleTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CFileImageHandleTestCase );
	CPPUNIT_TEST( testConstruct);
	CPPUNIT_TEST( testInitialize);
	CPPUNIT_TEST( testGetHandle);
	CPPUNIT_TEST( testGetSize);
	CPPUNIT_TEST_SUITE_END();

public:
	CFileImageHandleTestCase() {}
	~CFileImageHandleTestCase() {}

public:
	void testConstruct();
	void testInitialize();
	void testGetHandle();
	void testGetSize();
};

class CFileImageTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CFileImageTestCase );
	CPPUNIT_TEST( testGetData);
	CPPUNIT_TEST( testGetSize);
	CPPUNIT_TEST_SUITE_END();

public:
	void testGetData();
	void testGetSize();
};

class CPieceFileImageTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CPieceFileImageTestCase );
	CPPUNIT_TEST( testGetData);
	CPPUNIT_TEST( testGetSize);
	CPPUNIT_TEST_SUITE_END();

public:
	void testGetData();
	void testGetSize();
};