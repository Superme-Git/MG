#pragma once

#if defined(_DEBUG) 
// Memory tracking allocation - Only for debugging version
// Implemented in pfs.cpp
void* __cdecl operator new(size_t nSize, const char* lpszFileName, int nLine);
void __cdecl operator delete(void* p, const char*lpszFileName, int nLine);
void * __cdecl operator new[](size_t);
#if _MSC_VER >= 1210
void* __cdecl operator new[](size_t nSize, const char* lpszFileName, int nLine);
void __cdecl operator delete[](void* p, const char* lpszFileName, int nLine);
void __cdecl operator delete[](void *);
#endif // _MSC_VER
#endif // _DEBUG

#include "../pfs2.h"

#include <sstream>

//////////////////////////////////////////////////////////////////////////
//
// 下面的头文件不应该在pfslib和其辅助工具外使用
//
#include "../filemarshal.h"
#include "utility/md5hash.h"
#include "native/rawfileio.h"
#include "native/tempfile.h"
#include "meta/metafileio.h"
#include "zip/ziptempfile.h"

#ifdef _DEBUG
#define _PFS_ERROR(msg)	_ASSERT_EXPR(false, msg)
#else
#define _PFS_ERROR(msg) PFS::CEnv::GetOutLogInstance()->Print( ((msg)+std::wstring(L"\n")).c_str() );
#endif

namespace PFS
{
	inline bool CheckDirFileStringFormat( const std::wstring& dir)
	{
		int iSize = (int)dir.size();
		if(dir.empty() || L'/' != dir[0] || 1 == dir.size()  || L'/' == dir[dir.size() - 1])
		{
			return false;
		}else if( std::find_if( dir.begin(), dir.end(), ::iswupper) != dir.end())
		{
			CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
			return false;
		}
		
		if( std::find( dir.begin(), dir.end(), L'\\') != dir.end())
		{
			CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
			return false;
		}

		return true;
	}

	inline bool CheckDirFileStringFormatEx( const std::wstring& dir, std::wstring& dirNew )
	{
		if( dir.empty() || L'/' != dir[0] || 1 == dir.size() || L'/' == dir[dir.size() - 1])
		{
			CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
			return false;
		}

		dirNew.resize(dir.size());
		std::transform( dir.begin(), dir.end(), dirNew.begin(), ::towlower);

		return true;
	}

	inline bool CheckDeviceStringFormat( const std::wstring& device)
	{
		if( device.empty() || L'.' == device[0] )
		{
			CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", device.c_str());
			return false;
		}
		
		if ( device[0] == L'/' ) // PFS path
		{
			if( std::find( device.begin(), device.end(), L'\\') != device.end())
			{
				CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", device.c_str());
				return false;
			}

			// 针对DeviceName暂时不做如下限制:
			//if( std::find_if( device.begin(), device.end(), ::iswupper) != device.end())
			//{
			//	CEnv::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", device.c_str());
			//	return false;
			//}
		}
		
		return true;
	}

	class CMutex
	{
		CRITICAL_SECTION		m_cs;
	public:
		class CScoped
		{
			CMutex&	m_mutex;
		public:
			CScoped( CMutex& m) : m_mutex( m) { m_mutex.Lock(); }
			~CScoped() { m_mutex.Unlock(); }
		};
	public:
		CMutex() { InitializeCriticalSection( &m_cs); }
		~CMutex() { DeleteCriticalSection( &m_cs); }
	public:
		void Lock() { EnterCriticalSection( &m_cs); }
		void Unlock() { LeaveCriticalSection( &m_cs); }
	};


	//////////////////////////////////////////////////////////////////////////
	//
	// PFS_STATIC_ASSERT - 源于boost
	//
	// __LINE__ macro broken when -ZI is used see Q199057
	//
	template <bool x> struct STATIC_ASSERTION_FAILURE;
	template <> struct STATIC_ASSERTION_FAILURE<true>{};
	template<int x> struct static_assert_test{};
#define PFS_STATIC_ASSERT(B) \
	typedef static_assert_test<sizeof(STATIC_ASSERTION_FAILURE<B>) > \
	pfs_static_assert_typedef_ ## __LINE__
}

