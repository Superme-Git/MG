#pragma once
#include <map>
#include <vector>
#include <algorithm>
#include <iostream>
#include <locale>
#include <sstream>
#include <fstream>
#include <assert.h>

#include "../../pfs/pfs.h"
#include "../../pfs/filesmetafile.h"
#include "../../pfs/setupmetafile.h"
#include "../../pfs/versionmetafile.h"

#define BRF_ROOT  L"/.brief"
#define RES_ROOT  L"/.cache"
#define RUN_ROOT  L"/.run"

class CUpdateMountManager
{
public:
	CUpdateMountManager() { m_bReadOnlyMount = true; m_bNeedUnmount = false; };
	~CUpdateMountManager();

	// ��һ������hEventʹ GlobalFunction.cpp ��������CUpdateManager
	bool MountPFS( const std::wstring& strRunDir, bool bReadOnly, 
		HANDLE hStopEvent, const PFSX::SetupMetaVector& setupMetas);

	// Ϊ������pfs meta����pfs������󽫻���CommitPFS�м���Unmount
	bool MountPFSForNewAdded( const std::wstring& strRunDir,
		HANDLE hStopEvent, const PFSX::SetupMetaVector& newSetupMetas);

	// �ύ�޸ĵ�����
	bool CommitPFS( HANDLE hStopEvent);

	bool UnmountPFS();

	// �õ���Դ�ļ���mount��
	PFSX::SetupMetaVector& GetSetupMetaVector()
	{
		return m_setupMetas;
	}

	static CUpdateMountManager& GetInstance()
	{
		static CUpdateMountManager _instance;
		return _instance;
	}

private:
	bool _MountForSetupMetas( const std::wstring& strRunDir, bool bReadOnly, 
		HANDLE hStopEvent, const PFSX::SetupMetaVector& setupMetas);

	// ���� mount Ŀ¼�� .file.meta �ļ��� hash ֵ
	PFS::FILE_DATA_HASH GetMetaFileHash( const std::wstring& mount);

private:
	bool m_bReadOnlyMount; // ��ֻ����ʽMount
	bool m_bNeedUnmount;
	PFSX::SetupMetaVector m_setupMetas;
};

class CDownloadFiles
{
protected:
	PFS::CMetaInfoMap m_mimDownload;
	PFS::CMetaInfoMap m_mimDownloaded;
	PFS::CMetaInfoMap m_mimFailed;

	std::wstring m_strDir;
public:
	CDownloadFiles(std::wstring strDir)
		: m_strDir(strDir)
	{
	}

	~CDownloadFiles()
	{
	}

	// ��������Briefϵͳ�е��ļ�
	// ������ʧ�ܵ���Ŀ����10��ʱ�������� false.
	// ����10������ʹʧ����Ŀ��Ϊ0, Ҳ������ true.
	bool Download( HANDLE hBreakEvent );

	PFS::CMetaInfoMap& GetDownloaded()
	{
		return m_mimDownloaded;
	}

	PFS::CMetaInfoMap& GetFailed()
	{
		return m_mimFailed;
	}

protected:
	bool DownloadOneFile( const std::wstring& fileName );
};

class CSetupMetasDownloader
{
private:
	typedef PFSX::SetupMetaVector SetupMetaVector;

	SetupMetaVector		m_smVector;
	std::wstring		m_strBaseUrl;
	std::wstring		m_strBaseDir;

public:
	CSetupMetasDownloader(
		const SetupMetaVector&	smVector,
		std::wstring strBaseUrl,
		std::wstring strBaseDir)
		: m_smVector( smVector )
		, m_strBaseUrl(strBaseUrl)
		, m_strBaseDir(strBaseDir)
	{
	}

	// ��������Briefϵͳ�е��ļ�
	// ������ʧ�ܵ���Ŀ����10��ʱ�������� false.
	// ����10������ʹʧ����Ŀ��Ϊ0, Ҳ������ true.
	bool Download( HANDLE hBreakEvent );
};

extern bool DetectBreakEvent( HANDLE hEvent);

// ��CheckFileEx��ʽУ��strDirĿ¼�е��ļ�
extern bool CheckFilesInFolder(const std::wstring& strDir,
							   PFS::CMetaInfoMap& rMimCache,// ��ҪУ�����������
							   HANDLE hStopEvent );

// ��strInputDirָ����Ŀ¼�е��ļ���ʹ��Translate��ʽ�򿪲����浽strOutputDirָ����Ŀ¼��
extern bool TranslateFilesInFolder(
							const std::wstring& strInputDir,
							const std::wstring& strOutputDir,
							PFS::CMetaInfoMap& rMimMerge, // ��Ҫ�ϲ�����������
							PFS::CMetaInfoMap& rMimUpdate,// ͳ�Ƴɹ��ϲ�������
							HANDLE hStopEvent,
							bool bPatch);

extern bool TranslateFilesInFolderEn(const std::wstring& strInputDir,
							  const std::wstring& strOutputDir,
							  PFS::CMetaInfoMap& rMimMerge, // ��Ҫ�ϲ�����������
							  PFS::CMetaInfoMap& rMimUpdate,// ͳ�Ƴɹ��ϲ�������
							  HANDLE hStopEvent );

extern bool TranslateFilesForLauncher(
							   const std::wstring&	strInputDir,
							   const std::wstring&	strOutputDir,
							   PFS::CMetaInfoMap&		rMimMerge, // ��Ҫ�ϲ�����������
							   PFS::CMetaInfoMap&		rMimUpdate,// ͳ�Ƴɹ��ϲ�������
							   HANDLE hStopEvent,
							   bool bPatch);

//
// summary:
//		�Ƚ�����MetaInfo�Ƿ�һ��
//		����ֻ�Ƚ��ļ���С��Hashֵ
//;
extern bool IsEqualMetaInfo(const PFS::CMetaInfo& rLeft,const PFS::CMetaInfo& rRight);

// 
// summary:
//		��Server�˵�MetaInfo��Ϣ���û������汾��MetaInfo���в��컯�ж�
//		����ȷ����Ҫ���µ��б�����
//
// parameters:
//		[in] rMinuend - ��ȫ����Ϣ��MetaInfo��Ϣ��Server�˻�Server��Local�Ĳ����������
//		[in] rSubtrahend - �û�������MetaInfo��Ϣ��Local��Cache�е���Ϣ����������
//		[out] rDifference - ������������Ҫ���µ�MetaInfo��Ϣ
//
extern void SubtractMetaInfoMap(
						 const PFS::CMetaInfoMap& rMinuend,
						 const PFS::CMetaInfoMap& rSubtrahend,
						 PFS::CMetaInfoMap&		rDifference );

// 
// summary:
//		����һ�汾��MetaInfo��Ϣ�͵�ǰ�汾��MetaInfo���бȽ�
//		����ȷ����ǰ�汾��Ҫɾ�����б�
//
// parameters:
//		[in] rLast - ��һ�汾��MetaInfo��Ϣ
//		[in] rCur  - ��ǰ�汾��MetaInfo��Ϣ
//		[out] rDelete - ������������Ҫɾ����MetaInfo��Ϣ
//
extern void SubtractMetaInfoMapEx( const PFS::CMetaInfoMap& rLast, 
								  const PFS::CMetaInfoMap& rCur,
								  PFS::CMetaInfoMap&	rDelete );

//
// ���MetaInfoMap�е��ļ����б�����
//
extern void DumpMetaInfoMap( const PFS::CMetaInfoMap& mim, std::wostream& oss );

// ��ָ��Ŀ¼�µ�MetaInfo�ļ�
extern bool ReadMetaInfoFile( const std::wstring& dir, PFS::CMetaInfoMap& mimFile );

extern bool ReadMetaInfoFile2( const std::wstring& dir, PFS::CMetaInfoMap& mimFile );

// ��MetaInfo�ļ�д��ָ��Ŀ¼��.files.meta�ļ��У��汾1.1(����1.0)
extern bool WriteMetaInfoFile( const std::wstring& dir, const PFS::CMetaInfoMap& mimFile );

// ��MetaInfo�ļ�д��ָ��Ŀ¼��.files2.meta�ļ��У��汾1.1
extern bool WriteMetaInfoFile2( const std::wstring& dir, const PFS::CMetaInfoMap& mimFile );

// ��urlָ����λ������ .files.meta �ļ�������ȡ���ݵ�rMetaInfoMap������
extern bool DownloadMetaInfoFile( const std::wstring& url, PFS::CMetaInfoMap& rMetaInfoMap );

// ��urlָ����λ������ .files2.meta �ļ�������ȡ���ݵ�rMetaInfoMap������
extern bool DownloadMetaInfoFile2( const std::wstring& url, PFS::CMetaInfoMap& rMetaInfoMap );

// ɾ��ָ����Ŀ¼�Լ�Ŀ¼�µ������ļ�
extern bool DeleteCacheFolder( std::wstring strFolder );

// ���������ļ���
// ��������ھʹ��������ھ���Ϊ����
extern bool CreateHiddenDirectory( const std::wstring& path);

// ��Ϸ�ͻ��˳�����������Mutex����(��ʱû��)
// static const WCHAR SZ_GAME_CLIENT_MUTEX_NAME[] = L"MHZX_PRODUCT_MUTEX.{471CA900-DBAD-4d9e-9AD2-5FDC89B9B52E}";

// ��װ���־ǰ׺
static const WCHAR SZ_INSTALLATION_NAME_PREFIX[] = L"MHZX_INSTALLATION_MUTEX.{471CA900-DBAD-4d9e-9AD2-5FDC89B9B52E}";

// �õ����ð汾���е�Mutex���� - ��ͬλ�õİ�װ�汾�����ֲ�ͬ
extern LPCWSTR GetInstallationMutexName();

// �ж�ָ����Mutex�����Ƿ��Ѵ���
extern bool NamedMutexExists( LPCWSTR szName );

// �����־�ļ���������,ɾ��ȫ����־
extern bool PurgeLogFile( LPCWSTR lpszFileName, ULONGLONG ullFileSizeLimit );

// �������ļ���Ϊ�ɼ�
// ��Ϊ�����ļ��ѿ�д��ʽ��ʱ�᷵�ش����5
extern bool ShowHiddenFile( const std::wstring& filename);

// ��urlָ����λ�ö�ȡ .setup.meta �ļ������ݱ����� metas
extern bool ReadRemoteSetupMeta( const std::wstring& url, PFSX::SetupMetaVector& metas);

// �� file ָ�����ļ���ȡmeta��Ϣ������ȡ���ݵ�rMetaInfoMap������
extern bool ReadRemoteFilesMetaFile( const std::wstring& metafile, PFS::CMetaInfoMap& rMetaInfoMap );

// ������ɾ�����ļ�
extern void DealUnDeleteFiles(PFS::CMetaInfoMap& rDelete, const PFSX::SetupMetaVector& smvServer);

// ��URL�л�ȡHostName����IP��ַ��
extern bool GetHostNameFromUrl( LPCWSTR lpszUrl, std::wstring& hostname );

extern void DumpSetupMeta(const wchar_t* prefix, std::wostream& wos, const PFSX::SetupMetaVector& setupmeta );

// ���ݷ��������߸��°���SetupMeta�ͱ��ص�SetupMeta������������Ŀ�ͷ�����Ŀ
// �Լ����в����޸���Ŀ�����������޸ģ�
//
// smvServer - ����������Patch�е�setupmeta�������Ϊ�գ��������㡣
// smvLocal - ���ؿͻ����е�setupmeta�����������޳�������Ŀ(smvUseless)��
// smvDifference - smvServer �� smvLocal��ȫ��������Ŀ��������Ŀ���޸���Ŀ����������������Ҫɾ������Ŀ��
// smvNewAdded - smvDifference�е�������Ŀ��
// smvUseless - smvLocal�е�ɾ����Ŀ����smvLocal���ڣ���smvServer�в����ڵ���Ŀ��
//
extern void AnalyzeSetupMetaUpdateInfo(const PFSX::SetupMetaVector& smvServer,
								PFSX::SetupMetaVector& smvLocal,
								PFSX::SetupMetaVector& smvDifference,
								PFSX::SetupMetaVector& smvNewAdded,
								PFSX::SetupMetaVector& smvUseless);

//
// ����rMetaInfoMap�е�FileSize2ͳ�Ƽ��������ļ��Ĵ�С�ܺ͡�
//
extern PFS::fsize_t SumMetaInfoFileSize2( const PFS::CMetaInfoMap& rMetaInfoMap );

//
// ����config.xml�����õ���غ��Ը��µ���Ϣ����rMetaInfoMap���ų���Ӧ�ļ�
//
extern void EraseMetaInfosByIgnoringConfig( PFS::CMetaInfoMap& rMetaInfoMap );

//
// ���ݱ��ذ汾��Ϣ����ȡ�������Զ����»򲹶����Զ�����ģʽ�����°汾��Ϣ��
//
extern bool GetOnlineServerVersionInfo( const PFSX::CVersionInfo& rLocalVersionInfo, 
								 PFSX::CVersionInfo& rServerVersionInfo );