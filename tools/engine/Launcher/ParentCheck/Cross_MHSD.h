#include "VMProtectSDK.h"

struct _CHECK_LOADER
{
	DWORD dwSec;
	DWORD dwPid;
};
typedef _CHECK_LOADER CHECK_LOADER;
BYTE WINAPI CheckSum(PBYTE bArray,DWORD dwArrSize);
BOOL WINAPI _DecryptionMem(PVOID pBuff,CHECK_LOADER *pCheck);
DWORD WINAPI GetRandom();
VOID WINAPI CreateProcess_Encrypt(PBYTE pbuff);
DWORD WINAPI MyCreateProcess(LPTSTR lpCommandLine,LPCTSTR lpCurrentDirectory,LPCTSTR lpPPDir);


#define dwMagic1	0xb74c678e
#define dwMagic2	0x2a836c9b
#define dwMagic3	0x49bc3d6a


static TCHAR szEventName[] = _T("PP_MHSD_EventA");
static TCHAR szMapFileName[] = _T("PP_MHSD_MemA");



#define GetCount(dwTick) \
	__asm pushad \
	__asm pushfd \
	__asm int 2ah \
	__asm mov dwTick,eax \
	__asm popfd \
	__asm popad



#define Computation(a,b,c) \
	a = ((a^b)-(c^b))^(c+b)
#define Decomputation(a,b,c) \
	a = ((a^(c+b))+(c^b))^(b)

#define  CLIENT_NUM_MAX			3
#define  CLIENT_NUM_INDEX		0x50
#define  CLIENT_NUM_MAGIC		0x3f226f14