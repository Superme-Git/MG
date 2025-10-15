// WinPFSView.h : CWinPFSView 类的接口
//
#pragma once
#include "FileCentralCache.h"
#include "ProgressDlg.h"

class CLeftView;
class CFileViewerFrame;

class CWinPFSView : public CListView
{
protected: // 仅从序列化创建
	CWinPFSView();
	DECLARE_DYNCREATE(CWinPFSView)

// 属性
public:
	CWinPFSDoc* GetDocument() const
	{ return reinterpret_cast<CWinPFSDoc*>(m_pDocument); }
	CLeftView*				m_pLeftView;

// 操作
public:
	void FillCache( LPCTSTR parent);

// 重写
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);

protected:
	virtual void OnInitialUpdate(); // 构造后第一次调用

// 实现
public:
	virtual ~CWinPFSView();

protected:
	// 对指定项进行相应的操作(查看文件内容)
	BOOL ExecuteItem( int iItem );

	void InsertColumn();
	void SetItemCount();
	
	CFileCentralCache		m_cache;
	bool					m_bHex;
	int						m_sortType;
	bool					m_bSortAsc;
	CProgressDlg			m_dlgProgress;

// 生成的消息映射函数
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
