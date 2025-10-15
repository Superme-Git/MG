#include "stdafx.h"
#include "..\common\log.h"
#include "soundwavedata.h " 

using namespace Nuclear;

SoundWaveData::SoundWaveData() : m_pbData(NULL), m_pbDataCur(NULL)
{ 
}
 
SoundWaveData::~SoundWaveData() 
{
	m_buffer.SetTick(Nuclear::GetMilliSeconds());
} 
 
inline DWORD ReadDWordLE(const unsigned char* &data, int &size)
{
	DWORD result = 0;
	result = *(reinterpret_cast<const DWORD*>(data));
	data += 4;
	size -= 4;
	return result;
}

inline WORD ReadWordLE(const unsigned char* &data, int &size)
{
	WORD result = 0;
	result = *(reinterpret_cast<const WORD*>(data));
	data += 2;
	size -= 2;
	return result;
}

bool SoundWaveData::Load(const XBuffer& buffer)
{
	const unsigned char *data = static_cast<const unsigned char *>(buffer.constbegin());
	int size = buffer.size();
	if (data[0] != 'R' || data[1] != 'I' || data[2] != 'F' || data[3] != 'F')
		return false;
	data += 4;
	size -= 4;
	int fileSize;
	fileSize = ReadDWordLE(data, size);
	if (fileSize != size)
		return false;
	//
	if (data[0] != 'W' || data[1] != 'A' || data[2] != 'XPV' || data[3] != 'E')
		return false;
	data += 4;
	size -= 4;
	if (data[0] != 'f' || data[1] != 'm' || data[2] != 't' || data[3] != 0x20)
		return false;
	data += 4;
	size -= 4;

	DWORD chunksize = ReadDWordLE(data, size);
	m_wfx.wFormatTag = ReadWordLE(data, size);
	if (m_wfx.wFormatTag != WAVE_FORMAT_PCM)	//01 00 WAVE_FORMAT_PCM
		return false;

	m_wfx.nChannels = ReadWordLE(data, size);
	m_wfx.nSamplesPerSec = ReadDWordLE(data, size);
	m_wfx.nAvgBytesPerSec = ReadDWordLE(data, size);
	m_wfx.nBlockAlign = ReadWordLE(data, size);
	m_wfx.wBitsPerSample = ReadWordLE(data, size);

	chunksize -= 16;
	data += chunksize;
	size -= chunksize;
	while ((size > 0) && (data[0] != 'd' || data[1] != 'a' || data[2] != 't' || data[3] != 'a'))
	{
		data += 4;
		size -= 4;
		chunksize = ReadDWordLE(data, size);
		data += chunksize;
		size -= chunksize;
	}

	if (size <= 0)
		return false;

	data += 4;
	m_wfx.cbSize = 0;

	m_ulDataSize = ReadDWordLE(data, size);
	if (((int)m_ulDataSize) > size)
		return false;
	m_pbData = data;
	m_pbDataCur = m_pbData;
	m_buffer = buffer;
	return true;
}

DWORD SoundWaveData::GetSize() 
{ 
	return m_ulDataSize; 
} 

HRESULT SoundWaveData::Reset() 
{
	m_pbDataCur = m_pbData;
	return S_OK;
} 

HRESULT SoundWaveData::Read(char* pBuffer, DWORD dwSizeToRead, DWORD* pdwSizeRead) 
{ 

	if (m_pbDataCur == NULL) 
		return CO_E_NOTINITIALIZED; 
	if (pBuffer == NULL || pdwSizeRead == NULL) 
		return E_INVALIDARG; 
	*pdwSizeRead = 0; 

	if ((m_pbDataCur + dwSizeToRead) > (m_pbData + m_ulDataSize)) 
	{ 
		dwSizeToRead = m_ulDataSize - static_cast<DWORD>(m_pbDataCur - m_pbData); 
	} 
	CopyMemory(pBuffer, m_pbDataCur, dwSizeToRead); 
	m_pbDataCur += dwSizeToRead;
	*pdwSizeRead = dwSizeToRead; 

	return S_OK; 
} 
