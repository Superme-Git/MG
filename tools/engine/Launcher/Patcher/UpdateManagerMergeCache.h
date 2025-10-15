#pragma once
#include "UpdateManager.h"

class CUpdateManagerMergeCache : public CUpdateManager
{
public:
	CUpdateManagerMergeCache(void)
		: CUpdateManager( UPDATE_MODE_MERGECACHE )
	{
	}

	bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache );
	bool Initialize( LPCTSTR szRunDir);
	bool CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete );
	bool Download( ) {	return true; } // 不需要下载任何文件。
};
