#pragma once

#ifdef _MSC_VER
#if defined(_DEBUG) 
void* __cdecl operator new(size_t nSize, const char* lpszFileName, int nLine);
void __cdecl operator delete(void* p, const char*lpszFileName, int nLine);
void * __cdecl operator new[](size_t);
#if _MSC_VER >= 1210
void* __cdecl operator new[](size_t nSize, const char* lpszFileName, int nLine);
void __cdecl operator delete[](void* p, const char* lpszFileName, int nLine);
void __cdecl operator delete[](void *);
#endif // _MSC_VER
#endif // _DEBUG

#include "ljfmext.h"

#include <sstream>

#include <algorithm>
#include <Shlwapi.h>
#include "ljfmfs.h"
#include "platform/platform_types.h"

#ifdef _DEBUG
#define _LJFM_ERROR(msg)	_ASSERT_EXPR(false, msg)
#else
#define _LJFM_ERROR(msg) LJFM::LJFMOpen::GetOutLogInstance()->Print( ((msg)+std::wstring(L"\n")).c_str() );
#endif

namespace LJFM
{
	inline bool CheckDirFileStringFormat( const std::wstring& dir)
	{
		if( dir.empty() || L'/' != dir[0] || 1 == dir.size() || L'/' == dir[dir.size() - 1])
		{
			LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
			return false;
		}
		else if( std::find_if( dir.begin(), dir.end(), ::iswupper) != dir.end())
		{
			LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
			return false;
		}
		
		if( std::find( dir.begin(), dir.end(), L'\\') != dir.end())
		{
			LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
			return false;
		}

		return true;
	}

	inline bool CheckDirFileStringFormatEx( const std::wstring& dir, std::wstring& dirNew )
	{
		if( dir.empty() || L'/' != dir[0] || 1 == dir.size() || L'/' == dir[dir.size() - 1])
		{
			LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", dir.c_str());
			return false;
		}

		dirNew.resize(dir.size());
#ifndef NoPack
		std::transform( dir.begin(), dir.end(), dirNew.begin(), ::towlower);
#else
		dirNew = dir;
#endif
		return true;
	}

	inline bool CheckDeviceStringFormat( const std::wstring& device)
	{
		if( device.empty() || L'.' == device[0] )
		{
			LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", device.c_str());
			return false;
		}
		
		if ( device[0] == L'/' )
		{
			if( std::find( device.begin(), device.end(), L'\\') != device.end())
			{
				LJFMOpen::GetOutLogInstance()->Print( __FUNCTIONW__ L" error : %s\n", device.c_str());
				return false;
			}
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
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		CMutex() {  InitializeCriticalSectionEx( &m_cs, 0, 0); }
		~CMutex() { DeleteCriticalSection( &m_cs); }
	public:
		void Lock() { EnterCriticalSection( &m_cs); }
		void Unlock() { LeaveCriticalSection( &m_cs); }
#else
		CMutex() {  InitializeCriticalSection( &m_cs); }
		~CMutex() { DeleteCriticalSection( &m_cs); }
	public:
		void Lock() { EnterCriticalSection( &m_cs); }
		void Unlock() { LeaveCriticalSection( &m_cs); }
#endif
	};

	template <bool x> struct STATIC_ASSERTION_FAILURE;
	template <> struct STATIC_ASSERTION_FAILURE<true>{};
	template<int x> struct static_assert_test{};
#define LJFM_STATIC_ASSERT(B) \
	typedef static_assert_test<sizeof(STATIC_ASSERTION_FAILURE<B>) > \
	ljfm_static_assert_typedef_ ## __LINE__
}
    
    
#elif defined(_OS_IOS)
#include <string>
#include <pthread.h>
#include <assert.h>
#include <sstream>
#include <algorithm>

#include "platform/platform_types.h"

#include "ljfmext.h"
#include "ljfmfs.h"
#include "ljfmopen.h"

#ifdef _OS_ANDROID
	#include "util_android.h" 
#endif
    
#ifdef _DEBUG
#define _LJFM_ERROR(msg) LJFM::LJFMOpen::GetOutLogInstance()->Print( ((msg)+std::wstring(L"\n")).c_str() );
#else
#define _LJFM_ERROR(msg) LJFM::LJFMOpen::GetOutLogInstance()->Print( ((msg)+std::wstring(L"\n")).c_str() );
#endif
    
    
#define MAX_PATH 256    
    
namespace LJFM
{
        
#define __FUNCTIONW__
        
        bool CheckDirFileStringFormat( const std::wstring& dir);
        
		bool CheckDirFileStringFormatEx( const std::wstring& dir, std::wstring& dirNew );
        
        bool CheckDeviceStringFormat( const std::wstring& device);
        
        
        class CMutex
        {
            pthread_mutex_t mutex;
            
            CMutex(const CMutex&);
            void operator=(const CMutex&);
        public:
            CMutex();
            virtual ~CMutex();
            
            void Lock();
            void UNLock();
            
            class CScoped
            {
                CMutex & mm;
                CScoped(const CScoped&);
                void operator=(const CScoped&);
            public:
                explicit CScoped(CMutex& m) : mm(m) { mm.Lock(); }
                explicit CScoped(CMutex* m) : mm(*m) { mm.Lock(); }
                ~CScoped() { mm.UNLock(); }
            };
        };        

        template <bool x> struct STATIC_ASSERTION_FAILURE;
        template <> struct STATIC_ASSERTION_FAILURE<true>{};
        template<int x> struct static_assert_test{};
#define LJFM_STATIC_ASSERT(B) \
       typedef static_assert_test<sizeof(STATIC_ASSERTION_FAILURE<B>) > \
        ljfm_static_assert_typedef_ ## __LINE__

}
#endif

