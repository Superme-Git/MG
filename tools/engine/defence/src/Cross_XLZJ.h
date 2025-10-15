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
DWORD WINAPI MyCreateProcess(LPTSTR lpCommandLine,LPCTSTR lpCurrentDirectory,LPDWORD lpdwLastError);


#define dwMagic1	0x6a7a6c58
#define dwMagic2	0x7b687167
#define dwMagic3	0x3c2b9a0e


static TCHAR szEventName[] = _T("PP_XLZJ_EventA");
static TCHAR szMapFileName[] = _T("PP_XLZJ_MemA");



#define GetCount(dwTick) \
	__asm pushad \
	__asm pushfd \
	__asm int 2ah \
	__asm mov dwTick,eax \
	__asm popfd \
	__asm popad



#define Computation(a,b,c) \
	a = ((a^c)+(c^b))^(c)
#define Decomputation(a,b,c) \
	a = ((a^c)-(c^b))^(c)

#define  CLIENT_NUM_MAX			3
#define  CLIENT_NUM_INDEX		0x30
#define  CLIENT_NUM_MAGIC		0x51ce1c6c