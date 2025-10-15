#include "stdafx.h"
//#include "..\common\pfsutil.h"
#include "ptriggerobjs.h"
#include "pmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		PTriggerObjs::PTriggerObjs(void)
		{
			m_nNextId = 0;
			m_mapTriggers.clear();
		}

		PTriggerObjs::~PTriggerObjs(void){}

		XOStream& PTriggerObjs::marshal(XOStream &os) const
		{
			//´¥·¢ÌØÐ§¡«
			TriggerInfoMap::const_iterator iter = m_mapTriggers.begin();
			TriggerInfoVet::const_iterator vetIter;
			TriggerAreaMap::const_iterator areaIter;
			os << static_cast<unsigned int>(m_mapTriggers.size());
			unsigned int infoCount;
			for (; iter != m_mapTriggers.end(); ++iter)
			{
				const TriggerObjInfo &info = iter->second;
				os << info.name;
				infoCount = 0;
				for (vetIter=info.triggerInfos.begin();vetIter!=info.triggerInfos.end();++vetIter)
				{
					if (!(vetIter->triggerTypeFlag & TRIGGER_DELETE))
						++infoCount;
				}
				os << infoCount;
				for (vetIter=info.triggerInfos.begin();vetIter!=info.triggerInfos.end();++vetIter)
				{
					if (vetIter->triggerTypeFlag & TRIGGER_DELETE)
						continue;
					os << vetIter->effectMapKey;
					os << vetIter->triggerTypeFlag;
					os << vetIter->actionsFlag;
					os << vetIter->beginTime;
					os << vetIter->endTime;
					os << vetIter->frequency;
					os << vetIter->triDelta;
					os << vetIter->maxDelta;
					os << vetIter->excursion;
					os << static_cast<int>(vetIter->layer);

				}
				os << static_cast<int>(info.areas.size());
				for (areaIter=info.areas.begin();areaIter!=info.areas.end();++areaIter)
				{
					os << areaIter->second;
				}
			}
			return os;
		}
		const XIStream& PTriggerObjs::unmarshal(const XIStream &os, unsigned int version)
		{
			m_mapTriggers.clear();
			m_nNextId = 0;
			unsigned int count, i;
			os >> count;
			int j, tmpCount, tmpLayer;
			TriggerInfo tmpInfo;
			for (i=0;i<count;++i)
			{
				TriggerObjInfo &info = m_mapTriggers[i];
				os >> info.name;
				os >> tmpCount;
				for (j=0;j<tmpCount;++j)
				{
					info.triggerInfos.push_back(tmpInfo);
					os >> info.triggerInfos[j].effectMapKey;
					os >> info.triggerInfos[j].triggerTypeFlag;
					os >> info.triggerInfos[j].actionsFlag;
					os >> info.triggerInfos[j].beginTime;
					os >> info.triggerInfos[j].endTime;
					os >> info.triggerInfos[j].frequency;
					os >> info.triggerInfos[j].triDelta;
					os >> info.triggerInfos[j].maxDelta;
					os >> info.triggerInfos[j].excursion;
					os >> tmpLayer;
					info.triggerInfos[j].layer = static_cast<XPEFFECT_LAYER>(tmpLayer);
				}
				os >> tmpCount;
				for (j=0;j<tmpCount;++j)
				{
					os >> info.areas[j];
				}
				info.nextSamllID = tmpCount;
			}
			m_nNextId = count;
			return os;
		}

		TriggerInfoID PTriggerObjs::AddAnTriggerInfoVet(const std::wstring &name, const TriggerInfoVet& obj)
		{
			m_mapTriggers[m_nNextId].triggerInfos = obj;
			m_mapTriggers[m_nNextId].name = name;
			return m_nNextId++;
		}

		bool PTriggerObjs::RemoveAnTriggerInfoVet(TriggerInfoID infoID)
		{
			return (m_mapTriggers.erase(infoID) > 0);
		}

		void PTriggerObjs::ModifyAnTriggerInfoVet(TriggerInfoID infoID, const std::wstring &name, const TriggerInfoVet& obj)
		{
			m_mapTriggers[infoID].triggerInfos = obj;
			m_mapTriggers[infoID].name = name;
		}

		TriggerID PTriggerObjs::AddAnTriggerArea(TriggerInfoID infoID, const CRECT& area)
		{
			TriggerID result;
			result.infoID = infoID;
			TriggerObjInfo &info = m_mapTriggers[infoID];
			result.areaID = info.nextSamllID++;
			info.areas[result.areaID] = area;
			return result;
		}

		void PTriggerObjs::RemoveAnTriggerArea(TriggerID triggerID)
		{
			m_mapTriggers[triggerID.infoID].areas.erase(triggerID.areaID);
		}

		void PTriggerObjs::ModifyAnTriggerArea(TriggerID triggerID, const CRECT& newArea)
		{
			m_mapTriggers[triggerID.infoID].areas[triggerID.areaID] = newArea;
		}
	}
}
