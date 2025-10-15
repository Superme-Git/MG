#pragma once
#include "UpdateManager.h"

class CUpdateManagerPatch : public CUpdateManager
{
public:
	CUpdateManagerPatch()
		: CUpdateManager( UPDATE_MODE_PATCH )
	{
	}

	// 允许子类化新模式
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

	// 是否使用补丁包
	virtual bool IsUsePatch()
	{
		return true;
	}
};
