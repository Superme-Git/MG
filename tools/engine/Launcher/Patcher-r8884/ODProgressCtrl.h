//////////////////////////////////////////////////////////////////////////
// 
// ODProgressCtrl.h - declaration of CODProgressCtrl
//
// CODProgressCtrl 是一个自绘的Progress Control(进度条控件)
// 
#pragma once


// CODProgressCtrl
enum AppearanceFlag
{
	AF_DEFAULT = 0,
	AF_SHOW_PERCENT = 0x1,
};

class CODProgressCtrl : public CProgressCtrl
{
	DECLARE_DYNAMIC(CODProgressCtrl)

public:
	CODProgressCtrl();
	virtual ~CODProgressCtrl();

	//
	// summary : 是否显示百分比
	//
	void SetShowPercent(BOOL bFlag);
	BOOL GetShowPercent();

	//
	// summary : 百分比数值
	//
	float GetPercent();

	//
	// summary : 设置外观位图资源
	//
	// parameters:
	//		rbmFR - 外观前景位图
	//		rbmBK - 外观背景位图
	BOOL SetAppearance(
		const CBitmap& rbmFR, const CRect& rBoxFR, 
		const CBitmap& rbmBK, const CRect& rBoxBK);

	//
	// summary : 设置外观位图资源
	//
	// parameters:
	//		rbmAll - 包含外观前景位图和背景位图
	//		rBoxFR - 指定前景位图的剪裁区
	//		rBoxBK - 指定背景位图的剪裁区
	BOOL SetAppearance(
		const CBitmap& rbmAll, 
		const CRect& rBoxFR, 
		const CRect& rBoxBK);

	//
	// 设置控件的悬浮提示信息
	//
	void SetTipInfo(LPCTSTR lpszTipInfo)
	{
		m_strTipInfo = lpszTipInfo;
	}

	//
	// 获取控件的悬浮提示信息
	//
	LPCTSTR GetTipInfo() const
	{
		return m_strTipInfo.GetString();
	}

	// 
	// 设置控件的文本颜色（比如百分比数字的颜色） 
	//
	void SetTextColor(COLORREF clrTextColor)
	{
		m_clrTextColor = clrTextColor;
	}
	
	COLORREF GetTextColor() const
	{
		return m_clrTextColor;
	}

public:
	virtual void PreSubclassWindow();

protected:
	CDC		m_dcMemWnd;
	CDC		m_dcMemFR;
	CDC		m_dcMemBK;
	CBitmap m_bmWnd;
	int		m_LastPixPosX;
	CRect	m_rcLastClient;
	BOOL	_CheckUpdate(CDC* pDC);
	DWORD	m_dwAppearanceFlag;
	CString m_strTipInfo;
	COLORREF m_clrTextColor;

protected:
	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnPaint();
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
};

inline BOOL CODProgressCtrl::GetShowPercent()
{
	return !!(m_dwAppearanceFlag & AF_SHOW_PERCENT);
}

inline void CODProgressCtrl::SetShowPercent(BOOL bFlag)
{
	if( bFlag )
		m_dwAppearanceFlag |= AF_SHOW_PERCENT;
	else
		m_dwAppearanceFlag &= ~AF_SHOW_PERCENT;
}
