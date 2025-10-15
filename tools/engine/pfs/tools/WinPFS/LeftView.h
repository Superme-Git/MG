// LeftView.h : CLeftView ��Ľӿ�
//


#pragma once

#include "ProgressDlg.h"

class CWinPFSDoc;
class CWinPFSView;

class CLeftView : public CTreeView
{
protected: // �������л�����
	CLeftView();
	DECLARE_DYNCREATE(CLeftView)

// ����
public:
	CWinPFSDoc*		GetDocument()
	{ return reinterpret_cast<CWinPFSDoc*>(m_pDocument); }

	CWinPFSView*	m_pPFSView;

// ����
public:

// ��д
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

protected:
	virtual void OnInitialUpdate(); // ������һ�ε���

	void InsertFolder( LPCTSTR parent, HTREEITEM hParent);

	HTREEITEM InsertOneItem( LPCTSTR item, HTREEITEM hParent);

	LPCTSTR GetPathNameFromTreeItem( HTREEITEM item);

// ʵ��
public:
	virtual ~CLeftView();
	// ѡ����item
	void SelectChildItem( LPCTSTR childName);

protected:
	CProgressDlg			m_dlgProgress;

// ���ɵ���Ϣӳ�亯��
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
