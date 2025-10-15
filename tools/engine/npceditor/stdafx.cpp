// stdafx.cpp : ֻ������׼�����ļ���Դ�ļ�
// SoundEditor.pch ����ΪԤ����ͷ
// stdafx.obj ������Ԥ����������Ϣ

#include "stdafx.h"



LPITEMIDLIST GetIDListFromPath(LPCTSTR lpszPath)
{  
	LPITEMIDLIST pidl = NULL;  
	LPSHELLFOLDER pDesktopFolder;  
	OLECHAR szOleChar[MAX_PATH];    
	ULONG chEaten;    
	ULONG dwAttributes;    
	HRESULT hr;  
	// ��������pidl
	if (SUCCEEDED(SHGetDesktopFolder(&pDesktopFolder)))    
	{    
		//Unicode ת��
#if !defined(_UNICODE)  
		MultiByteToWideChar(CP_ACP, MB_PRECOMPOSED, lpszPath, -1, szOleChar, MAX_PATH);  
#else  
		//_tcscpy(szOleChar, lpszPath);  
		wcscpy_s(szOleChar, MAX_PATH, lpszPath);
#endif  
		//ת��·��
		hr = pDesktopFolder->ParseDisplayName(NULL, NULL, szOleChar, &chEaten,&pidl,&dwAttributes);
		pDesktopFolder->Release();  
		if (SUCCEEDED(hr))  
			return pidl;  
	}  
	return   NULL;  
}