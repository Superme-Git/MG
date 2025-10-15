// WinPFSView.h : CWinPFSView ��Ľӿ�
//
#pragma once
#include "FileCentralCache.h"
#include "ProgressDlg.h"

class CLeftView;
class CFileViewerFrame;

class CWinPFSView : public CListView
{
protected: // �������л�����
	CWinPFSView();
	DECLARE_DYNCREATE(CWinPFSView)

// ����
public:
	CWinPFSDoc* GetDocument() const
	{ return reinterpret_cast<CWinPFSDoc*>(m_pDocument); }
	CLeftView*				m_pLeftView;

// ����
public:
	void FillCache( LPCTSTR parent);

// ��д
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

protected:
	virtual void OnInitialUpdate(); // ������һ�ε���

// ʵ��
public:
	virtual ~CWinPFSView();

protected:
	// ��ָ���������Ӧ�Ĳ���(�鿴�ļ�����)
	BOOL ExecuteItem( int iItem );

	void InsertColumn();
	void SetItemCount();
	
	CFileCentralCache		m_cache;
	bool					m_bHex;
	int						m_sortType;
	bool					m_bSortAsc;
	CProgressDlg			m_dlgProgress;

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnStyleChanged(int nStyleType, LPSTYLESTRUCT lpStyleStruct);
	afx_msg void OnLvnGetdispinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnColumnclick(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnBegindrag(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclk(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMReturn(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclick(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnUpdateViewHexshow(CCmdUI *pCmdUI);
	afx_msg void OnViewHexshow();
	afx_msg void OnMenuInflate();
	afx_msg void OnMenuViewFile();
};
