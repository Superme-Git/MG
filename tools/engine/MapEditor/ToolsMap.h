#pragma once
#include "..\engine\map\pmap.h"

class CAllObjectResourceDlg;
class CMapEditorDoc;
class CActionList;
class CReplaceObjs;

class CToolsMap :
	public Nuclear::PMap
{
	friend class CActionList;
	friend class CMapEditorDoc;
	friend class CReplaceObjs;
	friend class CAllObjectResourceDlg;
public:
	typedef std::set<std::wstring> FileSet;
	struct FileStates
	{
		FileSet usedFiles;
		FileSet missFiles;
	};
	struct MapFileState
	{
		FileStates groundFile;
		FileStates elementFile;
		FileStates effectFile;
		FileStates linkedObjectFile;
		FileStates disFile;
	};

	struct TileLayerInfo
	{
		int layerID;
		bool isShow;
		CString name;
		bool operator== (const TileLayerInfo& x) const
		{
			if (layerID == x.layerID && isShow == x.isShow)
			{
				return true;
			}
			return false;
		}
	};
	typedef std::vector<TileLayerInfo> TileLayerInfoList;

	static const int TOOLS_VERSION = 1;

private:
	bool m_bIsExportImport;
	CToolsMap(const CToolsMap&);
	Nuclear::SubMap::ObjMapKey m_nMaxObjMapKey[PMap::MAP_OBJ_COUNT];

	std::map<std::wstring, Nuclear::SubMap::ObjMapKey> m_mapNamesToObjMapKey[PMap::MAP_OBJ_COUNT];
	TileLayerInfoList m_GroundLayerInfoArray;	
	Nuclear::SubMap::PtrTileTypeList m_GroundLayersArray;	

public:
	void ModifyWaterAreaCanvas(const Nuclear::CRECT &oldMap, const Nuclear::CRECT &newMap);
	void ModifyTriggerCanvas(const Nuclear::CRECT &oldMap, const Nuclear::CRECT &newMap);
	void MargeMap(CToolsMap& from);	

	bool SwapBackgroundInfo(int id1, int id2);
	bool DestoryLastBackgroundInfo();	
	bool ReAddBackgroundInfo(int id);
	bool DelBackgroundInfo(int id);
	bool ModifyBackgroundInfo(int id, const Nuclear::SubMap::PBackGoundInfo &info);
	int AddBackgroundInfo(const Nuclear::SubMap::PBackGoundInfo &info, Nuclear::SubMap::ObjMapKey* pPics);

	void ResetMapNamesToObjMapKeys();


	Nuclear::SubMap::ObjMapKey GetObjMapKey(PMap::MAP_OBJ_TYPE type, const std::wstring &name)
	{
		Nuclear::SubMap::ObjMapKey result;
		if (m_mapNamesToObjMapKey[type].find(name) == m_mapNamesToObjMapKey[type].end())
		{
			result = (++m_nMaxObjMapKey[type]);
			m_mapNamesToObjMapKey[type][name] = result;

			m_objs[type].SetObjFileName(result, name);
		} else {
			result = m_mapNamesToObjMapKey[type][name];
		}
		return result;
	}
	bool CheckObjMapKey(PMap::MAP_OBJ_TYPE type, const std::wstring &name, Nuclear::SubMap::ObjMapKey &key)
	{
		std::map<std::wstring, Nuclear::SubMap::ObjMapKey>::iterator iter;
		iter = m_mapNamesToObjMapKey[type].find(name);
		if (iter == m_mapNamesToObjMapKey[type].end())
		{
			return false;
		} else {
			key = iter->second;
			return true;
		}
	}

	void SetObjKey(PMap::MAP_OBJ_TYPE type, const std::wstring &name, const Nuclear::SubMap::ObjMapKey &key)
	{
		m_mapNamesToObjMapKey[type][name] = key;
	}
	void DeleteObjKey(PMap::MAP_OBJ_TYPE type, const std::wstring &name)
	{
		m_mapNamesToObjMapKey[type].erase(name);
	}
	void SaveTilesToGround();



	void SaveTilesFromGround();
	void RandomTiles(int layer, unsigned char type, int maxTileNum) 
	{
		int width = GetWidthForSmlTiles();;
		int height = GetHeightForSmlTiles();;
		int tileNum = 0;


		Nuclear::SubMap::TileType *ptrTile = m_GroundLayersArray[m_GroundLayerInfoArray.at(layer).layerID];
		Nuclear::SubMap::TileType typeMsk;
		tileNum = maxTileNum;
		int count = width * height;
		typeMsk = Nuclear::SubMap::PGround::TILE_NULL;
		typeMsk = Nuclear::SubMap::PGround::SetGroundType(typeMsk, type);
		typeMsk = Nuclear::SubMap::PGround::SetTileType(typeMsk, Nuclear::SubMap::PGround::TILE_SOLID);
		while (count--)
		{
			*(ptrTile++) = typeMsk | (rand() % tileNum);
		}
		ptrTile = NULL;
	}

	void FillSmallGroundLayer(int layer, Nuclear::SubMap::TileType tile) 
	{
		unsigned int tileCount = GetSmlTileCount();
		unsigned int i;
		Nuclear::SubMap::TileType *ptrTiles = m_GroundLayersArray[m_GroundLayerInfoArray.at(layer).layerID];
		for (i=0;i<tileCount;i++)
		{
			ptrTiles[i] = Nuclear::SubMap::PGround::TILE_NULL;
		}
	}


	void SetGroundLayerName(int layer, const CString &name) 
	{
		m_GroundLayerInfoArray.at(layer).name = name;
	}

	void SetGroundLayerShow(int layer, bool show) 
	{
		m_GroundLayerInfoArray.at(layer).isShow = show;
	}
	void SetGroundLayerID(int layer, int layerID) 
	{
		m_GroundLayerInfoArray.at(layer).layerID = layerID;
	}
	Nuclear::SubMap::TileType GetSmallTileData(int layer, int x, int y) const
	{
		int pos = m_Ground.GetSmlTilePos(x, y);
		if (pos == -1)
		{
			return Nuclear::SubMap::PGround::TILE_NULL;
		}
		return m_GroundLayersArray[layer][pos];
	}
	const TileLayerInfo& GetGroundLayerInfo(int layer) const {return m_GroundLayerInfoArray.at(layer);}

	const TileLayerInfoList& GetGroundLayerInfos() const {return m_GroundLayerInfoArray;}
	const Nuclear::SubMap::PtrTileTypeList& GetGroundLayers() const {return m_GroundLayersArray;}
	bool DeleteGroundLayer(int layer);
	bool NewGroundLayer(int count = 0, int size = 0);
	static bool ExpRes(LPCTSTR fn, const MapFileState &res);


	void CleanDistortObjects(FileStates &res, FileStates &eleres);
	void CleanLinkedObjects(FileStates &res, FileStates &eleres);
	bool CheckEffectRefCount(const std::wstring &name, FileStates &res);
	void CleanEffect(FileStates &res);
	void CleanElement(FileStates &res);
	void CleanGround(FileStates &res);	
	bool CleanMap(MapFileState &res);	
	bool ImportFromFile(LPCTSTR fn);


	bool ExportToFile(LPCTSTR fn, MapFileState &res);
	CToolsMap& operator=(const CToolsMap&);

	const Nuclear::XIStream& unmarshal(const Nuclear::XIStream &);

	Nuclear::XOStream& marshal(Nuclear::XOStream &) const;
	virtual ~CToolsMap(void);


	CToolsMap(void);

};