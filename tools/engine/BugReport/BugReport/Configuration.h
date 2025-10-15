#pragma once


LPCWSTR GetConfigurationFilePath();
BOOL GetOptionValueUnsignedLong(LPCWSTR lpszSectionName, LPCWSTR lpszOptionName, ULONG& rDefaultAndResultValue);
LPCWSTR GetOptionValueUnicodeString(LPCWSTR lpszSectionName, LPCWSTR lpszOptionName, LPWSTR lpszDefaultOptionValue, INT bufferLen );

BOOL GetConfigure_ConnectTimeout(ULONG& rOptionValue);
BOOL GetConfigure_SendTimeout(ULONG& rOptionValue);
BOOL GetConfigure_ReceiveTimeout(ULONG& rOptionValue);

LPCWSTR GetConfigure_HttpServerName(LPWSTR pwszDefaultValue, INT bufferLen);
BOOL GetConfigure_HttpServerPort(USHORT& defaultValue);
LPCWSTR GetConfigure_HttpServerApplication(LPWSTR pwszDefaultValue, INT bufferLen);
