#ifndef __Nuclear_SOUND_BUFFER_POOL_H
#define __Nuclear_SOUND_BUFFER_POOL_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <dsound.h>
#include "isounddata.h"


namespace Nuclear
{
	class SoundBufferPool
	{
	private:
		static const int MAX_SOUND_BUFFER = 16;
		typedef std::vector<LPDIRECTSOUNDBUFFER> PBufferVector;
		typedef std::list<LPDIRECTSOUNDBUFFER> FreeList;

		FreeList			m_freeList;
		PBufferVector		m_pBuffers;
		int					m_nSize;
		DWORD				m_dwCreationFlags;
		ISoundData*			m_pSoundDatas;
		LPDIRECTSOUND		m_pDirectSound;
		int					m_nTick;		//最后一次播放的时间戳
		DWORD				m_nInitFreq;


	private:
		void CleanAll();
		HRESULT FillBufferWithSound(LPDIRECTSOUNDBUFFER pBuffer);

	public:
		SoundBufferPool(void);
		~SoundBufferPool(void);

		bool Initialize(const XBuffer& buffer, LPDIRECTSOUND pDirectSound, DWORD dwCreationFlags = 0, XPSOUND_TYPE type = XPSOUNDTYPE_WAVE);
		bool RestoreBuffer(LPDIRECTSOUNDBUFFER pBuffer);

		DWORD GetPlayLength() const
		{
			if (!m_pSoundDatas)
				return 0;
			return m_pSoundDatas->GetPlayLength(); 
		}

		void SetTick(int tick) { m_nTick = tick; }
		int GetTick() const { return m_nTick; }
		bool IsPlaying() const { return m_freeList.size() != m_pBuffers.size(); }
		LPDIRECTSOUNDBUFFER GetBuffer();
		void FreeBuffer(LPDIRECTSOUNDBUFFER pBuffer);
		DWORD GetInitFreq() const { return m_nInitFreq; }
	};
}


#endif //#ifndef __Nuclear_SOUND_BUFFER_POOL_H
