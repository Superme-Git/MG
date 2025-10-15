#pragma once
#include "BezierControl.h"
#include "EffectEditorDoc.h"
#include "afxwin.h"



class CDlgPropSingleCurve : public CDialog, public bezier::INotify
{
	DECLARE_DYNAMIC(CDlgPropSingleCurve)
public:


	enum { IDD = IDD_PROP_CURVE };
	struct ConstructorParam
	{
		CEffectEditorDoc* pDoc;
		Nuclear::AbstractEffectClip::KeyType kt;
		CString title;
		float fYdiff;
		float fMaxMaxY;
		float fMinMinY;
		float fDefaultY;
		bool bUseDefaultY;
		ConstructorParam() {
			bUseDefaultY = false;
		}
	};

private:
	bezier::CBezierControl m_bezier;
	CComboBox m_comboWorkMode;
	CButton m_ButtonOk;
	CButton m_ButtonCancel;
	CStatic m_StaticSampleCount;
	CEdit m_EditSampleCount;
	ConstructorParam m_Param;
	int m_nSampleCount;
public:


private:
	void ResetSize();

public:
	virtual void OnClick() 
	{
		m_StaticSampleCount.SetFocus();
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
	virtual ~CDlgPropSingleCurve();

	CDlgPropSingleCurve(const ConstructorParam& param);


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
