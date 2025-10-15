#include "stdafx.h"
#include <windows.h>
#include "LkoTreeNodeListEX.h"

LkoTreeNodeListEX::LkoTreeNodeListEX(void)
{
}

LkoTreeNodeListEX::~LkoTreeNodeListEX(void)
{
}

void LkoTreeNodeListEX::RemoveAllNode()
{
	Nuclear::DTList &LinkedObjectList = GetLinkedObjectList();

	Nuclear::DTList::iterator iter = LinkedObjectList.begin();
	for (; iter!=LinkedObjectList.end(); ++iter)
	{
		delete *iter;
	}
	LinkedObjectList.clear();
}

Nuclear::LkoTreeNode* LkoTreeNodeListEX::GetLinkedObjectNode(int index)
{
	return *(GetLinkedObjectList().begin() +index);
}

void LkoTreeNodeListEX::DelLinkedObjectNode(int index)
{
	Nuclear::DTList &dtlist = GetLinkedObjectList();
	Nuclear::LkoTreeNode* pNode = dtlist[index];//*(GetLinkedObjectList().begin() +index);
	delete pNode;
	GetLinkedObjectList().erase( GetLinkedObjectList().begin() +index );

	//不应许删除主节点..--3-21---
	if(index == 0)//如果删除的是主节点
	{
		OutputDebugString(L"逻辑错误..删除主节点？？LkoTreeNodeListEX::DelLinkedObjectNode()\n");

		dtlist[0]->pParent = NULL;
		dtlist[0]->pRoot = dtlist[0];
		int cnt = (int)dtlist.size();
		for(int i=1; i<cnt; i++)
		{
			dtlist[i]->pParent = dtlist[0];
			dtlist[i]->pRoot = dtlist[0];
		}
	}
}

void LkoTreeNodeListEX::InsertLinkedObjectNode(Nuclear::LkoTreeNode* pNode, int index)
{
	GetLinkedObjectList().insert(GetLinkedObjectList().begin() +index, pNode);
}