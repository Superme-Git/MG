#ifndef __Nuclear_REGIONMAP_PTIMEEFFECT_H
#define __Nuclear_REGIONMAP_PTIMEEFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <map>
#include "..\common\xptypes.h"
#include "..\common\xmarshal.h"

namespace Nuclear
{
	namespace SubMap
	{
		//key是时间，目前是以毫秒为单位，在一个周期内的时间点
		typedef std::map<int, XPColorBalanceParam> TimeEffectParamMap;

		class PTimeEffect
		{
		public:
			//成员数据们
		private:
			TimeEffectParamMap m_mapEffects;

			//成员方法们
		private:

		public:
			///////////////////////////////初始化、构造与析构
			PTimeEffect(void);
			~PTimeEffect(void);

			///////////////////////////////输入输出
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////公共方法们
			bool AddAnTimeEffect(const int nTime, const XPColorBalanceParam& obj);
			bool RemoveAnTimeEffect(const int nTime);
			bool ModifyAnTimeEffect(const int nOldTime, const int nNewTime, const XPColorBalanceParam& obj);
			TimeEffectParamMap& GetTimeEffectParams()
			{
				return m_mapEffects;
			}
			void SetTimeEffectParams(const TimeEffectParamMap& map)
			{
				m_mapEffects = map;
			}

		};
	}
}
#endif