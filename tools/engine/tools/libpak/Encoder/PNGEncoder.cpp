#include "../../../contrib/lpng1234/png.h"
#include "../../../pfs/pfs2.h"
#include "PNGEncoder.h"
#include "../Picture.h"
#include "ImageEncoderHelper.h"

#ifdef _DEBUG
#pragma comment( lib, "../../../lib/libpngd.lib")
#else
#pragma comment( lib, "../../../lib/libpng.lib")
#endif

namespace MHZX
{

	CPNGEncoder::CPNGEncoder(void)
	{
		m_pContext1 = NULL;
		m_pContext2 = NULL;
		
		wcscpy_s( szExtensionBuffer,  L".png" );
	}

	CPNGEncoder::~CPNGEncoder(void)
	{
	}
	
	void CPNGEncoder::SetDefaultFileExtension(const wchar_t* lpszNewExt)
	{
		wcscpy_s( szExtensionBuffer, lpszNewExt );
	}

	const wchar_t* CPNGEncoder::GetDefaultFileExtension() const
	{
		return szExtensionBuffer;
	}

	static void _PNGWarnCallback(png_structp png_ptr, png_const_charp msg)
	{
		printf("write png warning: %s\n", msg);
		fflush(stdout);
	}

	static void _PNGErrorCallback(png_structp png_ptr, png_const_charp msg)
	{
		fprintf(stderr, "write png error: %s\n", msg);
		fflush(stderr);
		longjmp(png_ptr->jmpbuf, 1);
	}

	bool CPNGEncoder::Open()
	{
		png_structp pContext = png_create_write_struct(
			PNG_LIBPNG_VER_STRING, NULL, _PNGErrorCallback, _PNGWarnCallback);
		if( pContext == NULL )
		{
			return false;
		}

		m_pContext1 = pContext;
		m_pContext2 = png_create_info_struct( pContext );

		return true;
	}

	void _PNGWriteDataProxy(png_structp png_ptr, png_bytep data, png_size_t length)
	{
		PFS::CBaseFile * pFile = static_cast<PFS::CBaseFile*>(png_get_io_ptr(png_ptr));
		pFile->Write(data, length);
	}

	bool CPNGEncoder::Encode(const unsigned char* pBits, 
		unsigned int nWidth, unsigned int nHeight, unsigned int nBitCount, bool bHasAlphaChannel,
		PFS::CBaseFile& rFile,
		unsigned int& rNewFormat)
	{
		png_structp png_ptr = (png_structp)m_pContext1;
		png_infop info_ptr = (png_infop)m_pContext2;

		if (setjmp(png_ptr->jmpbuf)){
			/* If we get here, we had a problem writing the file */
			if (info_ptr->palette) free(info_ptr->palette);
			return false;
		}

		//	init i/o
		png_set_write_fn(png_ptr, static_cast<void*>(&rFile), _PNGWriteDataProxy, NULL);

		//	setup params

		// bit_depth: 8就是A8R8G8B8，4就是A4R4G4B4(16位)
		// 5应该是 A1R5G5B5 或者 R5G6B5

		unsigned int nPitch = nWidth * nBitCount / 8;
		unsigned int nBitDepth = bHasAlphaChannel ? nBitCount / 4 : nBitCount / 3;

		png_set_IHDR( png_ptr, info_ptr, nWidth, nHeight, nBitDepth, 
			bHasAlphaChannel?PNG_COLOR_TYPE_RGB_ALPHA:PNG_COLOR_TYPE_RGB, 
				PNG_INTERLACE_NONE,	PNG_COMPRESSION_TYPE_DEFAULT, PNG_FILTER_TYPE_DEFAULT);
		png_write_info(png_ptr, info_ptr);

		if ( !bHasAlphaChannel )
			png_set_filler(png_ptr, 0, PNG_FILLER_AFTER);	//	strip alpha

		// png_set_bgr(png_ptr);	//	switch to little-endian notation

		//	writing
		for ( unsigned int i = 0; i < nHeight; i++, pBits += nPitch )
		{
			png_write_row( png_ptr, (png_bytep)pBits );
		}

		png_write_end(png_ptr, info_ptr);

		// TODO: check bHasAlphaChannel
		rNewFormat = MAKELONG(PIX_FMT_A8R8G8B8, PSF_NATIVE_PNG); // reset format& storage

		return true;
	}

	bool CPNGEncoder::Encode( CPicture& rPicture, PFS::CBaseFile& rFile,
		unsigned int& rNewFormat )
	{
		png_structp png_ptr = (png_structp)m_pContext1;
		png_infop info_ptr = (png_infop)m_pContext2;

		if (setjmp(png_ptr->jmpbuf)){
			/* If we get here, we had a problem writing the file */
			if (info_ptr->palette) free(info_ptr->palette);
			return false;
		}

		//	init i/o
		png_set_write_fn(png_ptr, static_cast<void*>(&rFile), _PNGWriteDataProxy, NULL);

		//	setup params

		// bit_depth: 8就是A8R8G8B8，4就是A4R4G4B4(16位)
		// 5应该是 A1R5G5B5 或者 R5G6B5

		unsigned int nWidth = rPicture.m_PictureDesc.width;
		unsigned int nHeight = rPicture.m_PictureDesc.height;

		bool bHasAlphaChannel = true; // TODO:
		
		unsigned int nPitch = nWidth * 4; // rPicture is always 32 bit.
		unsigned int nBitDepth = 8;

		png_set_IHDR( png_ptr, info_ptr, nWidth, nHeight, nBitDepth, 
			bHasAlphaChannel?PNG_COLOR_TYPE_RGB_ALPHA:PNG_COLOR_TYPE_RGB, 
				PNG_INTERLACE_NONE,	PNG_COMPRESSION_TYPE_DEFAULT, PNG_FILTER_TYPE_DEFAULT);
		png_write_info(png_ptr, info_ptr);

		if ( !bHasAlphaChannel )
			png_set_filler(png_ptr, 0, PNG_FILLER_AFTER);	//	strip alpha

		png_set_bgr(png_ptr);	
		//png_set_expand(png_ptr);	//	switch to little-endian notation

		const unsigned char* pBits =  rPicture.GetBitmapData();

		//	writing
		for ( unsigned int i = 0; i < nHeight; i++, pBits += nPitch )
		{
			png_write_row( png_ptr, (png_bytep)pBits );
		}

		png_write_end(png_ptr, info_ptr);

		rNewFormat = MAKELONG(PIX_FMT_A8R8G8B8, PSF_NATIVE_PNG); // reset format& storage
	
		return true;
	}


	void CPNGEncoder::Close()
	{
		// if (info_ptr->palette) free(info_ptr->palette);
		png_destroy_write_struct( (png_structpp) &m_pContext1, 
			 (png_infopp) &m_pContext2 ); 
	};

} // namespace MHZX
