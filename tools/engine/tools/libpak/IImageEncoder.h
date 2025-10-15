#pragma once
#include <string>

namespace PFS
{
	class CBaseFile;
}

namespace MHZX
{
	//
	// E_NEW_FILE_NAME_POLICY:
	//
	// The following comments take the 1.pic to png format as the example. 
	// In the source pak, the id/index of the 1.pic is 0.
	//
	enum E_NEW_FILE_NAME_POLICY
	{
		COPY_SRC_FILE_NAME		= 0x00000000,	// copy the src file name as the new file name.		like 1.pic -> 1.pic 
		NEW_FILE_NAME_BY_ID		= 0x00000001,	// use the id to generate the new file name.		like 1.pic --> 0.pic.
		FILE_NAME_POLICY_MASK	= 0x000000FF,	// mask for generating file name part.

		REPLACE_SRC_EXTENSION	= 0x00000000,	// replace the old extension by the new extension. like 1.pic -->1.png.
		APPEND_NEW_EXTENSION	= 0x00000100,	// append the new extension to the src file extension. like 1.pic--> 1.pic.png.
		COPY_SRC_EXTENSION		= 0x00000200,	// copy the src file extension as the new file extension. like 1.pic->1.pic.
		TRIM_SRC_EXTENSION		= 0x00000300,	// copy the src file extension as the new file extension. like 1.pic->1.pic.
		EXTENSION_POLICY_MASK	= 0x00000F00,	// mask for generating file extension part.
	};
	typedef unsigned int NEW_FILE_NAME_POLICY;

	class CPicture;
	class IImageEncoder
	{
	public:
		
		// Called before Open(), and only need be called once at a time.
		virtual bool Init(NEW_FILE_NAME_POLICY policy) = 0;
		
		//
		// Replace the default extension with lpszNewExt.
		//
		virtual void SetDefaultFileExtension(const wchar_t* lpszNewExt)= 0;

		//
		// Can be used before Open();
		//
		virtual const wchar_t* GetDefaultFileExtension() const = 0;

		//
		// Can be used before Open();
		//
		// GenerateNewFileName by the NEW_FILE_NAME_POLICY set on Init().
		//
		virtual bool GenerateNewFileName( 
			unsigned int fileid,
			const std::wstring& srcname,
			std::wstring& newname ) = 0;

		// Init inner state.
		// After closed, can be open again.
		//
		virtual bool Open() = 0;

		virtual bool Encode(
			const unsigned char* pBits, 
			unsigned int nWidth,
			unsigned int nHeight,
			unsigned int nBitCount,
			bool bHasAlphaChannel,
			PFS::CBaseFile& rFile,
			unsigned int& rNewFormat
			) = 0;

		virtual bool Encode(
			CPicture& rPicture,
			PFS::CBaseFile& rFile,
			unsigned int& rNewFormat
			) = 0;

		// Close inner state
		virtual void Close() = 0;

		// Release/Finalize the encoder object.
		// After released, the encoder cannot be used anymore.
		virtual void Release() = 0;
	};

	static const wchar_t* const IMAGE_ENCODER_PNG = L"png";
	static const wchar_t* const IMAGE_ENCODER_TGA = L"tga";
	static const wchar_t* const IMAGE_ENCODER_BMP = L"bmp";
	static const wchar_t* const IMAGE_ENCODER_DDS = L"dds"; // not supported yet.
	static const wchar_t* const IMAGE_ENCODER_JPG = L"jpg"; // not supported yet.

	class CImageEncoderFactory 
	{
	public:
		static IImageEncoder* CreateImageEncoder( const wchar_t* image_type );
	};
}