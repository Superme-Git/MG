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
				//ĳ�����ε�ĳ������ȫ������һ������������ཻ�����������������һ�����ΰ�������һ�����ε������
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

				//һ�����ΰ�������һ�����ε���������߽��ཻ�ˣ�û���κ�һ�����ε��κ�һ���߱�����һ�����ΰ�ס��
				//���ڵ�(x,y)����������ϵ��Ӧ����(x-y,(x+y)*COS_58)��������ֻ�ж����ĵ�����ģʽ�µ�Y�Ĵ�С��
				//���ĵ������������µ�������((left+right)/2, (top+bottom)/2)������������ϵ�е�Yֵ��(left+right+top+bottom)*COS_58/2��
				//����ֻ��Ҫ�жϴ�С�����ԾͲ���*COS_58/2��
				int bigX, bigY, smallX, smallY, rectSqr, xSqr, cross;
				bool bIsmall = false;
				rectSqr = (bottom - top) * (right - left);
				xSqr = (x.bottom - x.top) * (x.right - x.left);
				if (rectSqr < xSqr)
				{
					//���ҡ�Сһ��
					bigY = x.top - x.bottom;
					bigX = x.right - x.left;
					smallX = (left + right) / 2 - x.left;
					smallY = (top + bottom) / 2 - x.bottom;
					bIsmall = true;
				} else if (rectSqr > xSqr) {
					//���ҡ���һ��
					bigY = top - bottom;
					bigX = right - left;
					smallX = (x.left + x.right) / 2 - left;
					smallY = (x.top + x.bottom) / 2 - bottom;
				} else {
					//�����ǡ�һ����
					if (top + left + bottom + right < x.top + x.left + x.bottom + x.right)
						return true;
					else
						return false;
				}
				cross = smallX*bigY - smallY*bigX;
				if (cross < 0)
				{
					//С���ܵ�ס���
					return !bIsmall;
				} else if (cross > 0)
				{
					//����ܵ�סС��
					return bIsmall;
				} else {
					//�����ǡ�һ����
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
	///////////////////////�ṹ�塢�Զ�������
		struct BaseStruct;
		typedef BaseStruct *PtrBaseStruct;
		typedef std::list<PtrBaseStruct> PtrBaseStructList;
		typedef std::set<PtrBaseStruct> PtrBaseStructSet;
		struct BaseStruct : public TopologySortBase
		{
			unsigned int dynamicInDegree;
			unsigned int inDegree;
			//PtrBaseStructList myFronts; //���Ե�ס������������ָ����
			//PtrBaseStructList myBacks;  //���Ա��������ס�������ָ����
			PtrBaseStructSet myFronts;//���Ե�ס������������ָ����
			PtrBaseStructSet myBacks; //���Ա��������ס�������ָ����
			BaseStruct& operator = (const TopologySortBase& x)
			{
				sortBaseID = x.sortBaseID;
				data = x.data;
				rect = x.rect;
				return *this;
			}
		};
	///////////////////////����
	private:
		PtrBaseStructList m_PtrBaseStructs;
		SortBaseIDSet m_SortBaseIDs;
		SortBaseIDSet m_NewSortBaseIDs;	//�����ʵ���Բ��ô���������
	///////////////////////����������
	public:
		RectTopologyList(void);
		~RectTopologyList(void);
	///////////////////////˽�з���
	private:
		bool RectTopologySort();
		void RemoveBaseStructInfoFromOthers(PtrBaseStruct ptrBaseStruct);
		void AddBaseStructInfoToOthers(PtrBaseStruct ptrBaseStruct);
		PtrBaseStructList::iterator FindPos(SortBaseId sortBaseID);
	///////////////////////����������
	public:
		bool Dump(TopologyBaseVector& topologyBases, bool sort = true);
		const SortBaseIDSet& GetSortBaseIDSet() const
		{
			return m_SortBaseIDs;
		}
	};

}
#endif