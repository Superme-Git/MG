#include "stdafx.h"
//#include "..\common\pfsutil.h"
#include "ptriggerobjs.h"
#include "pmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		PWaterArea::PWaterArea(void)
		{
			m_nMaxId = 0;
			m_mapWaters.clear();
		}

		PWaterArea::~PWaterArea(void){}

		XOStream& PWaterArea::marshal(XOStream &os) const
		{
			WaterAreaInfoMap::const_iterator iter = m_mapWaters.begin();
			WaterAreaMap::const_iterator areaIter, areaEnd;
			int count = static_cast<int>(m_mapWaters.size());
			os << count;
			int areacount;
			for (; iter != m_mapWaters.end(); ++iter)
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
		const XIStream& PWaterArea::unmarshal(const XIStream &os, unsigned int version)
		{
			m_mapWaters.clear();
			m_nMaxId = 0;
			unsigned int count, areacount, i, j;
			unsigned int waterID;
			CRECT rect;
			os >> count;
			for (i=0;i<count;++i)
			{
				os >> waterID;
				if (waterID > m_nMaxId)
				{
					m_nMaxId = waterID;
				}
				WaterAreaInfo& tmpInfo = m_mapWaters[waterID];
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
		WaterAreaInfoID PWaterArea::AddAnWaterArea(unsigned int depth, unsigned int border)
		{
			WaterAreaInfo& tmpInfo = m_mapWaters[++m_nMaxId];
			tmpInfo.depth = depth;
			tmpInfo.border = border;
			return m_nMaxId;
		}

		bool PWaterArea::RemoveAnWaterArea(WaterAreaInfoID waterID)
		{
			return (m_mapWaters.erase(waterID) > 0);
		}

		void PWaterArea::ModifyAnWaterArea(WaterAreaInfoID waterID, unsigned int depth, unsigned int border)
		{
			WaterAreaInfo& tmpInfo = m_mapWaters[waterID];
			tmpInfo.depth = depth;
			tmpInfo.border = border;
		}
	
		WaterAreaID PWaterArea::AddAnWaterArea(WaterAreaInfoID waterID, const CRECT& area)
		{
			WaterAreaInfo& tmpInfo = m_mapWaters[waterID];
			tmpInfo.areas[tmpInfo.nextSamllID] = area;
			return WaterAreaID(waterID, tmpInfo.nextSamllID++);
		}

		void PWaterArea::RemoveAnWaterArea(WaterAreaID areaID)
		{
			WaterAreaMap &wam = m_mapWaters[areaID.infoID].areas;
			wam.erase(areaID.areaID);
		}

		void PWaterArea::ModifyAnWaterArea(WaterAreaID areaID, const CRECT& newArea)
		{
			WaterAreaInfo& tmpInfo = m_mapWaters[areaID.infoID];
			WaterAreaMap::iterator iter = tmpInfo.areas.find(areaID.areaID);
			if (iter != tmpInfo.areas.end())
			{
				iter->second = newArea;
			}
		}
	}
}
