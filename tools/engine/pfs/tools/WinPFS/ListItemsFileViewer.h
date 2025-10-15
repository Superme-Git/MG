#pragma once

#include "FileViewer.h"

// ������ʾ�б���Ŀ���ļ���ͼ
class CListItemsFileViewer : public CFileViewer
{
public:
	CListItemsFileViewer(void);
	~CListItemsFileViewer(void);

	virtual bool Create( CWnd* parent);
	virtual bool SetData( const void* pData, size_t len );
	virtual void Show( bool bShow);
	virtual void Move(){};

protected:
	CListCtrl m_list;	

protected:
	virtual bool InitColumns()
	{ return true; }

protected:
	// �༭����
	void SelectAll();
	void Copy();
	afx_msg void OnUpdateSelectAll(CCmdUI *pCmdUI);
	afx_msg void OnUpdateCopy(CCmdUI *pCmdUI);

	DECLARE_MESSAGE_MAP()
};
