#pragma once

#include <windows.h>
#include <string>
#include <assert.h>
#include "../../gxwindow.h"

#include <sstream>
#include <stdio.h>
#include <vector>
#include <map>
#include "../../../../pfs/pfs.h"
#include "../../../../xmlio/xmlio.h"

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

class CElement
{
	XMLIO::CINode& m_Ele;
public:
	CElement( XMLIO::CINode& Ele) : m_Ele( Ele) {	}
	~CElement()	{ }
public:
	bool ReadString( const wchar_t* name1, const wchar_t* name2, std::wstring& value)
	{
		std::wstring _value;
		_value = m_Ele.GetAttribute( name1);
		if ( _value.empty())
		{
			_value = m_Ele.GetAttribute( name2);
			if ( _value.empty())
				return false;
		}
		value = _value;

		return true;
	}
	int ReadInteger( const wchar_t* name1, const wchar_t* name2, int nDefault = 0)
	{
		int value = m_Ele.GetAttributeInteger( name1, nDefault);
		if ( value == nDefault)
			value = m_Ele.GetAttributeInteger( name2, nDefault);
		return value;
	}
	GXWINDOW::COLOR ReadColor( const wchar_t* name1, const wchar_t* name2, GXWINDOW::COLOR nDefault = 0)
	{
		std::wstring color;
		if ( ReadString( name1, name2, color))
		{
			wchar_t buf[64];
			swprintf_s( buf, L"%s", color.c_str());
			return StringToColor( buf);
		}
		else
			return nDefault;
	}
	std::wstring GetText()
	{
		std::wstring strResult;
		XMLIO::CNodeList NodeList = m_Ele.GetChildren();
		if ( NodeList.empty())
			return strResult;

		for ( XMLIO::CNodeList::iterator it = NodeList.begin(); it != NodeList.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			int nt = node.GetType();
			if ( XMLIO::NT_TEXT != node.GetType())
				continue;

			strResult = node.GetText();
			return strResult;
		}

		return strResult;
	}
	std::wstring GetTipInfoText( const wchar_t* name1 = L"P", const wchar_t* name2 = L"p")
	{
		std::wstring strResult;
		XMLIO::CNodeList NodeList = m_Ele.GetChildren();
		
		for ( XMLIO::CNodeList::iterator it = NodeList.begin(); it != NodeList.end(); ++ it)
		{
			XMLIO::CINode& node = *it;
			if ( XMLIO::NT_ELEMENT != node.GetType())
				continue;

			std::wstring str = node.GetName();
			if ( _wcsicmp( str.c_str(), name1))
				continue;
			if ( _wcsicmp( str.c_str(), name2))
				continue;

			if ( node.GetChildrenCount() != 1)
				continue;

			const XMLIO::CINode& textnode = node.GetChildAt( 0);
			wchar_t buf[256];
			swprintf_s( buf, L"<%s>%s</%s>", node.GetName().c_str(), textnode.GetText().c_str(), node.GetName().c_str());
			strResult.assign( buf);
			return strResult;
		}

		return strResult;
	}
};

} // namespace INLINECLASS {
} // namespace GXWADDIN {
