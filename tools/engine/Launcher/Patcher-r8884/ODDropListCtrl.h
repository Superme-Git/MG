#pragma once
#include "LayoutManager.h"

// CODDropListCtrl
#define WM_ITEMCHANGED (WM_USER+100)

class CODDropListCtrl : public CWnd
{
	DECLARE_DYNAMIC(CODDropListCtrl)

public:
	CODDropListCtrl();
	virtual ~CODDropListCtrl();

	BOOL Create(HWND hParent, const RECT& rect, UINT nID);

	int AddString( LPCTSTR lpszItem );

	int GetCount() const { return (int)m_items.GetCount(); }

	int GetCurSel() const { return m_curSel; }

	void SetCurSel( int sel) { m_curSel = sel; }

	void GetText( int nIndex, CString& rString ) const;

	void SizeToContent();

	void SetTextColor( DWORD clr) { m_clrText = clr; }

	CRect GetItemRect( int index);

	void SetMoveIndex( int index) { m_moveIndex = index; }

protected:
	DECLARE_MESSAGE_MAP()

	int HitTest( CPoint point);

public:
	afx_msg void OnPaint();
	afx_msg void OnKillFocus(CWnd* pNewWnd);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);

	CStringArray	m_items;
	int				m_curSel;			// ��ǰѡ������
	int				m_moveIndex;		// ��ǰ������ڵ�����

	CRect			m_rcFR;				// ����ͼƬ�ľ���
	CRect			m_rcBorder;			// ����Χ�ı߾�
	CLayoutManager	m_layoutManager;	// 
	int				m_nInterval;		// item֮��ľ���
	CRect			m_rcItem;

	CBitmap			m_bmpFR;			// ����ͼƬ HBITMAP
	CBitmap			m_bmpNormal;
	CRect			m_rcNormal;
	CRect			m_rcDown;

	bool			m_bHideByParent;

	DWORD			m_clrText;

	int				m_textLeftInterval;		// ������ʾ�����ƫ��
	bool			m_bMouseDown;

	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg LRESULT OnMouseLeave( WPARAM wParam, LPARAM lParam);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
};


