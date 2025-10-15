#pragma once

namespace SFIL
{

// 得到系统文件图标列表
class CSystemFileIconList
{
	HMODULE			m_hShell32;
	HIMAGELIST		m_hilLarge;
	HIMAGELIST		m_hilSmall;
	int				m_folderIndex;

	CSystemFileIconList();
	~CSystemFileIconList();

	void Init();

public:
	
	static CSystemFileIconList& GetInstance();

	HIMAGELIST	GetLargeIconList()		{ return m_hilLarge; }
	HIMAGELIST	GetSmallIconList()		{ return m_hilSmall; }

	int GetIndex( LPCTSTR pszFile);
	int GetFolderIndex( LPCTSTR pszFile);
};

};		// namespace SFIL