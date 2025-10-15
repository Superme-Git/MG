///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : XMLHelper.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	XML Helper
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-14 Created.
///=============================================================================
#pragma once
#include <string>
#include <assert.h>
#import "msxml3.dll" raw_interfaces_only

#define CHECK_HR_RETURN(hr) if ( FAILED(hr) ) return (hr)
#define CHECK_HR_RETURN_VALUE(hr,val) if ( FAILED(hr) ) return (val)
#define CHECK_HR_RETURN_VOID(hr) if ( FAILED(hr) ) return;

namespace XMLHELPER
{
	///-----------------------------------------------------------------------------
	/// summary : COM服务器初始化和反初始化作用域
	///-----------------------------------------------------------------------------
	class CMSComServerScoped
	{
	public:
		CMSComServerScoped();
		~CMSComServerScoped();
	};

	///-----------------------------------------------------------------------------
	/// summary : XML结点访问器(Accessor)
	///-----------------------------------------------------------------------------
	class CXMLNode
	{
	protected:
		MSXML2::IXMLDOMNodePtr	m_Node;
	public:
		CXMLNode(){}
		~CXMLNode(){}
		CXMLNode( const CXMLNode& src) 
			: m_Node( src.m_Node)
		{}
		CXMLNode& operator=( const CXMLNode& src)
		{
			if( this == &src)
				return *this;
			m_Node = src.m_Node;
			return *this;
		}

		explicit CXMLNode( MSXML2::IXMLDOMNodePtr Node)
		{
			m_Node = Node;
		}

		MSXML2::IXMLDOMNodePtr GetInterface() const
		{
			return m_Node;
		}

	public:
		MSXML2::DOMNodeType GetType() const;
		void GetAttribute( const std::wstring& name, std::wstring& value) const;
		int GetAttributeInt32( const std::wstring& name, int defaultvalue) const;
		__int64 GetAttributeInt64( const std::wstring& name, __int64 defaultvalue) const;
		unsigned int GetAttributeUInt32( const std::wstring& name, unsigned int defaultvalue) const;
		double GetAttributeFloat( const std::wstring& name, double defaultvalue) const;
		void GetText( std::wstring& value) const;
		void GetSubText( std::wstring& value) const;
		void GetName( std::wstring& value) const;
	};

	class CXMLNode2: public XMLHELPER::CXMLNode
	{
	public:
		explicit CXMLNode2( MSXML2::IXMLDOMNodePtr Node)
			: CXMLNode( Node )
		{
		}

		// DWORD is a color type
		DWORD GetAttributeColor( const std::wstring& name ) const;
		bool GetAttributeBool( const std::wstring& name, bool defaultvalue) const;
	};

	// 加载XML文档,获取其根结点对象指针
	extern HRESULT LoadRootNodeFromXMLFile( BSTR bstrFileName, MSXML2::IXMLDOMNode** rpRootNodePtr );
	extern HRESULT LoadRootNodeFromXMLText( BSTR bstrXMLText, MSXML2::IXMLDOMNode** rpRootNodePtr );
	
	class CXMLFileWriter;
	class CXMLWriteNode 
	{
		friend class CXMLFileWriter;

		MSXML2::IXMLDOMElementPtr		m_node;
		CXMLFileWriter* m_writer;
		int	m_level;

	public:
		CXMLWriteNode() : m_writer( NULL), m_level(0) {}
		CXMLWriteNode( CXMLFileWriter* writer) : m_writer( writer), m_level(0) {}
		CXMLWriteNode( const CXMLWriteNode& src) : m_writer( src.m_writer), m_level( src.m_level), m_node( src.m_node) {}
		CXMLWriteNode& operator=( const CXMLWriteNode& src)
		{
			if( this == &src)
				return *this;
			assert( NULL == m_writer || src.m_writer == m_writer);
			m_writer = src.m_writer;
			m_node = src.m_node;
			m_level = src.m_level;
			return *this;
		}

		bool SetAttribute( const std::wstring& name, const std::wstring& value);
		CXMLWriteNode CreateSubElement( const std::wstring& name);
		bool InsertText( const std::wstring& text);
		bool InsertCData( const std::wstring& data);
	};

	class CXMLFileWriter
	{
		MSXML2::IXMLDOMElementPtr		m_RootElement;
		MSXML2::IXMLDOMDocument2Ptr		m_Document;

		bool CheckDocument();
		void OutXMLLine( CXMLWriteNode& node, int level);

	public:
		CXMLWriteNode CreateElement( CXMLWriteNode& parent, const std::wstring& name);
		bool InsertText( CXMLWriteNode& node, const std::wstring& text);

	public:
		bool GetRootNode( CXMLWriteNode& node)
		{
			if( !CheckDocument() )
			{
				return false;
			}

			node.m_writer = this;
			node.m_node = m_RootElement;
			return m_RootElement != NULL;
		}

		bool SaveToFile( const std::wstring& filename);

	};

} // namespace XMLHELPER