#ifndef __Nuclear_LKOTREENODELIST_H
#define __Nuclear_LKOTREENODELIST_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <list>
#include "..\map\plinkedobject.h"

namespace Nuclear
{

	//引擎、地图编辑器运行时结构体，不涉及到图片资源
	struct LkoTreeNode
	{
		LkoTreeNode *pParent;			//父节点指针
		LkoTreeNode *pRoot;				//根节点指针
		XPVECTOR2 fromMainRotateCent;		//从父节点旋转中心到自己的旋转中心的向量（如果自己是根节点，那么这个就是在地图上的坐标（世界坐标））
	//	XPMOVEFOLLOWTYPE moveFollowType;	//是否链接在主节点上（目前所有的链接都是软链接）
		int height;							//节点的高度，只有错切的时候用
		int width;							//节点的宽度，只有错切的时候用
		PLANTMOVEPARAM LinkedObjectMoveParam;	//美术设定值...
		CPOINT rotateCent;					//旋转中心，以当前节点左上角为原点

		///////////以下部分需要每帧Update
		float fTranslationx; //x方向平移值
		float fTranslationy; //y方向平移值

		int nBeginBrightTime;				//亮灯时间
		int nEndBrightTime;					//灭灯时间（当LinkedObjectMoveParam.m_dwFlag没有XP_LINKEDOBJ_LIGHT标志位的时候，这两个参数无效）
		float fAngle;						//当前旋转角度
		float fSkewx;						//当前斜切参数
		XPMATRIX3 mTransfer;				//平移矩阵（如果是硬链接在父节点上，会包括父节点的错切和旋转变形；
											//如果是软链接在父节点上，会包括父节点因错切和旋转变形造成的平移）
		XPMATRIX3 mTransAll;				//变换矩阵 >> Update后会等于 自身变形矩阵（包括错切、旋转）* mTransfer
		//如果不链接在父节点上，它的平移矩阵应该是 fromMainRotateCent * pParent->mTransfer 向量所代表的平移矩阵
		//如果是硬链接在父节点上，它的平移矩阵是 fromMainRotateCent 转换为平移矩阵后 乘以 pParent->mTransAll
		//如果是软链接在父节点上，它的平移矩阵应该是 fromMainRotateCent * pParent->mTransAll 向量所代表的平移矩阵
		//硬链接和软链接的区别就在于软挂的平移矩阵不包含平移以外的变换内容
		XPMATRIX3 mTransResult;				//变换矩阵（画图的时候的最终矩阵，包括原点从旋转中心到图片左上角的变换）
		bool CheckLightTime(int time) const
		{
			if (nBeginBrightTime > nEndBrightTime)
			{
				if (time > nEndBrightTime && time < nBeginBrightTime)
					return false;
			} else {
				if (time > nEndBrightTime || time < nBeginBrightTime)
					return false;
			}
			return true;
		}
	};

	typedef std::vector<LkoTreeNode*> DTList;

	class LkoTreeNodeList
	{
	protected:
		DTList m_Lists;

	private:
		bool m_bTouchFlag;
		float m_fLkoLife; //当扰动消除之后的倒计时..

	public:
		void SetZoomParamFlag(bool bflag) //触发特效..状态改变时候必须调用
		{
			m_bTouchFlag = bflag;
			if(bflag)
				m_fLkoLife = m_fLKOTime;
		}

	protected: //下面三个为美术参数---
		bool m_bLKOEnableZoom; //是否允许被干扰
		float m_fLKOzoom;//干扰最激烈程度
		float m_fLKOTime;//恢复所需时间
		bool  m_Static;//是否静止

	public:
		LkoTreeNodeList(void);
		virtual ~LkoTreeNodeList(void);

		const DTList &GetList() const { return m_Lists; }
		void Update(int timeDelta, int viewPortLeft, int viewPortTop, float fRatio);
		void SimpleUpdate(int timeDelta, int viewPortLeft, int viewPortTop, float fRatio);
		void AddANode(LkoTreeNode *node);
		void RandomBrightTime();
		void SetStaticFlag(bool bStatic) { m_Static = bStatic; }
		//void RemoveATree(LkoTreeNode *root);
	};
}


#endif