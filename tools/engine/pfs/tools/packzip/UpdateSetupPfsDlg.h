#pragma once
#include "afxwin.h"
#include "Operator.h"

class CUpdateSetupPfsOperator : public CNeedProjectOperator
{
	std::wstring	m_pfsFile;			// 原安装包路径
	std::wstring	m_updatePfsFile;	// 补丁包路径
	bool			m_bMakeMd5;			// 是否生成md5值
	bool			m_bChangeVersion;	// 是否修改版本信息

	virtual CZipTreeItem* Do();

	void Init();
	void Update();
	void UpdateVersionInfo();
	bool MakeMd5();

public:
	CUpdateSetupPfsOperator( CZipTreeItem& parent, CProject& project, const std::wstring& pfsFile, 
		const std::wstring& updatePfsFile, bool bMakeMd5, bool bChangeVersion)
	: CNeedProjectOperator( parent, project), m_pfsFile( pfsFile), 
		m_updatePfsFile( updatePfsFile), m_bMakeMd5( bMakeMd5), m_bChangeVersion( bChangeVersion) {}
};

// CUpdateSetupPfsDlg dialog

class CUpdateSetupPfsDlg : public CDialog
{
	DECLARE_DYNAMIC(CUpdateSetupPfsDlg)

public:
	CUpdateSetupPfsDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CUpdateSetupPfsDlg();

// Dialog Data
	enum { IDD = IDD_UPDATESETUPPFS };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	CEdit m_editPfsFile;
	CButton m_btnOpenPfs;
	CEdit m_editUpdatePfsFile;
	CButton m_btnOpenUpdatePfs;
	CButton m_checkMakeMd5;

	std::wstring	m_lastPfsFile;
	std::wstring	m_lastUpdatePfsFile;

protected:
	virtual void OnOK();
public:
	afx_msg void OnBnClickedButtonOpenpfs();
	afx_msg void OnBnClickedButtonOpenupdatepfs();
};
