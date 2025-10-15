///=============================================================================
/// Product   : PFS
/// Project   : PFS
/// Module    : StringHelper.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	定义可供各个模块共享的std::wstring/c string处理函数
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-6-26 Created.
///=============================================================================
#include "StringHelper.h"
#include <algorithm>
#include <assert.h>

namespace StringHelper
{
	wstring& TrimLeft( wstring& rString, const wchar_t* pcTrimmed )
	{
		const wchar_t* pString = rString.c_str();
		size_t iIndex = wcsspn(pString, pcTrimmed);
		if( iIndex > 0 )
		{
			rString.assign( pString + iIndex );
		}
		return rString;
	}

	// 从 wcsspn 改编而来（优化版，指定了传入字符串的长度）
	static int __cdecl _wcsrspn_s (
		const wchar_t * string,
		size_t len,
		const wchar_t * control)
	{
		if( len == 0 )
		{
			return -1;
		}

		wchar_t *str = (wchar_t *) string + len - 1;
		wchar_t *ctl;

		/* 1st char not in control string stops search */
		do
		{	for (ctl = (wchar_t *)control; *ctl != *str; ctl++) {
			if (*ctl == (wchar_t)0) {
				/*
				* reached end of control string without finding a match
				*/
				return (int)(str - string);
			}
		}
		str--;
		}while (str >= string);

		/*
		* The whole string consisted of characters from control
		*/
		return (int)(str - string);
	}

	// 从 wcsspn 改编而来（字符串以\0结尾）
	static int __cdecl _wcsrspn (
		const wchar_t * string,
		const wchar_t * control)
	{
		size_t len = wcslen(string);
		return _wcsrspn_s(string, len, control);
	}

	wstring& TrimRight( wstring& rString, const wchar_t* pcTrimmed )
	{
		if( rString.empty() )
		{
			return rString;
		}

		int iIndex = _wcsrspn_s(rString.c_str(), rString.size(), pcTrimmed );
		if( iIndex < 0 )
		{
			rString.clear();
		}
		else
		{
			rString.resize(iIndex + 1);
		}

		return rString;
	}

	wstring& Trim( wstring& rString, const wchar_t* pcTrimmed )
	{
		if( rString.empty() )
		{
			return rString;
		}

		const wchar_t* pString = rString.c_str();

		// find the right end
		int iIndex = _wcsrspn_s(pString, rString.size(), pcTrimmed );
		if( iIndex < 0 )
		{
			rString.clear();
		}
		else
		{
			// find the left end
			size_t iIndex2 = wcsspn(pString, pcTrimmed);
			rString.assign( pString + iIndex2, iIndex + 1 - iIndex2 );
		}

		return rString;
	}

	wstring& TrimTo( const wchar_t* pszString, wstring& rResult, const wchar_t* pcTrimmed )
	{
		assert( pszString != NULL );

		// find the right end
		int iIndex = _wcsrspn(pszString, pcTrimmed );
		if( iIndex < 0 )
		{
			rResult.clear();
		}
		else
		{
			// find the left end
			size_t iIndex2 = wcsspn(pszString, pcTrimmed);
			rResult.assign( pszString + iIndex2, iIndex + 1 - iIndex2 );
		}

		return rResult;
	}

	wstring& TrimTo( const wstring& rString, wstring& rResult, const wchar_t* pcTrimmed )
	{
		if( rString.empty() )
		{
			rResult.clear();
			return rResult;
		}

		const wchar_t* pString = rString.c_str();

		// find the right end
		int iIndex = _wcsrspn_s(pString, rString.size(), pcTrimmed );
		if( iIndex < 0 )
		{
			rResult.clear();
		}
		else
		{
			// find the left end
			size_t iIndex2 = wcsspn(pString, pcTrimmed);
			rResult.assign( pString + iIndex2, iIndex + 1 - iIndex2 );
		}

		return rResult;
	}

	// summary: Escape an string, which may contains XML keywords.
	//			After escaped, the result string can be filled in an XML element or an attribute node.
	// returns: The escaped string. If no any characters are escaped, the result retains same as input.
	wstring& EscapeXml( wstring& rXmlString )
	{
		// 替换方案:
		//       & ---> &amp;
		//       < ---> &lt;
		//       > ---> &gt;
		//       ' ---> &apos;
		//       " ---> &quot;
		size_t nCount1 = std::count( rXmlString.begin(), rXmlString.end(), L'&');
		size_t nCount2 = std::count( rXmlString.begin(), rXmlString.end(), L'<');
		size_t nCount3 = std::count( rXmlString.begin(), rXmlString.end(), L'>');
		size_t nCount4 = std::count( rXmlString.begin(), rXmlString.end(), L'"');
		size_t nCount5 = std::count( rXmlString.begin(), rXmlString.end(), L'\'');
		size_t nLen = rXmlString.size();
		size_t nNewLength = nCount1 * 4 + nCount2 * 3 + nCount3 * 3 + nCount4 * 5 + nCount5 * 5 + nLen;
		if ( nNewLength == nLen )
		{
			// 没有需要替换的
			return rXmlString;
		}

		rXmlString.resize( nNewLength ); // 调整新字符串的长度
		wchar_t* pch = (wchar_t*)rXmlString.c_str(); // 获得字符串首地址
		int i = (int)(nLen)-1, k = (int)(nNewLength)-1;
		for ( ; i >= 0; i -- )
		{
			switch( pch[i] )
			{
			case  L'&':
				{
					pch[k-4] = L'&';
					pch[k-3] = L'a';
					pch[k-2] = L'm';
					pch[k-1] = L'p';
					pch[k] = L';';
					k-=5;
				}
				break;
			case  L'<':
				{
					pch[k-3] = L'&';
					pch[k-2] = L'l';
					pch[k-1] = L't';
					pch[k] = L';';
					k-=4;
				}
				break;
			case  L'>':
				{
					pch[k-3] = L'&';
					pch[k-2] = L'g';
					pch[k-1] = L't';
					pch[k] = L';';
					k-=4;
				}
				break;
			case  L'"':
				{
					pch[k-5] = L'&';
					pch[k-4] = L'q';
					pch[k-3] = L'u';
					pch[k-2] = L'o';
					pch[k-1] = L't';
					pch[k] = L';';
					k-=6;
				}
				break;
			case  L'\'':
				{
					pch[k-5] = L'&';
					pch[k-4] = L'a';
					pch[k-3] = L'p';
					pch[k-2] = L'o';
					pch[k-1] = L's';
					pch[k] = L';';
					k-=6;
				}
				break;
			default:
				{
					pch[k] = pch[i];
					k--;
				}
				break;
			}
		}
		assert ( i == -1 && k == -1 );
		return rXmlString;
	}

	// summary: Escape a NULL terminated string buffer, which may contains XML keywords.
	//			After escaped, the result string can be filled in an XML element or an attribute node.
	// parameters:
	//			pszXmlString[in] - The input string need to be escaped.
	//			rResult[out] - The escaped result string. If no any characters are escaped, the result would be same as input.
	// returns: The count of the escaped characters in pszXmlString. 0 indicates that no any characters are escaped.
	// note:    pszXmlString should not be NULL.
	int EscapeXml( const wchar_t* pszXmlString, wstring& rResult )
	{
		assert( pszXmlString != NULL );

		// 替换方案:
		//       & ---> &amp;
		//       < ---> &lt;
		//       > ---> &gt;
		//       ' ---> &apos;
		//       " ---> &quot;
		size_t nLen = wcslen(pszXmlString);
		if ( nLen == 0 )
		{
			rResult.clear();
			return 0;
		}

		size_t nCount1 = std::count( pszXmlString, pszXmlString+nLen, L'&');
		size_t nCount2 = std::count( pszXmlString, pszXmlString+nLen, L'<');
		size_t nCount3 = std::count( pszXmlString, pszXmlString+nLen, L'>');
		size_t nCount4 = std::count( pszXmlString, pszXmlString+nLen, L'"');
		size_t nCount5 = std::count( pszXmlString, pszXmlString+nLen, L'\'');
		size_t nEscapedCount = nCount1 + nCount2 + nCount3 + nCount4 + nCount5;
		if( nEscapedCount == 0 )
		{
			rResult.assign(pszXmlString);
			return 0;
		}
		
		size_t nNewLength = nCount1 * 4 + nCount2 * 3 + nCount3 * 3 + nCount4 * 5 + nCount5 * 5 + nLen;
		
		rResult.resize( nNewLength ); // 调整新字符串的长度
		wchar_t* pch = (wchar_t*)rResult.c_str(); // 获得字符串首地址
		int i = (int)(nLen)-1, k = (int)(nNewLength)-1;
		for ( ; i >= 0; i -- )
		{
			switch( pszXmlString[i] )
			{
			case  L'&':
				{
					pch[k-4] = L'&';
					pch[k-3] = L'a';
					pch[k-2] = L'm';
					pch[k-1] = L'p';
					pch[k] = L';';
					k-=5;
				}
				break;
			case  L'<':
				{
					pch[k-3] = L'&';
					pch[k-2] = L'l';
					pch[k-1] = L't';
					pch[k] = L';';
					k-=4;
				}
				break;
			case  L'>':
				{
					pch[k-3] = L'&';
					pch[k-2] = L'g';
					pch[k-1] = L't';
					pch[k] = L';';
					k-=4;
				}
				break;
			case  L'"':
				{
					pch[k-5] = L'&';
					pch[k-4] = L'q';
					pch[k-3] = L'u';
					pch[k-2] = L'o';
					pch[k-1] = L't';
					pch[k] = L';';
					k-=6;
				}
				break;
			case  L'\'':
				{
					pch[k-5] = L'&';
					pch[k-4] = L'a';
					pch[k-3] = L'p';
					pch[k-2] = L'o';
					pch[k-1] = L's';
					pch[k] = L';';
					k-=6;
				}
				break;
			default:
				{
					pch[k] = pszXmlString[i];
					k--;
				}
				break;
			}
		}
		assert ( i == -1 && k == -1 );
		
		return nEscapedCount;
	}

	// 偏移结构体
	struct coff
	{
		wchar_t ch; // 替换的字符
		size_t off; // 被替换的实体在原字符串中的位置

		coff()
		{
			ch = 0;
			off = 0;
		}

		coff(const coff& other)
		{
			ch = other.ch;
			off = other.off;
		}

		const coff& operator = ( const coff& other )
		{
			if (this != &other )
			{
				ch = other.ch;
				off = other.off;
			}
			return *this;
		}

		// 用于根据偏移量排序
		bool operator < ( const coff& _Right ) const
		{
			return off < _Right.off;
		}
	};

	static void _collect_replacement_offset_array( 
		const wstring& rString, std::vector<coff>& rOffsetArray )
	{
		coff _entity;
		_entity.ch = L'&';
		_entity.off = 0;
		size_t& offset = _entity.off;
		do 
		{
			offset = rString.find( L"&amp;", offset );
			if( std::wstring::npos == offset )
				break;
			rOffsetArray.push_back( _entity );
			offset+=5;
		} while(1);

		_entity.ch = L'<';
		offset = 0;
		do 
		{
			offset = rString.find( L"&lt;", offset );
			if( std::wstring::npos == offset )
				break;
			rOffsetArray.push_back( _entity );
			offset += 4;
		} while(1);

		_entity.ch = L'>';
		offset = 0;
		do 
		{
			offset = rString.find( L"&gt;", offset );
			if( std::wstring::npos == offset )
				break;
			rOffsetArray.push_back( _entity );
			offset+=4;
		} while(1);

		_entity.ch = L'"';
		offset = 0;
		do 
		{
			offset = rString.find( L"&quot;", offset );
			if( std::wstring::npos == offset )
				break;
			rOffsetArray.push_back( _entity );
			offset+=6;
		} while(1);

		_entity.ch = L'\'';
		offset = 0;
		do 
		{
			offset = rString.find( L"&apos;", offset );
			if( std::wstring::npos == offset )
				break;
			rOffsetArray.push_back( _entity );
			offset+=6;
		} while(1);
	}

	// 根据偏移数组(已排序)进行替换
	static void _do_replacement( wstring& rXmlString, const std::vector<coff>& rOffsetArray )
	{
		size_t o = 0, off_count = rOffsetArray.size(); 
		size_t p1 = rOffsetArray[0].off;
		size_t p2 = p1;
		size_t nLen = rXmlString.size();
		wchar_t* pch = (wchar_t*)rXmlString.c_str();
		for ( ; o < off_count; o ++ )
		{
			if( rOffsetArray[o].off != p2 && p1 != p2 )
			{
				size_t count = rOffsetArray[o].off - p2;
				wmemcpy(pch + p1, pch + p2, count );
				p1 += count;
				p2 += count;
			}

			switch( rOffsetArray[o].ch )
			{
			case L'&':
				{
					pch[p1] = rOffsetArray[o].ch;
					p1 ++;
					p2 += 5;
				}
				break;
			case L'<':
				{
					pch[p1] = rOffsetArray[o].ch;
					p1 ++;
					p2 += 4;
				}
				break;
			case L'>':
				{
					pch[p1] = rOffsetArray[o].ch;
					p1 ++;
					p2 += 4;
				}
				break;
			case L'"':
				{
					pch[p1] = rOffsetArray[o].ch;
					p1 ++;
					p2 += 6;
				}
				break;
			case L'\'':
				{
					pch[p1] = rOffsetArray[o].ch;
					p1 ++;
					p2 += 6;
				}
				break;
			default:
				assert(false);
			}
		}

		if( nLen > p2 )
		{
			// copy remainder
			wmemcpy( pch + p1, pch + p2, nLen - p2 );
			rXmlString.resize( p1 + nLen - p2 );
		}
		else
		{
			rXmlString.resize( p1 );
		}	
	}

	// summary: Un-escape an string, which may contains escaped XML keywords.
	//			After un-escaped, the result string may contains XML keywords and can not be filled in an XML element or an attribute node.
	// returns: The un-escaped string. If no any characters are un-escaped, the result retains same as input.
	wstring& UnescapeXml( wstring& rXmlString )
	{
		// 偏移信息数组
		std::vector<coff> offsetArray;

		// 搜集要替换的实体的偏移数组
		_collect_replacement_offset_array( rXmlString, offsetArray );

		if( offsetArray.size() == 0 )
		{
			// 没有需要替换的实体
			return rXmlString;
		}

		// 偏移数组进行排序
		std::sort( offsetArray.begin(), offsetArray.end() );

		// 执行替换
		_do_replacement( rXmlString, offsetArray );

		return rXmlString;
	}

	// summary: Un-escape a NULL terminated string buffer, which may contains escaped XML keywords.
	//			After un-escaped, the result string may contains XML keywords and can not be filled in an XML element or an attribute node.
	// parameters:
	//			pszXmlString[in] - The input string need to be un-escaped.
	//			rResult[out] - The un-escaped result string. If no any characters are un-escaped, the result would be same as input.
	// returns: The count of the un-escaped characters in the input string buffer. 0 indicates that no any characters are un-escaped.
	// note:    pszXmlString should not be NULL.
	int UnescapeXml( const wchar_t* pszXmlString, wstring& rResult )
	{
		assert( pszXmlString != NULL );

		// Copy the input to the result
		rResult.assign(pszXmlString);

		std::vector<coff> offsetArray;
		
		// 搜集要替换的实体的偏移数组
		_collect_replacement_offset_array( rResult, offsetArray );

		if( offsetArray.size() == 0 )
		{
			// 没有需要替换的实体
			return 0;
		}

		// 偏移数组进行排序
		std::sort( offsetArray.begin(), offsetArray.end() );

		// 执行替换
		_do_replacement( rResult, offsetArray );

		return (int)offsetArray.size();
	}

} // namespace StringHelper

// 测试用例
#ifdef TEST_STRING_HELPER
#include <iostream>
namespace StringHelper
{
	void TestTrimmer()
	{
		{
			wstring strInput(L"  \tabc \t def \t ");
			TrimLeft( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L"  \tabc \t def \t ");
			TrimRight( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{

			wstring strInput(L"  \tabc \t def \t ");
			Trim( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L"abc \t def \t ");
			TrimLeft( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L"  \tabc \t def");
			TrimRight( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L"abc \t def");
			Trim( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L"abc \t def \t");
			Trim( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L" abc \t def");
			Trim( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L" \t ");
			TrimLeft( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
		{
			wstring strInput(L" \t ");
			TrimRight( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
		{
			wstring strInput(L" \t ");
			Trim( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L"\t");
			TrimLeft( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
		{
			wstring strInput(L"\t");
			TrimRight( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
		{
			wstring strInput(L"\t");
			Trim( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L"");
			TrimLeft( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
		{
			wstring strInput(L"");
			TrimRight( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
		{
			wstring strInput(L"");
			Trim( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}

		{
			wstring strInput(L" ");
			TrimLeft( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
		{
			wstring strInput(L" ");
			TrimRight( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
		{
			wstring strInput(L" ");
			Trim( strInput );
			std::wcout<< strInput << L"=======" << std::endl;
		}
	}

	template< class Container> 
	void OutputStlContainer( const Container& rContainer )
	{
		std::wcout << L"{" << std::endl;
		for( Container::const_iterator itr = rContainer.begin(), itrE = rContainer.end();
			itr != itrE; ++ itr )
		{
			std::wcout << L"\t[" << *(itr) << L"]" << std::endl;
		}

		std::wcout << L"}" << std::endl;
	}

	void TestTokenizer()
	{
		//typedef wstringlist StrContainer;
		//typedef wstringvector StrContainer;
		typedef wstringdeque StrContainer;

		{
			wstring strInput( L"a;b;c");
			StrContainer resultList;
			StrTokenize( strInput, resultList, L"" );
			OutputStlContainer( resultList );
		}

		{
			wstring strInput( L"a;b;c");
			StrContainer resultList;
			StrTokenize( strInput, resultList, L"a" );
			OutputStlContainer( resultList );
		}

		{
			wstring strInput( L"a;b;c");
			StrContainer resultList;
			StrTokenize( strInput, resultList, L";" );
			OutputStlContainer( resultList );
		}

		{
			wstring strInput( L"a;b;c");
			StrContainer resultList;
			StrTokenize( strInput, resultList, L"b" );
			OutputStlContainer( resultList );
		}


		{
			wstring strInput( L"a;b;c");
			StrContainer resultList;
			StrTokenize( strInput, resultList, L"c" );
			OutputStlContainer( resultList );
		}

		{
			wstring strInput( L"a;b;c");
			StrContainer resultList;
			StrTokenize( strInput, resultList, L"d" );
			OutputStlContainer( resultList );
		}
	}


	void TestUnescapeXml()
	{
		{
			wstring strInput(L"&amp;");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"0&amp;");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"01&amp;7");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"01&amp;78");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"&amp;6");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"&amp;67");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"&amp;&amp;");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}
		{
			wstring strInput(L"&amp;&lt;");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"&lt;");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}


		{
			wstring strInput(L"&lt;&amp;");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}
		{
			wstring strInput(L"0&lt;&amp;");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}
		{
			wstring strInput(L"0&lt;1&amp;");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}
		{
			wstring strInput(L"0&lt;&amp;1");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"0&lt;1&amp;2");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}


		{
			wstring strInput(L"0&lt;1&lt;2&amp;3&lt;&lt;&lt;&lt;&lt;0000&l");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"0&lt;1&lt;2amp;3&lt;&gt;&lt;&&quot;&&&&apos;qolt;&lt;0000&l");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}

		{
			wstring strInput(L"&amp;amp;amp;&lt;lt;lt;0&lt;1&lt;2amp;3&lt;&gt;&lt;&&quot;&&&&apos;qolt;&lt;0000&l");
			wstring strOutput = UnescapeXml( strInput );
			std::wcout<< strInput << L"=======" << strOutput << std::endl;
		}
	}

} // namespace StringHelper

#endif
