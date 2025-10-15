//////////////////////////////////////////////////////////////////////////
//
// IconFile.cpp
//
// Icon资源处理
//
// author : yanghaibo
// date : 2008-11-14
//
#include "StdAfx.h"
#include "IconFile.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction

// 4 字节对齐计算每行位图数据所占字节数
#define WIDTHBYTES(bits) ((((bits) + 31) >> 5)<<2)

CIconFile::CIconFile()
{
	m_hIcon = NULL;

	m_cxDesired = 0;
	m_cyDesired = 0;
}

CIconFile::~CIconFile()
{
	if (m_hIcon != NULL)
	{
		DestroyIcon(m_hIcon);
	}
}

void CIconFile::SetIconDesiredSize( int cx, int cy )
{
	if ( cx == 0 || cy == 0 )
	{
		m_cxDesired = GetSystemMetrics( SM_CXICON );
		m_cyDesired = GetSystemMetrics( SM_CYICON );
	}
	else
	{
		m_cxDesired = cx;
		m_cyDesired = cy;
	}
}

#pragma pack ( push )
#pragma pack( 2 )
typedef struct
{
	UINT Width,Height,Colors;
	LPBYTE lpBits;
	DWORD dwNumBytes;
	LPBITMAPINFO lpbi;
	LPBYTE lpXOR;
	LPBYTE lpAND;
}ICONIMAGE,*LPICONIMAGE;
typedef struct
{
	BYTE bWidth;
	BYTE bHeight;
	BYTE bColorCount;
	BYTE bReserved;
	WORD wPlanes;
	WORD wBitCount;
	DWORD dwBytesInRes;
	DWORD dwImageOffset;
}ICONDIRENTRY,*LPICONDIRENTRY;
typedef struct
{
	WORD idReserved;			// Reserved
	WORD idType;				// resource type (1 for icons)
	WORD idCount;				// how many images?
	ICONDIRENTRY idEntries[1];	// entries for each image (idCount of 'em)
}ICONDIR, *LPICONDIR;
typedef const ICONDIR * LPCICONDIR;
#pragma pack( pop )

BOOL _CheckHead(LPCICONDIR pHeader);
LPBYTE FindDIBBits(LPBITMAPINFOHEADER lpbi);
DWORD PaletteSize(LPBITMAPINFOHEADER lpbi);
DWORD BytesPerLine(LPBITMAPINFOHEADER lpBMIH);
HICON MakeIconFromResource(LPICONIMAGE lpIcon);

BOOL _CheckHead(LPCICONDIR pHeader )
{
	if (pHeader->idReserved != 0 ||
		pHeader->idType != 1 || 
		pHeader->idCount == 0  // 包含的图标个数
		)
	{
		return FALSE;
	}	
	else 
	{
		return TRUE; 
	}
}

LPBYTE FindDIBBits(LPBITMAPINFOHEADER lpbi)
{
	return ((LPBYTE)lpbi + lpbi->biSize + PaletteSize(lpbi));
}

DWORD PaletteSize(LPBITMAPINFOHEADER lpbi)
{
	DWORD dwClrUsed = lpbi->biClrUsed;
	if (dwClrUsed) 
	{
		return dwClrUsed * sizeof(RGBQUAD);
	}

	WORD wBitCount = lpbi->biBitCount;
	if ( wBitCount <= 8 )
	{
		return  (1UL << wBitCount) * sizeof(RGBQUAD);
	}
	else
	{
		return 0;
	}
}

DWORD BytesPerLine(LPBITMAPINFOHEADER lpBMIH)
{
	return WIDTHBYTES(lpBMIH->biWidth * lpBMIH->biPlanes * lpBMIH->biBitCount);
}

HICON MakeIconFromResource(LPICONIMAGE lpIcon)
{
	// Sanity Check
	if( lpIcon == NULL || lpIcon->lpBits == NULL )
		return NULL;

	// Let the OS do the real work :)
	HICON hIcon = CreateIconFromResourceEx( 
		lpIcon->lpBits, lpIcon->dwNumBytes, TRUE, 0x00030000,
		lpIcon->Width, lpIcon->Height, // icon real size
		LR_DEFAULTCOLOR );

	// It failed, odds are good we're on NT so try the non-Ex way
	if( hIcon == NULL )
	{
		// We would break on NT if we try with a 16bpp image
		if(lpIcon->lpbi->bmiHeader.biBitCount != 16)
		{
			hIcon = CreateIconFromResource( lpIcon->lpBits, lpIcon->dwNumBytes, TRUE, 0x00030000 );
		}
	}

	return hIcon;
}

BOOL CIconFile::CreateFromData(LPBYTE pBuff,long lDataSize)
{
	if (pBuff == NULL || lDataSize <= sizeof(ICONDIR) )
	{
		return FALSE;
	}

	ICONDIR* iconDir = (ICONDIR*)pBuff;
	if( !_CheckHead(iconDir) )
		return FALSE;

	if ( m_cyDesired == 0 || m_cxDesired == 0 )
	{
		m_cxDesired = GetSystemMetrics( SM_CXICON );
		m_cyDesired = GetSystemMetrics( SM_CYICON );
	}

	// ICON文件简要说明
	//
	// Icon Header，固定长度，说明还有的ICON数目
	//
	// IconDirEntries，与该文件中含有的ICON数目有关
	// 
	// 对于每一个单独的ICON，由以下部分组成
	// BITMAPINFO
	// XOR Bits
	// AND Bits
	//
	// BITMAPINFO,含BITMAPINFOHEADER，并根据biClrUsed和biBitCount确定是否含有RGBQUARD(颜色表)信息
	// 其中biHeight为XOR Bits和AND Bits两部分的高度和.
	// XOR Bits字段由biClrUsed和biBitCount决定
	//
	// AND Bits字段总是黑白图(monochrome)，并满足每行数据长度为4字节对齐。
	// 对于16*16的Icon来说，它每行为[((16+31)>>5)<<2]=4字节，所以共16*4=64字节
	// 对于32*32的Icon来说，它每行为[((32+31)>>5)<<2]=4字节，所以共32*4=128字节
	// 对于48*48的Icon来说，它每行为[((48+31)>>5)<<2]=8字节，所以共48*8=384字节
	//
	// 根据尺寸确定需要加载的数据
	ICONIMAGE iconImage;
	for ( int i = iconDir->idCount - 1; i >= 0; -- i )
	{
		ICONDIRENTRY& ide = iconDir->idEntries[i];

		iconImage.lpBits = (LPBYTE)(pBuff + ide.dwImageOffset);
		iconImage.dwNumBytes = ide.dwBytesInRes;
		iconImage.Width = ide.bWidth;
		iconImage.Height = ide.bHeight;

		// The following 4 members are really not used!
		//iconImage.lpbi = (LPBITMAPINFO)iconImage.lpBits;
		//iconImage.Colors = iconImage.lpbi->bmiHeader.biPlanes * iconImage.lpbi->bmiHeader.biBitCount;
		//iconImage.lpXOR = FindDIBBits(&iconImage.lpbi->bmiHeader);
		//iconImage.lpAND = iconImage.lpXOR + (iconImage.Height * BytesPerLine(&iconImage.lpbi->bmiHeader));

		if ( iconImage.Width == m_cxDesired && 
			iconImage.Height == m_cyDesired )
		{
			break;
		}
	}

	HICON hTemp = MakeIconFromResource(&iconImage);
	if (hTemp != NULL)
	{
		if (m_hIcon != NULL)
		{
			DestroyIcon(m_hIcon);
		}

		m_hIcon = hTemp;
	}

	return m_hIcon != NULL;
}
