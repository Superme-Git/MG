// FolderTree.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"
#include "FolderTree.h"

// CFolderTree

IMPLEMENT_DYNAMIC(CFolderTree, CTreeCtrl)

CFolderTree::CFolderTree()
{
}

CFolderTree::~CFolderTree()
{
}


BEGIN_MESSAGE_MAP(CFolderTree, CTreeCtrl)
	ON_NOTIFY_REFLECT(TVN_ITEMEXPANDINGA, &CFolderTree::OnTvnItemexpanding)
	ON_NOTIFY_REFLECT(TVN_ITEMEXPANDINGW, &CFolderTree::OnTvnItemexpanding)
END_MESSAGE_MAP()

// CFolderTree message handlers

void CFolderTree::GetDiskInfo()
{
	DWORD mask = GetLogicalDrives();
	static wchar_t szDrive[] = L"A:\\";
	DWORD bit = 1;
	while( mask)
	{
		if( mask & bit)
		{
			if( DRIVE_FIXED == GetDriveType( szDrive))
			{
				CDiskInfo info;
				wcscpy_s( info.szDisk, _countof(szDrive), szDrive);
				SHFILEINFO shinfo;
				SHGetFileInfo( info.szDisk, NULL, &shinfo, sizeof(shinfo), SHGFI_DISPLAYNAME);
				wcscpy_s( info.szName, MAX_PATH, shinfo.szDisplayName);

				ULARGE_INTEGER llFree, llTotal;
				GetDiskFreeSpaceEx( szDrive, &llFree, &llTotal, NULL);
				info.dwFreeSpace = static_cast<DWORD>( llFree.QuadPart /  1048576L);
				m_disks.push_back( info);
			}
		}
		szDrive[0]++;
		mask &= ~bit;
		bit <<= 1;
	}
}

void CFolderTree::PreSubclassWindow()
{
	// TODO: Add your specialized code here and/or call the base class
	CTreeCtrl::PreSubclassWindow();
	Init();
}

void CFolderTree::Init()
{
	m_images.Create( GetSystemMetrics(SM_CXSMICON), GetSystemMetrics(SM_CYSMICON), ILC_COLOR32|ILC_MASK, 0, 4);
	SetImageList( &m_images, TVSIL_NORMAL);

	//LPITEMIDLIST item;
	//SHGetSpecialFolderLocation( m_hWnd, CSIDL_DRIVES, &item);
	SHFILEINFO shinfo;
	SHGetFileInfo( L"::{20D04FE0-3AEA-1069-A2D8-08002B30309D}", NULL, &shinfo, sizeof(shinfo), 
		SHGFI_DISPLAYNAME|SHGFI_ICON|SHGFI_SMALLICON);
	HTREEITEM root = InsertItem( shinfo.szDisplayName, MYCOMPUTER, MYCOMPUTER, NULL);

	m_images.Add( shinfo.hIcon);
	SHGetFileInfo( L"C:\\", NULL, &shinfo, sizeof(shinfo), SHGFI_ICON|SHGFI_SMALLICON);
	m_images.Add( shinfo.hIcon);
	SHGetFileInfo( L"", FILE_ATTRIBUTE_DIRECTORY, &shinfo, sizeof(shinfo), SHGFI_ICON|SHGFI_SMALLICON);
	m_images.Add( shinfo.hIcon);

	GetDiskInfo();
	for( CDiskInfoVector::iterator itor = m_disks.begin(); itor != m_disks.end(); ++itor)
	{
		HTREEITEM hItem = InsertFolderItem( root, DISK, itor->szDisk, itor->szName);
		if( hItem)
		{
			SetItemData( hItem, (LPARAM)&(*itor));
		}
	}
	//Expand( root, TVE_EXPAND);
}

HTREEITEM CFolderTree::InsertFolderItem( HTREEITEM parent, int nIcon, LPCTSTR szFolder, LPTSTR szName)
{
	TV_INSERTSTRUCT tvins;                
	tvins.item.mask = TVIF_TEXT | TVIF_IMAGE | TVIF_SELECTEDIMAGE;
	tvins.item.pszText = szName;
	tvins.item.cchTextMax = MAX_PATH;
	if( IsHasChild( szFolder))
	{
		tvins.item.cChildren = 1;
		tvins.item.mask |= TVIF_CHILDREN;
	}
	tvins.item.iImage = nIcon;
	tvins.item.iSelectedImage = nIcon;
	tvins.hInsertAfter = NULL;
	tvins.hParent = parent;
	return InsertItem( &tvins);
}

bool CFolderTree::IsHasChild( LPCTSTR szFolder)
{
	CString strFolder = szFolder;
	if (strFolder.GetAt(strFolder.GetLength()-1) == L'\\' )
	{
		strFolder += "*.*";
	}
	else
	{
		strFolder += "\\*.*";
	}

	WIN32_FIND_DATAW wfd;
	HANDLE	hFind = FindFirstFile( strFolder, &wfd);
	if( INVALID_HANDLE_VALUE == hFind)
	{
		return false;
	}
	do
	{
		if( L'.' == wfd.cFileName[0])
			continue;
		if( IsValidFolder( wfd.dwFileAttributes))
		{
			FindClose( hFind);
			return true;
		}
	}while( FindNextFile( hFind, &wfd));
	FindClose( hFind);
	return false;
}

void CFolderTree::OnTvnItemexpanding(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here
	if ((pNMTreeView->itemNew.state & TVIS_EXPANDEDONCE))
		return;
	wchar_t szTemp[MAX_PATH] = {0};
	GetFolderFromTreeItem( szTemp, pNMTreeView->itemNew.hItem);
	if( szTemp[0])
		InsertFolderChildren( pNMTreeView->itemNew.hItem, szTemp);

	*pResult = 0;
}

void CFolderTree::GetFolderFromTreeItem( LPTSTR szFolder, HTREEITEM hItem)
{
	if( NULL == hItem)
		return;
	wchar_t szTemp[MAX_PATH] = {0};
	while( hItem != GetRootItem())
	{
		CDiskInfo* pInfo = reinterpret_cast<CDiskInfo*>( GetItemData( hItem));
		wcscpy_s( szTemp, MAX_PATH, szFolder);
		if( pInfo)
		{
			swprintf_s( szFolder, MAX_PATH, L"%s%s", pInfo->szDisk, szTemp);
			break;
		}
		else
		{
			CString str = GetItemText( hItem);
			swprintf_s( szFolder, MAX_PATH, L"%s\\%s", str, szTemp);
		}
		hItem = GetParentItem( hItem);
	}
}

void CFolderTree::InsertFolderChildren( HTREEITEM parent, LPCTSTR szFolder)
{
	wchar_t szTemp[MAX_PATH] = {0};
	swprintf_s( szTemp, L"%s*.*", szFolder);
	WIN32_FIND_DATAW wfd;
	HANDLE	hFind = FindFirstFile( szTemp, &wfd);
	if( INVALID_HANDLE_VALUE == hFind)
		return;
	do
	{
		if( L'.' == wfd.cFileName[0])
			continue;
		if( IsValidFolder( wfd.dwFileAttributes))
		{
			swprintf_s( szTemp, L"%s%s\\", szFolder, wfd.cFileName);
			InsertFolderItem( parent, FOLDER, szTemp, wfd.cFileName);
		}
	}while( FindNextFile( hFind, &wfd));
	FindClose( hFind);
}

DWORD CFolderTree::GetFreeSpaceOfItem( HTREEITEM hItem)
{
	if( NULL == hItem)
		return 0;
	while( hItem != GetRootItem())
	{
		CDiskInfo* pInfo = reinterpret_cast<CDiskInfo*>( GetItemData( hItem));
		if( pInfo)
			return pInfo->dwFreeSpace;
		hItem = GetParentItem( hItem);
	}
	return 0;
}

DWORD CFolderTree::GetFreeSpaceFromDiskChar( wchar_t disk)
{
	disk = ::towupper( disk);
	for( CDiskInfoVector::iterator itor = m_disks.begin(); itor != m_disks.end(); ++itor)
	{
		if( itor->szDisk[0] == disk)
			return itor->dwFreeSpace;
	}
	return 0;
}
