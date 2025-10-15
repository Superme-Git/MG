// stdafx.cpp : source file that includes just the standard includes
// MapEditor.pch will be the pre-compiled header
// stdafx.obj will contain the pre-compiled type information

#include "stdafx.h"


DWORD XL_MD::s_nProcessId = 0;

unsigned int ReverseColor(unsigned int color)
{
	unsigned int result = 0;
	result = color & 0x0000FF00;
	result |= (color & 0x000000FF) << 16;
	result |= (color & 0x00FF0000) >> 16;
	return result;
}

LPITEMIDLIST GetIDListFromPath(LPCTSTR lpszPath)
{  
	LPITEMIDLIST pidl = NULL;  
	LPSHELLFOLDER pDesktopFolder;  
	OLECHAR szOleChar[MAX_PATH];    
	ULONG chEaten;    
	ULONG dwAttributes;    
	HRESULT hr;  
	
	if (SUCCEEDED(SHGetDesktopFolder(&pDesktopFolder)))    
	{    
		
#if !defined(_UNICODE)  
		MultiByteToWideChar(CP_ACP, MB_PRECOMPOSED, lpszPath, -1, szOleChar, MAX_PATH);  
#else  
		
		wcscpy_s(szOleChar, MAX_PATH, lpszPath);
#endif  
		
		hr = pDesktopFolder->ParseDisplayName(NULL, NULL, szOleChar, &chEaten,&pidl,&dwAttributes);
		pDesktopFolder->Release();  
		if (SUCCEEDED(hr))  
			return pidl;  
	}  
	return   NULL;  
}