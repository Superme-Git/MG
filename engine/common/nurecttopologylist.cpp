#include "nurecttopologylist.h"


namespace Nuclear
{
	Rect2PologyList::Rect2PologyList(void)
	{
	}

	Rect2PologyList::~Rect2PologyList(void)
	{
		PtrBaseStructList::iterator iter = m_PtrBaseStructs.begin();
		for (; iter != m_PtrBaseStructs.end(); ++iter)
		{
			delete(*iter);
		}
		m_PtrBaseStructs.clear();
	}

	bool Rect2PologyList::Rect2PologySort()
	{
		PtrBaseStructList::iterator iter, pos, minIter;
		PtrBaseStructSet::iterator frontsIter;
		size_t minDegree;
		bool result = true;
		for (iter=m_PtrBaseStructs.begin();iter!=m_PtrBaseStructs.end();++iter)
		{
			(*iter)->dynamicInDegree = (*iter)->myBacks.size();
		}
		pos = m_PtrBaseStructs.begin();
		while (pos != m_PtrBaseStructs.end())
		{
			minDegree = m_PtrBaseStructs.size();
			minIter = m_PtrBaseStructs.end();
			for (iter = pos; iter != m_PtrBaseStructs.end(); ++iter)
			{
				if ((*iter)->dynamicInDegree == 0)
					break;
				if (minDegree > (*iter)->dynamicInDegree)
				{
					minDegree = (*iter)->dynamicInDegree;
					minIter = iter;
				}
			}
			if (iter == m_PtrBaseStructs.end())//˵�����ڻ�����һ�������С��
			{
				result = false;
				if (minIter == m_PtrBaseStructs.end())
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
			}
			else
			{
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
	void Rect2PologyList::RemoveBaseStructInfoFromOthers(PtrBaseStruct ptrBaseStruct)
	{
		PtrBaseStructSet::iterator iter,iterEnd = ptrBaseStruct->myFronts.end();
		for (iter=ptrBaseStruct->myFronts.begin();iter!=iterEnd;++iter)
		{
			(*iter)->myBacks.erase(ptrBaseStruct);
		}
		iterEnd = ptrBaseStruct->myBacks.end();
		for (iter=ptrBaseStruct->myBacks.begin();iter!=iterEnd;++iter)
		{
			(*iter)->myFronts.erase(ptrBaseStruct);
		}
	}
	
	 //ʹ��Set����list�޸��˸ú���
	void Rect2PologyList::AddBaseStructInfoToOthers(PtrBaseStruct ptrBaseStruct)
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
			}else if (ptrBaseStruct->rect.IsBehind((*baseStructIter)->rect))
			{
				ptrBaseStruct->myFronts.insert(*baseStructIter);
				(*baseStructIter)->myBacks.insert(ptrBaseStruct);
			}
		}
	}

	//�Ż�Dump����������
	bool Rect2PologyList::Dump(TopologyBaseVector& topologyBases, bool sort)
	{				
		//�����µ�BaseID�ļ���
		SortBaseIDSet sortBaseIDs;	

		TopologyBaseVector::iterator baseIter = topologyBases.begin();
		for (; baseIter != topologyBases.end(); ++baseIter)
		{
			sortBaseIDs.insert(baseIter->sortBaseID);
		}

		//ɾ����ID���������Ѿ�û���˵Ķ���
		PtrBaseStructList::iterator baseStructIter = m_PtrBaseStructs.begin();
		for (; baseStructIter != m_PtrBaseStructs.end();)
		{
			if (sortBaseIDs.find((*baseStructIter)->sortBaseID) == sortBaseIDs.end())
			{
				RemoveBaseStructInfoFromOthers(*baseStructIter);
				delete(*baseStructIter);
				m_PtrBaseStructs.erase(baseStructIter++);
			}
			else
			{
				++baseStructIter;
			}
		}

		//���롢�޸�
		std::vector<PtrBaseStruct> addVect;
		std::vector<PtrBaseStruct> modifyVect;
		addVect.reserve(128);
		modifyVect.reserve(128);

		BaseStruct* ptrBaseStruct = NULL;
		for (baseIter = topologyBases.begin(); baseIter != topologyBases.end(); ++baseIter)
		{
			if (m_SortBaseIDs.find(baseIter->sortBaseID) == m_SortBaseIDs.end())
			{
				//����ԭ��û�е�
				ptrBaseStruct = new BaseStruct();
				*ptrBaseStruct = *baseIter;
				addVect.push_back(ptrBaseStruct);
			}
			else
			{
				//�޸�ԭ���Ѿ��е�
				baseStructIter = FindPos(baseIter->sortBaseID);
				if (baseStructIter == m_PtrBaseStructs.end())
				{
					//�������������� ���ǳ�����
					continue;
				}
				else if (!((*baseStructIter)->rect == baseIter->rect))
				{
					RemoveBaseStructInfoFromOthers(*baseStructIter);
					**baseStructIter = *baseIter;
					(*baseStructIter)->myBacks.clear();
					(*baseStructIter)->myFronts.clear();
					modifyVect.push_back(*baseStructIter);
				}
				else
				{
					(*baseStructIter)->data = baseIter->data;
				}
			}
		}

		std::vector<PtrBaseStruct>::iterator vIt, vIe;
		for (vIt = modifyVect.begin(), vIe = modifyVect.end(); vIt != vIe; ++vIt)
		{
			AddBaseStructInfoToOthers(*vIt);
		}
		for (vIt = addVect.begin(), vIe = addVect.end(); vIt != vIe; ++vIt)
		{
			AddBaseStructInfoToOthers(*vIt);
			m_PtrBaseStructs.push_back(*vIt);
		}

		m_SortBaseIDs = sortBaseIDs;

		//����
		bool result = true;
		if (sort)
		{
			result = Rect2PologySort();
		}

		//����
		baseStructIter = m_PtrBaseStructs.begin();
		for (baseIter = topologyBases.begin(); baseIter != topologyBases.end(); ++baseIter, ++baseStructIter)
		{
			baseIter->data = (*baseStructIter)->data;
			baseIter->rect = (*baseStructIter)->rect;
			baseIter->sortBaseID = (*baseStructIter)->sortBaseID;
		}
		return result;
	}

	Rect2PologyList::PtrBaseStructList::iterator Rect2PologyList::FindPos(SortBaseID sortBaseID)
	{
		PtrBaseStructList::iterator iter, iterEnd;
		iterEnd = m_PtrBaseStructs.end();
		for (iter = m_PtrBaseStructs.begin(); iter != iterEnd; ++iter)
		{
			if ((*iter)->sortBaseID == sortBaseID)
			{
				break;
			}
		}
		return iter;
	}
}