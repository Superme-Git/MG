// LeftView.h : CLeftView 类的接口
//


#pragma once

#include "ProgressDlg.h"

class CWinPFSDoc;
class CWinPFSView;

class CLeftView : public CTreeView
{
protected: // 仅从序列化创建
	CLeftView();
	DECLARE_DYNCREATE(CLeftView)

// 属性
public:
	CWinPFSDoc*		GetDocument()
	{ return reinterpret_cast<CWinPFSDoc*>(m_pDocument); }

	CWinPFSView*	m_pPFSView;

// 操作
public:

// 重写
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

protected:
	virtual void OnInitialUpdate(); // 构造后第一次调用

	void InsertFolder( LPCTSTR parent, HTREEITEM hParent);

	HTREEITEM InsertOneItem( LPCTSTR item, HTREEITEM hParent);

	LPCTSTR GetPathNameFromTreeItem( HTREEITEM item);

// 实现
public:
	virtual ~CLeftView();
	// 选择子item
	void SelectChildItem( LPCTSTR childName);

protected:
	CProgressDlg			m_dlgProgress;

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnTvnItemexpanding(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTvnSelchanged(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTvnBegindrag(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclick(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnContextMenu(CWnd* /*pWnd*/, CPoint /*point*/);
	afx_msg void OnMenuInflate();
	afx_msg void OnMenuViewFile();
};
