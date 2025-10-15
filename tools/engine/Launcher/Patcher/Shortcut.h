//////////////////////////////////////////////////////////////////////////
//
// Shortcut.h
//
// 说明:
// 本文件实现方案源于install.exe/install.xml.
// 我们为了保证更新程序更新完毕之后同时能够更新快捷方式，才无条件执行此操作。
//
//////////////////////////////////////////////////////////////////////////


#pragma once
#include <string>
#include <vector>
#include "../../pfs/pfs.h"
#include "../../xmlio/msdom.h"

class CShortcut
{
	CShortcut() {}
public:
	static BOOL Create( LPCTSTR szSrc, LPCTSTR szLink)
	{
		BOOL res = FALSE;
		HRESULT hr = CoInitialize( NULL);
		if( SUCCEEDED(hr))
		{
			IShellLink *pisl = NULL;
			hr = CoCreateInstance( CLSID_ShellLink, NULL, CLSCTX_INPROC_SERVER, IID_IShellLink, (void**)&pisl);
			if( SUCCEEDED(hr))
			{
				pisl->SetPath( szSrc);
				IPersistFile* pIPF = NULL;
				hr = pisl->QueryInterface( IID_IPersistFile, (void**)&pIPF);
				if( SUCCEEDED(hr))
				{
					res = SUCCEEDED( pIPF->Save( szLink, FALSE));
					pIPF->Release();
				}
				pisl->Release();
			}
			CoUninitialize();
		}
		return res;
	}
};

const std::wstring DESKTOP = L"%desktop%";
const std::wstring PRODUCE = L"%product%";
class CSpecialFolder
{
	std::wstring	m_programgroup;
public:
	
	static std::wstring GetSpecialFolder( int nFolder)
	{
		LPITEMIDLIST pidl = NULL;
		LPMALLOC pShellMalloc = NULL;
		std::wstring res;
		wchar_t buf[MAX_PATH] = {0};
		if( SUCCEEDED( SHGetMalloc( &pShellMalloc)))
		{
			if( SUCCEEDED( SHGetSpecialFolderLocation( NULL, nFolder, &pidl)))
			{
				if( SHGetPathFromIDList( pidl, buf))
					res = buf;
				pShellMalloc->Free( pidl);
			}
			pShellMalloc->Release();
		}
		return res;
	}

	void SetProgramGroup( const std::wstring& programgroup) { m_programgroup = programgroup; }
	std::wstring GetFolder( const std::wstring& pos)
	{
		if( pos == DESKTOP)
			return GetSpecialFolder( CSIDL_DESKTOPDIRECTORY);
		else if( pos == PRODUCE)
		{
			std::wstring path = GetSpecialFolder( CSIDL_PROGRAMS);
			path += L"\\" + m_programgroup;
			return path;
		}
		return L"";
	}
};

class CConfig
{
	CConfig() : m_needspace( 0) {}
	~CConfig() {}

public:
	std::wstring	m_product;
	std::wstring	m_url;
	std::wstring	m_programegroup;
	std::wstring	m_programefile;
	size_t			m_needspace;
	
	class CShortCutInfo
	{
	public:
		std::wstring	m_name;
		std::wstring	m_link;
		std::wstring	m_pos;
		CShortCutInfo() {}
		CShortCutInfo( const CShortCutInfo& right) { *this = right; }
		CShortCutInfo& operator=( const CShortCutInfo& right)
		{
			if( this != &right)
			{	
				m_name = right.m_name;
				m_link = right.m_link;
				m_pos = right.m_pos;
			}
			return *this;
		}
	};
	typedef	std::vector<CShortCutInfo>		CShortCutVector;
	CShortCutVector		m_shortcuts;

	typedef std::vector<std::wstring>		CStrVector;
	CStrVector			m_strs;
	
	// 忽略的代码
	//PFS::CDataBlock			m_md5;
public:
	static CConfig& GetInstance()
	{
		static CConfig config;
		return config;
	}
	bool Open( const std::wstring& configfile)
	{
		MSXMLDOM::CReader reader;
		if( PFS::EC_SUCCESS != reader.OpenNativeFile( configfile))
			return false;
		MSXMLDOM::CReadNode root;
		reader.GetRootNode( root );
		MSXMLDOM::CReadNode child;
		std::wstring strName;
		std::wstring strValue;
		size_t count = root.GetChildrenCount();

		for( size_t i=0; i<count; i++)
		{
			if( !root.GetChildAt( i, child) )
			{
				continue;
			}

			child.GetName(strName);
			child.GetAttribute( L"name", strValue);
			
			if( strName == L"setup")					// setup
				ParseSetupNode( child);
			else if( strName == L"shortcut")			// shortcut
				ParseShortcutNode( child);		
			else if( strName == L"restable")			// str			
				ParseStrNode( child);		
			else if( strName == L"md5code")				// md5code
				ParseMd5CodeNode( child);
		}
		return true;
	}
	static const std::wstring& GetStr( size_t id)
	{
		if( (id >= 0) && (id < GetInstance().m_strs.size()))
			return GetInstance().m_strs[id];
		static std::wstring str;
		return str;
	}
	void CreateLink( const std::wstring& installpath, bool bCreateDesktopLink)
	{
		CSpecialFolder specialfolder;
		specialfolder.SetProgramGroup( m_programegroup);

		for( CConfig::CShortCutVector::iterator itor = CConfig::GetInstance().m_shortcuts.begin();
			itor != CConfig::GetInstance().m_shortcuts.end(); ++itor)
		{
			if( !bCreateDesktopLink && ( itor->m_pos == DESKTOP))
				continue;
			std::wstring path = specialfolder.GetFolder( itor->m_pos);
			if( !PFS::CFileIO::IsDirectoryExisting( path))
				CreateDirectory( path.c_str(), NULL);
			path += L"\\" + itor->m_name + L".lnk";
			std::wstring src = installpath;
			src += L"\\" + itor->m_link;
			CShortcut::Create( src.c_str(), path.c_str());
		}
	}

protected:
	void ParseSetupNode( const MSXMLDOM::CReadNode& child)
	{
		size_t count = child.GetChildrenCount();
		MSXMLDOM::CReadNode item;
		std::wstring strName;
		std::wstring strValue;
		for( size_t i=0; i<count; i++)
		{
			if( !child.GetChildAt( i, item ) )
			{
				continue;
			}

			item.GetName(strName);
			item.GetAttribute( L"name", strValue);
			if( strName == L"product")					
				m_product.swap( strValue );
			else if( strName == L"url")
				m_url.swap( strValue );
			else if( strName == L"programgroup")
				m_programegroup.swap( strValue );
			else if( strName == L"programfile")
				m_programefile.swap( strValue );
			else if( strName == L"needspace")
			{
				m_needspace = _wtoi(strValue.c_str());
			}
		}
	}
	void ParseShortcutNode( const MSXMLDOM::CReadNode& child)
	{
		size_t count = child.GetChildrenCount();
		MSXMLDOM::CReadNode shortcutnode;
		for( size_t i=0; i<count; i++)
		{
			if( child.GetChildAt( i, shortcutnode) )
			{
				CShortCutInfo shortcut;
				shortcutnode.GetAttribute( L"name", shortcut.m_name );
				shortcutnode.GetAttribute( L"link", shortcut.m_link );
				shortcutnode.GetAttribute( L"pos", shortcut.m_pos );
				m_shortcuts.push_back( shortcut);
			}
		}
	}
	void ParseStrNode( const MSXMLDOM::CReadNode& child)
	{
		size_t count = child.GetChildrenCount();
		MSXMLDOM::CReadNode node;
		std::wstring strValue;
		for( size_t i=0; i<count; i++)
		{
			if( child.GetChildAt( i, node ) )
			{	
				node.GetAttribute( L"name", strValue);
				m_strs.push_back( strValue );
			}
		}
	}
	void ParseMd5CodeNode( const MSXMLDOM::CReadNode& child)
	{
		// 忽略的代码
		//std::wstring md5 = child.GetAttribute( L"name");
		//m_md5 = PFS::CTransfer::String2Data( md5);
	}
};
