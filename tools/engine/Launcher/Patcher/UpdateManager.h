///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : UpdateManager.h
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	���¹�����
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-11 Created.
///=============================================================================
#pragma once

#include "../../pfs/src/common.h"
#include "../../pfs/versionmetafile.h"
#include "../../pfs/setupmetafile.h"
#include "../../pfs/patchmetafile.h"
#include "GlobalFunction.h"
#include "Profile.h"

//////////////////////////////////////////////////////////////////////////
//
// ����ĸ��¹���������ʵ��ȱʡ�����߼�
//
class CUpdateManager
{
public:
	/// <summary>
	///		�ж��Ƿ����°汾��Ҫ����
	/// </summary>
	/// <param name="szRunDir">
	/// 	[in]��������Ŀ¼
	/// </param>
	/// <param name="rHas">
	/// 	[out]������ֵΪtrueʱ��rHas��¼�Ƿ����°汾��Ҫ���¡�
	///		������ֵΪfalseʱ��rHas�����塣
	/// </param>
	/// <param name="rLocalValid">
	/// 	[out]rLocalValidΪtrueʱ�����ذ汾��Ч��
	///		Ϊfalseʱ�����ذ汾��Ч��
	/// </param>
	/// <param name="rAutoPatch">
	/// 	[out]rAutoPatchΪtrueʱ��ʹ���Զ����ز��������¡�
	///		Ϊfalseʱ����ͨ���·�ʽ��
	/// </param>
	/// <param name="rMergeCache">
	/// 	[out]rMergeCacheΪtrueʱ�������Ⱥϲ��Ѿ������ĺ�̨���¡�Ȼ�����¼���°汾��
	/// </param>
	/// <return type="bool">
	/// 	����°汾�Ķ����Ƿ�ɹ���ɡ�
	///		�����漰��������������������ʧ��ʱ������false�����
	///		�����޷���֪�Ƿ����°汾���ڡ�
	/// </return>
	/// <remarks>
	///		ע�⣺�����¹��̲�Ҫ��Initialize�������Ҫָ��runDir��UpdateMode
	/// </remarks>
	virtual bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache );

	/// <summary>
	/// 	��ʼ����Ϸ���¹�����
	/// </summary>
	/// <param name="szRunDir">
	/// 	[in]��������Ŀ¼
	/// </param>
	/// <return type="bool">
	/// 	��ʼ����Ϸ���¹������Ƿ�ɹ���
	/// </return>
	/// <remarks>
	///		���UpdateMode Ϊ UPDATE_MODE_FULL �� UPDATE_MODE_BACKGROUND��
	///	��ô����ʱ��ֻ��ȡ�����������ļ��б������бȶԣ�
	///	�������з������ļ�����ʱĿ¼��
	///	����FULLģʽ���滻�����ļ�������BACKGROUNDģʽ��������˳���
	/// ����ִ��Update()�������ϲ����̡�
	/// FULLģʽ�ɰ���ʵ�ֿͻ����㰲װ��
	/// </remarks>
	virtual bool Initialize( LPCTSTR szRunDir);
	
	/// <summary>
	/// 	���¹�����ѭ��ִ�и���ʱѭ��һ��ǰ�ĳ�ʼ��
	/// </summary>
	virtual bool BeginLoop( )
	{
		return true;
	}
	
	/// <summary>
	/// 	���¹�����ѭ��ִ�и���ʱѭ��һ�����Ĵ���
	/// </summary>
	virtual void EndLoop( bool bLoopSucceeded )
	{
	}

	/// <summary>
	/// 	���¹������Ƿ���Ҫѭ��ִ�и���
	/// </summary>
	/// <remarks>
	///		����һ��ĸ��¹�����������ѭ��ִ�У���ˣ�ѭ������Ϊ1��
	///		������AUTOPATCHģʽ�ĸ��¹���������Ҫ���ݲ�������Ŀ�����Ƿ���Ҫѭ�����¡�
	/// </remarks>
	/// <return type="bool">
	/// 	��ǰ������������Ϻ��Ƿ���ʣ�ಹ������Ҫ�������¡�
	/// </return>
	virtual bool HasNextLoop()
	{
		return false;
	}

	/// <summary>
	/// 	�����Ҫ���µ���Ŀ����
	/// </summary>
	/// <param name="rDownload">
	/// 	[out]��Ҫ���ص���Ŀ��
	/// </param>
	/// <param name="rMerge">
	/// 	[out]��Ҫ�ϲ�����Ŀ��(��ֵ��Ϊ����ֵ,��Ϊʵ������ʱ������ʧ�ܵ��ļ�)
	/// </param>
	/// <param name="rDelete">
	/// 	[out]��Ҫɾ������Ŀ��
	/// </param>
	/// <remarks>
	///		�������غ�, ���rDownloadΪ0������rMerge��Ϊ0,��ִ��Download����ִ��Merge.
	///		���ڳ��������˵, ���rDownloadΪ0������rMerge��Ϊ0,��Ҫִ��Merge.
	///		���ں�̨������˵, ��ִ�кϲ�����,���rMerge����Ϊ0,ִ��Merge����,Ҳ����ϲ��κ��ļ�.
	/// </remarks>
	/// <return type="bool">
	/// 	�����Ҫ���µ���Ŀ���������Ƿ�ɹ�
	/// </return>
	virtual bool CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete );

	/// <summary>
	/// 	���ظ��°�
	/// </summary>
	/// <remarks>
	///		ֻ��CheckDifference�е�rDownload��Ϊ0ʱ��Ҫִ�иö���.
	/// </remarks>
	/// <return type="bool">
	/// 	���ظ��°������Ƿ�ɹ�
	/// </return>
	virtual bool Download( );

	/// <summary>
	/// 	У�黺���ļ�
	/// </summary>
	/// <remarks>
	///		ֻ��m_mimCached��Ϊ��ʱִ�иù���.
	///		У��ʧ�ܵ��ļ�����ɾ��,����������.
	///		ȫ��У��ɹ����ļ���ͳ�Ƶ�rMerge��.
	/// </remarks>
	/// <return type="bool">
	/// 	У�黺���ļ������Ƿ�ɹ�
	/// </return>
	virtual bool Verify( );

	/// <summary>
	/// 	����ͻ����з������ļ�
	/// </summary>
	/// <remarks>
	///		ֻ��CheckDifference�е�rDelete��Ϊ0ʱ��Ҫִ�иö���.
	/// </remarks>
	/// <return type="bool">
	/// 	�Ƿ�ɹ�
	/// </return>
	virtual bool Purge( );

	/// <summary>
	/// 	�����ص��ĸ��°�������Ϸ�ͻ���
	/// </summary>
	/// <remarks>
	///		���ڳ��������˵, ���rDownloadΪ0������rMerge��Ϊ0,��Ҫִ��Merge.
	///		���ں�̨������˵, ��ִ�кϲ�����,���rMerge����Ϊ0,ִ��Merge����,Ҳ����ϲ��κ��ļ�.
	/// </remarks>
	/// <return type="bool">
	/// 	���¿ͻ��˹����Ƿ�ɹ�
	/// </return>
	virtual bool Merge( );

	/// <summary>
	/// 	�ύ���µ�����
	/// </summary>
	/// <remarks>
	///		��ֻ��Ҫ�ύ���ݶ�����unmount��ʱ��ʹ��
	/// </remarks>
	virtual bool Commit();

	/// <summary>
	/// 	�ͷŸ��¹������ڲ���Դ
	/// </summary>
	/// <remarks>
	///		�ù���ΪInitialize�Գƹ��̡�
	///		ֻҪInitialize()ִ�гɹ���һ��Ҫ��������Destory().
	///		����ᵼ����Դй¶�͸��¹��̲�������
	/// </remarks>
	virtual bool Destroy( bool bSucceeded );

	// �Ƿ�ʹ�ò�����
	virtual bool IsUsePatch()
	{
		return false;
	}

	void SetPatchPath( const std::wstring& patchpath)
	{
		m_patchpath = patchpath;
	}

	void SetPatchHash( const PFS::FILE_DATA_HASH& patchhash)
	{
		m_patchhash = patchhash;
	}

	void SetUpdateServerType( UPDATE_SERVER_TYPE type)
	{
		m_updateServerType = type;
	}

	// ���ø��²�������Ϣ�б�����swap��
	virtual void SetPatchInfoVector( PFSX::CPatchInfoVector& rPatchInfos )
	{
		// ֻ������CUpdateManagerAutoPatch��Ҫʵ��
	}

	/// <summary>
	/// 	��ȡ���ذ汾��Ϣ
	/// </summary>
	const PFSX::CVersionInfo& GetLocalVersion() const
	{
		return m_localVersion;
	}

	/// <summary>
	/// 	��ȡHasNewVersion()����ȡ�õķ������汾��Ϣ
	/// </summary>
	/// <remarks>
	///		�÷���û�мӶ��̱߳���,���Ա����ڸ����߳������
	/// </remarks>
	const PFSX::CVersionInfo& GetServerVersion() const
	{
		return m_newVersionInfo;
	}

	/// <summary>
	/// 	��������UpdateManager::HasNewVersion()����ȡ�õķ������汾��Ϣ����UpdateManager�С�
	///		�л�����ģʽʱ��Ҫ��
	/// </summary>
	void SetServerVersion(const PFSX::CVersionInfo& rNewVersionInfo)
	{
		m_newVersionInfo = rNewVersionInfo;
	}

	/// <summary>
	/// 	���⻯��������������ᵼ����������ݳ�Ա��й¶
	/// </summary>
	virtual ~CUpdateManager()
	{
	}

public:
	/// <summary>
	/// 	������ֹ�ź�
	/// </summary>
	static void TriggerBreakEvent();

	/// <summary>
	/// 	�����ֹ�ź�
	/// </summary>
	/// <return type="bool">
	/// 	��ֹ�ź�������ã�����true�����򷵻�false.
	/// </return>
	static bool DetectBreakEvent();

	/// <summary>
	/// 	������ɺ��Ƿ���Ҫ����
	/// </summary>
	/// <return type="bool">
	/// 	true - ��Ҫ����false - ����Ҫ����
	/// </return>
	static bool IsNeedReboot();
	
	// �����Ƿ���Ҫ����Launcher��Patcher��ص��ļ�
	static bool IsNeedUpdateLauncher( const PFS::CMetaInfoMap& mim);

protected:
	/// <summary>
	/// 	�����Ĺ��캯���������ⲿ����
	/// </summary>
	/// <param name="UpdateMode">
	/// 	[in]ȱʡ���� �� ȫ������������ �� ��̨����ģʽ
	/// </param>
	CUpdateManager(UPDATE_MODE updateMode);

	// ɾ�������ļ�
	bool DeleteLocalFiles();

	// ��ȡ�汾��Ϣ�ṩ�ķ�����url
	std::wstring GetVersionUrl(const PFSX::CVersionInfo& rVersionInfo, bool bUseConfigIfNeeded = false, bool bUseDefault = false ) const;
	
	bool IsBackground()
	{
		return m_UpdateMode == UPDATE_MODE_BACKGROUND;
	}

protected:
	std::wstring m_url;							// ����url
	std::wstring m_runDir;						// ��ϷĿ¼
	PFSX::CVersionInfo m_localVersion;			// ���ذ汾��Ϣ
	PFSX::CVersionInfo m_newVersionInfo;		// ���°汾��Ϣ
	std::wstring m_cacheDir;					// ��ʱĿ¼
	//std::wstring m_p2spDir;						// p2sp����Ŀ¼
	PFS::CMetaInfoMap m_mimDownload;			// ��Ҫ���ص���Ŀ
	PFS::CMetaInfoMap m_mimDownloadFailed;		// ����ʧ�ܵ���Ŀ
	PFS::CMetaInfoMap m_mimCached;				// ���������е��ļ�
	PFS::CMetaInfoMap m_mimUpdate;				// �ɹ��ϲ����ļ�
	PFS::CMetaInfoMap m_mimDelete;				// ������Ҫɾ�����ļ�

	PFSX::SetupMetaVector m_smvLocal;			// ���� SetupMeta ��Ϣ
	PFSX::SetupMetaVector m_smvServer;			// ������ SetupMeta ��Ϣ

	static bool s_bNeedReboot;	// ���³�����Ҫ����Ȼ���������(���統Launcher������Ҹ���ʱ)

protected:
	UPDATE_MODE			m_UpdateMode;	// ����ģʽ
	std::wstring		m_patchpath;		// ������·��
	std::wstring		m_patchmd5;			// ������md5ֵ
	PFS::FILE_DATA_HASH m_patchhash;		// ������hashֵ���������Զ�����ʱ��Ҫ
	UPDATE_SERVER_TYPE	m_updateServerType;	// ���·���������

protected:
	volatile static HANDLE s_hBreakEvent;	// ��ֹ�ź�
};

/// <summary>
/// 	��ȡ���¹������ĵ���ʵ��
/// </summary>
/// <param name="eUpdateMode">
/// 	[in]ȱʡ���� �� ȫ������������ �� ��̨����ģʽ
/// </param>
/// <return type="CUpdateManager &">
/// 	���¹������ĵ���ʵ��
/// </return>
extern CUpdateManager& GetUpdateManager(UPDATE_MODE eUpdateMode);

extern LPCWSTR _UpdateModeName( int mode );
