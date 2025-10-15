#pragma once

#include "FileViewer.h"

// 用于显示列表项目的文件视图
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
	// 编辑命令
	void SelectAll();
	void Copy();
	afx_msg void OnUpdateSelectAll(CCmdUI *pCmdUI);
	afx_msg void OnUpdateCopy(CCmdUI *pCmdUI);

	DECLARE_MESSAGE_MAP()
};
