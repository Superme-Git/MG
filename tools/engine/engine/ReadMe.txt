

简单使用示例代码

/*
....

#include <windows.h>
#include <stdio.h>
#include <tchar.h>
#include <assert.h>

#include "iapp.h"
#include "iengine.h"

// TestApp 的代码在 Engine目录下




INT WINAPI WinMain( HINSTANCE hInst, HINSTANCE, LPSTR, INT )
{
	TestApp tempApp = TestApp();
	Nuclear::EngineParameter ep;
	ep.szClassName = L"TestEngineClass";
	ep.szWindowTitle = L"TestEngine";
	ep.height = 600;
	ep.width = 800;
	ep.async_pfs = false;
	ep.app = &tempApp;

	Nuclear::IEngine* engine = Nuclear::GetEngine();
	
	engine ->Run(ep);
	return 0;
}

....

*/