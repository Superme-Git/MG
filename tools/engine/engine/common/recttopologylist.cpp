#include "stdafx.h"
#include "recttopologylist.h"


namespace Nuclear
{
	RectTopologyList::RectTopologyList(void)
	{
		//m_TopologyBaseList.reserve(1000);
		//m_PtrBaseStructs.reserve(1000);
	}

	RectTopologyList::~RectTopologyList(void)
	{
		PtrBaseStructList::iterator iter = m_PtrBaseStructs.begin();
		for(;iter!=m_PtrBaseStructs.end();++iter)
		{
			delete(*iter);
		}
	}	



	bool RectTopologyList::RectTopologySort()
	{
		PtrBaseStructList::iterator iter, pos, minIter, endIter;
		PtrBaseStructSet::iterator frontsIter;
		size_t minDegree;
		bool result = true;
		for (iter=m_PtrBaseStructs.begin();iter!=m_PtrBaseStructs.end();++iter)
		{
			(*iter)->dynamicInDegree = (*iter)->inDegree;
		}
		pos = m_PtrBaseStructs.begin();
		while (pos != m_PtrBaseStructs.end())
		{
			minDegree = m_PtrBaseStructs.size();
			endIter = m_PtrBaseStructs.end();
			minIter = endIter;
			for(iter = pos;iter!=endIter;++iter)
			{
				if ((*iter)->dynamicInDegree == 0)
					break;
				if (minDegree > (*iter)->dynamicInDegree)
				{
					minDegree = (*iter)->dynamicInDegree;
					minIter = iter;
				}
			}
			if (iter == endIter)//˵�����ڻ�����һ�������С��
			{
				result = false;
				if (minIter == endIter)
					break;
				iter = minIter;
			}
			for (frontsIter = (*iter)->myFronts.begin();frontsIter!=(*iter)->myFronts.end();++frontsIter)//�����ܵ�ס*iter�Ķ�̬��ȶ���ȥ1
			{
				((*frontsIter)->dynamicInDegree)--;
			}

			if (iter != pos)
			{
				m_PtrBaseStructs.insert(pos, *iter);
				m_PtrBaseStructs.erase(iter);
			} else {
				pos++;
			}
		}
		if (!result)
		{
			return false;
		}
		return true;
	}

	//ʹ��Set����list�Ż��˸ú���
	void RectTopologyList::RemoveBaseStructInfoFromOthers(PtrBaseStruct ptrBaseStruct)
	{
		PtrBaseStructSet::iterator iter,iterEnd = ptrBaseStruct->myFronts.end();
		for (iter=ptrBaseStruct->myFronts.begin();iter!=iterEnd;++iter)
		{
			(*iter)->myBacks.erase(ptrBaseStruct);
			(*iter)->inDegree--;
		}
		iterEnd = ptrBaseStruct->myBacks.end();
		for (iter=ptrBaseStruct->myBacks.begin();iter!=iterEnd;++iter)
		{
			(*iter)->myFronts.erase(ptrBaseStruct);
		}
	}
	
	 //ʹ��Set����list�޸��˸ú���
	void RectTopologyList::AddBaseStructInfoToOthers(PtrBaseStruct ptrBaseStruct)
	{
		PtrBaseStructList::iterator baseStructIter, baseStructIterEnd;
		baseStructIter = m_PtrBaseStructs.begin();
		baseStructIterEnd = m_PtrBaseStructs.end();
		for(;baseStructIter!=baseStructIterEnd;++baseStructIter)
		{
			if((*baseStructIter)->rect.IsBehind(ptrBaseStruct->rect))
			{
				(*baseStructIter)->myFronts.insert(ptrBaseStruct);
				ptrBaseStruct->myBacks.insert(*baseStructIter);
				(ptrBaseStruct->inDegree)++;
			}else if (ptrBaseStruct->rect.IsBehind((*baseStructIter)->rect))
			{
				ptrBaseStruct->myFronts.insert(*baseStructIter);
				(*baseStructIter)->myBacks.insert(ptrBaseStruct);
				((*baseStructIter)->inDegree)++;
			}
		}
	}

	//�Ż�Dump����������
	bool RectTopologyList::Dump(TopologyBaseVector& topologyBases, bool sort)
	{				
		//�����µ�BaseID�ļ���
		m_NewSortBaseIDs.clear();		

		TopologyBaseVector::iterator baseIter=topologyBases.begin();
		TopologyBaseVector::iterator baseIterEnd = topologyBases.end();	
		for (;baseIter!=baseIterEnd;++baseIter){
			m_NewSortBaseIDs.insert(baseIter->sortBaseID);
		}

		//ɾ����ID���������Ѿ�û���˵Ķ���
		SortBaseIDSet::iterator newSortEnd=m_NewSortBaseIDs.end();
		PtrBaseStructList::iterator baseStructIter=m_PtrBaseStructs.begin();
		for(;baseStructIter!=m_PtrBaseStructs.end();)
		{
			if (m_NewSortBaseIDs.find((*baseStructIter)->sortBaseID) == newSortEnd)
			{
				RemoveBaseStructInfoFromOthers(*baseStructIter);
				delete(*baseStructIter);
				m_PtrBaseStructs.erase(baseStructIter++);	
			} else {
				++baseStructIter;
			}
		}

		//���롢�޸�
		PtrBaseStruct ptrBaseStruct = NULL;
		SortBaseIDSet::iterator sortEnd=m_SortBaseIDs.end();
		for (baseIter=topologyBases.begin();baseIter!=baseIterEnd;++baseIter)
		{
			if (m_SortBaseIDs.find(baseIter->sortBaseID) == sortEnd)
			{
				//����ԭ��û�е�
				ptrBaseStruct = new BaseStruct();
				*ptrBaseStruct = *baseIter;
				ptrBaseStruct->inDegree = 0;
				AddBaseStructInfoToOthers(ptrBaseStruct);
				m_PtrBaseStructs.push_back(ptrBaseStruct);
			} else {
				//�޸�ԭ���Ѿ��е�
				baseStructIter = FindPos(baseIter->sortBaseID);
				if (baseStructIter == m_PtrBaseStructs.end())
				{
					//�������������� ���ǳ�����
					continue;
				} else if (!((*baseStructIter)->rect == baseIter->rect))
				{
					RemoveBaseStructInfoFromOthers(*baseStructIter);
					**baseStructIter = *baseIter;
					(*baseStructIter)->inDegree = 0;
					(*baseStructIter)->myBacks.clear();
					(*baseStructIter)->myFronts.clear();
					AddBaseStructInfoToOthers(*baseStructIter);
				} else {
					(*baseStructIter)->data = baseIter->data;
				}
			}
		}

		m_SortBaseIDs = m_NewSortBaseIDs;

		//����
		bool result=true;
		if (sort){
			result = RectTopologySort();
		}

		//����
		baseStructIter=m_PtrBaseStructs.begin();
		for (baseIter=topologyBases.begin();baseIter!=baseIterEnd;++baseIter,++baseStructIter)
		{
			baseIter->data = (*baseStructIter)->data;
			baseIter->rect = (*baseStructIter)->rect;
			baseIter->sortBaseID = (*baseStructIter)->sortBaseID;
		}
		return result;
	}

	RectTopologyList::PtrBaseStructList::iterator RectTopologyList::FindPos(SortBaseId sortBaseID)
	{
		//�Ժ������map
		PtrBaseStructList::iterator iter, iterEnd;
		iterEnd = m_PtrBaseStructs.end();
		for(iter=m_PtrBaseStructs.begin();iter!=iterEnd;++iter)
		{
			if ((*iter)->sortBaseID == sortBaseID)
			{
				break;
			}
		}
		return iter;
	}
	
	
	/*//�Ż��˸ú���
	bool RectTopologyList::RectTopologySort()
	{
		PtrBaseStructSet::iterator frontsIter,fEnd;
		std::list<PtrBaseStructList::iterator> minIter;
		PtrBaseStructList::iterator iter, pos,endIter;		
		size_t minDegree;
		bool result = true;

		//�������
		for (PtrBaseStructList::iterator iter=m_PtrBaseStructs.begin(),ie=m_PtrBaseStructs.end();iter!=ie;++iter)
		{
			(*iter)->dynamicInDegree = (*iter)->inDegree;
		}
	
		//�ҵ�ǰ�����С�Ľڵ㼯�ϲ����뵽�����б���
		pos = m_PtrBaseStructs.begin();
		while (pos != m_PtrBaseStructs.end())
		{
			//Ѱ����С��ȺͶ�Ӧ�Ľڵ㼯
			endIter = m_PtrBaseStructs.end();
			minDegree = m_PtrBaseStructs.size()+1;			

			for(iter = pos;iter!=endIter;++iter)
			{
				if(minDegree>(*iter)->dynamicInDegree){
					minDegree = (*iter)->dynamicInDegree;
					minIter.clear();					
					minIter.push_back(iter);
				}else if(minDegree==(*iter)->dynamicInDegree){
					minIter.push_back(iter);
				}
			}
			//�����ؽ��
			if (minDegree>0)//˵�����ڻ�����һ�������С��
			{
				result = false;  //XXX ��һ���Ĵ���
			}
			
			//���������С�Ľڵ���
			for(std::list<PtrBaseStructList::iterator>::iterator ir=minIter.begin(),ie=minIter.end();ir!=ie;ir++){
				iter=*ir;
				for (frontsIter = (*iter)->myFronts.begin(),fEnd=(*iter)->myFronts.end();frontsIter!=fEnd;++frontsIter)
				{
					((*frontsIter)->dynamicInDegree)--;
				}

				if (iter != pos)
				{
					m_PtrBaseStructs.insert(pos, *iter);
					m_PtrBaseStructs.erase(iter);
				} else {
					pos++;
				}
			}
		}

		if (!result)
		{
			return false;
		}
		return true;
	}*/	
}