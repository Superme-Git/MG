#pragma once
#include "afxwin.h"
#include "PerformanceMap.h"

class CMapEditorView;

enum eDisplayResolution 
{
	DR_800X600 = 0,
	DR_1024X768,
	DR_Count,
};

extern const int g_ViewPortWidth[DR_Count];
extern const int g_ViewPortHeight[DR_Count];

class CPerformanceDlg : public CDialog
{
	DECLARE_DYNAMIC(CPerformanceDlg)

public:
	enum { IDD = IDD_DLG_PERFORMANCE };

	virtual ~CPerformanceDlg();

	CPerformanceDlg(CWnd* pParent = NULL);   

private:
	Nuclear::PicResrcMap m_PictureResourceMap;
	Nuclear::CRECT m_ViewportRect;
	Nuclear::CPOINT m_ViewportCenter;
	CPerformanceMap* m_pPerformanceMap;
	CComboBox m_comboViewPort;
	CMapEditorView* m_pMapEditorView;
	eDisplayResolution m_DisplayResolution;
	float m_fViewportLayerCount;
	float m_fMapLayerCount;
	float m_fViewportSize;
	float m_fMapSize;

private:
	void SetViewportRect(const Nuclear::CPOINT &point, eDisplayResolution dm);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	Nuclear::CPOINT GetViewportCenter() { return m_ViewportCenter; }
	Nuclear::CRECT GetViewportRect() { return m_ViewportRect; }
	void SetViewportCenter(const Nuclear::CPOINT &point) { SetViewportRect(point, m_DisplayResolution); }
	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedButton2();
	afx_msg void OnCbnSelchangeComboViewport();
	afx_msg void OnBnClickedButton1();
};
