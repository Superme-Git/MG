///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : XMLHelper.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	XML Helper
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-14 Created.
///=============================================================================
#include "StdAfx.h"
#include "XMLHelper.h"

#ifndef ASSERT
#define ASSERT(s) assert(s)
#endif

#define XASSERT(s) ASSERT(s)

namespace XMLHELPER
{
	CMSComServerScoped::CMSComServerScoped()
	{
		CoInitialize( NULL);
	}

	CMSComServerScoped::~CMSComServerScoped()
	{
		CoUninitialize();
	}

	CMSComServerScoped g_scope;

	HRESULT LoadRootNodeFromXMLText( BSTR bstrXMLText, MSXML2::IXMLDOMNode** rpRootNodePtr )
	{
		if ( bstrXMLText == NULL )
		{
			XASSERT( NULL != bstrXMLText );
			return E_INVALIDARG;
		}

		if( rpRootNodePtr == NULL )
		{
			XASSERT( NULL != rpRootNodePtr );
			return E_INVALIDARG;
		}

		MSXML2::IXMLDOMDocument2Ptr theDocument = NULL;
		HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL, 
			CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&theDocument);
		CHECK_HR_RETURN(hr);

		VARIANT_BOOL bResult;
		hr = theDocument->loadXML( bstrXMLText, &bResult);
		CHECK_HR_RETURN(hr);

		if( bResult == VARIANT_FALSE )
		{
			MSXML2::IXMLDOMParseErrorPtr parseError;
			hr = theDocument->get_parseError( &parseError );
			if ( SUCCEEDED(hr) )
			{
				BSTR bstrReason;
				long errorCode;
				long line, linepos, filePos;
				parseError->get_reason( &bstrReason );
				parseError->get_errorCode( &errorCode );
				parseError->get_line( &line );
				parseError->get_linepos( &linepos );
				parseError->get_filepos( &filePos );
				SysFreeString( bstrReason );

				return HRESULT_FROM_WIN32(errorCode);
			}

			return E_FAIL;
		}

		MSXML2::IXMLDOMNodeListPtr nodeListPtr;
		hr = theDocument->get_childNodes( &nodeListPtr );
		CHECK_HR_RETURN(hr);

		long lCount = 0;
		hr = nodeListPtr->get_length( &lCount );
		CHECK_HR_RETURN(hr);
		if (lCount < 1)
		{
			return E_FAIL;
		}

		MSXML2::IXMLDOMNodePtr tempNode;
		for ( long lIndex = 0; lIndex < lCount; lIndex ++ )
		{
			hr = nodeListPtr->get_item( lIndex, &tempNode );
			CHECK_HR_RETURN(hr);
			MSXML2::DOMNodeType nodeType;
			hr = tempNode->get_nodeType(&nodeType);
			CHECK_HR_RETURN(hr);
			if ( nodeType == MSXML2::NODE_ELEMENT )
			{
				*rpRootNodePtr = tempNode.Detach();
				break;
			}
		}
		
		return *rpRootNodePtr != NULL ? S_OK : E_FAIL;
	}

	HRESULT LoadRootNodeFromXMLFile( BSTR bstrFileName, MSXML2::IXMLDOMNode** rpRootNodePtr )
	{
		if ( bstrFileName == NULL )
		{
			XASSERT( NULL != bstrFileName );
			return E_INVALIDARG;
		}

		if( rpRootNodePtr == NULL )
		{
			XASSERT( NULL != rpRootNodePtr );
			return E_INVALIDARG;
		}

		MSXML2::IXMLDOMDocument2Ptr theDocument = NULL;
		HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL, 
			CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&theDocument);
		CHECK_HR_RETURN(hr);

		VARIANT_BOOL bResult;
		VARIANT varSource;
		VariantClear(&varSource);
		varSource.vt = VT_BSTR;
		varSource.bstrVal = bstrFileName; // 临时使用,无需释放
		theDocument->put_async(VARIANT_TRUE);
		hr = theDocument->load( varSource, &bResult);
		CHECK_HR_RETURN(hr);

		if( bResult == VARIANT_FALSE )
		{
			MSXML2::IXMLDOMParseErrorPtr parseError;
			hr = theDocument->get_parseError( &parseError );
			if ( SUCCEEDED(hr) )
			{
				BSTR bstrReason;
				long errorCode;
				long line, linepos, filePos;
				parseError->get_reason( &bstrReason );
				parseError->get_errorCode( &errorCode );
				parseError->get_line( &line );
				parseError->get_linepos( &linepos );
				parseError->get_filepos( &filePos );
				SysFreeString( bstrReason );

				return HRESULT_FROM_WIN32(errorCode);
			}

			return E_FAIL;
		}

		MSXML2::IXMLDOMNodeListPtr nodeListPtr;
		hr = theDocument->get_childNodes( &nodeListPtr );
		CHECK_HR_RETURN(hr);

		long lCount = 0;
		hr = nodeListPtr->get_length( &lCount );
		CHECK_HR_RETURN(hr);
		if (lCount < 1)
		{
			return E_FAIL;
		}

		MSXML2::IXMLDOMNodePtr tempNode;
		for ( long lIndex = 0; lIndex < lCount; lIndex ++ )
		{
			hr = nodeListPtr->get_item( lIndex, &tempNode );
			CHECK_HR_RETURN(hr);
			MSXML2::DOMNodeType nodeType;
			hr = tempNode->get_nodeType(&nodeType);
			CHECK_HR_RETURN(hr);
			if ( nodeType == MSXML2::NODE_ELEMENT )
			{
				*rpRootNodePtr = tempNode.Detach();
				break;
			}
		}

		return *rpRootNodePtr != NULL ? S_OK : E_FAIL;
	}

	MSXML2::DOMNodeType CXMLNode::GetType() const
	{
		MSXML2::DOMNodeType			domnt;
		if( m_Node == NULL)
			return MSXML2::NODE_INVALID;
		HRESULT hr = m_Node->get_nodeType( &domnt);
		if( FAILED( hr))
			return MSXML2::NODE_INVALID;
		else
			return domnt;
	}

	void CXMLNode::GetAttribute( const std::wstring& name, std::wstring& value) const
	{
		if( m_Node == NULL)
			return;
		MSXML2::IXMLDOMElementPtr	EleChild;
		HRESULT hr = m_Node->QueryInterface( __uuidof(MSXML2::IXMLDOMElement), (void**)&EleChild);
		if( FAILED( hr) || EleChild == NULL)
			return;

		VARIANT	 variant;
		VariantInit(&variant);
		hr = EleChild->getAttribute( (BSTR)name.c_str(), &variant);
		CHECK_HR_RETURN_VOID(hr);

		if( VT_BSTR != variant.vt)
		{
			VariantClear(&variant);
			return;
		}

		value = variant.bstrVal;
		VariantClear(&variant);

		return;
	}

	int CXMLNode::GetAttributeInt32( const std::wstring& name, int defaultvalue) const
	{
		std::wstring value;
		GetAttribute( name, value);
		if( value.empty())
			return defaultvalue;
		else
			return _wtoi( value.c_str());
	}

	__int64 CXMLNode::GetAttributeInt64( const std::wstring& name, __int64 defaultvalue) const
	{
		std::wstring value;
		GetAttribute( name, value);
		if( value.empty())
			return defaultvalue;
		else
			return _wtoi64( value.c_str());
	}

	unsigned int CXMLNode::GetAttributeUInt32( const std::wstring& name, unsigned int defaultvalue) const
	{
		std::wstring value;
		GetAttribute( name, value);
		if( value.empty())
			return defaultvalue;
		else
			return wcstoul( value.c_str(), NULL, 10);
	}

	double CXMLNode::GetAttributeFloat( const std::wstring& name, double defaultvalue) const
	{
		std::wstring value;
		GetAttribute( name, value);
		if( value.empty())
			return defaultvalue;
		else
			return _wtof( value.c_str());
	}

	void CXMLNode::GetText( std::wstring& value) const
	{
		value.clear();

		if( m_Node == NULL)
			return;
		BSTR		bstrText = NULL;
		HRESULT hr = m_Node->get_text(&bstrText);
		CHECK_HR_RETURN_VOID(hr);

		value = bstrText;
		SysFreeString( bstrText );
	}

	void CXMLNode::GetSubText( std::wstring& value) const
	{
		value.clear();

		if( m_Node == NULL)
			return;

		MSXML2::IXMLDOMNodeListPtr nodeListPtr;
		HRESULT hr = m_Node->get_childNodes( &nodeListPtr );
		CHECK_HR_RETURN_VOID(hr);

		long lCount = 0;
		hr = nodeListPtr->get_length( &lCount );
		CHECK_HR_RETURN_VOID(hr);

		MSXML2::IXMLDOMNodePtr nodePtr;
		for( long lIndex = 0; lIndex < lCount; lIndex ++ )
		{
			hr = nodeListPtr->get_item( lIndex, &nodePtr );
			CHECK_HR_RETURN_VOID(hr);

			VARIANT variant;
			VariantInit(&variant);
			HRESULT hr = nodePtr->get_nodeValue( &variant);
			CHECK_HR_RETURN_VOID(hr);

			if( VT_BSTR != variant.vt)
			{
				VariantClear(&variant);
				continue;
			}

			value = variant.bstrVal;
			VariantClear(&variant);
			return;
		}
	}

	void CXMLNode::GetName( std::wstring& value) const
	{
		if( m_Node == NULL)
			return;

		BSTR name;
		HRESULT hr = m_Node->get_nodeName( &name);
		CHECK_HR_RETURN_VOID(hr);

		value = name;
		SysFreeString(name);
	}

	inline DWORD MakeColor( long r, long g, long b, long a = 255)
	{
		return  ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff) | (a << 24);
	}

	inline DWORD StringToColor( const wchar_t* color)
	{
		if( NULL == color)
			return 0;
		if( 0 == color[0])
			return 0;

		int r = 0, g = 0, b = 0, a = 255;

		// r
		r = _wtoi( color);

		// g
		const wchar_t* szFind  =  wcschr( color, L',');
		if( NULL == szFind)
			return MakeColor( r, g, b, a);
		color = szFind + 1;
		g = _wtoi( color);

		// b
		szFind  =  wcschr( color, L',');
		if( NULL == szFind)
			return MakeColor( r, g, b, a);
		color = szFind + 1;
		b = _wtoi( color);

		// a
		szFind  =  wcschr( color, L',');
		if( szFind)
		{
			color = szFind + 1;
			a = _wtoi( color);
		}

		return MakeColor( r, g, b, a);
	}

	DWORD CXMLNode2::GetAttributeColor( const std::wstring& name ) const
	{
		std::wstring value;
		GetAttribute( name, value);
		DWORD clr = StringToColor( value.c_str() );
		return clr;
	}

	bool CXMLNode2::GetAttributeBool( const std::wstring& name, bool defaultvalue) const
	{
		std::wstring value;
		GetAttribute( name, value);
		if ( value == L"true" )
		{
			return true;
		}
		else if ( value == L"false" )
		{
			return false;
		}
		else 
		{
			return defaultvalue;
		}
	}

	bool CXMLWriteNode::SetAttribute( const std::wstring& name, const std::wstring& value)
	{
		if( m_node == NULL)
			return false;

		HRESULT hr = m_node->setAttribute( (BSTR)name.c_str(), CComVariant( value.c_str()));
		return SUCCEEDED( hr);
	}

	CXMLWriteNode CXMLWriteNode::CreateSubElement( const std::wstring& name)
	{
		assert( m_writer);
		return m_writer->CreateElement( *this, name);
	}

	bool CXMLWriteNode::InsertText( const std::wstring& text)
	{
		assert (m_writer);
		return m_writer->InsertText( *this, text);
	}

	bool CXMLWriteNode::InsertCData( const std::wstring& data)
	{
		return true;
	}

	bool CXMLFileWriter::CheckDocument()
	{
		if( m_RootElement != NULL)
			return true;
		if( m_Document == NULL)
		{
			HRESULT hr = CoCreateInstance( CLSID_DOMDocument, NULL, 
				CLSCTX_INPROC_SERVER, IID_IXMLDOMDocument, (void**)&m_Document);
			if( FAILED( hr) || m_Document == NULL)
				return false;
			MSXML2::IXMLDOMProcessingInstructionPtr pi = NULL;
			hr = m_Document->createProcessingInstruction( (BSTR)L"xml", (BSTR)L"version=\"1.0\" encoding=\"UTF-16\"", &pi);
			if( FAILED( hr) )
				return false;
			hr = m_Document->appendChild( pi, NULL);
			if( FAILED( hr) )
				return false;
			
		}
		assert( m_Document != NULL);
		HRESULT hr = m_Document->createElement( L"data", &m_RootElement);
		if( FAILED( hr) || m_RootElement == NULL)
			return false;
		m_Document->putref_documentElement( m_RootElement);
		
		return true;
	}

	void CXMLFileWriter::OutXMLLine( CXMLWriteNode& node, int level)
	{
		std::wstring strtext = L"\r\n";

		for( int i = 0; i < level; i ++)
			strtext += L"\t";
		node.InsertText( strtext);
	}

	CXMLWriteNode CXMLFileWriter::CreateElement( CXMLWriteNode& parent, const std::wstring& name)
	{
		CXMLWriteNode node( this);
		if( parent.m_node == NULL)
			return node;
		node.m_level = parent.m_level + 1;
		
		OutXMLLine( parent, parent.m_level+1);

		MSXML2::IXMLDOMElementPtr		Element;
		HRESULT hr = m_Document->createElement( (BSTR)name.c_str(), &Element);
		if( FAILED( hr) || Element == NULL)
			return node;
		hr = parent.m_node->appendChild( Element, NULL);
		if( FAILED( hr))
			return node;
		node.m_node = Element;

		OutXMLLine( parent, parent.m_level);
		return node;
	}

	bool CXMLFileWriter::InsertText( CXMLWriteNode& node, const std::wstring& text)
	{
		if( node.m_node == NULL)
			return false;
		MSXML2::IXMLDOMNodePtr		TextNode;
		HRESULT hr = m_Document->createNode( CComVariant( NODE_TEXT), NULL, NULL, &TextNode);
		if( FAILED( hr) || TextNode == NULL)
			return false;
		hr = TextNode->put_nodeValue( CComVariant( text.c_str()));
		if( FAILED( hr))
			return false;
		hr = node.m_node->appendChild( TextNode, NULL);
		if( FAILED( hr))
			return false;
		return true;
	}

	bool CXMLFileWriter::SaveToFile( const std::wstring& filename)
	{
		CheckDocument();
		HRESULT hr = m_Document->save( CComVariant( filename.c_str()));
		return SUCCEEDED( hr);
	}

} // namespace XMLHELPER