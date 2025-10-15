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
DWORD WINAPI MyCreateProcess(LPTSTR lpCommandLine,LPCTSTR lpCurrentDirectory,
							 LPCTSTR lpPPDir,LPCTSTR lpTitleName,LPCTSTR lpShowText);
BOOL WINAPI MultiInstanceCheck (LPCTSTR lpszProcessImageFileName );

#define _MHZX

#ifdef _MHZX
#define dwMagic1	0x686a684d
#define dwMagic2	0x787A7459
#define dwMagic3	0x2b30c95a
#endif

static TCHAR szEventName[] = _T("PP_MHZX_EventA");
static TCHAR szMapFileName[] = _T("PP_MHZX_MemA");



#define GetCount(dwTick) \
	__asm pushad \
	__asm pushfd \
	__asm int 2ah \
	__asm mov dwTick,eax \
	__asm popfd \
	__asm popad



#define Computation(a,b,c) \
	a = ((a^b)+c)^(b)
#define Decomputation(a,b,c) \
	a = ((a^b)-c)^(b)


#define  CLIENT_NUM_MAX			5
#define  CLIENT_NUM_INDEX		0x10
#define  CLIENT_NUM_MAGIC	0x781b4aff
