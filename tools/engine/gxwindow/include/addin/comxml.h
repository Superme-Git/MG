#pragma once

#include <windows.h>
#include <string>
#include <assert.h>
#include "../gxwindow.h"

#import "msxml.dll"
#include <atlbase.h>
#include <sstream>

namespace GXWADDIN {

namespace INLINECLASS {

class CComInitScope
{
public:
	CComInitScope() { CoInitialize( NULL); }
	~CComInitScope() { CoUninitialize(); }
};

inline std::wstring ColorToSting( GXWINDOW::COLOR clr)
{
	std::wstringstream	wss;

	wss << GXWINDOW::GetColorRed( clr) << L"," << GXWINDOW::GetColorGreen( clr) << L"," << GXWINDOW::GetColorBlue( clr) << L"," << GXWINDOW::GetColorAlpha( clr);
	return wss.str();
}

inline GXWINDOW::COLOR StringToColor( wchar_t* color)
{
	if( NULL == color)
		return 0;
	if( 0 == color[0])
		return 0;

	int r = 0, g = 0, b = 0, a = 255;
	wchar_t* szFind  =  wcschr( color, L',');
	if( szFind)
		szFind[0] = 0;
	r = _wtoi( color);
	if( NULL == szFind)
		return GXWINDOW::MakeColor( r, g, b, a);
	color = szFind + 1;
	szFind  =  wcschr( color, L',');
	if( szFind)
		szFind[0] = 0;
	g = _wtoi( color);
	if( NULL == szFind)
		return GXWINDOW::MakeColor( r, g, b, a);
	color = szFind + 1;
	szFind  =  wcschr( color, L',');
	if( szFind)
	{
		szFind[0] = 0;
		b = _wtoi( color);
		color = szFind + 1;
		a = _wtoi( color);
	}
	else
	{
		b = _wtoi( color);
	}
	return GXWINDOW::MakeColor( r, g, b, a);
}

class CDomElement
{
	CComPtr<IXMLDOMElement>& m_Ele;
public:
	CDomElement( CComPtr<IXMLDOMElement>& Ele) : m_Ele( Ele) {	}
	~CDomElement()	{ }
public:
	bool ReadVariant( const wchar_t* name1, const wchar_t* name2, CComVariant& variant)
	{
		HRESULT hr = m_Ele->getAttribute( (BSTR)name1, &variant);
		if( S_OK != hr)
		{
			hr = m_Ele->getAttribute( (BSTR)name2, &variant);
			if( S_OK != hr)
				return false;
		}
		return true;
	}
	bool ReadString( const wchar_t* name1, const wchar_t* name2, CComBSTR& value)
	{
		CComVariant		variant;
		if( !ReadVariant( name1, name2, variant))
			return false;
		if( VT_BSTR != variant.vt)
			return false;
		value = variant.bstrVal;
		return true;
	}
	int ReadInteger( const wchar_t* name1, const wchar_t* name2, int nDefault = 0)
	{
		CComVariant		variant;
		if( !ReadVariant( name1, name2, variant))
			return nDefault;
		if( VT_BSTR != variant.vt)
			return nDefault;
		return _wtoi( variant.bstrVal);
	}
	GXWINDOW::COLOR ReadColor( const wchar_t* name1, const wchar_t* name2, GXWINDOW::COLOR nDefault = 0)
	{
		CComVariant		variant;
		if( !ReadVariant( name1, name2, variant))
			return nDefault;
		if( VT_BSTR != variant.vt)
			return nDefault;
		return StringToColor( variant.bstrVal);
	}
	std::wstring GetText()
	{
		std::wstring				strResult;
		CComPtr<IXMLDOMNodeList>	NodeList;
		HRESULT hr = m_Ele->get_childNodes( &NodeList);
		if( FAILED( hr))
			return strResult;
		if( NodeList == NULL)
			return strResult;

		long		lCount;
		hr = NodeList->get_length( &lCount);
		if( FAILED( hr))
			return strResult;
		for( long i = 0; i < lCount; i ++)
		{
			CComPtr<IXMLDOMNode>		DomNode;

			hr = NodeList->get_item( i, &DomNode);
			if( FAILED( hr) || DomNode == NULL)
				continue;
			DOMNodeType		nt;

			hr = DomNode->get_nodeType( &nt);
			if( FAILED( hr))
				continue;
			if( NODE_TEXT != nt)
				continue;

			CComBSTR	bstrName;
			hr = DomNode->get_text( &bstrName);
			strResult.assign( bstrName, bstrName.Length());
			return strResult;
		}
		return strResult;
	}
	std::wstring GetTipInfoText( const wchar_t* name1 = L"P", const wchar_t* name2 = L"p")
	{
		std::wstring				strResult;
		CComPtr<IXMLDOMNodeList>	NodeList;
		HRESULT hr = m_Ele->get_childNodes( &NodeList);
		if( FAILED( hr))
			return strResult;
		if( NodeList == NULL)
			return strResult;

		long		lCount;
		hr = NodeList->get_length( &lCount);
		if( FAILED( hr))
			return strResult;
		for( long i = 0; i < lCount; i ++)
		{
			CComPtr<IXMLDOMNode>		DomNode;

			hr = NodeList->get_item( i, &DomNode);
			if( FAILED( hr) || DomNode == NULL)
				continue;
			DOMNodeType		nt;

			hr = DomNode->get_nodeType( &nt);
			if( FAILED( hr))
				continue;
			if( NODE_ELEMENT != nt)
				continue;

			CComBSTR		str;

			CComPtr<IXMLDOMElement>		Ele;
			hr = DomNode->QueryInterface( __uuidof(IXMLDOMElement), (void**)&Ele);
			if( FAILED( hr) || Ele == NULL)
				continue;

			HRESULT	hr = Ele->get_tagName( &str);
			if( FAILED( hr))
				continue;
			if( _wcsicmp( str, name1))
				continue;
			if( _wcsicmp( str, name2))
				continue;
			DomNode->get_xml( &str);
			strResult.assign( str, str.Length());
			return strResult;
	}
		return strResult;
	}
};

} // namespace INLINECLASS {
} // namespace GXWADDIN {
