///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : TargaDecoder.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Targa(tga)ͼ������/�ļ�������
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
	// ����TGA�ļ�,������HBITMAP����,
	// ���ص�HBITMAP������Ҫʹ��Win32API CloseObject()�ͷ�.
	// ���ص�BITMAPINFO* ��Ҫʹ�� FreeBitmapInfo() �ͷ�(�μ�PictureLoader.h)
	// bPremultiply ָ�����ɵ�Bitmap�Ƿ��Ѿ�������AlphaBlend��Ԥ����(�μ��������˵��)
	extern BITMAPINFO* texReadTGA(LPCWSTR lpszFileName, HBITMAP* pBitmap, BOOL bPremultiply = TRUE);

	// ����TGA����,����ȡTGA�Ŀ����Ϣ�͸߶���Ϣ
	// ��TGA���ݿ���������bgra32λ��ʽ��bitmap,���߶�Ҫȡ��ֵ
	// ���ص�*lppvPixels������Ҫ��::HeapFree( ::GetProcessHeap(), 0, lpvPixels )�ͷ�
	// ������ʹ��CPictureLoader::FreePictureData()�ͷ�(�μ�PictureLoader.h)
	extern BOOL LoadTGAData(LPCWSTR lpszFileName, LPVOID* lppvPixels, 
		ULONG* pulWidth, ULONG* pulHeight, UINT* puiBitCount);


	extern BOOL LoadTGAData(PFS::CBaseFile& rFile, LPVOID* lppvPixels, 
		ULONG* pulWidth, ULONG* pulHeight, UINT* puiBitCount);

	// �ͷ��� LoadTGAData() ������ڴ�
	void FreePictureData( LPVOID pPixels );

}