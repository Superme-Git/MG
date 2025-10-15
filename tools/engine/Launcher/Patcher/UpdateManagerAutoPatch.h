#pragma once
#include "UpdateManagerPatch.h"
#include "../../pfs/patchmetafile.h"

class CUpdateManagerAutoPatch : public CUpdateManagerPatch
{
public:
	CUpdateManagerAutoPatch()
		: CUpdateManagerPatch(UPDATE_MODE_AUTOPATCH)
		, m_currentPatchIndex(0)
	{
	}

public:
	virtual bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache  );
	virtual bool Initialize( LPCTSTR szRunDir);

	virtual bool BeginLoop( );	
	virtual bool HasNextLoop();
	
	virtual bool Destroy( bool bSucceeded );

	// 设置更新补丁包信息列表（允许swap）
	virtual void SetPatchInfoVector( PFSX::CPatchInfoVector& rPatchInfos )
	{	
		m_patchInfos.swap(rPatchInfos);
	}

protected:
	int	m_currentPatchIndex; // 自动更新补丁包索引，记录当前的更新状态
	PFSX::CPatchInfoVector m_patchInfos;	// 补丁包信息列表，补丁包自动更新时需要
	PFSX::CVersionInfo m_cacheVersion;			// 缓存版本信息

};