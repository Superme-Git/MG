///=============================================================================
///	文 件 名：KHLogger.h
///	作    者：刘东学(meteor)
///=============================================================================
///	模块说明：日志记录器
///=============================================================================
/// 版本历史：
///=============================================================================
///		2007-06-21 13:37:19 - V1.0 创建
///=============================================================================
#pragma once

#ifdef KH_COMMON_API
#define KH_MESSAGE_API KH_COMMON_API
#else
#define KH_MESSAGE_API 
#endif

///=============================================================================
///	设置外挂消息钩子
///	注：同时工作的外挂消息钩子最多只有一个，每次设置信息外挂消息钩子都会使
///	原有消息钩子失效。
///=============================================================================
typedef void (*LPF_KHMessageHookA)(LPVOID pUserParam, LPCSTR lpszMessage);
typedef void (*LPF_KHMessageHookW)(LPVOID pUserParam, LPCWSTR lpwszMessage);

LPF_KHMessageHookA KH_MESSAGE_API KHInstallMessageHookA( LPF_KHMessageHookA newHook, LPVOID pUserParam );
LPF_KHMessageHookW KH_MESSAGE_API KHInstallMessageHookW( LPF_KHMessageHookW newHook, LPVOID pUserParam );

///=============================================================================
///	消息输出文件路径API
///=============================================================================
void KH_MESSAGE_API KHMsgLog_SetFilePathA(LPCSTR lpszFilePath);
void KH_MESSAGE_API KHMsgLog_SetFilePathW(LPCWSTR lpszFilePath);

///=============================================================================
///	消息输出API
///=============================================================================
void KH_MESSAGE_API KHMsgLogA(LPCSTR lpszMessage);
void KH_MESSAGE_API KHMsgLogW(LPCWSTR lpszMessage);

///=============================================================================
///	可变参数消息输出API
///=============================================================================

void KH_MESSAGE_API __cdecl KHMsgLog_InfoA(LPCSTR lpszFormat, ...);
void KH_MESSAGE_API __cdecl KHMsgLog_InfoW(LPCWSTR lpszFormat, ...);

void KH_MESSAGE_API __cdecl KHMsgLog_DebugA(LPCSTR lpszFormat, ...);
void KH_MESSAGE_API __cdecl KHMsgLog_DebugW(LPCWSTR lpszFormat, ...);

void KH_MESSAGE_API __cdecl KHMsgLog_WarningA(LPCSTR lpszFormat, ...);
void KH_MESSAGE_API __cdecl KHMsgLog_WarningW(LPCWSTR lpszFormat, ...);

void KH_MESSAGE_API __cdecl KHMsgLog_ErrorA(LPCSTR lpszFormat, ...);
void KH_MESSAGE_API __cdecl KHMsgLog_ErrorW(LPCWSTR lpszFormat, ...);

void KH_MESSAGE_API __cdecl KHMsgLog_FatalA(LPCSTR lpszFormat, ...);
void KH_MESSAGE_API __cdecl KHMsgLog_FatalW(LPCWSTR lpszFormat, ...);

#ifdef UNICODE
#define KHMsgLog_SetFilePath KHMsgLog_SetFilePathW
#define KHMsgLog			KHMsgLogW
#define KHMsgLog_Info		KHMsgLog_InfoW
#define KHMsgLog_Debug		KHMsgLog_DebugW
#define KHMsgLog_Warning	KHMsgLog_WarningW
#define KHMsgLog_Error		KHMsgLog_ErrorW
#define KHMsgLog_Fatal		KHMsgLog_FatalW
#else
#define KHMsgLog_SetFilePath KHMsgLog_SetFilePathA
#define KHMsgLog			KHMsgLogA
#define KHMsgLog_Info		KHMsgLog_InfoA
#define KHMsgLog_Debug		KHMsgLog_DebugA
#define KHMsgLog_Warning	KHMsgLog_WarningA
#define KHMsgLog_Error		KHMsgLog_ErrorA
#define KHMsgLog_Fatal		KHMsgLog_FatalA
#endif
