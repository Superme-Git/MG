#include "stdafx.h"
#include "c3dsprite.h"
#include "../iapp.h"
#include "../common/3dengine.h"
#include "../common/log.h"
#include "../common/recttopologylist.h"
#include "../engine/configmanager.h"
#include "../renderer/renderer.h"
#include "sprite.h"

#define SPRITE_NOTIFY_ID_PLAY_END 0

namespace Nuclear
{
	class C3DSpriteNotify : public GM::IEventHandler, public GM::IStopHandler
	{
	public:
		C3DSpriteNotify(C3DSprite* pSprite) : m_pSprite(pSprite) {}
		virtual void Process(const std::vector<int>& eventIDs) 
		{
			m_pSprite->ProcessEvent(eventIDs);
		}
		virtual void OnStop(const GM::GCString& curAction)
		{
			m_pSprite->OnActionEnd();
		}

	private:
		C3DSprite *m_pSprite;
	};

	C3DSprite::C3DSprite(Sprite* pLogicSprite) : m_pLogicSprite(pLogicSprite), m_pGSprite(NULL), 
		m_fIncline(PI/6), m_fDefaultActionSpeed(1.0f), m_fTmpActionSpeed(1.0f), m_fAutoRotationTimePerPI(0.0f),
		m_pFather(NULL), m_pHead(NULL), m_bIsLoading(false), m_bIsActive(true), m_bAlwaysUpdate(false), 
		m_bIsVisible(true),	m_nUpdateTick(-1), m_nAccumulateDelta(0), m_nCurActionTime(0),
		m_bIsDeleted(false), m_nRestNotifyCount(0)
	{
		m_pNotify = new C3DSpriteNotify(this);
		pLogicSprite->m_pEB->Add3DSprite(false);
	}

	C3DSprite::~C3DSprite(void)
	{
		ReleaseAll();
	}

	void C3DSprite::ReleaseAll()
	{
		if (!m_pLogicSprite)
			return;
		ReleaseChildren();
		if(m_pGSprite != NULL)
		{
			try{
				m_pGSprite->Unload();
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
			delete m_pGSprite;
			m_pGSprite = NULL;
		}
		if (UpdateState())
		{
			m_pLogicSprite->m_pEB->Remove3DSprite(m_bAlwaysUpdate);
		}
		delete m_pNotify;
		m_pLogicSprite = NULL;
	}

	void C3DSprite::SetUpdateEveryFrame(bool b)
	{
		if (m_bAlwaysUpdate != b)
		{
			m_bAlwaysUpdate = b;
			if (m_bIsActive)
				m_pLogicSprite->m_pEB->Modify3DSprite(b);
		}
	}

	void C3DSprite::ReleaseChildren()
	{
		ChildrenSpriteMap::iterator it = m_mapChildren.begin(), ie = m_mapChildren.end();
		for (;it!=ie;++it)
		{
			delete it->first;
		}
		m_mapChildren.clear();
		C3DEffectMap::iterator eIt = m_att3DEffect.begin(), eIe = m_att3DEffect.end();
		for (;eIt!=eIe;++eIt)
		{
			eIt->second.pEffect->DetachFromObject();
		}
		m_att3DEffect.clear();
	}

	void On3DSpriteLoadedFun(GM::ISprite* sprite, bool succeeded, void* customData)
	{
		C3DSprite* pSprite = (C3DSprite*)customData;
		pSprite->On3DSpriteLoaded(sprite, succeeded);
	}

	bool On3DSpriteComponentPrefetchFun(bool succeeded, const GM::GString& meshName, int partId, void* customData)
	{
		C3DSprite* pSprite = (C3DSprite*)customData;
		pSprite->On3DSpriteComponentPrefetch(succeeded, meshName, partId);
		return true;
	}

	void C3DSprite::On3DSpriteLoaded(GM::ISprite* sprite, bool succeed)
	{
		if (!m_bIsLoading)	//已经同步load好了？
			return;
		if (m_pGSprite != sprite)	//已经不是这个精灵了厄
			return;
		try{
			m_bIsLoading = false;
			if(!succeed)
			{
				delete m_pGSprite;
				m_pGSprite = NULL;
				XPLOG_ERROR(L"Async Load GSprite 失败 (%s)", m_pLogicSprite->GetModelName().c_str());
				ReleaseChildren();
				m_att3DEffect.clear();
				m_components.clear();
				return;
			}
			m_pGSprite->SetStopHandler(m_pNotify);
			m_pGSprite->SetActionBlendTime(0);
			m_pGSprite->SetScale(m_pLogicSprite->GetScale());
			m_pGSprite->SetAutoRotationTimePerPI(m_fAutoRotationTimePerPI);
			NuclearFPoint dir = m_pLogicSprite->m_pMovableImp->getVectorDirection();
			m_pGSprite->NuclearSetFaceDirection(GM::GVector2(dir.x, -dir.y), m_fIncline, true);
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
		ComponentMap::iterator cIt = m_components.begin(), cIe = m_components.end();
		for (;cIt!=cIe;++cIt)
		{
			SetComponent(cIt->first, cIt->second.name, cIt->second.color);
		}
		OnSetAlpha(m_pLogicSprite->m_nAlpha);
		RebindRes();
		OnHighlight(m_pLogicSprite->IsHighlight());
	}

	void C3DSprite::On3DSpriteComponentPrefetch(bool succeeded, const std::wstring& meshName, int partId)
	{
		--m_nRestNotifyCount;
		if (m_bIsDeleted)
		{
			if ( m_nRestNotifyCount == 0)
				delete this;
			return;
		}
		ComponentParam &param = m_components[partId];
		std::wstring path = L"model/" + m_pLogicSprite->m_modelName + L"/body/" + param.name + L"/" + param.name + L".skn";
		if (path == meshName && m_pGSprite)
		{
			if (succeeded)
			{
				m_pGSprite->ChangeMesh(partId, meshName);
			} else {
				m_pGSprite->ChangeMesh(partId, L"");
				param.color.data = 0xFFFFFFFF;
				param.name = L"";
			}
			m_pGSprite->SetMeshColorMask(partId, param.color.data);
		}
		
	}

	void C3DSprite::RebindRes()
	{
		if (m_bIsLoading)
			return;
		try{
			if (m_pFather)
			{
				ChildrenSpriteMap::iterator it = m_pFather->m_mapChildren.find(m_pLogicSprite);
				XPASSERT(it != m_pFather->m_mapChildren.end() && L"RebindRes");
				XPASSERT(m_pFather->m_pGSprite && L"RebindRes");
				if (!m_pFather->m_bIsLoading)
				{
					GM::GMatrix44 offset;
					offset.mat[3][0] = it->second.relpos.x;
					offset.mat[3][1] = it->second.relpos.y;
					offset.mat[3][2] = it->second.relpos.z;
					if (!m_pFather->m_pGSprite->AttachSprite(it->second.hostname, it->second.clientname, m_pGSprite, &offset))
					{
						if (!m_pFather->m_pGSprite->AttachSprite(L"", L"", m_pGSprite))
						{
							XPLOG_ERROR(L"RebindRes attach Father完全失败!父：%s 的 %s; 子：%s 的 %s", 
								m_pFather->m_pLogicSprite->GetModelName().c_str(), it->second.hostname.c_str(),
								m_pLogicSprite->GetModelName().c_str(), it->second.clientname.c_str());
						} else {
							XPLOG_ERROR(L"RebindRes attach Father失败!父：%s 的 %s; 子：%s 的 %s", 
								m_pFather->m_pLogicSprite->GetModelName().c_str(), it->second.hostname.c_str(),
								m_pLogicSprite->GetModelName().c_str(), it->second.clientname.c_str());
						}
					}
				}
			}
			ChildrenSpriteMap::iterator sIt = m_mapChildren.begin(), sIe = m_mapChildren.end();
			C3DSprite *p3DSprite = NULL;
			for (;sIt!=sIe;++sIt)
			{
				XPASSERT(sIt->first && L"RebindRes");
				p3DSprite = dynamic_cast<C3DSprite*>(sIt->first->m_pRenderableSprite);
				XPASSERT(p3DSprite && L"RebindRes");
				if (p3DSprite->m_bIsLoading)
					continue;
				if (!m_pGSprite->AttachSprite(sIt->second.hostname, sIt->second.clientname, p3DSprite->m_pGSprite))
				{
					if (!m_pGSprite->AttachSprite(L"", L"", p3DSprite->m_pGSprite))
					{
						XPLOG_ERROR(L"RebindRes attach child完全失败!父：%s 的 %s; 子：%s 的 %s", 
							m_pLogicSprite->GetModelName().c_str(), sIt->second.hostname.c_str(),
							sIt->first->GetModelName().c_str(), sIt->second.clientname.c_str());
					} else {
						XPLOG_ERROR(L"RebindRes attach child失败!父：%s 的 %s; 子：%s 的 %s", 
							m_pLogicSprite->GetModelName().c_str(), sIt->second.hostname.c_str(),
							sIt->first->GetModelName().c_str(), sIt->second.clientname.c_str());
					}
				}
			}
			C3DEffectMap::iterator eIt = m_att3DEffect.begin(), eIe = m_att3DEffect.end();
			GM::GMatrix44 offset;
			for (;eIt!=eIe;)
			{
				offset.mat[3][0] = static_cast<float>(eIt->second.relPos.x);
				offset.mat[3][1] = static_cast<float>(eIt->second.relPos.y);
				if (!eIt->second.pEffect->AttachToObject(m_pGSprite, eIt->second.hostname, offset, true))
				{
					eIt = m_att3DEffect.erase(eIt);
				} else {
					++eIt;
				}
			}
			if (m_pLogicSprite->m_strTmpActionName.size() > 0)
			{
				m_pGSprite->SetPlaySpeed(m_fTmpActionSpeed);
				m_pGSprite->PlayAction(m_pLogicSprite->m_strTmpActionName, 1);
				m_nCurActionTime = GetActionTimeByNameAndSpeed(m_pLogicSprite->m_strTmpActionName, m_fTmpActionSpeed);
			} else if (m_pLogicSprite->m_strDefaultActionName.size() > 0) {
				m_pGSprite->SetPlaySpeed(m_fDefaultActionSpeed);
				m_pGSprite->PlayAction(m_pLogicSprite->m_strDefaultActionName, 1);
				m_nCurActionTime = GetActionTimeByNameAndSpeed(m_pLogicSprite->m_strDefaultActionName, m_fDefaultActionSpeed);
			}
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
		ResetTitlePos();
		m_nUpdateTick = -1;
	}

	void C3DSprite::ResetTitlePos()
	{
		//计算头顶名字的位置，m_nTitlePos一般是个正数，外面拿到Pos之后用y去减
		C3DSprite *pRoot = GetRoot();
		if (!pRoot->m_bIsLoading && pRoot->m_pGSprite)
		{
			C3DSprite *pHead = m_pHead;
			if (pRoot->m_pHead == this)
			{
				pHead = this;
			}
			if (pHead)
			{
				if ((!pHead->m_bIsLoading) && (pHead->m_pGSprite))
				{
					pHead->m_pGSprite->Update(false, true);//先更新子再更新父
					pRoot->m_pGSprite->Update(false, true);
					GM::GVector3 g3dHeadloc = pHead->m_pGSprite->GetPosition();
					GM::GVector3 g3dRootloc = pRoot->m_pGSprite->GetPosition();
					pRoot->m_pLogicSprite->m_nTitlePos = static_cast<short>(g3dHeadloc.y - g3dRootloc.y + pHead->m_pGSprite->GetSize().y);
				}
			} else {
				m_pLogicSprite->m_nTitlePos = static_cast<short>(m_pGSprite->GetSize().y);
			}
		}
	}

	void C3DSprite::ProcessEvent(const std::vector<int>& eventIDs)
	{
		NotifySet::iterator it = m_notifies.begin(), ie = m_notifies.end();
		for (;it!=ie;++it)
		{
			(*it)->OnEvents(m_pLogicSprite, eventIDs);
		}
	}

	bool C3DSprite::SetModel(const std::wstring &modelname, bool async)
	{
		if (modelname == L"")
		{
			if(m_pGSprite)
			{
				try{
					m_pGSprite->Unload();
				} catch (ModMud::MAssertException &)
				{
					if (m_pLogicSprite->m_pEB->GetApp())
					{
						m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
					}
				}
				delete m_pGSprite;
				m_pGSprite = NULL;
			}
			ReleaseChildren();
			m_att3DEffect.clear();
			m_bIsLoading = false;
			return true;
		}
		if(NULL != m_pGSprite)
		{
			try {
				ChildrenSpriteMap::iterator sIt = m_mapChildren.begin(), sIe = m_mapChildren.end();
				C3DSprite *p3DSprite = NULL;
				for (;sIt!=sIe;++sIt)
				{
					XPASSERT(sIt->first && L"SetModel");
					p3DSprite = dynamic_cast<C3DSprite*>(sIt->first->m_pRenderableSprite);
					XPASSERT(p3DSprite && L"SetModel");
					if (!p3DSprite->m_bIsLoading)
						m_pGSprite->DetachSprite(p3DSprite->m_pGSprite);
				}
				m_pGSprite->Unload();
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
			delete m_pGSprite;
			m_pGSprite = NULL;
		}
		m_pGSprite = new GM::GSprite;
		GM::GString name = GM::GString::Printf(L"Sprite_%d", this);
		GM::GString path;
		XPASSERT(modelname.size() > 3);
		switch (modelname[2])
		{
		case 's':
			path = L"model/" + modelname + L"/default.spr";
			break;
		case 'a':
			path = L"model/anm/" + modelname + L"/" + modelname + L".anm";
			break;
		default:
			XPASSERT(false && L"switch (modelname[2])");
		}

		bool bres = false;
		std::replace( path.begin(), path.end(), L'/', L'\\');
		if (async)
		{
			m_bIsLoading = true;
			m_pGSprite->LoadAsync(path, name, On3DSpriteLoadedFun, this, GM::EFS_NATIVE, m_pNotify);
			return true;
		} else {
			m_bIsLoading = false;
			try {
				bres = m_pGSprite->Load(path, name, m_pNotify);
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
				bres = false;
			}
		}

		if(!bres)
		{
			delete m_pGSprite;
			m_pGSprite = NULL;
			XPLOG_ERROR(L"Load GSprite 失败 (%s)", modelname.c_str());
			ReleaseChildren();
			m_att3DEffect.clear();
			return false;
		}
		m_pGSprite->SetStopHandler(m_pNotify);
		m_pGSprite->SetActionBlendTime(0);
		RebindRes();
		OnHighlight(m_pLogicSprite->IsHighlight());
		return true;
	}

	void C3DSprite::SetActive(bool active)
	{
		if (m_bIsActive == active)
			return;
		bool oldState = UpdateState();
		m_bIsActive = active;
		PushPopUpdateQueue(oldState);
	}

	void C3DSprite::SetScale(float fScale)
	{
		if(NULL == m_pGSprite || m_bIsLoading)
			return;
		try {
			m_pGSprite->SetScale(fScale);
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
	}

	bool C3DSprite::SetComponent(int scid, const std::wstring& resource, uFireCOLOR color/*XRGB*/)
	{
		if(NULL == m_pGSprite)
			return false;
		ComponentParam &param = m_components[scid];
		if (m_bIsLoading)
		{
			param.name = resource;
			param.color = color;
			return true;
		}
		if (resource == L"")
		{
			m_pGSprite->ChangeMesh(scid, L"");
		} else {
			try {
				GM::GString path = L"model/" + m_pLogicSprite->m_modelName + L"/body/" + resource + L"/" + resource + L".skn";
				m_pGSprite->PreFetchMeshRes(scid, path, On3DSpriteComponentPrefetchFun, this, GM::EFS_NATIVE);
				++m_nRestNotifyCount;
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
		}
		param.name = resource;
		param.color = color;
		return true;
	}

	bool C3DSprite::GetComponent(int scid, std::wstring& resource)
	{
		ComponentMap::iterator it = m_components.find(scid);
		if (m_components.end() == it)
		{
			resource = L"";
			return false;
		}
		resource = it->second.name;
		return true;
	}

	bool C3DSprite::GetComponentColor(int scid, uFireCOLOR &color)
	{
		ComponentMap::iterator it = m_components.find(scid);
		if (m_components.end() == it)
		{
			color.data = 0xFFFFFFFF;
			return false;
		}
		color = it->second.color;
		return true;
	}

	bool C3DSprite::AttachSprite(const std::wstring &hostSocket, const std::wstring &clientSocket, Sprite* pSprite, const NuclearVector3 &relpos, int sign)
	{
		if (NULL == m_pGSprite)
			return false;
		if (NULL == pSprite)
			return false;
		if (pSprite->GetType() != XPSPRITETYPE_3D)
			return false;
		C3DSprite *p3DChildSprite = dynamic_cast<C3DSprite*>(pSprite->m_pRenderableSprite);
		XPASSERT(NULL != p3DChildSprite);
		XPASSERT(NULL != p3DChildSprite->m_pGSprite);
		XPASSERT(m_mapChildren.find(pSprite) == m_mapChildren.end());

		if (!(p3DChildSprite->m_bIsLoading || m_bIsLoading))
		{
			try {
				p3DChildSprite->m_pGSprite->SetPosition(GM::GVector3());
				GM::GMatrix44 offset;
				offset.mat[3][0] = relpos.x;
				offset.mat[3][1] = relpos.y;
				offset.mat[3][2] = relpos.z;
				if (!m_pGSprite->AttachSprite(hostSocket, clientSocket, p3DChildSprite->m_pGSprite, &offset))
				{
					if (!m_pGSprite->AttachSprite(L"", L"", p3DChildSprite->m_pGSprite))
					{
						XPLOG_ERROR(L"AttachSprite完全失败! 父：%s 的 %s; 子：%s 的 %s", 
							m_pLogicSprite->GetModelName().c_str(), hostSocket.c_str(),
							pSprite->GetModelName().c_str(), clientSocket.c_str());
					} else {
						XPLOG_ERROR(L"AttachSprite失败! 父：%s 的 %s; 子：%s 的 %s", 
							m_pLogicSprite->GetModelName().c_str(), hostSocket.c_str(),
							pSprite->GetModelName().c_str(), clientSocket.c_str());
					}
				}
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
		}
		pSprite->SetAlpha(m_pLogicSprite->m_nAlpha);
		bool oldState = p3DChildSprite->UpdateState();
		p3DChildSprite->m_pFather = this;
		p3DChildSprite->PushPopUpdateQueue(oldState);
		AttachSpriteSockets &sockets = m_mapChildren[pSprite];
		sockets.clientname = clientSocket;
		sockets.hostname = hostSocket;
		sockets.relpos = relpos;
		if (sign && XPSAS_TITLE)
		{
			m_pHead = p3DChildSprite;
			SetHead();
			ResetTitlePos();
		} else {
			if (p3DChildSprite->m_pHead)
			{
				m_pHead = p3DChildSprite->m_pHead;
				SetHead();
				ResetTitlePos();
			}
		}
		p3DChildSprite->OnHighlight(m_pLogicSprite->IsHighlight());

		return true;
	}

	bool C3DSprite::DetachSprite(Sprite* pSprite)
	{
		if (NULL == m_pGSprite)
			return false;
		if (NULL == pSprite)
			return false;
		if (pSprite->GetType() != XPSPRITETYPE_3D)
			return false;
		ChildrenSpriteMap::iterator iter = m_mapChildren.find(pSprite);
		if (iter == m_mapChildren.end())
			return false;
		C3DSprite *p3DChildSprite = dynamic_cast<C3DSprite*>(pSprite->m_pRenderableSprite);
		XPASSERT(NULL != p3DChildSprite);
		XPASSERT(NULL != p3DChildSprite->m_pGSprite);
		if (!(p3DChildSprite->m_bIsLoading || m_bIsLoading))
		{
			try {
				m_pGSprite->DetachSprite(p3DChildSprite->m_pGSprite);
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
		}
		bool oldState = p3DChildSprite->UpdateState();
		p3DChildSprite->m_pFather = NULL;
		p3DChildSprite->PushPopUpdateQueue(oldState);
		
		m_mapChildren.erase(iter);
		if (m_pHead == p3DChildSprite || m_pHead == p3DChildSprite->m_pHead)
		{
			m_pHead = NULL;
			SetHead();
			ResetTitlePos();
			p3DChildSprite->ResetTitlePos();
		}
		return true;
	}

	void C3DSprite::SetHead()
	{
		if (m_pFather)
		{
			m_pFather->m_pHead = m_pHead;
			m_pFather->SetHead();
		}
	}

	bool C3DSprite::SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame)
	{
		if (fScaleForTotalTime <= 0.0f)
			fScaleForTotalTime = 1.0f;
		m_fDefaultActionSpeed = 1.0f / fScaleForTotalTime;
		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_HOLD_LAST_FRAME, bHoldLastFrame);
		if(NULL == m_pGSprite)
			return false;
		if (m_bIsLoading)
			return true;
		m_pGSprite->SetPlaySpeed(m_fDefaultActionSpeed);
		if (m_pLogicSprite->m_strTmpActionName.size() == 0)
		{
			try {
				if (m_pGSprite->PlayAction(action_name, 1))
				{
					m_nCurActionTime = GetActionTimeByNameAndSpeed(action_name, m_fDefaultActionSpeed);
					return true;
				}
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
		} else {
			return true;
		}

		return false;
	}

	bool C3DSprite::PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime)
	{
		if (fScaleForTotalTime <= 0.0f)
			fScaleForTotalTime = 1.0f;
		m_fTmpActionSpeed = 1.0f / fScaleForTotalTime;
		if(NULL == m_pGSprite)
			return false;
		if (m_bIsLoading)
			return true;
		m_pGSprite->SetPlaySpeed(m_fTmpActionSpeed);
		try {
			if (m_pGSprite->PlayAction(action_name, 1))
			{
				m_nCurActionTime = GetActionTimeByNameAndSpeed(action_name, m_fTmpActionSpeed);
				return true;
			}
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
		return false;
	}

	bool C3DSprite::GetActionTimeByName(const std::wstring& action_name, float &time)
	{
		if ((NULL == m_pGSprite) || m_bIsLoading)
			return false;
		try {
			return m_pGSprite->GetActionTimeByName(action_name, time);
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
		return false;
	}

	int C3DSprite::GetActionTimeByNameAndSpeed(const std::wstring& act_name, float speed)
	{
		XPASSERT((NULL != m_pGSprite) && !m_bIsLoading);
		float time;
		try {
			if (m_pGSprite->GetActionTimeByName(act_name, time))
			{
				return static_cast<int>(time * speed * 1000);
			}
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
		return 0;
	}

	void C3DSprite::OnHighlight(bool isHighlight)
	{
		if (NULL == m_pGSprite)
			return;
		try {
			m_pGSprite->SetColorMaskScale(isHighlight?1.5f:1.0f, true);
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
	}

	void C3DSprite::RenderSprite(BOOL renderAttachedEffect, Canvas* canvas, FRECT *pOutrect, bool isAlpha)
	{
		if ((NULL == m_pGSprite) || m_pFather || m_bIsLoading)
			return;
		Renderer *pRenderer = m_pLogicSprite->m_pEB->GetRenderer();
		if (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_IS_SOLID_MASK))
		{
			//在XPSTT_ELEMENT_TRANSLUCENT下，一帧会绘制两次，一次实的，一次虚的
			ConfigManager *pCfgMan = m_pLogicSprite->m_pEB->GetConfigManager();
			if (pCfgMan)
			{
				if (isAlpha)
				{
					OnSetAlpha(static_cast<unsigned char>(pCfgMan->GetMaskAlpha() * m_pLogicSprite->m_nAlpha));
				} else {
					OnSetAlpha(m_pLogicSprite->m_nAlpha);
				}
			}
		}
		try {
			GM::GNuclear::SetDefaultRenderState();
			m_pGSprite->NuclearRender(renderAttachedEffect, TRUE);
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
		pRenderer->ResumeFrom3DRender();
		if (pOutrect)
		{
			XDisplayMode dm = pRenderer->GetDisplayMode();
			pOutrect->left = static_cast<float>(INT_MAX);
			pOutrect->top = pOutrect->left;
			pOutrect->bottom = static_cast<float>(INT_MIN);
			pOutrect->right = pOutrect->bottom;
			if (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_SCREEN_COORD))
			{
				GetMaxRect(CPOINT(dm.width >> 1, dm.height >> 1), *pOutrect);
			} else {
				GetMaxRect(m_pLogicSprite->m_pEB->GetViewport().Center(), *pOutrect);
			}
			*pOutrect += NuclearFPoint(static_cast<float>(dm.width >> 1), static_cast<float>(dm.height >> 1));
			//因为是骨骼的，留点余量
			pOutrect->left -= 20.0f;
			pOutrect->right += 20.0f;
			pOutrect->top -= 20.0f;
			pOutrect->bottom += 5.0f;
			//////画包围盒
			//Renderer *pRenderer = m_pLogicSprite->m_pEB->GetRenderer();
			//pRenderer->DrawBox(*pOutrect, 0xFFFFFFFF, XPFM_WIREFRAME);
		}
		if (canvas)
		{
			X3DRenderCanvas(m_pLogicSprite, canvas);
		}
	}

	void C3DSprite::RenderChildSpriteEffectFront()
	{
		ChildrenSpriteMap::iterator it = m_mapChildren.begin(), ie = m_mapChildren.end();
		for (;it!=ie;++it)
		{
			it->first->RenderEffectFront(NULL);
		}
	}

	void C3DSprite::RenderChildSpriteEffectBack()
	{
		ChildrenSpriteMap::iterator it = m_mapChildren.begin(), ie = m_mapChildren.end();
		for (;it!=ie;++it)
		{
			it->first->RenderEffectBack(NULL);
		}
	}

	void C3DSprite::Render3DFrontEffect()
	{
		if ((NULL == m_pGSprite) || m_bIsLoading || m_pFather)
			return;

		//位置肯定是设好了的
		try {
			GM::GNuclear::SetDefaultRenderState();
			m_pGSprite->NuclearRender(TRUE, FALSE);
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}
		m_pLogicSprite->m_pEB->GetRenderer()->ResumeFrom3DRender();
	}

	void C3DSprite::X3DRenderCanvas(Sprite *pSprite, Canvas* canvas)
	{
		if ((NULL == m_pGSprite) || m_bIsLoading)
			return;
		GM::GAABox aabox = m_pGSprite->GetAnimBox();
		canvas->Draw(pSprite, NuclearRect((int)aabox.low.x, -(int)aabox.high.y, 
			(int)aabox.high.x, -(int)aabox.low.y));
		ChildrenSpriteMap::iterator it = m_mapChildren.begin(), ie = m_mapChildren.end();
		Sprite *pChildSprite = NULL;
		for (;it!=ie;++it)
		{
			pChildSprite = it->first;
			XPASSERT(pChildSprite != NULL && L"C3DSprite::X3DRenderCanvas");
			XPASSERT(pChildSprite->m_pRenderableSprite != NULL && L"C3DSprite::X3DRenderCanvas");
			pChildSprite->m_pRenderableSprite->X3DRenderCanvas(pSprite, canvas);
		}
	}

	void C3DSprite::GetMaxRect(const Location &vpCenter, FRECT &outrect)
	{
		if ((NULL == m_pGSprite) || m_bIsLoading)
			return;

		GM::GAABox aabox = m_pGSprite->GetAnimBox();
		aabox.low.x -= vpCenter.x;
		aabox.high.x -= vpCenter.x;
		aabox.low.y += vpCenter.y;
		aabox.high.y += vpCenter.y;
		if (aabox.low.x < outrect.left)
		{
			outrect.left = aabox.low.x;
		}
		if (-aabox.high.y < outrect.top)
		{
			outrect.top = -aabox.high.y;
		}
		if (aabox.high.x > outrect.right)
		{
			outrect.right = aabox.high.x;
		}
		if (-aabox.low.y > outrect.bottom)
		{
			outrect.bottom = -aabox.low.y;
		}
		ChildrenSpriteMap::iterator it = m_mapChildren.begin(), ie = m_mapChildren.end();
		Sprite *pSprite = NULL;
		for (;it!=ie;++it)
		{
			pSprite = it->first;
			XPASSERT(pSprite != NULL && L"C3DSprite::GetMaxRect");
			XPASSERT(pSprite->m_pRenderableSprite != NULL && L"C3DSprite::GetMaxRect");
			pSprite->m_pRenderableSprite->GetMaxRect(vpCenter, outrect);
		}
	}

	void C3DSprite::GetBase(SortBaseRectType &base)
	{
		if(NULL == m_pGSprite)
			return;
		CPOINT loc = m_pLogicSprite->GetLocation();
		CPOINT tmpCPoint;
		TransToDiamondRadix(loc.x - 50, loc.y, tmpCPoint);
		base.left = tmpCPoint.x;
		base.bottom = tmpCPoint.y;
		TransToDiamondRadix(loc.x + 50, loc.y, tmpCPoint);
		base.right = tmpCPoint.x;
		base.top = tmpCPoint.y;
		base.srcRect.left = loc.x - 100;
		base.srcRect.right = loc.x + 100;
		base.srcRect.bottom = loc.y + 50;
		base.srcRect.top = loc.y - 150;
		base.y = loc.y;
	}

	void C3DSprite::UpdateAction(DWORD ticktime)
	{
		if (m_deledEffect.size() > 0)
		{
			std::list<IEffect*>::iterator it = m_deledEffect.begin(), ie = m_deledEffect.end();
			for (;it!=ie;++it)
			{
				m_att3DEffect.erase(*it);
			}
			m_deledEffect.clear();
		}
		if ((NULL == m_pGSprite) || m_bIsLoading)
			return;
		if (m_pFather)
		{
			GM::GVector3 pos = m_pGSprite->GetPosition();
			Location xppos(static_cast<int>(pos.x), static_cast<int>(-pos.y));
			m_pLogicSprite->SetLocation(xppos);
			NuclearFPoint dir = m_pFather->m_pLogicSprite->GetVectorDirection();
			xppos += dir;
			m_pLogicSprite->SetDirection(xppos);
		}
		m_nAccumulateDelta += ticktime;
		if (UpdateState())
		{
			Update3DPos();
			if (m_pLogicSprite->m_pEB->TestUpdate3DSprite(m_nUpdateTick, m_bAlwaysUpdate))
			{
				try {
					m_pGSprite->UpdateNodeByElapseTime(m_nAccumulateDelta / 1000.0f, FALSE);
					m_pGSprite->ApplyDelayedAnimation();
				} catch (ModMud::MAssertException &)
				{
					if (m_pLogicSprite->m_pEB->GetApp())
					{
						m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
					}
				}
				m_nAccumulateDelta = 0;
			}
		}
		if (m_nAccumulateDelta >= m_nCurActionTime)
		{
			m_nAccumulateDelta -= m_nCurActionTime;//消耗一个动作
			OnActionEnd();
		}
		ChildrenSpriteMap::iterator it = m_mapChildren.begin(), ie = m_mapChildren.end();
		{
			for (;it!=ie;++it)
			{
				it->first->UpdateAction(ticktime);
			}
		}
	}

	void C3DSprite::Update3DPos()
	{
		if ((NULL == m_pGSprite) || m_pFather || m_bIsLoading)
			return;

		NuclearFPoint dir = m_pLogicSprite->m_pMovableImp->getVectorDirection();
		float sinIncline = sin(m_fIncline);
		try {
			m_pGSprite->NuclearSetFaceDirection(GM::GVector2(dir.x, (sinIncline==0.0f)?(-dir.y):(-dir.y/sinIncline)), m_fIncline);
			Location loc = m_pLogicSprite->GetLocation();
			GM::GVector3 pos(static_cast<float>(loc.x), static_cast<float>(-loc.y), loc.y * -XP3DOBJ_THICKNESS);
			m_pGSprite->SetPosition(pos);
			m_pGSprite->SetScale(m_pLogicSprite->GetScale() / m_pLogicSprite->m_pEB->GetWorldScale());
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
		}

		if (m_pHead && m_pHead->m_pGSprite)
		{
			try {
				m_pHead->m_pGSprite->Update(false);
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
		}
	}


	void C3DSprite::OnActionEnd()
	{
		IApp *pApp = m_pLogicSprite->m_pEB->GetApp();
		if (m_pLogicSprite->m_strTmpActionName.size() > 0)
		{
			if (m_pLogicSprite->m_strDefaultActionName.size() > 0)
			{
				m_pGSprite->SetPlaySpeed(m_fDefaultActionSpeed);
				m_pLogicSprite->PlayDefaultAction();
				try {
					m_pGSprite->PlayAction(m_pLogicSprite->m_strDefaultActionName, 1);
				} catch (ModMud::MAssertException &)
				{
					if (m_pLogicSprite->m_pEB->GetApp())
					{
						m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
					}
				}
				m_nCurActionTime = GetActionTimeByNameAndSpeed(m_pLogicSprite->m_strDefaultActionName, m_fDefaultActionSpeed);
				m_pLogicSprite->m_strTmpActionName = L"";
			}
			if (pApp)
				pApp->OnUpdateSpriteAction(m_pLogicSprite, XPUSAT_PLAYACTIONFINISH);
		} else {
			if (!m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_HOLD_LAST_FRAME))
			{
				m_pLogicSprite->PlayDefaultAction();
				try {
					m_pGSprite->PlayAction(m_pLogicSprite->m_strDefaultActionName, 1);
				} catch (ModMud::MAssertException &)
				{
					if (m_pLogicSprite->m_pEB->GetApp())
					{
						m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
					}
				}
				m_nCurActionTime = GetActionTimeByNameAndSpeed(m_pLogicSprite->m_strDefaultActionName, m_fDefaultActionSpeed);
			}
			if (pApp)
				pApp->OnUpdateSpriteAction(m_pLogicSprite, XPUSAT_DEFAULTACTIONFINISH);
		}
	}

	// 3D模型的转向速度
	void C3DSprite::SetAutoRotationTimePerPI(float fTimePerPi)
	{
		m_fAutoRotationTimePerPI = fTimePerPi;
		if (m_pGSprite && !m_bIsLoading)
			m_pGSprite->SetAutoRotationTimePerPI(fTimePerPi);
	}

	void C3DSprite::OnSetAlpha(unsigned char a)
	{
		if ((!m_pGSprite) || m_bIsLoading)
			return;
		ComponentMap::iterator cIt = m_components.begin(), cIe = m_components.end();
		uFireCOLOR color;
		color.a = a;
		m_pGSprite->SetColorMask(color.data);
		for (;cIt!=cIe;++cIt)
		{
			cIt->second.color.a = a;
			m_pGSprite->SetMeshColorMask(cIt->first, cIt->second.color.data);
		}
		ChildrenSpriteMap::iterator sIt = m_mapChildren.begin(), sIe = m_mapChildren.end();
		for (;sIt!=sIe;++sIt)
		{
			sIt->first->SetAlpha(a);
		}
	}

	bool C3DSprite::SetDurativeEffect(XHardRef<C3DEffect> pEffect, const CPOINT& relpos, const std::wstring &hostname, bool async)
	{
		GM::GMatrix44 offset;
		offset.mat[3][0] = static_cast<float>(relpos.x);
		offset.mat[3][1] = static_cast<float>(relpos.y);
		pEffect->AssureResource(async);
		pEffect->SetLoopNum(-1);
		bool result = false;
		if (m_bIsLoading)
		{
			result = true;
		} else {
			try {
				result = pEffect->AttachToObject(m_pGSprite, hostname, offset, async);
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
		}
		if (result)
		{
			C3DEffectStruct &effStruct = m_att3DEffect[pEffect.operator ->()];
			effStruct.pEffect = pEffect;
			effStruct.hostname = hostname;
			effStruct.relPos = relpos;
			return true;
		}
		return false;
	}

	bool C3DSprite::RemoveEffect(IEffect *pEffect)
	{
		if (m_att3DEffect.find(pEffect) != m_att3DEffect.end())
		{
			pEffect->Stop();
			m_deledEffect.push_back(pEffect);
			return true;
		}
		return false;
	}

	bool C3DSprite::ChangeEffectOffset(IEffect* pEffect, const CPOINT &relpos)
	{
		C3DEffectMap::iterator it = m_att3DEffect.find(pEffect);
		if (it != m_att3DEffect.end())
		{
			it->second.pEffect->SetRel3DLocation(relpos);
			it->second.relPos = relpos;
			return true;
		}
		return false;
	}

	bool C3DSprite::MoveDurativeEffectToOtherSprite(IEffect *pEffect, Sprite *pSprite, const CPOINT& relpos, const std::wstring &hostname)
	{
		//既然有hostname，那么肯定就是3D绑法了
		C3DEffectMap::iterator it = m_att3DEffect.find(pEffect);
		if (it != m_att3DEffect.end())
		{
			it->second.pEffect->DetachFromObject();
			m_deledEffect.push_back(pEffect);
			if (pSprite->m_pRenderableSprite->SetDurativeEffect(it->second.pEffect, relpos, hostname, true))
				return true;
			//失败了也不加回来了
		}
		return false;
	}

	void C3DSprite::PlayEffect(C3DEffect *pEffect, const CPOINT& relpos, int times, const std::wstring &hostname, bool async)
	{
		GM::GMatrix44 offset;
		offset.mat[3][0] = static_cast<float>(relpos.x);
		offset.mat[3][1] = static_cast<float>(relpos.y);
		pEffect->AssureResource(async);
		pEffect->SetLoopNum(times);
		bool result = false;
		if (m_bIsLoading)
		{
			result = true;
		} else {
			try {
				result = pEffect->AttachToObject(m_pGSprite, hostname, offset, async);
			} catch (ModMud::MAssertException &)
			{
				if (m_pLogicSprite->m_pEB->GetApp())
				{
					m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
				}
			}
		}
		if (result)
		{
			C3DEffectStruct &effStruct = m_att3DEffect[pEffect];
			effStruct.pEffect = XHardRef<C3DEffect>(pEffect);
			effStruct.hostname = hostname;
			effStruct.relPos = relpos;
			pEffect->AddNotify(this);
			return;
		}
		delete pEffect;
	}

	//只有PlayEffect里面的Effect才注册进Notify
	void C3DSprite::OnStop(IEffect *pEffect)
	{
		m_deledEffect.push_back(pEffect);
	}

	bool C3DSprite::GetSocketPos(const std::wstring &socket, CPOINT &pos) const
	{
		if (m_bIsLoading || !m_pGSprite)
			return false;
		GM::GVector3 gmpos;
		try {
			if (!m_pGSprite->GetSocketPosition(socket, gmpos))
				return false;
		} catch (ModMud::MAssertException &)
		{
			if (m_pLogicSprite->m_pEB->GetApp())
			{
				m_pLogicSprite->m_pEB->GetApp()->On3DAssertErr();
			}
			return false;
		}
		pos.x = static_cast<int>(gmpos.x);
		pos.y = static_cast<int>(-gmpos.y);
		return true;
	}

	void C3DSprite::OnSetVisible()
	{
		if (m_bIsVisible != (m_pLogicSprite->IsVisiable() && m_pLogicSprite->IsEntityVisible()))
		{
			bool oldState = UpdateState();
			m_bIsVisible = !m_bIsVisible;
			PushPopUpdateQueue(oldState);
		}
	}

	void C3DSprite::DumpAllActionNames(std::vector<std::wstring> &actions) const
	{
		if (m_pGSprite)
		{
			int count = m_pGSprite->GetActionCount();
			for (int i=0;i<count;++i)
			{
				actions.push_back(m_pGSprite->GetActionName(i));
			}
		}
	}

	//检测是否加入更新队列的几个条件，全满足返回true，否则false
	//改变了条件之前和之后分别调用，如果两次结果不同，则进行更新队列移入/移出操作
	bool C3DSprite::UpdateState() const
	{
		if (!m_bIsActive)
			return false;
		if (m_pFather != NULL)
			return false;
		if (!m_bIsVisible)
			return false;
		return true;
	}

	void C3DSprite::PushPopUpdateQueue(bool oldState)
	{
		if (oldState != UpdateState()) //需要更新更新队列
		{
			if (oldState)	//原来在更新队列里面呢
			{
				m_pLogicSprite->m_pEB->Remove3DSprite(m_bAlwaysUpdate);
			} else {
				m_pLogicSprite->m_pEB->Add3DSprite(m_bAlwaysUpdate);
			}
		}
	}
}