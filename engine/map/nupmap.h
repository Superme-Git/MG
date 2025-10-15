#ifndef __Nuclear_PMAP_H__
#define __Nuclear_PMAP_H__

#if _MSC_VER > 1000
#pragma once
#endif

#ifndef XPTILE_TYPE
#define XPTILE_TYPE (L".png")
#endif

#include "../common/nuxptypes.h"
#include "../common/nupobject.h"
#include "nupground.h"
#include "nupwaterarea.h"
#include "numapobjs.h"
#include "nuptransobjs.h"
#include "nuptimeeffect.h"
#include "nupmask.h"
#include "nuptriggerobjs.h"
#include "nupbackground.h"

namespace Nuclear
{
	//��ʵNuclearMap��Public�ɣ������ṹ�壬�������õ�NuclearMap�����Ҳû�м�����
	class NuclearMap : public NuclearObject
	{	
	public:
		NuclearMap(void);
		virtual ~NuclearMap(void);

		virtual bool Init();		

	public:
		unsigned int static const MAP_VERSION=50;

		unsigned int static const MAP_NO_MID_2 = 49;			//û�������2��
		unsigned int static const MAP_MOUNT_ELEMENT = 48;		//���ǲ�Ҫ�������ˣ�47Ҳ������
		unsigned int static const MAP_NO_OBJ_ROUTE = 46;		//Ҫ������ת����ЧҪ�е�ɫ
		unsigned int static const MAP_NO_OBJ_SCALE = 45;		//�µĿ���������
		unsigned int static const MAP_OLD_GROUND_COLOR = 44;	//�ɵĵر��ɫ
		unsigned int static const MAP_OLD_LAYER_COUNT_AND_NO_MASK = 43;	//�ּӲ��ˡ�������Ǽ��ϵ�5��6�㣬�������ɰ��ͼ
		unsigned int static const MAP_OLD_LAYER_COUNT=42;		//�ּӲ��ˡ��������ˮ�������һ�㣬�����������һ��
		unsigned int static const MAP_NO_SIGN=41;				//û�б�־λ
		unsigned int static const MAP_OLD_MOUNT=40;				//�µĲ�mount bin��
		
		int static const TILE_HEIGHT = SubMap::NuclearGround::TILE_HEIGHT;
		int static const TILE_WIDTH = SubMap::NuclearGround::TILE_WIDTH;
		int static const TILE_S = TILE_WIDTH * TILE_HEIGHT;

		enum MAP_OBJ_TYPE
		{
			MAP_OBJ_ELEMENT = 0,
			MAP_OBJ_EFFECT,
			MAP_OBJ_LINKEDOBJ,
			MAP_OBJ_DISTORT,
		};

		enum MAP_SIGN
		{
			MAP_SIGN_UNDERGROUND_PALACE =		1 << 0,		//���¹���ͼ
			MAP_SIGN_ALWAYS_DAYTIME =			1 << 1,		//���ǰ��죬lko�ĵƹ��ܲ���
			MAP_SIGN_ALWAYS_NIGHT =				1 << 2,		//����ҹ��lko�ĵƹ�����
		};

		int static const MAP_OBJ_COUNT = MAP_OBJ_DISTORT + 1;

		///////////////////////////////�������
		NuclearOStream& marshal(NuclearOStream &) const;
		const INuclearStream& unmarshal(const INuclearStream &);

		///////////////////////////////��������
		int GetWidth() const { return m_nWidth; }
		int GetHeight() const { return m_nHeight; }
		virtual bool Init(int width, int height);
		unsigned int GetSign() const { return m_nSign; }
		void AddSign(MAP_SIGN sign) { m_nSign |= sign; }
		void RemoveSign(MAP_SIGN sign) { m_nSign &= ~sign; }
		void ClearSign() { m_nSign = 0; }

		///////////////////////////////���������
		bool GetGroundOrders(int i, SubMap::eGroundLayers &layer) const { return m_Ground.GetOrders(i, layer); }
		bool SetGroundOrders(int i, SubMap::eGroundLayers layer) { return m_Ground.SetOrders(i, layer); }
		SubMap::TileType *GetTileData(int layer) const { return m_Ground.GetSmlTileData(layer); }
		unsigned int *GetColorData() const { return m_Ground.GetColorData(); }
		SubMap::uWaterTileType *GetWaterData() const { return m_Ground.GetWaterData(); }
		unsigned int GetSmlTileSize() const {return m_Ground.GetSmlTileSize();}
		unsigned int GetSmlTileCount() const {return m_Ground.GetSmlTileCount();}
		unsigned int GetMidTileSize() const {return m_Ground.GetMidTileSize();}
		unsigned int GetMidTileCount() const {return m_Ground.GetMidTileCount();}
		unsigned int GetBigTileSize() const {return m_Ground.GetBigTileSize();}
		unsigned int GetBigTileCount() const {return m_Ground.GetBigTileCount();}
		SubMap::TileType GetSmlTileData(int layer, int x, int y) const;
		bool SetSmlTileData(int layer, int x, int y, SubMap::TileType data);
		SubMap::uBiggerTileType GetMidTileData(int layer, int x, int y) const;
		SubMap::uBiggerTileType* GetMidTileData(int layer) const { return m_Ground.GetMidTileData(layer); }
		bool SetMidTileData(int layer, int x, int y, SubMap::uBiggerTileType data);
		SubMap::uBiggerTileType GetBigTileData(int x, int y) const;
		SubMap::uBiggerTileType* GetBigTileData() const { return m_Ground.GetBigTileData(); }
		bool SetBigTileData(int x, int y, SubMap::uBiggerTileType data);
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
			if (i<0 || i>=SubMap::NuclearGround::WATER_EFFECT_COUNT)
			{
				return L"";
			}
			return m_Ground.GetWaterName(i);
		}

		bool SetWaterName(int i, const std::wstring& name) 
		{ 
			if (i<0 || i>=SubMap::NuclearGround::WATER_EFFECT_COUNT)
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
		SubMap::uWaterAreaID AddAnWaterArea(SubMap::WaterAreaInfoID waterID, const NuclearRect& area)
		{
			return m_Water.AddAnWaterArea(waterID, area);
		}
		void RemoveAnWaterArea(SubMap::WaterAreaInfoID waterID, SubMap::uWaterAreaID areaID)
		{
			m_Water.RemoveAnWaterArea(areaID);
		}
		void ModifyAnWaterArea(SubMap::uWaterAreaID areaID, const NuclearRect& newArea)
		{
			m_Water.ModifyAnWaterArea(areaID, newArea);
		}

		/////////////////////////////////��������
		const std::wstring& GetObjFileName(MAP_OBJ_TYPE objType, unsigned char layerID, SubMap::ObjID objID) const
		{
			return m_Objects[objType].GetObjFileName(layerID, objID);
		}

		const std::wstring& GetObjFileName(MAP_OBJ_TYPE objType, SubMap::ObjMapKey key) const
		{
			return m_Objects[objType].GetObjFileName(key);
		}

		SubMap::ObjInfo& GetObjInfo(MAP_OBJ_TYPE objType, unsigned char layerID, SubMap::ObjID objID)
		{
			return m_Objects[objType].GetObjInfoByID(layerID, objID);
		}

		const SubMap::ObjInfoVector& GetConstObjInfo(MAP_OBJ_TYPE objType, unsigned char layerID) const
		{
			return m_Objects[objType].GetObjInfos(layerID);
		}

		const SubMap::ObjInfo& GetConstObjInfo(MAP_OBJ_TYPE objType, unsigned char layerID, SubMap::ObjID objID) const
		{
			return m_Objects[objType].GetObjInfoByID(layerID, objID);
		}

		SubMap::ObjInfoVector& GetObjInfo(MAP_OBJ_TYPE objType, unsigned char layerID)
		{
			return m_Objects[objType].GetObjInfos(layerID);
		}

		const unsigned char GetObjLayerCount(MAP_OBJ_TYPE objType) const
		{
			return m_Objects[objType].GetLayerCount();
		}
		unsigned int AddObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjMapKey key, const NuclearRect& rect, const NuclearPoint& pos, unsigned int attachColor)
		{
			return m_Objects[objType].AddObj(layerID, key, rect, pos, attachColor);
		}
		
		bool ReAddObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjID objID);
		bool DeleteObj(MAP_OBJ_TYPE objType, int layerID, SubMap::ObjID objID);
		void DestoryLastObj(MAP_OBJ_TYPE objType, int layerID);

		/////////////////////////////////���Ͷ������
		unsigned int AddAnTransObj(const SubMap::sTransObjInfo& obj)
		{
			return m_TransObjects.AddAnTransObj(obj);
		}
		bool RemoveAnTransObj(const unsigned int TransId)
		{
			return m_TransObjects.RemoveAnTransObj(TransId);
		}
		void ModifyAnTransObj(const unsigned int TransId, const SubMap::sTransObjInfo& obj)
		{
			m_TransObjects.ModifyAnTransObj(TransId, obj);
		}
		const SubMap::TransObjInfoMap& GetTransInfos()
		{
			return m_TransObjects.GetTransInfos();			
		}


		/////////////////////////////////ʱ��Ч�����
		bool AddAnTimeEffect(const int nTime, const sNuclearColorBalanceParam& obj)
		{
			return m_TimeEffect.AddAnTimeEffect(nTime, obj);
		}
		bool RemoveAnTimeEffect(const int nTime)
		{
			return m_TimeEffect.RemoveAnTimeEffect(nTime);
		}
		bool ModifyAnTimeEffect(const int nOldTime, const int nNewTime, const sNuclearColorBalanceParam& obj)
		{
			return m_TimeEffect.ModifyAnTimeEffect(nOldTime, nNewTime, obj);
		}
		const SubMap::TimeEffectParamMap& GetTimeEffectParams()
		{
			return m_TimeEffect.GetTimeEffectParams();			
		}

		/////////////////////////////////Mask���
		bool AddAMask(const int nTime, const sNuclearMaskParam& obj)
		{
			return m_Mask.AddAMask(nTime, obj);
		}
		bool RemoveAMask(const int nTime)
		{
			return m_Mask.RemoveAMask(nTime);
		}
		bool ModifyAMask(const int nOldTime, const int nNewTime, const sNuclearMaskParam& obj)
		{
			return m_Mask.ModifyAMask(nOldTime, nNewTime, obj);
		}
		const SubMap::MaskParamMap& GetMaskParams()
		{
			return m_Mask.GetMaskParams();			
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

		SubMap::sTriggerID AddAnTriggerArea(SubMap::TriggerInfoID infoID, const NuclearRect& area)
		{
			return m_triggerObjs.AddAnTriggerArea(infoID, area);
		}
		void RemoveAnTriggerArea(SubMap::sTriggerID triggerID)
		{
			return m_triggerObjs.RemoveAnTriggerArea(triggerID);
		}
		void ModifyAnTriggerArea(SubMap::sTriggerID triggerID, const NuclearRect& newArea)
		{
			return m_triggerObjs.ModifyAnTriggerArea(triggerID, newArea);
		}
		///////////////////////////////////////�ƶ��������
		const SubMap::NuclearBackGoundInfoVector& GetBackGroundInfoVet() const { return m_BackGround.m_BackGroundInfos; }
		int GetBackGroundInfoCount() const { return m_BackGround.m_BackGroundInfoCount; }


		unsigned int GetVersion() {return m_nVersion;}

		// ����
		int m_nWidth;
		int m_nHeight;

		unsigned int m_nSign;

		////////////////////�ƶ�����
		SubMap::NuclearBackGround	m_BackGround;
		////////////////////�ر�
		SubMap::NuclearGround		m_Ground;
		///////////////////ˮ���߼�����
		SubMap::NuclearWaterArea		m_Water;
		//////////////////////�����
		SubMap::NuclearMapObjs		m_Objects[MAP_OBJ_COUNT];
		////////////////////���Ͷ���
		SubMap::NuclearTransObjs	m_TransObjects;
		////////////////////ʱ��Ч�������ݵ�ǰʱ���޸Ļ���ɫ�����Աȶȡ����������ְ����ҹ��
		SubMap::NuclearTimeEffect	m_TimeEffect;
		////////////////////Mask��ͼ
		SubMap::NuclearMask		m_Mask;
		////////////////////������Ч
		SubMap::NuclearTriggerObjs m_triggerObjs;		

		/////////////////////////////
		unsigned int m_nVersion;	//�汾

	private:
		NuclearMap(const NuclearMap&);
	};
}
#endif