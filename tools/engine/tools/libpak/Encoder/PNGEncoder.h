#pragma once

#include "../IImageEncoder.h"

namespace PFS
{
	class CBaseFile;
}

namespace MHZX
{
	// Abstract CPNGEncoder class. Can't be intialized. 
	class CPNGEncoder : public IImageEncoder
	{
	protected:
		CPNGEncoder(void);
		~CPNGEncoder(void);

	public:
		virtual void SetDefaultFileExtension(const wchar_t* lpszNewExt);

		virtual const wchar_t* GetDefaultFileExtension() const;

		virtual bool Open();

		virtual bool Encode(
			const unsigned char* pBits, 
			unsigned int nWidth,
			unsigned int nHeight,
			unsigned int nBitCount,
			bool bHasAlphaChannel,
			PFS::CBaseFile& rFile,
			unsigned int& rNewFormat );
		
		virtual bool Encode(
			CPicture& rPicture,
			PFS::CBaseFile& rFile,
			unsigned int& rNewFormat );

		virtual void Close();

	private:
		void* m_pContext1;
		void* m_pContext2;
		
	protected:
		wchar_t szExtensionBuffer[256];
	};

}