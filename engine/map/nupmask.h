#ifndef __Nuclear_REGIONMAP_PMASK_H__
#define __Nuclear_REGIONMAP_PMASK_H__

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
		//key��ʱ�䣬Ŀǰ���Ժ���Ϊ��λ����һ�������ڵ�ʱ���
		typedef std::map<int, sNuclearMaskParam> MaskParamMap;

		class NuclearMask
		{
		public:
			NuclearMask(void);
			~NuclearMask(void);

			///////////////////////////////�������///////////////////////////////
			NuclearOStream& marshal(NuclearOStream &os) const;
			const INuclearStream& unmarshal(const INuclearStream &os, unsigned int version);

			bool AddAMask(const int nTime, const sNuclearMaskParam& obj);
			bool RemoveAMask(const int nTime);
			bool ModifyAMask(const int nOldTime, const int nNewTime, const sNuclearMaskParam& obj);
			MaskParamMap& GetMaskParams() { return m_mapMasks; }
			void SetMaskParams(const MaskParamMap& map) { m_mapMasks = map; }

		private:
			MaskParamMap m_mapMasks;
		};
	}
}

#endif