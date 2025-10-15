#pragma once
#include "BezierControl.h"
#include "EffectEditorDoc.h"
#include "afxwin.h"



class CDlgPropDoubleCurve : public CDialog, public bezier::INotify
{
	DECLARE_DYNAMIC(CDlgPropDoubleCurve)
public:


	enum { IDD = IDD_PROP_CURVE };
	struct ConstructorParam
	{
		CEffectEditorDoc* pDoc;
		Nuclear::AbstractEffectClip::KeyType kt;
		CString title;
		float fYdiff;
	};

private:
	ConstructorParam m_param;
	bezier::CBezierControl m_bezier;
	CComboBox m_comboWorkMode;
	CButton m_ButtonOk;
	CButton m_ButtonCancel;
	CString m_title;
	CStatic m_StaticSampleCount;
	CEdit m_EditSampleCount;
	int m_nSampleCount;

private:
	void ResetSize();

public:
	virtual void OnClick() 
	{
		m_StaticSampleCount.SetFocus();
		UpdateData(FALSE);
	}
	virtual void OnSampleCount(int sampleCount)
	{
		m_nSampleCount = sampleCount;
		UpdateData(FALSE);
	}

	virtual void OnSamplesReady(bool bReady)
	{
		m_ButtonOk.EnableWindow(bReady);
	}
	virtual ~CDlgPropDoubleCurve();

	CDlgPropDoubleCurve(const ConstructorParam& param);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnEnKillfocusEditSampleCount();
	afx_msg void OnBnClickedButtonReset();
	afx_msg void OnCbnSelendokComboWorkMode();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedOk();

};
