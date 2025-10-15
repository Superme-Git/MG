#pragma once

#include "..\engine\world\lkotreenodelist.h"

class LkoTreeNodeListEX : public Nuclear::LkoTreeNodeList
{
public:
	LkoTreeNodeListEX(void);
	~LkoTreeNodeListEX(void);
	
	//�޸Ľڵ�..//DTList m_Lists;
	public:
		Nuclear::DTList& GetLinkedObjectList() { return m_Lists; }

		//�ڵ��������е�λ�� index 
		Nuclear::LkoTreeNode* GetLinkedObjectNode(int index);
		void DelLinkedObjectNode(int index);
		void InsertLinkedObjectNode(Nuclear::LkoTreeNode* pNode, int index);

		void RemoveAllNode();

		void SetLKOEnableZoomFlag(bool bflag) { m_bLKOEnableZoom = bflag; }
		void SetLKOZoom(float data) { m_fLKOzoom = data; }
		void SetLKOTime(float data) { m_fLKOTime = data; }
};
