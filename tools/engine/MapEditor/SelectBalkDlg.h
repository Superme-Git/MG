#pragma once

#include "afxcmn.h"

#include "..\Common\cmndata.h"
#include "afxwin.h"

class CMapEditorView;

using Nuclear::Renderer;
using Nuclear::DisplayMode;

// CSelectBalkDlg dialog

class CSelectBalkDlg : public CDialog
{
	DECLARE_DYNAMIC(CSelectBalkDlg)

private:
	CMapEditorView* m_pMapEditorView;
	DisplayMode m_d3dMode;
	Renderer *m_pRenderer;
	DWORD m_color;
	unsigned int m_brushType;

	static unsigned char const PICTURE_WIDTH = 134;
	static unsigned char const PICTURE_HEIGHT = 52;

	void checkRenderer(void) {
		if( m_pRenderer == NULL ) 
		{
			CreateRenderer(&m_pRenderer, m_hWnd, PICTURE_WIDTH, PICTURE_HEIGHT);
			m_d3dMode = m_pRenderer->GetDisplayMode();
			RECT destRect, sourceRect;
			destRect.top = 11;
			destRect.left = 11;
			destRect.bottom = PICTURE_HEIGHT + destRect.top;
			destRect.right = PICTURE_WIDTH + destRect.left;
			sourceRect.top = 0;
			sourceRect.left = 0;
			sourceRect.bottom = PICTURE_HEIGHT;
			sourceRect.right = PICTURE_WIDTH;
			m_pRenderer->SetDestRect(&destRect);
			m_pRenderer->SetSourceRect(&sourceRect);
		} 
	}
	bool RenewTrees(void);
	bool RenewCombo(void);
	bool SetEditMode(CString &val);
public:
	CTreeCtrl m_tree;
	CComboBox m_comboBrushSize;

	CSelectBalkDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CSelectBalkDlg();

// Dialog Data
	enum { IDD = IDD_DLG_SELECT_BALK };
	virtual BOOL OnInitDialog();
	virtual BOOL Create(UINT nIDTemplate, CWnd* pParentWnd = NULL, CMapEditorView* pMapEditorView = NULL);
	afx_msg void OnPaint();
	afx_msg void OnTvnSelchangedTreeBalk(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnCbnEditchangeCombo1();
	afx_msg void OnCbnSelchangeCombo1();
	afx_msg void OnShowWindow(BOOL bShow, UINT nStatus);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	
	virtual void OnOK();
	
};
