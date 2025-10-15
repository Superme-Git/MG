//////////////////////////////////////////////////////////////////////////
//
// BitmapUtils.cpp
//
// 位图资源处理工具
//
// author: yanghaibo
// date : 2008-11-12
//
#include "StdAfx.h"
#include "BitmapUtils.h"
#include "../../pfs/pfs2.h"
#include "RenderHelper.h"

BOOL CreateRgnByBodyColor(CBitmap &rContourBmp, COLORREF transColor, CRgn& rWndRgn)
{
	if (!rContourBmp.GetSafeHandle())
	{
		ASSERT(rContourBmp.GetSafeHandle());
		return FALSE;
	}

	BITMAP BitMap;
	if( !rContourBmp.GetBitmap( &BitMap ) )
	{
		return FALSE;
	}

	//取得图像的长和宽
	int nW = BitMap.bmWidth;
	int nH = BitMap.bmHeight;

	//创建总的窗体区域，初始region为0
	rWndRgn.DeleteObject();
	rWndRgn.CreateRectRgn(0,0,0,0); 

	DWORD dwCount = BitMap.bmHeight * BitMap.bmWidthBytes;

	LPBYTE lpBits = new BYTE[dwCount];
	DWORD dwRet = rContourBmp.GetBitmapBits(dwCount, lpBits);
	if ( dwRet != dwCount )
	{
		delete[] lpBits;
		return FALSE;
	}

	int stride = BitMap.bmBitsPixel>>3;

	BYTE* pLine = NULL;
	BYTE* pBit = NULL;
	int y;
	for(y = 0; y < nH ; y++)
	{
		pLine = lpBits + BitMap.bmWidthBytes * (y);
		if (pLine == NULL)
		{
			ASSERT(pLine != NULL);

			// TODO:
			break;
		}

		pBit = pLine;

		CRgn rgnTemp; //保存临时region

		int iX = 0;
		do
		{
			//跳过透明色找到下一个非透明色的点.
			while (iX < nW && 
				pBit[0] <= GetBValue(transColor)// + 15
				//&&
				//pBit[1] < GetGValue(transColor) 
				//&&
				//pBit[2] < GetRValue(transColor)
				)
			{
				pBit += stride;
				iX ++;
			}
			//记住这个起始点
			int iLeftX = iX;
			//寻找下个透明色的点
			while (iX < nW && 
				(pBit[0] > GetBValue(transColor)// + 15
				/*&&
				pBit[1] > GetGValue(transColor) >> 2 &&
				pBit[2] > GetRValue(transColor) >> 2 */
				) )
			{
				iX ++;
				pBit += stride;
			}

			if (iLeftX < nW)
			{
				//创建一个包含起点与终点间高为1像素的临时“region”
				rgnTemp.CreateRectRgn(iLeftX, y, iX, y+1);
				//合并到主"region".
				rWndRgn.CombineRgn(&rWndRgn, &rgnTemp, RGN_OR);
				//删除临时"region"
				rgnTemp.DeleteObject();
			}

		}while(iX < nW);
	}

	delete[] lpBits;

	return TRUE; 
}

BOOL CreateRgnByAlphaBits(const CBitmap &rAlphaBmp, CRgn& rWndRgn)
{
	if (!rAlphaBmp.GetSafeHandle())
	{
		ASSERT(rAlphaBmp.GetSafeHandle());
		return FALSE;
	}

	BITMAP BitMap;
	if( !const_cast<CBitmap&>(rAlphaBmp).GetBitmap( &BitMap ) )
	{
		return FALSE;
	}

	//取得图像的长和宽
	int nW = BitMap.bmWidth;
	int nH = BitMap.bmHeight;

	//创建总的窗体区域，初始region为0
	DWORD dwCount = BitMap.bmHeight * BitMap.bmWidthBytes;

	LPBYTE lpBits = new BYTE[dwCount];
	DWORD dwRet = rAlphaBmp.GetBitmapBits(dwCount, lpBits);
	if ( dwRet != dwCount )
	{
		delete[] lpBits;
		return FALSE;
	}

	if ( BitMap.bmBitsPixel != 32 )
	{
		// No alpha channel
		rWndRgn.DeleteObject();
		rWndRgn.CreateRectRgn(0,0,nW,nH); 

		return TRUE;
	}

	rWndRgn.DeleteObject();
	rWndRgn.CreateRectRgn(0,0,0,0); 

	BYTE* pLine = NULL;
	BYTE* pBit = NULL;
	int y;
	for(y = 0; y < nH ; y++)
	{
		pLine = lpBits + BitMap.bmWidthBytes * (y);
		pBit = pLine;
		CRgn rgnTemp; //保存临时region

		int iX = 0;
		do
		{
			//跳过透明色找到下一个非透明色的点.
			while (iX < nW && pBit[3] == 0 )
			{
				pBit += 4;
				iX ++;
			}
			//记住这个起始点
			int iLeftX = iX;
			//寻找下个透明色的点
			while (iX < nW && (pBit[3] != 0) )
			{
				iX ++;
				pBit += 4;
			}

			if (iLeftX < nW)
			{
				//创建一个包含起点与终点间高为1像素的临时“region”
				rgnTemp.CreateRectRgn(iLeftX, y, iX, y+1);
				//合并到主"region".
				rWndRgn.CombineRgn(&rWndRgn, &rgnTemp, RGN_OR);
				//删除临时"region"
				rgnTemp.DeleteObject();
			}

		}while(iX < nW);
	}

	delete[] lpBits;

	return TRUE; 
}

BOOL CreateRgnByAlphaBits(const CBitmap &rAlphaDIBmp, const CRect& rRect, CRgn& rWndRgn)
{
	if (!rAlphaDIBmp.GetSafeHandle())
	{
		ASSERT(rAlphaDIBmp.GetSafeHandle());
		return FALSE;
	}

	BITMAP BitMap;
	if( ! const_cast<CBitmap&>(rAlphaDIBmp).GetBitmap( &BitMap ) )
	{
		return FALSE;
	}


	//取得图像的长和宽
	int nW = BitMap.bmWidth;
	int nH = BitMap.bmHeight;

	if ( BitMap.bmBitsPixel != 32 )
	{
		// No alpha channel
		rWndRgn.DeleteObject();
		rWndRgn.CreateRectRgn(0,0,nW,nH); 

		return TRUE;
	}

	//创建总的窗体区域，初始region为0
	DWORD dwCount = BitMap.bmHeight * BitMap.bmWidthBytes;

	LPBYTE lpBits = new BYTE[dwCount];
	DWORD dwRet = rAlphaDIBmp.GetBitmapBits(dwCount, lpBits);
	if ( dwRet != dwCount )
	{
		delete[] lpBits;
		return FALSE;
	}

	if ( BitMap.bmBitsPixel != 32 )
	{
		// No alpha channel
		rWndRgn.DeleteObject();
		rWndRgn.CreateRectRgn(0,0,nW,nH); 

		return TRUE;
	}

	rWndRgn.DeleteObject();
	rWndRgn.CreateRectRgn(0,0,0,0); 

	BYTE* pLine = NULL;
	BYTE* pBit = NULL;
	int y;
	nW = rRect.right;
	for(y = rRect.top; y < rRect.bottom; y++)
	{
		pLine = lpBits + BitMap.bmWidthBytes * (BitMap.bmHeight-y-1);
		
		CRgn rgnTemp; //保存临时region
		int iX = rRect.left;
		pBit = pLine + iX * 4;
		do
		{
			//跳过透明色找到下一个非透明色的点.
			while (iX < nW && pBit[3] == 0 )
			{
				pBit += 4;
				iX ++;
			}
			//记住这个起始点
			int iLeftX = iX;
			//寻找下个透明色的点
			while (iX < nW && (pBit[3] != 0) )
			{
				iX ++;
				pBit += 4;
			}

			if (iLeftX < nW)
			{
				//创建一个包含起点与终点间高为1像素的临时“region”
				rgnTemp.CreateRectRgn(iLeftX-rRect.left, y-rRect.top, iX-rRect.left, y+1-rRect.top);
				//合并到主"region".
				rWndRgn.CombineRgn(&rWndRgn, &rgnTemp, RGN_OR);
				//删除临时"region"
				rgnTemp.DeleteObject();
			}

		} while(iX < nW);
	}

	delete[] lpBits;

	return TRUE; 
}

BOOL CreateRgnByAlphaBits(BITMAPINFOHEADER &BitMap, LPVOID lpBitsArray, CRgn& rWndRgn)
{
	//取得图像的长和宽
	int nW = BitMap.biWidth;
	int nH = BitMap.biHeight;

	//创建总的窗体区域，初始region为0
	if ( BitMap.biBitCount != 32 )
	{
		// No alpha channel
		rWndRgn.DeleteObject();
		rWndRgn.CreateRectRgn(0,0,nW,nH); 
		return TRUE;
	}

	DWORD bmWidthBytes = nW * 4;
	LPBYTE lpBits = (LPBYTE)lpBitsArray; // row ptr
	
	rWndRgn.DeleteObject();
	rWndRgn.CreateRectRgn(0,0,0,0); 

	BYTE* pLine = NULL;
	BYTE* pBit = NULL;
	int y;
	for(y = 0; y < nH; y++)
	{
		pLine = lpBits + bmWidthBytes * (nH - y-1);

		pBit = pLine;
		CRgn rgnTemp; //保存临时region

		int iX = 0;
		do
		{
			//跳过透明色找到下一个非透明色的点.
			while (iX < nW && pBit[3] == 0 )
			{
				pBit += 4;
				iX ++;
			}
			//记住这个起始点
			int iLeftX = iX;
			//寻找下个透明色的点
			while (iX < nW && (pBit[3] != 0) )
			{
				iX ++;
				pBit += 4;
			}

			if (iLeftX < nW)
			{
				//创建一个包含起点与终点间高为1像素的临时“region”
				rgnTemp.CreateRectRgn(iLeftX, y, iX, y+1);
				//合并到主"region".
				rWndRgn.CombineRgn(&rWndRgn, &rgnTemp, RGN_OR);
				//删除临时"region"
				rgnTemp.DeleteObject();
			}

		}while(iX < nW);
	}

	return TRUE; 
}


HBITMAP LoadBitmap(PFS::CBaseFile* pFile )
{
	BITMAPFILEHEADER fileHeader = {0};

	pFile->Seek(0, PFS::FSM_SET);

	size_t ret = pFile->Read( &fileHeader, sizeof( BITMAPFILEHEADER ) );
	if ( ret != sizeof( BITMAPFILEHEADER ) )
	{
		return NULL;
	}

	// Check file type
	if( fileHeader.bfType != 'MB' )
	{
		return NULL;
	}

	// Read info header
	BITMAPINFOHEADER infoHeader;
	ret = pFile->Read( &infoHeader, sizeof( BITMAPINFOHEADER ) );
	if ( ret != sizeof( BITMAPINFOHEADER ) )
	{
		return NULL;
	}

	size_t sizeOfBits = infoHeader.biSizeImage ? infoHeader.biSizeImage :
		fileHeader.bfSize - fileHeader.bfOffBits;
	if ( sizeOfBits == NULL )
	{
		// empty bitmap
		return NULL;
	}

	HDC hScreenDC = ::GetDC(NULL);
	HBITMAP hBitmap = NULL;

	// Alloc bits array
	LPVOID bitsArray = LocalAlloc(LPTR, sizeOfBits);
	if ( bitsArray == NULL )
	{
		// No enough memory
		return NULL;
	}

	// Read bits to the array
	pFile->Seek(fileHeader.bfOffBits, PFS::FSM_SET);
	if( sizeOfBits != pFile->Read( bitsArray, sizeOfBits ) )
	{
		goto __FAILED;
	}

	hBitmap = ::CreateCompatibleBitmap(
		hScreenDC, infoHeader.biWidth, infoHeader.biHeight);
	if ( hBitmap == NULL )
	{
		goto __FAILED;
	}

	int iScanLines = SetDIBits(
		NULL,					// handle to DC
		hBitmap,				// handle to bitmap
		0,          			// starting scan line
		infoHeader.biHeight,	// number of scan lines
		bitsArray,				// array of bitmap bits
		(CONST BITMAPINFO *)&infoHeader,  // bitmap data
		DIB_RGB_COLORS			// type of color indexes to use
		);

	if ( iScanLines != infoHeader.biHeight )
	{
		::DeleteObject( hBitmap );
		hBitmap = NULL;
	}

#ifdef DEBUG
	BITMAP bmp;
	::GetObject(hBitmap, sizeof(BITMAP), &bmp); 
	if ( bmp.bmBitsPixel != GetDeviceCaps( hScreenDC, BITSPIXEL ) )
	{
		DeleteObject( hBitmap );
		hBitmap = NULL;
		goto __FAILED;
	}
#endif // DEBUG

__FAILED:	
	if ( hScreenDC != NULL )
	{
		::ReleaseDC(NULL, hScreenDC);
	}
	LocalFree( bitsArray );
	return hBitmap;
}

HBITMAP LoadDIBitmap(PFS::CBaseFile* pFile, BOOL bPremultiply)
{
	BITMAPFILEHEADER fileHeader = {0};

	pFile->Seek(0, PFS::FSM_SET);

	size_t ret = pFile->Read( &fileHeader, sizeof( BITMAPFILEHEADER ) );
	if ( ret != sizeof( BITMAPFILEHEADER ) )
	{
		return NULL;
	}

	// Check file type
	if( fileHeader.bfType != 'MB' )
	{
		return NULL;
	}

	// Read info header
	BITMAPINFO bitmapInfo;
	BITMAPINFOHEADER& infoHeader = bitmapInfo.bmiHeader;
	ret = pFile->Read( &infoHeader, sizeof( BITMAPINFOHEADER ) );
	if ( ret != sizeof( BITMAPINFOHEADER ) )
	{
		return NULL;
	}

	int strideAbs = 0;
	if( infoHeader.biBitCount == 32 )
		strideAbs = infoHeader.biWidth * 4;
	else
		strideAbs = (infoHeader.biWidth * infoHeader.biBitCount + 31 ) >> 5 << 2; 

	UINT sizeOfBits = infoHeader.biHeight * strideAbs;
	if ( sizeOfBits == 0 )
	{
		// empty bitmap
		return NULL;
	}

	LPVOID bitsArray = NULL;
	HBITMAP hBitmap = ::CreateDIBSection (
		NULL,
		&bitmapInfo,
		DIB_RGB_COLORS,
		&bitsArray,
		NULL,
		0);
	if ( hBitmap == NULL )
	{
		return NULL;
	}

	// Read bits to the array
	pFile->Seek(fileHeader.bfOffBits, PFS::FSM_SET);
	if( sizeOfBits != pFile->Read( bitsArray, sizeOfBits ) )
	{
		DeleteObject(hBitmap);
		hBitmap = NULL;
	}

	if( infoHeader.biBitCount == 32 && bPremultiply )
	{
		MHZX::CRenderHelper::PremultiplyImageBuffer( bitsArray, infoHeader.biWidth, infoHeader.biHeight );
	}

#ifdef DEBUG
	BITMAP bmp;
	::GetObject(hBitmap, sizeof(BITMAP), &bmp); 
	if ( bmp.bmBitsPixel != bitmapInfo.bmiHeader.biBitCount )
	{
		DeleteObject( hBitmap );
		hBitmap = NULL;
	}
#endif // DEBUG

	return hBitmap;
}

//////////////////////////////////////////////////////////////////////////
//
// 从文件中读取Bitmap信息和二进制位图数据
//
LPVOID LoadBitmapData(PFS::CBaseFile* pFile, BITMAPINFOHEADER& rInfoHeader)
{
	BITMAPFILEHEADER fileHeader = {0};

	pFile->Seek(0, PFS::FSM_SET);

	size_t ret = pFile->Read( &fileHeader, sizeof( BITMAPFILEHEADER ) );
	if ( ret != sizeof( BITMAPFILEHEADER ) )
	{
		return NULL;
	}

	// Check file type
	if( fileHeader.bfType != 'MB' )
	{
		return NULL;
	}

	// Read info header
	ret = pFile->Read( &rInfoHeader, sizeof( BITMAPINFOHEADER ) );
	if ( ret != sizeof( BITMAPINFOHEADER ) )
	{
		return NULL;
	}

	size_t sizeOfBits = rInfoHeader.biSizeImage ? rInfoHeader.biSizeImage :
		fileHeader.bfSize - fileHeader.bfOffBits;
	if ( sizeOfBits == NULL )
	{
		// empty bitmap
		return NULL;
	}

	HBITMAP hBitmap = NULL;

	// Alloc bits array
	LPVOID bitsArray = LocalAlloc(LPTR, sizeOfBits);
	if ( bitsArray == NULL )
	{
		// No enough memory
		return NULL;
	}

	// Read bits to the array
	pFile->Seek(fileHeader.bfOffBits, PFS::FSM_SET);
	if( sizeOfBits != pFile->Read( bitsArray, sizeOfBits ) )
	{
		goto __FAILED;
	}

	return bitsArray;

__FAILED:	
	LocalFree( bitsArray );
	return bitsArray;
}



void ExtractBitmap( const CBitmap& rBitmapFull, const CRect& rClipBox, CBitmap& rBitmapResult )
{
	CDC dcScreen;
	dcScreen.Attach(::GetDC(NULL));

	CDC dcTemp;
	dcTemp.CreateCompatibleDC( &dcScreen );

	CDC rDC;
	rDC.CreateCompatibleDC( &dcScreen );

	dcTemp.SelectObject( (CBitmap*)&rBitmapFull );

	rBitmapResult.DeleteObject();
	rBitmapResult.CreateCompatibleBitmap(&dcScreen, rClipBox.Width(), rClipBox.Height() );
	rDC.SelectObject(&rBitmapResult);
	rDC.BitBlt( 0, 0, rClipBox.Width(), rClipBox.Height(), &dcTemp, rClipBox.left, rClipBox.top, SRCCOPY );
}

void PaintBitmap( CDC& rDC, const CBitmap& rBitmap, const CRect& rClipBox )
{
	CDC dcScreen;
	dcScreen.Attach(::GetDC(NULL));

	CDC dcTemp;
	dcTemp.CreateCompatibleDC( &dcScreen );

	if (rDC.GetSafeHdc() == NULL )
	{	
		rDC.CreateCompatibleDC( &dcScreen );
	}

	dcTemp.SelectObject( (CBitmap*)&rBitmap );

	CBitmap memBitmap;
	memBitmap.CreateCompatibleBitmap(&dcScreen, rClipBox.Width(), rClipBox.Height() );
	rDC.SelectObject(&memBitmap);
	rDC.BitBlt( 0, 0, rClipBox.Width(), rClipBox.Height(), &dcTemp, rClipBox.left, rClipBox.top, SRCCOPY );

	CBitmap* pBmpFR = rDC.GetCurrentBitmap();
	pBmpFR->SetBitmapDimension( rClipBox.Width(), rClipBox.Height() );
}

void PaintDIB(CDC& rDC, const CBitmap& rBitmap, const CRect& rClipBox )
{
	CDC dcTemp;
	dcTemp.CreateCompatibleDC( NULL );
	CGdiObject* pOldBitmap = dcTemp.SelectObject( (CBitmap*)&rBitmap );

	if (rDC.GetSafeHdc() == NULL )
	{
		// 创建DIB DC
		rDC.CreateCompatibleDC( NULL );
		rDC.SelectObject(const_cast<CBitmap*>(&rBitmap)); // 首先选进DIBitmap,使得rDC也成为DIB DC.
	}

	CBitmap memBitmap;
	memBitmap.CreateCompatibleBitmap(&dcTemp, rClipBox.Width(), rClipBox.Height() );
	rDC.SelectObject(&memBitmap);
	rDC.BitBlt( 0, 0, rClipBox.Width(), rClipBox.Height(), &dcTemp, rClipBox.left, rClipBox.top, SRCCOPY );

	CBitmap* pBmpFR = rDC.GetCurrentBitmap();
	pBmpFR->SetBitmapDimension( rClipBox.Width(), rClipBox.Height() );

	dcTemp.SelectObject(pOldBitmap);
}
