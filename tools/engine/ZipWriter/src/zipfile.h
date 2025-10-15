#pragma once

namespace ZIP
{
	class CFileCentralBase
	{
	public:
		virtual unsigned int GetDataOffset() const = 0;
		virtual unsigned int GetOrginalSize() const = 0;
		virtual unsigned int GetCompressedSize() const = 0;
		virtual unsigned short GetMethod() const = 0;
		virtual bool IsRawData() const = 0;
	};

	class CMarshalData : public ZIP::CFileMarshal
	{
		ZIP::CDataBlock&		m_data;
	protected:
		ZIP::CDataBlock& GetData() { return m_data; }
	public:
		explicit CMarshalData( ZIP::CDataBlock& data, size_t s) : m_data( data) { m_data.Resize( s); }
		explicit CMarshalData( const ZIP::CDataBlock& data) : m_data( const_cast<ZIP::CDataBlock&>( data)) {}
		virtual ~CMarshalData() {}
	public:
		virtual ZIP::CFileStream& marshal( ZIP::CFileStream & os) const
		{
			os.push_byte( m_data.GetData(), m_data.GetSize());
			return os;
		}
		virtual const ZIP::CFileStream& unmarshal( const ZIP::CFileStream & os)
		{
			os.pop_byte( m_data.GetData(), m_data.GetSize());
			return os;
		}
	};

	class CDirCentral : public ZIP::CFileMarshal
	{
	public:
		unsigned short	m_usDiskNumber;
		unsigned short	m_usStartOf;
		unsigned short	m_usEntriesOnDisk;
		unsigned short	m_usEntriesOnDir;
		unsigned int	m_uiSizeofDir;
		unsigned int	m_uiOffsetofDir;
		ZIP::CDataBlock	m_Comment;
	public:
		CDirCentral() {}
		virtual ~CDirCentral() {}
	public:
		virtual ZIP::CFileStream& marshal( ZIP::CFileStream & os) const
		{
			unsigned short	usCmtLength = (unsigned short)m_Comment.GetSize();
			os << m_usDiskNumber << m_usStartOf << m_usEntriesOnDisk << m_usEntriesOnDir;
			os << m_uiSizeofDir << m_uiOffsetofDir << usCmtLength;
			os << CMarshalData( m_Comment);
			return os;
		}
		virtual const ZIP::CFileStream& unmarshal( const ZIP::CFileStream & os)
		{
			unsigned short	usCmtLength;
			os >> m_usDiskNumber >> m_usStartOf >> m_usEntriesOnDisk >> m_usEntriesOnDir;
			os >> m_uiSizeofDir >> m_uiOffsetofDir >> usCmtLength;
			os >> CMarshalData( m_Comment, usCmtLength);
			return os;
		}
		size_t GetDataSize() const { return sizeof(unsigned short) * 4 + sizeof(unsigned int) * 2 + sizeof(unsigned short) + m_Comment.GetSize(); }
		unsigned int GetOffsetDirectory() const { return m_uiOffsetofDir; }
	};

		
	class CFileHeaderDataDescriptor : public ZIP::CFileMarshal
	{
	public:
		unsigned int	m_uiCRC32;
		unsigned int	m_uiSizeNow;
		unsigned int	m_uiSizeOrg;
	public:
		CFileHeaderDataDescriptor( const CFileHeaderDataDescriptor& src) : m_uiCRC32( src.m_uiCRC32), m_uiSizeNow( src.m_uiSizeNow), m_uiSizeOrg( src.m_uiSizeOrg) { }
		CFileHeaderDataDescriptor& operator=( const CFileHeaderDataDescriptor& src) 
		{
			if( this == &src)
				return *this;
			m_uiCRC32 = src.m_uiCRC32;
			m_uiSizeNow = src.m_uiSizeNow;
			m_uiSizeOrg = src.m_uiSizeOrg;
			return *this;
		}

		CFileHeaderDataDescriptor() : m_uiCRC32(0), m_uiSizeNow(0), m_uiSizeOrg(0) {}
		virtual ~CFileHeaderDataDescriptor() {}
	public:
		virtual ZIP::CFileStream& marshal( ZIP::CFileStream & os) const
		{
			os << m_uiCRC32 << m_uiSizeNow << m_uiSizeOrg;
			return os;
		}
		virtual const ZIP::CFileStream& unmarshal( const ZIP::CFileStream& os)
		{
			os >> m_uiCRC32 >> m_uiSizeNow >> m_uiSizeOrg;
			return os;
		}
		virtual size_t GetDataSize() const { return sizeof(unsigned int) * 3; }
		bool operator==( const CFileHeaderDataDescriptor& dst) const
		{
			return m_uiCRC32 == dst.m_uiCRC32 && m_uiSizeOrg == dst.m_uiSizeOrg && m_uiSizeNow == dst.m_uiSizeNow;
		}
	};

	class CFileHeader : public CFileHeaderDataDescriptor
	{
	public:
		unsigned short	m_usUnzipVesion;
		mutable unsigned short	m_usFlag;
		unsigned short	m_usMethod;
		unsigned short	m_usModifyTime;
		unsigned short	m_usModifyDate;

		std::wstring	m_name;
		ZIP::CDataBlock	m_ExtData;
	public:
		CFileHeader( const CFileHeader& src)
			: CFileHeaderDataDescriptor( src), m_usUnzipVesion( src.m_usUnzipVesion), m_usFlag( src.m_usFlag),
			m_usMethod( src.m_usMethod), m_usModifyDate( src.m_usModifyDate), m_usModifyTime( src.m_usModifyTime), 
			m_name( src.m_name), m_ExtData( src.m_ExtData)
		{}
		CFileHeader& operator=( const CFileHeader& src)
		{
			if( this == &src)
				return *this;
			CFileHeaderDataDescriptor::operator=( src);
			m_usUnzipVesion = src.m_usUnzipVesion;
			m_usFlag = src.m_usFlag;
			m_usMethod = src.m_usMethod;
			m_usModifyDate = src.m_usModifyDate;
			m_usModifyTime = src.m_usModifyTime;
			m_name = src.m_name;
			m_ExtData = src.m_ExtData;
			return *this;
		}

		CFileHeader() : m_usUnzipVesion(0x0014), m_usFlag(0), m_usMethod(8) {}
		
		virtual ~CFileHeader() {}
	protected:
		inline void unmarshalName( const ZIP::CFileStream & os, unsigned short usNameLength)
		{
			std::string str;
			str.resize( usNameLength);
			os.pop_byte( const_cast<char*>( str.c_str()), usNameLength);
			CStringHelper::MultiByteToWideChar( str, m_name);
		}
	public:
		virtual size_t GetDataSize() const 
		{ 
			return CFileHeaderDataDescriptor::GetDataSize() + sizeof(unsigned short) * 5 + sizeof(unsigned short) * 2 
				+ CStringHelper::GetMultiByteSize( m_name) + m_ExtData.GetSize(); 
		}

		virtual ZIP::CFileStream& marshal( ZIP::CFileStream& os) const
		{
			std::string str;
			CStringHelper::WideCharToMultiByte( m_name, str);
			unsigned short	usFileLength = (unsigned short)str.size();
			unsigned short	usExtLength = (unsigned short)m_ExtData.GetSize();

			os << m_usUnzipVesion << m_usFlag << m_usMethod << m_usModifyTime << m_usModifyDate;
			os << m_uiCRC32 << m_uiSizeNow << m_uiSizeOrg << usFileLength << usExtLength;

			os.push_byte( str.c_str(), str.size());
			if( usExtLength)
				os << CMarshalData( m_ExtData);
			return os;
		}
		virtual const ZIP::CFileStream& unmarshal( const ZIP::CFileStream & os)
		{
			unsigned short	usFileLength;
			unsigned short	usExtLength;

			os >> m_usUnzipVesion >> m_usFlag >> m_usMethod >> m_usModifyTime >> m_usModifyDate;
			os >> m_uiCRC32 >> m_uiSizeNow >> m_uiSizeOrg >> usFileLength >> usExtLength;

			unmarshalName( os, usFileLength);
			if( usExtLength)
				os >> CMarshalData( m_ExtData, usExtLength);

			return os;
		}
		//unsigned int GetCompressedSize() const { return m_uiSizeNow; }
		//unsigned int GetOrginalSize() const { return m_uiSizeOrg; }
		const std::wstring& GetFileName() const { return m_name; }
		bool IsDirectory() const { assert(m_name.size() > 0); return m_name[m_name.size()-1] == L'/'; }
		bool IsHasDataDescriptor() const { enum { BIT = 0x0004,}; return BIT == ( m_usFlag & BIT); }
		bool CheckDataDescriptor( const CFileHeaderDataDescriptor& dd) const { return dd.m_uiCRC32 == m_uiCRC32 && dd.m_uiSizeNow == m_uiSizeNow && dd.m_uiSizeOrg == m_uiSizeOrg; }

		bool operator==( const CFileHeader& dst) const
		{
			if( !CFileHeaderDataDescriptor::operator==( dst))
				return false;
			return m_usUnzipVesion == dst.m_usUnzipVesion && m_usFlag == dst.m_usFlag && m_usMethod == dst.m_usMethod;
		}
	};

	class CFileCentral : public CFileHeader, public CFileCentralBase
	{
	public:
		unsigned short	m_usVersion;

		unsigned short	m_usDiskNumberStart;
		unsigned short	m_usInternalFileAttributes;
		unsigned int	m_uiExternalFileAttributes;
		unsigned int	m_uiOffset;

		ZIP::CDataBlock	m_FileComment;

		enum
		{
			DELETED		=	0x0100,		/* 删除标记 */
			MODIFIED	=	0x0200,		/* 修改标记 */
		};
	public:
		CFileCentral( const CFileCentral& src)
			: CFileHeader( src), m_usVersion( src.m_usVersion), m_usDiskNumberStart( src.m_usDiskNumberStart), 
			m_usInternalFileAttributes( src.m_usInternalFileAttributes), m_uiExternalFileAttributes( src.m_uiExternalFileAttributes),
			m_uiOffset( src.m_uiOffset), m_FileComment( src.m_FileComment)
		{
		}
		CFileCentral& operator=( const CFileCentral& src)
		{
			if( this == &src)
				return *this;
			CFileHeader::operator=( src);
			m_usVersion = src.m_usVersion;
			m_usDiskNumberStart = src.m_usDiskNumberStart;
			m_usInternalFileAttributes = src.m_usInternalFileAttributes;
			m_uiExternalFileAttributes = src.m_uiExternalFileAttributes;
			m_uiOffset = src.m_uiOffset;
			m_FileComment = src.m_FileComment;
			return *this;
		}
		CFileCentral& operator=( const CFileHeader& src)
		{
			CFileHeader::operator=( src);
			return *this;
		}

		CFileCentral() : m_usVersion( 0x0B14 /*0x0314*/), m_usDiskNumberStart( 0), 
			m_usInternalFileAttributes( 0), m_uiExternalFileAttributes( 0), m_uiOffset( 0) {}
		virtual ~CFileCentral() {}
	public:
		virtual ZIP::CFileStream& marshal( ZIP::CFileStream & os) const
		{
			std::string str;
			CStringHelper::WideCharToMultiByte( m_name, str);
			unsigned short	usFileLength = (unsigned short)str.size();
			unsigned short	usExtLength = (unsigned short)m_ExtData.GetSize();
			unsigned short	usCmtLength = (unsigned short)m_FileComment.GetSize();

			os << m_usVersion << m_usUnzipVesion << m_usFlag << m_usMethod << m_usModifyTime << m_usModifyDate;
			os << m_uiCRC32 << m_uiSizeNow << m_uiSizeOrg << usFileLength << usExtLength << usCmtLength;
			os << m_usDiskNumberStart << m_usInternalFileAttributes << m_uiExternalFileAttributes << m_uiOffset;

			//os << CMarshalData( FileData);
			os.push_byte( str.c_str(), str.size());
			if( usExtLength)
				os << CMarshalData( m_ExtData);
			if( usCmtLength)
				os << CMarshalData( m_FileComment);
			return os;
		}
		virtual const ZIP::CFileStream& unmarshal( const ZIP::CFileStream & os)
		{
			unsigned short	usFileLength;
			unsigned short	usExtLength;
			unsigned short	usCmtLength;

			os >> m_usVersion >> m_usUnzipVesion >> m_usFlag >> m_usMethod >> m_usModifyTime >> m_usModifyDate;
			os >> m_uiCRC32 >> m_uiSizeNow >> m_uiSizeOrg >> usFileLength >> usExtLength >> usCmtLength;
			os >> m_usDiskNumberStart >> m_usInternalFileAttributes >> m_uiExternalFileAttributes >> m_uiOffset;

			unmarshalName( os, usFileLength);
			if( usExtLength)
				os >> CMarshalData( m_ExtData, usExtLength);
			if( usCmtLength)
				os >> CMarshalData( m_FileComment, usCmtLength);

			return os;
		}
		size_t GetDataSize() const { return CFileHeader::GetDataSize() + sizeof(unsigned short) * 3 + sizeof(unsigned int) * 2 + sizeof( unsigned short) + m_FileComment.GetSize(); }
		unsigned int GetOffsetFileHeader() const { return m_uiOffset; }
		void SetFileDeleted() const
		{
			m_usFlag &= 0x00ff;
			m_usFlag |= DELETED;
		}
		bool IsFileDeleted() const { return m_usFlag & DELETED ? true : false; }
		void SetFileModified() const
		{
			m_usFlag &= 0x00ff;
			m_usFlag |= MODIFIED;
		}
		bool IsFileModified() const { return m_usFlag & MODIFIED ? true : false; }

	public:
		virtual unsigned int GetDataOffset() const
		{ 
			return m_uiOffset + sizeof( unsigned int) + CFileHeader::GetDataSize(); 
		}
		virtual unsigned int GetOrginalSize() const { return m_uiSizeOrg; }
		virtual unsigned int GetCompressedSize() const { return m_uiSizeNow; }
		virtual unsigned short GetMethod() const { return m_usMethod; }
		virtual bool IsRawData() const
		{
			return (0 == m_usMethod && m_uiSizeNow == m_uiSizeOrg);
		}
	};
}

namespace ZIP {
	class CFileBase
	{
	protected:
		CBaseFile*			m_fileio;

	public:
		enum 
		{
			SIGNATURE_ZIP_FILE_HEADER	= 0x04034b50,
			SIGNATURE_ZIP_FILE_CENTRAL	= 0x02014b50,
			SIGNATURE_ZIP_DIR_CENTRAL	= 0x06054b50,
		};

		CFileBase() : m_fileio(NULL) {}
		virtual ~CFileBase() {}
		virtual void SetFileIO( ZIP::CBaseFile& fio) { m_fileio = &fio; }
		ZIP::CBaseFile& GetFileIO() { assert( m_fileio); return *m_fileio; }
		virtual void CloseFile()
		{
			m_fileio = NULL;
		}

		operator ZIP::CBaseFile&( )
		{
			assert(m_fileio) ;
			return *m_fileio;
		}

		operator const ZIP::CBaseFile&() const
		{
			assert(m_fileio);
			return *m_fileio;
		}
	};

	
	class CBaseFile;

	class CFileWriter : public CFileBase
	{
	protected:
		unsigned int		m_written;
		CFileCentral*		m_current;
		CZipStreamDeflate	m_zsd;
		CCRC32				m_crc;

		typedef std::map<std::wstring, CFileCentral> CFileCentralMap;
		CFileCentralMap m_mapcentral;

	public:
		CFileWriter();
		virtual ~CFileWriter();

	public:
		void SetZipLevel( int nLevel) throw();

		bool WriteNextCentral( const CFileCentral& fc) throw();
		bool WriteNextCentral( const std::wstring& strCentralName) throw();

		// Write file data into zip.
		bool WriteFileData( CBaseFile& rBaseFile ) throw();

		bool WriteFileData( const ZIP::CDataBlock& in, bool bFinish) throw();

		bool WriteFileData( const void* in, size_t insize, bool bFinish) throw();
		
		virtual void CloseFile() throw ()
		{
			WriteEnd();
			CFileBase::CloseFile();
			m_mapcentral.clear();
			m_written = 0;
		}

	private:
		bool CloseCentral() throw();
		bool WriteEnd() throw();

	protected:
		// returns ZIP::FILE_SIZE_ERROR if failed.
		size_t WriteFileHeader( const CFileCentral& fc, bool bSeek ) throw();
		
		// throws CFileWriteError if failed.
		size_t WriteDeflateData( const ZIP::CDataBlock& in, bool bSeek) throw (...);
		size_t WriteDeflateData( const void* in, size_t insize, bool bSeek) throw (...);

		// throws CZipStreamException if failed.
		void _WriteFileDataUnsafe( const void* in, size_t insize, bool bFinish)throw (...);
		
	public:
		// 主要在 CArrangeZipUnCompact 中使用

		// 预先计算压缩后大小
		size_t DeflateBound( size_t sourceLen)	
		{
			return m_zsd.Bound( sourceLen);
		}

		// 根据文件名得到对应的 CFileCentral 指针
		const CFileCentral* GetFileCentral( const std::wstring& name) const
		{
			CFileCentralMap::const_iterator it = m_mapcentral.find( name);
			if( it == m_mapcentral.end())
				return NULL;
			return &( it->second);
		}

		// 得到当前正在操作的 CFileCentral
		const CFileCentral* GetCurCentral( ) const
		{
			return m_current;
		}
	};

} // namespace ZIP {
