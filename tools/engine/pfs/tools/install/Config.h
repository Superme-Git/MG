#pragma once
#include <string>
#include <vector>
#include "../../src/meta/metafileio.h"
#include "../../src/native/rawfileio.h"
#include "../../../xmlio/xmlio.h"
#include "Shortcut.h"

class CConfig
{
	enum
	{
		DEFAULT_BKCHANGETIME = 5000,
	};

	CConfig()
		: m_needspace( 2000)
		, m_fullscreen(false)
		, m_bkChangeTime( DEFAULT_BKCHANGETIME)
		, m_checkOS64(false){}
	~CConfig() {}

public:
	std::wstring	m_product;
	std::wstring	m_url;
	std::wstring	m_programegroup;
	std::wstring	m_programefile;
	size_t			m_needspace;
	bool			m_fullscreen;
	DWORD			m_bkChangeTime;		// 背景图片切换间隔时间
	bool			m_checkOS64;		// 是否检测支持64位操作系统

	std::wstring	m_apppath;
	
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
	
	class CPackageInfo
	{
	public:
		std::wstring		m_name;
		//PFS::CDataBlock		m_md5;
		std::string			m_md5;
		CPackageInfo() {}
		CPackageInfo( const CPackageInfo& right) { *this = right; }
		CPackageInfo& operator=( const CPackageInfo& right)
		{
			if( this != &right)
			{	
				m_name = right.m_name;
				m_md5 = right.m_md5;
			}
			return *this;
		}
	};
	typedef std::vector<CPackageInfo>		CPackageVector;
	CPackageVector		m_packages;

public:
	static CConfig& GetInstance()
	{
		static CConfig config;
		return config;
	}
	bool Open( const std::wstring& apppath, const std::wstring& filename)
	{
		std::wstring strName = apppath + filename;
		XMLIO::CFileReader reader;
		if( PFS::EC_SUCCESS != reader.OpenNativeFile( strName))
			return false;
		m_apppath = apppath;

		XMLIO::CINode root;
		reader.GetRootNode(root);
		XMLIO::CINode child;
		
		//std::wstring strValue;
		size_t count = root.GetChildrenCount();
		for( size_t i=0; i<count; i++)
		{
			if( !root.GetChildAt( i, child))
			{
				continue;
			}

			strName = child.GetName();
			//child.GetAttribute( L"name", strValue);

			if( strName == L"setup")					// setup
				ParseSetupNode( child);
			else if( strName == L"shortcut")			// shortcut
				ParseShortcutNode( child);		
			else if( strName == L"restable")			// str			
				ParseStrNode( child);		
			else if( strName == L"pfs")				// md5code
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
	bool CreateLink( const std::wstring& installpath, bool bCreateDesktopLink)
	{
		CSpecialFolder specialfolder;
		specialfolder.SetProgramGroup( m_programegroup);

		bool bRes = true;
		for( CShortCutVector::iterator itor = m_shortcuts.begin(); itor != m_shortcuts.end(); ++itor)
		{
			if( !bCreateDesktopLink && ( itor->m_pos == DESKTOP))
				continue;
			std::wstring path = specialfolder.GetFolder( itor->m_pos);
			if( !PFS::CFileIO::IsDirectoryExisting( path))
			{
				if( !CreateDirectory( path.c_str(), NULL))
				{
					bRes = false;
					continue;
				}
			}
			path += L"\\" + itor->m_name + L".lnk";
			std::wstring src = installpath;
			src += L"\\" + itor->m_link;
			if( !CShortcut::Create( src.c_str(), path.c_str()))
			{
				bRes = false;
			}
		}
		return bRes;
	}

protected:
	void ParseSetupNode( const XMLIO::CINode& child)
	{
		size_t count = child.GetChildrenCount();
		std::wstring strName;
		std::wstring strValue;
		XMLIO::CINode item;
		for( size_t i=0; i<count; i++)
		{
			if( !child.GetChildAt( i, item ) )
			{
				continue;
			}

			strName = item.GetName();
			item.GetAttribute( L"name", strValue);
			if( strName == L"product")
			{
				m_product.swap( strValue );
			}
			else if( strName == L"url")
			{
				m_url.swap( strValue );
			}
			else if( strName == L"programgroup")
			{
				m_programegroup.swap( strValue );
			}
			else if( strName == L"programfile")
			{
				m_programefile.swap( strValue );
			}
			else if( strName == L"needspace")
			{
				m_needspace = _wtoi(strValue.c_str());
			}
			else if (strName == L"fullscreen")
			{
				m_fullscreen = ( strValue == L"true");
			}
			else if( strName == L"bkchangetime")
			{
				if( strValue.empty())
				{
					m_bkChangeTime = DEFAULT_BKCHANGETIME;
				}
				else
				{
					m_bkChangeTime = _wtoi( strValue.c_str());
				}
			}
			else if( strName == L"checkOS64")
			{
				m_checkOS64 = ( strValue == L"true");
			}
		}
	}
	void ParseShortcutNode( const XMLIO::CINode& child)
	{
		size_t count = child.GetChildrenCount();
		CShortCutInfo shortcut;
		XMLIO::CINode shortcutnode;
		for( size_t i=0; i<count; i++)
		{	
			if( child.GetChildAt( i, shortcutnode) )
			{
				shortcutnode.GetAttribute( L"name", shortcut.m_name );
				shortcutnode.GetAttribute( L"link", shortcut.m_link );
				shortcutnode.GetAttribute( L"pos", shortcut.m_pos );
				m_shortcuts.push_back( shortcut);
			}
		}
	}
	void ParseStrNode( const XMLIO::CINode& child)
	{
		size_t count = child.GetChildrenCount();
		std::wstring strValue;
		XMLIO::CINode node;
		for( size_t i=0; i<count; i++)
		{
			if( child.GetChildAt( i, node ) )
			{	
				node.GetAttribute( L"name", strValue);
				m_strs.push_back( strValue );
			}
		}
	}
	void ParseMd5CodeNode( const XMLIO::CINode& child)
	{
		XMLIO::CINode package;
		size_t count = child.GetChildrenCount();
		for( size_t i=0; i<count; i++)
		{
			if( child.GetChildAt( i, package))
			{
				CPackageInfo pi;
				package.GetAttribute( L"name", pi.m_name );

				PFSX::CNativeFile md5file;
				std::wstring md5filename = m_apppath + pi.m_name + L".md5";
				if( md5file.Open( md5filename, PFS::FM_EXCL, PFS::FA_RDONLY))
				{
					size_t len = (size_t)md5file.GetSize() ;
					pi.m_md5.resize(len);
					md5file.Read( (void*)pi.m_md5.c_str(), pi.m_md5.size());
					// 如果len<32，应该是不合法的MD5文件。
					// 不过，为了兼容将来可能出现的算法变化，暂时不对长度进行此严格检查。
					// 下面的逻辑仅仅用于支持linux下的md5sum命令生成的标准md5文件的处理逻辑。
					// 老版本的data.pfs.md5文件只含有32个字符的MD5值，并没有文件名。
					// 这种格式的md5文件不支持linux下的md5sum命令的校验。
					// 我们下面的处理用于从这种类型的MD5文件中截取MD5值，删去文件名部分。
					// 此逻辑实现之后，新的PackZip可以生成带有文件名的指纹文件。
					// 2010-11-10 yanghaibo
					if( len > 32 )
					{
						std::string::size_type md5value_end = pi.m_md5.find_first_of("\x20\t\v\f\r\n", 32 );
						if( md5value_end != std::string::npos && md5value_end >= 32 )
						{
							pi.m_md5.resize( md5value_end );
							// 其实，截取的部分应该含有pi.m_name这个名字。这里暂不做检查。
						}
					}
				}
				m_packages.push_back( pi);
			}
		}
	}
};