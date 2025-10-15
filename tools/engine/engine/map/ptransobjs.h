#ifndef __Nuclear_REGIONMAP_PTRANSOBJS_H
#define __Nuclear_REGIONMAP_PTRANSOBJS_H

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
		struct TransObjInfo
		{
			//std::wstring picName;//从/element开始。。。。
			CRECT picRect; //图片初始位置，地图坐标
			CPOINT scalPoint; //放大中心点，地图坐标
			unsigned int time;	//单位：毫秒
			int width;
			int height;
		};

		typedef std::map<unsigned int, TransObjInfo> TransObjInfoMap;

		class PTransObjs
		{
		public:
			//成员数据们
		private:
			TransObjInfoMap m_mapTrans;
			unsigned int m_nMaxId;

			//成员方法们
		private:

		public:
			///////////////////////////////初始化、构造与析构
			PTransObjs(void);
			~PTransObjs(void);

			///////////////////////////////输入输出
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////公共方法们
			unsigned int AddAnTransObj(const TransObjInfo& obj);
			bool RemoveAnTransObj(const unsigned int TransId);
			void ModifyAnTransObj(const unsigned int TransId, const TransObjInfo& obj);
			const TransObjInfoMap& GetTransInfos()
			{
				return m_mapTrans;
			}
			void SetTransObjInfoMap(const TransObjInfoMap& map)
			{
				m_mapTrans = map;
			}

		};
	}
}
#endif