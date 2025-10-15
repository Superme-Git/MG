#pragma once
#include <cppunit/extensions/HelperMacros.h>

// ���� PFS::CFile
// ��Ϊ Open �ӿڵĸ��ֲ����������CPfsEnvTestCase��OpenFile����Թ���
// ������Ҫ���� Read,Write,Seek,SetEnd,GetPos,GetSize ����
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


// ���� PFS::CImageFile
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

// ���� PFS::CDataFile
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