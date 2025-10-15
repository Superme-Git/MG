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
	virtual bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch );
	virtual bool Initialize( LPCTSTR szRunDir);

	virtual bool BeginLoop( );
	virtual void EndLoop( bool bLoopSucceeded );

	virtual bool HasNextLoop();
	
	// ���ø��²�������Ϣ�б�����swap��
	virtual void SetPatchInfoVector( PFSX::CPatchInfoVector& rPatchInfos )
	{	
		m_patchInfos.swap(rPatchInfos);
	}

	virtual bool Destroy(  bool bSucceeded );

protected:
	int	m_currentPatchIndex; // �Զ����²�������������¼��ǰ�ĸ���״̬
	PFSX::CPatchInfoVector m_patchInfos;	// ��������Ϣ�б��������Զ�����ʱ��Ҫ
};