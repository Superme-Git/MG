//////////////////////////////////////////////////////////////////////////
// 
// ODProgressCtrl.h - declaration of CODProgressCtrl
//
// CODProgressCtrl ��һ���Ի��Progress Control(�������ؼ�)
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
	// summary : �Ƿ���ʾ�ٷֱ�
	//
	void SetShowPercent(BOOL bFlag);
	BOOL GetShowPercent();

	//
	// summary : �ٷֱ���ֵ
	//
	float GetPercent();

	//
	// summary : �������λͼ��Դ
	//
	// parameters:
	//		rbmFR - ���ǰ��λͼ
	//		rbmBK - ��۱���λͼ
	BOOL SetAppearance(
		const CBitmap& rbmFR, const CRect& rBoxFR, 
		const CBitmap& rbmBK, const CRect& rBoxBK);

	//
	// summary : �������λͼ��Դ
	//
	// parameters:
	//		rbmAll - �������ǰ��λͼ�ͱ���λͼ
	//		rBoxFR - ָ��ǰ��λͼ�ļ�����
	//		rBoxBK - ָ������λͼ�ļ�����
	BOOL SetAppearance(
		const CBitmap& rbmAll, 
		const CRect& rBoxFR, 
		const CRect& rBoxBK);

	//
	// ���ÿؼ���������ʾ��Ϣ
	//
	void SetTipInfo(LPCTSTR lpszTipInfo)
	{
		m_strTipInfo = lpszTipInfo;
	}

	//
	// ��ȡ�ؼ���������ʾ��Ϣ
	//
	LPCTSTR GetTipInfo() const
	{
		return m_strTipInfo.GetString();
	}

	// 
	// ���ÿؼ����ı���ɫ������ٷֱ����ֵ���ɫ�� 
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
