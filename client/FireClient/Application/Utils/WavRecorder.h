#ifndef __WAVRECORDER_H__
#define __WAVRECORDER_H__ 

#include "VoiceManager.h"

class WavRecorder
{
public:
	bool initialize(const char* tempFilePath);
	void release();
	bool canRecord();
	bool start();
	void stop();
    const char* getTempFilePath();
	const char* getTempFileName();
	bool isRecording();
};

WavRecorder* gGetWavRecorder();

#endif // __WAVRECORDER_H__