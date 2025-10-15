#pragma once
namespace PFS
{

	//////////////////////////////////////////////////////////////////////////
	// 
	// �ر�˵��:
	//	CFile �� open ��������pfs·������nativeϵͳ·����.
	//  ������filename�����ַ��Ƿ�Ϊ'/'����.
	//
	class CFile : public CBaseFile
	{
	private:
		CBaseFile*	m_file;

	protected:
		void AttackBaseFile( CBaseFile* file)
		{ 
			assert( NULL == m_file);
			m_file = file;
		}
		CBaseFile* DetackBaseFile() 
		{ 
			CBaseFile* temp = m_file; 
			m_file = NULL; 
			return temp; 
		}
	public:
		CFile() : m_file( NULL) {}
		virtual ~CFile() 
		{ 
			if( m_file) 
				CEnv::CloseFile( m_file); 
		}
	public:
		CBaseFile& GetBaseFile() { return *m_file; }
	public:
		virtual size_t Read( void* data, size_t size) 
		{ 
			assert( m_file); 
			return m_file->Read( data, size); 
		}
		virtual size_t Write( const void* data, size_t size) 
		{ 
			assert( m_file); 
			return m_file->Write( data, size); 
		}
		virtual fsize_t GetSize() 
		{ 
			assert( m_file); 
			return m_file->GetSize(); 
		}
		virtual fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm) 
		{ 
			assert( m_file); 
			return m_file->Seek( pos, fsm); 
		}
		virtual fsize_t GetPos() 
		{ 
			assert( m_file); 
			return m_file->GetPos(); 
		}
		virtual bool SetEnd() 
		{ 
			assert( m_file); 
			return m_file->SetEnd(); 
		}
		virtual CImageData GetImage() 
		{ 
			assert( m_file); 
			return m_file->GetImage(); 
		}

		virtual bool ReadAll( void* data, fsize_t size )
		{
			assert( m_file); 
			return m_file->ReadAll(data, size);
		}

	public:
		virtual bool Open( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa);
		virtual void Close();

	public:
		// ����3��������filename,src,dst֧��windows filename ��pfs filename
		static bool IsFileExisting( const std::wstring& filename );
		static bool RemoveFile( const std::wstring& filename);

		// �ر�˵��:
		// ����2��CopyFile����ʵ����ͬ���߼�, ͬʱ�ṩ2������Ϊ�˱���Windows��ͷ�ļ�
		// ����CopyFile������µ����Ƹ���.
#pragma push_macro("CopyFile")
#if defined CopyFile
#undef CopyFile 
#endif // CopyFile
		static bool CopyFile( const std::wstring& src, const std::wstring& dst, bool failifexsting = true);
		static bool CopyFileW( const std::wstring& src, const std::wstring& dst, bool failifexsting = true);
#pragma pop_macro("CopyFile")
	};

	//////////////////////////////////////////////////////////////////////////
	//
	// ֻ֧�ֶ��������ļ�(Read-Only File)
	//
	class CImageFile : public CBaseFile
	{
	private:
		CImageData	m_data;
		fsize_t		m_sizeOffset;
	public:
		CImageFile() : m_sizeOffset( 0) {}
		CImageFile( const CImageData& id) : m_data( id), m_sizeOffset( 0) {}
		CImageFile( const CDataBlock& data) : m_data( DataBlockToImageData( data)), m_sizeOffset( 0) {}
		virtual ~CImageFile() {}
	public:
		virtual size_t Read( void* data, size_t size)
		{
			fsize_t leftsize = m_data.GetSize() - m_sizeOffset;
			if( leftsize < size)
				size = (size_t)leftsize;
			if( size)
			{
				memcpy( data, (const char*)m_data.GetData() + m_sizeOffset, size);
				m_sizeOffset += size;
			}
			return size;
		}
		virtual size_t Write( const void* data, size_t size)
		{
			return PFS::FILE_SIZE_ERROR;
		}
		virtual fsize_t GetSize()
		{
			return m_data.GetSize();
		}
		virtual fsize_t Seek( foff_t pos, PFS::FILE_SEEK_METHOD fsm)
		{
			fsize_t newoffset = PFS::FILE_SIZE_ERROR;

			switch( fsm)
			{
			case PFS::FSM_CUR:
				newoffset = m_sizeOffset + pos;
				break;
			case PFS::FSM_SET:
				newoffset = pos;
				break;
			case PFS::FSM_END:
				newoffset = m_data.GetSize() + pos;
				break;
			default:
				break;
			}
			if( newoffset > m_data.GetSize())
				return PFS::FILE_SIZE_ERROR;
			m_sizeOffset = newoffset;
			return m_sizeOffset;
		}
		virtual fsize_t GetPos() { return m_sizeOffset; }
		virtual bool SetEnd() { return false; }

		virtual PFS::CImageData GetImage() 
		{ 
			return m_data; 
		}

		virtual bool ReadAll( void* data, fsize_t size )
		{
			if ( m_data.GetSize() > size )
			{

				PFS::CEnv::SetLastError( PFS::EC_INSUFFICIENT_BUFFER );
				return false;
			}

			if( m_data.GetSize() > MAX_MEMORY_DATA_SIZE )
			{
				PFS::CEnv::SetLastError( PFS::EC_FILESIZE_TOO_LARGE);
				return false;
			}

			memcpy( data, m_data.GetData(), (size_t)m_data.GetSize() );
			return true;
		}

	public:
		virtual bool Open( const CImageData& image) 
		{
			m_data = image; 
			m_sizeOffset = 0; 
			return true; 
		}
		virtual bool Open( const CDataBlock& data) 
		{
			m_data = DataBlockToImageData( data); 
			m_sizeOffset = 0; 
			return true; 
		}
	};

	//////////////////////////////////////////////////////////////////////////
	//
	// ֻ֧��д�������ڴ��ļ�(Write-Only Memory File)
	//
	class CDataFile : public CBaseFile
	{
	protected:	
		CDataBlock		m_data;
		size_t			m_offset;
	public:
		CDataFile(): m_offset( 0) {}
		virtual ~CDataFile() {}
	public:
		virtual size_t Read( void* data, size_t size)
		{
			PFS::CEnv::SetLastError( PFS::EC_FILE_WRITEONLY);
			return PFS::FILE_SIZE_ERROR;
		}
		virtual size_t Write( const void* data, size_t size)
		{
			fsize_t orgsize = m_data.GetSize();
			if( m_offset < orgsize)
			{
				if( m_offset + size > orgsize)
				{
					m_data.Resize( m_offset);
					m_data.Append( data, size);
				}
				else
				{
					memcpy( (char*)m_data.GetData()+m_offset, data, size);
				}
			}
			else
			{
				m_data.Resize( m_offset);
				m_data.Append( data, size);
			}

			m_offset += size;
			return size;
		}
		virtual fsize_t GetSize()
		{
			return m_data.GetSize();
		}
		virtual fsize_t Seek( foff_t pos, PFS::FILE_SEEK_METHOD fsm)
		{
			fsize_t	_offset = m_offset;
			switch( fsm)
			{
			case PFS::FSM_CUR:
				_offset += pos;
				break;
			case PFS::FSM_SET:
				_offset = pos;
				break;
			case PFS::FSM_END:
				_offset = m_data.GetSize() + pos;
				break;
			default:
				break;
			}

			m_offset = (size_t)_offset;
			return m_offset;
		}
		virtual fsize_t GetPos()
		{
			return m_offset;
		}
		virtual bool SetEnd() 
		{
			m_data.Resize( m_offset);
			return true; 
		}
		virtual PFS::CImageData GetImage() 
		{ 
			return DataBlockToImageData( m_data); 
		}

		virtual bool ReadAll( void* data, fsize_t size )
		{
			PFS::CEnv::SetLastError( PFS::EC_FILE_WRITEONLY);
			return false;
		}
	};

	//////////////////////////////////////////////////////////////////////////
	//
	// ͬʱ֧�ֶ�д�������ڴ��ļ�(Read-Write-able Memory File)
	//
	class CMemFile : public CDataFile
	{
	public:
		virtual size_t Read( void* data, size_t size)
		{
			fsize_t all = m_data.GetSize();
			size_t read = (size_t)all-m_offset;
			if( read > size)
			{
				read = size;
			}
			if( read > 0 )
			{
				memcpy(data, (char*)m_data.GetData()+m_offset, read);
				m_offset += read;
				return read;
			}
			else
			{
				return 0;
			}
		}

		virtual bool ReadAll( void* data, fsize_t size )
		{
			if ( m_data.GetSize() > size )
			{

				PFS::CEnv::SetLastError( PFS::EC_INSUFFICIENT_BUFFER );
				return false;
			}

			memcpy( data, m_data.GetData(), m_data.GetSize() );
			return true;
		}
	};

} //namespace PFS {