#include <windows.h>
#include <cassert>
#include <string>
#include <map>
#include <ctime>

#include "zipcommon.h"
#include "datablock.h"
#include "zipstream.h"
#include "basefile.h"
#include "filemarshal.h"
#include "stringhelper.h"
#include "zipfile.h"

namespace ZIP {

CFileWriter::CFileWriter() 
: m_written( 0)
, m_current( NULL)
{ 
}

CFileWriter::~CFileWriter() 
{ 
}

void CFileWriter::SetZipLevel( int nLevel) throw ()
{
	m_zsd.SetLevel( nLevel);
}

bool CFileWriter::WriteFileData( const ZIP::CDataBlock& in, bool bFinish) throw ()
{
	return WriteFileData( in.GetData(), in.GetSize(), bFinish);
}

size_t CFileWriter::WriteDeflateData( const ZIP::CDataBlock& in, bool bSeek) throw ()
{
	return WriteDeflateData( in.GetData(), in.GetSize(), bSeek);
}

void CFileWriter::_WriteFileDataUnsafe( const void* in, size_t insize, bool bFinish) throw (...)
{
	if( m_current->m_usMethod == 0)
	{
		m_current->m_uiSizeNow += WriteDeflateData( in, insize, false);
	}
	else
	{
		ZIP::CDataBlock out;
		m_zsd.Deflate( in, insize, out);
		m_current->m_uiSizeNow += WriteDeflateData( out, false);

		if( bFinish)
		{
			m_zsd.End( out);
			m_current->m_uiSizeNow += WriteDeflateData( out, false);
		}
	}

	m_crc.Update( in, insize);
	m_current->m_uiCRC32 = m_crc.GetValue();
	m_current->m_uiSizeOrg += insize;
}

bool CFileWriter::WriteFileData( const void* in, size_t insize, bool bFinish) throw ()
{
	assert( m_current);
	try
	{
		_WriteFileDataUnsafe(in, insize, bFinish);
	}
	catch( CZipStreamException& zse )
	{
		::SetLastError( zse.GetErrorCode() );
		return false;
	}
	catch( CZipWriteError& )
	{
		return false;
	}

	return true;
}

size_t CFileWriter::WriteDeflateData( const void* in, size_t insize, bool bSeek) throw (...)
{
	if( bSeek)
		m_fileio->Seek( m_written, ZIP::FSM_SET);
	size_t len = m_fileio->Write( in, insize);
	if( ZIP::FILE_SIZE_ERROR == len)
		throw CZipWriteError();
	m_written += len;
	return len;
}

size_t CFileWriter::WriteFileHeader( const CFileCentral& fc, bool bSeek) throw()
{
	try
	{
		// 为了避免不必要的Copy对象，下面必须显式调用CFileHeader的相关方法
		size_t dataWritten = sizeof( SIGNATURE_ZIP_FILE_HEADER) + fc.CFileHeader::GetDataSize();
		if( bSeek)
		{
			m_fileio->Seek( fc.m_uiOffset, ZIP::FSM_SET);
			ZIP::CFileStream		fs( *m_fileio);
			fs << SIGNATURE_ZIP_FILE_HEADER;
		
			// 必须显式指定CFileHeader的marshal方法，而不能使用<<操作，
			// 否则无法只序列化FileHeader中的内容
			fc.CFileHeader::marshal( fs );
		}
		else
		{
			// 保留空间，等待 CloseCentral(bSeek 置为true) 时写入完整有效的数据
			fsize_t szPos = m_fileio->Seek( dataWritten, ZIP::FSM_CUR);
			if (szPos == ZIP::FILE_SIZE_ERROR)
			{
				return ZIP::FILE_SIZE_ERROR;
			}
		}
		return dataWritten;
	}
	catch( ZIP::CFileStreamException& )
	{
		return ZIP::FILE_SIZE_ERROR;
	}
	return ZIP::FILE_SIZE_ERROR;
}

bool CFileWriter::WriteEnd() throw()
{
	if ( !m_fileio)
		return false;

	try
	{
		CloseCentral();

		ZIP::CFileStream fs( *m_fileio);

		size_t size = 0;
		
		// ZIP 文件格式
		for( CFileCentralMap::const_iterator itor = m_mapcentral.begin();
			itor != m_mapcentral.end(); ++ itor)
		{
			const CFileCentral& fc = itor->second;
			fs << SIGNATURE_ZIP_FILE_CENTRAL << fc;
			size += sizeof( SIGNATURE_ZIP_FILE_CENTRAL) + fc.GetDataSize();
		}

		fs << SIGNATURE_ZIP_DIR_CENTRAL;

		CDirCentral endCentral;
		endCentral.m_usDiskNumber = 0;
		endCentral.m_usStartOf = 0;
		endCentral.m_usEntriesOnDir = m_mapcentral.size();
		endCentral.m_usEntriesOnDisk = m_mapcentral.size();
		endCentral.m_uiOffsetofDir = m_written;
		endCentral.m_uiSizeofDir = size;
		fs << endCentral;

		m_fileio->SetEnd();
	}
	catch( ZIP::CFileStreamException&)
	{
		return false;
	}
	return true;
}

bool CFileWriter::CloseCentral() throw()
{
	if( m_current)
	{
		// 写入真正CRC,sizeOrg,sizeNow
		size_t res = WriteFileHeader( *m_current, true);
		if( ZIP::FILE_SIZE_ERROR == res)
			return false;

		m_fileio->Seek( m_written, ZIP::FSM_SET);

		m_current = NULL;
	}
	m_crc.Reset();
	return true;
}

inline bool CheckCentralStringFormat( const std::wstring& dir)
{
	if( dir.empty())
		return false;
	if( 0 != dir.find_first_not_of(L'/'))
		return false;
	return true;
}

// from zlib ziplocal_TmzDateToDosDate
static void GetTime( unsigned short& ltime, unsigned short& ldate)
{
	time_t nNow;					// 距1970年1月1日0小时0分0秒的秒数
	time( &nNow );					// 取UTC时间

	struct tm today;
	_localtime64_s( &today, &nNow );	// 将UTC时间转换成本时区的时间

	static unsigned short sdate = 0;
	if( 0 == sdate)
	{
		unsigned short year = today.tm_year;
		if( year > 1980)
			year -= 1980;
		else if( year > 80)
			year -= 80;
		sdate = today.tm_mday + 32 * ( today.tm_mon + 1) + 512 * year;
	}
	ldate = sdate;
	ltime = today.tm_sec/2 + 32 * today.tm_min + 2048 * today.tm_hour;
}

bool CFileWriter::WriteNextCentral( const std::wstring& strCentralName) throw()
{
	const std::wstring& name = strCentralName;
	assert( CheckCentralStringFormat( name));

	if( !CloseCentral() )
	{
		return false;
	}

	std::wstring::size_type pos = name.find_last_of( L'/');
	bool ispath = ( pos == name.size()-1);
	if( pos != std::wstring::npos)
	{
		// 递归写入父目录
		if( ispath)
		{
			pos = name.find_last_of( L'/', name.size()-2);
		}

		if( pos != std::wstring::npos)
		{
			std::wstring slast = name.substr( 0, pos+1);
			if( m_mapcentral.find( slast) == m_mapcentral.end())
			{
				// 父目录不存在才写
				CFileCentral fcLast;
				fcLast.m_name = slast;
				WriteNextCentral( fcLast);
			}
		}
	}

	CFileCentral fc;
	fc.m_name = strCentralName;
	CFileCentralMap::_Pairib pr = m_mapcentral.insert( std::make_pair( fc.m_name, fc));
	if( !pr.second)
		return false;

	m_current = &pr.first->second;

	GetTime( m_current->m_usModifyTime, m_current->m_usModifyDate);

	m_current->m_uiOffset = m_written;
	if( ispath)
		m_current->m_uiExternalFileAttributes = 0x41fd0000;
	else
		m_current->m_uiExternalFileAttributes = 0x81b40000;

	size_t res = WriteFileHeader( *m_current, ispath);
	if( ispath)
	{
		m_current = NULL;
	}
	
	if( ZIP::FILE_SIZE_ERROR == res)
		return false;
	
	m_written += res;

	return true;
}

bool CFileWriter::WriteNextCentral( const CFileCentral& fc) throw()
{
	const std::wstring& name = fc.GetFileName();
	assert( CheckCentralStringFormat( name));

	if( !CloseCentral() )
	{
		return false;
	}

	std::wstring::size_type pos = name.find_last_of( L'/');
	bool ispath = ( pos == name.size()-1);
	if( pos != std::wstring::npos)
	{
		// 递归写入父目录
		if( ispath)
		{
			pos = name.find_last_of( L'/', name.size()-2);
		}

		if( pos != std::wstring::npos)
		{
			std::wstring slast = name.substr( 0, pos+1);
			if( m_mapcentral.find( slast) == m_mapcentral.end())
			{
				// 父目录不存在才写
				CFileCentral fcLast;
				fcLast.m_name = slast;
				WriteNextCentral( fcLast);
			}
		}
	}

	CFileCentralMap::_Pairib pr = m_mapcentral.insert( std::make_pair( fc.m_name, fc));
	if( pr.second)
		m_current = &pr.first->second;
	else
		return false;

	GetTime( m_current->m_usModifyTime, m_current->m_usModifyDate);

	m_current->m_uiOffset = m_written;
	if( ispath)
		m_current->m_uiExternalFileAttributes = 0x41fd0000;
	else
		m_current->m_uiExternalFileAttributes = 0x81b40000;

	size_t res = WriteFileHeader( *m_current, ispath);
	if( ispath )
	{
		m_current = NULL;
	}

	if( ZIP::FILE_SIZE_ERROR == res)
		return false;

	m_written += res;

	return true;
}

// Write file central and file data into zip.
bool CFileWriter::WriteFileData( CBaseFile& rBaseFile ) throw()
{
	const int LEN = 65536;
	unsigned char buffer[LEN] = {0};
	size_t len = (size_t)rBaseFile.GetSize();
	size_t read = 0;
	size_t current = 0;

	for(;;)
	{
		current = rBaseFile.Read( buffer, LEN);
		if( FILE_SIZE_ERROR == current)
		{
			return false;
		}

		read += current;
		bool bFinish = current ? ( read >= len) : true;
		try
		{
			_WriteFileDataUnsafe(buffer, current, bFinish);
		}
		catch( CZipStreamException& zse )
		{
			::SetLastError( zse.GetErrorCode() );
			return false;
		}
		catch( CZipWriteError& )
		{
			return false;
		}

		if( bFinish )
		{
			return true;
		}
	}

	return true;
}


} // namespace ZIP {