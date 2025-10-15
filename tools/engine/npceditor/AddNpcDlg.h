#pragma once
#include "resource.h"




// CAddNpcDlg dialog
class SoundApp;

class CAddNpcDlg : public CDialog
{
	DECLARE_DYNAMIC(CAddNpcDlg)

public:
	CAddNpcDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CAddNpcDlg();
	BOOL OnInitDialog();
	void InitCombox();

// Dialog Data
	enum { IDD = IDD_DIALOG_ADDNPC };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	CComboBox* m_pCombox;
	SoundApp* m_pSoundApp;
	

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedAddnpc();
	afx_msg void OnBnClickedCancelAddnpc();
};
