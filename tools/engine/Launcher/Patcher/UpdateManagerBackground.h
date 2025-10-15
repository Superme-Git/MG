#pragma once
#include "UpdateManager.h"

class CUpdateManagerBackground : public CUpdateManager
{
public:
	CUpdateManagerBackground()
		: CUpdateManager( UPDATE_MODE_BACKGROUND )
	{
	}

	virtual bool Purge() { return true; }	// Do nothing!
	virtual bool Merge() { return true; } // Do nothing!
	virtual bool Commit() { return true; } // Do nothing!
	virtual bool Destroy( bool bSucceeded );

	virtual bool Initialize( LPCTSTR szRunDir);
	virtual bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache );
	virtual bool CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete );

	virtual bool BeginLoop( );	
	virtual bool HasNextLoop();

	// 设置更新补丁包信息列表（允许swap）
	virtual void SetPatchInfoVector( PFSX::CPatchInfoVector& rPatchInfos )
	{	
		m_patchInfos.swap(rPatchInfos);
	}
protected:
	int	m_currentPatchIndex; // 更新补丁包索引，记录当前的更新状态
	PFSX::CPatchInfoVector m_patchInfos;	// 补丁包信息列表，补丁包自动更新时需要
	PFSX::CPatchMetaFile m_patchMetas;
};
