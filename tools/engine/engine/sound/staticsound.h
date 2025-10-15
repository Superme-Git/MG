#ifndef __Nuclear_STATICSOUND_H
#define __Nuclear_STATICSOUND_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <dsound.h>
#include "isound.h"
#include "isounddata.h"


namespace Nuclear
{
	class SoundBufferPool;
	class ISoundNotify;

	//一个SoundBufferPool实例对应一个声音文件，这个实例里面可能会有很多个Buffer，同时可能会有多个Buffer在同步或不同步地播
	//一个StaticSound对应一个SoundBufferPool里面的Buffer
	class StaticSound : public ISound
	{
		LPDIRECTSOUNDBUFFER m_pBuffer;
		SoundBufferPool *m_pPool;

	public:
		StaticSound(Param *pParam) : ISound(pParam), m_pBuffer(NULL), m_pPool(NULL) {}
		~StaticSound() {}
		bool Init(SoundBufferPool *pPool);
		void ReleaseRes();
		bool Play();
		bool Stop();
		void Pause();
		bool Resume();

		void SetVolume();
		bool CheckSoundStop();	//如果播放完了就返回true，否则返回false，如果对象有正在播放的音效，必须每帧为每个Buffer调一次
		DWORD GetPlayLength() const;
		DWORD GetPlayLength() { return ((const StaticSound*)this)->GetPlayLength(); }
		void SetFrequency();
		//ISoundNotify *GetNotify() { return m_pNotify; }

		void SetTick(int tick);

		virtual bool IsPlaying() const;

		virtual XPISoundType GetType() const { return XPIST_STATIC_SOUND; }

		virtual StaticSound *GetStaticSound() { return this; }
		virtual const StaticSound *GetStaticSound() const { return this; }

		virtual StreamSound *GetStreamSound() { return NULL; }
		virtual const StreamSound *GetStreamSound() const { return NULL; }
	};
}

#endif