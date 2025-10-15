// dllmain.cpp : 定义 DLL 应用程序的入口点。
#include "stdafx.h"
#include <tchar.h>
#include "DllInstance.h"
#include "dllmain.h"

#define WM_HOOK_INSTALLED (WM_USER + 1)

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
	DWORD processId = GetCurrentProcessId();

	TCHAR szMsg[1024];

	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:

		DllInstance::getInstance().init(hModule, processId);

		_stprintf(szMsg, _T("DLL附加到进程：%u"), processId);
		MessageBox(DllInstance::getInstance().mHostHWnd, szMsg, _T("WaiGuaDll"), MB_OK);

		break;

	case DLL_THREAD_ATTACH:
		break;

	case DLL_THREAD_DETACH:
		break;

	case DLL_PROCESS_DETACH:
		_stprintf(szMsg, _T("DLL与进程分离：%u"), processId);
		MessageBox(NULL, szMsg, _T("WaiGuaDll"), MB_OK);
		break;
	}
	return TRUE;
}

LRESULT CALLBACK GetMsgProc(int code, WPARAM wParam, LPARAM lParam)
{
	MSG* pMsg = (MSG*)lParam;
	if (pMsg)
	{
		switch (pMsg->message)
		{
		case WM_HOOK_INSTALLED:
			break;
		}
	}

	if (code < 0)
	{
		// 必须调CallNextHookEx
		return CallNextHookEx(DllInstance::getInstance().mHHook, code, wParam, lParam);
	}
	else
	{
		// 强烈建议调CallNextHookEx
		return CallNextHookEx(DllInstance::getInstance().mHHook, code, wParam, lParam);
	}
}

void setHook(DWORD processId)
{
	DWORD threadId = DllInstance::getProcessMainThreadId(processId);

	HHOOK hHook = SetWindowsHookEx(WH_GETMESSAGE, GetMsgProc, DllInstance::getInstance().mhInstance, threadId);
	if (hHook)
	{
		DllInstance::getInstance().mHHook = hHook;

		PostThreadMessage(threadId, WM_HOOK_INSTALLED, 0, 0);
	}
}