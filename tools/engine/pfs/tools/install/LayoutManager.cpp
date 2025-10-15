#include "StdAfx.h"
#include "LayoutManager.h"
#include "RenderHelper.h"
#include "../../pfs2.h"
#include <sstream>
#include <string>

CLayoutManager::CLayoutManager(void)
: m_pWnd( NULL)
, m_dcBg( NULL)
, m_bfullscreen( false )
, m_bstretch( false )
, m_fgWidth(1024) // ǰ��ͼ���
, m_fgHeight(768) // ǰ��ͼ�߶�
, m_bfitscreen(false)
{
}

CLayoutManager::~CLayoutManager(void)
{
	if( m_dcBg)
	{
		::DeleteDC( m_dcBg);
		m_dcBg = NULL;
	}
	for( CFgVector::iterator i = m_dcFgs.begin(), e = m_dcFgs.end(); i != e; ++i)
	{
		::DeleteDC( *i);
	}
	m_dcFgs.clear();
}

bool CLayoutManager::Create( CWnd* pWnd, const std::wstring& path, bool bFullScreen )
{
	ASSERT( pWnd);
	m_pWnd = pWnd;
	m_path = path;
	m_bfullscreen = bFullScreen;
	return true;
}

// ������ͼƬ - ��ǰ�汾�ѷ���
bool CLayoutManager::ReadBackgroundBmp()
{
	std::wstring bgpath( m_path);
	bgpath += L"/background/background.bmp";
	PFS::CFile file;
	if( !file.Open( bgpath, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	PFS::CImageData cid = file.GetImage();
	int width=0, height =0;
	HBITMAP hBg = MHZX::CRenderHelper::LoadBitmap( (LPVOID)cid.GetData(), (size_t)cid.GetSize(), false, 
		&width, &height);
	if( NULL == hBg)
	{
		return false;
	}

	CClientDC dc( m_pWnd);
	m_dcBg = ::CreateCompatibleDC( dc.GetSafeHdc() );
	HBITMAP hOld = (HBITMAP)::SelectObject( m_dcBg, hBg);
	if( hOld)
	{
		::DeleteObject( hOld);
	}
	HRGN rgn = NULL;
	if( !MHZX::CRenderHelper::CreateRgnByAlphaBits( hBg, rgn))
	{
		// ����Ļ�ֻ��û��rgn�������������
		//return false;
	}
	if( rgn)
	{
		::SetWindowRgn( m_pWnd->GetSafeHwnd(), rgn, TRUE);
	}

	CenterWindow( width, height);
	return true;
}

bool CLayoutManager::ReadForegroundTga( int index)
{
	std::wostringstream wos;
	wos << m_path << L"/background/install" << index << L".tga";
	PFS::CFile file;
	if( !file.Open( wos.str(), PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	PFS::CImageData cid = file.GetImage();
	int width=0, height =0;
	HBITMAP hFg = MHZX::CRenderHelper::LoadTarga( (LPCVOID)cid.GetData(), (size_t)cid.GetSize(), true, 
		&width, &height);
	if( NULL == hFg)
	{
		return false;
	}
	CRect rc;
	m_pWnd->GetClientRect( &rc);

	//CClientDC dc( m_pWnd);
	HDC hDC = ::CreateCompatibleDC( NULL );
	HBITMAP hOld = (HBITMAP)::SelectObject( hDC, hFg );
	if( hOld)
	{
		::DeleteObject( hOld);
	}
	
	m_dcFgs.push_back( hDC);

	if ( index == 1 )
	{
		m_fgWidth = width; // ǰ��ͼ���
		m_fgHeight = height;// ǰ��ͼ�߶�
		int screenwidth = GetSystemMetrics( SM_CXSCREEN);
		int screenheight = GetSystemMetrics( SM_CYSCREEN);
		// �Ƿ���Ҫ������Ⱦ
		m_bstretch = ( m_fgWidth != screenwidth || m_fgHeight != screenheight );
		m_bfitscreen = ( m_fgWidth >= screenwidth && m_fgHeight >= screenheight );

		// ���ݵ�һ��tga�ĳߴ�ȷ�����ڴ�С
		if( m_bfullscreen )
		{
			FullScreen();
		}
		else
		{
			// ��ȫ��ʱ������ʾ����
			CenterWindow( width, height);
		}
	}

	return true;
}

// ��ǰ��ͼƬ - ��ǰ�汾�ѷ���
bool CLayoutManager::ReadForegroundBmp( int index)
{
	std::wostringstream wos;
	wos << m_path << L"/background/setup" << index << L".bmp";
	PFS::CFile file;
	if( !file.Open( wos.str(), PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	PFS::CImageData cid = file.GetImage();
	int width=0, height =0;
	HBITMAP hFg = MHZX::CRenderHelper::LoadBitmap( (LPVOID)cid.GetData(), (size_t)cid.GetSize(), true, 
		&width, &height);
	if( NULL == hFg)
	{
		return false;
	}
	CRect rc;
	m_pWnd->GetClientRect( &rc);

	CClientDC dc( m_pWnd);
	HDC hDC = ::CreateCompatibleDC( dc.GetSafeHdc() );
	HBITMAP hBmp = ::CreateCompatibleBitmap( dc.GetSafeHdc(), rc.right, rc.bottom);
	HBITMAP hOld = (HBITMAP)::SelectObject( hDC, hBmp);
	if( hOld)
	{
		::DeleteObject( hOld);
	}
	::BitBlt( hDC, 0, 0, rc.right, rc.bottom, m_dcBg, 0, 0, SRCCOPY);

	HDC hTemp = ::CreateCompatibleDC( dc.GetSafeHdc() );
	hOld = (HBITMAP)::SelectObject( hTemp, hFg);
	const BLENDFUNCTION blend = { AC_SRC_OVER, 0, 255, AC_SRC_ALPHA };
	::AlphaBlend( hDC, 0, 0, rc.right, rc.bottom, hTemp, 0, 0, rc.right, rc.bottom, blend);
	::SelectObject( hTemp, hOld);
	::DeleteDC( hTemp);

	m_dcFgs.push_back( hDC);
	return true;
}

bool CLayoutManager::Draw( HDC hDes, int index)
{
	if( NULL == m_pWnd || !::IsWindow( m_pWnd->GetSafeHwnd()))
	{
		return false;
	}
	HDC hDC = NULL;	// ���ٿ�������ͼ
	if( index >= 0 && index < (int)m_dcFgs.size())
	{
		hDC = m_dcFgs[index];
	}
	
	CRect rc;
	m_pWnd->GetClientRect( &rc);

	BOOL bOK = FALSE;

	if ( hDC != NULL )
	{
		if( m_bstretch )
		{
			bOK = ::StretchBlt( hDes, 0, 0, rc.right, rc.bottom, hDC, 0, 0, m_fgWidth, m_fgHeight, SRCCOPY);
		}
		else
		{
			bOK = ::BitBlt( hDes, 0, 0, rc.right, rc.bottom, hDC, 0, 0, SRCCOPY);
		}
	}

	if( !bOK )
	{
		// ���ͼƬ����ʧ�ܣ�����ɫ��䱳��
		::SelectObject( hDes, ::GetStockObject( DKGRAY_BRUSH));
		::Rectangle( hDes, 0, 0, rc.right, rc.bottom);
	}
	return TRUE;
}

CLayoutManager& CLayoutManager::GetInstance()
{
	static CLayoutManager s_instance;
	return s_instance;
}

// �� logo.jpg
bool CLayoutManager::ReadLogoJpg()
{
	ASSERT( m_pWnd);
	std::wstring name( m_path);
	name += L"/background/logo.jpg";
	PFS::CFile file;
	if( !file.Open( name, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	PFS::CDataFile cdf;
	if( !PFSX::DecodeFile( &file, &cdf, PFSX::MakeTypeIDByName( 'J', 'F', 'I', 'F')))
	{
		return false;
	}
	PFS::CImageData cid = cdf.GetImage();
	int width=0, height =0;
	HBITMAP hBg = MHZX::CRenderHelper::LoadBitmap( (LPVOID)cid.GetData(), (size_t)cid.GetSize(), false, 
		&width, &height);
	if( NULL == hBg)
	{
		return false;
	}

	CClientDC dc( m_pWnd);
	HDC hDC = ::CreateCompatibleDC( dc.GetSafeHdc() );
	HBITMAP hOld = (HBITMAP)::SelectObject( hDC, hBg);
	if( hOld)
	{
		::DeleteObject( hOld);
	}
	m_dcFgs.push_back( hDC);

	CenterWindow( width, height);
	return true;
}

// �� copyright.txt
bool CLayoutManager::ReadCopyrightTxt( PFS::CDataBlock& data)
{
	std::wstring name( m_path);
	name += L"/copyright.txt" ;
	PFS::CFile file;
	if( !file.Open( name, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	data.Resize( (size_t)file.GetSize());
	size_t len = file.Read( data.GetData(), data.GetSize());
	if( PFS::FILE_SIZE_ERROR == len)
	{
		return false;
	}
	data.Resize( len);
	return true;
}

// ʹ���ھ���
void CLayoutManager::CenterWindow( int width, int height)
{
	ASSERT( m_pWnd);
	int screenwidth = GetSystemMetrics( SM_CXSCREEN);
	int screenheight = GetSystemMetrics( SM_CYSCREEN);
	int left = 0, top = 0;
	if( screenwidth > 0 && screenheight > 0)
	{
		if ( width >= screenwidth )
		{
			width = screenwidth;
		}
		else
		{
			left = (screenwidth - width) / 2;
		}

		if( height >= screenheight )
		{
			height = screenheight;
		}
		else
		{
			top = (screenheight - height) / 2;
		}
	}
	
	m_pWnd->SetWindowPos( NULL, left, top, width, height, SWP_NOZORDER);
}

// ʹ������ȫ��ģʽչʾ
void CLayoutManager::FullScreen()
{
	ASSERT( m_pWnd);
	int screenwidth = GetSystemMetrics( SM_CXSCREEN);
	int screenheight = GetSystemMetrics( SM_CYSCREEN);
	VERIFY(m_pWnd->SetWindowPos( NULL, 0, 0, screenwidth, screenheight, SWP_NOZORDER ) );
	// �Ƿ���Ҫ������Ⱦ
	m_bstretch = ( m_fgWidth != screenwidth || m_fgHeight != screenheight );
	m_bfullscreen = true;
}

// �л�ȫ��(�û�˫������ʱ�����л�ȫ��)
void CLayoutManager::ToggleFullScreen()
{
	if( m_bfullscreen )
	{
		m_bfullscreen = false;
		CenterWindow( m_fgWidth, m_fgHeight );
		m_bstretch = false;
	}
	else
	{
		FullScreen();
	}

	m_pWnd->Invalidate(TRUE);
}
