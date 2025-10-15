#pragma once
#include <assert.h>
#include "../packzip/basethread.h"
#include "../packzip/FindPath.h"
#include "../../setupmetafile.h"
#include "../install/ShortCut.h"
#include "../install/Config.h"
#include "GlobalFunction.h"

class CDeleteOperator
{
protected:
	std::wstring		m_path;
	std::wstring		m_appname;
	CThreadListener*	m_listener;

	// 删除快捷方式
	bool DeleteLink()
	{
		if( CConfig::GetInstance().m_programegroup.empty())
			return false;
		CSpecialFolder specialfolder;
		specialfolder.SetProgramGroup( CConfig::GetInstance().m_programegroup);

		for( CConfig::CShortCutVector::iterator itor = CConfig::GetInstance().m_shortcuts.begin();
			itor != CConfig::GetInstance().m_shortcuts.end(); ++itor)
		{
			std::wstring path = specialfolder.GetFolder( itor->m_pos);
			if( itor->m_pos == DESKTOP)
			{
				path += L"\\" + itor->m_name + L".lnk";
				::DeleteFile( path.c_str());
			}
			else
			{
				CGlobalFunction::DeletePath( path);
			}
		}
		return true;
	}

	bool DeleteOneFile( const std::wstring& file)
	{
		std::wstring filename = m_path + L"\\" + file;
		if( m_listener)
			m_listener->OnNotify( (WPARAM)file.c_str(), FILENAME);
		if( !::DeleteFile( filename.c_str()))
		{
			if( ::GetLastError() == ERROR_ACCESS_DENIED)
			{
				return false;
			}
		}
		return true;
	}

public:	
	enum
	{
		TOTALCOUNT,
		TOTALEND,
		FILENAME,
		PATHNAME,
		DELETELINK,
	};
	CDeleteOperator() : m_listener( NULL) {}
	void SetAppPath( const std::wstring& path, const std::wstring& appname)
	{
		m_path = path;
		m_appname = appname;
	}
	void SetListener( CThreadListener* listener) { m_listener = listener; }
	virtual bool Do() = 0;
	static DWORD m_error;
};

const std::wstring FILES_META = L".files.meta";
const std::wstring SETUP_META = L".setup.meta";
const std::wstring UNINSTALL_XML = L"uninstall.xml";
// 只删除必要文件
class CDeleteSystemOperator : public CDeleteOperator
{
	class CDeleteEmptyPath : public CFindPath
	{
	public:
		CDeleteEmptyPath( const std::wstring& path) : CFindPath( path) {}
		virtual bool OnFind( const std::wstring& child, bool bPath)
		{
			if( bPath)
			{
				std::wstring name = m_basepath + child + L"\\";
				CDeleteEmptyPath deleter( name);
				deleter.DoFind();
				::RemoveDirectory( name.c_str());
			}
			return false;
		}
	};

	bool DeleteEmptyPath( )
	{
		CDeleteEmptyPath deleteemptypath( m_path);
		deleteemptypath.DoFind();
		return true;
	}

	bool DeleteSystemFiles()
	{
		PFSX::CNativeFile metafile;
		if( !metafile.Open( m_path + FILES_META, PFS::FM_EXCL, PFS::FA_RDONLY))
			return false;
		PFS::CMetaInfoMap mim;
		if( !PFS::CMetaFileRead::DoRead( metafile, mim))
			return false;
		metafile.Close();
		
		PFSX::SetupPathVector setups;
		if( PFSX::CSetupMetaFile::DoRead( m_path + SETUP_META, setups))
		{
			for( PFSX::SetupPathVector::iterator itor = setups.begin(); itor != setups.end(); ++itor)
			{
				mim.insert( std::make_pair( *itor + L".pfs", PFS::CMetaInfo()));
			}
		}
	
		if( m_listener)
			m_listener->OnNotify( mim.size(), TOTALCOUNT);

		for( PFS::CMetaInfoMap::iterator itorMeta = mim.begin(); itorMeta != mim.end(); ++itorMeta)
		{
			std::wstring filename = itorMeta->first;
			filename.erase( 0, filename.find_first_not_of( L'/'));
			if( filename == SETUP_META || filename == FILES_META
				|| filename == m_appname || filename == UNINSTALL_XML)
				continue;
			if( !DeleteOneFile( filename))
				return false;
			Sleep( 10);
		}
		// 删除files.meta里没有的特殊文件
		DeleteOneFile( UNINSTALL_XML);
		// files.meta和setup.meta最后删除
		DeleteOneFile( FILES_META);
		DeleteOneFile( SETUP_META);
		return true;
	}
public:
	CDeleteSystemOperator() {}
	virtual bool Do()
	{
		// 先删文件
		if( DeleteSystemFiles())
		{
			// 再删空目录
			DeleteEmptyPath( );
			// 删快捷方式
			if( m_listener)
				m_listener->OnNotify( 1, DELETELINK);
			DeleteLink();
		}
		else
		{
			CDeleteOperator::m_error = ::GetLastError();
		}
		// 结束
		if( m_listener)
			m_listener->OnNotify( 1, TOTALEND);
		return true;
	}
};

// 删除安装目录下所有文件
class CDeleteAllOperator : public CDeleteOperator
{
	class CDeleteAllFindPath : public CFindPath
	{
		CDeleteAllOperator&		m_ope;
	public:
		CDeleteAllFindPath( CDeleteAllOperator& ope) : CFindPath( ope.m_path), m_ope( ope) {}
		virtual bool OnFind( const std::wstring& child, bool bPath)
		{
			std::wstring name = m_basepath + child;
			if( bPath)
			{
				if( m_ope.m_listener)
					m_ope.m_listener->OnNotify( (WPARAM)child.c_str(), PATHNAME);
				if( !CGlobalFunction::DeletePath( name))
				{
					CDeleteOperator::m_error = ::GetLastError();
					return true;
				}
			}
			else
			{
				if( child == SETUP_META || child == FILES_META
					|| child == m_ope.m_appname || child == UNINSTALL_XML)	
				{
					return false;
				}
				if( m_ope.m_listener)
					m_ope.m_listener->OnNotify( (WPARAM)child.c_str(), FILENAME);
				if( !::DeleteFile( name.c_str()))
				{
					CDeleteOperator::m_error = ::GetLastError();
					return true;
				}
			}
			Sleep( 10);
			return false;
		}
	};

	// 计算需要删除文件的数量
	class CChildCountFindPath : public CFindPath
	{
		int		m_count;
	public:
		CChildCountFindPath( const std::wstring& path) : CFindPath( path), m_count( 0) {}
		int GetChildCount() { return m_count; }
		virtual bool OnFind( const std::wstring& child, bool bPath)
		{
			m_count++;
			return false;
		}
	};

public:
	virtual bool Do()
	{
		if( m_path.empty())
			return false;

		CChildCountFindPath counter( m_path);
		counter.DoFind();
		if( m_listener)
			m_listener->OnNotify( counter.GetChildCount(), TOTALCOUNT);

		CDeleteAllFindPath deleter( *this);
		if( !deleter.DoFind())
		{
			// 删除files.meta里没有的特殊文件
			DeleteOneFile( UNINSTALL_XML);
			// files.meta和setup.meta最后删除
			DeleteOneFile( FILES_META);
			DeleteOneFile( SETUP_META);

			if( m_listener)
				m_listener->OnNotify( 1, DELETELINK);
			DeleteLink();
		}

		if( m_listener)
			m_listener->OnNotify( 1, TOTALEND);
		return true;
	}
};
