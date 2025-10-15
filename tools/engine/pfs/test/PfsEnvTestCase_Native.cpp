#include "StdAfx.h"
#include "PfsEnvTestCase_Native.h"
#include <assert.h>
#include <algorithm>
#include <windows.h>
#include "../pfs.h"

CPPUNIT_TEST_SUITE_REGISTRATION( CPfsEnvTestCase_Native);

CPfsEnvTestCase_Native::CPfsEnvTestCase_Native(void)
{
}

CPfsEnvTestCase_Native::~CPfsEnvTestCase_Native(void)
{
}

const std::wstring RUN_NATIVE = L"/run_native";
const std::wstring PATH_NATIVE = L"d:/pfstest/native";
const std::wstring RUN_UNEXIST = L"/run_unexist";
const std::wstring PATH_UNEXIST = L"d:/pfstest/unexist";
const std::wstring FILE_EXIST = L"/1.txt";
const std::wstring FILE_UNEXIST = L"/2.txt";
const std::wstring CHILD = L"/child";

void CPfsEnvTestCase_Native::testMount()
{
	// ����׼�����Ի�����d:\\pfstest\\nativeĿ¼��֤����
	bool b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	CPPUNIT_ASSERT( b);
	
	// ������豸mount��ͬһ��Ŀ��
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_UNEXIST, PFS::FST_NATIVE, PFS::MT_READONLY);
	CPPUNIT_ASSERT( !b && ( PFS::EC_DIR_HAS_ALREADY_MOUNT == PFS::CEnv::GetLastError()));
	
	b = PFS::CEnv::Unmount( RUN_NATIVE);

	// d:\\pfstest\\unexistĿ¼��֤������
	b = PFS::CEnv::Mount( RUN_UNEXIST, PATH_UNEXIST, PFS::FST_NATIVE, PFS::MT_READONLY);
	CPPUNIT_ASSERT( !b && ( PFS::EC_NATIVE_DEVICE_LOST == PFS::CEnv::GetLastError()));

	// writable
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	CPPUNIT_ASSERT( b);

	// writeable��ʽmount�����ڵ�Ŀ¼
	//b = PFS::CEnv::Mount( RUN_UNEXIST, PATH_UNEXIST, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	//CPPUNIT_ASSERT( b);
	
	//b = PFS::CEnv::Unmount( RUN_UNEXIST);
	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testUnMount()
{
	// unmount�����ڵ�Ŀ¼
	bool b = PFS::CEnv::Unmount( RUN_NATIVE);
	CPPUNIT_ASSERT( !b && (PFS::EC_MOUNT_DIR_UNFOUND == PFS::CEnv::GetLastError()));

	// �������κβ���ֱ��unmount
	PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::Unmount( RUN_NATIVE);
	CPPUNIT_ASSERT( b);

	// ���ļ���ʱunmountʧ��
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	if( basefile)
	{
		b = PFS::CEnv::Unmount( RUN_NATIVE);
		CPPUNIT_ASSERT( !b && (PFS::EC_FS_BUSY_NOW == PFS::CEnv::GetLastError()));
		PFS::CEnv::CloseFile( basefile);
		basefile = NULL;

		b = PFS::CEnv::Unmount( RUN_NATIVE);
		CPPUNIT_ASSERT( b);
	}
	else
	{
		b = PFS::CEnv::Unmount( RUN_NATIVE);
		CPPUNIT_ASSERT( b);
	}

	// ����Ŀ¼��mountʱunmountʧ��
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	b = PFS::CEnv::Mount( RUN_NATIVE + CHILD, RUN_NATIVE + CHILD, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( b)
	{
		b = PFS::CEnv::Unmount( RUN_NATIVE);
		CPPUNIT_ASSERT( !b && (PFS::EC_FS_BUSY_NOW == PFS::CEnv::GetLastError()));
		b = PFS::CEnv::Unmount( RUN_NATIVE + CHILD);

		b = PFS::CEnv::Unmount( RUN_NATIVE);
		CPPUNIT_ASSERT( b);
	}
	else
	{
		b = PFS::CEnv::Unmount( RUN_NATIVE);
		CPPUNIT_ASSERT( b);
	}
}

void CPfsEnvTestCase_Native::testGetMetaInfos()
{
	// mount֮ǰ����
	PFS::CMetaInfoMap mim;
	bool b = PFS::CEnv::GetMetaInfos( RUN_NATIVE, mim);
	CPPUNIT_ASSERT( !b && (PFS::EC_MOUNT_DIR_UNFOUND == PFS::CEnv::GetLastError()));

	// ����ʹ��
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::GetMetaInfos( RUN_NATIVE, mim);
	CPPUNIT_ASSERT( b && (0 < mim.size()));

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testGetMetaInfosEx()
{
	bool b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::Mount( RUN_NATIVE + CHILD, RUN_NATIVE + CHILD, PFS::FST_NATIVE, PFS::MT_READONLY);
	PFS::CMetaInfoMap mim;
	b = PFS::CEnv::GetMetaInfosEx( RUN_NATIVE, mim);
	CPPUNIT_ASSERT( b && ( 2 <= mim.size()));

	mim.clear();
	b = PFS::CEnv::GetMetaInfosEx( RUN_NATIVE + CHILD, mim);
	CPPUNIT_ASSERT( b && ( 1 <= mim.size()));

	b = PFS::CEnv::Unmount( RUN_NATIVE + CHILD);
	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testCheckMetaInfo()
{
	// mount֮ǰ����
	bool b = PFS::CEnv::CheckMetaInfo( RUN_NATIVE);
	CPPUNIT_ASSERT( !b && (PFS::EC_MOUNT_DIR_UNFOUND == PFS::CEnv::GetLastError()));

	// ����ʹ��
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::CheckMetaInfo( RUN_NATIVE);

	PFS::CMetaInfoMap mim;
	b = PFS::CEnv::GetMetaInfos( RUN_NATIVE, mim);
	CPPUNIT_ASSERT( b && (0 < mim.size()));

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testGetFileMetaInfo()
{
	// mount֮ǰ����
	PFS::CMetaInfo mi;
	bool b = PFS::CEnv::GetFileMetaInfo( RUN_NATIVE + FILE_EXIST, mi);
	CPPUNIT_ASSERT( !b && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));

	// ����ʹ��
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::CheckMetaInfo( RUN_NATIVE);

	// �Ѵ����ļ�
	b = PFS::CEnv::GetFileMetaInfo( RUN_NATIVE + FILE_EXIST, mi);
	CPPUNIT_ASSERT( b && ( 0 < mi.GetFileSize()));

	// �������ļ�
	b = PFS::CEnv::GetFileMetaInfo( RUN_NATIVE + FILE_UNEXIST, mi);
	CPPUNIT_ASSERT( !b && ( PFS::EC_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testSetFileMetaInfo()
{
	PFS::CMetaInfo mi;
	mi.SetFileDownloadURI( L"http://www.locojoy.com");
	mi.SetFileHash( 123456789);
	mi.SetFilePathHash( 987654321);
	mi.SetFilePfsxAttributes( 123456789123456789);
	mi.SetFileSize( 100);
	mi.SetMethod( -1);

	// mount֮ǰ����SetFileMetaInfo
	bool b = PFS::CEnv::SetFileMetaInfo( RUN_NATIVE + FILE_EXIST, mi);
	CPPUNIT_ASSERT( !b && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));
	
	// ����ʹ��
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::SetFileMetaInfo( RUN_NATIVE + FILE_EXIST, mi);
	PFS::CMetaInfo mi2;
	b = PFS::CEnv::GetFileMetaInfo( RUN_NATIVE + FILE_EXIST, mi2);
	CPPUNIT_ASSERT( b && !(mi != mi2));

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testAddFileMetaInfo()
{
	PFS::CMetaInfo mi;
	mi.SetFileDownloadURI( L"http://www.locojoy.com");
	mi.SetFileHash( 123456789);
	mi.SetFilePathHash( 987654321);
	mi.SetFilePfsxAttributes( 123456789123456789);
	mi.SetFileSize( 100);
	mi.SetMethod( -1);

	// �Ѵ��ڵ��ļ�
	bool b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::AddFileMetaInfo( RUN_NATIVE + FILE_EXIST, mi);
	PFS::CMetaInfo mi2;
	b = PFS::CEnv::GetFileMetaInfo( RUN_NATIVE + FILE_EXIST, mi2);
	CPPUNIT_ASSERT( b && !(mi != mi2));

	// �����ڵ��ļ�
	b = PFS::CEnv::GetFileMetaInfo( RUN_NATIVE + FILE_UNEXIST, mi2);
	CPPUNIT_ASSERT( !b && (PFS::EC_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));
	b = PFS::CEnv::AddFileMetaInfo( RUN_NATIVE + FILE_UNEXIST, mi);
	b = PFS::CEnv::GetFileMetaInfo( RUN_NATIVE + FILE_UNEXIST, mi2);
	CPPUNIT_ASSERT( b && !(mi != mi2));

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testIsFileExisting()
{
	// �Ѵ����ļ�
	bool b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::IsFileExisting( RUN_NATIVE + FILE_EXIST);
	CPPUNIT_ASSERT( b);

	// �����ڵ��ļ�
	b = PFS::CEnv::IsFileExisting( RUN_NATIVE + FILE_UNEXIST);
	CPPUNIT_ASSERT( !b);

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

class CTestFileHashMethod : public PFS::CFileHashMethod
{
	virtual CFileHashMethod* Clone() const
	{
		return new CTestFileHashMethod();
	}
	virtual bool MakeFileHash( PFS::CBaseFile& file, PFS::FILE_DATA_HASH& digest) const
	{
		return true;
	}
};

void CPfsEnvTestCase_Native::testSetFileHashMethod()
{
	const PFS::CFileHashMethod& defaultmethod = PFS::CEnv::GetFileHashMethod();

	CTestFileHashMethod method;
	PFS::CEnv::SetFileHashMethod( method);
	const PFS::CFileHashMethod& curmethod = PFS::CEnv::GetFileHashMethod();

	CPPUNIT_ASSERT( typeid( method) == typeid( curmethod));

	PFS::CEnv::SetDefaultFileHashMethod();
	const PFS::CFileHashMethod& curmethod2 = PFS::CEnv::GetFileHashMethod();
	CPPUNIT_ASSERT( &defaultmethod == &curmethod2);
}

void CPfsEnvTestCase_Native::testGetFileHashMethod()
{
	// �� testSetFileHashMethod �в���
}

void CPfsEnvTestCase_Native::testSetDefaultFileHashMethod()
{
	// �� testSetFileHashMethod �в���
}

void CPfsEnvTestCase_Native::testOpenFile()
{
	// mount֮ǰʹ��
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_EXIST, PFS::FM_CREAT, PFS::FA_RDWR);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));

	// readonly �� mount
	bool b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( basefile);
	PFS::CEnv::CloseFile( basefile);

	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_EXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));
	
	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_UNEXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));

	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_UNEXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( !basefile && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_NATIVE);

	// writable �� mount
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( basefile);
	PFS::CEnv::CloseFile( basefile);

	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_EXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( basefile );
	PFS::CEnv::CloseFile( basefile);

	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_UNEXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	CPPUNIT_ASSERT( !basefile && ( ERROR_FILE_NOT_FOUND == PFS::CEnv::GetLastError()));

	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_UNEXIST, PFS::FM_CREAT, PFS::FA_WRONLY);
	CPPUNIT_ASSERT( basefile );
	PFS::CEnv::CloseFile( basefile);

	// ɾ�����ļ�������һ�β���
	PFS::CEnv::RemoveFile( RUN_NATIVE + FILE_UNEXIST);

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testCloseFile()
{
	// �� testUnMount �� testOpenFile �в���
}

void CPfsEnvTestCase_Native::testRemoveFile()
{
	// mount֮ǰʹ��
	bool b = PFS::CEnv::RemoveFile( RUN_NATIVE + FILE_EXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_FILE_NOT_MOUNT == PFS::CEnv::GetLastError()));

	// readonly �� mount
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	PFS::CBaseFile* basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_EXIST, PFS::FM_EXCL, PFS::FA_RDONLY);
	b = PFS::CEnv::RemoveFile( RUN_NATIVE + FILE_EXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	PFS::CEnv::CloseFile( basefile);
	b = PFS::CEnv::RemoveFile( RUN_NATIVE + FILE_EXIST);
	CPPUNIT_ASSERT( !b && (PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::Unmount( RUN_NATIVE);

	// writable �� mount
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	basefile = PFS::CEnv::OpenFile( RUN_NATIVE + FILE_UNEXIST, PFS::FM_CREAT, PFS::FA_RDWR);
	b = PFS::CEnv::RemoveFile( RUN_NATIVE + FILE_UNEXIST);
	CPPUNIT_ASSERT( !b && (ERROR_SHARING_VIOLATION == PFS::CEnv::GetLastError()));

	PFS::CEnv::CloseFile( basefile);
	b = PFS::CEnv::RemoveFile( RUN_NATIVE + FILE_UNEXIST);
	CPPUNIT_ASSERT( b );

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testFileCopy()
{
	// readonly �� mount
	bool b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::FileCopy( RUN_NATIVE + FILE_EXIST , RUN_NATIVE + FILE_UNEXIST, false);
	CPPUNIT_ASSERT( !b &&( PFS::EC_FS_READONLY == PFS::CEnv::GetLastError()));
	
	b = PFS::CEnv::Unmount( RUN_NATIVE);

	// writeable �� mount
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_WRITABLE);
	b = PFS::CEnv::FileCopy( RUN_NATIVE + FILE_EXIST , RUN_NATIVE + FILE_UNEXIST, false);
	CPPUNIT_ASSERT( b);

	b = PFS::CEnv::FileCopy( RUN_NATIVE + FILE_EXIST , RUN_NATIVE + FILE_UNEXIST, true);
	CPPUNIT_ASSERT( !b && ( PFS::EC_COPY_DST_EXISTING == PFS::CEnv::GetLastError()));

	b = PFS::CEnv::RemoveFile( RUN_NATIVE + FILE_UNEXIST);
	CPPUNIT_ASSERT( b );

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testNativeFileNameToPFSFileName()
{
	// mount֮ǰ����
	std::wstring in = PATH_NATIVE + FILE_EXIST;
	std::wstring out;
	bool b = PFS::CEnv::NativeFileNameToPFSFileName( in, out);
	CPPUNIT_ASSERT( !b && ( PFS::EC_MAP_UNFOUND == PFS::CEnv::GetLastError()));

	// ��������
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::NativeFileNameToPFSFileName( in, out);
	CPPUNIT_ASSERT( b && ( out == RUN_NATIVE + FILE_EXIST));

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

void CPfsEnvTestCase_Native::testPFSFileNameToNativeFileName()
{
	// mount֮ǰ����
	std::wstring in = RUN_NATIVE + FILE_EXIST;
	std::wstring out;
	bool b = PFS::CEnv::PFSFileNameToNativeFileName( in, out);
	CPPUNIT_ASSERT( !b && ( PFS::EC_MAP_UNFOUND == PFS::CEnv::GetLastError()));

	// ��������
	b = PFS::CEnv::Mount( RUN_NATIVE, PATH_NATIVE, PFS::FST_NATIVE, PFS::MT_READONLY);
	b = PFS::CEnv::PFSFileNameToNativeFileName( in, out);
	std::replace( out.begin(), out.end(), L'\\', L'/');

	CPPUNIT_ASSERT( b && ( out == PATH_NATIVE + FILE_EXIST));

	b = PFS::CEnv::Unmount( RUN_NATIVE);
}

class CTestOutLog : public PFS::COutLog
{
	virtual void VPrint( const wchar_t* info, va_list vl)
	{

	}
};

void CPfsEnvTestCase_Native::testSetOutLogInstance()
{
	CTestOutLog logger;
	PFS::CEnv::SetOutLogInstance( &logger);
	PFS::COutLog* curlogger = PFS::CEnv::GetOutLogInstance();
	CPPUNIT_ASSERT( &logger == curlogger);
	PFS::CEnv::SetOutLogInstance( NULL);
}

void CPfsEnvTestCase_Native::testGetOutLogInstance()
{
	// �� testSetOutLogInstance �в���
}