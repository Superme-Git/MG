#include "stdafx.h"
#include "..\common\log.h"
#include "directsoundmanager.h"
#include "soundwrap.h"
#include "staticsound.h"
#include "streamsound.h"

namespace Nuclear
{
	//========================================SoundWrap===============================================
	SoundWrap::~SoundWrap(void)
	{
		FreeRes();
	}

	void SoundWrap::FreeRes()
	{
		XPSAFE_DELETE(m_pSound);
	}

	bool SoundWrap::Init(SoundBufferPool *pPool)
	{
		XPASSERT((m_pSound == NULL) && L"InitStaticSound");
		StaticSound* pSound = new StaticSound(&m_param);
		
		if (!pSound)
			return false;
		if (pSound->Init(pPool))
		{
			m_pSound = pSound;
			return true;
		}
		delete pSound;
		return false;
	}

	bool SoundWrap::Init(DirectSoundManager* pDSman, const XBuffer& buffer, bool bGlobalFocus, XPSOUND_TYPE type)
	{
		XPASSERT((m_pSound == NULL) && L"InitStreamSound");
		StreamSound* pSound = new StreamSound(pDSman, &m_param);

		if (!pSound)
			return false;
		if (pSound->Initialize(buffer, bGlobalFocus, pDSman->m_pDirectSound, type, m_param.m_pNotify))
		{
			m_pSound = pSound;
			return true;
		}
		delete pSound;
		return false;
	}

	bool SoundWrap::Play(bool isLooping)
	{
		m_param.m_bLooping = isLooping;
		m_param.m_bPause = false;
		m_param.m_bStoped = false;
		if (m_pSound)
			return m_pSound->Play();
		return true;
	}

	bool SoundWrap::Stop()
	{
		m_param.m_bStoped = true;
		NotifyPlayEnd();
		m_param.m_pNotify = NULL;
		if (m_pSound)
			return m_pSound->Stop();
		return true;
	}

	void SoundWrap::Pause()
	{
		m_param.m_bPause = true;
		if (m_pSound)
			m_pSound->Pause();
	}

	bool SoundWrap::Resume()
	{
		if (!m_param.m_bPause)
			return false;
		if (m_pSound)
			return m_pSound->Resume();
		m_param.m_bPause = false;
		m_param.m_bStoped = false;
		return true;
	}

	void SoundWrap::SetFadeRate(float fFadefRate, bool force)
	{
		if (m_param.m_fFadefRate == fFadefRate && !force)
			return;
		m_param.m_fFadefRate = fFadefRate;
		if (m_pSound)
			m_pSound->SetVolume();
	}

	void SoundWrap::SetVolume(unsigned char volume)
	{
		m_param.m_nVolume = volume;
		if (m_pSound)
			m_pSound->SetVolume();
	}

	void SoundWrap::SetFrequency(unsigned char freq)
	{
		m_param.m_nFrequency = freq;
		if (m_pSound)
			m_pSound->SetFrequency();
	}

	bool SoundWrap::IsPlaying() const
	{
		if (m_pSound)
			return m_pSound->IsPlaying();
		return !m_param.m_bPause;
	}

	void SoundWrap::OnWindowStateChange(XPFADE_SIGN state)
	{
		m_param.m_state = state;
	}

}
