#ifndef __Nuclear_SOUND_OGG_DATA_H
#define __Nuclear_SOUND_OGG_DATA_H

#if _MSC_VER > 1000 
#pragma once 
#endif // _MSC_VER > 1000 
#include "vorbis\codec.h"
#include "vorbis\vorbisfile.h"
#include "isounddata.h"
#include "..\common\xbuffer.h"

namespace Nuclear
{
	typedef struct OggFileBufferStruct
	{
		const unsigned char* data;
		ULONG cursor;
		ULONG size;
	} OggFileBuffer, *POggFileBuffer;

	class SoundOggData : public ISoundData
	{
	private:
		friend class ISoundData;

	private: 
		WAVEFORMATEX    m_wfx;        // Pointer to WAVEFORMATEX structure 
		POggFileBuffer  m_pBuffer; 
		ULONG			m_ulPCMDataSize;
		OggVorbis_File	*m_pvf;
		XBuffer			m_buffer;
	private:
		bool OpenFile();

	protected:
		SoundOggData(void);

	public: 
		virtual ~SoundOggData(void);
		virtual bool Load(const XBuffer &data);
		virtual HRESULT Read(char* pBuffer, DWORD dwSizeToRead, DWORD* pdwSizeRead); 
		virtual HRESULT Reset();
		virtual DWORD   GetSize(); 
		virtual WAVEFORMATEX* GetFormat() { return &m_wfx; }
		virtual XPSOUND_TYPE GetType () const { return XPSOUNDTYPE_OGG; }
		virtual DWORD GetPlayLength() const { return m_ulPCMDataSize * 1000 / m_wfx.nAvgBytesPerSec; }
		DWORD GetWavaFileSize() { return GetSize() + 44; }
		HRESULT ToWaveData(char *pData, DWORD size);

	};

}

#endif