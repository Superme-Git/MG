#pragma once
#include <cppunit/extensions/HelperMacros.h>

class CPfsEnvTestCase_Brief : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CPfsEnvTestCase_Brief);
	CPPUNIT_TEST( testMount);
	CPPUNIT_TEST( testUnMount);
	CPPUNIT_TEST( testGetMetaInfos);
	CPPUNIT_TEST( testGetMetaInfosEx);
	CPPUNIT_TEST( testCheckMetaInfo);
	CPPUNIT_TEST( testGetFileMetaInfo);
	CPPUNIT_TEST( testSetFileMetaInfo);
	CPPUNIT_TEST( testAddFileMetaInfo);
	CPPUNIT_TEST( testIsFileExisting);
	CPPUNIT_TEST( testSetFileHashMethod);
	CPPUNIT_TEST( testGetFileHashMethod);
	CPPUNIT_TEST( testSetDefaultFileHashMethod);
	CPPUNIT_TEST( testOpenFile);
	CPPUNIT_TEST( testCloseFile);
	CPPUNIT_TEST( testRemoveFile);
	CPPUNIT_TEST( testFileCopy);
	CPPUNIT_TEST( testNativeFileNameToPFSFileName);
	CPPUNIT_TEST( testPFSFileNameToNativeFileName);
	CPPUNIT_TEST( testSetOutLogInstance);
	CPPUNIT_TEST( testGetOutLogInstance);
	CPPUNIT_TEST_SUITE_END();

public:
	CPfsEnvTestCase_Brief(void);
	virtual ~CPfsEnvTestCase_Brief(void);

public:
	void testMount();
	void testUnMount();
	void testGetMetaInfos();
	void testGetMetaInfosEx();
	void testCheckMetaInfo();
	void testGetFileMetaInfo();
	void testSetFileMetaInfo();
	void testAddFileMetaInfo();
	void testIsFileExisting();
	void testSetFileHashMethod();
	void testGetFileHashMethod();
	void testSetDefaultFileHashMethod();
	void testOpenFile();
	void testCloseFile();
	void testRemoveFile();
	void testFileCopy();
	void testNativeFileNameToPFSFileName();
	void testPFSFileNameToNativeFileName();
	void testSetOutLogInstance();
	void testGetOutLogInstance();
};
