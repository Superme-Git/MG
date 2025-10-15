#pragma once
#include "afxcmn.h"
#include "ShowPartLoader.h"

// CValidateFileDlg dialog

class CValidateFileDlg : public CDialog
{
	DECLARE_DYNAMIC(CValidateFileDlg)

public:
	CValidateFileDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CValidateFileDlg();

// Dialog Data
	enum { IDD = IDD_DIALOG_VALIDATE };

	void SetShowPartLoaderPtr( CShowPartLoader* loader)
	{
		m_loader = loader;
	}

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

	// Ë¢ÐÂÎÄ¼þ×´Ì¬
	void RefreshFileState();

public:
	CListCtrl				m_lcFile;
	CShowPartLoader*		m_loader;
	virtual BOOL OnInitDialog();
protected:
	virtual void OnOK();
};
