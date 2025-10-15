#include "../common.h"
#include "../zip/ziphelper.h"
#include "../../translate.h"

namespace PFS {

	const static TSID X2O = PFSX::MakeTypeIDByName('X', '2', 'O');

	//
	// .files.meta �ļ�ͷ�ṹ��
	//
	struct MetaFileHeader
	{
		unsigned short	MagicCode;		// 'FM'  MetaFile
		unsigned short	Version;		// ��Ϊ1.0, MAKEWORD(1,0), 0x0100
										// ��Ϊ1.1, MAKEWORD(1,1), 0x0101
		unsigned short	HeaderSize;		// V1.0/V1.1 - ��СΪ 16�ֽ�
		unsigned char	FileType;		// 0, FileMeta, 1, VersionMeta, 2, SetupMeta
		unsigned char	CompressMethod;	// ѹ����ʽ��1, ��׼Zipѹ��
		unsigned int	ItemCount;		// Meta��Ŀ��
		unsigned int	Reserved;		// ������Ϣ��4�ֽ�
	};
	PFS_STATIC_ASSERT( sizeof(MetaFileHeader) == 16 );

	// V1.0 �����л�����
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
			// ԭʼ���л�����
			//os << m_filename << (unsigned int)m_data.m_sizeFile << m_data.m_nMethod << m_data.m_dataHash <<  m_data.m_attributes << m_data.m_uri;

			// ѹ�������л�����(������uri�ֶ�)
			os << m_filename << (unsigned char)m_data.m_nMethod << m_data.m_dataHash << m_data.m_attributes;
			os.push_size( (size_t)m_data.m_sizeFile ); 
			return os;
		}
		virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream& os)
		{
			// ԭʼ���л�����
			//os >> m_filename >> m_data.m_sizeFile >> m_data.m_nMethod >> m_data.m_hash >> m_data.m_attributes >> m_data.m_uri;
			
			// ѹ�������л�����(������uri�ֶ�)
			os >> m_filename >> (unsigned char&)m_data.m_nMethod >> m_data.m_dataHash >> m_data.m_attributes;
			m_data.m_sizeFile = os.pop_size();
			return os;
		}
	};

	// V1.1 �����л�����
	class CMarshalMetaInfo_1_1 : public PFS::CMarshalMetaInfo
	{
	public:
		CMarshalMetaInfo_1_1( std::wstring& filename, CMetaInfo& data) : CMarshalMetaInfo( filename, data ) {}
		virtual ~CMarshalMetaInfo_1_1() {}
	public:
		virtual PFS::CFileStream& marshal( PFS::CFileStream& os) const
		{
			// ѹ�������л�����(������uri�ֶ�)
			os << m_filename << (unsigned char)m_data.m_nMethod << m_data.m_dataHash << m_data.m_attributes;
			os.push_size( (size_t)m_data.m_sizeFile ); 
			// ���� tsidΪ X2O �� 0 ���ļ���˵�������2���ֶ�ֵ��ͬ��m_dataHash��m_sizeFile
			// ���������,�����л����ļ���,�Խ�ʡ�ļ��ռ�.
			if( m_data.m_tsid != 0 && m_data.m_tsid != X2O )
			{
				os.push_size( (size_t)m_data.m_sizeFile2 ); 
				os << m_data.m_dataHash2;
			}
			return os;
		}
		virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream& os)
		{
			// ѹ�������л�����(������uri�ֶ�)
			os >> m_filename >> (unsigned char&)m_data.m_nMethod >> m_data.m_dataHash >> m_data.m_attributes;
			m_data.m_sizeFile = os.pop_size();
			// ���� tsidΪ X2O �� 0 ���ļ���˵�������2���ֶ�ֵ��ͬ��m_dataHash��m_sizeFile
			// ���������,�����ļ��ж���.
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
	// ����3������ʵ�����ļ�·������������ѹ������
	//
	// StringCommonPrefix - ѹ��ʱ���ڼ���ɱ�ѹ������
	// FileNameRealToFake - ѹ��
	// FileNameFakeToReal - ��ѹ��
	//
	// *X ǰ׺�����˵�ͬ�������ĵı�ѹ���˵��ַ���Ŀ
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
		if( commonPrefixCount > 4 ) // 3 Ϊ������ѹ������С��ֵ, ����ֵȡ5
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
			unsigned int count = nCount; // ����ȡcount��
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
				// ��֧�ֵİ汾
				return false;
			}
			return true;
		}
		catch( CFileStreamException&)
		{
			// �����while(true)���������쳣��ʽ��ֹ�ļ�����
			// ���Լ�ʹ��ȡ�ɹ�,����Ҳ�����쳣������.
			// ���,�Ƿ����쳣����,�����ļ��Ƿ������ı�־.
			// ���ﲻ����CMetaIOError.
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
				// ��֧�ֵİ汾����ʱ������쳣
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
			// ʹ��baseFile��ȡ����ͷ(δѹ������)
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
		: m_imagefile( data), m_file( m_imagefile, FILE_SIZE_ERROR) // ���ﴫ���FILE_SIZE_ERROR��û������
	{
		// �����ļ�ͷ
		// ʹ��m_imagefile��Seek�������ⲻ��Ҫ���ڴ濽��
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
			// �������ͷ
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
				// ע: Ϊ�˱��ⲻ��Ҫ���ڴ濽��,
				// ���ﲢû�������ļ�ͷ,������CMetaDataRead()�Ĺ��캯����ʵ��
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
			// ��ԭʼ��ʽд���ļ�ͷ��Ϣ
			MetaFileHeader mfh;
			mfh.CompressMethod = 1;
			mfh.FileType = 0;
			mfh.HeaderSize = sizeof(MetaFileHeader);
			mfh.ItemCount = (unsigned int)mm.size();
			mfh.MagicCode = 'FM';
			mfh.Reserved = 0;
			mfh.Version = usVersion;
			basefile.Write( &mfh, sizeof(MetaFileHeader) );

			// ���MetaInfoMap������
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
			// ��ԭʼ��ʽд���ļ�ͷ��Ϣ
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