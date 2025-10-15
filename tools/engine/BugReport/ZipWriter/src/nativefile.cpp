#include <cassert>
#include "zipcommon.h"
#include "basefile.h"
#include "rawfileio.h"
#include "nativefile.h"

namespace ZIP {

CNativeFile::CNativeFile() 
	: m_fileio( NULL)
{
}

CNativeFile::~CNativeFile() 
{ 
	if( m_fileio) 
		delete m_fileio; 
}

bool CNativeFile::Open( const std::wstring& filename, ZIP::FILE_MODE fm, ZIP::FILE_ACCESS fa)
{
	CNativeFile::Close();

	try
	{
		m_fileio = new ZIP::CFileIO( filename, fm, fa);
	}
	catch( ZIP::CFileIOException& /*fioe*/)  
	{
		/*ZIP::CEnv::GetOutLogInstance()->Print(L"ERROR: CNativeFile::Open(name:%s, mode:%d,access:%d)(%d)\n", filename.c_str(), fm, fa, fioe.GetLastError() );
		ZIP::CEnv::SetLastError( fioe.GetLastError());*/
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
	catch( ZIP::CFileIOException& /*fioe*/)  
	{
		//ZIP::CEnv::SetLastError( fioe.GetLastError());
	}
	return ZIP::FILE_SIZE_ERROR;
}

size_t CNativeFile::Write( const void* data, size_t size)
{
	assert( m_fileio);
	try
	{
		return m_fileio->Write( data, size);
	}
	catch( ZIP::CFileIOException& /*fioe*/)
	{
		//ZIP::CEnv::SetLastError( fioe.GetLastError());
	}
	return ZIP::FILE_SIZE_ERROR;
}

fsize_t CNativeFile::GetSize()
{
	assert( m_fileio);
	try
	{
		return m_fileio->GetSize();
	}
	catch( ZIP::CFileIOException& /*fioe*/)
	{
		//ZIP::CEnv::SetLastError( fioe.GetLastError());
	}
	return ZIP::FILE_SIZE_ERROR;
}

fsize_t CNativeFile::Seek( foff_t pos, ZIP::FILE_SEEK_METHOD fsm)
{
	assert( m_fileio);
	try
	{
		return m_fileio->Seek( pos, fsm);
	}
	catch( ZIP::CFileIOException& /*fioe*/)
	{
		//ZIP::CEnv::SetLastError( fioe.GetLastError());
	}

	return ZIP::FILE_SIZE_ERROR;
}

fsize_t CNativeFile::GetPos()
{
	assert( m_fileio);
	try
	{
		return m_fileio->GetPos();
	}
	catch( ZIP::CFileIOException& /*fioe*/)
	{
		//ZIP::CEnv::SetLastError( fioe.GetLastError());
	}
	return ZIP::FILE_SIZE_ERROR;
}

bool CNativeFile::SetEnd()
{
	assert( m_fileio);
	try
	{
		return m_fileio->SetEnd();
	}
	catch( ZIP::CFileIOException& /*fioe*/)
	{
		//ZIP::CEnv::SetLastError( fioe.GetLastError());
	}
	return false;
}

bool CNativeFile::ReadAll( void* data, fsize_t size )
{
	if( NULL == m_fileio)
	{
		//ZIP::CEnv::SetLastError( ZIP::EC_FILE_NOTOPEN );
		return false;
	}

	fsize_t allsize = m_fileio->GetSize();
	if ( allsize > size )
	{
		//ZIP::CEnv::SetLastError( ZIP::EC_INSUFFICIENT_BUFFER );
		return false;
	}

	if( allsize > MAX_MEMORY_DATA_SIZE )
	{
		//ZIP::CEnv::SetLastError( ZIP::EC_FILESIZE_TOO_LARGE);
		return false;
	}

	// TODO:
	return false;
}

} // namespace ZIP {