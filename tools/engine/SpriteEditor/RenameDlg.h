#pragma once
#include "afxwin.h"




class CRenameDlg : public CDialog
{
	DECLARE_DYNAMIC(CRenameDlg)

public:
	enum { IDD = IDD_DLG_RENAME };
	virtual ~CRenameDlg();

	CRenameDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()

public:
	wchar_t m_Replace;
	wchar_t m_Pattern;
	int m_Pos;
	virtual BOOL OnInitDialog();

	CComboBox m_cmbDst;
	CComboBox m_cmbSrc;
	CComboBox m_cmbPos;

protected:
	virtual void OnOK();
};
