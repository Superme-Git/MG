#pragma once
#include "resource.h"

// CMonsterPanel �Ի���
class CMainDlg;

class CMonsterPanel : public CDialog
{
	DECLARE_DYNAMIC(CMonsterPanel)

public:
	CMonsterPanel(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CMonsterPanel();

	void CreateMonCombo();
	CMainDlg* m_pMainDlg;

	int mMonsterKind;
	int mMonsterIndex;
	
	void GetMonsterKind(int &MonKind);
	void GetMonsterIndex(int &MonIndex);
	
	int  GetSelectMonster();
	void SetMonsterKind(int MonKind);
// �Ի�������
	enum { IDD = IDD_MONSTER_PANEL };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��
	virtual BOOL OnInitDialog();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedMonOk();
};
