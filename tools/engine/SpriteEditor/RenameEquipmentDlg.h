#pragma once
#include "afxwin.h"

#include "..\engine\sprite\spritemanager.h"

// CRenameEquipDlg dialog

class CRenameEquipmentDlg : public CDialog
{
	Nuclear::SpriteManager *m_pSprMan;
	std::wstring m_modelname;
	std::vector<std::vector<std::wstring> > m_enames;


	DECLARE_DYNAMIC(CRenameEquipmentDlg)

public:
	CRenameEquipmentDlg(Nuclear::SpriteManager* pSprMan, const std::wstring &modelname, CWnd* pParent = NULL);   // standard constructor
	virtual ~CRenameEquipmentDlg();

	std::wstring oldname;
	std::wstring newname;

// Dialog Data
	enum { IDD = IDD_DLG_RENAMEEQUIP };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	virtual void OnOK();
public:
	CComboBox m_cmbOldEName;
public:
	CString m_newEName;
public:
	virtual BOOL OnInitDialog();
};
