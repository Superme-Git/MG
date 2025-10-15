#include "StdAfx.h"
#include "PfsEnvTestCase_Zip.h"
#include <assert.h>
#include "../pfs.h"

CPPUNIT_TEST_SUITE_REGISTRATION( CPfsEnvTestCase_Zip);

CPfsEnvTestCase_Zip::CPfsEnvTestCase_Zip(void)
{
}

CPfsEnvTestCase_Zip::~CPfsEnvTestCase_Zip(void)
{
}

const std::wstring RUN_ZIP = L"/run_zip";
const std::wstring PATH_ZIP = L"d:/pfstest/1.zip";
const std::wstring FILE_EXIST = L"/1.txt";
const std::wstring FILE_UNEXIST = L"/2.txt";
const std::wstring CHILD = L"/child";

void CPfsEnvTestCase_Zip::testMount()
{
	// 首先准备测试环境，d:/pfstest/1.zip文件保证存在
	bool b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	CPPUNIT_ASSERT( b);

	b = PFS::CEnv::Unmount( RUN_ZIP);

	// mount 不是zip格式的文件
	b = PFS::CEnv::Mount( RUN_ZIP, L"d:/pfstest/native/1.txt", PFS::FST_ZIP, PFS::MT_READONLY);
	CPPUNIT_ASSERT( !b && ( PFS::EC_INVALID_MOUNT_DEVICE == PFS::CEnv::GetLastError()));

	// writable
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_WRITABLE);
	CPPUNIT_ASSERT( b);

	// writeable方式mount不存在的目录
	//b = PFS::CEnv::Mount( RUN_UNEXIST, PATH_UNEXIST, PFS::FST_ZIP, PFS::MT_WRITABLE);
	//CPPUNIT_ASSERT( b);

	//b = PFS::CEnv::Unmount( RUN_UNEXIST);
	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testUnMount()
{
	// 不进行任何操作直接unmount
	PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	bool b = PFS::CEnv::Unmount( RUN_ZIP);
	CPPUNIT_ASSERT( b);

	// 有文件打开时unmount失败
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	if( basefile)
	{
		b = PFS::CEnv::Unmount( RUN_ZIP);
		CPPUNIT_ASSERT( !b && (PFS::EC_FS_BUSY_NOW == PFS::CEnv::GetLastError()));
		PFS::CEnv::CloseFile( basefile);
		basefile = NULL;

		b = PFS::CEnv::Unmount( RUN_ZIP);
		CPPUNIT_ASSERT( b);
	}
	else
	{
		b = PFS::CEnv::Unmount( RUN_ZIP);
		CPPUNIT_ASSERT( b);
	}

	// 有子目录被mount时unmount失败
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_WRITABLE);
	b = PFS::CEnv::Mount( RUN_ZIP + CHILD, RUN_ZIP + CHILD, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( b)
	{
		b = PFS::CEnv::Unmount( RUN_ZIP);
		CPPUNIT_ASSERT( !b && (PFS::EC_FS_BUSY_NOW == PFS::CEnv::GetLastError()));
		b = PFS::CEnv::Unmount( RUN_ZIP + CHILD);

		b = PFS::CEnv::Unmount( RUN_ZIP);
		CPPUNIT_ASSERT( b);
	}
	else
	{
		b = PFS::CEnv::Unmount( RUN_ZIP);
		CPPUNIT_ASSERT( b);
	}
}

void CPfsEnvTestCase_Zip::testGetMetaInfos()
{
	// mount之前调用
	PFS::CMetaInfoMap mim;
	bool b = PFS::CEnv::GetMetaInfos( RUN_ZIP, mim);
	CPPUNIT_ASSERT( !b && (PFS::EC_MOUNT_DIR_UNFOUND == PFS::CEnv::GetLastError()));

	// 正常使用
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	b = PFS::CEnv::CheckMetaInfo( RUN_ZIP);

	b = PFS::CEnv::GetMetaInfos( RUN_ZIP, mim);
	CPPUNIT_ASSERT( b && (0 < mim.size()));

	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testGetMetaInfosEx()
{
	bool b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	b = PFS::CEnv::Mount( RUN_ZIP + CHILD, RUN_ZIP + CHILD, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::CheckMetaInfo( RUN_ZIP);
	b = PFS::CEnv::CheckMetaInfo( RUN_ZIP + CHILD);

	PFS::CMetaInfoMap mim;
	b = PFS::CEnv::GetMetaInfosEx( RUN_ZIP, mim);
	CPPUNIT_ASSERT( b && ( 2 <= mim.size()));

	mim.clear();
	b = PFS::CEnv::GetMetaInfosEx( RUN_ZIP + CHILD, mim);
	CPPUNIT_ASSERT( b && ( 1 <= mim.size()));

	b = PFS::CEnv::Unmount( RUN_ZIP + CHILD);
	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testCheckMetaInfo()
{
	// mount之前调用
	bool b = PFS::CEnv::CheckMetaInfo( RUN_ZIP);
	CPPUNIT_ASSERT( !b && (PFS::EC_MOUNT_DIR_UNFOUND == PFS::CEnv::GetLastError()));

	// 正常使用
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	b = PFS::CEnv::CheckMetaInfo( RUN_ZIP);

	PFS::CMetaInfoMap mim;
	b = PFS::CEnv::GetMetaInfos( RUN_ZIP, mim);
	CPPUNIT_ASSERT( b && (0 < mim.size()));

	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testGetFileMetaInfo()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Zip::testSetFileMetaInfo()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Zip::testAddFileMetaInfo()
{
	// 此函数跟具体的mount类型无关
	// CPfsEnvTestCase_Native 中测了
}

void CPfsEnvTestCase_Zip::testIsFileExisting()
{
	// 已存在文件
	bool b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	b = PFS::CEnv::CheckMetaInfo( RUN_ZIP);

	b = PFS::CEnv::IsFileExisting( RUN_ZIP + FILE_EXIST);
	CPPUNIT_ASSERT( b);

	// 不存在的文件
	b = PFS::CEnv::IsFileExisting( RUN_ZIP + FILE_UNEXIST);
	CPPUNIT_ASSERT( !b);

	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testSetFileHashMethod()
{
	// 在 CPfsEnvTestCase_Native
}

void CPfsEnvTestCase_Zip::testGetFileHashMethod()
{
	// 在 CPfsEnvTestCase_Native
}

void CPfsEnvTestCase_Zip::testSetDefaultFileHashMethod()
{
	// 在 CPfsEnvTestCase_Native
}

void CPfsEnvTestCase_Zip::testOpenFile()
{
	// mount之前使用
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_EXIST, PFS::FM_CREAT, PFS::FA_RDWR);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));

	// readonly 的 mount
	bool b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( basefile);
	PFS::CEnv::CloseFile( basefile);

	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_EXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_UNEXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));

	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_UNEXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_ZIP);

	// writable 的 mount
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_WRITABLE);
	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( basefile);
	PFS::CEnv::CloseFile( basefile);

	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_EXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( basefile );
	PFS::CEnv::CloseFile( basefile);

	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_UNEXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( !basefile && ( PFS::EC_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));

	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_UNEXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( basefile );
	PFS::CEnv::CloseFile( basefile);

	// 删掉此文件方便下一次测试
	PFS::CEnv::RemoveFile( RUN_ZIP + FILE_UNEXIST);

	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testCloseFile()
{
	// 在 testUnMount 和 testOpenFile 中测了
}

void CPfsEnvTestCase_Zip::testRemoveFile()
{
	// mount之前使用
	bool b = PFS::CEnv::RemoveFile( RUN_ZIP + FILE_EXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));

	// readonly 的 mount
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	b = PFS::CEnv::RemoveFile( RUN_ZIP + FILE_EXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	PFS::CEnv::CloseFile( basefile);
	b = PFS::CEnv::RemoveFile( RUN_ZIP + FILE_EXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_ZIP);

	// writable 的 mount
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_WRITABLE);
	basefile = PFS::CEnv::OpenFile( RUN_ZIP + FILE_UNEXIST, PFS::FM_CREAT, PFS::FA_RDWR);
	b = PFS::CEnv::RemoveFile( RUN_ZIP + FILE_UNEXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_SHARING_VIOLATION == PFS::CEnv::GetLastError()));

	PFS::CEnv::CloseFile( basefile);
	b = PFS::CEnv::RemoveFile( RUN_ZIP + FILE_UNEXIST);
	CPPUNIT_ASSERT( b );

	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testFileCopy()
{
	// readonly 的 mount
	bool b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	b = PFS::CEnv::FileCopy( RUN_ZIP + FILE_EXIST , RUN_ZIP + FILE_UNEXIST, false);
	CPPUNIT_ASSERT( !b &&( PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_ZIP);

	// writeable 的 mount
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_WRITABLE);
	b = PFS::CEnv::FileCopy( RUN_ZIP + FILE_EXIST , RUN_ZIP + FILE_UNEXIST, false);
	CPPUNIT_ASSERT( b);

	b = PFS::CEnv::FileCopy( RUN_ZIP + FILE_EXIST , RUN_ZIP + FILE_UNEXIST, true);
	CPPUNIT_ASSERT( !b && ( PFS::EC_COPY_DST_EXISTING == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::RemoveFile( RUN_ZIP + FILE_UNEXIST);
	CPPUNIT_ASSERT( b );

	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testNativeFileNameToPFSFileName()
{
	// mount之前调用
	std::wstring in = PATH_ZIP + FILE_EXIST;
	std::wstring out;
	bool b = PFS::CEnv::NativeFileNameToPFSFileName( in, out);
	CPPUNIT_ASSERT( !b && ( PFS::EC_MAP_UNFOUND == PFS::CEnv::GetLastError()));

	// 正常调用
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	b = PFS::CEnv::NativeFileNameToPFSFileName( in, out);
	CPPUNIT_ASSERT( !b && ( PFS::EC_MAP_UNFOUND == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testPFSFileNameToNativeFileName()
{
	// mount之前调用
	std::wstring in = RUN_ZIP + FILE_EXIST;
	std::wstring out;
	bool b = PFS::CEnv::PFSFileNameToNativeFileName( in, out);
	CPPUNIT_ASSERT( !b && ( PFS::EC_MAP_UNFOUND == PFS::CEnv::GetLastError()));

	// 正常调用
	b = PFS::CEnv::Mount( RUN_ZIP, PATH_ZIP, PFS::FST_ZIP, PFS::MT_READONLY);
	b = PFS::CEnv::PFSFileNameToNativeFileName( in, out);
	CPPUNIT_ASSERT( !b && ( PFS::EC_MAP_UNFOUND == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_ZIP);
}

void CPfsEnvTestCase_Zip::testSetOutLogInstance()
{
	// 此函数跟具体的mount类型无关
	// 在 CPfsEnvTestCase_Native
}

void CPfsEnvTestCase_Zip::testGetOutLogInstance()
{
	// 此函数跟具体的mount类型无关
	// 在 CPfsEnvTestCase_Native
}