//  FireClientAppDelegate.h
//  FireClient

#import <UIKit/UIKit.h>
#import "FireClientViewController.h"
#import "UpdateEngineViewController.h"
#ifdef _QUINCY_
#import "BWQuincyManager.h"
#endif
#import "CCVideoPlayer.h"
#import <ReplayKit/ReplayKit.h>

#ifdef _QUINCY_
@interface FireClientAppDelegate : NSObject <UIApplicationDelegate,UIAlertViewDelegate,BWQuincyManagerDelegate>
#else
@interface FireClientAppDelegate : NSObject <UIApplicationDelegate,UIAlertViewDelegate>
#endif

@property (nonatomic, retain) IBOutlet UIWindow *window;

@property (nonatomic, retain) IBOutlet FireClientViewController *viewController;

@property UIBackgroundTaskIdentifier taskID;

-(bool) handleShortcutItem: (UIApplicationShortcutItem*) shortcutItem;

-(void) StopKCBackgroundTask;

@property (nonatomic, retain) IBOutlet UpdateEngineViewController *updateengineViewController;

-(void) returnToGameController;

-(void) flurryStartSession;

-(void) pauseGame;
-(void) resumeGame;
-(void) startRecord;
-(void) stopRecord;

@end
void uncaughtExceptionHandler(NSException *exception);
extern FireClientViewController * g_viewController;
extern FireClientAppDelegate* g_AppDelegate;
