
#ifndef __VIDEOPLAYERENGINE_H_
#define __VIDEOPLAYERENGINE_H_

#include "CCPlatformConfig.h"

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID || CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_WP8 || CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)

#include "cocos2d.h"
#include "../cocoa/CCObject.h"
#include <stdlib.h>
#include <string>
#include <map>

NS_CC_BEGIN

class CC_DLL VideoPlayer : public CCObject
{
public:
	enum SourceType
	{
		FILENAME = 0,
		URL
	};
	enum EventType
	{
		PLAYING = 0,
		PAUSED,
		STOPPED,
		COMPLETED,
		TERMINATION,
		QUIT_FULLSCREEN = 1000,
	};
	typedef void (*VideoPlayerCallback)(VideoPlayer::EventType);

public:
	VideoPlayer();
	virtual ~VideoPlayer();

	static VideoPlayer* create();

	void setFileName(const std::string& videoPath, const std::string& title, const std::string& backgroundiamge);
	const std::string& getFileName() const { return _videoURL;}
	void setURL(const std::string& _videoURL, const std::string& title, const std::string& backgroundiamge);
	const std::string& getURL() const { return _videoURL;}

	void play();
	void pause();
	void resume();
	void stop();
	void seekTo(float sec);
	bool isPlaying() const;
	void setKeepAspectRatioEnabled(bool enable);
	bool isKeepAspectRatioEnabled()const { return _keepAspectRatioEnabled;}
	void setFullScreenEnabled(bool fullscreen);
	bool isFullScreenEnabled()const;
	void setVisible(bool visible);
	void setVideoRect(float posX, float poxY, float width, float height, float scale=1.0f);

	void addEventListener(const VideoPlayer::VideoPlayerCallback& callback);
	void onPlayEvent(int event);
	void setCallback(const std::string& event, const std::string& fun);

	static void executeVideoCallback(int index,int event);
	void executeVideoCallback(int e);

	static void applicationWillEnterForeground();

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	virtual void update(float dt);
#endif
	
protected:
	bool _isPlaying;
	bool _fullScreenEnabled;
	bool _keepAspectRatioEnabled;

	std::string _videoURL;
	SourceType _videoSource;

	int _videoPlayerIndex;
	VideoPlayerCallback _eventCallback;
	static std::map<int, VideoPlayer*> s_allVideoPlayers;
	
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	void* _videoView;
#endif
	
	std::string _playingCallback;
	std::string _pausedCallback;
	std::string _stoppedCallback;
	std::string _completedCallback;
};

NS_CC_END

#endif//(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID || CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
#endif//__VIDEOPLAYERENGINE_H_
