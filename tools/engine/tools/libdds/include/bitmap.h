#pragma once

#ifndef __MIX_BITMAP_H
#define __MIX_BITMAP_H

#include "MixBase.h"
#include <string.h> // for memcpy()

namespace MIX
{
	//! 图片原始像素定义
	class CBitmap : public CUncopyable
	{
	protected:
		PixelFormat m_format;
		int			m_width;
		int			m_height;
		uchar_t*	m_pixels;
		int			m_curFillLine;

	public:
		CBitmap()
			: m_width(0), m_height(0), m_pixels( NULL)
		{}

		CBitmap( PixelFormat format, int width, int height)
			: m_format( format), m_width( width), m_height( height), m_pixels( NULL)
		{
			MIX_ASSERT( m_format < PIX_FMT_DXT1);
		}
		~CBitmap()
		{
			FreePixels();
		}

	public:
		//! 重设图片信息,图片将变成空图
		void SetInfo( PixelFormat format, int width, int height)
		{
			FreePixels();
			m_format = format;
			MIX_ASSERT( m_format < PIX_FMT_DXT1);
			m_width = width;
			m_height = height;
		}

		PixelFormat	GetFormat() const
		{
			return m_format;
		}
		int			GetWidth() const 
		{
			return m_width;
		}
		int			GetHeight() const
		{
			return m_height;
		}

		const uchar_t* GetPixels() const
		{
			return m_pixels;
		}

		uint_t		GetBytesPerPixel() const
		{
			int bypp[] = { 2,2,3,4,4,1,2,2,0,0,0,0,0};
			return bypp[m_format];
		}

		uint_t		GetSize() const
		{
			return m_width * m_height * GetBytesPerPixel();
		}

		bool		FillPixels( const void* pixels, size_t size)
		{
			if ( !m_pixels)
				AllocPixels();
			if ( !m_pixels)
				return false;

			uint_t invalidSize = GetSize();
			if ( size < invalidSize)
				invalidSize = (int)size;
			
			memcpy( m_pixels, pixels, invalidSize);
			return true;
		}

	public:
		bool BeginFillLine()
		{
			if ( !m_pixels)
				AllocPixels();
			if ( !m_pixels)
				return false;
			m_curFillLine = 0;
			return true;
		}
		bool FillLine( const void* buffer, size_t size)
		{
			if ( m_curFillLine < 0 || m_curFillLine >= m_height || m_pixels == NULL)
				return false;

			size_t lineSize = m_width * GetBytesPerPixel();
			uchar_t* dst = m_pixels + lineSize * m_curFillLine;
			if ( size < lineSize)
				lineSize = size;
			memcpy( dst, buffer, lineSize);
			++m_curFillLine;

			return true;		
		}

	public:
		bool SetPixel( int x, int y, PixelData32 pixel)
		{
			if ( m_format != PIX_FMT_A8R8G8B8 && m_format != PIX_FMT_X8R8G8B8)
				return false;

			uchar_t* ppix = m_pixels + y * m_width * 4 + x * 4;
			*((PixelData32*)ppix) = pixel;
			return true;
		}
	private:
		void AllocPixels()
		{
			MIX_ASSERT( m_pixels == NULL);
			uint_t size = GetSize();
			if ( size)
				m_pixels = new uchar_t[size];
		}
		void FreePixels()
		{
			if ( m_pixels)
			{
				delete []m_pixels;
				m_pixels = NULL;
			}
		}
	};

}	// namespace MIX

#endif	// __MIX_BITMAP_H
