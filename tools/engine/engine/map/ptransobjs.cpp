#include "stdafx.h"
//#include "..\common\pfsutil.h"
#include "ptransobjs.h"
#include "pmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		PTransObjs::PTransObjs(void)
		{
			m_nMaxId = 0;
		}

		PTransObjs::~PTransObjs(void){}

		XOStream& PTransObjs::marshal(XOStream &os) const
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
		const XIStream& PTransObjs::unmarshal(const XIStream &os, unsigned int version)
		{
			m_mapTrans.clear();
			m_nMaxId = 0;			
			int count, i;
			unsigned int transID;
			TransObjInfo tmpInfo;
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

		unsigned int PTransObjs::AddAnTransObj(const TransObjInfo& obj)
		{
			m_mapTrans[++m_nMaxId] = obj;
			return m_nMaxId;
		}

		bool PTransObjs::RemoveAnTransObj(const unsigned int TransId)
		{
			return (m_mapTrans.erase(TransId) > 0);
		}

		void PTransObjs::ModifyAnTransObj(const unsigned int TransId, const TransObjInfo& obj)
		{
			m_mapTrans[TransId] = obj;
		}
	}
}
