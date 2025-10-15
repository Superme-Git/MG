#include "stdafx.h"
//#include "..\common\pfsutil.h"
#include "ptimeeffect.h"
#include "pmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		PTimeEffect::PTimeEffect(void)
		{
		}

		PTimeEffect::~PTimeEffect(void){}

		XOStream& PTimeEffect::marshal(XOStream &os) const
		{
			TimeEffectParamMap::const_iterator iter = m_mapEffects.begin();
			int count = static_cast<int>(m_mapEffects.size());
			os << count;
			for (; iter != m_mapEffects.end(); ++iter)
			{
				os << iter->first;
				os << iter->second.fCoefr;
				os << iter->second.fCoefg;
				os << iter->second.fCoefb;
				os << iter->second.fContrastk;
				os << iter->second.fContrastb;
		//		os << iter->second.fContrast;
				os << iter->second.fLightness;
			}
			return os;
		}
		const XIStream& PTimeEffect::unmarshal(const XIStream &os, unsigned int version)
		{
			m_mapEffects.clear();
			int count, i, nTime;
			float fContrast = 0.0;
			XPColorBalanceParam param;
			os >> count;
			for (i=0;i<count;++i)
			{
				os >> nTime;
				os >> param.fCoefr;
				os >> param.fCoefg;
				os >> param.fCoefb;
				os >> param.fContrastk;
				os >> param.fContrastb;
				os >> param.fLightness;
				m_mapEffects[nTime] = param;
			}
			return os;
		}

		bool PTimeEffect::AddAnTimeEffect(const int nTime, const XPColorBalanceParam& obj)
		{
			if (m_mapEffects.find(nTime) != m_mapEffects.end())
				return false;
			m_mapEffects[nTime] = obj;
			return true;
		}

		bool PTimeEffect::RemoveAnTimeEffect(const int nTime)
		{
			return (m_mapEffects.erase(nTime) > 0);
		}

		bool PTimeEffect::ModifyAnTimeEffect(const int nOldTime, const int nNewTime, const XPColorBalanceParam& obj)
		{
			if (nOldTime != nNewTime)
			{
				if (m_mapEffects.find(nNewTime) != m_mapEffects.end())
					return false;
				m_mapEffects.erase(nOldTime);
			}
			m_mapEffects[nNewTime] = obj;
			return true;
		}
	}
}
