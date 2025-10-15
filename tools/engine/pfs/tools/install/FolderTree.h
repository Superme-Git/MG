#pragma once
// CFolderTree
#include <vector>

class CFolderTree : public CTreeCtrl
{
	struct CDiskInfo
	{
		DWORD		dwFreeSpace;
		wchar_t		szDisk[4];
		wchar_t		szName[MAX_PATH];
		CDiskInfo() : dwFreeSpace( 0) {}
	};

	typedef	std::vector<CDiskInfo>		CDiskInfoVector;
	CDiskInfoVector		m_disks;
	enum
	{
		MYCOMPUTER = 0,
		DISK = 1,
		FOLDER = 2,
	};
	CImageList			m_images;

	DECLARE_DYNAMIC(CFolderTree)

public:
	CFolderTree();
	virtual ~CFolderTree();
	
	void GetFolderFromTreeItem( LPTSTR szFolder, HTREEITEM hItem);

	DWORD GetFreeSpaceOfItem( HTREEITEM hItem);

	DWORD GetFreeSpaceFromDiskChar( wchar_t disk);

protected:
	DECLARE_MESSAGE_MAP()
	void GetDiskInfo();
	void Init();
	HTREEITEM InsertFolderItem( HTREEITEM parent, int nIcon, LPCTSTR szFolder, LPTSTR szName);
	bool IsHasChild( LPCTSTR szFolder);
	void InsertFolderChildren( HTREEITEM parent, LPCTSTR szFolder);
	static bool IsValidFolder( DWORD dwAttribute)
	{
		return !!(FILE_ATTRIBUTE_DIRECTORY & dwAttribute) && !(FILE_ATTRIBUTE_HIDDEN & dwAttribute);
	}

	virtual void PreSubclassWindow();
	afx_msg void OnTvnItemexpanding(NMHDR *pNMHDR, LRESULT *pResult);
};


