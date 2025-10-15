#pragma once
#include <Windows.h>
#include "../filemarshal.h"
#include "zip/ziphelper.h"
#include "common.h"
#include "timelog/timelog.h"

namespace ZIPFILE
{

	using  PFS::fsize_t;
	using  PFS::foff_t;

class CStr
{
	//enum { CodePage = CP_ACP, };
	enum { CodePage = 936 }; // 明确指定ZIPFILE中的文件名编码为936

public:
	static size_t GetWideSize(const std::wstring& _src)
	{
		return WideCharToMultiByte( CodePage, 0, _src.c_str(), (int)_src.size(), NULL, 0, 0, FALSE);
	}

	static bool WideToMutilByte( const std::wstring& _src, std::string& des)
	{
		des.resize( _src.size());
		int len = ::WideCharToMultiByte( CodePage, 0, _src.c_str(), (int)_src.size(), const_cast<LPSTR>( des.c_str()), (int)des.size(), 0, FALSE);
		bool bFailed = ( 0 == len); 
		if (bFailed)
		{
			if( ::GetLastError() == ERROR_INSUFFICIENT_BUFFER)
			{
				int nBufSize = WideCharToMultiByte( CodePage, 0, _src.c_str(), (int)_src.size(), NULL, 0, 0, FALSE); 
				if( 0 == nBufSize)
					return false;
				des.resize( nBufSize);
				bFailed = ( 0 == ::WideCharToMultiByte( CodePage, 0, _src.c_str(), (int)_src.size(), const_cast<LPSTR>( des.c_str()), nBufSize, 0, FALSE)); 
			}			
		}
		else
		{
			des.resize( len);
		}
		return !bFailed;
	}

	static bool MutilByteToWide( const std::string& _src, std::wstring& des)
	{
		des.resize( _src.size());
		int len = ::MultiByteToWideChar( CodePage, 0, _src.c_str(), (int)_src.size(), const_cast<LPWSTR>( des.c_str()), (int)des.size());
		bool bFailed = ( 0 == len); 
		if (bFailed)
		{
			if( ::GetLastError() == ERROR_INSUFFICIENT_BUFFER)
			{
				int nBufSize = MultiByteToWideChar( CodePage, 0, _src.c_str(), (int)_src.size(), NULL, 0); 
				if( 0 == nBufSize)
					return false;
				des.resize( nBufSize);
				bFailed = ( 0 == ::MultiByteToWideChar( CodePage, 0, _src.c_str(), (int)_src.size(), const_cast<LPWSTR>( des.c_str()), nBufSize)); 
			}			
		}
		else
		{
			des.resize( len);
		}
		return !bFailed;
	}
};

class CFileCentralBase
{
public:
	virtual unsigned int GetDataOffset() const = 0;
	virtual unsigned int GetOrginalSize() const = 0;
	virtual unsigned int GetCompressedSize() const = 0;
	virtual unsigned short GetMethod() const = 0;
	virtual bool IsRawData() const = 0;
};

class CMarshalData : public PFS::CFileMarshal
{
	PFS::CDataBlock&		m_data;
protected:
	PFS::CDataBlock& GetData() { return m_data; }
public:
	explicit CMarshalData( PFS::CDataBlock& data, size_t s) : m_data( data) { m_data.Resize( s); }
	explicit CMarshalData( const PFS::CDataBlock& data) : m_data( const_cast<PFS::CDataBlock&>( data)) {}
	virtual ~CMarshalData() {}
public:
	virtual PFS::CFileStream& marshal( PFS::CFileStream & os) const
	{
		os.push_byte( m_data.GetData(), m_data.GetSize());
		return os;
	}
	virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream & os)
	{
		os.pop_byte( m_data.GetData(), m_data.GetSize());
		return os;
	}
};

class CDirCentral : public PFS::CFileMarshal
{
public:
	unsigned short	m_usDiskNumber;
	unsigned short	m_usStartOf;
	unsigned short	m_usEntriesOnDisk;
	unsigned short	m_usEntriesOnDir;
	unsigned int	m_uiSizeofDir;
	unsigned int	m_uiOffsetofDir;
	PFS::CDataBlock	m_Comment;
public:
	CDirCentral() {}
	virtual ~CDirCentral() {}
public:
	virtual PFS::CFileStream& marshal( PFS::CFileStream & os) const
	{
		unsigned short	usCmtLength = (unsigned short)m_Comment.GetSize();
		os << m_usDiskNumber << m_usStartOf << m_usEntriesOnDisk << m_usEntriesOnDir;
		os << m_uiSizeofDir << m_uiOffsetofDir << usCmtLength;
		os << CMarshalData( m_Comment);
		return os;
	}
	virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream & os)
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

class CFileBase
{
protected:
	PFS::CBaseFile*			m_fileio;

public:
	enum 
	{
		SIGNATURE_FILE_HEADER	= 0x04034b50,
		SIGNATURE_FILE_CENTRAL	= 0x02014b50,
		SIGNATURE_DIR_CENTRAL	= 0x06054b50,
	};

	enum 
	{
		SIGNATURE_ZIP_FILE_HEADER  = SIGNATURE_FILE_HEADER, // 别名
		SIGNATURE_ZIP_FILE_CENTRAL = SIGNATURE_FILE_CENTRAL,
		SIGNATURE_ZIP_DIR_CENTRAL  = SIGNATURE_DIR_CENTRAL,
		SIGNATURE_PFS_FILE_HEADER  = (DWORD)'HSFP', // PFSH
		SIGNATURE_PFS_FILE_CENTRAL = (DWORD)'FSFP', // PFSF
		SIGNATURE_PFS_DIR_CENTRAL  = (DWORD)'DSFP', // PFSD
	};

	CFileBase() : m_fileio(NULL) {}
	virtual ~CFileBase() {}
	virtual void SetFileIO( PFS::CBaseFile& fio) { m_fileio = &fio; }
	PFS::CBaseFile& GetFileIO() { assert( m_fileio); return *m_fileio; }
	virtual void CloseFile()
	{
		m_fileio = NULL;
	}

	operator PFS::CBaseFile&( )
	{
		assert(m_fileio) ;
		return *m_fileio;
	}

	operator const PFS::CBaseFile&() const
	{
		assert(m_fileio);
		return *m_fileio;
	}
};

class CZipFileBadFormat {};

class CFileReaderBase : public CFileBase
{
protected:
	virtual void OnBeforeSetFileIO() {}
	virtual void OnReadFileCentral( const PFS::CFileStream& os) = 0;
	virtual void OnBeforeParseCentral( unsigned short count) {}
	void OnReadDirCentral( const PFS::CFileStream& os)
	{
		CDirCentral			central;
		os >> central;
	}
	bool ParseFileCentral( const PFS::CFileStream& os)
	{
		try
		{
			unsigned int		uiSignature;
			os >> uiSignature;

			switch( uiSignature)
			{
			case SIGNATURE_ZIP_FILE_CENTRAL:
			case SIGNATURE_PFS_FILE_CENTRAL:
				OnReadFileCentral( os);
				break;
			case SIGNATURE_ZIP_DIR_CENTRAL:
			case SIGNATURE_PFS_DIR_CENTRAL:
				return false;
			default:
				throw CZipFileBadFormat();
				break;
			}
		}
		catch( PFS::CFileStreamException&)
		{
			throw CZipFileBadFormat();
		}
		return true;
	}
	void ParseFileCentral()
	{
		PFS::CFileStream fs( *m_fileio);
		CDirCentral	central;
		size_t offset = central.GetDataSize() + sizeof(unsigned int); // sizeof(uiSignature)
		if( offset <= m_fileio->GetSize())
		{
			size_t fileSize = m_fileio->GetSize();
			fs.Seek( -(foff_t)offset, PFS::FSM_END);
			unsigned int uiSignature = 0;
			unsigned int errorsize = 0;
			const unsigned int MAXERRORSIZE = fileSize / 2;	// 从后往前找 文件的一半大小，还找不到就认为是错误文件
			do
			{
				fs >> uiSignature;
				if( errorsize > MAXERRORSIZE)
				{
					throw CZipFileBadFormat();
				}
				errorsize += 1;
			}
			while ( uiSignature != SIGNATURE_ZIP_DIR_CENTRAL && uiSignature != SIGNATURE_PFS_DIR_CENTRAL 
				&& PFS::FILE_SIZE_ERROR != fs.Seek( -(long)(1+sizeof(unsigned int)), PFS::FSM_CUR));
			if( uiSignature != SIGNATURE_ZIP_DIR_CENTRAL && uiSignature != SIGNATURE_PFS_DIR_CENTRAL )
				throw CZipFileBadFormat();

			fs >> central;
			if( PFS::FILE_SIZE_ERROR == fs.Seek( central.GetOffsetDirectory(), PFS::FSM_SET))
				throw CZipFileBadFormat();
			if( offset == m_fileio->GetSize())
			{
				// 一种特殊文件只有一个 CDirCentral 结构
				if( 0 != central.m_uiOffsetofDir 
					|| 0 != central.m_usEntriesOnDir 
					|| 0 != central.m_uiSizeofDir)
				{
					throw CZipFileBadFormat();
				}
			}

			OnBeforeParseCentral( central.m_usEntriesOnDir);
			while( ParseFileCentral( fs));
		}
		else
		{
			throw CZipFileBadFormat();
		}
	}

public:
	virtual void SetFileIO( PFS::CBaseFile& fio) 
	{
		OnBeforeSetFileIO();

		CFileBase::SetFileIO(fio);
		if( fio.GetSize() > 0)
		{
			ParseFileCentral();
		}
	}
};

class CFileCentralRead
{
	size_t						m_curPos;
	helper::CZipStreamInflate	m_szi;
	PFS::CDataBlock				m_Buffer;
	PFS::CBaseFile&				m_reader;
	const CFileCentralBase&		m_central;

public:
	CFileCentralRead( PFS::CBaseFile& fr, const CFileCentralBase& fc) 
		: m_reader( fr), m_central( fc), m_curPos( 0) {}
	virtual ~CFileCentralRead() {}

public:
	PFS::CBaseFile& GetFileIO() { return m_reader; }
	const CFileCentralBase& GetFileCentral() const { return m_central; }
	size_t ReadOrg( PFS::CDataBlock& out, size_t piece = 4096)
	{
		out.Resize( piece);
		
		size_t sount = ReadOrg( out.GetData(), piece);
		if( PFS::FILE_SIZE_ERROR == sount)
		{
			out.Clear();
			return sount;
		}
		if( sount != piece )
		{
			out.Resize( sount);
		}
		return sount;
	}

	size_t ReadOrg( void* outbuffer, size_t piece = 4096)
	{
		if( m_curPos >= m_central.GetCompressedSize())
			return 0;

		m_reader.Seek( m_central.GetDataOffset() + m_curPos, PFS::FSM_SET);
		size_t sleft = m_central.GetCompressedSize() - m_curPos;
		piece = min( sleft, piece);
		size_t sout = m_reader.Read( outbuffer, piece);
		if( PFS::FILE_SIZE_ERROR == sout)
		{
			return sout;
		}

		m_curPos += sout;
		return sout;
	}

	size_t Read( PFS::CDataBlock& out, size_t piece = 4096)
	{
		if( piece > 0)
		{
			try
			{
				if( m_central.IsRawData() ) // 如果是未压缩数据就直接返回源数据
				{
					piece = ReadOrg( out, piece);
				}
				else
				{
					// 在这种情况下，实际返回的数据可能比piece还要大
					piece = ReadOrg( m_Buffer, piece);
					out.Clear();
					if( piece > 0 && piece != PFS::FILE_SIZE_ERROR )
					{
						piece = m_szi.Inflate( m_Buffer, out);
					}
				}
			}
			catch( const helper::CZipStreamException& zse ) 
			{
				PFS::CEnv::GetOutLogInstance()->Print(__FUNCTIONW__  L" CZipStreamException: %d\n", zse.GetErrorCode() );
				throw CZipFileBadFormat();
			}
		}

		return piece;
	}
	inline size_t GetCurPos() { return m_curPos; }
	void Reset()
	{ 
		m_curPos = 0; 
		m_szi.Reset( true); 
	}

	//
	// 一次性获取该central的所有数据到目标缓存区(如果是压缩数据,将首先执行解压缩)。
	// 本函数不支持多线程，由调用方控制。
	//
	bool ReadAll( void* data, size_t size )
	{
		assert( m_central.GetOrginalSize() == size);
		if ( size == 0 && m_central.GetOrginalSize() == 0 )
		{
			// empty file
			return true;
		}

		if( size < m_central.GetOrginalSize() )
		{
			PFS::CEnv::SetLastError(PFS::EC_INSUFFICIENT_BUFFER);
			return false;
		}

		// 如果未压缩
		if( m_central.IsRawData() )
		{
			PFSX::helper::CLogScoped	logscoped( L"ZIPFILE::CFileCentralRead", L"ZIPFILE::CFileCentralRead::ReadOrg");
			size_t old = m_curPos;
			m_curPos = 0;
			size_t out = ReadOrg( data, size);
			m_curPos = old;
			return out == size;
		}
		else
		{
			// 对于压缩数据，首先读取原始的压缩数据，然后解压缩
			size_t old = m_curPos;

			PFS::CDataBlock temp;
			temp.Resize( m_central.GetCompressedSize());
			size_t nRead = 0;
			try
			{
				m_curPos = 0;
				nRead =	ReadOrg( temp, temp.GetSize());
				m_curPos = old;
			}
			catch( const PFS::CFileIOException& fioe ) 
			{
				m_curPos = old;
#ifdef _DEBUG	
				PFS::CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L"PFS::CFileIOException: %d, %s\n", 
					fioe.GetLastError(), fioe.GetFileName().c_str() );
#else
				PFS::CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" PFS::CFileIOException: %d\n",
					fioe.GetLastError() );
#endif
				PFS::CEnv::SetLastError(fioe.GetLastError());
				return false;
			}

			if( nRead == temp.GetSize() )
			{
				try
				{
					size_t out = helper::CZipStreamInflate::InflateAll( 
						temp.GetData(), temp.GetSize(), data, size );
					return( out == size );
				}
				catch( helper::CZipStreamException& zse ) 
				{
					PFS::CEnv::GetOutLogInstance()->Print(__FUNCTIONW__  L" CZipStreamException: %d\n",
						zse.GetErrorCode() );
					PFS::CEnv::SetLastError( PFS::EC_FILE_BAD_ZIPDATA );
					return false;
				}
			}

			return false;
		}

		return true;
	}
	
	//
	// 使用内存映射方式，一次性获取该central的所有数据到目标缓存区(如果是压缩数据,将首先执行解压缩)。
	// 可以保证多线程安全。
	//
	bool ReadAll( PFS::CFileImageHandle image, void* data, size_t size)
	{
		assert( m_central.GetOrginalSize() == size);
		if ( size == 0 && m_central.GetOrginalSize() == 0 )
		{
			// empty file
			return true;
		}

		if( size < m_central.GetOrginalSize() )
		{
			PFS::CEnv::SetLastError(PFS::EC_INSUFFICIENT_BUFFER);
			return false;
		}

		assert( data != NULL );
		assert( image.GetHandle() != NULL );
		
		try
		{
			PFS::CPieceFileImage pfi( image, m_central.GetDataOffset(), m_central.GetCompressedSize());
			if( m_central.IsRawData())
			{
				// 如果是未压缩,不能使用InflateAll()进行解压缩,否则会导致读取失败.
				PFSX::helper::CLogScoped	logscoped(__FUNCTIONW__, L"PFS::CPieceFileImage::GetData");
				assert( size == m_central.GetCompressedSize() );
				assert( size == pfi.GetSize() );
				size_t out = min( size, (size_t)pfi.GetSize() );
				memcpy( data, pfi.GetData(), out );
				return out == size;
			}
			else
			{
				PFSX::helper::CLogScoped	logscoped(__FUNCTIONW__, L"helper::CZipStreamInflate::InflateAll");

				size_t out = helper::CZipStreamInflate::InflateAll( pfi.GetData(), (size_t)pfi.GetSize(), data, size);
				return out == size;
			}
		}
		catch( const PFS::CFileIOException& fioe ) 
		{
#ifdef _DEBUG	
			PFS::CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L"PFS::CFileIOException: %d, %s\n",
				fioe.GetLastError(), fioe.GetFileName().c_str() );
#else
			PFS::CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" PFS::CFileIOException: %d\n", fioe.GetLastError() );
#endif
			PFS::CEnv::SetLastError(fioe.GetLastError());
			return false;
		}
		catch( const helper::CZipStreamException& zse ) 
		{
			PFS::CEnv::GetOutLogInstance()->Print(__FUNCTIONW__  L" CZipStreamException: %d\n", zse.GetErrorCode() );
			PFS::CEnv::SetLastError( PFS::EC_FILE_BAD_ZIPDATA );
			return false;
		}
		
		return false;
	}

	
	//
	// 使用内存映射方式，一次性获取该central的所有数据(如果是压缩数据,将首先执行解压缩)。
	// 可以保证多线程安全。
	//
	PFS::CImageData GetImage( PFS::CFileImageHandle image )
	{
		assert( image.GetHandle() != NULL );

		size_t size = m_central.GetOrginalSize();
		if( size > 0 )
		{
			if( m_central.IsRawData() )
			{
				try
				{
					PFS::CPieceFileImage* pfi = new PFS::CPieceFileImage( image, 
						m_central.GetDataOffset(), m_central.GetOrginalSize() );
					return PFS::CImageData( new PFS::CRawImageData( pfi));
				}
				catch( const PFS::CFileIOException& fioe ) 
				{
#ifdef _DEBUG
					PFS::CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" PFS::CFileIOException: %d, %s\n",
						fioe.GetLastError(), fioe.GetFileName().c_str() );
#else
					PFS::CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" PFS::CFileIOException: %d\n", fioe.GetLastError() );
#endif
					PFS::CEnv::SetLastError(fioe.GetLastError());
				}
			}
			else
			{
				PFS::CDataBlock buffer;
				buffer.Resize( size);
				if( ReadAll( image, buffer.GetData(), size))
				{
					return PFS::DataBlockToImageData( buffer);
				}
			}
		
			assert( false);
		}

		// for empty file
		return PFS::CImageData();
	}
	
	// 
	// 一次性获取该central的所有数据(如果是压缩数据,将首先执行解压缩)。
	// 本函数不能在多线程模式下调用，由调用方控制。
	// 
	PFS::CImageData GetImage()
	{
		size_t size = m_central.GetOrginalSize();
		if( size > 0 )
		{
			PFS::CDataBlock buffer;
			buffer.Resize( size);
			if( ReadAll( buffer.GetData(), size))
			{
				return PFS::DataBlockToImageData( buffer);
			}

			assert( false);
		}

		// for empty file
		return PFS::CImageData();
	}
};

class CZipReadError {};

class CZipSystemReadFile : public PFS::CBaseFile
{
	CFileCentralRead				m_zipread;
	size_t							m_current;
	size_t							m_offset;
	PFS::CDataBlock					m_buffer;
	PFS::CFileImageHandle			m_Image;

	enum { BUFFERSIZE = 65536 };// 64K

public:
	CZipSystemReadFile( CFileReaderBase& zipfile, const CFileCentralBase& central, const PFS::CFileImageHandle& fih) 
		: m_current( 0), m_offset( 0), m_Image( fih), m_zipread( zipfile, central) { }
	virtual ~CZipSystemReadFile() { }
public:
	CFileCentralRead& GetFileCentralRead() { return m_zipread; }

	virtual size_t Read( void* data, size_t size)
	{
		try
		{
			size_t valid = m_buffer.GetSize() - m_offset;
			if( valid < size)
			{
				if( valid > 0 )
				{
					memcpy( data, reinterpret_cast<const char*>( m_buffer.GetData())+m_offset, valid );
				}

				size_t read = m_zipread.Read( m_buffer, max( BUFFERSIZE, size)); 
				if( read > 0 && read != PFS::FILE_SIZE_ERROR )
				{
					size_t need_copy = size-valid;
					need_copy = min(read, need_copy);
					memcpy( (char*)data+valid, m_buffer.GetData(), need_copy );
					m_offset = need_copy;
					m_current += need_copy;
					return valid + need_copy;
				}
				else
				{
					m_buffer.Clear();
					m_offset = 0;
					m_current += valid;
					return valid;
				}
			}
			else
			{
				size_t read = min( size, valid);
				if( read <= 0)
					return 0;
				memcpy( data, (const char*)m_buffer.GetData()+m_offset, read);
				m_offset += read;
				m_current += read;
				return read;
			}
		}
		catch( CZipFileBadFormat&)
		{
		}
		return PFS::FILE_SIZE_ERROR;
	}
	virtual size_t Write( const void* data, size_t size)
	{ 
		throw CZipReadError();
	}

	virtual fsize_t Seek( foff_t pos, PFS::FILE_SEEK_METHOD fsm) 
	{
		if( (0 == pos) && ( PFS::FSM_SET == fsm))
		{
			if( m_offset == m_current)
			{
				m_offset = m_current = 0;
				//m_zipread->Reset();	// 这里不用reset，因为第一次读出的数据还在m_buffer中缓存着
				return 0;
			}
		}
		throw CZipReadError();
	}

	virtual fsize_t GetSize(){ return m_zipread.GetFileCentral().GetOrginalSize(); }
	virtual fsize_t GetPos() { return m_current; }
	virtual bool SetEnd()
	{
		throw CZipReadError();
	}

	virtual bool ReadAll( void* data, fsize_t size)
	{
		if( m_Image.GetHandle() != NULL )
		{
			// 说明, m_zipread.ReadAll(m_Image, data, size) 与 m_zipread.ReadOrg() 方法没有共享状态,
			// 所以,这里可以直接使用ReadAll().
			return m_zipread.ReadAll( m_Image, data, (size_t)size);
		}
		else
		{
			// 不使用CFileImageHandle方式读取时必然不是多线程使用环境，
			// 可以放心使用下面的代码。
			return m_zipread.ReadAll( data, (size_t)size);
		}
	}

	virtual PFS::CImageData GetImage() 
	{
		if( m_Image.GetHandle() != NULL)
		{
			// 说明, m_zipread.GetImage(m_Image) 与 m_zipread.ReadOrg() 方法没有共享状态,
			// 所以,这里可以直接使用GetImage(m_Image).
			return m_zipread.GetImage(m_Image);
		}
		else
		{
			// 不使用CFileImageHandle方式读取时必然不是多线程使用环境，
			// 可以放心使用下面的代码。
			return m_zipread.GetImage();
		}
	}
};

}