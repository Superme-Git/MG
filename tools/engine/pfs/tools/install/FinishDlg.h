#pragma once
#include "afxwin.h"


// CFinishDlg dialog
class CFinishDlg : public CDialog
{
	DECLARE_DYNAMIC(CFinishDlg)

public:
	CFinishDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CFinishDlg();

// Dialog Data
	enum { IDD = IDD_FINISH };

	BOOL Create( CWnd* parent) { return CDialog::Create( IDD_FINISH, parent); }

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	virtual void OnCancel();
public:
	virtual BOOL OnInitDialog();
protected:
	virtual void OnOK();

	void OnCreateLink();
	void OnStartGame();
	void OnAutoInstall();
	BOOL HaveInstalled360SE(LPTSTR sePath , int maxcount);
	BOOL CheckTouchInstallation(char* version, int versionLength);
	bool CompareVersion(const char* src, const char* dest);

	CButton m_CreateLink;
	CButton m_StartGame;
	CButton m_AutoInstall;
	CButton m_AutoInstallTouch;
	CStatic m_finishstate;
};
