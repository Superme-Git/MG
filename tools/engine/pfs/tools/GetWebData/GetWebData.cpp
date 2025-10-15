// GetWebData.cpp : Defines the entry point for the console application.
//
//
// TODO: 考虑扩展该程序的功能，使用第3个命令行参数argv[3]传递共享内存的大小
// [3/2/2010 yanghaibo]
//
#include "stdafx.h"

const size_t g_sShareSize = 1024 * 1024;

void* GetShareMemory( const wchar_t* filename)
{
	HANDLE hImage = OpenFileMapping( FILE_MAP_WRITE, FALSE, filename);
	if( NULL == hImage)
		return NULL;
	void* data = MapViewOfFileEx( hImage, FILE_MAP_WRITE, 0, 0, g_sShareSize, NULL);
	CloseHandle( hImage);
	return data;
}

PFS::CImageData GetNetData( const wchar_t* filename)
{
	PFSX::CInetFile		file;

	if( file.Open( filename))
		return file.GetImage();
	else
		return PFS::CImageData();	
}

class CMappingDataScoped
{
	void* m_data;
public:
	CMappingDataScoped( void* d)
		: m_data( d)
	{}
	~CMappingDataScoped()
	{
		UnmapViewOfFile( m_data);
	}
};

void usage(const _TCHAR* progname)
{
	_ftprintf( stderr, _TEXT("Usage:\n%s <file-url> <sharing-memory-name>\n"), progname);
}

int _tmain(int argc, _TCHAR* argv[])
{
	if( argc < 3 )
	{
		// 参数不足
		_ftprintf( stderr, _TEXT("Not enough arguments!\n") );
		usage( argv[0] );
		return -1;
	}

	void* data = GetShareMemory( argv[2] );
	if( NULL == data )
	{
		// 无效的共享内存对象名
		_ftprintf( stderr, _TEXT("Invalid sharing memory name!\n") );
		return -2;
	}

	CMappingDataScoped		mappingdatascoped( data);

	PFS::CImageData file = GetNetData( argv[1] );
	
	size_t filesize = (size_t)file.GetSize();
	if( 0 == filesize )
	{
		// 获取资源失败(空的网络资源数据)
		_ftprintf( stderr, _TEXT("Get network resource failed!\nNo data can be fetched from %s(%d).\n"),
			argv[1], PFS::CEnv::GetLastError() );
		return -3;
	}

	if( (filesize + sizeof(size_t) ) > g_sShareSize )
	{
		// 网络资源数据太大，超过1M-4B个字节
		_ftprintf( stderr, _TEXT("Network resource is too large (%d bytes) - larger than %d bytes.\n."), filesize, g_sShareSize-sizeof(size_t) );
		return -4;
	}

	*((size_t*)data) = filesize;
	memcpy( (unsigned char*)data + sizeof(size_t), file.GetData(), filesize );

	return 0;
}

