#pragma once


namespace CrashDump {

	enum eDumpLevel
	{
		DL_1 = 1, 
		DL_2,
		DL_3,
		DL_4,	 
	};

	void CrashDump_Clean();
	bool CrashDump_Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName, eDumpLevel dlevel);

} 


