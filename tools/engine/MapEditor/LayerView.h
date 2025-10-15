
#pragma once
#include <vector>
#include "ImgInfo.h"
#include "..\engine\map\pmap.h"
#include "..\engine\common\recttopologylist.h"
#include "..\engine\map\pdistortionobject.h"

namespace Nuclear {
	class Effect;
	struct XPBase;
}

class Sprite;

class CLayerView
{
private:
	static const int ADJ = 29;
public:
	enum LAYER_EDIT_ID 
	{
		LEI_GROUND = 0,
		LEI_WATER_1,
		LEI_WATER_2,
		LEI_LOW_1,
		LEI_LOW_2,
		LEI_LOW_3,
		LEI_LOW_4,
		LEI_LOW_5,
		LEI_LOW_6,
		LEI_MID,
		LEI_HIGH_1,
		LEI_HIGH_2,
		LEI_TRANS,
		LEI_TRIGGER,
		LEI_WATER,	
		LEI_BACKGROUND,
		LEI_GAMING,
	};

	enum LAYER_OBJ_TYPE
	{
		LOT_ELEMENT = Nuclear::PMap::MAP_OBJ_ELEMENT << ADJ,	 	 
		LOT_EFFECT = Nuclear::PMap::MAP_OBJ_EFFECT << ADJ,	 		 
		LOT_LINKEDOBJECT = Nuclear::PMap::MAP_OBJ_LINKEDOBJ << ADJ, 	 
		LOT_DISTORT = Nuclear::PMap::MAP_OBJ_DISTORT << ADJ,		 

		LOT_SPRITE = 5 << ADJ,										 
		LOT_TRIGGER_EFFECT = 6 << ADJ								 
	};

private:
	static const unsigned int TYPE_MASK = 7 << ADJ;					 
public:
	virtual bool IsLayerShow(LAYER_EDIT_ID layer) { return true; }
	virtual bool CheckDistortObjectUpdateList(char layerID, unsigned int objID, Nuclear::PDistortionObject* pDistObj, const Nuclear::CPOINT &pos, const std::wstring& name) = 0;
	virtual bool CheckLinkedObjectUpdateList(char layerID, unsigned int objID, Nuclear::PLinkedObject* pLinkedObj, const Nuclear::CPOINT &pos) = 0;
	virtual std::list<Nuclear::Effect*>* GetMiddleTriggerEffect() = 0;
	virtual std::vector<Sprite*>* GetSprites() = 0;
	virtual bool GetEffectBase(const std::wstring& name, Nuclear::XPBase &base) = 0;
	virtual Nuclear::PDistortionObject* GetDistortInfo(const std::wstring& name) = 0;
	virtual Nuclear::PLinkedObject* GetLinkedObjectInfo(const std::wstring& name) = 0;
	virtual LPImgInfo GetImgInfo(const std::wstring& name) = 0;
	virtual Nuclear::CRECT GetViewport() const = 0;
	static LAYER_OBJ_TYPE ConvertObjectType(Nuclear::PMap::MAP_OBJ_TYPE type)
	{
		return static_cast<LAYER_OBJ_TYPE>(type << ADJ);
	}
	static bool ConvertObjectType(LAYER_OBJ_TYPE layerObjType, Nuclear::PMap::MAP_OBJ_TYPE& type)
	{
		if (!CheckIsObject(layerObjType))
			return false;
		type = static_cast<Nuclear::PMap::MAP_OBJ_TYPE>(layerObjType >> ADJ);
		return true;
	}
	static bool CheckIsObject(LAYER_OBJ_TYPE layerObjType)
	{
		if (layerObjType == LOT_SPRITE || layerObjType == LOT_TRIGGER_EFFECT)
			return false;
		return true;
	}
	static Nuclear::SortBaseId GetSortBaseId(Nuclear::SubMap::ObjectId objID, LAYER_OBJ_TYPE type)
	{
		return objID | type;
	}

	static bool GetObjectType(Nuclear::SortBaseId id, Nuclear::PMap::MAP_OBJ_TYPE& type)
	{
		return ConvertObjectType(GetObjectType(id), type);
	}
	static Nuclear::SubMap::ObjectId GetObjectId(Nuclear::SortBaseId id)
	{
		return id & (~TYPE_MASK);
	}

	static LAYER_OBJ_TYPE GetObjectType(Nuclear::SortBaseId id)
	{
		return static_cast<LAYER_OBJ_TYPE>(id & TYPE_MASK);
	}
};
