#pragma once
#include "..\engine\map\pbackground.h"



class CBackgroundInfoDlg : public CDialog
{
	DECLARE_DYNAMIC(CBackgroundInfoDlg)
public:
	
	enum { IDD = IDD_DLG_BACKGROUND_INFO };

public:
	virtual ~CBackgroundInfoDlg();
	CBackgroundInfoDlg(CWnd* pParent = NULL);   
	Nuclear::SubMap::PBackGoundInfo info;

private:
	bool CheckData(bool checkActiveRect);
	void ResetActiveRect();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedReset();
	afx_msg void OnBnClickedOk();
};
