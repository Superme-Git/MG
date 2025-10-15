//////////////////////////////////////////////////////////////////////////
//
// InetFile.cpp
//
//  [10/27/2008 yanghaibo]
//
//////////////////////////////////////////////////////////////////////////
#include "../common.h"
#include "../../InetFile.h"
#include "InetIO.h"
#include <cassert>

namespace PFSX {

CInetFile::CInetFile(void)
	: m_fileio(NULL)
{
}

CInetFile::~CInetFile(void)
{
	// Close the file but do not delete the file io object
	Close();

	if( m_fileio)
	{
		delete m_fileio;
		m_fileio = NULL;
	}
}

bool CInetFile::Open( const std::wstring& url )
{
	Close();

	if( m_fileio == NULL )
	{
		m_fileio = new PFS::CInetIO();
		if ( m_fileio == NULL )
		{
			PFS::CEnv::SetLastError( ERROR_OUTOFMEMORY );
			return false;
		}
	}

	BOOL bRet = m_fileio->Open( url.c_str() );
	if( !bRet )
	{
		PFS::CEnv::SetLastError( ::GetLastError() );
		return false;
	}

	return true;
}

bool CInetFile::Open( const std::wstring& url, long lRangeBegin, long lRangeEnd )
{
	Close();

	if( m_fileio == NULL )
	{
		m_fileio = new PFS::CInetIO();
		if ( m_fileio == NULL )
		{
			PFS::CEnv::SetLastError( ERROR_OUTOFMEMORY );
			return false;
		}
	}

	BOOL bRet = m_fileio->Open( url.c_str(), lRangeBegin, lRangeEnd );
	if( !bRet )
	{
		PFS::CEnv::SetLastError( ::GetLastError() );
		return false;
	}

	return true;
}

bool CInetFile::Open( HINTERNET hConnect, const std::wstring& strObjectName )
{
	Close();

	if( m_fileio == NULL )
	{
		m_fileio = new PFS::CInetIO();
	}

	if ( m_fileio == NULL )
	{
		return false;
	}

	BOOL bRet = m_fileio->Open( hConnect, strObjectName.c_str() );
	if( !bRet )
	{
		PFS::CEnv::SetLastError( ::GetLastError() );
		return false;
	}

	return true;
}

bool CInetFile::Open( HINTERNET hConnect, const std::wstring& strObjectName, long lRangeBegin, long lRangeEnd )
{
	Close();

	if( m_fileio == NULL )
	{
		m_fileio = new PFS::CInetIO();
	}

	if ( m_fileio == NULL )
	{
		return false;
	}

	BOOL bRet = m_fileio->Open( hConnect, strObjectName.c_str(), lRangeBegin, lRangeEnd );
	if( !bRet )
	{
		PFS::CEnv::SetLastError( ::GetLastError() );
		return false;
	}

	return true;
}

void CInetFile::Close()
{
	if( m_fileio)
	{
		m_fileio->Close();
	}
}

size_t CInetFile::Read( void* data, size_t size)
{
	assert( m_fileio);
	try
	{
		// TODO: if size is large than MAX_DWORD, we should accumulate them
		// The concern only makes sense for IA64.
		return m_fileio->Read( data, size);
	}
	catch( const PFS::CInetIOException& ex )
	{
		PFS::CEnv::SetLastError( ex.GetErrorCode() );
	}

	return PFS::FILE_SIZE_ERROR;
}

size_t CInetFile::Write( const void* data, size_t size)
{
	assert( m_fileio);
	try
	{
		// TODO: if size is large than MAX_DWORD, we should segment them
		// The concern only makes sense for IA64.
		return (size_t)m_fileio->Write( data, (DWORD)size );
	}
	catch( const PFS::CInetIOException& ex )
	{
		if ( ex.GetErrorCode() == ERROR_INVALID_TARGET_HANDLE )
		{
			PFS::CEnv::SetLastError( PFS::EC_FILE_READONLY );
		}
		else
		{
			PFS::CEnv::SetLastError( ex.GetErrorCode() );
		}
	}
	return PFS::FILE_SIZE_ERROR;
}

fsize_t CInetFile::GetSize()
{
	assert( m_fileio);
	try
	{
		return (fsize_t)m_fileio->GetSize();
	}
	catch( const PFS::CInetIOException& ex )
	{
		PFS::CEnv::SetLastError( ex.GetErrorCode() );
	}

	return PFS::FILE_SIZE_ERROR;
}

fsize_t CInetFile::Seek( foff_t pos, PFS::FILE_SEEK_METHOD fsm)
{
	assert( m_fileio);
	try
	{
		return (fsize_t)m_fileio->Seek( (LONG)pos, fsm);
	}
	catch( const PFS::CInetIOException& ex )
	{
		PFS::CEnv::SetLastError( ex.GetErrorCode() );
	}

	return PFS::FILE_SIZE_ERROR;
}

fsize_t CInetFile::GetPos()
{
	assert( m_fileio);
	try
	{
		return (fsize_t)m_fileio->GetPos();
	}
	catch( const PFS::CInetIOException& ex )
	{
		PFS::CEnv::SetLastError( ex.GetErrorCode() );
	}

	return PFS::FILE_SIZE_ERROR;
}

bool CInetFile::SetEnd()
{
	assert( m_fileio );
	
	PFS::CEnv::SetLastError( PFS::EC_FILE_READONLY );

	return false;
}

PFS::CImageData CInetFile::GetImage()
{
	return PFS::BaseFileToImageData( *this);
}

bool CInetFile::ReadAll( void* data, fsize_t size )
{
	assert( m_fileio);
	try
	{
		fsize_t fallsize = (fsize_t)m_fileio->GetSize();
		if ( fallsize > MAX_MEMORY_DATA_SIZE )
		{
			PFS::CEnv::SetLastError( PFS::EC_FILESIZE_TOO_LARGE );
			return false;
		}
		if ( fallsize > size )
		{
			PFS::CEnv::SetLastError( PFS::EC_INSUFFICIENT_BUFFER );
			return false;
		}

		// TODO: if size is large than MAX_DWORD, we should accumulate them
		// The concern only makes sense for IA64.
		DWORD dwRead = m_fileio->Read( data, (DWORD)fallsize );
		if ( dwRead == fallsize )
		{
			return true;
		}
	}
	catch( const PFS::CInetIOException& ex )
	{
		PFS::CEnv::SetLastError( ex.GetErrorCode() );
	}

	return false;
}

} // namespace PFSX