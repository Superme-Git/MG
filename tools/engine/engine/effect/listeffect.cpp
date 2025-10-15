#include "stdafx.h"
#include "listeffect.h"


namespace Nuclear
{
	ListEffect::ListEffect(EngineBase* pEB) : Effect(pEB), m_nowEffectPos(0), m_nOnceTime(0), 
		m_vertexColor(255, 255, 255, 255), m_scaleType(XPEST_NOSCALE), m_bIsScreenCrood(false)
	{
		
	}

	ListEffect::~ListEffect(void)
	{
		EffectList::iterator it, ie;
		for (it=m_effectList.begin(),ie=m_effectList.end();it!=ie;++it)
		{
			delete (*it);
		}
	}

	void ListEffect::SetScreenCrood(bool b)
	{
		if (m_bIsScreenCrood != b)
		{
			m_bIsScreenCrood = b;
			if (m_state != XPES_EMPTY)
			{
				EffectList::iterator it = m_effectList.begin(), ie = m_effectList.end();
				for (;it!=ie;++it)
				{
					(*it)->SetScreenCrood(b);
				}
			}
		}
	}

	void ListEffect::SetScaleType(XPEFFECT_SCALE_TYPE t)
	{
		if (m_scaleType != t)
		{
			m_scaleType = t;
			if (m_state != XPES_EMPTY)
			{
				EffectList::iterator it = m_effectList.begin(), ie = m_effectList.end();
				for (;it!=ie;++it)
				{
					(*it)->SetScaleType(t);
				}
			}
		}
	}

	XPEffectAssureResResult ListEffect::AssureResource(bool async) 
	{ 
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		m_state = XPES_STOP;
		return XPEARR_RESREADY;
	}
	
	void ListEffect::Release() 
	{
		if (m_state != XPES_STOP)
			return;
		m_state = XPES_EMPTY;
	}
	
	XPEFFECT_BIND_TYPE ListEffect::GetEffectBindType() const
	{
		if (m_effectList.size() > 0)
		{
			return m_effectList[0]->GetEffectBindType();
		}
		return XPEBT_NULL;
	}

	bool ListEffect::Update(DWORD tickTime)
	{
		if (m_state != XPES_PLAYING) return true;
		if (m_nCurTime < 0)
		{
			m_nCurTime = 0;
			tickTime = 0;
		}
		m_nCurTime += tickTime;
		Effect* pEffect = NULL;
		while (CheckNowEffectIsOk())
		{
			pEffect = m_effectList[m_nowEffectPos];
			pEffect->SetDirection(m_dir);
			pEffect->SetLocation(m_pos);
			pEffect->SetTranslation(m_translate);
			pEffect->SetScale(m_scale);
			pEffect->SetRotationRadian(m_rotationRadian);
			pEffect->SetVertexColor(m_vertexColor);
			pEffect->Play();
			if (!pEffect->Update(tickTime))
			{
				pEffect->Stop();
				++m_nowEffectPos;
				tickTime = 0;//这里可能以后要更精确，例如，这次Update之前pEffect剩下10毫秒时间，tickTime是30毫秒，那么这次的tickTime应该是剩下的20毫秒
			} else {
				break;
			}
		}
		if (!CheckNowEffectIsOk())
		{
			NotifyEnd();
			if (m_state != XPES_PLAYING) return true;
			++m_curLoopNum;

			switch (m_playMode)
			{
			case XPPM_LOOPNUM:
				if ( m_curLoopNum > m_nLoopNum)
				{
					Stop();
					return false;
				}
				//这里不用break;
			case XPPM_LOOP:
				{
					m_nCurTime = 0;
					m_nowEffectPos = 0;
					EffectList::iterator it, ie;
					it = m_effectList.begin();
					ie = m_effectList.end();
					for (;it!=ie;++it)
					{
						(*it)->Play();
					}
				}
				break;
			default:
				Stop();
				return false;
			}
		}
		return true;
	}

	void ListEffect::PushAnEffect(Effect* pEffect)
	{
		if (pEffect)
		{
			m_effectList.push_back(pEffect);
			pEffect->AssureResource(false);
			pEffect->SetScreenCrood(m_bIsScreenCrood);
			pEffect->SetScaleType(m_scaleType);
			m_nOnceTime += pEffect->GetPlayTime();
		}
	}

	bool ListEffect::Play()
	{
		if (Effect::Play())
		{
			if (CheckNowEffectIsOk())
			{
				return m_effectList[m_nowEffectPos]->Play();
			}
			return true;
		}
		return false;
	}

	bool ListEffect::Stop()
	{
		if (Effect::Stop())
		{
			if (CheckNowEffectIsOk())
			{
				return m_effectList[m_nowEffectPos]->Stop();
			}
			return true;
		}
		return false;
	}

	bool ListEffect::Pause()
	{
		if (Effect::Pause())
		{
			if (CheckNowEffectIsOk())
			{
				return m_effectList[m_nowEffectPos]->Pause();
			}
			return true;
		}
		return false;
	}

	bool ListEffect::Resume()
	{
		if (Effect::Resume())
		{
			if (CheckNowEffectIsOk())
			{
				return m_effectList[m_nowEffectPos]->Resume();
			}
			return true;
		}
		return false;
	}
}
