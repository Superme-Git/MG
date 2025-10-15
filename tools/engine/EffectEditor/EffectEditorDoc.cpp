

#include "stdafx.h"
#include "EffectEditor.h"
#include "EffectEditorDoc.h"
#include "EffectEditorView.h"
#include "Action.h"
#include "..\engine\effect\effectmanager.h"
#include "..\engine\effect\particleeffect.h"
#include "..\engine\common\xpmaths.h"
#include "..\engine\particlesystem\particlemanager.h"
#include "..\engine\effect\audioeffect.h"
#include "DlgSelNewFileType.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif



IMPLEMENT_DYNCREATE(CEffectEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CEffectEditorDoc, CDocument)
	ON_COMMAND(ID_CLIP_CENTER, &CEffectEditorDoc::OnClipCenter)
	ON_UPDATE_COMMAND_UI(ID_CLIP_CENTER, &CEffectEditorDoc::OnUpdateClipCenter)
	ON_COMMAND(ID_EDIT_UNDO, &CEffectEditorDoc::OnEditUndo)
	ON_UPDATE_COMMAND_UI(ID_EDIT_UNDO, &CEffectEditorDoc::OnUpdateEditUndo)
	ON_COMMAND(ID_EDIT_REDO, &CEffectEditorDoc::OnEditRedo)
	ON_UPDATE_COMMAND_UI(ID_EDIT_REDO, &CEffectEditorDoc::OnUpdateEditRedo)
	ON_COMMAND(ID_FILE_EXPORT, &CEffectEditorDoc::OnFileExport)
	ON_COMMAND(ID_REFRESH, &CEffectEditorDoc::OnRefresh)
	ON_COMMAND(ID_FILE_NEW, &CEffectEditorDoc::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CEffectEditorDoc::OnFileOpen)
END_MESSAGE_MAP()




void CEffectEditorDoc::ResetEffectForBind()
{

	if (m_EffectFileType != EFT_SEBIND)
		return;
	m_pSelectedEffectClips.clear();
	if (m_pSprite && m_pSprite->GetFrameCount() > 0)
	{

		Nuclear::EffectClip *pClip = m_ToolsSebind.GetActionClip(m_pSprite->GetDirection(), m_pSprite->GetActionName());
		if (!pClip)
		{
			pClip = m_ToolsSebind.AddClip(m_pSprite->GetDirection(), m_pSprite->GetActionName(), m_pSprite->GetFrameCount());
			if (pClip)
				SetModifiedFlag();
		}
		if (pClip)
		{
			m_pSelectedEffectClips.insert(pClip);
		}
	}
}

void CEffectEditorDoc::Tick(DWORD tickTime) { 
	XPASSERT(m_pToolsEffect != NULL);
	if (m_pSprite)
	{
		if (m_pToolsEffect->GetPlayState() == Nuclear::XPES_PLAYING)
		{
			m_pSprite->Tick(tickTime);
		}
	}
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		m_pToolsEffect->Update(tickTime); 
		break;
	case EFT_SEBIND:
		m_ToolsSebind.Tick(m_pSprite, tickTime);
		break;
	}	
}

const Nuclear::CRECT& CEffectEditorDoc::GetRelativeBouningBox() const { return m_pToolsEffect->GetRelBouningBox(); }

bool CEffectEditorDoc::SetPlayingFrame(int frame) 
{
	if (frame < 0)
		frame = 0;
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		return m_pToolsEffect->SetPlayingFrame(frame);
		break;
	case EFT_SEBIND:
		if (m_pSprite)
		{
			m_pSprite->SetFrame(frame);
			return true;
		}
		break;
	}
	return false;
}
Nuclear::XPEffectState CEffectEditorDoc::GetPlayState() const 
{
	if (m_pToolsEffect)
		return m_pToolsEffect->GetPlayState(); 
	return Nuclear::XPES_EMPTY;
}

float CEffectEditorDoc::GetPlayingFrame() const
{ 
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		return m_pToolsEffect->GetPlayingFrame();
		break;
	case EFT_SEBIND:
		if (m_pSprite)
		{
			return m_pSprite->GetFloatFrame();
		}
		break;
	}
	return 0;
}

Nuclear::XPEFFECT_BIND_TYPE CEffectEditorDoc::GetBindType() const
{
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		return m_pToolsEffect->GetEffectBindType(); 
		break;
	case EFT_SEBIND:
		break;
	}
	return Nuclear::XPEBT_NULL;
}

float CEffectEditorDoc::GetFPS() const
{ 
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		return m_pToolsEffect->GetFPS(); 
		break;
	case EFT_SEBIND:
		break;
	}
	return 0.0f;
}

void CEffectEditorDoc::GetClipListByLayer (int layer, Nuclear::AbstractEffectClip_LIST& clipList) 
{
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		m_pToolsEffect->GetClipList(layer, clipList);
		break;
	case EFT_SEBIND:
		clipList.clear();
		if (layer == 0 && m_pSprite)
		{
			Nuclear::EffectClip *pClip = m_ToolsSebind.GetActionClip(m_pSprite->GetDirection(), m_pSprite->GetActionName());
			Nuclear::Effect *pEffect = m_ToolsSebind.GetEffect();
			if (pClip && pEffect)
			{
				pClip->SetAnimation(pEffect);
				clipList.push_back(pClip);
			}
		}
		break;
	}
}

int CEffectEditorDoc::GetDivideLayer() const 
{
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		return m_pToolsEffect->GetDivideLayer(); 
		break;
	case EFT_SEBIND:
		break;
	}
	return -1;

}

int CEffectEditorDoc::GetTotalFrames() const 
{ 
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		return m_pToolsEffect->GetTotalFrames();
		break;
	case EFT_SEBIND:
		if (m_pSprite)
		{
			return m_pSprite->GetFrameCount();
		}
		break;
	}
	return 0;
}

bool CEffectEditorDoc::Pause() {
	return m_pToolsEffect->Pause(); 
}

bool CEffectEditorDoc::Play()
{
	if (!m_pToolsEffect->Play())
	{
		return m_pToolsEffect->Resume();
	}
	return true;
}

bool CEffectEditorDoc::Stop() 
{ 
	if (m_pToolsEffect->Stop())
	{
		m_pSprite->SetFrame(0);
		return true;
	}
	return false;
}

BOOL CEffectEditorDoc::DoSave(LPCTSTR lpszPathName, BOOL bReplace)
{
	if (lpszPathName == NULL)
	{
		WCHAR filestr[256];
		CString defaultExt;
		switch (m_EffectFileType)
		{
		case EFT_EFFECT:
			swprintf(filestr, L"特效文件(*%s)|*%s||", Effect_ExtName, Effect_ExtName);
			defaultExt = Effect_ExtName;
			break;
		case EFT_SEBIND:
			swprintf(filestr, L"绑定文件(*%s)|*%s||", Bind_ExtName, Bind_ExtName);
			defaultExt = Bind_ExtName;
			break;
		}
		CFileDialog dlg(FALSE, defaultExt, m_strTitle, OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, filestr);
		if (dlg.DoModal() == IDOK)
		{
			return CDocument::DoSave(dlg.GetPathName(), bReplace);
		} else {
			return FALSE;
		}

	}
	else
	{
		return CDocument::DoSave(lpszPathName, bReplace);
	}
}

void CEffectEditorDoc::OnFileOpen()
{
	WCHAR extFilter[256];
	swprintf(extFilter, L"特效文件 (*%s)|*%s|绑定文件 (*%s)|*%s||", Effect_ExtName, Effect_ExtName, Bind_ExtName, Bind_ExtName);
	CFileDialog dlg(TRUE, NULL, NULL, OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, extFilter);
	if (dlg.DoModal() == IDOK)
	{
		theApp.OpenDocumentFile(dlg.GetPathName());
	}
}

void CEffectEditorDoc::OnFileNew()
{
	CDlgSelNewFileType dlg;
	if (dlg.DoModal() == IDOK)
	{
		m_EffectFileType = dlg.type;
		POSITION pos = theApp.GetFirstDocTemplatePosition();
		CDocTemplate* pTemplate = theApp.GetNextDocTemplate(pos);
		if (pTemplate)
		{
			pTemplate->OpenDocumentFile(NULL);
		}
	}
}

void CEffectEditorDoc::OnRefresh()
{
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		if (m_pToolsEffect)
		{
			Nuclear::Location loc = m_pToolsEffect->GetLocation();
			Nuclear::XPDIRECTION dir = m_pToolsEffect->GetDirection();
			m_pToolsEffect->Stop();
			XMLIO::CFileWriter fw;
			XMLIO::CONode oRoot;
			fw.GetRootNode(oRoot);
			if (m_pToolsEffect->SaveEffect(oRoot, NULL, Nuclear::EFFECT_IO_EDIT) == 0)
			{
				delete m_pToolsEffect;
				dynamic_cast<Nuclear::ParticleManager*>(theApp.GetEngine()->GetRenderer()->GetParticleManager())->ClearAllResouce();
				theApp.GetEngine()->GetAniManager()->Destroy();
				if (m_pActionList)
					delete m_pActionList;
				m_pActionList = new CActionList(this);
				m_pToolsEffect = new CToolsEffect(theApp.GetEngine());
				XMLIO::CFileReader fr;
				fw.GetAsReader(fr);
				XMLIO::CINode iRoot;
				fr.GetRootNode(iRoot);
				if (m_pToolsEffect->LoadEffect(iRoot, Nuclear::EFFECT_IO_EDIT) == Nuclear::GEffect::EC_S_OK)
				{
					m_pToolsEffect->SetLocation(loc);
					m_pToolsEffect->SetDirection(dir);
					m_pToolsEffect->SetLoopNum(-1);
					m_pToolsEffect->AssureResource(false);
					m_pToolsEffect->Stop();
					m_pSelectedEffectClips.clear();
					m_bNeedReplay = true;
					UpdateAllViews(NULL);
					return;
				}
			}
		}
		break;
	case EFT_SEBIND:
		if (m_pToolsEffect)
		{			
			Nuclear::Effect* pEffect = m_ToolsSebind.GetEffect();
			if (pEffect)
			{
				pEffect->Stop();
				m_pToolsEffect->Stop();
				Nuclear::Effect* pAni = theApp.GetEngine()->GetEffectManager()->CreateEffect(pEffect->GetName().c_str(), false);
				if (pAni != NULL)
				{			
					switch (pAni->GetType())
					{
					case Nuclear::XPET_ANI:
					case Nuclear::XPET_PARTICLE:
						pAni->SetVertexColor(pEffect->GetVertexColor());
						break;
					case Nuclear::XPET_GEFFECT:
					case Nuclear::XPET_AUDIO:
						break;
					}
					pAni->AssureResource(false);
					pAni->SetLoopNum(-1);
					SetEffectForBind(pAni);
					UpdateAllViews(NULL);
					pAni->Stop();
					m_pSelectedEffectClips.clear();
					m_bNeedReplay = true;
					return;
				}
			}
		}
		break;
	}


	AfxMessageBox(L"刷新失败\n");
}

void CEffectEditorDoc::SetSpriteDirection(Nuclear::XPDIRECTION dir)
{
	assert(m_pSprite != NULL);
	assert(m_pToolsEffect != NULL);
	m_pSprite->SetDirection(dir);
	m_pToolsEffect->SetDirection(dir);
	ResetEffectForBind();
}
Nuclear::AbstractEffectClip* CEffectEditorDoc::AddEffectClip(int layer, int startFrame, int endFrame)
{
	if (m_EffectFileType == EFT_SEBIND)
		return false;
	CActionDelAddClip* pOper = new CActionDelAddClip();
	pOper->m_pClips[CAction::BEFORE_ACTION] = NULL;
	pOper->m_pClips[CAction::AFTER_ACTION] = m_pToolsEffect->AddClip(layer, startFrame, endFrame);
	m_pActionList->DoOneAction(pOper);
	m_bNeedReplay = true;
	return pOper->m_pClips[CAction::AFTER_ACTION];
}

bool CEffectEditorDoc::DelEffectClip(Nuclear::AbstractEffectClip* clip)
{
	if (m_EffectFileType == EFT_SEBIND)
		return false;
	CActionDelAddClip* pOper = new CActionDelAddClip();
	pOper->m_pClips[CAction::BEFORE_ACTION] = clip;
	if (m_pToolsEffect->DelClip(clip))
	{
		pOper->m_pClips[CAction::AFTER_ACTION] = NULL;
		m_pActionList->DoOneAction(pOper);
		m_bNeedReplay = true;
		return true;
	} else {
		delete (pOper);
		return false;
	}
}

void CEffectEditorDoc::SetBoundsRect(const Nuclear::CRECT &rt)
{
	CActionSetBoundsRect* pOper = new CActionSetBoundsRect();
	pOper->m_Rects[CAction::BEFORE_ACTION] = m_pToolsEffect->GetRelBouningBox();
	m_pToolsEffect->SetBoundsRect(rt);
	pOper->m_Rects[CAction::AFTER_ACTION] = m_pToolsEffect->GetRelBouningBox();
	m_pActionList->DoOneAction(pOper);
}

void CEffectEditorDoc::SetFPS(float fps)
{
	if (m_EffectFileType == EFT_SEBIND)
		return;
	CActionSetFps* pOper = new CActionSetFps();
	pOper->m_Fps[CAction::BEFORE_ACTION] = m_pToolsEffect->GetFPS();
	m_pToolsEffect->SetFps(fps);
	pOper->m_Fps[CAction::AFTER_ACTION] = m_pToolsEffect->GetFPS();
	m_pActionList->DoOneAction(pOper);
	m_bNeedReplay = true;
}

void CEffectEditorDoc::SetBindType(Nuclear::XPEFFECT_BIND_TYPE t)
{
	if (m_EffectFileType == EFT_SEBIND)
		return;
	CActionSetBindType* pOper = new CActionSetBindType();
	pOper->m_BindTypes[CAction::BEFORE_ACTION] = m_pToolsEffect->GetEffectBindType();
	m_pToolsEffect->SetEffectBindType(t);
	pOper->m_BindTypes[CAction::AFTER_ACTION] = m_pToolsEffect->GetEffectBindType();
	m_pActionList->DoOneAction(pOper);
	m_bNeedReplay = false;
}

bool CEffectEditorDoc::SetTotalFrameNum(int frames)
{
	if (m_EffectFileType == EFT_SEBIND)
		return false;
	CActionSetTotalFrame* pOper = new CActionSetTotalFrame();
	pOper->m_Frames[CAction::BEFORE_ACTION] = m_pToolsEffect->GetTotalFrames();
	if (m_pToolsEffect->SetTotalFrame(frames))
	{
		pOper->m_Frames[CAction::AFTER_ACTION] = m_pToolsEffect->GetTotalFrames();
		m_pActionList->DoOneAction(pOper);
		m_bNeedReplay = true;
		return true;
	} else {
		delete (pOper);
		return false;
	}
}

bool CEffectEditorDoc::SetDivideLayer(int layer)
{
	if (m_EffectFileType == EFT_SEBIND)
		return false;
	CActionSetDivideLayer* pOper = new CActionSetDivideLayer();
	pOper->m_Layers[CAction::BEFORE_ACTION] = m_pToolsEffect->GetDivideLayer();
	if (m_pToolsEffect->SetDivideLayer(layer))
	{
		pOper->m_Layers[CAction::AFTER_ACTION] = m_pToolsEffect->GetDivideLayer();
		m_pActionList->DoOneAction(pOper);
		return true;
	} else {
		delete (pOper);
		return false;
	}
}

bool CEffectEditorDoc::ModifyLayer(Nuclear::AbstractEffectClip* clip, int layer)
{
	if (m_EffectFileType == EFT_SEBIND)
		return false;
	CActionModifyLayer* pOper = new CActionModifyLayer();
	pOper->m_pClip = clip;
	pOper->m_Layers[CAction::BEFORE_ACTION] = clip->GetLayer();
	if (m_pToolsEffect->ModifyLayer(clip, layer))
	{
		pOper->m_Layers[CAction::AFTER_ACTION] = clip->GetLayer();
		m_pActionList->DoOneAction(pOper);
		return true;
	} else {
		delete (pOper);
		return false;
	}
}

bool CEffectEditorDoc::ModifyStartEndFrame(Nuclear::AbstractEffectClip* clip, int startFrame, int endFrame, CView* pSender)
{
	if (m_EffectFileType == EFT_SEBIND)
		return false;
	CActionModifyStartEndFrame* pOper = new CActionModifyStartEndFrame();
	pOper->m_pClip = clip;
	pOper->m_EndFrames[CAction::BEFORE_ACTION] = clip->GetEndFrame();
	pOper->m_StartFrames[CAction::BEFORE_ACTION] = clip->GetStartFrame();
	if (m_pToolsEffect->ModifyStartEndFrame(clip, startFrame, endFrame))
	{
		pOper->m_EndFrames[CAction::AFTER_ACTION] = clip->GetEndFrame();
		pOper->m_StartFrames[CAction::AFTER_ACTION] = clip->GetStartFrame();
		m_pActionList->DoOneAction(pOper);
		m_bNeedReplay = true;
		UpdateAllViews(pSender);
		return true;
	} else {
		delete (pOper);
		return false;
	}
}

void CEffectEditorDoc::Draw(Nuclear::Renderer* pRenderer) 
{
	XPASSERT(m_pToolsEffect != NULL);
	switch(m_EffectFileType)
	{
	case EFT_EFFECT:
		m_pToolsEffect->RenderBack(NULL);
		if (m_bDrawRole && m_pToolsEffect->GetPlayState() != Nuclear::XPES_STOP)
		{
			m_pSprite->Render();
		}
		m_pToolsEffect->RenderFront(NULL);
		if (m_bDrawClipCenter)
		{
			Nuclear::Effect *pAni;
			EffectClipSet::iterator iter = m_pSelectedEffectClips.begin();
			Nuclear::FPOINT pos;

			for (;iter!=m_pSelectedEffectClips.end();++iter)
			{
				pAni = (*iter)->GetAnimation();
				if (pAni)
				{
					if (pAni->GetType() != Nuclear::XPET_AUDIO)
					{
						pos = pAni->GetLocation().ToFPOINT();
						pos += pAni->GetTranslation();
						pRenderer->DrawLine(pos.x - 5, pos.y, pos.x + 5, pos.y, 0xFFFF0000);
						pRenderer->DrawLine(pos.x, pos.y - 5, pos.x, pos.y + 5, 0xFFFF0000);
					}
				}
			}
		}
		break;
	case EFT_SEBIND:
		if (m_pSprite && m_pToolsEffect->GetPlayState() != Nuclear::XPES_STOP)
		{
			Nuclear::EffectClip *pClip = m_ToolsSebind.GetActionClip(m_pToolsEffect->GetDirection(), m_pSprite->GetActionName());
			Nuclear::Effect* pEffect = NULL;
			if (pClip)
			{
				pEffect = m_ToolsSebind.GetEffect();
			}
			if (pEffect) pEffect->RenderBack(NULL);
			if (m_bDrawRole) m_pSprite->Render();
			if (pEffect)
			{
				pEffect->RenderFront(NULL);
				if (m_bDrawClipCenter)
				{
					Nuclear::FPOINT pos = pEffect->GetLocation();
					pos += pEffect->GetTranslation();
					pRenderer->DrawLine(pos.x - 5, pos.y, pos.x + 5, pos.y, 0xFFFF0000);
					pRenderer->DrawLine(pos.x, pos.y - 5, pos.x, pos.y + 5, 0xFFFF0000);
				}
			}
		}
		break;
	}


	NotifySet::iterator iter = m_NotifySet.begin();
	NotifySet::iterator iterEnd = m_NotifySet.end();
	for (;iter!=iterEnd;++iter)
	{
		(*iter)->OnRender();
	}
}


bool CEffectEditorDoc::GetClipStartEndFrame(const Nuclear::AbstractEffectClip* pClip, 
											eAnimationPlayType& playType, 
											int& nStartFrame, int& nEndFrame) const
{
	if (m_EffectFileType == EFT_SEBIND)
	{
		playType = APT_NORMAL;
		nStartFrame = 0;
		nEndFrame = pClip->GetEndFrame() + 1;
		return true;
	}
	const Nuclear::Effect* pAni = pClip->GetAnimation();
	if (pAni == NULL)
		return false;
	nStartFrame = pClip->GetStartFrame();
	if (pClip->GetEndFrame() == -1)
	{
		XPASSERT(m_EffectFileType == EFT_EFFECT);
		playType = APT_SPEC_NORMAL;
		nEndFrame = nStartFrame + 1;
		float fps = GetFPS();
		int tmpTime;
		switch (pAni->GetType())
		{
		case Nuclear::XPET_PARTICLE:
		case Nuclear::XPET_GEFFECT:
		case Nuclear::XPET_ANI:
			tmpTime = pAni->GetPlayTime();
			if (tmpTime > 0)
			{
				nEndFrame += static_cast<int>(tmpTime / 1000.0f * fps) - 1;
			} else {
				playType = APT_SPEC_LOOP;
				nEndFrame = nStartFrame + 5;
			}
			break;
		default:
			nEndFrame = nStartFrame + 5;
			break;
		}
	} else {
		nEndFrame = pClip->GetEndFrame() + 1;
		playType = APT_NORMAL;
	}
	return true;
}

bool CEffectEditorDoc::SetScaleForSelectedClip(const Nuclear::FPOINT& scale)
{
	if (m_pSelectedEffectClips.size() != 1)
		return false;
	Nuclear::AbstractEffectClip* pClip = *m_pSelectedEffectClips.begin();
	Nuclear::Effect* pAni;
	int frame;
	int index;	
	Nuclear::FPOINT scaleVet(scale.x, scale.y);
	Nuclear::FPOINT v;

	CActionSetClipsDoubleKeys* pOper = new CActionSetClipsDoubleKeys();

	int endFrame, startFrame;
	eAnimationPlayType playType;

	pOper->m_KeyType = Nuclear::AbstractEffectClip::KT_SCALE;

	int firstFrame, lastFrame;
	pOper->AddAnContaints(CAction::BEFORE_ACTION, pClip, firstFrame, lastFrame);


	GetClipStartEndFrame(pClip, playType, startFrame, endFrame);
	if (playType == APT_SPEC_LOOP)
	{
		endFrame = GetTotalFrames();
	}
	frame = static_cast<int>(GetPlayingFrame()) - startFrame;
	pAni = pClip->GetAnimation();

	if ((index = pClip->GetScaleKeyValue(frame, v)) >= 0)
	{
		pClip->SetScaleKeyValue(index, scaleVet);
	} else {
		pClip->AddScaleKey(frame, scaleVet);
	}
	Nuclear::AbstractEffectClip::KP_VECTOR kps = pClip->GetToolsKeyPoints(pOper->m_KeyType);
	CheckTwoKeyPointArray(kps, scaleVet, static_cast<float>(endFrame - startFrame));
	InsertTwoKeyPointArray(kps, scaleVet, frame, firstFrame, lastFrame);

	pClip->SetToolsKeyPoints(pOper->m_KeyType, kps);
	pClip->ClearSamplePoints(pOper->m_KeyType);


	pOper->AddAnContaints(CAction::AFTER_ACTION, pClip, firstFrame, lastFrame);
	m_pActionList->DoOneAction(pOper);
	return true;
}

bool CEffectEditorDoc::SetRotationForSelectedClip(float angle)
{
	if (m_pSelectedEffectClips.size() != 1)
		return false;
	Nuclear::AbstractEffectClip* pClip = *m_pSelectedEffectClips.begin();
	int frame;
	int index;	

	CActionSetClipSingleKeys* pOper = new CActionSetClipSingleKeys();
	int endFrame, startFrame;
	eAnimationPlayType playType;
	float v;


	pOper->m_KeyType = Nuclear::AbstractEffectClip::KT_ROTATION;
	pOper->m_pClip = pClip;

	pOper->m_KeyPoints[CAction::BEFORE_ACTION] = (pClip->GetToolsKeyPoints(pOper->m_KeyType));
	pOper->m_SamplePoints[CAction::BEFORE_ACTION] = (pClip->GetToolsSamplePoints(pOper->m_KeyType));
	SingleKeyMap& keyMap = pOper->m_KeyMaps[CAction::BEFORE_ACTION];
	int firstFrame = INT_MAX, lastFrame = -1;
	for (unsigned int i=0;i<pClip->GetKeyCount(pOper->m_KeyType);++i)
	{
		pClip->GetRotationKeyValue(i, frame, v);
		keyMap[frame] = v;
		if (i == 0)
		{
			firstFrame = frame;
		}
		lastFrame = frame;
	}


	GetClipStartEndFrame(pClip, playType, startFrame, endFrame);
	if (playType == APT_SPEC_LOOP)
	{
		endFrame = GetTotalFrames();
	}
	frame = static_cast<int>(GetPlayingFrame()) - startFrame;

	if ((index = pClip->GetRotationKeyValue(frame, v)) >= 0)
	{
		pClip->SetRotationKeyValue(index, angle);
	} else {
		pClip->AddRotationKey(frame, angle);
	}
	Nuclear::AbstractEffectClip::KP_VECTOR kps = pClip->GetToolsKeyPoints(pOper->m_KeyType);
	CheckOneKeyPointArray(kps, angle, static_cast<float>(endFrame - startFrame));
	InsertOneKeyPointArray(kps, angle, frame, firstFrame, lastFrame);
	pClip->SetToolsKeyPoints(pOper->m_KeyType, kps);
	pClip->ClearSamplePoints(pOper->m_KeyType);

	pOper->m_KeyPoints[CAction::AFTER_ACTION] = pClip->GetToolsKeyPoints(pOper->m_KeyType);
	pOper->m_SamplePoints[CAction::AFTER_ACTION] = pClip->GetToolsSamplePoints(pOper->m_KeyType);
	SingleKeyMap& keyMapAO = pOper->m_KeyMaps[CAction::AFTER_ACTION];
	for (unsigned int i=0;i<pClip->GetKeyCount(pOper->m_KeyType);++i)
	{
		pClip->GetRotationKeyValue(i, frame, v);
		keyMapAO[frame] = v;
	}

	m_pActionList->DoOneAction(pOper);
	return true;
}

bool CEffectEditorDoc::ModifyAdjPosForSelectedClip(CPoint adjPoint)
{
	EffectClipSet::iterator iter = m_pSelectedEffectClips.begin();
	Nuclear::AbstractEffectClip* pClip;
	Nuclear::Effect* pAni;
	int frame;
	int index;	
	Nuclear::FPOINT trans;
	Nuclear::FPOINT pos;
	Nuclear::FPOINT v;

	CActionSetClipsDoubleKeys* pOper = new CActionSetClipsDoubleKeys();

	int endFrame, startFrame;
	eAnimationPlayType playType;

	pOper->m_KeyType = Nuclear::AbstractEffectClip::KT_POS;
	for (;iter!=m_pSelectedEffectClips.end();++iter)
	{
		pClip = *iter;

		int firstFrame, lastFrame;
		pOper->AddAnContaints(CAction::BEFORE_ACTION, pClip, firstFrame, lastFrame);


		GetClipStartEndFrame(pClip, playType, startFrame, endFrame);
		if (playType == APT_SPEC_LOOP)
		{
			endFrame = GetTotalFrames();
		}
		frame = static_cast<int>(GetPlayingFrame()) - startFrame;
		pAni = m_EffectFileType==EFT_EFFECT?pClip->GetAnimation():m_ToolsSebind.GetEffect();
		trans = pAni->GetTranslation() - m_pToolsEffect->GetLocation();
		pos.x = adjPoint.x + trans.x;
		pos.y = adjPoint.y + trans.y;
		if ((index = pClip->GetPosKeyValue(frame, v)) >= 0)
		{
			pClip->SetPosKeyValue(index, pos);
		} else {
			pClip->AddPosKey(frame, pos);
		}
		Nuclear::AbstractEffectClip::KP_VECTOR kps = pClip->GetToolsKeyPoints(pOper->m_KeyType);
		CheckTwoKeyPointArray(kps, pos, static_cast<float>(endFrame - startFrame));
		InsertTwoKeyPointArray(kps, pos, frame, firstFrame, lastFrame);

		pClip->SetToolsKeyPoints(pOper->m_KeyType, kps);
		pClip->ClearSamplePoints(pOper->m_KeyType);


		pOper->AddAnContaints(CAction::AFTER_ACTION, pClip, firstFrame, lastFrame);
	}
	m_pActionList->DoOneAction(pOper);
	return true;
}

bool CEffectEditorDoc::SetFrameDivideLayer(int layer)
{
	if (m_pSelectedEffectClips.size() != 1)
		return false;
	XPASSERT(m_EffectFileType == EFT_SEBIND);
	Nuclear::AbstractEffectClip* pClip = *m_pSelectedEffectClips.begin();
	int frame;
	int index;

	CActionSetClipSingleKeys* pOper = new CActionSetClipSingleKeys();
	int endFrame, startFrame;
	eAnimationPlayType playType;
	float v;


	pOper->m_KeyType = Nuclear::AbstractEffectClip::KT_DIVIDE;
	pOper->m_pClip = pClip;

	pOper->m_KeyPoints[CAction::BEFORE_ACTION] = (pClip->GetToolsKeyPoints(pOper->m_KeyType));
	pOper->m_SamplePoints[CAction::BEFORE_ACTION] = (pClip->GetToolsSamplePoints(pOper->m_KeyType));
	SingleKeyMap& keyMap = pOper->m_KeyMaps[CAction::BEFORE_ACTION];
	int firstFrame = INT_MAX, lastFrame = -1;
	for (unsigned int i=0;i<pClip->GetKeyCount(pOper->m_KeyType);++i)
	{
		pClip->GetDivideKeyValue(i, frame, v);
		keyMap[frame] = v;
		if (i == 0)
		{
			firstFrame = frame;
		}
		lastFrame = frame;
	}


	GetClipStartEndFrame(pClip, playType, startFrame, endFrame);
	frame = static_cast<int>(GetPlayingFrame()) - startFrame;

	if ((index = pClip->GetDivideKeyValue(frame, v)) >= 0)
	{
		pClip->SetDivideKeyValue(index, static_cast<float>(layer));
	} else {
		pClip->AddDivideKey(frame, static_cast<float>(layer));
	}
	Nuclear::AbstractEffectClip::KP_VECTOR kps = pClip->GetToolsKeyPoints(pOper->m_KeyType);
	CheckOneKeyPointArray(kps, static_cast<float>(layer), static_cast<float>(endFrame - startFrame));
	InsertOneKeyPointArray(kps, static_cast<float>(layer), frame, firstFrame, lastFrame);
	pClip->SetToolsKeyPoints(pOper->m_KeyType, kps);
	pClip->ClearSamplePoints(pOper->m_KeyType);

	pOper->m_KeyPoints[CAction::AFTER_ACTION] = pClip->GetToolsKeyPoints(pOper->m_KeyType);
	pOper->m_SamplePoints[CAction::AFTER_ACTION] = pClip->GetToolsSamplePoints(pOper->m_KeyType);
	SingleKeyMap& keyMapAO = pOper->m_KeyMaps[CAction::AFTER_ACTION];
	for (unsigned int i=0;i<pClip->GetKeyCount(pOper->m_KeyType);++i)
	{
		pClip->GetDivideKeyValue(i, frame, v);
		keyMapAO[frame] = v;
	}

	m_pActionList->DoOneAction(pOper);
	return true;
}

bool CEffectEditorDoc::SetSelectedClipColors(const ColorKeyMap& keyMap)
{
	if (m_pSelectedEffectClips.size() == 1)
	{
		Nuclear::AbstractEffectClip* pClip = *m_pSelectedEffectClips.begin();
		CActionSetClipColorKeys* pOper = new CActionSetClipColorKeys();
		pOper->m_pClip = pClip;
		int i, frame, count;
		Nuclear::XPCOLOR v;
		count = pClip->GetKeyCount(Nuclear::AbstractEffectClip::KT_COLOR);
		for (i=0;i<count;++i)
		{
			pClip->GetColorKeyValue(i, frame, v);
			pOper->m_KeyMaps[CAction::BEFORE_ACTION][frame] = v;
		}
		pOper->m_KeyMaps[CAction::AFTER_ACTION] = keyMap;
		m_pActionList->DoOneAction(pOper);
		pClip->RemoveKeys(Nuclear::AbstractEffectClip::KT_COLOR);
		ColorKeyMap::const_iterator iter = keyMap.begin();
		for (;iter!=keyMap.end();++iter)
		{
			pClip->AddColorKey(iter->first, iter->second);
		}
		return true;
	}
	return false;
}

bool CEffectEditorDoc::SetSelectedClipSingleKeys(Nuclear::AbstractEffectClip::KeyType kt, 
												 const Nuclear::AbstractEffectClip::KP_VECTOR& vKP, 
												 const Nuclear::AbstractEffectClip::SP_VECTOR& vSP, 
												 const SingleKeyMap& keyMap)
{
	if (m_pSelectedEffectClips.size() == 1)
	{
		Nuclear::AbstractEffectClip* pClip = *m_pSelectedEffectClips.begin();
		CActionSetClipSingleKeys* pOper = new CActionSetClipSingleKeys();
		pOper->m_KeyType = kt;
		pOper->m_KeyPoints[CAction::BEFORE_ACTION] = pClip->GetToolsKeyPoints(kt);
		pOper->m_SamplePoints[CAction::BEFORE_ACTION] = pClip->GetToolsSamplePoints(kt);
		pOper->m_KeyPoints[CAction::AFTER_ACTION] = vKP;
		pOper->m_SamplePoints[CAction::AFTER_ACTION] = vSP;
		pOper->m_pClip = pClip;
		SetSelectedEffectClipsKeys(kt, vKP, vSP);
		int i, count, frame;
		float v;
		count = pClip->GetKeyCount(kt);
		for (i=0;i<count;++i)
		{
			switch (kt)
			{
			case Nuclear::AbstractEffectClip::KT_ROTATION:
				pClip->GetRotationKeyValue(i, frame, v);
				break;
			case Nuclear::AbstractEffectClip::KT_ALPHA:
				pClip->GetAlphaKeyValue(i, frame, v);
				break;
			case Nuclear::AbstractEffectClip::KT_DIVIDE:
				pClip->GetDivideKeyValue(i, frame, v);
			}
			pOper->m_KeyMaps[CAction::BEFORE_ACTION][frame] = v;
		}
		pOper->m_KeyMaps[CAction::AFTER_ACTION] = keyMap;
		m_pActionList->DoOneAction(pOper);
		pClip->RemoveKeys(kt);
		SingleKeyMap::const_iterator iter = keyMap.begin();
		for (;iter!=keyMap.end();++iter)
		{
			switch (kt)
			{
			case Nuclear::AbstractEffectClip::KT_ROTATION:
				pClip->AddRotationKey(iter->first, iter->second);
				break;
			case Nuclear::AbstractEffectClip::KT_ALPHA:
				pClip->AddAlphaKey(iter->first, iter->second);
				break;
			case Nuclear::AbstractEffectClip::KT_DIVIDE:
				pClip->AddDivideKey(iter->first, iter->second);
			}
		}
		return true;
	}
	return false;
}

bool CEffectEditorDoc::SetSelectedClipDoubleKeys(Nuclear::AbstractEffectClip::KeyType kt, 
												 const Nuclear::AbstractEffectClip::KP_VECTOR& vKP, 
												 const Nuclear::AbstractEffectClip::SP_VECTOR& vSP, 
												 const DoubleKeyMap& keyMap)
{
	if (m_pSelectedEffectClips.size() == 1)
	{
		Nuclear::AbstractEffectClip* pClip = *m_pSelectedEffectClips.begin();
		CActionSetClipsDoubleKeys* pOper = new CActionSetClipsDoubleKeys();
		pOper->m_KeyType = kt;
		pOper->m_KeyPoints[CAction::BEFORE_ACTION].push_back(pClip->GetToolsKeyPoints(kt));
		pOper->m_SamplePoints[CAction::BEFORE_ACTION].push_back(pClip->GetToolsSamplePoints(kt));
		pOper->m_KeyPoints[CAction::AFTER_ACTION].push_back(vKP);
		pOper->m_SamplePoints[CAction::AFTER_ACTION].push_back(vSP);
		pOper->m_pClips.push_back(pClip);
		DoubleKeyMap tmpMap;
		pOper->m_KeyMaps[CAction::BEFORE_ACTION].push_back(tmpMap);
		SetSelectedEffectClipsKeys(kt, vKP, vSP);
		int i, count, frame;
		Nuclear::FPOINT v;
		count = pClip->GetKeyCount(kt);
		for (i=0;i<count;++i)
		{
			switch (kt)
			{
			case Nuclear::AbstractEffectClip::KT_POS:
				pClip->GetPosKeyValue(i, frame, v);
				break;
			case Nuclear::AbstractEffectClip::KT_SCALE:
				pClip->GetScaleKeyValue(i, frame, v);
				break;
			}
			pOper->m_KeyMaps[CAction::BEFORE_ACTION][0][frame] = v;
		}
		pOper->m_KeyMaps[CAction::AFTER_ACTION].push_back(keyMap);
		m_pActionList->DoOneAction(pOper);
		pClip->RemoveKeys(kt);
		DoubleKeyMap::const_iterator iter = keyMap.begin();
		for (;iter!=keyMap.end();++iter)
		{
			switch (kt)
			{
			case Nuclear::AbstractEffectClip::KT_POS:
				pClip->AddPosKey(iter->first, iter->second);
				break;
			case Nuclear::AbstractEffectClip::KT_SCALE:
				pClip->AddScaleKey(iter->first, iter->second);
				break;
			}
		}
		return true;
	}
	return false;
}

bool CEffectEditorDoc::SetSelectedAudioSoundProperty(int nID, unsigned char value)
{
	if (nID < 0) return false;
	if (nID >= Nuclear::AudioEffect::SOUND_PROP_COUNT) return false;
	if (m_pSelectedEffectClips.size() == 1)
	{
		Nuclear::AbstractEffectClip* pClip = *(m_pSelectedEffectClips.begin());
		Nuclear::AudioEffect* pAni = dynamic_cast<Nuclear::AudioEffect*>(pClip->GetAnimation());
		if (pAni)
		{
			Nuclear::AudioEffect::SOUND_PROP type = static_cast<Nuclear::AudioEffect::SOUND_PROP>(nID);

			pAni->SetSoundProp(type, value);
			int todo;
			return true;
		}
	}
	return false;
}

bool CEffectEditorDoc::SetSelectedClipSpec(bool spec, CView* pSender)
{	
	if (m_pSelectedEffectClips.size() == 1)
	{
		Nuclear::AbstractEffectClip* pClip = *m_pSelectedEffectClips.begin();
		int startFrame, endFrame;
		eAnimationPlayType oldPlayTime;
		if (!GetClipStartEndFrame(pClip, oldPlayTime, startFrame, endFrame))
			return false;
		if (spec == (oldPlayTime == APT_NORMAL))
		{
			if (spec)	
			{
				endFrame = -1;
			} else {
				endFrame -= 1;
			}
			ModifyStartEndFrame(pClip, startFrame, endFrame, pSender);
			return true;
		}
	}
	return false;
}

bool CEffectEditorDoc::SaveSelectedClipPlayModeAndTime(Nuclear::XPPLAY_MODE type, int nTime, CView* pSender)
{
	if (m_pSelectedEffectClips.size() == 1)
	{
		Nuclear::Effect* pAni = (*m_pSelectedEffectClips.begin())->GetAnimation();
		if (pAni)
		{
			CActionSetClipPlayModeAndTime* pOper = new CActionSetClipPlayModeAndTime();
			pOper->m_pEffect = pAni;
			switch (pAni->GetPlayMode())
			{
			case Nuclear::XPPM_LOOP:
				pOper->m_Types[CAction::BEFORE_ACTION] = Nuclear::XPPM_LOOP;
				break;
			case Nuclear::XPPM_LOOPNUM:
				pOper->m_Types[CAction::BEFORE_ACTION] = Nuclear::XPPM_LOOPNUM;
				pOper->m_nTimes[CAction::BEFORE_ACTION] =pAni->GetLoopNum();
				break;
			case Nuclear::XPPM_TIME:
				pOper->m_Types[CAction::BEFORE_ACTION] = Nuclear::XPPM_TIME;
				pOper->m_nTimes[CAction::BEFORE_ACTION] = pAni->GetPlayTime();
				break;
			}
			pOper->m_Types[CAction::AFTER_ACTION] = type;
			pOper->m_nTimes[CAction::AFTER_ACTION] = nTime;
			m_pActionList->DoOneAction(pOper);
			switch (type)
			{
			case Nuclear::XPPM_LOOP:
				pAni->SetLoopNum(-1);
				break;
			case Nuclear::XPPM_LOOPNUM:
				pAni->SetLoopNum(nTime);
				break;
			case Nuclear::XPPM_TIME:
				pAni->SetPlayTime(nTime);
				break;
			}
			m_bNeedReplay = true;
			UpdateAllViews(pSender);
			return true;
		}
	}
	return false;
}

bool CEffectEditorDoc::SetSelectedClipVertexColor(const Nuclear::XPCOLOR& color, CView* pSender)
{
	if (m_pSelectedEffectClips.size() == 1)
	{
		Nuclear::Effect* pAni = (*m_pSelectedEffectClips.begin())->GetAnimation();
		if (pAni)
		{
			CActionSetClipVertexColor* pOper = new CActionSetClipVertexColor();
			pOper->m_pEffect = pAni;
			pOper->m_Colors[CAction::BEFORE_ACTION] = pAni->GetVertexColor();
			pOper->m_Colors[CAction::AFTER_ACTION] = color;
			m_pActionList->DoOneAction(pOper);
			pAni->SetVertexColor(color);
			UpdateAllViews(pSender);
			return true;
		}
	}
	return false;
}

bool CEffectEditorDoc::SetSelectedClipFPS(float fps, CView* pSender)
{
	if (m_pSelectedEffectClips.size() == 1)
	{
		Nuclear::XapEffect* pXapEffect = dynamic_cast<Nuclear::XapEffect*>((*m_pSelectedEffectClips.begin())->GetAnimation());
		if (pXapEffect)
		{
			float fFps = pXapEffect->GetFps();
			if (fabs(fFps - fps) > EPSILON)
			{
				CActionSetClipFps* pOper = new CActionSetClipFps();
				pOper->m_pEffect = pXapEffect;
				pOper->m_fFps[CAction::BEFORE_ACTION] = fFps;
				pOper->m_fFps[CAction::AFTER_ACTION] = fps;
				m_pActionList->DoOneAction(pOper);
				pXapEffect->SetFps(fps);
				m_bNeedReplay = true;
				UpdateAllViews(pSender);
				return true;
			}
		}
	}
	return false;
}

void CEffectEditorDoc::MoveSelectedClips(int addX, int addY)
{
	if (m_EffectFileType == EFT_SEBIND)
		return;	
	if (m_pSelectedEffectClips.size() > 0)
	{
		EffectClipSet::iterator iter = m_pSelectedEffectClips.begin();
		Nuclear::Location loc;
		CActionSetClipPos* pOper = new CActionSetClipPos();
		for (;iter!= m_pSelectedEffectClips.end();++iter)
		{
			loc = (*iter)->GetAnimation()->GetLocation();
			pOper->m_ClipPositions[CAction::BEFORE_ACTION][*iter] = loc;
			loc.x += addX;
			loc.y += addY;
			pOper->m_ClipPositions[CAction::AFTER_ACTION][*iter] = loc;
			(*iter)->GetAnimation()->SetLocation(loc);
		}
		m_pActionList->DoOneAction(pOper);
	}

}

void CEffectEditorDoc::InsertOneKeyPointArray(Nuclear::AbstractEffectClip::KP_VECTOR& kps, 
											const float& v, int frame, int firstFrame, int lastFrame)
{
	size_t i;
	bool ok = false;
	for (i=0;i<kps.size();++i)
	{
		if (fabs(kps[i].point[0].x - frame) <= EPSILON)
		{
			ok = true;
		} else if (kps[i].point[0].x > frame)
		{
			if (frame - kps[i-1].point[0].x > EPSILON)	
			{
				Nuclear::AbstractEffectClip::KeyPoint kp;
				Nuclear::AbstractEffectClip::KP_VECTOR::iterator kpIter = kps.begin() + i;
				kp.point[0].x = static_cast<float>(frame);
				kps.insert(kpIter, kp);
				ok = true;
			}
		}
		if (ok)
		{
			kps[i].point[0].y = v;
			kps[i].t = 1;
			kps[i].point[1] = kps[i].point[0];
			kps[i].point[2] = kps[i].point[0];
			if (i > 0)
			{
				if (i == 1 && firstFrame >= frame)
				{
					kps[0].point[0].y = v;
					kps[0].point[1] = kps[0].point[0];
				}
				kps[i-1].t = 1;
				kps[i-1].point[2] = kps[i-1].point[0];
			}
			if (i+1 < kps.size())
			{
				if (i+2 == kps.size() && lastFrame <= frame)
				{
					kps[i+1].point[0].y = v;
					kps[i+1].point[2] = kps[i+1].point[0];
				}
				kps[i+1].t = 1;
				kps[i+1].point[1] = kps[i+1].point[0];
			}
			ok = false;
		}
	}
}

void CEffectEditorDoc::CheckOneKeyPointArray(Nuclear::AbstractEffectClip::KP_VECTOR& kps, 
										   const float& v, float maxX)
{
	Nuclear::AbstractEffectClip::KeyPoint kp;
	if (kps.size() == 0)
	{
		kp.point[0].x = 0.0f;
		kp.point[0].y = v;
		kp.point[1] = kp.point[0];
		kp.point[2] = kp.point[0];
		kp.t = 1;
		kps.push_back(kp);
		kp.point[0].x = maxX;
		kp.point[0].y = v;
		kp.point[1] = kp.point[0];
		kp.point[2] = kp.point[0];
		kps.push_back(kp);
	}
	if (kps.back().point[0].x < maxX)
	{
		Nuclear::AbstractEffectClip::KP_VECTOR::iterator kpIter = kps.begin();
		Nuclear::AbstractEffectClip::KP_VECTOR::iterator kpPreIter;
		kpPreIter = kps.end();
		--kpPreIter;	
		kpPreIter->point[2] = kpPreIter->point[0];
		kpPreIter->t = 1;
		kp.point[0].y = kpPreIter->point[0].y;
		kp.point[0].x = maxX;
		kp.point[1] = kp.point[0];
		kp.point[2] = kp.point[0];
		kp.t = 1;
		kps.push_back(kp);
	}
}

void CEffectEditorDoc::InsertTwoKeyPointArray(Nuclear::AbstractEffectClip::KP_VECTOR& kps, 
											const Nuclear::FPOINT& v, int frame, int firstFrame, int lastFrame)
{
	size_t i;
	bool bIsX;
	bool ok = false;
	int ptIndex;
	bIsX = true;
	ptIndex = 0;
	for (i=0;i<kps.size();++i,++ptIndex)
	{
		if (bIsX && kps[i].t > 1)
		{
			ptIndex = 0;
			bIsX = false;
		}
		if (fabs(kps[i].point[0].x - frame) <= EPSILON)
		{
			ok = true;
		} else if (kps[i].point[0].x > frame)
		{
			if (frame - kps[i-1].point[0].x > EPSILON)	
			{
				Nuclear::AbstractEffectClip::KeyPoint kp;
				Nuclear::AbstractEffectClip::KP_VECTOR::iterator kpIter = kps.begin() + i;
				kp.point[0].x = static_cast<float>(frame);
				kps.insert(kpIter, kp);
				ok = true;
			}
		}
		if (ok)
		{
			if (bIsX)
			{
				kps[i].point[0].y = v.x;
				kps[i].t = 1;
			} else {
				kps[i].point[0].y = v.y;
				kps[i].t = 3;
			}
			kps[i].point[1] = kps[i].point[0];
			kps[i].point[2] = kps[i].point[0];

			if (i > 0 && ((kps[i-1].t > 1) != bIsX))
			{
				if (bIsX)
				{
					if (ptIndex == 1 && firstFrame >= frame)
					{
						kps[i-1].point[0].y = v.x;
						kps[i-1].point[1] = kps[i-1].point[0];
					}
					kps[i-1].t = 1;
				} else {
					if (ptIndex == 1 && firstFrame >= frame)
					{
						kps[i-1].point[0].y = v.y;
						kps[i-1].point[1] = kps[i-1].point[0];
					}
					kps[i-1].t = 3;
				}
				kps[i-1].point[2] = kps[i-1].point[0];
			}
			if (i+1 < kps.size() && ((kps[i+1].t > 1) != bIsX))
			{
				if (bIsX)
				{
					if ( i+2 < kps.size() && kps[i+2].t > 1 && lastFrame <= frame)
					{
						kps[i+1].point[0].y = v.x;
						kps[i+1].point[2] = kps[i+1].point[0];
					}
					kps[i+1].t = 1;
				} else {
					if (i+2 == kps.size() && lastFrame <= frame)
					{
						kps[i+1].point[0].y = v.y;
						kps[i+1].point[2] = kps[i+1].point[0];
					}
					kps[i+1].t = 3;
				}
				kps[i+1].point[1] = kps[i+1].point[0];
			}
			ok = false;
		}
	}
}

void CEffectEditorDoc::CheckTwoKeyPointArray(Nuclear::AbstractEffectClip::KP_VECTOR& kps, 
										   const Nuclear::FPOINT& v, float maxX)
{
	Nuclear::AbstractEffectClip::KeyPoint kp;
	if (kps.size() == 0)
	{

		kp.point[0].x = 0.0f;
		kp.point[0].y = v.x;
		kp.point[1] = kp.point[0];
		kp.point[2] = kp.point[0];
		kp.t = 1;
		kps.push_back(kp);
		kp.point[0].x = maxX;
		kp.point[0].y = v.x;
		kp.point[1] = kp.point[0];
		kp.point[2] = kp.point[0];
		kps.push_back(kp);

		kp.point[0].x = 0.0f;
		kp.point[0].y = v.y;
		kp.point[1] = kp.point[0];
		kp.point[2] = kp.point[0];
		kp.t = 3;
		kps.push_back(kp);
		kp.point[0].x = maxX;
		kp.point[0].y = v.y;
		kp.point[1] = kp.point[0];
		kp.point[2] = kp.point[0];
		kps.push_back(kp);
	}
	if (kps.back().point[0].x < maxX)
	{
		Nuclear::AbstractEffectClip::KP_VECTOR::iterator kpIter = kps.begin();
		Nuclear::AbstractEffectClip::KP_VECTOR::iterator kpPreIter;
		for (;kpIter!=kps.end();++kpIter)
		{
			if (kpIter->t > 1)
			{
				kpPreIter = kpIter;
				--kpPreIter;	
				kpPreIter->point[2] = kpPreIter->point[0];
				kpPreIter->t = 1;
				kp.point[0].y = kpPreIter->point[0].y;
				kp.point[0].x = maxX;
				kp.point[1] = kp.point[0];
				kp.point[2] = kp.point[0];
				kp.t = 1;
				kps.insert(kpIter, kp);
				break;
			}
		}
		kpPreIter = kps.end();
		--kpPreIter;	
		kpPreIter->point[2] = kpPreIter->point[0];
		kpPreIter->t = 3;
		kp.point[0].y = kpPreIter->point[0].y;
		kp.point[0].x = maxX;
		kp.point[1] = kp.point[0];
		kp.point[2] = kp.point[0];
		kp.t = 3;
		kps.push_back(kp);
	}
}

void CEffectEditorDoc::SetSelectedEffectClipsKeys(Nuclear::AbstractEffectClip::KeyType kt, 
												const Nuclear::AbstractEffectClip::KP_VECTOR& vKP, 
												const Nuclear::AbstractEffectClip::SP_VECTOR& vSP)
{
	Nuclear::AbstractEffectClip* pClip = *m_pSelectedEffectClips.begin();
	pClip->SetToolsKeyPoints(kt, vKP);
	pClip->SetToolsSamplePoints(kt, vSP);
}

void CEffectEditorDoc::ResetEffect(Nuclear::EngineBase *pEB)
{
	ReleaseRes();
	m_pToolsEffect = new CToolsEffect(pEB);
	m_pSelectedEffectClips.clear();
	m_pActionList = new CActionList(this);
	m_pToolsEffect->SetLocation(Nuclear::Location(0, 0));
	m_pToolsEffect->SetDirection(Nuclear::XPDIR_TOP);
	m_pToolsEffect->SetLoopNum(-1);
	m_pToolsEffect->AssureResource(false);
	m_pToolsEffect->Stop();

	m_pSprite = new Sprite(pEB);
	m_pSprite->SetPosition(Nuclear::Location(0, 0));
	m_pSprite->SetDirection(Nuclear::XPDIR_TOP);

	m_pSpriteMgr = pEB->GetSpriteManager();

	wchar_t tmpProStr[500];
	GetPrivateProfileString(L"Sprite", L"Model", L"", tmpProStr, 500, theApp.GetCfgFilePath());
	std::wstring defaultModelName = tmpProStr;
	SetModelByName(defaultModelName);
	std::map<std::wstring, std::wstring> compMap;
	GetPrivateProfileString(L"Sprite", L"Component", L"", tmpProStr, 500, theApp.GetCfgFilePath());
	std::wstring componet = tmpProStr;
	std::wstring::size_type layer = 0, layer2 = 0, comp = 0;
	while (1)
	{
		comp = componet.find_first_of(L':', layer);
		if (comp == std::wstring::npos)
			break;
		layer2 = componet.find_first_of(L',', comp);
		if (layer2 == std::wstring::npos)
			layer2 = componet.size();
		compMap[componet.substr(layer, comp-layer)] = componet.substr(comp+1, layer2-comp-1);
		layer = layer2 + 1;
	}

	int layerCount = m_pSpriteMgr->GetLayerCount(defaultModelName);
	std::map<std::wstring, std::wstring>::iterator comit;
	for (int i=0;i<layerCount;++i)
	{
		comit = compMap.find(m_pSpriteMgr->GetLayerName(defaultModelName, i));
		if (comit != compMap.end())
		{
			SetComponent(i, comit->second);
		}
	}
	GetPrivateProfileString(L"Sprite", L"Action", L"", tmpProStr, 500, theApp.GetCfgFilePath());
	SetActionByName(tmpProStr);
}

void CEffectEditorDoc::OnFileExport()
{
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		{
			WCHAR extFilter[256];
			swprintf(extFilter, L"特效文件(*%s)|*%s||", Effect_ExtName, Effect_ExtName);
			CFileDialog dlg(FALSE, Effect_ExtName, m_strTitle, OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, extFilter);
			if (dlg.DoModal() == IDOK)
			{
				m_pToolsEffect->SaveEffect(dlg.GetPathName(), Nuclear::EFFECT_IO_NORMAL);
			}
		}
		break;
	case EFT_SEBIND:
		{
			WCHAR extFilter[256];
			swprintf(extFilter, L"绑定文件 (*%s)|*%s||", Bind_ExtName, Bind_ExtName);
			CFileDialog dlg(FALSE, Bind_ExtName, m_strTitle, OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, extFilter);
			if (dlg.DoModal() == IDOK)
			{
				m_ToolsSebind.Save(dlg.GetPathName(), Nuclear::EFFECT_IO_NORMAL);
			}
		}
		break;
	}
}


BOOL CEffectEditorDoc::OnSaveDocument(LPCTSTR lpszPathName)
{
	switch (m_EffectFileType)
	{
	case EFT_EFFECT:
		{
			Nuclear::AbstractEffectClip_LIST::iterator clipIter;
			Nuclear::Effect* pAni;
			int startFrame;
			CString errorStr;
			CString tmpStr;
			for (int layer=0;layer<Nuclear::GEffect::MAX_CLIP_LAYER;++layer)
			{
				std::set<int> audioFrames;
				audioFrames.clear();
				Nuclear::AbstractEffectClip_LIST clipList;
				clipList.clear();
				m_pToolsEffect->GetClipList(layer, clipList);
				clipIter = clipList.begin();
				for (;clipIter!=clipList.end();++clipIter)
				{
					pAni = (*clipIter)->GetAnimation();
					if (pAni && pAni->GetType() == Nuclear::XPET_AUDIO)
					{
						startFrame = (*clipIter)->GetStartFrame();
						if (audioFrames.find(startFrame) != audioFrames.end())
						{
							tmpStr.Format(L" %d层 %d帧有多于1个声音\n", layer, startFrame);
							errorStr += tmpStr;
						} else {
							audioFrames.insert(startFrame);
						}
					}
				}
			}
			if (tmpStr.GetLength() > 0)
			{
				errorStr = L"发现如下问题，是否继续保存：\n" + errorStr;
				if (AfxMessageBox(errorStr, MB_YESNO) != IDYES)
				{
					return FALSE;
				}
			}
			if (m_pToolsEffect->SaveEffect(lpszPathName, Nuclear::EFFECT_IO_EDIT))
			{
				SetModifiedFlag(FALSE);
				return TRUE;
			}
		}
		break;
	case EFT_SEBIND:
		{
			if (m_ToolsSebind.Save(lpszPathName, Nuclear::EFFECT_IO_EDIT))
			{
				SetModifiedFlag(FALSE);
				return TRUE;
			}
		}
		break;
	}
	return FALSE;
}

BOOL CEffectEditorDoc::OnOpenDocument(LPCTSTR lpszPathName)
{
	if (theApp.GetEngine()->GetRenderer() == NULL)
	{

		Nuclear::EngineBase* pEB = theApp.GetEngine();
		CEffectEditorView* pView = static_cast<CEffectEditorView*>(pEB);
		pView->SetLaterOpenFile(lpszPathName);
		return TRUE;
	}
	ResetEffect(theApp.GetEngine());

	XMLIO::CFileReader fr;
	int nError = fr.OpenNativeFile(lpszPathName);
	if( XMLIO::EC_SUCCESS != nError)
	{
		fr.CloseFile();
		return FALSE;
	}
	XMLIO::CINode root;
	if (!fr.GetRootNode(root))
	{
		fr.CloseFile();
		return FALSE;
	}

	std::wstring name(lpszPathName);
	std::wstring typeStr = name.substr(name.size()-7, 3);
	std::transform(typeStr.begin(), typeStr.end(), typeStr.begin(), ::towlower);

	if (typeStr == L"eff")
	{
		int loadResult = m_pToolsEffect->LoadEffect(root, Nuclear::EFFECT_IO_EDIT);
		if (m_pToolsEffect->HasIndependentSound())
		{
			std::wstring audiouri = name.substr(0, name.size() - 8) + L"audio.inf";
			XMLIO::CFileReader afr;
			int nError = afr.OpenNativeFile(audiouri.c_str());
			if( XMLIO::EC_SUCCESS == nError)
			{
				XMLIO::CINode aRoot;
				if (afr.GetRootNode(aRoot))
				{
					m_pToolsEffect->LoadEffectClips(aRoot, Nuclear::EFFECT_IO_EDIT);
				}
			}
			afr.CloseFile();
		}
		if (loadResult != 0)
		{
			fr.CloseFile();
			CString errMsg;
			if (loadResult & Nuclear::GEffect::EC_LOAD_NO_RES)
			{
				errMsg += L"  特效文件导入clip错误，加载资源失败!\n";
			}
			if (loadResult & Nuclear::GEffect::EC_LOAD_RES_FAILD)
			{
				errMsg += L"  特效文件导入clip错误，加载Clip参数失败!\n";
			}
			if (loadResult & Nuclear::GEffect::EC_LOAD_ATTRIBUTE_FAILD)
			{
				errMsg += L"  特效文件导入clip错误，找不到资源的名字!\n";
			}
			errMsg = L"加载发生错误！\n" + errMsg + L"详情请打开日志文件：\n";
			wchar_t workDir[255];
			GetCurrentDirectory(255, workDir);
			errMsg += workDir;
			errMsg += L"\\error.log";
			AfxMessageBox(errMsg);
			return FALSE;
		}
		m_EffectFileType = EFT_EFFECT;
		m_pToolsEffect->AssureResource(false);
	}
	else 
	{
		if (!m_ToolsSebind.Load(root, Nuclear::EFFECT_IO_EDIT))
		{
			XPASSERT(false);
		}
		m_EffectFileType = EFT_SEBIND;
		ResetEffectForBind();
	}

	fr.CloseFile();
	m_pSelectedEffectClips.clear();
	m_bNeedReplay = true;
	UpdateAllViews(NULL);

	return TRUE;
}

void CEffectEditorDoc::OnUpdateEditRedo(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(m_pActionList->CanRedo());
}

void CEffectEditorDoc::OnEditRedo()
{
	m_pActionList->RedoOneAction();
}

void CEffectEditorDoc::OnUpdateEditUndo(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(m_pActionList->CanUndo());
}

void CEffectEditorDoc::OnEditUndo()
{
	m_pActionList->UndoOneAction();
}

void CEffectEditorDoc::OnUpdateClipCenter(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bDrawClipCenter);
}

void CEffectEditorDoc::OnClipCenter()
{
	m_bDrawClipCenter = !m_bDrawClipCenter;
}

#ifdef _DEBUG
void CEffectEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CEffectEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif 




void CEffectEditorDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
	}
	else
	{
	}
}




BOOL CEffectEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	ResetEffect(theApp.GetEngine());
	ResetEffectForBind();

	return TRUE;
}

void CEffectEditorDoc::ReleaseRes()
{
	m_ToolsSebind.ReleaseAll();
	if (m_pActionList)
		delete m_pActionList;
	m_pActionList = NULL;
	if (m_pToolsEffect)
		delete m_pToolsEffect;
	if (m_pSprite)
		delete m_pSprite;
	m_pToolsEffect = NULL;
	m_pSprite = NULL;
}

CEffectEditorDoc::~CEffectEditorDoc()
{
	ReleaseRes();
}

CEffectEditorDoc::CEffectEditorDoc()
{
	m_pToolsEffect = NULL;
	m_pActionList = NULL;
	m_pSprite = NULL;
	m_pSpriteMgr = NULL;
	m_EffectFileType = EFT_EFFECT;
	m_pSelectedEffectClips.clear();
	m_bDrawRole = true;
}