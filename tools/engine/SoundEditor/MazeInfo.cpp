// MazeInfo.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "MazeInfo.h"
#include "Input2Dlg.h"


// CMazeInfo dialog

IMPLEMENT_DYNAMIC(CMazeInfo, CDialog)

CMazeInfo::CMazeInfo(CWnd* pParent /*=NULL*/)
	: CDialog(CMazeInfo::IDD, pParent), mazeUrlFormat(_T("")), m_colorChanged(false), spriteMask(0),
	m_mapmapChanged(false), ingoreColors(false)
{

}

CMazeInfo::~CMazeInfo()
{
}

void CMazeInfo::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST_MAP_ID, m_listMapId);
	DDX_Control(pDX, IDC_LIST_MAZE_COLOR, m_listMazeColor);
	DDX_Text(pDX, IDC_EDIT_MAZE_URL_FORMAT, mazeUrlFormat);
	if (pDX->m_bSaveAndValidate)
	{
		CString str;
		DDX_Text(pDX, IDC_EDIT_SPRITE_MASK, str);
		wchar_t *p;
		spriteMask = wcstoul(str.GetBuffer(), &p, 16);
	} else {
		CString str;
		str.Format(L"0x%X", spriteMask);
		DDX_Text(pDX, IDC_EDIT_SPRITE_MASK, str);
	}
	DDX_Control(pDX, IDC_SLIDER_ALPHA, m_SilderAlpha);
}


BEGIN_MESSAGE_MAP(CMazeInfo, CDialog)
	ON_BN_CLICKED(IDC_BUTTON_ADD_MAPID, &CMazeInfo::OnBnClickedButtonAddMapid)
	ON_BN_CLICKED(IDC_BUTTON_DEL_MAPID, &CMazeInfo::OnBnClickedButtonDelMapid)
	ON_BN_CLICKED(IDC_BUTTON_ADD_MASK_COLOR, &CMazeInfo::OnBnClickedButtonAddMaskColor)
	ON_BN_CLICKED(IDC_BUTTON_DEL_MASK_COLOR, &CMazeInfo::OnBnClickedButtonDelMaskColor)
	ON_NOTIFY(NM_DBLCLK, IDC_LIST_MAZE_COLOR, &CMazeInfo::OnNMDblclkListMazeColor)
END_MESSAGE_MAP()


void CMazeInfo::LoadMapIdMap()
{
	m_listMapId.DeleteAllItems();
	wchar_t NameBuffer[5000];
	GetPrivateProfileSection(L"MazeMaps", NameBuffer, 5000, theApp.GetCfgFilePath());
	wchar_t *p = NameBuffer;
	CString mapname, mapid;
	int pos, i = 0;
	while (*p != L'\0')
	{
		mapname = p;
		p += mapname.GetLength() + 1;
		pos = mapname.Find(L'=');
		mapid = mapname.Mid(pos + 1);
		mapname = mapname.Left(pos);
		m_listMapId.InsertItem(i, mapname.GetBuffer());
		m_listMapId.SetItemText(i, 1, mapid.GetBuffer());
		mapname.ReleaseBuffer();
		mapid.ReleaseBuffer();
		++i;
	}
	m_mapmapChanged = false;
}

void CMazeInfo::SaveMapIdMap()
{
	if (!m_mapmapChanged)
		return;
	std::list<std::wstring>::iterator it = m_deleteMapIDlist.begin(), ie = m_deleteMapIDlist.end();
	for (;it!=ie;++it)
	{
		WritePrivateProfileString(L"MazeMaps", it->c_str(), NULL, theApp.GetCfgFilePath());
	}
	int i = 0, count = m_listMapId.GetItemCount();
	for (;i<count;++i)
	{
		WritePrivateProfileString(L"MazeMaps", m_listMapId.GetItemText(i, 0).GetBuffer(), 
			m_listMapId.GetItemText(i, 1).GetBuffer(), theApp.GetCfgFilePath());
	}
	m_mapmapChanged = false;
}

void CMazeInfo::LoadColorMap()
{
	Nuclear::XPMazeColors::iterator it = colors.begin(), ie = colors.end();
	m_nItemDataSeed = 0;
	CString str;
	for (;it!=ie;++it,++m_nItemDataSeed)
	{
		str.Format(L"0x%X", it->mask);
		m_listMazeColor.InsertItem((int)m_nItemDataSeed, str.GetBuffer());
		str.ReleaseBuffer();
		m_listMazeColor.SetItemData((int)m_nItemDataSeed, m_nItemDataSeed);
		m_listMazeColor.SetItemColor(m_nItemDataSeed, XPC2REF(it->color));
	}
}

void CMazeInfo::SaveColorMap()
{
	if (m_nAlpha != m_SilderAlpha.GetPos())
	{
		m_nAlpha = m_SilderAlpha.GetPos();
		m_colorChanged = true;
	}
	if (!m_colorChanged)
		return;
	Nuclear::XPMazeColors::iterator it = colors.begin(), ie = colors.end();
	for (;it!=ie;++it)
	{
		it->color.a = m_nAlpha;
	}
	theApp.SaveMazeColors(colors, m_nOldColorCount);
}

// CMazeInfo message handlers

void CMazeInfo::OnBnClickedButtonAddMapid()
{
	CInput2Dlg dlg;
	dlg.strInfo = L"添加地图名和ID的映射关系";
	dlg.strCaption = dlg.strInfo;
	dlg.strInfo1 = L"地图名：";
	dlg.strInfo2 = L"地图ID：";
	if (dlg.DoModal() == IDOK)
	{
		int i = m_listMapId.GetItemCount();
		m_listMapId.InsertItem(i, dlg.strEdit1.GetBuffer());
		m_listMapId.SetItemText(i, 1, dlg.strEdit2.GetBuffer());
		m_mapmapChanged = true;
	}
}

void CMazeInfo::OnBnClickedButtonDelMapid()
{
	if (m_listMapId.GetSelectedCount() != 1)
	{
		AfxMessageBox(L"请选择一个映射");
		return;
	}
	POSITION pos = m_listMapId.GetFirstSelectedItemPosition();
	int cursel = m_listMapId.GetNextSelectedItem(pos);
	CString mapname = m_listMapId.GetItemText(cursel, 0);
	m_deleteMapIDlist.push_back(mapname.GetBuffer());
	mapname.ReleaseBuffer();
	m_listMapId.DeleteItem(cursel);
	m_mapmapChanged = true;
}

void CMazeInfo::OnBnClickedButtonAddMaskColor()
{
	CInput2Dlg dlg;
	dlg.strInfo = L"添加Mask";
	dlg.strCaption = dlg.strInfo;
	dlg.strInfo1 = L"Mask(16进制)";
	dlg.oneEdit = true;
	if (dlg.DoModal() == IDOK)
	{
		Nuclear::XPMazeColor mc;
		wchar_t *p;
		mc.mask =  wcstoul(dlg.strEdit1.GetBuffer(), &p, 16);
		mc.color.data = 0xFFFFFFFF;
		CString str;
		str.Format(L"0x%X", mc.mask);
		int i = m_listMazeColor.GetItemCount();
		m_listMazeColor.InsertItem(i, str.GetBuffer());
		m_listMazeColor.SetItemData(i, ++m_nItemDataSeed);
		m_listMazeColor.SetItemColor(m_nItemDataSeed, RGB(255,255,255));
		colors.push_back(mc);
		m_colorChanged = true;
	}
}

void CMazeInfo::OnBnClickedButtonDelMaskColor()
{
	if (m_listMazeColor.GetSelectedCount() != 1)
	{
		AfxMessageBox(L"请选择一个Mask");
		return;
	}
	POSITION pos = m_listMazeColor.GetFirstSelectedItemPosition();
	int cursel = m_listMazeColor.GetNextSelectedItem(pos);
	m_listMazeColor.DeleteItem(cursel);
	colors.erase(colors.begin() + cursel);
	m_colorChanged = true;
}

BOOL CMazeInfo::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_listMapId.InsertColumn(0, L"地图名", 0, 100);
	m_listMapId.InsertColumn(1, L"地图ID", 0, 100);
	m_listMazeColor.InsertColumn(0, L"Mask", 0, 100);
	m_listMazeColor.InsertColumn(1, L"颜色", 0, 90);
	m_listMapId.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	m_SilderAlpha.SetRange(0, 255);
	m_SilderAlpha.SetTicFreq(25);
	if (ingoreColors)
	{
		theApp.LoadMazeColors(colors);
	}
	m_nOldColorCount = colors.size();
	if (m_nOldColorCount == 0)
	{
		m_nAlpha = 0;
	} else {
		m_nAlpha = colors.begin()->color.a;
	}
	m_SilderAlpha.SetPos(m_nAlpha);
	wchar_t tempStr[200];
	GetPrivateProfileString(L"MazeData", L"Format", L"", tempStr, 200, theApp.GetCfgFilePath());
	mazeUrlFormat = tempStr;
	spriteMask = GetPrivateProfileInt(L"MazeData", L"SpriteMask", 3, theApp.GetCfgFilePath());
	LoadMapIdMap();
	LoadColorMap();
	UpdateData(FALSE);
	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CMazeInfo::OnOK()
{
	UpdateData();
	SaveMapIdMap();
	SaveColorMap();
	WritePrivateProfileString(L"MazeData", L"Format", mazeUrlFormat, theApp.GetCfgFilePath());
	CString str;
	str.Format(L"0x%X", spriteMask);
	WritePrivateProfileString(L"MazeData", L"SpriteMask", str, theApp.GetCfgFilePath());
	CDialog::OnOK();
}

void CMazeInfo::OnNMDblclkListMazeColor(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;
	if (m_listMazeColor.GetSelectedCount() != 1)
	{
		MessageBox(L"请选择一种颜色");
		return;
	}
	POSITION pos = m_listMazeColor.GetFirstSelectedItemPosition();
	int curSel = m_listMazeColor.GetNextSelectedItem(pos);
	DWORD_PTR itemData = m_listMazeColor.GetItemData(curSel);
	Nuclear::XPMazeColors::iterator it = colors.begin() + curSel, ie = colors.end();
	if (it == ie)
		return;
	COLORREF color = XPC2REF(it->color);
	CColorDialog dlg(color);
	if (dlg.DoModal() == IDOK)
	{
		color = dlg.GetColor();
		it->color = REF2XPC(color);
		m_listMazeColor.SetItemColor(itemData, color);
		m_listMazeColor.RedrawItems(curSel, curSel);
		m_colorChanged = true;
	}
}
