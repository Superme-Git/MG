#include "stdafx.h"
#include "..\common\log.h"
#include "SoundOggData.h"


namespace Nuclear
{

	//4个操作函数
	/*typedef struct {
		size_t (*read_func)  (void *ptr, size_t size, size_t nmemb, void *datasource);
		int    (*seek_func)  (void *datasource, ogg_int64_t offset, int whence);
		int    (*close_func) (void *datasource);
		long   (*tell_func)  (void *datasource);
	} ov_callbacks;*/

	size_t readOggFileBuffer (void *ptr, size_t size, size_t nmemb, void *datasource)
	{
		POggFileBuffer pBuffer = static_cast<POggFileBuffer>(datasource);
		size_t readsize = size * nmemb;
		size_t canReadSize = pBuffer->size - pBuffer->cursor;
		if (canReadSize == 0)
		{
			return 0;
		}
		if (readsize > canReadSize)
		{
			readsize = canReadSize;
		}
		memcpy_s(ptr, readsize, pBuffer->data + pBuffer->cursor, readsize);
		pBuffer->cursor += readsize;
		return readsize;
	}

	int seekOggFileBuffer (void *datasource, ogg_int64_t offset, int whence)
	{
		if (datasource == NULL)
			return -1;
		POggFileBuffer pBuffer = static_cast<POggFileBuffer>(datasource);
		ogg_int64_t cursor;
		switch (whence)
		{
		case SEEK_CUR:
			cursor = pBuffer->cursor + offset;
			break;
		case SEEK_SET:
			cursor = offset;
			break;
		case SEEK_END:
			cursor = pBuffer->size + offset;
			break;
		}
		if (cursor > pBuffer->size)
			return -1;
		pBuffer->cursor = static_cast<ULONG>(cursor);
		return 0;
	}

	long tellOggFileBuffer (void *datasource)
	{
		if (datasource == NULL)
			return -1;
		POggFileBuffer pBuffer = static_cast<POggFileBuffer>(datasource);
		return pBuffer->cursor;
	}

	SoundOggData::SoundOggData(void) : m_pBuffer(NULL), m_pvf(NULL)
	{
	}

	SoundOggData::~SoundOggData(void)
	{
		if (m_pvf)
		{
			ov_clear(m_pvf);
			delete(m_pvf);
		}
		if (m_pBuffer)
		{
			delete(m_pBuffer);
		}
		m_buffer.SetTick(Nuclear::GetMilliSeconds());
	}

	bool SoundOggData::OpenFile()
	{
		if (m_pvf)
		{
			ov_clear(m_pvf);
		} else {
			m_pvf = new OggVorbis_File();
		}
		ZeroMemory(m_pvf, sizeof(OggVorbis_File));
		ov_callbacks oggCallBacks;
		oggCallBacks.read_func = readOggFileBuffer;
		oggCallBacks.seek_func = seekOggFileBuffer;
		oggCallBacks.close_func = NULL;
		oggCallBacks.tell_func = tellOggFileBuffer;

		if(ov_open_callbacks(m_pBuffer, m_pvf, NULL, 0, oggCallBacks) < 0) {
			XPALARM(L"读ogg文件错误");
			return false;
		}

		//目前只支持一个bitstream，所以第二个参数为0，ov_pcm_total(m_pvf, 0)也是，还有下面read的最后一个参数也是
		vorbis_info *vi=ov_info(m_pvf, 0);

		
		m_wfx.wFormatTag = WAVE_FORMAT_PCM;

		m_wfx.nChannels = vi->channels;
		m_wfx.nSamplesPerSec = vi->rate;
		m_wfx.wBitsPerSample = 16;	//因为底下ov_read第5个参数是2，所以就是16位的
		m_wfx.nBlockAlign = (WORD) (m_wfx.wBitsPerSample / 8 * m_wfx.nChannels);
		m_wfx.nAvgBytesPerSec = (DWORD) (m_wfx.nSamplesPerSec * m_wfx.nBlockAlign);
		m_wfx.cbSize = 0;

		m_ulPCMDataSize = static_cast<ULONG>(ov_pcm_total(m_pvf, 0) * m_wfx.nBlockAlign);
		return true;
	}

	bool SoundOggData::Load(const XBuffer &data)
	{
		if (!m_pBuffer)
		{
			m_pBuffer = new OggFileBuffer();
		}
		m_buffer = data;
		m_pBuffer->size = m_buffer.size();
		m_pBuffer->data = static_cast<const unsigned char*>(m_buffer.constbegin());
		m_pBuffer->cursor = 0;
		return OpenFile();
	}

	HRESULT SoundOggData::Read(char* pBuffer, DWORD dwSizeToRead, DWORD* pdwSizeRead)
	{
		int current_section = 0;
		long ret;
		*pdwSizeRead = dwSizeToRead;
		while (dwSizeToRead > 0) {
			ret = ov_read(m_pvf, pBuffer, dwSizeToRead, 0, 2, 1, &current_section);
			if (ret == 0) {
				//文件结束了，啥也没有读出来
				break;
			} else if (ret < 0) 
			{
				//有错误了
				break;
			} else 
			{
				dwSizeToRead -= ret;
				pBuffer += ret;
			}
		}

		*pdwSizeRead -= dwSizeToRead;
		return ret>=0?S_OK:ret;
	}
	HRESULT SoundOggData::Reset()
	{
		return ov_time_seek(m_pvf, 0);
	}
	DWORD SoundOggData::GetSize()
	{
		return m_ulPCMDataSize;
	}

	HRESULT SoundOggData::ToWaveData(char *pData, DWORD size)
	{
		XPASSERT(size >= GetWavaFileSize());
		*(pData++) = 'R';
		*(pData++) = 'I';
		*(pData++) = 'F';
		*(pData++) = 'F';
		*((DWORD*)pData) = size - 8;
		pData += 4;
		*(pData++) = 'W';
		*(pData++) = 'A';
		*(pData++) = 'XPV';
		*(pData++) = 'E';
		*(pData++) = 'f';
		*(pData++) = 'm';
		*(pData++) = 't';
		*(pData++) = 0x20;
		*((DWORD*)pData) = 16;//10 00 00 00
		pData += 4;
		*((WORD*)pData) = WAVE_FORMAT_PCM;//01 00
		pData += 2;
		*((WORD*)pData) = m_wfx.nChannels;
		pData += 2;
		*((DWORD*)pData) = m_wfx.nSamplesPerSec;
		pData += 4;
		*((DWORD*)pData) = m_wfx.nAvgBytesPerSec;
		pData += 4;
		*((WORD*)pData) = m_wfx.nBlockAlign;//01 00
		pData += 2;
		*((WORD*)pData) = m_wfx.wBitsPerSample;
		pData += 2;
		*(pData++) = 'd';
		*(pData++) = 'a';
		*(pData++) = 't';
		*(pData++) = 'a';
		*((DWORD*)pData) = m_ulPCMDataSize;
		pData += 4;
		DWORD dwWavDataRead;
		return Read(pData, m_ulPCMDataSize, &dwWavDataRead);
	}
}