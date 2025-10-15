#include "StdAfx.h"
#include "Operators.h"

#include "SoundEditorDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

CAction::CAction(void)
{
}

CAction::~CAction(void)
{
}


////////////////////////////////OperatorList/////////////////////////
CActionList::CActionList(CSoundEditorDoc* pDoc) : m_pDoc(pDoc)
{
	m_Iter = m_ActionList.end();
}

CActionList::~CActionList()
{
	CleanAll();
}

void CActionList::CleanAll()
{
	std::list<CAction*>::iterator iter;
	int curType = CAction::BEFORE_ACTION;
	for (iter=m_ActionList.begin();iter!=m_ActionList.end();++iter)
	{
		if (iter == m_Iter)
			curType = CAction::AFTER_ACTION;
		switch ((*iter)->GetType())
		{
		case CAction::ADD_DEL_ENV_AREA:
			{
				AddDelEnvArea* pOper = dynamic_cast<AddDelEnvArea*>(*iter);
				if (pOper && pOper->IsCurrentAdd(curType))
					delete pOper->pArea;
			}
			break;
		case CAction::ADD_DEL_BKG_AREA:
			{
				AddDelBkgArea* pOper = dynamic_cast<AddDelBkgArea*>(*iter);
				if (pOper && pOper->IsCurrentAdd(curType))
					delete pOper->pArea;
			}
			break;
		case CAction::ADD_DEL_BKG_VOL_AREA:
			{
				AddDelBkgVolArea* pOper = dynamic_cast<AddDelBkgVolArea*>(*iter);
				if (pOper && pOper->IsCurrentAdd(curType))
					delete pOper->pArea;
			}
			break;
		default:
			break;
		}
		delete (*iter);
	}
	m_ActionList.clear();
}

void CActionList::DoOneAction(CAction* pOper)
{
	if (m_Iter != m_ActionList.end())	//被撤销了的东西，也就是还没有做的部分
	{
		std::list<CAction*>::iterator iter = m_Iter;
		for (;iter!=m_ActionList.end();++iter)
		{
			switch ((*iter)->GetType())
			{
			case CAction::ADD_DEL_ENV_AREA:
				{
					AddDelEnvArea* pOper = dynamic_cast<AddDelEnvArea*>(*iter);
					if (pOper && pOper->IsCurrentAdd(CAction::AFTER_ACTION))
						delete pOper->pArea;
				}
				break;
			case CAction::ADD_DEL_BKG_AREA:
				{
					AddDelBkgArea* pOper = dynamic_cast<AddDelBkgArea*>(*iter);
					if (pOper && pOper->IsCurrentAdd(CAction::AFTER_ACTION))
						delete pOper->pArea;
				}
				break;
			case CAction::ADD_DEL_BKG_VOL_AREA:
				{
					AddDelBkgVolArea* pOper = dynamic_cast<AddDelBkgVolArea*>(*iter);
					if (pOper && pOper->IsCurrentAdd(CAction::AFTER_ACTION))
						delete pOper->pArea;
				}
				break;
			default:
				break;
			}

			delete (*iter);
		}
		m_ActionList.erase(m_Iter, m_ActionList.end());
	}
	m_ActionList.push_back(pOper);

	if (m_ActionList.size() > MAX_LENGTH)	//队列满了，已经做了的部分
	{
		CAction* front = m_ActionList.front();
		m_ActionList.pop_front();
		switch (front->GetType())
		{
		case CAction::ADD_DEL_ENV_AREA:
			{
				AddDelEnvArea* pOper = dynamic_cast<AddDelEnvArea*>(front);
				if (pOper && pOper->IsCurrentAdd(CAction::BEFORE_ACTION))
					delete pOper->pArea;
			}
			break;
		case CAction::ADD_DEL_BKG_AREA:
			{
				AddDelBkgArea* pOper = dynamic_cast<AddDelBkgArea*>(front);
				if (pOper && pOper->IsCurrentAdd(CAction::BEFORE_ACTION))
					delete pOper->pArea;
			}
			break;
		case CAction::ADD_DEL_BKG_VOL_AREA:
			{
				AddDelBkgVolArea* pOper = dynamic_cast<AddDelBkgVolArea*>(front);
				if (pOper && pOper->IsCurrentAdd(CAction::BEFORE_ACTION))
					delete pOper->pArea;
			}
			break;
		default:
			break;
		}

		delete(front);
	}

	m_Iter = m_ActionList.end();
	m_pDoc->SetModifiedFlag();
}

bool CActionList::TodoAction(int type)
{
	bool result = false;
	m_pDoc->m_pSelectedEnvArea = NULL;
	m_pDoc->m_pSelectedBkgArea = NULL;
	switch ((*m_Iter)->GetType())
	{
	case CAction::ADD_DEL_ENV_AREA:
		result = SetAddDelEnvArea(type);
		break;
	case CAction::ADD_DEL_BKG_AREA:
		result = SetAddDelBkgArea(type);
		break;
	case CAction::ADD_DEL_BKG_VOL_AREA:
		result = SetAddDelBkgVolArea(type);
		break;
	case CAction::ENV_SOUND_PROP:
		result = SetEnvSoundProp(type);
		break;
	case CAction::ENV_SOUND_TIME:
		result = SetEnvSoundTime(type);
		break;
	case CAction::ENV_SOUND_LOOPING:
		result = SetEnvSoundLoop(type);
		break;
	case CAction::ENV_SOUND_INTERVAL:
		result = SetEnvSoundInterval(type);
		break;
	case CAction::ENV_SOUND_PRIORITY:
		result = SetEnvSoundPriority(type);
		break;
	case CAction::BKG_SOUND_VOLUME:
		result = SetBkgSoundVolume(type);
		break;
	case CAction::BKG_SOUND_TYPE:
		result = SetBkgSoundType(type);
		break;
	case CAction::BKG_SOUND_INTERVAL:
		result = SetBkgSoundInterval(type);
		break;
	case CAction::ADD_DEL_ENV_SOUND:
		result = SetAddDelEnvSound(type);
		break;
	case CAction::ADD_DEL_BKG_SOUND:
		result = SetAddDelBkgSound(type);
		break;
	case CAction::ADD_DEL_ENV_TYPE:
		result = SetAddDelEnvType(type);
		break;
	case CAction::MOV_AREA:
		result = SetMovArea(type);
		break;
	case CAction::ENV_AREA_TRANSITION:
		result = SetEnvAreaTransition(type);
		break;
	case CAction::BKV_AREA_TRANSITION:
		result = SetBkvAreaTransition(type);
		break;
	case CAction::POLYGON_MODIFY:
		result = SetPolygonModify(type);
		break;
	case CAction::CIRCLE_MODIFY:
		result = SetCircleModify(type);
		break;
	case CAction::STEP_SOUND_AREA:
		result = SetStepSoundArea(type);
		break;
	case CAction::BKV_AREA_VOL:
		result = SetBkvAreaVolume(type);
		break;
	default:
		XPASSERT(false && L"TodoOperator switch");
		break;
	}
	if (result)
	{
		EDIT_STATE state = (*m_Iter)->GetTabEditType();
		if (state != m_pDoc->GetEditState())
		{
			m_pDoc->SetEditState(state);
		}
		m_pDoc->m_nCurOperatorType = type;
		m_pDoc->m_pCurOperator = (*m_Iter);
		m_pDoc->SetModifiedFlag();
		m_pDoc->m_us = US_UNDOREDO;
		m_pDoc->m_nUpdateViewSign = UVS_TAB;
		m_pDoc->UpdateAllViews(NULL);
		m_pDoc->m_us = US_DEFAULT;
		m_pDoc->m_nUpdateViewSign = 0;
		m_pDoc->m_pCurOperator = NULL;
		m_pDoc->m_nCurOperatorType = -1;

	}
	return result;
}

bool CActionList::UndoOneAction()
{
	if (!CanUndo())
		return false;
	m_Iter--;
	return TodoAction(CAction::BEFORE_ACTION);
}

bool CActionList::RedoOneAction()
{
	if (!CanRedo())
		return false;
	if (TodoAction(CAction::AFTER_ACTION))
	{
		m_Iter++;
		return true;
	}
	return false;
}


bool CActionList::SetAddDelEnvArea(int type)
{
	AddDelEnvArea* pOper = dynamic_cast<AddDelEnvArea*>(*m_Iter);
	if (!pOper)
		return false;
	CToolsSoundArea::HandleVet::iterator hIt, hIe;
	CToolsSoundArea::HandleVetVet::iterator hvIt = pOper->pArea->soundHandleVet.begin(), 
		hvIe = pOper->pArea->soundHandleVet.end();
	size_t count = 0;
	if (pOper->IsCurrentAdd(type))
	{
		m_pDoc->m_envAreas.insert(pOper->pArea);
		if (pOper->isAdd)
			++(m_pDoc->m_nEnvOrdersSeed);
		m_pDoc->m_pSelectedEnvArea = pOper->pArea;
		for (;hvIt!=hvIe;++hvIt)
		{
			count += hvIt->size();
			for (hIt = hvIt->begin(),hIe = hvIt->end();hIt!=hIe;++hIt)
				m_pDoc->m_envSoundMap[*hIt] = pOper->pArea;
		}
		m_pDoc->ModifyEnvAreaSoundOrders(pOper->pArea->nOrders, (int)count);
	} else {
		m_pDoc->m_envAreas.erase(pOper->pArea);
		if (pOper->isAdd)
			--(m_pDoc->m_nEnvOrdersSeed);
		for (;hvIt!=hvIe;++hvIt)
		{
			count += hvIt->size();
			for (hIt = hvIt->begin(),hIe = hvIt->end();hIt!=hIe;++hIt)
				m_pDoc->m_envSoundMap.erase(*hIt);
		}
		m_pDoc->ModifyEnvAreaSoundOrders(pOper->pArea->nOrders, -(int)count);
	}
	return true;
}

bool CActionList::SetAddDelBkgArea(int type)
{
	AddDelBkgArea* pOper = dynamic_cast<AddDelBkgArea*>(*m_Iter);
	if (!pOper)
		return false;
	CToolsSoundArea::HandleVet::iterator hIt = pOper->pArea->soundHandleVet.begin(), 
		hIe = pOper->pArea->soundHandleVet.end();
	size_t count = pOper->pArea->soundHandleVet.size();
	if (pOper->IsCurrentAdd(type))
	{
		m_pDoc->m_bkgAreas.insert(pOper->pArea);
		if (pOper->isAdd)
			++(m_pDoc->m_nBkgOrdersSeed);
		m_pDoc->m_pSelectedBkgArea = pOper->pArea;
		
		for (;hIt!=hIe;++hIt)
			m_pDoc->m_bkgSoundMap[*hIt] = pOper->pArea;
		m_pDoc->ModifyBkgAreaSoundOrders(pOper->pArea->nOrders, (int)count);
	} else {
		m_pDoc->m_bkgAreas.erase(pOper->pArea);
		if (pOper->isAdd)
			--(m_pDoc->m_nBkgOrdersSeed);
		for (;hIt!=hIe;++hIt)
			m_pDoc->m_bkgSoundMap.erase(*hIt);
		m_pDoc->ModifyBkgAreaSoundOrders(pOper->pArea->nOrders, -(int)count);
	}
	return true;
}

bool CActionList::SetAddDelBkgVolArea(int type)
{
	AddDelBkgVolArea* pOper = dynamic_cast<AddDelBkgVolArea*>(*m_Iter);
	if (!pOper)
		return false;
	if (pOper->IsCurrentAdd(type))
	{
		m_pDoc->m_bkgVolAreas.insert(pOper->pArea);
		m_pDoc->m_pSelectedBkgVolArea = pOper->pArea;
	} else {
		m_pDoc->m_bkgVolAreas.erase(pOper->pArea);
		m_pDoc->m_pSelectedBkgVolArea = NULL;
	}
	return true;
}

bool CActionList::SetEnvSoundProp(int type)
{
	EnvSoundProp* pOper = dynamic_cast<EnvSoundProp*>(*m_Iter);
	if (!pOper)
		return false;
	switch (pOper->type)
	{
	case EnvSoundProp::MAX_VOL:
		pOper->pArea->SetMaxVol(pOper->typeID, pOper->soundID, pOper->data[type]);
		break;
	case EnvSoundProp::MIN_VOL:
		pOper->pArea->SetMinVol(pOper->typeID, pOper->soundID, pOper->data[type]);
		break;
	case EnvSoundProp::MAX_FREQ:
		pOper->pArea->SetMaxFreq(pOper->typeID, pOper->soundID, pOper->data[type]);
		break;
	case EnvSoundProp::MIN_FREQ:
		pOper->pArea->SetMinFreq(pOper->typeID, pOper->soundID, pOper->data[type]);
		break;
	case EnvSoundProp::SOUND_TYPE:
		pOper->pArea->SetSoundType(pOper->typeID, pOper->soundID, pOper->data[type]);
		break;
	default:
		XPASSERT(false && L"SetEnvSoundProp type Error!");
		return false;
		break;
	}
	m_pDoc->m_pSelectedEnvArea = pOper->pArea;
	return true;
}

bool CActionList::SetEnvSoundTime(int type)
{
	EnvSoundTime* pOper = dynamic_cast<EnvSoundTime*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetTime(pOper->typeID, pOper->soundID, pOper->type, pOper->time[type]);
	m_pDoc->m_pSelectedEnvArea = pOper->pArea;
	return true;
}

bool CActionList::SetEnvSoundLoop(int type)
{
	EnvSoundLoop* pOper = dynamic_cast<EnvSoundLoop*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetLooping(pOper->typeID, pOper->soundID, pOper->loop[type]);
	m_pDoc->m_pSelectedEnvArea = pOper->pArea;
	return true;
}

bool CActionList::SetEnvSoundInterval(int type)
{
	EnvSoundInterval* pOper = dynamic_cast<EnvSoundInterval*>(*m_Iter);
	if (!pOper)
		return false;
	switch (pOper->type)
	{
	case EnvSoundInterval::MAX:
		pOper->pArea->SetMaxInterval(pOper->typeID, pOper->interval[type]);
		break;
	case EnvSoundInterval::MIN:
		pOper->pArea->SetMinInterval(pOper->typeID, pOper->interval[type]);
		break;
	default:
		XPASSERT(false && L"SetEnvSoundInterval type Error!");
		return false;
		break;
	}
	m_pDoc->m_pSelectedEnvArea = pOper->pArea;
	return true;
}

bool CActionList::SetEnvSoundPriority(int type)
{
	EnvSoundPriority* pOper = dynamic_cast<EnvSoundPriority*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetPriority(pOper->typeID, pOper->priority[type]);
	m_pDoc->m_pSelectedEnvArea = pOper->pArea;
	return true;
}

void CActionList::SetDocSelectedArea(CToolsBkgSoundArea *pArea)
{
	if (!(pArea->GetType() == Nuclear::XPAREA_TYPE_NULL))
	{
		m_pDoc->m_pSelectedBkgArea = pArea;
	}
}

bool CActionList::SetBkgSoundVolume(int type)
{
	BkgSoundVolume* pOper = dynamic_cast<BkgSoundVolume*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetVol(pOper->soundID, pOper->vol[type]);
	SetDocSelectedArea(pOper->pArea);
	return true;
}

bool CActionList::SetBkgSoundType(int type)
{
	BkgSoundType* pOper = dynamic_cast<BkgSoundType*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetSoundType(pOper->soundID, pOper->type[type]);
	SetDocSelectedArea(pOper->pArea);
	return true;
}

bool CActionList::SetBkgSoundInterval(int type)
{
	BkgSoundInterval* pOper = dynamic_cast<BkgSoundInterval*>(*m_Iter);
	if (!pOper)
		return false;
	switch (pOper->type)
	{
	case BkgSoundInterval::MAX:
		pOper->pArea->SetMaxInterval(pOper->interval[type]);
		break;
	case BkgSoundInterval::MIN:
		pOper->pArea->SetMinInterval(pOper->interval[type]);
		break;
	default:
		XPASSERT(false && L"SetBkgSoundInterval type Error!");
		return false;
		break;
	}
	m_pDoc->m_pSelectedBkgArea = pOper->pArea;
	return true;
}

bool CActionList::SetBkgSoundFadeTime(int type)
{
	BkgSoundFadeTime* pOper = dynamic_cast<BkgSoundFadeTime*>(*m_Iter);
	if (!pOper)
		return false;
	switch (pOper->type)
	{
	case BkgSoundFadeTime::T_IN:
		pOper->pArea->SetFadeInTime(pOper->fadeTime[type]);
		break;
	case BkgSoundFadeTime::T_OUT:
		pOper->pArea->SetFadeOutTime(pOper->fadeTime[type]);
		break;
	default:
		XPASSERT(false && L"SetBkgSoundFadeTime type Error!");
		return false;
		break;
	}
	m_pDoc->m_pSelectedBkgArea = pOper->pArea;
	return true;
}

bool CActionList::SetBkgSoundWaitPrevArea(int type)
{
	BkgSoundWaitPrevArea* pOper = dynamic_cast<BkgSoundWaitPrevArea*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetWaitPrevAreaPlayEnd(pOper->wait[type]);
	m_pDoc->m_pSelectedBkgArea = pOper->pArea;
	return true;
}

bool CActionList::SetBkgSoundPlayMode(int type)
{
	BkgSoundPlayMode* pOper = dynamic_cast<BkgSoundPlayMode*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetPlayMode(pOper->type[type]);
	m_pDoc->m_pSelectedBkgArea = pOper->pArea;
	return true;
}

bool CActionList::SetAddDelEnvSound(int type)
{
	AddDelEnvSound* pOper = dynamic_cast<AddDelEnvSound*>(*m_Iter);
	if (!pOper)
		return false;
	if (pOper->IsCurrentAdd(type))
	{
		pOper->pArea->InsertSound(pOper->typeID, pOper->soundID, pOper->param);
		++(pOper->pArea->nSoundListEndID);
		if (pOper->isAdd)
		{
			++m_pDoc->m_nHandleSeed;
		}
		m_pDoc->m_envSoundMap[pOper->handle] = pOper->pArea;
		CToolsEnvSoundArea::HandleVet::iterator hanit = pOper->pArea->soundHandleVet[pOper->typeID].begin() + pOper->soundID;
		pOper->pArea->soundHandleVet[pOper->typeID].insert(hanit, pOper->handle);
		m_pDoc->ModifyEnvAreaSoundOrders(pOper->pArea->nOrders, 1);
	} else {
		XPASSERT(pOper->handle == pOper->pArea->soundHandleVet[pOper->typeID][pOper->soundID]);
		if (pOper->isAdd)
		{
			--m_pDoc->m_nHandleSeed;
		}
		pOper->pArea->DeleteSound(pOper->typeID, pOper->soundID);
		m_pDoc->m_envSoundMap.erase(pOper->handle);
		pOper->pArea->soundHandleVet[pOper->typeID].erase(pOper->pArea->soundHandleVet[pOper->typeID].begin() + pOper->soundID);
		--(pOper->pArea->nSoundListEndID);
		m_pDoc->ModifyEnvAreaSoundOrders(pOper->pArea->nOrders, -1);
	}
	m_pDoc->m_pSelectedEnvArea = pOper->pArea;
	return true;
}

bool CActionList::SetAddDelBkgSound(int type)
{
	AddDelBkgSound* pOper = dynamic_cast<AddDelBkgSound*>(*m_Iter);
	if (!pOper)
		return false;
	if (pOper->IsCurrentAdd(type))
	{
		pOper->pArea->InsertSound(pOper->soundID, pOper->param);
		++(pOper->pArea->nSoundListEndID);
		if (pOper->isAdd)
		{
			++m_pDoc->m_nHandleSeed;
		}
		m_pDoc->m_bkgSoundMap[pOper->handle] = pOper->pArea;
		CToolsBkgSoundArea::HandleVet::iterator hanit = pOper->pArea->soundHandleVet.begin() + pOper->soundID;
		pOper->pArea->soundHandleVet.insert(hanit, pOper->handle);
		m_pDoc->ModifyBkgAreaSoundOrders(pOper->pArea->nOrders, 1);
	} else {
		XPASSERT(pOper->handle == pOper->pArea->soundHandleVet[pOper->soundID]);
		if (pOper->isAdd)
		{
			--m_pDoc->m_nHandleSeed;
		}
		pOper->pArea->DeleteSound(pOper->soundID);
		m_pDoc->m_bkgSoundMap.erase(pOper->handle);
		pOper->pArea->soundHandleVet.erase(pOper->pArea->soundHandleVet.begin() + pOper->soundID);
		--(pOper->pArea->nSoundListEndID);
		m_pDoc->ModifyBkgAreaSoundOrders(pOper->pArea->nOrders, -1);
	}
	SetDocSelectedArea(pOper->pArea);
	return true;
}

bool CActionList::SetAddDelEnvType(int type)
{
	AddDelEnvType* pOper = dynamic_cast<AddDelEnvType*>(*m_Iter);
	if (!pOper)
		return false;
	int count = static_cast<int>(pOper->soundHandles.size());
	CToolsSoundArea::HandleVet::iterator hIt = pOper->soundHandles.begin(), 
		hIe = pOper->soundHandles.end();
	CToolsEnvSoundArea::HandleVetVet::iterator hanit = pOper->pArea->soundHandleVet.begin() + pOper->typeID;
	if (pOper->IsCurrentAdd(type))
	{
		pOper->pArea->InsertSoundType(pOper->typeID, pOper->param);
		pOper->pArea->nSoundListEndID += count;
		for (;hIt!=hIe;++hIt)
			m_pDoc->m_envSoundMap[*hIt] = pOper->pArea;	
		pOper->pArea->soundHandleVet.insert(hanit, pOper->soundHandles);
		m_pDoc->ModifyEnvAreaSoundOrders(pOper->pArea->nOrders, count);
	} else {
		pOper->pArea->DeleteSoundType(pOper->typeID);
		pOper->pArea->nSoundListEndID -= count;
		for (;hIt!=hIe;++hIt)
			m_pDoc->m_envSoundMap.erase(*hIt);
		pOper->pArea->soundHandleVet.erase(hanit);
		m_pDoc->ModifyEnvAreaSoundOrders(pOper->pArea->nOrders, -count);
	}
	m_pDoc->m_pSelectedEnvArea = pOper->pArea;
	return true;
}

bool CActionList::SetMovArea(int type)
{
	MovArea* pOper = dynamic_cast<MovArea*>(*m_Iter);
	if (!pOper)
		return false;
	switch (type)
	{
	case CAction::BEFORE_ACTION:
		{
			POINT pt;
			pt.x = -pOper->vet.x;
			pt.y = -pOper->vet.y;
			pOper->pArea->Move(pt);
		}
		break;
	case CAction::AFTER_ACTION:
		pOper->pArea->Move(pOper->vet);
		break;
	default:
		XPASSERT(false && L"SetMovArea type Error!");
		break;
	}
	m_pDoc->ResetAreaRegion(pOper->pArea);
	switch (pOper->GetTabEditType())
	{
	case EDIT_STATE_ENV_SOUND:
		m_pDoc->m_pSelectedEnvArea = dynamic_cast<CToolsEnvSoundArea*>(pOper->pArea);
		break;
	case EDIT_STATE_BKG_MUSIC:
		SetDocSelectedArea(dynamic_cast<CToolsBkgSoundArea*>(pOper->pArea));
		break;
	case EDIT_STATE_TESTING:
		XPASSERT(false && L"SetMovArea switch Error!");
		break;
	}
	return true;
}

bool CActionList::SetEnvAreaTransition(int type)
{
	EnvAreaTransition* pOper = dynamic_cast<EnvAreaTransition*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetTransition(pOper->trans[type]);
	m_pDoc->ResetAreaRegion(pOper->pArea);
	m_pDoc->m_pSelectedEnvArea = pOper->pArea;
	return true;
}

bool CActionList::SetBkvAreaTransition(int type)
{
	BkvAreaTransition* pOper = dynamic_cast<BkvAreaTransition*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetTransition(pOper->trans[type]);
	m_pDoc->ResetAreaRegion(pOper->pArea);
	m_pDoc->m_pSelectedBkgVolArea = pOper->pArea;
	return true;
}

bool CActionList::SetPolygonModify(int type)
{
	PolygonModify* pOper = dynamic_cast<PolygonModify*>(*m_Iter);
	if (!pOper)
		return false;
	switch (pOper->state)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			CToolsEnvSoundPolygonArea* pArea = dynamic_cast<CToolsEnvSoundPolygonArea*>(pOper->pArea);
			XPASSERT(pArea && L"CToolsEnvSoundPolygonArea");
			pArea->m_innerPolygon = pOper->polygon[type];
			pArea->m_ToolsCutPairs = pOper->cutpair[type];
			pArea->ResetOutter();
			pArea->ResetToolsPolygon(pArea->m_innerPolygon, pArea->m_ToolsInnerPolygons);
			m_pDoc->m_pSelectedEnvArea = pArea;
		}
		break;
	case EDIT_STATE_BKG_MUSIC:
		{
			CToolsBkgSoundPolygonArea* pArea = dynamic_cast<CToolsBkgSoundPolygonArea*>(pOper->pArea);
			XPASSERT(pArea && L"CToolsBkgSoundPolygonArea");
			pArea->m_polygon = pOper->polygon[type];
			pArea->m_ToolsCutPairs = pOper->cutpair[type];
			pArea->ResetToolsPolygon();
			m_pDoc->m_pSelectedBkgArea = pArea;
		}
		break;
	default:
		XPASSERT(false && L"SetPolygonModify switch Error!");
		return false;
	}
	m_pDoc->ResetAreaRegion(pOper->pArea);
	return true;
}
bool CActionList::SetCircleModify(int type)
{
	CircleModify* pOper = dynamic_cast<CircleModify*>(*m_Iter);
	if (!pOper)
		return false;
	switch (pOper->state)
	{
	case EDIT_STATE_ENV_SOUND:
		{
			CToolsEnvSoundCircleArea* pArea = dynamic_cast<CToolsEnvSoundCircleArea*>(pOper->pArea);
			XPASSERT(pArea && L"CToolsEnvSoundCircleArea");
			pArea->m_circle.m_nRadius = pOper->radius[type];
			pArea->ResetShape();
			m_pDoc->m_pSelectedEnvArea = pArea;
		}
		break;
	case EDIT_STATE_BKG_MUSIC:
		{
			CToolsBkgSoundCircleArea* pArea = dynamic_cast<CToolsBkgSoundCircleArea*>(pOper->pArea);
			XPASSERT(pArea && L"CToolsBkgSoundCircleArea");
			pArea->m_circle.m_nRadius = pOper->radius[type];
			m_pDoc->m_pSelectedBkgArea = pArea;
		}
		break;
	default:
		XPASSERT(false && L"SetPolygonModify switch Error!");
		return false;
	}
	m_pDoc->ResetAreaRegion(pOper->pArea);
	return true;
}

bool CActionList::SetStepSoundArea(int type)
{
	StepSoundArea* pOper = dynamic_cast<StepSoundArea*>(*m_Iter);
	if (!pOper)
		return false;
	std::vector<std::pair<int, Nuclear::StepSoundGT>>::iterator it = pOper->olddata.begin(), ie = pOper->olddata.end();
	Nuclear::StepSoundGT* pData = m_pDoc->m_pSSM->GetData();
	switch (type)
	{
	case CAction::BEFORE_ACTION:
		for (;it!=ie;++it)
		{
			*(pData + it->first) = it->second;
		}
		break;
	case CAction::AFTER_ACTION:
		for (;it!=ie;++it)
		{
			*(pData + it->first) = pOper->newdata;
		}
		break;
	}
	return true;
}

bool CActionList::SetBkvAreaVolume(int type)
{
	BkvAreaVolume* pOper = dynamic_cast<BkvAreaVolume*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->pArea->SetVol(pOper->vol[type]);
	m_pDoc->ResetAreaRegion(pOper->pArea);
	m_pDoc->m_pSelectedBkgVolArea = pOper->pArea;
	return true;
}
