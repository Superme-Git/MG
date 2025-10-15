#ifndef __Nuclear_DIRECTSOUND_H
#define __Nuclear_DIRECTSOUND_H

#if _MSC_VER > 1000 
#pragma once 
#endif // _MSC_VER > 1000 

#include <mmsystem.h>
#include <dsound.h>
#include "..\common\xlock.h"
#include "isound.h"
#include "isounddata.h"

namespace Nuclear
{
	class DirectSoundManager;
	class ISoundNotify;
	class ISoundLoadNotify;
	
	typedef std::list<ISoundLoadNotify*> ISoundLoadNotifyList;

	class StreamSound : public ISound
	{ 
	private:
		static const int BUFFER_PLAY_LENGTH = 8;//�������ĳ���(��λΪ��) 
		static const int NOTIFICATIONS_NUM = 16;//�û�������֪ͨ���� 

	public:
		enum XPStreamSoundUpdateResult
		{
			XPSSUR_FAILD = -1,
			XPSSUR_OK = 0,
			XPSSUR_PLAY_END,
		};
		//��Ա������
	private: 
		//ISoundData *m_pCurSoundDatas;
		DirectSoundManager *m_pDirectSoundMan;
		XCSLock m_bufferLock;

		//DWORD		m_curSoundSize;	//CurSoundDatas�Ĵ�С
		ISoundDataList m_sounds;
		XCSLock m_soundsLock;

		//���뵭�����
		unsigned short m_nFadeTime;
		unsigned short m_nFadingTime;//����ǵݼ���
		unsigned char m_nFadeSign;

		bool m_bNotifyWillEnd;//�Ƿ��Ѿ�֪ͨ������ļ��첥����

		//����HandleNotificationThread��صĲ��� 
		bool m_bFillNextNotificationWithSilence; //ֻ������̸߳ģ�ֹͣ���ŵ�ʱ����������̸߳�
		DWORD m_dwPlayProgress; //ֻ������̸߳ģ�ֹͣ���ŵ�ʱ����������̸߳�
		DWORD m_dwFillProgress; //ֻ������̸߳ģ�ֹͣ���ŵ�ʱ����������̸߳�
		DWORD m_dwSoundSize; //ֻ������̸߳ģ�ֹͣ���ŵ�ʱ����������̸߳ģ���ǰ�������Ĵ�С
		DWORD m_dwLastPlayPos; //ֻ������̸߳ģ�ֹͣ���ŵ�ʱ����������̸߳�
		DWORD m_dwNextWriteOffset; //ֻ������̸߳ģ�ֹͣ���ŵ�ʱ����������̸߳�

		LPDIRECTSOUNDBUFFER m_pDSBuffer; 
		DWORD m_dwDSBufferSize; 
		DWORD m_dwNotifySize; 

		HANDLE m_hNotificationEvent; 

		//��Ա������
	private: 
		bool PlayBuffer(); 
		bool RestoreBuffer(); 
		void ReleaseAllSounds();

	public: 
		StreamSound(DirectSoundManager* pDSman, Param *pParam); 
		virtual ~StreamSound(); 

		//���������ˣ�������Ҳû����˼
		////���ò��Ż������ĳ��ȣ�����Ϊ��λ,��������ã�Ĭ�ϵ���3�� 
		//void SetBufferLength(int nSecond){m_nBufferPlayLength = nSecond;} 
		//DWORD GetBufferLength(){return m_nBufferPlayLength;} 
		////���øó��ȵĲ��Ż�������֪ͨ�¼�����,��������ã�Ĭ�ϵ���16��
		//void SetNotificationsNum(int nCount){m_nNotificationsNum = nCount;} 
		//DWORD GetNotificationsNum(){return m_nNotificationsNum;} 

		bool Initialize(const XBuffer& buffer, bool bGlobalFocus, LPDIRECTSOUND pDirectSound, XPSOUND_TYPE type = XPSOUNDTYPE_WAVE, ISoundNotify* pNotify = NULL);//bGlobalFocus���ΪTRUE����ʧȥ����ʱ��Ȼ����
		bool PushNextBuufer(const XBuffer& buffer,  XPSOUND_TYPE type);

		bool Play(); 
		void Pause(); 
		bool Resume();
		bool Stop(); 
		void SetVolume();

		//���뵭������λ����
		bool PlayAndFadeIn(unsigned short time, bool bLooping);
		bool FadeOutToStop(unsigned short time);
		void SetFrequency();
		bool IsPlaying() const; 

		DWORD GetPlayLength();
		virtual XPISoundType GetType() const { return XPIST_STREAM_SOUND; }
		virtual StaticSound *GetStaticSound() { return NULL; }
		virtual const StaticSound *GetStaticSound() const { return NULL; }
		virtual StreamSound *GetStreamSound() { return this; }
		virtual const StreamSound *GetStreamSound() const { return this; }
		
		XPStreamSoundUpdateResult Update();//ֻӦ��������̵߳���
	}; 
}
#endif