///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : MemoryDC.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	GDI内存设备包装类
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-30 Created.
///=============================================================================
#pragma once

#ifndef ASSERT
#define ASSERT(s) _ASSERT(s)
#endif

class CMemoryDC
{
public:
	CMemoryDC(void)
	{
		m_hMemDC = NULL;
		m_hMemBitmap = NULL;
		m_hMemBitmapOld = NULL;
	}

	~CMemoryDC(void)
	{
		Destroy();
	}

	operator HDC()
	{
		return m_hMemDC;
	}

	// 是否已创建(已初始化)
	BOOL IsValid()
	{
		return m_hMemDC != NULL;
	}

	// 创建无需或暂时不使用内存位图的兼容内存DC
	BOOL Create()
	{
		m_hMemDC = ::CreateCompatibleDC(NULL);
		if ( m_hMemDC == NULL )
		{
			return FALSE;
		}

		return TRUE;
	}

	//
	// cx, cy - 内存Bitmap的尺寸
	// hdcCompitableTo 不由此类管理
	//
	BOOL Create(int cx, int cy, HDC hdcCompitableTo = NULL)
	{
		if( !_InnerCreate( cx, cy, hdcCompitableTo ) )
		{
			Destroy();
			return FALSE;
		}
		
		return TRUE;
	}

	//
	// 如果创建成功, hBitmap将被拥有, 否则将由用户释放
	//
	BOOL Create( HBITMAP hBitmap )
	{
		m_hMemDC = ::CreateCompatibleDC(NULL);
		if ( m_hMemDC == NULL )
		{
			return FALSE;
		}

		m_hMemBitmapOld = ::SelectObject( m_hMemDC, hBitmap );
		if( m_hMemBitmapOld == NULL )
		{
			DeleteDC( m_hMemDC );
			return FALSE;
		}

		//
		// 这里似乎马上删除hBitmap 也可以(待确认)
		// 如果删除了 hBitmap, 就不再保存m_hMemBitmap了
		//DeleteObject( hBitmap );

		m_hMemBitmap = hBitmap;

		return TRUE;
	}

	void Destroy();

// 注: 下面的2个方法用于CEmotionRender的实现,其它场合请慎用
public:
	HGDIOBJ SelectObject( HGDIOBJ hObj )
	{
		ASSERT( IsValid() );
		return ::SelectObject( m_hMemDC, hObj );
	}

	BOOL CreateInnerBitmap( int cx, int cy );

protected:
	BOOL _InnerCreate(int cx, int cy, HDC hdcCompitableTo);

protected:
	HDC		m_hMemDC;
	HBITMAP m_hMemBitmap;
	HGDIOBJ m_hMemBitmapOld;
};

