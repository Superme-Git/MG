// stdafx.cpp : 只包括标准包含文件的源文件
// SoundEditor.pch 将作为预编译头
// stdafx.obj 将包含预编译类型信息

#include "stdafx.h"



LPITEMIDLIST GetIDListFromPath(LPCTSTR lpszPath)
{  
	LPITEMIDLIST pidl = NULL;  
	LPSHELLFOLDER pDesktopFolder;  
	OLECHAR szOleChar[MAX_PATH];    
	ULONG chEaten;    
	ULONG dwAttributes;    
	HRESULT hr;  
	// 获得桌面的pidl
	if (SUCCEEDED(SHGetDesktopFolder(&pDesktopFolder)))    
	{    
		//Unicode 转换
#if !defined(_UNICODE)  
		MultiByteToWideChar(CP_ACP, MB_PRECOMPOSED, lpszPath, -1, szOleChar, MAX_PATH);  
#else  
		//_tcscpy(szOleChar, lpszPath);  
		wcscpy_s(szOleChar, MAX_PATH, lpszPath);
#endif  
		//转换路径
		hr = pDesktopFolder->ParseDisplayName(NULL, NULL, szOleChar, &chEaten,&pidl,&dwAttributes);
		pDesktopFolder->Release();  
		if (SUCCEEDED(hr))  
			return pidl;  
	}  
	return   NULL;  
}