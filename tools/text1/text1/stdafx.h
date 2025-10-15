// stdafx.h : ��׼ϵͳ�����ļ��İ����ļ���
// ���Ǿ���ʹ�õ��������ĵ�
// �ض�����Ŀ�İ����ļ�
//

#pragma once

#include "targetver.h"

#include <stdio.h>
#include <tchar.h>
#define _ATL_CSTRING_EXPLICIT_CONSTRUCTORS      // ĳЩ CString ���캯��������ʽ��
#define _AFX_NO_MFC_CONTROLS_IN_DIALOGS         // �Ƴ��Ի����е� MFC �ؼ�֧��

#ifndef VC_EXTRALEAN
#define VC_EXTRALEAN            //  �� Windows ͷ�ļ����ų�����ʹ�õ���Ϣ
#endif

#include <afx.h>
#include <afxwin.h>         // MFC ��������ͱ�׼���
#include <afxext.h>         // MFC ��չ
#ifndef _AFX_NO_OLE_SUPPORT
#include <afxdtctl.h>           // MFC �� Internet Explorer 4 �����ؼ���֧��
#endif
#ifndef _AFX_NO_AFXCMN_SUPPORT
#include <afxcmn.h>                     // MFC �� Windows �����ؼ���֧��
#endif // _AFX_NO_AFXCMN_SUPPORT

#include <iostream>
#include <string>
#include <vector>


// TODO:  �ڴ˴����ó�����Ҫ������ͷ�ļ�
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

