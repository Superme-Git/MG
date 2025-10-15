#pragma once

#include <list>
#include "Action.h"

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
	bool ToDoAction(int type);
	bool SetBackgroundOrder(int type);
	bool SetBackgroundData(int type);
	bool SetBackgroundInfo(int type);
	bool SetMaskParam(int type);
	bool SetTimeEffect(int type);
	bool SetWaterAreaAction(int type);
	bool SetTriggerObjectsAction(int type);
	bool SetTransparentObjectsAction(int type);
	bool SetSingleObjectName(int type);
	bool SetLinkedObjectBase(int type);
	bool SetElementBase(int type);
	bool SetObjectNames(int type);
	bool SetNewDeleteLayersInfo(int type);
	bool SetAllGroundLayersInfo(int type);
	bool SetSingleGroundLayerInfo(int type);
	bool SetObjects(int type);
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

typedef CActionList *LPTRACTIONLIST;