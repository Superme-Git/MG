//////////////////////////////////////////////////////////////////////////
// File:		MusicSoundVolumeMixer.h
// Author:	杨春雷
// Date:		2016/04/21
//////////////////////////////////////////////////////////////////////////

#ifndef _MusicSoundVolumeMixer_h_
#define _MusicSoundVolumeMixer_h_

#include "Singleton.hpp"
#include <set>

#include <string>

// 音乐和音效的音量混合器：当播放音效时，音乐的音量适当调低；音效播放完毕后，再恢复音乐的音量。

class MusicSoundVolumeMixer : public CSingleton < MusicSoundVolumeMixer >
{
private:
	const float MAX_FADE_TIME = 0.5f;  // 音乐的音量淡入/淡出总时间
	const float VOLUME_LOW_PERCENT = 0.3f;  // 低音量是正常音量的百分之多少

	enum STATE
	{
		STATE_VOLUME_NORMAL,
		STATE_VOLUME_DOWN,
		STATE_VOLUME_LOW,
		STATE_VOLUME_UP,
	};

	typedef std::set<std::string> StringSet;

private:
	STATE mState;
	StringSet mSounds;
	float mVolume;
	float mFadingTime;
	float mVolumeFactor;

public:
	MusicSoundVolumeMixer();

	void update(int dtMs);

	// 设置音乐的音量
	void setBackgroundMusicVolume(float val);

	// 通知有音效开始播放了
	void notifySoundStart(const std::string& pszFilePath);
	// 通知音效播放完毕了
	void notifySoundEnd(const std::string& pszFilePath);

private:
	void updateVolumeDown(float dt);
	void updateVolumeUp(float dt);
	void checkSoundIsPlaying();
};

#endif