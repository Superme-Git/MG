// PicSizer.cpp : Defines the entry point for the console application.
//
#include "stdafx.h"
#include <windows.h>
#include <string>
#include <vector>
#include <map>
#include <set>
#include "..\engine\renderer\renderer.h"

LRESULT CALLBACK MsgProc( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
{
	return DefWindowProc( hWnd, msg, wParam, lParam );
}

void scaleFile(Nuclear::Renderer* pRenderer, const std::wstring &path, double scale)
{
	WIN32_FIND_DATA findFileData;
	HANDLE hFind = INVALID_HANDLE_VALUE;
	std::wstring file = path + L"\\*.*";
	hFind = FindFirstFile(file.c_str(), &findFileData);
	BOOL ok = (hFind != INVALID_HANDLE_VALUE);
	for (;ok;ok=FindNextFile(hFind, &findFileData))
	{
		if (findFileData.dwFileAttributes == FILE_ATTRIBUTE_DIRECTORY)
		{
			if (wcscmp(findFileData.cFileName, L".") == 0 ||
				wcscmp(findFileData.cFileName, L"..") == 0)
				continue;
			scaleFile(pRenderer, path + L"\\" + findFileData.cFileName, scale);
		} else {
			size_t le = wcslen(findFileData.cFileName);
			if (le < 4)
				continue;
			le -= 4;
			if (findFileData.cFileName[le] != '.')
				continue;
			++le;
			if (findFileData.cFileName[le] != 'd' && findFileData.cFileName[le] != 'D')
				continue;
			++le;
			if (findFileData.cFileName[le] != 'd' && findFileData.cFileName[le] != 'D')
				continue;
			++le;
			if (findFileData.cFileName[le] != 's' && findFileData.cFileName[le] != 'S')
				continue;
			std::wstring filestr = path + L"\\" + findFileData.cFileName;
			Nuclear::PictureHandle handle = pRenderer->LoadPictureFromNativePath(filestr);
			if (handle == Nuclear::INVALID_PICTURE_HANDLE)
				continue;
			Nuclear::XBuffer buffer;
			Nuclear::PictureHandle scaledHandle = pRenderer->ClonePicture(handle, scale, false);
			
			if (!pRenderer->SaveTextureToFile(scaledHandle, filestr))
				continue;
			/*FILE *f = Nuclear::FOpen(filestr.c_str(), L"wb");
			if( f == NULL )
				continue;
			Nuclear::FWrite(f, buffer.constbegin(), buffer.size());
			fclose(f);*/
		}
	}
}

int _tmain(int argc, _TCHAR* argv[])
{
	_CrtSetDbgFlag( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
	wchar_t tmpString[MAX_PATH];//最后这个就记录输出文件格式
	GetPrivateProfileString(L"INPUT", L"path", L"", tmpString, MAX_PATH, L".\\PicSizer.ini");
	std::wstring inPath = tmpString;
	if (inPath.size() == 0)
	{
		MessageBox(0, L"输入路径错误", 0, 0);
		return 0;
	}

	GetPrivateProfileString(L"INPUT", L"scale", L"1.0", tmpString, MAX_PATH, L".\\PicSizer.ini");
	double fScale = _wtof(tmpString);
	if (fScale < 0.1)
	{
		MessageBox(0, L"缩得太小了吧？小于0.1？如果真需要这么小", 0, 0);
		return 0;
	}
	if (fScale > 1)
	{
		MessageBox(0, L"还需要放大？", 0, 0);
		return 0;
	}

	WNDCLASSEX wc = { sizeof(WNDCLASSEX) }; // 类结构
	// 注册窗口类
	wc.style = CS_CLASSDC;
	wc.lpfnWndProc = MsgProc;
	wc.hInstance = GetModuleHandle(NULL);
	wc.lpszClassName =  L"PicConverter";

	RegisterClassEx( &wc );

	// 初始化主窗口
	HWND hWnd = CreateWindow(wc.lpszClassName, wc.lpszClassName,
		WS_POPUP | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX,
		100, 100, 100, 100, GetDesktopWindow(), NULL, wc.hInstance, NULL );
	Nuclear::Renderer* pRenderer;
	Nuclear::XDisplayMode mode;
	if( !CreateRenderer(&pRenderer, hWnd, mode, NULL, 0) )
	{
		//XPALARM(L"渲染器创建失败");
		return 0;
	}

	scaleFile(pRenderer, inPath, fScale);
	Nuclear::DestroyRenderer(pRenderer);
	return 0;
}

