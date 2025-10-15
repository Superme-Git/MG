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

	// ���ø��²�������Ϣ�б�����swap��
	virtual void SetPatchInfoVector( PFSX::CPatchInfoVector& rPatchInfos )
	{	
		m_patchInfos.swap(rPatchInfos);
	}
protected:
	int	m_currentPatchIndex; // ���²�������������¼��ǰ�ĸ���״̬
	PFSX::CPatchInfoVector m_patchInfos;	// ��������Ϣ�б��������Զ�����ʱ��Ҫ
	PFSX::CPatchMetaFile m_patchMetas;
};
