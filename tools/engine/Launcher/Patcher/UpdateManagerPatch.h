#pragma once
#include "UpdateManager.h"

class CUpdateManagerPatch : public CUpdateManager
{
public:
	CUpdateManagerPatch()
		: CUpdateManager( UPDATE_MODE_PATCH )
	{
	}

	// �������໯��ģʽ
	CUpdateManagerPatch(UPDATE_MODE updateMode)
		: CUpdateManager( updateMode )
	{
	}

public:
	virtual bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache );

	virtual bool Initialize( LPCTSTR szRunDir);
	
	virtual bool BeginLoop( );

	virtual bool CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete );

	virtual bool Download( ) { return true; }

	virtual bool Verify( ) { return true; }

	virtual void EndLoop( bool bLoopSucceeded );

	// �Ƿ�ʹ�ò�����
	virtual bool IsUsePatch()
	{
		return true;
	}
};
