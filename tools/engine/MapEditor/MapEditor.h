
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       
#include "..\engine\common\pfsutil.h"
#include "..\engine\map\ppathmap.h"

namespace Nuclear
{
	class CFileIOManager;
}

union TILT_TYPE_ID
{
	unsigned int id;
	struct 
	{
		int type : 8;
		int tileType : 24;
	};
};

class CMapEditorApp : public CWinApp
{
public:
	~CMapEditorApp();
	CMapEditorApp();

public:
	afx_msg void OnAppAbout();
	virtual BOOL InitInstance();

	DECLARE_MESSAGE_MAP()

public:
	const std::map<unsigned int, bool>& GetTileType()
	{
		CheckTileType();
		return m_TileTypeMap;
	}

	Nuclear::CFileIOManager *GetFileIOManager()
	{
		return m_pFileIOManager;
	}

	inline Nuclear::SubMap::PPathMap *GetPathMap()
	{
		return m_pPathMap;
	}
	bool IsMergeImport() {return m_bIsMergeImport;}
	bool IsImport() {return m_bIsImport;}
	CString GetResPath() { 
		return m_ResPath; 
	}
	afx_msg void OnImport();
	bool static SelectExportFolder(CString &fullPath, CString &folderName, HWND hWnd, TCHAR *title = NULL, UINT flag = BIF_RETURNFSANCESTORS | BIF_NEWDIALOGSTYLE);

private:
	virtual int ExitInstance();
	void CheckTileType(Nuclear::TILE_SIZE_TYPE tileSize);
	void CheckTileType();
	std::map<unsigned int, bool> m_TileTypeMap;	
	Nuclear::PFSLog m_pLog;
	bool m_bIsInitTileType;
	bool m_bIsMergeImport;
	bool m_bIsImport;
	Nuclear::SubMap::PPathMap *m_pPathMap;
	Nuclear::CFileIOManager *m_pFileIOManager;
	CString m_ResPath;

public:	
	afx_msg void OnViewReplaceLinkedObject();
	afx_msg void OnShowAllObjectResource();
	afx_msg void OnMergeImport();
};

extern CMapEditorApp theApp;