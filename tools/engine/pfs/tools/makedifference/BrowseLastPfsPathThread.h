#pragma once
#include <string>
#include <vector>
#include <map>
#include <sstream>
#include <assert.h>
#include "../packzip/basethread.h"
#include "../packzip/GlobalErrorString.h"
#include "../../pfs.h"
#include "../../src/meta/metafileio.h"
#include "../../versionmetafile.h"
#include "FindFiles.h"

#define WM_BROWSELASTPFSPATH		(WM_USER+101)

struct CFileInfo
{
	std::wstring	m_filename;
	DWORD			m_version;
	DWORD			m_lastVersion;
};

typedef std::map<std::wstring, CFileInfo>	CFileInfoMap;

class CBrowseLastPfsPathThread : public CBaseThread
{
	std::wstring				m_lastpfspath;
	CFileInfoMap				m_lastpfss;
	CMutex						m_lock;

	bool CheckOneFile( const std::wstring& filename)
	{
		static int count = 0;
		std::wostringstream wos;
		wos << L"/last" << (count++);
		const std::wstring& mountstr = wos.str();		// 保证mountstr唯一
		std::wstring versionStr, lastVersionStr;
		CFileInfo fileinfo;

		try
		{
			if( !PFS::CEnv::Mount( mountstr, filename, PFS::FST_RUNZIP, PFS::MT_READONLY))
				throw 0;
			PFS::CFile file;
			if( !file.Open( mountstr + L"/.version.meta", PFS::FM_EXCL, PFS::FA_RDONLY))
				throw 0;
			PFSX::CVersionInfo versionfile;
			if( !versionfile.LoadFrom( file))
				throw 0;
			fileinfo.m_version = versionfile.m_Version;
			PFSX::Version2String( versionfile.m_Version, versionStr);
			fileinfo.m_lastVersion = versionfile.m_lastVersion;
			PFSX::Version2String( versionfile.m_lastVersion, lastVersionStr);
		}
		catch( int)
		{
			PFS::CEnv::Unmount( mountstr);
		}
		
		CErrorStringArray::GetInstance().Push( filename);
		if( !versionStr.empty())
		{
			fileinfo.m_filename = filename;
			{
				CScoped	scoped( m_lock);
				m_lastpfss.insert( std::make_pair( mountstr, fileinfo));
			}
		}
		else
		{
			versionStr = L"格式不正确！";
		}
		CErrorStringArray::GetInstance().Push( versionStr);
		CErrorStringArray::GetInstance().Push( lastVersionStr);

		if( m_listener)
		{
			m_listener->OnNotify( 0, 0);
		}
		return true;
	}

	//class CFindLastPfsError {};
	//class CFindLastPfsFiles : public CFindFiles
	//{
	//	virtual void OnOneFoundPath( const std::wstring& pathname)	{}

	//	virtual void OnOneFoundFile( const std::wstring& filename)
	//	{
	//		if( m_thread.m_stop)	// 中途退出
	//			throw CFindLastPfsError();
	//		m_thread.CheckOneFile( m_strBasePath + filename);
	//	}

	//	CBrowseLastPfsPathThread&		m_thread;

	//public:

	//	CFindLastPfsFiles( const std::wstring& basepath, CBrowseLastPfsPathThread& thread) 
	//		: CFindFiles( basepath), m_thread( thread)
	//	{
	//	}
	//};


	virtual void OnRunning()
	{
		//try
		//{
		//	CFindLastPfsFiles finder( m_lastpfspath, *this);
		//	finder.DoFind();
		//}
		//catch( CFindLastPfsError&)
		//{
		//}
		CheckOneFile( m_lastpfspath);
	}

public:
	void SetLastPfsPath( const std::wstring& path)
	{
		m_lastpfspath = path;
	}

	void Clear()
	{
		std::vector<std::wstring> unmounts;
		unmounts.reserve( m_lastpfss.size());
		{
			CScoped	scoped( m_lock);

			for( CFileInfoMap::iterator i=m_lastpfss.begin(), e=m_lastpfss.end(); i!=e; ++i)
			{
				unmounts.push_back( i->first);
			}
			m_lastpfss.clear();
		}

		for( std::vector<std::wstring>::iterator i=unmounts.begin(), e=unmounts.end(); i!=e; ++i)
		{
			PFS::CEnv::Unmount( *i);
		}
	}

	bool DeleteFileItem( const std::wstring& filename)
	{
		std::wstring deletefile;
		{
			CScoped	scoped( m_lock);

			for( CFileInfoMap::iterator i = m_lastpfss.begin(), e=m_lastpfss.end(); i != e; ++i)
			{
				if( i->second.m_filename == filename)
				{
					deletefile = i->first;
					m_lastpfss.erase( i);
					break;
				}
			}
		}
		if( deletefile.empty())
			return false;
		PFS::CEnv::Unmount( deletefile);
		return true;
	}

	CFileInfoMap& GetFileInfoMap() { return m_lastpfss; }
};