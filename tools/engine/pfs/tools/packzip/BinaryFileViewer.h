#pragma once
#include <afxrich.h>
#include "FileViewer.h"
#include "../../pfs2.h"

struct StreamContext
{
	LPBYTE	pbBuffer;
	LONG	cbRemaining;
	LONG	cbOffset;
};

static DWORD CALLBACK BinaryStreamCallback(DWORD dwCookie, LPBYTE pbBuff, LONG cb, LONG *pcb)
{
	StreamContext* data = (StreamContext*) dwCookie;
	if( data->cbRemaining >= cb)
	{
		memcpy( pbBuff, data->pbBuffer+data->cbOffset, cb);
		data->cbOffset += cb;
		data->cbRemaining -= cb;
		*pcb = cb;
	}
	else
	{
		*pcb = data->cbRemaining;
		memcpy( pbBuff, data->pbBuffer+data->cbOffset, *pcb);
		data->cbRemaining = 0;
		data->cbOffset += *pcb;
	}
	return 0;
}

static const size_t size = 64*1024;

class CBinaryFileViewer : public CFileViewer
{
	CRichEditCtrl		m_edit;
	PFS::CDataBlock		out;
public:
	virtual bool Create( CWnd* parent)
	{
		CFileViewer::Create( parent);
		out.Reserve( size);
		CRect rc;
		parent->GetClientRect( &rc);
		if( !m_edit.Create( WS_CHILD|ES_MULTILINE|ES_READONLY|WS_VSCROLL, rc, parent, 0) )
		{
			return false;
		}

		CHARFORMAT charformat;
		charformat.cbSize = sizeof(CHARFORMAT); 
		m_edit.GetDefaultCharFormat(charformat);
		charformat.dwMask |= CFM_COLOR;
		charformat.dwMask |= CFM_SIZE;
		charformat.dwMask |= CFM_ITALIC;
		charformat.dwEffects &= ~CFE_AUTOCOLOR;
		charformat.dwEffects &= ~CFE_BOLD;
		charformat.dwEffects &=  ~CFE_ITALIC;
		charformat.yHeight = 180;
		wcscpy_s(charformat.szFaceName, _T("simfang"));
		m_edit.SetDefaultCharFormat(charformat);

		return true;
	}

	virtual bool SetTreeItem( CZipTreeItem& item)
	{
		PFS::CFile file;
		if( file.Open( item.GetMount(), PFS::FM_EXCL, PFS::FA_RDONLY))
		{
			size_t res = file.Read( out.GetData(), size-sizeof(int));
			out.Resize( res);
			int end = 0;
			out.Append( &end, sizeof( int));

			std::wstring	wstrRet;
			LPCTSTR data = (LPCTSTR)out.GetData();
			if( res <= size-sizeof(int))
			{
				if( *data == 0xFEFF)
				{
					data++;
					m_edit.SetWindowText( data);
					return true;
				}
				else
				{
					int nBufSize = MultiByteToWideChar( CP_ACP, 0, (LPCSTR)data, -1, NULL, 0) - 1;
					if( nBufSize >= (int)res)
					{
						wstrRet.resize( nBufSize);
						MultiByteToWideChar( CP_ACP, 0, (LPCSTR)data, -1, const_cast<LPWSTR>(wstrRet.c_str()), nBufSize);
						m_edit.SetWindowText( wstrRet.c_str());
						return true;
					}
				}
			}
			
			StreamContext sc;
			sc.pbBuffer = (LPBYTE)out.GetData();
			sc.cbOffset = 0;
			sc.cbRemaining = (LONG)out.GetSize();

			EDITSTREAM es;
			es.dwCookie = (DWORD)( &sc);
			es.pfnCallback = BinaryStreamCallback; 
			m_edit.StreamIn( SF_TEXT, es);
		}
		return true;
	}
	virtual void Show( bool bShow)
	{
		m_edit.ShowWindow( bShow ? SW_SHOW : SW_HIDE);
	}
	virtual void Move()
	{
		CRect rc;
		m_parent->GetClientRect( &rc);
		m_edit.MoveWindow( rc);
	}
};