#include "nupmap.h"

namespace Nuclear
{
	namespace SubMap
	{
		NuclearMask::NuclearMask(void)
		{
		}

		NuclearMask::~NuclearMask(void)
		{
		}

		NuclearOStream& NuclearMask::marshal(NuclearOStream &os) const
		{
			MaskParamMap::const_iterator iter = m_mapMasks.begin();
			int count = static_cast<int>(m_mapMasks.size());
			os << count;
			for (; iter != m_mapMasks.end(); ++iter)
			{
				os << iter->first;
				os << iter->second.picuri;
				os << (int)iter->second.blendMode;
				os << iter->second.lastAlpha;
				os << iter->second.alpha;
			}
			return os;
		}

		const INuclearStream& NuclearMask::unmarshal(const INuclearStream &os, unsigned int version)
		{
			m_mapMasks.clear();
			if (version <= NuclearMap::MAP_OLD_LAYER_COUNT_AND_NO_MASK)
				return os;
			int count, i, nTime, tmpBlendMode;
			sNuclearMaskParam param;
			os >> count;
			for (i=0;i<count;++i)
			{
				os >> nTime;
				os >> param.picuri;
				os >> tmpBlendMode;
				param.blendMode = static_cast<NuclearTextureBlendMode>(tmpBlendMode);
				os >> param.lastAlpha;
				os >> param.alpha;
				m_mapMasks[nTime] = param;
			}
			return os;
		}

		bool NuclearMask::AddAMask(const int nTime, const sNuclearMaskParam& obj)
		{
			if (m_mapMasks.find(nTime) != m_mapMasks.end())
				return false;
			m_mapMasks[nTime] = obj;
			return true;
		}

		bool NuclearMask::RemoveAMask(const int nTime)
		{
			return (m_mapMasks.erase(nTime) > 0);
		}

		bool NuclearMask::ModifyAMask(const int nOldTime, const int nNewTime, const sNuclearMaskParam& obj)
		{
			if (nOldTime != nNewTime)
			{
				if (m_mapMasks.find(nNewTime) != m_mapMasks.end())
					return false;
				m_mapMasks.erase(nOldTime);
			}
			m_mapMasks[nNewTime] = obj;
			return true;
		}
	}
}
