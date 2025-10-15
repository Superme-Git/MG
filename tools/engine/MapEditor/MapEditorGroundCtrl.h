
#pragma once
#include "..\engine\map\pmap.h"
#include "MapEditorDoc.h"
#include "Action.h"
#include "ActionList.h"

class CMapEditorView;

class CMapEditorGroundCtrl
{
private:
	CMapEditorView *m_pView;
	CMapEditorDoc *m_pDoc;
public:
	void Init(CMapEditorDoc *pDoc, CMapEditorView* pView)
	{
		m_pDoc = pDoc;
		m_pView = pView;
	}
	~CMapEditorGroundCtrl(void);
	CMapEditorGroundCtrl(void);

private:
	bool SetBiggerTileType(unsigned int nBrushType, Nuclear::CRECT& rect, LPTRACTION& pNowOper);
	bool SetSmallTileType(unsigned int nBrushType, Nuclear::CRECT& rect, LPTRACTION& pNowOper);
	bool SetBiggerGroundType(unsigned int nBrushType, const Nuclear::CRECT& rect, LPTRACTION& pNowOper);
	bool SetSmallGroundType(unsigned int nBrushType, const Nuclear::CRECT& rect, LPTRACTION& pNowOper);
	void SetTileModify(CAction::BiggerTileGranule* tileData, const Nuclear::CRECT& rect, int type);
	void SetTileModify(CAction::TileGranule* tileData, const Nuclear::CRECT& rect, int type);

public:
	bool SetWaterType(Nuclear::SubMap::WaterTileType type, Nuclear::CRECT& rect, LPTRACTION& pNowOper);
	bool SetTileColorType(unsigned int nBrushType, Nuclear::CRECT& rect, LPTRACTION& pNowOper);
	bool SuperDeleteTile(Nuclear::CRECT& rect, LPTRACTION& pNowOper);

	bool SetTileType(unsigned int nBrushType, Nuclear::CRECT& rect, LPTRACTION& pNowOper);
	bool SetGroundType(unsigned int nBrushType, const Nuclear::CRECT& rect, LPTRACTION& pNowOper);
	bool NewGroundLayer(LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList);
	bool DeleteGroundLayer(int layer, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList); 
	void SetLayerShow(int layer, bool bShow, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList); 
	void SetGroundLayerName(int layer, const CString& name, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList);
};
