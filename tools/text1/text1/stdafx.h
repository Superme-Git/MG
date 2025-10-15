// stdafx.h : 标准系统包含文件的包含文件，
// 或是经常使用但不常更改的
// 特定于项目的包含文件
//

#pragma once

#include "targetver.h"

#include <stdio.h>
#include <tchar.h>
#define _ATL_CSTRING_EXPLICIT_CONSTRUCTORS      // 某些 CString 构造函数将是显式的
#define _AFX_NO_MFC_CONTROLS_IN_DIALOGS         // 移除对话框中的 MFC 控件支持

#ifndef VC_EXTRALEAN
#define VC_EXTRALEAN            //  从 Windows 头文件中排除极少使用的信息
#endif

#include <afx.h>
#include <afxwin.h>         // MFC 核心组件和标准组件
#include <afxext.h>         // MFC 扩展
#ifndef _AFX_NO_OLE_SUPPORT
#include <afxdtctl.h>           // MFC 对 Internet Explorer 4 公共控件的支持
#endif
#ifndef _AFX_NO_AFXCMN_SUPPORT
#include <afxcmn.h>                     // MFC 对 Windows 公共控件的支持
#endif // _AFX_NO_AFXCMN_SUPPORT

#include <iostream>
#include <string>
#include <vector>


// TODO:  在此处引用程序需要的其他头文件
enum FileCodeType
{
	FCT_ANSI,
	FCT_UTF8,
	FCT_UNICODE,
};

int UnicodeToUTF8(const wchar_t* strUnicode, int strUnicodeLen, char* strUTF8, int strUTF8Len);
BYTE* UTF8ToANSI2(const char* strIn, int sourceCodePage, int targetCodepage);
void UTF8ToANSI(const char* strIn, char* strOut, int sourceCodePage, int targetCodepage);
std::vector<std::string> stovs(const char* s, const char splitChar1, const char splitChar2, const char splitChar3, const char splitChar4);
std::vector<std::wstring> wstowvs(const wchar_t* s, const wchar_t splitChar1, const wchar_t splitChar2=0);
std::wstring wvstows(std::vector<std::wstring>& vsDir, int beginIndex, wchar_t splitChar);
std::wstring wvstows(std::vector<std::wstring>& vsDir, int beginIndex, int endIndex, wchar_t splitChar);

#include "filesystem.h"

