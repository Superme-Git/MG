#pragma once
#include "afxwin.h"




class CTransparentObjectEditorDlg : public CDialog
{
	DECLARE_DYNAMIC(CTransparentObjectEditorDlg)

public:
	enum { IDD = IDD_DLG_TRANSOBJ_EDITER };
	virtual ~CTransparentObjectEditorDlg();

	CTransparentObjectEditorDlg(CWnd* pParent = NULL);   

protected:
	CComboBox m_comboLayerPosition;
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	virtual BOOL OnInitDialog();

	int m_nHeight;

	int m_nWidth;
	int m_nTime;
	int m_nY;
	int m_nX;
	
};
