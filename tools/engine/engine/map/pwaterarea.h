#ifndef __Nuclear_REGIONMAP_PWATERAREA_H
#define __Nuclear_REGIONMAP_PWATERAREA_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"
#include "..\common\xmarshal.h"

//ˮ������
//ˮ����������һ�����ֵ������һ���߽�ֵ��Ȼ�����һ�����򣨾��Σ�
//��������ڴ����棬�����Լ����ڴ�ṹ������һ�����һ���߽��һ������
class CToolsMap;

namespace Nuclear
{
	namespace SubMap
	{
		typedef WORD WaterAreaInfoID;
		typedef WORD WaterAreaSmallID;

		typedef std::map<WaterAreaSmallID, CRECT> WaterAreaMap;

		struct WaterAreaInfo
		{
			unsigned int depth;
			unsigned int border;
			unsigned int nextSamllID;
			WaterAreaMap areas;
			WaterAreaInfo () : nextSamllID(0) {}
		};

		union WaterAreaID
		{
			struct {
				WaterAreaInfoID infoID;
				WaterAreaSmallID areaID;
			};
			unsigned int id;
			WaterAreaID(WaterAreaInfoID _info, WaterAreaSmallID _area):infoID(_info), areaID(_area) {}
			WaterAreaID(unsigned int _id = 0):id(_id) {}
		};

		typedef std::map<WaterAreaInfoID, WaterAreaInfo> WaterAreaInfoMap;

		class PWaterArea
		{
			friend class CToolsMap;
		public:
			//��Ա������
		private:
			WaterAreaInfoMap m_mapWaters;
			WaterAreaInfoID m_nMaxId;

			//��Ա������
		private:

		public:
			///////////////////////////////��ʼ��������������
			PWaterArea(void);
			~PWaterArea(void);

			///////////////////////////////�������
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////����������
			WaterAreaInfoID AddAnWaterArea(unsigned int depth, unsigned int border);
			bool RemoveAnWaterArea(WaterAreaInfoID waterID);
			void ModifyAnWaterArea(WaterAreaInfoID waterID, unsigned int depth, unsigned int border);
			
			const WaterAreaInfoMap& GetWaterAreaInfos() const { return m_mapWaters; }
			void SetWaterAreaInfos(const WaterAreaInfoMap& map) { m_mapWaters = map; }

			WaterAreaID AddAnWaterArea(WaterAreaInfoID waterID, const CRECT& area);
			void RemoveAnWaterArea(WaterAreaID areaID);
			void ModifyAnWaterArea(WaterAreaID areaID, const CRECT& newArea);
		};
	}
}
#endif