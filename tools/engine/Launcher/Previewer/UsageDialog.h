#pragma once


// CUsageDialog dialog

class CUsageDialog : public CDialog
{
	DECLARE_DYNAMIC(CUsageDialog)

public:
	CUsageDialog(CWnd* pParent = NULL);   // standard constructor
	virtual ~CUsageDialog();

// Dialog Data
	enum { IDD = IDD_USAGE_DIALOG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
};
