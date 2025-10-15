//////////////////////////////////////////////////////////////////////////
//
// BitmapUtils.h
//
// 位图资源处理工具
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
//	summary: 创建根据位图的特殊窗体
//
//　rContourBmp: 含有窗体形状的位图对象
//　transColor: 透明色，非此透明色的点将组成结果区域
//  rWndRgn : 结果区域
//
BOOL CreateRgnByBodyColor(CBitmap &rContourBmp, COLORREF transColor, CRgn& rWndRgn);

//
//	summary: 创建根据位图的特殊窗体
//
//　rAlphaBmp: 含有窗体形状的位图对象，它的Alpha值为0的点将被视为透明点
//	rAlphaDIBmp : 含有窗体形状的DIB 32位图对象，
//	rRect : 从 rAlphaDIBmp 指定的裁剪区
//	rInfoHeader : DIB Info Header
//	lpBitsArray : DIB 位图数据数组
//  rWndRgn : 结果区域
//
BOOL CreateRgnByAlphaBits(const CBitmap &rAlphaBmp, CRgn& rWndRgn);
BOOL CreateRgnByAlphaBits(const CBitmap &rAlphaDIBmp, const CRect& rRect, CRgn& rWndRgn);
BOOL CreateRgnByAlphaBits(BITMAPINFOHEADER& rInfoHeader, LPVOID lpBitsArray,  CRgn& rWndRgn);
//
// summary : 从BaseFile中加载位图资源
// parameter:
//		pFile - 位图资源文件
// returns : 位图资源句柄
//
HBITMAP LoadBitmap(PFS::CBaseFile* pFile);
HBITMAP LoadDIBitmap(PFS::CBaseFile* pFile, BOOL bPremultiply);
LPVOID LoadBitmapData(PFS::CBaseFile* pFile, BITMAPINFOHEADER& rInfoHeader);

//
// summary: 从一张全图中剪裁出一张小图
//
void ExtractBitmap( const CBitmap& rBitmapFull, const CRect& rClipBox, CBitmap& rBitmapResult );

//
// summary: 从一张全图中剪裁出一张小图，将其绘制在指定的DC中
//
void PaintBitmap( CDC& rDC, const CBitmap& rBitmap, const CRect& rClipBox );

// 
// summary: 从一张DIB全图中剪裁出一张小图，将其绘制在指定的DC中
//
void PaintDIB(CDC& rDC, const CBitmap& rBitmap, const CRect& rClipBox );
