#pragma once
#include <cppunit/extensions/HelperMacros.h>

#include <assert.h>
#include <string>
#include <map>
#include "../pfs.h"
#include "../nativezipfile.h"

class CAsyncReadTestCase : public CPPUNIT_NS::TestFixture
{
	CPPUNIT_TEST_SUITE( CAsyncReadTestCase);
	CPPUNIT_TEST( testReadExistFile);
	CPPUNIT_TEST( testReadUnExistFile);
	CPPUNIT_TEST( testSyncFile);
	CPPUNIT_TEST_SUITE_END();

	int					m_error_exist;
	std::wstring		m_file_exist;
	PFS::CImageData		m_data_exist;

	int					m_error_unexist;
	std::wstring		m_file_unexist;
	PFS::CImageData		m_data_unexist;

	PFSX::CSyncFile		m_syncfile;

public:
	CAsyncReadTestCase(void);
	virtual ~CAsyncReadTestCase(void);

	void setUp();
	void tearDown();

	int& GetError_Exist() { return m_error_exist; }
	std::wstring& GetFile_Exist( ) { return m_file_exist; }
	PFS::CImageData& GetData_Exist( ) { return m_data_exist; }
	int& GetError_UnExist() { return m_error_unexist; }
	std::wstring& GetFile_UnExist( ) { return m_file_unexist; }
	PFS::CImageData& GetData_UnExist( ) { return m_data_unexist; }

public:
	void testReadExistFile();
	void testReadUnExistFile();
	void testSyncFile();
};
