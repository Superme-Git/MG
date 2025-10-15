#pragma once

#include "../zipcommon.h"

namespace ZIPFILE {

//local file header: 
//     local file header signature     4 bytes  (0x04034b50) 
//     version needed to extract       2 bytes 
//     general purpose bit flag        2 bytes 
//     compression method              2 bytes 
//     last mod file time              2 bytes 
//     last mod file date              2 bytes 
//     crc-32                          4 bytes 
//     compressed size                 4 bytes 
//     uncompressed size               4 bytes 
//     filename length                 2 bytes 
//     extra field length              2 bytes 
//
//     filename (variable size) 
//     extra field (variable size) 

class CFileHeaderDataDescriptor : public PFS::CFileMarshal
{
public:
	unsigned int	m_uiCRC32;
	unsigned int	m_uiSizeNow;
	unsigned int	m_uiSizeOrg;
public:
	CFileHeaderDataDescriptor( const CFileHeaderDataDescriptor& src) 
		: m_uiCRC32( src.m_uiCRC32), m_uiSizeNow( src.m_uiSizeNow), m_uiSizeOrg( src.m_uiSizeOrg)
	{ }

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
	virtual PFS::CFileStream& marshal( PFS::CFileStream & os) const
	{
		os << m_uiCRC32 << m_uiSizeNow << m_uiSizeOrg;
		return os;
	}
	virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream& os)
	{
		os >> m_uiCRC32 >> m_uiSizeNow >> m_uiSizeOrg;
		return os;
	}

	static size_t GetDataSize() { return sizeof(unsigned int) * 3; }

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
	PFS::CDataBlock	m_ExtData;
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
	inline void unmarshalName( const PFS::CFileStream & os, unsigned short usNameLength)
	{
		std::string str;
		str.resize( usNameLength);
		os.pop_byte( const_cast<char*>( str.c_str()), usNameLength);
		CStr::MutilByteToWide( str, m_name);
	}
public:
	virtual size_t GetDataSize() const 
	{ 
		return CFileHeaderDataDescriptor::GetDataSize() + sizeof(unsigned short) * 5 + sizeof(unsigned short) * 2 
			+ CStr::GetWideSize( m_name) + m_ExtData.GetSize(); 
	}

	virtual PFS::CFileStream& marshal( PFS::CFileStream& os) const
	{
		std::string str;
		CStr::WideToMutilByte( m_name, str);
		unsigned short	usFileLength = (unsigned short)str.size();
		unsigned short	usExtLength = (unsigned short)m_ExtData.GetSize();

		os << m_usUnzipVesion << m_usFlag << m_usMethod << m_usModifyTime << m_usModifyDate;
		os << m_uiCRC32 << m_uiSizeNow << m_uiSizeOrg << usFileLength << usExtLength;

		os.push_byte( str.c_str(), str.size());
		if( usExtLength)
			os << CMarshalData( m_ExtData);
		return os;
	}
	virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream & os)
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

	PFS::CDataBlock	m_FileComment;

	enum
	{
		DELETED		=	0x0100,		/* ɾ����� */
		MODIFIED	=	0x0200,		/* �޸ı�� */
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
	virtual PFS::CFileStream& marshal( PFS::CFileStream & os) const
	{
		std::string str;
		CStr::WideToMutilByte( m_name, str);
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

	virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream & os)
	{
		// 
		// �ر�˵����
		// unmarshal�������ڽ���zipfile��filecentral��Ϣ������һ������filecentral��Ϣ�ṩ��offset��λfiledata��
		// һ������£�filecentral�е���ϢӦ�ú͹����ļ���localfileheader��ͬ���ֶα���һ�������ݡ�
		// ���� compression method��filename length���ֶΡ����ǣ�zip�ļ��淶��û��ǿ��Ҫ�����һ�¡�
		// �ر��Ƕ���extra field length��extra field data�������ֶΡ�
		// ������Ը��ӵ�unmarshal���룬Ŀ����ֱ������filecentral�е�extra field��Ϣ������local file header��
		// ����extra field length��extra field data��Ϣ�����ھ�ȷ��λfile data offset��
		//  [1/10/2012 ����ѧ]
		//
		unsigned short	usFileLength;
		unsigned short	usExtLength;
		unsigned short	usCmtLength;

		os >> m_usVersion >> m_usUnzipVesion >> m_usFlag >> m_usMethod >> m_usModifyTime >> m_usModifyDate;
		os >> m_uiCRC32 >> m_uiSizeNow >> m_uiSizeOrg >> usFileLength >> usExtLength >> usCmtLength;
		os >> m_usDiskNumberStart >> m_usInternalFileAttributes >> m_uiExternalFileAttributes >> m_uiOffset;

		unmarshalName( os, usFileLength);
		if( usExtLength)
		{
			// ����ֱ������ExtData�ĳ�ʼ����
			//os >> CMarshalData( m_ExtData, usExtLength);
			os.Seek(usExtLength,PFS::FSM_CUR);
		}
		if( usCmtLength)
			os >> CMarshalData( m_FileComment, usCmtLength);

		// �������ڵ�ƫ��λ��
		fsize_t currentPos = os.Seek(0,PFS::FSM_CUR);

		// 30 - Local Header Fix Length
		// 2 - extra field length
		unsigned int fixdataoffset = m_uiOffset + 30 - 2; // ������չ�ֶ����ݳ����ֶ�

		// ��local header�н���extdata�����ǿ�����filecenter�е����ݲ�һ����
		fsize_t fixoffset = os.Seek(fixdataoffset, PFS::FSM_SET ); // �����Ѿ������ļ��������ֶ�
		os >> usExtLength;
		if( usExtLength)
		{
			os.Seek(usFileLength, PFS::FSM_CUR); // �����ļ������ݲ��֡�
			os >> CMarshalData( m_ExtData, usExtLength);
		}

		// �ص�ԭ����ƫ��λ�ã��������л���һ��filecentral��
		os.Seek(currentPos, PFS::FSM_SET);

		return os;
	}

	size_t GetDataSize() const {
		return CFileHeader::GetDataSize() + sizeof(unsigned short) * 3 + sizeof(unsigned int) * 2 + sizeof( unsigned short) + m_FileComment.GetSize();
	}
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

	void ClearModifiedFlag()
	{
		// �����޸ı�ǡ������Ѿ��ύ�޸ĵ��ļ��������޸ı�ǿ��Է�ֹ�ٴ��ύ��
		// ע������ɾ�����ļ���Ӧ������ɾ�����ļ���
		assert((m_usFlag & DELETED) == 0);
		m_usFlag &= 0x00ff;
	}

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

class CFileReader : public CFileReaderBase
{
public:
	CFileReader() {}
	virtual ~CFileReader() {}
	
	typedef std::map<std::wstring, CFileCentral> CFileCentralMap;
	
protected:
	CFileCentralMap			m_mapcentral;

	virtual void OnBeforeSetFileIO()
	{
		m_mapcentral.clear();
	}

	virtual void OnReadFileCentral( const PFS::CFileStream& os)
	{
		CFileCentral		central;
		os >> central;
		m_mapcentral.insert( std::make_pair( central.GetFileName(), central));
	}

public:
	
	CFileCentralMap& GetFileCentralMap() { return m_mapcentral; }
	
	const CFileCentral* GetFileCentral( const std::wstring& name) const
	{
		CFileCentralMap::const_iterator it = m_mapcentral.find( name);
		if( it == m_mapcentral.end())
			return NULL;
		return &( it->second);
	}

	// dirname �� '/' ��β
	bool IsDirectoryEmpty( const std::wstring& dirname)
	{
		CFileCentralMap::iterator it = m_mapcentral.find( dirname);
		if( it == m_mapcentral.end())
			return true;// �����ڴ�Ŀ¼, �Կ�Ŀ¼�Դ�

		while( ++ it != m_mapcentral.end() )
		{
			if( wcsncmp( it->first.c_str(), dirname.c_str(), dirname.length() ) != 0 ) 
			{
				return true;
			}

			// �ж��Ƿ��Ǵ�Ŀ¼�µ���δɾ�����ļ�����Ŀ¼
			if( !it->second.IsFileDeleted() )
			{
				return false;
			}
		} 

		return true;
	}
};

class CFileEditableReader : public CFileReader 
{
public:
	CFileEditableReader() {}
	virtual ~CFileEditableReader() {}

public:
	// �ȿ�������ɾ���ļ�,Ҳ��������ɾ����Ŀ¼
	virtual bool DeleteFile( const std::wstring& name)
	{
		CFileCentralMap::iterator it = m_mapcentral.find( name);
		if( it == m_mapcentral.end())
			return false;
		CFileCentral& filecentral = it->second;
		filecentral.SetFileDeleted();
		return true;
	}

	// ɾ����dirname��ͷ�������ļ�, dirname�Ѿ��ڵ���ǰ����"/"��Ϊ��������
	virtual bool RemoveDirectory( const std::wstring& dirname )
	{
		CFileCentralMap::iterator itBgn = m_mapcentral.lower_bound( dirname );
		if( itBgn == m_mapcentral.end() )
			return false;

		CFileCentralMap::iterator itEnd = m_mapcentral.end();
		if( dirname.size() > 0 )
		{			
			std::wstring dirnameEnd = dirname;
			dirnameEnd[dirnameEnd.size()-1] ++;
			CFileCentralMap::iterator itEnd = m_mapcentral.lower_bound( dirnameEnd );
		}

		for( CFileCentralMap::iterator it = itBgn; it != itEnd; ++ it )
		{
			CFileCentral& filecentral = it->second;
			filecentral.SetFileDeleted();
		}

		return true;
	}

	virtual const CFileCentral* AddFile( const std::wstring& name)
	{
		CFileCentralMap::iterator it = m_mapcentral.find( name);
		if( it == m_mapcentral.end())
		{
			CFileCentralMap::_Pairib pr = m_mapcentral.insert( std::make_pair( name, CFileCentral()));
			pr.first->second.m_name = name;
			pr.first->second.SetFileModified();
			return &pr.first->second;
		}
		if( it->second.IsFileDeleted())
			it->second.SetFileModified();
		return &it->second;
	}

	bool IsZipFileModified()
	{
		CFileCentralMap::iterator itor = m_mapcentral.begin();
		for( ; itor != m_mapcentral.end(); ++itor)
		{
			if( itor->second.IsDirectory())
				continue;
			if( itor->second.IsFileDeleted() || itor->second.IsFileModified())
				return true;
		}
		return false;
	}
	
	//
	// �ύ�޸ĵ��ļ���Ŀ��
	// ����ZipFS commit���֮����С�
	// ����ȫ��Ϊ�ڴ������Ӧ�����ǻ�ɹ���
	//
	void ClearDeletedFileCentrals()
	{
		CFileCentralMap::iterator itor = m_mapcentral.begin();
		while( itor != m_mapcentral.end() )
		{
			// QUESTION: Check directory or not?

			// 1. delete files that marked as deleted.
			if( itor->second.IsFileDeleted())
			{
				itor = m_mapcentral.erase(itor);
				continue;
			}

			 ++ itor;
		}
	}

	//
	// ͳ���޸ĵ��ļ���Ŀ��
	//
	size_t CountModifiedFileCentrals() const
	{
		size_t c = 0;
		CFileCentralMap::const_iterator itor = m_mapcentral.begin();
		while( itor != m_mapcentral.end() )
		{
			if( itor->second.IsFileModified())
			{
				c ++;
			}

			 ++ itor;
		}

		return c;
	}
};

class CZipWriteError {};

class CFileWriter : public CFileBase
{
	unsigned int				m_written;
	CFileCentral*				m_current;
	helper::CZipStreamDeflate	m_zsd;
	helper::CCRC32				m_crc;
	bool						m_bZip; // true: zip��ʽ, false: pfs��ʽ

protected:
	CFileWriter(bool bZip);				// �������ඨ��

public:
	CFileWriter();
	virtual ~CFileWriter();

	typedef std::map<std::wstring, CFileCentral> CFileCentralMap;
	CFileCentralMap			m_mapcentral;

private:
	size_t WriteFileHeader( const CFileCentral& fc, bool bSeek);

	bool CloseCentral();

	bool WriteEnd();

public:
	void SetZipLevel( int nLevel);

	bool WriteNextCentral( const CFileCentral& fc);

	bool WriteFileData( const PFS::CDataBlock& in, bool bFinish);
	size_t WriteDeflateData( const PFS::CDataBlock& in, bool bSeek);

	bool WriteFileData( const void* in, size_t insize, bool bFinish);
	size_t WriteDeflateData( const void* in, size_t insize, bool bSeek);

	virtual void CloseFile()
	{
		WriteEnd();
		CFileBase::CloseFile();
		m_mapcentral.clear();
		m_written = 0;
	}

	bool SetOffset( unsigned int uiOffset);

	CFileCentral* AddFileCentral( const CFileCentral& fc);

	// �����Ƿ�Ϊzip��ʽ�����ؾɵ�ֵ
	bool SetIsZip( bool bZip)
	{
		bool old = m_bZip;
		m_bZip = bZip;
		return old;
	}

public:		// ��Ҫ�� CArrangeZipUnCompact ��ʹ��

	// Ԥ�ȼ���ѹ�����С
	size_t DeflateBound( size_t sourceLen)	
	{
		return m_zsd.Bound( sourceLen);
	}

	// �����ļ����õ���Ӧ�� CFileCentral ָ��
	const CFileCentral* GetFileCentral( const std::wstring& name) const
	{
		CFileCentralMap::const_iterator it = m_mapcentral.find( name);
		if( it == m_mapcentral.end())
			return NULL;
		return &( it->second);
	}

	// �õ���ǰ���ڲ����� CFileCentral
	const CFileCentral* GetCurCentral( ) const
	{
		return m_current;
	}
};

} // namespace ZIPFILE {

namespace PFS
{
	class CPFSWriter: public ZIPFILE::CFileWriter
	{
	public:
		CPFSWriter() : ZIPFILE::CFileWriter(false)
		{
		}
	};
}