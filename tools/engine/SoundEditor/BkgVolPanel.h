#pragma once


// CBkgVolPanel dialog
class CPropView;

class CBkgVolPanel : public CDialog
{
	DECLARE_DYNAMIC(CBkgVolPanel)
public:
	// Dialog Data
	enum { IDD = IDD_BKGVOL_PANEL };

private:
	CPropView* m_pView;
	int m_nTransition;
	float m_fVolume;
	CEdit m_editL;
	CEdit m_editVol;

public:
	CBkgVolPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~CBkgVolPanel();

private:
	void ShowPolygonProp(BOOL b);
	
protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual void OnOK() {}
	virtual void OnCancel() {}

public:
	void ResetCont();

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnEnKillfocusEditL();
	afx_msg void OnEnKillfocusEdit1();
	afx_msg void OnBnClickedButtonDeleteCuts();
	afx_msg void OnBnClickedRadioPolygonRadio(UINT nID);
	virtual BOOL OnInitDialog();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);

};
