#pragma once
#include "afxwin.h"


// CTextInputDlg �Ի���
int CALLBACK EnumFontFamProc(LPENUMLOGFONT lpelf,LPNEWTEXTMETRIC lpntm,DWORD nFontType,long lparam);

class CTextInputDlg : public CDialog
{
	DECLARE_DYNAMIC(CTextInputDlg)

public:
	CTextInputDlg(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CTextInputDlg();

// �Ի�������
	enum { IDD = IDD_DIALOG_TextInputDlg };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

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
