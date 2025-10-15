#ifndef __Nuclear_SOUND_WRAP_H
#define __Nuclear_SOUND_WRAP_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"
#include "isound.h"

namespace Nuclear
{

	class SoundBufferPool;
	class DirectSoundManager;
	class SoundWrap
	{
	private:
		ISound::Param m_param;
		ISound *m_pSound;

	public:
		SoundWrap(void) : m_pSound(NULL) { m_param.m_pSoundWrap = this; }
		~SoundWrap(void);

		void FreeRes();
		bool Init(SoundBufferPool *pPool);
		bool Init(DirectSoundManager* pDSman, const XBuffer& buffer, bool bGlobalFocus, XPSOUND_TYPE type);

		DWORD GetPlayLength() {return m_pSound?(m_pSound->GetPlayLength()):0; }

		void SetSoundType(unsigned char type) { m_param.m_nType = type; }
		unsigned char GetSoundType() const { return m_param.m_nType; }

		bool Play(bool isLooping);
		bool Stop();
		void Pause();
		bool Resume();

		void SetFadeRate(float fFadefRate, bool force);
		void SetVolume(unsigned char volume);
		unsigned char GetVolume() const { return m_param.m_nVolume; }

		void SetPriority(short priority) { m_param.m_priority = priority; }
		short GetPriority() const { return m_param.m_priority; }

		void SetTypeVolume(float fTypeVol)
		{
			m_param.m_ftypeVol = fTypeVol;
			SetVolume(m_param.m_nVolume);
		}

		float GetTypeVolume() const { return m_param.m_ftypeVol; }

		void SetTotalVolume(float fTypeVol, unsigned char volume)
		{
			m_param.m_ftypeVol = fTypeVol;
			SetVolume(volume);
		}

		void SetFrequency(unsigned char freq);

		bool IsPlaying() const;
		bool IsPaused() const { return m_param.m_bPause; } 
		bool IsLooping() const { return m_param.m_bLooping; }
		bool IsStoped() const { return m_param.m_bStoped; }

		void NotifyGoingToEnd() { if (m_param.m_pNotify) m_param.m_pNotify->OnSoundGoingToEnd(this); }
		void NotifyPlayEnd() { if (m_param.m_pNotify) m_param.m_pNotify->OnSoundPlayEnd(this); }
		void SetNotify(ISoundNotify *pNotify) { m_param.m_pNotify = pNotify; }
		const ISoundNotify *GetNotify() const { return m_param.m_pNotify; }
		ISoundNotify *GetNotify() { return m_param.m_pNotify; }

		void OnWindowStateChange(XPFADE_SIGN state);


		XPISoundType GetType() const { return m_pSound?m_pSound->GetType():XPIST_EMPTY; }

		StaticSound *GetStaticSound() { return m_pSound?m_pSound->GetStaticSound():NULL; }
		const StaticSound *GetStaticSound() const { return m_pSound?m_pSound->GetStaticSound():NULL; }

		StreamSound *GetStreamSound() { return m_pSound?m_pSound->GetStreamSound():NULL; }
		const StreamSound *GetStreamSound() const { return m_pSound?m_pSound->GetStreamSound():NULL; }
	};

}

#endif //#ifndef __Nuclear_SOUND_WRAP_H
