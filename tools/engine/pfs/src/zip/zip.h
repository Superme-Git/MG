///=============================================================================
/// Product   : MHZX
/// Project   : pfslib
/// Module    : zip.h
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : wangzao, yanghaibo
///=============================================================================
/// Description :
/// 	Zip File System
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-25 Created.
///=============================================================================
#pragma once

#include "../base/pfsbase.h"
#include "zipfile.h"

namespace PFS {

namespace helper
{
	class CArrangeZipCompact;
	class CArrangeZipUnCompact;
	class CArrangeZipManager;
}

class CZipFS : public CPFSBase
{
private:
	friend class helper::CArrangeZipCompact;
	friend class helper::CArrangeZipUnCompact;
	friend class helper::CArrangeZipManager;
	friend class CZipBaseFile;

	typedef std::map<CBaseFile*, std::wstring>		CFileAccessMap;
	CMutex							m_cs;
	CFileAccessMap					m_mapFileAccess;

	ZIPFILE::CFileEditableReader	m_zipfile;
	std::wstring					m_strBasePath;
	CBaseFile*						m_basefile;
	CZipTempFileManager				m_tempfilemanager;
	
	CFileImageHandle				m_ImageHandle;

public:
	CZipFS( void*) : CPFSBase( FST_ZIP), m_basefile(NULL) {}
	CZipFS() : m_basefile(NULL) {}
	virtual ~CZipFS();
	virtual void OnFileClose(CBaseFile* pFile );

protected:
	virtual std::wstring GetNativeFilePath( const std::wstring& rFileName ) const 
	{
		if( !rFileName.empty() )
			return GetNativeDevicePath() + L'$' + rFileName;
		else
			return GetNativeDevicePath();
	}

private:	
	void MakeZipMetaInfo( CMetaInfoMap& mim, bool bMakeUnModify);
	void InitMetaInfoFromMetaFile();
	void CheckOpenFileMetaInfo( const std::wstring& filename, FILE_MODE fm);
	void MakeImageHandle();
	void CloseFile();
	int ReOpenFS();

public:
	virtual CPFSBase* Clone() const;
	virtual int Initialize( const std::wstring& device, MOUNT_TYPE mt);
	virtual FS_TYPE GetFSType() const;
	virtual int OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file);
	virtual int RemoveFile( const std::wstring& filename);
	virtual bool IsFileExisting( const std::wstring& filename);

	virtual int CreateDirectory( const std::wstring& path, bool bFailIfExisting = false );
	virtual int RemoveDirectory( const std::wstring& path, bool bFailIfExisting = false );
	virtual bool IsDirectoryExisting( const std::wstring& path );

	virtual bool IsFSBusy() const;
	virtual void CheckMetaInfo();
	virtual bool Commit( );
	virtual bool Destroy( bool fastUnmount);
};

//////////////////////////////////////////////////////////////////////////
//
// Inline functions 
//
inline FS_TYPE CZipFS::GetFSType() const
{
	return FST_ZIP;
}

inline void CZipFS::OnFileClose(CBaseFile* pFile )
{
	CMutex::CScoped lock( m_cs );
	m_mapFileAccess.erase(pFile);
}

inline CPFSBase* CZipFS::Clone() const
{
	return new CZipFS();
}

inline bool CZipFS::IsFSBusy() const
{
	return !m_mapFileAccess.empty();
}

} // PFS namespace