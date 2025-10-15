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
		//�ö���Ԫ���治ˬ���൱��ˬ������
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
		//��̬����

		unsigned int static const MAP_VERSION=45;
		
		unsigned int static const MAP_OLD_GROUND_COLOR = 44;	//�ɵĵر��ɫ
		unsigned int static const MAP_OLD_LAYER_COUNT_AND_NO_MASK = 43;	//�ּӲ��ˡ�������Ǽ��ϵ�5��6�㣬�������ɰ��ͼ
		unsigned int static const MAP_OLD_LAYER_COUNT=42;		//�ּӲ��ˡ��������ˮ�������һ�㣬�����������һ��
		unsigned int static const MAP_NO_SIGN=41;				//û�б�־λ
		unsigned int static const MAP_OLD_MOUNT=40;				//�µĲ�mount bin��
		//��֧�����°汾��
		//unsigned int static const MAP_EFFECT_HAS_EXC=39;		//�е�effect����չ��
		//unsigned int static const MAP_NO_TRIGGER_SPRITE_EXC=38;	//Trigger����Sprite������ƫ��
		//unsigned int static const MAP_OLD_TRIGGER=37;		//û�н���Trigger�ṹ����
		//unsigned int static const MAP_NO_BACKGROUND=36;		//û�п��Ը����ӿ��ܵı���
		//unsigned int static const MAP_NO_WATER_TRANS=35;	//û��ˮ�����
		//unsigned int static const MAP_NO_DISTORTION_BOJ=34;	//û��Ʈ��������
		//unsigned int static const MAP_OLD_MAP_ORDER = 33;	//��element��effect��linkedobjͳһ����
		//unsigned int static const MAP_NO_BIG_TILE_ORDER = 32;//��ر���˳��û��û�г�ʼ����������ʵ����д���ļ������˳���Ǵ��
		//unsigned int static const MAP_NO_BIG_TILE=31;//û�д�ر�
		//unsigned int static const MAP_NO_WATER_AREA=30;//û����ˮЧ��
		//unsigned int static const MAP_NO_WATER_LAYER=29;
		//unsigned int static const MAP_NO_TRIGGER=28;
		//unsigned int static const MAP_EFFECT_NO_POS=27;	//����汾֮��İ汾ͬʱ������ļ������ʽ���иĶ����Ժ��ֱ�� << ���� >> std::wstring��, effect����Ҳ����¼��չ����
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
			MAP_SIGN_UNDERGROUND_PALACE = 1,		//���¹���ͼ
		};

		int static const MAP_OBJ_COUNT = MAP_OBJ_MAX;	// yeqing 2015-11-07

		///////////////////////////////�������
		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);

		///////////////////////////////��������
		int GetWidth() const { return m_nWidth; }
		int GetHeight() const { return m_nHeight; }
		virtual bool Init(int width, int height);
		unsigned int GetSign() const { return m_nSign; }
		void AddSign(MAP_SIGN sign) { m_nSign |= sign; }
		void RemoveSign(MAP_SIGN sign) { m_nSign &= ~sign; }
		void ClearSign() { m_nSign = 0; }

		///////////////////////////////���������
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

		bool IsPointInMapForSmlTiles(int x, int y) const //��СTileΪ��λ���ж�Tile�Ƿ��ڵ�ͼ����
		{
			return m_Ground.IsPointInMapForSmlTiles(x, y);
		}
		bool IsPointInMapForSmlGround(int x, int y) //��СTileΪ��λ���ж�Tile�߽���Ƿ��ڵ�ͼ����
		{
			if (x > (GetWidthForSmlTiles()) || y > (GetHeightForSmlTiles())
				|| x < 0 || y < 0)
				return false;
			else
				return true;
		}
		bool IsPointInMapForMidTiles(int x, int y) const //����TileΪ��λ���ж�Tile�Ƿ��ڵ�ͼ����
		{
			return m_Ground.IsPointInMapForMidTiles(x, y);
		}
		bool IsPointInMapForMidGround(int x, int y) //����TileΪ��λ���ж�Tile�߽���Ƿ��ڵ�ͼ����
		{
			if (x > (GetWidthForMidTiles()) || y > (GetHeightForMidTiles())
				|| x < 0 || y < 0)
				return false;
			else
				return true;
		}
		bool IsPointInMapForBigTiles(int x, int y) const //�Դ�TileΪ��λ���ж�Tile�Ƿ��ڵ�ͼ����
		{
			return m_Ground.IsPointInMapForBigTiles(x, y);
		}
		bool IsPointInMapForBigGround(int x, int y) //�Դ�TileΪ��λ���ж�Tile�߽���Ƿ��ڵ�ͼ����
		{
			if (x > (GetWidthForBigTiles()) || y > (GetHeightForBigTiles())
				|| x < 0 || y < 0)
				return false;
			else
				return true;
		}
		/////////////////////////////////ˮ���������
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

		/////////////////////////////////��������
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

		/////////////////////////////////���Ͷ������
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


		/////////////////////////////////ʱ��Ч�����
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

		/////////////////////////////////Mask���
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

		/////////////////////////////////////////������Ч���
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
		///////////////////////////////////////�ƶ��������
		const SubMap::PBackGoundInfoVector& GetBackGroundInfoVet() const { return m_backGround.m_backGroundInfos; }
		int GetBackGroundInfoCount() const { return m_backGround.m_backGroundInfoCount; }


		unsigned int GetVersion() {return m_nVersion;}

		// ����
	protected:
		int m_nWidth;
		int m_nHeight;

		unsigned int m_nSign;

		////////////////////�ƶ�����
		SubMap::PBackGround m_backGround;
		////////////////////�ر�
		SubMap::PGround m_Ground;
		///////////////////ˮ���߼�����
		SubMap::PWaterArea m_Water;
		//////////////////////�����
		SubMap::MapObjs m_objs[MAP_OBJ_COUNT];
		////////////////////���Ͷ���
		SubMap::PTransObjs m_transObjs;
		////////////////////ʱ��Ч�������ݵ�ǰʱ���޸Ļ���ɫ�����Աȶȡ����������ְ����ҹ��
		SubMap::PTimeEffect m_timeEffect;
		////////////////////Mask��ͼ
		SubMap::PMask m_mask;
		////////////////////������Ч
		SubMap::PTriggerObjs m_triggerObjs;
		

		/////////////////////////////
		unsigned int m_nVersion;	//�汾

	private:
		PMap(const PMap&);
	};
}
#endif