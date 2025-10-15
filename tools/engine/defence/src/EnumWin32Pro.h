#pragma once

#include <windows.h>
#include <stdio.h>

typedef BOOL (CALLBACK *PROCENUMPROC)(DWORD, WORD, LPSTR, LPARAM);

typedef struct {
	DWORD dwPID;
	PROCENUMPROC lpProc;
	DWORD lParam;
	BOOL bEnd;
} EnumInfoStruct;

BOOL WINAPI EnumProcs(PROCENUMPROC lpProc, LPARAM lParam);

BOOL WINAPI Enum16(DWORD dwThreadId, WORD hMod16, WORD hTask16,
				   PSZ pszModName, PSZ pszFileName, LPARAM lpUserDefined);

