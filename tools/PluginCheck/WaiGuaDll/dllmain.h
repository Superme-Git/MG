#include <wtypes.h>

#if defined(_USRDLL) && defined(WAIGUADLL_EXPORTS)
#define DLLAPI     __declspec(dllexport)
#else         /* use a DLL library */
#define DLLAPI     __declspec(dllimport)
#endif

extern "C"
{
	DLLAPI void setHook(DWORD processId);
}