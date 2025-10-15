#include "../common.h"


namespace PFS {

CUncopyableObject::CUncopyableObject( const CUncopyableObject&) {}
CUncopyableObject& CUncopyableObject::operator=( const CUncopyableObject&) { return *this; }
CUncopyableObject::CUncopyableObject() {}
CUncopyableObject::~CUncopyableObject() {}

namespace helper { 

class CMemoryData
{
	enum { 
		SMALL_BLOCK_SIZE = 1024, 
		MEDIUM_BLOCK_SIZE = 1024*8, 
		LARGE_BLOCK_SIZE = 1024 * 1024, // 1M
	};

	size_t		m_cur;
	size_t		m_max;
	char*		m_data;
	volatile LONG m_refcount;

private:
	static size_t GetMaxSize( size_t s)
	{
		s += 3;
		s = s >> 2;
		s = s << 2;
		return s;
	}

	static size_t IncreaseMaxSize( size_t c)
	{
		if( c <= SMALL_BLOCK_SIZE )
		{
			c <<= 1;	// Increasing by double size
		}
		else if( c <= LARGE_BLOCK_SIZE ) 
		{
			c = c + SMALL_BLOCK_SIZE; // Increasing by 1K
		}
		else
		{
			c = c + MEDIUM_BLOCK_SIZE; // Increasing by 8K
		}
		
		c = GetMaxSize(c); // 4字节对齐
		return c;
	}
private:
	CMemoryData( CMemoryData&){}
	CMemoryData& operator = ( CMemoryData&)  { return *this; }

	// 构造一个指定容量capacity大小的内存块数据，但长度为0
	CMemoryData( size_t c) : m_data( NULL), m_cur( 0), m_refcount( 1) 
	{
		m_max = GetMaxSize( c);
		if( m_max != 0 )
		{
			m_data = (char*)::malloc(m_max);
			if( m_data == NULL )
			{
				m_max = 0;
				throw std::bad_alloc();
			}
		}
	}
	~CMemoryData() 
	{
		assert( 0 == m_refcount);
		if( m_data != NULL ) 
		{
			::free(m_data);
			m_data = NULL;
		}
	}
public:
	static CMemoryData* Create( size_t c = 0)
	{
		return new CMemoryData( c);
	}

	// 有预见性的重生
	CMemoryData* Spawn( size_t c, size_t s ) const
	{
		CMemoryData* egg = new CMemoryData(c);
		if( s > 0 )
		{
			// 保留现有的数据
			egg->Append( m_data, min( m_cur, s) );

			// 置新的大小
			egg->m_cur = s;
		}
		return egg;
	}

	LONG Release()
	{
		assert( m_refcount > 0);
		if( LONG lResult = InterlockedDecrement( &m_refcount ) )
		{
			return lResult;
		}
		else
		{
			delete this;
			return 0;
		}
	}
	LONG AddRef()
	{
		assert( m_refcount > 0);
		return InterlockedIncrement( &m_refcount );
	}
	
	LONG RefCount() const
	{
		return InterlockedExchangeAdd( (volatile LONG *)&m_refcount, 0);
	}

	bool Compare( const CMemoryData& dst) const
	{
		if( m_data == dst.m_data)
			return true;
		if( m_cur != dst.m_cur)
			return false;
		return 0 == memcmp( m_data, dst.m_data, m_cur);
	}
	void* GetData() const
	{
		return m_data;
	}
	size_t GetSize() const
	{
		return m_cur;
	}
	void Resize( size_t s)
	{
		Reserve( s);
		m_cur = s;
	}
	void Reserve( size_t s)
	{
		if( m_max >= s)
			return;
		size_t _max = GetMaxSize( s);
		char* _data = (char*)::realloc( m_data, _max );
		if( _data == NULL )
			throw std::bad_alloc();
		m_data = _data;
		m_max = _max;
	}
	CMemoryData* Clone() const
	{
		CMemoryData* data = new CMemoryData(m_max); // same capacity
		if( m_cur > 0 )
		{
			data->m_cur = m_cur; // same size
			memcpy( data->m_data, m_data, m_cur); // same data
		}
		return data;
	}
	void Clear( bool deep)
	{
		if( deep)
		{
			if( m_data != NULL )
			{
				::free( m_data );
				m_data = NULL;
			}
			m_max = 0;
		}
		m_cur = 0;
	}
	
	void Append( const void* data, size_t size)
	{
		if( m_max - m_cur < size)
		{
			size_t _max = IncreaseMaxSize( m_cur + size);

			if ( m_data <= data || (char *)m_data >= ((char *)data + m_cur) )
			{
				char* _data = (char*)::realloc(m_data, _max);
				if( _data == NULL )
					throw std::bad_alloc();

				m_data = _data;
				m_max = _max;

				// 无重叠Copy
				::memcpy_s( &m_data[m_cur], m_max, data, size);
			}
			else
			{
				char* newdata = (char*)::malloc(_max);
				if ( newdata == NULL )
					throw std::bad_alloc();

				m_max = _max;
				::memcpy_s( newdata, m_max, m_data, m_cur);
				::memcpy_s( &newdata[m_cur], m_max, data, size);
				m_data = newdata;
			}
		}
		else
		{
			// 使用memmove_s 而不使用memcpy_s 防止内存重叠情况下的拷贝问题
			::memmove_s( &m_data[m_cur], m_max, data, size);
		}

		m_cur += size;
	}
	void Dump() const
	{
		for( size_t i = 0; i < m_cur; i ++)
			printf( "%02x ", m_data[i]);
	}
};

} // namespace helper { 


CDataBlock::CDataBlock( size_t c) 
	: m_data( helper::CMemoryData::Create(c))
{
}

CDataBlock::CDataBlock( const void* data, size_t s) 
	: m_data(helper::CMemoryData::Create(s)) 
{
	if( m_data ) 
	{
		m_data->Append(data, s);
	}
}

CDataBlock::~CDataBlock()
{ 
	if( m_data )
	{
		m_data->Release();
	}
}

CDataBlock::CDataBlock( const CDataBlock& src)
	: m_data( src.m_data)
{
	if( m_data ) 
	{
		m_data->AddRef();
	}
}

CDataBlock& CDataBlock::operator = ( const CDataBlock& src)
{
	if( this == &src)
		return *this;
	if( m_data )
		m_data->Release();
	m_data = src.m_data;
	if( m_data )
		m_data->AddRef();
	return *this;
}

void CDataBlock::Unique()
{
	if( 1 == m_data->RefCount())
		return;
	helper::CMemoryData* data = m_data->Clone();
	m_data->Release();
	m_data = data;
}

void CDataBlock::UniqueReserveAndResize(size_t c, size_t s)
{
	assert( c >= s );
	if( 1 == m_data->RefCount())
	{
		if( c != s )
		{
			m_data->Reserve(c);
		}

		// new size is s
		m_data->Resize(s);
		return;
	}
	else
	{
		// new size is s;
		helper::CMemoryData* data = m_data->Spawn(c, s);
		m_data->Release();
		m_data = data;
	}
}

void* CDataBlock::GetData()
{
	Unique();
	return m_data->GetData();
}

const void* CDataBlock::GetData() const
{
	return m_data->GetData();
}

size_t CDataBlock::GetSize() const
{
	return m_data->GetSize();
}

void CDataBlock::Resize( size_t s)
{
	if ( s == PFS::FILE_SIZE_ERROR )
	{
		std::string message = "invalid argument: pass -1 as the data size! ";
		message += __FUNCTION__;
		throw std::invalid_argument(message);
	}

	UniqueReserveAndResize(s, s);
}

void CDataBlock::Reserve( size_t s)
{
	if ( s == PFS::FILE_SIZE_ERROR )
	{
		std::string message = "invalid argument: pass -1 as the data size! ";
		message += __FUNCTION__;
		throw std::invalid_argument(message);
	}

	// do not change the existing data and the current size
	UniqueReserveAndResize(s, GetSize());
}

bool CDataBlock::Compare( const CDataBlock& dst) const
{
	return m_data->Compare( *dst.m_data);
}

bool CDataBlock::operator!=( const CDataBlock& dst) const
{
	return !Compare( dst);
}

bool CDataBlock::operator==( const CDataBlock& dst) const
{
	return Compare( dst);
}

void CDataBlock::Clear( bool deep)
{
	if( 1 == m_data->RefCount())
	{
		m_data->Clear( deep);
	}
	else
	{
		m_data->Release();
		m_data = helper::CMemoryData::Create( 0);
	}
}

void CDataBlock::Assign( const void* data, size_t size)
{
	UniqueReserveAndResize(size, 0);
	m_data->Append( data, size);
}

void CDataBlock::Append( const void* data, size_t size)
{
	size_t curr = m_data->GetSize();
	UniqueReserveAndResize(curr + size, curr);
	m_data->Append( data, size);
}

void CDataBlock::Append( const CDataBlock& data)
{
	Append( data.GetData(), data.GetSize());
}

void CDataBlock::Dump() const
{
	m_data->Dump();
}

void CDataBlock::Swap( CDataBlock& dst)
{
	helper::CMemoryData* temp = dst.m_data; 
	dst.m_data = m_data;
	m_data = temp;
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAbstactImageImplement::CAbstactImageImplement() : m_counter( 1) {}
CAbstactImageImplement::~CAbstactImageImplement() { assert( 0 == m_counter); }

long CAbstactImageImplement::AddRef()
{
	assert( m_counter > 0); 
	return InterlockedIncrement(& m_counter ); 
}

long CAbstactImageImplement::Release()
{
	assert( m_counter > 0);
	if( LONG lResult = InterlockedDecrement(&m_counter) )
	{
		return lResult;
	}
	else
	{
		delete this;
		return 0;
	}
}

CImageData::CImageData() : m_impl( NULL) {}
CImageData::CImageData( const CImageData& src): m_impl( src.m_impl) { if( m_impl) m_impl->AddRef(); }
CImageData::CImageData( CAbstactImageImplement* impl) : m_impl( impl) {}
CImageData::~CImageData() { if( m_impl) m_impl->Release(); }

CImageData& CImageData::operator=( const CImageData& src)
{
	if( this == &src)
		return *this;
	if( m_impl)
		m_impl->Release();
	m_impl = src.m_impl;
	if( m_impl) 
		m_impl->AddRef();
	return *this;
}

CImageData& CImageData::Attach( CAbstactImageImplement* impl) 
{
	if( m_impl)
		m_impl->Release();
	m_impl = impl;
	assert( m_impl);
	return *this;
}

CAbstactImageImplement* CImageData::Detach()
{
	CAbstactImageImplement*	temp = m_impl;
	m_impl = NULL;
	return temp;
}

const void* CImageData::GetData() const
{
	if( m_impl)
		return m_impl->GetData();
	else
		return NULL;
}

fsize_t CImageData::GetSize() const
{
	if( m_impl)
		return m_impl->GetSize();
	else
		return 0;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

namespace helper {

class COctetsImageData : public PFS::CAbstactImageImplement
{
	CDataBlock		m_data;
public:
	COctetsImageData( const CDataBlock& data) : m_data( data) {}
	virtual ~COctetsImageData() {}
public:
	virtual const void* GetData() const
	{
		return m_data.GetData();
	}
	virtual fsize_t GetSize() const
	{
		return m_data.GetSize();
	}
};

} // namespace helper {

PFS::CImageData DataBlockToImageData( const PFS::CDataBlock& data)
{
	return PFS::CImageData( new helper::COctetsImageData( data));
}

PFS::CImageData BaseFileToImageData( PFS::CBaseFile& file)
{
	CDataBlock		Buffer;
	fsize_t	size = file.GetSize();
	if( PFS::FILE_SIZE_ERROR == size)
		return PFS::CImageData(); 
	Buffer.Resize( (size_t)size);
	file.Seek( 0, PFS::FSM_SET);
	size_t sizeRead = file.Read( Buffer.GetData(), Buffer.GetSize());
	if( PFS::FILE_SIZE_ERROR == sizeRead)
		return PFS::CImageData(); 
	Buffer.Resize( sizeRead );
	return DataBlockToImageData( Buffer);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void COutLog::Print( const wchar_t* info, ...)
{
	va_list vl;  

	va_start( vl, info);
	VPrint( info, vl); 
	va_end( vl);
}

} // namespace PFS {