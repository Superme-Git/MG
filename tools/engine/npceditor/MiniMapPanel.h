#pragma once
#include "afxcmn.h"


// CMiniMapPanel dialog
class CPropView;

class CMiniMapPanel : public CDialog
{
	DECLARE_DYNAMIC(CMiniMapPanel)

public:
	// Dialog Data
	enum { IDD = IDD_MINIMAP_PANEL };

private:
	CPropView* m_pView;
	bool m_bIsInited;
	CSliderCtrl m_sliderScale;

public:
	CMiniMapPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~CMiniMapPanel();

private:
	void ResetSize();

public:
	void ResetCont();


protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	virtual BOOL OnInitDialog();
};
