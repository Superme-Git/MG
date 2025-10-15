#ifndef __MIX_LIBDDS_H_
#define __MIX_LIBDDS_H_

#if _MSC_VER > 1000
#pragma once
#endif

#define LIBDDS_API

#include "MixBase.h"
#include "bitmap.h"

namespace MIX
{
	class LIBDDS_API IDDrawSurface
	{
	public:
		virtual int LoadFromFile( const wchar_t* fileName) = 0;
		virtual int LoadFromMem( const void* data, size_t size) = 0;
		virtual int Save( const wchar_t* fileName) = 0;
		virtual int Decompress( CBitmap& bitmap) = 0;
		virtual int GetWidth() = 0;
		virtual int GetHeight() = 0;
		virtual size_t GetDataSize() = 0;
		virtual bool IsValid() const = 0;
		virtual const uchar_t* GetData( ) = 0;
		virtual const uchar_t* GetData( size_t& size) = 0;
		virtual void Destroy() = 0;
	};

	extern "C"  LIBDDS_API IDDrawSurface* CreateDDrawSurface();

}	// namespace MIX


#endif	// __MIX_LIBDDS_H_
