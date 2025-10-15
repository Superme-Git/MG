#pragma once

class DllInstance
{
public:
	HINSTANCE mhInstance;

	DWORD mHostProcessId;
	HWND mHostHWnd;
	DWORD mHostThreadId;

	HHOOK mHHook;

public:
	DllInstance();
	~DllInstance();

	static DllInstance& getInstance();

	void init(HINSTANCE hSelfInstance, DWORD hostProcessId);

	static HWND getProcessMainWindowHandle(DWORD processId);
	static DWORD getProcessMainThreadId(DWORD processId);
};