// stdafx.cpp : 只包括标准包含文件的源文件
// text1.pch 将作为预编译头
// stdafx.obj 将包含预编译类型信息

#include "stdafx.h"

// TODO:  在 STDAFX.H 中
// 引用任何所需的附加头文件，而不是在此文件中引用

void UTF8ToANSI(const char* strIn, char* strOut, int sourceCodePage, int targetCodepage)
{
	int len = strlen(strIn);
	int unicodeLen = MultiByteToWideChar(sourceCodePage, 0, strIn, -1, NULL, 0);
	wchar_t* pUnicode;
	pUnicode = new wchar_t[unicodeLen + 1];
	memset(pUnicode, 0, (unicodeLen + 1)*sizeof(wchar_t));
	MultiByteToWideChar(sourceCodePage, 0, strIn, -1, (LPWSTR)pUnicode, unicodeLen);
	BYTE* pTargetData = NULL;
	int targetLen = WideCharToMultiByte(targetCodepage, 0, (LPWSTR)pUnicode, -1, (char*)pTargetData, 0, NULL, NULL);
	pTargetData = new BYTE[targetLen + 1];
	memset(pTargetData, 0, targetLen + 1);
	WideCharToMultiByte(targetCodepage, 0, (LPWSTR)pUnicode, -1, (char*)pTargetData, targetLen, NULL, NULL);
	pTargetData[targetLen] = 0;
	strcpy_s(strOut, 40960, (char*)pTargetData);
	/*for (int i = 0; i < targetLen; ++i)
	{
		strOut[i] == ((char*)pTargetData)[i];
	}*/
	delete pUnicode;
	delete pTargetData;
}

BYTE* UTF8ToANSI2(const char* strIn,int sourceCodePage,int targetCodepage)
{
	int len = strlen(strIn);
	int unicodeLen = MultiByteToWideChar(sourceCodePage, 0, strIn, -1, NULL, 0);
	wchar_t* pUnicode;
	pUnicode = new wchar_t[unicodeLen + 1];
	memset(pUnicode, 0, (unicodeLen + 1)*sizeof(wchar_t));
	MultiByteToWideChar(sourceCodePage, 0, strIn, -1, (LPWSTR)pUnicode, unicodeLen);
	BYTE* pTargetData = NULL;
	int targetLen = WideCharToMultiByte(targetCodepage, 0, (LPWSTR)pUnicode, -1, (char*)pTargetData, 0, NULL, NULL);
	pTargetData = new BYTE[targetLen + 1];
	memset(pTargetData, 0, targetLen + 1);
	WideCharToMultiByte(targetCodepage, 0, (LPWSTR)pUnicode, -1, (char*)pTargetData, targetLen, NULL, NULL);
	pTargetData[targetLen] = 0;
	delete pUnicode;
	return pTargetData;
}

int UnicodeToUTF8(const wchar_t* strUnicode, int strUnicodeLen, char* strUTF8, int strUTF8Len)
{
	if ((strUnicode == NULL) || (strUnicodeLen <= 0) || strUTF8Len <= 0 && strUTF8Len != -1)
	{
		return -1;
	}
	int i, offset = 0;
	if (strUTF8Len == -1)
	{
		for (i = 0; i < strUnicodeLen; i++)
		{
			if (strUnicode[i] <= 0x007f)
			{
				offset += 1;
			}
			else if (strUnicode[i] >= 0x0080 && strUnicode[i] <= 0x07ff)
			{
				offset += 2;
			}
			else if (strUnicode[i] >= 0x0800 && strUnicode[i] <= 0xffff)
			{
				offset += 3;
			}
		}
		return offset + 1;
	}
	
	if (strUTF8 == NULL)
	{
		return -1;
	}

	for (i = 0; i < strUnicodeLen; i++)
	{
		if (strUnicode[i] <= 0x007f)
		{
			strUTF8[offset++] = (char)(strUnicode[i] & 0x007f);
		}
		else if (strUnicode[i] >= 0x0080 && strUnicode[i] <= 0x07ff)
		{
			strUTF8[offset++] = (char)(((strUnicode[i] & 0x07c0) >> 6) | 0x00c0);
			strUTF8[offset++] = (char)((strUnicode[i] & 0x003f) | 0x0080);
		}
		else if (strUnicode[i] >= 0x0800 && strUnicode[i] <= 0xffff)
		{
			strUTF8[offset++] = (char)(((strUnicode[i] & 0xf000) >> 12) | 0x00e0);
			strUTF8[offset++] = (char)(((strUnicode[i] & 0x0fc0) >> 6) | 0x0080);
			strUTF8[offset++] = (char)((strUnicode[i] & 0x003f) | 0x0080);
		}
	}
	strUTF8[offset] = '\0';
	return offset + 1;
}

std::vector<std::string> stovs(const char* s, const char splitChar1,const char splitChar2,const char splitChar3,const char splitChar4)
{
	std::vector<std::string> vsRet;
	std::string text;
	while (*s)
	{
		if (*s == splitChar1||*s==splitChar2||*s==splitChar3||*s==splitChar4)
		{
			vsRet.push_back(text);
			text = "";
		}
		else
			text += *s;
		++s;
	}
	if (text.size())
		vsRet.push_back(text);

	return vsRet;
}

std::vector<std::wstring> wstowvs(const wchar_t* s, const wchar_t splitChar1,const wchar_t splitChar2)
{
	std::vector<std::wstring> vsRet;
	std::wstring text;
	while (*s)
	{
		if (*s == splitChar1||*s==splitChar2)
		{
			vsRet.push_back(text);
			text = L"";
		}
		else
			text += *s;
		++s;
	}
	if (text.size())
		vsRet.push_back(text);

	return vsRet;
}

std::wstring wvstows(std::vector<std::wstring>& vsDir, int beginIndex, wchar_t splitChar)
{
	std::wstring ret;
	for (int i = beginIndex; i < vsDir.size(); ++i)
	{
		ret += vsDir[i];
		ret += splitChar;
	}

	if (ret.size())
		ret.resize(ret.size() - 1);

	return ret;
}

std::wstring wvstows(std::vector<std::wstring>& vsDir, int beginIndex, int endIndex, wchar_t splitChar)
{
	std::wstring ret;
	for (int i = beginIndex; i < vsDir.size() || i < endIndex; ++i)
	{
		ret += vsDir[i];
		ret += splitChar;
	}

	if (ret.size())
		ret.resize(ret.size() - 1);

	return ret;
}
