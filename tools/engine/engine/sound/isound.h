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


	//�������̵߳���
	class ISoundNotify
	{
	public:
		virtual void OnSoundGoingToEnd(SoundHandle handle) {}
		//�������ֶ�Stop������Looping���л�����FadeOut(0)����ô���õ�����ص���ʱ��
		//���handle�ܿ����Ѿ���ɾ���ˣ�����Manager�����д����handle���м�飬���Բ����
		//����Looping��������ص�����Stop����Release���handle���ǲ���Ҫ��
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
			DWORD			m_nInitFreq;	//��ʼƵ��
			float			m_ftypeVol;		//��ǰ���͵�����
			short			m_priority;		//���ȼ�
			bool			m_bPause;		//�Ƿ���ͣ
			bool			m_bLooping;		//�Ƿ�ѭ��
			bool			m_bStoped;		//�Ƿ��ֶ�ֹͣ
			unsigned char	m_nVolume;		//����[0, 255]
			unsigned char	m_nFrequency;	//Ƶ��
			unsigned char	m_nType;		//���ͣ�ĿǰĬ��0�Ǳ������֣�1�ǻ�����
			float			m_fFadefRate;		//FadeIn/Out�ı���
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
