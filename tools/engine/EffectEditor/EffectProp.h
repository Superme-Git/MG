#pragma once


class CPropertyView;

class CEffectProp : public CDialog
{
	DECLARE_DYNAMIC(CEffectProp)
public:

	enum { IDD = IDD_EFFECT_PROP };

private:
	CPropertyView *m_pView;
	float m_fFPS;
	BOOL m_bBindType;

public:
	void ResetData();
	virtual ~CEffectProp();
	CEffectProp(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
protected:
	virtual void OnCancel();
	virtual void OnOK();

public:
	afx_msg void OnBnClickedCheckBindType();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	virtual BOOL OnInitDialog();
	afx_msg void OnEnKillfocusEditFps();

};
