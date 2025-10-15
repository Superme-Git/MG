// ChildView.cpp : CChildView 类的实现
//
//
// 2008-10-08	Liu Dongxue, initial version
// 2008-10-27	Liu Dongxue, alpha blending support 
// 2008-11-07	Liu Dongxue, PPM support and optimization by using the HBITMAP
// 2008-11-21	Liu Dongxue, support to display bmp files
// 2008-12-02	Liu Dongxue, add a toolbar like that in windows picture and fax viewer. 
//							Implement the actions of feat-size and actual-size, previous- and next-navigation.
// 2009-03-28	Liu Dongxue, 修复CompitableBitmap导致图像数据丢失的问题(16bit显示设备下)，改为创建DIB
//							 修复可能会误把目录当做文件名读取的BUG。
//							重点在于兼容Memory设备，DIBBitmap，Display DC 3者之间的兼容关系做了严格检测。
//
#include "stdafx.h"
#include "PictureViewer.h"
#include "ChildView.h"
#include <afxadv.h>
#include <shlwapi.h>
#include <assert.h>
#include "../libdds/include/libdds.h"
#include "../libpak/PicPak.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define GATReportError(msg,filename) \
	{\
		::MessageBox(GetSafeHwnd(), filename, _T(msg), MB_OK| MB_ICONERROR );\
	}\


// CChildView

CChildView::CChildView()
	: m_bAlphaBlendMode(TRUE)
{
	m_posInFileList = NULL;
	m_dwSizeScale = 0;
	m_maxClipWidth = 0;
	m_maxClipHeight = 0;
	m_stretchBltMode = COLORONCOLOR;
	m_hBITMAP = NULL;
}

CChildView::~CChildView()
{
	if (m_hBITMAP) 
	{
		DeleteObject(m_hBITMAP);
		m_hBITMAP = NULL;
	}

	if( m_hbrBackground != NULL )
	{
		DeleteObject(m_hbrBackground);
		m_hbrBackground = NULL;
	}
}

// 因为现在实现了图片的Alpha混合处理，所以，需要移除的部分就不能全跑去一个矩形区了

BEGIN_MESSAGE_MAP(CChildView, CWnd)
	ON_WM_PAINT()
	ON_WM_SIZE()
	ON_WM_ERASEBKGND()
	ON_COMMAND( ID_FILE_OPEN, OnFileOpen )
	ON_COMMAND(ID_PIC_ALPHA_BLEND, &CChildView::OnPicAlphaBlend)
	ON_UPDATE_COMMAND_UI(ID_PIC_ALPHA_BLEND, &CChildView::OnUpdatePicAlphaBlend)
	ON_COMMAND_EX_RANGE( ID_FILE_MRU_FIRST, ID_FILE_MRU_LAST,  &CChildView::OnOpenRecentFile )
	ON_COMMAND_EX_RANGE( ID_PIC_STRETCH_BLACKONWHITE, ID_PIC_STRETCH_HALFTONE, &CChildView::OnPicStretchBltMode)
	ON_UPDATE_COMMAND_UI_RANGE(ID_PIC_STRETCH_BLACKONWHITE, ID_PIC_STRETCH_HALFTONE, &CChildView::OnUpdatePicStretchBltMode)
	ON_COMMAND( ID_PIC_PREV, OnPrev )
	ON_COMMAND( ID_PIC_NEXT, OnNext )
	ON_COMMAND( ID_PIC_ZOOM_IN, OnZoomIn )
	ON_COMMAND( ID_PIC_ZOOM_OUT, OnZoomOut )
	ON_COMMAND( ID_PIC_FEAT_SIZE, OnFeatSize )
	ON_COMMAND( ID_PIC_ACTUAL_SIZE, OnActualSize )
	ON_UPDATE_COMMAND_UI(ID_PIC_FEAT_SIZE, OnUpdateFeatSize )
	ON_UPDATE_COMMAND_UI(ID_PIC_ACTUAL_SIZE, OnUpdateActualSize )
	ON_UPDATE_COMMAND_UI(ID_PIC_PREV, OnUpdatePictureOpened)
	ON_UPDATE_COMMAND_UI(ID_PIC_NEXT, OnUpdatePictureOpened)
	ON_UPDATE_COMMAND_UI(ID_PIC_ZOOM_IN, OnUpdatePictureOpened)
	ON_UPDATE_COMMAND_UI(ID_PIC_ZOOM_OUT, OnUpdatePictureOpened)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVE_AS, &CChildView::OnUpdateFileSaveAs)
	ON_COMMAND(ID_FILE_SAVE_AS, &CChildView::OnFileSaveAs)
END_MESSAGE_MAP()

// CChildView 消息处理程序


void CChildView::OnUpdateFileSaveAs(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_hBITMAP != NULL );
}

void CChildView::OnUpdatePictureOpened(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_memDC2.GetSafeHdc() != NULL );
}

void CChildView::OnUpdateFeatSize(CCmdUI *pCmdUI)
{
	if ( m_memDC2.GetSafeHdc() == NULL )
	{
		pCmdUI->SetCheck( FALSE );
		pCmdUI->Enable( FALSE );
	}
	else
	{
		pCmdUI->SetCheck( m_dwSizeScale == 0 );
		pCmdUI->Enable( TRUE );
	}
}

void CChildView::OnUpdateActualSize(CCmdUI *pCmdUI)
{
	if ( m_memDC2.GetSafeHdc() == NULL )
	{
		pCmdUI->SetCheck( FALSE );
		pCmdUI->Enable( FALSE );
	}
	else
	{
		pCmdUI->SetCheck( m_dwSizeScale == 1 );
		pCmdUI->Enable( TRUE );
	}
}

void CChildView::OnPrev()
{
	if ( m_posInFileList != NULL )
	{
		if ( m_posInFileList != m_fileNameList.GetHeadPosition() )
		{
			m_fileNameList.GetPrev( m_posInFileList );
		}
		else
		{
			// goto the last one if we are at the head position
			m_posInFileList = m_fileNameList.GetTailPosition();
		}

		CString strFileName = m_fileNameList.GetAt( m_posInFileList );
		ASSERT ( m_posInFileList != NULL );
		if( InnerShowPicture( strFileName ) )
		{
			AfxGetApp()->AddToRecentFileList( strFileName );
		}
	}
}

void CChildView::OnNext()
{
	if ( m_posInFileList != NULL )
	{
		if ( m_posInFileList != m_fileNameList.GetTailPosition() )
		{
			m_fileNameList.GetNext( m_posInFileList );
		}
		else
		{
			// goto the first one if we are at the tail position
			m_posInFileList = m_fileNameList.GetHeadPosition();
		}

		CString strFileName = m_fileNameList.GetAt( m_posInFileList );
		ASSERT ( m_posInFileList != NULL );
		if( InnerShowPicture( strFileName ) )
		{
			AfxGetApp()->AddToRecentFileList( strFileName );
		}
	}
}

void CChildView::OnZoomIn()
{
	//MHZX::CPicPak pak;
	//if( pak.Open( L"D:\\release\\pak\\bhicon.pak" ) )
	//{
	//	return;
	//}

	// TODO: 
	MessageBox( _T("尚未实现！") );
}

void CChildView::OnZoomOut()
{
	// TODO: 
	MessageBox( _T("尚未实现！") );
}

void CChildView::OnFeatSize()
{
	m_dwSizeScale = 0;
	Refresh();
}

void CChildView::OnActualSize()
{
	m_dwSizeScale = 1;
	Refresh();
}

BOOL CChildView::PreCreateWindow(CREATESTRUCT& cs) 
{
	if (!CWnd::PreCreateWindow(cs))
		return FALSE;

	//cs.dwExStyle |= WS_EX_CLIENTEDGE;
	cs.style &= ~WS_BORDER;

	m_hbrBackground = CreateSolidBrush( RGB(238,242,251) );

	cs.lpszClass = AfxRegisterWndClass(CS_HREDRAW|CS_VREDRAW|CS_DBLCLKS, 
		::LoadCursor(NULL, IDC_ARROW), m_hbrBackground, NULL);

	return TRUE;
}

BITMAPINFO* texReadPPM( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels = NULL);
BITMAPINFO* texReadPIC( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels = NULL);
BITMAPINFO* texReadP0X( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels = NULL);
BITMAPINFO* texReadB0X( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels = NULL);
BITMAPINFO* texReadTGA( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels = NULL);
BITMAPINFO* texReadPNG( LPCTSTR filename, HBITMAP* pBitmap, unsigned char** ppPixels = NULL);

void CChildView::OnPaint() 
{
	CPaintDC dc(this);
	
	DrawPicture( &dc );
}

BOOL CChildView::OnOpenRecentFile(UINT nID)
{
	class CAppHelper : public CWinApp
	{
	public:
		CRecentFileList* GetRecentFileList() const { return m_pRecentFileList; };
	};

	CRecentFileList* pRecentFileList = ((CAppHelper*)AfxGetApp())->GetRecentFileList();

	ASSERT_VALID(this);
	ENSURE(pRecentFileList != NULL);

	ENSURE_ARG(nID >= ID_FILE_MRU_FILE1);
	ENSURE_ARG(nID < ID_FILE_MRU_FILE1 + (UINT)pRecentFileList->GetSize());
	int nIndex = nID - ID_FILE_MRU_FILE1;
	ASSERT((*pRecentFileList)[nIndex].GetLength() != 0);

	TRACE(traceAppMsg, 0, _T("MRU: open file (%d) '%s'.\n"), (nIndex) + 1,
		(LPCTSTR)(*pRecentFileList)[nIndex]);

	CString strPathName = (*pRecentFileList)[nIndex];
	if (!InnerShowPicture(strPathName))
		pRecentFileList->Remove(nIndex);
	else
	{
		InitFileNameList(strPathName);
	}

	return TRUE;
}

HBITMAP LoadBitmap(LPCTSTR lpszFileName, BITMAPINFO** ppBitmapInfo)
{
	if( ppBitmapInfo != NULL )
		*ppBitmapInfo = NULL;

	CFile theFile;
	if( !theFile.Open( lpszFileName, CFile::modeRead | CFile::shareDenyWrite  ) )
	{
		return NULL;
	}

	CFile* pFile = &theFile;
	BITMAPFILEHEADER fileHeader = {0};
	size_t ret = pFile->Read( &fileHeader, sizeof( BITMAPFILEHEADER ) );
	if ( ret != sizeof( BITMAPFILEHEADER ) )
	{
		return NULL;
	}

	// Check file type
	if( fileHeader.bfType != 'MB' || fileHeader.bfOffBits <  sizeof( BITMAPFILEHEADER ))
	{
		return NULL;
	}

	int nBitmapInfoSize = fileHeader.bfOffBits - sizeof(BITMAPFILEHEADER);
	if ( nBitmapInfoSize < sizeof(BITMAPINFOHEADER) )
	{
		// 无效的Bitmap文件或不支持的Bitmap文件格式
		return NULL;
	}

	BITMAPINFO*	pBitmapInfo = (BITMAPINFO*)HeapAlloc(GetProcessHeap(), 0, nBitmapInfoSize  );
	if ( pBitmapInfo == NULL )
	{
		return NULL;
	}
	
	// Read info header
	BITMAPINFOHEADER& infoHeader = pBitmapInfo->bmiHeader;
	ret = pFile->Read( pBitmapInfo, nBitmapInfoSize );
	if ( ret != nBitmapInfoSize )
	{
		return NULL;
	}
	
	int strideAbs = 0;
	if( infoHeader.biBitCount == 32 )
		strideAbs = infoHeader.biWidth * 4;
	else
		strideAbs = (infoHeader.biWidth * infoHeader.biBitCount + 31 ) >> 5 << 2; 

	//UINT sizeOfBits = infoHeader.biSizeImage;
	UINT sizeOfBits = abs( infoHeader.biHeight ) * strideAbs; // biHeight may be negative.
	//sizeOfBits = fileHeader.bfSize - fileHeader.bfOffBits;
	if ( sizeOfBits == 0 )
	{
		// empty bitmap
		return NULL;
	}

	LPVOID bitsArray = NULL;
	HBITMAP hBitmap = ::CreateDIBSection (
		NULL,
		pBitmapInfo,
		DIB_RGB_COLORS,
		&bitsArray,
		NULL,
		0);
	if ( hBitmap == NULL )
	{
		return NULL;
	}

	// Read bits to the array
	pFile->Seek(fileHeader.bfOffBits, CFile::begin);
	if( sizeOfBits != pFile->Read( bitsArray, sizeOfBits ) )
	{
		DeleteObject(hBitmap);
		hBitmap = NULL;
	}

	if( ppBitmapInfo != NULL )
		*ppBitmapInfo = pBitmapInfo;

	return hBitmap;
}

//
// 将hBitmap指代的Bitmap对象保存到lpszFileName指代的文件中。
// pBitmapInfo 含有 hBitmap的DIB信息。
//
BOOL SaveBitmap( LPCTSTR lpszFileName, HBITMAP hBitmap, BITMAPINFO*	pBitmapInfo )
{
	BOOL bRet = FALSE;
	
	CFile theFile;
	if( !theFile.Open( lpszFileName, CFile::modeCreate | CFile::modeWrite | CFile::shareDenyWrite  ) )
	{
		return NULL;
	}

	CFile* pFile = &theFile;
	BITMAPFILEHEADER fileHeader = {0};
	BITMAPINFOHEADER& infoHeader = pBitmapInfo->bmiHeader;
	int strideAbs = 0;
	if( infoHeader.biBitCount == 32 )
		strideAbs = infoHeader.biWidth * 4;
	else
		strideAbs = (infoHeader.biWidth * infoHeader.biBitCount + 31 ) >> 5 << 2; 
	UINT sizeOfBits = abs( infoHeader.biHeight ) * strideAbs;

	fileHeader.bfType = 'MB';
	fileHeader.bfOffBits = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);
	fileHeader.bfSize = fileHeader.bfOffBits + sizeOfBits;
	pFile->Write( &fileHeader, sizeof(BITMAPFILEHEADER) );
	pFile->Write( &infoHeader, sizeof(BITMAPINFOHEADER) ) ;
	
	LPVOID pBits = (LPVOID)LocalAlloc( LPTR, sizeOfBits );
	if( pBits != NULL )
	{
		// Note: hdc, the first parameter of GetDIBits, should not be NULL.
		// MSDN says: 
		// The bitmap identified by the hbmp parameter must not be selected into a device context when the application calls this function. 
		// I don't know why! 
		HDC hdcScreen = ::GetDC(NULL);
		if( 0 != GetDIBits( hdcScreen, hBitmap, 0, abs(pBitmapInfo->bmiHeader.biHeight), pBits, pBitmapInfo, DIB_RGB_COLORS ) )
		{
			pFile->Write( pBits, sizeOfBits );
			bRet = TRUE;
		}

		::ReleaseDC( NULL, hdcScreen );
		::LocalFree( (HGLOBAL) pBits );
	}
	
	return bRet;
}

BITMAPINFO*	texReadBmp( LPCTSTR lpszFilePath, HBITMAP *phBitmap )
{
	BITMAPINFO*	pBitmapInfo = NULL;
	HBITMAP	hBITMAP = LoadBitmap( lpszFilePath, &pBitmapInfo );
	if( hBITMAP == NULL || pBitmapInfo == NULL)
	{
		return NULL;
	}

	if( phBitmap ) 
	{
		*phBitmap = hBITMAP;
	}
	else
	{
		DeleteObject(hBITMAP);
	}

	return pBitmapInfo;
}

extern HBITMAP DecodeJpegFile( CFile* pInputStream );

BITMAPINFO*	texReadJpg( LPCTSTR lpszFileName, HBITMAP *phBitmap )
{
	if( phBitmap == NULL )
	{
		return NULL;
	}

	CFile theFile;
	if( !theFile.Open( lpszFileName, CFile::modeRead | CFile::shareDenyWrite  ) )
	{
		return NULL;
	}

	CFile* pFile = &theFile;
	*phBitmap = DecodeJpegFile( &theFile );

	BITMAP bitmap = {0};
	int nBytesWritten = GetObject( *phBitmap, sizeof(BITMAP), &bitmap );
	if( nBytesWritten == 0 )
	{
		return NULL;
	}

	BITMAPINFO*	pBitmapInfo = (BITMAPINFO*)HeapAlloc(GetProcessHeap(), 0, sizeof( BITMAPINFOHEADER ) );
	if ( pBitmapInfo == NULL )
	{
		return NULL;
	}

	pBitmapInfo->bmiHeader.biSize = sizeof( BITMAPINFOHEADER );
	pBitmapInfo->bmiHeader.biBitCount = bitmap.bmBitsPixel;
	pBitmapInfo->bmiHeader.biCompression = BI_RGB;
	pBitmapInfo->bmiHeader.biWidth = bitmap.bmWidth;
	pBitmapInfo->bmiHeader.biHeight = bitmap.bmHeight;
	pBitmapInfo->bmiHeader.biSizeImage = bitmap.bmWidthBytes * bitmap.bmHeight;
	pBitmapInfo->bmiHeader.biClrUsed = 0;
	pBitmapInfo->bmiHeader.biClrImportant = 0;
	pBitmapInfo->bmiHeader.biPlanes = bitmap.bmPlanes;
	pBitmapInfo->bmiHeader.biXPelsPerMeter = 0;
	pBitmapInfo->bmiHeader.biYPelsPerMeter = 0;

	return pBitmapInfo;
}

#pragma pack(4)
	struct PicHeader
	{
		int format;
		short width;
		short height;
		int blockNum;
	};

	
struct BlockDesc
{
	short imageWidth;
	short imageHeight;
	short offsetX;
	short offsetY;
	unsigned fileSize;
};

#pragma pack()

//
// 解析标志DDS文件(现在只支持DXT1,DXT3,DXT5)
// 待重构
// yanghaibo
// 2010-09-25
BITMAPINFO*	texReadDDS( LPCTSTR lpszFileName, HBITMAP *phBitmap, bool SkipHeader = false )
{
	MIX::IDDrawSurface* pidds = MIX::CreateDDrawSurface();
	if( pidds == NULL )
	{
		return NULL;
	}

	BITMAPINFO*	pBitmapInfo = NULL;
	unsigned char* pbBuffer = NULL;
	MIX::CBitmap bmp;

	CFile theFile;
	if( !theFile.Open( lpszFileName, CFile::modeRead | CFile::shareDenyWrite  ) )
	{
		goto EXIT;
	}

	CFile* pFile = &theFile;

	SIZE_T ullSize = (SIZE_T)pFile->GetLength();
	if( ullSize == 0 )
	{
		goto EXIT;
	}

	struct PicHeader header;
	if( SkipHeader )
	{
		UINT nRead = pFile->Read( &header, sizeof( PicHeader ) );
		if( (INT)nRead < sizeof( PicHeader ) )
		{
			// Memory/Disk error ?
			goto EXIT;
		}

		UINT nHeaderLength = nRead + header.blockNum * sizeof( BlockDesc );
		if(  nHeaderLength != (UINT)pFile->Seek( header.blockNum * sizeof( BlockDesc ), PFS::FSM_CUR ) )
		{
			// Memory/Disk error ?
			goto EXIT;
		}

		ullSize -= nHeaderLength;
	}

	pbBuffer = (unsigned char*)::LocalAlloc( LPTR, (SIZE_T)ullSize );
	if( pbBuffer == NULL )
	{
		goto EXIT;
	}

	SIZE_T ullSizeTotal = 0;
	do 
	{
		UINT nRead = pFile->Read( pbBuffer+ullSizeTotal, (UINT)(ullSize-ullSizeTotal) );
		if( (INT)nRead <= 0 )
		{
			// Memory/Disk error ?
			goto EXIT;
		}
		ullSizeTotal += nRead;
	}while( ullSizeTotal < ullSize );

	int nRet = pidds->LoadFromMem(  pbBuffer, ullSize );
	if ( nRet != 0 )
	{
		goto EXIT;
	}

	if( !pidds->IsValid() )
	{
		goto EXIT;
	}

	if( pidds->Decompress( bmp ) != 0 )
	{
		goto EXIT;
	}

	int nBitmapInfoSize = sizeof(BITMAPINFO);			
	pBitmapInfo = (BITMAPINFO*)HeapAlloc(GetProcessHeap(), 0, nBitmapInfoSize  );
	if ( pBitmapInfo == NULL )
	{
		goto EXIT;
	}
	
	unsigned biBitCount = 8 * bmp.GetBytesPerPixel();
	unsigned nWidth = bmp.GetWidth();
	unsigned nHeight = bmp.GetHeight(); 
	DWORD biSizeImage = bmp.GetSize();

	// Read info header
	BITMAPINFOHEADER& infoHeader = pBitmapInfo->bmiHeader;
	infoHeader.biSize = sizeof (BITMAPINFOHEADER);
	infoHeader.biPlanes = 1;
	infoHeader.biBitCount = biBitCount;
	infoHeader.biCompression = BI_RGB;
	infoHeader.biXPelsPerMeter = 0;
	infoHeader.biYPelsPerMeter = 0;
	infoHeader.biClrUsed = 0;
	infoHeader.biClrImportant = 0;
	infoHeader.biWidth =  nWidth;
	infoHeader.biHeight = - (LONG) nHeight;
	infoHeader.biSizeImage = biSizeImage;

	LPVOID bitsArray = NULL;
	HBITMAP hBitmap = ::CreateDIBSection (
		NULL,
		pBitmapInfo,
		DIB_RGB_COLORS,
		&bitsArray,
		NULL,
		0);
	if ( hBitmap == NULL )
	{
		goto EXIT0;
	}

	memcpy( bitsArray, bmp.GetPixels(), biSizeImage );
	*phBitmap = hBitmap;

	goto EXIT;

EXIT0:
	if ( pBitmapInfo != NULL )
	{
		HeapFree( GetProcessHeap(), 0, pBitmapInfo );
		pBitmapInfo = NULL;
	}

EXIT:
	if( pbBuffer != NULL )
	{
		LocalFree( pbBuffer );
		pbBuffer = NULL;
	}

	if ( pidds != NULL )
	{
		pidds->Destroy();
		pidds = NULL;
	}

	return pBitmapInfo;
}

void CChildView::OnFileSaveAs()
{
	// 暂时实现 - 另存为BMP文件
	// 

	// 替换后缀（如果文件名原本就是以.bmp结尾，保持不变）
	CString strBitmapFileName = m_strFilePath;
	if( strBitmapFileName.Right(4).CompareNoCase( _T(".bmp") ) != 0 )
	{
		int nDotPos = strBitmapFileName.ReverseFind( _T('.') );
		if( nDotPos > 0 )
		{
			strBitmapFileName.Truncate( nDotPos );
		}
		strBitmapFileName.Append( _T(".bmp") );
	}

	CFileDialog OpenDlg( FALSE, _T("bmp"), strBitmapFileName,
		OFN_OVERWRITEPROMPT,
		_T("Bmp Files(*.bmp)|*.bmp|"),
		this, 0 );
	if( IDOK != OpenDlg.DoModal() )
	{
		return ;
	}
	CString pathName = OpenDlg.GetPathName();
	if( !SaveBitmap( pathName, m_hBITMAP, &m_BitmapInfo ) )
	{
		AfxMessageBox( _T("保存失败"), MB_ICONERROR );
	}
	else
	{
		// 如果文件保存在了本地目录，那么是否应该将其加到文件列表中？
	}
}

BOOL CChildView::ShowPicture( LPCTSTR lpszFilePath )
{
	if( InnerShowPicture( lpszFilePath ) )
	{
		AfxGetApp()->AddToRecentFileList( lpszFilePath );

		InitFileNameList(lpszFilePath);	
		return TRUE;
	}

	return FALSE;
}

BOOL CChildView::InnerShowPicture( LPCTSTR lpszFilePath )
{
	if ( lpszFilePath == NULL )
	{
		return FALSE;
	}

	m_strFilePath = lpszFilePath;
	
	BITMAPINFO*		pBitmapInfo = NULL;
	HBITMAP			hBITMAP = NULL;

	if ( m_strFilePath.Right(4).CompareNoCase(_T(".bmp") ) == 0 )
	{
		pBitmapInfo = texReadBmp( m_strFilePath, &hBITMAP );
	}
	else if ( m_strFilePath.Right(4).CompareNoCase(_T(".pic") ) == 0 )
	{
		pBitmapInfo = texReadPIC (m_strFilePath, &hBITMAP);
		if ( pBitmapInfo == NULL )
		{
			pBitmapInfo = texReadDDS(m_strFilePath, &hBITMAP, true);
			if ( pBitmapInfo == NULL )
			{
				pBitmapInfo = texReadDDS(m_strFilePath, &hBITMAP, false);
			}
		}
	}
	else if ( m_strFilePath.Right(4).CompareNoCase(_T(".p08") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p09") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p10") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p11") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p12") ) == 0 
		)
	{
		// Try read DDS with picture header.
		pBitmapInfo = texReadDDS(m_strFilePath, &hBITMAP, true);
		if ( pBitmapInfo == NULL )
		{
			// Try read DDS without picture header.
			pBitmapInfo = texReadDDS(m_strFilePath, &hBITMAP, false);
		}
	}
	else if (m_strFilePath.Right(4).CompareNoCase(_T(".p00") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p01") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p02") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p03") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p04") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p06") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".p07") ) == 0 )
	{
		pBitmapInfo =  texReadP0X (m_strFilePath, &hBITMAP);
	}
	else if (m_strFilePath.Right(4).CompareNoCase(_T(".b00") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".b01") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".b02") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".b03") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".b04") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".b06") ) == 0 ||
		m_strFilePath.Right(4).CompareNoCase(_T(".b07") ) == 0 )
	{
		pBitmapInfo =  texReadB0X (m_strFilePath, &hBITMAP);
	}
	else if ( m_strFilePath.Right(4).CompareNoCase(_T(".tga") ) == 0 )
	{
		pBitmapInfo =  texReadTGA(m_strFilePath, &hBITMAP);
	}
	else if ( m_strFilePath.Right(4).CompareNoCase(_T(".ppm") ) == 0 )
	{
		pBitmapInfo =  texReadPPM(m_strFilePath, &hBITMAP);
	}
	else if ( m_strFilePath.Right(4).CompareNoCase(_T(".jpg") ) == 0 || 
		m_strFilePath.Right(5).CompareNoCase(_T(".jpeg") ) == 0 )
	{
		pBitmapInfo = texReadJpg(m_strFilePath, &hBITMAP);
	}
	else if (  m_strFilePath.Right(4).CompareNoCase(_T(".dds") ) == 0 )
	{
		pBitmapInfo = texReadDDS(m_strFilePath, &hBITMAP, false);
	}
	else if (  m_strFilePath.Right(4).CompareNoCase(_T(".png") ) == 0 )
	{
		pBitmapInfo = texReadPNG(m_strFilePath, &hBITMAP);
	}

	if (!pBitmapInfo)
	{
		GATReportError ("Could not read file", m_strFilePath);
		return FALSE;
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// 这里得到的BitmapInfo为DIB Info
	//
	//////////////////////////////////////////////////////////////////////////
	m_BitmapInfo = *pBitmapInfo;
	
	HeapFree (GetProcessHeap(), 0, pBitmapInfo);
	pBitmapInfo = NULL;

	// 检测设备兼容性，不兼容的设备要重新创建
	if ( m_memDC.GetSafeHdc() != NULL && 
		(m_BitmapInfo.bmiHeader.biBitCount > m_memDC.GetDeviceCaps(BITSPIXEL)) )
	{
		m_memDC.DeleteDC();
	}

	if ( m_memDC2.GetSafeHdc() != NULL && 
		(m_BitmapInfo.bmiHeader.biBitCount == 32 || 
		// 如果第一次显示了一张8bpp的bmp,再次显示32位的bmp,会导致设备不兼容.
		m_BitmapInfo.bmiHeader.biBitCount > m_memDC2.GetDeviceCaps(BITSPIXEL)) )
	{
		m_memDC2.DeleteDC();
	}

	if ( m_memDC.GetSafeHdc() == NULL )
	{
		m_memDC.CreateCompatibleDC(NULL);
	}

	if (hBITMAP) 
	{
		HGDIOBJ hOld = m_memDC.SelectObject(hBITMAP);
		if ( m_hBITMAP != NULL )
		{
			// Delete the old one.
			DeleteObject(hBITMAP);
		}
		m_hBITMAP = hBITMAP;
	}

	Refresh();
	return TRUE;
}

void CChildView::OnSize(UINT nType, int cx, int cy)
{
	CWnd::OnSize(nType, cx, cy);
	
	Refresh();
}

void CChildView::Refresh()
{
	if ( m_memDC.GetSafeHdc() == NULL )
	{
		return;
	}

	CRect rcClient;
	GetClientRect( &rcClient );
	rcClient.DeflateRect(0,0,0,40);
	rcClient.NormalizeRect();	

	if ( rcClient.IsRectEmpty() || rcClient.IsRectNull()  )
	{
		return;
	}
	
	CSize szClient= rcClient.Size();
	
	BITMAPINFO* pBitmapInfo = &m_BitmapInfo;
	int newWidth  = pBitmapInfo->bmiHeader.biWidth;
	int newHeight = abs( pBitmapInfo->bmiHeader.biHeight ); // biHeight may be negative.

	if ( m_dwSizeScale == 0 ) // FeatSize
	{
		// scale based on which is higher			
		if (newWidth > szClient.cx)
		{
			newHeight = (newHeight*szClient.cx)/newWidth;
			newWidth  = szClient.cx;
		}
		if (newHeight > szClient.cy)
		{
			newWidth  = (newWidth*szClient.cy)/newHeight;
			newHeight = szClient.cy;
		}
	}
	else
	{
		if ( newWidth > m_maxClipWidth || newHeight > m_maxClipHeight )
		{
			if( m_memDC2.GetSafeHdc() != NULL )
			{
				m_memDC2.DeleteDC();
			}

			HDC hdcScreen = ::GetDC(NULL);
			CDC dcScreen;
			dcScreen.Attach(hdcScreen);
			m_memDC2.CreateCompatibleDC(NULL);
			// Create a compatible bitmap for hdcScreen. 
			HBITMAP hbmCompatible = CreateCompatibleBitmap(
				m_memDC.GetSafeHdc(),
				m_maxClipWidth = max(newWidth , m_maxClipWidth), 
				m_maxClipHeight = max(newHeight, m_maxClipHeight) ); 

			m_memDC2.SelectObject( hbmCompatible );
		}
	}

	if( m_memDC2.GetSafeHdc() == NULL )
	{
		HDC hdcScreen = ::GetDC(NULL);
		m_memDC2.CreateCompatibleDC(NULL);
		// Create a compatible bitmap for hdcScreen. 
		HBITMAP hbmCompatible = CreateCompatibleBitmap(
			m_memDC.GetSafeHdc(),
			// 特别说明: 尽管我们以为这里同样可以使用 hdcScreen，但是，区别在于，hdcScreen是显示设备相关的DC，
			// 当用户将桌面更改为16bit显示时，我们创建的兼容Bitmap将丢失Alpha通道。
			// 所不同的是，我们的m_memDC是DIB DC，它是设备无关的，因此，这里不会丢失Alpha通道。
			// m_memDC is a DIB memDC, we can use it as the dc. 
			// see msdn remarks for CreateCompatibleBitmap
			m_maxClipWidth = GetDeviceCaps(hdcScreen, HORZRES), 
			m_maxClipHeight = GetDeviceCaps(hdcScreen, VERTRES)
			); 
		m_memDC2.SelectObject( hbmCompatible );
		::ReleaseDC(NULL, hdcScreen);
	}

	// BLACKONWHITE is default mode?
	// HALFTONE better effect but less performance. 
	// HALFTONE 对于alpha渲染模式与COLORONCOLOR有很大区别
	//
	// see http://baike.baidu.com/view/1080401.htm
	// m_memDC2.SetStretchBltMode( HALFTONE ); 
	//
	m_memDC2.SetStretchBltMode( m_stretchBltMode ); 
	m_memDC2.SetBrushOrg( 0, 0 );
	m_memDC2.StretchBlt(
		0,0, 
		newWidth, newHeight, 
		&m_memDC, 
		0, 0, pBitmapInfo->bmiHeader.biWidth, abs( pBitmapInfo->bmiHeader.biHeight ), // biHeight may be negative.
		SRCCOPY);
	
	// Save the bounding box of the picture
	m_rcPicture.left = (szClient.cx-newWidth)/2;
	m_rcPicture.top = (szClient.cy-newHeight)/2;
	m_rcPicture.right = m_rcPicture.left + newWidth + 2;
	m_rcPicture.bottom = m_rcPicture.top + newHeight + 2;
	
	m_rcPicture.OffsetRect(0, 30);

	InvalidateRect( NULL );
	UpdateWindow();
}

void CChildView::DrawPicture(CDC* pDC)
{
	if ( m_memDC2.GetSafeHdc() )
	{
		int nbppMemory = m_memDC.GetDeviceCaps( BITSPIXEL );
		int nBitsPerPixel = m_memDC2.GetDeviceCaps( BITSPIXEL );
		int originalMode = pDC->SetBkMode( TRANSPARENT );
		CString strPicInfo;
		strPicInfo.Format( _T("%s (%d x %d). image bpp:%d. memory bpp:%d display bpp:%d"),
			m_strFilePath, m_BitmapInfo.bmiHeader.biWidth,
			abs(m_BitmapInfo.bmiHeader.biHeight), m_BitmapInfo.bmiHeader.biBitCount, nbppMemory, nBitsPerPixel);
		// biHeight may be negative.

		pDC->SelectStockObject( DEFAULT_GUI_FONT );
		pDC->TextOut( 10, 10, strPicInfo );
		pDC->SetBkMode( originalMode );

		// The border
		HGDIOBJ hObj = pDC->SelectStockObject(NULL_BRUSH);
		pDC->Rectangle(m_rcPicture.left, m_rcPicture.top, 
			m_rcPicture.right, m_rcPicture.bottom);
		hObj = pDC->SelectObject(hObj);

		if ( m_bAlphaBlendMode 
			&& m_BitmapInfo.bmiHeader.biBitCount == 32 // 不含alpha通道的图像不使用AlphaBlend
			)
		{
			BLENDFUNCTION blend;
			blend.AlphaFormat = AC_SRC_ALPHA;
			blend.BlendFlags = 0;
			blend.BlendOp = AC_SRC_OVER;
			blend.SourceConstantAlpha = 255;
			pDC->AlphaBlend(
				m_rcPicture.left+1, m_rcPicture.top+1, 
				m_rcPicture.Width()-2, m_rcPicture.Height()-2,
				&m_memDC2, 0, 0, 
				m_rcPicture.Width()-2, m_rcPicture.Height()-2,
				blend );
		}
		else
		{
			pDC->BitBlt(
				m_rcPicture.left+1, m_rcPicture.top+1, 
				m_rcPicture.Width()-2, m_rcPicture.Height()-2,
				&m_memDC2, 0, 0, 
				SRCCOPY );
		}
	}
}

BOOL CChildView::OnEraseBkgnd(CDC* pDC)
{
	if ( m_bAlphaBlendMode )
	{
		Default();
	}
	else
	{
		// 当需要移除背景的时候， 只填充必要的位置(图片覆盖区之外的位置)
		CRect rcClient;
		GetClientRect( &rcClient );

		HRGN hrgnSrc1 = CreateRectRgn(rcClient.left, rcClient.top, rcClient.right, rcClient.bottom );
		HRGN hrgnSrc2 = CreateRectRgn(m_rcPicture.left, m_rcPicture.top, m_rcPicture.right, m_rcPicture.bottom );
		HRGN hrgnDest = CreateRectRgn(rcClient.left, rcClient.top, rcClient.right, rcClient.bottom );

		int iRet = CombineRgn( hrgnDest, hrgnSrc1,  hrgnSrc2,  RGN_DIFF); 
		if( iRet == ERROR )
		{
			::DeleteObject( hrgnDest );
			::DeleteObject( hrgnSrc1 );
			::DeleteObject( hrgnSrc2 );

			return CWnd::OnEraseBkgnd(pDC);
		}

		::FillRgn( pDC->GetSafeHdc(), hrgnDest, m_hbrBackground );

		::DeleteObject( hrgnDest );
		::DeleteObject( hrgnSrc1 );
		::DeleteObject( hrgnSrc2 );
	}
	return TRUE;
}

void CChildView::InitFileNameList(CString strPathName)
{
	int iPos = strPathName.ReverseFind( _T('\\') );
	CString strFolderName = strPathName.Left( iPos + 1 );
	CFileFind finder;
	m_fileNameList.RemoveAll();
	LPCTSTR pstrNames[] = 
	{
		L"*.bmp",
		L"*.jpg",
		L"*.jpeg",
		L"*.b0?",
		L"*.p0?",
		L"*.pic",
		L"*.dds",
		L"*.ppm",
		L"*.tga",
		L"*.png",
	};

	INT iIndex = 0;
	INT iIndexCurrent = 0;

	for ( int i = 0; i < _countof(pstrNames); i ++ )
	{
		CString strMask = strFolderName + pstrNames[i];
		BOOL bFound = finder.FindFile(strMask);
		if( bFound )
		{
			while( bFound )
			{
				bFound = finder.FindNextFile();
				CString strFilePath = finder.GetFilePath();
				if( ::PathIsDirectory( strFilePath ) )
				{
					continue;
				}

				if ( strFilePath == strPathName )
				{
					iIndexCurrent = iIndex;
				}
				iIndex ++;
				m_fileNameList.AddTail(strFilePath);
			} 
		}

		finder.Close();
	}

	m_posInFileList = m_fileNameList.FindIndex(iIndexCurrent);
}

void CChildView::OnFileOpen()
{
	CFileDialog OpenDlg( TRUE, NULL, NULL, OFN_EXPLORER | OFN_FILEMUSTEXIST, 
		_T("All Supported Files(*.bmp,*.jpg,*.pic,*.tga,*.ppm;*.dds,*.png)|*.bmp;*.jpg;*.jpeg;*.pic;*.p0?;*.p1?;*.b0？;*.b1?;*.tga;*.ppm;*.dds;*.png|") 
		_T("Bmp Files(*.bmp)|*.bmp|")
		_T("Jpg Files(*.jpg,*.jpeg)|*.jpg;*.jpeg|")
		_T("Pic Files(*.pic)|*.pic;*.p0?;*.p1?;*.b0?;*.b1?|")
		_T("P0x/p1x Files(*.p0x,*.p1x)|*.p0?;*.p1?|")
		_T("b0x/b1x Files(*.b0x,*.b1x)|*.b0?;*.b1*|")
		_T("dds Files(*.dds)|*.dds|")
		_T("Ppm Files(*.ppm)|*.ppm|")
		_T("Tga Files(*.tga)|*.tga|")
		_T("Png Files(*.png)|*.png|")
		_T("All Files(*.*)|*.*||"),
		this, 0 );

	if( IDOK != OpenDlg.DoModal() )
	{
		return ;
	}

	CString pathName = OpenDlg.GetPathName();

	ShowPicture( pathName );
}

void CChildView::OnPicAlphaBlend()
{
	m_bAlphaBlendMode = !m_bAlphaBlendMode;
	Refresh();
}

void CChildView::OnUpdatePicAlphaBlend(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_bAlphaBlendMode );
}

BOOL CChildView::OnPicStretchBltMode(UINT nID)
{
	m_stretchBltMode = nID-ID_PIC_STRETCH_BLACKONWHITE+1;
	Refresh();
	return TRUE;
}

void CChildView::OnUpdatePicStretchBltMode(CCmdUI *pCmdUI)
{
	if( pCmdUI->m_nID-ID_PIC_STRETCH_BLACKONWHITE+1 == m_stretchBltMode )
		pCmdUI->SetRadio(TRUE);
	else
		pCmdUI->SetRadio(FALSE);
}
