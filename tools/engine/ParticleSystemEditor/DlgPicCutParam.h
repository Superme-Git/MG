#pragma once


// CDlgPicCutParam �Ի���

class CDlgPicCutParam : public CDialog
{
	DECLARE_DYNAMIC(CDlgPicCutParam)

public:
	CDlgPicCutParam(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CDlgPicCutParam();

// �Ի�������
	enum { IDD = IDD_DIALOG_mnPicTestDlg };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()

public:
	int GetRow() { return m_nRow; }
	int GetCol() { return m_nCol; }
	CString GetPathDir() { return m_strFilePathDir; }

public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	int m_nRow;
	int m_nCol;
	CString m_strFilePathDir;
	afx_msg void OnBnClickedButtonSetfilepath();
	virtual BOOL OnInitDialog();

	afx_msg void OnEnChangeEdit(UINT nID);
};
