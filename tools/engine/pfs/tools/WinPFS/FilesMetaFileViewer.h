#pragma once
#include "listitemsfileviewer.h"
#include <vector>
#include "../../pfs2.h"

// ������ʾMeta��Ϣ�б���ļ���ͼ
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

	PFS::CMetaInfoMap			m_mim;	// Meta ��Ϣ�б�
	std::vector<CMetaInfoPtr>	m_cache; // ���������Ļ���������
	bool m_bValidMetaFile;

protected:
	afx_msg void OnLvnGetdispinfoList1(NMHDR *pNMHDR, LRESULT *pResult);
	DECLARE_MESSAGE_MAP()
};
