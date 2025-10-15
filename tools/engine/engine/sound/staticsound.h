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

	//һ��SoundBufferPoolʵ����Ӧһ�������ļ������ʵ��������ܻ��кܶ��Buffer��ͬʱ���ܻ��ж��Buffer��ͬ����ͬ���ز�
	//һ��StaticSound��Ӧһ��SoundBufferPool�����Buffer
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
		bool CheckSoundStop();	//����������˾ͷ���true�����򷵻�false��������������ڲ��ŵ���Ч������ÿ֡Ϊÿ��Buffer��һ��
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