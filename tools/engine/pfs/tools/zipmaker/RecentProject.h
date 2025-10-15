#pragma once
#include "../../src/common.h"
#include <atlbase.h>
#include "../../../xmlio/msdom.h"

class CRecentProject
{
	std::vector<std::wstring>	m_projects;
	std::wstring				m_filename;

	~CRecentProject() { Save(); }

public:
	enum { MAXCOUNT = 20, };

	std::vector<std::wstring>& GetProjects() { return m_projects; }

	void AddProject( const std::wstring& project)
	{
		if( m_projects.size() >= MAXCOUNT)		// 最多保存maxcount个
			m_projects.erase( m_projects.begin());
		DelProject( project);
		m_projects.push_back( project);
	}
	void DelProject( const std::wstring& project)
	{
		std::vector<std::wstring>::iterator itor = std::remove( m_projects.begin(), m_projects.end(), project);
		if( itor != m_projects.end())
			m_projects.erase( itor);
	}

	bool Open()
	{
		m_filename.resize( MAX_PATH);
		::GetModuleFileName( NULL, const_cast<LPWSTR>( m_filename.c_str()), MAX_PATH);
		m_filename.erase( m_filename.find_last_of( L"\\")+1);
		m_filename += L"recentproject.xml";

		MSXMLDOM::CReader reader;
		if( PFS::EC_SUCCESS == reader.OpenNativeFile( m_filename))
		{
			MSXMLDOM::CReadNode root, child;
			reader.GetRootNode(root);
			size_t count = root.GetChildrenCount();
			std::wstring strValue;
			for( size_t i=0; i<count; i++)
			{
				root.GetChildAt( i, child);
				child.GetAttribute( L"name", strValue);
				m_projects.push_back( strValue );
			}
		}
		return true;
	}

	bool Save()
	{
		MSXMLDOM::CWriter writer;
		MSXMLDOM::CWriterNode root, child;
		writer.GetRootNode(root);
		for( std::vector<std::wstring>::iterator itor = m_projects.begin(); itor!=m_projects.end(); ++itor)
		{
			root.CreateSubElement( L"child", child); 
			child.SetAttribute( L"name", (*itor).c_str());
		}

		writer.SaveNative( m_filename);
		return true;
	}

	static CRecentProject& GetInstance()
	{
		static CRecentProject instance;
		return instance;
	}
};