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

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS

//-------------------------------------------------------------------------------------

#import <MediaPlayer/MediaPlayer.h>
#import <UIKit/UIKit.h>
#include "EAGLView.h"
//#include "CCFileUtils.h"
//#include "testsAppDelegate.h"
//#include "RootViewController.h"
//#include "VideoViewController.h"
//#include "Method.h"
//#include "LJComPlatform.h"
#include "../CocosDenshion/include/SimpleAudioEngine.h"

@interface UIVideoViewWrapperIos : NSObject

@property (strong,nonatomic) MPMoviePlayerController * moviePlayer;
@property (nonatomic,assign) UIViewController* videoViewController;
@property (nonatomic,assign) UIButton* videoButton;

- (void) rotate:(bool) landscape;
- (void) setFrame:(int) left :(int) top :(int) width :(int) height :(float) scale;
- (void) setURL:(int) videoSource :(const std::string&) videoUrl :(const std::string&) title :(const std::string&) backgroundimage;
- (void) play;
- (void) pause;
- (void) resume;
- (void) stop;
- (void) seekTo:(float) sec;
- (void) setVisible:(bool) visible;
- (void) setKeepRatioEnabled:(bool) enabled;
- (void) setFullScreenEnabled:(bool) enabled;
- (bool) isFullScreenEnabled;

-(id) init:(void*) videoPlayer;

-(void) videoFinished:(NSNotification*) notification;
-(void) playStateChange;
-(void) orientationChanged;
-(void) appEnterBackground;
-(void) appBecomeActive;

@end

@implementation UIVideoViewWrapperIos
{
    int _left;
    int _top;
    int _width;
    int _height;
	float _scale;
	float _alpha; // 按钮控件透明度
	float _alphadelta; // 按钮控件透明变化
    bool _keepRatioEnabled;
	bool _landScape;
    
	cocos2d::VideoPlayer* _videoPlayer;
}

-(id)init:(void*)videoPlayer
{
    if (self = [super init]) {
		self.moviePlayer = NULL;
		self.videoViewController = NULL;
		self.videoButton = NULL;
		_videoPlayer = (cocos2d::VideoPlayer*)videoPlayer;
        _keepRatioEnabled = true;
		_left = 0;
		_top = 0;
		_width = 0;
		_height = 0;
		_scale = 1.0f;
		_alpha = 1.0f;
		_alphadelta = 0.01f;
		
		UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
		if (UIDeviceOrientationIsPortrait(deviceOrientation))
			_landScape = false;
        else if (UIDeviceOrientationIsLandscape(deviceOrientation))
            _landScape = true;
		else
        {
//            if (bIsAscendingVersion("8.0"))
//            {
//                AppController* appDelegate = (AppController*)[UIApplication sharedApplication].delegate;
//                _landScape = appDelegate._landScape;
//            }
//            else
//            {
//                _landScape = false;
//            }
        }
        _landScape = true;
        NSLog(@"UIDeviceOrientation:%d", _landScape);
    }
    
    return self;
}

-(void) dealloc
{
    if (self.moviePlayer != NULL) {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:MPMoviePlayerPlaybackDidFinishNotification object:self.moviePlayer];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:MPMoviePlayerPlaybackStateDidChangeNotification object:self.moviePlayer];
		[[NSNotificationCenter defaultCenter] removeObserver:self name:UIDeviceOrientationDidChangeNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationDidBecomeActiveNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationDidEnterBackgroundNotification object:nil];
		// if ([[UIDevice currentDevice] isGeneratingDeviceOrientationNotifications] == TRUE)
		//	[[UIDevice currentDevice] endGeneratingDeviceOrientationNotifications];
		[UIVideoViewWrapperIos cancelPreviousPerformRequestsWithTarget:self selector:@selector(update) object:nil];
		
        [self.moviePlayer stop];
        [self.moviePlayer.view removeFromSuperview];
		self.moviePlayer = NULL;
		[self.videoViewController release];
		self.videoViewController = NULL;
		[self.videoButton release];
		self.videoButton = NULL;
        _videoPlayer = NULL;
    }
    [super dealloc];
}

-(void) rotate:(bool) landscape
{
	/* ycl 横屏游戏不需要支持旋转
	_landScape = landscape;
	if (self.moviePlayer != NULL) {
		if (_landScape == false) {
			[self.moviePlayer.view setFrame:CGRectMake(_left, _top, _width, _height)];
		}
		else {
			[self.moviePlayer.view setFrame:CGRectMake(_top, _left, _height, _width)];
		}
		[self.moviePlayer play];
	}
	if (self.videoButton != NULL) {
		
		[self.videoButton setFrame:CGRectMake(0,0,120*_scale,24*_scale)];
		
		if (_landScape == false) {
			
			[self.videoButton setCenter:CGPointMake(_width-120*_scale*0.5, _height-24)];
		}
		else {
			[self.videoButton setCenter:CGPointMake(_height-120*_scale*0.5, _width-24)];
		}
	}
	*/
}

-(void) setFrame:(int)left :(int)top :(int)width :(int)height :(float) scale
{
	NSLog(@"VideoPlayer:setFrame[l:%d][t:%d][w:%d][h:%d]", left, top, width, height);
    CGRect rc = [[UIScreen mainScreen] bounds];
    _left = rc.origin.x;
    _top = rc.origin.y;
    _width = rc.size.width;
    _height = rc.size.height;
	_scale = 1.0;
    
    if (_width > _height) {
        
        int temp = _width;
        _width = _height;
        _height = temp;
    }
    
    if (self.moviePlayer != NULL) {
		if (_landScape == false) {
			[self.moviePlayer.view setFrame:CGRectMake(_left, _top, _width, _height)];
		}
		else {
			[self.moviePlayer.view setFrame:CGRectMake(_top, _left, _height, _width)];
		}
    }
    [self.moviePlayer.view setFrame:CGRectMake(0, 0, 480, 320)];
	if (self.videoButton != NULL) {
		
		[self.videoButton setFrame:CGRectMake(0,0,120*_scale,24*_scale)];
		
		if (_landScape == false) {
			
			[self.videoButton setCenter:CGPointMake(_width-120*_scale*0.5, _height-24)];
		}
		else {
			[self.videoButton setCenter:CGPointMake(_height-120*_scale*0.5, _width-24)];
		}
	}
}

-(void) setFullScreenEnabled:(bool) enabled
{
    if (self.moviePlayer != NULL) {
		NSLog(@"VideoPlayer:setFullScreenEnabled[%d]", enabled);
        [self.moviePlayer setFullscreen:enabled animated:(true)];
    }
}

-(bool) isFullScreenEnabled
{
    if (self.moviePlayer != NULL) {
        return [self.moviePlayer isFullscreen];
    }
    
    return false;
}

-(void) setURL:(int)videoSource :(const std::string &)videoUrl :(const std::string&) title :(const std::string&) backgroundimage
{
	NSLog(@"VideoPlayer:setURL[%d][%s]", videoSource, videoUrl.c_str());
    if (self.moviePlayer != NULL) {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:MPMoviePlayerPlaybackDidFinishNotification object:self.moviePlayer];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:MPMoviePlayerPlaybackStateDidChangeNotification object:self.moviePlayer];
		[[NSNotificationCenter defaultCenter] removeObserver:self name:UIDeviceOrientationDidChangeNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationDidBecomeActiveNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationDidEnterBackgroundNotification object:nil];
		// if ([[UIDevice currentDevice] isGeneratingDeviceOrientationNotifications] == TRUE)
		//	[[UIDevice currentDevice] endGeneratingDeviceOrientationNotifications];
        
        [self.moviePlayer stop];
        [self.moviePlayer.view removeFromSuperview];
        self.moviePlayer = NULL;
    }
    
	if (videoSource == cocos2d::VideoPlayer::URL) {
        self.moviePlayer = [[MPMoviePlayerController alloc] initWithContentURL:[NSURL URLWithString:@(videoUrl.c_str())]];
        self.moviePlayer.movieSourceType = MPMovieSourceTypeStreaming;
    } else {
        NSString *path = @(videoUrl.c_str());
        self.moviePlayer = [[MPMoviePlayerController alloc] initWithContentURL:[NSURL fileURLWithPath:path]];
        self.moviePlayer.movieSourceType = MPMovieSourceTypeFile;
    }
    self.moviePlayer.allowsAirPlay = false;
    self.moviePlayer.controlStyle = MPMovieControlStyleNone;
    self.moviePlayer.view.userInteractionEnabled = false;
    
    UIColor* clearColor = [UIColor clearColor];
    self.moviePlayer.backgroundView.backgroundColor = clearColor;
    self.moviePlayer.view.backgroundColor = clearColor;
    for (UIView * subView in self.moviePlayer.view.subviews) {
        subView.backgroundColor = clearColor;
    }
    
    if (_keepRatioEnabled) {
        self.moviePlayer.scalingMode = MPMovieScalingModeAspectFit;
    } else {
        self.moviePlayer.scalingMode = MPMovieScalingModeFill;
    }
	
	// [[EAGLView sharedEGLView] addSubview: self.moviePlayer.view];
	
	if (self.videoViewController == NULL) {
		self.videoViewController = [[UIViewController alloc] initWithNibName:nil bundle:nil];
		self.videoViewController.wantsFullScreenLayout = YES;
	}
	
	//AppController* delegate = (AppController *)[UIApplication sharedApplication].delegate;
	[[UIApplication sharedApplication].keyWindow.rootViewController presentModalViewController:self.videoViewController animated:FALSE];
	[self.videoViewController.view addSubview: self.moviePlayer.view];
	
	if (self.videoButton == NULL) {
		
		_alpha = 1.0f;
		_alphadelta = 0.01f;
		
		NSString* titledesc = [NSString stringWithCString: title.c_str() encoding: NSUTF8StringEncoding];
		self.videoButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 120, 24)];
//		[self.videoButton setTitle:titledesc forState:nil];
//		[self.videoButton setTitleColor:[UIColor colorWithRed:246/255.0f green:213/255.0f blue:139/255.0f alpha:_alpha] forState:nil];
//		self.videoButton.titleLabel.font = [UIFont boldSystemFontOfSize:12];
//		self.videoButton.titleLabel.textAlignment = NSTextAlignmentCenter;
		
//		unsigned long nSize = 0;
//		unsigned char* pBuffer = cocos2d::CCFileUtils::sharedFileUtils()->getFileData(
//			cocos2d::CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(backgroundimage.c_str()), "rb", &nSize);
//		
//		NSData* data = [NSData dataWithBytes:pBuffer length:nSize];
//		[self.videoButton setBackgroundImage:[UIImage imageWithData:data] forState:nil];
		[self.videoButton addTarget:self action:@selector(videoSkip) forControlEvents:UIControlEventTouchUpInside];
//		[self performSelector:@selector(update) withObject:nil afterDelay:_alphadelta];
//		
//		CC_SAFE_DELETE_ARRAY(pBuffer);
	}
	[self.videoViewController.view addSubview: self.videoButton];
	
	if ([[UIDevice currentDevice] isGeneratingDeviceOrientationNotifications] == FALSE)
		[[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(videoFinished:) name:MPMoviePlayerPlaybackDidFinishNotification object:self.moviePlayer];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(playStateChange) name:MPMoviePlayerPlaybackStateDidChangeNotification object:self.moviePlayer];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(orientationChanged) name:UIDeviceOrientationDidChangeNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(appBecomeActive) name: UIApplicationDidBecomeActiveNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(appEnterBackground) name: UIApplicationDidEnterBackgroundNotification object:nil];
}

-(void) update
{
//	if (self.videoButton) {
//		
//		_alpha = _alpha + _alphadelta;
//		
//		if (_alpha < 0.0f) {
//			_alpha = 0.0f;
//			_alphadelta = 0.01f;
//		}
//		if (_alpha > 1.0f) {
//			_alpha = 1.0f;
//			_alphadelta = -0.01f;
//		}
//		
//		[self.videoButton setTitleColor:[UIColor colorWithRed:246/255.0f green:213/255.0f blue:139/255.0f alpha:_alpha] forState:nil];
//		[self performSelector:@selector(update) withObject:nil afterDelay:0.01];
//	}
}

-(void) videoSkip
{
    [self.videoViewController dismissModalViewControllerAnimated:FALSE];
	_videoPlayer->onPlayEvent((int)cocos2d::VideoPlayer::COMPLETED);
	
	NSLog(@"VideoPlayer: videoSkip");
}

-(void) videoFinished:(NSNotification *)notification
{
    if(_videoPlayer != NULL)
    {
        if([self.moviePlayer playbackState] != MPMoviePlaybackStateStopped)
        {
            [self.videoViewController dismissModalViewControllerAnimated:FALSE];
			_videoPlayer->onPlayEvent((int)cocos2d::VideoPlayer::COMPLETED);
			
			NSLog(@"VideoPlayer: MPMoviePlayerPlaybackDidFinishNotification");
        }
    }
}

-(void) playStateChange
{
    MPMoviePlaybackState state = [self.moviePlayer playbackState];
    switch (state) {
        case MPMoviePlaybackStatePaused:
			NSLog(@"VideoPlayer: MPMoviePlayerPlaybackStateDidChangeNotification MPMoviePlaybackStatePaused");
			_videoPlayer->onPlayEvent((int)cocos2d::VideoPlayer::PAUSED);
            break;
        case MPMoviePlaybackStateStopped:
			NSLog(@"VideoPlayer: MPMoviePlayerPlaybackStateDidChangeNotification MPMoviePlaybackStateStopped");
            _videoPlayer->onPlayEvent((int)cocos2d::VideoPlayer::STOPPED);
            break;
        case MPMoviePlaybackStatePlaying:
			NSLog(@"VideoPlayer: MPMoviePlayerPlaybackStateDidChangeNotification MPMoviePlaybackStatePlaying");
            _videoPlayer->onPlayEvent((int)cocos2d::VideoPlayer::PLAYING);
            CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
            break;
        case MPMoviePlaybackStateInterrupted:
            NSLog(@"VideoPlayer: MPMoviePlayerPlaybackStateDidChangeNotification MPMoviePlaybackStateInterrupted");

            break;
        case MPMoviePlaybackStateSeekingBackward:
            break;
        case MPMoviePlaybackStateSeekingForward:
            break;
        default:
            NSLog(@"VideoPlayer: MPMoviePlayerPlaybackStateDidChangeNotification other");
            break;
    }
}

-(void) orientationChanged
{
	UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
	
	if (UIDeviceOrientationIsLandscape(deviceOrientation))
	{
		[self rotate:true];
	}
	else if (UIDeviceOrientationIsPortrait(deviceOrientation))
	{
		[self rotate:false];
	}
}

-(void) appBecomeActive
{
    NSLog(@"VideoPlayer:appBecomeActive");
    
    [self resume];
}

-(void) appEnterBackground
{
    NSLog(@"VideoPlayer:appEnterBackground");
    
    [self pause];
}

-(void) seekTo:(float)sec
{
    if (self.moviePlayer != NULL) {
		NSLog(@"VideoPlayer:seekTo[%f]", sec);
        [self.moviePlayer setCurrentPlaybackTime:(sec)];
    }
}

-(void) setVisible:(bool)visible
{
    if (self.moviePlayer != NULL) {
		NSLog(@"VideoPlayer:visible");
        [self.moviePlayer.view setHidden:!visible];
    }
}

-(void) setKeepRatioEnabled:(bool)enabled
{
	NSLog(@"VideoPlayer:setKeepRatioEnabled[%d]", enabled);
    _keepRatioEnabled = enabled;
    if (self.moviePlayer != NULL) {
        if (enabled) {
            self.moviePlayer.scalingMode = MPMovieScalingModeAspectFit;
        } else {
            self.moviePlayer.scalingMode = MPMovieScalingModeFill;
        }
    }
}

-(void) play
{
    if (self.moviePlayer != NULL) {
		NSLog(@"VideoPlayer:play");
		if (_landScape == false) {
			[self.moviePlayer.view setFrame:CGRectMake(_left, _top, _width, _height)];
		}
		else {
			[self.moviePlayer.view setFrame:CGRectMake(_top, _left, _height, _width)];
		}
		if (self.videoButton != NULL) {
			
            if(_landScape == false)
            {
                [self.videoButton setFrame:CGRectMake(_left, _top, _width, _height)];
            }
            else
            {
                [self.videoButton setFrame:CGRectMake(_left, _top, _height, _width)];
            }
			
//			if (_landScape == false) {
//				
//				[self.videoButton setCenter:CGPointMake(_width-120*_scale*0.5, _height-24)];
//			}
//			else {
//				[self.videoButton setCenter:CGPointMake(_height-120*_scale*0.5, _width-24)];
//			}
		}
        [self.moviePlayer play];
    }
}

-(void) pause
{
    if (self.moviePlayer != NULL) {
		NSLog(@"VideoPlayer:pause");
		[self.moviePlayer pause];
    }
}

-(void) resume
{
    if (self.moviePlayer != NULL) {
		NSLog(@"VideoPlayer:resume");
        if([self.moviePlayer playbackState] == MPMoviePlaybackStatePaused)
        {
            [self.moviePlayer play];
        }
    }
}

-(void) stop
{
    if (self.moviePlayer != NULL) {
		
		NSLog(@"VideoPlayer:stop");
        [self.moviePlayer stop];
    }
}

+(NSString*) fullPathFromRelativePath:(NSString*) relPath
{
	std::string tempPath = [relPath UTF8String];
    return [NSString stringWithCString: cocos2d::CCFileUtils::sharedFileUtils()->fullPathFromRelativePath(tempPath.c_str()) encoding: [NSString defaultCStringEncoding]];
}
@end
//------------------------------------------------------------------------------------------------------------

NS_CC_BEGIN

VideoPlayer::VideoPlayer()
: _videoPlayerIndex(-1)
, _eventCallback(NULL)
, _fullScreenEnabled(false)
, _keepAspectRatioEnabled(false)
, _isPlaying(false)
{
    _videoView = [[UIVideoViewWrapperIos alloc] init:this];

#if CC_VIDEOPLAYER_DEBUG_DRAW
    _debugDrawNode = DrawNode::create();
    addChild(_debugDrawNode);
#endif
}

VideoPlayer::~VideoPlayer()
{
    if(_videoView)
    {
        [((UIVideoViewWrapperIos*)_videoView) release];
    }
}

VideoPlayer* VideoPlayer::create()
{
	VideoPlayer* pRet = new VideoPlayer();
	if (pRet)
	{
		pRet->autorelease();
		return pRet;
	}
	return NULL;
}

void VideoPlayer::setFileName(const std::string& fileName, const std::string& title, const std::string& backgroundiamge)
{
    bool bInvalidExt = false;
    size_t lastDotPos = fileName.find_last_of(".");
    if(lastDotPos == std::string::npos)
    {
        bInvalidExt = true;
    }
    else
    {
        std::string extStr = fileName.substr(lastDotPos);
        if(extStr != ".mp4")
        {
            bInvalidExt = true;
        }
    }
    
    if(bInvalidExt)
    {
        std::string tmpFilename;
        size_t lastSplashPos = fileName.find_last_of("/");
        if(lastSplashPos != std::string::npos)
        {
            tmpFilename = fileName.substr(lastSplashPos + 1);
        }
        if(tmpFilename.empty())
        {
            tmpFilename = "TempVideo";
        }
        
        std::string tempFileFullPath = CCFileUtils::sharedFileUtils()->getCachePath() + tmpFilename + ".mp4";
        
        bool bTempFileAlreadyExist = false;
        FILE* pFile = fopen(tempFileFullPath.c_str(), "rb");
        if(pFile)
        {
            fclose(pFile);
            pFile = NULL;
            
            bTempFileAlreadyExist = true;
        }
        
        if(!bTempFileAlreadyExist)
        {
            unsigned long fileSize = 0;
            unsigned char* pFileData = CCFileUtils::sharedFileUtils()->getFileData(fileName.c_str(), "rb", &fileSize);
            if(pFileData && fileSize > 0)
            {
                FILE* pFile = fopen(tempFileFullPath.c_str(), "wb");
                if(pFile)
                {
                    fwrite(pFileData, fileSize, 1, pFile);
                    
                    fclose(pFile);
                    pFile = NULL;
                }
                CC_SAFE_DELETE_ARRAY(pFileData);
            }
        }
        
        _videoURL = tempFileFullPath;
    }
    else
    {
        _videoURL = fileName;
    }
    
    _videoSource = VideoPlayer::FILENAME;
	
    std::string utf8_str = title;
//	CMethod::mbcs_to_utf8(title.c_str(), utf8_str);
	
	[((UIVideoViewWrapperIos*)_videoView) setURL:(int)_videoSource :_videoURL :utf8_str.c_str() :backgroundiamge];
}

void VideoPlayer::setURL(const std::string& videoUrl, const std::string& title, const std::string& backgroundiamge)
{
    _videoURL = videoUrl;
    _videoSource = VideoPlayer::URL;
	
    std::string utf8_str = title;
//	CMethod::mbcs_to_utf8(title.c_str(), utf8_str);
	
    [((UIVideoViewWrapperIos*)_videoView) setURL:(int)_videoSource :_videoURL :utf8_str.c_str() :backgroundiamge];
}

void VideoPlayer::setVideoRect(float posX, float poxY, float width, float height, float scale)
{
	[((UIVideoViewWrapperIos*)_videoView) setFrame :posX :poxY :width :height :scale];
}

bool VideoPlayer::isFullScreenEnabled()const
{
    return [((UIVideoViewWrapperIos*)_videoView) isFullScreenEnabled];
}

void VideoPlayer::setFullScreenEnabled(bool enabled)
{
    [((UIVideoViewWrapperIos*)_videoView) setFullScreenEnabled:enabled];
}

void VideoPlayer::setKeepAspectRatioEnabled(bool enable)
{
    if (_keepAspectRatioEnabled != enable)
    {
        _keepAspectRatioEnabled = enable;
        [((UIVideoViewWrapperIos*)_videoView) setKeepRatioEnabled:enable];
    }
}

void VideoPlayer::play()
{
    if (! _videoURL.empty())
    {
        [((UIVideoViewWrapperIos*)_videoView) play];
    }
}

void VideoPlayer::pause()
{
    if (! _videoURL.empty())
    {
        [((UIVideoViewWrapperIos*)_videoView) pause];
    }
}

void VideoPlayer::resume()
{
    if (! _videoURL.empty())
    {
        [((UIVideoViewWrapperIos*)_videoView) resume];
    }
}

void VideoPlayer::stop()
{
    if (! _videoURL.empty())
    {
        [((UIVideoViewWrapperIos*)_videoView) stop];
    }
}

void VideoPlayer::seekTo(float sec)
{
    if (! _videoURL.empty())
    {
        [((UIVideoViewWrapperIos*)_videoView) seekTo:sec];
    }
}

bool VideoPlayer::isPlaying() const
{
    return _isPlaying;
}

void VideoPlayer::setVisible(bool visible)
{
    if (! _videoURL.empty())
    {
        [((UIVideoViewWrapperIos*)_videoView) setVisible:visible];
    }
}

void VideoPlayer::addEventListener(const VideoPlayer::VideoPlayerCallback& callback)
{
    _eventCallback = callback;
}

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

void VideoPlayer::onPlayEvent(int event)
{
    if (event == (int)VideoPlayer::PLAYING)
	{
        _isPlaying = true;
    }
	else
	{
        _isPlaying = false;
    }
    
    if (_eventCallback)
    {
        _eventCallback((VideoPlayer::EventType)event);
    }
	
	CCScriptEngineProtocol* pScriptEngine = CCScriptEngineManager::sharedManager()->getScriptEngine();
	
	if (pScriptEngine)
	{
		switch ((VideoPlayer::EventType)event)
		{
			case VideoPlayer::PLAYING:
				if (false == _playingCallback.empty())
				{
					pScriptEngine->executeString(_playingCallback.c_str());
				}
				break;
			case VideoPlayer::PAUSED:
				if (false == _pausedCallback.empty())
				{
					pScriptEngine->executeString(_pausedCallback.c_str());
				}
				break;
			case VideoPlayer::STOPPED:
				if (false == _stoppedCallback.empty())
				{
					pScriptEngine->executeString(_stoppedCallback.c_str());
				}
				break;
			case VideoPlayer::COMPLETED:
				if (false == _completedCallback.empty())
				{
					pScriptEngine->executeString(_completedCallback.c_str());
				}
				break;
			default:
				break;
		}
	}
}

std::map<int, VideoPlayer*> VideoPlayer::s_allVideoPlayers;

void VideoPlayer::executeVideoCallback(int index,int event)
{}

void VideoPlayer::applicationWillEnterForeground()
{
}

NS_CC_END

#endif
