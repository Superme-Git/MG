#include "StdAfx.h"
#include "..\engine\world\lkotreenodelist.h"
#include "..\engine\world\picresrc.h"
#include "..\engine\common\log.h"
#include "..\engine\particlesystem\iparticlemanager.h"
#include "..\engine\engine\configmanager.h"
#include "..\engine\engine\animanager.h"
#include "..\engine\engine\systemresourcemanager.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\ieffect.h"
#include "EditorRender.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "DisplayMapBase.h"



#ifdef _DEBUG
#define new DEBUG_NEW
#endif


void CDisplayMapBase::UpdateLinkedObjs(DWORD dwDelta)
{
	
	bool updateNewLightTiem = false;
	int nightTime = Nuclear::XP_NIGHT_START_TIME - Nuclear::XP_LIGNT_RANGE;
	int dayTime = Nuclear::XP_DAY_START_TIME + Nuclear::XP_LIGNT_RANGE;
	if (nightTime < 0)
		nightTime += Nuclear::XP_A_DAY_TIME;
	if (dayTime > Nuclear::XP_A_DAY_TIME)
		dayTime -= Nuclear::XP_A_DAY_TIME;
	if (nightTime < dayTime)
	{
		if ((!m_bNewDay) && (GetGameTime() > dayTime || 
			GetGameTime() < nightTime))
		{
			m_bNewDay = true;
			updateNewLightTiem = true;
		}
		if (m_bNewDay && GetGameTime() > nightTime && 
			GetGameTime() < dayTime)
		{
			m_bNewDay = false;
		}
	} else {
		if ((!m_bNewDay) && GetGameTime() < nightTime && 
			GetGameTime() > dayTime)
		{
			m_bNewDay = true;
			updateNewLightTiem = true;
		}
		if (m_bNewDay && (GetGameTime() > nightTime || 
			GetGameTime() < dayTime))
		{
			m_bNewDay = false;
		}
	}

	std::map<std::pair<char, unsigned int>, Nuclear::LkoTreeNodeList>::iterator nodeIter = m_TreeMap.begin();
	for(;nodeIter!=m_TreeMap.end();++nodeIter)
	{
		if (!m_bIsShowLayers[nodeIter->first.first + FIRST_ELEMENT_LAYER])
			continue;
		nodeIter->second.Update(dwDelta, m_DrawScrollPosition.x, m_DrawScrollPosition.y, m_fRatio);
		if (updateNewLightTiem)
		{
			nodeIter->second.RandomBrightTime();
		}
	}
}

void CDisplayMapBase::UpdateDistortObjs(DWORD dwDelta)
{
	std::map<std::pair<char, unsigned int>, Nuclear::DistortBase*>::iterator disIter = m_DistortMap.begin();
	float fTime = static_cast<float>(dwDelta) / 1000;
	for(;disIter!=m_DistortMap.end();++disIter)
	{
		if (!m_bIsShowLayers[disIter->first.first + FIRST_ELEMENT_LAYER])
			continue;
		disIter->second->Update(fTime);
	}
}

void CDisplayMapBase::UpdateEffects(DWORD dwDelta)
{
	m_pRenderer->GetParticleManager()->OnViewportUpdate(GetViewport());

	std::list<Nuclear::Effect*>::iterator effiter;
	Nuclear::Effect* pEffect;
	for (int i=0;i<4;++i)
	{
		for (effiter=m_TriggerEffectArray[i].begin();effiter!=m_TriggerEffectArray[i].end();)
		{
			pEffect = *effiter;
			if (!pEffect->Update(dwDelta))	
			{
				m_ScreenElements.RemoveTriggerEffect(pEffect);
				m_ScreenElements.SetMustRenew();
				effiter = m_TriggerEffectArray[i].erase(effiter);
				delete pEffect;
			} else {
				++effiter;
			}
		}
	}

	for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
		m_WaterEffectArray[i].Update(dwDelta);
}

Nuclear::Effect* CDisplayMapBase::CreateTriggerEffect(const Nuclear::SubMap::TriggerInfo& info)
{
	Nuclear::Effect* pEffect = m_pEffectMgr->CreateEffect(GetDocument()->GetToolsMap().GetObjFileName(PMap::MAP_OBJ_EFFECT, info.effectMapKey), false);
	if (pEffect)
	{
		pEffect->AssureResource(false);
		pEffect->SetDirection(Nuclear::XPDIR_TOP);
		pEffect->Play();
		m_TriggerEffectArray[info.layer].push_back(pEffect);
		m_ScreenElements.SetMustRenew();
	}
	return pEffect;
}

Nuclear::Effect* CDisplayMapBase::TryTrigger(const Nuclear::SubMap::TriggerInfo& info)
{
	float f = static_cast<float>(rand()) / (RAND_MAX + 1);	
	if (f < info.frequency)
	{
		
		return CreateTriggerEffect(info);
	}
	return NULL;
}

class SingleEffectNotify : public Nuclear::IEffectNotify
{
public:
	unsigned __int64 triggerTimeKey;
	CDisplayMapBase* pBase;
	virtual void OnStop(Nuclear::IEffect* pEffect) override
	{
		pBase->m_TriggerTimeMap[triggerTimeKey].m_bHasTriggered = false;
		pBase->m_pNotifys.erase(this);
		pEffect->RemoveNotify(this);
		delete this;
	}
};

void CDisplayMapBase::UpdateTriggers(DWORD dwDelta)
{
	const Nuclear::SubMap::TriggerInfoMap &infos = GetDocument()->GetTriggerInfos();
	Nuclear::SubMap::TriggerInfoMap::const_iterator iter = infos.begin();
	Nuclear::SubMap::TriggerAreaMap::const_iterator areaiter, areaiterEnd;
	Nuclear::SubMap::TriggerInfoVet::const_iterator infoIter, infoIterEnd;
	TriggerTimeMap::iterator timeIter;
	Nuclear::CRECT vp = GetViewport();
	DWORD gameTime = GetGameTime() % Nuclear::XP_A_DAY_TIME;
	Nuclear::Effect* pEffect;
	Nuclear::CRECT objRect;
	Nuclear::Location loc;
	int effectCount = 0, i = 0;
	int s;	
	int tmp;	
	TriggerTimeKey key;
	Nuclear::ActionTypeFlag atf;
	bool spriteIsReady = true;
	if (!GetSpritePosAndActionFlag(loc, atf))
		spriteIsReady = false;
	for (;iter!=infos.end();++iter)
	{
		if (!CheckUpdateTrigger(iter->first))
			continue;
		const Nuclear::SubMap::TriggerObjInfo &info = iter->second;
		key.infoID = iter->first;
		for (areaiter=info.areas.begin(),areaiterEnd=info.areas.end();areaiter!=areaiterEnd;++areaiter)
		{
			if (Nuclear::IsRectCross(vp, areaiter->second))
			{
				key.areaID = areaiter->first;
				infoIter=info.triggerInfos.begin();
				infoIterEnd=info.triggerInfos.end();
				key.infoVetID = 0;
				for (;infoIter!=infoIterEnd;++infoIter, ++key.infoVetID)
				{
					if (infoIter->triggerTypeFlag & Nuclear::SubMap::TRIGGER_DELETE)
						continue;
					if (infoIter->triggerTypeFlag & Nuclear::SubMap::TRIGGER_TIME)
					{
						
						if ( ((infoIter->beginTime > gameTime) && (gameTime >= infoIter->endTime)) ||
							((infoIter->beginTime > gameTime) && (infoIter->endTime >= infoIter->beginTime)) ||
							((gameTime >= infoIter->endTime) && (infoIter->endTime >= infoIter->beginTime)))
							continue;
					}
					timeIter = m_TriggerTimeMap.find(key.id);
					if (timeIter == m_TriggerTimeMap.end())
					{
						m_TriggerTimeMap[key.id] = sTimeStruct();
						timeIter = m_TriggerTimeMap.find(key.id);
					}
					if (infoIter->triggerTypeFlag & Nuclear::SubMap::TRIGGER_SPRITE)
					{
						if (!spriteIsReady)
							continue;
						if (!areaiter->second.PtInRect(loc))
							continue;
						if (!(atf & infoIter->actionsFlag))
							continue;
						if ((infoIter->maxDelta > 0) && (timeIter->second.m_EffectTime > infoIter->maxDelta))
						{
							pEffect = CreateTriggerEffect(*infoIter);
							if (pEffect)
							{
								pEffect->SetLocation(loc+infoIter->excursion);
							}
							timeIter->second.m_EffectTime = 0;
							timeIter->second.m_TriggerTime = 0;
						} else {
							timeIter->second.m_EffectTime += dwDelta;
						}
						if (timeIter->second.m_TriggerTime >= infoIter->triDelta)
						{
							timeIter->second.m_TriggerTime -= infoIter->triDelta;
							pEffect = TryTrigger(*infoIter);
							if (pEffect)
							{
								pEffect->SetLocation(loc+infoIter->excursion);
								timeIter->second.m_EffectTime = 0;
							}
						}
						timeIter->second.m_TriggerTime += dwDelta;
					} else if (infoIter->triggerTypeFlag & Nuclear::SubMap::TRIGGER_NONRANDOM_AND_SINGLE) {
						if (!timeIter->second.m_bHasTriggered)
						{
							timeIter->second.m_TriggerTime += dwDelta;
							if ((infoIter->maxDelta > 0) && (timeIter->second.m_EffectTime >= infoIter->maxDelta))
							{
								pEffect = CreateTriggerEffect(*infoIter);
								if (pEffect)
								{
									pEffect->SetLocation(areaiter->second.Center());
									SingleEffectNotify* pNotify = new SingleEffectNotify();
									m_pNotifys.insert(pNotify);
									pNotify->pBase = this;
									pNotify->triggerTimeKey = key.id;
									pEffect->AddNotify(pNotify);
								}
								timeIter->second.m_EffectTime = 0;
								timeIter->second.m_TriggerTime = 0;
								timeIter->second.m_bHasTriggered = true;
							} else {
								timeIter->second.m_EffectTime += dwDelta;
							}
							if (timeIter->second.m_TriggerTime >= infoIter->triDelta)
							{
								timeIter->second.m_TriggerTime -= infoIter->triDelta;
								pEffect = TryTrigger(*infoIter);
								if (pEffect)
								{
									pEffect->SetLocation(areaiter->second.Center());
									timeIter->second.m_EffectTime = 0;
									SingleEffectNotify* pNotify = new SingleEffectNotify();
									m_pNotifys.insert(pNotify);
									pNotify->pBase = this;
									pNotify->triggerTimeKey = key.id;
									pEffect->AddNotify(pNotify);
									timeIter->second.m_bHasTriggered = true;
								}
							}
						}
					} else {
						objRect.left = vp.left>areaiter->second.left?vp.left:areaiter->second.left;
						objRect.top = vp.top>areaiter->second.top?vp.top:areaiter->second.top;
						objRect.right = vp.right<areaiter->second.right?vp.right:areaiter->second.right;
						objRect.bottom = vp.bottom<areaiter->second.bottom?vp.bottom:areaiter->second.bottom;
						s = objRect.Width() * objRect.Height();
						if (s == 0)
						{
							timeIter->second.m_EffectTime = 0;
							timeIter->second.m_TriggerTime = 0;
							continue;
						}
						tmp = Nuclear::PMap::TILE_S * infoIter->triDelta;
						effectCount = s * timeIter->second.m_TriggerTime / tmp;
						if ((infoIter->maxDelta > 0) && (effectCount == 0) && (timeIter->second.m_EffectTime > infoIter->maxDelta))
						{
							pEffect = CreateTriggerEffect(*infoIter);
							if (pEffect)
							{
								loc.x = rand() % (objRect.Width()) + objRect.left;
								loc.y = rand() % (objRect.Height()) + objRect.top;
								pEffect->SetLocation(loc);
							}
							timeIter->second.m_EffectTime = 0;
							timeIter->second.m_TriggerTime = 0;
						} else {
							timeIter->second.m_EffectTime += dwDelta;
						}
						timeIter->second.m_TriggerTime += dwDelta;
						timeIter->second.m_TriggerTime -= effectCount * tmp / s;
						if (effectCount > 0)
						{
							/*CString str;
							str.Format(L"%d, %d\n", effectCount, timeIter->second.triggerTime);
							OutputDebugString(str);*/
							if (effectCount > 20)
							{
								effectCount = 20;
							}
							for (i=0;i<effectCount;++i)
							{
								pEffect = TryTrigger(*infoIter);
								if (pEffect)
								{
									
									loc.x = rand() % (objRect.Width()) + objRect.left;
									loc.y = rand() % (objRect.Height()) + objRect.top;
									pEffect->SetLocation(loc);
									timeIter->second.m_EffectTime = 0;
								}
							}
						}
					}
				}

			}
		}
	}

}

void CDisplayMapBase::ChangeScale(void)
{
	if (m_pRenderer)
	{
		GetWndClientRect(&m_ClientRect);
		Nuclear::CRECT sourceRect(0, 0, m_ClientRect.right-m_ClientRect.left, m_ClientRect.bottom-m_ClientRect.top);
		m_pRenderer->SetSourceRect(&sourceRect);
	}
	SetRatio(m_fRatio);
}

void CDisplayMapBase::SetRatio(float ratio)
{
	if (!m_bInited)
		return;
	CMapEditorDoc* pDoc = GetDocument();
	Nuclear::PMap &pmap = pDoc->GetToolsMap();
	m_fRatio = ratio;
	
	m_DrawScrollPosition.x += m_nViewportWidth / 2;
	m_DrawScrollPosition.y += m_nViewportHeight / 2;
	m_nViewportWidth = static_cast<int>((m_ClientRect.right - m_ClientRect.left) / ratio);
	m_nViewportHeight = static_cast<int>((m_ClientRect.bottom - m_ClientRect.top) / ratio);
	
	m_DrawScrollPosition.x -= m_nViewportWidth / 2;
	m_DrawScrollPosition.y -= m_nViewportHeight / 2;
	if (m_nViewportWidth > pmap.GetWidth())
	{
		m_nViewportWidth = pmap.GetWidth();
		m_DrawScrollPosition.x = 0;
	}

	if (m_nViewportHeight > pmap.GetHeight())
	{
		m_nViewportHeight = pmap.GetHeight();
		m_DrawScrollPosition.y = 0;
	}

	if (m_DrawScrollPosition.x < 0)
		m_DrawScrollPosition.x = 0;
	if (m_DrawScrollPosition.y < 0)
		m_DrawScrollPosition.y = 0;

	UpdateLinkedObjs(0);
	ResetScrollBar();
	
}

bool CDisplayMapBase::checkRenderer()
{
	if( m_pRenderer == NULL ) 
	{
		Nuclear::CRECT sourceRect(0, 0, m_ClientRect.right-m_ClientRect.left, m_ClientRect.bottom-m_ClientRect.top);
		
		CreateEditorRenderer(&m_pRenderer, GetHWnd(), Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)), theApp.GetFileIOManager());
		
		m_pRenderer->SetSourceRect(&sourceRect);
		m_BackgroundSprite = m_pRenderer->CreateRenderTarget(m_pRenderer->GetDisplayMode().width,
			m_pRenderer->GetDisplayMode().height, Nuclear::XPTEXFMT_A8R8G8B8);
		if (m_BackgroundSprite == Nuclear::INVALID_PICTURE_HANDLE)
		{
			XPLOG_INFO(L"Create back sprite error\n");
			OutputDebugString(L"Create back sprite error\n");
		}
		m_pSystemResourceMgr = new Nuclear::SystemResourceManager(this);
		m_pSystemResourceMgr->Init();
		return true;
	}
	return false;
}
bool CDisplayMapBase::CheckLinkedObjectUpdateList(char layerID, unsigned int objID, Nuclear::PLinkedObject* pLinkedObj, const Nuclear::CPOINT &pos)
{
	

	std::pair<char, unsigned int> tmpKey;
	tmpKey.first = layerID;
	tmpKey.second = objID;
	if (m_TreeMap.find(tmpKey) == m_TreeMap.end())
	{
		Nuclear::LkoTreeNodeList tmp;
		tmp.SetStaticFlag(pLinkedObj->m_bStatic);
		m_TreeMap[tmpKey] = tmp;
		std::map<std::pair<char, unsigned int>, Nuclear::LkoTreeNodeList>::iterator iter = m_TreeMap.find(tmpKey);
		Nuclear::LkoTreeNode* tmpNode, *nodeRoot;
		DWORD rootId = pLinkedObj->GetMainNodeID();
		Nuclear::PLinkedObject::VectorLinkedObjectInfo::iterator nodeIter = pLinkedObj->m_vectorLinkedObjectInfo.begin() + rootId;
		if (nodeIter == pLinkedObj->m_vectorLinkedObjectInfo.end())
			return false;
		tmpNode = new Nuclear::LkoTreeNode();
		tmpNode->pParent = NULL;
		tmpNode->pRoot = tmpNode;
		nodeRoot = tmpNode;
		tmpNode->LinkedObjectMoveParam.m_nMoveFollowType = nodeIter->m_LinkedObjectMoveParam.m_nMoveFollowType;
		tmpNode->height = nodeIter->m_nHeight;
		tmpNode->width = nodeIter->m_nWidth;
		tmpNode->rotateCent = nodeIter->m_rotateCent;
		Nuclear::CPOINT rootRotateCent = nodeIter->m_rotateCent;
		tmpNode->rotateCent.x += nodeIter->m_nWidth / 2;
		tmpNode->rotateCent.y += nodeIter->m_nHeight / 2;
		tmpNode->fromMainRotateCent = (pos - pLinkedObj->m_ptCentPos + tmpNode->rotateCent).ToFPOINT();
		tmpNode->LinkedObjectMoveParam = nodeIter->m_LinkedObjectMoveParam;
		tmpNode->fAngle = tmpNode->fSkewx = 0.0f;
		iter->second.AddANode(tmpNode);
		int i = 0;
		for (nodeIter=pLinkedObj->m_vectorLinkedObjectInfo.begin();nodeIter!=pLinkedObj->m_vectorLinkedObjectInfo.end();++nodeIter,++i)
		{
			if (i == rootId)
				continue;
			tmpNode = new Nuclear::LkoTreeNode();
			tmpNode->pRoot = nodeRoot;
			tmpNode->pParent = nodeRoot;
			tmpNode->LinkedObjectMoveParam.m_nMoveFollowType = nodeIter->m_LinkedObjectMoveParam.m_nMoveFollowType;
			tmpNode->height = nodeIter->m_nHeight;
			tmpNode->width = nodeIter->m_nWidth;
			tmpNode->fromMainRotateCent = (nodeIter->m_rotateCent_2 - rootRotateCent).ToFPOINT();
			tmpNode->rotateCent = nodeIter->m_rotateCent;
			tmpNode->rotateCent.x += nodeIter->m_nWidth / 2;
			tmpNode->rotateCent.y += nodeIter->m_nHeight / 2;
			tmpNode->LinkedObjectMoveParam = nodeIter->m_LinkedObjectMoveParam;
			tmpNode->fAngle = tmpNode->fSkewx = 0.0f;
			iter->second.AddANode(tmpNode);
		}
		iter->second.Update(0, m_DrawScrollPosition.x, m_DrawScrollPosition.y, m_fRatio);
	}
	return true;
}
bool CDisplayMapBase::CheckDistortObjectUpdateList(char layerID, unsigned int objID, Nuclear::PDistortionObject* pDistObj, const Nuclear::CPOINT &pos, const std::wstring& name)
{
	std::pair<char, unsigned int> tmpKey;
	tmpKey.first = layerID;
	tmpKey.second = objID;
	if (m_DistortMap.find(tmpKey) == m_DistortMap.end())
	{
		Nuclear::DistortionEffectParam effectParam;
		effectParam.nType = pDistObj->m_distortionObj.nType;
		effectParam.fCycNum = pDistObj->m_distortionObj.fCycNum;
		effectParam.fAmplitudes = pDistObj->m_distortionObj.fAmplitudes;
		effectParam.fSpeed = pDistObj->m_distortionObj.fSpeed;
		effectParam.fPos = 0.0f;
		Nuclear::DistortBase* pTmpDist = new Nuclear::DistortBase(this, effectParam);
		std::wstring imgFilename = pDistObj->m_distortionObj.strImgFileName;
		std::wstring imgLightFilename = pDistObj->m_distortionObj.strLightFile;
		imgFilename.erase(imgFilename.size()-4, 4);
		CheckPictureLoad(imgFilename);
		LPImgInfo pLightImgInfo = NULL;
		LPImgInfo pImgInfo = m_ImgInfoMap[imgFilename];
		if (pDistObj->m_bLightFlag)	
		{
			imgLightFilename.erase(imgLightFilename.size()-4, 4);
			CheckPictureLoad(imgLightFilename);
			pLightImgInfo = m_ImgInfoMap[imgLightFilename];
		}
		if (pLightImgInfo)
		{
			if ((pLightImgInfo->m_nFileWidth != pImgInfo->m_nFileWidth) ||
				(pLightImgInfo->m_nFileHeight != pImgInfo->m_nFileHeight))
			{
				std::wstring err = L"飘动物件\n" + name + L"\n的灯光节点和主节点大小不一致";
				AfxMessageBox(err.c_str());
				delete pLightImgInfo;
				pLightImgInfo = NULL;
			}
		}
		std::vector<Nuclear::CRECT>::iterator rectIter = pImgInfo->m_RectArray.begin();
		std::vector<Nuclear::PictureHandle>::iterator handleIter = pImgInfo->m_HandleArray.begin();
		std::vector<Nuclear::PictureHandle>::iterator handleIE = pImgInfo->m_HandleArray.end();
		std::vector<Nuclear::PictureHandle>::iterator lightHandleIter;
		if (pLightImgInfo)
		{
			lightHandleIter = pLightImgInfo->m_HandleArray.begin();
		}
		Nuclear::PicResrc *pPic, *pLightPic;
		for (;handleIter!=handleIE;++handleIter,++rectIter)
		{
			if (m_PictureResMap.find(*handleIter) == m_PictureResMap.end())
			{
				pPic = new Nuclear::PicResrc();
				m_PictureResMap[*handleIter] = pPic;
				pPic->handle = *handleIter;
			} else {
				pPic = m_PictureResMap[*handleIter];
			}
			pTmpDist->PushAnPic(pPic, *rectIter);
			if (pLightImgInfo)
			{
				if (m_PictureResMap.find(*lightHandleIter) == m_PictureResMap.end())
				{
					pLightPic = new Nuclear::PicResrc();
					m_PictureResMap[*lightHandleIter] = pLightPic;
					pLightPic->handle = *lightHandleIter;
				} else {
					pLightPic = m_PictureResMap[*lightHandleIter];
				}
				pTmpDist->PushLightPic(pLightPic, *rectIter);
				++lightHandleIter;
			}
		}

		Nuclear::CRECT rect;
		rect.left = pos.x;
		rect.top = pos.y;
		rect.right = rect.left + pImgInfo->m_nFileWidth;
		rect.bottom = rect.top + pImgInfo->m_nFileHeight;
		pTmpDist->setPos(rect);
		m_DistortMap[tmpKey] = pTmpDist;
				
	}
	return true;
}

void CDisplayMapBase::ResetWaterEffect()
{
	if (!GetDocument())
		return;
	Nuclear::PMap &pmap = GetDocument()->GetToolsMap();
	if (m_WaterAlphaMask == Nuclear::INVALID_PICTURE_HANDLE)
	{
		m_WaterAlphaMask = m_pRenderer->LoadPicture(L"/map/tiles/tilemask.png");
	}
	for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
	{
		m_WaterEffectArray[i].SetAni(pmap.GetWaterName(i));
		m_WaterEffectArray[i].SetAlphaMask(m_WaterAlphaMask);
	}
}

void CDisplayMapBase::CheckDistortObjectLoad(const std::wstring& name)
{
	Nuclear::PDistortionObject* pDistortObj = NULL;
	if (m_DistortObjectMap.find(name) == m_DistortObjectMap.end())
	{
		Nuclear::XBuffer buffer;
		theApp.GetFileIOManager()->GetFileImage(name, buffer);
		if (buffer.size() > 0)
		{
			pDistortObj = new Nuclear::PDistortionObject();
			pDistortObj->LoadFromMem(buffer.constbegin(), (int)buffer.size());
		} else {
			CString error = _T("飘动物件加载错误：");
			error = error + name.data();
			error = error + _T("\n");
			XPLOG_MAPEDITOR(error);
		}
		m_DistortObjectMap[name] = pDistortObj;
	}
}

void CDisplayMapBase::CheckLinkedObjectLoad(const std::wstring& name)
{
	Nuclear::PLinkedObject* pLinkedObj = NULL;
	if (m_LinkedObjectMap.find(name) == m_LinkedObjectMap.end())
	{
		Nuclear::XBuffer buffer;
		theApp.GetFileIOManager()->GetFileImage(name, buffer);
		if (buffer.size() > 0)
		{
			pLinkedObj = new Nuclear::PLinkedObject();
			pLinkedObj->LoadFromMem(buffer.constbegin(), (int)buffer.size());
		} else {
			CString error = _T("动态物件加载错误：");
			error = error + name.data();
			error = error + _T("\n");
			XPLOG_MAPEDITOR(error);
		}
		m_LinkedObjectMap[name] = pLinkedObj;
	}
}

void CDisplayMapBase::CheckPictureLoad(const std::wstring& name)
{
	LPImgInfo pImgInfo = NULL;
	if (m_ImgInfoMap.find(name) == m_ImgInfoMap.end())
	{
		CString fileName;
		fileName = name.data();
		fileName = fileName + ImageSet_ExtName;
		Nuclear::XBuffer buffer;
		theApp.GetFileIOManager()->GetFileImage(fileName.GetBuffer(), buffer);
		fileName.ReleaseBuffer();
		if (buffer.size() > 0)
		{
			Nuclear::PictureHandle handle;
			Nuclear::PImg tmpimg;
			int col, row, i;
			CString nameFormat;
			nameFormat = name.data();
			nameFormat = nameFormat + _T("_%d");
			tmpimg.LoadFromMem(buffer.constbegin(), (int)buffer.size());
			pImgInfo = new sImgInfo();
			*pImgInfo = tmpimg;
			i = 0;
			for (row=0;row<pImgInfo->m_nRow;row++)
			{
				for (col=0;col<pImgInfo->m_nCol;col++,i++)
				{
					fileName.Format(nameFormat, i);
					handle = m_pRenderer->LoadPicture(std::wstring(fileName));
					pImgInfo->m_HandleArray.push_back(handle);
				}
			}
		} else {
			CString error = _T("图片加载错误：");
			error = error + name.data();
			error = error + _T("\n");
			XPLOG_MAPEDITOR(error);
		}
		m_ImgInfoMap[name] = pImgInfo;
	}
}

bool CDisplayMapBase::drawLinkedObjectTree(char layerID, Nuclear::SubMap::sObjInfo* ptrObjInfo, bool drawBase)
{
	std::pair<char, unsigned int> tmpKey;
	tmpKey.first = layerID;
	tmpKey.second = ptrObjInfo->m_ObjectId;
	const Nuclear::DTList &nodeUpdateList = m_TreeMap[tmpKey].GetList();
	if (nodeUpdateList.size() < 1) return false;
	
	Nuclear::PLinkedObject *pLinkedObj = m_LinkedObjectMap[GetDocument()->GetToolsMap().GetObjFileName(Nuclear::PMap::MAP_OBJ_LINKEDOBJ, ptrObjInfo->m_MapKey)];
	Nuclear::PLinkedObject::VectorLinkedObjectInfo::iterator linkedObjInfoIter = pLinkedObj->m_vectorLinkedObjectInfo.begin();
	Nuclear::DTList::const_iterator dynamicObjRoot = nodeUpdateList.begin();
	Nuclear::DTList::const_iterator dynamicObjNode = dynamicObjRoot;
	Nuclear::DTList::const_iterator dynamicIter;
	++dynamicObjNode;
	std::wstring imgFilename;
	bool result = true;
	bool ok, isLight;
	isLight = false;
	for (;linkedObjInfoIter!=pLinkedObj->m_vectorLinkedObjectInfo.end();++linkedObjInfoIter)
	{
		ok = true;
		imgFilename = linkedObjInfoIter->m_strPicName;
		imgFilename.erase(imgFilename.size()-4, 4);		
		if (linkedObjInfoIter->m_bMainNode)
		{
			dynamicIter = dynamicObjRoot;
		} else {
			dynamicIter = dynamicObjNode++;
		}
		if (linkedObjInfoIter->m_LinkedObjectMoveParam.m_dwFlag & Nuclear::XP_LINKEDOBJ_LIGHT)
		{
			isLight = true;
			if (m_bSettingTimeEffect || m_bPlayingTime)
			{
				ok = (*dynamicIter)->CheckLightTime(GetGameTime());
			} else {
				ok = false;
			}
		}
		
		if (ok)
		{
#ifndef TIME_EFFECT_USE_RENDERTARGET
			Nuclear::XPRENDER_EFFECT oldEffect = m_pRenderer->GetRenderEffect();
			if (isLight)
			{
				m_pRenderer->SelectRenderEffect(Nuclear::XPRE_NULL);
			}
#endif
			result = drawLinkedObjectNode(imgFilename, &((*dynamicIter)->mTransResult), ptrObjInfo->m_AttachColor.data) && result;
#ifndef TIME_EFFECT_USE_RENDERTARGET
			if (isLight)
			{
				m_pRenderer->SelectRenderEffect(oldEffect);
				isLight = false;
			}
#endif
		}		
	}
	if (!pLinkedObj->m_bStatic && m_bShowBase && drawBase)
	{
		float x1, x2, y1, y2, left, top;
		left = static_cast<float>(ptrObjInfo->m_Position.x - pLinkedObj->m_ptCentPos.x - m_DrawScrollPosition.x) * m_fRatio;
		top = static_cast<float>(ptrObjInfo->m_Position.y - pLinkedObj->m_ptCentPos.y - m_DrawScrollPosition.y) * m_fRatio;
		int i;
		for (i=0;i<3;i++)
		{
			x1 = left + pLinkedObj->m_ptBasePos[i].x * m_fRatio;
			x2 = left + pLinkedObj->m_ptBasePos[i+1].x * m_fRatio;
			y1 = top +pLinkedObj->m_ptBasePos[i].y * m_fRatio;
			y2 = top + pLinkedObj->m_ptBasePos[i+1].y * m_fRatio;
			m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
		}
		x1 = left + pLinkedObj->m_ptBasePos[3].x * m_fRatio;
		x2 = left + pLinkedObj->m_ptBasePos[0].x * m_fRatio;
		y1 = top + pLinkedObj->m_ptBasePos[3].y * m_fRatio;
		y2 = top + pLinkedObj->m_ptBasePos[0].y * m_fRatio;
		m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
		
		x1 = left + pLinkedObj->m_ptBasePos[2].x * m_fRatio;
		y1 = top + pLinkedObj->m_ptBasePos[2].y * m_fRatio;
		m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
	}
	return result;
}

bool CDisplayMapBase::drawLinkedObjectNode(const std::wstring& imgname, Nuclear::XPMATRIX3 const *m, unsigned int attachColor)
{
	CheckPictureLoad(imgname);
	LPImgInfo pImgInfo = m_ImgInfoMap[imgname];

	if (!pImgInfo)
	{
		return false;
	}
	std::vector<Nuclear::CRECT>::iterator rectIter = pImgInfo->m_RectArray.begin();
	std::vector<Nuclear::CRECT>::iterator rectEnd = pImgInfo->m_RectArray.end();
	std::vector<Nuclear::PictureHandle>::iterator handleIter = pImgInfo->m_HandleArray.begin();
	Nuclear::DrawPictureParam param;
	Nuclear::FRECT tmpRect;
	param.pRect = &tmpRect;
	param.colorCount = 1;
	param.pColor = reinterpret_cast<Nuclear::XPCOLOR*>(&attachColor);
	param.pMatrix = m;
	for (;rectIter!=rectEnd;rectIter++,handleIter++)
	{
		tmpRect = rectIter->ToFRECT();
		param.handle = *handleIter;
		m_pRenderer->DrawPicture(param);
	}
	return true;
}

bool CDisplayMapBase::drawElement(LPImgInfo pImgInfo, float x, float y, bool drawBase, unsigned int attachColor)
{
	if (pImgInfo == NULL)
		return false;
	Nuclear::FRECT rect;
	rect.left = x;
	rect.top = y;
	rect.right = x + pImgInfo->m_nFileWidth * m_fRatio;
	rect.bottom = y + pImgInfo->m_nFileHeight * m_fRatio;
	return drawElement(pImgInfo, rect, drawBase, attachColor);
}

bool CDisplayMapBase::drawElement(LPImgInfo pImgInfo, const Nuclear::FRECT& rect, bool drawBase, unsigned int attachColor)
{
	if (!pImgInfo)
	{
		m_pRenderer->DrawBox(rect, 0xffff0000);
		return false;
	}
	std::vector<Nuclear::CRECT>::iterator rectIter = pImgInfo->m_RectArray.begin();
	std::vector<Nuclear::CRECT>::iterator rectEnd = pImgInfo->m_RectArray.end();
	std::vector<Nuclear::PictureHandle>::iterator handleIter = pImgInfo->m_HandleArray.begin();
	Nuclear::FRECT tmpRect;
	for (;rectIter!=rectEnd;rectIter++,handleIter++)
	{
		tmpRect.left = rect.left + rectIter->left * m_fRatio;
		tmpRect.top = rect.top + rectIter->top * m_fRatio;
		tmpRect.right = rect.left + rectIter->right * m_fRatio;
		tmpRect.bottom = rect.top + rectIter->bottom * m_fRatio;
		m_pRenderer->DrawPicture(*handleIter, tmpRect, attachColor);
	}
	if (m_bShowBase && drawBase)
	{
		float x1, x2, y1, y2;
		int i;
		for (i=0;i<3;i++)
		{
			x1 = rect.left + pImgInfo->m_Positions[i].x * m_fRatio;
			x2 = rect.left + pImgInfo->m_Positions[i+1].x * m_fRatio;
			y1 = rect.top + pImgInfo->m_Positions[i].y * m_fRatio;
			y2 = rect.top + pImgInfo->m_Positions[i+1].y * m_fRatio;
			m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
		}
		x1 = rect.left + pImgInfo->m_Positions[3].x * m_fRatio;
		x2 = rect.left + pImgInfo->m_Positions[0].x * m_fRatio;
		y1 = rect.top + pImgInfo->m_Positions[3].y * m_fRatio;
		y2 = rect.top + pImgInfo->m_Positions[0].y * m_fRatio;
		m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
		
		x1 = rect.left + pImgInfo->m_Positions[2].x * m_fRatio;
		y1 = rect.top + pImgInfo->m_Positions[2].y * m_fRatio;
		m_pRenderer->DrawLine(x1, y1, x2, y2, m_BaseColor);
	}
	return true;
}

bool CDisplayMapBase::drawTransparent(void)
{
	unsigned int transID = GetSelectedTransID();
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	const Nuclear::SubMap::TransObjInfoMap& tranobjs = pmap.GetTransInfos();
	const Nuclear::SubMap::TransObjInfoMap::const_iterator iter = tranobjs.find(transID);
	if (iter == tranobjs.end())
		return false;
	const Nuclear::SubMap::TransObjInfo& transInfo = iter->second;
	CPoint scalPoint;
	int oldPicWidth = transInfo.picRect.Width();
	int oldPicHeight = transInfo.picRect.Height();
	scalPoint.x = transInfo.scalPoint.x * oldPicWidth / transInfo.width + transInfo.picRect.left - m_DrawScrollPosition.x;
	scalPoint.y = transInfo.scalPoint.y * oldPicHeight / transInfo.height + transInfo.picRect.top - m_DrawScrollPosition.y;
	if (m_TransparentObject == Nuclear::INVALID_PICTURE_HANDLE)
	{
		m_dwTickCount = GetTickCount();
	}
	DWORD dwTc = GetTickCount();
	DWORD dwDelta = dwTc - m_dwTickCount;
	if (dwDelta < 1000)	
	{
		dwDelta = 0;
	} else {
		dwDelta -= 1000;
	}


	
	if (dwDelta > transInfo.time)
	{
		if (dwDelta >= transInfo.time + 1000)	
		{
			m_bTransparent = false;
		}
		dwDelta = transInfo.time;
	}
	double dDelta = static_cast<double>(dwDelta) / transInfo.time;
	
	

	Nuclear::FRECT backRect;

	
	int picWidth = static_cast<int>(dDelta * (transInfo.width - oldPicWidth)) + oldPicWidth;
	int picHeight = static_cast<int>(dDelta * (transInfo.height - oldPicHeight)) + oldPicHeight;
	int bgWidth = (m_nViewportWidth * picWidth) / oldPicWidth;
	int bgHeight = (m_nViewportHeight * picHeight) / oldPicHeight;
	backRect.left = scalPoint.x - static_cast<float>((scalPoint.x) * picWidth) / oldPicWidth;
	backRect.right = backRect.left + bgWidth;
	backRect.top = scalPoint.y - static_cast<float>((scalPoint.y) * picHeight) / oldPicHeight;
	backRect.bottom = backRect.top + bgHeight;
	

	
	Nuclear::DrawPictureParam param;
	param.handle = m_TransparentObject;
	param.blend = Nuclear::XPTEXBM_COPY;
	param.pRect = &backRect;
	m_pRenderer->DrawPicture(param);
	
	return true;
}
bool CDisplayMapBase::drawCityGrid(void)
{
	int xAdj, yAdj,x,y,i,width,height,halfW,halfH;
	width = PMap::TILE_WIDTH;
	height = PMap::TILE_HEIGHT;
	xAdj = m_DrawScrollPosition.x % width;
	yAdj = m_DrawScrollPosition.y % height;
	halfW = width >> 1;
	halfH = height >> 1;
	x = static_cast<int>(((m_ClientRect.right - m_ClientRect.left) / m_fRatio + m_DrawScrollPosition.x) / width) + 1;
	y = static_cast<int>(((m_ClientRect.bottom - m_ClientRect.top) / m_fRatio + m_DrawScrollPosition.y) / height) + 1;
	x -= m_DrawScrollPosition.x / width;
	y -= m_DrawScrollPosition.y / height;
	i = x>y?x:y;
	Nuclear::CRECT tmpRect(-xAdj, -yAdj, i * width - xAdj, i * height - yAdj);
	Nuclear::CRECT rect = tmpRect;
	for (;rect.top<rect.bottom;rect.top+=halfH,rect.right-=halfW)
	{
		m_pRenderer->DrawLine(static_cast<float>(rect.left * m_fRatio), static_cast<float>(rect.top * m_fRatio), 
			static_cast<float>(rect.right * m_fRatio), static_cast<float>(rect.bottom * m_fRatio), m_GridColor);
	}
	rect = tmpRect;
	for (;rect.top<rect.bottom;rect.bottom-=halfH,rect.left+=halfW)
	{
		m_pRenderer->DrawLine(static_cast<float>(rect.left * m_fRatio), static_cast<float>(rect.top * m_fRatio), 
			static_cast<float>(rect.right * m_fRatio), static_cast<float>(rect.bottom * m_fRatio), m_GridColor);
	}
	rect.left = tmpRect.left;
	rect.top = tmpRect.top;
	rect.right = rect.left + halfW;
	rect.bottom = rect.top + halfH;
	for (;rect.bottom<=tmpRect.bottom;rect.bottom+=halfH,rect.right+=halfW)
	{
		m_pRenderer->DrawLine(static_cast<float>(rect.right * m_fRatio), static_cast<float>(rect.top * m_fRatio), 
			static_cast<float>(rect.left * m_fRatio), static_cast<float>(rect.bottom * m_fRatio), m_GridColor);
	}
	for (;rect.top<rect.bottom;rect.top+=halfH,rect.left+=halfW)
	{
		m_pRenderer->DrawLine(static_cast<float>(rect.right * m_fRatio), static_cast<float>(rect.top * m_fRatio), 
			static_cast<float>(rect.left * m_fRatio), static_cast<float>(rect.bottom * m_fRatio), m_GridColor);
	}
	return true;
}

bool CDisplayMapBase::drawGrids(void)
{
	
	int i,j, xAdj, yAdj,x,y,width,height;
	bool ok = true;
	if(m_ShowGridType != SGS_CLOSE)
	{
		switch (m_ShowGridType)
		{
		case SGS_SMALL_TILE:
			width = PMap::TILE_WIDTH;
			height = PMap::TILE_HEIGHT;
			break;
		case SGS_MIDDLE_TILE:
			width = Nuclear::SubMap::PGround::TILE_MID_WIDTH;
			height = Nuclear::SubMap::PGround::TILE_MID_HEIGHT;
			break;
		case SGS_BIG_TILE:
			width = Nuclear::SubMap::PGround::TILE_BIG_WIDTH;
			height = Nuclear::SubMap::PGround::TILE_BIG_HEIGHT;
			break;
		case SGS_WATER:
			width = Nuclear::SubMap::PGround::WATER_WIDTH;
			height = Nuclear::SubMap::PGround::WATER_HEIGHT;
			break;
		case SGS_800X600:
			width = 800;
			height = 600;
			break;
		case SGS_1024X768:
			width = 1024;
			height = 768;
			break;
		case SGS_BLOCK:
			width = GRID_BLOCK_WIDTH;
			height = GRID_BLOCK_HEIGHT;
			break;
		case SGS_CITY:
			drawCityGrid();
		default:
			ok = false;
			break;
		}
		
		if (ok)
		{
			xAdj = m_DrawScrollPosition.x % width;
			yAdj = m_DrawScrollPosition.y % height;
			x = static_cast<int>(((m_ClientRect.right - m_ClientRect.left) / m_fRatio + m_DrawScrollPosition.x) / width);
			y = static_cast<int>(((m_ClientRect.bottom - m_ClientRect.top) / m_fRatio + m_DrawScrollPosition.y) / height);
			x -= m_DrawScrollPosition.x / width;
			y -= m_DrawScrollPosition.y / height;
			for (i=0; i<=x ; i++) 
			{
				m_pRenderer->DrawLine((i*width - xAdj) * m_fRatio, static_cast<float>(m_ClientRect.top), (i*width - xAdj) * m_fRatio, 
					static_cast<float>(m_ClientRect.bottom), m_GridColor);
			}
			for (j=0; j<=y ; j++)
			{
				m_pRenderer->DrawLine(static_cast<float>(m_ClientRect.left), (j*height-yAdj) * m_fRatio, static_cast<float>(m_ClientRect.right), 
					(j*height-yAdj) * m_fRatio, m_GridColor);
			}
		}
	}
	return drawSelectedRects();
}

void CDisplayMapBase::DrawTriggerEffect(int i)
{
	std::list<Nuclear::Effect*>::iterator effiter;
	for (effiter=m_TriggerEffectArray[i].begin();effiter!=m_TriggerEffectArray[i].end();++effiter)
	{
		(*effiter)->Render(NULL);
	}
}

bool CDisplayMapBase::drawElementLayers(DWORD delta)
{
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	const CScreenElements::ElementLayersArray &elementLayers = m_ScreenElements.GetElements();
	CScreenElements::ElementLayersArray::const_iterator layerIter;
	Nuclear::TopologyBaseVector::const_iterator elementsIter;
	Nuclear::FRECT rect;
	Nuclear::SubMap::sObjInfo *ptrInfo;
	EffectMap::iterator effIter;
	Nuclear::Effect* pEffect;
	LPImgInfo pImgInfo;
	bool drawBase, noScal;
	noScal = (fabs(m_fRatio - 1.0f) < 0.0001f);
	for (layerIter=elementLayers.begin();layerIter!=elementLayers.end();layerIter++)
	{
		if (!m_bIsShowLayers[layerIter->m_LayerID + FIRST_ELEMENT_LAYER])
			continue;
		drawBase = layerIter->m_LayerID + FIRST_ELEMENT_LAYER == LEI_MID?true:false;
		if (layerIter->m_LayerID + FIRST_ELEMENT_LAYER == WATER_BEFORE_LAYER)
		{
			
			drawWater();
		}
		const Nuclear::TopologyBaseVector &elements = layerIter->m_TopologyArray;
		elementsIter = elements.begin();
		for (;elementsIter!=elements.end();elementsIter++)
		{
			
			switch (GetObjectType(elementsIter->sortBaseID))
			{
			case LOT_ELEMENT:	
				if (CheckDragObj(layerIter->m_LayerID, elementsIter->sortBaseID))
					continue;
				ptrInfo = static_cast<Nuclear::SubMap::sObjInfo*>(elementsIter->data);
				pImgInfo = m_ImgInfoMap[pmap.GetObjFileName(PMap::MAP_OBJ_ELEMENT, ptrInfo->m_MapKey)];
				rect.left = (ptrInfo->m_Rect.left - m_DrawScrollPosition.x) * m_fRatio;
				rect.top = (ptrInfo->m_Rect.top - m_DrawScrollPosition.y) * m_fRatio;
				rect.right = (ptrInfo->m_Rect.right - m_DrawScrollPosition.x) * m_fRatio;
				rect.bottom = (ptrInfo->m_Rect.bottom - m_DrawScrollPosition.y) * m_fRatio;
				drawElement(pImgInfo, rect, drawBase, ptrInfo->m_AttachColor.data);
				break;
			case LOT_EFFECT:
				if (noScal && m_bShowEffect)
				{
					if (CheckDragObj(layerIter->m_LayerID, elementsIter->sortBaseID))
						continue;
					ptrInfo = static_cast<Nuclear::SubMap::sObjInfo*>(elementsIter->data);
					effIter = m_EffectArray[layerIter->m_LayerID].find(ptrInfo->m_ObjectId);
					if (effIter == m_EffectArray[layerIter->m_LayerID].end())
					{
						const std::wstring &effectName = pmap.GetObjFileName(PMap::MAP_OBJ_EFFECT, ptrInfo->m_MapKey);
						pEffect = GetEffectManager()->CreateEffect(effectName, true);
						if( pEffect == NULL )
							continue;
						pEffect->AssureResource(false);
						if (m_EffectInfoMap.find(effectName) == m_EffectInfoMap.end())
						{
							sEffInfo &info = m_EffectInfoMap[effectName];
							info.m_Rect = pEffect->GetRelBouningBox();
							pEffect->GetBase(info.m_Base);
						}
						pEffect->SetDirection(Nuclear::XPDIR_TOP);
						m_EffectArray[layerIter->m_LayerID][ptrInfo->m_ObjectId] = pEffect;
						pEffect->Play();
						pEffect->Update(0);
					} else {
						pEffect = effIter->second;
						if( pEffect == NULL )
							continue;
					}
					
					pEffect->SetLocation(ptrInfo->m_Position);
					pEffect->Update(delta);
					pEffect->Render(NULL);
					if (m_bShowBase && drawBase)
					{
						Nuclear::XPBase base;
						pEffect->GetBase(base);

						Nuclear::FRECT frect;
						Nuclear::CPOINT tmpBase[4];
						tmpBase[0] = base.left;
						tmpBase[2] = base.right;
						const float tga = 2;
						tmpBase[1].x = (tmpBase[0].x+tmpBase[2].x)/2 + (LONG)(tga * (tmpBase[0].y-tmpBase[2].y)/2);
						tmpBase[1].y = (tmpBase[0].y+tmpBase[2].y)/2 + (LONG)((tmpBase[0].x-tmpBase[2].x)/(2*tga));
						tmpBase[3].x = (tmpBase[0].x+tmpBase[2].x)/2 + (LONG)(-tga * (tmpBase[0].y-tmpBase[2].y)/2);
						tmpBase[3].y = (tmpBase[0].y+tmpBase[2].y)/2 + (LONG)((tmpBase[0].x-tmpBase[2].x)/(2*-tga));

						for(int i=0; i< 4; ++i)
						{
							int j = i + 1;
							if( j == 4 ) 
								j = 0;
							frect.left = (ptrInfo->m_Position.x - m_DrawScrollPosition.x + tmpBase[i].x) * m_fRatio;
							frect.top = (ptrInfo->m_Position.y - m_DrawScrollPosition.y + tmpBase[i].y) * m_fRatio;
							frect.right = (ptrInfo->m_Position.x - m_DrawScrollPosition.x + tmpBase[j].x) * m_fRatio;
							frect.bottom = (ptrInfo->m_Position.y - m_DrawScrollPosition.y + tmpBase[j].y) * m_fRatio;
							m_pRenderer->DrawLine(frect, m_BaseColor);
						}

						
						frect.left = (ptrInfo->m_Position.x - m_DrawScrollPosition.x + tmpBase[0].x) * m_fRatio;
						frect.top = (ptrInfo->m_Position.y - m_DrawScrollPosition.y + tmpBase[0].y) * m_fRatio;
						frect.right = (ptrInfo->m_Position.x - m_DrawScrollPosition.x + tmpBase[2].x) * m_fRatio;
						frect.bottom = (ptrInfo->m_Position.y - m_DrawScrollPosition.y + tmpBase[2].y) * m_fRatio;
						m_pRenderer->DrawLine(frect, m_BaseColor);
					}
				}
				break;
			case LOT_SPRITE:	
				static_cast<Sprite*>(elementsIter->data)->Render(NULL);
				m_pRenderer->PushRenderTarget(m_BackgroundSprite);
				static_cast<Sprite*>(elementsIter->data)->Render(NULL);
				m_pRenderer->PopRenderTarget();
				break;
			case LOT_LINKEDOBJECT:
				if (CheckDragObj(layerIter->m_LayerID, elementsIter->sortBaseID))
					continue;
				ptrInfo = static_cast<Nuclear::SubMap::sObjInfo*>(elementsIter->data);
				drawLinkedObjectTree(static_cast<char>(layerIter->m_LayerID), ptrInfo, drawBase);
				break;
			case LOT_DISTORT:
				{
					if (CheckDragObj(layerIter->m_LayerID, elementsIter->sortBaseID))
						continue;
					ptrInfo = static_cast<Nuclear::SubMap::sObjInfo*>(elementsIter->data);
					std::pair<char, unsigned int> tmpKey;
					tmpKey.first = layerIter->m_LayerID;
					tmpKey.second = ptrInfo->m_ObjectId;
					Nuclear::DistortBase *pDist = m_DistortMap[tmpKey];
					pDist->setZoom(m_fRatio);
					pDist->setColor(ptrInfo->m_AttachColor.data);
					pDist->Render(NULL);

				}
				break;
			case LOT_TRIGGER_EFFECT:
				if (m_bShowEffect)
				{
					static_cast<Nuclear::Effect*>(elementsIter->data)->Render(NULL);
				}
				break;
			}
		}

		DrawMouseObj(layerIter->m_LayerID);

		if (layerIter->m_LayerID + FIRST_ELEMENT_LAYER == LEI_MID)
		{
			if (!dynamic_cast<CEditorRender*>(m_pRenderer)->GetLayerChunkState())
			{
				Nuclear::FRECT rect(.0f, 0.f, static_cast<float>(GetSystemMetrics(SM_CXSCREEN)), 
					static_cast<float>(GetSystemMetrics(SM_CYSCREEN)));
				m_pRenderer->DrawPicture(m_BackgroundSprite, rect, 0x50FFFFFF);
			}
			
		}

		if (m_bShowEffect)
		{
			switch (layerIter->m_LayerID + FIRST_ELEMENT_LAYER)
			{
			case LEI_LOW_4:	
				DrawTriggerEffect(0);
				break;
			case LEI_MID:	
				DrawTriggerEffect(2);
				break;
			case LEI_HIGH_2:	
				DrawTriggerEffect(3);
				break;
			}
		}
	}
	return true;
}

bool CDisplayMapBase::drawWater(void)
{	
	CMapEditorDoc* pDoc = (GetDocument());
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	int x, y, i, j, xAdj, yAdj, height, width, ci, cj;
	height = pmap.GetHeightForWater();
	width = pmap.GetWidthForWater();
	xAdj = m_DrawScrollPosition.x % Nuclear::SubMap::PGround::WATER_WIDTH;
	yAdj = m_DrawScrollPosition.y % Nuclear::SubMap::PGround::WATER_HEIGHT;
	x = static_cast<int>(((m_ClientRect.right - m_ClientRect.left) / m_fRatio + m_DrawScrollPosition.x) / Nuclear::SubMap::PGround::WATER_WIDTH);
	if (x >= width)
	{
		x = width - 1;
	}
	y = static_cast<int>(((m_ClientRect.bottom - m_ClientRect.top) / m_fRatio + m_DrawScrollPosition.y) / Nuclear::SubMap::PGround::WATER_HEIGHT);
	if (y >= height)
	{
		y = height - 1;
	}
	ci = 0;
	cj = 0;
	Nuclear::FRECT tmpRect;
	for (i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
		m_WaterEffectArray[i].ClearRects();
	Nuclear::SubMap::WaterTileType *ptrWater = pmap.GetWaterData();
	Nuclear::SubMap::WaterTileType *pTmpWater;
	for (j=m_DrawScrollPosition.y / Nuclear::SubMap::PGround::WATER_HEIGHT; j<=y ; j++, cj++) 
	{
		ci = 0;
		i = m_DrawScrollPosition.x / Nuclear::SubMap::PGround::WATER_WIDTH;
		pTmpWater =  ptrWater + (j * width + i);
		for ( ; i<=x ; i++, ci++, pTmpWater++)
		{
			if (pTmpWater->tileRes > 0)
			{
				tmpRect.left = static_cast<float>(ci*Nuclear::SubMap::PGround::WATER_WIDTH - xAdj) * m_fRatio;
				tmpRect.top =  static_cast<float>(cj*Nuclear::SubMap::PGround::WATER_HEIGHT - yAdj) * m_fRatio;
				tmpRect.right = static_cast<float>((ci+1)*Nuclear::SubMap::PGround::WATER_WIDTH - xAdj) * m_fRatio;
				tmpRect.bottom = static_cast<float>((cj+1)*Nuclear::SubMap::PGround::WATER_HEIGHT - yAdj) * m_fRatio;
				m_WaterEffectArray[pTmpWater->tileRes-1].PushARect(tmpRect, pTmpWater->tileType);
			}
		}
	}
	for (i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
		m_WaterEffectArray[i].Render();
	return true;
}

bool CDisplayMapBase::drawSmallTiles(void)
{
	CMapEditorDoc* pDoc = GetDocument();
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	pDoc->SetPictureInited(true);
	int x, y, i, j, xAdj, yAdj, height, width, ci, cj, layer, layerID, pos, tmpLayer;
	CString FileName;
	std::wstring pfsFilename, tmpFilename;
	TileType *ptrTile;
	TileType tileToDraw[4];
	CFileFind myFinder;
	unsigned int *pGroundColor = pmap.GetColorData();
	BOOL working;
	bool pathmapChanged = false;
	height = pmap.GetHeightForSmlTiles();
	width = pmap.GetWidthForSmlTiles();
	xAdj = m_DrawScrollPosition.x % PMap::TILE_WIDTH;
	yAdj = m_DrawScrollPosition.y % PMap::TILE_HEIGHT;
	x = static_cast<int>(((m_ClientRect.right - m_ClientRect.left) / m_fRatio + m_DrawScrollPosition.x) / PMap::TILE_WIDTH);
	if (x >= width)
	{
		x = width - 1;
	}
	y = static_cast<int>(((m_ClientRect.bottom - m_ClientRect.top) / m_fRatio + m_DrawScrollPosition.y) / PMap::TILE_HEIGHT);
	if (y >= height)
	{
		y = height - 1;
	}
	const Nuclear::SubMap::PtrTileTypeList &layers = pDoc->GetGroundLayers();
	ci = 0;
	cj = 0;
	Nuclear::FRECT tmpRect;
	unsigned char tmpTileType, nowTile;
	for (j=m_DrawScrollPosition.y / PMap::TILE_HEIGHT; j<=y ; j++, cj++) 
	{
		ci = 0;
		i = m_DrawScrollPosition.x / PMap::TILE_WIDTH;
		pos = j * width + i;
		for ( ; i<=x ; i++, ci++, pos++)
		{
			nowTile = Nuclear::SubMap::PGround::TILE_EMPTY;
			tmpLayer = 0;
			for (layer=0;layer<static_cast<int>(layers.size());layer++)
			{
				layerID = GetGroundLayerID(layer);
				if (layerID == -1)	
					continue;
				ptrTile = layers[layerID] + pos;
				tmpTileType = Nuclear::SubMap::PGround::GetTileType(*ptrTile);
				tmpTileType -= tmpTileType & nowTile;
				if (tmpTileType) 
				{	
					nowTile |= tmpTileType;	
					if (m_TileMap.find(*ptrTile)==m_TileMap.end()) 
					{
						pfsFilename = theApp.GetPathMap()->GetTileFileName(*ptrTile);
						if (pfsFilename == _T(""))
						{
							FileName.Format(theApp.GetResPath() + _T(".\\map\\tiles\\smalltile\\%d-*"), Nuclear::SubMap::PGround::GetGroundType(*ptrTile));
							working = myFinder.FindFile(LPCTSTR(FileName));
							if (working) 
							{
								myFinder.FindNextFile();
								if (myFinder.IsDirectory()) 
								{
									std::wstring name(myFinder.GetFileTitle());
									theApp.GetPathMap()->Insert(Nuclear::TST_SMALL, Nuclear::SubMap::PGround::GetGroundType(*ptrTile), name);
									FileName.Format(myFinder.GetFilePath() + _T("\\%d-%d") + TILE_TYPE, 
										Nuclear::SubMap::PGround::GetTileType(*ptrTile), Nuclear::SubMap::PGround::GetTileNumber(*ptrTile));
									working = myFinder.FindFile(LPCTSTR(FileName));
									pathmapChanged = true;
								} else {
									working = 0;
								}
							}
							myFinder.Close();
						} else {
							if (PFS::CEnv::PFSFileNameToNativeFileName(pfsFilename, tmpFilename))
							{
								FileName = tmpFilename.data();
								working = TRUE;
							} else {
								working = FALSE;
							}
						}

						if (!working) 
						{
							pDoc->SetPictureInited(false);
						}
						else
						{
							m_TileMap[*ptrTile] = m_pRenderer->LoadPictureFromNativePath(std::wstring(FileName));
						}

					}
					if (pDoc->IsPictureInited())
					{
						tileToDraw[tmpLayer++] = *ptrTile;
					} else {
						
						pDoc->SetPictureInited(true);
					}
				}
				if (nowTile == Nuclear::SubMap::PGround::TILE_SOLID || tmpLayer == 4)	
				{
					break;
				}
			}
			Nuclear::DrawPictureParam param;
			param.colorCount = 4;
			Nuclear::XPCOLOR color[4];
			param.pColor = color;
			param.pRect = &tmpRect;
			tmpRect.left = static_cast<float>(ci*PMap::TILE_WIDTH - xAdj) * m_fRatio;
			tmpRect.top =  static_cast<float>(cj*PMap::TILE_HEIGHT - yAdj) * m_fRatio;
			tmpRect.right = static_cast<float>((ci+1)*PMap::TILE_WIDTH - xAdj) * m_fRatio;
			tmpRect.bottom = static_cast<float>((cj+1)*PMap::TILE_HEIGHT - yAdj) * m_fRatio;
			
			int widthforcolor = pmap.GetWidthForColor();
			color[0] = pGroundColor[j * widthforcolor + i];
			color[1] = pGroundColor[j * widthforcolor + i + 1];
			color[2] = pGroundColor[(j + 1) * widthforcolor + i + 1];
			color[3] = pGroundColor[(j + 1) * widthforcolor + i];
			for (layer=tmpLayer-1;layer>=0;layer--)
			{
				param.handle = m_TileMap[tileToDraw[layer]];
				m_pRenderer->DrawPicture(param);
			}
		}
	}
	if (pathmapChanged)
	{
		theApp.GetPathMap()->Save();
	}
	return true;
}

bool CDisplayMapBase::drawLayerTiles(Nuclear::SubMap::GroundLayers layers)
{
	int width, height, tileWidth, tileHeight;
	std::map<int, Nuclear::PictureHandle>* pImgMap;
	Nuclear::TILE_SIZE_TYPE tileType;
	CString formatStr;
	CMapEditorDoc* pDoc = GetDocument();
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	switch(layers)
	{
	case Nuclear::SubMap::GL_SMALL:
		return drawSmallTiles();
		break;
	case Nuclear::SubMap::GL_MID1:
	case Nuclear::SubMap::GL_MID2:
		width = pmap.GetWidthForMidTiles();
		height = pmap.GetHeightForMidTiles();
		tileWidth = Nuclear::SubMap::PGround::TILE_MID_WIDTH;
		tileHeight = Nuclear::SubMap::PGround::TILE_MID_HEIGHT;
		pImgMap = &m_MidTileMap;
		tileType = Nuclear::TST_MIDDLE;
		formatStr = theApp.GetResPath() + L".\\map\\tiles\\midtile\\%d-*";
		break;
	case Nuclear::SubMap::GL_BIG:
		width = pmap.GetWidthForBigTiles();
		height = pmap.GetHeightForBigTiles();
		tileWidth = Nuclear::SubMap::PGround::TILE_BIG_WIDTH;
		tileHeight = Nuclear::SubMap::PGround::TILE_BIG_HEIGHT;
		pImgMap = &m_BigTileMap;
		tileType = Nuclear::TST_BIG;
		formatStr = theApp.GetResPath() + L".\\map\\tiles\\bigtile\\%d-*";
		break;
	}

	int x, y, i, j, xAdj, yAdj, ci, cj, pos;
	CString FileName;
	std::wstring pfsFilename, tmpFilename;
	CFileFind myFinder;
	BOOL working;
	bool pathmapChanged = false;
	bool bPicIsOk;

	
	xAdj = m_DrawScrollPosition.x % tileWidth;
	yAdj = m_DrawScrollPosition.y % tileHeight;
	x = static_cast<int>(((m_ClientRect.right - m_ClientRect.left) / m_fRatio + m_DrawScrollPosition.x) / tileWidth);
	if (x >= width)
	{
		x = width - 1;
	}
	y = static_cast<int>(((m_ClientRect.bottom - m_ClientRect.top) / m_fRatio + m_DrawScrollPosition.y) / tileHeight);
	if (y >= height)
	{
		y = height - 1;
	}

	ci = 0;
	cj = 0;
	Nuclear::FRECT tmpRect;
	BiggerTileType *pTiles, *pTmpTile;
	switch(layers)
	{
	case Nuclear::SubMap::GL_MID1:
		pTiles = pmap.GetMidTileData(0);
		break;
	case Nuclear::SubMap::GL_MID2:
		pTiles = pmap.GetMidTileData(1);
		break;
	case Nuclear::SubMap::GL_BIG:
		pTiles = pmap.GetBigTileData();		
		break;
	}

	if (pTiles == NULL)
		return false;

	for (j=m_DrawScrollPosition.y / tileHeight; j<=y ; j++, cj++) 
	{
		ci = 0;
		i = m_DrawScrollPosition.x / tileWidth;
		pos = j * width + i;
		for ( ; i<=x ; i++, ci++, pos++)
		{
			bPicIsOk = true;
			pTmpTile = pTiles + pos;
			if (pTmpTile->tileID == 0)
				continue;
			if (pImgMap->find(pTmpTile->data)==pImgMap->end()) 
			{
				pfsFilename = theApp.GetPathMap()->GetTileFileName(tileType, *pTmpTile);
				if (pfsFilename == _T(""))
				{
					FileName.Format(formatStr, pTmpTile->tileType);
					working = myFinder.FindFile(LPCTSTR(FileName));
					if (working) 
					{
						myFinder.FindNextFile();
						if (myFinder.IsDirectory()) 
						{
							std::wstring name(myFinder.GetFileTitle());
							theApp.GetPathMap()->Insert(tileType, pTmpTile->tileType, name);
							FileName.Format(myFinder.GetFilePath() + L"\\%d" + TILE_TYPE, pTmpTile->tileID);
							working = myFinder.FindFile(LPCTSTR(FileName));
							pathmapChanged = true;
						} else {
							working = 0;
						}
					}
					myFinder.Close();
				} else {
					if (PFS::CEnv::PFSFileNameToNativeFileName(pfsFilename, tmpFilename))
					{
						FileName = tmpFilename.data();
						working = TRUE;
					} else {
						working = FALSE;
					}
				}
				if (working)
				{
					(*pImgMap)[pTmpTile->data] = m_pRenderer->LoadPictureFromNativePath(std::wstring(FileName));
				} else {
					bPicIsOk = false;
				}
			}
			if (bPicIsOk)
			{
				tmpRect.left = static_cast<float>(ci*tileWidth - xAdj) * m_fRatio;
				tmpRect.top =  static_cast<float>(cj*tileHeight - yAdj) * m_fRatio;
				tmpRect.right = static_cast<float>((ci+1)*tileWidth - xAdj) * m_fRatio;
				tmpRect.bottom = static_cast<float>((cj+1)*tileHeight - yAdj) * m_fRatio;
				m_pRenderer->DrawPicture((*pImgMap)[pTmpTile->data], tmpRect, 0xFFFFFFFF);
			} else {
				
			}
		}
	}
	if (pathmapChanged)
	{
		theApp.GetPathMap()->Save();
	}
	return true;
}


bool CDisplayMapBase::drawBackground(void)
{
	if (!m_bIsShowLayers[LEI_GROUND])
		return true;
	CMapEditorDoc* pDoc = GetDocument();
	XPASSERT(pDoc);
	PMap &pmap = pDoc->GetToolsMap();
	bool result = true;
	int i;
	Nuclear::SubMap::GroundLayers layer;
	for (i=Nuclear::TILES_ALL_LAYER_COUNT-1;i>=0;--i)
	{
		if (pmap.GetGroundOrders(i, layer))
		{
			drawLayerTiles(layer);
		} else {
			result = false;
		}
	}
	return result;
}

bool CDisplayMapBase::drawMovingBackground()
{
	CMapEditorDoc* pDoc = GetDocument();
	CToolsMap &pmap = pDoc->GetToolsMap();
	const Nuclear::SubMap::PBackGoundInfoVector &pbgI = pmap.GetBackGroundInfoVet();
	Nuclear::SubMap::PBackGoundInfoVector::const_iterator it = pbgI.begin(), ie = pbgI.end();
	Nuclear::CRECT vp = GetViewport();
	Nuclear::CPOINT viewportCenter = vp.Center();
	Nuclear::FRECT bgRect;
	LPImgInfo pImgInfo;
	int bgid = 0;
	for (;it!=ie;++it, ++bgid)
	{
		const Nuclear::SubMap::PBackGoundInfo &info = *it;
		if (info.m_bDeleted)
			continue;
		Nuclear::CRECT res;
		if (vp.IsCross(info.activeRect, res))
		{
			Nuclear::CPOINT adj;
			if (bgid == m_SelectedMbg)
			{
				GetAdjMovingBackground(adj);
			}
			Nuclear::CPOINT bgVet = viewportCenter - info.pos - adj;
			bgVet.x = static_cast<int>(bgVet.x * info.relSpeed.x);
			bgVet.y = static_cast<int>(bgVet.y * info.relSpeed.y);
			int bgwidth = info.girdWidth * info.width;
			int bgheight = info.girdHeight * info.height;
			int renderwidth, renderheight;
			Nuclear::CPOINT pos = info.pos + bgVet + adj;
			pos.x -= bgwidth / 2;
			pos.y -= bgheight / 2;
			Nuclear::CPOINT ltpos;
			if (info.wrapHorz)
			{
				ltpos.x = 0;
				renderwidth = pmap.GetWidth();
			} else {
				ltpos.x = pos.x;
				renderwidth = bgwidth;
			}
			if (info.wrapVert)
			{
				ltpos.y = 0;
				renderheight = pmap.GetHeight();
			} else {
				ltpos.y = pos.y;
				renderheight = bgheight;
			}
			Nuclear::CRECT rect(ltpos, renderwidth, renderheight);
			Nuclear::CRECT objRect;
			if (rect.IsCross(res, objRect))	
			{
				if (bgid == m_SelectedMbg)
				{
					m_SelectedMovBackgroundPosition = objRect;
					m_SmbPosition = pos;
				}
				
				ltpos.x = objRect.left - vp.left;
				ltpos.y = objRect.top - vp.top;
				objRect -= pos;  
				

				if (objRect.left < 0)
				{
					XPASSERT(info.wrapHorz);	
					int n = -(objRect.left + 1) / bgwidth + 1;
					objRect.left += n*bgwidth;
					objRect.right += n*bgwidth;
				}
				if (objRect.left >= bgwidth)
				{
					int n = objRect.left / bgwidth;
					objRect.left -= n*bgwidth;
					objRect.right -= n*bgwidth;
				}
				if (objRect.top < 0)
				{
					XPASSERT(info.wrapVert);	
					int n = -(objRect.top + 1) / bgheight + 1;
					objRect.top += n*bgheight;
					objRect.bottom += n*bgheight;
				}
				if (objRect.top >= bgheight)
				{
					int n = objRect.top / bgheight;
					objRect.top -= n*bgheight;
					objRect.bottom -= n*bgheight;
				}
				int x, y, w, h, tx, ty, bx;
				bx = objRect.left / info.girdWidth;
				w = (objRect.right + info.girdWidth - 1) / info.girdWidth;
				y = objRect.top / info.girdHeight;
				h = (objRect.bottom + info.girdHeight - 1) / info.girdHeight;
				ltpos.x -= objRect.left % info.girdWidth;
				ltpos.y -= objRect.top % info.girdHeight;
				bgRect.top = ltpos.y * m_fRatio;
				bgRect.bottom = bgRect.top + info.girdHeight * m_fRatio;
				float deltaX = info.girdWidth * m_fRatio, deltaY = info.girdHeight * m_fRatio;
				for (ty=y;y<h;++y,++ty)
				{
					if (ty >= info.height) ty = 0;
					Nuclear::SubMap::ObjMapKey *pKey = info.pPics + ty * info.width + bx;
					bgRect.left = ltpos.x * m_fRatio;
					bgRect.right = bgRect.left + deltaX;
					for (x=tx=bx;x<w;++x,++pKey,++tx)
					{								
						if (tx >= info.width)
						{
							tx = 0;
							pKey = info.pPics + ty * info.width;
						}
						if (*pKey != Nuclear::SubMap::INVALID_OBJ_MAP_KEY)
						{
							std::wstring name = pmap.GetObjFileName(Nuclear::PMap::MAP_OBJ_ELEMENT, *pKey);
							CheckPictureLoad(name);
							pImgInfo = m_ImgInfoMap[name];
							drawElement(pImgInfo, bgRect, false, 0xFFFFFFFF);
						}
						bgRect.left = bgRect.right;
						bgRect.right += deltaX;
					}
					bgRect.top = bgRect.bottom;
					bgRect.bottom += deltaY;
				}
				if (bgid == m_SelectedMbg)	
				{
					

					y = objRect. top / info.girdHeight;
					float xx, yy;
					yy = ltpos.y * m_fRatio;
					xx = ltpos.x * m_fRatio;
					for (;y<=h;++y,yy+=deltaY)
					{
						m_pRenderer->DrawLine(xx, yy, bgRect.left, yy, 0xFF000000);
					}
					yy = ltpos.y * m_fRatio;
					for (x=bx;x<=w;++x,xx+=deltaX)
					{
						m_pRenderer->DrawLine(xx, yy, xx, bgRect.top, 0xFF000000);
					}
				}
			}
		}
		
	}
	return true;
}

void CDisplayMapBase::drawMap(void)
{
	
	CMapEditorDoc* pDoc = (GetDocument());
	bool updateNewLightTiem = false;
	XPASSERT(pDoc);
	GetWndClientRect(&m_ClientRect);
	updateNewLightTiem = checkRenderer();
	if (!m_bInited)
	{
		m_bInited = true;
		SetRatio(1.0f);
		ResetWaterEffect();
	}
	PMap &pmap = pDoc->GetToolsMap();
	
	if (pDoc->IsInited())
	{
		ResetScrollPos();
		m_pEffectMgr->OnTick();

		DWORD dwTickCount = GetTickCount();
		DWORD delta = dwTickCount - m_dwTickCount;
		DWORD realDelta = delta;
		m_dwTickCount = dwTickCount;
		if (delta > 200)
			delta = 200;
		UpdateTriggers(delta);
		bool updateLko = m_ScreenElements.RenewLayers() == CScreenElements::RENEW_SUCCEEDED;
		if (m_bUpdateLinkedObjectsEveryFrame || updateLko)
			UpdateLinkedObjs(delta);
		UpdateDistortObjs(delta);
		UpdateEffects(delta);
		

		m_pRenderer->Begin(m_BackgroundColor);
		if (m_bTransparent)
		{
			if (GetSelectedObjSize() > 0)
			{
				if (!drawTransparent() || !m_bTransparent)
				{
					m_pRenderer->FreePicture(m_TransparentObject);
					m_TransparentObject = Nuclear::INVALID_PICTURE_HANDLE;
					
					m_bTransparent = false;
				}
			} else {
				m_bTransparent = false;
				m_pRenderer->FreePicture(m_TransparentObject);
				m_TransparentObject = Nuclear::INVALID_PICTURE_HANDLE;
				m_dwTickCount = 0;
			}
		} else {
			if (m_bSettingTimeEffect || m_bPlayingTime)
			{
				if (m_bPlayingTime)
					UpdateGameTime(realDelta);
#ifdef TIME_EFFECT_USE_RENDERTARGET
				if (m_TimeEffectHandle==Nuclear::INVALID_PICTURE_HANDLE)
				{
					m_TimeEffectHandle = m_pRenderer->CreateRenderTarget(Nuclear::XPTEXFMT_A8R8G8B8);
				}
				m_pRenderer->PushRenderTarget(m_TimeEffectHandle);
#else
				if (pmap.GetSign() & Nuclear::PMap::MAP_SIGN_UNDERGROUND_PALACE)
				{					
					m_pRenderer->SelectRenderEffect(Nuclear::XPRE_COLORBALANCE_2);
					m_pRenderer->SetShaderParam(Nuclear::XPRE_COLORBALANCE_2, GetTimeEffectParam());
				} else {
					m_pRenderer->SelectRenderEffect(Nuclear::XPRE_COLORBALANCE);
					m_pRenderer->SetShaderParam(Nuclear::XPRE_COLORBALANCE, GetTimeEffectParam());
				}
				
#endif
			} else {
#ifdef TIME_EFFECT_USE_RENDERTARGET
				if (m_TimeEffectHandle!=Nuclear::INVALID_PICTURE_HANDLE)
				{
					m_pRenderer->FreePicture(m_TimeEffectHandle);
					m_TimeEffectHandle = Nuclear::INVALID_PICTURE_HANDLE;
				}
#endif
			}

			m_pRenderer->PushRenderTarget(m_BackgroundSprite);
			m_pRenderer->Clear(0x00000000);
			m_pRenderer->PopRenderTarget();
			drawMovingBackground();
			drawBackground();
			drawElementLayers(delta);
			drawWaterAreaObjs();
			drawTransObjs();
			drawTriggerObjs();
			drawGrids();
			if (m_bSettingTimeEffect || m_bPlayingTime)
			{
#ifdef TIME_EFFECT_USE_RENDERTARGET
				m_pRenderer->PopRenderTarget();
				Nuclear::FRECT rect(.0f, .0f, static_cast<float>(GetSystemMetrics(SM_CXSCREEN)), 
					static_cast<float>(GetSystemMetrics(SM_CYSCREEN)));

				if (pmap.GetSign() & Nuclear::PMap::MAP_SIGN_UNDERGROUND_PALACE)
				{					
					m_pRenderer->SelectRenderEffect(Nuclear::XPRE_COLORBALANCE_2);
					m_pRenderer->SetShaderParam(Nuclear::XPRE_COLORBALANCE_2, GetTimeEffectParam());
				} else {
					m_pRenderer->SelectRenderEffect(Nuclear::XPRE_COLORBALANCE);
					m_pRenderer->SetShaderParam(Nuclear::XPRE_COLORBALANCE, GetTimeEffectParam());
				}
				
				Nuclear::DrawPictureParam paramPic;
				paramPic.handle = m_TimeEffectHandle;
				paramPic.pRect = &rect;
				paramPic.blend = Nuclear::XPTEXBM_COPY;
				m_pRenderer->DrawPicture(paramPic);
#endif
				m_pRenderer->SelectRenderEffect(Nuclear::XPRE_NULL);
			}
			
			if (m_bSettingMask || m_bPlayingTime)
			{
				if (m_MaskUri != GetMaskUri())
				{
					m_MaskUri = GetMaskUri();
					m_pRenderer->FreePicture(m_MaskPicture);
					if (m_MaskUri != L"")
					{
						m_MaskPicture = m_pRenderer->LoadPicture(m_MaskUri);
					}
				}
				if (m_MaskPicture != Nuclear::INVALID_PICTURE_HANDLE)
				{
					const Nuclear::XPMaskParam *pMaskParam = GetMaskParam();
					if (pMaskParam)
					{
						Nuclear::DrawPictureParam param;
						param.handle = m_MaskPicture;
						Nuclear::FRECT rect((float)m_ClientRect.left, (float)m_ClientRect.top, (float)m_ClientRect.right, (float)m_ClientRect.bottom);
						Nuclear::FRECT srcrect;
						int mapWidth = pmap.GetWidth();
						int mapHeight = pmap.GetHeight();
						srcrect.left = static_cast<float>(m_DrawScrollPosition.x) / mapWidth;
						srcrect.top = static_cast<float>(m_DrawScrollPosition.y) / mapHeight;
						srcrect.right = static_cast<float>(m_DrawScrollPosition.x + m_nViewportWidth) / mapWidth;
						srcrect.bottom = static_cast<float>(m_DrawScrollPosition.y + m_nViewportHeight) / mapHeight;
						Nuclear::XPCOLOR color;
						param.pRect = &rect;
						param.pSrcrect = &srcrect;
						param.pColor = &color;
						param.blend = pMaskParam->blendMode;
						switch (pMaskParam->blendMode)
						{
						case Nuclear::XPTEXBM_MODULATE:
						case Nuclear::XPTEXBM_ADDITIVE_ALPHA:
						case Nuclear::XPTEXBM_MODULATE2X:
							color.a = pMaskParam->alpha;
							break;
						case Nuclear::XPTEXBM_MUTIPLY_ALPHA:
							color.a = color.r = color.g = color.b = pMaskParam->alpha;
							break;
						}
						m_pRenderer->DrawPicture(param);
					}
				}
			}
		}
		m_pRenderer->End();
	}
}

int CDisplayMapBase::GetGroundLayerID(int layer)
{
	const std::vector<CToolsMap::TileLayerInfo>& layerInfo = GetDocument()->GetGroundLayerInfos();
	if (layerInfo.at(layer).isShow)
		return layerInfo.at(layer).layerID;
	else
		return -1;
}


CDisplayMapBase::~CDisplayMapBase(void)
{
	std::set<Nuclear::IEffectNotify*>::iterator notifyIter = m_pNotifys.begin(), niend = m_pNotifys.end();
	for (;notifyIter!=niend;++notifyIter)
	{
		delete (*notifyIter);
	}
	delete m_pSystemResourceMgr;
	std::list<Nuclear::Effect*>::iterator effiter;
	for (int i=0;i<4;++i)
	{
		for (effiter=m_TriggerEffectArray[i].begin();effiter!=m_TriggerEffectArray[i].end();++effiter)
		{
			delete (*effiter);
		}
	}
	std::vector<EffectMap>::iterator layerEffectIter = m_EffectArray.begin();
	EffectMap::iterator effIter;
	for (;layerEffectIter!=m_EffectArray.end();++layerEffectIter)
	{
		for (effIter=layerEffectIter->begin();effIter!=layerEffectIter->end();++effIter)
		{
			delete (effIter->second);
		}
	}
	std::map<Nuclear::PictureHandle, Nuclear::PicResrc*>::iterator picResrcIter = m_PictureResMap.begin();
	for (;picResrcIter!=m_PictureResMap.end();++picResrcIter)
	{
		delete (picResrcIter->second);
	}
	delete m_pConfigMgr;
	delete m_pEffectMgr;
	delete m_pSpriteMgr;
	delete m_pAnimationMgr;
	std::map<std::wstring, LPImgInfo>::iterator imgInfoIter = m_ImgInfoMap.begin();
	for (;imgInfoIter!=m_ImgInfoMap.end();++imgInfoIter)
	{
		delete(imgInfoIter->second);
	}
	std::map<std::wstring, Nuclear::PLinkedObject*>::iterator linkedObjIter = m_LinkedObjectMap.begin();
	for (;linkedObjIter!=m_LinkedObjectMap.end();++linkedObjIter)
	{
		delete (linkedObjIter->second);
	}
	std::map<std::pair<char, unsigned int>, Nuclear::DistortBase*>::iterator distIter = m_DistortMap.begin();
	for (;distIter!=m_DistortMap.end();++distIter)
	{
		delete (distIter->second);
	}
	std::map<std::wstring, Nuclear::PDistortionObject*>::iterator distObjIter = m_DistortObjectMap.begin();
	for (;distObjIter!=m_DistortObjectMap.end();++distObjIter)
	{
		delete (distObjIter->second);
	}
	
	
	DestroyEditorRenderer(m_pRenderer);
	m_pRenderer = NULL;
}




CDisplayMapBase::CDisplayMapBase(void) : m_EffectArray(Nuclear::MAP_LAYER_COUNT - 1), m_WaterAlphaMask(Nuclear::INVALID_PICTURE_HANDLE)
{
	m_SelectedMbg = -1;
	m_bUpdateLinkedObjectsEveryFrame = true;
	m_bShowEffect = true;
	m_bTransparent = false;
	m_bReadyForSprite = false;
	m_bInited = false;
	m_bShowBase = true;
	m_bSettingTimeEffect = false;
	m_bSettingMask = false;
	m_bPlayingTime = false;
	m_bIsShowSelectElementRect = false;
	m_dwTickCount = 0;
	m_fRatio = 1.0f;
	m_TransparentObject = Nuclear::INVALID_PICTURE_HANDLE;
	m_TimeEffectHandle = Nuclear::INVALID_PICTURE_HANDLE;
	m_MaskPicture = Nuclear::INVALID_PICTURE_HANDLE;
	m_ShowGridType = SGS_CLOSE;
	m_pRenderer = NULL;
	m_pSpriteMgr = NULL;
	m_pAnimationMgr = NULL;
	m_pEffectMgr = NULL;
	m_pSystemResourceMgr = NULL;

	CString FilePath = theApp.GetResPath() + _T("\\MapEditorCfg.ini");

	CString strSection = _T("VIEW");
	CString strSectionKey = _T("BaseColor");
	TCHAR value[30];
	TCHAR* stopStr;
	GetPrivateProfileString(strSection, strSectionKey, _T("FFFFFFFF"), value, 30, FilePath);
	m_BaseColor = _tcstoul(value, &stopStr, 16);

	strSectionKey = _T("GirdColor");
	GetPrivateProfileString(strSection, strSectionKey, _T("FF000000"), value, 30, FilePath);
	m_GridColor = _tcstoul(value, &stopStr, 16);

	strSectionKey = _T("BackGroundColor");
	GetPrivateProfileString(strSection, strSectionKey, _T("FF00C090"), value, 30, FilePath);
	m_BackgroundColor = _tcstoul(value, &stopStr, 16);

	for (int i=0;i<Nuclear::MAP_LAYER_COUNT;i++)
	{
		m_bIsShowLayers[i] = true;
	}
	m_bIsShowLayers[Nuclear::MAP_LAYER_COUNT] = false;
	m_bIsShowLayers[Nuclear::MAP_LAYER_COUNT+1] = false;

	m_LinkedObjectMap.clear();
	m_TreeMap.clear();

	Nuclear::XapBatch tmpEffect(this);
	for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
	{
		m_WaterEffectArray.push_back(tmpEffect);
	}
	m_pConfigMgr = new Nuclear::ConfigManager(this);
	m_pConfigMgr->Init();
	m_pConfigMgr->SetRenderNightEffectByShader(true);
	m_pEffectMgr = new Nuclear::EffectManager(this);
	m_pSpriteMgr = new Nuclear::SpriteManager(this);

	m_pAnimationMgr = new Nuclear::AniManager(this);
	if( !m_pAnimationMgr->Init() ) 
	{
		
		XPLOG_INFO(L"Init Faild.\n");
		return;
	}

	if (!m_pSpriteMgr->PreInit())
	{
		AfxMessageBox(L"精灵管理器初始化整体失败");
		return;
	}
	size_t SprCount = m_pSpriteMgr->GetInitCount();
	bool succ = true;
	for (size_t i=0;i<SprCount;++i)
	{
		if( m_pSpriteMgr->Init(i, 1) )
		{	
			XPLOG_ERROR(L"精灵管理器初始化部分失败，第 %d 号精灵有问题！", i);
			succ = false;
		}
	}
	if (!succ)
	{
		AfxMessageBox(L"精灵管理器初始化部分失败，请查看日志并且联系美术～");
	}
	if (!m_pSpriteMgr->PostInit())
	{
		AfxMessageBox(L"精灵管理器初始化失败");
		return;
	}
	m_bReadyForSprite = true;
}

