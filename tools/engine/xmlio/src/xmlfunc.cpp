#include <vector>
#include "../xmliocommon.h"
#include <Windows.h>
#include "XMLIOHeapManager.h"

namespace XMLIO
{
	// Implement in both released version(Debug/Release)
	void* __stdcall CNoTrackData::operator new(size_t nSize )
	{
		return CXMLIOHeapManager::GetInstance().Alloc( nSize );
	}

	void __stdcall CNoTrackData::operator delete(void* p )
	{
		CXMLIOHeapManager::GetInstance().Free( p );
	}

	void* __stdcall CNoTrackData::operator new[](size_t nSize)
	{
		return CXMLIOHeapManager::GetInstance().Alloc( nSize );
	}

	void __stdcall CNoTrackData::operator delete[](void * p)
	{
		CXMLIOHeapManager::GetInstance().Free( p );
	}


#ifdef _DEBUG
	void* __stdcall CNoTrackData::operator new(size_t nSize, const char* /*lpszFileName*/, int /*nLine*/)
	{
		return CXMLIOHeapManager::GetInstance().Alloc( nSize );
	}

	void __stdcall CNoTrackData::operator delete(void* p, const char* /*lpszFileName*/, int /*nLine*/)
	{
		CXMLIOHeapManager::GetInstance().Free( p );
	}

	void* __stdcall CNoTrackData::operator new[](size_t nSize, const char* /*lpszFileName*/, int /*nLine*/)
	{
		return CXMLIOHeapManager::GetInstance().Alloc( nSize );
	}

	void __stdcall CNoTrackData::operator delete[](void* p, const char* /*lpszFileName*/, int /*nLine*/)
	{
		CXMLIOHeapManager::GetInstance().Free( p );
	}
#endif

} // namespace XMLIO
