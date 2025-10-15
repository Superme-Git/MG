


#pragma once
#include <vector>

#include "ToolsMap.h"
#include "..\engine\map\ppathmap.h"

using Nuclear::PMap;
using Nuclear::SubMap::TileType;
using Nuclear::SubMap::BiggerTileType;

class CActionList;
class CMapEditorElementsCtrl;
class CMapEditorDoc : public CDocument
{
public:
	struct sModifyCanvasParam
	{
		int m_NewWidth;
		int m_NewHeight;
		int m_ModifyMode;
		std::wstring m_EffectName[Nuclear::SubMap::PGround::WATER_EFFECT_COUNT];
	};

protected: 
	DECLARE_DYNCREATE(CMapEditorDoc)
	CMapEditorDoc();

private:
	bool SetTileForLine(Nuclear::TILE_SIZE_TYPE tileSize, Nuclear::SubMap::PtrBiggerTileType pTile, int tileCount, int step, unsigned char toSetTileType, unsigned char type, bool isDelete);
	bool SetTileForLine(Nuclear::SubMap::PtrTileType pTile, int tileCount, int step, unsigned char toSetTileType, unsigned char type, bool isDelete);
	static TileType GetRandomTile(TileType tile, unsigned char count)
	{
		unsigned char randomNum;

		if (count > 1)
		{
			randomNum = rand() % count;	
		} else {
			randomNum = 0;
		}
		return Nuclear::SubMap::PGround::SetTileNumber(tile, randomNum);
	}
	BiggerTileType GetRendomBiggerTile(BiggerTileType tile, unsigned char count) const
	{
		unsigned char randomNum;

		if (count > 1)
		{
			randomNum = rand() % count;	
		} else {
			randomNum = 0;
		}
		randomNum += 15;
		tile.tileID = randomNum;
		return tile;
	}
	bool ModifyCanvasForBackground(Nuclear::CRECT oldMap, Nuclear::CRECT newMap);
	bool ModifyCanvasForObjects(Nuclear::CRECT oldMap, Nuclear::CRECT newMap);
	bool ModifyCanvasForTile(int newWidth, int newHeight, int modifyMode, Nuclear::CRECT oldMap, Nuclear::CRECT newMap);
	int CalculateBiggerTileNum(Nuclear::TILE_SIZE_TYPE tileSize, unsigned char type);

	int CalculateTileNum(unsigned char type, unsigned char transType = Nuclear::SubMap::PGround::TILE_SOLID);
	int CalculateFileCountFromPath(CString fileName);

	bool FillRect(int layer, Nuclear::CRECT rect, unsigned char type);
	void SetAutoSave();
	friend class CMapEditorEffectsCtrl;

	friend class CMapEditorElementsCtrl;
	friend class CActionList;
	CToolsMap m_ToolsMap;
	CSize m_DocSize;
	bool m_bInit;
	bool m_bPictureInit;

public:
	
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

	virtual ~CMapEditorDoc();
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);

	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual void Serialize(CArchive& ar);
	virtual BOOL OnNewDocument();
	void ModifyTriggerArea(Nuclear::SubMap::TriggerID triggerID, const Nuclear::CRECT& newArea);

	void RemoveTriggerArea(Nuclear::SubMap::TriggerID triggerID);
	Nuclear::SubMap::TriggerID AddTriggerArea(Nuclear::SubMap::TriggerInfoID infoID, const Nuclear::CRECT& area);
	void SetTriggerInfos(const Nuclear::SubMap::TriggerInfoMap& pmap);

	const Nuclear::SubMap::TriggerInfoMap& GetTriggerInfos() const;
	void ModifyTriggerInfoArray(Nuclear::SubMap::TriggerInfoID infoID, const std::wstring &name, const Nuclear::SubMap::TriggerInfoVet& obj);

	bool RemoveTriggerInfoArray(Nuclear::SubMap::TriggerInfoID infoID);
	Nuclear::SubMap::TriggerInfoID AddTriggerInfoArray(const std::wstring &name, const Nuclear::SubMap::TriggerInfoVet& obj);
	void SetObjectNameMap(PMap::MAP_OBJ_TYPE type, const Nuclear::SubMap::ObjNameMap& nameMap);
	void DeleteObjectNameMap(PMap::MAP_OBJ_TYPE type, const Nuclear::SubMap::ObjMapKey key);

	void SetObjectNameMap(PMap::MAP_OBJ_TYPE type, const Nuclear::SubMap::ObjMapKey key, const std::wstring &name);
	const Nuclear::SubMap::ObjInfoVector& GetConstObjectInfo(PMap::MAP_OBJ_TYPE type, unsigned char layerID) const { return m_ToolsMap.GetConstObjInfo(type, layerID); }
	Nuclear::SubMap::ObjectId AddObject(PMap::MAP_OBJ_TYPE type, int layerID, Nuclear::SubMap::ObjMapKey mapKey, const RECT& rect, const POINT& point, unsigned int attachColor);
	Nuclear::SubMap::ObjMapKey GetObjectMapKey(PMap::MAP_OBJ_TYPE type, const std::wstring &name, CActionList* pOperatorList) { return m_ToolsMap.GetObjMapKey(type, name); }
	bool CheckObjectMapKey(PMap::MAP_OBJ_TYPE type, const std::wstring &name, Nuclear::SubMap::ObjMapKey &key) { return m_ToolsMap.CheckObjMapKey(type, name, key); }
	void SetObjectKey(PMap::MAP_OBJ_TYPE type, const std::wstring &name, const Nuclear::SubMap::ObjMapKey &key) { m_ToolsMap.SetObjKey(type, name, key); }
	void DeleteObjectKey(PMap::MAP_OBJ_TYPE type, const std::wstring &name) { m_ToolsMap.DeleteObjKey(type, name); }
	unsigned int GetFactTileRect(Nuclear::CRECT& rect, Nuclear::TILE_SIZE_TYPE tileSize, bool isCloseRange = false);


	bool DeleteGroundLayer(int layer)
	{
		return m_ToolsMap.DeleteGroundLayer(layer);
	}
	bool NewGroundLayer(int count = 0, int size = 0) { return m_ToolsMap.NewGroundLayer(count, size); }
	void SetGroundLayerName(int layer, const CString &name) 
	{
		m_ToolsMap.SetGroundLayerName(layer, name);
	}
	void SetGroundLayerShow(int layer, bool show = true) 
	{
		m_ToolsMap.SetGroundLayerShow(layer, show);
	}
	void SetGroundLayerID(int layer, int layerID) 
	{
		m_ToolsMap.SetGroundLayerID(layer, layerID);
	}
	BiggerTileType GetBiggerTileData(Nuclear::SubMap::GroundLayers layer, int x, int y) const
	{
		int pos;
		BiggerTileType* pTile;
		switch(layer)
		{
		case Nuclear::SubMap::GL_SMALL:
			return Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
			break;
		case Nuclear::SubMap::GL_MID1:
			pTile = m_ToolsMap.GetMidTileData(0);
			pos = m_ToolsMap.m_Ground.GetMidTilePos(x, y);
			break;
		case Nuclear::SubMap::GL_MID2:
			pTile = m_ToolsMap.GetMidTileData(1);
			pos = m_ToolsMap.m_Ground.GetMidTilePos(x, y);
			break;
		case Nuclear::SubMap::GL_BIG:
			pTile = m_ToolsMap.GetBigTileData();
			pos = m_ToolsMap.m_Ground.GetBigTilePos(x, y);
			break;
		}
		if (pos == -1)
		{
			return Nuclear::SubMap::PGround::BIGGER_TILE_NULL;
		}
		return pTile[pos];
	}
	TileType GetSmallTileData(int layer, int x, int y) const { return m_ToolsMap.GetSmallTileData(layer, x, y); }

	const CToolsMap::TileLayerInfo& GetGroundLayerInfo(int layer) const {return m_ToolsMap.GetGroundLayerInfo(layer);}
	const CToolsMap::TileLayerInfoList& GetGroundLayerInfos() const {return m_ToolsMap.GetGroundLayerInfos();}
	const Nuclear::SubMap::PtrTileTypeList& GetGroundLayers() const {return m_ToolsMap.GetGroundLayers();}
	bool SetTileForcibly(Nuclear::SubMap::GroundLayers layer, int x, int y, BiggerTileType tile);
	bool SetTileForcibly(int layer, int x, int y, TileType tile);
	bool SetTile(Nuclear::SubMap::GroundLayers layer, CPoint point, BiggerTileType tile);
	bool SetTile(int layer, CPoint point, TileType tile);
	bool SetType(Nuclear::SubMap::GroundLayers layer, Nuclear::CRECT rect, unsigned char type, bool isDelete = false);
	bool SetType(int layer, Nuclear::CRECT rect, unsigned char type, bool isDelete = false);
	void ResetAllObjectRects();
	bool GetAvailableRectInMaps(int newWidth, int newHeight, int modifyMode, Nuclear::CRECT& inOldMap, Nuclear::CRECT& inNewMap);
	bool ModifyCanvas(const sModifyCanvasParam& param);
	bool IsPictureInited() const { return m_bPictureInit; }
	void SetPictureInited(bool f) { m_bPictureInit = f; }
	bool IsInited() const { return m_bInit; }
	CToolsMap& GetToolsMap() 
	{ 
		return m_ToolsMap;
	}
	const CToolsMap& GetToolsMap() const { return m_ToolsMap; }
	CSize GetDocSize(){return m_DocSize;}
	void ExportToFile(const CString& fileName);
	void AutoSave();

protected:
	DECLARE_MESSAGE_MAP()
	
};


