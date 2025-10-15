//#include "../common/ljfmutil.h"
#include "nuptriggerobjs.h"
#include "nupmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		NuclearWaterArea::NuclearWaterArea(void)
		{
			m_nMaxWaterAreaInfoID = 0;
			m_WaterAreas.clear();
		}

		NuclearWaterArea::~NuclearWaterArea(void){}

		NuclearOStream& NuclearWaterArea::marshal(NuclearOStream &os) const
		{
			WaterAreaInfoMap::const_iterator iter = m_WaterAreas.begin();
			WaterAreaMap::const_iterator areaIter, areaEnd;
			int count = static_cast<int>(m_WaterAreas.size());
			os << count;
			int areacount;
			for (; iter != m_WaterAreas.end(); ++iter)
			{
				os << static_cast<unsigned int>(iter->first);
				os << iter->second.depth;
				os << iter->second.border;
				areacount = static_cast<int>(iter->second.areas.size());
				os << areacount;
				areaIter=iter->second.areas.begin();
				areaEnd=iter->second.areas.end();
				for (;areaIter!=areaEnd;++areaIter)
				{
					os << areaIter->second;

				}
			}
			return os;
		}
		const INuclearStream& NuclearWaterArea::unmarshal(const INuclearStream &os, unsigned int version)
		{
			m_WaterAreas.clear();
			m_nMaxWaterAreaInfoID = 0;
			unsigned int count, areacount, i, j;
			unsigned int waterID;
			NuclearRect rect;
			os >> count;
			for (i=0;i<count;++i)
			{
				os >> waterID;
				if (waterID > m_nMaxWaterAreaInfoID)
				{
					m_nMaxWaterAreaInfoID = waterID;
				}
				sWaterAreaInfo& tmpInfo = m_WaterAreas[waterID];
				os >> tmpInfo.depth;
				os >> tmpInfo.border;
				os >> areacount;
				tmpInfo.nextSamllID = static_cast<WaterAreaSmallID>(areacount);
				for (j=0;j<tmpInfo.nextSamllID;++j)
				{
					os >> rect;
					tmpInfo.areas[j] = rect;
				}
			}
			return os;
		}
		WaterAreaInfoID NuclearWaterArea::AddAnWaterArea(unsigned int depth, unsigned int border)
		{
			sWaterAreaInfo& tmpInfo = m_WaterAreas[++m_nMaxWaterAreaInfoID];
			tmpInfo.depth = depth;
			tmpInfo.border = border;
			return m_nMaxWaterAreaInfoID;
		}

		bool NuclearWaterArea::RemoveAnWaterArea(WaterAreaInfoID waterID)
		{
			return (m_WaterAreas.erase(waterID) > 0);
		}

		void NuclearWaterArea::ModifyAnWaterArea(WaterAreaInfoID waterID, unsigned int depth, unsigned int border)
		{
			sWaterAreaInfo& tmpInfo = m_WaterAreas[waterID];
			tmpInfo.depth = depth;
			tmpInfo.border = border;
		}
	
		uWaterAreaID NuclearWaterArea::AddAnWaterArea(WaterAreaInfoID waterID, const NuclearRect& area)
		{
			sWaterAreaInfo& tmpInfo = m_WaterAreas[waterID];
			tmpInfo.areas[tmpInfo.nextSamllID] = area;
			return uWaterAreaID(waterID, tmpInfo.nextSamllID++);
		}

		void NuclearWaterArea::RemoveAnWaterArea(uWaterAreaID areaID)
		{
			WaterAreaMap &wam = m_WaterAreas[areaID.infoID].areas;
			wam.erase(areaID.areaID);
		}

		void NuclearWaterArea::ModifyAnWaterArea(uWaterAreaID areaID, const NuclearRect& newArea)
		{
			sWaterAreaInfo& tmpInfo = m_WaterAreas[areaID.infoID];
			WaterAreaMap::iterator iter = tmpInfo.areas.find(areaID.areaID);
			if (iter != tmpInfo.areas.end())
			{
				iter->second = newArea;
			}
		}
	}
}
