#pragma once


// CSetEffectGroupNameDlg 对话框

class CSetEffectGroupNameDlg : public CDialog
{
	DECLARE_DYNAMIC(CSetEffectGroupNameDlg)

public:
	CSetEffectGroupNameDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CSetEffectGroupNameDlg();

// 对话框数据
	enum { IDD = IDD_DIALOG_SETEFFECTGROUPNAME };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()

public:
	CString GetEffectGroupName() { return m_strEffectGroupName; }

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
private:
	CString m_strEffectGroupName;
};
