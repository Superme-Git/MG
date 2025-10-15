#include "stdafx.h"
#include "..\common\log.h"
#include "..\iengine.h"
#include "soundwrap.h"
#include "streamsound.h"
#include "directsoundmanager.h"


#pragma comment(lib,"dxguid")  

//----------------------------------------------------------------------------- 
// Construction/Destruction 
//----------------------------------------------------------------------------- 
namespace Nuclear
{
	StreamSound::StreamSound(DirectSoundManager* pDSman, Param *pParam) : ISound(pParam),
		m_pDirectSoundMan(pDSman), m_nFadeTime(0), m_nFadingTime(0), m_nFadeSign(XPFS_NORMAL), 
		m_pDSBuffer(NULL), m_dwDSBufferSize(0), m_dwNotifySize(0), m_bNotifyWillEnd(false),
		m_bFillNextNotificationWithSilence(false), m_dwPlayProgress(0),
		m_dwFillProgress(0), m_dwSoundSize(0), m_dwLastPlayPos(0), m_dwNextWriteOffset(0) 
	{ 
		m_bufferLock.SetSpinCount(4000);//设置等待的循环次数，先试试4000
		m_soundsLock.SetSpinCount(2000);
	} 

	StreamSound::~StreamSound() 
	{ 
		// 关闭Notify
		CloseHandle(m_hNotificationEvent); 
		ReleaseAllSounds();
		//释放缓冲区 
		XPSAFE_RELEASE(m_pDSBuffer); 
	}

	void StreamSound::ReleaseAllSounds()
	{
		m_soundsLock.Lock();
		ISoundDataList::iterator it = m_sounds.begin(), ie = m_sounds.end();
		for (;it!=ie;++it)
		{
			delete *it;
		}
		m_sounds.clear();
		m_soundsLock.Unlock();
	}

	inline bool operator==(const WAVEFORMATEX &p1, const WAVEFORMATEX &p2)
	{
		if (p1.cbSize != p2.cbSize)
			return false;
		if (p1.nAvgBytesPerSec != p2.nAvgBytesPerSec)
			return false;
		if (p1.nBlockAlign != p2.nBlockAlign)
			return false;
		if (p1.nChannels != p2.nChannels)
			return false;
		if (p1.nSamplesPerSec != p2.nSamplesPerSec)
			return false;
		if (p1.wBitsPerSample != p2.wBitsPerSample)
			return false;
		if (p1.wFormatTag != p2.wFormatTag)
			return false;
		return true;
	}

	bool StreamSound::Initialize(const XBuffer& buffer, bool bGlobalFocus, LPDIRECTSOUND pDirectSound, XPSOUND_TYPE type, ISoundNotify *pNotify) 
	{ 
		LRESULT hr; 
		DSBPOSITIONNOTIFY*  pPosNotify = NULL;  
		LPDIRECTSOUNDNOTIFY pDSNotify  = NULL; 

		//删除以前的内容 
		m_bFillNextNotificationWithSilence = false; 
		m_dwPlayProgress = 0; 
		m_dwFillProgress = 0;
		m_dwSoundSize = 0;
		m_dwLastPlayPos = 0; 
		m_dwNextWriteOffset = 0; 

		if (m_pDSBuffer) 
		{ 
			m_pDSBuffer->Stop(); 
			XPSAFE_RELEASE(m_pDSBuffer); 
		} 

		ReleaseAllSounds();
		ISoundData *pSoundData = ISoundData::GetSoundData(type);
		if (!pSoundData)
			return false;
		
		if (!pSoundData->Load(buffer))
		{
			delete pSoundData;
			return false;
		}
		//这里不锁了，因为都还没有开始播呢
		m_sounds.push_back(pSoundData);

		//m_curSoundSize = pSoundData->GetSize();
		WAVEFORMATEX *pWfx = pSoundData->GetFormat();

		//定义一个m_nBufferPlayLength秒的缓冲区,并将这个缓冲区分成m_nNotificationsNum个通知块; 
		m_dwNotifySize = pWfx->nSamplesPerSec * BUFFER_PLAY_LENGTH * pWfx->nBlockAlign / NOTIFICATIONS_NUM; 
		//确保m_dwNotifySize是nBlockAlign的倍数 
		m_dwNotifySize = m_dwNotifySize - m_dwNotifySize % pWfx->nBlockAlign;		 
		m_dwDSBufferSize = m_dwNotifySize * NOTIFICATIONS_NUM; 

		//创建辅助缓冲区 
		DSBUFFERDESC BufferDesc;//定义DSUBUFFERDESC结构对象; 
		memset (&BufferDesc, 0, sizeof (DSBUFFERDESC)); 	 
		BufferDesc.lpwfxFormat = pWfx;
		BufferDesc.dwSize = sizeof(DSBUFFERDESC); 
		BufferDesc.dwBufferBytes = m_dwDSBufferSize; 
		BufferDesc.guid3DAlgorithm = GUID_NULL; 
		if(bGlobalFocus) BufferDesc.dwFlags = DSBCAPS_GLOBALFOCUS; 
		else BufferDesc.dwFlags = 0; 
		BufferDesc.dwFlags |= DSBCAPS_CTRLVOLUME | DSBCAPS_CTRLFREQUENCY; 
		BufferDesc.dwFlags |= DSBCAPS_CTRLPOSITIONNOTIFY | DSBCAPS_GETCURRENTPOSITION2; 

		if(FAILED(hr = pDirectSound->CreateSoundBuffer(&BufferDesc, &m_pDSBuffer, 0))) 
		{ 
			XPLOG_ERROR(L"创建m_pDSBuffer失败, hr=%x\n", hr); 
			return false; 
		} 

		m_pDSBuffer->GetFrequency(&(m_pParam->m_nInitFreq));

		if(FAILED(hr = m_pDSBuffer->QueryInterface(IID_IDirectSoundNotify,  
			(VOID**)&pDSNotify))) 
		{ 
			XPLOG_ERROR(L"QueryInterface失败, hr=%x\n", hr); 
			return false; 
		}


		//创建大小为m_nNotificationsNum的DSBPOSITIONNOTIFY数组 
		m_hNotificationEvent = CreateEvent(NULL, FALSE, FALSE, NULL); 
		pPosNotify = new DSBPOSITIONNOTIFY[NOTIFICATIONS_NUM]; 
		for(int i=0; i < NOTIFICATIONS_NUM; i++) 
		{ 
			pPosNotify[i].dwOffset = m_dwDSBufferSize/NOTIFICATIONS_NUM*(i+1) - 1; 
			pPosNotify[i].hEventNotify = m_hNotificationEvent; 
		} 

		// Tell StreamSound when to notify us. The notification will come in the from  
		// of signaled events that are handled in WinMain() 
		if(FAILED(hr = pDSNotify->SetNotificationPositions(NOTIFICATIONS_NUM, pPosNotify))) 
		{
			XPSAFE_RELEASE(pDSNotify); 
			if (pPosNotify)
			{
				delete[] pPosNotify;
				pPosNotify = NULL;
			}
			XPLOG_ERROR(L"SetNotificationPositions失败, hr=%x\n", hr); 
			return false; 
		} 

		XPSAFE_RELEASE(pDSNotify); 
		if (pPosNotify)
		{
			delete[] pPosNotify;
			pPosNotify = NULL;
		}

		m_pParam->m_pNotify = pNotify;
		return true; 
	} 

	
	bool StreamSound::PushNextBuufer(const XBuffer& buffer,  XPSOUND_TYPE type)
	{
		ISoundData *pSoundData = ISoundData::GetSoundData(type);
		if (!pSoundData)
			return false;

		if (!pSoundData->Load(buffer))
		{
			delete pSoundData;
			return false;
		}
		m_soundsLock.Lock();
		m_sounds.push_back(pSoundData);
		m_soundsLock.Unlock();
		return true; 
	}
	
	//----------------------------------------------------------------------------- 
	// 恢复缓冲区和数据 
	//----------------------------------------------------------------------------- 

	bool StreamSound::RestoreBuffer() 
	{ 
		//这里的读取先不管了。。。。
		int todolostdevice;
		//丢失设备的时候应该返回而不是在这儿循环
		HRESULT hr;  
		VOID*   pDSLockedBuffer      = NULL; // Pointer to locked buffer memory 
		DWORD   dwDSLockedBufferSize = 0;    // Size of the locked StreamSound buffer 
		DWORD   dwWavDataRead        = 0;    // Amount of data read from the wav file  
		//如果缓冲区丢失需要恢复 
		DWORD dwStatus; 
		if(FAILED(hr = m_pDSBuffer->GetStatus(&dwStatus)))		 
		{ 
			XPLOG_ERROR(L"m_pDSBuffer->GetStatus()失败, hr=%x\n", hr); 
			return false; 
		} 
		if(dwStatus & DSBSTATUS_BUFFERLOST) 
		{ 
			
			do  
			{ 
				hr = m_pDSBuffer->Restore(); 
				if(hr == DSERR_BUFFERLOST) 
					Sleep(10); 
			} 
			while((hr = m_pDSBuffer->Restore()) == DSERR_BUFFERLOST); 


			m_soundsLock.Lock();
			if (m_sounds.size() == 0)
			{
				m_soundsLock.Unlock();
				return false;
			}
			ISoundData *pSData = *m_sounds.begin();
			m_soundsLock.Unlock();

			//向缓冲区写数据 
			if(FAILED(hr = m_pDSBuffer->Lock(0, m_dwDSBufferSize, &pDSLockedBuffer, 
				&dwDSLockedBufferSize,NULL,NULL,0L))) 
			{ 
				XPLOG_ERROR(L"RestoreBuffer(),Lock失败, hr=%x\n", hr); 
				return false; 
			}

			WAVEFORMATEX *pWfx = pSData->GetFormat();

			//首先读出缓冲区长度的数据 
			pSData->Read(static_cast<char*>(pDSLockedBuffer),dwDSLockedBufferSize,&dwWavDataRead); 
			if(dwWavDataRead == 0) 
			{ 
				// Wav is blank, so just fill with silence 
				FillMemory((BYTE*) pDSLockedBuffer,  
					dwDSLockedBufferSize,  
					(BYTE)(pWfx->wBitsPerSample == 8 ? 128 : 0)); 
			}		 
			else if(dwWavDataRead < dwDSLockedBufferSize)//如果文件小于缓冲区,用静音来填充剩余的空间 
			{ 
				FillMemory((BYTE*) pDSLockedBuffer + dwWavDataRead,  
					dwDSLockedBufferSize - dwWavDataRead,  
					(BYTE)(pWfx->wBitsPerSample == 8 ? 128 : 0));			 
			} 

			if(FAILED(hr = m_pDSBuffer->Unlock(pDSLockedBuffer, 
				dwDSLockedBufferSize,NULL,NULL))) 
			{ 
				XPLOG_ERROR(L"RestoreBuffer(),Unlock失败, hr=%x\n", hr); 
				return false; 
			} 
		} 
		return true; 

	} 

	//----------------------------------------------------------------------------- 
	// 播放缓冲区 
	//----------------------------------------------------------------------------- 

	bool StreamSound::PlayBuffer() 
	{
		//没有Play也不用锁
		if (m_sounds.size() == 0)
			return false;
		HRESULT hr; 
		VOID*   pDSLockedBuffer      = NULL; // Pointer to locked buffer memory 
		DWORD   dwDSLockedBufferSize = 0;    // Size of the locked StreamSound buffer 
		DWORD   dwWavDataRead        = 0;    // Amount of data read from the wav file  

		if(m_pDSBuffer == NULL) 
		{ 
			return false; 
		} 
		if(m_pParam->m_bPause == true)  
		{ 
			if(RestoreBuffer() == false) 
			{ 
				XPLOG_ERROR(L"PlayBuffer()：恢复缓冲区和数据失败\n"); 
				return false; 
			} 
			m_pParam->m_bPause = false; 
		} 
		else 
		{ 
			//这里不用加锁，如果还没有播放，肯定就还不会填充数据
			m_bFillNextNotificationWithSilence = false; 
			m_dwPlayProgress = 0;
			m_dwFillProgress = 0;
			m_dwLastPlayPos = 0; 
			m_dwNextWriteOffset = 0; 


			//如果缓冲区丢失需要恢复 
			int todolostdevice;
			//怎么这里还有丢失设备啊。。。。。
			DWORD dwStatus; 
			if(FAILED(hr = m_pDSBuffer->GetStatus(&dwStatus)))		 
			{ 
				XPLOG_ERROR(L"m_pDSBuffer->GetStatus()失败, hr=%x\n", hr); 
				return false; 
			} 
			if(dwStatus & DSBSTATUS_BUFFERLOST) 
			{ 
				// Since the app could have just been activated, then 
				// StreamSound may not be giving us control yet, so  
				// the restoring the buffer may fail.   
				// If it does, sleep until StreamSound gives us control. 
				do  
				{ 
					hr = m_pDSBuffer->Restore(); 
					if(hr == DSERR_BUFFERLOST) 
						Sleep(10); 
				} 
				while((hr = m_pDSBuffer->Restore()) == DSERR_BUFFERLOST); 

			} 

			//向缓冲区写数据 
			m_pDSBuffer->Stop(); 
			m_pDSBuffer->SetCurrentPosition(0L);	 
			if(FAILED(hr = m_pDSBuffer->Lock(0, m_dwDSBufferSize, &pDSLockedBuffer, 
				&dwDSLockedBufferSize,NULL,NULL,0L))) 
			{ 
				//XPALARM(L"PlayBuffer(),Lock失败\n");
				XPLOG_ERROR(L"PlayBuffer(),Lock失败, hr=%x\n", hr);
				return false; 
			} 
			//没有Play呢，不锁
			ISoundData *pSData = *m_sounds.begin();

			// Reset the wave file to the beginning  
			pSData->Reset(); 
			DWORD tmpSize = pSData->GetSize();
			if (tmpSize < dwDSLockedBufferSize)
			{
				tmpSize = (tmpSize / m_dwNotifySize) * m_dwNotifySize;//得是m_dwNotifySize的倍数
			} else {
				tmpSize = dwDSLockedBufferSize;
			}
			//首先读出缓冲区长度的数据 
			pSData->Read(static_cast<char*>(pDSLockedBuffer), tmpSize, &dwWavDataRead); 

			//如果文件小于缓冲区,用静音来填充剩余的空间（当然可能会被无缝的下一首声音覆盖）
			if(dwWavDataRead < dwDSLockedBufferSize) 
			{ 
				FillMemory((BYTE*) pDSLockedBuffer + dwWavDataRead,  
					dwDSLockedBufferSize - dwWavDataRead,  
					(BYTE)(pSData->GetFormat()->wBitsPerSample == 8 ? 128 : 0));
				m_dwNextWriteOffset = dwWavDataRead;
			} 

			//静音部分不要
			m_dwFillProgress = dwWavDataRead;

			if(FAILED(hr = m_pDSBuffer->Unlock(pDSLockedBuffer, 
				dwDSLockedBufferSize,NULL,NULL))) 
			{ 
				XPLOG_ERROR(L"PlayBuffer(),Unlock失败, hr=%x\n", hr); 
				return false; 
			}
			m_dwSoundSize = pSData->GetSize();
			if (m_dwFillProgress + m_dwDSBufferSize >= m_dwSoundSize && m_sounds.size() == 1)
			{
				m_pParam->m_pSoundWrap->NotifyGoingToEnd();
				m_bNotifyWillEnd = true;
			} else {
				m_bNotifyWillEnd = false;
			}
		} 

		//不加这两行再开发播放时不能设置音量，不知道原因 
		//Zhongli 注：未测试，先加上
		//m_pDSBuffer->SetVolume(-10000); 
		//m_pDSBuffer->SetVolume(0); 

		//SetMute(m_bMute); 
		SetVolume();	
		//这里不用加锁，如果还没有播放，肯定就还不会填充数据
		m_pDSBuffer->Play(0, 0, DSBPLAY_LOOPING); 
		return true; 
	} 

	DWORD StreamSound::GetPlayLength()
	{ 
		m_soundsLock.Lock();
		ISoundDataList::iterator it = m_sounds.begin(), ie = m_sounds.end();
		DWORD result = 0;
		for (;it!=ie;++it)
		{
			result += (*it)->GetPlayLength();
		}
		m_soundsLock.Unlock();
		return result;
	}

	//----------------------------------------------------------------------------- 
	// 播放，停止，暂停，设置音量函数 
	//----------------------------------------------------------------------------- 
	bool StreamSound::Play() 
	{ 
		return PlayBuffer(); 
	} 

	bool StreamSound::Resume()
	{
		return PlayBuffer(); 
	}

	bool StreamSound::Stop() 
	{ 
		if(m_pDSBuffer == NULL) 
		{ 
			return false; 
		}
		if (m_pParam->m_bPause || IsPlaying())
		{
			HRESULT hr;
			m_pParam->m_bPause = false; 
			m_bufferLock.Lock();
			if(FAILED(hr = m_pDSBuffer->Stop())) 
			{ 
				m_bufferLock.Unlock();
				XPLOG_ERROR(L"Stop()失败, hr=%x\n", hr); 
				return false; 
			} 

			if(FAILED(hr = m_pDSBuffer->SetCurrentPosition(0L))) 
			{ 
				m_bufferLock.Unlock();
				XPLOG_ERROR(L"SetCurrentPosition()失败, hr=%x\n", hr); 
				return false; 
			} 
			m_bufferLock.Unlock();
			m_bFillNextNotificationWithSilence =false; 
			m_dwPlayProgress = 0; 
			m_dwFillProgress = 0;
			m_dwSoundSize = 0;
			m_dwLastPlayPos = 0; 
			m_dwNextWriteOffset = 0;
			return true; 
		}
		return false;
	} 

	void StreamSound::Pause() 
	{ 
		if(m_pDSBuffer == NULL) 
		{ 
			return; 
		} 
		m_bufferLock.Lock();
		m_pDSBuffer->Stop();
		m_bufferLock.Unlock();
	} 

	void StreamSound::SetVolume() 
	{ 
		if(m_pDSBuffer == NULL) 
			return; 
		unsigned char nVolume = m_pParam->m_nVolume;
		switch (m_nFadeSign)
		{
		case XPFS_NORMAL:
			break;
		case XPFS_FADE_OUT:
			nVolume = Lerpf((unsigned char)0, m_pParam->m_nVolume, static_cast<float>(m_nFadingTime) / m_nFadeTime);
			break;
		case XPFS_FADE_IN:
			nVolume = Lerpf((unsigned char)0, m_pParam->m_nVolume, static_cast<float>(m_nFadeTime - m_nFadingTime) / m_nFadeTime);
			break;
		}
		LONG volume = GetDXVolume(nVolume, m_pParam->m_ftypeVol);

		switch (m_pParam->m_state)
		{
		case XPFS_NORMAL:
			break;
		case XPFS_FADE_OUT:
			volume -= static_cast<LONG>((DSBVOLUME_MAX - DSBVOLUME_MIN) * m_pParam->m_fFadefRate);
			if (volume < DSBVOLUME_MIN) volume = DSBVOLUME_MIN;
			break;
		case XPFS_FADE_IN:
			volume -= static_cast<LONG>((DSBVOLUME_MAX - DSBVOLUME_MIN) * (1 - m_pParam->m_fFadefRate));
			if (volume < DSBVOLUME_MIN) volume = DSBVOLUME_MIN;
			break;
		case XPFS_MUTE:
			volume = DSBVOLUME_MIN;
			break;
		}

		m_bufferLock.Lock();
		m_pDSBuffer->SetVolume(volume);
		m_bufferLock.Unlock();
	} 

	void StreamSound::SetFrequency()
	{		
		if(m_pDSBuffer != NULL) 
		{ 
			m_pDSBuffer->SetFrequency(m_pParam->m_nInitFreq * m_pParam->m_nFrequency / 128);
		} 
	}

	bool StreamSound::IsPlaying() const
	{ 
		bool bIsPlaying = false;	 
		if(m_pDSBuffer == NULL) 
			return false;  
		DWORD dwStatus = 0; 
		m_pDSBuffer->GetStatus(&dwStatus); 
		bIsPlaying |= ((dwStatus & DSBSTATUS_PLAYING) != 0); 
		return bIsPlaying; 
	}

	//淡入淡出，单位毫秒
	bool StreamSound::PlayAndFadeIn(unsigned short time, bool bLooping)
	{
		if (time > 0)
		{
			m_nFadeTime = time;
			m_nFadingTime = time;
			m_nFadeSign = XPFS_FADE_IN;
		}
		m_pParam->m_bLooping = bLooping;
		return PlayBuffer(); 
	}

	bool StreamSound::FadeOutToStop(unsigned short time)
	{
		XPASSERT(time > 0);
		if (m_nFadeSign == XPFS_FADE_IN)
		{
			m_nFadingTime = time * (m_nFadeTime - m_nFadingTime) / m_nFadeTime;
		} else {
			m_nFadingTime = time;
		}
		m_nFadeTime = time;
		m_nFadeSign = XPFS_FADE_OUT;
		return true;
	}

	StreamSound::XPStreamSoundUpdateResult StreamSound::Update()
	{
		if (!IsPlaying())
			return XPSSUR_OK;
		static int lastTime;
		switch (m_nFadeSign)
		{
		case XPFS_FADE_IN:
		case XPFS_FADE_OUT:
			if (m_nFadeTime == m_nFadingTime)
			{
				lastTime = GetMilliSeconds();
				m_nFadingTime -= 1;
			} else {
				unsigned int now = GetMilliSeconds();
				unsigned int delta = now - lastTime;
				lastTime = now;
				if (m_nFadingTime > delta)
				{
					m_nFadingTime -= delta;
				} else {
					m_nFadingTime = 0;
				}
			}
			if (m_nFadingTime == 0)//完成
			{
				if (m_nFadeSign == XPFS_FADE_OUT)
				{
					m_bufferLock.Lock();
					m_pDSBuffer->Stop();
					m_bufferLock.Unlock();
					m_dwPlayProgress = 0; 
					m_dwFillProgress = 0;
					m_dwSoundSize = 0;
					m_dwLastPlayPos = 0;
					m_nFadeSign = XPFS_MUTE;
					return XPSSUR_PLAY_END;
				} else {
					m_nFadeSign = XPFS_NORMAL;
					LONG volume = GetDXVolume(m_pParam->m_nVolume, m_pParam->m_ftypeVol);
					m_bufferLock.Lock();
					m_pDSBuffer->SetVolume(volume);
					m_bufferLock.Unlock();
				}
			} else {
				unsigned char tmpVol;
				if (m_nFadeSign == XPFS_FADE_OUT)
				{
					tmpVol = Lerpf((unsigned char)0, m_pParam->m_nVolume, static_cast<float>(m_nFadingTime) / m_nFadeTime);
				} else {
					tmpVol = Lerpf((unsigned char)0, m_pParam->m_nVolume, static_cast<float>(m_nFadeTime - m_nFadingTime) / m_nFadeTime);
				}
				LONG volume = GetDXVolume(tmpVol, m_pParam->m_ftypeVol);
				m_bufferLock.Lock();
				m_pDSBuffer->SetVolume(volume);
				m_bufferLock.Unlock();
			}
			break;
		}
		DWORD dwResult = WaitForSingleObject(m_hNotificationEvent, 0); 
		switch (dwResult)
		{
		case WAIT_TIMEOUT:
			return XPSSUR_OK;
			break;
		case WAIT_OBJECT_0:
			//等到了，继续干活吧
			break;
		case WAIT_FAILED:
			return XPSSUR_FAILD;
			break;
		}
		if (m_sounds.size() == 0)
			return XPSSUR_FAILD;
		//如果缓冲区丢失需要恢复 
		if(RestoreBuffer() == false) 
		{
			return XPSSUR_FAILD; 
		}
		HRESULT hr; 
		DWORD   dwCurrentPlayPos = 0; 
		DWORD   dwCurrentWritePos = 0; 
		DWORD   dwPlayDelta = 0; 
		DWORD   dwBytesWrittenToBuffer = 0; 
		VOID*   pDSLockedBuffer = NULL; 
		VOID*   pDSLockedBuffer2 = NULL; 
		DWORD   dwDSLockedBufferSize = 0; 
		DWORD   dwDSLockedBufferSize2 = 0; 
		if(FAILED(hr = m_pDSBuffer->GetCurrentPosition(&dwCurrentPlayPos, &dwCurrentWritePos))) 
		{ 
			XPLOG_ERROR(L"HandleNotificationThread(void* pParam),GetCurrentPosition()失败, hr=%x\n", hr); 
			return XPSSUR_FAILD; 
		} 

		if( dwCurrentPlayPos > dwCurrentWritePos )
			dwCurrentWritePos += m_dwDSBufferSize;

		DWORD lockSize = m_dwNotifySize;
		if( m_dwNextWriteOffset < dwCurrentWritePos)
		{
			if (m_dwNextWriteOffset + m_dwNotifySize > dwCurrentPlayPos )	//等下一个Notify吧
				return XPSSUR_OK;
			//可以一直填到playPos，但是得保证是m_dwNotifySize的整数倍
			lockSize = ((int)((dwCurrentPlayPos - m_dwNextWriteOffset) / m_dwNotifySize)) * m_dwNotifySize;
		} else {
			//可以一直填到Buffer的最后
			lockSize = m_dwDSBufferSize - m_dwNextWriteOffset;
		}

		// Lock the StreamSound buffer 
		m_bufferLock.Lock();
		if(FAILED(hr = m_pDSBuffer->Lock(
			m_dwNextWriteOffset, lockSize,  
			&pDSLockedBuffer, &dwDSLockedBufferSize,  
			&pDSLockedBuffer2, &dwDSLockedBufferSize2, 0L))) 
		{ 
			m_bufferLock.Unlock();
			//XPALARM(L"HandleNotificationThread(void* pParam),Lock失败\n"); 
			XPLOG_ERROR(L"HandleNotificationThread(void* pParam),Lock失败, hr=%x\n", hr);
			//下一个Notify再试试，会不会一直失败死循环呢？
			return XPSSUR_OK;
		} 
		m_bufferLock.Unlock();
		lockSize = lockSize < dwDSLockedBufferSize ? lockSize : dwDSLockedBufferSize;
		if(pDSLockedBuffer2 != NULL) 
		{ 
			XPLOG_ERROR(L"pDSLockedBuffer2不为空\n"); 
			return XPSSUR_FAILD;
		} 

		m_soundsLock.Lock();
		ISoundData* pSData = *m_sounds.begin();
		m_soundsLock.Unlock();

		//每一小块播放完后，就填充它 
		if(m_bFillNextNotificationWithSilence != true) 
		{ 
			pSData->Read(static_cast<char*>(pDSLockedBuffer),  
				dwDSLockedBufferSize,  
				&dwBytesWrittenToBuffer); 
			m_dwFillProgress += dwBytesWrittenToBuffer;
		} 
		else 
		{ 
			//已经全部播完了，填空白的声音吧
			FillMemory(pDSLockedBuffer, dwDSLockedBufferSize,  
				(BYTE)(pSData->GetFormat()->wBitsPerSample == 8 ? 128 : 0)); 
			//每次填了空白的声音，都要检查一下m_sounds有没有加上的声音
		} 

		//当填充了的声音比Locked的Buffer还小的时候，代表已经到了文件尾了
		if(dwBytesWrittenToBuffer < dwDSLockedBufferSize) 
		{
			m_soundsLock.Lock();
			if (m_sounds.size() > 1 || m_pParam->m_bLooping)
			{
				m_sounds.pop_front();
				if (m_pParam->m_bLooping)
				{
					m_sounds.push_back(pSData);
					pSData->Reset();
				} else {
					delete pSData;
					//后面没有用pSData了
				}
				ISoundData* pNextSData = *m_sounds.begin();
				m_soundsLock.Unlock();
				m_dwSoundSize -= m_dwPlayProgress;
				m_dwFillProgress -= m_dwPlayProgress;
				m_dwSoundSize += pNextSData->GetSize();
				m_bNotifyWillEnd = false;
				m_dwPlayProgress = 0;
				//如果已经填了的Buffer的长度不是m_dwNotifySize的倍数，那么再补补吧
				DWORD dwRest = lockSize - dwBytesWrittenToBuffer % m_dwNotifySize;
				if (dwRest < m_dwNotifySize)
				{
					DWORD tmpWriter;
					pNextSData->Read(static_cast<char*>(pDSLockedBuffer) + dwBytesWrittenToBuffer,  
						dwRest,  
						&tmpWriter); 
					if (tmpWriter != dwRest)//接下来的声音居然填不满一个m_dwNotifySize？
					{
						XPLOG_ERROR(L"这个声音太短了！%d, %d\n", dwRest, tmpWriter);
						pNextSData->Reset();
						//dwBytesWrittenToBuffer -= m_dwNotifySize;
					}
					//dwBytesWrittenToBuffer += dwRest;后面不再用到dwBytesWrittenToBuffer了
					m_dwFillProgress += dwRest;
				}
			} else {
				m_soundsLock.Unlock();
				//剩下的得填空白的声音
				FillMemory((BYTE*) pDSLockedBuffer + dwBytesWrittenToBuffer,  
					dwDSLockedBufferSize - dwBytesWrittenToBuffer,  
					(BYTE)(pSData->GetFormat()->wBitsPerSample == 8 ? 128 : 0)); 
					//如果还有Notify过来，还得填空白的声音
				m_bFillNextNotificationWithSilence = true; 
			}
		} 

		// unlock the StreamSound buffer 
		m_bufferLock.Lock();
		if(FAILED(hr = m_pDSBuffer->Unlock( 
			pDSLockedBuffer, dwDSLockedBufferSize,  
			NULL, 0L))) 
		{ 
			m_bufferLock.Unlock();
			XPLOG_ERROR(L"HandleNotificationThread(void* pParam),Unlock失败, hr=%x\n", hr); 
			return XPSSUR_FAILD; 
		} 
		m_bufferLock.Unlock();

		// Check to see if the position counter looped 
		if(dwCurrentPlayPos < m_dwLastPlayPos) 
			dwPlayDelta = (m_dwDSBufferSize - m_dwLastPlayPos) + dwCurrentPlayPos; 
		else 
			dwPlayDelta = dwCurrentPlayPos - m_dwLastPlayPos; 

		m_dwPlayProgress += dwPlayDelta; 
		m_dwLastPlayPos = dwCurrentPlayPos; 
		if(m_dwPlayProgress >= m_dwSoundSize) 
		{ 
			if (!m_pParam->m_bLooping)
			{
				m_bufferLock.Lock();
				m_pDSBuffer->Stop();
				m_bufferLock.Unlock();
				m_dwPlayProgress = 0; 
				m_dwLastPlayPos = 0;
				m_dwSoundSize = 0;
				return XPSSUR_PLAY_END;
			}
		} else {
			if (m_pParam->m_pNotify)
			{
				if (m_dwFillProgress + m_dwDSBufferSize >= m_dwSoundSize)
				{
					if (!m_bNotifyWillEnd)
					{
						QueueUserAPC(DirectSoundManager::NotifyGoingToEnd, m_pDirectSoundMan->GetMainThread(), (ULONG_PTR)m_pParam->m_pSoundWrap);
						m_bNotifyWillEnd = true;
					}
				} else {
					XPASSERT(!m_bNotifyWillEnd);
					m_bNotifyWillEnd = false;
				}
			}
			
			// Update where the buffer will lock (for next time) 
			m_dwNextWriteOffset += lockSize;  
			m_dwNextWriteOffset %= m_dwDSBufferSize; // Circular buffer 
		}
		return XPSSUR_OK;
	}

}