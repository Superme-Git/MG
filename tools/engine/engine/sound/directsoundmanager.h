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
		tmpvolume = tmpvolume << 4;//传入的参数volume是0~255，而DXSound的volume是0~-4080（这个4080是我的经验值，DX是-10000为最小值，事实上到-3000就很小声了，我就选了4080，这个值刚好是255 << 4）
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

	//都在主线程调用
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
	//自定义类型
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
				else if (handle > x.handle)//这里handle的大小和播放的先后顺序没有关系了，比较郁闷……
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
		typedef std::map<std::wstring, XBuffer> StreamSoundDataMap;//StaticSound就不用啦，StaticSound自己就支持同一个文件多次播放
		typedef std::list<SoundHandle> StreamSoundList;
	
	//成员们
	private:
		PrioritySoundSet	m_setPrioritySet;

		SoundSet			m_setStaticSound;//为了避免锁，还是和StreamSound分开吧
		LoadingSounds		m_loadingUrls;	 //正在异步加载的声音
		SoundPoolMap		m_Pools;		 //StaticSound的Buffer池子们

		StreamSoundDataMap	m_mapStreamSoundDatas;

		unsigned int m_dwStreamFillThreadID; 
		HANDLE m_hStreamFillThread; 
		HANDLE m_hMainThread;
		XCSLock m_lockInitiativeDelSound;//m_setInitiativeDelSound的锁，如果需要同时锁，就按照这个顺序来
		XCSLock m_lockStreamSounds;//m_setStreamSounds的锁
		XCSLock m_lockAddSounds;//m_setAddSounds的锁
		XCSLock m_lockDelSounds;//m_setDelSounds的锁
		SoundSet m_setStreamSounds;//只在填充线程增加删除，所以在填充线程里面的读取可以不加锁
		SoundSet m_setAddSounds;	//在主线程和填充线程都会有添加删除操作，所以只要使用到就加锁
		SoundSet m_setDelSounds;	//在主线程和填充线程都会有添加删除操作，所以只要使用到就加锁
		SoundSet m_setInitiativeDelSound;	//在填充线程增加，在主线程删除
		static StreamSoundList s_willDelSounds;		//都在主线程操作，不用锁，由QueueUserAPC新增
		static StreamSoundList s_streamPlayEndNotify;		//都在主线程操作，不用锁，由QueueUserAPC新增
		static StreamSoundList s_streamPlayGoingEndNotify; //都在主线程操作，不用锁，由QueueUserAPC新增
		VolumeMap m_volumeMap;
		
		LPDIRECTSOUND m_pDirectSound;
		bool m_bStreamSoundRuning;
		XPFADE_SIGN m_fadeState;//窗口激活的淡入淡出状态
		CFileIOManager *m_pFileIOMan;
		EngineBase *m_pEB;
		int m_nInactiveFadeOutTime;	//当窗口失去激活时，声音淡出的时间（毫秒，-1代表不停止播放）
		int m_nActiveFadeInTime; //当窗口重新激活时，声音淡入的时间（毫秒，<0无意义）
		int m_nBeginTick;	//如果fade了，开始的时间
		float m_fFadeRate;
	private:
		static XPSOUND_TYPE GetFileType(const std::wstring &fn);
		bool ReplySound(SoundHandle handle, const std::wstring &filename, XPISoundType soundType);//加载完了可以播放了
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

		DWORD GetSoundLength(const std::wstring &fn);	//毫秒

		CHECK_SOUND_STATE CheckSound(const std::wstring &fn, bool async, ISoundLoadNotify* pNotify, XPISoundType& type);

		bool TestPriority(short priority) const;

		void OnWindowStateChange(bool active);

		int GetAllSoundCount() const;
		void GetAllSoundDetail(std::vector<std::wstring> &detail) const;

		//当窗口失去激活时，声音淡出的时间（毫秒，-1代表不停止播放）
		int GetInactiveSoundFadeOutTime() const { return m_nInactiveFadeOutTime; }
		void SetInactiveSoundFadeOutTime(int t);

		//当窗口重新激活时，声音淡入的时间（毫秒，<0无意义）
		int GetActiveSoundFadeInTime() const { return m_nActiveFadeInTime; }
		void SetActiveSoundFadeInTime(int t) { m_nActiveFadeInTime = t<0?0:t; }

		
		//freq取值范围0~255，其中128为正常
		//volume取值范围0~255，其中255为正常
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

		//只有已经加载完才能Create成功，只能在主线程调用，而加载，可以通过CheckSound来做
		SoundHandle CreateStreamSound(const std::wstring &fn, ISoundNotify* pNotify);
		//只有已经加载完才能Push成功，只能在主线程调用
		bool PushNextStreamSound(SoundHandle handle, const std::wstring &fn);

		bool PlayStreamSound(SoundHandle handle, short priority, unsigned char type, bool looping);
		bool PlayAndFadeIn(SoundHandle handle, unsigned short time, short priority, unsigned char type, bool bLooping);
		bool FadeOutToStop(SoundHandle handle, unsigned short time);

		//这个是在主线程执行的
		void OnTick();
		//这个是在填充线程执行的，主要是给StreamSound用。DirectSoundManager里面，除了这个方法，别的方法只能在主线程调用
		void Update(StreamSoundList &willDel);
		void GarbageCollection(int nowtick);		//背景音乐不用清理
		HANDLE GetMainThread() const { return m_hMainThread; }



	private:
		DirectSoundManager(const DirectSoundManager&);
		DirectSoundManager& operator= (const DirectSoundManager&);
	};
}
#endif