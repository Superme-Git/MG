#pragma once
#include "listitemsfileviewer.h"
#include <vector>
#include "../../pfs2.h"

// 用于显示Meta信息列表的文件视图
class CFilesMetaFileViewer : public CListItemsFileViewer
{
public:
	CFilesMetaFileViewer(void);
	~CFilesMetaFileViewer(void);

	virtual bool SetData( const void* pData, size_t len );

protected:
	virtual bool InitColumns();

protected:
	struct CMetaInfoPtr
	{
		LPCWSTR				m_name;
		PFS::CMetaInfo*		m_meta;
		CMetaInfoPtr() : m_name( NULL), m_meta( NULL) {}
	};

	PFS::CMetaInfoMap			m_mim;	// Meta 信息列表
	std::vector<CMetaInfoPtr>	m_cache; // 基于索引的缓冲区对象
	bool m_bValidMetaFile;

protected:
	afx_msg void OnLvnGetdispinfoList1(NMHDR *pNMHDR, LRESULT *pResult);
	DECLARE_MESSAGE_MAP()
};
