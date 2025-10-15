#include "StdAfx.h"
#include "BinaryFileViewer.h"
#include <sstream>

CBinaryFileViewer::CBinaryFileViewer(void)
{
}

CBinaryFileViewer::~CBinaryFileViewer(void)
{
}

static const wchar_t TABLE[] = L"0123456789ABCDEF";
static void FormatBinaryText( const void* pData, size_t len, std::wostringstream& woss)
{
	const BYTE* pByte = (const BYTE*)pData;
	size_t col = 0;
	std::wstring temp;
	temp.resize( 16);

	for( size_t i=0; i<len; i++)
	{
		BYTE b = *pByte++;
		woss << TABLE[b>>4] << TABLE[b&0x0F] << L" ";
		
		if( b < 0x20)
		{
			b = L'.';
		}
		else if( b >= 0x80)
		{
			b = L'*';
		}
		temp[col++] = b;

		if( col == 16)
		{
			col = 0;
			woss << L"; " << temp << L"\r\n";
		}
	}
}

bool CBinaryFileViewer::SetData( const void* pData, size_t len )
{
	std::wostringstream woss;
	FormatBinaryText( pData, len, woss);
	m_wndEditPanel.SetSel( 0, -1);
	m_wndEditPanel.ReplaceSel( woss.str().c_str());

	return true;
}