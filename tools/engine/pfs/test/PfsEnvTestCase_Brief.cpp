#include "StdAfx.h"
#include "PfsEnvTestCase_Brief.h"
#include <assert.h>
#include <windows.h>
#include "../pfs.h"

CPPUNIT_TEST_SUITE_REGISTRATION( CPfsEnvTestCase_Brief);

CPfsEnvTestCase_Brief::CPfsEnvTestCase_Brief(void)
{
}

CPfsEnvTestCase_Brief::~CPfsEnvTestCase_Brief(void)
{
}

const std::wstring RUN_BRIEF = L"/run_brief";
const std::wstring PATH_BRIEF = L"d:/pfstest/brief";
const std::wstring FILE_EXIST = L"/1.txt";
const std::wstring FILE_UNEXIST = L"/2.txt";
const std::wstring CHILD = L"/child";

void CPfsEnvTestCase_Brief::testMount()
{
	// 首先准备测试环境，d:/pfstest/brief 目录保证存在，并存在version.meta和files.meta文件
	bool b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	CPPUNIT_ASSERT( b);
	b = PFS::CEnv::Unmount( RUN_BRIEF);

	// mount 不符合条件的目录
	b = PFS::CEnv::Mount( RUN_BRIEF, L"d:/pfstest/native", PFS::FST_BRIEF, PFS::MT_READONLY);
	CPPUNIT_ASSERT( !b && ( ERROR_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));

	// writable
	b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_WRITABLE);
	CPPUNIT_ASSERT( !b && ( PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

}

void CPfsEnvTestCase_Brief::testUnMount()
{
	// 不进行任何操作直接unmount
	PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	bool b = PFS::CEnv::Unmount( RUN_BRIEF);
	CPPUNIT_ASSERT( b);

	// 有文件打开时unmount失败
	b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_BRIEF + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	if( basefile)
	{
		b = PFS::CEnv::Unmount( RUN_BRIEF);
		CPPUNIT_ASSERT( !b && (PFS::EC_FS_BUSY_NOW == PFS::CEnv::GetLastError()));
		PFS::CEnv::CloseFile( basefile);
		basefile = NULL;

		b = PFS::CEnv::Unmount( RUN_BRIEF);
		CPPUNIT_ASSERT( b);
	}
	else
	{
		b = PFS::CEnv::Unmount( RUN_BRIEF);
		CPPUNIT_ASSERT( b);
	}
}

void CPfsEnvTestCase_Brief::testGetMetaInfos()
{
	// mount之前调用
	PFS::CMetaInfoMap mim;
	bool b = PFS::CEnv::GetMetaInfos( RUN_BRIEF, mim);
	CPPUNIT_ASSERT( !b && (PFS::EC_MOUNT_DIR_UNFOUND == PFS::CEnv::GetLastError()));

	// 正常使用
	b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);

	b = PFS::CEnv::GetMetaInfos( RUN_BRIEF, mim);
	CPPUNIT_ASSERT( b && (0 < mim.size()));

	b = PFS::CEnv::Unmount( RUN_BRIEF);
}

void CPfsEnvTestCase_Brief::testGetMetaInfosEx()
{
	// CPfsEnvTestCase_Native 和 CPfsEnvTestCase_Zip
}

void CPfsEnvTestCase_Brief::testCheckMetaInfo()
{
	// 正常使用
	bool b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	b = PFS::CEnv::CheckMetaInfo( RUN_BRIEF);

	// 这里应该返回 false ?
	CPPUNIT_ASSERT( b );

	b = PFS::CEnv::Unmount( RUN_BRIEF);
}

void CPfsEnvTestCase_Brief::testGetFileMetaInfo()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Brief::testSetFileMetaInfo()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Brief::testAddFileMetaInfo()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Brief::testIsFileExisting()
{
	// 已存在文件
	bool b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);

	b = PFS::CEnv::IsFileExisting( RUN_BRIEF + FILE_EXIST);
	CPPUNIT_ASSERT( b);

	// 不存在的文件
	b = PFS::CEnv::IsFileExisting( RUN_BRIEF + FILE_UNEXIST);
	CPPUNIT_ASSERT( !b);

	b = PFS::CEnv::Unmount( RUN_BRIEF);
}

void CPfsEnvTestCase_Brief::testSetFileHashMethod()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Brief::testGetFileHashMethod()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Brief::testSetDefaultFileHashMethod()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Brief::testOpenFile()
{
	// mount之前使用
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_BRIEF + FILE_EXIST, PFS::FM_CREAT, PFS::FA_RDWR);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));

	// readonly 的 mount
	bool b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	basefile = PFS::CEnv::OpenFile( RUN_BRIEF + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( basefile);
	PFS::CEnv::CloseFile( basefile);

	basefile = PFS::CEnv::OpenFile( RUN_BRIEF + FILE_EXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_READONLY == PFS::CEnv::GetLastError()));

	basefile = PFS::CEnv::OpenFile( RUN_BRIEF + FILE_UNEXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));

	basefile = PFS::CEnv::OpenFile( RUN_BRIEF + FILE_UNEXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_READONLY == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_BRIEF);
}

void CPfsEnvTestCase_Brief::testCloseFile()
{
	// 在 testUnMount 和 testOpenFile 中测了
}

void CPfsEnvTestCase_Brief::testRemoveFile()
{
	// mount之前使用
	bool b = PFS::CEnv::RemoveFile( RUN_BRIEF + FILE_EXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	b = PFS::CEnv::RemoveFile( RUN_BRIEF + FILE_EXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_BRIEF);
}

void CPfsEnvTestCase_Brief::testFileCopy()
{
	
}

void CPfsEnvTestCase_Brief::testNativeFileNameToPFSFileName()
{
	// CPfsEnvTestCase_Native 和 CPfsEnvTestCase_Zip
}

void CPfsEnvTestCase_Brief::testPFSFileNameToNativeFileName()
{
	// CPfsEnvTestCase_Native 和 CPfsEnvTestCase_Zip
}

void CPfsEnvTestCase_Brief::testSetOutLogInstance()
{
	// 此函数跟具体的mount类型无关
	// 在 CPfsEnvTestCase_Native
}

void CPfsEnvTestCase_Brief::testGetOutLogInstance()
{
	// 此函数跟具体的mount类型无关
	// 在 CPfsEnvTestCase_Native
}