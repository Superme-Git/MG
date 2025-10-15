// ChildFrm.h : CChildFrame 类的接口
//

#pragma once


class CViewExSplitWnd : public CSplitterWnd
{
	DECLARE_DYNAMIC(CViewExSplitWnd)

	// Implementation
public:
	CViewExSplitWnd();
	~CViewExSplitWnd();
	CWnd* GetActivePane(int* pRow = NULL, int* pCol = NULL);
};

class CChildFrame : public CMDIChildWnd
{
	DECLARE_DYNCREATE(CChildFrame)
protected:
	CChildFrame();           // protected constructor used by dynamic creation
	virtual ~CChildFrame();

	CViewExSplitWnd m_wndSplitter1;
	CViewExSplitWnd m_wndSplitter2;

protected:
	virtual BOOL OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext);

	DECLARE_MESSAGE_MAP()
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
public:
	afx_msg void OnClose();

};
