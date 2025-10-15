/****************************************************************************
 Copyright (c) 2014 Chukong Technologies Inc.

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/

#include "VideoPlayerEngine.h"

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include "jni/JniHelper.h"
#include "jni/VideoEngineJni.h"
#include "CCDirector.h"

NS_CC_BEGIN
//--------------------------------------------------------
std::map<int, VideoPlayer*> VideoPlayer::s_allVideoPlayers;
//--------------------------------------------------------
VideoPlayer::VideoPlayer()
: _videoPlayerIndex(-1)
, _eventCallback(NULL)
, _isPlaying(false)
, _fullScreenEnabled(false)
, _keepAspectRatioEnabled(false)
{
	createVideoActivityJni();
    _videoPlayerIndex = createVideoWidgetJNI();
    s_allVideoPlayers[_videoPlayerIndex] = this;
}
//--------------------------------------------------------
VideoPlayer::~VideoPlayer()
{
    s_allVideoPlayers.erase(_videoPlayerIndex);
    removeVideoWidgetJNI(_videoPlayerIndex);
	destroyVideoActivityJni();
}
//--------------------------------------------------------
VideoPlayer* VideoPlayer::create()
{
	VideoPlayer* pRet = new VideoPlayer();
	if (pRet)
	{
		pRet->autorelease();
		return pRet;
	}
	else
	{
		delete pRet;
		pRet = NULL;
		return NULL;
	}
}
//--------------------------------------------------------
void VideoPlayer::setFileName(const std::string& fileName, const std::string& title, const std::string& backgroundiamge)
{
    _videoURL = (fileName);
    _videoSource = VideoPlayer::FILENAME;
    setVideoURLJNI(_videoPlayerIndex, (int)VideoPlayer::FILENAME,_videoURL.c_str());
}
//--------------------------------------------------------
void VideoPlayer::setURL(const std::string& videoUrl, const std::string& title, const std::string& backgroundiamge)
{
    _videoURL = videoUrl;
    _videoSource = VideoPlayer::URL;
    setVideoURLJNI(_videoPlayerIndex,(int)VideoPlayer::URL,_videoURL.c_str());
}

//--------------------------------------------------------
void VideoPlayer::play()
{
	if (! _videoURL.empty())
	{
		startVideoJNI(_videoPlayerIndex);
	}
}
//--------------------------------------------------------
void VideoPlayer::pause()
{
	if (! _videoURL.empty())
	{
		pauseVideoJNI(_videoPlayerIndex);
	}
}
//--------------------------------------------------------
void VideoPlayer::resume()
{
	if (! _videoURL.empty())
	{
		resumeVideoJNI(_videoPlayerIndex);
	}
}
//--------------------------------------------------------
void VideoPlayer::stop()
{
	if (! _videoURL.empty())
	{
		stopVideoJNI(_videoPlayerIndex);
	}
}
//--------------------------------------------------------
void VideoPlayer::seekTo(float sec)
{
	if (! _videoURL.empty())
	{
		seekVideoToJNI(_videoPlayerIndex,int(sec * 1000));
	}
}
//--------------------------------------------------------
bool VideoPlayer::isPlaying() const
{
	return _isPlaying;
}
//--------------------------------------------------------
void VideoPlayer::setKeepAspectRatioEnabled(bool enable)
{
	if (_keepAspectRatioEnabled != enable)
	{
		_keepAspectRatioEnabled = enable;
		setVideoKeepRatioEnabledJni(_videoPlayerIndex,enable);
	}
}
//--------------------------------------------------------
void VideoPlayer::setFullScreenEnabled(bool enabled)
{
	if (_fullScreenEnabled != enabled)
	{
		_fullScreenEnabled = enabled;

		const CCSize& frameSize = CCDirector::sharedDirector()->getOpenGLView()->getFrameSize();
		setFullScreenEnabledJni(_videoPlayerIndex, enabled, frameSize.width, frameSize.height);
	}
}
//--------------------------------------------------------
bool VideoPlayer::isFullScreenEnabled()const
{
	return _fullScreenEnabled;
}
//--------------------------------------------------------
void VideoPlayer::setVisible(bool visible)
{
	if (! _videoURL.empty())
	{
		setVideoVisibleJni(_videoPlayerIndex,visible);
	}
}
//--------------------------------------------------------
void VideoPlayer::setVideoRect(float posX, float posY, float width, float height, float scale)
{
	setVideoRectJNI(_videoPlayerIndex, posX, posY, width, height);
}
//--------------------------------------------------------
void VideoPlayer::addEventListener(const VideoPlayer::VideoPlayerCallback& callback)
{
    _eventCallback = callback;
}
//--------------------------------------------------------
void VideoPlayer::setCallback(const std::string& event, const std::string& fun)
{
	if (event == "playing")
	{
		_playingCallback = fun;
	}
	else if (event == "paused")
	{
		_pausedCallback = fun;
	}
	else if (event == "stopped")
	{
		_stoppedCallback = fun;
	}
	else if (event == "completed")
	{
		_completedCallback = fun;
	}
}
//--------------------------------------------------------
void VideoPlayer::onPlayEvent(int event)
{
	VideoPlayer::EventType videoEvent = (VideoPlayer::EventType)event;

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
//--------------------------------------------------------
void VideoPlayer::executeVideoCallback(int index, int event)
{
    std::map<int, VideoPlayer*>::iterator it = s_allVideoPlayers.find(index);

    if (it != s_allVideoPlayers.end())
    {
        s_allVideoPlayers[index]->onPlayEvent(event);
    }
	
	CCScriptEngineProtocol* pScriptEngine = CCScriptEngineManager::sharedManager()->getScriptEngine();
	
	if (pScriptEngine)
	{
		switch ((VideoPlayer::EventType)event)
		{
			case VideoPlayer::PLAYING:
				if (false == s_allVideoPlayers[index]->_playingCallback.empty())
				{
					pScriptEngine->executeString(s_allVideoPlayers[index]->_playingCallback.c_str());
				}
				break;
			case VideoPlayer::PAUSED:
				if (false == s_allVideoPlayers[index]->_pausedCallback.empty())
				{
					pScriptEngine->executeString(s_allVideoPlayers[index]->_pausedCallback.c_str());
				}
				break;
			case VideoPlayer::STOPPED:
			if (false == s_allVideoPlayers[index]->_stoppedCallback.empty())
				{
					pScriptEngine->executeString(s_allVideoPlayers[index]->_stoppedCallback.c_str());
				}
				break;
			case VideoPlayer::COMPLETED:
			case VideoPlayer::TERMINATION:
				if (false == s_allVideoPlayers[index]->_completedCallback.empty())
				{
					pScriptEngine->executeString(s_allVideoPlayers[index]->_completedCallback.c_str());
				}
				break;
			default:
				break;
		}
	}
}

void VideoPlayer::applicationWillEnterForeground()
{
}

NS_CC_END

#endif
