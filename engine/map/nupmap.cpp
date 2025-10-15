#include "../common/nulog.h"
#include <algorithm>
#include "nupmap.h"

using Nuclear::SubMap::TileType;
using Nuclear::SubMap::uBiggerTileType;

namespace Nuclear
{
	NuclearMap::NuclearMap(void)
	{
		m_nWidth = 0;
		m_nHeight = 0;
		m_nSign = 0;
		m_nVersion = MAP_VERSION;
		m_Objects[MAP_OBJ_ELEMENT].Init();
		m_Objects[MAP_OBJ_ELEMENT].SetNoPos();
		m_Objects[MAP_OBJ_ELEMENT].SetNoScale();
		m_Objects[MAP_OBJ_ELEMENT].SetNoRotation();

		m_Objects[MAP_OBJ_EFFECT].Init();

		m_Objects[MAP_OBJ_LINKEDOBJ].Init();
		m_Objects[MAP_OBJ_LINKEDOBJ].SetNoScale();
		m_Objects[MAP_OBJ_LINKEDOBJ].SetNoRotation();

		m_Objects[MAP_OBJ_DISTORT].Init();
		m_Objects[MAP_OBJ_DISTORT].SetNoScale();
		m_Objects[MAP_OBJ_DISTORT].SetNoRotation();
	}

	NuclearMap::~NuclearMap(void)
	{
	}

	bool NuclearMap::Init()
	{
		return m_Ground.Init(m_nWidth, m_nHeight);
	}

	bool NuclearMap::Init(int width, int height)
	{

		if (width == 0 || height == 0)
			return false;
		if (width % TILE_WIDTH != 0)
			return false;
		if (height % TILE_HEIGHT != 0)
			return false;
		m_nWidth = width;
		m_nHeight = height;

		return Init();
	}

	NuclearOStream& NuclearMap::marshal(NuclearOStream &os) const
	{
		const char fileHead[4] = {'R','M','A','P'};
		os.push_byte(fileHead, sizeof(char) * 4);
		os << MAP_VERSION;
		os << m_nWidth;
		os << m_nHeight;
		os << m_nSign;
		m_Ground.marshal(os);
		m_Water.marshal(os);
		for (int i=0;i<MAP_OBJ_COUNT;++i)
		{
			m_Objects[i].marshal(os);
		}
		m_TransObjects.marshal(os);
		m_TimeEffect.marshal(os);
		m_Mask.marshal(os);
		m_triggerObjs.marshal(os);
		m_BackGround.marshal(os);
		return os;
	}

	const INuclearStream& NuclearMap::unmarshal(const INuclearStream &os)
	{
		char fileHead[4];
		os.pop_byte(fileHead, sizeof(char) * 4);

		if (fileHead[0] != 'R' || fileHead[1] != 'M' || fileHead[2] != 'A' || fileHead[3] != 'P' )
		{
			XPLOG_ERROR(L"rmap fileHead Error\n");
			throw Exception();
		}
		os >> m_nVersion;
		os >> m_nWidth;
		os >> m_nHeight;
		if (m_nVersion > MAP_NO_SIGN)
			os >> m_nSign;
		int colorWidth = 0;
		int colorHeight = 0;
		Init();
		if (m_nVersion <= NuclearMap::MAP_OLD_GROUND_COLOR)
		{
			os >> colorWidth;
			os >> colorHeight;
		}
		m_Ground.unmarshal(os, m_nVersion, colorWidth, colorHeight);
		m_Water.unmarshal(os, m_nVersion);
		for (int i=0;i<MAP_OBJ_COUNT;++i)
		{
			m_Objects[i].unmarshal(os, m_nVersion);
		}
		m_TransObjects.unmarshal(os, m_nVersion);
		m_TimeEffect.unmarshal(os, m_nVersion);
		m_Mask.unmarshal(os, m_nVersion);
		m_triggerObjs.unmarshal(os, m_nVersion);
		m_BackGround.unmarshal(os, m_nVersion);
		return os;
	}

	TileType NuclearMap::GetSmlTileData(int layer, int x, int y) const
	{
		return m_Ground.GetSmlTileData(layer, x, y);
	}

	bool NuclearMap::SetSmlTileData(int layer, int x, int y, TileType data)
	{
		return m_Ground.SetSmlTileData(layer, x, y, data);
	}

	uBiggerTileType NuclearMap::GetMidTileData(int layer, int x, int y) const
	{
		return m_Ground.GetMidTileData(layer, x, y);
	}

	bool NuclearMap::SetMidTileData(int layer, int x, int y, uBiggerTileType data)
	{
		return m_Ground.SetMidTileData(layer, x, y, data);
	}

	uBiggerTileType NuclearMap::GetBigTileData(int x, int y) const
	{
		return m_Ground.GetBigTileData(x, y);
	}

	bool NuclearMap::SetBigTileData(int x, int y, uBiggerTileType data)
	{
		return m_Ground.SetBigTileData(x, y, data);
	}

	bool NuclearMap::ReAddObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjID objID)
	{
		if ( layerID >= GetObjLayerCount(objType))
			return false;
		if ( objID >= m_Objects[objType].GetObjSize(layerID) )
			return false;
		m_Objects[objType].ReAddObj(layerID, objID);
		return true;
	}


	bool NuclearMap::DeleteObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjID objID)
	{
		if ( layerID >= GetObjLayerCount(objType))
			return false;
		if ( objID >= m_Objects[objType].GetObjSize(layerID) )
			return false;
		m_Objects[objType].DeleteObj(layerID, objID);
		return true;
	}

	void NuclearMap::DestoryLastObj(MAP_OBJ_TYPE objType, int layerID)
	{
		if ( layerID >= GetObjLayerCount(objType))
			return;
		m_Objects[objType].DestoryLastObj(layerID);
	}

}
