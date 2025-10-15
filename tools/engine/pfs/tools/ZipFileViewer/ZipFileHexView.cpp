// ZipFileHexView.cpp : implementation file
//

#include "stdafx.h"
#include "ZipFileViewer.h"
#include "ZipFileHexView.h"


// CZipFileHexView

IMPLEMENT_DYNCREATE(CZipFileHexView, CEditView)

CZipFileHexView::CZipFileHexView()
{

}

CZipFileHexView::~CZipFileHexView()
{
}


BEGIN_MESSAGE_MAP(CZipFileHexView, CEditView)
	ON_WM_CREATE()
END_MESSAGE_MAP()


// CZipFileHexView drawing


static const wchar_t TABLE[] = L"0123456789ABCDEF";

UINT BytesToHexString( const BYTE* pBytes, UINT nBytesCount, LPWSTR pHexStringBuffer )
{
	UINT k = 0;
	for ( UINT i=0; i < nBytesCount;)
	{
		for ( UINT j=0; j < 16 && i < nBytesCount; j ++ )
		{
			pHexStringBuffer[k++] = TABLE[pBytes[i]>>4]; // 高4位
			pHexStringBuffer[k++] = TABLE[pBytes[i]&0xF];// 低4位
			pHexStringBuffer[k++] = L' ';				 // 空格
			i++;
		}

		pHexStringBuffer[k-1] = L'\r';					// 回车
		pHexStringBuffer[k++] = L'\n';					// 换行	
	}

	pHexStringBuffer[k++] = L'\0';						// 字符串结尾

	return k;
}

UINT BytesToHexString_0( const BYTE* pBytes, UINT nBytesCount, LPWSTR pHexStringBuffer )
{
	UINT k = 0;
	for ( UINT i=0; i < nBytesCount;)
	{
		for ( UINT j=0; j < 16 && i < nBytesCount; j ++ )
		{
			pHexStringBuffer[k++] = TABLE[pBytes[i]>>4]; // 高4位
			pHexStringBuffer[k++] = TABLE[pBytes[i]&0xF];// 低4位
			pHexStringBuffer[k++] = L' ';				 // 空格
			i++;
		}

		pHexStringBuffer[k-1] = L'\r';					// 回车
		pHexStringBuffer[k++] = L'\n';					// 换行	
	}

	return k;
}

void CZipFileHexView::OnInitialUpdate()
{
	CEditView::OnInitialUpdate();

	const CString& strFilePath = GetDocument()->GetPathName();
	if( strFilePath.IsEmpty() )
	{
		return;
	}

	CFile fileSrc;
	if( !fileSrc.Open( strFilePath.GetString(), 
		CFile::modeRead|CFile::shareDenyWrite|CFile::typeBinary ) )
	{
		return;
	}

	
	GetEditCtrl().Clear();

	ULONGLONG ullSize = fileSrc.GetLength();

	UINT nBufferSize = min( 10240, (UINT)ullSize );
	BYTE* pBuffer = NULL;
	if( ullSize > 0 )
	{
		pBuffer = new BYTE[nBufferSize];
		if(pBuffer==NULL)
		{
			return;
		}
	}

	//UINT nHexBufferSize = nBufferSize * 4 * 2;
	
	UINT nHexBufferSize = (UINT)(ullSize * 3 + ullSize / 16) * 2 + 2;

	//WCHAR* pHexBuffer = new WCHAR[nHexBufferSize];
	//if(pHexBuffer == NULL)
	//{
	//	delete[] pBuffer;
	//	return;
	//}

	//pHexBuffer[0] = 0;
	//pHexBuffer[1] = 0;

	//ULONGLONG ullRemain = ullSize;
	//do 
	//{
	//	UINT nRead = fileSrc.Read( pBuffer, nBufferSize );
	//	if ( nRead > 0 )
	//	{
	//		ullRemain -= nRead;
	//		BytesToHexString( pBuffer, nRead, pHexBuffer );
	//		GetEditCtrl().SetSel( -1, -1, TRUE );
	//		GetEditCtrl().ReplaceSel( pHexBuffer, FALSE );
	//	}
	//} while( ullRemain > 0 );

	//delete[] pBuffer;
	//delete[] pHexBuffer;

	//GetEditCtrl().SetModify(FALSE);
	//GetDocument()->SetModifiedFlag(FALSE);
	//GetEditCtrl().SetSel(0,0, FALSE);

	LPWSTR pHexBuffer = (LPWSTR)::LocalAlloc(LPTR, (UINT)nHexBufferSize);
	if( pHexBuffer == NULL )
	{
		return;
	}

	HLOCAL hOld = GetEditCtrl().GetHandle();
	::LocalFree( hOld );

	if( ullSize > 0 )
	{
		LPWSTR pBlock = pHexBuffer;
		ULONGLONG ullRemain = ullSize ;
		int totalWritten = 0;
		do
		{
			UINT nBufferSize = min ( 10240, (UINT)ullRemain );
			UINT nRead = fileSrc.Read( pBuffer, nBufferSize );
			if ( nRead > 0 )
			{
				ullRemain -= nRead;

				int k = BytesToHexString_0( pBuffer, nRead, pBlock );

				pBlock += k;
				totalWritten += k * 2;
			}
		}while( ullRemain > 0 ) ;

		ASSERT( totalWritten == nHexBufferSize );
		pBlock[ -2 ] = 0;
		pBlock[ -1 ] = 0;
	}

	if( pBuffer != NULL )
		delete[] pBuffer;

	GetEditCtrl().SetHandle( (HLOCAL) pHexBuffer );
	
	GetDocument()->SetModifiedFlag(FALSE);
	GetEditCtrl().SetSel(0,0, FALSE);
}

// CZipFileHexView message handlers

int CZipFileHexView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CEditView::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	// Set GUI font
	HGDIOBJ hFont = GetStockObject( DEFAULT_GUI_FONT );
	::SendMessage(m_hWnd, WM_SETFONT, (WPARAM)hFont, FALSE );
	GetEditCtrl().SetReadOnly();
	GetEditCtrl().SetLimitText(-1);
	GetEditCtrl().SetMargins(10, 10);	

	return 0;
}
