#include "stdafx.h"
#include "DllInstance.h"

DllInstance::DllInstance()
	: mhInstance(NULL)

	, mHostProcessId(0)
	, mHostHWnd(NULL)
	, mHostThreadId(0)

	, mHHook(NULL)
{
}

DllInstance::~DllInstance()
{
}

static DllInstance sInstance;

DllInstance& DllInstance::getInstance()
{
	return sInstance;
}

void DllInstance::init(HINSTANCE hSelfInstance, DWORD hostProcessId)
{
	mhInstance = hSelfInstance;

	mHostProcessId = hostProcessId;

	GUITHREADINFO guiThreadInfo;
	memset(&guiThreadInfo, 0, sizeof(guiThreadInfo));
	guiThreadInfo.cbSize = sizeof(guiThreadInfo);

	if (GetGUIThreadInfo(mHostProcessId, &guiThreadInfo))
	{
		mHostHWnd = guiThreadInfo.hwndActive;

		if (mHostHWnd)
		{
			mHostThreadId = GetWindowThreadProcessId(mHostHWnd, NULL);
		}
	}
}

struct EWPParam
{
	DWORD processId;
	DWORD threadId;
	HWND hWnd;
};

BOOL CALLBACK EnumWindowsProc(HWND hwnd, LPARAM lParam)
{
	DWORD processId, threadId;
	threadId = GetWindowThreadProcessId(hwnd, &processId);

	EWPParam* pParam = (EWPParam*)lParam;
	if (pParam)
	{
		if (processId == pParam->processId)
		{
			pParam->hWnd = hwnd;
			pParam->threadId = threadId;

			return FALSE;
		}
	}

	return TRUE;
}

HWND DllInstance::getProcessMainWindowHandle(DWORD processId)
{
	EWPParam param = { processId, 0, NULL };
	EnumWindows(EnumWindowsProc, (LPARAM)&param);

	return param.hWnd;
}

DWORD DllInstance::getProcessMainThreadId(DWORD processId)
{
	EWPParam param = { processId, 0, NULL };
	EnumWindows(EnumWindowsProc, (LPARAM)&param);

	return param.threadId;
}