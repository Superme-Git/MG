#pragma once
#include "resource.h"

// CMonsterPanel 对话框
class CMainDlg;

class CMonsterPanel : public CDialog
{
	DECLARE_DYNAMIC(CMonsterPanel)

public:
	CMonsterPanel(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CMonsterPanel();

	void CreateMonCombo();
	CMainDlg* m_pMainDlg;

	int mMonsterKind;
	int mMonsterIndex;
	
	void GetMonsterKind(int &MonKind);
	void GetMonsterIndex(int &MonIndex);
	
	int  GetSelectMonster();
	void SetMonsterKind(int MonKind);
// 对话框数据
	enum { IDD = IDD_MONSTER_PANEL };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持
	virtual BOOL OnInitDialog();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedMonOk();
};
