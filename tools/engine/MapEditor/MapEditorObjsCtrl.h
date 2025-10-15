
#pragma once
#include "..\engine\map\pmap.h"
#include "..\engine\common\recttopologylist.h"
#include "MapEditorDoc.h"
#include "Action.h"
#include "ActionList.h"

class CMapEditorView;

class CMapEditorObjsCtrl
{
private:
	CMapEditorView *m_pView;
	CMapEditorDoc *m_pDoc;

public:
	void Init(CMapEditorDoc *pDoc, CMapEditorView* pView)
	{
		m_pDoc = pDoc;
		m_pView = pView;
	}
	~CMapEditorObjsCtrl(void);
	CMapEditorObjsCtrl();

private:
	bool GetDistort_BaseAndRect(const std::wstring& name, const CPoint& pos, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect);
	bool GetLinkedObject_BaseAndRect(const std::wstring& name, const CPoint& pos, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect);
	bool GetEffect_BaseAndRect(const std::wstring& name, const CPoint& pos, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect);
	bool GetImg_BaseAndRect(const std::wstring& name, const CPoint& pos, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect);

public:
	bool DeleteObjects(const Nuclear::SortBaseIDSet& setSortBaseIDs, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList);
	bool MoveObjects(long addX, long addY, const Nuclear::SortBaseIDSet& setSortBaseIDs, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList);
	bool AddObject(PMap::MAP_OBJ_TYPE objType, const std::wstring& name, const CPoint& point, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList);
};
