#pragma once
#include "..\IImageEncoder.h"

namespace MHZX
{
	// Abstract CTGAEncoder class. Can't be intialized. 
	class CTGAEncoder : public IImageEncoder
	{
	protected:
		CTGAEncoder(void);
		~CTGAEncoder(void);
		
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
		
	protected:
		wchar_t szExtensionBuffer[256];
	};

}