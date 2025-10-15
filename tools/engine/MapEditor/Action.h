
#pragma once
#include "..\engine\map\pmap.h"
#include "ImgInfo.h"
#include "MapEditorDoc.h"
#include <vector>
#include <set>

using Nuclear::PMap;
using Nuclear::SubMap::TileType;
using Nuclear::SubMap::BiggerTileType;

class CAction
{
public:
	enum eActionType {	
		AT_CHANGE_CANVAS = 0,
		AT_TILE,
		AT_TILE_COLOR,
		AT_SUPER_TILE,
		AT_BIGGER_TILE,
		AT_WATER,
		AT_OBJECTS,
		AT_SINGLE_GROUND_LAYER_INFO,
		AT_ALL_GROUND_LAYER_INFOS,
		AT_NEW_DELETE_GROUND_LAYER,
		AT_OBJECT_NAMES,
		AT_ELEMENT_BASE,
		AT_DYNAMIC_BASE,
		AT_SINGLE_OBJECT_NAMES,
		AT_TRANSPARENT_OBJECT,
		AT_TRIGGER_OBJECT,
		AT_WATER_AREA,
		AT_TIME_EFFECT,
		AT_MASK_PARAM,
		AT_BACKGROUND_INFO,
		AT_BACKGROUND_DATA,
		AT_BACKGROUND_ORDER,
	};

	class CActionAtom
	{
	public:
		enum
		{
			BEFORE_OPERATOR = 0,
			AFTER_OPERATOR = 1
		};
		int m_LayerID;
		CActionAtom() {};
		virtual ~CActionAtom() {};
		virtual eActionType GetType() = 0;
	};
	class CMaskParamGranule : 
		public Nuclear::XPMaskParam
	{
	public:
		int m_nTime;
		CMaskParamGranule &operator =(const XPMaskParam &param)
		{
			alpha = param.alpha;
			lastAlpha = param.lastAlpha;
			blendMode = param.blendMode;
			picuri = param.picuri;
			return *this;
		}
	};

	class CTimeEffectGranule : 
		public Nuclear::XPColorBalanceParam
	{
	public:
		int m_nTime;
		CTimeEffectGranule &operator =(const XPColorBalanceParam &param)
		{
			fCoefr = param.fCoefr;
			fCoefg = param.fCoefg;
			fCoefb = param.fCoefb;
			fContrastb = param.fContrastb;
			fContrastk = param.fContrastk;
			fLightness = param.fLightness;
			return *this;
		}
	};

	struct SingleElementNameGranule
	{
		Nuclear::SubMap::ObjMapKey m_ObjMapKey;
		Nuclear::PMap::MAP_OBJ_TYPE m_MapObjType;
		CString m_Name;
	};

	struct WaterGranule
	{
		unsigned int x;
		unsigned int y;
		Nuclear::SubMap::WaterTileType oper[2];
		bool operator < (const WaterGranule &other) const
		{
			if (x < other.x)
			{
				return true;
			} else if (x > other.x)
			{
				return false;
			}
			if (y < other.y)
				return true;
			return false;
		}

		bool operator == (const WaterGranule &other) const
		{
			if (x == other.x && y == other.y)
				return true;
			else
				return false;
		}
	};

	struct TileColorGranule
	{
		unsigned int x;
		unsigned int y;
		unsigned int oper[2];
		bool operator < (const TileColorGranule &other) const
		{
			if (x < other.x)
			{
				return true;
			} else if (x > other.x)
			{
				return false;
			}
			if (y < other.y)
				return true;
			return false;
		}

		bool operator == (const TileColorGranule &other) const
		{
			if (x == other.x && y == other.y)
				return true;
			else
				return false;
		}
	};

	struct BiggerTileGranule
	{
		unsigned int x;
		unsigned int y;
		BiggerTileType oper[2];
		bool operator < (const BiggerTileGranule &other) const
		{
			if (x < other.x)
			{
				return true;
			} else if (x > other.x)
			{
				return false;
			}
			if (y < other.y)
				return true;
			return false;
		}

		bool operator == (const BiggerTileGranule &other) const
		{
			if (x == other.x && y == other.y)
				return true;
			else
				return false;
		}
	};

	struct SuperTileGranule
	{
		unsigned int x;
		unsigned int y;
		std::map<int, TileType> oper[2];	
		bool operator < (const SuperTileGranule &other) const
		{
			if (x < other.x)
			{
				return true;
			} else if (x > other.x)
			{
				return false;
			}
			if (y < other.y)
				return true;
			return false;
		}

		bool operator == (const SuperTileGranule &other) const
		{
			if (x == other.x && y == other.y)
				return true;
			else
				return false;
		}
	};

	struct TileGranule
	{
		unsigned int x;
		unsigned int y;
		TileType oper[2];
		bool operator < (const TileGranule &other) const
		{
			if (x < other.x)
			{
				return true;
			} else if (x > other.x)
			{
				return false;
			}
			if (y < other.y)
				return true;
			return false;
		}

		bool operator == (const TileGranule &other) const
		{
			if (x == other.x && y == other.y)
				return true;
			else
				return false;
		}
	};

	class CBackgroundOrderActionAtom : public CActionAtom
	{
	public:
		int m_InfoID[2];
		virtual eActionType GetType() {return AT_BACKGROUND_ORDER;}
	};
	class CBackgroundDataActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::ObjMapKey oper[2];
		int m_InfoID;
		int m_Position;
		virtual eActionType GetType() {return AT_BACKGROUND_DATA;}
	};

	class CBackgroundInfoActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::PBackGoundInfo oper[2];
		int m_InfoID[2];
		virtual eActionType GetType() {return AT_BACKGROUND_INFO;}
	};

	class CMaskParamActionAtom : public CActionAtom
	{
	public:
		CMaskParamGranule oper[2];
		virtual eActionType GetType() {return AT_MASK_PARAM;}
	};

	class CTimeEffectActionAtom : public CActionAtom
	{
	public:
		CTimeEffectGranule oper[2];
		virtual eActionType GetType() {return AT_TIME_EFFECT;}
	};

	class CWaterAreaActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::WaterAreaInfoMap oper[2];
		virtual eActionType GetType() {return AT_WATER_AREA;}
	};

	class CTriggerObjectActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::TriggerInfoMap oper[2];
		virtual eActionType GetType() {return AT_TRIGGER_OBJECT;}
	};

	class CTransparentObjectActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::TransObjInfoMap oper[2];
		virtual eActionType GetType() {return AT_TRANSPARENT_OBJECT;}
	};

	class CSingleObjectNameActionAtom : public CActionAtom
	{
	public:
		SingleElementNameGranule oper[2];
		Nuclear::PMap::MAP_OBJ_TYPE m_MapObjectType;
		std::vector<std::pair<int, unsigned int>> m_Pictures;
		virtual eActionType GetType() {return AT_SINGLE_OBJECT_NAMES;}
	};

	class CObjectNameActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::ObjNameMap oper[Nuclear::PMap::MAP_OBJ_COUNT][2];
		virtual eActionType GetType() {return AT_OBJECT_NAMES;}
	};

	class CNewDeleteGroundLayerActionAtom : public CActionAtom
	{
	public:
		unsigned int m_LayerID;	
		CToolsMap::TileLayerInfoList oper[2];
		Nuclear::SubMap::PtrTileType m_Data;
		CNewDeleteGroundLayerActionAtom() : m_Data(NULL) {};
		~CNewDeleteGroundLayerActionAtom() {
			if (m_Data)
				delete (m_Data);
		}
		virtual eActionType GetType() {return AT_NEW_DELETE_GROUND_LAYER;}
	};

	class CLinkedObjectBaseActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::ObjMapKey m_ObjectMapKey;
		POINT oper[4][2];
		virtual eActionType GetType() {return AT_DYNAMIC_BASE;}
	};

	class CElementBaseActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::ObjMapKey m_ObjectMapKey;
		POINT oper[4][2];
		virtual eActionType GetType() {return AT_ELEMENT_BASE;}
	};

	class CGroundLayerInfosActionAtom : public CActionAtom
	{
	public:
		CToolsMap::TileLayerInfoList oper[2];
		virtual eActionType GetType() {return AT_ALL_GROUND_LAYER_INFOS;}
	};

	class CGroundLayerInfoActionAtom : public CActionAtom
	{
	public:
		int m_Layer;
		CToolsMap::TileLayerInfo oper[2];
		virtual eActionType GetType() {return AT_SINGLE_GROUND_LAYER_INFO;}
	};

	class CObjectsActionAtom : public CActionAtom
	{
	public:
		Nuclear::SubMap::ObjInfoVector m_AtomObjInfo[2];	
		virtual eActionType GetType() {return AT_OBJECTS;}
	};

	class CWaterActionAtom : public CActionAtom
	{
	public:
		typedef std::set<WaterGranule>::iterator WaterIterator;
		std::set<WaterGranule> m_WaterData;
		virtual eActionType GetType() {return AT_WATER;}
	};

	class CTileColorActionAtom : public CActionAtom
	{
	public:
		typedef std::set<TileColorGranule>::iterator TileColorIterator;
		std::set<TileColorGranule> m_TileColorData;
		virtual eActionType GetType() {return AT_TILE_COLOR;}
	};

	class CBiggerTileActionAtom : public CActionAtom
	{
	public:
		typedef std::set<BiggerTileGranule>::iterator TileDataIterator;
		Nuclear::SubMap::GroundLayers m_GroundLayers;
		std::set<BiggerTileGranule> m_TileData;
		virtual eActionType GetType() {return AT_BIGGER_TILE;}
	};

	class CSuperTileActionAtom : public CActionAtom
	{
	public:
		std::set<SuperTileGranule> m_TileData;
		virtual eActionType GetType() {return AT_SUPER_TILE;}
	};

	class CTileActionAtom : public CActionAtom
	{
	public:
		typedef std::set<TileGranule>::iterator TileDataIterator;
		int m_SmallLayerID;
		std::set<TileGranule> m_TileData;
		virtual eActionType GetType() {return AT_TILE;}
	};

	class CCanvasActionAtom : public CActionAtom
	{
	public:
		CToolsMap oper[2];
		virtual eActionType GetType() {return AT_CHANGE_CANVAS;}
	};

private:
	CActionAtom *m_pActionAtomData;
	
public:
	CBackgroundOrderActionAtom* GetBackgroundOrderActionData()
	{
		return dynamic_cast<CBackgroundOrderActionAtom*>(m_pActionAtomData);
	}
	CBackgroundDataActionAtom* GetBackgroundDataActionData()
	{
		return dynamic_cast<CBackgroundDataActionAtom*>(m_pActionAtomData);
	}

	CBackgroundInfoActionAtom* GetBackgroundInfoActionData()
	{
		return dynamic_cast<CBackgroundInfoActionAtom*>(m_pActionAtomData);
	}

	CMaskParamActionAtom* GetMaskParamActionData()
	{
		return dynamic_cast<CMaskParamActionAtom*>(m_pActionAtomData);
	}

	CTimeEffectActionAtom* GetTimeEffectActionData()
	{
		return dynamic_cast<CTimeEffectActionAtom*>(m_pActionAtomData);
	}

	CWaterAreaActionAtom* GetWaterAreaActionData()
	{
		return dynamic_cast<CWaterAreaActionAtom*>(m_pActionAtomData);
	}

	CTriggerObjectActionAtom* GetTriggerObjectActionData()
	{
		return dynamic_cast<CTriggerObjectActionAtom*>(m_pActionAtomData);
	}

	CTransparentObjectActionAtom* GetTransparentObjectsActionData()
	{
		return dynamic_cast<CTransparentObjectActionAtom*>(m_pActionAtomData);
	}

	CSingleObjectNameActionAtom* GetSingleObjectNameActionData()
	{
		return dynamic_cast<CSingleObjectNameActionAtom*>(m_pActionAtomData);
	}

	CLinkedObjectBaseActionAtom* GetLinkedObjectBaseActionData()
	{
		return dynamic_cast<CLinkedObjectBaseActionAtom*>(m_pActionAtomData);
	}

	CElementBaseActionAtom* GetElementBaseActionData()
	{
		return dynamic_cast<CElementBaseActionAtom*>(m_pActionAtomData);
	}

	CObjectNameActionAtom* GetObjectNameActionData()
	{
		return dynamic_cast<CObjectNameActionAtom*>(m_pActionAtomData);
	}

	CNewDeleteGroundLayerActionAtom* GetNewDeleteGroundLayerActionData()
	{
		return dynamic_cast<CNewDeleteGroundLayerActionAtom*>(m_pActionAtomData);
	}

	CGroundLayerInfosActionAtom* GetAllGroundLayerInfosActionData()
	{
		return dynamic_cast<CGroundLayerInfosActionAtom*>(m_pActionAtomData);
	}
	CGroundLayerInfoActionAtom* GetSingleGroundLayerInfoActionData()
	{
		return dynamic_cast<CGroundLayerInfoActionAtom*>(m_pActionAtomData);
	}

	CObjectsActionAtom* GetObjectsActionData()
	{
		return dynamic_cast<CObjectsActionAtom*>(m_pActionAtomData);
	}

	CWaterActionAtom* GetWaterActionData()
	{
		return dynamic_cast<CWaterActionAtom*>(m_pActionAtomData);
	}

	CBiggerTileActionAtom* GetBiggerTileActionData()
	{
		return dynamic_cast<CBiggerTileActionAtom*>(m_pActionAtomData);
	}

	CTileColorActionAtom* GetTileColorActionData()
	{
		return dynamic_cast<CTileColorActionAtom*>(m_pActionAtomData);
	}

	CSuperTileActionAtom* GetSuperTileActionData()
	{
		return dynamic_cast<CSuperTileActionAtom*>(m_pActionAtomData);
	}

	CTileActionAtom* GetTileActionData()
	{
		return dynamic_cast<CTileActionAtom*>(m_pActionAtomData);
	}

	CCanvasActionAtom* GetCanvasActionData()
	{
		return dynamic_cast<CCanvasActionAtom*>(m_pActionAtomData);
	}

	int GetActionLayerID() const { return m_pActionAtomData->m_LayerID; }

	int GetActionType() const { return m_pActionAtomData->GetType(); }
	~CAction(void);
	CAction(eActionType operatorType, int layerID);
};


typedef CAction *LPTRACTION;