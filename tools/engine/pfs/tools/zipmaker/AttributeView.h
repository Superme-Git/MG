#pragma once
#include "LeftViewListener.h"
#include "afxwin.h"

#define	WM_EDITKILLFOCUS	(WM_USER+101)
class CKillFocusEdit : public CEdit
{
protected:
	DECLARE_MESSAGE_MAP()

	afx_msg void OnKillFocus( CWnd* pNewWnd);
};

// CAttributeView form view
class CAttributeView : public CFormView, public CLeftViewListener
{
	DECLARE_DYNCREATE(CAttributeView)

protected:
	CAttributeView();           // protected constructor used by dynamic creation
	virtual ~CAttributeView();

public:
	enum { IDD = IDD_FILEATTRIBUTE };
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	
public:
	virtual void OnInitialUpdate();
	afx_msg void OnCbnSelchangeCombo1();
	HRESULT OnEditKillFocus( WPARAM wParam, LPARAM lParam);

	// 设置文件数据
	virtual void SetZipTreeItem( CZipTreeItem* item);
	virtual void SetIsResource( bool bIsResource);

public:
	CComboBox			m_cbZipLevel;
	bool				m_bIsResource;
	CKillFocusEdit		m_url;
	
};


