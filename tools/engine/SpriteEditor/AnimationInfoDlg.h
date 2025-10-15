#pragma once

#include "..\engine\sprite\spritemanager.h"
#include "afxwin.h"




class CAnimationInfoDlg : public CDialog
{

	DECLARE_DYNAMIC(CAnimationInfoDlg)

public:
	virtual ~CAnimationInfoDlg();
	CAnimationInfoDlg(Nuclear::SpriteManager *SprMan, const std::wstring &modelname, CWnd* pParent = NULL);   

private:
	std::vector<std::pair<int, std::wstring> > m_EffectLayers;

public:
	void GetAnimFileName(std::wstring &name);
	CString m_AnimationFilename;
	enum { IDD = IDD_DLG_ACTIONINFO };



	void InitNew();


	std::wstring m_ModelName;
	Nuclear::SpriteManager *m_pSpriteMgr;
	struct sLink
	{
		std::wstring m_Name;
		int m_Frame;
		sLink(const std::wstring &n = L"", int f = 0) : m_Name(n), m_Frame(f) { }
	};


protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	BOOL m_fDir0;
	BOOL m_fDir1;
	BOOL m_fDir2;
	BOOL m_fDir3;
	BOOL m_fDir4;
	BOOL m_fDir5;
	BOOL m_fDir6;
	BOOL m_fDir7;
	BOOL m_fLayer[Nuclear::XPSC_MAXCOUNT];

	int m_Time;
	int m_nFrame;
	CString m_Name;
	std::vector<sLink> m_Links;
	bool m_fIndAction;

protected:
	virtual void OnOK();

public:
	CComboBox m_ComboBoxEditEffectLayer;
	afx_msg void OnCbnSelchangeComboAnimationType();
	afx_msg void OnBnClickedBtnEdtindact();
	CComboBox m_ComboBoxAnimationType;
	virtual BOOL OnInitDialog();
	int m_EffectPoint;
	int m_DamagePoint;
	int m_Stride;
};
