#ifndef __Nuclear_REGIONMAP_PWATERAREA_H__
#define __Nuclear_REGIONMAP_PWATERAREA_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxptypes.h"
#include "../common/nuxmarshal.h"

//水面区域
//水面区域会包含一个深度值，还有一个边界值，然后会有一堆区域（矩形）
//在引擎的内存里面，将有自己的内存结构，会是一个深度一个边界对一个区域。
class ToolsPMap;

namespace Nuclear
{
	namespace SubMap
	{
		typedef unsigned short WaterAreaInfoID;
		typedef unsigned short WaterAreaSmallID;

		typedef std::map<WaterAreaSmallID, NuclearRect> WaterAreaMap;

		struct sWaterAreaInfo
		{
			unsigned int depth;
			unsigned int border;
			unsigned int nextSamllID;
			WaterAreaMap areas;
			sWaterAreaInfo () : nextSamllID(0) {}
		};

		union uWaterAreaID
		{
			struct {
				WaterAreaInfoID infoID;
				WaterAreaSmallID areaID;
			};
			unsigned int id;
			uWaterAreaID(WaterAreaInfoID _info, WaterAreaSmallID _area):infoID(_info), areaID(_area) {}
			uWaterAreaID(unsigned int _id = 0):id(_id) {}
		};

		typedef std::map<WaterAreaInfoID, sWaterAreaInfo> WaterAreaInfoMap;

		class NuclearWaterArea
		{
			friend class ToolsPMap;
		public:
			NuclearWaterArea(void);
			~NuclearWaterArea(void);

			///////////////////////////////输入输出///////////////////////////////
			NuclearOStream& marshal(NuclearOStream &os) const;
			const INuclearStream& unmarshal(const INuclearStream &os, unsigned int version);

			WaterAreaInfoID AddAnWaterArea(unsigned int depth, unsigned int border);
			bool RemoveAnWaterArea(WaterAreaInfoID waterID);
			void ModifyAnWaterArea(WaterAreaInfoID waterID, unsigned int depth, unsigned int border);
			
			const WaterAreaInfoMap& GetWaterAreaInfos() const { return m_WaterAreas; }
			void SetWaterAreaInfos(const WaterAreaInfoMap& map) { m_WaterAreas = map; }

			uWaterAreaID AddAnWaterArea(WaterAreaInfoID waterID, const NuclearRect& area);
			void RemoveAnWaterArea(uWaterAreaID areaID);
			void ModifyAnWaterArea(uWaterAreaID areaID, const NuclearRect& newArea);

		private:
			WaterAreaInfoMap m_WaterAreas;
			WaterAreaInfoID m_nMaxWaterAreaInfoID;
		};
	}
}
#endif