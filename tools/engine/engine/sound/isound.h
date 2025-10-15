#ifndef __Nuclear_ISOUND_H
#define __Nuclear_ISOUND_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"

namespace Nuclear
{
	enum XPISoundType
	{
		XPIST_EMPTY = 0,
		XPIST_STATIC_SOUND,
		XPIST_STREAM_SOUND,
	};
	class StaticSound;
	class StreamSound;
	class SoundWrap;


	//都在主线程调用
	class ISoundNotify
	{
	public:
		virtual void OnSoundGoingToEnd(SoundHandle handle) {}
		//除非是手动Stop或者是Looping，有或者是FadeOut(0)，那么调用到这个回调的时候，
		//这个handle很可能已经被删除了，不过Manager对所有传入的handle都有检查，所以不会崩
		//除了Looping，在这个回调里面Stop或者Release这个handle都是不需要的
		virtual void OnSoundPlayEnd(SoundHandle handle) {}
	};

	class ISound
	{
	private:
		friend class SoundWrap;
	public:
		typedef struct _Param
		{
			SoundWrap		*m_pSoundWrap;
			ISoundNotify	*m_pNotify;		//Notify
			DWORD			m_nInitFreq;	//初始频率
			float			m_ftypeVol;		//当前类型的音量
			short			m_priority;		//优先级
			bool			m_bPause;		//是否暂停
			bool			m_bLooping;		//是否循环
			bool			m_bStoped;		//是否手动停止
			unsigned char	m_nVolume;		//音量[0, 255]
			unsigned char	m_nFrequency;	//频率
			unsigned char	m_nType;		//类型，目前默认0是背景音乐，1是环境音
			float			m_fFadefRate;		//FadeIn/Out的比例
			XPFADE_SIGN		m_state;
			_Param() : m_pNotify(NULL), m_nInitFreq(0), m_ftypeVol(1.0f), 
				m_priority(0), m_bPause(false), m_bLooping(false), m_bStoped(false), 
				m_nVolume(0), m_nFrequency(128), m_nType(0), m_fFadefRate(1.0f), m_state(XPFS_NORMAL) {}
		} Param;

	protected:
		Param *m_pParam;

	private:
		ISound(void) {}

	protected:
		ISound(Param *pParam) : m_pParam(pParam) {}

	public:
		
		virtual ~ISound(void) {}

		virtual DWORD GetPlayLength() = 0;

		virtual bool Play() = 0;
		virtual bool Stop() = 0;
		virtual void Pause() = 0;
		virtual bool Resume() = 0;

		virtual void SetVolume() = 0;
		virtual void SetFrequency() = 0;
		virtual bool IsPlaying() const = 0;
		
		virtual XPISoundType GetType() const = 0;
		
		virtual StaticSound *GetStaticSound() = 0;
		virtual const StaticSound *GetStaticSound() const = 0;

		virtual StreamSound *GetStreamSound() = 0;
		virtual const StreamSound *GetStreamSound() const = 0;
	};
}
#endif //#ifndef __Nuclear_ISOUND_H
