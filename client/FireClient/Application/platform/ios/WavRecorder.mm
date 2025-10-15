#import <string>
#import <Utils/WavRecorder.h>
#import <AVFoundation/AVFoundation.h>

#include "SimpleAudioEngine.h"
#include "ConfigManager.h"

AVAudioRecorder *g_audioRecorder;  //º«¬º∆˜.

static std::string s_WavTempFilePath;
static std::string s_WavTempFileName; //wav¡Ÿ ±Œƒº˛√˚◊÷.
static bool s_bRecording = false;

static WavRecorder s_wavRecorder;

WavRecorder* gGetWavRecorder()
{
	return &s_wavRecorder;
}

const char* WavRecorder::getTempFilePath()
{
    return s_WavTempFilePath.c_str();
}

const char* WavRecorder::getTempFileName()
{
    return s_WavTempFileName.c_str();
}

bool WavRecorder::isRecording()
{
	return s_bRecording;
}

bool WavRecorder::initialize(const char* tempFilePath)
{
	s_WavTempFilePath = tempFilePath;
	s_WavTempFileName = tempFilePath;
    s_WavTempFileName += "temp.caf";
	return true;
}
void WavRecorder::release()
{
	stop();
}

bool WavRecorder::canRecord()
{
    __block BOOL bCanRecord = YES;
    if ([[[UIDevice currentDevice] systemVersion] compare:@"7.0"] != NSOrderedAscending)
    {
        AVAudioSession *audioSession = [AVAudioSession sharedInstance];
        if ([audioSession respondsToSelector:@selector(requestRecordPermission:)]) {
            [audioSession performSelector:@selector(requestRecordPermission:) withObject:^(BOOL granted) {
                if (granted) {
                    bCanRecord = YES;
                } else {
                    bCanRecord = NO;
                }
            }];
        }
    }
    
    return bCanRecord;
}

bool WavRecorder::start()
{
    NSLog(@"startRecording");
	
	int nError = remove(s_WavTempFileName.c_str());

    // Init audio with record capability
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    NSError* error = nil;
    [audioSession setCategory:AVAudioSessionCategoryPlayAndRecord error: &error];
    if(audioSession == nil)
    {
        NSLog(@"Error creating session: %@", [error description]);
        return false;
    }
    else
        [audioSession setActive:YES error: &error];
    
    NSMutableDictionary *recordSettings = [[NSMutableDictionary alloc] initWithCapacity:10];

    [recordSettings setObject:[NSNumber numberWithInt: kAudioFormatLinearPCM] forKey: AVFormatIDKey];
    [recordSettings setObject:[NSNumber numberWithFloat:8000.0] forKey: AVSampleRateKey];
    [recordSettings setObject:[NSNumber numberWithInt:1] forKey:AVNumberOfChannelsKey];
    [recordSettings setObject:[NSNumber numberWithInt:16] forKey:AVLinearPCMBitDepthKey];
    [recordSettings setObject:[NSNumber numberWithBool:NO] forKey:AVLinearPCMIsBigEndianKey];
    [recordSettings setObject:[NSNumber numberWithBool:NO] forKey:AVLinearPCMIsFloatKey];

	const char* tempFilePathName = s_WavTempFileName.c_str();
    //NSURL *url = [NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/recordTest.caf", [[NSBundle mainBundle] resourcePath]]];
    NSURL *url = [NSURL fileURLWithPath:[NSString stringWithUTF8String:tempFilePathName]];
    
	gGetVoiceManager()->setForbiddenVoicePlay(true);
	CocosDenshion::SimpleAudioEngine::sharedEngine()->stopAllEffects();
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(false);
	CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();

    g_audioRecorder = [[ AVAudioRecorder alloc] initWithURL:url settings:recordSettings error:&error];
    //[g_audioRecorder peakPowerForChannel:0];
    //[g_audioRecorder setDelegate:self];
    if ( g_audioRecorder &&[g_audioRecorder prepareToRecord] == YES)
	{
        [g_audioRecorder record];
				s_bRecording=true;
    }
	else
	{
		gGetVoiceManager()->setForbiddenVoicePlay(false);
		CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(true);
		int BackMusic = gGetGameConfigManager()->GetConfigValue(L"sound");
		if (BackMusic != 0) {
			CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
		}

        int errorCode = (int)CFSwapInt32HostToBig ([error code]);
        NSLog(@"Error: %@ [%4.4s])" , [error localizedDescription], (char*)&errorCode);
				return false;
    }
    NSLog(@"recording");
		return true;
}

void WavRecorder::stop()
{
		gGetVoiceManager()->setForbiddenVoicePlay(false);
		s_bRecording = false;
		[g_audioRecorder stop];
		[g_audioRecorder release];

    g_audioRecorder = nil;
		NSLog(@"stopped");
	
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    NSError* error = nil;
    [audioSession setCategory:AVAudioSessionCategoryAmbient error: &error];
    [audioSession setActive:YES error: &error];
}

  


	
	
	
 