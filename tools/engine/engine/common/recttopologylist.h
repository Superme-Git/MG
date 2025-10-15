#ifndef __Nuclear_RECTTOPOLOGYLIST_H
#define __Nuclear_RECTTOPOLOGYLIST_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "xptypes.h"

namespace Nuclear
{

	typedef unsigned int SortBaseId;
	typedef std::set<SortBaseId> SortBaseIDSet;

	struct SortBaseRectType : public CRECT
	{
		bool IsSuperposition (const SortBaseRectType &x) const
		{
			return (*this == x);
		}

		bool IsBehind (const SortBaseRectType &x) const
		{
			if (IsSuperposition(x))
				return false;
			if ((bottom <= x.top) && 
				(
				(left >= x.left &&  left <= x.right) ||
				(right <= x.right)|| 
				(left < x.left &&  right > x.right)
				))
				return true;
			if ((right <= x.left) &&
				(
				(top >= x.top &&  top <= x.bottom) ||
				(bottom <= x.bottom)|| 
				(top < x.top &&  bottom > x.bottom)
				))
				return true;
			if (IsRectCross((*this), x))
			{
				////////////////////////////////////////////////////
				//某个菱形的某条边完全在另外一个菱形里面的相交的情况……（不包括一个菱形包括另外一个菱形的情况）
				if (
					((left < x.left &&  right > x.right) ||
					(left >= x.left &&  right <= x.right))
					&&
					((top < x.top && bottom < x.bottom) ||
					(bottom > x.bottom && top > x.top))
					)
				{
					if (bottom > x.top && top < x.top)
						return true;
					else 
						return false;
				}
				if (
					((top < x.top &&  bottom > x.bottom) ||
					(top >= x.top &&  bottom <= x.bottom))
					&&
					((left < x.left && right < x.right) ||
					(right > x.right && left > x.left))
					)
				{
					if (right > x.left && left < x.left)
						return true;
					else 
						return false;
				}

				//一个菱形包括另外一个菱形的情况，或者角相交了（没有任何一个菱形的任何一条边被另外一个菱形包住）
				//现在的(x,y)在正常坐标系下应该是(x-y,(x+y)*COS_58)，在现在只判断中心点正常模式下的Y的大小。
				//中心点在菱形坐标下的坐标是((left+right)/2, (top+bottom)/2)，在正常坐标系中的Y值是(left+right+top+bottom)*COS_58/2，
				//由于只需要判断大小，所以就不用*COS_58/2了
				int bigX, bigY, smallX, smallY, rectSqr, xSqr, cross;
				bool bIsmall = false;
				rectSqr = (bottom - top) * (right - left);
				xSqr = (x.bottom - x.top) * (x.right - x.left);
				if (rectSqr < xSqr)
				{
					//“我”小一点
					bigY = x.top - x.bottom;
					bigX = x.right - x.left;
					smallX = (left + right) / 2 - x.left;
					smallY = (top + bottom) / 2 - x.bottom;
					bIsmall = true;
				} else if (rectSqr > xSqr) {
					//“我”大一点
					bigY = top - bottom;
					bigX = right - left;
					smallX = (x.left + x.right) / 2 - left;
					smallY = (x.top + x.bottom) / 2 - bottom;
				} else {
					//“我们”一样大
					if (top + left + bottom + right < x.top + x.left + x.bottom + x.right)
						return true;
					else
						return false;
				}
				cross = smallX*bigY - smallY*bigX;
				if (cross < 0)
				{
					//小的能挡住大的
					return !bIsmall;
				} else if (cross > 0)
				{
					//大的能挡住小的
					return bIsmall;
				} else {
					//“我们”一样大
					if (top + left + bottom + right < x.top + x.left + x.bottom + x.right)
						return true;
				}
			}
			return false;
		}
	};

	struct TopologySortBase
	{
		SortBaseId sortBaseID;
		void *data;
		SortBaseRectType rect;
	};

	typedef TopologySortBase *PtrTopologySortBase;
	typedef std::vector<TopologySortBase> TopologyBaseVector;

	class RectTopologyList
	{
	///////////////////////结构体、自定义类型
		struct BaseStruct;
		typedef BaseStruct *PtrBaseStruct;
		typedef std::list<PtrBaseStruct> PtrBaseStructList;
		typedef std::set<PtrBaseStruct> PtrBaseStructSet;
		struct BaseStruct : public TopologySortBase
		{
			unsigned int dynamicInDegree;
			unsigned int inDegree;
			//PtrBaseStructList myFronts; //可以挡住本物件的物件的指针们
			//PtrBaseStructList myBacks;  //可以被本物件挡住的物件的指针们
			PtrBaseStructSet myFronts;//可以挡住本物件的物件的指针们
			PtrBaseStructSet myBacks; //可以被本物件挡住的物件的指针们
			BaseStruct& operator = (const TopologySortBase& x)
			{
				sortBaseID = x.sortBaseID;
				data = x.data;
				rect = x.rect;
				return *this;
			}
		};
	///////////////////////数据
	private:
		PtrBaseStructList m_PtrBaseStructs;
		SortBaseIDSet m_SortBaseIDs;
		SortBaseIDSet m_NewSortBaseIDs;	//这个其实可以不用存下来……
	///////////////////////构造与析构
	public:
		RectTopologyList(void);
		~RectTopologyList(void);
	///////////////////////私有方法
	private:
		bool RectTopologySort();
		void RemoveBaseStructInfoFromOthers(PtrBaseStruct ptrBaseStruct);
		void AddBaseStructInfoToOthers(PtrBaseStruct ptrBaseStruct);
		PtrBaseStructList::iterator FindPos(SortBaseId sortBaseID);
	///////////////////////公共方法们
	public:
		bool Dump(TopologyBaseVector& topologyBases, bool sort = true);
		const SortBaseIDSet& GetSortBaseIDSet() const
		{
			return m_SortBaseIDs;
		}
	};

}
#endif