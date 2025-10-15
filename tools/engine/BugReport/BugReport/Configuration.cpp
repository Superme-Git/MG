#include <windows.h>
#include "Configuration.h"
#include <string>

LPCWSTR GetConfigurationFilePath()
{
	static WCHAR filePath[MAX_PATH] = {0};
	if( filePath[0] == 0 )
	{
		DWORD dwRet = GetModuleFileName( NULL, filePath, MAX_PATH);
		std::wstring inifilename(filePath);
		std::wstring::size_type dotpos = inifilename.rfind(L'.');
		if( dotpos != std::wstring::npos )
		{
			std::wstring::size_type slashpos = inifilename.rfind(L'/');
			if( slashpos == std::wstring::npos || slashpos < dotpos )
			{
				std::wstring::size_type backslashpos = inifilename.rfind(L'\\');
				if( backslashpos == std::wstring::npos || backslashpos < dotpos  )
				{
					// trim the extension with dot. 
					// like 
					// abc.txt --> abc
					// ./a/bc.txt --> ./a/bc
					// ./a\bc.txt --> ./a\bc
					// ./a.b/c --> ./a.b/c
					inifilename.resize( dotpos );
				}
			}
		}
	
		inifilename.append(L".ini");
		wcscpy_s(filePath, MAX_PATH, inifilename.c_str());
	}

	return filePath;
}

BOOL GetOptionValueUnsignedLong(LPCWSTR lpszSectionName, LPCWSTR lpszOptionName, ULONG& rDefaultAndResultValue)
{
	static LPCWSTR filePath = GetConfigurationFilePath();
	ULONG ulOriginalDefaultValue = rDefaultAndResultValue;
	rDefaultAndResultValue = GetPrivateProfileIntW( lpszSectionName, lpszOptionName, rDefaultAndResultValue, filePath);
	return rDefaultAndResultValue != ulOriginalDefaultValue;
}

LPCWSTR GetOptionValueUnicodeString(LPCWSTR lpszSectionName, LPCWSTR lpszOptionName, LPWSTR lpszDefaultOptionValue, INT bufferLen )
{
	static LPCWSTR filePath = GetConfigurationFilePath();	
	GetPrivateProfileStringW( lpszSectionName, lpszOptionName, lpszDefaultOptionValue, lpszDefaultOptionValue, bufferLen, filePath);
	return lpszDefaultOptionValue;
}

BOOL GetConfigure_ConnectTimeout(ULONG& rOptionValue)
{
	return GetOptionValueUnsignedLong(L"bugreport", L"connect-timeout", rOptionValue);
}

BOOL GetConfigure_SendTimeout(ULONG& rOptionValue)
{
	return GetOptionValueUnsignedLong(L"bugreport", L"send-timeout", rOptionValue);
}

BOOL GetConfigure_ReceiveTimeout(ULONG& rOptionValue)
{
	return GetOptionValueUnsignedLong(L"bugreport", L"receive-timeout", rOptionValue);
}

LPCWSTR GetConfigure_HttpServerName(LPWSTR pwszDefaultValue, INT bufferLen )
{
	return GetOptionValueUnicodeString(L"bugreport", L"http-server-name", pwszDefaultValue, bufferLen );
}

BOOL GetConfigure_HttpServerPort(USHORT& defaultValue)
{
	ULONG ulValue = defaultValue;
	if( !GetOptionValueUnsignedLong(L"bugreport", L"http-server-port", ulValue) )
	{
		return false;
	}
	else
	{
		defaultValue = (USHORT)ulValue;
		return true;
	}
}

LPCWSTR GetConfigure_HttpServerApplication(LPWSTR pwszDefaultValue, INT bufferLen )
{
	return GetOptionValueUnicodeString(L"bugreport", L"http-server-application", pwszDefaultValue, bufferLen );
}
