#pragma once


// CDlgSwingCurveAdjust 对话框

#include "BezierControl.h"

class CDlgSwingCurveAdjust : public CDialog, public bezier::INotify
{
	DECLARE_DYNAMIC(CDlgSwingCurveAdjust)

public:
	CDlgSwingCurveAdjust(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CDlgSwingCurveAdjust();

// 对话框数据
	enum { IDD = IDD_DIALOG_SwingCurveAdjust };

public:
	bezier::SamplePointContanters m_SPCSamplePoints;
	bezier::TangentPointContanters m_TPCTangentPoints;

	void setCurveParamData(const bezier::SamplePointContanters& TPCTangentPoints, const bezier::TangentPointContanters& SPCSamplePoints);

	bool bInitDataFlag;

private:
	BOOL m_bSampleFlag;
	bezier::CBezierControl m_bezier;
	bool m_bReady;
	int m_nSampleCount;

	virtual void OnSamplesReady(bool bReady) { m_bReady = bReady; GetDlgItem(IDOK)->EnableWindow(bReady);}
	virtual void OnSampleCount(int sampleCount) { m_nSampleCount = sampleCount; }

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedCheckSamplepoint();
	afx_msg void OnBnClickedOk();
	afx_msg void OnPaint();
	int m_nSamplePointNum;
	afx_msg void OnEnChangeEditSamplepointnum();
};
