#ifndef __Nuclear_REGIONMAP_PTIMEEFFECT_H__
#define __Nuclear_REGIONMAP_PTIMEEFFECT_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <map>
#include "../common/nuxptypes.h"
#include "../common/nuxmarshal.h"

namespace Nuclear
{
	namespace SubMap
	{
		//key是时间，目前是以毫秒为单位，在一个周期内的时间点
		typedef std::map<int, sNuclearColorBalanceParam> TimeEffectParamMap;

		class NuclearTimeEffect
		{
		public:
			NuclearTimeEffect(void);
			~NuclearTimeEffect(void);

			///////////////////////////////输入输出///////////////////////////////
			NuclearOStream& marshal(NuclearOStream &os) const;
			const INuclearStream& unmarshal(const INuclearStream &os, unsigned int version);

			bool AddAnTimeEffect(const int nTime, const sNuclearColorBalanceParam& obj);
			bool RemoveAnTimeEffect(const int nTime);
			bool ModifyAnTimeEffect(const int nOldTime, const int nNewTime, const sNuclearColorBalanceParam& obj);
			TimeEffectParamMap& GetTimeEffectParams() { return m_TimeEffects; }
			void SetTimeEffectParams(const TimeEffectParamMap& map) { m_TimeEffects = map; }

		private:
			TimeEffectParamMap m_TimeEffects;

		};
	}
}
#endif