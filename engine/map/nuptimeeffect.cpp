//#include "../common/ljfmutil.h"
#include "nuptimeeffect.h"
#include "nupmap.h"


namespace Nuclear
{
	namespace SubMap
	{
		NuclearTimeEffect::NuclearTimeEffect(void)
		{
		}

		NuclearTimeEffect::~NuclearTimeEffect(void){}

		NuclearOStream& NuclearTimeEffect::marshal(NuclearOStream &os) const
		{
			TimeEffectParamMap::const_iterator iter = m_TimeEffects.begin();
			int count = static_cast<int>(m_TimeEffects.size());
			os << count;
			for (; iter != m_TimeEffects.end(); ++iter)
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
		const INuclearStream& NuclearTimeEffect::unmarshal(const INuclearStream &os, unsigned int version)
		{
			m_TimeEffects.clear();
			int count, i, nTime;
			float fContrast = 0.0;
			sNuclearColorBalanceParam param;
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
				m_TimeEffects[nTime] = param;
			}
			return os;
		}

		bool NuclearTimeEffect::AddAnTimeEffect(const int nTime, const sNuclearColorBalanceParam& obj)
		{
			if (m_TimeEffects.find(nTime) != m_TimeEffects.end())
				return false;
			m_TimeEffects[nTime] = obj;
			return true;
		}

		bool NuclearTimeEffect::RemoveAnTimeEffect(const int nTime)
		{
			return (m_TimeEffects.erase(nTime) > 0);
		}

		bool NuclearTimeEffect::ModifyAnTimeEffect(const int nOldTime, const int nNewTime, const sNuclearColorBalanceParam& obj)
		{
			if (nOldTime != nNewTime)
			{
				if (m_TimeEffects.find(nNewTime) != m_TimeEffects.end())
					return false;
				m_TimeEffects.erase(nOldTime);
			}
			m_TimeEffects[nNewTime] = obj;
			return true;
		}
	}
}
