#include "platform/VideoPlayerEngine.h"
#include "CWMPView.h"
#include <io.h>

NS_CC_BEGIN;

static int sIndex = 0;

std::map<int, VideoPlayer*> VideoPlayer::s_allVideoPlayers;

VideoPlayer::VideoPlayer()
	: _isPlaying(false)
	, _fullScreenEnabled(false)
	, _keepAspectRatioEnabled(true)

	, _videoSource(FILENAME)

	, _videoPlayerIndex(sIndex++)
	, _eventCallback(NULL)

	, _videoView(NULL)
{
	s_allVideoPlayers[_videoPlayerIndex] = this;

	CWMPView* pView = new CWMPView(_videoPlayerIndex);
	if (pView)
	{
		HWND hWnd = CCEGLView::sharedOpenGLView()->getHWnd();
		RECT rc;
		GetClientRect(hWnd, &rc);
		pView->init(hWnd, rc);
	}

	_videoView = pView;
}

VideoPlayer::~VideoPlayer()
{
	s_allVideoPlayers.erase(_videoPlayerIndex);

	CWMPView* pView = (CWMPView*)_videoView;
	if(pView)
	{
		delete pView;
	}
	_videoView = NULL;
}

VideoPlayer* VideoPlayer::create()
{
	VideoPlayer* pPlayer = new VideoPlayer;
	if(pPlayer)
	{
		pPlayer->autorelease();
	}
	else
	{
		CC_SAFE_DELETE(pPlayer);
	}

	return pPlayer;
}

void VideoPlayer::setFileName(const std::string& videoPath, const std::string& title, const std::string& backgroundiamge)
{
	_videoURL = videoPath;
	_videoSource = FILENAME;

	// win32 °æ²¥·ÅÊÓÆµÎÄ¼þ±ØÐëÓÐºó×º
	int len = _videoURL.length();
	if (_videoURL.at(len - 4) != '.')
	{
		_videoURL += ".wmv";

		if (-1 == ::_access(_videoURL.c_str(), 0))
		{
			::rename(videoPath.c_str(), _videoURL.c_str());
		}
	}

	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{ 
		return;
	}

#ifdef _UNICODE
	std::string filename = _videoURL.c_str();
	//CMethod::mbcs_to_utf8(filename, filename);
	//TCHAR* utf16 = (TCHAR*)cc_utf16_from_utf8(filename.c_str());
	TCHAR utf16[MAX_PATH] = {0};
	MultiByteToWideChar(CP_ACP, 0, filename.c_str(), filename.size(), utf16, MAX_PATH);
	pView->setFilename(utf16);
	//delete [] utf16;
#else
	pView->setFilename(_videoURL.c_str());
#endif
}

void VideoPlayer::setURL(const std::string& _videoURL, const std::string& title, const std::string& backgroundiamge)
{
	this->_videoURL = _videoURL;
	_videoSource = URL;

	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}

#ifdef _UNICODE
	std::string filename = _videoURL.c_str();
	//CMethod::mbcs_to_utf8(filename, filename);
	//TCHAR* utf16 = (TCHAR*)cc_utf16_from_utf8(filename.c_str());
	TCHAR utf16[MAX_PATH];
	MultiByteToWideChar(CP_ACP, 0, filename.c_str(), filename.size(), utf16, MAX_PATH);
	pView->setFilename(utf16);
	//delete [] utf16;
#else
	pView->setFilename(_videoURL.c_str());
#endif
}

void VideoPlayer::play()
{
	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->play();

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	cocos2d::CCDirector::sharedDirector()->SetBackgroundMode(true);  // ÔÝÍ£ opengl äÖÈ¾
	cocos2d::CCDirector::sharedDirector()->getScheduler()->scheduleUpdateForTarget(this, 0, false);
#endif
}

void VideoPlayer::pause()
{
	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->pause();
}

void VideoPlayer::resume()
{
	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->play();
}

void VideoPlayer::stop()
{
	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->stop();

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	cocos2d::CCDirector::sharedDirector()->SetBackgroundMode(false);  // »Ö¸´ opengl äÖÈ¾
	cocos2d::CCDirector::sharedDirector()->getScheduler()->unscheduleUpdateForTarget(this);
#endif
}

void VideoPlayer::seekTo(float sec)
{
	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->setPosition(sec);
}

bool VideoPlayer::isPlaying() const
{
	return _isPlaying;
}

void VideoPlayer::setKeepAspectRatioEnabled(bool enable)
{
	_keepAspectRatioEnabled = enable;

	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->setStrechToFit(_keepAspectRatioEnabled);
}

void VideoPlayer::setFullScreenEnabled(bool fullscreen)
{
	_fullScreenEnabled = fullscreen;

	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->setFullscreen(_fullScreenEnabled);
}

bool VideoPlayer::isFullScreenEnabled()const
{
	return _fullScreenEnabled;
}

void VideoPlayer::setVisible(bool visible)
{
	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->setVisible(visible);
}

void VideoPlayer::setVideoRect(float posX, float posY, float width, float height, float scale/*=1.0f*/)
{
	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}
	pView->setVideoRect(posX, posY, width, height);
}

void VideoPlayer::addEventListener(const VideoPlayer::VideoPlayerCallback& callback)
{
	_eventCallback = callback;
}

void VideoPlayer::onPlayEvent(int e)
{
	VideoPlayer::EventType videoEvent = (VideoPlayer::EventType)e;

	if (videoEvent == VideoPlayer::QUIT_FULLSCREEN)
	{
		_fullScreenEnabled = false;
	} 
	else
	{
		if (videoEvent == VideoPlayer::PLAYING) 
		{
			_isPlaying = true;
		}
		else 
		{
			_isPlaying = false;
		}

		if (_eventCallback)
		{
			_eventCallback(videoEvent);
		}
	}
}

void VideoPlayer::setCallback(const std::string& e, const std::string& fun)
{
	if (e == "playing")
	{
		_playingCallback = fun;
	}
	else if (e == "paused")
	{
		_pausedCallback = fun;
	}
	else if (e == "stopped")
	{
		_stoppedCallback = fun;
	}
	else if (e == "completed")
	{
		_completedCallback = fun;
	}
}

void VideoPlayer::executeVideoCallback(int e)
{
	onPlayEvent(e);

	CCScriptEngineProtocol* pScriptEngine = CCScriptEngineManager::sharedManager()->getScriptEngine();

	if (pScriptEngine)
	{
		switch ((VideoPlayer::EventType)e)
		{
		case VideoPlayer::PLAYING:
			if (!_playingCallback.empty())
			{
				pScriptEngine->executeString(_playingCallback.c_str());
			}
			break;
		case VideoPlayer::PAUSED:
			if (!_pausedCallback.empty())
			{
				pScriptEngine->executeString(_pausedCallback.c_str());
			}
			break;
		case VideoPlayer::STOPPED:
			if (!_stoppedCallback.empty())
			{
				pScriptEngine->executeString(_stoppedCallback.c_str());
			}
			break;
		case VideoPlayer::COMPLETED:
		case VideoPlayer::TERMINATION:
			if (!_completedCallback.empty())
			{
				pScriptEngine->executeString(_completedCallback.c_str());
			}
			break;
		default:
			break;
		}
	}
}

void VideoPlayer::executeVideoCallback(int index,int e)
{
	if (-1 == index)
	{
		std::map<int, VideoPlayer*> tmpMap = s_allVideoPlayers;
		for (std::map<int, VideoPlayer*>::iterator it = tmpMap.begin(); it != tmpMap.end(); ++it)
		{
			VideoPlayer* pVideoPlayer = it->second;
			if (pVideoPlayer)
			{
				pVideoPlayer->executeVideoCallback(e);
			}
		}
	}
	else
	{
		std::map<int, VideoPlayer*>::iterator it = s_allVideoPlayers.find(index);

		if (it != s_allVideoPlayers.end())
		{
			VideoPlayer* pVideoPlayer = it->second;
			if (pVideoPlayer)
			{
				pVideoPlayer->executeVideoCallback(e);
			}
		}
	}
}

void VideoPlayer::applicationWillEnterForeground()
{
}

void VideoPlayer::update(float dt)
{
	CWMPView* pView = (CWMPView*)_videoView;
	if (!pView)
	{
		return;
	}

	pView->update(dt);
}

NS_CC_END;