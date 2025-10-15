#pragma once

#include "../base/pfsbase.h"

namespace PFS {

class CBriefFS;
class CBriefBaseFile : public PFSX::CInetFile
{
	CBriefFS*	m_bfs;
	void OnFileClosed();
public:
	CBriefBaseFile( CBriefFS* bfs) : m_bfs( bfs) {}
	~CBriefBaseFile() { OnFileClosed(); }
};

class CBriefFS : public CPFSBase
{
	friend class CBriefBaseFile;

	typedef std::set<CBaseFile*> CBriefFileSet;
	CMutex				m_cs;
	CBriefFileSet		m_setFiles;
	std::wstring		m_strBasePath;
	std::wstring		m_hostName;
	std::wstring		m_baseUrl;
	HINTERNET			m_hInternet;
	HINTERNET			m_hConnect;

public:
	CBriefFS( void*);
	CBriefFS();
	virtual ~CBriefFS();

	virtual void OnFileClose(CBaseFile* pFile )
	{
		CMutex::CScoped lock( m_cs );
		m_setFiles.erase( pFile );
	}

public:
	virtual CPFSBase* Clone() const { return new CBriefFS(); }
	virtual int Initialize( const std::wstring& device, MOUNT_TYPE mt);
	virtual FS_TYPE GetFSType() const;
	virtual int OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file);
	virtual int RemoveFile( const std::wstring& filename)
	{
		return EC_FS_READONLY;
	}
	virtual bool IsFileExisting( const std::wstring& filename);

	virtual int CreateDirectory( const std::wstring& path, bool bFailIfExisting = false )
	{
		return EC_FS_READONLY;
	}

	virtual int RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty = false )
	{
		return EC_FS_READONLY;
	}

	virtual bool IsDirectoryExisting( const std::wstring& path )
	{
		if ( path.empty() )
		{
			return false;
		}
		
		CMetaInfoMap& mim = GetMetaInfoMap();

		std::wstring dirname = path + L"/";
		CMetaInfoMap::const_iterator itr = mim.lower_bound( dirname );

		if ( itr != mim.end() && 
			wcsncmp( itr->first.c_str(), dirname.c_str(), dirname.length() ) == 0 )
		{
			return true;
		}
		
		return false;
	}

	virtual bool IsFSBusy() const { return !m_setFiles.empty();	}
	virtual void CheckMetaInfo() { }
	virtual void CloseAllFiles();
};

inline FS_TYPE CBriefFS::GetFSType() const
{
	return FST_BRIEF;
}

inline void CBriefBaseFile::OnFileClosed()
{
	assert( m_bfs);
	m_bfs->OnFileClose( this);
}

}