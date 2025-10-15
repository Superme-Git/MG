///=============================================================================
/// Product   : PFS
/// Project   : PFS
/// Module    : StringHelper.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	定义可供各个模块共享的std::wstring/c string处理函数
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-6-26 Created.
///=============================================================================
#pragma once
#include <string>
#include <string.h>

#include <vector>
#include <list>
#include <deque>

namespace StringHelper
{
	typedef std::wstring wstring;
	typedef std::vector<wstring> wstringvector;
	typedef std::list<wstring> wstringlist;
	typedef std::deque<wstring> wstringdeque;

	// summary: Trim specified space characters of string from the left end.
	// returns: the input source string.
	// note: pcTrimmed should not be NULL.
	wstring& TrimLeft( wstring& rString, const wchar_t* pcTrimmed = L"\x20\t" );

	// summary: Trim specified space characters of string from the right end
	// returns: the input source string.
	// note: pcTrimmed should not be NULL.
	wstring& TrimRight( wstring& rString, const wchar_t* pcTrimmed = L"\x20\t" );

	// summary: Trim specified space characters of string from the two ends
	// returns: The input source string.
	// note: pcTrimmed should not be NULL.
	wstring& Trim( wstring& rString, const wchar_t* pcTrimmed = L"\x20\t" );

	// summary: Trim specified space characters of a string from the two ends, and put the result to the rResult variable
	// returns: The result string.
	// note: pcTrimmed should not be NULL.
	wstring& TrimTo( const wchar_t* pszString, wstring& rResult, const wchar_t* pcTrimmed = L"\x20\t" );
	wstring& TrimTo( const wstring& rString, wstring& rResult, const wchar_t* pcTrimmed = L"\x20\t" );
	
	// summary: Tokenize a string to the string stl container,
	//          which could be a vector/list/deque, etc.
	// returns: the input destination string container.
	// note:    pszDelimiter should not be NULL.
	template< class stringcontainer >
	stringcontainer& Tokenize( const wstring& rString, 
		stringcontainer& rContainer, const wchar_t* pszDelimiter)
	{
		wchar_t * pContext = NULL;
		wstring stringCopied = rString; // Copy the input first
		wchar_t * pToken = wcstok_s((wchar_t *)stringCopied.c_str(), pszDelimiter, &pContext);
		while ( pToken != NULL )
		{
			rContainer.push_back(wstring(pToken));
			pToken = wcstok_s(NULL, pszDelimiter, &pContext);
		}

		return rContainer;
	}

	// summary: Converts all the characters in this string to uppercase characters. 
	// returns: The uppercase of the input string
	inline wstring& MakeUpper( wstring& rString )
	{
		_wcsupr_s( (wchar_t*)rString.c_str(), rString.size()+1 );
		return rString;
	}
	
	// summary: Converts all the characters in this string to lowercase characters. 
	// returns: The loercase of the input string
	inline wstring& MakeLower( wstring& rString )
	{
		_wcslwr_s( (wchar_t*)rString.c_str(), rString.size()+1 );
		return rString;
	}

	// summary: Compares two strings (case sensitive). 
	// returns: Zero if the strings are identical,
	//          <0 if this rLeft string object is less than rRight, 
	//          or >0 if this rLeft string object is greater than rRight.
	inline int Compare( const wstring& rLeft, const wstring& rRight )
	{
		return wcscmp( rLeft.c_str(), rRight.c_str() );
	}

	// summary: Compares two strings (case insensitive). 
	// returns: Zero if the strings are identical (ignoring case), 
	//          <0 if this rLeft string object is less than rRight, (ignoring case), 
	//          or >0 if this rLeft string object is greater than rRight (ignoring case).
	inline int CompareNoCase( const wstring& rLeft, const wstring& rRight )
	{
		return _wcsicmp( rLeft.c_str(), rRight.c_str() );
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// XML Entity Reference Specification:
	//       & ---> &amp;
	//       < ---> &lt;
	//       > ---> &gt;
	//       ' ---> &apos;
	//       " ---> &quot;
	//////////////////////////////////////////////////////////////////////////

	// summary: Escape an string, which may contains XML keywords.
	//			After escaped, the result string can be filled in an XML element or an attribute node.
	// returns: The escaped string. If no any characters are escaped, the result retains same as input.
	wstring& EscapeXml( wstring& rXmlString );

	// summary: Escape a NULL terminated string buffer, which may contains XML keywords.
	//			After escaped, the result string can be filled in an XML element or an attribute node.
	// parameters:
	//			pszXmlString[in] - The input string need to be escaped.
	//			rResult[out] - The escaped result string. If no any characters are escaped, the result would be same as input.
	// returns: The count of the escaped characters in pszXmlString. 0 indicates that no any characters are escaped.
	// note:    pszXmlString should not be NULL.
	int EscapeXml( const wchar_t* pszXmlString, wstring& rResult );

	// summary: Un-escape an string, which may contains escaped XML keywords.
	//			After un-escaped, the result string may contains XML keywords and can not be filled in an XML element or an attribute node.
	// returns: The un-escaped string. If no any characters are un-escaped, the result retains same as input.
	wstring& UnescapeXml( wstring& rXmlString );

	// summary: Un-escape a NULL terminated string buffer, which may contains escaped XML keywords.
	//			After un-escaped, the result string may contains XML keywords and can not be filled in an XML element or an attribute node.
	// parameters:
	//			pszXmlString[in] - The input string need to be un-escaped.
	//			rResult[out] - The un-escaped result string. If no any characters are un-escaped, the result would be same as input.
	// returns: The count of the un-escaped characters in the input string buffer. 0 indicates that no any characters are un-escaped.
	// note:    pszXmlString should not be NULL.
	int UnescapeXml( const wchar_t* pszXmlString, wstring& rResult );

} // namespace StringHelper
