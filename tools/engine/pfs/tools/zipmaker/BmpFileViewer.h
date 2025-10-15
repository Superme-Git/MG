#pragma once
#include "FileViewer.h"
#include "GlobalInfo.h"
#include "../../pfs2.h"
#include "../../pfsx/jpeg/JpgTranslator.h"
#include "../../pfsx/targa/TargaTranslator.h"
#include "../../pfsx/pic/PicTranslator.h"

#define IDR_SETISALPHA	12000
#define IDR_SETBKCOLOR	12001

class CShowBmpWnd : public CWnd
{
	HBITMAP			m_hBmp;
	CDC				m_dcMem;
	bool			m_bAlpha;
	HBRUSH			m_hbrBackground;
	CRect			m_rcBmp;
public:
	CShowBmpWnd() : m_hBmp( NULL), m_bAlpha( false), m_hbrBackground( NULL) {}
	virtual ~CShowBmpWnd() 
	{ 
		if( m_hBmp)
			::DeleteObject( m_hBmp);
		if( m_hbrBackground)
			::DeleteObject( m_hbrBackground);
	}

protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnPaint()
	{
		CPaintDC dc( this); // device context for painting
		dc.SetBkMode( TRANSPARENT);
		if( NULL == m_hBmp)
		{
			dc.SetTextColor( RGB(255,0,0));
			dc.TextOutW( 0, 0, L"Image Format Error!", 19);
		}
		else
		{
			BITMAP info;
			memset( &info, 0, sizeof( BITMAP));
			GetObject( m_hBmp, sizeof( BITMAP), &info);
			
			CString str;
			str.Format( L"Size : %d x %d", info.bmWidth, info.bmHeight);
			dc.TextOutW( 0, 0, str);

			if( m_bAlpha)
			{
				CRect temp = m_rcBmp;
				temp.InflateRect( 1, 1);
				dc.Rectangle( &temp);

				BLENDFUNCTION blend;
				blend.AlphaFormat = AC_SRC_ALPHA;
				blend.BlendFlags = 0;
				blend.BlendOp = AC_SRC_OVER;
				blend.SourceConstantAlpha = 255;
				dc.AlphaBlend( m_rcBmp.left, m_rcBmp.top, m_rcBmp.Width(), m_rcBmp.Height(), 
					&m_dcMem, 0, 0, info.bmWidth, info.bmHeight, blend);
			}
			else
			{
				dc.BitBlt( m_rcBmp.left, m_rcBmp.top, m_rcBmp.Width(), m_rcBmp.Height(), 
					&m_dcMem, 0, 0, SRCCOPY);
			}
		}
	}
	void SetBmpFile( const void* buffer)
	{
		CClientDC dc( this);
		if( m_dcMem.GetSafeHdc() == NULL)
			m_dcMem.CreateCompatibleDC( &dc);
		if( m_hBmp)
			::DeleteObject( m_hBmp);
		m_hBmp = GetDIBitmap( dc, reinterpret_cast<const char*>( buffer));
		m_dcMem.SelectObject( m_hBmp);
		CalcBmpRect();
		InvalidateRect( NULL);
	}
	void CalcBmpRect()
	{
		if( m_hBmp)
		{
			BITMAP info;
			memset( &info, 0, sizeof( BITMAP));
			GetObject( m_hBmp, sizeof( BITMAP), &info);
			int nWidth = info.bmWidth;
			int nHeight = info.bmHeight;

			CRect rc;
			GetClientRect( &rc);
			rc.top += 16;
			if( (nWidth > rc.Width()) || (nHeight > rc.Height()))
			{
				float fX = float( nWidth) / rc.Width();
				float fY = float( nHeight) / rc.Height();
				if( fX > fY)
					fY = fX;
				nWidth = int( nWidth / fY);
				nHeight = int( nHeight / fY);
			}
			m_rcBmp.left = rc.left + (rc.Width() - nWidth) / 2;
			m_rcBmp.top = rc.top + (rc.Height() - nHeight) / 2;
			m_rcBmp.right = m_rcBmp.left + nWidth;
			m_rcBmp.bottom = m_rcBmp.top + nHeight;
		}
		else
		{
			m_rcBmp.SetRectEmpty();
		}
	}
	HBITMAP GetDIBitmap( CDC& dc, const char* buffer)
	{
		if( !buffer)
			return NULL;
		BITMAPFILEHEADER  *pHeader = (BITMAPFILEHEADER*)buffer;
		BITMAPINFOHEADER* pInfo = (LPBITMAPINFOHEADER)(pHeader+1);
		return ::CreateDIBitmap( dc.GetSafeHdc(), 
			pInfo, 
			CBM_INIT, 
			const_cast<char*>( buffer+pHeader->bfOffBits), 
			reinterpret_cast<const BITMAPINFO*>( pInfo),
			DIB_RGB_COLORS);

	}
	void BltToDC( CDC& dc)
	{
		//CRect rc;
		//GetClientRect( &rc);
		//
		//rc.top += 16;
		//BITMAPFILEHEADER  *pHeader = (BITMAPFILEHEADER*)m_buffer;
		//BITMAPINFOHEADER* pInfo = (LPBITMAPINFOHEADER)(pHeader+1);
		//int nWidth = pInfo->biWidth;
		//int nHeight = pInfo->biHeight;
		//if( (pInfo->biWidth > rc.Width()) || (pInfo->biHeight > rc.Height()))
		//{
		//	float fX = float( pInfo->biWidth) / rc.Width();
		//	float fY = float( pInfo->biHeight) / rc.Height();
		//	if( fX > fY)
		//		fY = fX;
		//	nWidth = int( nWidth / fY);
		//	nHeight = int( nHeight / fY);
		//}
		//int nLeft = rc.left + (rc.Width() - nWidth) / 2;
		//int	nTop = rc.top + (rc.Height() - nHeight) / 2;
		//
		//int i = SetStretchBltMode( dc.GetSafeHdc(), COLORONCOLOR);
		//if( 0 == StretchDIBits( dc.GetSafeHdc(), nLeft, nTop, nWidth, nHeight, 0, 0, 
		//		pInfo->biWidth, pInfo->biHeight, 
		//		reinterpret_cast<LPVOID>( m_buffer+pHeader->bfOffBits), 
		//		reinterpret_cast<const BITMAPINFO*>( pInfo),
		//		DIB_RGB_COLORS, SRCCOPY))
		//{
		//	dc.SetTextColor( RGB(255,0,0));
		//	dc.TextOutW( 0, 0, L"Image Format Error!", 19);
		//}
		//else
		//{
		//	CString str;
		//	str.Format( L"Size : %d x %d", pInfo->biWidth, pInfo->biHeight);
		//	dc.TextOutW( 0, 0, str);
		//}
		//SetStretchBltMode( dc.GetSafeHdc(), i);
	}
	afx_msg BOOL OnEraseBkgnd( CDC* pDC)
	{
		CRect rc;
		GetClientRect( &rc);
		::FillRect( pDC->GetSafeHdc(), &rc, m_hbrBackground);
		return TRUE;
	}
	afx_msg void OnRButtonDown( UINT nFlags, CPoint point)
	{
		ClientToScreen( &point);
		CMenu menu;
		menu.CreatePopupMenu();
		menu.AppendMenuW( MF_STRING, IDR_SETISALPHA, L"AlphaÏÔÊ¾");
		menu.CheckMenuItem( IDR_SETISALPHA, m_bAlpha ? MF_CHECKED : MF_UNCHECKED);
		menu.AppendMenuW( MF_STRING, IDR_SETBKCOLOR, L"ÉèÖÃ±³¾°É«");
		menu.TrackPopupMenu( TPM_LEFTALIGN, point.x, point.y, this);
	}
	virtual BOOL OnCommand( WPARAM wParam, LPARAM lParam)
	{
		switch( wParam)
		{
		case IDR_SETISALPHA:
			m_bAlpha = !m_bAlpha;
			InvalidateRect( NULL);
			break;
		case IDR_SETBKCOLOR:
			OnSetBkColor();
			break;
		}
		return CWnd::OnCommand( wParam, lParam);
	}
	void OnSetBkColor()
	{
		CColorDialog dlg( 0, CC_FULLOPEN);
		if( IDOK == dlg.DoModal())
		{
			if( m_hbrBackground)
				::DeleteObject( m_hbrBackground);
			m_hbrBackground = CreateSolidBrush( dlg.GetColor());
			InvalidateRect( NULL);
		}
	}
	afx_msg void OnLButtonDblClk( UINT nFlags, CPoint point )
	{
		OnSetBkColor();
	}
	void OnSize(UINT nType, int cx, int cy)
	{
		CWnd::OnSize( nType, cx, cy);
		CalcBmpRect();
	}
};


BEGIN_MESSAGE_MAP(CShowBmpWnd, CWnd)
	ON_WM_PAINT()
	ON_WM_ERASEBKGND()
	ON_WM_RBUTTONDOWN()
	ON_WM_LBUTTONDBLCLK()
	ON_WM_SIZE()
END_MESSAGE_MAP()

class CBmpFileViewer : public CFileViewer
{
	CShowBmpWnd			m_bmp;
	PFS::CDataFile		m_datafile;
public:
	CBmpFileViewer() 
	{
		// Ìí¼ÓÄÜÔ¤ÀÀµÄÍ¼Æ¬ºó×ºÃû
		m_exts.push_back( L"bmp");
		m_exts.push_back( L"jpg");
		m_exts.push_back( L"tga");
		m_exts.push_back( L"p00");
		m_exts.push_back( L"p01");
		m_exts.push_back( L"p02");
		m_exts.push_back( L"p03");
		m_exts.push_back( L"p04");
		m_exts.push_back( L"p06");
		m_exts.push_back( L"p08");
		m_exts.push_back( L"b00");
		m_exts.push_back( L"b01");
		m_exts.push_back( L"b02");
		m_exts.push_back( L"b03");
		m_exts.push_back( L"b04");
		m_exts.push_back( L"b06");
		m_exts.push_back( L"b08");

		// ×¢²á½âÂëÆ÷
		PFSX::JPEG::Initialize();
		PFSX::TARGA::Initialize();
		PFSX::PIC::Initialize();
	}
	static PFSX::TSID GetDecodeTSID( const std::wstring& filename)
	{
		std::wstring ext = CGlobalInfo::GetFileExt( filename);
		if( ext.size() == 3 )
		{
			if( L"jpg" == ext)
			{
				return PFSX::MakeTypeIDByName( 'J', 'F', 'I', 'F');
			}
			if( L"tga" == ext)
			{
				return PFSX::MakeTypeIDByName( 'T', 'G', 'A');
			}
			if( ( ext[0] == L'p' || ext[0] == L'b')
				&& ( ext[1] == L'0')
				&& ( ext[2] >= L'0' && ext[2] <= L'9'))
			{
				// p00-p09 && b00-b09
				return PFSX::MakeTypeIDByName( (char)::towupper(ext[0]), (char)ext[1], (char)ext[2]);
			}
		}
		return 0;
	}
	virtual bool Create( CWnd* parent)
	{
		CFileViewer::Create( parent);
		CRect rc;
		parent->GetClientRect( &rc);
		return !!m_bmp.Create( NULL, NULL, WS_CHILD, rc, parent, 0);
	}
	virtual bool SetTreeItem( CZipTreeItem& item)
	{
		const std::wstring& name = item.GetMount();
		PFS::CFile file;
		if( file.Open( name, PFS::FM_EXCL, PFS::FA_RDONLY))
		{
			m_datafile.Seek( 0, PFS::FSM_SET);
			if( PFSX::DecodeFile( &file, &m_datafile, GetDecodeTSID( name)))
			{
				PFS::CImageData data = m_datafile.GetImage();
				m_bmp.SetBmpFile( data.GetData());
				return true;
			}
		}
		m_bmp.SetBmpFile( NULL);
		return true;
	}
	virtual void Show( bool bShow)
	{
		m_bmp.ShowWindow( bShow ? SW_SHOW : SW_HIDE);
	}
	virtual void Move()
	{
		CRect rc;
		m_parent->GetClientRect( &rc);
		m_bmp.MoveWindow( rc);
	}
};