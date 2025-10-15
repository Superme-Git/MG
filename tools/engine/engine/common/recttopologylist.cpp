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
			if (iter == endIter)//说明存在环，找一个入度最小的
			{
				result = false;
				if (minIter == endIter)
					break;
				iter = minIter;
			}
			for (frontsIter = (*iter)->myFronts.begin();frontsIter!=(*iter)->myFronts.end();++frontsIter)//所有能挡住*iter的动态入度都减去1
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

	//使用Set代替list优化了该函数
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
	
	 //使用Set代替list修改了该函数
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

	//优化Dump函数的性能
	bool RectTopologyList::Dump(TopologyBaseVector& topologyBases, bool sort)
	{				
		//建立新的BaseID的集合
		m_NewSortBaseIDs.clear();		

		TopologyBaseVector::iterator baseIter=topologyBases.begin();
		TopologyBaseVector::iterator baseIterEnd = topologyBases.end();	
		for (;baseIter!=baseIterEnd;++baseIter){
			m_NewSortBaseIDs.insert(baseIter->sortBaseID);
		}

		//删除新ID集合里面已经没有了的东西
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

		//插入、修改
		PtrBaseStruct ptrBaseStruct = NULL;
		SortBaseIDSet::iterator sortEnd=m_SortBaseIDs.end();
		for (baseIter=topologyBases.begin();baseIter!=baseIterEnd;++baseIter)
		{
			if (m_SortBaseIDs.find(baseIter->sortBaseID) == sortEnd)
			{
				//插入原来没有的
				ptrBaseStruct = new BaseStruct();
				*ptrBaseStruct = *baseIter;
				ptrBaseStruct->inDegree = 0;
				AddBaseStructInfoToOthers(ptrBaseStruct);
				m_PtrBaseStructs.push_back(ptrBaseStruct);
			} else {
				//修改原来已经有的
				baseStructIter = FindPos(baseIter->sortBaseID);
				if (baseStructIter == m_PtrBaseStructs.end())
				{
					//不会出现这种情况 除非出错了
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

		//排序
		bool result=true;
		if (sort){
			result = RectTopologySort();
		}

		//拷贝
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
		//以后或者用map
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
	
	
	/*//优化了该函数
	bool RectTopologyList::RectTopologySort()
	{
		PtrBaseStructSet::iterator frontsIter,fEnd;
		std::list<PtrBaseStructList::iterator> minIter;
		PtrBaseStructList::iterator iter, pos,endIter;		
		size_t minDegree;
		bool result = true;

		//复制入度
		for (PtrBaseStructList::iterator iter=m_PtrBaseStructs.begin(),ie=m_PtrBaseStructs.end();iter!=ie;++iter)
		{
			(*iter)->dynamicInDegree = (*iter)->inDegree;
		}
	
		//找当前入度最小的节点集合并加入到处理列表中
		pos = m_PtrBaseStructs.begin();
		while (pos != m_PtrBaseStructs.end())
		{
			//寻找最小入度和对应的节点集
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
			//处理返回结果
			if (minDegree>0)//说明存在环，找一个入度最小的
			{
				result = false;  //XXX 进一步的处理
			}
			
			//处理入度最小的节点们
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