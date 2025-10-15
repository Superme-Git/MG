#pragma once




class CInputDialog : public CDialog
{
	DECLARE_DYNAMIC(CInputDialog)
public:
	float fInput;
public:
	virtual ~CInputDialog();
	CInputDialog(CWnd* pParent = NULL);   


	enum { IDD = IDD_DIALOG1 };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);  

	DECLARE_MESSAGE_MAP()

};
