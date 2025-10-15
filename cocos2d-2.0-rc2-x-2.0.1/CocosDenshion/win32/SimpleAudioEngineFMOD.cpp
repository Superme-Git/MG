/****************************************************************************
Copyright (c) 2011      Laschweinski
Copyright (c) 2013-2014 Chukong Technologies Inc.

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
#ifndef OPENAL

#include "SimpleAudioEngine.h"
#include "FmodAudioPlayer.h"

namespace CocosDenshion {

static FmodAudioPlayer* oAudioPlayer;

SimpleAudioEngine::SimpleAudioEngine() {
	m_bEffectEnable = true;
	mCurEffectPriority = -1;
	oAudioPlayer = FmodAudioPlayer::sharedPlayer();
}

SimpleAudioEngine::~SimpleAudioEngine() {
}

SimpleAudioEngine* SimpleAudioEngine::sharedEngine() {
	static SimpleAudioEngine s_SharedEngine;
	return &s_SharedEngine;
}

void SimpleAudioEngine::end() {
    if(oAudioPlayer)
    {
        oAudioPlayer->close();
    }
}

//////////////////////////////////////////////////////////////////////////
// BackgroundMusic
//////////////////////////////////////////////////////////////////////////

void SimpleAudioEngine::playBackgroundMusic(const char* pszFilePath, bool bLoop) {
	oAudioPlayer->playBackgroundMusic(pszFilePath, bLoop);

	if (!m_bEffectEnable) {
		pauseBackgroundMusic();
	}
}

void SimpleAudioEngine::stopBackgroundMusic(bool bReleaseData) {
	oAudioPlayer->stopBackgroundMusic(bReleaseData);
}

void SimpleAudioEngine::pauseBackgroundMusic() {
	oAudioPlayer->pauseBackgroundMusic();
}

void SimpleAudioEngine::resumeBackgroundMusic() {
	if (!m_bEffectEnable) {
		return;
	}

	oAudioPlayer->resumeBackgroundMusic();
}

void SimpleAudioEngine::rewindBackgroundMusic() {
	oAudioPlayer->rewindBackgroundMusic();
}

bool SimpleAudioEngine::willPlayBackgroundMusic() {
	return oAudioPlayer->willPlayBackgroundMusic();
}

bool SimpleAudioEngine::isBackgroundMusicPlaying() {
	return oAudioPlayer->isBackgroundMusicPlaying();
}

void SimpleAudioEngine::preloadBackgroundMusic(const char* pszFilePath) {
	oAudioPlayer->preloadBackgroundMusic(pszFilePath);
}

//////////////////////////////////////////////////////////////////////////
// effect function
//////////////////////////////////////////////////////////////////////////

unsigned int SimpleAudioEngine::playEffect(const char* pszFilePath, bool bLoop) {
	if (!m_bEffectEnable) {
		return 0;
	}

	return oAudioPlayer->playEffect(pszFilePath, bLoop);
}

void SimpleAudioEngine::stopEffect(unsigned int nSoundId) {
	oAudioPlayer->stopEffect(nSoundId);
}

void SimpleAudioEngine::stopEffectByPath(const char* pszFilePath) {
	oAudioPlayer->stopEffectByPath(pszFilePath);
}

bool SimpleAudioEngine::getEffectIsPlaying(const char* pszFilePath) {
	return oAudioPlayer->getEffectIsPlaying(pszFilePath);
}

void SimpleAudioEngine::preloadEffect(const char* pszFilePath) {
	oAudioPlayer->preloadEffect(pszFilePath);
}

void SimpleAudioEngine::unloadEffect(const char* pszFilePath) {
	oAudioPlayer->unloadEffect(pszFilePath);
}

void SimpleAudioEngine::pauseEffect(unsigned int uSoundId) {
	oAudioPlayer->pauseEffect(uSoundId);
}

void SimpleAudioEngine::pauseAllEffects() {
	oAudioPlayer->pauseAllEffects();
}

void SimpleAudioEngine::resumeEffect(unsigned int uSoundId) {
	oAudioPlayer->resumeEffect(uSoundId);
}

void SimpleAudioEngine::setEffectVolume(unsigned int nSoundId, float fVolume)
{
	oAudioPlayer->setEffectVolume(nSoundId, fVolume);
}

bool SimpleAudioEngine::hasEffect(unsigned int nSoundId)
{
	return oAudioPlayer->hasEffect(nSoundId);
}

bool SimpleAudioEngine::isEffectPlaying(unsigned int nSoundId)
{
	return oAudioPlayer->isEffectPlaying(nSoundId);
}

void SimpleAudioEngine::resumeAllEffects() {
	oAudioPlayer->resumeAllEffects();
}

void SimpleAudioEngine::stopAllEffects() {
	oAudioPlayer->stopAllEffects();
}



//////////////////////////////////////////////////////////////////////////
// volume interface
//////////////////////////////////////////////////////////////////////////

float SimpleAudioEngine::getBackgroundMusicVolume() {
	return oAudioPlayer->getBackgroundMusicVolume();
}

void SimpleAudioEngine::setBackgroundMusicVolume(float volume) {
	oAudioPlayer->setBackgroundMusicVolume(volume);
}

float SimpleAudioEngine::getEffectsVolume() {
	return oAudioPlayer->getEffectsVolume();
}

void SimpleAudioEngine::setEffectsVolume(float volume) {
	oAudioPlayer->setEffectsVolume(volume);
}


} // end of namespace CocosDenshion

#endif
