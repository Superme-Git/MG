#include "stdafx.h"
#include "..\common\log.h"
#include "soundbufferpool.h"

namespace Nuclear
{
	SoundBufferPool::SoundBufferPool(void)
		: m_nSize(0), m_dwCreationFlags(0), m_pSoundDatas(NULL), m_pDirectSound(NULL)
	{
		m_nInitFreq = 0;
		m_nTick = 0;
		m_pBuffers.reserve(8);
	}

	SoundBufferPool::~SoundBufferPool(void)
	{
		CleanAll();
	}

	void SoundBufferPool::CleanAll()
	{
		PBufferVector::iterator iter = m_pBuffers.begin();
		for(;iter!=m_pBuffers.end();iter++)
		{
			XPSAFE_RELEASE(*iter);
		}
		XPSAFE_DELETE(m_pSoundDatas);
		m_freeList.clear();
		m_pBuffers.clear();
	}

	bool SoundBufferPool::Initialize(const XBuffer& buffer, LPDIRECTSOUND pDirectSound, DWORD dwCreationFlags, XPSOUND_TYPE type) 
	{
		if( buffer.size() == 0 ) 
			return false;
		CleanAll();
		m_pDirectSound = pDirectSound;
		m_pSoundDatas = ISoundData::GetSoundData(type);
		if (!m_pSoundDatas)
			return false;

		if (!m_pSoundDatas->Load(buffer))
			return false;

		m_nSize = m_pSoundDatas->GetSize();
		m_dwCreationFlags = dwCreationFlags|DSBCAPS_CTRLVOLUME|DSBCAPS_CTRLFREQUENCY|DSBCAPS_GLOBALFOCUS;

		DSBUFFERDESC dsbd;
		ZeroMemory(&dsbd, sizeof(DSBUFFERDESC));
		dsbd.dwSize          = sizeof(DSBUFFERDESC);
		dsbd.dwFlags         = m_dwCreationFlags;
		dsbd.dwBufferBytes   = m_nSize;
		dsbd.guid3DAlgorithm = DS3DALG_DEFAULT;
		dsbd.lpwfxFormat     = m_pSoundDatas->GetFormat();

		if(dsbd.lpwfxFormat->wFormatTag != WAVE_FORMAT_PCM)
		{
			return false;
		}

		LPDIRECTSOUNDBUFFER pBuffer;
		if (FAILED(pDirectSound->CreateSoundBuffer(&dsbd, &pBuffer, NULL)))
		{
			return false;
		}

		m_pBuffers.push_back(pBuffer);
		m_freeList.push_back(pBuffer);
		if (FAILED(FillBufferWithSound(pBuffer)))
			return false;
		else {
			pBuffer->GetFrequency(&m_nInitFreq);
			return true;
		}
	}

	HRESULT SoundBufferPool::FillBufferWithSound(LPDIRECTSOUNDBUFFER pBuffer)
	{
		//

		VOID*   pDSLockedBuffer      = NULL; // Pointer to locked buffer memory
		DWORD   dwDSLockedBufferSize = 0;    // Size of the locked DirectSound buffer
		DWORD   dwWavDataRead        = 0;    // Amount of data read from the wav file

		HRESULT hr;
		// Make sure we have focus, and we didn't just switch in from
		// an app which had a DirectSound device
		if( FAILED( hr = RestoreBuffer( pBuffer ) ) )
			return hr;

		// Lock the buffer down
		if( FAILED( hr = pBuffer->Lock( 0, m_nSize,&pDSLockedBuffer, &dwDSLockedBufferSize,NULL, NULL, 0L ) ) )
			return hr;

		m_pSoundDatas->Reset();
		m_pSoundDatas->Read(static_cast<char*>(pDSLockedBuffer), dwDSLockedBufferSize, &dwWavDataRead);

		return pBuffer->Unlock( pDSLockedBuffer, dwDSLockedBufferSize, NULL, 0 );
		//
	}

	bool SoundBufferPool::RestoreBuffer( LPDIRECTSOUNDBUFFER pBuffer)
	{
		//不用检查pBuffer是否真的是这个Pool里面的了吧
		if( pBuffer == NULL )
			return false;
		
		HRESULT hr;
		DWORD dwStatus;
		if( FAILED( hr = pBuffer->GetStatus( &dwStatus ) ) )
			return false;

		if( dwStatus & DSBSTATUS_BUFFERLOST )
		{
			// Since the app could have just been activated, then
			// DirectSound may not be giving us control yet, so
			// the restoring the buffer may fail.
			// If it does, sleep until DirectSound gives us control.
			do
			{
				hr = pBuffer->Restore();
				if( hr == DSERR_BUFFERLOST )
					Sleep( 10 );
			}
			while( ( hr = pBuffer->Restore() ) == DSERR_BUFFERLOST );
			FillBufferWithSound(pBuffer);
		}
		return true;
	}

	void SoundBufferPool::FreeBuffer(LPDIRECTSOUNDBUFFER pBuffer)
	{
		XPASSERT(pBuffer != NULL);
		//不用检查pBuffer是否真的是这个Pool里面的了吧
		m_freeList.push_back(pBuffer);
	}

	LPDIRECTSOUNDBUFFER SoundBufferPool::GetBuffer()
	{
		LPDIRECTSOUNDBUFFER result;
		if (m_freeList.size() > 0)
		{
			result = m_freeList.front();
			m_freeList.pop_front();
			result->SetFrequency(m_nInitFreq);
			result->SetCurrentPosition(0L);
			return result;
		}
		//没有空的Buffer了
		int size = m_pBuffers.size();
		if (size >= MAX_SOUND_BUFFER)	//同时在播的声音太多了，不能播了
		{
			return NULL;
		}

		int i = 0;
		if( (m_dwCreationFlags & DSBCAPS_CTRLFX) == 0 )
		{

			for(i=0;i<size;i++)
			{
				if(SUCCEEDED(m_pDirectSound->DuplicateSoundBuffer(m_pBuffers[0], &result)))
				{
					m_pBuffers.push_back(result);
					m_freeList.push_back(result);
				}
			}
		}
		else
		{
			DSBUFFERDESC dsbd;
			ZeroMemory(&dsbd, sizeof(DSBUFFERDESC));
			dsbd.dwSize          = sizeof(DSBUFFERDESC);
			dsbd.dwFlags         = m_dwCreationFlags;
			dsbd.dwBufferBytes   = m_nSize;
			dsbd.guid3DAlgorithm = DS3DALG_DEFAULT;
			dsbd.lpwfxFormat     = m_pSoundDatas->GetFormat();
			for(i=0;i<size;i++)
			{
				if(SUCCEEDED(m_pDirectSound->CreateSoundBuffer(&dsbd, &result, NULL)))
				{

					m_pBuffers.push_back(result);
					m_freeList.push_back(result);
				}
			}
		}
		if (m_freeList.size() > 0)
		{
			result = m_freeList.front();
			m_freeList.pop_front();
			result->SetFrequency(m_nInitFreq);
			result->SetCurrentPosition(0L);
			return result;
		}
		return NULL;
	}

}
