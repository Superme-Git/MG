#pragma once


// CZoomMoveParamDlg 对话框

class CZoomMoveParamDlg : public CDialog
{
	DECLARE_DYNAMIC(CZoomMoveParamDlg)

public:
	CZoomMoveParamDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CZoomMoveParamDlg();

// 对话框数据
	enum { IDD = IDD_DIALOG_ZOOMPARAM };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()

public:
	float GetZoomParam() { return m_fZoom; }
	float GetTimeParam() { return m_fTime; }
	BOOL GetEnableZoomFlag() { return m_bEnableZoomParam; }

	void SetLKOZoomFlag(bool bLKOEnableZoom) { m_bEnableZoomParam = bLKOEnableZoom; }
	void SetLKOZoom(float fLKOzoom) { m_fZoom = fLKOzoom; }
	void SetLKOTime(float fLKOTime) { m_fTime = fLKOTime; }

public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();

	afx_msg void OnEnKillFocusEdit(UINT nID);

private:
	float m_fZoom;
	float m_fTime;
	BOOL m_bEnableZoomParam;

public:
	afx_msg void OnBnClickedCheckZoomenable();
	virtual BOOL OnInitDialog();
};
