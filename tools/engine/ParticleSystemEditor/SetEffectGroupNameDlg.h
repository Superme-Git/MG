#pragma once


// CSetEffectGroupNameDlg �Ի���

class CSetEffectGroupNameDlg : public CDialog
{
	DECLARE_DYNAMIC(CSetEffectGroupNameDlg)

public:
	CSetEffectGroupNameDlg(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CSetEffectGroupNameDlg();

// �Ի�������
	enum { IDD = IDD_DIALOG_SETEFFECTGROUPNAME };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()

public:
	CString GetEffectGroupName() { return m_strEffectGroupName; }

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
private:
	CString m_strEffectGroupName;
};
