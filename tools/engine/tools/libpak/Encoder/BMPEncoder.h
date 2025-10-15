#pragma once
#include "../IImageEncoder.h"

namespace MHZX
{
	// Abstract CBMPEncoder class. Can't be intialized. 
	class CBMPEncoder : public IImageEncoder
	{
	protected:
		CBMPEncoder(void);
		~CBMPEncoder(void);

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
			unsigned int& rNewFormat);
		
		virtual bool Encode(
			CPicture& rPicture,
			PFS::CBaseFile& rFile,
			unsigned int& rNewFormat);

		virtual void Close();

	protected:
		wchar_t szExtensionBuffer[256];
	};

} // namespace MHZX