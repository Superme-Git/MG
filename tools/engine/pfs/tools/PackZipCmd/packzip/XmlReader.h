//////////////////////////////////////////////////////////////////////////
//
// XmlReader.h
//
// ֧��XPath�ӿڵ�XmlReader
//
// author : yanghaibo
// Date: 2008-11-10
// Date: 2009-11-27 ����ȱʡֵ����
//
#pragma once

#include "d:\\nuclear\\pfs\\pfs.h"
#include "d:\\nuclear\\xmlio\\msdom.h"

class CXmlReader : public MSXMLDOM::CReadNode
{
public:
	explicit CXmlReader( CComPtr<IXMLDOMNode>& node )
		: MSXMLDOM::CReadNode(node)
	{
	}
	//
	// XPath ���ʽӿ�
	//
	HRESULT SelectNodes(BSTR queryString, CComPtr<IXMLDOMNodeList>& nodeList);
	HRESULT SelectSingleNode(BSTR queryString, CComPtr<IXMLDOMNode>& node );

	//
	// ���ͻ������Է��ʽӿ�
	//
	int GetAttributeInt32( const std::wstring& name, int defaultvalue = 0) const;
	__int64 GetAttributeInt64( const std::wstring& name, __int64 defaultvalue = 0) const;
	unsigned int GetAttributeUInt32( const std::wstring& name, unsigned int defaultvalue = 0) const;
	double GetAttributeDouble( const std::wstring& name, double defaultvalue = 0.0) const;
};

//
// inline ����ʵ��
//

inline HRESULT CXmlReader::SelectNodes(BSTR queryString, CComPtr<IXMLDOMNodeList>& nodeList)
{
	HRESULT hr = GetInterface()->selectNodes( queryString, &nodeList ) ;
	return hr;
}

inline HRESULT CXmlReader::SelectSingleNode(BSTR queryString, CComPtr<IXMLDOMNode>& node )
{
	HRESULT hr = GetInterface()->selectSingleNode( queryString, &node );
	return hr;
}

inline int CXmlReader::GetAttributeInt32( const std::wstring& name, int defaultvalue) const
{
	std::wstring value;
	if( GetAttribute(name, value) )
		return _wtoi( value.c_str() );
	else
		return defaultvalue;
}

inline __int64 CXmlReader::GetAttributeInt64( const std::wstring& name, __int64 defaultvalue) const
{
	std::wstring value;
	if( GetAttribute(name, value) )
		return _wtoi64( value.c_str() );
	else
		return defaultvalue;
}

inline unsigned int CXmlReader::GetAttributeUInt32( const std::wstring& name, unsigned int defaultvalue) const
{
	std::wstring value;
	if( GetAttribute(name, value) )
		return (unsigned int)_wtoi( value.c_str() );
	else
		return defaultvalue;
}

inline double CXmlReader::GetAttributeDouble( const std::wstring& name, double defaultvalue) const
{
	std::wstring value;
	if( GetAttribute(name, value) )
		return _wtof( value.c_str() );
	else
		return defaultvalue;
}
