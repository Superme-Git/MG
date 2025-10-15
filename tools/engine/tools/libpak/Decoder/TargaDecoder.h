///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : TargaDecoder.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Targa(tga)图像数据/文件解码器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-22 Created.
///=============================================================================
#pragma once

namespace PFS
{
	class CBaseFile;
}

namespace HELPER
{
	// 加载TGA文件,并生成HBITMAP对象,
	// 返回的HBITMAP对象需要使用Win32API CloseObject()释放.
	// 返回的BITMAPINFO* 需要使用 FreeBitmapInfo() 释放(参见PictureLoader.h)
	// bPremultiply 指明生成的Bitmap是否已经经过了AlphaBlend的预处理(参见其它相关说明)
	extern BITMAPINFO* texReadTGA(LPCWSTR lpszFileName, HBITMAP* pBitmap, BOOL bPremultiply = TRUE);

	// 加载TGA数据,并读取TGA的宽度信息和高度信息
	// 该TGA数据可用于生成bgra32位格式的bitmap,但高度要取负值
	// 返回的*lppvPixels数据需要用::HeapFree( ::GetProcessHeap(), 0, lpvPixels )释放
	// 还可以使用CPictureLoader::FreePictureData()释放(参见PictureLoader.h)
	extern BOOL LoadTGAData(LPCWSTR lpszFileName, LPVOID* lppvPixels, 
		ULONG* pulWidth, ULONG* pulHeight, UINT* puiBitCount);


	extern BOOL LoadTGAData(PFS::CBaseFile& rFile, LPVOID* lppvPixels, 
		ULONG* pulWidth, ULONG* pulHeight, UINT* puiBitCount);

	// 释放由 LoadTGAData() 分配的内存
	void FreePictureData( LPVOID pPixels );

}