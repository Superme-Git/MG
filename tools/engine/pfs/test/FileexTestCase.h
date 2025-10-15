#pragma once
#include <cppunit/extensions/HelperMacros.h>

// 测试 PFS::CFile
// 因为 Open 接口的各种参数组合已在CPfsEnvTestCase的OpenFile里测试过了
// 这里主要测试 Read,Write,Seek,SetEnd,GetPos,GetSize 操作
class CFileTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CFileTestCase);
	CPPUNIT_TEST( testNative);
	CPPUNIT_TEST( testZip);
	CPPUNIT_TEST( testBrief);
	CPPUNIT_TEST_SUITE_END();

public:
	CFileTestCase(void);
	virtual ~CFileTestCase(void);

public:
	void testNative();
	void testZip();
	void testBrief();
};


// 测试 PFS::CImageFile
class CImageFileTestCase  : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CImageFileTestCase);
	CPPUNIT_TEST( testImageFile);
	CPPUNIT_TEST_SUITE_END();

public:
	CImageFileTestCase(void) {}
	virtual ~CImageFileTestCase(void) {}

public:
	void testImageFile();
};

// 测试 PFS::CDataFile
class CDataFileTestCase  : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CDataFileTestCase);
	CPPUNIT_TEST( testDataFile);
	CPPUNIT_TEST_SUITE_END();

public:
	CDataFileTestCase(void) {}
	virtual ~CDataFileTestCase(void) {}

public:
	void testDataFile();
};