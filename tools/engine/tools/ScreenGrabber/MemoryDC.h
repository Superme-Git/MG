///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : MemoryDC.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	GDI�ڴ��豸��װ��
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

	// �Ƿ��Ѵ���(�ѳ�ʼ��)
	BOOL IsValid()
	{
		return m_hMemDC != NULL;
	}

	// �����������ʱ��ʹ���ڴ�λͼ�ļ����ڴ�DC
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
	// cx, cy - �ڴ�Bitmap�ĳߴ�
	// hdcCompitableTo ���ɴ������
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
	// ��������ɹ�, hBitmap����ӵ��, �������û��ͷ�
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
		// �����ƺ�����ɾ��hBitmap Ҳ����(��ȷ��)
		// ���ɾ���� hBitmap, �Ͳ��ٱ���m_hMemBitmap��
		//DeleteObject( hBitmap );

		m_hMemBitmap = hBitmap;

		return TRUE;
	}

	void Destroy();

// ע: �����2����������CEmotionRender��ʵ��,��������������
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

