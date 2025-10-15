#include "StdAfx.h"
#include "MapEditor.h"
#include "ActionList.h"
#include "MapEditorView.h"
#include "MapEditorDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


using Nuclear::SubMap::PtrTileTypeList;
using Nuclear::SubMap::PtrTileType;

bool CActionList::SetBackgroundOrder(int type)
{
	CAction::CBackgroundOrderActionAtom* oper = (*m_Iter)->GetBackgroundOrderActionData();
	CToolsMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	pmap.SwapBackgroundInfo(oper->m_InfoID[0], oper->m_InfoID[1]);
	m_pMapEditorView->m_BackgroundListDlg.RenewList();
	return true;
}

bool CActionList::SetBackgroundData(int type)
{
	CAction::CBackgroundDataActionAtom* oper = (*m_Iter)->GetBackgroundDataActionData();
	CToolsMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	pmap.m_backGround.m_backGroundInfos[oper->m_InfoID].pPics[oper->m_Position] = oper->oper[type];
	return true;
}

bool CActionList::SetBackgroundInfo(int type)
{
	CAction::CBackgroundInfoActionAtom* oper = (*m_Iter)->GetBackgroundInfoActionData();
	CToolsMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();

	if (oper->m_InfoID[type] >= (int)pmap.m_backGround.m_backGroundInfos.size())
	{
		XPASSERT(oper->m_InfoID[type] == pmap.m_backGround.m_backGroundInfos.size());
		pmap.AddBackgroundInfo(oper->oper[type], oper->oper[type].pPics);
	} else if (oper->m_InfoID[type] == -1) {
		pmap.DestoryLastBackgroundInfo();
	} else {
		if (pmap.m_backGround.m_backGroundInfos[oper->m_InfoID[type]].m_bDeleted != oper->oper[type].m_bDeleted)
		{
			if (oper->oper[type].m_bDeleted)
			{
				pmap.DelBackgroundInfo(oper->m_InfoID[type]);
			} else {
				pmap.ReAddBackgroundInfo(oper->m_InfoID[type]);
			}
		} else {
			pmap.m_backGround.m_backGroundInfos[oper->m_InfoID[type]] = oper->oper[type];
		}
	}
	m_pMapEditorView->m_BackgroundListDlg.RenewList();
	return true;
}

bool CActionList::SetMaskParam(int type)
{
	int otherType = (type == CAction::CActionAtom::AFTER_OPERATOR)?CAction::CActionAtom::BEFORE_OPERATOR:CAction::CActionAtom::AFTER_OPERATOR;
	CAction::CMaskParamActionAtom* oper = (*m_Iter)->GetMaskParamActionData();
	Nuclear::PMap &map = m_pMapEditorView->GetDocument()->GetToolsMap();
	Nuclear::XPMaskParam* pParam = &(oper->oper[type]);
	if (oper->oper[type].m_nTime == -1)
	{
		map.RemoveAMask(oper->oper[otherType].m_nTime);
	} else if (oper->oper[otherType].m_nTime == -1)
	{
		map.AddAMask(oper->oper[type].m_nTime, *pParam);
	} else {
		map.ModifyAMask(oper->oper[otherType].m_nTime, oper->oper[type].m_nTime, *pParam);
	}
	m_pMapEditorView->m_MaskEditorDlg.Reset();
	return true;
}

bool CActionList::SetTimeEffect(int type)
{
	int otherType = (type == CAction::CActionAtom::AFTER_OPERATOR)?CAction::CActionAtom::BEFORE_OPERATOR:CAction::CActionAtom::AFTER_OPERATOR;
	CAction::CTimeEffectActionAtom* oper = (*m_Iter)->GetTimeEffectActionData();
	Nuclear::PMap &map = m_pMapEditorView->GetDocument()->GetToolsMap();
	Nuclear::XPColorBalanceParam* pParam = &(oper->oper[type]);
	if (oper->oper[type].m_nTime == -1)
	{
		map.RemoveAnTimeEffect(oper->oper[otherType].m_nTime);
	} else if (oper->oper[otherType].m_nTime == -1)
	{
		map.AddAnTimeEffect(oper->oper[type].m_nTime, *pParam);
	} else {
		map.ModifyAnTimeEffect(oper->oper[otherType].m_nTime, oper->oper[type].m_nTime, *pParam);
	}
	m_pMapEditorView->m_TimeEffectEditorDlg.Reset();
	return true;
}

bool CActionList::SetWaterAreaAction(int type)
{
	CAction::CWaterAreaActionAtom* oper = (*m_Iter)->GetWaterAreaActionData();
	m_pMapEditorView->GetDocument()->GetToolsMap().m_Water.SetWaterAreaInfos(oper->oper[type]);
	m_pMapEditorView->ClearAllSelection();
	m_pMapEditorView->m_SelectWaterDlg.RenewList();
	return true;
}

bool CActionList::SetTriggerObjectsAction(int type)
{
	CAction::CTriggerObjectActionAtom* oper = (*m_Iter)->GetTriggerObjectActionData();
	m_pMapEditorView->GetDocument()->GetToolsMap().m_triggerObjs.SetTriggerInfos(oper->oper[type]);
	m_pMapEditorView->ClearAllSelection();
	m_pMapEditorView->m_TriggerListDlg.ReNewTree();
	return true;
}

bool CActionList::SetTransparentObjectsAction(int type)
{
	CAction::CTransparentObjectActionAtom* oper = (*m_Iter)->GetTransparentObjectsActionData();
	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	Nuclear::PMap &map = pDoc->GetToolsMap();
	map.m_transObjs.SetTransObjInfoMap(oper->oper[type]);
	m_pMapEditorView->ClearAllSelection();
	m_pMapEditorView->m_TransparentObjectListDlg.ReNewTree();
	return true;
}

bool CActionList::SetSingleObjectName(int type)
{
	CAction::CSingleObjectNameActionAtom* oper = (*m_Iter)->GetSingleObjectNameActionData();
	int otherType = (type == CAction::CActionAtom::AFTER_OPERATOR)?CAction::CActionAtom::BEFORE_OPERATOR:CAction::CActionAtom::AFTER_OPERATOR;
	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	Nuclear::PMap &map = pDoc->GetToolsMap();
	std::wstring toSetName(oper->oper[type].m_Name);
	std::wstring toDeleteName(oper->oper[otherType].m_Name);
	if (oper->oper[otherType].m_Name != _T("deleted"))
	{
		pDoc->DeleteObjectKey(oper->m_MapObjectType, toDeleteName);
	}
	if (oper->oper[type].m_Name == _T("deleted"))
	{
		pDoc->DeleteObjectNameMap(oper->m_MapObjectType, oper->oper[otherType].m_ObjMapKey);
	} else {
		pDoc->SetObjectKey(oper->m_MapObjectType, toSetName, oper->oper[type].m_ObjMapKey);
		pDoc->SetObjectNameMap(oper->m_MapObjectType, oper->oper[type].m_ObjMapKey, toSetName);
	}
	if (oper->m_Pictures.size() > 0)
	{
		std::vector<std::pair<int, unsigned int>>::iterator iter = oper->m_Pictures.begin();
		Nuclear::SubMap::ObjLayerVector &layerVector = map.m_objs[oper->m_MapObjectType].m_vectorLayers;
		for(;iter!=oper->m_Pictures.end();iter++)
		{
			layerVector[iter->first].vectorObjs[iter->second].m_MapKey = oper->oper[type].m_ObjMapKey;
		}
	}
	if (m_pMapEditorView->m_ObjsListDlg.m_hWnd)
		m_pMapEditorView->m_ObjsListDlg.ResetLists();
	m_pMapEditorView->ClearScreenElementLayers();
	return true;
}

bool CActionList::SetLinkedObjectBase(int type)
{
	CAction::CLinkedObjectBaseActionAtom* oper = (*m_Iter)->GetLinkedObjectBaseActionData();
	Nuclear::PLinkedObject *pLinkObj = 
		m_pMapEditorView->m_LinkedObjectMap[m_pMapEditorView->GetDocument()->GetToolsMap().GetObjFileName(PMap::MAP_OBJ_LINKEDOBJ, oper->m_ObjectMapKey)];
	if (!pLinkObj)
		return false;
	int i;
	for (i=0;i<4;i++)
	{
		pLinkObj->m_ptBasePos[i] = oper->oper[i][type];
	}
	if (type == CAction::CActionAtom::BEFORE_OPERATOR)
	{
		m_pMapEditorView->m_ChangedLinkedObjects[oper->m_ObjectMapKey]--;
	} else {
		m_pMapEditorView->m_ChangedLinkedObjects[oper->m_ObjectMapKey]++;
	}
	m_pMapEditorView->m_ScreenElements.ClearLayers();
	return true;
}

bool CActionList::SetElementBase(int type)
{
	CAction::CElementBaseActionAtom* oper = (*m_Iter)->GetElementBaseActionData();
	LPImgInfo pImgInfo = 
		m_pMapEditorView->m_ImgInfoMap[m_pMapEditorView->GetDocument()->GetToolsMap().GetObjFileName(PMap::MAP_OBJ_ELEMENT, oper->m_ObjectMapKey)];
	if (!pImgInfo)
		return false;
	int i;
	for (i=0;i<4;i++)
	{
		pImgInfo->m_Positions[i] = oper->oper[i][type];
	}
	if (type == CAction::CActionAtom::BEFORE_OPERATOR)
	{
		m_pMapEditorView->m_ChangedImages[oper->m_ObjectMapKey]--;
	} else {
		m_pMapEditorView->m_ChangedImages[oper->m_ObjectMapKey]++;
	}
	m_pMapEditorView->m_ScreenElements.ClearLayers();
	return true;
}

bool CActionList::SetObjects(int type)
{
	int otherType = (type == CAction::CActionAtom::AFTER_OPERATOR)?CAction::CActionAtom::BEFORE_OPERATOR:CAction::CActionAtom::AFTER_OPERATOR;
	CAction::CObjectsActionAtom* oper = (*m_Iter)->GetObjectsActionData();
	int layerID = oper->m_LayerID-CMapEditorView::FIRST_ELEMENT_LAYER;
	PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	Nuclear::SubMap::ObjInfoVector::iterator iterInfo = oper->m_AtomObjInfo[type].begin();

	Nuclear::SortBaseIDSet setSortBaseIDs;
	std::pair<char, unsigned int> tmpKey;
	PMap::MAP_OBJ_TYPE objType;
	Nuclear::SubMap::ObjectId objID;
	Nuclear::SubMap::sObjInfo info;
	for(;iterInfo!=oper->m_AtomObjInfo[type].end();++iterInfo)
	{
		if (!CLayerView::ConvertObjectType(CLayerView::GetObjectType(iterInfo->m_ObjectId), objType))
			continue;
		objID = CLayerView::GetObjectId(iterInfo->m_ObjectId);
		if (objID >= pmap.m_objs[objType].GetObjSize(layerID))
		{
			XPASSERT(objID == pmap.m_objs[objType].GetObjSize(layerID));
			pmap.AddObj(objType, layerID, iterInfo->m_MapKey, iterInfo->m_Rect, iterInfo->m_Position, iterInfo->m_AttachColor.data);
			m_pMapEditorView->GetObjectListDlg().TryAddObject(iterInfo->m_ObjectId);
		} else {

			if (pmap.GetObjInfo(objType, layerID, objID).m_bDeleted != iterInfo->m_bDeleted)
			{
				if (iterInfo->m_bDeleted)
				{
					pmap.DeleteObj(objType, layerID, objID);
					m_pMapEditorView->GetObjectListDlg().TryRemoveObject(iterInfo->m_ObjectId);
				} else {
					pmap.ReAddObj(objType, layerID, objID);
					m_pMapEditorView->GetObjectListDlg().TryAddObject(iterInfo->m_ObjectId);
				}
			} else {
				info = *iterInfo;
				info.m_ObjectId = CLayerView::GetObjectId(info.m_ObjectId);
				pmap.GetObjInfo(objType, layerID, objID) = info;
			}
			switch (objType)
			{
			case PMap::MAP_OBJ_LINKEDOBJ:
				tmpKey.first = layerID;
				tmpKey.second = objID;
				m_pMapEditorView->m_TreeMap.erase(tmpKey);
				break;
			case PMap::MAP_OBJ_DISTORT:
				{
					tmpKey.first = layerID;
					tmpKey.second = objID;
					std::map<std::pair<char, unsigned int>, Nuclear::DistortBase*>::iterator iter = m_pMapEditorView->m_DistortMap.find(tmpKey);
					if (iter != m_pMapEditorView->m_DistortMap.end())
					{
						delete (iter->second);
						m_pMapEditorView->m_DistortMap.erase(iter);
					}
				}
				break;
			case PMap::MAP_OBJ_EFFECT:
				{
					CDisplayMapBase::EffectMap::iterator effIter = m_pMapEditorView->m_EffectArray[layerID].find(objID);
					if (effIter != m_pMapEditorView->m_EffectArray[layerID].end())
					{
						delete effIter->second;
						m_pMapEditorView->m_EffectArray[layerID].erase(effIter);
					}
				}
				break;
			}
			setSortBaseIDs.insert(iterInfo->m_ObjectId);
		}
	}
	if (oper->m_AtomObjInfo[type].size() == 0)
	{
		iterInfo = oper->m_AtomObjInfo[otherType].end();
		while (iterInfo != oper->m_AtomObjInfo[otherType].begin())
		{
			iterInfo--;
			if (!CLayerView::ConvertObjectType(CLayerView::GetObjectType(iterInfo->m_ObjectId), objType))
				continue;
			objID = CLayerView::GetObjectId(iterInfo->m_ObjectId);
			XPASSERT(pmap.GetConstObjInfo(objType, layerID).back().m_ObjectId == objID);
			setSortBaseIDs.insert(iterInfo->m_ObjectId);
			m_pMapEditorView->GetObjectListDlg().TryRemoveObject(iterInfo->m_ObjectId);

			switch (objType)
			{
			case PMap::MAP_OBJ_LINKEDOBJ:
				tmpKey.first = layerID;
				tmpKey.second = objID;
				m_pMapEditorView->m_TreeMap.erase(tmpKey);
				break;
			case PMap::MAP_OBJ_DISTORT:
				{
					tmpKey.first = layerID;
					tmpKey.second = objID;
					std::map<std::pair<char, unsigned int>, Nuclear::DistortBase*>::iterator iter = m_pMapEditorView->m_DistortMap.find(tmpKey);
					if (iter != m_pMapEditorView->m_DistortMap.end())
					{
						delete (iter->second);
						m_pMapEditorView->m_DistortMap.erase(iter);
					}
				}
				break;
			case PMap::MAP_OBJ_EFFECT:
				{
					CDisplayMapBase::EffectMap::iterator effIter = m_pMapEditorView->m_EffectArray[layerID].find(objID);
					if (effIter != m_pMapEditorView->m_EffectArray[layerID].end())
					{
						delete effIter->second;
						m_pMapEditorView->m_EffectArray[layerID].erase(effIter);
					}
				}
				break;
			}
			pmap.DestoryLastObj(objType, layerID);
		}
	}
	m_pMapEditorView->m_ScreenElements.RemoveObjects(static_cast<unsigned char>(layerID), setSortBaseIDs);
	m_pMapEditorView->m_ScreenElements.SetMustRenew();
	return true;
}

bool CActionList::SetNewDeleteLayersInfo(int type)
{
	CAction::CNewDeleteGroundLayerActionAtom* oper = (*m_Iter)->GetNewDeleteGroundLayerActionData();
	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	CToolsMap::TileLayerInfoList& layerInfos = oper->oper[type];
	if (pDoc->m_ToolsMap.m_GroundLayerInfoArray.size() > layerInfos.size())
	{

		delete(pDoc->m_ToolsMap.m_GroundLayersArray[oper->m_LayerID]);
		pDoc->m_ToolsMap.m_GroundLayersArray.erase(remove(pDoc->m_ToolsMap.m_GroundLayersArray.begin(), pDoc->m_ToolsMap.m_GroundLayersArray.end(), pDoc->m_ToolsMap.m_GroundLayersArray[oper->m_LayerID]));
	} else {

		PtrTileType data = new TileType[pDoc->GetToolsMap().GetSmlTileCount()];
		memcpy(data, oper->m_Data, pDoc->GetToolsMap().GetSmlTileSize());
		pDoc->m_ToolsMap.m_GroundLayersArray.push_back(data);
		Nuclear::SubMap::PtrTileTypeList::reverse_iterator rIter = pDoc->m_ToolsMap.m_GroundLayersArray.rbegin();
		size_t counter = pDoc->m_ToolsMap.m_GroundLayersArray.size() - 1;
		for (;;++rIter,counter--)
		{
			if (counter == oper->m_LayerID)
			{
				*rIter = data;
				break;
			}
			*rIter = *(rIter+1);
		}
	}
	pDoc->m_ToolsMap.m_GroundLayerInfoArray = layerInfos;
	m_pMapEditorView->m_LayerListDlg.ReNewTree();
	return true;
}

bool CActionList::SetObjectNames(int type)
{
	CAction::CObjectNameActionAtom* oper = (*m_Iter)->GetObjectNameActionData();
	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	Nuclear::PMap::MAP_OBJ_TYPE objType;
	for (int t=0;t<Nuclear::PMap::MAP_OBJ_COUNT;++t)
	{
		objType = static_cast<Nuclear::PMap::MAP_OBJ_TYPE>(t);
		pDoc->SetObjectNameMap(objType, oper->oper[objType][type]);
	}
	return true;
}

bool CActionList::SetAllGroundLayersInfo(int type)
{
	CAction::CGroundLayerInfosActionAtom* oper = (*m_Iter)->GetAllGroundLayerInfosActionData();
	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	CToolsMap::TileLayerInfoList& layerInfos = oper->oper[type];
	pDoc->m_ToolsMap.m_GroundLayerInfoArray = layerInfos;
	m_pMapEditorView->m_LayerListDlg.ReNewTree();
	return true;
}

bool CActionList::SetSingleGroundLayerInfo(int type)
{
	CAction::CGroundLayerInfoActionAtom* oper = (*m_Iter)->GetSingleGroundLayerInfoActionData();
	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	pDoc->m_ToolsMap.m_GroundLayerInfoArray[oper->m_Layer] = oper->oper[type];

	m_pMapEditorView->m_LayerListDlg.SetLayerChecked(oper->m_Layer, oper->oper[type].isShow);
	m_pMapEditorView->m_LayerListDlg.SetLayerName(oper->m_Layer, oper->oper[type].name);
	return true;
}

bool CActionList::SetWater(int type)
{
	CAction::CWaterActionAtom* oper = (*m_Iter)->GetWaterActionData();
	PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	CAction::CWaterActionAtom::WaterIterator iter = oper->m_WaterData.begin();
	Nuclear::SubMap::WaterTileType *pWaterData = pmap.GetWaterData();
	for (;iter!=oper->m_WaterData.end();iter++)
	{
		pWaterData[pmap.GetWaterPos(iter->x, iter->y)] = iter->oper[type];
	}
	return true;
}

bool CActionList::SetTileColor(int type)
{
	CAction::CTileColorActionAtom* oper = (*m_Iter)->GetTileColorActionData();
	PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	CAction::CTileColorActionAtom::TileColorIterator iter = oper->m_TileColorData.begin();
	unsigned int *pColorData = pmap.GetColorData();
	for (;iter!=oper->m_TileColorData.end();iter++)
	{
		pColorData[pmap.GetColorPos(iter->x, iter->y)] = iter->oper[type];
	}
	return true;
}

bool CActionList::SetSuperTile(int type)
{
	CAction::CSuperTileActionAtom* oper = (*m_Iter)->GetSuperTileActionData();

	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	std::set<CAction::SuperTileGranule>::iterator iter = oper->m_TileData.begin();
	std::map<int, TileType>::iterator tileIter;
	for (;iter!=oper->m_TileData.end();iter++)
	{
		for (tileIter=iter->oper[type].begin();tileIter!=iter->oper[type].end();++tileIter)
		{
			pDoc->SetTileForcibly(tileIter->first, iter->x, iter->y, tileIter->second);
		}
	}
	return true;
}

bool CActionList::SetTile(int type)
{
	CAction::CTileActionAtom* oper = (*m_Iter)->GetTileActionData();

	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	CAction::CTileActionAtom::TileDataIterator iter = oper->m_TileData.begin();
	for (;iter!=oper->m_TileData.end();iter++)
	{
		pDoc->SetTileForcibly(oper->m_SmallLayerID, iter->x, iter->y, iter->oper[type]);
	}
	m_pMapEditorView->SetSmallLayerEdit(oper->m_SmallLayerID);
	return true;
}

bool CActionList::SetBiggerTile(int type)
{
	CAction::CBiggerTileActionAtom* oper = (*m_Iter)->GetBiggerTileActionData();

	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	CAction::CBiggerTileActionAtom::TileDataIterator iter = oper->m_TileData.begin();
	for (;iter!=oper->m_TileData.end();iter++)
	{
		pDoc->SetTileForcibly(oper->m_GroundLayers, iter->x, iter->y, iter->oper[type]);
	}
	m_pMapEditorView->SetGroundLayerEdit(oper->m_GroundLayers);
	return true;
}

bool CActionList::SetChangeCanvas(int type)
{
	CAction::CCanvasActionAtom* oper = (*m_Iter)->GetCanvasActionData();
	CMapEditorDoc* pDoc = m_pMapEditorView->GetDocument();
	CToolsMap &pmap = pDoc->GetToolsMap();
	bool changeWaterEffect = false;
	for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
	{
		if (pmap.GetWaterName(i) != oper->oper[type].GetWaterName(i))
		{
			changeWaterEffect = true;
			break;
		}
	}
	pmap = oper->oper[type];
	m_pMapEditorView->m_ScreenElements.ClearLayers();
	m_pMapEditorView->SetRatio(m_pMapEditorView->GetRatio());
	m_pMapEditorView->m_TreeMap.clear();
	if (changeWaterEffect)
	{
		if (m_pMapEditorView->m_SelectWaterDlg.m_hWnd)
		{
			m_pMapEditorView->m_SelectWaterDlg.RenewList();
		}
		m_pMapEditorView->ResetWaterEffect();
		if (m_pMapEditorView->m_SmallMapDlg.m_hWnd)
		{
			m_pMapEditorView->m_SmallMapDlg.ResetWaterEffect();
		}
	}
	m_pMapEditorView->m_SmallMapDlg.TryInvalidate(true);
	return true;
}

bool CActionList::RedoOneAction()
{
	if (!CanRedo())
		return false;
	if (ToDoAction(CAction::CActionAtom::AFTER_OPERATOR))
	{
		m_pMapEditorView->ChangeEditLayer((*m_Iter)->GetActionLayerID());
		m_pMapEditorView->GetDocument()->SetModifiedFlag();
		m_Iter++;
		return true;
	}
	return false;
}

bool CActionList::UndoOneAction()
{
	if (!CanUndo())
		return false;
	m_Iter--;
	if (ToDoAction(CAction::CActionAtom::BEFORE_OPERATOR))
	{
		m_pMapEditorView->ChangeEditLayer((*m_Iter)->GetActionLayerID());
		m_pMapEditorView->GetDocument()->SetModifiedFlag();
		return true;
	}
	return false;
}

bool CActionList::ToDoAction(int type)
{
	switch ((*m_Iter)->GetActionType())
	{
	case CAction::AT_CHANGE_CANVAS:
		return SetChangeCanvas(type);
		break;
	case CAction::AT_TILE:
		return SetTile(type);
		break;
	case CAction::AT_SUPER_TILE:
		return SetSuperTile(type);
		break;
	case CAction::AT_TILE_COLOR:
		return SetTileColor(type);
		break;
	case CAction::AT_BIGGER_TILE:
		return SetBiggerTile(type);
		break;
	case CAction::AT_WATER:
		return SetWater(type);
		break;
	case CAction::AT_OBJECTS:
		return SetObjects(type);
		break;
	case CAction::AT_SINGLE_GROUND_LAYER_INFO:
		return SetSingleGroundLayerInfo(type);
		break;
	case CAction::AT_ALL_GROUND_LAYER_INFOS:
		return SetAllGroundLayersInfo(type);
		break;
	case CAction::AT_NEW_DELETE_GROUND_LAYER:
		return SetNewDeleteLayersInfo(type);
		break;
	case CAction::AT_OBJECT_NAMES:
		return SetObjectNames(type);
		break;
	case CAction::AT_ELEMENT_BASE:
		return SetElementBase(type);
		break;
	case CAction::AT_DYNAMIC_BASE:
		return SetLinkedObjectBase(type);
		break;
	case CAction::AT_SINGLE_OBJECT_NAMES:
		return SetSingleObjectName(type);
		break;
	case CAction::AT_TRANSPARENT_OBJECT:
		return SetTransparentObjectsAction(type);
		break;
	case CAction::AT_TRIGGER_OBJECT:
		return SetTriggerObjectsAction(type);
		break;
	case CAction::AT_WATER_AREA:
		return SetWaterAreaAction(type);
		break;
	case CAction::AT_TIME_EFFECT:
		return SetTimeEffect(type);
		break;
	case CAction::AT_MASK_PARAM:
		return SetMaskParam(type);
		break;
	case CAction::AT_BACKGROUND_INFO:
		return SetBackgroundInfo(type);
		break;
	case CAction::AT_BACKGROUND_DATA:
		return SetBackgroundData(type);
		break;
	case CAction::AT_BACKGROUND_ORDER:
		return SetBackgroundOrder(type);
		break;
	}
	return false;
}

void CActionList::DoOneAction(CAction* oper)
{
	if (m_Iter != m_ActionList.end())
	{
		std::list<CAction*>::iterator iter = m_Iter;
		for (;iter!=m_ActionList.end();iter++)
		{
			if ((*iter)->GetActionType() == CAction::AT_BACKGROUND_INFO)
			{
				CAction::CBackgroundInfoActionAtom *oper = (*iter)->GetBackgroundInfoActionData();
				if (oper->oper[0].pPics != oper->oper[1].pPics)
				{
					Nuclear::XFree(oper->oper[CAction::CActionAtom::AFTER_OPERATOR].pPics);
				}
			}
			delete(*iter);
		}
		m_ActionList.erase(m_Iter, m_ActionList.end());
	}
	m_ActionList.push_back(oper);

	if (m_ActionList.size() > MAX_LENGTH)
	{
		CAction* front = m_ActionList.front();
		m_ActionList.pop_front();
		if (front->GetActionType() == CAction::AT_BACKGROUND_INFO)
		{
			CAction::CBackgroundInfoActionAtom *oper = front->GetBackgroundInfoActionData();
			if (oper->oper[0].pPics != oper->oper[1].pPics)
			{
				Nuclear::XFree(oper->oper[CAction::CActionAtom::BEFORE_OPERATOR].pPics);
			}
		}
		delete(front);
	}

	m_Iter = m_ActionList.end();
}

CActionList::~CActionList(void)
{
	std::list<CAction*>::iterator iter = m_ActionList.begin(), iterEnd = m_ActionList.end();
	bool hasDo = true;
	for (;iter!=iterEnd;++iter)
	{
		if (iter == m_Iter)
			hasDo = false;
		if ((*iter)->GetActionType() == CAction::AT_BACKGROUND_INFO)
		{
			CAction::CBackgroundInfoActionAtom *oper = (*iter)->GetBackgroundInfoActionData();
			if (hasDo)
			{
				if ((oper->m_InfoID[CAction::CActionAtom::BEFORE_OPERATOR] != -1) && (oper->oper[0].pPics != oper->oper[1].pPics))
				{
					Nuclear::XFree(oper->oper[CAction::CActionAtom::BEFORE_OPERATOR].pPics);
				}
			} else {
				if (oper->oper[0].pPics != oper->oper[1].pPics)
				{
					Nuclear::XFree(oper->oper[CAction::CActionAtom::AFTER_OPERATOR].pPics);
				}
			}
		}
		delete (*iter);
	}
}

CActionList::CActionList(CMapEditorView* pMapEditorView)
: m_pMapEditorView(pMapEditorView)
{
	m_Iter = m_ActionList.end();
}

