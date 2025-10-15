

#include "stdafx.h"

LPITEMIDLIST GetIDListFromPath(LPCTSTR lpszPath);


bool SelectFolder(CString &fullPath, CString &folderName, HWND hWnd, CString &title, UINT flag)
{
	BROWSEINFO bi;
	ZeroMemory(&bi,sizeof(BROWSEINFO));
	bi.hwndOwner = hWnd;
	bi.pszDisplayName = folderName.GetBuffer(MAX_PATH);
	bi.lpszTitle = title.GetBuffer();
	bi.ulFlags = flag;
	bi.pidlRoot = GetIDListFromPath(fullPath.GetBuffer());
	LPITEMIDLIST idl = SHBrowseForFolder(&bi);
	folderName.ReleaseBuffer();
	fullPath.ReleaseBuffer();
	title.ReleaseBuffer();
	if(idl == NULL)
		return false;
	SHGetPathFromIDList(idl, fullPath.GetBuffer(MAX_PATH));
	fullPath.ReleaseBuffer();
	if (fullPath.Right(1) == L"\\")
	{
		fullPath.Delete(fullPath.GetLength()-1);
	}
	GlobalFree(idl);
	return true;
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

unsigned int ReverseColor(unsigned int color)
{
	unsigned int result = 0;
	result = color & 0x0000FF00;
	result |= (color & 0x000000FF) << 16;
	result |= (color & 0x00FF0000) >> 16;
	return result;
}

