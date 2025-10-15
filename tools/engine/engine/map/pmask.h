#ifndef __Nuclear_REGIONMAP_PMASK_H
#define __Nuclear_REGIONMAP_PMASK_H

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
		typedef std::map<int, XPMaskParam> MaskParamMap;

		class PMask
		{
			//��Ա������
		private:
			MaskParamMap m_mapMasks;

		public:
			///////////////////////////////��ʼ��������������
			PMask(void);
			~PMask(void);

			///////////////////////////////�������
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////����������
			bool AddAMask(const int nTime, const XPMaskParam& obj);
			bool RemoveAMask(const int nTime);
			bool ModifyAMask(const int nOldTime, const int nNewTime, const XPMaskParam& obj);
			MaskParamMap& GetMaskParams()
			{
				return m_mapMasks;
			}
			void SetMaskParams(const MaskParamMap& map)
			{
				m_mapMasks = map;
			}
		};
	}
}

#endif