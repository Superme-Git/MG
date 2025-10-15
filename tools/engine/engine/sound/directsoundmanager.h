#ifndef __Nuclear_DIRECTSOUNDMANAGER_H
#define __Nuclear_DIRECTSOUNDMANAGER_H

#if _MSC_VER > 1000 
#pragma once 
#endif // _MSC_VER > 1000 

#include <mmsystem.h>
#include <dsound.h>
#include "..\common\xlock.h"
#include "streamsound.h"
#include "staticsound.h"
#include "soundbufferpool.h"

namespace Nuclear
{
	class EngineBase;

	inline int GetDXVolume(unsigned char volume, float fTypeVolume)
	{
		int tmpvolume = volume;
		tmpvolume = tmpvolume << 4;//����Ĳ���volume��0~255����DXSound��volume��0~-4080�����4080���ҵľ���ֵ��DX��-10000Ϊ��Сֵ����ʵ�ϵ�-3000�ͺ�С���ˣ��Ҿ�ѡ��4080�����ֵ�պ���255 << 4��
		tmpvolume = static_cast<int>(fTypeVolume * tmpvolume);
		tmpvolume -= 4080;
		return tmpvolume;
	}

	struct VolumeStruct
	{
		bool bEnable;
		float fVolume;
		VolumeStruct(bool _bEnable = true, float _fVolume = 1.0f) : bEnable(_bEnable), fVolume(_fVolume) {}
	};
	typedef std::map<unsigned char, VolumeStruct> VolumeMap;
	class CFileIOManager;

	//�������̵߳���
	class ISoundLoadNotify
	{
	public:
		virtual void OnSoundLoaded(const std::wstring &fn, bool success) {}
	};

	enum NEXT_STREAM_SOUND_ERR
	{
		NSSE_OK = 0,
		NEES_NO_STREAM_SOUND,
		NSSE_OPEN_FILE_ERROR,
		NSSE_FILE_TYPE_ERROR,
	};

	class DirectSoundManager
	{
	private:
	//�Զ�������
		friend class SoundReadTask;
		friend class SoundWrap;
		struct PriorityStruct
		{
			short priority;
			SoundHandle handle;
			PriorityStruct(short _priority = 0, SoundHandle _handle = INVALID_SOUND_HANDLE)
				: priority(_priority), handle(_handle) {}
			bool operator < (const PriorityStruct &x) const
			{
				if (priority < x.priority)
					return true;
				else if (priority > x.priority)
					return false;
				else if (handle > x.handle)//����handle�Ĵ�С�Ͳ��ŵ��Ⱥ�˳��û�й�ϵ�ˣ��Ƚ����ơ���
					return true;
				return false;
			}
		};

		typedef std::set<SoundHandle> SoundSet;

		
		typedef std::set<PriorityStruct> PrioritySoundSet;
		
		typedef std::vector<SoundHandle> SHandleVet;
		struct LoadingParam
		{
			SHandleVet handles;
			ISoundLoadNotifyList notifies;
		};
		typedef std::map<std::wstring, LoadingParam> LoadingSounds;

		typedef std::map<std::wstring, SoundBufferPool*> SoundPoolMap;
		typedef std::map<std::wstring, XBuffer> StreamSoundDataMap;//StaticSound�Ͳ�������StaticSound�Լ���֧��ͬһ���ļ���β���
		typedef std::list<SoundHandle> StreamSoundList;
	
	//��Ա��
	private:
		PrioritySoundSet	m_setPrioritySet;

		SoundSet			m_setStaticSound;//Ϊ�˱����������Ǻ�StreamSound�ֿ���
		LoadingSounds		m_loadingUrls;	 //�����첽���ص�����
		SoundPoolMap		m_Pools;		 //StaticSound��Buffer������

		StreamSoundDataMap	m_mapStreamSoundDatas;

		unsigned int m_dwStreamFillThreadID; 
		HANDLE m_hStreamFillThread; 
		HANDLE m_hMainThread;
		XCSLock m_lockInitiativeDelSound;//m_setInitiativeDelSound�����������Ҫͬʱ�����Ͱ������˳����
		XCSLock m_lockStreamSounds;//m_setStreamSounds����
		XCSLock m_lockAddSounds;//m_setAddSounds����
		XCSLock m_lockDelSounds;//m_setDelSounds����
		SoundSet m_setStreamSounds;//ֻ������߳�����ɾ��������������߳�����Ķ�ȡ���Բ�����
		SoundSet m_setAddSounds;	//�����̺߳�����̶߳��������ɾ������������ֻҪʹ�õ��ͼ���
		SoundSet m_setDelSounds;	//�����̺߳�����̶߳��������ɾ������������ֻҪʹ�õ��ͼ���
		SoundSet m_setInitiativeDelSound;	//������߳����ӣ������߳�ɾ��
		static StreamSoundList s_willDelSounds;		//�������̲߳���������������QueueUserAPC����
		static StreamSoundList s_streamPlayEndNotify;		//�������̲߳���������������QueueUserAPC����
		static StreamSoundList s_streamPlayGoingEndNotify; //�������̲߳���������������QueueUserAPC����
		VolumeMap m_volumeMap;
		
		LPDIRECTSOUND m_pDirectSound;
		bool m_bStreamSoundRuning;
		XPFADE_SIGN m_fadeState;//���ڼ���ĵ��뵭��״̬
		CFileIOManager *m_pFileIOMan;
		EngineBase *m_pEB;
		int m_nInactiveFadeOutTime;	//������ʧȥ����ʱ������������ʱ�䣨���룬-1����ֹͣ���ţ�
		int m_nActiveFadeInTime; //���������¼���ʱ�����������ʱ�䣨���룬<0�����壩
		int m_nBeginTick;	//���fade�ˣ���ʼ��ʱ��
		float m_fFadeRate;
	private:
		static XPSOUND_TYPE GetFileType(const std::wstring &fn);
		bool ReplySound(SoundHandle handle, const std::wstring &filename, XPISoundType soundType);//�������˿��Բ�����
		bool CheckStreamSound(SoundHandle handle);
		bool CheckSound(SoundHandle handle);
		bool PreparePlayStreamSound(SoundHandle handle, short priority, unsigned char type);

		static unsigned int __stdcall StreamSoundFillThread(void* pParam);
		static void __stdcall DelSound(ULONG_PTR pParam);
		static void __stdcall DummyFunc(ULONG_PTR pParam) { }

	public:
		DirectSoundManager(EngineBase *pEngineBase);
		~DirectSoundManager(void);

		static void __stdcall NotifyGoingToEnd(ULONG_PTR pParam);
		static void __stdcall NotifyPlayEnd(ULONG_PTR pParam);

		bool Init(HWND hWnd, DWORD dwCoopLevel);
		bool SetPrimaryBufferFormat(WORD dwPrimaryChannels, DWORD dwPrimaryFreq, WORD dwPrimaryBitRate);

		DWORD GetSoundLength(const std::wstring &fn);	//����

		CHECK_SOUND_STATE CheckSound(const std::wstring &fn, bool async, ISoundLoadNotify* pNotify, XPISoundType& type);

		bool TestPriority(short priority) const;

		void OnWindowStateChange(bool active);

		int GetAllSoundCount() const;
		void GetAllSoundDetail(std::vector<std::wstring> &detail) const;

		//������ʧȥ����ʱ������������ʱ�䣨���룬-1����ֹͣ���ţ�
		int GetInactiveSoundFadeOutTime() const { return m_nInactiveFadeOutTime; }
		void SetInactiveSoundFadeOutTime(int t);

		//���������¼���ʱ�����������ʱ�䣨���룬<0�����壩
		int GetActiveSoundFadeInTime() const { return m_nActiveFadeInTime; }
		void SetActiveSoundFadeInTime(int t) { m_nActiveFadeInTime = t<0?0:t; }

		
		//freqȡֵ��Χ0~255������128Ϊ����
		//volumeȡֵ��Χ0~255������255Ϊ����
		SoundHandle XPPlaySound(const std::wstring &fn, short priority, unsigned char type, unsigned char volume, unsigned char freq, bool looping = false);
		bool PauseSound(SoundHandle handle);
		bool ResumeSound(SoundHandle handle);
		bool StopSound(SoundHandle handle);
		bool IsSoundPause(SoundHandle handle);
		bool IsSoundSoundPlaying(SoundHandle handle);
		void StopTypeSound(unsigned char type);
		void PauseTypeSound(unsigned char type);
		void ResumeTypeSound(unsigned char type);

		bool SetSoundVolume(SoundHandle handle, unsigned char volume);

		//fVolume[0, 1]
		void SetSoundTypeVolume(unsigned char type, float fVolume);
		float GetSoundTypeVolume(unsigned char type) const 
		{
			VolumeMap::const_iterator it = m_volumeMap.find(type);
			if (it == m_volumeMap.end())
				return 1.0f;
			return it->second.fVolume;
		}
		void EnableSoundType(unsigned char type, bool bEnable);
		bool IsSoundTypeEnable(unsigned char type) const
		{
			VolumeMap::const_iterator it = m_volumeMap.find(type);
			if (it == m_volumeMap.end())
				return true;
			return it->second.bEnable;
		}
		
		void SetNotify(SoundHandle handle, ISoundNotify *pNotify);
		
		bool IsStreamSoundRunning() const { return m_bStreamSoundRuning; }
		void CancelSoundLoadingNotify(const std::wstring &fn, ISoundLoadNotify* pNotify);

		//ֻ���Ѿ����������Create�ɹ���ֻ�������̵߳��ã������أ�����ͨ��CheckSound����
		SoundHandle CreateStreamSound(const std::wstring &fn, ISoundNotify* pNotify);
		//ֻ���Ѿ����������Push�ɹ���ֻ�������̵߳���
		bool PushNextStreamSound(SoundHandle handle, const std::wstring &fn);

		bool PlayStreamSound(SoundHandle handle, short priority, unsigned char type, bool looping);
		bool PlayAndFadeIn(SoundHandle handle, unsigned short time, short priority, unsigned char type, bool bLooping);
		bool FadeOutToStop(SoundHandle handle, unsigned short time);

		//����������߳�ִ�е�
		void OnTick();
		//�����������߳�ִ�еģ���Ҫ�Ǹ�StreamSound�á�DirectSoundManager���棬���������������ķ���ֻ�������̵߳���
		void Update(StreamSoundList &willDel);
		void GarbageCollection(int nowtick);		//�������ֲ�������
		HANDLE GetMainThread() const { return m_hMainThread; }



	private:
		DirectSoundManager(const DirectSoundManager&);
		DirectSoundManager& operator= (const DirectSoundManager&);
	};
}
#endif