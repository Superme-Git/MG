#include <windows.h>
#include <stdio.h>
#include <tchar.h>
#include <string>
//Assert
#include <assert.h>
//STL
#include <vector>
#include <map>
#include <list>
#include <set>
#include <deque>
#include <hash_map>

#include "..\..\engine\common\fileiomanager.h"
#include "..\ThumbnailManager\ithumbnailmanager.h"
#include "txprenderer.h"

Nuclear::FileIOManager *g_pFileIOMan = NULL;
TMan::TXPRenderer* g_pRenderer = NULL;
Nuclear::Renderer* g_pXPRenderer = NULL;
TMan::IThumbnailManager *g_pThumbnailManager = NULL;

std::list<TMan::ThumbnailHandle> g_handles;


BOOL Init(HWND hwnd)
{
	//降低浮点运算精度提高计算速度
	//	_control87(_PC_24, _MCW_PC);
	//	_control87( _CW_DEFAULT, 0xfffff);
	//	_control87(_DN_FLUSH, _MCW_DN);

	g_pFileIOMan = new Nuclear::FileIOManager();
	g_pFileIOMan->Init(true, true);

	Nuclear::CreateRenderer(&g_pXPRenderer, hwnd, Nuclear::XDisplayMode(1024, 768, 32), g_pFileIOMan);
	if (g_pXPRenderer)
	{
		g_pXPRenderer->SetAutoTestDevice(true);
	}
	
	g_pRenderer = new TMan::TXPRenderer(g_pXPRenderer);
	g_pThumbnailManager = TMan::CreateThumbnailManager(g_pRenderer, NULL);

	int iCaptionHeight = GetSystemMetrics(SM_CYCAPTION);
	int iFixedFrameSizeY = GetSystemMetrics(SM_CYFIXEDFRAME);
	int iFixedFrameSizeX = GetSystemMetrics(SM_CXFIXEDFRAME);
	MoveWindow(hwnd, 0, 0, 1024+2*iFixedFrameSizeX, 768+2*iFixedFrameSizeY+iCaptionHeight, true);
	return true;
}

BOOL Destory()
{
	if (g_pThumbnailManager)
	{
		TMan::DestroyThumbnailManager(g_pThumbnailManager);
		g_pThumbnailManager = NULL;
	}

	if (g_pRenderer)
	{
		delete g_pRenderer;
		g_pRenderer = NULL;
	}

	if (g_pXPRenderer)
	{
		Nuclear::DestroyRenderer(g_pXPRenderer);
		g_pXPRenderer = NULL;
	}

	if (g_pFileIOMan)
	{
		g_pFileIOMan->Destroy();
		delete g_pFileIOMan;
		g_pFileIOMan = NULL;
	}
	return true;
}

void ReleaseHandle(TMan::ThumbnailHandle &handle)
{
	if (handle != TMan::INVALID_THUNMBNAIL_HANDLE)
	{
		g_pThumbnailManager->ReleasePicture(handle);
		handle = TMan::INVALID_THUNMBNAIL_HANDLE;
	}
}

LRESULT WINAPI MsgProc( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
{
	switch( msg )
	{
	case WM_TIMER:
		if( wParam == 1 )
			Nuclear::TimerOB::OnSourceTimer();
		break;
	case WM_DESTROY:
		Destory();
		PostQuitMessage(0);
		break;

	case WM_KEYDOWN:
		switch( wParam)
		{
		case VK_F1:
			break;

		case VK_F2:
			g_pXPRenderer->DumpAllPicture(L"D:\\AAA\\");
			break;

		case VK_F3:
			{
				Nuclear::XBuffer buffer;
				g_pFileIOMan->GetFileImage(L"/res/image/pak/skill/anduchencang_48.tga", buffer);
				TMan::ThumbnailHandle handle = g_pThumbnailManager->LoadPicture(buffer.constbegin(), buffer.size());
				g_handles.push_back(handle);
			}
			
			break;

		case VK_F4:
			{
				Nuclear::XBuffer buffer;
				g_pFileIOMan->GetFileImage(L"/res/image/pak/skill/anshenguming_32.tga", buffer);
				TMan::ThumbnailHandle handle = g_pThumbnailManager->LoadPicture(buffer.constbegin(), buffer.size());
				g_handles.push_back(handle);
			}
			break;

		case VK_F5:
			{
				Nuclear::XBuffer buffer;
				g_pFileIOMan->GetFileImage(L"/res/image/pak/skill/anshenjue_48.tga", buffer);
				TMan::ThumbnailHandle handle = g_pThumbnailManager->LoadPicture(buffer.constbegin(), buffer.size());
				g_handles.push_back(handle);
			}
			break;

		case VK_F6:
			{
				Nuclear::XBuffer buffer;
				g_pFileIOMan->GetFileImage(L"/res/image/pak/skill/baiheliangchi_32.tga", buffer);
				TMan::ThumbnailHandle handle = g_pThumbnailManager->LoadPicture(buffer.constbegin(), buffer.size());
				g_handles.push_back(handle);
			}
			break;

		case VK_F7:
			{
				TMan::ThumbnailHandle handle = g_handles.front();
				g_handles.pop_front();
				ReleaseHandle(handle);
			}

		default:
			break;
		}
		break;

	default:
		break;
	}

	return DefWindowProc( hWnd, msg, wParam, lParam );
}

void DrawPicture(TMan::ThumbnailHandle handle, int x, int y, unsigned int color)
{
	if (handle != TMan::INVALID_THUNMBNAIL_HANDLE)
	{
		int w, h;
		g_pThumbnailManager->GetPictureInfo(handle, w, h);
		TMan::FRECT rect((float)x, (float)y, (float)x+w, (float)y+h);
		g_pThumbnailManager->DrawPicture(handle, rect, NULL, color);
	}
}

void Render()
{
	g_pXPRenderer->Begin();
	std::list<TMan::ThumbnailHandle>::iterator it = g_handles.begin(), ie = g_handles.end();
	int i = 0;
	for (;it!=ie;++it,i+=50)
	{
		DrawPicture(*it, i, 30, 0xFFFFFFFF);
	}
	g_pXPRenderer->End();
	Sleep(100);
}

INT WINAPI WinMain( HINSTANCE hInst, HINSTANCE, LPSTR, INT )
{    
	WNDCLASSEX wc = { sizeof(WNDCLASSEX), CS_CLASSDC, MsgProc, 0L, 0L,
		GetModuleHandle(NULL), NULL, NULL, NULL, NULL,
		L"NuclearTest", NULL };
	RegisterClassEx( &wc );


	HWND hWnd = CreateWindow( L"NuclearTest", L"NuclearTest",
		WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX, 0, 0, 1280, 1024,
		GetDesktopWindow(), NULL, wc.hInstance, NULL );

	SetTimer(hWnd, 1, 1000, NULL);

	if( Init( hWnd ) )
	{		
		ShowWindow( hWnd, SW_SHOWDEFAULT );
		UpdateWindow( hWnd );


		MSG msg;
		ZeroMemory( &msg, sizeof(msg) );
		while( msg.message!=WM_QUIT )
		{
			if( PeekMessage( &msg, NULL, 0U, 0U, PM_REMOVE ) )
			{
				TranslateMessage( &msg );
				DispatchMessage( &msg );
			}
			else
			{
				Render();
			}
		}
	}

	UnregisterClass( L"NuclearTest", wc.hInstance );
	return 0;
}