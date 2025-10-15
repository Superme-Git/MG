///=============================================================================
/// Product   : MHZX
/// Project   : Patcher
/// Module    : UpdateManagerRepair.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	ǿ��ʹ�ò��������¿ͻ���
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-9-21 Created.
///=============================================================================
#pragma once
#include "UpdateManager.h"

class CUpdateManagerRepair : public CUpdateManager
{
public:
	CUpdateManagerRepair(void)
		: CUpdateManager( UPDATE_MODE_REPAIR )
	{
	}

public:
	virtual bool HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch );

	virtual bool CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete );

	virtual bool Merge( );

	virtual bool Download( );

	virtual bool Verify( );

	virtual bool Destroy( bool bSucceeded );
};
