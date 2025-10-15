#include "stdafx.h"
#include "pngwriter.h"
#include "..\common\log.h"

#ifdef _DEBUG
#pragma comment(lib, "libpngd.lib")
#else
#pragma comment(lib, "libpng.lib")
#endif

namespace Nuclear
{
	class PNGError {};

	void WarningCallback(png_structp png_ptr,
		png_const_charp msg)
	{
		XPLOG_INFO(L"LIBPNG Warning: %s\n", msg);
		//	hge->System_Log("LIBPNG Warning: %s", msg);
	}

	void ErrorCallback(png_structp png_ptr,
		png_const_charp msg)
	{
		XPLOG_INFO(L"LIBPNG Error: %s\n", msg);
		//hge->System_Log("LIBPNG Error: %s", msg);
		throw PNGError();
	}

	void XBufferWriteData(png_structp png_ptr, png_bytep data, png_size_t length)
	{
		XBuffer* pBuffer = static_cast<XBuffer*>(png_get_io_ptr(png_ptr));
		pBuffer->insert(pBuffer->normalend(), data, length);
	}

	bool Write32BitPNGWithPitch(XBuffer &buffer, unsigned char* pBits, bool bNeedAlpha, int nWidth, int nHeight, int nPitch)
	{
		png_structp png_ptr = 0;
		png_infop info_ptr = 0;
		buffer.reserve(nWidth * nHeight * 4);
		try 
		{
			//	create png structs
			png_ptr = png_create_write_struct(PNG_LIBPNG_VER_STRING, 0,	ErrorCallback, WarningCallback);
			if (png_ptr == 0) return false;

			info_ptr = png_create_info_struct(png_ptr);
			if (info_ptr == 0) 
			{
				png_destroy_write_struct(&png_ptr, NULL);
				return false;
			}

			if (setjmp(png_ptr->jmpbuf)){
				/* If we get here, we had a problem reading the file */
				if (info_ptr->palette) free(info_ptr->palette);
				png_destroy_write_struct(&png_ptr,  (png_infopp)&info_ptr);
				return false;
			}

			//	init i/o
			png_set_write_fn(png_ptr, static_cast<void*>(&buffer), XBufferWriteData, NULL);

			//	setup params
			png_set_IHDR( png_ptr, info_ptr, nWidth, nHeight, 8, //8就是A8R8G8B8，4就是A4R4G4B4(16位)，目前有问题，不知道这个16位的是什么意思
				bNeedAlpha?PNG_COLOR_TYPE_RGB_ALPHA:PNG_COLOR_TYPE_RGB, 
					PNG_INTERLACE_NONE,	PNG_COMPRESSION_TYPE_DEFAULT, PNG_FILTER_TYPE_DEFAULT);
			png_write_info(png_ptr, info_ptr);

			if ( !bNeedAlpha )
				png_set_filler(png_ptr, 0, PNG_FILLER_AFTER);	//	strip alpha

			png_set_bgr(png_ptr);	//	switch to little-endian notation

			//	writing
			for ( int i = 0; i < nHeight; i++, pBits += nPitch )
			{
				png_write_row( png_ptr, pBits );
			}
			png_write_end(png_ptr, info_ptr);
		} 
		catch(PNGError)
		{
			png_destroy_write_struct(&png_ptr, (info_ptr == 0) ? NULL : &info_ptr);
			return false;
		}

		//	cleanup
		png_destroy_write_struct( &png_ptr, (info_ptr == 0) ? NULL : &info_ptr);
		
		return true;
	}

}