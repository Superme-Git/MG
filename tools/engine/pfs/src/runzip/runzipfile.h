#pragma once

#include "../zipcommon.h"
#include <hash_map>

namespace ZIPFILE
{

class CHash
{
	CHash() {}
	~CHash() {}
public:
	// ELF Hash Function 
	static unsigned int Hash_ELF( const char *str, unsigned int length)
	{
		register unsigned int hash  =   0 ;
		register unsigned int x     =   0 ;

		while( length--)
		{
			hash = ( hash << 4 ) + ( *str++);
			if( (x = hash & 0xF0000000L) != 0)
			{
				hash ^= (x >> 24);
				hash &= ~x;
			} 
		} 
		return hash;
	}
	// JS Hash Function 
	static unsigned int Hash_JS( const char * str, unsigned int length)
	{
		register unsigned int hash = 1315423911;
		while( length--)
		{
			hash ^= ((hash << 5) + ( *str++) + ( hash >> 2));
		}
		return hash;
	} 
	// BKDR Hash Function
	static unsigned int Hash_BKDR( const char *str, unsigned int length)
	{ 
		register unsigned int hash = 0;
		while( length--)
		{
			hash = 31 * hash + *str++;
		}
		return hash;
	}
};

class CRunFileCentral : public PFS::CFileMarshal, public CFileCentralBase
{
	unsigned int	m_uiSizeNow;
	unsigned int	m_uiSizeOrg;
	unsigned int	m_uiDataOffset;
	unsigned short	m_usMethod;
	unsigned int	m_uiHash1;
	unsigned int	m_uiHash2;

#ifdef OUTLOG_DUMP_INFOS_WITH_FILE_NAME
	std::wstring	m_name;
#endif

	void unmarshalName( const PFS::CFileStream & os, unsigned short usNameLength, std::wstring& name)
	{
		std::string str;
		str.resize( usNameLength);
		os.pop_byte( const_cast<char*>( str.c_str()), usNameLength);
		CStr::MutilByteToWide( str, name);
	}

public:
	virtual unsigned int GetDataOffset() const { return m_uiDataOffset; }
	virtual unsigned int GetOrginalSize() const { return m_uiSizeOrg; }
	virtual unsigned int GetCompressedSize() const { return m_uiSizeNow; }
	virtual unsigned short GetMethod() const { return m_usMethod; }
	virtual bool IsRawData() const
	{
		return (0 == m_usMethod && m_uiSizeNow == m_uiSizeOrg);
	}

public:
	void DumpInfos( PFS::COutLog& outlog) const
	{
#ifdef OUTLOG_DUMP_INFOS_WITH_FILE_NAME
		outlog.Print( L"m_name = '%s' ", m_name.c_str());
#endif
		outlog.Print( L"m_uiSizeNow = %d m_uiSizeOrg = %d m_uiDataOffset = %d m_usMethod = %d m_uiHash1 = %d(0x%08x) m_uiHash2 = %d(0x%08x)", m_uiSizeNow, m_uiSizeOrg, m_uiDataOffset, m_usMethod, m_uiHash1, m_uiHash1, m_uiHash2, m_uiHash2);
	}

	virtual PFS::CFileStream& marshal( PFS::CFileStream & os) const
	{
		return os;
	}
	virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream & os)
	{
		unsigned short	usFileLength;
		unsigned short	usExtLength;
		unsigned short	usCmtLength;
		unsigned int	uiOffset;

		PFS::CFileStream& fos = const_cast<PFS::CFileStream&>( os);
		// m_usVersion >> m_usUnzipVesion >> m_usFlag 
		fos.Seek( 3*sizeof( unsigned short), PFS::FSM_CUR);
		os >> m_usMethod ;
		// m_usModifyTime >> m_usModifyDate >> m_uiCRC32
		fos.Seek( 2*sizeof( unsigned short) + sizeof( unsigned int), PFS::FSM_CUR);
		os >> m_uiSizeNow >> m_uiSizeOrg >> usFileLength >> usExtLength >> usCmtLength;
		// m_usDiskNumberStart >> m_usInternalFileAttributes >> m_uiExternalFileAttributes 
		fos.Seek( 2*sizeof( unsigned short) + sizeof( unsigned int), PFS::FSM_CUR);
		os >> uiOffset;
		m_uiDataOffset = uiOffset + 4*sizeof( unsigned int) + 7*sizeof( unsigned short) + usFileLength + usExtLength;

#ifndef OUTLOG_DUMP_INFOS_WITH_FILE_NAME
		std::wstring m_name;
#endif
		unmarshalName( os, usFileLength, m_name);
		m_uiHash1 = CHash::Hash_ELF( (const char*)m_name.c_str(), m_name.size()*sizeof( wchar_t));
		m_uiHash2 = CHash::Hash_JS( (const char*)m_name.c_str(), m_name.size()*sizeof( wchar_t));

		if( usExtLength)
			fos.Seek( usExtLength, PFS::FSM_CUR);
		if( usCmtLength)
			fos.Seek( usCmtLength, PFS::FSM_CUR);

		return os;
	}
public:
	unsigned int GetHash1() const { return m_uiHash1; }
	unsigned int GetHash2() const { return m_uiHash2; }
	friend bool operator<( const CRunFileCentral& left, const CRunFileCentral& right)
	{
		if( left.m_uiHash1 == right.m_uiHash1)
			return left.m_uiHash2 < right.m_uiHash2;
		return left.m_uiHash1 < right.m_uiHash1;
	}
	CRunFileCentral( unsigned int hash1, unsigned int hash2) : m_uiHash1( hash1), m_uiHash2( hash2) {}
	CRunFileCentral( ) {}
};

class CRunFileReader : public CFileReaderBase
{
public:
	typedef std::vector<CRunFileCentral>		CRunFileCentralVector;
	CRunFileReader() {}
	~CRunFileReader() {}

private:
	CRunFileCentralVector		m_vCentral;

	virtual void OnReadFileCentral( const PFS::CFileStream& os)
	{
		CRunFileCentral central;
		os >> central;
		
		m_vCentral.push_back( central);
	}
	virtual void OnBeforeParseCentral( unsigned short count)
	{
		m_vCentral.reserve( count);
	}

public:
	void DumpInfos( PFS::COutLog& outlog) const
	{
		for( CRunFileCentralVector::const_iterator it = m_vCentral.begin(), ite = m_vCentral.end(); it != ite; ++ it)
		{
			const CRunFileCentral& rfc = *it;
			outlog.Print( L"\t\t{ ");
			rfc.DumpInfos( outlog);
			outlog.Print( L" }\n");
		}
	}

	const CRunFileCentralVector& GetFileCentralVector() { return m_vCentral; }
	
	virtual void SetFileIO( PFS::CBaseFile& fio)
	{
	   CFileReaderBase::SetFileIO( fio);
	   std::sort( m_vCentral.begin(), m_vCentral.end());
	}

	const CRunFileCentral* GetFileCentral( const std::wstring& name) const
	{
		unsigned int hash1 = CHash::Hash_ELF( (const char*)name.c_str(), name.size()*sizeof( wchar_t));
		unsigned int hash2 = CHash::Hash_JS( (const char*)name.c_str(), name.size()*sizeof( wchar_t));

		CRunFileCentral hash(hash1, hash2);
		CRunFileCentralVector::const_iterator itor = std::lower_bound( m_vCentral.begin(), 
			m_vCentral.end(), hash);
		if( itor == m_vCentral.end() || hash < *itor )
			return NULL;
		return &(*itor);
	}
};

}