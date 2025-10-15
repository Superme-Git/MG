//  FireClientAppDelegate.m
//  FireClient

//#include "../Application/Framework/GameApplication.h"

#import "FireClientAppDelegate.h"
#include <EAGLView.h>
#import <OpenGLES/ES2/gl.h>
#import <OpenGLES/ES2/glext.h>
//#import <libkern/OSMemoryNotification.h> //comment by liugeng. not found OSMemoryNotification.h under libkern
//#import "Crittercism.h"
#include "cocos2d.h"
#import "Flurry.h"
#include "log/CoreLog.h"
#include "IViewCtrl.h"

#include "ConfigManager.h"
#include "SimpleAudioEngine.h"
#include "Framework/3rdplatform/ChannelManager.h"
#include "utils/FileUtil.h"
#include "GlobalFunction.h"
#include "utils/StringUtil.h"
#include "GameApplication.h"
#include "LoginManager.h"
#include "utils/UpdateUtil.h"
#import <AVFoundation/AVFoundation.h>

#ifdef LOCALNOTIFY_ENABLE
#import "LocalNotify.h"
#endif

//-------------------------------------------------------[[
#import <KSCrash/KSCrash.h>
// Include to use the standard reporter.
#import <KSCrash/KSCrashInstallationStandard.h>
// Include to use Quincy or Hockey.
#import <KSCrash/KSCrashInstallationQuincyHockey.h>
// Include to use the email reporter.
#import <KSCrash/KSCrashInstallationEmail.h>
// Include to use Victory.
#import <KSCrash/KSCrashInstallationVictory.h>
//-------------------------------------------------------]]

#ifdef _LOCOJOY_SDK_
#import "PlatformSDK.h"
#import "GameSdk.h"
#import "ljstatistics.h"
#import "ParameterSDK.h"
#endif

//＝＝＝＝＝＝＝＝＝＝ShareSDK头文件＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
#import <ShareSDK/ShareSDK.h>
#import <ShareSDKConnector/ShareSDKConnector.h>
//以下是ShareSDK必须添加的依赖库：
//1、libicucore.dylib
//2、libz.dylib
//3、libstdc++.dylib
//4、JavaScriptCore.framework
//＝＝＝＝＝＝＝＝＝＝以下是各个平台SDK的头文件，根据需要继承的平台添加＝＝＝
//腾讯开放平台（对应QQ和QQ空间）SDK头文件
#import <TencentOpenAPI/TencentOAuth.h>
#import <TencentOpenAPI/QQApiInterface.h>
//以下是腾讯SDK的依赖库：
//libsqlite3.dylib

//微信SDK头文件
#import "WXApi.h"
//以下是微信SDK的依赖库：
//libsqlite3.dylib

//新浪微博SDK头文件
#import "WeiboSDK.h"
//新浪微博SDK需要在项目Build Settings中的Other Linker Flags添加"-ObjC"
//以下是新浪微博SDK的依赖库：
//ImageIO.framework
//libsqlite3.dylib
//AdSupport.framework



//＝＝＝＝＝＝＝＝＝＝美洽SDK头文件＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
#import "MQChatViewManager.h"
#import "MQChatDeviceUtil.h"
#import <MeiQiaSDK/MQManager.h>


#import <AVFoundation/AVFoundation.h>
#import <Chartboost/Chartboost.h>


extern const char* GetQuincyServerAddress();

//#include "FileUtil.h"

static bool s_ApplicationIsRuning = false;
using namespace cocos2d;


extern bool gRunGameApplication();
extern void gGCNow(int level);

extern void gSetBackgroundMode(bool bBackgroundMode);
extern void gRefuseAppBrightness(bool bBackgroundMode);

FireClientAppDelegate* pAd = NULL;
void StopBackgroundTask()
{
    if (pAd) {
        [pAd StopKCBackgroundTask];
    }
}

FireClientAppDelegate* g_AppDelegate;
FireClientViewController * g_viewController;

@implementation FireClientAppDelegate


@synthesize window = _window;
@synthesize viewController = _viewController;
@synthesize updateengineViewController = _updateengineViewController;
@synthesize taskID = _taskID;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{

	AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    NSError* error = nil;
    if (![audioSession isOtherAudioPlaying]) { //避免从别的应用切换过来后声音变大
        [audioSession setCategory:AVAudioSessionCategoryPlayback error: &error];
        [audioSession setActive:YES error: &error];
    }
    [audioSession setCategory:AVAudioSessionCategoryAmbient error: &error];
    [audioSession setActive:YES error: &error];
    
    //yangbin---处理ios8以上不能接受本地推送的问题
    float ver = [[UIDevice currentDevice]systemVersion].floatValue;
    if (ver >= 8.0) {
        UIUserNotificationType type = UIUserNotificationTypeAlert | UIUserNotificationTypeBadge | UIUserNotificationTypeSound;
        UIUserNotificationSettings* settings = [UIUserNotificationSettings settingsForTypes:type categories:0];
        [[UIApplication sharedApplication]registerUserNotificationSettings:settings];
    }
    
    //yangbin 是否打开录像功能
    if (ver >= 9.0 && [[RPScreenRecorder sharedRecorder] isAvailable]){
        gGetGameApplication()->SetRecordEnable(true);
    }
    else{
        gGetGameApplication()->SetRecordEnable(false);
    }
    
    std::string dst = CFileUtil::GetCacheDir() + "/res";
    if(!IsDirectoryExisting(SHARE_String2Wstring(dst).c_str()) )
    {
        core::Logger::isfirstInstall = true;
    }
    
    //[self flurryStartSession];
    
    MT3::ChannelManager::GetPlatformLoginSuffix();
    
#ifdef LOCALNOTIFY_ENABLE
    application.applicationIconBadgeNumber = 0;
#endif
    
    g_AppDelegate = pAd = self;
    
    application.applicationIconBadgeNumber = 0;
    self.taskID = UIBackgroundTaskInvalid;
    EAGLView *__glView = [EAGLView viewWithFrame: [self.window bounds]
                                     pixelFormat: kEAGLColorFormatRGBA8
                                     depthFormat: GL_DEPTH_COMPONENT16
                              preserveBackbuffer: NO
                                      sharegroup: nil
                                   multiSampling: NO
                                 numberOfSamples: 0 ];
    
    //    __glView.transform = CGAffineTransformMakeScale(0.5, 0.5);
    // Use RootViewController manage EAGLView
    self.viewController = [[FireClientViewController alloc] init];
    self.viewController.wantsFullScreenLayout = YES;
    self.viewController.view = __glView;
    
    if ([self.viewController respondsToSelector:@selector(traitCollection)] &&
        [self.viewController.traitCollection respondsToSelector:@selector(forceTouchCapability)] &&
        self.viewController.traitCollection.forceTouchCapability == UIForceTouchCapabilityAvailable) {
        __glView.forceTouchEnable = true;
    }
    
    //[self.viewController.view addSubview:__glView];
    
    // Set RootViewController to window
    //[self.window addSubview: self.viewController.view];
    
    //    [self.window.rootViewController willRotateToInterfaceOrientation:UIDeviceOrientationLandscapeRight duration:0.5];
    
#ifdef UPDATE_ENGINE_ENABLE
    self.updateengineViewController = Nil;
    
    if (self.updateengineViewController == Nil) {
        self.updateengineViewController = [[[UpdateEngineViewController alloc] initWithNibName:@"UpdateView_iPad" bundle:nil] autorelease];
    }
    [self.window setRootViewController:self.updateengineViewController];
#else
    [self.window setRootViewController:self.viewController];
#endif
    
    [self.window makeKeyAndVisible];
    
    if (ver >= 7.0) {
        [self.viewController prefersStatusBarHidden];
    } else {
        [[UIApplication sharedApplication] setStatusBarHidden: YES];
    }
    
    //不自动锁屏：ＹＥＳ。
    [UIApplication sharedApplication].idleTimerDisabled = YES;
    g_viewController = self.viewController;
    
    //yangbin let ios could MultipleTouch;
    [g_viewController.view setMultipleTouchEnabled:YES];
    
    
#ifndef UPDATE_ENGINE_ENABLE
    gRunGameApplication();
#endif
    
    //KS CrashReport
    [self installCrashHandler];
    
#ifdef _LOCOJOY_SDK_	
	NSString* channelid = [[[NSBundle mainBundle] infoDictionary] valueForKey:@"CHANNEL_ID"];
    
    UpdateJson::NewInstance()->SetChannelNameInt(channelid.intValue);

    [[PlatformSDK sharedInstance] setViewController:self.viewController];
    [[LJStatistics getInstance] initGameID:GAME_ID channel:channelid logKey:@"1"];
    
#endif
    
    /**
     *  设置ShareSDK的appKey，如果尚未在ShareSDK官网注册过App，请移步到http://mob.com/login 登录后台进行应用注册，
     *  在将生成的AppKey传入到此方法中。我们Demo提供的appKey为内部测试使用，可能会修改配置信息，请不要使用。
     *  方法中的第二个参数用于指定要使用哪些社交平台，以数组形式传入。第三个参数为需要连接社交平台SDK时触发，
     *  在此事件中写入连接代码。第四个参数则为配置本地社交平台时触发，根据返回的平台类型来配置平台信息。
     *  如果您使用的时服务端托管平台信息时，第二、四项参数可以传入nil，第三项参数则根据服务端托管平台来决定要连接的社交SDK。
     */
    NSString * shareAppID = @"12c7d2470d237";
    NSString * wxfAppID = @"wxf371ae989c3b889a";
    NSString * wxfAppSercet = @"c38ce62ab2f5ce4a47df3d92cafbcffd";
    if([[[[NSBundle mainBundle]infoDictionary]objectForKey:@"IS_CARD_SERVER"] boolValue])
    {
        shareAppID = @"14b1062353e2b";
        wxfAppID = @"wx9ae4a3a2c9a54855";
        wxfAppSercet = @"beb99f687a1cd487610d7bfc7b29c307";
    }
    [ShareSDK registerApp:shareAppID
          activePlatforms:@[
                            @(SSDKPlatformTypeSinaWeibo),
                            @(SSDKPlatformTypeWechat),
                            @(SSDKPlatformTypeQQ),
                            ]
                 onImport:^(SSDKPlatformType platformType) {
                     
                     switch (platformType)
                     {
                         case SSDKPlatformTypeWechat:
                             //[ShareSDKConnector connectWeChat:[WXApi class]];
                             [ShareSDKConnector connectWeChat:[WXApi class] delegate:self];
                             break;
                         case SSDKPlatformTypeQQ:
                             [ShareSDKConnector connectQQ:[QQApiInterface class]
                                        tencentOAuthClass:[TencentOAuth class]];
                             break;
                         case SSDKPlatformTypeSinaWeibo:
                             [ShareSDKConnector connectWeibo:[WeiboSDK class]];
                             break;
                         default:
                             break;
                     }
                 }
          onConfiguration:^(SSDKPlatformType platformType, NSMutableDictionary *appInfo) {
              
              switch (platformType)
              {
                  case SSDKPlatformTypeSinaWeibo://正式
                      //设置新浪微博应用信息,其中authType设置为使用SSO＋Web形式授权
                      [appInfo SSDKSetupSinaWeiboByAppKey:@"858896314"
                                                appSecret:@"958bce67da12e2a53773f980577c8683"
                                              redirectUri:@"http://www.sharesdk.cn"
                                                 authType:SSDKAuthTypeBoth];
                      break;
                  case SSDKPlatformTypeWechat://正式
                      [appInfo SSDKSetupWeChatByAppId:wxfAppID
                                            appSecret:wxfAppSercet];
                      break;
                  case SSDKPlatformTypeQQ:
                      [appInfo SSDKSetupQQByAppId:@"100371282"
                                           appKey:@"aed9b0303e3ed1e27bae87c33761161d"
                                         authType:SSDKAuthTypeBoth];
                      break;
                  default:
                      break;
              }
          }];
    
    
    // ========================美洽SDK注册===================================
    NSString * meiQiaAppKey = @"1c4e54a7fc4f8df1de294af2c3e6c413";
    [MQManager initWithAppkey:meiQiaAppKey completion:^(NSString *clientId, NSError *error) {
        if (!error) {
            NSLog(@"美洽 SDK：初始化成功");
        }
    }];

    UIApplicationShortcutItem* shortcutItem = nil;
    @try
    {
        float iosVersion = [[[UIDevice currentDevice] systemVersion] floatValue];
        if(iosVersion >= 9)
        {
            shortcutItem = [launchOptions objectForKey:UIApplicationLaunchOptionsShortcutItemKey];
        }
    }
    @catch(NSException* err)
    {
    }
    
    if(shortcutItem != nil)
    {
        [self handleShortcutItem: shortcutItem];
        return NO;  // tell ios not to call performActionForShortcutItem
    }
    
    return YES;   
}

- (void)application:(UIApplication*)application performActionForShortcutItem:(nonnull UIApplicationShortcutItem *)shortcutItem completionHandler:(nonnull void (^)(BOOL))completionHandler
{
    completionHandler([self handleShortcutItem:shortcutItem]);
}

-(bool) handleShortcutItem: (UIApplicationShortcutItem*) shortcutItem
{
    bool bRet = false;
    
    eShortcutItem esi = eShortcutItem_None;
    if([shortcutItem.type isEqualToString: @"1"])
    {
        esi = eShortcutItem_ViewStall;
        bRet = true;
    }
    else if([shortcutItem.type isEqualToString: @"2"])
    {
        esi = eShortcutItem_FriendChat;
        bRet = true;
    }
    else if([shortcutItem.type isEqualToString: @"3"])
    {
        esi = eShortcutItem_ActivityCalendar;
        bRet = true;
    }
    
    LoginManager::NewInstance()->setShortcutItemLaunchedBy(esi);
    
    return bRet;
}

// ======================================================================
#pragma mark - Basic Crash Handling -
// ======================================================================

- (void) installCrashHandler
{
    // Create an installation (choose one)
    //    KSCrashInstallation* installation = [self makeStandardInstallation];
    //    KSCrashInstallation* installation = [self makeEmailInstallation];
    //    KSCrashInstallation* installation = [self makeHockeyInstallation];
    KSCrashInstallation* installation = [self makeQuincyInstallation];
    //    KSCrashInstallation *installation = [self makeVictoryInstallation];
    
    
    // Install the crash handler. This should be done as early as possible.
    // This will record any crashes that occur, but it doesn't automatically send them.
    [installation install];
    //    [KSCrash sharedInstance].deleteBehaviorAfterSendAll = KSCDeleteNever; // TODO: Remove this
    
    
    // Send all outstanding reports. You can do this any time; it doesn't need
    // to happen right as the app launches. Advanced-Example shows how to defer
    // displaying the main view controller until crash reporting completes.
    [installation sendAllReportsWithCompletion:^(NSArray* reports, BOOL completed, NSError* error)
     {
         if(completed)
         {
             NSLog(@"Sent %d reports", (int)[reports count]);
         }
         else
         {
             NSLog(@"Failed to send reports: %@", error);
         }
     }];
}

- (KSCrashInstallation*) makeQuincyInstallation
{
    NSString* QuincyServerAddress=[NSString stringWithUTF8String: GetQuincyServerAddress()];
    NSURL* quincyURL = [NSURL URLWithString:QuincyServerAddress];//@"http://120.26.89.140/server/crash_v300.php"
    
    KSCrashInstallationQuincy* quincy = [KSCrashInstallationQuincy sharedInstance];
    quincy.url = quincyURL;
    quincy.userID = @"mt3";
    quincy.contactEmail = @"mt3@locojoy.com";
    quincy.crashDescription = @"Crash!";
    
    return quincy;
}

void uncaughtExceptionHandler(NSException *exception)
{
    [Flurry logError:@"Uncaught" message:@"Crashed" exception:exception];
}
-(void) flurryStartSession
{
    NSString* version = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
    std::wstring csuffix = MT3::ChannelManager::GetPlatformLoginSuffix();
    if (version != nil){
        NSString *suffix = [[NSString alloc] initWithBytes:csuffix.data() length:csuffix.length()*sizeof(wchar_t) encoding:NSUTF32LittleEndianStringEncoding];
        NSString *result = [NSString stringWithFormat:@"%@_%@",suffix,version];
        [Flurry setAppVersion:result];
    }
    NSSetUncaughtExceptionHandler(&uncaughtExceptionHandler);
    [Flurry setCrashReportingEnabled:true];
    [Flurry setBackgroundSessionEnabled:true];
    std::wstring flurryid = MT3::ChannelManager::GetFlurryId();
    NSString *sflurryid = [[NSString alloc] initWithBytes:flurryid.data() length:flurryid.length()*sizeof(wchar_t) encoding:NSUTF32LittleEndianStringEncoding];
    [Flurry startSession:sflurryid];
}
-(void) returnToGameController
{
    [self.window setRootViewController:self.viewController];
    
	s_ApplicationIsRuning = gRunGameApplication();
//    s_ApplicationIsRuning = true;
    
//    try {
//        NSString* nspath = [NSString stringWithCString: "logo.mp4" encoding:NSUTF8StringEncoding];//[NSString stringWithCString: path.c_str() encoding:NSUTF8StringEncoding];
//    
//        [CCVideoPlayer playMovieWithFile: nspath ];
//        [CCVideoPlayer setNoSkip:YES];
//        [CCVideoPlayer setDelegate:self];
//    } catch (...) { }        
}

- (void) moviePlaybackFinished
{
//    [CCVideoPlayer Release];
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
//  [CCVideoPlayer Release];
//    [CCVideoPlayer cancelPlaying];
//    if (CCDirector::sharedDirector() && CCDirector::sharedDirector()->getRunningScene()){
//	    CCDirector::sharedDirector()->pause();
//	}
	gRefuseAppBrightness(true);
}

-(void) pauseGame
{
    if (s_ApplicationIsRuning == false) return;
    UIApplication * app = [UIApplication sharedApplication];
    
    if (self.taskID != UIBackgroundTaskInvalid) {
        [app endBackgroundTask:self.taskID];
        self.taskID = UIBackgroundTaskInvalid;
        return;
    }
    
    self.taskID = [app beginBackgroundTaskWithExpirationHandler:^{
        [app endBackgroundTask:self.taskID];
    }];
    
    if (self.taskID == UIBackgroundTaskInvalid) {
        NSLog(@"fail to request back ground task");
        CCDirector::sharedDirector()->stopAnimation();
        return;
    }
    
    gSetBackgroundMode(true);//不需要切入后台
    CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
    
    core::Logger::flurryEvent(L"enter_background");
    CCDirector::sharedDirector()->pause();
}
-(void) resumeGame
{
    CCDirector::sharedDirector()->resume();
}

-(void) StopKCBackgroundTask
{
    if (s_ApplicationIsRuning == false) return;
    if (self.taskID == UIBackgroundTaskInvalid) {
        NSLog(@"stop an invalid background task");
        return;
    }
    UIApplication * app = [UIApplication sharedApplication];
    [app endBackgroundTask:self.taskID];
    self.taskID = UIBackgroundTaskInvalid;
    CCDirector::sharedDirector()->stopAnimation();
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    if (s_ApplicationIsRuning == false) return;
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
     */
    UIApplication * app = [UIApplication sharedApplication];
#ifdef LOCALNOTIFY_ENABLE
    if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()) {
        [LocalNotify AddLocalNotify];
    }
#endif   

    if (self.taskID != UIBackgroundTaskInvalid) {
        [app endBackgroundTask:self.taskID];
        self.taskID = UIBackgroundTaskInvalid;
        return;
    }
    
    self.taskID = [app beginBackgroundTaskWithExpirationHandler:^{
        [app endBackgroundTask:self.taskID];
    }];
    
    if (self.taskID == UIBackgroundTaskInvalid) {
        NSLog(@"fail to request back ground task");
        CCDirector::sharedDirector()->stopAnimation();
        return;
    }
    
    gSetBackgroundMode(true);//不需要切入后台
//    [CCVideoPlayer Release];
//    [CCVideoPlayer cancelPlaying];
    CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();

    core::Logger::flurryEvent(L"enter_background");
    
    [MQManager closeMeiqiaService];
    
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    if (s_ApplicationIsRuning == false) return;
    
    
    /*
     Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
     */
 //   [CCVideoPlayer Release];
 //   [CCVideoPlayer cancelPlaying];
    [self StopKCBackgroundTask];
    gSetBackgroundMode(false);//不需要进行切入前台操作
    CCDirector::sharedDirector()->startAnimation();
    if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()) {
        cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ResetServerTimer");
    }
    
#ifdef _LOCOJOY_SDK_
    [[PlatformSDK sharedInstance]WillEnterForeground];
    [[LJStatistics getInstance] willEnterForeground];
#endif
    
#ifdef LOCALNOTIFY_ENABLE
    [LocalNotify clearNotification];
#endif
    core::Logger::flurryEvent(L"will_enter_forground");
    
    [MQManager openMeiqiaService];
    
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    if (s_ApplicationIsRuning == false) return;
    
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    NSError* error = nil;
    if (![audioSession isOtherAudioPlaying]) { //避免从别的应用切换过来后声音变大
        [audioSession setCategory:AVAudioSessionCategoryPlayback error: &error];
        [audioSession setActive:YES error: &error];
    }
    [audioSession setCategory:AVAudioSessionCategoryAmbient error: &error];
    [audioSession setActive:YES error: &error];

    CCDirector::sharedDirector()->resume();
    
	if (gGetGameConfigManager() && gGetGameConfigManager()->isPlayBackMusic()) {
        gGetGameConfigManager()->ApplyConfig();
        CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
    }
    if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()) {
        cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ResetServerTimer");
    }
    core::Logger::flurryEvent(L"do_enter_forground");
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
	 gRefuseAppBrightness(false);
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    
    if (s_ApplicationIsRuning == false) return;
    [self StopKCBackgroundTask];
    /*
     Called when the application is about to terminate.
     Save data if appropriate.
     See also applicationDidEnterBackground:.
     */
  
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{

}


#ifdef LOCALNOTIFY_ENABLE
- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification
{
    application.applicationIconBadgeNumber = 0;
}
#endif

- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application
{
    //comment by liugeng. not found OSMemoryNotification.h under libkern
    //printf("!!!memory warning, level:%d \n", (int)OSMemoryNotificationCurrentLevel());
    
    //comment by liugeng. not found OSMemoryNotification.h under libkern
    //gGCNow((int)OSMemoryNotificationCurrentLevel());
    printf("!!!memory warning. \n");
	gReceiveMemoryWarning();
}

- (void)dealloc
{
    [_window release];
    [_viewController release];
    [super dealloc];
    CocosDenshion::SimpleAudioEngine::end();
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
#ifdef _LOCOJOY_SDK_
    [[PlatformSDK sharedInstance] handleOpenURL:url];
    return YES;
#endif
    return YES;
}

-(NSUInteger)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(UIWindow *)window
{
    return UIInterfaceOrientationMaskAll;
    //return UIInterfaceOrientationMaskLandscape; //change by liugeng
}

-(void) application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    //上传设备deviceToken，以便美洽自建推送后，迁移推送
    [MQManager registerDeviceToken:deviceToken];
}

//应用开始推送接受后，返回的值
-(void) application:(UIApplication *)application didRegisterUserNotificationSettings:(nonnull UIUserNotificationSettings *)notificationSettings
{
    unsigned long type = notificationSettings.types;
	NSLog(@"Notification Type:%d", (int)type);
		
	gGetGameApplication()->SendLocalPushType((int)type);
}



#ifdef _LOCOJOY_SDK_
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
[[PlatformSDK sharedInstance] openURL:url sourceApplication:sourceApplication annotation:annotation];
    return YES;
}
#endif

@end


void CEditViewControl::show(float fTop,float fHeight,float aniTime)
{
    [g_viewController OnBeginEditing:fTop Boxheight:fHeight ViewAniTime:aniTime];
}

void CEditViewControl::hide(float aniTime)
{
    [g_viewController OnEndEditing:aniTime];
}

