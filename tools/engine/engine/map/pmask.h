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
		//key是时间，目前是以毫秒为单位，在一个周期内的时间点
		typedef std::map<int, XPMaskParam> MaskParamMap;

		class PMask
		{
			//成员数据们
		private:
			MaskParamMap m_mapMasks;

		public:
			///////////////////////////////初始化、构造与析构
			PMask(void);
			~PMask(void);

			///////////////////////////////输入输出
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////公共方法们
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