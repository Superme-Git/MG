#include "stdafx.h"
#include "..\common\log.h"
#include <algorithm>
#include "pmap.h"

using Nuclear::SubMap::TileType;
using Nuclear::SubMap::BiggerTileType;

namespace Nuclear
{
	PMap::PMap(void)
	{
		m_nWidth = 0;
		m_nHeight = 0;
		m_nSign = 0;
		m_nVersion = MAP_VERSION;
		m_objs[MAP_OBJ_ELEMENT].Init();
		m_objs[MAP_OBJ_ELEMENT].SetNoPos();

		m_objs[MAP_OBJ_EFFECT].Init();
		m_objs[MAP_OBJ_EFFECT].SetNoColor();

		m_objs[MAP_OBJ_LINKEDOBJ].Init();
		m_objs[MAP_OBJ_DISTORT].Init();

		// yeqing 2015-11-07
		//m_objs[MAP_OBJ_SPINE].Init();
	}

	PMap::~PMap(void)
	{
	}

	bool PMap::Init()
	{
		return m_Ground.Init(m_nWidth, m_nHeight);
	}

	bool PMap::Init(int width, int height)
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

	XOStream& PMap::marshal(XOStream &os) const
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
			m_objs[i].marshal(os);
		}
		m_transObjs.marshal(os);
		m_timeEffect.marshal(os);
		m_mask.marshal(os);
		m_triggerObjs.marshal(os);
		m_backGround.marshal(os);
		return os;
	}

	const XIStream& PMap::unmarshal(const XIStream &os)
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
		if (m_nVersion <= PMap::MAP_OLD_GROUND_COLOR)
		{
			os >> colorWidth;
			os >> colorHeight;
		}
		m_Ground.unmarshal(os, m_nVersion, colorWidth, colorHeight);
		m_Water.unmarshal(os, m_nVersion);
		for (int i=0;i<MAP_OBJ_COUNT;++i)
		{
			m_objs[i].unmarshal(os, m_nVersion);
		}
		m_transObjs.unmarshal(os, m_nVersion);
		m_timeEffect.unmarshal(os, m_nVersion);
		m_mask.unmarshal(os, m_nVersion);
		m_triggerObjs.unmarshal(os, m_nVersion);
		m_backGround.unmarshal(os, m_nVersion);
		return os;
	}

	TileType PMap::GetSmlTileData(int layer, int x, int y) const
	{
		return m_Ground.GetSmlTileData(layer, x, y);
	}

	bool PMap::SetSmlTileData(int layer, int x, int y, TileType data)
	{
		return m_Ground.SetSmlTileData(layer, x, y, data);
	}

	BiggerTileType PMap::GetMidTileData(int layer, int x, int y) const
	{
		return m_Ground.GetMidTileData(layer, x, y);
	}

	bool PMap::SetMidTileData(int layer, int x, int y, BiggerTileType data)
	{
		return m_Ground.SetMidTileData(layer, x, y, data);
	}

	BiggerTileType PMap::GetBigTileData(int x, int y) const
	{
		return m_Ground.GetBigTileData(x, y);
	}

	bool PMap::SetBigTileData(int x, int y, BiggerTileType data)
	{
		return m_Ground.SetBigTileData(x, y, data);
	}

	bool PMap::ReAddObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjectId objID)
	{
		if ( layerID >= GetObjLayerCount(objType))
			return false;
		if ( objID >= m_objs[objType].GetObjSize(layerID) )
			return false;
		m_objs[objType].ReAddObj(layerID, objID);
		return true;
	}


	bool PMap::DeleteObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjectId objID)
	{
		if ( layerID >= GetObjLayerCount(objType))
			return false;
		if ( objID >= m_objs[objType].GetObjSize(layerID) )
			return false;
		m_objs[objType].DeleteObj(layerID, objID);
		return true;
	}

	void PMap::DestoryLastObj(MAP_OBJ_TYPE objType, int layerID)
	{
		if ( layerID >= GetObjLayerCount(objType))
			return;
		m_objs[objType].DestoryLastObj(layerID);
	}

}
