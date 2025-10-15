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
	// ����׼�����Ի�����d:/pfstest/brief Ŀ¼��֤���ڣ�������version.meta��files.meta�ļ�
	bool b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	CPPUNIT_ASSERT( b);
	b = PFS::CEnv::Unmount( RUN_BRIEF);

	// mount ������������Ŀ¼
	b = PFS::CEnv::Mount( RUN_BRIEF, L"d:/pfstest/native", PFS::FST_BRIEF, PFS::MT_READONLY);
	CPPUNIT_ASSERT( !b && ( ERROR_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));

	// writable
	b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_WRITABLE);
	CPPUNIT_ASSERT( !b && ( PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

}

void CPfsEnvTestCase_Brief::testUnMount()
{
	// �������κβ���ֱ��unmount
	PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	bool b = PFS::CEnv::Unmount( RUN_BRIEF);
	CPPUNIT_ASSERT( b);

	// ���ļ���ʱunmountʧ��
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
	// mount֮ǰ����
	PFS::CMetaInfoMap mim;
	bool b = PFS::CEnv::GetMetaInfos( RUN_BRIEF, mim);
	CPPUNIT_ASSERT( !b && (PFS::EC_MOUNT_DIR_UNFOUND == PFS::CEnv::GetLastError()));

	// ����ʹ��
	b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);

	b = PFS::CEnv::GetMetaInfos( RUN_BRIEF, mim);
	CPPUNIT_ASSERT( b && (0 < mim.size()));

	b = PFS::CEnv::Unmount( RUN_BRIEF);
}

void CPfsEnvTestCase_Brief::testGetMetaInfosEx()
{
	// CPfsEnvTestCase_Native �� CPfsEnvTestCase_Zip
}

void CPfsEnvTestCase_Brief::testCheckMetaInfo()
{
	// ����ʹ��
	bool b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);
	b = PFS::CEnv::CheckMetaInfo( RUN_BRIEF);

	// ����Ӧ�÷��� false ?
	CPPUNIT_ASSERT( b );

	b = PFS::CEnv::Unmount( RUN_BRIEF);
}

void CPfsEnvTestCase_Brief::testGetFileMetaInfo()
{
	// �˺����������mount�����޹�
	// CPfsEnvTestCase_Native �в���
}

void CPfsEnvTestCase_Brief::testSetFileMetaInfo()
{
	// �˺����������mount�����޹�
	// CPfsEnvTestCase_Native �в���
}

void CPfsEnvTestCase_Brief::testAddFileMetaInfo()
{
	// �˺����������mount�����޹�
	// CPfsEnvTestCase_Native �в���
}

void CPfsEnvTestCase_Brief::testIsFileExisting()
{
	// �Ѵ����ļ�
	bool b = PFS::CEnv::Mount( RUN_BRIEF, PATH_BRIEF, PFS::FST_BRIEF, PFS::MT_READONLY);

	b = PFS::CEnv::IsFileExisting( RUN_BRIEF + FILE_EXIST);
	CPPUNIT_ASSERT( b);

	// �����ڵ��ļ�
	b = PFS::CEnv::IsFileExisting( RUN_BRIEF + FILE_UNEXIST);
	CPPUNIT_ASSERT( !b);

	b = PFS::CEnv::Unmount( RUN_BRIEF);
}

void CPfsEnvTestCase_Brief::testSetFileHashMethod()
{
	// �˺����������mount�����޹�
	// CPfsEnvTestCase_Native �в���
}

void CPfsEnvTestCase_Brief::testGetFileHashMethod()
{
	// �˺����������mount�����޹�
	// CPfsEnvTestCase_Native �в���
}

void CPfsEnvTestCase_Brief::testSetDefaultFileHashMethod()
{
	// �˺����������mount�����޹�
	// CPfsEnvTestCase_Native �в���
}

void CPfsEnvTestCase_Brief::testOpenFile()
{
	// mount֮ǰʹ��
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_BRIEF + FILE_EXIST, PFS::FM_CREAT, PFS::FA_RDWR);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));

	// readonly �� mount
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
	// �� testUnMount �� testOpenFile �в���
}

void CPfsEnvTestCase_Brief::testRemoveFile()
{
	// mount֮ǰʹ��
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
	// CPfsEnvTestCase_Native �� CPfsEnvTestCase_Zip
}

void CPfsEnvTestCase_Brief::testPFSFileNameToNativeFileName()
{
	// CPfsEnvTestCase_Native �� CPfsEnvTestCase_Zip
}

void CPfsEnvTestCase_Brief::testSetOutLogInstance()
{
	// �˺����������mount�����޹�
	// �� CPfsEnvTestCase_Native
}

void CPfsEnvTestCase_Brief::testGetOutLogInstance()
{
	// �˺����������mount�����޹�
	// �� CPfsEnvTestCase_Native
}