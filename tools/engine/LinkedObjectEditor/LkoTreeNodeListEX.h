#pragma once

#include "..\engine\world\lkotreenodelist.h"

class LkoTreeNodeListEX : public Nuclear::LkoTreeNodeList
{
public:
	LkoTreeNodeListEX(void);
	~LkoTreeNodeListEX(void);
	
	//修改节点..//DTList m_Lists;
	public:
		Nuclear::DTList& GetLinkedObjectList() { return m_Lists; }

		//节点在数组中的位置 index 
		Nuclear::LkoTreeNode* GetLinkedObjectNode(int index);
		void DelLinkedObjectNode(int index);
		void InsertLinkedObjectNode(Nuclear::LkoTreeNode* pNode, int index);

		void RemoveAllNode();

		void SetLKOEnableZoomFlag(bool bflag) { m_bLKOEnableZoom = bflag; }
		void SetLKOZoom(float data) { m_fLKOzoom = data; }
		void SetLKOTime(float data) { m_fLKOTime = data; }
};
