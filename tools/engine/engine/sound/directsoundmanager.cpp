#include "stdafx.h"
#include <process.h>
#include "..\common\log.h"
#include "..\common\fileiomanager.h"
#include "..\engine\enginebase.h"
#include "..\iengine.h"
#include "directsoundmanager.h"
#include "streamsound.h"
#include "soundwrap.h"

namespace Nuclear 
{

	DirectSoundManager::StreamSoundList DirectSoundManager::s_willDelSounds;
	DirectSoundManager::StreamSoundList DirectSoundManager::s_streamPlayEndNotify;
	DirectSoundManager::StreamSoundList DirectSoundManager::s_streamPlayGoingEndNotify;

	int const MAX_PLAYING_STATIC_SOUND = 10;
	unsigned char STREAM_SOUND_INIT_VOLUME = 255;
	class SoundReadTask : public CFileIOManager::AsyncReadTask
	{
		DirectSoundManager *m_pDSMan;
		XPISoundType m_logicType;
		int m_tick;

	private:
		inline void NotifyAll(bool succ)
		{
			DirectSoundManager::LoadingSounds::iterator it = m_pDSMan->m_loadingUrls.find(m_filename);
			if (it == m_pDSMan->m_loadingUrls.end())
				return;

			DirectSoundManager::SHandleVet::iterator hIt = it->second.handles.begin(), hIe = it->second.handles.end();
			if (succ)
			{
				for (;hIt!=hIe;++hIt)
				{
					SoundHandle handle = *hIt;
					if (handle->IsStoped())
					{
						delete handle;
						continue;
					}
					if (!m_pDSMan->ReplySound(handle, m_filename, m_logicType))
					{
						handle->FreeRes();
						if (!m_pDSMan->StopSound(handle))
						{
							handle->NotifyPlayEnd();
						}
						//����FreeRes��StopSound����ɾ��handle��
						delete handle;
					}
				}
			} else {
				for (;hIt!=hIe;++hIt)
				{
					SoundHandle handle = *hIt;
					//�����ж�m_logicType����Ϊ���е�������Ҫdelete���������ֲ���������
					m_pDSMan->StopSound(handle);
					delete handle;
				}
			}
			
			ISoundLoadNotifyList::iterator nIt = it->second.notifies.begin(), nIe = it->second.notifies.end();
			for (;nIt!=nIe;++nIt)
			{
				(*nIt)->OnSoundLoaded(m_filename, succ);
			}
			m_pDSMan->m_loadingUrls.erase(it);
		}

	public:
		SoundReadTask(const std::wstring &filename, CFileIOManager *pFileIOMan, DirectSoundManager *pDSMan, XPISoundType type)
			: CFileIOManager::AsyncReadTask(pFileIOMan, filename, 0), m_pDSMan(pDSMan), m_logicType(type)
		{
			m_tick = GetMilliSeconds();
		}

		virtual void OnReady()
		{
			if( m_data.size() == 0 )
			{
				XPLOG_ERROR(L"Async load sound failed!, path is %s\n", m_filename.c_str());
				XPTRACE(L"Async load sound failed!, path is %s\n", m_filename.c_str());
				OnDiscard();
				return;
			} else {
				m_pFileIOMan->OnReady(m_tick, m_data.size());
				XPTRACE(L"Async load sound ok!, path is %s\n", m_filename.c_str());
				XPSOUND_TYPE type = DirectSoundManager::GetFileType(m_filename.c_str());
				if (m_logicType == XPIST_EMPTY)
				{
					//����100k��Ogg��Ū��StreamSound��
					if ((type == XPSOUNDTYPE_OGG) && (m_data.size() > 102400))
					{
						m_logicType = XPIST_STREAM_SOUND;
					} else {
						m_logicType = XPIST_STATIC_SOUND;
					}
				}
				if (m_logicType == XPIST_STATIC_SOUND)
				{
					//StaticSound...............
					DirectSoundManager::SoundPoolMap::iterator spIt = m_pDSMan->m_Pools.find(m_filename);
					SoundBufferPool* pPool = NULL;
					if (spIt != m_pDSMan->m_Pools.end() && spIt->second != NULL)
					{
						pPool = spIt->second;
					} else {
						pPool = new SoundBufferPool();
						if (pPool->Initialize(m_data, m_pDSMan->m_pDirectSound, 0, type))
						{
							m_pDSMan->m_Pools[m_filename] = pPool;
							pPool->SetTick(Nuclear::GetMilliSeconds());
						} else {
							XPTRACE(L"Initialize sound error!, path is %s\n", m_filename.c_str());
							XPLOG_ERROR(L"Initialize sound error!, path is %s\n", m_filename.c_str());
							delete pPool;
							OnDiscard();
							return;
						}
					}
				} else {
					//StreamSound...............
					m_data.SetTick(Nuclear::GetMilliSeconds());
					m_pDSMan->m_mapStreamSoundDatas[m_filename] = m_data;
				}
				NotifyAll(true);
				delete this;
			}
		}

		virtual void OnDiscard() 
		{
			NotifyAll(false);
			delete this;
		}
		virtual ~SoundReadTask() { }
	};

	DirectSoundManager::DirectSoundManager(EngineBase *pEngineBase) : m_pDirectSound(NULL), 
		m_hStreamFillThread(NULL), m_hMainThread(NULL), m_bStreamSoundRuning(false), m_fadeState(XPFS_NORMAL), 
		m_pFileIOMan(pEngineBase->GetFileIOManager()), m_pEB(pEngineBase), m_dwStreamFillThreadID(0),
		m_nInactiveFadeOutTime(0), m_nActiveFadeInTime(0)
	{
	}

	DirectSoundManager::~DirectSoundManager(void)
	{
		if (m_dwStreamFillThreadID)
		{
			m_bStreamSoundRuning = false;//�������ˣ����˳�ʼ��������ôһ����ֵ�ĵط�
			WaitForSingleObject(m_hStreamFillThread, INFINITE); 
			CloseHandle(m_hStreamFillThread);
			CloseHandle(m_hMainThread);
		}
		QueueUserAPC(DummyFunc, GetCurrentThread(), NULL);
		::SleepEx(0, TRUE);
		LoadingSounds::iterator lIt = m_loadingUrls.begin(), lIe = m_loadingUrls.end();
		for (;lIt != lIe;++lIt)
		{
			DirectSoundManager::SHandleVet::iterator hIt = lIt->second.handles.begin(), hIe = lIt->second.handles.end();
			for (;hIt!=hIe;++hIt)
			{
				SoundHandle handle = *hIt;
				if (handle->IsStoped())
					delete handle;
			}
		}
		SoundSet::iterator iter = m_setStaticSound.begin();
		for(;iter!=m_setStaticSound.end();++iter)
		{
			SoundHandle handle = *iter;
			XPASSERT((handle != NULL) && L"DirectSoundManager");
			handle->Stop();
			delete handle;
		}

		SoundPoolMap::iterator pIt = m_Pools.begin();
		for (;pIt!=m_Pools.end();++pIt)
		{
			delete pIt->second;
		}
		//m_hStreamFillThread�߳�ͣ��
		//�ӽ���һ��ɾ
		m_setStreamSounds.insert(m_setAddSounds.begin(), m_setAddSounds.end());
		m_setStreamSounds.insert(m_setDelSounds.begin(), m_setDelSounds.end());
		SoundSet::iterator siter = m_setStreamSounds.begin();
		for(;siter!=m_setStreamSounds.end();++siter)
		{
			delete (*siter);
		}

		//���ܻ�û�е���OnTick
		if (s_willDelSounds.size() > 0)
		{
			DirectSoundManager::StreamSoundList::iterator iter = s_willDelSounds.begin(), ie = s_willDelSounds.end();
			for (;iter!=ie;++iter)
			{
				delete *iter;
			}
			s_willDelSounds.clear();
		}

		//�ͷ�directsound 
		XPSAFE_RELEASE(m_pDirectSound); 
	}

	bool DirectSoundManager::Init(HWND hWnd, DWORD dwCoopLevel)
	{
		//���õȴ���ѭ��������������4000
		m_lockStreamSounds.SetSpinCount(4000);
		m_lockAddSounds.SetSpinCount(4000);
		m_lockDelSounds.SetSpinCount(4000);
		m_lockInitiativeDelSound.SetSpinCount(4000);
		HRESULT hr;
		//����directsound 
		if(FAILED(hr = DirectSoundCreate(NULL, &m_pDirectSound, NULL))) 
		{ 
			XPLOG_ERROR(L"DirectSoundCreateʧ��, %d\n", hr);
			return false; 
		} 
		if( ! ::DuplicateHandle(GetCurrentProcess(), GetCurrentThread(), GetCurrentProcess(), &m_hMainThread, NULL, FALSE, DUPLICATE_SAME_ACCESS) )
		{
			XPLOG_ERROR(L"DirectSound DuplicateHandle ʧ��\n");
			return false;
		}
		m_pDirectSound->SetCooperativeLevel(hWnd, dwCoopLevel);
		m_bStreamSoundRuning = true;
		m_hStreamFillThread = reinterpret_cast<HANDLE>(_beginthreadex(NULL, 0, StreamSoundFillThread, this, 0, &m_dwStreamFillThreadID));
		if (m_hStreamFillThread == NULL)
		{
			CloseHandle(m_hMainThread);
			XPLOG_ERROR(L"DirectSound �����ػ��߳�ʧ��\n");
			return false;
		}
		return true;
	}

	void DirectSoundManager::OnWindowStateChange(bool active)
	{
		if (m_nInactiveFadeOutTime < 0)
			return;
		float frate = 1.0f;
		XPFADE_SIGN newState = active?XPFS_FADE_IN:XPFS_FADE_OUT;
		XPFADE_SIGN oldState = m_fadeState;
		if (newState == m_fadeState)
			return;
		if (active)
		{
			if (m_fadeState == XPFS_NORMAL)
				return;
		} else {
			if (m_fadeState == XPFS_MUTE)
				return;
		}

		m_fadeState = newState;

		if (active)
		{
			if (m_nActiveFadeInTime == 0)
				m_fadeState = XPFS_NORMAL;
		} else {
			if (m_nInactiveFadeOutTime == 0)
				m_fadeState = XPFS_MUTE;
		}

		if (m_fadeState != XPFS_NORMAL && m_fadeState != XPFS_MUTE)
		{
			if (oldState != XPFS_NORMAL && oldState != XPFS_MUTE)
			{
				int now = m_pEB->GetTick();
				if (active)
				{
					now -= (m_nInactiveFadeOutTime + m_nBeginTick - now) * m_nActiveFadeInTime / m_nInactiveFadeOutTime;
					frate = static_cast<float>(m_pEB->GetTick() - now) / m_nActiveFadeInTime;
				} else {
					now -= (m_nActiveFadeInTime + m_nBeginTick - now) * m_nInactiveFadeOutTime / m_nActiveFadeInTime;
					frate = static_cast<float>(m_pEB->GetTick() - now) / m_nInactiveFadeOutTime;
				}
				m_nBeginTick = now;
				
			} else {
				m_nBeginTick = m_pEB->GetTick();
				frate = 0.0f;
			}
		}

		SoundSet::iterator it, ie;
		SoundSet *pSoundSet[3] = { &m_setStaticSound, &m_setStreamSounds, &m_setAddSounds};
		XCSLock *pLock[3] = { NULL, &m_lockStreamSounds, &m_lockAddSounds };
		for (int i=0;i<3;++i)
		{
			if (pLock[i])
				pLock[i]->Lock();
			it = pSoundSet[i]->begin();
			ie = pSoundSet[i]->end();
			for (;it!=ie;++it)
			{
				SoundWrap *pSound = *it;
				XPASSERT(pSound != NULL);
				
				if (m_fadeState == XPFS_NORMAL || m_fadeState == XPFS_MUTE)
				{
					pSound->SetFadeRate(1.0f, true);
					pSound->OnWindowStateChange(m_fadeState);
				} else {
					pSound->SetFadeRate(frate, true);
					pSound->OnWindowStateChange(newState);
				}
			}
			if (pLock[i])
				pLock[i]->Unlock();
		}
	}

	int DirectSoundManager::GetAllSoundCount() const
	{
		return (int)(m_Pools.size() + m_mapStreamSoundDatas.size());
	}

	void DirectSoundManager::GetAllSoundDetail(std::vector<std::wstring> &detail) const
	{
		detail.clear();
		SoundPoolMap::const_iterator sIt = m_Pools.begin(), sIe = m_Pools.end();
		for (;sIt!=sIe;++sIt)
		{
			detail.push_back(sIt->first);
		}
		StreamSoundDataMap::const_iterator streamIt = m_mapStreamSoundDatas.begin(), streamIe = m_mapStreamSoundDatas.end();
		for (;streamIt!=streamIe;++streamIt)
		{
			detail.push_back(streamIt->first);
		}
	}

	bool DirectSoundManager::SetPrimaryBufferFormat(WORD dwPrimaryChannels, DWORD dwPrimaryFreq, WORD dwPrimaryBitRate)
	{
		//������������ 
		LPDIRECTSOUNDBUFFER pDSBPrimary = NULL; 
		HRESULT hr;
		// Get the primary buffer  
		DSBUFFERDESC dsbd; 
		ZeroMemory(&dsbd, sizeof(DSBUFFERDESC)); 
		dsbd.dwSize        = sizeof(DSBUFFERDESC); 
		dsbd.dwFlags       = DSBCAPS_PRIMARYBUFFER; 
		dsbd.dwBufferBytes = 0; 
		dsbd.lpwfxFormat   = NULL; 

		WAVEFORMATEX wfx;
		ZeroMemory(&wfx, sizeof(WAVEFORMATEX));
		wfx.wFormatTag      = (WORD) WAVE_FORMAT_PCM;
		wfx.nChannels       = dwPrimaryChannels;
		wfx.nSamplesPerSec  = dwPrimaryFreq;
		wfx.wBitsPerSample  = dwPrimaryBitRate;
		wfx.nBlockAlign     = (WORD) (wfx.wBitsPerSample / 8 * wfx.nChannels);
		wfx.nAvgBytesPerSec = (DWORD) (wfx.nSamplesPerSec * wfx.nBlockAlign);

		if (FAILED(hr=m_pDirectSound->CreateSoundBuffer(&dsbd, &pDSBPrimary, NULL))) 
		{
			XPLOG_ERROR(L"��������CreateSoundBufferʧ��, %d\n", hr);
			return false;
		}

		if(FAILED(hr = pDSBPrimary->SetFormat(&wfx))) 
		{ 
			XPLOG_ERROR(L"pDSBPrimary->SetFormat(&wfx)ʧ��, %d\n", hr);
			pDSBPrimary->Release();
			return false; 
		} 
		pDSBPrimary->Release();	 
		return true;
	}

	XPSOUND_TYPE DirectSoundManager::GetFileType(const std::wstring &fn)
	{
		TCHAR extension[3];
		int len = fn.size();
		extension[0] = towlower(fn[len-3]);
		extension[1] = towlower(fn[len-2]);
		extension[2] = towlower(fn[len-1]);
		if (extension[0] == L'o' && extension[1] == L'g' && extension[2] == L'g')
			return XPSOUNDTYPE_OGG;
		if (extension[0] == L'w' && extension[1] == L'a' && extension[2] == L'v')
			return XPSOUNDTYPE_WAVE;
		return XPSOUNDTYPE_WAVE;
	}

	void DirectSoundManager::StopTypeSound(unsigned char type)
	{
		SoundSet::iterator it = m_setStaticSound.begin(), ie = m_setStaticSound.end();
		int tick = m_pEB->GetTick();
		for (;it!=ie;)
		{
			SoundWrap *pSoundWrap = *it;
			XPASSERT((pSoundWrap != NULL) && L"StopAllStaticSound");
			if (pSoundWrap->GetSoundType() != type)
			{
				++it;
				continue;
			}
			m_setPrioritySet.erase(PriorityStruct(pSoundWrap->GetPriority(), pSoundWrap));
			m_setStaticSound.erase(it++);
			pSoundWrap->Stop();
			StaticSound* pSound = pSoundWrap->GetStaticSound();
			if (pSound)
			{
				pSound->SetTick(tick);
				delete pSoundWrap;
			}
		}

		SoundSet *pSoundSet[2] = { &m_setStreamSounds, &m_setAddSounds};
		XCSLock *pLock[2] = { &m_lockStreamSounds, &m_lockAddSounds };
		for (int i=0;i<2;++i)
		{
			pLock[i]->Lock();
			it = pSoundSet[i]->begin();
			ie = pSoundSet[i]->end();
			for (;it!=ie;++it)
			{
				SoundWrap *pSound = *it;
				XPASSERT(pSound != NULL);
				if (pSound->GetSoundType() != type)
					continue;
				m_setPrioritySet.erase(PriorityStruct(pSound->GetPriority(), pSound));
				pSound->Stop();
				m_lockDelSounds.Lock();
				m_setDelSounds.insert(pSound);
				m_lockDelSounds.Unlock();
			}
			pLock[i]->Unlock();
		}
	}

	void DirectSoundManager::PauseTypeSound(unsigned char type)
	{
		SoundSet::iterator it = m_setStaticSound.begin(), ie = m_setStaticSound.end();
		for (;it!=ie;++it)
		{
			SoundWrap *pSoundWrap = *it;
			XPASSERT((pSoundWrap != NULL) && L"PauseTypeSound");
			if (pSoundWrap->GetSoundType() != type)
			{
				continue;
			}
			m_setPrioritySet.erase(PriorityStruct(pSoundWrap->GetPriority(), pSoundWrap));
			pSoundWrap->Pause();
		}

		SoundSet *pSoundSet[2] = { &m_setStreamSounds, &m_setAddSounds};
		XCSLock *pLock[2] = { &m_lockStreamSounds, &m_lockAddSounds };
		for (int i=0;i<2;++i)
		{
			pLock[i]->Lock();
			it = pSoundSet[i]->begin();
			ie = pSoundSet[i]->end();
			for (;it!=ie;++it)
			{
				SoundWrap *pSound = *it;
				XPASSERT(pSound != NULL);
				if (pSound->GetSoundType() != type)
					continue;
				m_setPrioritySet.erase(PriorityStruct(pSound->GetPriority(), pSound));
				pSound->Pause();
			}
			pLock[i]->Unlock();
		}
	}

	void DirectSoundManager::ResumeTypeSound(unsigned char type)
	{
		SoundSet::iterator it, ie;
		SoundSet *pSoundSet[3] = { &m_setStaticSound, &m_setStreamSounds, &m_setAddSounds};
		XCSLock *pLock[3] = { NULL, &m_lockStreamSounds, &m_lockAddSounds };
		for (int i=0;i<3;++i)
		{
			if (pLock[i])
				pLock[i]->Lock();
			it = pSoundSet[i]->begin();
			ie = pSoundSet[i]->end();
			for (;it!=ie;++it)
			{
				SoundWrap *pSound = *it;
				XPASSERT(pSound != NULL && L"ResumeTypeSound");
				if (pSound->GetSoundType() != type)
					continue;
				if (pSound->Resume())
				{
					m_setPrioritySet.insert(PriorityStruct(pSound->GetPriority(), pSound));
				}
			}
			if (pLock[i])
				pLock[i]->Unlock();
		}
	}

	CHECK_SOUND_STATE DirectSoundManager::CheckSound(const std::wstring &fn, bool async, ISoundLoadNotify* pNotify, XPISoundType& type)
	{
		if (!m_pDirectSound)
			return CSS_FAILD;

		if (m_Pools.find(fn) != m_Pools.end())
		{
			type = XPIST_STATIC_SOUND;
			return CSS_CHECK_OK;
		}
			
		if (m_mapStreamSoundDatas.find(fn) != m_mapStreamSoundDatas.end())
		{
			type = XPIST_STREAM_SOUND;
			return CSS_CHECK_OK;
		}

		if (async && m_pEB->IsAsyncRead())//�첽
		{
			LoadingSounds::iterator it = m_loadingUrls.find(fn);
			if (it == m_loadingUrls.end())
			{
				//��û�м���
				SoundReadTask *pTask = new SoundReadTask(fn, m_pFileIOMan, this, type);
				if (pNotify)
				{
					m_loadingUrls[fn].notifies.push_back(pNotify);
				} else {
					m_loadingUrls.insert(std::make_pair(fn, LoadingParam()));
				}
				//m_loadingUrls[fn].push_back(INVALID_SOUND_HANDLE);
				pTask->Submit();
			} else {
				if (pNotify)
				{
					it->second.notifies.push_back(pNotify);
				}
			}
			return CSS_LOADING;
		} else { //ͬ��
			XBuffer buf;
			if( m_pFileIOMan->GetFileImage(fn, buf) )
			{
				XPSOUND_TYPE fileType = GetFileType(fn.c_str());
				//����100k��Ogg��Ū��StreamSound��
				if (((fileType == XPSOUNDTYPE_OGG) && (buf.size() > 102400)) || type == XPIST_STREAM_SOUND )
				{
					buf.SetTick(m_pEB->GetTick());
					m_mapStreamSoundDatas[fn] = buf;
					type = XPIST_STREAM_SOUND;
					return CSS_CHECK_OK;
				}

				SoundBufferPool *pool = new SoundBufferPool();
				if (!pool)
					return CSS_FAILD;
				pool->SetTick(m_pEB->GetTick());
				if (pool->Initialize(buf, m_pDirectSound, 0, fileType))
				{
					m_Pools[fn] = pool;
					type = XPIST_STATIC_SOUND;
					return CSS_CHECK_OK;
				}
				delete pool;
			}
			return CSS_FAILD;
		}
	}

	DWORD DirectSoundManager::GetSoundLength(const std::wstring &fn)
	{
		SoundPoolMap::iterator staticIt = m_Pools.find(fn);
		if (staticIt != m_Pools.end())
		{
			return staticIt->second->GetPlayLength();
		}
		StreamSoundDataMap::iterator streamIt = m_mapStreamSoundDatas.find(fn);
		if (streamIt != m_mapStreamSoundDatas.end())
		{
			ISoundData *pData = ISoundData::GetSoundData(GetFileType(fn));
			if (pData)
			{
				DWORD result = 0;
				if (pData->Load(streamIt->second))
				{
					result = pData->GetPlayLength();
				}
				delete pData;
				return result;
			}
		}
		return 0;
	}

	void DirectSoundManager::GarbageCollection(int nowtick)
	{
		SoundPoolMap::iterator it = m_Pools.begin(), ie = m_Pools.end();
		for (;it!=ie;)
		{
			SoundBufferPool *pool = it->second;
			XPASSERT(pool != NULL);
			if ((nowtick - pool->GetTick() > 10000) && (!pool->IsPlaying()))
			{
				m_Pools.erase(it++);
				delete pool;
			} else {
				++it;
			}
		}

		StreamSoundDataMap::iterator sIt = m_mapStreamSoundDatas.begin(), 
			sIe = m_mapStreamSoundDatas.end();
		for (;sIt!=sIe;)
		{
			if ((sIt->second.GetRefCount() == 1) && 
				(nowtick - sIt->second.GetTick() > 120000))
			{
				m_mapStreamSoundDatas.erase(sIt++);
			} else {
				++sIt;
			}
		}

	}

	bool DirectSoundManager::TestPriority(short priority) const
	{
		PrioritySoundSet::const_reverse_iterator setRIt = m_setPrioritySet.rbegin();
		if (m_setPrioritySet.size() >= MAX_PLAYING_STATIC_SOUND)
		{
			if (setRIt->priority <= priority)
				return false;
		}
		return true;
	}

	bool DirectSoundManager::ReplySound(SoundHandle handle, const std::wstring &filename, XPISoundType soundType)//�������˿��Բ�����
	{
		SoundSet::iterator it = m_setStaticSound.find(handle);
		if (it == m_setStaticSound.end())
			return false;
		
		PrioritySoundSet::reverse_iterator setRIt = m_setPrioritySet.rbegin();
		bool mustCut = false;
		if (m_setPrioritySet.size() >= MAX_PLAYING_STATIC_SOUND)
		{
			if (setRIt->priority <= handle->GetPriority())
				return false;
			mustCut = true;
		}
		VolumeStruct& volStruct = m_volumeMap[handle->GetType()];
		if (!volStruct.bEnable)
			return false;

		if (mustCut)
		{
			if (!StopSound(setRIt->handle))
				return false;
		}
		if (soundType == XPIST_STATIC_SOUND)
		{
			if (handle->Init(m_Pools[filename]) && handle->Play(handle->IsLooping()))
			{
				m_setPrioritySet.insert(PriorityStruct(handle->GetPriority(), handle));
				return true;
			}
			handle->FreeRes();
		} else {
			m_setStaticSound.erase(it);
			StreamSoundDataMap::iterator iter = m_mapStreamSoundDatas.find(filename);
			if (iter != m_mapStreamSoundDatas.end())
			{
				if (iter->second.size() > 0)
				{
					if (handle->Init(this, iter->second, true, GetFileType(filename)) && handle->Play(handle->IsLooping()))
					{
						m_setPrioritySet.insert(PriorityStruct(handle->GetPriority(), handle));
						//m_setAddSounds��
						m_lockAddSounds.Lock();
						m_setAddSounds.insert(handle);
						m_lockAddSounds.Unlock();
						return true;	
					}
				}			
			}
		}	
		return false;
	}

	SoundHandle DirectSoundManager::XPPlaySound(const std::wstring &fn, short priority, unsigned char type, unsigned char volume, unsigned char freq, bool looping)
	{
		if (!m_pDirectSound)
			return INVALID_SOUND_HANDLE;
		//�Ժ���������֣�һ���Ǵ�ϸ������ȼ��ģ�һ���ǲ�����
		//Ŀǰ�Ǹ����ȼ����˾ʹ�ϵ����ȼ���
		PrioritySoundSet::reverse_iterator setRIt = m_setPrioritySet.rbegin();
		bool mustCut = false;
		if (m_setPrioritySet.size() >= MAX_PLAYING_STATIC_SOUND)
		{
			if (setRIt->priority <= priority)
				return INVALID_SOUND_HANDLE;
			mustCut = true;
		}
		VolumeStruct& volStruct = m_volumeMap[type];
		if (!volStruct.bEnable)
			return INVALID_SOUND_HANDLE;

		XPISoundType logicType = XPIST_EMPTY;
		switch (CheckSound(fn, true, NULL, logicType))
		{
		case CSS_CHECK_OK:
			{
				if (mustCut)
				{
					if (!StopSound(setRIt->handle))
						return INVALID_SOUND_HANDLE;
				}
				SoundWrap *pSound = new SoundWrap();
				if (!pSound)
					return INVALID_SOUND_HANDLE;
				pSound->SetSoundType(type);
				pSound->SetFrequency(freq);
				pSound->SetTotalVolume(volStruct.fVolume, volume);
				pSound->SetPriority(priority);
				pSound->OnWindowStateChange(m_fadeState);

				switch (logicType)
				{
				case XPIST_STATIC_SOUND:
					if (pSound->Init(m_Pools[fn]) && pSound->Play(looping))
					{
						m_setStaticSound.insert(pSound);
						m_setPrioritySet.insert(PriorityStruct(priority, pSound));
						return pSound;
					}
					break;
				case XPIST_STREAM_SOUND:
					if (pSound->Init(this, m_mapStreamSoundDatas[fn], true, GetFileType(fn)) && pSound->Play(looping))
					{
						m_setPrioritySet.insert(PriorityStruct(priority, pSound));
						//m_setAddSounds��
						m_lockAddSounds.Lock();
						m_setAddSounds.insert(pSound);
						m_lockAddSounds.Unlock();
						return pSound;
					}
					break;
				}
				delete pSound;
			}
			break;
		case CSS_LOADING:
			{
				SoundWrap *pSound = new SoundWrap();
				if (!pSound)
					return INVALID_SOUND_HANDLE;
				m_loadingUrls[fn].handles.push_back(pSound);

				pSound->OnWindowStateChange(m_fadeState);
				pSound->SetSoundType(type);
				pSound->SetFrequency(freq);
				pSound->SetTotalVolume(volStruct.fVolume, volume);
				pSound->SetPriority(priority);
				pSound->Play(looping);
				m_setStaticSound.insert(pSound);
				return pSound;
			}
			break;
		default:
			break;
		}
		return INVALID_SOUND_HANDLE;
	}

	void DirectSoundManager::SetNotify(SoundHandle handle, ISoundNotify *pNotify)
	{
		bool ok = m_setStaticSound.find(handle) != m_setStaticSound.end();
		if (!ok)
		{
			m_lockStreamSounds.Lock();
			ok = m_setStreamSounds.find(handle) != m_setStreamSounds.end();
			m_lockStreamSounds.Unlock();
			if (!ok)
			{
				m_lockAddSounds.Lock();
				ok = m_setAddSounds.find(handle) != m_setAddSounds.end();//����ӽ���
				m_lockAddSounds.Unlock();
				if (!ok)
				{
					m_lockInitiativeDelSound.Lock();
					ok = m_setInitiativeDelSound.find(handle) != m_setInitiativeDelSound.end();//�ող����ˣ����ǻ�û��ɾ����׼��Notify��
					m_lockInitiativeDelSound.Unlock();
				}
			}
		}
		//�����ж�ɾ����(��m_setDelSounds)�������ɾ����Ҳ����SetNotify;
		if (ok)
		{
			handle->SetNotify(pNotify);
		}
	}

	bool DirectSoundManager::StopSound(SoundHandle handle)
	{
		SoundSet::iterator iter = m_setStaticSound.find(handle);
		if (iter != m_setStaticSound.end())
		{
			handle->Stop();
			m_setStaticSound.erase(iter);
			StaticSound* pSound = handle->GetStaticSound();
			if (pSound)
			{
				//��������StreamSound�������û�м�������Ȳ�Delete
				XPASSERT(handle->GetType() == XPIST_STATIC_SOUND);
				pSound->SetTick(m_pEB->GetTick());
				m_setPrioritySet.erase(PriorityStruct(handle->GetPriority(), handle));
				delete handle;
			}
			return true;
		}
		if (CheckStreamSound(handle))
		{
			handle->Stop();
			m_setPrioritySet.erase(PriorityStruct(handle->GetPriority(), handle));
			m_lockDelSounds.Lock();
			m_setDelSounds.insert(handle);
			m_lockDelSounds.Unlock();
			return true;
		}
		return false;
	}

	//fVolume[0, 1]
	void DirectSoundManager::SetSoundTypeVolume(unsigned char type, float fVolume)
	{
		VolumeStruct& volStruct = m_volumeMap[type];
		if (volStruct.fVolume != fVolume)	//ֱ��!=�ͺã����ÿ���ľ���ֵ��
		{
			volStruct.fVolume = fVolume;
			SoundSet::iterator it, ie;
			SoundSet *pSoundSet[3] = { &m_setStaticSound, &m_setStreamSounds, &m_setAddSounds};
			XCSLock *pLock[3] = { NULL, &m_lockStreamSounds, &m_lockAddSounds };
			for (int i=0;i<3;++i)
			{
				if (pLock[i])
					pLock[i]->Lock();
				it = pSoundSet[i]->begin();
				ie = pSoundSet[i]->end();
				for (;it!=ie;++it)
				{
					SoundWrap *pSound = *it;
					XPASSERT(pSound != NULL);
					if (pSound->GetSoundType() == type)
					{
						pSound->SetTypeVolume(fVolume);
					}
				}
				if (pLock[i])
					pLock[i]->Unlock();
			}
		}
	}

	void DirectSoundManager::EnableSoundType(unsigned char type, bool bEnable)
	{
		VolumeStruct& volStruct = m_volumeMap[type];
		if (volStruct.bEnable != bEnable)
		{
			volStruct.bEnable = bEnable;
			if (!bEnable)
			{
				StopTypeSound(type);
			}
		}
	}

	bool DirectSoundManager::CheckSound(SoundHandle handle)
	{
		if (m_setStaticSound.find(handle) != m_setStaticSound.end())
			return true;
		return CheckStreamSound(handle);
	}

	bool DirectSoundManager::SetSoundVolume(SoundHandle handle, unsigned char volume)
	{
		if (!CheckSound(handle))
			return false;
		handle->SetVolume(volume);
		return true;
	}

	
	//======================================ż��StreamSound�ηָ���============================
	//��һ���̵߳Ľ���ͦ�鷳�ģ�Ϊ�˼���������Ƶ��ر��ӣ�Ҳ���Ҿ��鲻���ɡ�
	//��Ҫ�������飨����������������˵���ػ��߳�Ҳ�����������IO�߳�
	//��һ��ֻ��һ������������m_setStreamSounds����ֻ���ػ��߳̽��в���ɾ����
	//�������ػ��߳�����Ķ�ȡ��find�������ȣ�����������ס�߳���������ж�ȡ����������

	//�ڶ�����m_setAddSounds��m_setDelSounds��������ֻ�����߳̽��в���ɾ����
	//���������߳�����Ķ�ȡ�����������ػ��߳���������ж�ȡ����������


	//�������£�
	//1 �������
	//  ���̰߳�StreamSound����m_setAddSounds����m_setAddSounds����
	//  �ػ��߳���m_setStreamSounds��m_setAddSounds����m_setAddSounds������Ų��m_setStreamSounds��
	//  ���m_setAddSounds
	//2 ɾ������
	//  ɾ�����ַ����������һ�����û�����ɾ��������һ���ǲ�����ɺ��Զ�ɾ��
	//  ��һ����������̰߳���������m_setDelSounds����m_setDelSounds����
	//	�ػ��߳���m_setStreamSounds��m_setDelSounds����m_setStreamSoundsɾ��m_setDelSounds�����������
	//  ���m_setDelSounds����������APC����s_willDelSounds
	//  �ڶ�����������ʱ����������m_setDelSounds
	//  Ȼ�������̵߳Ȼص�����֮���s_willDelSounds�������ڴ�����ɾ��
	//����Ϊʲô�������̰߳����/ɾ����������APC���н��в�����������Ϊ���š�������������ͣ�Ȳ�����Ҫ��飬
	//����ػ��̻߳�û�����ü�ִ��APC���оͻ�������

	bool DirectSoundManager::CheckStreamSound(SoundHandle handle)
	{
		bool result = false;
		m_lockStreamSounds.Lock();
		result = m_setStreamSounds.find(handle) != m_setStreamSounds.end();
		m_lockStreamSounds.Unlock();
		if (result)
		{
			m_lockDelSounds.Lock();
			result = m_setDelSounds.find(handle) == m_setDelSounds.end();//û�б�ɾ��
			m_lockDelSounds.Unlock();
		} else {
			m_lockAddSounds.Lock();
			result = m_setAddSounds.find(handle) != m_setAddSounds.end();//����ӽ���
			m_lockAddSounds.Unlock();
		}
		return result;
	}

	void DirectSoundManager::CancelSoundLoadingNotify(const std::wstring &fn, ISoundLoadNotify* pNotify)
	{
		LoadingSounds::iterator it = m_loadingUrls.find(fn);
		if (it != m_loadingUrls.end())
		{
			ISoundLoadNotifyList &nlist = it->second.notifies;
			ISoundLoadNotifyList::iterator nIt = nlist.begin(), nIe = nlist.end();
			for (;nIt!=nIe;)
			{
				if (*nIt == pNotify)
				{
					nIt = nlist.erase(nIt);
				} else {
					++nIt;
				}
			}
		}
	}

	SoundHandle DirectSoundManager::CreateStreamSound(const std::wstring &fn, ISoundNotify* pNotify)
	{
		StreamSoundDataMap::iterator iter = m_mapStreamSoundDatas.find(fn);
		if (iter != m_mapStreamSoundDatas.end())
		{
			if (iter->second.size() > 0)
			{
				SoundWrap *pSound = new SoundWrap();
				if (!pSound)
					return INVALID_SOUND_HANDLE;
				pSound->OnWindowStateChange(m_fadeState);
				pSound->SetNotify(pNotify);
				if (!pSound->Init(this, iter->second, true, GetFileType(fn)))
				{
					delete pSound;
					return INVALID_SOUND_HANDLE;
				}
				
				//m_setAddSounds��
				m_lockAddSounds.Lock();
				m_setAddSounds.insert(pSound);
				m_lockAddSounds.Unlock();
				return pSound;
			}			
		}
		return INVALID_SOUND_HANDLE;
	}

	bool DirectSoundManager::PushNextStreamSound(SoundHandle handle, const std::wstring &fn)
	{
		StreamSoundDataMap::iterator iter = m_mapStreamSoundDatas.find(fn);
		if (iter == m_mapStreamSoundDatas.end())
			return false;
		if (iter->second.size() == 0)
			return false;
		if (CheckStreamSound(handle))
		{
			return handle->GetStreamSound()->PushNextBuufer(iter->second, GetFileType(fn));
		}
		return false;
	}

	bool DirectSoundManager::PreparePlayStreamSound(SoundHandle handle, short priority, unsigned char type)
	{
		PrioritySoundSet::reverse_iterator setRIt = m_setPrioritySet.rbegin();
		bool mustCut = false;
		if (m_setPrioritySet.size() >= MAX_PLAYING_STATIC_SOUND)
		{
			if (setRIt->priority <= priority)
				return false;
			mustCut = true;
		}
		VolumeStruct& volStruct = m_volumeMap[type];
		if (!volStruct.bEnable)
			return false;
		if (CheckStreamSound(handle))
		{
			if (mustCut)
			{
				if (!StopSound(setRIt->handle))
					return false;
			}
			handle->SetSoundType(type);
			handle->SetTypeVolume(volStruct.fVolume);
			handle->SetPriority(priority);
			return true;
		}
		return false;
	}

	bool DirectSoundManager::PlayStreamSound(SoundHandle handle, short priority, unsigned char type, bool looping)
	{
		if (PreparePlayStreamSound(handle, priority, type))
		{
			return handle->Play(looping);
		}
		return false;
	}

	bool DirectSoundManager::PlayAndFadeIn(SoundHandle handle, unsigned short time, short priority, unsigned char type, bool bLooping)
	{
		if (PreparePlayStreamSound(handle, priority, type))
		{
			return handle->GetStreamSound()->PlayAndFadeIn(time, bLooping);
		}
		return false;
	}

	bool DirectSoundManager::FadeOutToStop(SoundHandle handle, unsigned short time)
	{
		if (CheckStreamSound(handle))
		{
			if (time == 0)
			{
				handle->Stop();
				m_setPrioritySet.erase(PriorityStruct(handle->GetPriority(), handle));
				m_lockDelSounds.Lock();
				m_setDelSounds.insert(handle);
				m_lockDelSounds.Unlock();
				return true;
			}
			return handle->GetStreamSound()->FadeOutToStop(time);
		}
		return false;
	}

	bool DirectSoundManager::PauseSound(SoundHandle handle)
	{
		if (CheckSound(handle))
		{
			handle->Pause();
			return true;
		}
		return false;
	}

	bool DirectSoundManager::IsSoundPause(SoundHandle handle)
	{
		if (CheckSound(handle))
		{
			return handle->IsPaused();
		}
		return false;
	}
	bool DirectSoundManager::IsSoundSoundPlaying(SoundHandle handle)
	{
		if (!CheckSound(handle))
			return false;
		return handle->IsPlaying();
	}

	void DirectSoundManager::OnTick()
	{
		{
			//����FadeRate
			switch (m_fadeState)
			{
			case XPFS_NORMAL:
			case XPFS_MUTE:
				break;
			case XPFS_FADE_IN:
				m_fFadeRate = static_cast<float>(m_pEB->GetTick() - m_nBeginTick) / m_nActiveFadeInTime;
				if (m_fFadeRate >= 1.0f)
				{
					m_fadeState = XPFS_NORMAL;
					m_fFadeRate = 1.0f;
				}
				break;
			case XPFS_FADE_OUT:
				m_fFadeRate = static_cast<float>(m_pEB->GetTick() - m_nBeginTick) / m_nInactiveFadeOutTime;
				if (m_fFadeRate >= 1.0f)
				{
					m_fadeState = XPFS_MUTE;
					m_fFadeRate = 1.0f;
				}
				break;
			}

		}
		{
			//StaticSound�Ĳ��֡�����
			float fFade = 1.0f;
			bool bFade = false;
			
			SoundSet::iterator iter = m_setStaticSound.begin(), ie = m_setStaticSound.end();
			int tick = m_pEB->GetTick();
			StaticSound *pSound = NULL;
			SoundHandle handle = NULL;
			for (;iter!=ie;)
			{
				handle = *iter;
				handle->SetFadeRate(m_fFadeRate, false);
				if (handle->GetType() == XPIST_EMPTY)
				{
					++iter;
					continue;
				}
				pSound = handle->GetStaticSound();
				XPASSERT((pSound != NULL) && L"OnTick");
				if (pSound->CheckSoundStop())
				{
					pSound->SetTick(tick);
					m_setPrioritySet.erase(PriorityStruct(handle->GetPriority(), handle));
					m_setStaticSound.erase(iter++);
					handle->NotifyPlayEnd();
					delete handle;
				} else {
					++iter;
				}
			}
		}

		{
			//StreamSound���֡�������
			if (s_streamPlayEndNotify.size() > 0)
			{
				SoundHandle handle = NULL;
				DirectSoundManager::StreamSoundList::iterator it = s_streamPlayEndNotify.begin(), 
					ie = s_streamPlayEndNotify.end();
				for (;it!=ie;++it)
				{
					handle = (*it);
					handle->NotifyPlayEnd();
					handle->SetNotify(NULL);
				}
				s_streamPlayEndNotify.clear();
			}

			if (s_streamPlayGoingEndNotify.size() > 0)
			{
				DirectSoundManager::StreamSoundList::iterator it = s_streamPlayGoingEndNotify.begin(), 
					ie = s_streamPlayGoingEndNotify.end();
				for (;it!=ie;++it)
				{
					(*it)->NotifyGoingToEnd();
				}
				s_streamPlayGoingEndNotify.clear();
			}

			if (s_willDelSounds.size() > 0)
			{
				SoundHandle handle = NULL;
				m_lockInitiativeDelSound.Lock();
				DirectSoundManager::StreamSoundList::iterator it = s_willDelSounds.begin(), ie = s_willDelSounds.end();
				for (;it!=ie;++it)
				{
					handle = *it;
					m_setPrioritySet.erase(PriorityStruct(handle->GetPriority(), handle));
					m_setInitiativeDelSound.erase(handle);
					delete handle;
				}
				m_lockInitiativeDelSound.Unlock();
				s_willDelSounds.clear();
				
			}
		}
	}

	void DirectSoundManager::Update(StreamSoundList &willDel)//�����������߳�ִ�еģ���Ҫ�Ǹ�StreamSound��
	{
		SoundSet::iterator iter = m_setStreamSounds.begin();
		for (;iter!=m_setStreamSounds.end() && m_bStreamSoundRuning;++iter)
		{
			SoundHandle handle = *iter;
			StreamSound* pSSound = handle->GetStreamSound();
			switch (pSSound->Update())
			{
			case StreamSound::XPSSUR_OK:
				handle->SetFadeRate(m_fFadeRate, false);
				break;
			case StreamSound::XPSSUR_FAILD://���ﲻBreak
				handle->Pause();
			case StreamSound::XPSSUR_PLAY_END:
				willDel.push_back(handle);
				if (handle->GetNotify())
				{
					QueueUserAPC(NotifyPlayEnd, GetMainThread(), (ULONG_PTR)handle);
				}

				break;
			}
		}
	}

	void DirectSoundManager::SetInactiveSoundFadeOutTime(int t) 
	{ 
		bool bActive = (GetActiveWindow() != NULL);
		if (t<0)
		{
			if ((m_nInactiveFadeOutTime >= 0) && !bActive)
			{
				m_nInactiveFadeOutTime = 0;
				OnWindowStateChange(true);
			}
			m_nInactiveFadeOutTime = -1;
		} else {
			if ((m_nInactiveFadeOutTime < 0) && !bActive)
			{
				m_nInactiveFadeOutTime = t;
				OnWindowStateChange(false);
			} else {
				m_nInactiveFadeOutTime = t;
			}
		}
	}

	void __stdcall DirectSoundManager::DelSound(ULONG_PTR pParam)
	{
		s_willDelSounds.push_back((SoundHandle)pParam);
	}

	void __stdcall DirectSoundManager::NotifyGoingToEnd(ULONG_PTR pParam)
	{
		s_streamPlayGoingEndNotify.push_back((SoundHandle)pParam);
	}

	void __stdcall DirectSoundManager::NotifyPlayEnd(ULONG_PTR pParam)
	{
		s_streamPlayEndNotify.push_back((SoundHandle)pParam);
	}

	unsigned int __stdcall DirectSoundManager::StreamSoundFillThread(void* pParam) 
	{ 
		DirectSoundManager *pDSM =(DirectSoundManager *)pParam; 
		StreamSoundList willDel;
		while(pDSM->IsStreamSoundRunning())
		{
			//��m_setAddSounds�������ӵ�m_setStreamSounds����
			SoundSet::iterator iter, iterEnd;
			
			pDSM->m_lockStreamSounds.Lock();
			pDSM->m_lockAddSounds.Lock();
			iter = pDSM->m_setAddSounds.begin();
			iterEnd = pDSM->m_setAddSounds.end();
			for (;iter!=iterEnd;++iter)
			{
				pDSM->m_setStreamSounds.insert(*iter);
			}
			pDSM->m_setAddSounds.clear();
			pDSM->m_lockAddSounds.Unlock();

			SoundHandle handle;
			//���Ҫ��Add�ĺ��棬�ܿ��ܸ�Add֮���Ҫɾ����
			//��m_setStreamSoundsɾ��m_setDelSounds���������
			pDSM->m_lockDelSounds.Lock();
			iter = pDSM->m_setDelSounds.begin();
			iterEnd = pDSM->m_setDelSounds.end();
			for (;iter!=iterEnd;++iter)
			{
				handle = *iter;
				if (pDSM->m_setStreamSounds.erase(handle) > 0)
					QueueUserAPC(DelSound, pDSM->m_hMainThread, (ULONG_PTR)handle);
			}
			pDSM->m_setDelSounds.clear();
			pDSM->m_lockDelSounds.Unlock();

			pDSM->m_lockStreamSounds.Unlock();

			willDel.clear();
			pDSM->Update(willDel);
			
			//�Ѳ�����ϵ���������m_setDelSounds
			pDSM->m_lockInitiativeDelSound.Lock();
			pDSM->m_lockStreamSounds.Lock();
			StreamSoundList::iterator listIt = willDel.begin(), listIe = willDel.end();
			for (;listIt!=listIe;++listIt)
			{
				handle = *listIt;
				pDSM->m_setStreamSounds.erase(handle);
				pDSM->m_setInitiativeDelSound.insert(handle);
				QueueUserAPC(DelSound, pDSM->m_hMainThread, (ULONG_PTR)handle);
			}
			pDSM->m_lockStreamSounds.Unlock();
			pDSM->m_lockInitiativeDelSound.Unlock();
			Sleep(20);
		}
		return 0; 
	} 

}