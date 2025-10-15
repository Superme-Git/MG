///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : MemoryDC.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	GDI内存设备包装类
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-30 Created.
///=============================================================================
#include "StdAfx.h"
#include "MemoryDC.h"

BOOL CMemoryDC::_InnerCreate(int cx, int cy, HDC hdcCompitableTo)
{
	m_hMemDC = ::CreateCompatibleDC(NULL);
	if ( m_hMemDC == NULL )
	{
		return FALSE;
	}

	if ( hdcCompitableTo == NULL )
	{
		hdcCompitableTo = ::GetDC(NULL);
		m_hMemBitmap = ::CreateCompatibleBitmap( hdcCompitableTo, cx, cy );
		::ReleaseDC(NULL, hdcCompitableTo);
	}
	else
	{
		m_hMemBitmap = ::CreateCompatibleBitmap( hdcCompitableTo, cx, cy );
	}

	if ( m_hMemBitmap == NULL )
	{
		return FALSE;
	}

	m_hMemBitmapOld = ::SelectObject( m_hMemDC, m_hMemBitmap );
	if( m_hMemBitmapOld == NULL )
	{
		return FALSE;
	}

	//
	// 这里似乎马上删除m_hMemBitmap 也可以(待确认)
	// 

	//DeleteObject( m_hMemBitmap );
	//m_hMemBitmap = NULL;

	return TRUE;
}

void CMemoryDC::Destroy()
{
	if( m_hMemDC != NULL )
	{
		if( m_hMemBitmapOld != NULL )
		{
			::SelectObject( m_hMemDC, m_hMemBitmapOld );
			m_hMemBitmapOld = NULL;
		}

		::DeleteDC(m_hMemDC);
		m_hMemDC = NULL;
	}

	if( m_hMemBitmap != NULL )
	{
		::DeleteObject( m_hMemBitmap );
		m_hMemBitmap = NULL;
	}
}

BOOL CMemoryDC::CreateInnerBitmap( int cx, int cy )
{
	ASSERT( IsValid() );
	ASSERT( m_hMemBitmap  == NULL );

	m_hMemBitmap = ::CreateCompatibleBitmap( m_hMemDC, cx, cy );
	if( m_hMemBitmap == NULL )
	{
		return FALSE;
	}

	m_hMemBitmapOld = ::SelectObject( m_hMemDC, m_hMemBitmap );
	if( m_hMemBitmapOld == NULL )
	{
		return FALSE;
	}

	return TRUE;
}
