///=============================================================================
/// Product   : Nuclear
/// Project   : xmliolib
/// Module    : xmliocommon.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	common definitions of xmlio library
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-8-27 Created.
///=============================================================================
#pragma once

namespace XMLIO
{
	class CUncopyableObject
	{
		CUncopyableObject( const CUncopyableObject&) {}
		CUncopyableObject& operator=( const CUncopyableObject&) { return *this; }
	public:
		CUncopyableObject() {}
		virtual ~CUncopyableObject() {}
	};

	class CNoTrackData
	{
	public:
		// Implement in both released version
		void* __stdcall operator new(size_t nSize );
		void __stdcall operator delete(void* p );

		void* __stdcall operator new[](size_t nSize);
		void __stdcall operator delete[](void * p);

	public:
#ifdef _DEBUG
		void* __stdcall operator new(size_t nSize, const char* lpszFileName, int nLine);
		void __stdcall operator delete(void* p, const char*lpszFileName, int nLine);
		void* __stdcall operator new[](size_t nSize, const char* lpszFileName, int nLine);
		void __stdcall operator delete[](void* p, const char* lpszFileName, int nLine);
#endif
	};

	//
	// Error Code of xmlio library
	//
	enum
	{
		EC_SUCCESS				= 0,
		EC_CREATE_XML_DOCUMENT	= -3000,
		EC_PARSE_XML_CONTEXT,
		EC_FILE_SIZE_LACK,
		EC_GET_ROOT_ELEMENT,
		EC_XML_ENCODE,
		EC_XML_DECODE,
		EC_BAD_OCTETS_XML_DATA, // 无效的OCTETS XML数据
		EC_X2O_TRANSLATE_FAILED, // X2O变换失败
		EC_O2X_TRANSLATE_FAILED, // O2X变换失败
		EC_NOT_SUPPORT_WRITER,	// 不支持写操作
	};

	enum NodeType
	{
		NT_UNKNOW	= -1,		//BAD name
		NT_UNKNOWN = NT_UNKNOW, 
		NT_ELEMENT = 0,
		NT_TEXT,
		NT_CDATA_SECTION,
	};

	//
	// forward declarations
	//
	class CINode;
	typedef std::vector<CINode> CNodeList;
	typedef std::vector<std::wstring> CAttributesNames;

} // namespace XMLIO

#if _MSC_FULL_VER >= 140050320
#define _XMLIO_DEPRECATE_TEXT(_Text) __declspec(deprecated(_Text))
#else
#define _XMLIO_DEPRECATE_TEXT(_Text) __declspec(deprecated)
#endif

#ifndef XMLIO_PERFORMANCE_DEPRECATED_TEXT
#define XMLIO_PERFORMANCE_DEPRECATED_TEXT(_Text) _XMLIO_DEPRECATE_TEXT(_Text)
#endif

#ifdef XMLIO_PERFORMANCE_NO_WARNINGS
#define XMLIO_PERFORMANCE_DEPRECATED(_Replacement)
#else
#define XMLIO_PERFORMANCE_DEPRECATED(_Replacement) XMLIO_PERFORMANCE_DEPRECATED_TEXT("This function has performance issues or not recomended to use, and would be removed in future. Consider using " #_Replacement " instead. To disable deprecation, use XMLIO_PERFORMANCE_NO_WARNINGS. Contact XMLIO developers for details.")
#endif
