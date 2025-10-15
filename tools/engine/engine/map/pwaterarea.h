#ifndef __Nuclear_REGIONMAP_PWATERAREA_H
#define __Nuclear_REGIONMAP_PWATERAREA_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"
#include "..\common\xmarshal.h"

//水面区域
//水面区域会包含一个深度值，还有一个边界值，然后会有一堆区域（矩形）
//在引擎的内存里面，将有自己的内存结构，会是一个深度一个边界对一个区域。
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
			//成员数据们
		private:
			WaterAreaInfoMap m_mapWaters;
			WaterAreaInfoID m_nMaxId;

			//成员方法们
		private:

		public:
			///////////////////////////////初始化、构造与析构
			PWaterArea(void);
			~PWaterArea(void);

			///////////////////////////////输入输出
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////公共方法们
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