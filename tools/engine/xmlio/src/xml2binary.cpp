///=============================================================================
/// Product   : Nuclear
/// Project   : xmliolib
/// Module    : xml2binary.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	XML 到 OctetsXML 的转换
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-7-17 Created.
///=============================================================================
#include "xml2binary.h"
#include "../xmlio.h"

// same in xmlfunc.h
const unsigned int OCTETS_IO_PREFIX = 0xEFCDAB89;
const size_t INVALID_INDEX = -1;

class CXML2Binary::CMemoryOut
{
	PFS::CDataBlock& m_data;
public:
	CMemoryOut( PFS::CDataBlock& data) : m_data( data) {}
	~CMemoryOut() {}
public:
	void push_byte( unsigned char t)
	{
		m_data.Append( &t, sizeof(t));
	}
	void push_byte( unsigned short t)
	{
		m_data.Append( &t, sizeof(t));
	}
	void push_byte( unsigned int t)
	{
		m_data.Append( &t, sizeof(t));
	}
	static inline unsigned __int16 byteorder_16(unsigned __int16 x)
	{
		__asm ror x, 8
		return x;
	}

	static inline unsigned __int32 byteorder_32(unsigned __int32 x)
	{
		__asm mov eax, x
		__asm bswap eax
		__asm mov x, eax
		return x;
	}

	void PushCompactUint32(unsigned int x)
	{
		if( x < 0x80) return push_byte( (unsigned char)x);
		else if( x < 0x4000) return push_byte(  byteorder_16( x | 0x8000));
		else if( x < 0x20000000) return push_byte( byteorder_32( x| 0xc0000000));
		push_byte( (unsigned char)0xe0);
		return push_byte( byteorder_32( x));
	}
	
public:
	void PushSize( size_t size)
	{
		PushCompactUint32( (unsigned int)size);
	}
	
	// 映射NodeType
	static unsigned int MapNodeType(DOMNodeType type )
	{
		switch( type )
		{
		case NODE_ELEMENT:
			return XMLIO::NT_ELEMENT;
		case NODE_TEXT:
			return XMLIO::NT_TEXT;
		case NODE_CDATA_SECTION:
			return XMLIO::NT_CDATA_SECTION;
		default:
			return XMLIO::NT_UNKNOW;
		}
	}

	void Push( DOMNodeType type)
	{
		PushCompactUint32( MapNodeType(type) );
	}

	void Push( size_t size)
	{
		PushCompactUint32( (unsigned int)size);
	}

	void Push( BSTR bstr )
	{
		UINT nByteLen = bstr == NULL ? 2 : ::SysStringByteLen(bstr) + 2; // 字节长度，并补充2个0字节
		PushCompactUint32( nByteLen );
		m_data.Append( (void*)bstr, nByteLen );
	}
};

DOMNodeType CXML2Binary::GetNodeType( IXMLDOMNode* pNode )
{
	DOMNodeType type;
	HRESULT hr = pNode->get_nodeType( &type );
	if( FAILED( hr) )
	{
		return NODE_INVALID;
	}
	return type;
}

BSTR CXML2Binary::GetText(IXMLDOMNode* pNode)
{
	CComVariant		variant;
	DOMNodeType nt = GetNodeType(pNode);

	// 只有下面几个XML节点支持nodeValue属性
	if( nt == NODE_ATTRIBUTE ||
		nt == NODE_CDATA_SECTION ||
		nt == NODE_TEXT || 
		nt == NODE_PROCESSING_INSTRUCTION || 
		nt == NODE_COMMENT )
	{
		HRESULT hr = pNode->get_nodeValue( &variant);
		if( FAILED( hr) || VT_BSTR != variant.vt)
			return NULL;
		BSTR bstrValue = variant.bstrVal;
		variant.vt = VT_EMPTY; // Detach the bstrVal
		variant.bstrVal = NULL;// 
		return bstrValue;
	}

	return NULL;
}

void CXML2Binary::CStringMap::DumpItems( CMemoryOut& mo ) const
{
	mo.PushSize( size() );
	for( const_iterator it = begin(); it != end(); ++ it)
	{
		mo.Push( it->first );
	}
}

void CXML2Binary::CStringMap::CalcOrdinal()
{
	// 计算字典序号
	size_t size = 0;
	for( iterator it = begin(); it != end(); ++ it, size ++)
	{
		it->second = size;
	}
}

size_t CXML2Binary::CStringMap::ItemIndex( BSTR bstrKey ) const
{
	if( bstrKey == NULL )
		return INVALID_INDEX;
	const_iterator it = find( bstrKey );
	if( it == end() )
		return INVALID_INDEX;
	else
		return it->second;
}

CXML2Binary::CStringMap::~CStringMap()
{
	for( iterator it = begin(); it != end(); ++ it )
	{
		::SysFreeString( it->first );
	}
}

void CXML2Binary::OutputDictionary( CMemoryOut& mo)
{
	m_map.DumpItems( mo );
}

void CXML2Binary::OutputAttributes( IXMLDOMNode* pNode, CMemoryOut& mo)
{
	long lCount = 0;
	CComPtr<IXMLDOMElement>	spElement;
	HRESULT hr = pNode->QueryInterface( IID_IXMLDOMNode, (void**)&(spElement));
	if( FAILED( hr) || spElement == NULL)
	{
		mo.PushSize(0);
		return;
	}

	CComPtr<IXMLDOMNamedNodeMap> spAttriNodeMap;
	hr = spElement->get_attributes(&spAttriNodeMap);
	if( FAILED( hr) || spAttriNodeMap == NULL)
	{
		mo.PushSize(0);
		return;
	}

	hr = spAttriNodeMap->get_length( &lCount );
	mo.PushSize(lCount);
	if( FAILED( hr) || lCount == 0 )
	{
		return;
	}

	IXMLDOMNode * pAttriNode = NULL;
	IXMLDOMAttribute * pAttribute = NULL;
	for( long lIndex = 0; lIndex < lCount; lIndex ++ )
	{
		hr = spAttriNodeMap->get_item(lIndex, &pAttriNode);
		if( FAILED( hr) || pAttriNode == NULL )
		{
			// assert(false);
			mo.Push( INVALID_INDEX ); 
			mo.Push( INVALID_INDEX ); 
			continue;
		}

		hr = pAttriNode->QueryInterface(IID_IXMLDOMAttribute, (void**) &pAttribute);
		if( FAILED( hr) || pAttribute == NULL )
		{
			// assert(false);
			mo.Push( INVALID_INDEX ); 
			mo.Push( INVALID_INDEX ); 
			continue;
		}

		BSTR bstrName = NULL;
		hr = pAttribute->get_name( &bstrName );
		if( FAILED( hr) )
		{
			// assert(false);
			mo.Push( INVALID_INDEX ); 
			mo.Push( INVALID_INDEX ); 
			continue;
		}

		mo.Push( GetStringIndex( bstrName ) );
		::SysFreeString(bstrName);

		BSTR bstrValue = GetText( pAttriNode );
		mo.Push( GetStringIndex( bstrValue ) );
		if( bstrValue )
		{	
			::SysFreeString(bstrValue);
		}

		pAttribute->Release();
		pAttribute = NULL;
		pAttriNode->Release();
		pAttriNode = NULL;
	}

	if( pAttribute != NULL )
		pAttribute->Release();
	if( pAttriNode != NULL )
		pAttriNode->Release();
}

void CXML2Binary::OutputChildNodes( IXMLDOMNode* pNode, CMemoryOut& mo)
{
	CComPtr<IXMLDOMNodeList> spNodeList;
	HRESULT hr = pNode->get_childNodes( &spNodeList );
	if ( FAILED(hr) || spNodeList == NULL)
	{
		mo.PushSize( 0 );
		return;
	}

	long lCount = 0;
	hr = spNodeList->get_length( &lCount );
	mo.PushSize( lCount );
	if( FAILED( hr) || lCount == 0 )
	{
		return;
	}

	IXMLDOMNode* pSubNode = NULL;
	for( long lIndex = 0; lIndex < lCount; lIndex ++ )
	{
		hr = spNodeList->get_item( lIndex, &pSubNode);
		if( FAILED( hr) )
		{
			// assert(false);
			break;
		}

		OutputNode( pSubNode, mo );

		pSubNode->Release();
		pSubNode = NULL;
	}

	if ( pSubNode != NULL)
	{
		pSubNode->Release();
	}	
}

void CXML2Binary::OutputNode(IXMLDOMNode* pNode, CMemoryOut& mo)
{
	DOMNodeType type = GetNodeType(pNode);
	mo.Push( type );
	
	if( type == NODE_INVALID )
	{
		return;
	}

	BSTR bstrName ;
	HRESULT hr = pNode->get_nodeName( &bstrName );
	if( FAILED( hr) )
	{
		return;
	}

	mo.Push( GetStringIndex( bstrName ) );
	::SysFreeString(bstrName);

	BSTR bstrValue = GetText( pNode );
	if( bstrValue == NULL )
	{
		mo.Push( INVALID_INDEX );
	}
	else
	{
		mo.Push( GetStringIndex( bstrValue ) );
		::SysFreeString(bstrValue);
	}
	
	OutputAttributes(pNode, mo);

	OutputChildNodes(pNode, mo);
}

void CXML2Binary::InsertToStringMap( BSTR bstr)
{
	if( bstr != NULL )
	{
		// 为了与XML语义保持一致,这里为空串也建立字典
		CStringMap::_Pairib ib = m_map.insert( std::make_pair(bstr, 0) );
		if ( !ib.second )
		{
			// 重复的bstr导致插入失败,这里需要释放此字符串,否则会导致该字符串泄露
			::SysFreeString( bstr );
		}
	}
}

void CXML2Binary::MakeNodeAttributesStringMap( IXMLDOMNode* node)
{
	CComPtr<IXMLDOMElement>	spElement;
	HRESULT hr = node->QueryInterface( IID_IXMLDOMNode, (void**)&(spElement));
	if( FAILED( hr) || spElement == NULL)
		return;

	CComPtr<IXMLDOMNamedNodeMap> spAttriNodeMap;
	hr = spElement->get_attributes(&spAttriNodeMap);
	if( FAILED( hr) || spAttriNodeMap == NULL)
		return;

	long lCount = 0;
	hr = spAttriNodeMap->get_length( &lCount );
	if( FAILED( hr) || lCount == 0 )
		return;

	IXMLDOMNode * pAttriNode = NULL;
	IXMLDOMAttribute * pAttribute = NULL;
	for( long lIndex = 0; lIndex < lCount; lIndex ++ )
	{
		hr = spAttriNodeMap->get_item(lIndex, &pAttriNode);
		if( FAILED( hr) || pAttriNode == NULL )
		{
			break;
		}

		hr = pAttriNode->QueryInterface(IID_IXMLDOMAttribute, (void**)&pAttribute);
		if( FAILED( hr) || pAttribute == NULL )
		{
			break;
		}

		BSTR bstrName = NULL;
		hr = pAttribute->get_name( &bstrName );
		if( FAILED( hr) )
		{
			break;
		}

		if( bstrName )
		{
			InsertToStringMap( bstrName );
		}
		
		BSTR bstrValue = GetText( pAttriNode );
		if( bstrValue != NULL )
		{	
			InsertToStringMap( bstrValue );
		}
	
		pAttribute->Release();
		pAttribute = NULL;
		pAttriNode->Release();
		pAttriNode = NULL;
	}

	if( pAttribute != NULL )
		pAttribute->Release();
	if( pAttriNode != NULL )
		pAttriNode->Release();
}

void CXML2Binary::MakeNodeChildrenStringMap( IXMLDOMNode* node)
{
	CComPtr<IXMLDOMNodeList> spNodeList;
	HRESULT hr = node->get_childNodes( &spNodeList );
	if ( FAILED(hr) || spNodeList == NULL )
	{
		return;
	}

	long lCount = 0;
	hr = spNodeList->get_length( &lCount );
	if( FAILED( hr) || lCount == 0 )
		return;

	IXMLDOMNode* pNode = NULL;
	for( long lIndex = 0; lIndex < lCount; lIndex ++ )
	{
		hr = spNodeList->get_item( lIndex, &pNode);
		if( FAILED( hr) )
			break;
		MakeNodeStringMap( pNode );
		pNode->Release();
		pNode = NULL;
	}

	if ( pNode != NULL)
	{
		pNode->Release();
	}
}

void CXML2Binary::MakeNodeStringMap( IXMLDOMNode* pNode )
{
	BSTR bstrName;
	HRESULT hr = pNode->get_nodeName( &bstrName );
	if( FAILED( hr) )
	{
		return;
	}

	InsertToStringMap( bstrName );
	
	BSTR bstrText = GetText( pNode );
	if( bstrText )
	{
		InsertToStringMap( bstrText );
	}

	MakeNodeAttributesStringMap( pNode );
	MakeNodeChildrenStringMap( pNode );
}

bool CXML2Binary::Translate(
	CComPtr<IXMLDOMDocument> spDocument,
	PFS::CDataBlock& rData )
{
	// 得到文档根结点
	CComPtr<IXMLDOMElement>	spElement;
	HRESULT hr = spDocument->get_documentElement( &spElement );
	if( FAILED( hr) || spElement == NULL)
		return false;

	MakeNodeStringMap( spElement );
	MakeStringOrdinal();

	CMemoryOut mo( rData );
	mo.push_byte( OCTETS_IO_PREFIX );
	OutputDictionary( mo );
	OutputNode( spElement, mo);

	return true;
}
