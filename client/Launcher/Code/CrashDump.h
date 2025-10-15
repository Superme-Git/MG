#pragma once
#include <string>
namespace MHSD_CrashDump {

	bool CrashDump_Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName);
	void CrashDump_SendAssert();
	void CrashDump_Clean();
 
} // namespace CrashDump 
