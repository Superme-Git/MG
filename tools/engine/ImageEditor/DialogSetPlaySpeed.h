#pragma once


// CDialogSetPlaySpeed �Ի���

class CDialogSetPlaySpeed : public CDialog
{
	DECLARE_DYNAMIC(CDialogSetPlaySpeed)

public:
	CDialogSetPlaySpeed(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CDialogSetPlaySpeed();

// �Ի�������
	enum { IDD = IDD_DIALOG_SETSPEED };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()

public:
	void SetFPS(int fps);
	int GetFPS();

private:
	int m_nFPS;
};
