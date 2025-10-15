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
		//key��ʱ�䣬Ŀǰ���Ժ���Ϊ��λ����һ�������ڵ�ʱ���
		typedef std::map<int, XPColorBalanceParam> TimeEffectParamMap;

		class PTimeEffect
		{
		public:
			//��Ա������
		private:
			TimeEffectParamMap m_mapEffects;

			//��Ա������
		private:

		public:
			///////////////////////////////��ʼ��������������
			PTimeEffect(void);
			~PTimeEffect(void);

			///////////////////////////////�������
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////����������
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