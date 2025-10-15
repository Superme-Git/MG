#pragma once

#include <list>
#include "Operator.h"

class CMapEditorView;

class CActionList
{
private:
	CMapEditorView* m_pMapEditorView;
	std::list<CAction*>::iterator m_Iter;
	std::list<CAction*> m_ActionList;

public:
	static int const MAX_LENGTH = 50;

private:
	bool ToDoOperator(int type);
	bool SetBackgroundOrder(int type);
	bool SetBackgroundData(int type);
	bool SetBackgroundInfo(int type);
	bool SetMaskParam(int type);
	bool SetTimeEffect(int type);
	bool SetWaterAreaOperator(int type);
	bool SetTriggerObjsOperator(int type);
	bool SetTransObjsOperator(int type);
	bool SetSingleObjName(int type);
	bool SetLkoBase(int type);
	bool SetElementBase(int type);
	bool SetObjNames(int type);
	bool SetNewDeleteLayersInfo(int type);
	bool SetAllGroundLayersInfo(int type);
	bool SetSingleGroundLayerInfo(int type);
	bool SetObjs(int type);
	bool SetWater(int type);
	bool SetBiggerTile(int type);
	bool SetTileColor(int type);
	bool SetSuperTile(int type);
	bool SetTile(int type);
	bool SetChangeCanvas(int type);

public:
	bool CanRedo()
	{
		if (m_Iter == m_ActionList.end())
			return false;
		else
			return true;
	}
	bool CanUndo()
	{
		if (m_Iter == m_ActionList.begin())
			return false;
		else
			return true;
	}
	bool RedoOneAction();
	bool UndoOneAction();
	void DoOneAction(CAction* oper);
	~CActionList(void);
	CActionList(CMapEditorView* pMapEditorView);
};

typedef CActionList *LPTROPERATORLIST;