//#include "../common/ljfmutil.h"
#include "nuptransobjs.h"
#include "nupmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		NuclearTransObjs::NuclearTransObjs(void)
		{
			m_nMaxId = 0;
		}

		NuclearTransObjs::~NuclearTransObjs(void){}

		NuclearOStream& NuclearTransObjs::marshal(NuclearOStream &os) const
		{
			//传送对象列表
			TransObjInfoMap::const_iterator iter = m_mapTrans.begin();
			int count = static_cast<int>(m_mapTrans.size());
			os << count;
			for (; iter != m_mapTrans.end(); ++iter)
			{
				os << iter->first;
				os << iter->second.picRect;
				os << iter->second.scalPoint;
				os << iter->second.time;
				os << iter->second.width;
				os << iter->second.height;
			}
			return os;
		}
		const INuclearStream& NuclearTransObjs::unmarshal(const INuclearStream &os, unsigned int version)
		{
			m_mapTrans.clear();
			m_nMaxId = 0;			
			int count, i;
			unsigned int transID;
			sTransObjInfo tmpInfo;
			os >> count;
			m_nMaxId = 0;
			for (i=0;i<count;++i)
			{
				os >> transID;
				if (transID > m_nMaxId)
				{
					m_nMaxId = transID;
				}
				os >> tmpInfo.picRect;
				os >> tmpInfo.scalPoint;
				os >> tmpInfo.time;
				os >> tmpInfo.width;
				os >> tmpInfo.height;
				m_mapTrans[transID] = tmpInfo;
			}
			return os;
		}

		unsigned int NuclearTransObjs::AddAnTransObj(const sTransObjInfo& obj)
		{
			m_mapTrans[++m_nMaxId] = obj;
			return m_nMaxId;
		}

		bool NuclearTransObjs::RemoveAnTransObj(const unsigned int TransId)
		{
			return (m_mapTrans.erase(TransId) > 0);
		}

		void NuclearTransObjs::ModifyAnTransObj(const unsigned int TransId, const sTransObjInfo& obj)
		{
			m_mapTrans[TransId] = obj;
		}
	}
}
