//////////////////////////////////////////////////////////////////////////
// File:		MusicSoundVolumeMixer.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/04/22
//////////////////////////////////////////////////////////////////////////

#include "MusicSoundVolumeMixer.h"
#include "SimpleAudioEngine.h"
#include <string>

MusicSoundVolumeMixer::MusicSoundVolumeMixer()
	: mState(STATE_VOLUME_NORMAL)
	, mVolume(1)
	, mFadingTime(0)
	, mVolumeFactor(1)
{}

void MusicSoundVolumeMixer::update(int dtMs)
{
	float dt = dtMs * 0.001f;

	switch (mState)
	{
	case STATE_VOLUME_DOWN: updateVolumeDown(dt); break;
	case STATE_VOLUME_UP: updateVolumeUp(dt); break;
	case STATE_VOLUME_LOW: checkSoundIsPlaying(); break;
	}
}

void MusicSoundVolumeMixer::setBackgroundMusicVolume(float val)
{
	mVolume = val;

	float realVolume = mVolume * mVolumeFactor;
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setBackgroundMusicVolume(realVolume);
}

void MusicSoundVolumeMixer::notifySoundStart(const std::string& pszFilePath)
{
	mSounds.insert(pszFilePath);

	if (mState != STATE_VOLUME_LOW)
	{
		if (mState == STATE_VOLUME_NORMAL)
		{
			mVolume = CocosDenshion::SimpleAudioEngine::sharedEngine()->getBackgroundMusicVolume();
			mFadingTime = 0;
		}
		else if (mState == STATE_VOLUME_UP)
		{
			mFadingTime = MAX_FADE_TIME - mFadingTime;
		}
		mState = STATE_VOLUME_DOWN;
	}
}

void MusicSoundVolumeMixer::notifySoundEnd(const std::string& pszFilePath)
{
	mSounds.erase(pszFilePath);

	if (mSounds.empty())
	{
		if (mState != STATE_VOLUME_NORMAL)
		{
			if (mState == STATE_VOLUME_LOW)
			{
				mFadingTime = 0;
			}
			else if (mState == STATE_VOLUME_DOWN)
			{
				mFadingTime = MAX_FADE_TIME - mFadingTime;
			}
			mState = STATE_VOLUME_UP;
		}
	}
}

void MusicSoundVolumeMixer::updateVolumeDown(float dt)
{
	mFadingTime += dt;

	if (mFadingTime > MAX_FADE_TIME)
	{
		mFadingTime = MAX_FADE_TIME;
		mState = STATE_VOLUME_LOW;
	}

	float factor = mFadingTime / MAX_FADE_TIME;
	float srcVolume = mVolume;
	float dstVolume = mVolume * VOLUME_LOW_PERCENT;

	float curVolume = srcVolume + (dstVolume - srcVolume) * factor;
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setBackgroundMusicVolume(curVolume);

	mVolumeFactor = mVolume > 0 ? curVolume / mVolume : mVolumeFactor;
}

void MusicSoundVolumeMixer::updateVolumeUp(float dt)
{
	mFadingTime += dt;

	if (mFadingTime > MAX_FADE_TIME)
	{
		mFadingTime = MAX_FADE_TIME;
		mState = STATE_VOLUME_NORMAL;
	}

	float factor = mFadingTime / MAX_FADE_TIME;
	float srcVolume = mVolume * VOLUME_LOW_PERCENT;
	float dstVolume = mVolume;

	float curVolume = srcVolume + (dstVolume - srcVolume) * factor;
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setBackgroundMusicVolume(curVolume);

	mVolumeFactor = mVolume > 0 ? curVolume / mVolume : mVolumeFactor;
}

void MusicSoundVolumeMixer::checkSoundIsPlaying()
{
	StringSet sounds = mSounds;
	for (StringSet::iterator it = sounds.begin(); it != sounds.end(); ++it)
	{
		const std::string& soundFilename = *it;
		const char* szFilename = soundFilename.c_str();
		if (!CocosDenshion::SimpleAudioEngine::sharedEngine()->getEffectIsPlaying(szFilename))
		{
			notifySoundEnd(soundFilename);
		}
	}
}