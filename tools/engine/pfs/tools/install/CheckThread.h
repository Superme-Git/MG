#pragma once
#include "../packzip/basethread.h"
#include "ProgressThreadListener.h"
#include "../../pfs.h"
#include "../../src/meta/metafileio.h"
#include "../../src/utility/md5hash.h"

static void Data2AnsiString( PFS::CDataBlock& in, std::string& out)
{
	static const std::string TABLE = "0123456789abcdef";

	out.clear();
	const char* data = (const char*)in.GetData();
	for( size_t i = 0; i < in.GetSize(); i ++)
	{
		const char& c = data[i];
		out += TABLE[ (c >> 4) & 0x0f];
		out += TABLE[ c & 0x0f];
	}
}

class CCheckThread : public CBaseThread
{
	std::vector<std::wstring>		m_zipfiles;
	std::vector<std::string>		m_md5s;

	virtual void OnRunning()
	{
		for( std::vector<std::wstring>::const_iterator itor = m_zipfiles.begin(); itor!=m_zipfiles.end(); ++itor)
		{
			if( m_stop)
				break;
			PFS::CFile file;
			PFS::CDataBlock md5data;
			if( file.Open( *itor, PFS::FM_EXCL, PFS::FA_RDONLY))
			{
				m_listener->OnNotify( (size_t)file.GetSize(), FILELEN);

				PFS::helper::CMD5Hash md5;
				const int buffersize = 4096;
				PFS::CDataBlock out( buffersize);
				size_t written = 0;
				while( size_t s = file.Read( out.GetData(), buffersize))
				{
					if( m_stop)
						break;
					if( PFS::FILE_SIZE_ERROR == s)
						break;
					out.Resize( s);
					md5.Update( out);
					written += s;
					if( written > 1024*1024)
					{
						// 1M 刷新一次进度条
						m_listener->OnNotify( written, FILEPROGRESS);
						written = 0;
					}
				}
				if( written > 0)
				{
					m_listener->OnNotify( written, FILEPROGRESS);
				}
				md5.Final( md5data);

				std::string temp;
				Data2AnsiString( md5data, temp);
				m_md5s.push_back( temp);
			}
		}
		if( !m_stop)
			m_listener->OnNotify( 1, FILEEND);
	}

public:
	std::vector<std::string>& GetMD5() { return m_md5s; }
	void SetFileName( const std::vector<std::wstring>& zipfiles) { m_zipfiles = zipfiles; }
};