#pragma once


// CInput2Dlg dialog

class CInput2Dlg : public CDialog
{
	DECLARE_DYNAMIC(CInput2Dlg)
public:
	CString strInfo;
	CString strInfo1;
	CString strInfo2;
	CString strEdit1;
	CString strEdit2;
	CString strCaption;
	bool oneEdit;

public:
	CInput2Dlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CInput2Dlg();

// Dialog Data
	enum { IDD = IDD_2INPUTS_DIALOG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

public:
	virtual BOOL OnInitDialog();
};
