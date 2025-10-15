#pragma once
#include "afxcmn.h"
#include <vector>

// CParameterAdjustDlg 对话框

class CParameterAdjustDlg : public CDialog
{
	DECLARE_DYNAMIC(CParameterAdjustDlg)

public:
	CParameterAdjustDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~CParameterAdjustDlg();

// 对话框数据
	enum { IDD = IDD_DIALOG_PARAMETERADJUST };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	void SetParameterPropertyVector(std::vector<PARAMETERPROPERTY> paramPropertyVector) { m_paramPropertyVector = paramPropertyVector; }

private:
	CTreeCtrl m_treeParameterList;
	CRect m_rctDrawParameterCurve; //绘制参数曲线的区域
	std::vector<PARAMETERPROPERTY> m_paramPropertyVector;
	int m_nCurSelParam;

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	afx_msg void OnNMClickTreeParameterlist(NMHDR *pNMHDR, LRESULT *pResult);
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
public:
	afx_msg void OnTvnSelchangedTreeParameterlist(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
};
