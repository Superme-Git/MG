#pragma once




class CWaterAreaInfoDlg : public CDialog
{
	DECLARE_DYNAMIC(CWaterAreaInfoDlg)

public:
	enum { IDD = IDD_DLG_WATER_AREA_INFO };
	virtual ~CWaterAreaInfoDlg();

	CWaterAreaInfoDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	int m_Border;
	int m_Depth;
};
