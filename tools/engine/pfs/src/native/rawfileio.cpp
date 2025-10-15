#include "../common.h"
#include "rawfileio.h"
#include "../timelog/timelog.h"

namespace PFS {

#ifdef _MSC_VER

static size_t GetAllocationGranularity()
{
	SYSTEM_INFO		si;
	GetSystemInfo( &si);
	return si.dwAllocationGranularity;
}

//
//static size_t GetPageSize()
//{
//	SYSTEM_INFO		si;
//	GetSystemInfo( &si);
//	return si.dwPageSize;
//}
//
//static int g_ImageDummy = 0;
//
//int MakeImageDummyValue( unsigned char* data, fsize_t size)
//{
//	static size_t pagesize = GetPageSize();
//	if( 0 == size)
//		return 0;
//
//	size_t count = (size_t)size / pagesize;
//	int		result = (int)data[0];
//	for( size_t i = 1; i < count; i ++)
//		result = (int)( *(data + (i * pagesize)));
//	return result;
//}
//
//int MakeImageDummyValue( unsigned char* base, unsigned char* data, size_t size)
//{
//	static size_t pagesize = GetPageSize();
//	if( 0 == size)
//		return 0;
//
//	unsigned char* start = (( data - base) / pagesize) * pagesize + base;
//	size_t count = data + size - start;
//	return MakeImageDummyValue( start, count);
//}

void CFileImageHandle::CCountableHandle::AddRef() 
{ 
	assert( m_counter > 0);			
	InterlockedIncrement( &m_counter );			
}

void CFileImageHandle::CCountableHandle::Release() 
{
	if( 0 == InterlockedDecrement( &m_counter ) ) 
		delete this; 
}

CFileImageHandle::CFileImageHandle( CFileIO& fio) 
{
	if( INVALID_HANDLE_VALUE == fio.m_file)
		fio.ThrowFileIOException( ERROR_INVALID_PARAMETER);
	HANDLE image = CreateFileMapping( fio.m_file, NULL, PAGE_READONLY, 0, 0, NULL);
	if( NULL == image)
		fio.ThrowFileIOException( ::GetLastError());
	m_image = new CCountableHandle( image);
	m_size = fio.GetSize();
	m_bNew = true;
}

CFileImageHandle::CFileImageHandle( const wchar_t* name, size_t size)
{
	HANDLE image = CreateFileMapping( INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, size, name);
	if( NULL == image)
	{
#ifdef _DEBUG		
		throw CFileIOException( ::GetLastError(), name);
#else
		throw CFileIOException( ::GetLastError());
#endif
	}
	m_bNew = ERROR_ALREADY_EXISTS != ::GetLastError();
	m_image = new CCountableHandle( image);
	m_size = size;
}

bool CFileImageHandle::Initialize( CFileIO& fio)
{
	if( m_image)
	{
		m_image->Release();
		m_image = NULL;
	}

	m_size = 0;

	if( INVALID_HANDLE_VALUE == fio.m_file)
		return false;

	HANDLE image = CreateFileMapping( fio.m_file, NULL, PAGE_READONLY, 0, 0, NULL);
	if( NULL == image)
	{
		return false;
	}

	m_image = new CCountableHandle( image);
	m_size = fio.GetSize();
	return true;
}

CFileImage::CFileImage( CFileImageHandle& fih) : CAbstactFileImage( fih) 
{
#ifdef OUT_SYNC_FILEIO_TIME_LOG___
	struct CInlineFunc
	{
		static std::wstring MakeHandleName( CFileImageHandle& fih)
		{
			std::wstringstream	wss;

			wss << L"handle = " << (DWORD)fih.GetHandle() << L" size = " << fih.GetSize();
			return wss.str();
		}
	};
	PFSX::helper::CLogScoped		log_scoped( L"CFileImage::CFileImage", CInlineFunc::MakeHandleName( fih).c_str());
#endif

	HANDLE hImage = fih.GetHandle();
	if( NULL == hImage)
		throw CFileIOException( ERROR_INVALID_PARAMETER);
	m_data = MapViewOfFileEx( hImage, FILE_MAP_READ, 0, 0, 0, NULL);
	if( NULL == m_data)
		throw CFileIOException( ::GetLastError());
	m_size = fih.GetSize();
	//g_ImageDummy = MakeImageDummyValue( (unsigned char*)m_data, m_size);
}

CFileImage::~CFileImage()
{
	if( m_data != NULL )
	{
		UnmapViewOfFile( m_data);
		m_data = NULL;
	}
}


CPieceFileImage::CPieceFileImage( CFileImageHandle& fih, fsize_t offset, size_t size) : CAbstactFileImage( fih)
{
#ifdef OUT_SYNC_FILEIO_TIME_LOG___
	struct CInlineFunc
	{
		static std::wstring MakeHandleName( CFileImageHandle& fih, size_t offset, size_t size)
		{
			std::wstringstream	wss;

			wss << L"handle = " << (DWORD)fih.GetHandle() << L"all size = " << fih.GetSize() << L" offset = " << offset << L" size = " << size;
			return wss.str();
		}
	};
	PFSX::helper::CLogScoped		log_scoped( L"CPieceFileImage::CPieceFileImage", CInlineFunc::MakeHandleName( fih, offset, size).c_str());
#endif

	HANDLE hImage = fih.GetHandle();
	if( NULL == hImage)
		throw CFileIOException( ERROR_INVALID_PARAMETER);

	assert( (offset + size) <= fih.GetSize());

	static const size_t pagesize = GetAllocationGranularity();
	fsize_t startpos = pagesize * (offset / pagesize);
	size_t mapsize = (size_t)(size + offset - startpos);

	m_base = MapViewOfFileEx( hImage, FILE_MAP_READ, (DWORD)(startpos>>32), (DWORD)startpos, mapsize, NULL);
//	DWORD dwError = GetLastError();
	if( NULL == m_base)
		throw CFileIOException( ::GetLastError());
	m_size = size;
	m_data = (unsigned char*)m_base + offset - startpos;
//	g_ImageDummy = MakeImageDummyValue( (unsigned char*)m_base, (unsigned char*)m_data, (size_t)m_size);
}

CPieceFileImage::~CPieceFileImage() 
{
	// 注意与CFileImage的区别
	if ( m_base != NULL )
	{
		UnmapViewOfFile( m_base);
		m_base = NULL;
	}

	m_data = NULL;
}

#else
#endif

} // namespace PFS {