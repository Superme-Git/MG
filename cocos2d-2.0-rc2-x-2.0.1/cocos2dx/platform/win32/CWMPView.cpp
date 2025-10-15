/********************************************************************
	filename:	CWMPView.cpp
	author:		Ñî´ºÀ×
	date:		2015/05/11
	desc:		
	Copyright (c) 2015 locojoy. All rights reserved.
*********************************************************************/

#include "CWMPView.h"
#include "platform/VideoPlayerEngine.h"
#include "CWMPEventDispatch.h"
#include <Digitalv.h>

#define VIDEO_WINDOW_CLASS_NAME _T("CWMPView")

static LRESULT CALLBACK _WindowProc(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	switch (uMsg)
	{
	case WM_PAINT:
		PAINTSTRUCT ps;
		BeginPaint(hWnd, &ps);
		EndPaint(hWnd, &ps);

		cocos2d::CCDirector::sharedDirector()->SetBackgroundMode(true);

		return 0;
		break;

	case WM_ACTIVATE:
		cocos2d::CCDirector::sharedDirector()->SetBackgroundMode(true);
		return 0;

	case WM_LBUTTONDOWN:
		cocos2d::VideoPlayer::executeVideoCallback(-1, cocos2d::VideoPlayer::COMPLETED);
		return 0;

	default:
		return DefWindowProc(hWnd, uMsg, wParam, lParam);
	}
}

CWMPView::CWMPView(int index)
	: mIndex(index)

	, mHWnd(NULL)
	, mHWndInput(NULL)
	, mDeviceId(0)
	, mStatus(0)

	, mParentWindowWidth(0)
	, mParentWindowHeight(0)

	, mbVisible(false)
	, mbStartPlaying(false)
{
}

CWMPView::~CWMPView()
{
	destroy();
}

bool CWMPView::init(HWND hParent, const RECT& rc)
{
	mParentWindowWidth = rc.right - rc.left;
	mParentWindowHeight = rc.bottom - rc.top;

	HINSTANCE hInstance = GetModuleHandle(NULL);

	WNDCLASS wc;
	memset(&wc, 0, sizeof(wc));
	wc.hInstance = hInstance;
	wc.lpfnWndProc = _WindowProc;
	wc.lpszClassName = VIDEO_WINDOW_CLASS_NAME;
	wc.style = CS_HREDRAW | CS_VREDRAW | CS_OWNDC;
	wc.hbrBackground = (HBRUSH)GetStockObject(BLACK_BRUSH);
	RegisterClass(&wc);

	mHWnd = CreateWindow(VIDEO_WINDOW_CLASS_NAME, _T(""), WS_CHILD | WS_VISIBLE, 0, 0, mParentWindowWidth, mParentWindowHeight, hParent, NULL, hInstance, NULL);

	if (mHWnd)
	{
		mHWndInput = CreateWindow(VIDEO_WINDOW_CLASS_NAME, _T(""), WS_CHILD | WS_VISIBLE, 0, 0, mParentWindowWidth, mParentWindowHeight, mHWnd, NULL, hInstance, NULL);
	}

	return true;
}

void CWMPView::destroy()
{
	if (mDeviceId)
	{
		stop();

		mciSendCommand(mDeviceId, MCI_CLOSE, MCI_WAIT, NULL);
		mDeviceId = 0;
	}

	if (mHWnd)
	{
		DestroyWindow(mHWnd);
		mHWnd = NULL;
	}

	if (mHWndInput)
	{
		DestroyWindow(mHWndInput);
		mHWndInput = NULL;
	}

	HINSTANCE hInstance = GetModuleHandle(NULL);
	UnregisterClass(VIDEO_WINDOW_CLASS_NAME, hInstance);
}

void CWMPView::setFilename(const TCHAR* filename)
{
	MCI_DGV_OPEN_PARMS mciOpen;
	memset(&mciOpen, 0, sizeof(mciOpen));

	mciOpen.lpstrElementName = const_cast<TCHAR*>(filename);  // Set the filename.
	mciOpen.dwStyle = WS_CHILD | WS_VISIBLE;            // Set the style. 
	mciOpen.hWndParent = mHWnd;             // Give a window handle. 

	MCIERROR err = mciSendCommand(0, MCI_OPEN, (DWORD)(MCI_OPEN_ELEMENT | MCI_DGV_OPEN_PARENT | MCI_DGV_OPEN_WS), (DWORD)&mciOpen);
	if (err)
	{
		TCHAR errStr[512];
		mciGetErrorString(err, errStr, 512);
		TCHAR msg[1024];
		_stprintf(msg, _T("[CWMPView::setFilename] error %s\n"), errStr);
		OutputDebugString(msg);
		return;
	}

	// Open operation is successful. Continue. 
	mDeviceId = mciOpen.wDeviceID;
}

bool CWMPView::isFullscreen() const
{
	return false;
}

void CWMPView::setFullscreen(bool var)
{
}

void CWMPView::enableContextMenu(bool var)
{
}

void CWMPView::showUIButtons(bool var)
{
}

void CWMPView::setStrechToFit(bool var)
{
}

void CWMPView::setVideoRect(int x, int y, int width, int height)
{
	MCI_DGV_RECT_PARMS mciRect;
	memset(&mciRect, 0, sizeof(mciRect));

	MCIERROR err = mciSendCommand(mDeviceId, MCI_WHERE, MCI_DGV_WHERE_SOURCE, (DWORD)&mciRect);
	if (err)
	{
		TCHAR errStr[512];
		mciGetErrorString(err, errStr, 512);
		TCHAR msg[1024];
		_stprintf(msg, _T("[CWMPView::setVideoRect] error %s\n"), errStr);
		OutputDebugString(msg);
		return;
	}

	int videoWidth = mciRect.rc.right - mciRect.rc.left;
	int videoHeight = mciRect.rc.bottom - mciRect.rc.top;
	float videoRatio = (float)videoWidth / videoHeight;

	float parentWindowRatio = (float)mParentWindowWidth / mParentWindowHeight;

	float scale = videoRatio > parentWindowRatio ? (float)mParentWindowWidth / videoWidth : (float)mParentWindowHeight / videoHeight;

	int playWindowWidth = videoWidth * scale;
	int playWindowHeight = videoHeight * scale;
	int playWindowX = (mParentWindowWidth - playWindowWidth) / 2;
	int playWindowY = (mParentWindowHeight - playWindowHeight) / 2;

	MCI_DGV_PUT_PARMS mciPut;
	memset(&mciPut, 0, sizeof(mciPut));
	mciPut.rc = { playWindowX, playWindowY, playWindowWidth, playWindowHeight };
	err = mciSendCommand(mDeviceId, MCI_PUT, MCI_DGV_PUT_CLIENT | MCI_DGV_PUT_WINDOW | MCI_DGV_RECT, (DWORD)&mciPut);
	if (err)
	{
		TCHAR errStr[512];
		mciGetErrorString(err, errStr, 512);
		TCHAR msg[1024];
		_stprintf(msg, _T("[CWMPView::setVideoRect] error %s\n"), errStr);
		OutputDebugString(msg);
		return;
	}
}

void CWMPView::play()
{
	MCI_DGV_PLAY_PARMS mciPlay;    // play parameters 
	DWORD dwFlags = 0;

	// Send the MCI_PLAY command and return the result. 
	MCIERROR err = mciSendCommand(mDeviceId, MCI_PLAY, dwFlags, (DWORD)&mciPlay);
	if (err)
	{
		TCHAR errStr[512];
		mciGetErrorString(err, errStr, 512);
		TCHAR msg[1024];
		_stprintf(msg, _T("[CWMPView::play] error %s\n"), errStr);
		OutputDebugString(msg);
		return;
	}

	PostMessage(mHWnd, WM_ACTIVATE, 0, 0);
}

void CWMPView::pause()
{
}

void CWMPView::stop()
{
	MCIERROR err = mciSendCommand(mDeviceId, MCI_STOP, NULL, NULL);
	if (err)
	{
		TCHAR errStr[512];
		mciGetErrorString(err, errStr, 512);
		TCHAR msg[1024];
		_stprintf(msg, _T("[CWMPView::stop] error %s\n"), errStr);
		OutputDebugString(msg);
		return;
	}
}

void CWMPView::setPosition(float sec)
{
}

void CWMPView::setVisible(bool var)
{
	mbVisible = var;

	if (mHWnd)
	{
		ShowWindow(mHWnd, mbVisible ? SW_SHOW : SW_HIDE);
	}
}

void CWMPView::onPlaying()
{
	cocos2d::VideoPlayer::executeVideoCallback(mIndex, cocos2d::VideoPlayer::PLAYING);

	mbStartPlaying = true;
}

void CWMPView::onPaused()
{
	cocos2d::VideoPlayer::executeVideoCallback(mIndex, cocos2d::VideoPlayer::PAUSED);
}

void CWMPView::onStopped()
{
	cocos2d::VideoPlayer::executeVideoCallback(mIndex, cocos2d::VideoPlayer::STOPPED);
}

void CWMPView::onCompleted()
{
	cocos2d::VideoPlayer::executeVideoCallback(mIndex, cocos2d::VideoPlayer::COMPLETED);
}

void CWMPView::onTerminated()
{
	cocos2d::VideoPlayer::executeVideoCallback(mIndex, cocos2d::VideoPlayer::TERMINATION);
}

void CWMPView::onQuitFullscreen()
{
	cocos2d::VideoPlayer::executeVideoCallback(mIndex, cocos2d::VideoPlayer::QUIT_FULLSCREEN);
}

void CWMPView::update(float dt)
{
	if (mDeviceId)
	{
		DWORD_PTR newStatus = getStatus();
		if (mStatus != newStatus)
		{
			mStatus = newStatus;

			switch (mStatus)
			{
			case MCI_MODE_PLAY:
				onPlaying();
				break;

			case MCI_MODE_PAUSE:
				onPaused();
				break;

			case MCI_MODE_STOP:
				onCompleted();
				break;
			}
		}
	}
}

DWORD_PTR CWMPView::getStatus()
{
	MCI_DGV_STATUS_PARMS mciStatus;
	memset(&mciStatus, 0, sizeof(mciStatus));
	mciStatus.dwItem = MCI_STATUS_MODE;
	MCIERROR err = mciSendCommand(mDeviceId, MCI_STATUS, MCI_STATUS_ITEM, (DWORD)&mciStatus);
	if (err)
	{
		TCHAR errStr[512];
		mciGetErrorString(err, errStr, 512);
		TCHAR msg[1024];
		_stprintf(msg, _T("[CWMPView::getStatus] error %s\n"), errStr);
		OutputDebugString(msg);
		return 0;
	}

	return mciStatus.dwReturn;
}