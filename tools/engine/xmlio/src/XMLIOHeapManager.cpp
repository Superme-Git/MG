#include <Windows.h>
#include "XMLIOHeapManager.h"

CXMLIOHeapManager::CXMLIOHeapManager(void)
	: m_hHeap (NULL)
{
	// Initial size is 1M, and no high limit!
	Create( 1<<20, 0 );
}

CXMLIOHeapManager::~CXMLIOHeapManager(void)
{
	if ( m_hHeap != NULL )
	{
		Destroy();
		m_hHeap = NULL;
	}
}

CXMLIOHeapManager& CXMLIOHeapManager::GetInstance()
{
	static CXMLIOHeapManager s_XMLIOHeapManagerInstance;
	return s_XMLIOHeapManagerInstance;
}

BOOL CXMLIOHeapManager::Create(
							   SIZE_T dwInitialSize,
							   SIZE_T dwMaximumSize )
{
	if ( m_hHeap != NULL )
	{
		// Already created, please Destroy first!
		return FALSE;
	}

	DWORD flOptions = HEAP_GENERATE_EXCEPTIONS;
	HANDLE hHeap = HeapCreate( flOptions, dwInitialSize, dwMaximumSize );
	if ( hHeap == NULL )
	{
		return FALSE;
	}

	m_hHeap = hHeap;

	return TRUE;
}

BOOL CXMLIOHeapManager::Destroy()
{
	return HeapDestroy( m_hHeap );
}

LPVOID CXMLIOHeapManager::Alloc(SIZE_T dwBytes)
{
	return HeapAlloc( m_hHeap, 0, dwBytes );
}

BOOL CXMLIOHeapManager::Free(LPVOID lpMem)
{
	return HeapFree( m_hHeap, 0, lpMem );
}

SIZE_T CXMLIOHeapManager::Size(LPCVOID lpMem)
{
	return HeapSize( m_hHeap, 0, lpMem );
}