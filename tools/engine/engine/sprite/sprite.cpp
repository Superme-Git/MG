#include "stdafx.h"
#include "..\effect\sebind.h"
#include "..\effect\effectmanager.h"
#include "..\effect\xapeffect.h"
#include "..\world\world.h"
#include "..\soundarea\stepsoundtype.h"
#include "..\iapp.h"
#include "astar/astar.hpp" // share
#include "sprite.h"

namespace Nuclear
{
	enum XPSPREFF_FLAG_EX
	{
		XPSPREFFFLAG_DELETED = 1 << 31,	//已经删除了
	};

	class SpriteEffectNotify : public IEffectNotify, public EffectGC
	{
	public:
		Sprite* pSprite;
		int dx;
		int dy;
		unsigned flag;
		bool isSetEffect;//外面有没有effect的指针

		virtual void OnDelete(IEffect* pEffect) override
		{
			//pEffect->RemoveNotify(this);都要删除了，就不用了
			if (pSprite)
			{
				pSprite->m_sprEffNotifys.erase(this);
				if (isSetEffect)
					pSprite->m_sprEffNotMap.erase(pEffect);
			}
			delete this;
		}
		virtual void OnAsyncLoaded(IEffect* pEffect, bool succeeded) override
		{
			pEffect->RemoveNotify(this);
			if (pSprite)
			{
				Effect *tpEffect = (Effect*)pEffect;
				pSprite->m_sprEffNotifys.erase(this);
				if (succeeded)
				{
					if (flag & XPSPREFFFLAG_BIND_AUTO)
					{
						flag &= ~XPSPREFFFLAG_BIND_AUTO;
						if (pEffect->GetEffectBindType() == XPEBT_EFFECT_POS)
							flag |= XPSPREFFFLAG_BIND_EFF_POS;
					}
					Sprite::EffectInfo info(flag, dx, dy, tpEffect);
					if (isSetEffect)
					{
						pSprite->m_mapDuraEffects[tpEffect] = info;
						pSprite->m_sprEffNotMap.erase(pEffect);
					} else {
						pSprite->m_lstTmpEffects.push_back(info);
					}
					if ((flag & XPSPREFFFLAG_NO_FOLLOW_SCALE) == 0)
					{
						tpEffect->SetScale(pSprite->m_fScale);
					}
					tpEffect->SetScaleType(XPEST_ONLY_SCALE_POS);
					
				} else {
					if (isSetEffect)
					{
						pSprite->m_sprEffNotMap.erase(pEffect);
					} else {
						RemoveEffect(pEffect);
					}
				}
			} else {
				RemoveEffect(pEffect);
			}
			delete this;
		}

	};

	Sprite::Sprite(EngineBase *pEB) : EntitativeObj(pEB), m_nPropFlag(0), m_iCurFrame(0), m_iAniDurationTime(1000), 
		m_surfaceType(WST_ALWAYS_PLANE), m_iDefaultAniDurationTime(0), m_iAniStartTime(0), m_nTitlePos(0), 
		m_actionTypeFlag(0), m_defaultActionTypeFlag(0), m_nHorseHeight(0), m_nAlpha(255), m_nTeleportStartTime(0), 
		m_soundActorName(L"")
	{
		SetPropFlag(XPSPPF_IS_COVERD_ALPHA, true);
		SetPropFlag(XPSPPF_USE_RENDER_TARGET, true);
		m_fScale = 1.0f;
		m_pMovableImp = new MovableSpriteImp(this, pEB); // 只有一个实现
	}

	Sprite::~Sprite()
	{
		SpriteEffectNotifySet::iterator it = m_sprEffNotifys.begin(), ie = m_sprEffNotifys.end();
		for (;it!=ie;++it)
		{
			(*it)->pSprite = NULL;
		}
		delete m_pMovableImp;
	}

	void Sprite::SetRideName(const std::wstring& name)
	{
		ResetStepAction(name);
		if (name == L"")
		{
			m_nHorseHeight = 0;
			SetPropFlag(XPSPPF_IS_READING, false);
			ResetEffectPos();
			return;
		}
		ResetHorseHeight(name);
		ResetEffectPos();
		SetPropFlag(XPSPPF_IS_READING, true);
	}

	void Sprite::EnableStepSound(bool b)
	{
		if (TestPropFlag(XPSPPF_HAS_SET_STEP_SOUND) != b)
		{
			SetPropFlag(XPSPPF_HAS_SET_STEP_SOUND, b);
			StepSoundType* pSST = m_pEngine->GetStepSoundType();
			if (!pSST)
				return;
			const XPSSModelMap &mmap = pSST->GetModelMap();
			XPSSModelMap::const_iterator mIt = mmap.find(GetModelName());
			if (mIt == mmap.end())
				return;
			XPStepNotifies::const_iterator nIt = mIt->second.notifies.begin(),
				nIe = mIt->second.notifies.end();
			World* pWorld = m_pEngine->GetXPWorld();
			if (b)
			{
				for (;nIt!=nIe;++nIt)
				{
					RegisterFrameNotify(pWorld, nIt->actname, nIt->frame);
				}
			} else {
				for (;nIt!=nIe;++nIt)
				{
					DeregisterFrameNotify(pWorld, nIt->actname, nIt->frame);
				}
			}
		}
	}
	
	bool Sprite::SetBindFile(int id, const std::wstring &bindFile)
	{
		if (id < 0 || id > 1)
			return false;
		if (m_strBindFile[id] == bindFile)
			return true;
		if (bindFile == L"")
		{
			m_Sebind[id] = XHardRef<Sebind>();
			m_strBindFile[id] = bindFile;
			return true;
		}
		m_Sebind[id] = m_pEngine->GetEffectManager()->GetSebindManager()->GetSebind(bindFile);
		if (!m_Sebind[id].IsValid())
		{
			m_strBindFile[id] = L"";
			return false;
		}
		m_strBindFile[id] = bindFile;
		return true;
	}

	void Sprite::SetScale(float scale)
	{
		m_fScale = scale;
		EffectInfoList::iterator lIt = m_lstTmpEffects.begin(), lIe = m_lstTmpEffects.end();
		for (;lIt!=lIe;++lIt)
		{
			if (lIt->flag & XPSPREFFFLAG_NO_FOLLOW_SCALE)
				continue;
			lIt->effect->SetScale(scale);
		}
		EffectInfoMap::iterator mIt = m_mapDuraEffects.begin(), mIe = m_mapDuraEffects.end();
		for (;mIt!=mIe;++mIt)
		{
			if (mIt->second.flag & XPSPREFFFLAG_NO_FOLLOW_SCALE)
				continue;
			mIt->second.effect->SetScale(scale);
		}
	}

	void Sprite::PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype)
	{
		Effect *tpEffect = m_pEngine->GetEffectManager()->CreateEffect(effect_name, false);
		if( tpEffect )
		{
			if (flag & XPSPREFFFLAG_NO_SOUND)
				tpEffect->RemoveAudio();
			else
				tpEffect->SetSoundType(soundtype);
			tpEffect->SetLoopNum(times);
			switch (tpEffect->AssureResource((flag & XPSPREFFFLAG_ASYNC) != 0))
			{
			case XPEARR_RESREADY:
				{
					tpEffect->Play();
					EffectInfo info(flag, dx, dy, tpEffect);
					m_lstTmpEffects.push_back(info);
				}
				break;
			case XPEARR_LOADING:
				{
					SpriteEffectNotify* pNotify = new SpriteEffectNotify();
					pNotify->flag = flag;
					pNotify->dx = dx;
					pNotify->dy = dy;
					pNotify->pSprite = this;
					pNotify->isSetEffect = false;
					tpEffect->AddNotify(pNotify);
					m_sprEffNotifys.insert(pNotify);
				}
				break;
			case XPEARR_LOAD_ERROR:
				delete tpEffect;
				return;
				break;
			}
			if ((flag & XPSPREFFFLAG_NO_FOLLOW_SCALE) == 0)
			{
				tpEffect->SetScale(m_fScale);
			}
			tpEffect->SetScaleType(XPEST_ONLY_SCALE_POS);
		}
	}

	IEffect* Sprite::SetDurativeEffect(const std::wstring &effect_name,  const CPOINT& relpos, unsigned int flag)
	{
		Effect* tpEffect = m_pEngine->GetEffectManager()->CreateEffect(effect_name, true);
		if( tpEffect )
		{
			if (flag & XPSPREFFFLAG_NO_SOUND)
				tpEffect->RemoveAudio();
			switch (tpEffect->AssureResource((flag & XPSPREFFFLAG_ASYNC) != 0))
			{
			case XPEARR_RESREADY:
				{
					tpEffect->Play();
					if (flag & XPSPREFFFLAG_BIND_AUTO)
					{
						flag &= ~XPSPREFFFLAG_BIND_AUTO;
						if (tpEffect->GetEffectBindType() == XPEBT_EFFECT_POS)
							flag |= XPSPREFFFLAG_BIND_EFF_POS;
					}
					EffectInfo info(flag, relpos.x, relpos.y, tpEffect);
					m_mapDuraEffects[tpEffect] = info;
				}
				break;
			case XPEARR_LOADING:
				{
					SpriteEffectNotify* pNotify = new SpriteEffectNotify();
					pNotify->flag = flag;
					pNotify->dx = relpos.x;
					pNotify->dy = relpos.y;
					pNotify->pSprite = this;
					pNotify->isSetEffect = true;
					tpEffect->AddNotify(pNotify);
					m_sprEffNotifys.insert(pNotify);
					m_sprEffNotMap[tpEffect] = pNotify;
				}
				break;
			case XPEARR_LOAD_ERROR:
				delete tpEffect;
				return NULL;
				break;
			}
			if ((flag & XPSPREFFFLAG_NO_FOLLOW_SCALE) == 0)
			{
				tpEffect->SetScale(m_fScale);
			}
			tpEffect->SetScaleType(XPEST_ONLY_SCALE_POS);
			return tpEffect;
		}
		return NULL;
	}

	IEffect* Sprite::SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const CPOINT& relpos, unsigned int flag)
	{
		Effect* tpEffect = m_pEngine->GetEffectManager()->CreateListEffect(headEffect, midEffect, endEffect, continueTime, true, (flag & XPSPREFFFLAG_ASYNC) != 0, (flag & XPSPREFFFLAG_NO_SOUND) != 0);
		if( tpEffect )
		{
			if (tpEffect->GetPlayState() == XPES_EMPTY)
			{
				SpriteEffectNotify* pNotify = new SpriteEffectNotify();
				pNotify->flag = flag;
				pNotify->dx = relpos.x;
				pNotify->dy = relpos.y;
				pNotify->pSprite = this;
				pNotify->isSetEffect = true;
				tpEffect->AddNotify(pNotify);
				m_sprEffNotifys.insert(pNotify);
				m_sprEffNotMap[tpEffect] = pNotify;
			} else {
				tpEffect->Play();
				if (flag & XPSPREFFFLAG_BIND_AUTO)
				{
					flag &= ~XPSPREFFFLAG_BIND_AUTO;
					if (tpEffect->GetEffectBindType() == XPEBT_EFFECT_POS)
						flag |= XPSPREFFFLAG_BIND_EFF_POS;
				}
				EffectInfo info(flag, relpos.x, relpos.y, tpEffect);
				m_mapDuraEffects[tpEffect] = info;
			}
			if ((flag & XPSPREFFFLAG_NO_FOLLOW_SCALE) == 0)
			{
				tpEffect->SetScale(m_fScale);
			}
			tpEffect->SetScaleType(XPEST_ONLY_SCALE_POS);
		}
		return tpEffect;

	}

	void Sprite::RemoveDurativeEffect(IEffect* pEffect)
	{
		EffectInfoMap::iterator it = m_mapDuraEffects.find(pEffect);
		if (it != m_mapDuraEffects.end())
		{
			pEffect->ClearNotify();
			pEffect->Stop();
			it->second.flag = XPSPREFFFLAG_DELETED;
		} else {
			std::map<IEffect*, SpriteEffectNotify*>::iterator mIt = m_sprEffNotMap.find(pEffect);
			if (mIt != m_sprEffNotMap.end())//还没有加载完呢，就删掉了
			{
				m_sprEffNotifys.erase(mIt->second);
				mIt->second->pSprite = NULL;
			}
		}
	}

	void Sprite::UpdateEffect(DWORD tickTime)
	{
		static float HALF_RADICAL_2 = 0.70710678118654752440084436210485f;//同时也是cos45 、sin45
		static float COS[8] = { -1.0f, -HALF_RADICAL_2, 0.0f, HALF_RADICAL_2,  1.0f, HALF_RADICAL_2, 0.0f, -HALF_RADICAL_2};
		static float SIN[8] = { 0.0f, -HALF_RADICAL_2, -1.0f, -HALF_RADICAL_2,  0.0f, HALF_RADICAL_2, 1.0f, HALF_RADICAL_2};
		XPDIRECTION dir = GetDirection();
		Location loc = m_pMovableImp->getLocation();
		Location eLoc = loc + m_effectPos;
		Location *pLoc = NULL;
		for(EffectInfoMap::iterator it = m_mapDuraEffects.begin(); it != m_mapDuraEffects.end();)
		{
			EffectInfo& info = it->second;
			if (info.flag & XPSPREFFFLAG_DELETED)
			{
				it = m_mapDuraEffects.erase(it);
				continue;
			}
			if (info.flag & XPSPREFFFLAG_BIND_EFF_POS)
			{
				pLoc = &eLoc;
			} else {
				pLoc = &loc;
			}
			if (info.flag & XPSPREFFFLAG_BIND_WEAPON_0)
			{
				//受绑定文件0控制
				if ((!m_Sebind[0].IsValid()) || (!m_Sebind[0]->Tick(this, info.effect.operator ->(), tickTime)))
				{
					info.effect->SetDirection(dir);
				}
			} else if (info.flag & XPSPREFFFLAG_BIND_WEAPON_1) {
				//受绑定文件1控制
				if ((!m_Sebind[1].IsValid()) || (!m_Sebind[1]->Tick(this, info.effect.operator ->(), tickTime)))
				{
					info.effect->SetDirection(dir);
				}
			} else {
				//不受绑定文件控制
				if (info.flag & XPSPREFFFLAG_SELF_REF)
				{
					info.effect->SetLocation(static_cast<int>(pLoc->x + info.dx*COS[dir] - info.dy*SIN[dir]), 
						static_cast<int>(pLoc->y + info.dx*SIN[dir] + info.dy*COS[dir]));
				} else {
					info.effect->SetLocation(pLoc->x + info.dx, pLoc->y + info.dy);
				}
				info.effect->SetDirection(dir);
				info.effect->Update(tickTime);
			}
			if (info.flag & XPSPREFFFLAG_LINKING_FRAME)
			{
				XapEffect* pEffect = dynamic_cast<XapEffect*>(info.effect.operator->());
				XPASSERT(pEffect && L"精灵某特效有XPSPREFFFLAG_LINKING_FRAME标记，但是不是XapEffect");
				pEffect->SetPlayingFrame(m_iCurFrame);
				
			}
			++it;
		}
		for(std::list<EffectInfo>::iterator it = m_lstTmpEffects.begin(); it != m_lstTmpEffects.end();)
		{
			EffectInfo& info = *it;
			if (info.flag & XPSPREFFFLAG_SELF_REF)
			{
				info.effect->SetLocation(static_cast<int>(loc.x + info.dx*COS[dir] - info.dy*SIN[dir]), 
					static_cast<int>(loc.y + info.dx*SIN[dir] + info.dy*COS[dir]));
			} else {
				info.effect->SetLocation(loc.x + info.dx, loc.y + info.dy);
			}
			
			info.effect->SetDirection(dir);
			if( ! info.effect->Update(tickTime) )
				it = m_lstTmpEffects.erase(it);
			else
				++it;
		}
	}

	void Sprite::RenderEffectFront(Canvas* canvas)
	{
		World *pWorld = m_pEngine->GetXPWorld();
		for(EffectInfoMap::iterator it = m_mapDuraEffects.begin(); it != m_mapDuraEffects.end(); ++it)
		{
			if (((it->second.flag & XPSPREFFFLAG_DRAW_ON_TOP) != 0) && pWorld)
			{
				pWorld->PushSpriteTopEffect(&(*it->second.effect));
			} else {
				it->second.effect->RenderFront(canvas);
			}
		}
		for(std::list<EffectInfo>::iterator it = m_lstTmpEffects.begin(); it != m_lstTmpEffects.end(); ++it)
		{
			if (((it->flag & XPSPREFFFLAG_DRAW_ON_TOP) != 0) && pWorld)
			{
				pWorld->PushSpriteTopEffect(&(*it->effect));
			} else {
				it->effect->RenderFront(canvas);
			}
		}
	}
	void Sprite::RenderEffectBack(Canvas* canvas)
	{
		for(EffectInfoMap::iterator it = m_mapDuraEffects.begin(); it != m_mapDuraEffects.end(); ++it)
		{
			if ((it->second.flag & XPSPREFFFLAG_DRAW_ON_TOP) != 0)
				continue;
			it->second.effect->RenderBack(canvas);
		}
		for(std::list<EffectInfo>::iterator it = m_lstTmpEffects.begin(); it != m_lstTmpEffects.end(); ++it)
		{
			if ((it->flag & XPSPREFFFLAG_DRAW_ON_TOP) != 0)
				continue;
			it->effect->RenderBack(canvas);
		}
	}

	void Sprite::Tick(DWORD ticktime)
	{
		UpdateAction();
		UpdateEffect(ticktime);
	}

	// 移动嘀嗒。参数为上一次嘀嗒依赖的时间差。by world
	void Sprite::tick(int now, int deltaTime) 
	{
		if ( TestPropFlag(XPSPPF_IS_IN_TELEPORT) )
		{	//正在瞬间移动中
			int telD = m_pEngine->GetTick() - m_nTeleportStartTime;
			if (telD < m_nTeleportTime)	//还没有移动完成
			{
				Location vec = m_TeleportObj - m_TeleportStart;
				float delta = static_cast<float>(telD) / m_nTeleportTime;
				vec.x = static_cast<int>(vec.x * delta);
				vec.y = static_cast<int>(vec.y * delta);
				vec += m_TeleportStart;
				SetLocation(vec);
			} else if ( telD <= m_nTeleportTime + m_nTeleportBlurTime) { //移动到位了，但是留着残影
				SetLocation(m_TeleportObj);
			} else {	//连残影都没有了
				//m_iAniStartTime += TELEPORT_AND_BLUR_TIME;
				SetLocation(m_TeleportObj);
				SetPropFlag(XPSPPF_IS_IN_TELEPORT, false);
				m_pEngine->GetApp()->OnUpdateSpriteAction(this, XPUSAT_TELEPORTEND);	
			}
		}
		m_pMovableImp->tick(now, deltaTime); 
		UpdateEffect(deltaTime);
	}

};
