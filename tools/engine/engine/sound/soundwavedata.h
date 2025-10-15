#ifndef __Nuclear_SOUND_WAVE_DATA_H
#define __Nuclear_SOUND_WAVE_DATA_H

#if _MSC_VER > 1000 
#pragma once 
#endif // _MSC_VER > 1000 
#include "isounddata.h"

namespace Nuclear
{
	class SoundWaveData : public ISoundData
	{ 
	private:
		friend class ISoundData;
	private: 
		WAVEFORMATEX			m_wfx;
		const unsigned char*	m_pbData; 
		const unsigned char*	m_pbDataCur; 
		ULONG					m_ulDataSize;
		XBuffer					m_buffer;

	protected:
		SoundWaveData(); 

	public: 
		virtual ~SoundWaveData(); 
		virtual bool Load(const XBuffer& data);
		virtual HRESULT Read(char* pBuffer, DWORD dwSizeToRead, DWORD* pdwSizeRead); 
		virtual HRESULT Reset();
		virtual DWORD   GetSize(); 
		virtual WAVEFORMATEX* GetFormat() { return &m_wfx; }
		virtual XPSOUND_TYPE GetType () const { return XPSOUNDTYPE_WAVE; }
		virtual DWORD GetPlayLength() const { return m_ulDataSize * 1000 / m_wfx.nAvgBytesPerSec; }
	}; 
}
#endif