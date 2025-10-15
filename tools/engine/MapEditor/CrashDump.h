#pragma once


namespace CrashDump {

	enum eDumpLevel
	{
		DL_1 = 1, 
		DL_2,
		DL_3,
		DL_4,	 
	};

	bool CrashDump_Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName, eDumpLevel dlevel);
	void CrashDump_Clean();

} 


