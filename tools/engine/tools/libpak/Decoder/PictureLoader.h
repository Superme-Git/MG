///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : PictureLoader.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	P0X,B0X,TGA,BMP,JPG等文件的加载器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-17 Created.
///=============================================================================
#pragma once

namespace HELPER
{
	class CPictureLoader
	{
	public:
		static HBITMAP LoadBitmap(LPCWSTR lpszFileName, BITMAPINFO** ppBitmapInfo, BOOL bPremultiply = TRUE);
		static HBITMAP LoadPicture(LPCWSTR lpszFileName, BITMAPINFO** ppBitmapInfo, BOOL bPremultiply = TRUE);
		static HBITMAP LoadTarga(LPCWSTR lpszFileName, BITMAPINFO** ppBitmapInfo, BOOL bPremultiply = TRUE);

		// 释放由 LoadBitmap()/LoadPicture()/LoadTarga() 分配的BITMAPINFO结构
		static void FreeBitmapInfo( BITMAPINFO* pBitmapInfo );

		static bool LoadBitmapData(__in const wchar_t* lpszFileName,
			__out_bcount(*lpulWidth**lpulHeight*4) void** lppvPixels,
			__out unsigned long* lpulWidth, __out unsigned long* lpulHeight,
			__out_opt unsigned int* lpuiBitCount);
		static bool LoadPictureData(__in const wchar_t* lpszFileName, 
			__out_bcount(*lpulWidth**lpulHeight*4) void** lppvPixels,
			__out unsigned long* lpulWidth, __out unsigned long* lpulHeight,
			__out_opt unsigned int* lpuiBitCount);
		static bool LoadTargaData(__in const wchar_t* lpszFileName,
			__out_bcount(*lpulWidth**lpulHeight*4) void** lppvPixels,
			__out unsigned long* lpulWidth, __out unsigned long* lpulHeight,
			__out_opt unsigned int* lpuiBitCount);

		// 释放由 LoadBitmapData()/LoadP0XData()/LoadTargaData() 分配的内存
		static void FreePictureData( LPVOID pPixels );
	};

}// namespace HELPER