
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

	struct WaterOperator : public CActionAtom
	{
		typedef std::set<WaterGranule>::iterator WaterIterator;
		std::set<WaterGranule> WaterData;
		virtual eActionType GetType() {return AT_WATER;}
	};

	struct TileColorOperator : public CActionAtom
	{
		typedef std::set<TileColorGranule>::iterator TileColorIterator;
		std::set<TileColorGranule> TileColorData;
		virtual eActionType GetType() {return AT_TILE_COLOR;}
	};

	struct BiggerTileOperator : public CActionAtom
	{
		typedef std::set<BiggerTileGranule>::iterator TileDataIterator;
		Nuclear::SubMap::GroundLayers gllayer;
		std::set<BiggerTileGranule> TileData;
		virtual eActionType GetType() {return AT_BIGGER_TILE;}
	};

	struct SuperTileOperator : public CActionAtom
	{
		std::set<SuperTileGranule> TileData;
		virtual eActionType GetType() {return AT_SUPER_TILE;}
	};

	struct TileOperator : public CActionAtom
	{
		typedef std::set<TileGranule>::iterator TileDataIterator;
		int smallLayerID;
		std::set<TileGranule> TileData;
		virtual eActionType GetType() {return AT_TILE;}
	};

	struct CanvasOperator : public CActionAtom
	{
		CToolsMap oper[2];
		virtual eActionType GetType() {return AT_CHANGE_CANVAS;}
	};

private:
	CActionAtom *data;
	
public:
	CBackgroundOrderActionAtom* GetBackgroundOrderOperatorData()
	{
		return dynamic_cast<CBackgroundOrderActionAtom*>(data);
	}
	CBackgroundDataActionAtom* GetBackgroundDataOperatorData()
	{
		return dynamic_cast<CBackgroundDataActionAtom*>(data);
	}

	CBackgroundInfoActionAtom* GetBackgroundInfoOperatorData()
	{
		return dynamic_cast<CBackgroundInfoActionAtom*>(data);
	}

	CMaskParamActionAtom* GetMaskParamOperatorData()
	{
		return dynamic_cast<CMaskParamActionAtom*>(data);
	}

	CTimeEffectActionAtom* GetTimeEffectOperatorData()
	{
		return dynamic_cast<CTimeEffectActionAtom*>(data);
	}

	CWaterAreaActionAtom* GetWaterAreaOperatorData()
	{
		return dynamic_cast<CWaterAreaActionAtom*>(data);
	}

	CTriggerObjectActionAtom* GetTriggerObjOperatorData()
	{
		return dynamic_cast<CTriggerObjectActionAtom*>(data);
	}

	CTransparentObjectActionAtom* GetTransObjsOperatorData()
	{
		return dynamic_cast<CTransparentObjectActionAtom*>(data);
	}

	CSingleObjectNameActionAtom* GetSingleObjNameOperatorData()
	{
		return dynamic_cast<CSingleObjectNameActionAtom*>(data);
	}

	CLinkedObjectBaseActionAtom* GetLkoBaseOperatorData()
	{
		return dynamic_cast<CLinkedObjectBaseActionAtom*>(data);
	}

	CElementBaseActionAtom* GetElementBaseOperatorData()
	{
		return dynamic_cast<CElementBaseActionAtom*>(data);
	}

	CObjectNameActionAtom* GetObjNameOperatorData()
	{
		return dynamic_cast<CObjectNameActionAtom*>(data);
	}

	CNewDeleteGroundLayerActionAtom* GetNewDeleteGroundLayerOperData()
	{
		return dynamic_cast<CNewDeleteGroundLayerActionAtom*>(data);
	}

	CGroundLayerInfosActionAtom* GetAllGroundLayerInfosOperData()
	{
		return dynamic_cast<CGroundLayerInfosActionAtom*>(data);
	}
	CGroundLayerInfoActionAtom* GetSingleGroundLayerInfoOperData()
	{
		return dynamic_cast<CGroundLayerInfoActionAtom*>(data);
	}

	CObjectsActionAtom* GetObjsOperData()
	{
		return dynamic_cast<CObjectsActionAtom*>(data);
	}

	WaterOperator* GetWaterOperData()
	{
		return dynamic_cast<WaterOperator*>(data);
	}

	BiggerTileOperator* GetBiggerTileOperData()
	{
		return dynamic_cast<BiggerTileOperator*>(data);
	}

	TileColorOperator* GetTileColorOperData()
	{
		return dynamic_cast<TileColorOperator*>(data);
	}

	SuperTileOperator* GetSuperTileOperData()
	{
		return dynamic_cast<SuperTileOperator*>(data);
	}

	TileOperator* GetTileOperData()
	{
		return dynamic_cast<TileOperator*>(data);
	}

	CanvasOperator* GetCanvasOperData()
	{
		return dynamic_cast<CanvasOperator*>(data);
	}

	int GetOperatorLayerID() const { return data->m_LayerID; }

	int GetOperatorType() const { return data->GetType(); }
	~CAction(void);
	CAction(eActionType operatorType, int layerID);
};


typedef CAction *LPTROPERATOR;