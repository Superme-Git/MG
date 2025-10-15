#pragma once

extern "C"
{
__declspec(dllexport) bool CrashDump_Init( const wchar_t* lpszDumpFilename, const wchar_t* lpszReportExeName );
__declspec(dllexport) void CrashDump_Clean();

}