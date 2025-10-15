#include "StdAfx.h"
#include "MapEditorObjsCtrl.h"
#include "MapEditor.h"
#include "MapEditorView.h"


bool CMapEditorObjsCtrl::MoveObjects(long addX, long addY, const Nuclear::SortBaseIDSet& setSortBaseIDs, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList)
{
	bool result = false;
	int selectedPid = -1;
	PMap &pmap = m_pDoc->GetToolsMap();
	if (pNowOper)
		delete (pNowOper);
	int nLayerEditID = m_pView->GetLayerEditId();
	pNowOper = new CAction(CAction::AT_OBJECTS, nLayerEditID);
	CAction::CObjectsActionAtom* oper = pNowOper->GetObjectsActionData();

	Nuclear::SortBaseIDSet::const_iterator iter = setSortBaseIDs.begin();
	nLayerEditID -= CMapEditorView::FIRST_ELEMENT_LAYER;
	Nuclear::SubMap::ObjInfoVector toSetObjs;	
	PMap::MAP_OBJ_TYPE objType;
	CLayerView::LAYER_OBJ_TYPE layerObjType;
	Nuclear::SubMap::ObjectId objID;
	Nuclear::SubMap::sObjInfo info;
	for(;iter!=setSortBaseIDs.end();iter++)
	{
		layerObjType = CLayerView::GetObjectType(*iter);
		objID = CLayerView::GetObjectId(*iter);
		if (!CLayerView::ConvertObjectType(layerObjType, objType))
			continue;
		info = pmap.GetObjInfo(objType,	nLayerEditID, objID);
		info.m_ObjectId = CLayerView::GetSortBaseId(info.m_ObjectId, layerObjType);


		oper->m_AtomObjInfo[CAction::CActionAtom::BEFORE_OPERATOR].push_back(info);

		info.m_Rect.left += addX;
		info.m_Rect.right += addX;
		info.m_Rect.top += addY;
		info.m_Rect.bottom += addY;
		info.m_Position.x += addX;
		info.m_Position.y += addY;
		toSetObjs.push_back(info);
		pmap.GetObjInfo(objType, nLayerEditID, objID).m_bDeleted = true;
		if (objType == PMap::MAP_OBJ_LINKEDOBJ)
		{
			std::pair<char, unsigned int> tmpKey;
			tmpKey.first = nLayerEditID;
			tmpKey.second = objID;
			m_pView->m_TreeMap.erase(tmpKey);
		} else if (objType == PMap::MAP_OBJ_DISTORT)
		{
			std::pair<char, unsigned int> tmpKey;
			tmpKey.first = nLayerEditID;
			tmpKey.second = objID;
			std::map<std::pair<char, unsigned int>, Nuclear::DistortBase*>::iterator iter = m_pView->m_DistortMap.find(tmpKey);
			if (iter != m_pView->m_DistortMap.end())
			{
				delete (iter->second);
				m_pView->m_DistortMap.erase(iter);
			}			
		}
	}
	m_pView->m_ScreenElements.RemoveObjects(nLayerEditID, setSortBaseIDs);


	int todo;
	result = true;

	if (result)
	{
		Nuclear::SubMap::ObjInfoVector::const_iterator objInfoIter = toSetObjs.begin();
		Nuclear::SortBaseRectType baseRect;
		Nuclear::CRECT rect;

		for (;objInfoIter!=toSetObjs.end();objInfoIter++)
		{

			layerObjType = CLayerView::GetObjectType(objInfoIter->m_ObjectId);
			objID = CLayerView::GetObjectId(objInfoIter->m_ObjectId);
			if (!CLayerView::ConvertObjectType(layerObjType, objType))
			{
				result = false;
				break;
			}

			if (!m_pView->m_ScreenElements.GetBaseRect(objType, &(*objInfoIter), baseRect))
			{
				result = false;
				break;
			}

			if (!m_pView->m_ScreenElements.CheckBaseRect(nLayerEditID, baseRect, objInfoIter->m_Rect))
			{
				result = false;
				break;
			}
		}

	}
	if (result)
	{
		for(iter=setSortBaseIDs.begin();iter!=setSortBaseIDs.end();++iter)
		{
			layerObjType = CLayerView::GetObjectType(*iter);
			objID = CLayerView::GetObjectId(*iter);
			if (!CLayerView::ConvertObjectType(layerObjType, objType))
				continue;
			Nuclear::SubMap::sObjInfo &objInfo = pmap.GetObjInfo(objType, nLayerEditID, objID);
			objInfo.m_Rect.left += addX;
			objInfo.m_Rect.right += addX;
			objInfo.m_Rect.top += addY;
			objInfo.m_Rect.bottom += addY;
			objInfo.m_Position.x += addX;
			objInfo.m_Position.y += addY;
			objInfo.m_bDeleted = false;
			objInfo.m_ObjectId = CLayerView::GetSortBaseId(objInfo.m_ObjectId, layerObjType);
			oper->m_AtomObjInfo[CAction::CActionAtom::AFTER_OPERATOR].push_back(objInfo);
			objInfo.m_ObjectId = CLayerView::GetObjectId(objInfo.m_ObjectId);
		}
		pOperatorList->DoOneAction(pNowOper);
		pNowOper = NULL;
		m_pDoc->SetModifiedFlag();
		return true;
	}
	for(iter=setSortBaseIDs.begin();iter!=setSortBaseIDs.end();++iter)
	{
		CLayerView::GetObjectType(*iter, objType);
		pmap.GetObjInfo(objType, nLayerEditID, CLayerView::GetObjectId(*iter)).m_bDeleted = false;
	}

	delete(pNowOper);
	pNowOper = NULL;
	return false;
}

bool CMapEditorObjsCtrl::AddObject(PMap::MAP_OBJ_TYPE objType, const std::wstring& name, const CPoint& point, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList)
{
	int nLayerEditID = m_pView->GetLayerEditId();
	POINT pos = point + m_pView->m_DrawScrollPosition;
	Nuclear::SortBaseRectType baseRect;
	Nuclear::CRECT rect;

	Nuclear::SubMap::ObjMapKey mapKey = m_pDoc->GetObjectMapKey(objType, name, pOperatorList);

	static bool (CMapEditorObjsCtrl::*pGetBaseAndRect[Nuclear::PMap::MAP_OBJ_COUNT])(const std::wstring& name, const CPoint& point, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect) =
	{
		&CMapEditorObjsCtrl::GetImg_BaseAndRect, &CMapEditorObjsCtrl::GetEffect_BaseAndRect, &CMapEditorObjsCtrl::GetLinkedObject_BaseAndRect, &CMapEditorObjsCtrl::GetDistort_BaseAndRect
	};

	if (!((this->*pGetBaseAndRect[objType])(name, pos, baseRect, rect)))
		return false;

	if (!m_pView->m_ScreenElements.CheckBaseRect(nLayerEditID - CMapEditorView::FIRST_ELEMENT_LAYER, baseRect, rect))
		return false;

	if (pNowOper)
		delete (pNowOper);
	pNowOper = new CAction(CAction::AT_OBJECTS, nLayerEditID);
	CAction::CObjectsActionAtom* oper = pNowOper->GetObjectsActionData();
	Nuclear::SubMap::ObjectId objID = m_pDoc->AddObject(objType, nLayerEditID - CMapEditorView::FIRST_ELEMENT_LAYER, mapKey
		, rect, pos, m_pView->GetAttachColor());
	m_pView->m_ScreenElements.SetMustRenew();
	Nuclear::SubMap::sObjInfo info = m_pDoc->GetToolsMap().GetObjInfo(objType, nLayerEditID - CMapEditorView::FIRST_ELEMENT_LAYER, objID);
	info.m_ObjectId = CLayerView::GetSortBaseId(info.m_ObjectId, CLayerView::ConvertObjectType(objType));
	oper->m_AtomObjInfo[CAction::CActionAtom::AFTER_OPERATOR].push_back(info);
	pOperatorList->DoOneAction(pNowOper);
	m_pView->GetObjectListDlg().TryAddObject(info.m_ObjectId);
	m_pDoc->SetModifiedFlag();
	m_pView->m_SmallMapDlg.TryInvalidate();
	pNowOper = NULL;
	return true;
}

bool CMapEditorObjsCtrl::GetDistort_BaseAndRect(const std::wstring& name, const CPoint& pos, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect)
{
	Nuclear::PDistortionObject* pDistortInfo = m_pView->GetDistortInfo(name);
	if (pDistortInfo == NULL)
		return false;

	Nuclear::SubMap::sObjInfo info;
	if (!m_pDoc->CheckObjectMapKey(PMap::MAP_OBJ_DISTORT, name, info.m_MapKey))
		return false;
	info.m_Rect.left = pos.x;
	info.m_Rect.top = pos.y;
	if (!m_pView->m_ScreenElements.GetBaseRect(PMap::MAP_OBJ_DISTORT, &info, baseRect))
		return false;

	std::wstring imgFilename = pDistortInfo->m_distortionObj.strImgFileName;
	imgFilename.erase(imgFilename.size()-4, 4);
	rect.left = pos.x;
	rect.top = pos.y;
	LPImgInfo pImgInfo = m_pView->GetImgInfo(imgFilename);
	if (pImgInfo == NULL)
		return false;
	rect.right = rect.left + pImgInfo->m_nFileWidth;
	rect.bottom = rect.top + pImgInfo->m_nFileHeight;
	return true;
}

bool CMapEditorObjsCtrl::GetLinkedObject_BaseAndRect(const std::wstring& name, const CPoint& pos, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect)
{
	Nuclear::PLinkedObject* pLinkedObjInfo = m_pView->GetLinkedObjectInfo(name);
	if (pLinkedObjInfo == NULL)
		return false;

	Nuclear::SubMap::sObjInfo info;
	if (!m_pDoc->CheckObjectMapKey(PMap::MAP_OBJ_LINKEDOBJ, name, info.m_MapKey))
		return false;
	info.m_Position = pos;

	if (!m_pView->m_ScreenElements.GetBaseRect(PMap::MAP_OBJ_LINKEDOBJ, &info, baseRect))
		return false;

	rect.left = pos.x + pLinkedObjInfo->m_rctMaxBox.left - CMapEditorView::DYNAMIC_MAX_RECT_WIDEN;
	rect.top = pos.y + pLinkedObjInfo->m_rctMaxBox.top - CMapEditorView::DYNAMIC_MAX_RECT_WIDEN;
	rect.right = pos.x + pLinkedObjInfo->m_rctMaxBox.right + CMapEditorView::DYNAMIC_MAX_RECT_WIDEN;
	rect.bottom = pos.y + pLinkedObjInfo->m_rctMaxBox.bottom + CMapEditorView::DYNAMIC_MAX_RECT_WIDEN;
	return true;
}

bool CMapEditorObjsCtrl::GetEffect_BaseAndRect(const std::wstring& name, const CPoint& pos, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect)
{
	Nuclear::SubMap::sObjInfo info;
	if (!m_pDoc->CheckObjectMapKey(PMap::MAP_OBJ_EFFECT, name, info.m_MapKey))
		return false;
	info.m_Position = pos;
	m_pView->m_ScreenElements.GetBaseRect(PMap::MAP_OBJ_EFFECT, &info, baseRect);

	if (!m_pView->GetEffectRect(name, rect))
		return false;
	rect += pos;
	return true;
}

bool CMapEditorObjsCtrl::GetImg_BaseAndRect(const std::wstring& name, const CPoint& pos, Nuclear::SortBaseRectType &baseRect, Nuclear::CRECT &rect)
{

	Nuclear::SubMap::sObjInfo info;
	if (!m_pDoc->CheckObjectMapKey(PMap::MAP_OBJ_ELEMENT, name, info.m_MapKey))
		return false;
	info.m_Rect.left = pos.x;
	info.m_Rect.top = pos.y;
	if (!m_pView->m_ScreenElements.GetBaseRect(PMap::MAP_OBJ_ELEMENT, &info, baseRect))
		return false;

	rect.left = pos.x;
	rect.top = pos.y;
	LPImgInfo pImgInfo = m_pView->GetImgInfo(name);
	if (pImgInfo == NULL)
		return false;
	rect.right = rect.left + pImgInfo->m_nFileWidth;
	rect.bottom = rect.top + pImgInfo->m_nFileHeight;
	return true;
}

bool CMapEditorObjsCtrl::DeleteObjects(const Nuclear::SortBaseIDSet& setSortBaseIDs, LPTRACTION& pNowOper, LPTRACTIONLIST pOperatorList)
{
	Nuclear::SortBaseIDSet::const_iterator iter = setSortBaseIDs.begin();
	bool result = false;
	if (pNowOper)
		delete (pNowOper);
	int nLayerEditID = m_pView->GetLayerEditId();
	pNowOper = new CAction(CAction::AT_OBJECTS, nLayerEditID);
	nLayerEditID -= CMapEditorView::FIRST_ELEMENT_LAYER;
	CAction::CObjectsActionAtom* oper = pNowOper->GetObjectsActionData();
	Nuclear::SubMap::sObjInfo info;
	PMap &pmap = m_pDoc->GetToolsMap();
	PMap::MAP_OBJ_TYPE objType;
	CLayerView::LAYER_OBJ_TYPE layerObjType;
	Nuclear::SubMap::ObjectId objID;
	for(;iter!=setSortBaseIDs.end();iter++)
	{
		layerObjType = CLayerView::GetObjectType(*iter);
		objID = CLayerView::GetObjectId(*iter);
		if (!CLayerView::ConvertObjectType(layerObjType, objType))
			continue;
		info = pmap.GetObjInfo(objType,	nLayerEditID, objID);
		info.m_ObjectId = *iter;
		oper->m_AtomObjInfo[CAction::CActionAtom::BEFORE_OPERATOR].push_back(info);
		if (m_pDoc->GetToolsMap().DeleteObj(objType, nLayerEditID, objID))
		{
			info = pmap.GetObjInfo(objType,	nLayerEditID, objID);
			info.m_ObjectId = *iter;
			oper->m_AtomObjInfo[CAction::CActionAtom::AFTER_OPERATOR].push_back(info);
			m_pView->GetObjectListDlg().TryRemoveObject(info.m_ObjectId);
			if (objType == PMap::MAP_OBJ_LINKEDOBJ)
			{
				std::pair<char, unsigned int> tmpKey;
				tmpKey.first = nLayerEditID;
				tmpKey.second = *iter;
				m_pView->m_TreeMap.erase(tmpKey);
			}
			result = true;
		} else {
			oper->m_AtomObjInfo[CAction::CActionAtom::BEFORE_OPERATOR].pop_back();
		}
	}
	if (result)
	{
		pOperatorList->DoOneAction(pNowOper);
		m_pDoc->SetModifiedFlag();
	} else {
		delete (pNowOper); 
	}
	pNowOper = NULL;
	return true;
}

CMapEditorObjsCtrl::~CMapEditorObjsCtrl(void)
{
}

CMapEditorObjsCtrl::CMapEditorObjsCtrl()
{
}

