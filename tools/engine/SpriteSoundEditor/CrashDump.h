#pragma once
//namespace CrashDump{
//bool CrashDump_Init(TCHAR* DumpFileName);
//void CrashDump_Clean(void);
//}
//
//#ifndef __out
//#define   __out /**/ 
//#define   __in /**/ 
//#define   __inout   /**/ 
//
//#define   __in_opt /**/ 
//#define   __out_opt /**/ 
//#define   __inout_opt   /**/ 
//
//#define   __in_ecount(THING)   /**/ 
//#define   __in_ecount_opt(THING)   /**/ 
//#define   __out_ecount(THING)   /**/ 
//#define   __out_ecount_opt(THING)   /**/ 
//#define   __inout_ecount(THING)   /**/ 
//#define   __inout_ecount_opt(THING)   /**/ 
//
//#define   __in_bcount_opt(THING)   /**/ 
//#define   __out_bcount_opt(THING)   /**/ 
//#define   __inout_bcount_opt(THING)   /**/ 
//#endif
//
//typedef LONG (NTAPI *PVECTORED_EXCEPTION_HANDLER)(
//    struct _EXCEPTION_POINTERS *ExceptionInfo
//    );
//
//#ifdef __cplusplus
//extern "C"{
//#endif
//
//WINBASEAPI
//__out
//PVOID
//WINAPI
//AddVectoredExceptionHandler (
//    __in ULONG First,
//    __in PVECTORED_EXCEPTION_HANDLER Handler
//    );
//
//WINBASEAPI
//ULONG
//WINAPI
//RemoveVectoredExceptionHandler (
//    __in PVOID Handle
//    );
//#ifdef __cplusplus
//}

namespace CrashDump {

	enum eDumpLevel
	{
		DL_1 = 1, // full dump
		DL_2,
		DL_3,
		DL_4,	 // min dump
	};

	bool CrashDump_Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName, eDumpLevel dlevel);
	void CrashDump_Clean();

} // namespace CrashDump {


