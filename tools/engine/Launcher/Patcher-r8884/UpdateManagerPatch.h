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
	virtual bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch );

	virtual bool Initialize( LPCTSTR szRunDir);

	virtual bool CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete );

	virtual bool Download( );

	virtual bool Verify( );

	virtual bool Destroy( bool bSucceeded );

	// �Ƿ�ʹ�ò�����
	virtual bool IsUsePatch()
	{
		return true;
	}
};
