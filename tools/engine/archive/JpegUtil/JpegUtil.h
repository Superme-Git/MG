//////////////////////////////////////////////////////////////////////////
//
// JpegUtil.h
//
// author: yanghaibo	
//
#ifdef _MSC_VER
#pragma once
#endif

#ifndef __JPEG_UTIL_H__
#define __JPEG_UTIL_H__

//
// return 0 success
// < 0 error
//
#ifndef JPEG_UTIL_API
#ifdef _MSC_VER
#define JPEG_UTIL_API __declspec( dllimport ) 
#else 
#define JPEG_UTIL_API
#endif
#endif // !defined JPEG_UTIL_API 

typedef unsigned char BYTE;
typedef const wchar_t* LPCWSTR;

extern "C"
{
	//
	// summary: 从BMP内存数据生成Jpeg文件
	// 
	// parameter:
	//
	//			[IN] pbData - 用于生成Jpeg文件的点阵内存数据块首地址
	//
	//			[IN] nWidth - 点阵图宽度
	//
	//			[IN] nHeight - 点阵图高度
	//
	//			[IN] lpszFilePath - JPEG 文件路径
	//
	// remarks:
	//			data size : nWidth * nHeight * 4
	//			DWORD pixel format: a r g b
	//
	JPEG_UTIL_API int JUtilRawMemoryToJpegFile(const BYTE* pbData, 
		int nWidth, int nHeight, LPCWSTR lpszFilePath);
}

#endif // __JPEG_UTIL_H__