#include "platform/platform_types.h"
/********************************************************************
	filename:	CWMPView.h
	author:		Ñî´ºÀ×
	date:		2015/05/11
	desc:		
	Copyright (c) 2015 locojoy. All rights reserved.
*********************************************************************/

#ifndef _CWMPView_h
#define _CWMPView_h

#include <windows.h>

class CWMPView
{
private:
	int mIndex;

	HWND mHWnd;
	HWND mHWndInput;
	UINT mDeviceId;
	DWORD_PTR mStatus;

	int mParentWindowWidth;
	int mParentWindowHeight;

	bool mbVisible;
	bool mbStartPlaying;

public:
	CWMPView(int index);
	~CWMPView();

	bool init(HWND hParent, const RECT& rc);
	void destroy();

	void setFilename(const TCHAR* filename);
	bool isFullscreen() const;
	void setFullscreen(bool var);
	void enableContextMenu(bool var);
	void showUIButtons(bool var);
	void setStrechToFit(bool var);
	void setVideoRect(int x, int y, int width, int height);

	void play();
	void pause();
	void stop();
	void setPosition(float sec);
	void setVisible(bool var);

	void onPlaying();
	void onPaused();
	void onStopped();
	void onCompleted();
	void onTerminated();
	void onQuitFullscreen();

	void update(float dt);

private:
	DWORD_PTR getStatus();
};

#endif // _CWMPView_h