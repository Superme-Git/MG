//////////////////////////////////////////////////////////////////////////
//
// native.h
//
// native file system µÄÉùÃ÷
//

#pragma once

#include "../base/pfsbase.h"

namespace PFS {

class CNativeAbstractFileSystem;

class CNativeFS : public CPFSBase
{
	friend class CNativeAbstractFileSystem;

	CNativeAbstractFileSystem*		m_filesystem;

	typedef std::set<CBaseFile*> CNativeFileSet;
	CNativeFileSet		m_setFiles;
	CMutex	m_cs;

public:
	CNativeFS( void*) : CPFSBase( FST_NATIVE), m_filesystem( NULL) { }
	CNativeFS() : m_filesystem( NULL) { }
	virtual ~CNativeFS();
	virtual void OnFileClose(CBaseFile* pFile)
	{
		CMutex::CScoped lock( m_cs );
		m_setFiles.erase( pFile );
	}

public:
	virtual CPFSBase* Clone() const { return new CNativeFS(); }
	virtual bool IsFSBusy() const { return !m_setFiles.empty(); }
	virtual int Initialize( const std::wstring& device, MOUNT_TYPE mt);	
	virtual FS_TYPE GetFSType() const;
	virtual int OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file);
	virtual void CheckMetaInfo();
	virtual int RemoveFile( const std::wstring& filename);

	virtual int CreateDirectory( const std::wstring& path, bool bFailIfExisting = false );
	virtual int RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty = false );
	virtual bool IsDirectoryExisting( const std::wstring& path );

	std::wstring GetBasePath() const;
	virtual CMetaInfoMap& GetMetaInfoMap();
	virtual bool IsFileExisting( const std::wstring& filename);

protected:
	void CheckOpenFileMetaInfo( const std::wstring& filename, FILE_MODE fm);
};

inline FS_TYPE CNativeFS::GetFSType() const
{
	return FST_NATIVE;
}

} // namespace PFS{