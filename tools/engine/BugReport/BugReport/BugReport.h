#pragma once


#include "resource.h"
#include <windows.h>
#include <tchar.h>
#include <stdio.h>


typedef struct _PartInfo
{
	LPWSTR lpContent;
	DWORD dwInfoLen;
}PartInfo,* PPartInfo;


enum ePartIndex
{
	ExceptionCode=0,		// 异常码
	ExceptionAddress,
	Register,
	StackInfo,
	OSVersion,

	Graphic,
	Driver,
	Descript,
	AppVersion,
	ComputerName,
	UserName,
	QQMsn,
	Phone,
	EMail,
	PartCount,
};

extern DWORD WINAPI GetDisplayCardInfo_GetVideoInfo(LPDWORD pdwGviError,PPartInfo pPartArray);
extern DWORD WINAPI GetDumpInfo_GetInfo(PVOID lpMappedFile,PartInfo* pPartArray);
extern DWORD WINAPI GetDumpInfo();
extern BOOL WINAPI GetDumpInfo_DummyInfo(PartInfo* pPartArray);

// Log Functions
extern bool InitLog(const TCHAR *lpFileName);
extern void Log(...);
extern void CloseLog();

// 显示状态信息
extern void NotifyStatus( LPCTSTR lpszMessage );

DWORD WINAPI PostUp_PostInfo();


//全局变量
extern TCHAR	g_szDumpFile[MAX_PATH];

extern CHAR		g_ccDumpFile[MAX_PATH];

extern HWND		g_hDlg;
extern HANDLE	g_hPostThread;
extern HWND		g_hProgressBar;
extern HWND		g_hDescript;
extern HWND		g_hQQMsn;
extern HWND		g_hPhone;
extern HWND		g_hEMail;
extern BOOL		g_bGetDumpInfo;
extern HWND		g_hFileList;

extern PartInfo g_szData[PartCount];

#define WM_SENDRESULT		(WM_USER+100)