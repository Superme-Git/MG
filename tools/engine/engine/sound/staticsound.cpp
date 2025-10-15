#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\xptypes.h"
#include "directsoundmanager.h"
#include "staticsound.h"

namespace Nuclear
{
	void StaticSound::ReleaseRes()
	{
		if (m_pPool && m_pBuffer)
		{
			m_pPool->FreeBuffer(m_pBuffer);
		}
		m_pPool = NULL;
		m_pBuffer = NULL;
	}

	bool StaticSound::Init(SoundBufferPool *pPool)
	{
		XPASSERT(pPool != NULL);
		ReleaseRes();
		m_pPool = pPool;
		m_pBuffer = m_pPool->GetBuffer();
		m_pParam->m_nInitFreq = m_pPool->GetInitFreq();
		SetVolume();
		SetFrequency();
		return m_pBuffer != NULL;
	}

	bool StaticSound::Play()
	{
		if (!m_pBuffer)
			return false;

		if (!m_pPool->RestoreBuffer(m_pBuffer))
		{
			ReleaseRes();
			return false;
		}

		if (SUCCEEDED(m_pBuffer->Play(0, 0, m_pParam->m_bLooping?DSBPLAY_LOOPING:0)))
			return true;

		ReleaseRes();
		return false;
	}

	bool StaticSound::Stop()
	{
		if (!m_pBuffer)
			return false;
		DWORD dwStatus = 0;
		m_pBuffer->GetStatus(&dwStatus);
		if (dwStatus & DSBSTATUS_PLAYING)
		{
			m_pBuffer->Stop();
			ReleaseRes();
			return true;
		}
		return false;
	}

	void StaticSound::Pause()
	{
		if (!m_pBuffer)
			return;
		DWORD dwStatus = 0;
		m_pBuffer->GetStatus(&dwStatus);
		if (dwStatus & DSBSTATUS_PLAYING)
		{
			m_pBuffer->Stop();
		}
	}

	bool StaticSound::Resume()
	{
		return Play();
	}

	bool StaticSound::IsPlaying() const
	{ 
		bool bIsPlaying = false;	 
		if(m_pBuffer == NULL) 
			return false;  
		DWORD dwStatus = 0; 
		m_pBuffer->GetStatus(&dwStatus); 
		bIsPlaying |= ((dwStatus & DSBSTATUS_PLAYING) != 0); 
		return bIsPlaying; 
	}

	
	bool StaticSound::CheckSoundStop()
	{
		if (!m_pBuffer)
			return true;
		if (m_pParam->m_bPause)
			return false;
		DWORD dwStatus = 0;
		m_pBuffer->GetStatus(&dwStatus);
		if ((dwStatus & DSBSTATUS_PLAYING) == 0)
		{
			ReleaseRes();
			return true;
		}
		return false;
	}

	void StaticSound::SetVolume()
	{
		if (!m_pBuffer)
			return;

		LONG volume = GetDXVolume(m_pParam->m_nVolume, m_pParam->m_ftypeVol);

		switch (m_pParam->m_state)
		{
		case XPFS_NORMAL:
			break;
		case XPFS_FADE_OUT:
			volume -= static_cast<LONG>((DSBVOLUME_MAX - DSBVOLUME_MIN) * m_pParam->m_fFadefRate);
			if (volume < DSBVOLUME_MIN) volume = DSBVOLUME_MIN;
			break;
		case XPFS_FADE_IN:
			volume -= static_cast<LONG>((DSBVOLUME_MAX - DSBVOLUME_MIN) * (1 - m_pParam->m_fFadefRate));
			if (volume < DSBVOLUME_MIN) volume = DSBVOLUME_MIN;
			break;
		case XPFS_MUTE:
			volume = DSBVOLUME_MIN;
		}

		m_pBuffer->SetVolume(volume);
	}

	DWORD StaticSound::GetPlayLength() const 
	{ 
		if (!m_pPool)
			return 0;
		return m_pPool->GetPlayLength(); 
	}

	void StaticSound::SetTick(int tick)
	{
		if (!m_pPool)
			return;
		m_pPool->SetTick(tick);
	}

	void StaticSound::SetFrequency()
	{
		if(!m_pBuffer) 
			return;
		m_pBuffer->SetFrequency(m_pParam->m_nInitFreq * m_pParam->m_nFrequency / 128);
	}


	
};