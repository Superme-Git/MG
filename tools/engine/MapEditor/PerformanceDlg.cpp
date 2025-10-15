

#include "stdafx.h"
#include "MapEditor.h"
#include "../engine/map/pmap.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "PerformanceDlg.h"

#ifdef _DEBUG
#include "..\engine\common\log.h"
#endif"

const int g_ViewPortWidth[DR_Count] = {800, 1024};
const int g_ViewPortHeight[DR_Count] = {600, 768};



IMPLEMENT_DYNAMIC(CPerformanceDlg, CDialog)

void CPerformanceDlg::SetViewportRect(const Nuclear::CPOINT &point, eDisplayResolution dm)
{
	m_ViewportRect.left = point.x - (g_ViewPortWidth[dm] / 2);
	m_ViewportRect.top = point.y - (g_ViewPortHeight[dm] / 2);
	if (m_ViewportRect.left < 0)
		m_ViewportRect.left = 0;
	if (m_ViewportRect.top < 0)
		m_ViewportRect.top = 0;
	m_ViewportRect.right = m_ViewportRect.left + g_ViewPortWidth[dm];
	m_ViewportRect.bottom = m_ViewportRect.top + g_ViewPortHeight[dm];
	Nuclear::PMap& pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	if (m_ViewportRect.right > pmap.GetWidth())
	{
		m_ViewportRect.right = pmap.GetWidth();
		m_ViewportRect.left = m_ViewportRect.right - g_ViewPortWidth[dm];
		if (m_ViewportRect.left < 0)
			m_ViewportRect.left = 0;
	}
	if (m_ViewportRect.bottom > pmap.GetHeight())
	{
		m_ViewportRect.bottom = pmap.GetHeight();
		m_ViewportRect.top = m_ViewportRect.bottom - g_ViewPortHeight[dm];
		if (m_ViewportRect.top < 0)
			m_ViewportRect.top = 0;
	}
	if (m_ViewportRect.Center() != m_ViewportCenter || dm != m_DisplayResolution)
	{
		m_ViewportCenter = m_ViewportRect.Center();
		m_DisplayResolution = dm;
		m_pPerformanceMap->UpdateLinkedObjectTree(m_ViewportRect);
		m_fViewportSize = static_cast<float>(m_pPerformanceMap->GetStorageSize(m_ViewportRect)) / 1024;
		m_fViewportLayerCount = static_cast<float>(m_pPerformanceMap->GetAreaSize(m_ViewportRect, pmap.GetWaterData())) / (m_ViewportRect.Width() * m_ViewportRect.Height());
		UpdateData(FALSE);
	}

}
BOOL CPerformanceDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_comboViewPort.InsertString(0, L"800X600");
	m_comboViewPort.SetItemData(0, DR_800X600);
	m_comboViewPort.InsertString(1, L"1024X768");
	m_comboViewPort.SetItemData(1, DR_1024X768);

	m_comboViewPort.SetCurSel(0);

	CMapEditorDoc* pDoc = m_pMapEditorView->GetDocument();
	CToolsMap& pmap = pDoc->GetToolsMap();
	pmap.SaveTilesToGround();
	m_pPerformanceMap->LoadMap(pmap);

	m_fMapSize = static_cast<float>(m_pPerformanceMap->GetMapStorageSize()) / 1024 / 1024;
	Nuclear::CRECT rect(0, 0, pmap.GetWidth(), pmap.GetHeight());
	m_fMapLayerCount = static_cast<float>(m_pPerformanceMap->GetAreaSize(rect, pmap.GetWaterData())) / (rect.Width() * rect.Height());
	UpdateData(FALSE);
	SetViewportRect(Nuclear::CPOINT(0, 0), DR_800X600);
	return TRUE;  

}

void CPerformanceDlg::OnCbnSelchangeComboViewport()
{
	SetViewportRect(m_ViewportCenter, static_cast<eDisplayResolution>(m_comboViewPort.GetItemData(m_comboViewPort.GetCurSel())));
}

BOOL CPerformanceDlg::Create(CMapEditorView* pMapEditorView)
{
	m_pMapEditorView = pMapEditorView;
	m_pPerformanceMap = new CPerformanceMap(pMapEditorView, &m_PictureResourceMap);
	return CDialog::Create(IDD, pMapEditorView);
}



BEGIN_MESSAGE_MAP(CPerformanceDlg, CDialog)
	ON_CBN_SELCHANGE(IDC_COMBO_VIEWPORT, &CPerformanceDlg::OnCbnSelchangeComboViewport)
END_MESSAGE_MAP()


void CPerformanceDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_VIEWPORT_SIZE, m_fViewportSize);
	DDX_Text(pDX, IDC_VIEWPORT_LAYER, m_fViewportLayerCount);
	DDX_Text(pDX, IDC_MAP_SIZE, m_fMapSize);
	DDX_Text(pDX, IDC_MAP_LAYER, m_fMapLayerCount);
	DDX_Control(pDX, IDC_COMBO_VIEWPORT, m_comboViewPort);
}


CPerformanceDlg::~CPerformanceDlg()
{
	if (m_pPerformanceMap)
	{
		delete m_pPerformanceMap;
	}
}

CPerformanceDlg::CPerformanceDlg(CWnd* pParent /*=NULL*/)
: CDialog(CPerformanceDlg::IDD, pParent)
, m_fMapSize(0)
, m_fViewportSize(0)
, m_fMapLayerCount(0)
, m_fViewportLayerCount(0)
, m_pPerformanceMap(NULL)
{
	m_ViewportCenter.x = 0;
	m_ViewportCenter.y = 0;
}

