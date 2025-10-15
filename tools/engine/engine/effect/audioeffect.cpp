#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\xml.h"
#include "audioeffect.h"

namespace Nuclear
{
	AudioEffect::AudioEffect(EngineBase* pEB) : Effect(pEB), m_handle(INVALID_SOUND_HANDLE), m_priority(0), m_type(2), m_bNoPlay(false)
	{
		m_nSoundProp[SP_MAX_VOL] = 255;
		m_nSoundProp[SP_MIN_VOL] = 255;
		m_nSoundProp[SP_MAX_FREQ] = 128;
		m_nSoundProp[SP_MIN_FREQ] = 128;
	}

	AudioEffect::~AudioEffect(void)
	{
		Stop();
	}

	const XPVECTOR4& AudioEffect::GetColorate() const
	{
		const static XPVECTOR4 tmpcolor(0.0f, 0.0f, 0.0f, 0.0f);
		return tmpcolor;
	}

	int	AudioEffect::GetPlayOnceTime() const //∫¡√Î
	{
		return static_cast<int>(m_pEngine->GetSoundManager()->GetSoundLength(m_name));
	}

	void AudioEffect::OnSoundPlayEnd(SoundHandle handle)
	{
		XPASSERT(m_handle == handle);
		m_handle = INVALID_SOUND_HANDLE;
		m_nCurTime = -1;
		++m_curLoopNum;
		NotifyEnd();
	}



	bool AudioEffect::Stop()
	{
		if (Effect::Stop())
		{
			if (m_handle != INVALID_SOUND_HANDLE)
			{
				m_pEngine->GetSoundManager()->StopSound(m_handle);
			}
			return true;
		}
		return false;
	}
	bool AudioEffect::Update(DWORD tickTime)
	{
		if (m_state != XPES_PLAYING) return true;

		if (m_nCurTime < 0)
		{
			m_nCurTime = 0;
			tickTime = 0;
		}
		m_nCurTime += tickTime;

		switch (m_playMode)
		{
		case XPPM_LOOPNUM:
			if (m_curLoopNum < m_nLoopNum) break;
		case XPPM_TIME:
			if (m_curLoopNum >= 1)
			{
				Stop();
				return false;
			}
			break;
		case XPPM_LOOP:
			break;
		}
		return true;
	}

	int AudioEffect::LoadAttribute(XMLIO::CINode& node)
	{
		m_nSoundProp[SP_MAX_VOL] = static_cast<unsigned char>(node.GetIntAttribute(L"max_vol", 255));
		m_nSoundProp[SP_MIN_VOL] = static_cast<unsigned char>(node.GetIntAttribute(L"min_vol", 255));
		m_nSoundProp[SP_MAX_FREQ] = static_cast<unsigned char>(node.GetIntAttribute(L"max_freq", 128));
		m_nSoundProp[SP_MIN_FREQ] = static_cast<unsigned char>(node.GetIntAttribute(L"min_freq", 128));
		return 0;
	}

	int AudioEffect::SaveAttribute(XMLIO::CONode& node)
	{
		node.SetAttribute(L"max_vol", m_nSoundProp[SP_MAX_VOL]);
		node.SetAttribute(L"min_vol", m_nSoundProp[SP_MIN_VOL]);
		node.SetAttribute(L"max_freq", m_nSoundProp[SP_MAX_FREQ]);
		node.SetAttribute(L"min_freq", m_nSoundProp[SP_MIN_FREQ]);
		return 0;
	}

	bool AudioEffect::Render(Canvas* canvas)
	{
		if (m_bNoPlay)
			return true;
		if (m_state != XPES_PLAYING) return true;
		if (m_handle == INVALID_SOUND_HANDLE)
		{
			DirectSoundManager *pDSM = m_pEngine->GetSoundManager();
			unsigned char vol = m_nSoundProp[SP_MIN_VOL] + (m_nSoundProp[SP_MAX_VOL] - m_nSoundProp[SP_MIN_VOL]);
			m_handle = pDSM->XPPlaySound(m_name, m_priority, m_type,
				RangeRand(m_nSoundProp[SP_MIN_VOL], m_nSoundProp[SP_MAX_VOL]), 
				RangeRand(m_nSoundProp[SP_MIN_FREQ], m_nSoundProp[SP_MAX_FREQ]), 
				false);
			if (m_handle != INVALID_SOUND_HANDLE)
				pDSM->SetNotify(m_handle, this);
		}
		return true;
	}

	XPEffectAssureResResult AudioEffect::AssureResource(bool async)
	{
		if (m_bNoPlay)
			return XPEARR_RESREADY;
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		XPISoundType type = XPIST_EMPTY;
		m_pEngine->GetSoundManager()->CheckSound(m_name, async, NULL, type);
		m_state = XPES_STOP;
		Play();
		return XPEARR_RESREADY;
	}

}
