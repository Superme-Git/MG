#ifndef SMALL_MAP_DLG_H
#define SMALL_MAP_DLG_H
#pragma once



#include "..\engine\common\xptypes.h"
#include "..\engine\map\ppathmap.h"
#include "DisplayMapBase.h"
#include <map>
#include <set>

class CMapEditorView;
using Nuclear::Renderer;
using Nuclear::XDisplayMode;

class CSmallMapDlg : public CDialog, public CDisplayMapBase
{
	DECLARE_DYNAMIC(CSmallMapDlg)

private:
	void SetClientPos(CPoint point);
	void ResetDestAndSourceRects();
	void TransScrollPosition(int nBar, UINT nSBCode, UINT nPos);
	void GetDestRect();
	CMenu m_PopMenu;

	bool m_bIsPrintScreen;
	bool m_bLButtonDown;
	unsigned int m_nDestWidth;
	unsigned int m_nDestHeight;
	Nuclear::CRECT m_DestRect;
	CMapEditorView* m_pMapEditorView;

protected:
	virtual const Nuclear::XPColorBalanceParam* GetTimeEffectParam() const;
	virtual void UpdateGameTime(DWORD delta) {}
	virtual void ResetScrollPos() {}
	virtual HWND GetHWnd() const { return m_hWnd; }
	virtual unsigned int GetSelectedTransID() const { return 0; }
	virtual void GetWndClientRect(LPRECT lpRect) const { GetClientRect(lpRect); }
	virtual void ResetScrollBar();
	virtual bool drawSelectedRects(void);

public:
	enum { IDD = IDD_SMALL_MAP };
	virtual std::vector<Sprite*>* GetSprites() { return NULL; }
	void TryInvalidate(bool changeSize = false);

	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);
	virtual ~CSmallMapDlg();
	CSmallMapDlg(CWnd* pParent = NULL);   
	virtual CMapEditorDoc* GetDocument() const;


protected:
	afx_msg void OnExportSmallMap();
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnPopZoomOut();
	afx_msg void OnPopZoomIn();
	afx_msg void OnPopNonzoom();
	afx_msg void OnPopFullScreen();
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnContextMenu(CWnd* /*pWnd*/, CPoint /*point*/);
	afx_msg void OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnPaint();
	DECLARE_MESSAGE_MAP()
	virtual void OnOK();
	virtual void DoDataExchange(CDataExchange* pDX);    
};
#endif