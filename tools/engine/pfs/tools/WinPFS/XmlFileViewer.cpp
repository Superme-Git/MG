#include "StdAfx.h"
#include "XmlFileViewer.h"
#include "../../pfs2.h"
#include "../../../xmlio/xmlio.h"
#include <sstream>

CXmlFileViewer::CXmlFileViewer(void)
{
}

CXmlFileViewer::~CXmlFileViewer(void)
{
}

// 格式化xml字符串
static void FormatXmlText( XMLIO::CINode& node, int level, std::wostringstream& woss)
{
	for( int i=0; i<level; i++)
	{
		woss << L"\t";
	}

	std::wstring name = node.GetName( );
	std::wstring value = node.GetText();

	if ( name.size() > 0 && name[0] == L'#')
	{
		if( name == L"#comment" )
		{
			woss << L"<!--"<< value << L"-->\r\n";
			return;
		}
		else if ( name == L"#cdata-section" )
		{
			woss << L"<![CDATA["<< value << L"]]>\r\n";
			return;
		}
		else if (name == L"#text" )
		{
			woss << value << L"\r\n";
			return;
		}
	}

	// name
	woss << L"<" << name;

	// attribute
	XMLIO::CAttributesNames atts;
	node.GetAttributeNames( atts);
	std::wstring attValue;
	for( XMLIO::CAttributesNames::const_iterator i=atts.begin(), e=atts.end(); i != e; ++i)
	{
		const std::wstring& attName = *i;
		node.GetAttribute( attName, attValue);
		woss << L" " << attName << L"=" << L"\"" << attValue << L"\"";
	}

	size_t count = node.GetChildrenCount();

	if( value.empty() && 0 == count)
	{
		woss << L"/>\r\n";
	}
	else
	{
		woss << L">" ;

		if ( count > 0 )
		{
			woss << L"\r\n" ;
			// 有子结点 - children
			XMLIO::CINode child;
			for( size_t i=0; i<count; i++)
			{
				if( node.GetChildAt( i, child))
				{
					FormatXmlText( child, level+1, woss);
				}
			}

			if( !value.empty() )
			{
				for( int i=0; i<level; i++)
				{
					woss << L"\t";
				}
				woss << value << L"\r\n";
			}

			for( int i=0; i<level; i++)
			{
				woss << L"\t";
			}
		}
		else
		{
			woss << value;
		}

		woss << L"</" << name << L">\r\n";
	}
}

bool CXmlFileViewer::SetData( const void* pData, size_t len )
{
	XMLIO::CFileReader fr;
	if( XMLIO::EC_SUCCESS != fr.OpenData( pData, len))
	{
		m_wndEditPanel.SetWindowText( L"不是有效的xml文件");
		return false;
	}

	// 因为当前的CFileReader::SaveAsString()接口已经完全实现了所有格式的XML文本化
	// 这里改为直接使用SaveAsString接口完成转换过程。
	// [2010-04-01 yanghaibo] 
	std::wstring str;
	fr.SaveAsString( str );
	m_wndEditPanel.SetSel( 0, -1);
	m_wndEditPanel.ReplaceSel( str.c_str());

	/*
	XMLIO::CINode root;
	if( !fr.GetRootNode( root))
	{
	m_wndEditPanel.SetWindowText( L"不是有效的xml文件");
	return false;
	}
	std::wostringstream woss;
	FormatXmlText( root, 0, woss);
	m_wndEditPanel.SetSel( 0, -1);
	m_wndEditPanel.ReplaceSel( woss.str().c_str());
	*/
	return true;
}

