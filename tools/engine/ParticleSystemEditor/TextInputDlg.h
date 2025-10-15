#pragma once
#include "afxwin.h"


// CTextInputDlg 对话框
int CALLBACK EnumFontFamProc(LPENUMLOGFONT lpelf,LPNEWTEXTMETRIC lpntm,DWORD nFontType,long lparam);

class CTextInputDlg : public CDialog
{
	DECLARE_DYNAMIC(CTextInputDlg)

public:
	CTextInputDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CTextInputDlg();

// 对话框数据
	enum { IDD = IDD_DIALOG_TextInputDlg };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()

public:
	CString GetInputText() { return m_strInputText; }
	int GetFontHeight() { return m_nFontHeight; }
	int GetFontSpace() { return m_nFontSpace; }
	int GetFontAngle() { return m_nFontAngle; }
	CString GetFontName() { return m_strFontName; }

public:
	CComboBox m_comboFontName;

private:
	CString m_strInputText;
	int m_nFontHeight;
	int m_nFontSpace;
	int m_nFontAngle;

	CString m_strFontName;

private:
	void fillFontFaceName();

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	afx_msg void OnCbnSelchangeComboFontname();

public:
	virtual BOOL OnInitDialog();
};
