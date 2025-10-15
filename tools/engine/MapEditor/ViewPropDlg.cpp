

#include "stdafx.h"
#include "MapEditor.h"
#include "ViewPropDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif



IMPLEMENT_DYNAMIC(CViewPropDlg, CDialog)

void CViewPropDlg::OnBnClickedSaveProp()
{

	if (!UpdateData())
		return ;
	CString FilePath = theApp.GetResPath() + _T("\\MapEditorCfg.ini");

	CString strSection = _T("VIEW");
	CString strSectionKey = _T("BaseColor");
	CString strValue;
	strValue.Format(_T("%X"), GetBaseColor());
	WritePrivateProfileString(strSection, strSectionKey, strValue, FilePath);

	strSectionKey = _T("GirdColor");
	strValue.Format(_T("%X"), GetGridColor());
	WritePrivateProfileString(strSection, strSectionKey, strValue, FilePath);

	strSectionKey = _T("BackGroundColor");
	strValue.Format(_T("%X"), GetBgColor());
	WritePrivateProfileString(strSection, strSectionKey, strValue, FilePath);

	strSectionKey = _T("MagnetRadio");
	strValue.Format(_T("%d"), m_nRadius);
	WritePrivateProfileString(strSection, strSectionKey, strValue, FilePath);
}

void CViewPropDlg::OnBnClickedSelectBgColor()
{
	CColorDialog dlg(m_cBgColor);
	if (dlg.DoModal() == IDOK)
	{
		m_cBgColor = dlg.GetColor();
		this->InvalidateRect(&m_BgRect, FALSE);
	}
}

void CViewPropDlg::OnBnClickedSelectBaseColor()
{

	CColorDialog dlg(m_cBaseColor);
	if (dlg.DoModal() == IDOK)
	{
		m_cBaseColor = dlg.GetColor();
		this->InvalidateRect(&m_BaseRect, FALSE);
	}
}

void CViewPropDlg::OnPaint()
{
	CPaintDC dc(this); 



	CBrush brushGird(m_cGirdColor);
	dc.FillRect(&m_GridRect, &brushGird);

	CBrush brushBase(m_cBaseColor);
	dc.FillRect(&m_BaseRect, &brushBase);

	CBrush brushBackGround(m_cBgColor);
	dc.FillRect(&m_BgRect, &brushBackGround);
}

void CViewPropDlg::OnBnClickedSelectGridColor()
{

	CColorDialog dlg(m_cGirdColor);
	if (dlg.DoModal() == IDOK)
	{
		m_cGirdColor = dlg.GetColor();
		this->InvalidateRect(&m_GridRect, FALSE);
	}
}

BEGIN_MESSAGE_MAP(CViewPropDlg, CDialog)
	ON_WM_PAINT()
	ON_BN_CLICKED(IDC_SELECT_GRID_COLOR, &CViewPropDlg::OnBnClickedSelectGridColor)
	ON_BN_CLICKED(IDC_SELECT_BASE_COLOR, &CViewPropDlg::OnBnClickedSelectBaseColor)
	ON_BN_CLICKED(IDC_SELECT_BACKGROUND_COLOR, &CViewPropDlg::OnBnClickedSelectBgColor)
	ON_BN_CLICKED(ID_SAVE_PROP, &CViewPropDlg::OnBnClickedSaveProp)
END_MESSAGE_MAP()

void CViewPropDlg::DoDataExchange(CDataExchange* pDX)
{
	DDX_Text(pDX, IDC_MAGNET_RADIUS, m_nRadius);
	DDV_MinMaxInt(pDX, m_nRadius, 1, 100);
	CDialog::DoDataExchange(pDX);
}

CViewPropDlg::~CViewPropDlg()
{
}

CViewPropDlg::CViewPropDlg(CWnd* pParent /*=NULL*/)
: CDialog(CViewPropDlg::IDD, pParent), m_cGirdColor(0), m_cBaseColor(0), m_cBgColor(0)
, m_nRadius(0)
{
	int widht = 65;
	int height = 15;
	m_GridRect.left = 75;
	m_GridRect.top = 12;
	m_GridRect.right = m_GridRect.left + widht;
	m_GridRect.bottom = m_GridRect.top + height;
	m_BaseRect.left = m_GridRect.left;
	m_BaseRect.top = m_GridRect.top + 25;
	m_BaseRect.right = m_BaseRect.left + widht;
	m_BaseRect.bottom = m_BaseRect.top + height;
	m_BgRect.left = m_BaseRect.left;
	m_BgRect.top = m_BaseRect.top + 25;
	m_BgRect.right = m_BgRect.left + widht;
	m_BgRect.bottom = m_BgRect.top + height;
}

int CViewPropDlg::GetRadius() const
{
	return m_nRadius;
}

void CViewPropDlg::SetRadius(int nMagnetRadius)
{
	m_nRadius = nMagnetRadius;
}

void CViewPropDlg::SetGridColor(unsigned int color)
{
	m_cGirdColor = ReverseColor(color) & 0x00FFFFFF;
}

void CViewPropDlg::SetBgColor(unsigned int color)
{
	m_cBgColor = ReverseColor(color) & 0x00FFFFFF;
}

void CViewPropDlg::SetBaseColor(unsigned int color)
{
	m_cBaseColor = ReverseColor(color) & 0x00FFFFFF;
}

unsigned int CViewPropDlg::GetBgColor() const
{
	return ReverseColor(m_cBgColor) | 0xFF000000;
}

unsigned int CViewPropDlg::GetGridColor() const
{
	return ReverseColor(m_cGirdColor) | 0xFF000000;
}

unsigned int CViewPropDlg::GetBaseColor() const
{
	return ReverseColor(m_cBaseColor) | 0xFF000000;
}

