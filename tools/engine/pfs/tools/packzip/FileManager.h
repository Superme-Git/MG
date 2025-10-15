#pragma once
#include <string>
#include "ZipTreeItem.h"
#include <assert.h>
#include "basethread.h"
#include "../../src/common.h"
#include "../../pfsx.h"
#include "Project.h"

class CFileManager
{
public:
	CFileManager(void);
	virtual ~CFileManager(void);

	bool IsItemDefaultLevel( CZipTreeItem& item, bool bIsResource);

	// 得到指定目录下子结点
	//virtual CZipTreeItem* GetChild(const std::wstring& path);

	// 保存到zip文件
	bool SaveToZip( const std::wstring& file, bool bResource);

public:
	// 得到根结点
	CZipTreeItem& GetRootItem() { return m_root; }

	static const std::wstring& GetSrcName()
	{
		static std::wstring src = L"/src";
		return src;
	}

	//void MakeFileTree( const std::wstring& filename);

protected:
	CZipTreeItem			m_root;

	CProject*				m_project;
};


