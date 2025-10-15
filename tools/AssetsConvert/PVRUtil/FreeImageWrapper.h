
#ifndef __FreeImageWrapper_H
#define __FreeImageWrapper_H

#include "FreeImage/Include/FreeImage.h"

class FreeImageWrapper
{
public:
	static void fiwGetImageInfo( const std::wstring& file_name , int& fict , unsigned int& width , unsigned int& height );
	static FREE_IMAGE_FORMAT fiwMapImageFormat( const std::wstring& );
	static bool convertImageFormat( const std::wstring& src_image, const std::wstring& dst_image );
	static bool rescaleImage( const std::wstring& src_image, const std::wstring& dst_image, float scale );
};

#endif