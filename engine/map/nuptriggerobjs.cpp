//#include "../common/ljfmutil.h"
#include "nuptriggerobjs.h"
#include "nupmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		NuclearTriggerObjs::NuclearTriggerObjs(void)
		{
			m_nNextTriggerID = 0;
			m_TriggerInfos.clear();
		}

		NuclearTriggerObjs::~NuclearTriggerObjs(void){}

		NuclearOStream& NuclearTriggerObjs::marshal(NuclearOStream &os) const
		{
			//´¥·¢ÌØÐ§¡«
			TriggerInfoMap::const_iterator iter = m_TriggerInfos.begin();
			TriggerInfoVet::const_iterator vetIter;
			TriggerAreaMap::const_iterator areaIter;
			os << static_cast<unsigned int>(m_TriggerInfos.size());
			unsigned int infoCount;
			for (; iter != m_TriggerInfos.end(); ++iter)
			{
				const sTriggerObjInfo &info = iter->second;
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
		const INuclearStream& NuclearTriggerObjs::unmarshal(const INuclearStream &os, unsigned int version)
		{
			m_TriggerInfos.clear();
			m_nNextTriggerID = 0;
			unsigned int count, i;
			os >> count;
			int j, tmpCount, tmpLayer;
			sTriggerInfo tmpInfo;
			for (i=0;i<count;++i)
			{
				sTriggerObjInfo &info = m_TriggerInfos[i];
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
					info.triggerInfos[j].layer = static_cast<Nuclear_EffectLayer>(tmpLayer);
				}
				os >> tmpCount;
				for (j=0;j<tmpCount;++j)
				{
					os >> info.areas[j];
				}
				info.nextSamllID = tmpCount;
			}
			m_nNextTriggerID = count;
			return os;
		}

		TriggerInfoID NuclearTriggerObjs::AddAnTriggerInfoVet(const std::wstring &name, const TriggerInfoVet& obj)
		{
			m_TriggerInfos[m_nNextTriggerID].triggerInfos = obj;
			m_TriggerInfos[m_nNextTriggerID].name = name;
			return m_nNextTriggerID++;
		}

		bool NuclearTriggerObjs::RemoveAnTriggerInfoVet(TriggerInfoID infoID)
		{
			return (m_TriggerInfos.erase(infoID) > 0);
		}

		void NuclearTriggerObjs::ModifyAnTriggerInfoVet(TriggerInfoID infoID, const std::wstring &name, const TriggerInfoVet& obj)
		{
			m_TriggerInfos[infoID].triggerInfos = obj;
			m_TriggerInfos[infoID].name = name;
		}

		sTriggerID NuclearTriggerObjs::AddAnTriggerArea(TriggerInfoID infoID, const NuclearRect& area)
		{
			sTriggerID result;
			result.infoID = infoID;
			sTriggerObjInfo &info = m_TriggerInfos[infoID];
			result.areaID = info.nextSamllID++;
			info.areas[result.areaID] = area;
			return result;
		}

		void NuclearTriggerObjs::RemoveAnTriggerArea(sTriggerID triggerID)
		{
			m_TriggerInfos[triggerID.infoID].areas.erase(triggerID.areaID);
		}

		void NuclearTriggerObjs::ModifyAnTriggerArea(sTriggerID triggerID, const NuclearRect& newArea)
		{
			m_TriggerInfos[triggerID.infoID].areas[triggerID.areaID] = newArea;
		}
	}
}
