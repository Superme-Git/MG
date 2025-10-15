#include "../common.h"

namespace PFSX {

CNativeFile::CNativeFile() 
	: m_fileio( NULL)
{
}

CNativeFile::~CNativeFile() 
{ 
	if( m_fileio) 
		delete m_fileio; 
}

bool CNativeFile::Open( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa)
{
	CNativeFile::Close();

	try
	{
		m_fileio = new PFS::CFileIO( filename, fm, fa);
	}
	catch( PFS::CFileIOException& fioe)  
	{
		PFS::CEnv::GetOutLogInstance()->Print(L"ERROR: CNativeFile::Open(name:%s, mode:%d,access:%d)(%d)\n", filename.c_str(), fm, fa, fioe.GetLastError() );
		PFS::CEnv::SetLastError( fioe.GetLastError());
		return false;
	}

	return true;
}

void CNativeFile::Close()
{
	if( m_fileio)
	{
		delete m_fileio;
		m_fileio = NULL;
	}
}

size_t CNativeFile::Read( void* data, size_t size)
{
	assert( m_fileio);
	try
	{
		return m_fileio->Read( data, size);
	}
	catch( PFS::CFileIOException& fioe)  
	{
		PFS::CEnv::SetLastError( fioe.GetLastError());
	}
	return PFS::FILE_SIZE_ERROR;
}

size_t CNativeFile::Write( const void* data, size_t size)
{
	assert( m_fileio);
	try
	{
		return m_fileio->Write( data, size);
	}
	catch( PFS::CFileIOException& fioe)
	{
		PFS::CEnv::SetLastError( fioe.GetLastError());
	}
	return PFS::FILE_SIZE_ERROR;
}

fsize_t CNativeFile::GetSize()
{
	assert( m_fileio);
	try
	{
		return m_fileio->GetSize();
	}
	catch( PFS::CFileIOException& fioe)
	{
		PFS::CEnv::SetLastError( fioe.GetLastError());
	}
	return PFS::FILE_SIZE_ERROR;
}

fsize_t CNativeFile::Seek( foff_t pos, PFS::FILE_SEEK_METHOD fsm)
{
	assert( m_fileio);
	try
	{
		return m_fileio->Seek( pos, fsm);
	}
	catch( PFS::CFileIOException& fioe)
	{
		PFS::CEnv::SetLastError( fioe.GetLastError());
	}

	return PFS::FILE_SIZE_ERROR;
}

fsize_t CNativeFile::GetPos()
{
	assert( m_fileio);
	try
	{
		return m_fileio->GetPos();
	}
	catch( PFS::CFileIOException& fioe)
	{
		PFS::CEnv::SetLastError( fioe.GetLastError());
	}
	return PFS::FILE_SIZE_ERROR;
}

bool CNativeFile::SetEnd()
{
	assert( m_fileio);
	try
	{
		return m_fileio->SetEnd();
	}
	catch( PFS::CFileIOException& fioe)
	{
		PFS::CEnv::SetLastError( fioe.GetLastError());
	}
	return false;
}

PFS::CImageData CNativeFile::GetImage()
{
	if( NULL == m_fileio)
		return PFS::CImageData();
	try
	{
		PFS::CFileImageHandle fih ( *m_fileio);
		PFS::CFileImage* image = new PFS::CFileImage( fih);

		return PFS::CImageData( new PFS::CRawImageData( image));
	}
	catch( PFS::CFileIOException&) 
	{
	}
	return BaseFileToImageData( *this);
}

bool CNativeFile::ReadAll( void* data, fsize_t size )
{
	if( NULL == m_fileio)
	{
		PFS::CEnv::SetLastError( PFS::EC_FILE_NOTOPEN );
		return false;
	}

	fsize_t allsize = m_fileio->GetSize();
	if ( allsize > size )
	{
		PFS::CEnv::SetLastError( PFS::EC_INSUFFICIENT_BUFFER );
		return false;
	}

	if( allsize > MAX_MEMORY_DATA_SIZE )
	{
		PFS::CEnv::SetLastError( PFS::EC_FILESIZE_TOO_LARGE);
		return false;
	}

	try
	{
		PFS::CFileImageHandle fih ( *m_fileio);
		PFS::CFileImage image( fih);
		memcpy( data, image.GetData(), (size_t)image.GetSize() );
		return true;
	}
	catch( PFS::CFileIOException&) 
	{
		return false;
	}

	// TODO:
	return true;
}

} // namespace PFSX {