

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "SelectGroundDlg.h"
#include "..\engine\map\ppathmap.h"

#include <list>


#ifdef _DEBUG
#define new DEBUG_NEW
#endif



IMPLEMENT_DYNAMIC(CSelectGroundDlg, CDialog)

void CSelectGroundDlg::OnTvnSelchangedTree6(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	SetSelectBiggerTile(Nuclear::TST_BIG);
	*pResult = 0;
}
void CSelectGroundDlg::OnTvnSelchangedTree5(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	SetSelectBiggerGround(Nuclear::TST_BIG);
	*pResult = 0;
}

void CSelectGroundDlg::OnTvnSelchangedTree4(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	SetSelectBiggerTile(Nuclear::TST_MIDDLE);
	*pResult = 0;
}

void CSelectGroundDlg::OnTvnSelchangedTree3(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	SetSelectBiggerGround(Nuclear::TST_MIDDLE);
	*pResult = 0;
}

void CSelectGroundDlg::OnTvnSelchangedTree2(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	SetSelectSmallTile();
	*pResult = 0;
}

void CSelectGroundDlg::OnTvnSelchangedTree1(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	SetSelectSmallGround();
	*pResult = 0;
}

void CSelectGroundDlg::OnTcnSelchangeTab2(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;
	RenewEditMode();
}

void CSelectGroundDlg::OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;
	RenewEditMode();
}


void CSelectGroundDlg::OnTcnSelchangeTabGroundSize(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;
	RenewEditMode();
}

void CSelectGroundDlg::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);


	if (m_bSizing)
	{
		RenewSize();
		m_bSizing = false;
	}
}

void CSelectGroundDlg::OnBnClickedSelectBackgroundColor()
{

	CColorDialog dlg(m_nBrushColor);
	if (dlg.DoModal() == IDOK)
	{
		m_nBrushColor = dlg.GetColor();
		m_pMapEditorView->SetBrushType(ReverseColor(m_nBrushColor) | 0xFF000000);
		InvalidateRect(&m_BitmapRect, FALSE);
	}
}
void CSelectGroundDlg::RenewSize()
{
	CRect rectCtrl;
	GetClientRect(&rectCtrl);
	m_BitmapRect.top = 30;
	switch (m_nEditMode)
	{

	case EMT_SMALL_GROUND_TYPE:
	case EMT_SMALL_GROUND_TILE:
	case EMT_SMALL_GROUND_COLOR:
		m_BitmapRect.left = (rectCtrl.Width() - PMap::TILE_WIDTH) / 2;
		m_BitmapRect.right = m_BitmapRect.left + PMap::TILE_WIDTH;
		m_BitmapRect.bottom = m_BitmapRect.top + PMap::TILE_HEIGHT;
		break;
	case EMT_MIDDLE_GROUND:
	case EMT_MIDDLE_GROUND_TILE:
		if (rectCtrl.Width() - 20 >= Nuclear::SubMap::PGround::TILE_MID_WIDTH)
		{
			m_BitmapRect.left = (rectCtrl.Width() - Nuclear::SubMap::PGround::TILE_MID_WIDTH) / 2;
			m_BitmapRect.right = m_BitmapRect.left + Nuclear::SubMap::PGround::TILE_MID_WIDTH;
			m_BitmapRect.bottom = m_BitmapRect.top + Nuclear::SubMap::PGround::TILE_MID_HEIGHT;
		} else {
			m_BitmapRect.left = rectCtrl.left + 10;
			m_BitmapRect.right = rectCtrl.right - 10;
			m_BitmapRect.bottom = m_BitmapRect.top + m_BitmapRect.Width();
		}
		break;
	case EMT_BIG_GROUND:
	case EMT_BIG_GROUND_TILE:
		if (rectCtrl.Width() - 20 >= Nuclear::SubMap::PGround::TILE_BIG_WIDTH)
		{
			m_BitmapRect.left = (rectCtrl.Width() - Nuclear::SubMap::PGround::TILE_BIG_WIDTH) / 2;
			m_BitmapRect.right = m_BitmapRect.left + Nuclear::SubMap::PGround::TILE_BIG_WIDTH;
			m_BitmapRect.bottom = m_BitmapRect.top + Nuclear::SubMap::PGround::TILE_BIG_HEIGHT;
		} else {
			m_BitmapRect.left = rectCtrl.left + 10;
			m_BitmapRect.right = rectCtrl.right - 10;
			m_BitmapRect.bottom = m_BitmapRect.top + m_BitmapRect.Width();
		}
		break;
	}
	GetDlgItem(IDC_PICTURE)->MoveWindow(&m_BitmapRect);

	rectCtrl.left += 5;
	rectCtrl.right -= 5;
	rectCtrl.bottom -= 30;
	rectCtrl.top += 5;
	m_TabGroundSize.MoveWindow(&rectCtrl);
	rectCtrl.left += 5;
	rectCtrl.right -= 5;
	rectCtrl.bottom -= 5;
	rectCtrl.top = m_BitmapRect.bottom + 5;
	m_tab.MoveWindow(&rectCtrl);
	m_tab2.MoveWindow(&rectCtrl);

	rectCtrl.left += 5;
	rectCtrl.right -= 5;
	rectCtrl.bottom -= 5;
	rectCtrl.top += 25;
	for (int i=0;i<3;++i)
	{
		m_treeGroundList[i].MoveWindow(&rectCtrl);
		m_treeTileList[i].MoveWindow(&rectCtrl);
	}

	rectCtrl.left = 5;
	rectCtrl.right = rectCtrl.left + 62;
	rectCtrl.top = rectCtrl.bottom + 18;
	rectCtrl.bottom = rectCtrl.top + 14;
	GetDlgItem(IDC_STATIC)->MoveWindow(&rectCtrl);
	rectCtrl.top -= 2;
	rectCtrl.bottom = rectCtrl.top + 21;
	rectCtrl.left = rectCtrl.right + 5;
	rectCtrl.right = rectCtrl.left + 72;
	m_comboBrushSize.MoveWindow(&rectCtrl);

	rectCtrl.top = m_BitmapRect.bottom + 35;
	rectCtrl.bottom = rectCtrl.top + 12;
	rectCtrl.left = 15;
	rectCtrl.right = rectCtrl.left + 62;
	GetDlgItem(IDC_COLOR)->MoveWindow(&rectCtrl);

	rectCtrl.top -= 1;
	rectCtrl.left = rectCtrl.right + 5;
	rectCtrl.right = rectCtrl.left + 40;
	rectCtrl.bottom = rectCtrl.top + 14;
	GetDlgItem(IDC_SELECT_BACKGROUND_COLOR)->MoveWindow(&rectCtrl);





	InvalidateRect(&m_BitmapRect, FALSE);

}

void CSelectGroundDlg::OnSizing(UINT fwSide, LPRECT pRect)
{
	if (pRect->right - pRect->left < m_nMinWidth)
	{
		pRect->right = pRect->left + m_nMinWidth;
	}

	if (pRect->bottom - pRect->top < m_nMinHeight)
	{
		pRect->bottom = pRect->top + m_nMinHeight;
	}
	CDialog::OnSizing(fwSide, pRect);


	m_bSizing = true;
	RenewSize();
}

bool CSelectGroundDlg::SetBrushSize(CString &val)
{
	int size = 0;
	size = _wtoi(val);
	if (size == 0)
		return false;
	m_pMapEditorView->SetBrushSize(size);
	return true;
}

void CSelectGroundDlg::OnCbnSelchangeBrushSize()
{

	CString temp;
	m_comboBrushSize.GetLBText(m_comboBrushSize.GetCurSel(), temp);
	if (!SetBrushSize(temp))
	{
		AfxMessageBox(_T("请选择大于0的数字"));
	}
}

void CSelectGroundDlg::OnCbnEditchangeBrushSize()
{

	CString temp;
	m_comboBrushSize.GetWindowText(temp);
	if (!SetBrushSize(temp))
	{
		AfxMessageBox(_T("请输入大于0的数字"));
	}
}

void CSelectGroundDlg::OnShowWindow(BOOL bShow, UINT nStatus)
{
	CDialog::OnShowWindow(bShow, nStatus);


	RenewEditMode();
}

void CSelectGroundDlg::OnPaint()
{


	if (m_nEditMode == EMT_SMALL_GROUND_COLOR)
	{
		CPaintDC fulldc(this);
		CBrush brushGround(m_nBrushColor);
		fulldc.FillRect(&m_BitmapRect, &brushGround);
		ReleaseDC(&fulldc);
	} else {
		CPaintDC dc(GetDlgItem(IDC_PICTURE));
		CWnd::DefWindowProc(WM_PAINT, 0, 0);
		m_Image.Draw(dc.GetSafeHdc(), 0, 0, m_BitmapRect.Width(), m_BitmapRect.Height());
		ReleaseDC(&dc);
	}	

}

bool CSelectGroundDlg::LoadPictureByAddress(CString file)
{
	m_Image.Destroy();
	if (file == "")
	{
		m_Image.Create(128, 64, 32, 0);
	} else {
		m_Image.Load(file);
	}
	return true;
}

EDIT_MODE_TYPE CSelectGroundDlg::GetCurrentEditMode() 
{
	return m_nEditMode;
}

bool CSelectGroundDlg::SetSelectColor()
{
	LoadPictureByAddress(_T(""));
	m_pMapEditorView->SetBrushType(ReverseColor(m_nBrushColor) | 0xFF000000);
	return true;
}

bool CSelectGroundDlg::SetSelectBiggerTile(Nuclear::TILE_SIZE_TYPE tileSize)
{
	if (tileSize == Nuclear::TST_SMALL)
		return false;
	if (m_treeTileList[tileSize].GetSelectedItem() == NULL)
		return false;
	m_nBrushType = static_cast<unsigned int>(m_treeTileList[tileSize].GetItemData(m_treeTileList[tileSize].GetSelectedItem()));
	m_pMapEditorView->SetBrushType(m_nBrushType);
	if (m_nBrushType == Nuclear::SubMap::PGround::BIGGER_TILE_NULL.data)
	{
		LoadPictureByAddress(_T(""));
	} else {
		unsigned int mapKey = m_nBrushType;
		switch (tileSize)
		{
		case Nuclear::TST_MIDDLE:
			mapKey |= MAP_KEY_MID;
			break;
		case Nuclear::TST_BIG:
			mapKey |= MAP_KEY_BIG;
			break;
		}
		LoadPictureByAddress(m_PictureMap[mapKey]);
	}
	this->InvalidateRect(&m_BitmapRect);
	return true;
}

bool CSelectGroundDlg::SetSelectBiggerGround(Nuclear::TILE_SIZE_TYPE tileSize)
{
	if (tileSize == Nuclear::TST_SMALL)
		return false;
	if (m_treeGroundList[tileSize].GetSelectedItem() == NULL)
		return false;
	m_nBrushType = static_cast<unsigned int>(m_treeGroundList[tileSize].GetItemData(m_treeGroundList[tileSize].GetSelectedItem()));
	m_pMapEditorView->SetBrushType(m_nBrushType);
	BiggerTileType tileData;
	tileData.tileID = 15;
	tileData.tileType = m_nBrushType;
	unsigned int mapKey;
	mapKey = tileData.data;
	switch (tileSize)
	{
	case Nuclear::TST_MIDDLE:
		mapKey |= MAP_KEY_MID;
		break;
	case Nuclear::TST_BIG:
		mapKey |= MAP_KEY_BIG;
		break;
	}
	LoadPictureByAddress(m_PictureMap[mapKey]);
	InvalidateRect(&m_BitmapRect);
	return true;
}
bool CSelectGroundDlg::SetSelectSmallTile()
{
	if (m_treeTileList[0].GetSelectedItem() == NULL)
		return false;
	m_nBrushType = static_cast<unsigned int>(m_treeTileList[0].GetItemData(m_treeTileList[0].GetSelectedItem()));
	m_pMapEditorView->SetBrushType(m_nBrushType);
	if (m_nBrushType == Nuclear::SubMap::PGround::TILE_NULL)
	{
		LoadPictureByAddress(_T(""));
	} else {
		LoadPictureByAddress(m_PictureMap[m_nBrushType]);
	}
	this->InvalidateRect(&m_BitmapRect);
	return true;
}

bool CSelectGroundDlg::SetSelectSmallGround()
{
	if (m_treeGroundList[0].GetSelectedItem() == NULL)
		return false;
	m_nBrushType = static_cast<unsigned int>(m_treeGroundList[0].GetItemData(m_treeGroundList[0].GetSelectedItem()));
	m_pMapEditorView->SetBrushType(m_nBrushType);
	TileType tileData = Nuclear::SubMap::PGround::TILE_NULL;
	tileData = Nuclear::SubMap::PGround::SetGroundType(tileData, m_nBrushType);
	LoadPictureByAddress(m_PictureMap[tileData]);
	this->InvalidateRect(&m_BitmapRect);
	return true;

}

BOOL CSelectGroundDlg::Create(CMapEditorView* pMapEditorView /*=NULL*/)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

void CSelectGroundDlg::RenewEditMode()
{
	int size = m_TabGroundSize.GetCurSel();
	int sel;
	if (size == 0)
	{
		sel = m_tab.GetCurSel();
		m_tab.ShowWindow(SW_SHOW);
		m_tab2.ShowWindow(SW_HIDE);
		m_pMapEditorView->SetGroundLayerEdit(Nuclear::SubMap::GL_SMALL);
	} else {
		sel = m_tab2.GetCurSel();
		m_tab.ShowWindow(SW_HIDE);
		m_tab2.ShowWindow(SW_SHOW);
		if (size == 1)
		{
			if (m_pMapEditorView->GetGroundLayerEditId() != Nuclear::SubMap::GL_MID2)
				m_pMapEditorView->SetGroundLayerEdit(Nuclear::SubMap::GL_MID1);
		} else {
			m_pMapEditorView->SetGroundLayerEdit(Nuclear::SubMap::GL_BIG);
		}
	}
	bool result = false;
	for (int i=0;i<3;++i)
	{
		m_treeGroundList[i].ShowWindow(SW_HIDE);
		m_treeTileList[i].ShowWindow(SW_HIDE);
	}
	GetDlgItem(IDC_COLOR)->ShowWindow(SW_HIDE);
	GetDlgItem(IDC_SELECT_BACKGROUND_COLOR)->ShowWindow(SW_HIDE);
	switch (sel)
	{
	case 0:
		m_treeGroundList[size].ShowWindow(SW_SHOW);
		switch (size)
		{
		case 0:
			m_nEditMode = EMT_SMALL_GROUND_TYPE;
			result = SetSelectSmallGround();
			break;
		case 1:
			m_nEditMode = EMT_MIDDLE_GROUND;
			result = SetSelectBiggerGround(Nuclear::TST_MIDDLE);
			break;
		case 2:
			m_nEditMode = EMT_BIG_GROUND;
			result = SetSelectBiggerGround(Nuclear::TST_BIG);
			break;
		}
		break;
	case 1:
		m_treeTileList[size].ShowWindow(SW_SHOW);
		switch (size)
		{
		case 0:
			m_nEditMode = EMT_SMALL_GROUND_TILE;
			result = SetSelectSmallTile();
			break;
		case 1:
			m_nEditMode = EMT_MIDDLE_GROUND_TILE;
			result = SetSelectBiggerGround(Nuclear::TST_MIDDLE);
			break;
		case 2:
			m_nEditMode = EMT_BIG_GROUND_TILE;
			result = SetSelectBiggerGround(Nuclear::TST_BIG);
			break;
		}

		break;
	case 2:
		GetDlgItem(IDC_COLOR)->ShowWindow(SW_SHOW);
		GetDlgItem(IDC_SELECT_BACKGROUND_COLOR)->ShowWindow(SW_SHOW);
		m_nEditMode = EMT_SMALL_GROUND_COLOR;
		result = SetSelectColor();
		break;
	case 3:
		m_nEditMode = EMT_SMALL_SUPER_DELETE;
		result = true;
		LoadPictureByAddress(_T(""));
		break;
	}
	m_pMapEditorView->SetEditMode(m_nEditMode);
	if (!result)
		LoadPictureByAddress(_T(""));
	RenewSize();
	this->Invalidate();
}

bool CSelectGroundDlg::RenewSmallTrees(void)
{
	CFileFind myFinder;
	CFileFind myTileFinder;
	CString FileName;
	HTREEITEM tempTileItem,tempGroundItem;
	BOOL working, workingInFolder;
	unsigned char typeData;
	TileType tileData;
	tempGroundItem = m_treeGroundList[0].InsertItem(_T("删除"), 0);
	m_treeGroundList[0].SetItemData(tempGroundItem, CMapEditorView::GROUND_TYPE_BRUSH_NULL);
	FileName=theApp.GetResPath() + _T("\\map\\tiles\\smalltile\\*.*");
	working = myFinder.FindFile(LPCTSTR(FileName));
	while (working)
	{  
		working = myFinder.FindNextFile();
		if (myFinder.IsDots())
			continue;
		if (myFinder.IsDirectory() && !(myFinder.IsHidden())) 
		{
			tempGroundItem = m_treeGroundList[0].InsertItem(myFinder.GetFileTitle(), 0);
			typeData = _ttoi(myFinder.GetFileTitle().GetString());
			std::wstring strValue = myFinder.GetFileTitle();
			theApp.GetPathMap()->Insert(Nuclear::TST_SMALL, typeData, strValue);
			m_treeGroundList[0].SetItemData(tempGroundItem, typeData);
			tileData = Nuclear::SubMap::PGround::TILE_NULL;
			tileData = Nuclear::SubMap::PGround::SetGroundType(tileData, typeData);
			m_PictureMap[tileData] = myFinder.GetFilePath() + _T("\\15-0") + TILE_TYPE;
			tempGroundItem = m_treeTileList[0].InsertItem(myFinder.GetFileTitle(), 0);
			m_treeTileList[0].SetItemData(tempGroundItem, 0);

			workingInFolder = myTileFinder.FindFile(myFinder.GetFilePath() + _T("\\*") + TILE_TYPE);
			while (workingInFolder)
			{
				workingInFolder = myTileFinder.FindNextFile();
				if (myTileFinder.IsDots())
					continue;
				tempTileItem = m_treeTileList[0].InsertItem(myTileFinder.GetFileName(),0,0,tempGroundItem);
				FileName = myTileFinder.GetFileTitle();
				tileData = Nuclear::SubMap::PGround::SetTileType(tileData, static_cast<unsigned char>(_ttoi(FileName.GetString())));
				tileData = Nuclear::SubMap::PGround::SetTileNumber(tileData, 
					static_cast<unsigned char>(_ttoi(FileName.Mid(FileName.Find('-') + 1).GetString())));
				m_treeTileList[0].SetItemData(tempTileItem, tileData);
				m_PictureMap[tileData] = myTileFinder.GetFilePath();
			}
		}  
	}
	myFinder.Close();
	myTileFinder.Close();
	return true;
}

bool CSelectGroundDlg::RenewTrees(Nuclear::TILE_SIZE_TYPE tileSize)
{
	int order;
	unsigned int mapKeyMsk;
	CString FileName;
	FileName=theApp.GetResPath() + L"\\map\\tiles\\";

	switch(tileSize)
	{
	case Nuclear::TST_SMALL:
		return RenewSmallTrees();
		break;
	case Nuclear::TST_MIDDLE:
		order = 1;
		mapKeyMsk = MAP_KEY_MID;
		FileName += L"midtile";
		break;
	case Nuclear::TST_BIG:
		order = 2;
		mapKeyMsk = MAP_KEY_BIG;
		FileName += L"bigtile";
		break;
	}
	FileName += L"\\*.*";

	CFileFind myFinder;
	CFileFind myTileFinder;


	HTREEITEM tempTileItem,tempGroundItem;

	BOOL working, workingInFolder;
	unsigned char typeData;
	BiggerTileType tileData;
	tempGroundItem = m_treeGroundList[order].InsertItem(_T("删除"), 0);
	m_treeGroundList[order].SetItemData(tempGroundItem, 0);

	working = myFinder.FindFile(LPCTSTR(FileName));
	while (working)
	{  
		working = myFinder.FindNextFile();
		if (myFinder.IsDots())
			continue;
		if (myFinder.IsDirectory() && !(myFinder.IsHidden())) 
		{
			tempGroundItem = m_treeGroundList[order].InsertItem(myFinder.GetFileTitle(), 0);
			typeData = _ttoi(myFinder.GetFileTitle().GetString());
			std::wstring strValue = myFinder.GetFileTitle();
			theApp.GetPathMap()->Insert(tileSize, typeData, strValue);
			m_treeGroundList[order].SetItemData(tempGroundItem, typeData);
			tileData = Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
			tileData.tileType = typeData;
			tileData.tileID = 15;
			m_PictureMap[static_cast<unsigned int>(tileData.data) | mapKeyMsk] = myFinder.GetFilePath() + _T("\\15") + TILE_TYPE;
			tempGroundItem = m_treeTileList[order].InsertItem(myFinder.GetFileTitle(), 0);
			m_treeTileList[order].SetItemData(tempGroundItem, 0);

			workingInFolder = myTileFinder.FindFile(myFinder.GetFilePath() + _T("\\*") + TILE_TYPE);
			while (workingInFolder)
			{
				workingInFolder = myTileFinder.FindNextFile();
				if (myTileFinder.IsDots())
					continue;
				tempTileItem = m_treeTileList[order].InsertItem(myTileFinder.GetFileName(),0,0,tempGroundItem);
				FileName = myTileFinder.GetFileTitle();
				tileData.tileID = static_cast<unsigned short>(_ttoi(FileName.GetString()));
				m_treeTileList[order].SetItemData(tempTileItem, tileData.data);
				m_PictureMap[static_cast<unsigned int>(tileData.data) | mapKeyMsk] = myTileFinder.GetFilePath();
			}
		}  
	}
	myFinder.Close();
	myTileFinder.Close();

	return true;
}

bool CSelectGroundDlg::RenewTrees(void)
{
	bool result = RenewTrees(Nuclear::TST_SMALL) && RenewTrees(Nuclear::TST_MIDDLE) && 
		RenewTrees(Nuclear::TST_BIG);
	theApp.GetPathMap()->Save();
	return result;
}
BOOL CSelectGroundDlg::OnInitDialog()
{
	CDialog::OnInitDialog();


	SetWindowText(_T("地图Tile列表"));

	m_tab.InsertItem(0, L"类型");
	m_tab.InsertItem(1, L"图素");
	m_tab.InsertItem(2, L"颜色");
	m_tab.InsertItem(3, L"超级删除");
	m_tab2.InsertItem(0, L"类型");
	m_tab2.InsertItem(1, L"图素");
	m_TabGroundSize.InsertItem(0, L"小地表");
	m_TabGroundSize.InsertItem(1, L"中地表");
	m_TabGroundSize.InsertItem(2, L"大地表");
	m_treeTileList[0].ShowWindow(SW_HIDE);
	m_pMapEditorView->SetEditMode(EMT_SMALL_GROUND_TYPE);
	RenewTrees();
	RenewCombo();
	LoadPictureByAddress(_T(""));


	m_nBrushColor = 0xFFFFFF;


	CRect rect;
	m_tab.BringWindowToTop();
	m_tab2.BringWindowToTop();
	GetDlgItem(IDC_PICTURE)->BringWindowToTop();
	for (int i=0;i<3;++i)
	{
		m_treeGroundList[i].BringWindowToTop();
		m_treeTileList[i].BringWindowToTop();
	}
	GetWindowRect(&rect);
	m_nMinWidth = rect.Width();
	m_nMinHeight = rect.Height();
	RenewSize();


	return TRUE;  

}
void CSelectGroundDlg::RenewCombo()
{
	m_comboBrushSize.InsertString(0, _T("1"));
	m_comboBrushSize.InsertString(1, _T("2"));
	m_comboBrushSize.InsertString(2, _T("3"));
	m_comboBrushSize.InsertString(3, _T("4"));
	m_comboBrushSize.InsertString(4, _T("5"));
	m_comboBrushSize.InsertString(5, _T("8"));
	m_comboBrushSize.InsertString(6, _T("10"));
	m_comboBrushSize.InsertString(7, _T("15"));
	m_comboBrushSize.SetCurSel(0);
	m_pMapEditorView->SetBrushSize(1);
}


BEGIN_MESSAGE_MAP(CSelectGroundDlg, CDialog)
	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB1, &CSelectGroundDlg::OnTcnSelchangeTab1)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE1, &CSelectGroundDlg::OnTvnSelchangedTree1)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE2, &CSelectGroundDlg::OnTvnSelchangedTree2)
	ON_WM_PAINT()
	ON_WM_SHOWWINDOW()
	ON_CBN_EDITCHANGE(IDC_BRUSH_SIZE, &CSelectGroundDlg::OnCbnEditchangeBrushSize)
	ON_CBN_SELCHANGE(IDC_BRUSH_SIZE, &CSelectGroundDlg::OnCbnSelchangeBrushSize)
	ON_WM_SIZING()
	ON_BN_CLICKED(IDC_SELECT_BACKGROUND_COLOR, &CSelectGroundDlg::OnBnClickedSelectBackgroundColor)
	ON_WM_SIZE()
	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB_GROUND_SIZE, &CSelectGroundDlg::OnTcnSelchangeTabGroundSize)
	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB2, &CSelectGroundDlg::OnTcnSelchangeTab2)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE3, &CSelectGroundDlg::OnTvnSelchangedTree3)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE4, &CSelectGroundDlg::OnTvnSelchangedTree4)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE5, &CSelectGroundDlg::OnTvnSelchangedTree5)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE6, &CSelectGroundDlg::OnTvnSelchangedTree6)
END_MESSAGE_MAP()


void CSelectGroundDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TAB1, m_tab);
	DDX_Control(pDX, IDC_TAB2, m_tab2);
	DDX_Control(pDX, IDC_TAB_GROUND_SIZE, m_TabGroundSize);
	DDX_Control(pDX, IDC_TREE1, m_treeGroundList[0]);
	DDX_Control(pDX, IDC_TREE2, m_treeTileList[0]);
	DDX_Control(pDX, IDC_TREE3, m_treeGroundList[1]);
	DDX_Control(pDX, IDC_TREE4, m_treeTileList[1]);
	DDX_Control(pDX, IDC_TREE5, m_treeGroundList[2]);
	DDX_Control(pDX, IDC_TREE6, m_treeTileList[2]);
	DDX_Control(pDX, IDC_BRUSH_SIZE, m_comboBrushSize);
}


CSelectGroundDlg::~CSelectGroundDlg()
{
}

CSelectGroundDlg::CSelectGroundDlg(CWnd* pParent /*=NULL*/)
: CDialog(CSelectGroundDlg::IDD, pParent), m_pMapEditorView(NULL), m_bSizing(false)
{
}

