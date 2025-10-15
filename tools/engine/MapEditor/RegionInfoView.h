#pragma once
#include "afxwin.h"



// CRegionInfoView form view

class CRegionInfoView : public CFormView
{
	DECLARE_DYNCREATE(CRegionInfoView)

protected:
	CRegionInfoView();           // protected constructor used by dynamic creation
	virtual ~CRegionInfoView();

public:
	enum { IDD = IDD_VIEW_REGINFO };
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	float regleft;
public:
	float regtop;
protected:
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
public:
	float regwidth;
public:
	float regheight;
public:
	CComboBox sellayer;
public:
	virtual void OnInitialUpdate();
public:
	CListCtrl lstPicture;
public:
	afx_msg void OnCbnSelchangeCmbSellayer();
private:
	int m_curSelLayer;
	afx_msg void OnNMClickLstPicture(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnHdnItemchangedLstPicture(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnItemActivateLstPicture(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclickLstPicture(NMHDR *pNMHDR, LRESULT *pResult);
};


