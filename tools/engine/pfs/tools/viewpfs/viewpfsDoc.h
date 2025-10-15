// viewpfsDoc.h : interface of the CviewpfsDoc class
//
#pragma once

#include "../../src/common.h"
#include "../../src/zip/zipfile.h"
#include "../../src/runzip/runzipfile.h"

namespace ZIPFILE
{
	// 记录RUNZIP Hash信息的FileCentral类。
	// CFileCentral & CRunFileCentral 的结合体。
	class CPFSInfoFileCentral : public CFileCentral
	{
	public:
		CPFSInfoFileCentral()
		{
		}

		CPFSInfoFileCentral( unsigned int hash1, unsigned int hash2) 
			: m_uiHash1( hash1), m_uiHash2( hash2)
		{
		}
		
		unsigned int GetHash1() const { return m_uiHash1; }
		unsigned int GetHash2() const { return m_uiHash2; }

		CFileCentral& operator = ( const CFileCentral& src)
		{
			if( this == &src)
				return *this;

			CFileCentral::operator=( src);
			m_uiHash1 = ((CPFSInfoFileCentral&)src).m_uiHash1;
			m_uiHash2 = ((CPFSInfoFileCentral&)src).m_uiHash2;

			return *this;
		}

		friend bool operator<( const CPFSInfoFileCentral& left, const CPFSInfoFileCentral& right)
		{
			return ( left.m_uiHash1 < right.m_uiHash1 ) ||  
				( left.m_uiHash1 == right.m_uiHash1 ) &&  ( left.m_uiHash2 < right.m_uiHash2 );
		}

		virtual const PFS::CFileStream& unmarshal( const PFS::CFileStream & os)
		{
			CFileCentral::unmarshal(os);

			m_uiHash1 = CHash::Hash_ELF( (const char*)m_name.c_str(), m_name.size()*sizeof( wchar_t));
			m_uiHash2 = CHash::Hash_JS( (const char*)m_name.c_str(), m_name.size()*sizeof( wchar_t));

			return os;
		}

	protected:
		unsigned int	m_uiHash1;
		unsigned int	m_uiHash2;
	};

	// 读取RUNZIP Hash信息的CFileReader类。
	// CFileReader & CRunFileReader 的结合体。
	class CPFSInfoFileReader : public CFileReader
	{
	public:
		typedef std::vector<CPFSInfoFileCentral>				CRunFileCentralVector;
		typedef std::map<std::wstring, CPFSInfoFileCentral>		CFileCentralMap;
	
	protected:
		CFileCentralMap				m_mapcentral;
		CRunFileCentralVector		m_vCentral;

		virtual void OnReadFileCentral( const PFS::CFileStream& os)
		{
			CPFSInfoFileCentral central;
			os >> central;
			m_vCentral.push_back( central);
			m_mapcentral.insert( std::make_pair( central.GetFileName(), central));
		}
		virtual void OnBeforeParseCentral( unsigned short count)
		{
			CFileReader::OnBeforeParseCentral(count);
			m_vCentral.reserve( count);
		}

	public:		
		CFileCentralMap& GetFileCentralMap() 
		{
			return m_mapcentral;
		}

		const CFileCentral* GetFileCentral( const std::wstring& name) const
		{
			CFileCentralMap::const_iterator it = m_mapcentral.find( name);
			if( it == m_mapcentral.end())
				return NULL;
			return &( it->second);
		}

		const CRunFileCentralVector& GetFileCentralVector()
		{
			return m_vCentral;
		}
		
		virtual void SetFileIO( PFS::CBaseFile& fio)
		{
		   CFileReader::SetFileIO( fio);
		   std::sort( m_vCentral.begin(), m_vCentral.end());
		}
	};

} // namespace ZIPFILE

class CviewpfsDoc : public CDocument
{
protected: // create from serialization only
	CviewpfsDoc();
	DECLARE_DYNCREATE(CviewpfsDoc)

// Attributes
public:

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// Implementation
public:
	virtual ~CviewpfsDoc();

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()

public:
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	ZIPFILE::CPFSInfoFileReader m_filereader;
	const ZIPFILE::CPFSInfoFileReader::CRunFileCentralVector& GetFileCentralVector()
	{
		return m_filereader.GetFileCentralVector();
	}
};
