//////////////////////////////////////////////////////////////////////////
//
// IconFile.cpp
//
// Icon��Դ����
//
// author : yanghaibo
// date : 2008-11-14
//
#include "StdAfx.h"
#include "IconFile.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction

// 4 �ֽڶ������ÿ��λͼ������ռ�ֽ���
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
		pHeader->idCount == 0  // ������ͼ�����
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

	// ICON�ļ���Ҫ˵��
	//
	// Icon Header���̶����ȣ�˵�����е�ICON��Ŀ
	//
	// IconDirEntries������ļ��к��е�ICON��Ŀ�й�
	// 
	// ����ÿһ��������ICON�������²������
	// BITMAPINFO
	// XOR Bits
	// AND Bits
	//
	// BITMAPINFO,��BITMAPINFOHEADER��������biClrUsed��biBitCountȷ���Ƿ���RGBQUARD(��ɫ��)��Ϣ
	// ����biHeightΪXOR Bits��AND Bits�����ֵĸ߶Ⱥ�.
	// XOR Bits�ֶ���biClrUsed��biBitCount����
	//
	// AND Bits�ֶ����Ǻڰ�ͼ(monochrome)��������ÿ�����ݳ���Ϊ4�ֽڶ��롣
	// ����16*16��Icon��˵����ÿ��Ϊ[((16+31)>>5)<<2]=4�ֽڣ����Թ�16*4=64�ֽ�
	// ����32*32��Icon��˵����ÿ��Ϊ[((32+31)>>5)<<2]=4�ֽڣ����Թ�32*4=128�ֽ�
	// ����48*48��Icon��˵����ÿ��Ϊ[((48+31)>>5)<<2]=8�ֽڣ����Թ�48*8=384�ֽ�
	//
	// ���ݳߴ�ȷ����Ҫ���ص�����
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
