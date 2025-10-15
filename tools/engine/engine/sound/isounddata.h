#ifndef __Nuclear_I_SOUND_DATA_H
#define __Nuclear_I_SOUND_DATA_H

#if _MSC_VER > 1000 
#pragma once 
#endif // _MSC_VER > 1000 

#include "..\common\xptypes.h"

namespace Nuclear
{
	enum XPSOUND_TYPE;

	class ISoundData
	{ 
	public: 
		ISoundData () {}
		virtual ~ISoundData() {}
		virtual bool Load(const XBuffer &data) = 0;
		virtual HRESULT Read(char* pBuffer, DWORD dwSizeToRead, DWORD* pdwSizeRead) = 0;
		virtual HRESULT Reset() = 0;
		virtual DWORD GetSize() = 0;
		virtual WAVEFORMATEX* GetFormat() = 0;
		virtual XPSOUND_TYPE GetType () const = 0;
		virtual DWORD GetPlayLength() const = 0;//µ•Œª£∫∫¡√Î
		static ISoundData* GetSoundData(int type);
	}; 

	typedef std::list<ISoundData*> ISoundDataList;
}
#endif