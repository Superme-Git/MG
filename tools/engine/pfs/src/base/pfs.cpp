//////////////////////////////////////////////////////////////////////////
// 
// Only for checking the compilation
//

#define _CRT_SECURE_NO_DEPRECATE

#include <map>
#include <set>
#include <vector>
#include <sstream>
#include <algorithm>

#include <assert.h>

#ifdef _MSC_VER
#include <windows.h>
#else
#endif

#include "../../pfs.h"
#include "../../../xmlio/xmlio.h"
#include "../../mountmanager.h"
//
//#ifdef _DEBUG
//void* __cdecl operator new(size_t nSize, LPCSTR lpszFileName, int nLine)
//{
//	void* pResult;
//	pResult = _malloc_dbg(nSize, _NORMAL_BLOCK, lpszFileName, nLine);
//	return pResult;
//}
//
//void __cdecl operator delete(void * _P, const char *, int)
//{
//	::operator delete(_P);
//}
//
//#if _MSC_VER >= 1210
//void* __cdecl operator new[](size_t nSize, LPCSTR lpszFileName, int nLine)
//{
//	return ::operator new(nSize, lpszFileName, nLine);
//}
//
//void __cdecl operator delete[](void * _P, const char *, int)
//{
//	::operator delete[](_P);
//}
//#endif // _MSC_VER >= 1210
//
//#endif // _DEBUG