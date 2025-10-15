#include "stdafx.h"
#include "SystemFileIconList.h"

namespace SFIL
{

typedef BOOL (WINAPI * SH_GIL_PROC)(HIMAGELIST *phLarge, HIMAGELIST *phSmall);
typedef BOOL (WINAPI * FII_PROC)   (BOOL fFullInit);

CSystemFileIconList::CSystemFileIconList()
: m_hShell32( NULL)
, m_hilLarge( NULL)
, m_hilSmall( NULL)
, m_folderIndex( -1)
{
	Init();	
}

CSystemFileIconList::~CSystemFileIconList()
{
	if( m_hShell32)
	{
		::FreeLibrary( m_hShell32);
		m_hShell32 = NULL;
	}
}

void CSystemFileIconList::Init()
{
	m_hShell32 = LoadLibrary( L"shell32.dll");
	if( NULL == m_hShell32)
	{
		return;
	}
	FII_PROC m_pFileIconInit = (FII_PROC)GetProcAddress( m_hShell32, (LPCSTR)660);
	if( m_pFileIconInit)
	{
		m_pFileIconInit( TRUE);
	}
	
	SH_GIL_PROC m_pShell_GetImageLists = (SH_GIL_PROC)GetProcAddress( m_hShell32, (LPCSTR)71);
	if( m_pShell_GetImageLists)
	{
		m_pShell_GetImageLists( &m_hilLarge, &m_hilSmall);
	}
}

int CSystemFileIconList::GetIndex( LPCTSTR pszFile)
{
	SHFILEINFO shfi = {0};
	SHGetFileInfo( pszFile, 0 , &shfi, sizeof(SHFILEINFO), 
		SHGFI_SYSICONINDEX | SHGFI_SMALLICON | SHGFI_ICON | SHGFI_USEFILEATTRIBUTES);
	return shfi.iIcon;
}

int CSystemFileIconList::GetFolderIndex( LPCTSTR pszFile)
{
	if( -1 == m_folderIndex)
	{
		SHFILEINFO shfi = {0};
		SHGetFileInfo( pszFile, FILE_ATTRIBUTE_DIRECTORY, &shfi, sizeof(SHFILEINFO), 
			SHGFI_SYSICONINDEX | SHGFI_SMALLICON | SHGFI_ICON | SHGFI_USEFILEATTRIBUTES);
		m_folderIndex = shfi.iIcon;
	}
	return m_folderIndex;
}

CSystemFileIconList& CSystemFileIconList::GetInstance()
{
	static CSystemFileIconList s_instance;
	return s_instance;
}

};