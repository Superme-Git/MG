#include "../common.h"
#include "../zip/ziphelper.h"
#include "../../translate.h"

namespace PFS {

	const static TSID X2O = PFSX::MakeTypeIDByName('X', '2', 'O');

	//
	// .files.meta 文件头结构体
	//
	struct MetaFileHeader
	{
		unsigned short	MagicCode;		// 'FM'  MetaFile
		unsigned short	Version;		// 若为1.0, MAKEWORD(1,0), 0x0100
										// 若为1.1, MAKEWORD(1,1), 0x0101
		unsigned short	HeaderSize;		// V1.0/V1.1 - 大小为 16字节
		unsigned char	FileType;		// 0, FileMeta, 1, VersionMeta, 2, SetupMeta
		unsigned char	CompressMethod;	// 压缩方式，1, 标准Zip压缩
		unsigned int	ItemCount;		// Meta项目数
		unsigned int	Reserved;		// 保留信息，4字节
	};
	PFS_STATIC_ASSERT( sizeof(MetaFileHeader) == 16 );

	// V1.0 的序列化方案
	class CMarshalMetaInfo : public PFS::CFileMarshal
	{
	protected:
		std::wstring&	m_filename;		
		CMetaInfo&		m_data;
	public:
		CMarshalMetaInfo( std::wstring& filename, CMetaInfo& data) : m_filename( filename), m_data( data) {}
		virtual ~CMarshalMetaInfo() {}
	public:
		virtual PFS::CFileStream& marshal( PFS::CFileStream& os) const
		{
			// 原始序列化方案
			//os << m_filename << (unsigned int)m_data.m_sizeFile << m_data.m_nMethod << m_data.m_dataHash <<  m_data.m_attributes << m_data.m_uri;

			// 压缩版序列化方案(忽略了uri字段)
			os << m_filename << (unsigned char)m_data.m_nMethod << m_data.m_dataHash << m_data.m_attributes;
			os.push_size( (size_t)m_data.m_sizeFile ); 
			return os;
		}
		virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream& os)
		{
			// 原始序列化方案
			//os >> m_filename >> m_data.m_sizeFile >> m_data.m_nMethod >> m_data.m_hash >> m_data.m_attributes >> m_data.m_uri;
			
			// 压缩版序列化方案(忽略了uri字段)
			os >> m_filename >> (unsigned char&)m_data.m_nMethod >> m_data.m_dataHash >> m_data.m_attributes;
			m_data.m_sizeFile = os.pop_size();
			return os;
		}
	};

	// V1.1 的序列化方案
	class CMarshalMetaInfo_1_1 : public PFS::CMarshalMetaInfo
	{
	public:
		CMarshalMetaInfo_1_1( std::wstring& filename, CMetaInfo& data) : CMarshalMetaInfo( filename, data ) {}
		virtual ~CMarshalMetaInfo_1_1() {}
	public:
		virtual PFS::CFileStream& marshal( PFS::CFileStream& os) const
		{
			// 压缩版序列化方案(忽略了uri字段)
			os << m_filename << (unsigned char)m_data.m_nMethod << m_data.m_dataHash << m_data.m_attributes;
			os.push_size( (size_t)m_data.m_sizeFile ); 
			// 对于 tsid为 X2O 和 0 的文件来说，下面的2个字段值等同于m_dataHash和m_sizeFile
			// 这种情况下,不序列化到文件中,以节省文件空间.
			if( m_data.m_tsid != 0 && m_data.m_tsid != X2O )
			{
				os.push_size( (size_t)m_data.m_sizeFile2 ); 
				os << m_data.m_dataHash2;
			}
			return os;
		}
		virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream& os)
		{
			// 压缩版序列化方案(忽略了uri字段)
			os >> m_filename >> (unsigned char&)m_data.m_nMethod >> m_data.m_dataHash >> m_data.m_attributes;
			m_data.m_sizeFile = os.pop_size();
			// 对于 tsid为 X2O 和 0 的文件来说，下面的2个字段值等同于m_dataHash和m_sizeFile
			// 这种情况下,不从文件中读出.
			if( m_data.m_tsid != 0 && m_data.m_tsid != X2O )
			{
				m_data.m_sizeFile2 = os.pop_size();
				os >> m_data.m_dataHash2;
			}
			else
			{
				m_data.m_sizeFile2 = m_data.m_sizeFile; 
				m_data.m_dataHash2 = m_data.m_dataHash;
			}
			return os;
		}
	};


	//////////////////////////////////////////////////////////////////////////
	//
	// 下面3个函数实现了文件路径名的上下文压缩方案
	//
	// StringCommonPrefix - 压缩时用于计算可被压缩长度
	// FileNameRealToFake - 压缩
	// FileNameFakeToReal - 解压缩
	//
	// *X 前缀代表了等同于上下文的被压缩了的字符数目
	//
	static size_t StringCommonPrefix( const std::wstring& string1, const std::wstring& string2 )
	{
		size_t count = 0;
		std::wstring::const_pointer p1 = string1.data();
		std::wstring::const_pointer p2 = string2.data();
		while ( *p1 && (*(p1++) == *(p2++)) )
		{
			count ++;
		}

		return count;
	}

	static void FileNameRealToFake( const std::wstring& filenameReal, std::wstring& filenameFake, std::wstring& ctx )
	{
		size_t commonPrefixCount = StringCommonPrefix( filenameReal, ctx );
		if( commonPrefixCount > 4 ) // 3 为有意义压缩的最小阈值, 合理值取5
		{
			filenameFake[0] = L'*';
			filenameFake[1] = (std::wstring::value_type)commonPrefixCount;
			filenameFake.replace( filenameFake.begin() + 2, filenameFake.end(), filenameReal.begin() + commonPrefixCount, filenameReal.end() );
		}
		else
		{
			filenameFake = filenameReal;
		}
		ctx = filenameReal;
	}

	static void FileNameFakeToReal( std::wstring& filenameFake, std::wstring& filenameReal, std::wstring& ctx )
	{
		if ( filenameFake[0] == L'*' )
		{
			filenameReal.assign( &*ctx.begin(), (size_t)filenameFake[1] );
			filenameReal.replace(filenameReal.begin() + filenameFake[1], filenameReal.end(), filenameFake.begin() + 2, filenameFake.end() );
		}
		else
		{
			filenameReal = filenameFake;
		}

		ctx = filenameReal;
	}

	bool CMetaRead::DoParseFile( CMetaInfoMap& mm, unsigned short usVersion, unsigned int nCount)
	{
		try
		{
			CFileStream fs( GetFile());

			std::wstring filenameFake;
			std::wstring filenameReal;
			std::wstring ctx;
			CMetaInfo meta;
			unsigned int count = nCount; // 最多读取count个
			if( usVersion == 0x0100 )
			{
				while( count -- )
				{
					fs >> CMarshalMetaInfo( filenameFake, meta);
					FileNameFakeToReal( filenameFake, filenameReal, ctx );
					mm.insert( std::make_pair( filenameReal, meta) );
				}
			}
			else if( usVersion == 0x0101 )
			{
				while( count -- )
				{
					fs >> CMarshalMetaInfo_1_1( filenameFake, meta);
					FileNameFakeToReal( filenameFake, filenameReal, ctx );
					mm.insert( std::make_pair( filenameReal, meta) );
				}
			}
			else
			{
				// 不支持的版本
				return false;
			}
			return true;
		}
		catch( CFileStreamException&)
		{
			// 上面的while(true)总是以抛异常方式终止文件解析
			// 所以即使读取成功,这里也会有异常被捕获.
			// 因此,是否有异常产生,不是文件是否完整的标志.
			// 这里不能抛CMetaIOError.
			//
			// throw CMetaIOError();
		}

		return false;
	}

	void CMetaWrite::DoDumpFile( const CMetaInfoMap& mm, unsigned short usVersion )
	{
		CBaseFile&	basefile = GetFile();
		try
		{
			CFileStream	fs( basefile);
			std::wstring filenameFake, ctx;
			if( usVersion == 0x0100 )
			{
				for( CMetaInfoMap::const_iterator it = mm.begin(); it != mm.end(); ++ it)
				{
					const std::wstring& filenameReal = it->first;
					const CMetaInfo& mi = it->second;
					FileNameRealToFake( filenameReal, filenameFake, ctx );
					fs << CMarshalMetaInfo( const_cast<std::wstring&>(filenameFake), const_cast<CMetaInfo&>( mi));
				}
			}
			else if( usVersion == 0x0101 )
			{
				for( CMetaInfoMap::const_iterator it = mm.begin(); it != mm.end(); ++ it)
				{
					const std::wstring& filenameReal = it->first;
					const CMetaInfo& mi = it->second;
					FileNameRealToFake( filenameReal, filenameFake, ctx );
					fs << CMarshalMetaInfo_1_1( const_cast<std::wstring&>(filenameFake), const_cast<CMetaInfo&>( mi));
				}
			}
			else
			{
				// 不支持的版本，暂时抛这个异常
				throw CMetaIOError();
			}
		}
		catch( CFileStreamException&)
		{
			throw CMetaIOError();
		}
		if( !basefile.SetEnd())
			throw CMetaIOError();
	}

	CMetaFileRead::CMetaFileRead( CBaseFile& basefile)
		: m_file( basefile, FILE_SIZE_ERROR)
	{
	}

	bool CMetaFileRead::DoRead( CBaseFile& basefile, CMetaInfoMap& mm)
	{
		PFS::CEnv::SetLastError( EC_INVALID_META_FILE );

		try
		{
			// 使用baseFile读取数据头(未压缩数据)
			MetaFileHeader mfh;
			if( basefile.Read( &mfh, sizeof(MetaFileHeader) ) != sizeof(MetaFileHeader) )
			{
				return false;
			}

			if( mfh.CompressMethod != 1 ||
				mfh.FileType != 0 ||
				mfh.HeaderSize != sizeof(MetaFileHeader) ||
				mfh.MagicCode != 'FM' )
			{
				return false;
			}

			if( mfh.Version != 0x0100 &&
				mfh.Version != 0x0101 )
			{
				PFS::CEnv::SetLastError( EC_UNSUPPORT_META_FILE_VERSION );
				return false;
			}

			if( mfh.ItemCount > 0 )
			{
				CMetaFileRead dummy( basefile );
				if( !dummy.DoParseFile( mm, mfh.Version, mfh.ItemCount ) )
				{
					return false;
				}

				if ( mm.size() != mfh.ItemCount )
				{
					return false;
				}
			}
			else
			{
				// empty file?
				return true;
			}
		}
		catch( CMetaIOError&)
		{
			return false;
		}

		PFS::CEnv::SetLastError( EC_SUCCESS );
		return true;
	}

	CMetaDataRead::CMetaDataRead( const CDataBlock& data)
		: m_imagefile( data), m_file( m_imagefile, FILE_SIZE_ERROR) // 这里传入的FILE_SIZE_ERROR并没有意义
	{
		// 跳过文件头
		// 使用m_imagefile的Seek方法避免不必要的内存拷贝
		if( m_imagefile.Seek( sizeof(MetaFileHeader), PFS::FSM_SET ) != 
			sizeof(MetaFileHeader) )
		{
			throw CMetaIOError();
		}
	}

	bool CMetaDataRead::DoRead( const CDataBlock& data, CMetaInfoMap& mm)
	{
		PFS::CEnv::SetLastError( EC_INVALID_META_FILE );

		try
		{
			// 检查数据头
			if ( data.GetSize() < sizeof(MetaFileHeader))
			{
				return false;
			}

			const MetaFileHeader& mfh = *(const MetaFileHeader*)data.GetData();
			if( mfh.CompressMethod != 1 ||
				mfh.FileType != 0 ||
				mfh.HeaderSize != sizeof(MetaFileHeader) ||
				mfh.MagicCode != 'FM')
			{
				return false;
			}

			if( mfh.Version != 0x0100 &&
				mfh.Version != 0x0101 )
			{
				PFS::CEnv::SetLastError( EC_UNSUPPORT_META_FILE_VERSION );
				return false;
			}

			if( mfh.ItemCount > 0 )
			{
				// 注: 为了避免不必要的内存拷贝,
				// 这里并没有跳过文件头,而是在CMetaDataRead()的构造函数里实现
				CMetaDataRead dummy( data );
				if( !dummy.DoParseFile( mm, mfh.Version, mfh.ItemCount ) )
				{
					return false;
				}
				if ( mm.size() != mfh.ItemCount )
				{
					return false;
				}
			}
			else
			{
				// empty data?
				return true;
			}
		}
		catch( CMetaIOError&)
		{
			return false;
		}

		PFS::CEnv::SetLastError( EC_SUCCESS );

		return true;
	}

	bool CMetaFileWrite::DoWriteByVersion( CBaseFile& basefile, const CMetaInfoMap& mm, unsigned short usVersion)
	{
		if( usVersion != 0x0100 &&
			usVersion != 0x0101 )
		{
			PFS::CEnv::SetLastError( EC_UNSUPPORT_META_FILE_VERSION );
			return false;
		}

		try
		{
			// 以原始方式写入文件头信息
			MetaFileHeader mfh;
			mfh.CompressMethod = 1;
			mfh.FileType = 0;
			mfh.HeaderSize = sizeof(MetaFileHeader);
			mfh.ItemCount = (unsigned int)mm.size();
			mfh.MagicCode = 'FM';
			mfh.Reserved = 0;
			mfh.Version = usVersion;
			basefile.Write( &mfh, sizeof(MetaFileHeader) );

			// 输出MetaInfoMap的内容
			CMetaFileWrite	dummy( basefile, mm, mfh.Version);
		}
		catch( CMetaIOError&)
		{
			return false;
		}

		return true;
	}

	bool CMetaFileWrite::DoWrite( CBaseFile& basefile, const CMetaInfoMap& mm)
	{
		// V 1.0
		return DoWriteByVersion(basefile, mm, 0x0100);
	}

	bool CMetaDataWrite::DoWriteByVersion( CDataBlock& data, const CMetaInfoMap& mm, unsigned short usVersion)
	{
		if( usVersion != 0x0100 &&
			usVersion != 0x0101 )
		{
			PFS::CEnv::SetLastError( EC_UNSUPPORT_META_FILE_VERSION );
			return false;
		}

		try
		{
			// 以原始方式写入文件头信息
			MetaFileHeader mfh;
			mfh.CompressMethod = 1;
			mfh.FileType = 0;
			mfh.HeaderSize = sizeof(MetaFileHeader);
			mfh.ItemCount = (unsigned int)mm.size();
			mfh.MagicCode = 'FM';
			mfh.Reserved = 0;
			mfh.Version = usVersion;
			data.Append( &mfh, sizeof(MetaFileHeader) );
			CMetaDataWrite	dummy( data, mm, usVersion );
		}
		catch( CMetaIOError&)
		{
			return false;
		}
		return true;
	}

	bool CMetaDataWrite::DoWrite( CDataBlock& data, const CMetaInfoMap& mm)
	{
		// V1.0
		return DoWriteByVersion( data, mm, 0x0100 );
	}

	namespace helper {

		class CDefaultMD5FileHashMethod : public CMD5FileHashMethod
		{
			CDefaultMD5FileHashMethod() {}
			~CDefaultMD5FileHashMethod() {}
		public:
			static CDefaultMD5FileHashMethod* GetInstance()
			{
				static CDefaultMD5FileHashMethod		fhm;
				return &fhm;
			}
		public:
			virtual CFileHashMethod* Clone() const
			{
				return GetInstance();
			}
			virtual void Delete() { }
		};

	} // namespace helper {

	CFileMetaInfo::CFileMetaInfo() : m_fileHashMethod( helper::CDefaultMD5FileHashMethod::GetInstance()) {}
	CFileMetaInfo::~CFileMetaInfo()
	{
		m_fileHashMethod->Delete();
	}

	void CFileMetaInfo::SetDefault()
	{
		SetMethod( *helper::CDefaultMD5FileHashMethod::GetInstance());
	}

	CFileMetaInfo CFileMetaInfo::s_instance;

} // namespace PFS