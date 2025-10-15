#ifndef __MIX_LIBDDS_DDRAWSURFACE_H_
#define __MIX_LIBDDS_DDRAWSURFACE_H_

#if _MSC_VER > 1000
#pragma once
#endif

#include "../include/libdds.h"
#include "iolib/mixstream.h"

namespace MIX
{

	typedef OctetsStream Stream;

struct DDSPixelFormat
{
	uint_t size;
	uint_t flags;
	uint_t fourcc;
	uint_t bitcount;
	uint_t rmask;
	uint_t gmask;
	uint_t bmask;
	uint_t amask;
};

struct DDSCaps
{
	uint_t caps1;
	uint_t caps2;
	uint_t caps3;
	uint_t caps4;
};

/// DDS file header for DX10.
struct DDSHeader10
{
	uint_t dxgiFormat;
	uint_t resourceDimension;
	uint_t miscFlag;
	uint_t arraySize;
	uint_t reserved;
};

/// DDS file header.
struct DDSHeader
{
	uint_t fourcc;
	uint_t size;
	uint_t flags;
	uint_t height;
	uint_t width;
	uint_t pitch;
	uint_t depth;
	uint_t mipmapcount;
	uint_t reserved[11];
	DDSPixelFormat pf;
	DDSCaps caps;
	uint_t notused;
	DDSHeader10 header10;


	// Helper methods.
	DDSHeader();

	void setWidth(uint_t w);
	void setHeight(uint_t h);
	void setDepth(uint_t d);
	void setMipmapCount(uint_t count);
	void setTexture2D();
	void setTexture3D();
	void setTextureCube();
	void setLinearSize(uint_t size);
	void setPitch(uint_t pitch);
	void setFourCC(uchar_t c0, uchar_t c1, uchar_t c2, uchar_t c3);
	void setPixelFormat(uint_t bitcount, uint_t rmask, uint_t gmask, uint_t bmask, uint_t amask);
	void setDX10Format(uint_t format);
	void setNormalFlag(bool b);

	void swapBytes();

	bool hasDX10Header() const;
};

Stream & operator<< ( Stream & s, DDSHeader & header);



class CDDrawSurface
	: public IDDrawSurface
{
public:
	CDDrawSurface(void);
	CDDrawSurface( Stream& s);

	~CDDrawSurface(void);

public:
	virtual int LoadFromFile( const wchar_t* fileName);
	virtual int LoadFromMem( const void* data, size_t size);

	virtual int Save( const wchar_t* fileName);


	virtual int Decompress( CBitmap& bitmap);

	virtual int GetWidth();
	virtual int GetHeight();


	virtual bool IsValid() const;
	virtual size_t GetDataSize();

	virtual const uchar_t* GetData( );
	virtual const uchar_t* GetData( size_t& size);
	virtual void Destroy();

private:
	uint_t	offset( const uint_t face, const uint_t mipmap);
//
//protected:
//	virtual void			WeakRef()
//	{
//		++m_weakRefCount;
//	}
//	virtual void			WeakRelease()
//	{
//		--m_weakRefCount;
//		if ( m_weakRefCount)
//			return;
//
//		delete this;
//	}
//
//protected:
//	int				m_weakRefCount;

protected:
	DDSHeader		m_header;
	DDSHeader10		m_header10;

	Stream			m_stream;

public:
	DDSHeader&		GetDDSHeader() 
	{
		return		m_header;
	}

	
};



}


#endif	// __MIX_LIBDDS_DDRAWSURFACE_H_