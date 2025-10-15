#pragma once
#include "afxwin.h"

#include <string>
#include <vector>
#include "..\engine\isprite.h"



class CModelInfoDlg : public CDialog
{
	DECLARE_DYNAMIC(CModelInfoDlg)

public:
	enum { IDD = IDD_DLG_MODEL };
	virtual ~CModelInfoDlg();

	CModelInfoDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	virtual BOOL OnInitDialog();
	BOOL	m_DelArray[Nuclear::XPSC_MAXCOUNT];

	BOOL	m_bIsEffectArray[Nuclear::XPSC_MAXCOUNT];
	BOOL	m_bIsRideArray[Nuclear::XPSC_MAXCOUNT];
	CString m_LayerDesArray[Nuclear::XPSC_MAXCOUNT];
	CString m_LayerNameArray[Nuclear::XPSC_MAXCOUNT];
	CComboBox m_cmbLayerCount;
	CString m_ModelDes;
	CString m_ModelName;
	std::vector<std::wstring> m_ModelNames;

	int m_LayerCount;
	bool m_bNew;

protected:
	virtual void OnOK();
};
