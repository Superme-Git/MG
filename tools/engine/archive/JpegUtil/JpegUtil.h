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
	// summary: ��BMP�ڴ���������Jpeg�ļ�
	// 
	// parameter:
	//
	//			[IN] pbData - ��������Jpeg�ļ��ĵ����ڴ����ݿ��׵�ַ
	//
	//			[IN] nWidth - ����ͼ���
	//
	//			[IN] nHeight - ����ͼ�߶�
	//
	//			[IN] lpszFilePath - JPEG �ļ�·��
	//
	// remarks:
	//			data size : nWidth * nHeight * 4
	//			DWORD pixel format: a r g b
	//
	JPEG_UTIL_API int JUtilRawMemoryToJpegFile(const BYTE* pbData, 
		int nWidth, int nHeight, LPCWSTR lpszFilePath);
}

#endif // __JPEG_UTIL_H__