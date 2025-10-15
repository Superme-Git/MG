#include "../common.h"
#include "ziphelper.h"

namespace PFSX {

class CZipReadFile::CData
{
	enum { BUFFER_SIZE = 1024, };


	ZIPFILE::helper::CZipStreamInflate		m_zsi;
	fsize_t									m_sizefull;
	fsize_t									m_posfull;
	fsize_t									m_outoffset;
	PFS::CDataBlock							m_inbuffer;
	PFS::CDataBlock							m_outbuffer;
	PFS::CBaseFile&							m_basefile;
	bool									m_iszip;
public:
	CData( CBaseFile& bf, fsize_t fullsize, bool zip) 
		: m_basefile( bf), m_zsi( zip), m_iszip( zip), m_sizefull( fullsize), m_outoffset( 0), m_posfull( 0)
	{
		m_inbuffer.Resize( BUFFER_SIZE);
	}
	~CData() {}
private:
	size_t FillOutBuffer()
	{
		m_outoffset = 0;
		m_outbuffer.Resize( 0);
		size_t out = m_basefile.Read( m_inbuffer.GetData(), BUFFER_SIZE);
		if( PFS::FILE_SIZE_ERROR == out)
			return PFS::FILE_SIZE_ERROR;
		m_inbuffer.Resize( out);

		try
		{
			m_zsi.Inflate( m_inbuffer, m_outbuffer);
		}
		catch( ZIPFILE::helper::CZipStreamException&)
		{
			return PFS::FILE_SIZE_ERROR;
		}
		return out;
	}
	size_t ReadFromFile( char* data, size_t size)
	{
		size_t		allsize = 0;
		while( size)
		{
			size_t sizeread = ReadFromBuffer( data, size);
			allsize += sizeread;
			size -= sizeread;
			if( 0 == size)
				break;
			data += sizeread;
			
			sizeread = FillOutBuffer();
			if( PFS::FILE_SIZE_ERROR == sizeread)
				return PFS::FILE_SIZE_ERROR;
			if( 0 == sizeread)
				break;
		}
		m_posfull += allsize;
		return allsize;
	}
	size_t ReadFromBuffer( char* data, size_t size)
	{
		fsize_t sizehave = m_outbuffer.GetSize() - m_outoffset;
		if( 0 == sizehave)
			return 0;

		size_t temp = (size_t)sizehave;
		if( temp > size)
			temp = size;
		memcpy( data, (char*)m_outbuffer.GetData() + m_outoffset, temp);
		m_outoffset += temp;
		return temp;
	}
public:
	fsize_t GetFullPos() { return m_posfull; }
	fsize_t GetFullSize() { return m_sizefull; }
	size_t TryRead( char* data, size_t size)
	{
		return ReadFromFile( data, size);
	}
	fsize_t TrySeek( foff_t pos, PFS::FILE_SEEK_METHOD fsm)
	{
		if( ( 0 == pos) && ( PFS::FSM_SET == fsm))
		{
			m_posfull = 0;

			m_outbuffer.Clear();
			m_outoffset = 0;
			m_zsi.Reset( m_iszip);	// m_zsi也要reset，否则下次 m_zsi.Inflate 得不到正确结果
			return m_basefile.Seek( 0, PFS::FSM_SET);
		}
		else
			return PFS::FILE_SIZE_ERROR; 
	}

	bool ReadAll(void* data, fsize_t size)
	{
		fsize_t fallsize = m_basefile.GetSize();
		if (fallsize > MAX_MEMORY_DATA_SIZE || m_sizefull > MAX_MEMORY_DATA_SIZE )
		{
			PFS::CEnv::SetLastError(PFS::EC_FILESIZE_TOO_LARGE);
			return false;
		}

		if ( m_sizefull > size )
		{
			PFS::CEnv::SetLastError(PFS::EC_INSUFFICIENT_BUFFER);
			return false;
		}

		PFS::CImageData imageData = m_basefile.GetImage();

		try
		{
			ZIPFILE::helper::CZipStreamInflate::InflateAll( imageData.GetData(), (size_t)imageData.GetSize(), data, (size_t)size );
		}
		catch( ZIPFILE::helper::CZipStreamException&)
		{
			PFS::CEnv::SetLastError( PFS::EC_FILE_BAD_ZIPDATA );
			return false;
		}
		return true;
	}
};

CZipReadFile::CZipReadFile( CBaseFile& bf, fsize_t fullsize, bool zip)
	: m_data( new CData( bf, fullsize, zip))
{

}

CZipReadFile::~CZipReadFile() 
{ 
	delete m_data; 
}

size_t CZipReadFile::Read( void* data, size_t size)
{
	return m_data->TryRead( (char*)data, size);
}

size_t CZipReadFile::Write( const void* data, size_t size) 
{ 
	return PFS::FILE_SIZE_ERROR; 
}

fsize_t CZipReadFile::Seek( foff_t pos, PFS::FILE_SEEK_METHOD fsm) 
{
	return m_data->TrySeek( pos, fsm);
}

fsize_t CZipReadFile::GetSize() 
{ 
	return m_data->GetFullSize(); 
}

fsize_t CZipReadFile::GetPos() 
{ 
	return m_data->GetFullPos(); 
}

bool CZipReadFile::SetEnd() 
{ 
	return false;
}

PFS::CImageData CZipReadFile::GetImage() 
{ 
	assert( 0 == GetPos());
	return PFS::BaseFileToImageData( *this);
}

bool CZipReadFile::ReadAll( void* data, fsize_t size )
{
	return m_data->ReadAll(data, size);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CZipWriteFile::CData
{
	enum { BUFFER_SIZE = 1024, };

	ZIPFILE::helper::CZipStreamDeflate		m_deflate;
	CBaseFile&								m_basefile;
	PFS::CDataBlock							m_inbuffer;
	PFS::CDataBlock							m_buffer;
	fsize_t									m_sizefull;
	bool									m_alreadySetEnd;	// 已经SetEnd
public:
	CData( PFS::CBaseFile& bf, int level) : m_basefile( bf), m_deflate( level), m_sizefull(0), m_alreadySetEnd( false)
	{
		m_inbuffer.Reserve( BUFFER_SIZE);
	}
	~CData() { SetEnd(); }
public:
	fsize_t GetFullSize() { return m_sizefull; }
	fsize_t GetFullPos() { return m_sizefull; /*只写操作，位置和大小一样*/}
	bool SetEnd() 
	{
		if( m_alreadySetEnd)
		{
			return true;
		}
		try
		{
			if( m_inbuffer.GetSize() > 0)
			{
				m_deflate.Deflate( m_inbuffer.GetData(), m_inbuffer.GetSize(), m_buffer);
				m_inbuffer.Clear();
				m_basefile.Write( m_buffer.GetData(), m_buffer.GetSize());
			}
			m_deflate.End( m_buffer);
		}
		catch( ZIPFILE::helper::CZipStreamException&)
		{
			return false;
		}

		m_alreadySetEnd = true;
		m_basefile.Write( m_buffer.GetData(), m_buffer.GetSize());
		return m_basefile.SetEnd();
	}
	size_t TryWrite( const void* data, size_t size)
	{
		// 当size很小的时候，每次都调 Deflate 反而比原数据要大
		if( m_inbuffer.GetSize() + size <= BUFFER_SIZE)
		{
			m_inbuffer.Append( data, size);
			m_sizefull += size;
			return size;
		}
		try
		{
			m_deflate.Deflate( m_inbuffer.GetData(), m_inbuffer.GetSize(), m_buffer);
			m_inbuffer.Clear();
			m_inbuffer.Append( data, size);
		}
		catch( ZIPFILE::helper::CZipStreamException&)
		{
			return PFS::FILE_SIZE_ERROR;
		}
		size_t out = m_basefile.Write( m_buffer.GetData(), m_buffer.GetSize());
		if( PFS::FILE_SIZE_ERROR == out)
			return PFS::FILE_SIZE_ERROR;
		m_sizefull += size;
		return size;
	}
};

CZipWriteFile::CZipWriteFile( PFS::CBaseFile& bf, int level, bool zip) : m_data( new CData( bf, level)) {}

CZipWriteFile::~CZipWriteFile()
{
	delete m_data;
}

size_t CZipWriteFile::Read( void* data, size_t size)
{
	PFS::CEnv::SetLastError( PFS::EC_FILE_WRITEONLY  );
	return PFS::FILE_SIZE_ERROR;
}

size_t CZipWriteFile::Write( const void* data, size_t size)
{
	return m_data->TryWrite( data, size);
}

fsize_t CZipWriteFile::Seek( foff_t pos, PFS::FILE_SEEK_METHOD fsm)
{
	// TODO:文件不支持SEEK操作
	return PFS::FILE_SIZE_ERROR;
}

fsize_t CZipWriteFile::GetSize()
{
	return m_data->GetFullSize();
}

fsize_t CZipWriteFile::GetPos()
{
	return m_data->GetFullPos();
}

bool CZipWriteFile::SetEnd()
{
	return m_data->SetEnd();
}

PFS::CImageData CZipWriteFile::GetImage()
{
	PFS::CEnv::SetLastError( PFS::EC_FILE_WRITEONLY );
	return PFS::CImageData();
}

bool CZipWriteFile::ReadAll( void* data, fsize_t size )
{
	PFS::CEnv::SetLastError( PFS::EC_FILE_WRITEONLY );
	return false;
}

} // namespace PFSX {

