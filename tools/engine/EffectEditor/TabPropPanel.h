#pragma once
#include "afxcmn.h"
#include "AniPicProp.h"
#include "ParticleProp.h"
#include "AudioProp.h"
#include "AniEffectProp.h"
#include "EffectProp.h"
#include "RoleProp.h"


class PropertyView;

class CTabPropPanel : public CDialog
{
	DECLARE_DYNAMIC(CTabPropPanel)
private:
public:

	enum { IDD = IDD_TAB_PROP };
private:
	CTabCtrl m_Tabs;
	bool m_bIsInited;

	CAniPicProp m_aniProp;
	CParticleProp m_particleProp;
	CAudioProp m_audioProp;
	CAniEffectProp m_aniEffectProp;
	CEffectProp m_effectProp;
	CRoleProp m_roleProp;

	CPropertyView* m_pView;
public:

private:
	void ResetSize();

public:
	void ResetCont();
	virtual ~CTabPropPanel();
	CTabPropPanel(CWnd* pParent = NULL);   

protected:
	virtual void OnCancel();
	virtual void OnOK();
	virtual void DoDataExchange(CDataExchange* pDX);    
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);	
	afx_msg void OnSize(UINT nType, int cx, int cy);
	virtual BOOL OnInitDialog();
	afx_msg void OnTcnSelchangeTab(NMHDR *pNMHDR, LRESULT *pResult);
};
