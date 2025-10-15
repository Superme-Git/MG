#include "stdafx.h"
#include "EffectEditor.h"

#include "..\engine\effect\geffect.h"
#include "..\engine\effect\xapeffect.h"
#include "EffectEditorDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

bool CActionList::SetClipColorKeys(int type)
{
	CActionSetClipColorKeys* pOper = dynamic_cast<CActionSetClipColorKeys*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->m_pClip->RemoveKeys(Nuclear::AbstractEffectClip::KT_COLOR);
	ColorKeyMap::const_iterator iter = pOper->m_KeyMaps[type].begin();
	for (;iter!=pOper->m_KeyMaps[type].end();++iter)
	{
		pOper->m_pClip->AddColorKey(iter->first, iter->second);
	}
	return true;
}

bool CActionList::SetClipOneKeys(int type)
{
	CActionSetClipSingleKeys* pOper = dynamic_cast<CActionSetClipSingleKeys*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->m_pClip->SetToolsKeyPoints(pOper->m_KeyType, pOper->m_KeyPoints[type]);
	pOper->m_pClip->SetToolsSamplePoints(pOper->m_KeyType, pOper->m_SamplePoints[type]);
	pOper->m_pClip->RemoveKeys(pOper->m_KeyType);
	SingleKeyMap::const_iterator iter = pOper->m_KeyMaps[type].begin();
	for (;iter!=pOper->m_KeyMaps[type].end();++iter)
	{
		switch (pOper->m_KeyType)
		{
		case Nuclear::AbstractEffectClip::KT_ROTATION:
			pOper->m_pClip->AddRotationKey(iter->first, iter->second);
			break;
		case Nuclear::AbstractEffectClip::KT_ALPHA:
			pOper->m_pClip->AddAlphaKey(iter->first, iter->second);
			break;
		}
	}
	return true;
}

bool CActionList::SetClipsTwoKeys(int type)
{
	CActionSetClipsDoubleKeys* pOper = dynamic_cast<CActionSetClipsDoubleKeys*>(*m_Iter);
	if (!pOper)
		return false;
	DoubleKeyMap::const_iterator iter;
	for (size_t i=0;i<pOper->m_pClips.size();++i)
	{
		pOper->m_pClips[i]->SetToolsKeyPoints(pOper->m_KeyType, pOper->m_KeyPoints[type][i]);
		pOper->m_pClips[i]->SetToolsSamplePoints(pOper->m_KeyType, pOper->m_SamplePoints[type][i]);
		pOper->m_pClips[i]->RemoveKeys(pOper->m_KeyType);
		iter = pOper->m_KeyMaps[type][i].begin();
		for (;iter!=pOper->m_KeyMaps[type][i].end();++iter)
		{
			switch (pOper->m_KeyType)
			{
			case Nuclear::AbstractEffectClip::KT_POS:
				pOper->m_pClips[i]->AddPosKey(iter->first, iter->second);
				break;
			case Nuclear::AbstractEffectClip::KT_SCALE:
				pOper->m_pClips[i]->AddScaleKey(iter->first, iter->second);
				break;
			}
		}
	}
	return true;
}

bool CActionList::SetClipPlayModeAndTime(int type)
{
	CActionSetClipPlayModeAndTime* pOper = dynamic_cast<CActionSetClipPlayModeAndTime*>(*m_Iter);
	if (!pOper)
		return false;
	switch (pOper->m_Types[type])
	{
	case Nuclear::XPPM_LOOP:
		pOper->m_pEffect->SetLoopNum(-1);
		break;
	case Nuclear::XPPM_LOOPNUM:
		pOper->m_pEffect->SetLoopNum(pOper->m_nTimes[type]);
		break;
	case Nuclear::XPPM_TIME:
		pOper->m_pEffect->SetPlayTime(pOper->m_nTimes[type]);
		break;
	}
	return true;
}

bool CActionList::SetClipVertexColor(int type)
{
	CActionSetClipVertexColor* pOper = dynamic_cast<CActionSetClipVertexColor*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->m_pEffect->SetVertexColor(pOper->m_Colors[type]);
	return true;
}

bool CActionList::SetClipFps(int type)
{
	CActionSetClipFps* pOper = dynamic_cast<CActionSetClipFps*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->m_pEffect->SetFps(pOper->m_fFps[type]);
	return true;
}

bool CActionList::SetClipPosition(int type)
{
	CActionSetClipPos* pOper = dynamic_cast<CActionSetClipPos*>(*m_Iter);
	if (!pOper)
		return false;
	CActionSetClipPos::ClipPos::iterator iter = pOper->m_ClipPositions[type].begin();
	for (;iter!=pOper->m_ClipPositions[type].end();++iter)
	{
		iter->first->GetAnimation()->SetLocation(iter->second);
	}
	return true;
}

bool CActionList::DelAddClip(int type)
{
	CActionDelAddClip* pOper = dynamic_cast<CActionDelAddClip*>(*m_Iter);
	if (!pOper)
		return false;
	if (pOper->m_pClips[type] == NULL)
	{
		int otherType = type==CAction::BEFORE_ACTION?CAction::AFTER_ACTION:CAction::BEFORE_ACTION;
		m_pDoc->m_pToolsEffect->DelClip(pOper->m_pClips[otherType]);
	} else {
		m_pDoc->m_pToolsEffect->AddClip(pOper->m_pClips[type]);
	}
	m_pDoc->UnSelectedAllClip();
	return true;
}

bool CActionList::SetBoundsRect(int type)
{
	CActionSetBoundsRect* pOper = dynamic_cast<CActionSetBoundsRect*>(*m_Iter);
	if (!pOper)
		return false;
	m_pDoc->m_pToolsEffect->SetBoundsRect(pOper->m_Rects[type]);
	return true;
}

bool CActionList::SetBindType(int type)
{
	CActionSetBindType* pOper = dynamic_cast<CActionSetBindType*>(*m_Iter);
	if (!pOper)
		return false;
	m_pDoc->m_pToolsEffect->SetEffectBindType(pOper->m_BindTypes[type]);
	return true;
}

bool CActionList::SetFps(int type)
{
	CActionSetFps* pOper = dynamic_cast<CActionSetFps*>(*m_Iter);
	if (!pOper)
		return false;
	m_pDoc->m_pToolsEffect->SetFps(pOper->m_Fps[type]);
	return true;
}

bool CActionList::SetTotalFrame(int type)
{
	CActionSetTotalFrame* pOper = dynamic_cast<CActionSetTotalFrame*>(*m_Iter);
	if (!pOper)
		return false;
	m_pDoc->m_pToolsEffect->SetTotalFrame(pOper->m_Frames[type]);
	return true;
}

bool CActionList::SetDivideLayer(int type)
{
	CActionSetDivideLayer* pOper = dynamic_cast<CActionSetDivideLayer*>(*m_Iter);
	if (!pOper)
		return false;
	m_pDoc->m_pToolsEffect->SetDivideLayer(pOper->m_Layers[type]);
	return true;
}

bool CActionList::SetModifyClipAnimation(int type)
{
	CActionModifyClipAni* pOper = dynamic_cast<CActionModifyClipAni*>(*m_Iter);
	if (!pOper)
		return false;
	pOper->m_pClip->SetAnimation(pOper->m_pEffects[type]);
	return true;
}

bool CActionList::SetModifyLayer(int type)
{
	CActionModifyLayer* pOper = dynamic_cast<CActionModifyLayer*>(*m_Iter);
	if (!pOper)
		return false;
	m_pDoc->m_pToolsEffect->ModifyLayer(pOper->m_pClip, pOper->m_Layers[type]);
	return true;
}

bool CActionList::SetModifyStartEndFrame(int type)
{
	CActionModifyStartEndFrame* pOper = dynamic_cast<CActionModifyStartEndFrame*>(*m_Iter);
	if (!pOper)
		return false;
	m_pDoc->m_pToolsEffect->ModifyStartEndFrame(pOper->m_pClip, pOper->m_StartFrames[type], pOper->m_EndFrames[type]);
	return true;
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

bool CActionList::UndoOneAction()
{
	if (!CanUndo())
		return false;
	m_Iter--;
	return TodoAction(CAction::BEFORE_ACTION);
}

bool CActionList::TodoAction(int type)
{
	bool result = false;
	bool bNeedReplay = true;
	switch ((*m_Iter)->GetType())
	{
	case CAction::AT_MODIFY_START_END_FRAME:
		result = SetModifyStartEndFrame(type);
		break;
	case CAction::AT_MODIFY_LAYER:
		result = SetModifyLayer(type);
		bNeedReplay = false;
		break;
	case CAction::AT_MODIFY_CLIP_ANI:
		result = SetModifyClipAnimation(type);
		break;
	case CAction::AT_SET_DIVIDE_LAYER:
		result = SetDivideLayer(type);
		break;
	case CAction::AT_SET_TOTAL_FRAME:
		result = SetTotalFrame(type);
		break;
	case CAction::AT_SET_FPS:
		result = SetFps(type);
		break;
	case CAction::AT_SET_BIND_TYPE:
		result = SetBindType(type);
		break;
	case CAction::AT_SET_BOUNDS_RECT:
		result = SetBoundsRect(type);
		bNeedReplay = false;
		break;
	case CAction::AT_DEL_ADD_CLIP:
		result = DelAddClip(type);
		break;
	case CAction::AT_SET_CLIP_POS:
		result = SetClipPosition(type);
		bNeedReplay = false;
		break;
	case CAction::AT_SET_CLIP_FPS:
		result = SetClipFps(type);
		break;
	case CAction::AT_SET_CLIP_VERTEX_COLOR:
		result = SetClipVertexColor(type);
		bNeedReplay = false;
		break;
	case CAction::AT_SET_CLIP_PLAY_MODE_TIME:
		result = SetClipPlayModeAndTime(type);
		break;
	case CAction::AT_SET_CLIPS_DOUBLE_KEY:
		result = SetClipsTwoKeys(type);
		bNeedReplay = false;
		break;
	case CAction::AT_SET_CLIP_SINGLE_KEY:
		result = SetClipOneKeys(type);
		bNeedReplay = false;
		break;
	case CAction::AT_SET_CLIP_COLOR_KEY:
		result = SetClipColorKeys(type);
		bNeedReplay = false;
		break;
	default:
		break;
	}
	if (result)
	{
		m_pDoc->SetModifiedFlag();
		if (bNeedReplay)
		{
			m_pDoc->m_bNeedReplay = true;
		}
		m_pDoc->UpdateAllViews(NULL);
	}
	return result;
}

void CActionList::DoOneAction(CAction* pOper)
{
	if (m_Iter != m_ActionList.end())	
	{
		std::list<CAction*>::iterator iter = m_Iter;
		for (;iter!=m_ActionList.end();++iter)
		{
			switch ((*iter)->GetType())
			{
			case CAction::AT_DEL_ADD_CLIP:
				{
					CActionDelAddClip* pOper = dynamic_cast<CActionDelAddClip*>(*iter);
					if (pOper->m_pClips[CAction::AFTER_ACTION])
					{
						delete pOper->m_pClips[CAction::AFTER_ACTION];
					}
				}
				break;
			case CAction::AT_MODIFY_CLIP_ANI:
				{
					CActionModifyClipAni* pOper = dynamic_cast<CActionModifyClipAni*>(*iter);
					delete pOper->m_pEffects[CAction::AFTER_ACTION];
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

	if (m_ActionList.size() > MAX_LENGTH)	
	{
		CAction* front = m_ActionList.front();
		m_ActionList.pop_front();
		switch(front->GetType())
		{
		case CAction::AT_DEL_ADD_CLIP:
			{
				CActionDelAddClip* pOper = dynamic_cast<CActionDelAddClip*>(front);
				if (pOper->m_pClips[CAction::BEFORE_ACTION])
				{
					delete pOper->m_pClips[CAction::BEFORE_ACTION];
				}
			}
			break;
		case CAction::AT_MODIFY_CLIP_ANI:
			{
				CActionModifyClipAni* pOper = dynamic_cast<CActionModifyClipAni*>(front);
				delete pOper->m_pEffects[CAction::BEFORE_ACTION];
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

void CActionList::CleanAll()
{
	std::list<CAction*>::iterator iter;
	bool hadDo = true;
	for (iter=m_ActionList.begin();iter!=m_ActionList.end();++iter)
	{
		if (iter == m_Iter)
			hadDo = false;
		switch ((*iter)->GetType())
		{
		case CAction::AT_DEL_ADD_CLIP:
			{
				CActionDelAddClip* pOper = dynamic_cast<CActionDelAddClip*>(*iter);
				if (hadDo)
				{
					if (pOper->m_pClips[CAction::BEFORE_ACTION])
					{
						delete pOper->m_pClips[CAction::BEFORE_ACTION];
					}
				} else {
					if (pOper->m_pClips[CAction::AFTER_ACTION])
					{
						delete pOper->m_pClips[CAction::AFTER_ACTION];
					}
				}
			}
			break;
		case CAction::AT_MODIFY_CLIP_ANI:
			{
				CActionModifyClipAni* pOper = dynamic_cast<CActionModifyClipAni*>(*iter);
				if (hadDo)
				{
					delete pOper->m_pEffects[CAction::BEFORE_ACTION];
				} else {
					delete pOper->m_pEffects[CAction::AFTER_ACTION];
				}
			}
			break;
		default:
			break;
		}
		delete (*iter);
	}
	m_ActionList.clear();
}

CActionList::~CActionList()
{
	CleanAll();
}


CActionList::CActionList(CEffectEditorDoc* pDoc) : m_pDoc(pDoc)
{
	m_Iter = m_ActionList.end();
}

CAction::~CAction(void)
{
}

CAction::CAction(void)
{
}

