#ifndef __Nuclear_PMAP_H
#define __Nuclear_PMAP_H

#if _MSC_VER > 1000
#pragma once
#endif

#ifndef XPTILE_TYPE
#define XPTILE_TYPE (L".png")
#endif

#include "..\common\xptypes.h"
#include "..\common\pobject.h"
#include "pground.h"
#include "pwaterarea.h"
#include "mapobjs.h"
#include "ptransobjs.h"
#include "ptimeeffect.h"
#include "pmask.h"
#include "ptriggerobjs.h"
#include "pbackground.h"

class CMapEditorDoc;
class CObjectResourceDlg;
class CAllObjectResourceDlg;
class CActionList;
class CLinkedObjectInfoView;
class CReplaceObjs;

namespace Nuclear
{
	class PMap :
		public PObject
	{	
	public:
		//好多友元，真不爽，相当不爽！！！
		friend class ::CAllObjectResourceDlg;
		friend class ::CObjectResourceDlg;
		friend class ::CActionList;
		friend class ::CMapEditorDoc;
		friend class ::CLinkedObjectInfoView;
		friend class ::CReplaceObjs;
		friend class RegionMap;
		friend class Map;
		
		PMap(void);
		virtual ~PMap(void);

	protected:
		virtual bool Init();		

	public:
		//静态常量

		unsigned int static const MAP_VERSION=45;
		
		unsigned int static const MAP_OLD_GROUND_COLOR = 44;	//旧的地表叠色
		unsigned int static const MAP_OLD_LAYER_COUNT_AND_NO_MASK = 43;	//又加层了……这次是加上低5低6层，加上了蒙板叠图
		unsigned int static const MAP_OLD_LAYER_COUNT=42;		//又加层了……这次在水层下面加一层，在物中下面加一层
		unsigned int static const MAP_NO_SIGN=41;				//没有标志位
		unsigned int static const MAP_OLD_MOUNT=40;				//新的不mount bin了
		//不支持以下版本了
		//unsigned int static const MAP_EFFECT_HAS_EXC=39;		//有的effect有扩展名
		//unsigned int static const MAP_NO_TRIGGER_SPRITE_EXC=38;	//Trigger里面Sprite触发的偏移
		//unsigned int static const MAP_OLD_TRIGGER=37;		//没有进行Trigger结构调整
		//unsigned int static const MAP_NO_BACKGROUND=36;		//没有可以跟着视口跑的背景
		//unsigned int static const MAP_NO_WATER_TRANS=35;	//没有水面过渡
		//unsigned int static const MAP_NO_DISTORTION_BOJ=34;	//没有飘动的旗子
		//unsigned int static const MAP_OLD_MAP_ORDER = 33;	//将element、effect、linkedobj统一起来
		//unsigned int static const MAP_NO_BIG_TILE_ORDER = 32;//大地表间的顺序没有没有初始化……所以实际上写进文件里面的顺序是错的
		//unsigned int static const MAP_NO_BIG_TILE=31;//没有大地表
		//unsigned int static const MAP_NO_WATER_AREA=30;//没有涉水效果
		//unsigned int static const MAP_NO_WATER_LAYER=29;
		//unsigned int static const MAP_NO_TRIGGER=28;
		//unsigned int static const MAP_EFFECT_NO_POS=27;	//这个版本之后的版本同时还会对文件名表格式进行改动，以后就直接 << 或者 >> std::wstring了, effect里面也不记录扩展名了
		//unsigned int static const MAP_OLD_GROUND_COLOR=26;
		//unsigned int static const MAP_OLD_DYNAMIC_OBJ=25;
		//unsigned int static const MAP_NO_DYNAMIC_OBJ=24;	
		//unsigned int static const MAP_NO_ELEMENT_EFFECT_COLOR=23;
		//unsigned int static const MAP_NO_GROUND_COLOR=22;
		//unsigned int static const MAP_OLD_TIME_EFFECT=21;
		//unsigned int static const MAP_NO_TIME_EFFECT=20;
		//unsigned int static const MAP_NO_TRANSOBJ=19;
		//unsigned int static const MAP_OLD_ORDER=18;
		//unsigned int static const MAP_OLD_IMGFILE_NAME=17;//(0x11)
		//unsigned int static const MAP_OLD_LAYER_COUNT=0x10;
		//unsigned int static const MAP_NO_EFFECT=0x9;
		
		int static const TILE_HEIGHT = SubMap::PGround::TILE_HEIGHT;
		int static const TILE_WIDTH = SubMap::PGround::TILE_WIDTH;
		int static const TILE_S = TILE_WIDTH * TILE_HEIGHT;

		enum MAP_OBJ_TYPE
		{
			MAP_OBJ_ELEMENT = 0,
			MAP_OBJ_EFFECT,
			MAP_OBJ_LINKEDOBJ,
			MAP_OBJ_DISTORT,
			MAP_OBJ_MAX,	// yeqing 2015-11-07
		};

		enum MAP_SIGN
		{
			MAP_SIGN_UNDERGROUND_PALACE = 1,		//地下宫殿图
		};

		int static const MAP_OBJ_COUNT = MAP_OBJ_MAX;	// yeqing 2015-11-07

		///////////////////////////////输入输出
		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);

		///////////////////////////////基本操作
		int GetWidth() const { return m_nWidth; }
		int GetHeight() const { return m_nHeight; }
		virtual bool Init(int width, int height);
		unsigned int GetSign() const { return m_nSign; }
		void AddSign(MAP_SIGN sign) { m_nSign |= sign; }
		void RemoveSign(MAP_SIGN sign) { m_nSign &= ~sign; }
		void ClearSign() { m_nSign = 0; }

		///////////////////////////////背景层相关
		bool GetGroundOrders(int i, SubMap::GroundLayers &layer) const { return m_Ground.GetOrders(i, layer); }
		bool SetGroundOrders(int i, SubMap::GroundLayers layer) { return m_Ground.SetOrders(i, layer); }
		SubMap::TileType *GetTileData(int layer) const { return m_Ground.GetSmlTileData(layer); }
		unsigned int *GetColorData() const { return m_Ground.GetColorData(); }
		SubMap::WaterTileType *GetWaterData() const { return m_Ground.GetWaterData(); }
		unsigned int GetSmlTileSize() const {return m_Ground.GetSmlTileSize();}
		unsigned int GetSmlTileCount() const {return m_Ground.GetSmlTileCount();}
		unsigned int GetMidTileSize() const {return m_Ground.GetMidTileSize();}
		unsigned int GetMidTileCount() const {return m_Ground.GetMidTileCount();}
		unsigned int GetBigTileSize() const {return m_Ground.GetBigTileSize();}
		unsigned int GetBigTileCount() const {return m_Ground.GetBigTileCount();}
		SubMap::TileType GetSmlTileData(int layer, int x, int y) const;
		bool SetSmlTileData(int layer, int x, int y, SubMap::TileType data);
		SubMap::BiggerTileType GetMidTileData(int layer, int x, int y) const;
		SubMap::BiggerTileType* GetMidTileData(int layer) const { return m_Ground.GetMidTileData(layer); }
		bool SetMidTileData(int layer, int x, int y, SubMap::BiggerTileType data);
		SubMap::BiggerTileType GetBigTileData(int x, int y) const;
		SubMap::BiggerTileType* GetBigTileData() const { return m_Ground.GetBigTileData(); }
		bool SetBigTileData(int x, int y, SubMap::BiggerTileType data);
		int GetWidthForSmlTiles(void) const { return m_Ground.GetWidthForSmlTiles(); }
		int GetHeightForSmlTiles(void)	const { return m_Ground.GetHeightForSmlTiles(); }
		int GetWidthForMidTiles(void) const { return m_Ground.GetWidthForMidTiles(); }
		int GetHeightForMidTiles(void)	const { return m_Ground.GetHeightForMidTiles(); }
		int GetWidthForBigTiles(void) const { return m_Ground.GetWidthForBigTiles(); }
		int GetHeightForBigTiles(void)	const { return m_Ground.GetHeightForBigTiles(); }
		int GetWidthForColor(void) const { return m_Ground.GetWidthForColor(); }
		int GetHeightForColor(void) const { return m_Ground.GetHeightForColor(); }
		int GetWidthForWater(void) const { return m_Ground.GetWidthForWater(); }
		int GetHeightForWater(void) const { return m_Ground.GetHeightForWater(); }
		int GetColorPos(int x, int y) const { return m_Ground.GetColorPos(x, y); }
		int GetWaterPos(int x, int y) const { return m_Ground.GetWaterPos(x, y); }

		const std::wstring& GetWaterName(int i) const 
		{ 
			if (i<0 || i>2)
			{
				static std::wstring err = L"";
				return err;
			}
			return m_Ground.GetWaterName(i);
		}
		std::wstring GetWaterName(int i) 
		{
			if (i<0 || i>=SubMap::PGround::WATER_EFFECT_COUNT)
			{
				return L"";
			}
			return m_Ground.GetWaterName(i);
		}

		bool SetWaterName(int i, const std::wstring& name) 
		{ 
			if (i<0 || i>=SubMap::PGround::WATER_EFFECT_COUNT)
			{
				return false;
			}
			m_Ground.SetWaterName(i, name);
			return true;
		}

		bool IsPointInMapForSmlTiles(int x, int y) const //以小Tile为单位，判断Tile是否在地图里面
		{
			return m_Ground.IsPointInMapForSmlTiles(x, y);
		}
		bool IsPointInMapForSmlGround(int x, int y) //以小Tile为单位，判断Tile边界点是否在地图里面
		{
			if (x > (GetWidthForSmlTiles()) || y > (GetHeightForSmlTiles())
				|| x < 0 || y < 0)
				return false;
			else
				return true;
		}
		bool IsPointInMapForMidTiles(int x, int y) const //以中Tile为单位，判断Tile是否在地图里面
		{
			return m_Ground.IsPointInMapForMidTiles(x, y);
		}
		bool IsPointInMapForMidGround(int x, int y) //以中Tile为单位，判断Tile边界点是否在地图里面
		{
			if (x > (GetWidthForMidTiles()) || y > (GetHeightForMidTiles())
				|| x < 0 || y < 0)
				return false;
			else
				return true;
		}
		bool IsPointInMapForBigTiles(int x, int y) const //以大Tile为单位，判断Tile是否在地图里面
		{
			return m_Ground.IsPointInMapForBigTiles(x, y);
		}
		bool IsPointInMapForBigGround(int x, int y) //以大Tile为单位，判断Tile边界点是否在地图里面
		{
			if (x > (GetWidthForBigTiles()) || y > (GetHeightForBigTiles())
				|| x < 0 || y < 0)
				return false;
			else
				return true;
		}
		/////////////////////////////////水面区域相关
		SubMap::WaterAreaInfoID AddAnWaterArea(unsigned int depth, unsigned int border)
		{
			return m_Water.AddAnWaterArea(depth, border);
		}
		bool RemoveAnWaterArea(SubMap::WaterAreaInfoID waterID)
		{
			return m_Water.RemoveAnWaterArea(waterID);
		}
		void ModifyAnWaterArea(SubMap::WaterAreaInfoID waterID, unsigned int depth, unsigned int border)
		{
			return m_Water.ModifyAnWaterArea(waterID, depth, border);
		}
		const SubMap::WaterAreaInfoMap& GetWaterAreaInfos() const { return m_Water.GetWaterAreaInfos(); }
		void SetWaterAreaInfos(const SubMap::WaterAreaInfoMap& map) { m_Water.SetWaterAreaInfos(map); }
		SubMap::WaterAreaID AddAnWaterArea(SubMap::WaterAreaInfoID waterID, const CRECT& area)
		{
			return m_Water.AddAnWaterArea(waterID, area);
		}
		void RemoveAnWaterArea(SubMap::WaterAreaInfoID waterID, SubMap::WaterAreaID areaID)
		{
			m_Water.RemoveAnWaterArea(areaID);
		}
		void ModifyAnWaterArea(SubMap::WaterAreaID areaID, const CRECT& newArea)
		{
			m_Water.ModifyAnWaterArea(areaID, newArea);
		}

		/////////////////////////////////物件层相关
		const std::wstring& GetObjFileName(MAP_OBJ_TYPE objType, unsigned char layerID, SubMap::ObjectId objID) const
		{
			return m_objs[objType].GetObjFileName(layerID, objID);
		}

		const std::wstring& GetObjFileName(MAP_OBJ_TYPE objType, SubMap::ObjMapKey key) const
		{
			return m_objs[objType].GetObjFileName(key);
		}

		SubMap::sObjInfo& GetObjInfo(MAP_OBJ_TYPE objType, unsigned char layerID, SubMap::ObjectId objID)
		{
			return m_objs[objType].GetObjInfoByID(layerID, objID);
		}

		const SubMap::ObjInfoVector& GetConstObjInfo(MAP_OBJ_TYPE objType, unsigned char layerID) const
		{
			return m_objs[objType].GetObjInfos(layerID);
		}

		const SubMap::sObjInfo& GetConstObjInfo(MAP_OBJ_TYPE objType, unsigned char layerID, SubMap::ObjectId objID) const
		{
			return m_objs[objType].GetObjInfoByID(layerID, objID);
		}

		SubMap::ObjInfoVector& GetObjInfo(MAP_OBJ_TYPE objType, unsigned char layerID)
		{
			return m_objs[objType].GetObjInfos(layerID);
		}

		const unsigned char GetObjLayerCount(MAP_OBJ_TYPE objType) const
		{
			return m_objs[objType].GetLayerCount();
		}
		unsigned int AddObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjMapKey key, const CRECT& rect, const CPOINT& pos, unsigned int attachColor)
		{
			return m_objs[objType].AddObj(layerID, key, rect, pos, attachColor);
		}
		
		bool ReAddObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjectId objID);
		bool DeleteObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjectId objID);
		void DestoryLastObj(MAP_OBJ_TYPE objType, int layerID);

		/////////////////////////////////传送对象相关
		unsigned int AddAnTransObj(const SubMap::TransObjInfo& obj)
		{
			return m_transObjs.AddAnTransObj(obj);
		}
		bool RemoveAnTransObj(const unsigned int TransId)
		{
			return m_transObjs.RemoveAnTransObj(TransId);
		}
		void ModifyAnTransObj(const unsigned int TransId, const SubMap::TransObjInfo& obj)
		{
			m_transObjs.ModifyAnTransObj(TransId, obj);
		}
		const SubMap::TransObjInfoMap& GetTransInfos()
		{
			return m_transObjs.GetTransInfos();			
		}


		/////////////////////////////////时间效果相关
		bool AddAnTimeEffect(const int nTime, const XPColorBalanceParam& obj)
		{
			return m_timeEffect.AddAnTimeEffect(nTime, obj);
		}
		bool RemoveAnTimeEffect(const int nTime)
		{
			return m_timeEffect.RemoveAnTimeEffect(nTime);
		}
		bool ModifyAnTimeEffect(const int nOldTime, const int nNewTime, const XPColorBalanceParam& obj)
		{
			return m_timeEffect.ModifyAnTimeEffect(nOldTime, nNewTime, obj);
		}
		const SubMap::TimeEffectParamMap& GetTimeEffectParams()
		{
			return m_timeEffect.GetTimeEffectParams();			
		}

		/////////////////////////////////Mask相关
		bool AddAMask(const int nTime, const XPMaskParam& obj)
		{
			return m_mask.AddAMask(nTime, obj);
		}
		bool RemoveAMask(const int nTime)
		{
			return m_mask.RemoveAMask(nTime);
		}
		bool ModifyAMask(const int nOldTime, const int nNewTime, const XPMaskParam& obj)
		{
			return m_mask.ModifyAMask(nOldTime, nNewTime, obj);
		}
		const SubMap::MaskParamMap& GetMaskParams()
		{
			return m_mask.GetMaskParams();			
		}

		/////////////////////////////////////////触发特效相关
		SubMap::TriggerInfoID AddAnTriggerInfoVet(const std::wstring &name, const SubMap::TriggerInfoVet& obj)
		{
			return m_triggerObjs.AddAnTriggerInfoVet(name, obj);
		}
		bool RemoveAnTriggerInfoVet(SubMap::TriggerInfoID infoID)
		{
			return m_triggerObjs.RemoveAnTriggerInfoVet(infoID);
		}
		void ModifyAnTriggerInfoVet(SubMap::TriggerInfoID infoID, const std::wstring &name, const SubMap::TriggerInfoVet& obj)
		{
			return m_triggerObjs.ModifyAnTriggerInfoVet(infoID, name, obj);
		}

		const SubMap::TriggerInfoMap& GetTriggerInfos() const 
		{
			return m_triggerObjs.GetTriggerInfos();
		}
		void SetTriggerInfos(const SubMap::TriggerInfoMap& pmap) 
		{ 
			return m_triggerObjs.SetTriggerInfos(pmap);
		}

		SubMap::TriggerID AddAnTriggerArea(SubMap::TriggerInfoID infoID, const CRECT& area)
		{
			return m_triggerObjs.AddAnTriggerArea(infoID, area);
		}
		void RemoveAnTriggerArea(SubMap::TriggerID triggerID)
		{
			return m_triggerObjs.RemoveAnTriggerArea(triggerID);
		}
		void ModifyAnTriggerArea(SubMap::TriggerID triggerID, const CRECT& newArea)
		{
			return m_triggerObjs.ModifyAnTriggerArea(triggerID, newArea);
		}
		///////////////////////////////////////移动背景相关
		const SubMap::PBackGoundInfoVector& GetBackGroundInfoVet() const { return m_backGround.m_backGroundInfos; }
		int GetBackGroundInfoCount() const { return m_backGround.m_backGroundInfoCount; }


		unsigned int GetVersion() {return m_nVersion;}

		// 数据
	protected:
		int m_nWidth;
		int m_nHeight;

		unsigned int m_nSign;

		////////////////////移动背景
		SubMap::PBackGround m_backGround;
		////////////////////地表
		SubMap::PGround m_Ground;
		///////////////////水面逻辑区域
		SubMap::PWaterArea m_Water;
		//////////////////////物件们
		SubMap::MapObjs m_objs[MAP_OBJ_COUNT];
		////////////////////传送对象
		SubMap::PTransObjs m_transObjs;
		////////////////////时间效果（根据当前时间修改画面色调、对比度、亮度以区分白天黑夜）
		SubMap::PTimeEffect m_timeEffect;
		////////////////////Mask叠图
		SubMap::PMask m_mask;
		////////////////////触发特效
		SubMap::PTriggerObjs m_triggerObjs;
		

		/////////////////////////////
		unsigned int m_nVersion;	//版本

	private:
		PMap(const PMap&);
	};
}
#endif