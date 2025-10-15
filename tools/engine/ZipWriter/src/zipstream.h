#pragma once

// ../../contrib/zlib-1.2.3/
#include <zlib.h>

namespace ZIP {

class CZipStream
{
	CZipStream( const CZipStream& sr) {}
	CZipStream& operator=( const CZipStream& sr) { return *this; }
protected:
	z_stream				m_strm;
public:
	CZipStream() 
	{
		m_strm.zalloc = Z_NULL;    
		m_strm.zfree = Z_NULL;    
		m_strm.opaque = Z_NULL;
		m_strm.avail_in = 0;    
		m_strm.next_in = Z_NULL;
	}
	virtual ~CZipStream() {}
};

class CZipStreamException
{
protected:
	int m_nErrorCode; // zip error code

public:
	CZipStreamException(int nErrorCode)
	{
		m_nErrorCode = nErrorCode;
	}

	int GetErrorCode() const
	{
		return m_nErrorCode;
	}
};

class CZipStreamInflate : public CZipStream
{
	ZIP::CDataBlock		m_buffer;
	bool				m_first;
public:
	CZipStreamInflate( bool bZip = true) : m_first( true)
	{
		Reset(bZip);
	}
	virtual ~CZipStreamInflate() 
	{
		inflateEnd( &m_strm);
	}
public:
	void Reset( bool bZip)
	{
		if( m_first)
			m_first = false;
		else
			inflateEnd( &m_strm);
		int ret = inflateInit2( &m_strm, bZip ? (-MAX_WBITS) : MAX_WBITS);
		if( ret != Z_OK)
			throw CZipStreamException(ret);
	}

	//
	// һ���Խ����뻺�����е�����(in[insize])��ѹ��Ŀ�껺������(out[outsize])
	// out�Ĵ�С�Ѿ�Ԥ֪����outsizeָ��.
	// һ�������,returnֵӦ�õ���outsize.
	//
	static size_t InflateAll( const void* in, size_t insize, void* out, size_t outsize)
	{
		if( 0 == insize)
		{
			return 0;
		}

		z_stream strm = {0};
		int ret = inflateInit2( &strm, -MAX_WBITS );
		if( ret != Z_OK)
		{
			throw CZipStreamException(ret);
		}
		strm.avail_in = insize;
		strm.next_in = (Bytef*)in;
		strm.avail_out = outsize;
		strm.next_out = (Bytef*)out;

		ret = inflate( &strm, Z_SYNC_FLUSH);
		if( ret < Z_OK)
		{
			inflateEnd( &strm);
			throw CZipStreamException(ret);
		}

		assert( Z_STREAM_END == ret);
		assert( 0 == strm.avail_in && 0 == strm.avail_out);

		inflateEnd( &strm);
		return outsize;
	}

	//
	// �����뻺�����е�����(in[insize])��ѹ��Ŀ�����ݿ������
	// ��ѹǰ��out ���ᱻ��գ�Ȼ���Ž�ѹ������
	// ����out�Ĵ�С
	//
	size_t Inflate( const void* in, size_t insize, ZIP::CDataBlock& out)
	{
		out.Clear();

		m_strm.avail_in = insize;
		if( 0 == m_strm.avail_in)
			return 0;
		m_strm.next_in = (Bytef*)in;

		size_t buffersize = m_strm.avail_in * 5;
		{
			size_t temp = m_buffer.GetSize();
			if( temp > buffersize)
				buffersize = temp;
			else
				m_buffer.Resize( buffersize);
		}

		while( true)
		{
			m_strm.avail_out = buffersize;
			m_strm.next_out = (Bytef*)m_buffer.GetData();

			int ret = inflate( &m_strm, Z_SYNC_FLUSH);
			if( ret == Z_BUF_ERROR)
			{
				assert( m_strm.avail_in == 0 && m_strm.avail_out == buffersize);
				break;
			}
			if( (ret < Z_OK) || (ret == Z_NEED_DICT))	// error
			{
				throw CZipStreamException(ret);
			}
			size_t have = buffersize - m_strm.avail_out;
			out.Append( m_buffer.GetData(), have);
			if ( ret == Z_STREAM_END)
			{
				break;
			}
		}

		return out.GetSize();
	}

	//
	// �������ڴ������е�����(in)��ѹ��Ŀ�����ݿ����(out)��
	// ��ѹǰ��out ���ᱻ��գ�Ȼ���Ž�ѹ������
	// ����out�Ĵ�С
	//
	size_t Inflate( const ZIP::CDataBlock& in, ZIP::CDataBlock& out)
	{
		return Inflate( in.GetData(), in.GetSize(), out);
	}
};

class CZipStreamDeflate : public CZipStream
{
	ZIP::CDataBlock		m_buffer;
	bool				m_first;
	int					m_nLevel;
public:
	CZipStreamDeflate( int nLevel = -1) : m_first( true)
	{
		SetLevel( nLevel);
	}
	virtual ~CZipStreamDeflate() 
	{
		deflateEnd( &m_strm);
	}
public:
	int SetLevel( int nLevel) 
	{ 
		if( m_first)
		{
			m_first = false;
		}
		else
		{
			if( m_nLevel == nLevel)
			{
				deflateReset( &m_strm);
				return Z_OK;
			}
			deflateEnd( &m_strm);
		}
		m_nLevel = nLevel;
		int ret = deflateInit2( &m_strm, nLevel, Z_DEFLATED, -MAX_WBITS, 8, Z_DEFAULT_STRATEGY); 
		if( ret != Z_OK)
			throw CZipStreamException(ret);
		return ret;
	}
	size_t Deflate( const ZIP::CDataBlock& in, ZIP::CDataBlock& out)
	{
		return Deflate( in.GetData(), in.GetSize(), out);
	}
	size_t Deflate( const void* in, size_t insize, ZIP::CDataBlock& out)
	{
		out.Clear();

		m_strm.avail_in = insize;
		if( 0 == m_strm.avail_in)
			return 0;

		m_strm.next_in = (Bytef*)in;

		// ����̫С��buffersize���ܵ�����ѭ�������⡣
		// ������
		// ((WORD*)in)[0] == 0xFFFF, insize=2���������ѭ����
		// ���PFS��ͬ�Ĵ���
		size_t buffersize = m_strm.avail_in * 2;
		if( buffersize < 16 ) 
			buffersize = 16;
		
		size_t temp = m_buffer.GetSize();
		if( temp > buffersize)
			buffersize = temp;
		else
			m_buffer.Resize( buffersize);
	
		while( true)
		{
			m_strm.avail_out = buffersize;
			m_strm.next_out = (Bytef*)m_buffer.GetData();

			int ret = deflate( &m_strm, Z_SYNC_FLUSH);
			if( ret == Z_BUF_ERROR)
			{
				assert( m_strm.avail_in == 0 && m_strm.avail_out == buffersize);
				break;
			}
			if( (ret < Z_OK) || (ret == Z_NEED_DICT))	// error
			{
				throw CZipStreamException(ret);
			}
			size_t have = buffersize - m_strm.avail_out;
			out.Append( m_buffer.GetData(), have);
			if ( ret == Z_STREAM_END)
			{
				break;
			}
		}
		return out.GetSize();
	}
	size_t End( ZIP::CDataBlock& out)
	{
		out.Clear();

		m_strm.avail_in = 0;
		m_strm.next_in = NULL;
		
		size_t buffersize = m_buffer.GetSize();
		if( 0 == buffersize)
			return 0;

		m_strm.avail_out = buffersize;
		m_strm.next_out = (Bytef*)m_buffer.GetData();

		int ret = deflate( &m_strm, Z_FINISH);
		if( ret < Z_OK)
			throw CZipStreamException(ret);
		deflateReset( &m_strm);		// deflateReset����ҲӦ�õ�һ�Σ���ΪSetLevel��һ��ÿ��Deflate֮ǰ����

		size_t have = buffersize - m_strm.avail_out;
		out.Append( m_buffer.GetData(), have);
		return out.GetSize();
	}
	size_t Bound( size_t sourceLen)
	{
		return deflateBound( &m_strm, sourceLen);
	}
};

class CCRC32
{
	unsigned int m_uiCRC32;
public:
	CCRC32() { Reset(); }
	unsigned int GetValue() { return m_uiCRC32; }
	void Update( const ZIP::CDataBlock& in)
	{
		Update( in.GetData(), in.GetSize());
	}
	void Update( const void* in, size_t insize)
	{
		m_uiCRC32 = crc32_z( m_uiCRC32, (const Bytef*)in, insize);
	}
	void Reset() { m_uiCRC32 = crc32_z( 0L, Z_NULL, 0); }
};


}// namespace ZIP {
