#include "StdAfx.h"
#include "MapEditorGroundCtrl.h"
#include "MapEditor.h"
#include "MapEditorView.h"

bool CMapEditorGroundCtrl::SetTileColorType(unsigned int nBrushType, Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	Nuclear::PMap& pmap = m_pView->GetDocument()->GetToolsMap();
	CAction::CTileColorActionAtom *oper = pNowOper->GetTileColorActionData();
	CAction::TileColorGranule tileColorData;
	int min, max;
	min = -1;
	max = pmap.GetWidthForColor() - 1;
	if (rect.left < min)
		rect.left = min;
	if (rect.right > max)
		rect.right = max;
	if (rect.top < min)
		rect.top = min;
	max = pmap.GetHeightForColor() - 1;
	if (rect.bottom > max)
		rect.bottom = max;
	if (rect.left >= rect.right)
		return false;
	if (rect.top >= rect.bottom)
		return false;
	unsigned int size = (rect.right - rect.left) * (rect.bottom - rect.top);
	CPoint tmpPoint;
	bool changed = false;
	unsigned int *pColorData = pmap.GetColorData();
	unsigned int *pTmpColorData;
	for (tmpPoint.y = rect.top + 1;tmpPoint.y <= rect.bottom; tmpPoint.y++)
	{
		pTmpColorData = pColorData + pmap.GetColorPos(rect.left + 1, tmpPoint.y);
		for (tmpPoint.x = rect.left + 1;tmpPoint.x <= rect.right; ++tmpPoint.x, ++pTmpColorData)
		{
			tileColorData.x = tmpPoint.x;
			tileColorData.y = tmpPoint.y;
			tileColorData.oper[CAction::CActionAtom::BEFORE_OPERATOR] = *pTmpColorData;
			*pTmpColorData = nBrushType;
			tileColorData.oper[CAction::CActionAtom::AFTER_OPERATOR] = nBrushType;
			oper->m_TileColorData.insert(tileColorData);
			changed = true;
		}
	}
	return changed;
}

bool CMapEditorGroundCtrl::SetWaterType(Nuclear::SubMap::WaterTileType type, Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	Nuclear::PMap& pmap = m_pView->GetDocument()->GetToolsMap();
	CAction::CWaterActionAtom *oper = pNowOper->GetWaterActionData();
	CAction::WaterGranule waterData;
	int min, max;
	min = 0;
	max = pmap.GetWidthForWater() - 1;
	if (rect.left < min)
		rect.left = min;
	if (rect.right > max)
		rect.right = max;
	max = pmap.GetHeightForWater() - 1;
	if (rect.top < min)
		rect.top = min;
	if (rect.bottom > max)
		rect.bottom = max;
	if (rect.left > rect.right)
		return false;
	if (rect.top > rect.bottom)
		return false;
	unsigned int size = (rect.right - rect.left + 1) * (rect.bottom - rect.top + 1);
	CPoint tmpPoint;
	bool changed = false;
	Nuclear::SubMap::WaterTileType *pWaterData = pmap.GetWaterData();
	Nuclear::SubMap::WaterTileType *pTmpWaterData;
	for (tmpPoint.y = rect.top;tmpPoint.y <= rect.bottom; tmpPoint.y++)
	{
		pTmpWaterData = pWaterData + pmap.GetWaterPos(rect.left, tmpPoint.y);
		for (tmpPoint.x = rect.left;tmpPoint.x <= rect.right; ++tmpPoint.x, ++pTmpWaterData)
		{
			waterData.x = tmpPoint.x;
			waterData.y = tmpPoint.y;
			waterData.oper[CAction::CActionAtom::BEFORE_OPERATOR] = *pTmpWaterData;
			*pTmpWaterData = type;
			waterData.oper[CAction::CActionAtom::AFTER_OPERATOR] = *pTmpWaterData;
			oper->m_WaterData.insert(waterData);
			changed = true;
		}
	}
	return changed;
}

bool CMapEditorGroundCtrl::SetTileType(unsigned int nBrushType, Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	switch(m_pView->GetGroundLayerEditId())
	{
	case Nuclear::SubMap::GL_SMALL:
		return SetSmallTileType(nBrushType, rect, pNowOper);
		break;
	default:
		return SetBiggerTileType(nBrushType, rect, pNowOper);
		break;
	}
}

bool CMapEditorGroundCtrl::SuperDeleteTile(Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	assert(m_pView->GetGroundLayerEditId() == Nuclear::SubMap::GL_SMALL);
	CAction::CSuperTileActionAtom *oper = pNowOper->GetSuperTileActionData();
	CAction::SuperTileGranule tileData;
	unsigned int size = m_pDoc->GetFactTileRect(rect, Nuclear::TST_SMALL, true);

	CPoint tmpPoint;
	bool changed = false;
	int layerCount = static_cast<int>(m_pDoc->GetGroundLayers().size());
	int layer;
	TileType oldType;
	for (tmpPoint.y = rect.top;tmpPoint.y <= rect.bottom; tmpPoint.y++)
	{
		for (tmpPoint.x = rect.left;tmpPoint.x <= rect.right; tmpPoint.x++)
		{
			tileData.x = tmpPoint.x;
			tileData.y = tmpPoint.y;
			for (layer=0;layer<layerCount;++layer)
			{
				oldType = m_pDoc->GetSmallTileData(layer, tmpPoint.x, tmpPoint.y);
				if (m_pDoc->SetTileForcibly(layer, tmpPoint.x, tmpPoint.y, Nuclear::SubMap::PGround::TILE_NULL))
				{
					tileData.oper[CAction::CActionAtom::BEFORE_OPERATOR][layer] = oldType;
					tileData.oper[CAction::CActionAtom::AFTER_OPERATOR][layer] = Nuclear::SubMap::PGround::TILE_NULL;
					changed = true;
				}
			}
			if (tileData.oper[0].size() > 0)	
				oper->m_TileData.insert(tileData);
			tileData.oper[0].clear();
			tileData.oper[1].clear();

		}
	}
	return changed;
}

bool CMapEditorGroundCtrl::SetBiggerTileType(unsigned int nBrushType, Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	CAction::CBiggerTileActionAtom *oper = pNowOper->GetBiggerTileActionData();
	CAction::BiggerTileGranule tileData;
	Nuclear::TILE_SIZE_TYPE tileSize;
	switch(m_pView->GetGroundLayerEditId())
	{
	case Nuclear::SubMap::GL_MID1:
	case Nuclear::SubMap::GL_MID2:
		tileSize = Nuclear::TST_MIDDLE;
		break;
	case Nuclear::SubMap::GL_BIG:
		tileSize = Nuclear::TST_BIG;
		break;
	}
	BiggerTileType tileType;
	tileType.data = nBrushType;
	unsigned int size = m_pDoc->GetFactTileRect(rect, tileSize, true);
	CPoint tmpPoint;
	bool changed = false;
	for (tmpPoint.y = rect.top;tmpPoint.y <= rect.bottom; tmpPoint.y++)
	{
		for (tmpPoint.x = rect.left;tmpPoint.x <= rect.right; tmpPoint.x++)
		{
			tileData.x = tmpPoint.x;
			tileData.y = tmpPoint.y;
			tileData.oper[CAction::CActionAtom::BEFORE_OPERATOR] = m_pDoc->GetBiggerTileData(m_pView->GetGroundLayerEditId(), tmpPoint.x, tmpPoint.y);
			if (m_pDoc->SetTile(m_pView->GetGroundLayerEditId(), tmpPoint, tileType))
			{
				tileData.oper[CAction::CActionAtom::AFTER_OPERATOR] = nBrushType;
				oper->m_TileData.insert(tileData);
				changed = true;
			}
		}
	}
	return changed;
}

bool CMapEditorGroundCtrl::SetSmallTileType(unsigned int nBrushType, Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	CAction::CTileActionAtom *oper = pNowOper->GetTileActionData();
	CAction::TileGranule tileData;
	unsigned int size = m_pDoc->GetFactTileRect(rect, Nuclear::TST_SMALL, true);
	int nSmallLayerEditID = m_pView->GetSmallLayerEditId();
	CPoint tmpPoint;
	bool changed = false;
	for (tmpPoint.y = rect.top;tmpPoint.y <= rect.bottom; tmpPoint.y++)
	{
		for (tmpPoint.x = rect.left;tmpPoint.x <= rect.right; tmpPoint.x++)
		{
			tileData.x = tmpPoint.x;
			tileData.y = tmpPoint.y;
			tileData.oper[CAction::CActionAtom::BEFORE_OPERATOR] = m_pDoc->GetSmallTileData(nSmallLayerEditID, tmpPoint.x, tmpPoint.y);
			if (m_pDoc->SetTile(nSmallLayerEditID, tmpPoint, nBrushType))
			{
				tileData.oper[CAction::CActionAtom::AFTER_OPERATOR] = nBrushType;
				oper->m_TileData.insert(tileData);
				changed = true;
			}
		}
	}
	return changed;
}

bool CMapEditorGroundCtrl::SetGroundType(unsigned int nBrushType, const Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	switch(m_pView->GetGroundLayerEditId())
	{
	case Nuclear::SubMap::GL_SMALL:
		return SetSmallGroundType(nBrushType, rect, pNowOper);
		break;
	default:
		return SetBiggerGroundType(nBrushType, rect, pNowOper);
		break;
	}
}
bool CMapEditorGroundCtrl::SetSmallGroundType(unsigned int nBrushType, const Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	bool result = false;
	Nuclear::CRECT factRect = rect;
	unsigned int size = m_pDoc->GetFactTileRect(factRect, Nuclear::TST_SMALL, true);
	CAction::TileGranule* ptrTileData = NULL;
	if (size > 0)
	{
		ptrTileData = new CAction::TileGranule[size];
		SetTileModify(ptrTileData, factRect, CAction::CActionAtom::BEFORE_OPERATOR);
	}
	if (m_pDoc->SetType(m_pView->GetSmallLayerEditId(), rect, nBrushType, nBrushType==CMapEditorView::GROUND_TYPE_BRUSH_NULL?true:false))
	{
		SetTileModify(ptrTileData, factRect, CAction::CActionAtom::AFTER_OPERATOR);
		CAction::CTileActionAtom *oper = pNowOper->GetTileActionData();
		unsigned int i;
		for (i=0;i<size;i++)
		{
			CAction::CTileActionAtom::TileDataIterator iter = oper->m_TileData.find(ptrTileData[i]);
			if (iter != oper->m_TileData.end())
			{
				iter->oper[CAction::CActionAtom::AFTER_OPERATOR] = ptrTileData[i].oper[CAction::CActionAtom::AFTER_OPERATOR];
			} else {
				oper->m_TileData.insert(ptrTileData[i]);
			}
		}
		m_pDoc->SetModifiedFlag();
		result = true;
	}
	if (ptrTileData)
		delete (ptrTileData);
	return result;
}

bool CMapEditorGroundCtrl::SetBiggerGroundType(unsigned int nBrushType, const Nuclear::CRECT& rect, LPTRACTION& pNowOper)
{
	bool result = false;
	Nuclear::CRECT factRect = rect;
	Nuclear::TILE_SIZE_TYPE sizeType;
	switch(m_pView->GetGroundLayerEditId())
	{
	case Nuclear::SubMap::GL_BIG:
		sizeType = Nuclear::TST_BIG;
		break;
	case Nuclear::SubMap::GL_MID1:
	case Nuclear::SubMap::GL_MID2:
		sizeType = Nuclear::TST_MIDDLE;
		break;
	}
	unsigned int size = m_pDoc->GetFactTileRect(factRect, sizeType, true);
	CAction::BiggerTileGranule* ptrTileData = NULL;
	if (size > 0)
	{
		ptrTileData = new CAction::BiggerTileGranule[size];
		SetTileModify(ptrTileData, factRect, CAction::CActionAtom::BEFORE_OPERATOR);
	}
	if (m_pDoc->SetType(m_pView->GetGroundLayerEditId(), rect, nBrushType, nBrushType==0?true:false))
	{
		SetTileModify(ptrTileData, factRect, CAction::CActionAtom::AFTER_OPERATOR);
		CAction::CBiggerTileActionAtom *oper = pNowOper->GetBiggerTileActionData();
		unsigned int i;
		for (i=0;i<size;i++)
		{
			CAction::CBiggerTileActionAtom::TileDataIterator iter = oper->m_TileData.find(ptrTileData[i]);
			if (iter != oper->m_TileData.end())
			{
				iter->oper[CAction::CActionAtom::AFTER_OPERATOR] = ptrTileData[i].oper[CAction::CActionAtom::AFTER_OPERATOR];
			} else {
				oper->m_TileData.insert(ptrTileData[i]);
			}
		}
		m_pDoc->SetModifiedFlag();
		result = true;
	}
	if (ptrTileData)
		delete (ptrTileData);
	return result;
}

void CMapEditorGroundCtrl::SetTileModify(CAction::TileGranule* tileData, const Nuclear::CRECT& rect, int type)
{
	int row, col, width, nSmallLayerEditID;
	unsigned int i = 0;
	TileType *ptrTile;
	PMap& map = m_pDoc->GetToolsMap();
	width = map.GetWidthForSmlTiles();
	nSmallLayerEditID = m_pView->GetSmallLayerEditId();
	const Nuclear::SubMap::PtrTileTypeList& layers = m_pDoc->GetGroundLayers();
	for (row=rect.top;row<=rect.bottom;row++)
	{
		ptrTile = layers[nSmallLayerEditID] + row * width + rect.left;
		for (col=rect.left;col<=rect.right;col++)
		{
			tileData[i].oper[type] = *(ptrTile++);
			if (type == CAction::CActionAtom::AFTER_OPERATOR)
			{
				tileData[i].x = col;
				tileData[i].y = row;
			}
			i++;
		}
	}
}

void CMapEditorGroundCtrl::SetTileModify(CAction::BiggerTileGranule* tileData, const Nuclear::CRECT& rect, int type)
{
	int row, col, width;
	Nuclear::SubMap::GroundLayers layer = m_pView->GetGroundLayerEditId();
	BiggerTileType *pTiles, *ptrTile;
	PMap& pmap = m_pDoc->GetToolsMap();
	switch(layer)
	{
	case Nuclear::SubMap::GL_SMALL:
		return;
		break;
	case Nuclear::SubMap::GL_MID1:
		pTiles = pmap.GetMidTileData(0);
		width = pmap.GetWidthForMidTiles();
		break;
	case Nuclear::SubMap::GL_MID2:
		pTiles = pmap.GetMidTileData(1);
		width = pmap.GetWidthForMidTiles();
		break;
	case Nuclear::SubMap::GL_BIG:
		pTiles = pmap.GetMidTileData(0);
		width = pmap.GetWidthForBigTiles();
		break;
	}
	unsigned int i = 0;
	for (row=rect.top;row<=rect.bottom;row++)
	{
		ptrTile = pTiles + row * width + rect.left;
		for (col=rect.left;col<=rect.right;col++)
		{
			tileData[i].oper[type] = *(ptrTile++);
			if (type == CAction::CActionAtom::AFTER_OPERATOR)
			{
				tileData[i].x = col;
				tileData[i].y = row;
			}
			i++;
		}
	}
}

bool CMapEditorGroundCtrl::NewGroundLayer(LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList)
{
	if (pNowOper)
		delete (pNowOper);
	pNowOper = new CAction(CAction::AT_NEW_DELETE_GROUND_LAYER, m_pView->GetLayerEditId());
	CAction::CNewDeleteGroundLayerActionAtom* oper = pNowOper->GetNewDeleteGroundLayerActionData();
#ifdef _DEBUG
	const CToolsMap::TileLayerInfoList layers = m_pDoc->GetGroundLayerInfos();
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = layers;
#else
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = m_pDoc->GetGroundLayerInfos();
#endif
	if (m_pDoc->NewGroundLayer())
	{
#ifdef _DEBUG
		const CToolsMap::TileLayerInfoList layers = m_pDoc->GetGroundLayerInfos();
		oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = layers;
#else
		oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = m_pDoc->GetGroundLayerInfos();
#endif
		oper->m_Data = new TileType[m_pDoc->GetToolsMap().GetSmlTileCount()];
		oper->m_LayerID = m_pDoc->GetGroundLayerInfo(0).layerID;
		memcpy(oper->m_Data, m_pDoc->GetGroundLayers().at(oper->m_LayerID), m_pDoc->GetToolsMap().GetSmlTileSize());
		pOperatorList->DoOneAction(pNowOper);
		pNowOper = NULL;
		m_pDoc->SetModifiedFlag();
		return true;
	}
	delete (pNowOper);
	pNowOper = NULL;
	return false;
}
bool CMapEditorGroundCtrl::DeleteGroundLayer(int layer, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList) 
{
	if (pNowOper)
		delete (pNowOper);
	pNowOper = new CAction(CAction::AT_NEW_DELETE_GROUND_LAYER, m_pView->GetLayerEditId());
	CAction::CNewDeleteGroundLayerActionAtom* oper = pNowOper->GetNewDeleteGroundLayerActionData();
#ifdef _DEBUG
	const CToolsMap::TileLayerInfoList layers = m_pDoc->GetGroundLayerInfos();
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = layers;
#else
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = m_pDoc->GetGroundLayerInfos();
#endif
	oper->m_Data = new TileType[m_pDoc->GetToolsMap().GetSmlTileCount()];
	oper->m_LayerID = m_pDoc->GetGroundLayerInfo(layer).layerID;
	memcpy(oper->m_Data, m_pDoc->GetGroundLayers().at(oper->m_LayerID), m_pDoc->GetToolsMap().GetSmlTileSize());
	if (m_pDoc->DeleteGroundLayer(layer))
	{
#ifdef _DEBUG
		const CToolsMap::TileLayerInfoList layers = m_pDoc->GetGroundLayerInfos();
		oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = layers;
#else
		oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = m_pDoc->GetGroundLayerInfos();
#endif
		pOperatorList->DoOneAction(pNowOper);
		pNowOper = NULL;
		m_pDoc->SetModifiedFlag();
		return true;
	}
	return false;
}


void CMapEditorGroundCtrl::SetLayerShow(int layer, bool bShow, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList)
{
	const CToolsMap::TileLayerInfo& layerInfo =  m_pDoc->GetGroundLayerInfo(layer);

	if (layerInfo.isShow == bShow)
		return;
	if (pNowOper)
		delete pNowOper;
	pNowOper = new CAction(CAction::AT_SINGLE_GROUND_LAYER_INFO, m_pView->GetLayerEditId());
	CAction::CGroundLayerInfoActionAtom* oper = pNowOper->GetSingleGroundLayerInfoActionData();
	oper->m_Layer = layer;
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = layerInfo;
	m_pDoc->SetGroundLayerShow(layer, bShow);
	oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = layerInfo;
	pOperatorList->DoOneAction(pNowOper);
	m_pDoc->SetModifiedFlag();
	pNowOper = NULL;
}

void CMapEditorGroundCtrl::SetGroundLayerName(int layer, const CString& name, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList)
{
	const CToolsMap::TileLayerInfo& layerInfo = m_pDoc->GetGroundLayerInfo(layer);

	if (layerInfo.name == name)
		return;
	if (pNowOper)
		delete pNowOper;
	pNowOper = new CAction(CAction::AT_SINGLE_GROUND_LAYER_INFO, m_pView->GetLayerEditId());
	CAction::CGroundLayerInfoActionAtom* oper = pNowOper->GetSingleGroundLayerInfoActionData();
	oper->m_Layer = layer;
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = layerInfo;
	m_pDoc->SetGroundLayerName(layer, name);
	oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = layerInfo;
	pOperatorList->DoOneAction(pNowOper);
	m_pDoc->SetModifiedFlag();
	pNowOper = NULL;
}


CMapEditorGroundCtrl::~CMapEditorGroundCtrl(void)
{
}

CMapEditorGroundCtrl::CMapEditorGroundCtrl(void)
{
}