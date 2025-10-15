//////////////////////////////////////////////////////////////////////////
//
// BitmapUtils.h
//
// λͼ��Դ������
//
// author: yanghaibo
// date : 2008-11-12
//

#pragma once

namespace PFS
{
	class CBaseFile;
}

//
//	summary: ��������λͼ�����ⴰ��
//
//��rContourBmp: ���д�����״��λͼ����
//��transColor: ͸��ɫ���Ǵ�͸��ɫ�ĵ㽫��ɽ������
//  rWndRgn : �������
//
BOOL CreateRgnByBodyColor(CBitmap &rContourBmp, COLORREF transColor, CRgn& rWndRgn);

//
//	summary: ��������λͼ�����ⴰ��
//
//��rAlphaBmp: ���д�����״��λͼ��������AlphaֵΪ0�ĵ㽫����Ϊ͸����
//	rAlphaDIBmp : ���д�����״��DIB 32λͼ����
//	rRect : �� rAlphaDIBmp ָ���Ĳü���
//	rInfoHeader : DIB Info Header
//	lpBitsArray : DIB λͼ��������
//  rWndRgn : �������
//
BOOL CreateRgnByAlphaBits(const CBitmap &rAlphaBmp, CRgn& rWndRgn);
BOOL CreateRgnByAlphaBits(const CBitmap &rAlphaDIBmp, const CRect& rRect, CRgn& rWndRgn);
BOOL CreateRgnByAlphaBits(BITMAPINFOHEADER& rInfoHeader, LPVOID lpBitsArray,  CRgn& rWndRgn);
//
// summary : ��BaseFile�м���λͼ��Դ
// parameter:
//		pFile - λͼ��Դ�ļ�
// returns : λͼ��Դ���
//
HBITMAP LoadBitmap(PFS::CBaseFile* pFile);
HBITMAP LoadDIBitmap(PFS::CBaseFile* pFile, BOOL bPremultiply);
LPVOID LoadBitmapData(PFS::CBaseFile* pFile, BITMAPINFOHEADER& rInfoHeader);

//
// summary: ��һ��ȫͼ�м��ó�һ��Сͼ
//
void ExtractBitmap( const CBitmap& rBitmapFull, const CRect& rClipBox, CBitmap& rBitmapResult );

//
// summary: ��һ��ȫͼ�м��ó�һ��Сͼ�����������ָ����DC��
//
void PaintBitmap( CDC& rDC, const CBitmap& rBitmap, const CRect& rClipBox );

// 
// summary: ��һ��DIBȫͼ�м��ó�һ��Сͼ�����������ָ����DC��
//
void PaintDIB(CDC& rDC, const CBitmap& rBitmap, const CRect& rClipBox );
