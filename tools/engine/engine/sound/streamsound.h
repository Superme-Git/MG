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
		static const int BUFFER_PLAY_LENGTH = 8;//缓冲区的长度(单位为秒) 
		static const int NOTIFICATIONS_NUM = 16;//该缓冲区的通知个数 

	public:
		enum XPStreamSoundUpdateResult
		{
			XPSSUR_FAILD = -1,
			XPSSUR_OK = 0,
			XPSSUR_PLAY_END,
		};
		//成员数据们
	private: 
		//ISoundData *m_pCurSoundDatas;
		DirectSoundManager *m_pDirectSoundMan;
		XCSLock m_bufferLock;

		//DWORD		m_curSoundSize;	//CurSoundDatas的大小
		ISoundDataList m_sounds;
		XCSLock m_soundsLock;

		//淡入淡出相关
		unsigned short m_nFadeTime;
		unsigned short m_nFadingTime;//这个是递减的
		unsigned char m_nFadeSign;

		bool m_bNotifyWillEnd;//是否已经通知了这个文件快播完了

		//处理HandleNotificationThread相关的参数 
		bool m_bFillNextNotificationWithSilence; //只在填充线程改，停止播放的时候可以在主线程改
		DWORD m_dwPlayProgress; //只在填充线程改，停止播放的时候可以在主线程改
		DWORD m_dwFillProgress; //只在填充线程改，停止播放的时候可以在主线程改
		DWORD m_dwSoundSize; //只在填充线程改，停止播放的时候可以在主线程改，当前的声音的大小
		DWORD m_dwLastPlayPos; //只在填充线程改，停止播放的时候可以在主线程改
		DWORD m_dwNextWriteOffset; //只在填充线程改，停止播放的时候可以在主线程改

		LPDIRECTSOUNDBUFFER m_pDSBuffer; 
		DWORD m_dwDSBufferSize; 
		DWORD m_dwNotifySize; 

		HANDLE m_hNotificationEvent; 

		//成员方法们
	private: 
		bool PlayBuffer(); 
		bool RestoreBuffer(); 
		void ReleaseAllSounds();

	public: 
		StreamSound(DirectSoundManager* pDSman, Param *pParam); 
		virtual ~StreamSound(); 

		//不让设置了，设置了也没有意思
		////设置播放缓冲区的长度，以秒为单位,如果不设置，默认的是3秒 
		//void SetBufferLength(int nSecond){m_nBufferPlayLength = nSecond;} 
		//DWORD GetBufferLength(){return m_nBufferPlayLength;} 
		////设置该长度的播放缓冲区的通知事件数量,如果不设置，默认的是16个
		//void SetNotificationsNum(int nCount){m_nNotificationsNum = nCount;} 
		//DWORD GetNotificationsNum(){return m_nNotificationsNum;} 

		bool Initialize(const XBuffer& buffer, bool bGlobalFocus, LPDIRECTSOUND pDirectSound, XPSOUND_TYPE type = XPSOUNDTYPE_WAVE, ISoundNotify* pNotify = NULL);//bGlobalFocus如果为TRUE，在失去焦点时仍然播放
		bool PushNextBuufer(const XBuffer& buffer,  XPSOUND_TYPE type);

		bool Play(); 
		void Pause(); 
		bool Resume();
		bool Stop(); 
		void SetVolume();

		//淡入淡出，单位毫秒
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
		
		XPStreamSoundUpdateResult Update();//只应该在填充线程调用
	}; 
}
#endif