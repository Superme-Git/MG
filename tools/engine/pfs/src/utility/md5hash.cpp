#include "../common.h"
#define __i386__
#include "../../contrib/perf/perf.h"

namespace PFS {
namespace helper {

CMD5Hash::CMD5Hash() 
{
	md5_init_context( m_context);
}

CMD5Hash::~CMD5Hash() {}

void CMD5Hash::Update( const void* data, size_t dataLen)
{
	md5_update( m_context, data, (unsigned int)dataLen);
}

void CMD5Hash::Update( const CDataBlock& data)
{
	md5_update( m_context, data.GetData(), (unsigned int)data.GetSize());
}

void CMD5Hash:: Update( const unsigned long long& data)
{
	md5_update( m_context, &data, (unsigned int)sizeof(data));
}

CDataBlock& CMD5Hash::Final( CDataBlock& digest)
{
	md5_final( m_context);
	digest.Resize( 16);
	memcpy( digest.GetData(), m_context, 16);
	return digest;
}

FILE_DATA_HASH& CMD5Hash::Final( FILE_DATA_HASH& digest)
{
	md5_final( m_context);
	digest = *(FILE_NAME_HASH*)m_context;
	digest ^= *((FILE_NAME_HASH*)m_context + 1);
	return digest;
}

FILE_NAME_HASH& CFileNameHash::Final( FILE_NAME_HASH& digest)
{
	CDataBlock digestBlock;
	m_md5.Final( digestBlock);
	digest = *(FILE_NAME_HASH*)digestBlock.GetData();
	digest ^= *((FILE_NAME_HASH*)digestBlock.GetData() + 1);
	return digest;
}

bool CMD5FileHashMethod::MakeFileHash( CBaseFile& file, CDataBlock& digest) const
{
	CMD5Hash		md5;

	CImageData data = file.GetImage();

	if( NULL == data.GetData() || 0 == data.GetSize())
	{
		// data 有可能无效
		const size_t sizeBuffer = 4096;
		char buffer[sizeBuffer] = {0};
		while( size_t s = file.Read( buffer, sizeBuffer))
		{
			if( FILE_SIZE_ERROR == s)
				return false;
			md5.Update( buffer, s);
		}
	}
	else
	{
		md5.Update( data.GetData(), (size_t)data.GetSize());
	}

	md5.Final( digest);
	return true;
}

bool CMD5FileHashMethod::MakeFileHash( CBaseFile& file, FILE_DATA_HASH& digest) const
{
	CDataBlock digestBlock;
	if( !MakeFileHash(file, digestBlock) )
	{
		return false;
	}
	
	digest = *(FILE_DATA_HASH*)digestBlock.GetData();
	digest ^= *((FILE_DATA_HASH*)digestBlock.GetData() + 1);

	return true;
}

} // namespace helper {
} // namespace PFS {