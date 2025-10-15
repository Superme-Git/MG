// ZipFileViewerView.cpp : CZipFileViewerView 类的实现
//

#include "stdafx.h"
#include "ZipFileViewer.h"

#include "ZipFileViewerDoc.h"
#include "ZipFileViewerView.h"
#include "../../contrib/zlib-1.2.3/zlib.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CZipFileViewerView

IMPLEMENT_DYNCREATE(CZipFileViewerView, CEditView)

BEGIN_MESSAGE_MAP(CZipFileViewerView, CEditView)
	ON_WM_CREATE()
END_MESSAGE_MAP()

// CZipFileViewerView 构造/析构

CZipFileViewerView::CZipFileViewerView()
{
	// TODO: 在此处添加构造代码
}

CZipFileViewerView::~CZipFileViewerView()
{
}

BOOL CZipFileViewerView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	BOOL bPreCreated = CEditView::PreCreateWindow(cs);
	cs.style &= ~(ES_AUTOHSCROLL|WS_HSCROLL);	// 启用换行

	return bPreCreated;
}

int UnzipFile(CFile& source, CFile& destination )
{
	ULONGLONG total = source.GetLength();
	ULONGLONG remaining = total;
	UINT BLOCK = 1024*100;
	UINT BLOCK_UNZIPPED = BLOCK*8;

	char* buffer = (char*)malloc( BLOCK );
	char* buffer_unzipped = (char*)malloc( BLOCK_UNZIPPED);

	z_stream m_strm;
	m_strm.zalloc = Z_NULL;
	m_strm.zfree = Z_NULL; 
	m_strm.opaque = Z_NULL;
	m_strm.avail_in = 0;
	m_strm.next_in = Z_NULL;

	int ret = inflateInit2( &m_strm, -MAX_WBITS);
	if( ret != Z_OK )
	{
		return ret;
	}

	ULONGLONG current = 0;
	while ( remaining > 0 )
	{
		current = source.Read(buffer, BLOCK );
		if ( current == 0 )
		{
			ASSERT( current != 0 );
			break;
		}
		remaining -= current;
		bool bFinish = remaining==0;

		m_strm.avail_in = (uInt)current;
		m_strm.next_in = (Bytef*)buffer;

		// normally this loop will do only once
		// Note: we should check the avail_in ==0 but not the avail_out !=0 as the quit condition.
		// because in some cases, the avail_out == 0 and also the avail_in == 0,
		// in this case, inflate will failed with -5.
		while( m_strm.avail_in ) 
		{
			m_strm.avail_out = BLOCK_UNZIPPED;
			m_strm.next_out = (Bytef*)buffer_unzipped;

			ret = inflate( &m_strm, Z_SYNC_FLUSH);
			if( ret < Z_OK)
				break;

			UINT have = BLOCK_UNZIPPED - m_strm.avail_out;
			destination.Write( buffer_unzipped, have );

			// Note: in some cases, when ret == Z_STREAM_END, 
			// but avail_in is still not 0, and if we don't break here,
			// it will cause a dead-loop.
			if ( ret == Z_STREAM_END )
				break;
		} 
	}

	free( buffer );
	free( buffer_unzipped );

	inflateEnd( &m_strm);

	return ret;
}

// CZipFileViewerView 消息处理程序

void CZipFileViewerView::OnInitialUpdate()
{
	CEditView::OnInitialUpdate();
	const CString& strFilePath = GetDocument()->GetPathName();
	if( strFilePath.IsEmpty() )
	{
		return;
	}

	// TODO:
	CString strFilePathTemp = strFilePath + _T(".zip.txt");
	CFile fileSrc;
	if( !fileSrc.Open( strFilePath.GetString(), 
		CFile::modeRead|CFile::shareDenyWrite|CFile::typeBinary ) )
	{
		return;
	}
	
	ULONGLONG ullSizeSrc = fileSrc.GetLength();
	if( ullSizeSrc == 0 )
	{
		HLOCAL hOld = GetEditCtrl().GetHandle();
		::LocalFree( hOld );
		BYTE* pBuffer = (BYTE*)::LocalAlloc(LPTR, (UINT)2);
		GetEditCtrl().SetHandle( (HLOCAL) pBuffer );
		return;
	}

	CFile fileDest;
	if( !fileDest.Open( strFilePathTemp .GetString(),
		CFile::modeCreate|CFile::modeReadWrite|CFile::shareDenyWrite|CFile::typeBinary  ) )
	{
		HLOCAL hOld = GetEditCtrl().GetHandle();
		::LocalFree( hOld );
		BYTE* pBuffer = (BYTE*)::LocalAlloc(LPTR, (UINT)2);
		GetEditCtrl().SetHandle( (HLOCAL) pBuffer );

		return;
	}

	if( UnzipFile(fileSrc, fileDest ) < 0 )
	{
		HLOCAL hOld = GetEditCtrl().GetHandle();
		::LocalFree( hOld );
		BYTE* pBuffer = (BYTE*)::LocalAlloc(LPTR, (UINT)2);
		GetEditCtrl().SetHandle( (HLOCAL) pBuffer );

		return;
	}

	fileDest.Flush();
	fileDest.SeekToBegin();

	ULONGLONG ullSize = fileDest.GetLength();
	if( ullSize == 0 )
	{
		HLOCAL hOld = GetEditCtrl().GetHandle();
		::LocalFree( hOld );
		BYTE* pBuffer = (BYTE*)::LocalAlloc(LPTR, (UINT)2);
		GetEditCtrl().SetHandle( (HLOCAL) pBuffer );

		return;
	}

	BYTE* pBuffer = (BYTE*)::LocalAlloc(LPTR, (UINT)ullSize+2);
	// UINT nBufferSize = (UINT)ullSize;

	//UINT nBufferSize = min( 1024 * 1024, (UINT)ullSize );
	//BYTE* pBuffer = new BYTE[nBufferSize];
	if(pBuffer==NULL)
	{
		HLOCAL hOld = GetEditCtrl().GetHandle();
		::LocalFree( hOld );
		BYTE* pBuffer = (BYTE*)::LocalAlloc(LPTR, (UINT)2);
		GetEditCtrl().SetHandle( (HLOCAL) pBuffer );

		return;
	}

	HLOCAL hOld = GetEditCtrl().GetHandle();
	::LocalFree( hOld );

	BYTE* pBlock = pBuffer;
	ULONGLONG ullRemain = ullSize ;
	do 
	{
		UINT nBlockSize = min ( 65536, (UINT)ullRemain );
		UINT nRead = fileDest.Read( pBlock, nBlockSize );
		if ( nRead > 0 )
		{
			ullRemain -= nRead;
			pBlock += nRead;
		}
	} while( ullRemain > 0 );
	pBuffer[ ullSize ] = 0;
	pBuffer[ ullSize + 1] = 0;

	GetEditCtrl().SetHandle( (HLOCAL) pBuffer );

	//pBuffer[0] = 0;
	//pBuffer[1] = 0;
	//GetEditCtrl().Clear();

	//ULONGLONG ullRemain = ullSize ;
	//do 
	//{
	//	UINT nRead = fileDest.Read( pBuffer, nBufferSize-2 );
	//	if ( nRead > 0 )
	//	{
	//		ullRemain -= nRead;
	//		pBuffer[nRead] = 0;
	//		pBuffer[nRead+1] = 0;
	//		GetEditCtrl().SetSel( -1, -1, TRUE );
	//		GetEditCtrl().ReplaceSel( (LPCTSTR)pBuffer, FALSE );
	//	}
	//} while( ullRemain > 0 );

	//delete[] pBuffer;

	// GetEditCtrl().SetModify(FALSE);
	GetDocument()->SetModifiedFlag(FALSE);
	GetEditCtrl().SetSel(0,0, FALSE);
}

int CZipFileViewerView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CEditView::OnCreate(lpCreateStruct) == -1)
		return -1;

	// Set GUI font
	HGDIOBJ hFont = GetStockObject( DEFAULT_GUI_FONT );
	::SendMessage(m_hWnd, WM_SETFONT, (WPARAM)hFont, FALSE );
	
	// DS_LOCALEDIT 

	GetEditCtrl().SetLimitText(-1);
	GetEditCtrl().SetReadOnly();

	return 0;
}
