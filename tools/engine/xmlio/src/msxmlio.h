#pragma once

namespace XMLIO {
namespace MSXMLIO {

class CMSXMLINode : public CAbstractINode
{
	friend class CCMSXMLReader;

	MSXMLDOM::CReadNode		m_node;
public:
	CMSXMLINode() {}
	CMSXMLINode( const MSXMLDOM::CReadNode& src) : m_node( src) {}
	virtual ~CMSXMLINode() {}
public:
	virtual CAbstractINode* Clone() const
	{
		return new CMSXMLINode( m_node);
	}
public:
	virtual NodeType GetType() const
	{
		DOMNodeType			domnt = m_node.GetType();
		switch( domnt)
		{
		case NODE_ELEMENT:
			return NT_ELEMENT;
		case NODE_TEXT:
			return NT_TEXT;
		case NODE_CDATA_SECTION:
			return NT_CDATA_SECTION;
		default:
			return NT_UNKNOW;
		}
	}
	virtual size_t GetChildrenCount() const
	{
		return m_node.GetChildrenCount();
	}
	
	virtual bool GetChildAt( size_t index, CINode& rNode) const;

	virtual void GetAttributeNames( CAttributesNames& names) const
	{
		m_node.GetAttributeNames( names);
	}
	
	virtual bool GetAttribute( const std::wstring& name, std::wstring& value) const
	{
		return m_node.GetAttribute( name, value);
	}

	virtual void GetText( std::wstring& value) const
	{
		m_node.GetText( value);
	}

	virtual void GetSubText( std::wstring& value) const
	{
		size_t ncount = m_node.GetChildrenCount();
		for( size_t i = 0; i < ncount; i ++)
		{
			MSXMLDOM::CReadNode node;
			m_node.GetChildAt( i, node );
			DOMNodeType nt = node.GetType();
			if( nt == NODE_TEXT || nt == NODE_CDATA_SECTION )
			{
				node.GetText( value);
				return;
			}
		}
	}

	virtual void GetName( std::wstring& value) const
	{
		m_node.GetName( value);
	}
};

class CCMSXMLReader : public CAbstractReader
{
	MSXMLDOM::CReader	m_Document;
public:
	CCMSXMLReader() {}
	CCMSXMLReader( MSXMLDOM::CReader& src) : m_Document( src) {}
	virtual ~CCMSXMLReader() { Close();	}
private:
	int TranslateErrorCode( int code)
	{
		switch( code)
		{
		case MSXMLDOM::EC_MSXMLDOM_CREATE_DOCUMENT:
			return EC_CREATE_XML_DOCUMENT;
		case MSXMLDOM::EC_MSXMLDOM_PARSE_XML_CONTEXT:
			return EC_PARSE_XML_CONTEXT;
		default:
			return code;
		}
	}
public:
	virtual int OpenData( const PFS::CImageData& data)
	{
		return TranslateErrorCode( m_Document.OpenData( data.GetData(), (size_t)data.GetSize()));
	}
	virtual int OpenData( const PFS::CDataBlock& data)
	{
		return TranslateErrorCode( m_Document.OpenData( data));
	}
	virtual int OpenFile( PFS::CBaseFile& bf)
	{
		return TranslateErrorCode( m_Document.OpenFile( bf));
	}
	virtual int OpenNativeFile( const std::wstring& filename)
	{
		return TranslateErrorCode( m_Document.OpenNativeFile( filename.c_str() ));
	}
	virtual void Close()
	{
		m_Document.Close();
	}
	
	virtual bool GetRootNode(CINode& rNode) const;

public:
	CComPtr<IXMLDOMDocument> GetInterface() const
	{
		return m_Document.GetInterface();
	}

	int OpenString( const wchar_t* xml)
	{
		return m_Document.OpenString( xml);
	}

public:
	// 还原XML文本
	virtual bool EncodeToXML( std::wstring& str);

	// 转换为二进制数据块
	virtual bool EncodeToBinary( PFS::CDataBlock& data);
};

} // namespace MSXMLIO {
} // namespace XMLIO {
