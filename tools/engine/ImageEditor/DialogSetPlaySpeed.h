#pragma once


// CDialogSetPlaySpeed 对话框

class CDialogSetPlaySpeed : public CDialog
{
	DECLARE_DYNAMIC(CDialogSetPlaySpeed)

public:
	CDialogSetPlaySpeed(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CDialogSetPlaySpeed();

// 对话框数据
	enum { IDD = IDD_DIALOG_SETSPEED };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()

public:
	void SetFPS(int fps);
	int GetFPS();

private:
	int m_nFPS;
};
