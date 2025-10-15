#ifndef __Nuclear_PBACKGROUND_H
#define __Nuclear_PBACKGROUND_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "mapobjs.h"

namespace Nuclear
{
	namespace SubMap
	{
		typedef struct _PBackGoundInfo 
		{
			std::wstring name;	//名字
			CPOINT pos;			//图片中心点位置，地图坐标
			FPOINT relSpeed;	//速度比，[0~1]，当速度比->(1, 1)时，意味着几乎全地图可见，几乎永远跟着viewport跑，当速度比->(0, 0)时，就退化为普通背景了
			CRECT activeRect;	//激活区，地图坐标
			int girdWidth;		//格子的宽度
			int girdHeight;		//格子的高度
			int width;			//图片几个格子宽
			int height;			//图片几个格子高
			ObjMapKey* pPics;	//图片的内容数组
			bool wrapHorz;		//是否水平方向缠绕	//如果以后要做什么镜像之类的就用标志位来做吧
			bool wrapVert;		//是否竖直方向缠绕
			bool m_bDeleted;		//是否删除了
			_PBackGoundInfo() : name(L""), pos(0, 0), relSpeed(0.0f, 0.0f), activeRect(0, 0, 0, 0), girdWidth(0), 
				girdHeight(0), width(0), height(0), pPics(NULL), wrapHorz(false), wrapVert(false), m_bDeleted(false) {}
		} PBackGoundInfo;

		typedef std::vector<PBackGoundInfo> PBackGoundInfoVector;

		class PBackGround
		{
		public:
			PBackGoundInfoVector m_backGroundInfos;
			int					 m_backGroundInfoCount;

		private:
			void ClearAll();

		public:
			PBackGround(void);
			~PBackGround(void);

			PBackGround& operator= (const PBackGround &x);
			///////////////////////////////输入输出
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);
		};
	}
}

#endif