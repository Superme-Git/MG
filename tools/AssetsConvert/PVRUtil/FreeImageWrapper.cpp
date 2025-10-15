#include "StdAfx.h"
#include "FreeImageWrapper.h"
#include <algorithm>

void FreeImageWrapper::fiwGetImageInfo( const std::wstring& file_name , int& fict, unsigned int& width , unsigned int& height )
{
	FREE_IMAGE_FORMAT fif = FreeImage_GetFIFFromFilenameU(file_name.c_str());

	//wangbin add
	/*size_t nPos = file_name.find(L"map_");
	if (nPos != std::wstring::npos )
	{
		fif = FIF_DDS;
	}*/

	int fiFlags = 0;

	if( fif == FIF_PNG )
	{
		fiFlags = PNG_DEFAULT;
	}

	else if( fif == FIF_JPEG )
	{
		fiFlags = JPEG_DEFAULT;
	}

	else if( fif == FIF_TARGA )
	{
		fiFlags = TARGA_DEFAULT;
	}

	FIBITMAP* fi_handle = 0;
	int bpp = 0;
	if( fif != FIF_UNKNOWN && FreeImage_FIFSupportsReading(fif) )
	{
		fi_handle = FreeImage_LoadU( fif, file_name.c_str(), fiFlags );

		if( !fi_handle )
		{
			fif = FIF_UNKNOWN;
			width = 0;
			height = 0;
			return ;
		}

		fict = FreeImage_GetColorType(fi_handle);
		bpp = FreeImage_GetBPP(fi_handle);

		//if( bpp != 32 && bpp != 24 )
		//{
		//	fif = FIF_UNKNOWN;
		//	width = 0;
		//	height = 0;
		//	return ;
		//}

		if( fict == FIC_RGB && bpp == 32 )
		{
			fict = FIC_RGBALPHA;
		}

		width = FreeImage_GetWidth(fi_handle);
		height = FreeImage_GetHeight(fi_handle);
		FreeImage_Unload(fi_handle);
	}
	else
	{
		std::wstring msg = L"FreImage Unknown Image type: ";
		msg += file_name;
		//::MessageBox( 0, msg.c_str(), L"Error", MB_OK );
	}
}

FREE_IMAGE_FORMAT FreeImageWrapper::fiwMapImageFormat( const std::wstring& ext )
{
	std::wstring _ext = ext;

	std::transform( _ext.begin(), _ext.end(), _ext.begin(), tolower );

	if( _ext == L"png" )
	{
		return FIF_PNG;
	}
	else if( _ext == L"tga" )
	{
		return FIF_TARGA;
	}
	else if( _ext == L"jpg" )
	{
		return FIF_JPEG;
	}

	return FIF_UNKNOWN;
}

bool FreeImageWrapper::convertImageFormat( const std::wstring& src_image, const std::wstring& dst_image )
{
	int src_flag = 0;
	FIBITMAP* image_handle = 0;
	FREE_IMAGE_FORMAT src_fif = FIF_UNKNOWN;
	src_fif = FreeImage_GetFIFFromFilenameU( src_image.c_str() );  

	/*size_t nPos = src_image.rfind(L"map_");

	if (nPos != std::wstring::npos)
	{
		src_fif = FIF_DDS;
	}*/

	switch ( src_fif )
	{
	case FIF_JPEG:
		src_flag = JPEG_DEFAULT;
		break;

	case FIF_PNG:
		src_flag = PNG_DEFAULT;
		break;

	case FIF_TARGA:
		src_flag = TARGA_DEFAULT;
		break;

	case FIF_DDS:
		src_flag = DDS_DEFAULT;
		break;

	case FIF_BMP:
		src_flag = BMP_DEFAULT;
		break;

	}

	if( src_fif != FIF_UNKNOWN && FreeImage_FIFSupportsReading( src_fif ) )
	{
		image_handle = FreeImage_LoadU( src_fif, src_image.c_str(), src_flag );
	}

	if( !image_handle )
	{
		return false;
	}

	FREE_IMAGE_FORMAT dst_fif = FIF_UNKNOWN;
	int dst_flag = -1;
	dst_fif = FreeImage_GetFIFFromFilenameU( dst_image.c_str() );

	switch ( dst_fif )
	{
	case FIF_JPEG:
		dst_flag = JPEG_DEFAULT;
		break;

	case FIF_PNG:
		dst_flag = PNG_DEFAULT;
		break;

	case FIF_TARGA:
		dst_flag = TARGA_DEFAULT;
		break;

	case FIF_DDS:
		dst_flag = DDS_DEFAULT;
		break;

	case FIF_BMP:
		dst_flag = BMP_DEFAULT;
		break;

	}

	FREE_IMAGE_TYPE src_fit = FreeImage_GetImageType(image_handle);
	bool can_convert = false, convert_success = false;
	if(src_fit == FIT_BITMAP)  
	{  
		int bpp = FreeImage_GetBPP( image_handle );

		can_convert = (FreeImage_FIFSupportsWriting(dst_fif)  
			&& FreeImage_FIFSupportsExportBPP(dst_fif,bpp));  
	}  
	else  
	{  
		can_convert = FreeImage_FIFSupportsExportType(dst_fif,src_fit);  
	}

	if( can_convert )
	{
		convert_success = FreeImage_SaveU(dst_fif, image_handle, dst_image.c_str());
	}

	if( image_handle )
	{
		FreeImage_Unload(image_handle);
	}

	if( !convert_success )
	{
	//::MessageBox( 0, std::wstring(dst_image + L" Converted to png Failed !").c_str(), L"Error", 0 );
	}

	return convert_success;
}

bool FreeImageWrapper::rescaleImage( const std::wstring& src_image, const std::wstring& dst_image, float scale )
{
	if( scale >= 1.0f )
	{
		return true;
	}

	int src_flag = 0;
	FIBITMAP* image_handle = 0;
	FREE_IMAGE_FORMAT src_fif = FIF_UNKNOWN;
	src_fif = FreeImage_GetFIFFromFilenameU( src_image.c_str() );

	switch ( src_fif )
	{
	case FIF_JPEG:
		src_flag = JPEG_DEFAULT;
		break;

	case FIF_PNG:
		src_flag = PNG_DEFAULT;
		break;

	case FIF_TARGA:
		src_flag = TARGA_DEFAULT;
		break;

	case FIF_DDS:
		src_flag = DDS_DEFAULT;
		break;

	case FIF_BMP:
		src_flag = BMP_DEFAULT;
		break;

	}

	if( src_fif != FIF_UNKNOWN && FreeImage_FIFSupportsReading( src_fif ) )
	{
		image_handle = FreeImage_LoadU( src_fif, src_image.c_str(), src_flag );
	}

	if( !image_handle ) 
	{
		return false;
	}

	int oriWidth = FreeImage_GetWidth(image_handle);
	int oriHeight = FreeImage_GetHeight(image_handle);

	int newWidth = int(scale * oriWidth);
	int newHeight = int(scale * oriHeight);

	FIBITMAP* newImageHandle = FreeImage_Rescale(image_handle, newWidth, newHeight, FILTER_BILINEAR);

	if( !newImageHandle )
	{
		FreeImage_Unload(image_handle);
		return false;
	}

	if( !FreeImage_SaveU( src_fif, newImageHandle,dst_image.c_str() ) )
	{
		FreeImage_Unload(image_handle);
		FreeImage_Unload(newImageHandle);
		return false;
	}

	FreeImage_Unload(image_handle);
	FreeImage_Unload(newImageHandle);

	return true;
/*
	if( can_convert )
	{
		convert_success = FreeImage_SaveU(dst_fif, image_handle, dst_image.c_str());
	}

	if( image_handle )
	{
		FreeImage_Unload(image_handle);
	}

	return convert_success;*/
}