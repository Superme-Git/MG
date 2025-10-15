#include "StdAfx.h"
#include "VersionMetaFileTestCase.h"
#include "../pfs2.h"
#include "../src/meta/metafileio.h"
#include "../versionmetafile.h"

//CPPUNIT_TEST_SUITE_REGISTRATION( CVersionMetaFileTestCase);

CVersionMetaFileTestCase::CVersionMetaFileTestCase(void)
{
}

CVersionMetaFileTestCase::~CVersionMetaFileTestCase(void)
{
}

void CVersionMetaFileTestCase::testLoadFrom()
{
	// 错误文件
	PFSX::CNativeFile errfile;
	errfile.Open( L"d:/pfstest/native/1.txt", PFS::FM_EXCL, PFS::FA_RDONLY);

	PFSX::CVersionInfo version1;
	bool b = version1.LoadFrom( errfile);
	CPPUNIT_ASSERT( !b && ( PFS::EC_INVALID_VERSION_INFO == PFS::CEnv::GetLastError()));

	PFS::CDataFile df;
	
	version1.m_Version = 1234567890;
	version1.m_Hash = L"1234567890";
	version1.m_ReleaseDate = 1234567890;
	version1.m_UpdateLog = L"0987654321";
	PFSX::VersionUpdateInfo info1;
	info1.Type = PFSX::VERSION_DEFAULT;
	info1.Url = L"www.locojoy.com";
	version1.AddUpdateInfo( info1);
	PFSX::VersionUpdateInfo info2;
	info2.Type = PFSX::VERSION_FULL;
	info2.Url = L"www.wanmei1.com";
	version1.AddUpdateInfo( info2);

	version1.SaveTo( df);

	PFS::CImageFile cif( df.GetImage());
	PFSX::CVersionInfo version2;
	version2.LoadFrom( cif);
	CPPUNIT_ASSERT( version1.m_Version == version2.m_Version);
	CPPUNIT_ASSERT( version1.m_Hash == version2.m_Hash);
	CPPUNIT_ASSERT( version1.m_ReleaseDate == version2.m_ReleaseDate);
	//CPPUNIT_ASSERT( version1.m_UpdateLog == version2.m_UpdateLog);
	const PFSX::VersionUpdateInfo* pInfo3 = version2.GetUpdateInfoByType( PFSX::VERSION_DEFAULT);
	CPPUNIT_ASSERT( pInfo3 && (pInfo3->Url == info1.Url));
	const PFSX::VersionUpdateInfo* pInfo4 = version2.GetUpdateInfoByType( PFSX::VERSION_FULL);
	CPPUNIT_ASSERT( pInfo4 && (pInfo4->Url == info2.Url));
}

void CVersionMetaFileTestCase::testGetUpdateInfoByType()
{
	// 在 testLoadFrom 中
}

void CVersionMetaFileTestCase::testAddUpdateInfo()
{
	// 在 testLoadFrom 中
}

void CVersionMetaFileTestCase::testReadNode()
{
	// 在 testLoadFrom 中
}

void CVersionMetaFileTestCase::testWriteNode()
{
	// 在 testLoadFrom 中
}

void CVersionMetaFileTestCase::testWriteTo()
{
	// 在 testLoadFrom 中
}

void CVersionMetaFileTestCase::testSaveTo()
{
	// 在 testLoadFrom 中
}