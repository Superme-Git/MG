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
	DWORD			m_bkChangeTime;		// ����ͼƬ�л����ʱ��
	bool			m_checkOS64;		// �Ƿ���֧��64λ����ϵͳ

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
					// ���len<32��Ӧ���ǲ��Ϸ���MD5�ļ���
					// ������Ϊ�˼��ݽ������ܳ��ֵ��㷨�仯����ʱ���Գ��Ƚ��д��ϸ��顣
					// ������߼���������֧��linux�µ�md5sum�������ɵı�׼md5�ļ��Ĵ����߼���
					// �ϰ汾��data.pfs.md5�ļ�ֻ����32���ַ���MD5ֵ����û���ļ�����
					// ���ָ�ʽ��md5�ļ���֧��linux�µ�md5sum�����У�顣
					// ��������Ĵ������ڴ��������͵�MD5�ļ��н�ȡMD5ֵ��ɾȥ�ļ������֡�
					// ���߼�ʵ��֮���µ�PackZip�������ɴ����ļ�����ָ���ļ���
					// 2010-11-10 yanghaibo
					if( len > 32 )
					{
						std::string::size_type md5value_end = pi.m_md5.find_first_of("\x20\t\v\f\r\n", 32 );
						if( md5value_end != std::string::npos && md5value_end >= 32 )
						{
							pi.m_md5.resize( md5value_end );
							// ��ʵ����ȡ�Ĳ���Ӧ�ú���pi.m_name������֡������ݲ�����顣
						}
					}
				}
				m_packages.push_back( pi);
			}
		}
	}
};