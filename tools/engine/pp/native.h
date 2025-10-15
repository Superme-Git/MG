#pragma once

#ifndef __out
#define   __out /**/ 
#define   __in /**/ 
#define   __inout   /**/ 

#define   __in_opt /**/ 
#define   __out_opt /**/ 
#define   __inout_opt   /**/ 

#define   __in_ecount(THING)   /**/ 
#define   __in_ecount_opt(THING)   /**/ 
#define   __out_ecount(THING)   /**/ 
#define   __out_ecount_opt(THING)   /**/ 
#define   __inout_ecount(THING)   /**/ 
#define   __inout_ecount_opt(THING)   /**/ 

#define   __in_bcount_opt(THING)   /**/ 
#define   __out_bcount_opt(THING)   /**/ 
#define   __inout_bcount_opt(THING)   /**/ 
#endif


typedef unsigned long ULONG_PTR, *PULONG_PTR;
typedef   ULONG_PTR   DWORD_PTR,   *PDWORD_PTR;   

#ifdef __cplusplus
extern "C"{
#endif

typedef LONG NTSTATUS;
/*lint -save -e624 */  // Don't complain about different typedefs.
typedef NTSTATUS *PNTSTATUS;

typedef CHAR *PSZ;
typedef CONST char *PCSZ;

typedef ULONG ACCESS_MASK;
typedef ACCESS_MASK *PACCESS_MASK;

typedef struct _STRING {
    USHORT Length;
    USHORT MaximumLength;
#ifdef MIDL_PASS
    [size_is(MaximumLength), length_is(Length) ]
#endif // MIDL_PASS
    PCHAR Buffer;
} STRING;
typedef STRING *PSTRING;

typedef STRING ANSI_STRING;
typedef PSTRING PANSI_STRING;
typedef STRING CANSI_STRING;
typedef PSTRING PCANSI_STRING;


typedef struct _UNICODE_STRING {
    USHORT Length;
    USHORT MaximumLength;
#ifdef MIDL_PASS
    [size_is(MaximumLength / 2), length_is((Length) / 2) ] USHORT * Buffer;
#else // MIDL_PASS
    PWSTR  Buffer;
#endif // MIDL_PASS
} UNICODE_STRING;
typedef UNICODE_STRING *PUNICODE_STRING;
typedef const UNICODE_STRING *PCUNICODE_STRING;

typedef struct _OBJECT_ATTRIBUTES {
    ULONG Length;
    HANDLE RootDirectory;
    PUNICODE_STRING ObjectName;
    ULONG Attributes;
    PVOID SecurityDescriptor;        // Points to type SECURITY_DESCRIPTOR
    PVOID SecurityQualityOfService;  // Points to type SECURITY_QUALITY_OF_SERVICE
} OBJECT_ATTRIBUTES;
typedef OBJECT_ATTRIBUTES *POBJECT_ATTRIBUTES;
typedef CONST OBJECT_ATTRIBUTES *PCOBJECT_ATTRIBUTES;

#define OBJ_INHERIT             0x00000002L
#define OBJ_PERMANENT           0x00000010L
#define OBJ_EXCLUSIVE           0x00000020L
#define OBJ_CASE_INSENSITIVE    0x00000040L
#define OBJ_OPENIF              0x00000080L
#define OBJ_OPENLINK            0x00000100L
#define OBJ_KERNEL_HANDLE       0x00000200L
#define OBJ_FORCE_ACCESS_CHECK  0x00000400L
#define OBJ_VALID_ATTRIBUTES    0x000007F2L


typedef enum _SECTION_INHERIT {
    ViewShare = 1,
    ViewUnmap = 2
} SECTION_INHERIT;

typedef LONG KPRIORITY;
typedef struct _PEB *PPEB;

typedef struct _PROCESS_BASIC_INFORMATION {
    NTSTATUS ExitStatus;
    DWORD PebBaseAddress;
    ULONG_PTR AffinityMask;
    KPRIORITY BasePriority;
    DWORD UniqueProcessId;
    DWORD InheritedFromUniqueProcessId;
} PROCESS_BASIC_INFORMATION;
typedef PROCESS_BASIC_INFORMATION *PPROCESS_BASIC_INFORMATION;

typedef struct _LDR_DATA_TABLE_ENTRY
{
     LIST_ENTRY InLoadOrderLinks;
     LIST_ENTRY InMemoryOrderLinks;
     LIST_ENTRY InInitializationOrderLinks;
     PVOID DllBase;
     PVOID EntryPoint;
     ULONG SizeOfImage;
     UNICODE_STRING FullDllName;
     UNICODE_STRING BaseDllName;
     ULONG Flags;
     WORD LoadCount;
     WORD TlsIndex;
     union
     {
          LIST_ENTRY HashLinks;
          struct
          {
               PVOID SectionPointer;
               ULONG CheckSum;
          };
     };
     union
     {
          ULONG TimeDateStamp;
          PVOID LoadedImports;
     };
     DWORD EntryPointActivationContext;
     PVOID PatchInformation;
     LIST_ENTRY ForwarderLinks;
     LIST_ENTRY ServiceTagLinks;
     LIST_ENTRY StaticLinks;
} LDR_DATA_TABLE_ENTRY, *PLDR_DATA_TABLE_ENTRY;

ULONG
_cdecl
DbgPrint(
		 PCHAR Format,
		 ...
		 );

NTSYSAPI
VOID
NTAPI
RtlInitAnsiString(
    PANSI_STRING DestinationString,
    PCSZ SourceString
    );

NTSYSAPI
VOID
NTAPI
RtlInitUnicodeString(
					 IN OUT PUNICODE_STRING  DestinationString,
					 IN PCWSTR  SourceString
					 );

NTSYSAPI
NTSTATUS
NTAPI
RtlAnsiStringToUnicodeString(
    PUNICODE_STRING DestinationString,
    PCANSI_STRING SourceString,
    BOOLEAN AllocateDestinationString
    );

NTSYSAPI
VOID
NTAPI
RtlFreeUnicodeString(
    PUNICODE_STRING UnicodeString
    );

NTSYSAPI
NTSTATUS
NTAPI
ZwCreateSection (
    __out PHANDLE SectionHandle,
    __in ACCESS_MASK DesiredAccess,
    __in_opt POBJECT_ATTRIBUTES ObjectAttributes,
    __in_opt PLARGE_INTEGER MaximumSize,
    __in ULONG SectionPageProtection,
    __in ULONG AllocationAttributes,
    __in_opt HANDLE FileHandle
    );

NTSYSAPI
NTSTATUS
NTAPI
ZwOpenSection(
			  OUT PHANDLE SectionHandle,
			  IN ACCESS_MASK DesiredAccess,
			  IN POBJECT_ATTRIBUTES ObjectAttributes
			  );

NTSYSAPI
NTSTATUS
NTAPI
ZwMapViewOfSection(
    __in HANDLE SectionHandle,
    __in HANDLE ProcessHandle,
    __inout PVOID *BaseAddress,
    __in ULONG_PTR ZeroBits,
    __in SIZE_T CommitSize,
    __inout_opt PLARGE_INTEGER SectionOffset,
    __inout PSIZE_T ViewSize,
    __in SECTION_INHERIT InheritDisposition,
    __in ULONG AllocationType,
    __in ULONG Win32Protect
    );

NTSYSAPI
NTSTATUS
NTAPI
ZwUnmapViewOfSection(
    __in HANDLE ProcessHandle,
    __in PVOID BaseAddress
    );

NTSYSAPI
NTSTATUS
NTAPI
ZwClose(
    __in HANDLE Handle
    );

typedef enum _PROCESSINFOCLASS {
ProcessBasicInformation, // 0 Y N
ProcessQuotaLimits, // 1 Y Y
ProcessIoCounters, // 2 Y N
ProcessVmCounters, // 3 Y N
ProcessTimes, // 4 Y N
ProcessBasePriority, // 5 N Y
ProcessRaisePriority, // 6 N Y
ProcessDebugPort, // 7 Y Y
ProcessExceptionPort, // 8 N Y
ProcessAccessToken, // 9 N Y
ProcessLdtInformation, // 10 Y Y
ProcessLdtSize, // 11 N Y
ProcessDefaultHardErrorMode, // 12 Y Y
ProcessIoPortHandlers, // 13 N Y
ProcessPooledUsageAndLimits, // 14 Y N
ProcessWorkingSetWatch, // 15 Y Y
ProcessUserModeIOPL, // 16 N Y
ProcessEnableAlignmentFaultFixup, // 17 N Y
ProcessPriorityClass, // 18 N Y
ProcessWx86Information, // 19 Y N
ProcessHandleCount, // 20 Y N
ProcessAffinityMask, // 21 N Y
ProcessPriorityBoost, // 22 Y Y
ProcessDeviceMap, // 23 Y Y
ProcessSessionInformation, // 24 Y Y
ProcessForegroundInformation, // 25 N Y
ProcessWow64Information // 26 Y N
} PROCESSINFOCLASS;

NTSYSAPI
NTSTATUS
NTAPI
ZwQueryInformationProcess(
						  IN HANDLE ProcessHandle,
						  IN PROCESSINFOCLASS ProcessInformationClass,
						  OUT PVOID ProcessInformation,
						  IN ULONG ProcessInformationLength,
						  OUT PULONG ReturnLength OPTIONAL
						  );

#ifdef __cplusplus
}
#endif
