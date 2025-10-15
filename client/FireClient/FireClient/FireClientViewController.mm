//  MyClass.m
//  FireClient

#import "FireClientViewController.h"
#import "FireClientAppDelegate.h"

#import <CommonCrypto/CommonDigest.h>
#import "GameSdk.h"

#import <ShareSDK/ShareSDK.h>
#import <ShareSDKExtension/SSEShareHelper.h>
#import <ShareSDKUI/ShareSDK+SSUI.h>
#import <ShareSDKUI/SSUIShareActionSheetStyle.h>
#import <ShareSDKUI/SSUIShareActionSheetCustomItem.h>
#import <ShareSDK/ShareSDK+Base.h>

#import <ShareSDKExtension/ShareSDK+Extension.h>

#import "MQChatDeviceUtil.h"
#import <MeiQiaSDK/MQManager.h>

#include "ConnectGetServerInfo.h"
#include "GameApplication.h"
#include "EAGLView.h"

int prewTag=0;   //0为初始值，1为向上推中
float prewMoveY=0.0f;



@implementation FireClientViewController



- (id)init
{
    self = [super init];
    if (self) {
        // Initialization code here.
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
   
}

- (void) traitCollectionDidChange:(UITraitCollection *)previousTraitCollection
{
    if ([self respondsToSelector:@selector(traitCollection)] &&
        [self.traitCollection respondsToSelector:@selector(forceTouchCapability)] &&
        self.traitCollection.forceTouchCapability == UIForceTouchCapabilityAvailable) {
        
        EAGLView* view = (EAGLView*)[EAGLView sharedEGLView];
        if(view != nil)
        {
            view.forceTouchEnable = true;
        }
    }
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
{
    return toInterfaceOrientation == UIInterfaceOrientationLandscapeLeft
        || toInterfaceOrientation == UIInterfaceOrientationLandscapeRight;
    
//    return toInterfaceOrientation == UIInterfaceOrientationLandscapeRight;
   
}

-(NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskLandscape;
}

- (void)didReceiveMemoryWarning
{
    printf("receive memory warning\n");
}

-(BOOL)shouldAutorotate
{
    return YES;
}

-(void) OnBeginEditing:(float)top Boxheight:(float)height ViewAniTime:(float)aniTime
{
   
//    if (prewTag==1) {
//        
//        return;
//        
//    }
//    height=406;
    
    CGRect rect=[[UIScreen mainScreen] bounds];
    CGFloat scale=[UIScreen mainScreen].scale;
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
    {
        scale=1.0f;
    }
    else if ((int)scale == 3) //iPhone6Plus
    {
        scale = 2.0f;
    }
    UIInterfaceOrientation deviceDir=[[UIApplication sharedApplication] statusBarOrientation];
    
      
    //height*=scale;
    float textY =top;
    //float bottomY = rect.size.width*scale-textY;
    //float bottomY = (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad ? rect.size.height*scale-textY : rect.size.width*scale-textY); //change by liugeng
    float bottomY = (rect.size.width > rect.size.height ? rect.size.height*scale-textY : rect.size.width*scale-textY); //change by liugeng
    
    if(bottomY>=height)  //判断当前的高度是否已经有216，如果超过了就不需要再移动主界面的View高度
    {
//        prewTag = 0;
        return;
    }
//    else
//    {
//        prewTag=1;
//    }
//    prewTag = textField.tag;
    float moveY = (height-bottomY)/scale;
   
    
    NSTimeInterval animationDuration = aniTime;
    CGRect frame = self.view.frame;
    
    //change by liugeng
    //if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
    if (frame.size.width > frame.size.height) {
        frame.origin.y -= moveY-prewMoveY;
    } else if (deviceDir==UIInterfaceOrientationLandscapeRight) {
        frame.origin.x+=moveY-prewMoveY;
    } else if (deviceDir==UIInterfaceOrientationLandscapeLeft) {
        frame.origin.x-=moveY-prewMoveY;
    }


    //frame.size.width -=moveY-prewMoveY; //View的高度增加  //shield by liugeng

    self.view.frame = frame;
    [UIView beginAnimations:@"ResizeView" context:nil];
    [UIView setAnimationDuration:animationDuration];
    self.view.frame = frame;
     prewMoveY = moveY;
    [UIView commitAnimations];//设置调整界面的动画效果
}

-(void) OnEndEditing:(float)aniTime
{
//    if(prewTag == 0) //当编辑的View不是需要移动的View
//    {
//        return;
//    }
//    
//    prewTag=0;
    
    float moveY ;
    NSTimeInterval animationDuration = aniTime;
    CGRect frame = self.view.frame;
//    if(prewTag == textField.tag) //当结束编辑的View的TAG是上次的就移动
    {   //还原界面
        moveY =  prewMoveY;
        
        UIInterfaceOrientation deviceDir=[[UIApplication sharedApplication] statusBarOrientation];

        //change by liugeng
        //if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        if (frame.size.width > frame.size.height) {
            frame.origin.y += moveY;
        } else if (deviceDir==UIInterfaceOrientationLandscapeRight) {
            frame.origin.x -=moveY;
        } else if (deviceDir==UIInterfaceOrientationLandscapeLeft) {
            frame.origin.x += moveY;
        }
        

        //frame.size. width +=moveY; //shield by liugeng

        self.view.frame = frame;
    }
    prewMoveY=0.0f;
    //self.view移回原位置
    [UIView beginAnimations:@"ResizeView" context:nil];
    [UIView setAnimationDuration:animationDuration];
    self.view.frame = frame;
    [UIView commitAnimations];
 
} 


-(void) showWebView:(int)nX posY:(int)nY width:(int)nWidth height:(int)nHeigt
{
    NSHTTPCookie *cookie;
    NSHTTPCookieStorage *storgage = [NSHTTPCookieStorage sharedHTTPCookieStorage];
    for (cookie in [storgage cookies]) {
        [storgage deleteCookie:cookie];
    }
    
    NSURLCache *shareCache = [[NSURLCache alloc] initWithMemoryCapacity:0 diskCapacity:0 diskPath:nil];
    [NSURLCache setSharedURLCache:shareCache];
    [shareCache release];
    
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    
    UIWebView* webV = [[UIWebView alloc] initWithFrame:CGRectMake(nX, nY, nWidth, nHeigt)];
    [webV setBackgroundColor:[UIColor clearColor]];
    webV.hidden = true;
    self.webView = webV;
    [webV release];
    self.webView.opaque = NO;
    [self.webView setDelegate:self];

    std::string strServerInfo = GetServerInfo()->getHttpAdressByEnum(eHttpNoticeUrl)+"?r=x"+StringCover::intToString(rand());
    
    NSString *nstrServerInfo = [NSString stringWithUTF8String:strServerInfo.c_str()];
    
    NSURLRequest *request =[NSURLRequest requestWithURL:[NSURL URLWithString:nstrServerInfo] cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:10.0];

    [self.view addSubview: self.webView ];
    [(UIScrollView*)[[self.webView subviews] objectAtIndex:0] setBounces:NO];
    [self.webView  loadRequest:request];
    
    for(UIView *_aView in [self.webView subviews])
    {
        if ([_aView isKindOfClass:[UIScrollView class]])
        {
            [(UIScrollView*)_aView setShowsVerticalScrollIndicator:NO];
            [(UIScrollView*)_aView setShowsHorizontalScrollIndicator:NO];
            
        }
    }
    
}

-(void) resumeWebView
{
    if (self.webView)
    {
        self.webView.hidden = false;
    }
}

-(void) hideWebView
{
    if (self.webView)
    {
        self.webView.hidden = true;
    }
}

-(void)delayShowView
{
    if (self.webView)
    {
        self.webView.hidden = false;
    }
}

-(void)closeWebView
{
    [self.webView  removeFromSuperview];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSURL *requestURL = [[request URL] retain];
    if(([[requestURL scheme] isEqualToString:@"http"] || [[requestURL scheme] isEqualToString:@"https"]) && (navigationType == UIWebViewNavigationTypeLinkClicked ))
    {
        return ![[UIApplication sharedApplication] openURL:[requestURL autorelease]];
    }
    [requestURL release];
    return YES;
}
- (void)webViewDidStartLoad:(UIWebView *)webView
{
    self.webView.hidden = true;
}
- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    [self performSelector:@selector(delayShowView) withObject:nil afterDelay:0.5f];
}
- (void)webView:(UIWebView *)webView didFailLoadWithError:(nullable NSError *)error
{}

/**
 *  简单分享
 */
-(void)oneKeyShareToPlatform:(NSInteger)shareSdk shareType:(NSInteger)shareType title:(NSString *)title text:(NSString *)text imagePath:(NSString *)imagePath url:(NSString *)url
{
    /**
     * 在简单分享中，只要设置共有分享参数即可分享到任意的社交平台
     **/
    //创建分享参数
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    
    //UIImage* img = [UIImage imageNamed:imagePath];
    //if (!img) {
        //CCLOG("Can't find capture image: %s", [imagePath UTF8String]);
        //return;
    //}

    SSDKPlatformType sharePlatFormType = SSDKPlatformTypeUnknown;
    switch (shareSdk) {
        case 1://新浪微博
            sharePlatFormType = SSDKPlatformTypeSinaWeibo;
            //url = @"http://mt3.locojoy.com/share/wbiosshare/index.html";
            break;
        case 2://腾讯微博
            sharePlatFormType = SSDKPlatformTypeTencentWeibo;
            break;
        case 4://QQ空间
            sharePlatFormType = SSDKPlatformSubTypeQZone;
            //url = @"http://mt3.locojoy.com/share/qqiosshare/index.html";
            break;
        case 17://微信好友
            sharePlatFormType = SSDKPlatformSubTypeWechatSession;
            //url = @"http://mt3.locojoy.com/share/wxiosshare/index.html";
            break;
        case 18://微信朋友圈
            sharePlatFormType = SSDKPlatformSubTypeWechatTimeline;
            //url = @"http://mt3.locojoy.com/share/wxiosshare/index.html";
            break;
        case 27://微信收藏
            sharePlatFormType = SSDKPlatformSubTypeWechatFav;
            //url = @"http://mt3.locojoy.com/share/wxiosshare/index.html";
            break;
        case 19://QQ好友
            sharePlatFormType = SSDKPlatformSubTypeQQFriend;
            //url = @"http://mt3.locojoy.com/share/qqiosshare/index.html";
            break;
            
        default:
            break;
    }
    
        //if (img) 
		{
            
            [shareParams SSDKSetupShareParamsByText:text
                                             images:[NSURL URLWithString:imagePath]
                                                url:[NSURL URLWithString:url]
                                              title:title
                                               type:SSDKContentTypeImage];
            if ( shareType == 3 )
            {
                switch (shareSdk)
                {
                    case 1://新浪微博
                        NSString *str;
                        str = [NSString stringWithFormat:@"%@%@",text,url];
                        sharePlatFormType = SSDKPlatformTypeSinaWeibo;
                        [shareParams SSDKSetupSinaWeiboShareParamsByText:str
                                                                   title:title
                                                                   image:imagePath
                                                                     url:nil
                                                                latitude:0
                                                               longitude:0
                                                                objectID:nil
                                                                    type:SSDKContentTypeImage];
                        break;
                    case 2://腾讯微博
                        sharePlatFormType = SSDKPlatformTypeTencentWeibo;
                        break;
                    case 4://QQ空间
                        sharePlatFormType = SSDKPlatformSubTypeQZone;
                        break;
                    case 17://微信好友
                        sharePlatFormType = SSDKPlatformSubTypeWechatSession;
                        [shareParams SSDKSetupWeChatParamsByText:text title:title url:[NSURL URLWithString:url] thumbImage:imagePath image:imagePath musicFileURL:nil extInfo:nil fileData:nil emoticonData:nil sourceFileExtension:nil sourceFileData:nil type:SSDKContentTypeWebPage forPlatformSubType:SSDKPlatformSubTypeWechatSession];
                        break;
                    case 18://微信朋友圈
                        sharePlatFormType = SSDKPlatformSubTypeWechatTimeline;
                        [shareParams SSDKSetupWeChatParamsByText:text title:title url:[NSURL URLWithString:url] thumbImage:imagePath image:imagePath musicFileURL:nil extInfo:nil fileData:nil emoticonData:nil sourceFileExtension:nil sourceFileData:nil type:SSDKContentTypeWebPage forPlatformSubType:SSDKPlatformSubTypeWechatTimeline];
                        break;
                    case 27://微信收藏
                        sharePlatFormType = SSDKPlatformSubTypeWechatFav;
                        [shareParams SSDKSetupWeChatParamsByText:text title:title url:[NSURL URLWithString:url] thumbImage:imagePath image:imagePath musicFileURL:nil extInfo:nil fileData:nil emoticonData:nil sourceFileExtension:nil sourceFileData:nil type:SSDKContentTypeWebPage forPlatformSubType:SSDKPlatformSubTypeWechatFav];
                        break;
                    case 19://QQ好友
                        sharePlatFormType = SSDKPlatformSubTypeQQFriend;
                        break;
                }
                
            }
            
            //进行分享
            [ShareSDK share:sharePlatFormType
                 parameters:shareParams
             onStateChanged:^(SSDKResponseState state, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error) {
                 
                 switch (state) {
                     case SSDKResponseStateSuccess:
                     {
                         UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享成功"
                                                                             message:nil
                                                                            delegate:nil
                                                                   cancelButtonTitle:@"确定"
                                                                   otherButtonTitles:nil];
                         [alertView show];
						 gGetGameApplication()->ShareResultCallBack(1);
                         break;
                     }
                     case SSDKResponseStateFail:
                     {
                         UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享失败"
                                                                             message:[NSString stringWithFormat:@"分享失败"]
                                                                            delegate:nil
                                                                   cancelButtonTitle:@"确定"
                                                                   otherButtonTitles:nil];
                         [alertView show];
                         break;
                     }
                     case SSDKResponseStateCancel:
                     {
                         UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享已取消"
                                                                             message:nil
                                                                            delegate:nil
                                                                   cancelButtonTitle:@"确定"
                                                                   otherButtonTitles:nil];
                         [alertView show];
                         break;
                     }
                     default:
                         break;
                 }
             }];
        }
    
}


-(void)evaluate
{
    
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"游戏评论"
                                                        message:@"请您给游戏评个分吧，我们会努力做的更好。"
                                                       delegate:self
                                              cancelButtonTitle:@"残忍拒绝"
                                              otherButtonTitles:@"欣然前往", nil];
    [alertView show];
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch (buttonIndex) {
        case 0:
        {

        }
            break;
        case 1:
        {
//            NSString *evaluateString = [NSString stringWithFormat:@"itms-apps://ax.itunes.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?type=Purple+Software&id=1106818611"];
//            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:evaluateString]];
            SKStoreProductViewController *storeProductViewContorller = [[SKStoreProductViewController alloc] init];
            //设置代理请求为当前控制器本身
            storeProductViewContorller.delegate = self;
            //加载一个新的视图展示
			NSString * appStoreID = @"1106818611";
			if([[[[NSBundle mainBundle]infoDictionary]objectForKey:@"IS_CARD_SERVER"] boolValue])
			{
				appStoreID = @"1124820388";
			}
            [storeProductViewContorller loadProductWithParameters:@{SKStoreProductParameterITunesItemIdentifier : appStoreID} completionBlock:^(BOOL result, NSError *error) {
                 if(error){
                     NSLog(@"error %@ with userInfo %@",error,[error userInfo]);
                 }else{
                     NSLog(@"success");
                     [self presentViewController:storeProductViewContorller animated:YES completion:^{}];
                 }
                 
             }];
        }
            
            break;
        default:
            break;
    }
}

- (void)productViewControllerDidFinish:(SKStoreProductViewController *)viewController{
    
    [self dismissViewControllerAnimated:YES completion:^{
        
    }];
    
}

-(void)showCSView
{
    //基本功能 - 在线客服
    if (!_chatViewMgr) {
        _chatViewMgr = [[MQChatViewManager alloc] init];
        
        NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
        NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
        NSString* mqGroupId = [data valueForKey:@"meiqiaGroupID"];
        if (mqGroupId && [mqGroupId length] > 0) {
             [_chatViewMgr setScheduledGroupId:mqGroupId];
        }
        [data release];
    }
    
    //[chatViewManager enableOutgoingAvatar:false];
    [_chatViewMgr pushMQChatViewControllerInViewController:self];
}

- (void)dealloc
{
    [super dealloc];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [_chatViewMgr release];
}

- (NSString *)urlEncode:(NSString *)string {
    NSString *output = (__bridge NSString *) CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault, (__bridge CFStringRef)string, NULL, (CFStringRef)@"!*'();:@&=+$,/?%#[]~", kCFStringEncodingUTF8);
    return output;
}

- (void)sdkWillLogOut {
    cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("Logout_CalledBySdk");
}

-(void)initChartBoostSdk{
    // Begin a user session. Must not be dependent on user actions or any prior network requests.
    if([[[[NSBundle mainBundle]infoDictionary]objectForKey:@"IS_CARD_SERVER"] boolValue])
    {
        [Chartboost startWithAppId:@"577c787804b01608a6c4ffaf" appSignature:@"2fd872fe3cc3f7af9ade17d25ef8ef74bd51ceee" delegate:self];
    }
    else
    {
        [Chartboost startWithAppId:@"57281a8404b0161c513e812c" appSignature:@"a6a649d07e57ffa94813ea641ff63f21546e0d3e" delegate:self];
    }

}

/*
 * Chartboost Delegate Methods
 *
 */

/*
 * didInitialize
 *
 * This is used to signal when Chartboost SDK has completed its initialization.
 *
 * status is YES if the server accepted the appID and appSignature as valid
 * status is NO if the network is unavailable or the appID/appSignature are invalid
 *
 * Is fired on:
 * -after startWithAppId has completed background initialization and is ready to display ads
 */
- (void)didInitialize:(BOOL)status {
    NSLog(@"didInitialize");
    // chartboost is ready
    //[Chartboost cacheRewardedVideo:CBLocationMainMenu];
    //[Chartboost cacheMoreApps:CBLocationHomeScreen];
    
    // Show an interstitial whenever the app starts up
    //[Chartboost showInterstitial:CBLocationHomeScreen];
}


/*
 * shouldDisplayInterstitial
 *
 * This is used to control when an interstitial should or should not be displayed
 * The default is YES, and that will let an interstitial display as normal
 * If it's not okay to display an interstitial, return NO
 *
 * For example: during gameplay, return NO.
 *
 * Is fired on:
 * -Interstitial is loaded & ready to display
 */

- (BOOL)shouldDisplayInterstitial:(NSString *)location {
    NSLog(@"about to display interstitial at location %@", location);
    
    // For example:
    // if the user has left the main menu and is currently playing your game, return NO;
    
    // Otherwise return YES to display the interstitial
    return YES;
}


/*
 * didFailToLoadInterstitial
 *
 * This is called when an interstitial has failed to load. The error enum specifies
 * the reason of the failure
 */

- (void)didFailToLoadInterstitial:(NSString *)location withError:(CBLoadError)error {
    NSLog([self getErrorText:error adType:@"Interstitial"]);
}

- (NSString *)getErrorText:(CBLoadError)e adType:(NSString *)t {
    NSString *errorText;
    switch(e){
        case CBLoadErrorInternal: {
            errorText = [NSString stringWithFormat:@"Failed to load %@, internal error !",t];
        } break;
        case CBLoadErrorInternetUnavailable: {
            errorText = [NSString stringWithFormat:@"Failed to load %@, no Internet connection !",t];
        } break;
        case CBLoadErrorTooManyConnections: {
            errorText = [NSString stringWithFormat:@"Failed to load %@, too many connections !",t];
        } break;
        case CBLoadErrorWrongOrientation: {
            errorText = [NSString stringWithFormat:@"Failed to load %@, wrong orientation !",t];
        } break;
        case CBLoadErrorFirstSessionInterstitialsDisabled: {
            errorText = [NSString stringWithFormat:@"Failed to load %@, first session !",t];
        } break;
        case CBLoadErrorNetworkFailure: {
            errorText = [NSString stringWithFormat:@"Failed to load %@, network error !",t];
        } break;
        case CBLoadErrorNoAdFound : {
            errorText = [NSString stringWithFormat:@"Failed to load %@, no ad found !",t];
        } break;
        case CBLoadErrorSessionNotStarted : {
            errorText = [NSString stringWithFormat:@"Failed to load %@, session not started !",t];
        } break;
        case CBLoadErrorImpressionAlreadyVisible : {
            errorText = [NSString stringWithFormat:@"Failed to load %@, impression already visible !",t];
        } break;
        case CBLoadErrorUserCancellation : {
            errorText = [NSString stringWithFormat:@"Failed to load %@, impression cancelled !",t];
        } break;
        case CBLoadErrorNoLocationFound : {
            NSLog(@"Failed to load Interstitial, missing location parameter !");
        } break;
        case CBLoadErrorAssetDownloadFailure : {
            errorText = [NSString stringWithFormat:@"Failed to load %@, asset download failed !",t];
        } break;
        case CBLoadErrorPrefetchingIncomplete : {
            errorText = [NSString stringWithFormat:@"Failed to load %@, prefetching of video content is incomplete !",t];
        } break;
        default: {
            errorText = [NSString stringWithFormat:@"Failed to load %@, unknown error %lul!", t, (long)e];
        }
    }
    return errorText;
}

/*
 * didCacheInterstitial
 *
 * Passes in the location name that has successfully been cached.
 *
 * Is fired on:
 * - All assets loaded
 * - Triggered by cacheInterstitial
 *
 * Notes:
 * - Similar to this is: (BOOL)hasInterstitial:(NSString *)location;
 * Which will return true if a cached interstitial exists for that location
 */

- (void)didCacheInterstitial:(NSString *)location {
    NSLog(@"interstitial cached at location %@", location);
}

/*
 * didFailToLoadMoreApps
 *
 * This is called when the more apps page has failed to load for any reason
 *
 * Is fired on:
 * - No network connection
 * - No more apps page has been created (add a more apps page in the dashboard)
 * - No publishing campaign matches for that user (add more campaigns to your more apps page)
 *  -Find this inside the App > Edit page in the Chartboost dashboard
 */

- (void)didFailToLoadMoreApps:(NSString *)location withError:(CBLoadError)error {
    NSLog([self getErrorText:error adType:@"MoreApps"]);
}

/*
 * didDismissInterstitial
 *
 * This is called when an interstitial is dismissed
 *
 * Is fired on:
 * - Interstitial click
 * - Interstitial close
 *
 */

- (void)didDismissInterstitial:(NSString *)location {
    NSLog(@"dismissed interstitial at location %@", location);
}

/*
 * didDismissMoreApps
 *
 * This is called when the more apps page is dismissed
 *
 * Is fired on:
 * - More Apps click
 * - More Apps close
 *
 */

- (void)didDismissMoreApps:(NSString *)location {
    NSLog(@"dismissed more apps page at location %@", location);
}

/*
 * didCompleteRewardedVideo
 *
 * This is called when a rewarded video has been viewed
 *
 * Is fired on:
 * - Rewarded video completed view
 *
 */
- (void)didCompleteRewardedVideo:(CBLocation)location withReward:(int)reward {
    NSLog(@"completed rewarded video view at location %@ with reward amount %d", location, reward);
}

/*
 * didFailToLoadRewardedVideo
 *
 * This is called when a Rewarded Video has failed to load. The error enum specifies
 * the reason of the failure
 */

- (void)didFailToLoadRewardedVideo:(NSString *)location withError:(CBLoadError)error {
    NSLog([self getErrorText:error adType:@"Rewarded Video"]);
}

/*
 * didDisplayInterstitial
 *
 * Called after an interstitial has been displayed on the screen.
 */

- (void)didDisplayInterstitial:(CBLocation)location {
    NSLog(@"Did display interstitial");
    
    // We might want to pause our in-game audio, lets double check that an ad is visible
    //if ([Chartboost isAnyViewVisible]) 
	{
        // Use this function anywhere in your logic where you need to know if an ad is visible or not.
        NSLog(@"Pause audio");
    }
}


/*!
 @abstract
 Called after an InPlay object has been loaded from the Chartboost API
 servers and cached locally.
 
 @param location The location for the Chartboost impression type.
 
 @discussion Implement to be notified of when an InPlay object has been loaded from the Chartboost API
 servers and cached locally for a given CBLocation.
 */
- (void)didCacheInPlay:(CBLocation)location {
    NSLog(@"Successfully cached inPlay");
}

/*!
 @abstract
 Called after a InPlay has attempted to load from the Chartboost API
 servers but failed.
 
 @param location The location for the Chartboost impression type.
 
 @param error The reason for the error defined via a CBLoadError.
 
 @discussion Implement to be notified of when an InPlay has attempted to load from the Chartboost API
 servers but failed for a given CBLocation.
 */
- (void)didFailToLoadInPlay:(CBLocation)location
                  withError:(CBLoadError)error {
    
    NSString *errorString = [self getErrorText:error adType:@"InPlay"];
    NSLog(@"Error: %@", errorString);
    
}

#pragma mark - 视频预览页面

-(void) startRecord{
    gGetGameApplication()->SetRecordState(1);
    
    //在此可以设置是否允许麦克风（传YES即是使用麦克风，传NO则不是用麦克风）
    [[RPScreenRecorder sharedRecorder] startRecordingWithMicrophoneEnabled:NO handler:^(NSError *error){
        NSLog(@"录制开始...");
        
        if (error) {
            NSLog(@"失败消息:%@", error);
        } else {
            gGetGameApplication()->SetRecordState(2);
        }
    }];
}

-(void) stopRecord {
    gGetGameApplication()->SetRecordState(0);
    
    [[RPScreenRecorder sharedRecorder] stopRecordingWithHandler:^(RPPreviewViewController *previewViewController, NSError *  error){
        if (error) {
            NSLog(@"失败消息:%@", error);
            
        } else {
            //显示录制到的视频的预览页
            NSLog(@"显示预览页面");
            previewViewController.previewControllerDelegate = self;
            
            [self showVideoPreviewController:previewViewController withAnimation:YES];
            //[self.view.window.rootViewController presentViewController:previewViewController animated:YES completion:nil];
        }
    }];
}

//关闭的回调
- (void)previewControllerDidFinish:(RPPreviewViewController *)previewController {
    [self hideVideoPreviewController:previewController withAnimation:YES];
    //[previewController dismissViewControllerAnimated:YES completion:nil];
}

- (BOOL)prefersStatusBarHidden
{
    return YES;
}

//显示视频预览页面，animation=是否要动画显示
- (void)showVideoPreviewController:(RPPreviewViewController *)previewController withAnimation:(BOOL)animation {
    //UI需要放到主线程
    dispatch_async(dispatch_get_main_queue(), ^{
        
        CGRect rect = [UIScreen mainScreen].bounds;
        
        if (animation) {
            
            rect.origin.x += rect.size.width;
            previewController.view.frame = rect;
            rect.origin.x -= rect.size.width;
            [UIView animateWithDuration:0.3 animations:^(){
                previewController.view.frame = rect;
            } completion:^(BOOL finished){
                
            }];
            
        } else {
            previewController.view.frame = rect;
        }
        
        [self.view addSubview:previewController.view];
        [self addChildViewController:previewController];
    });
}

//关闭视频预览页面，animation=是否要动画显示
- (void)hideVideoPreviewController:(RPPreviewViewController *)previewController withAnimation:(BOOL)animation {
    
    //UI需要放到主线程
    dispatch_async(dispatch_get_main_queue(), ^{
        
        CGRect rect = previewController.view.frame;
        
        if (animation) {
            
            rect.origin.x += rect.size.width;
            [UIView animateWithDuration:0.3 animations:^(){
                previewController.view.frame = rect;
            } completion:^(BOOL finished){
                //移除页面
                [previewController.view removeFromSuperview];
                [previewController removeFromParentViewController];
                
            }];
            
        } else {
            //移除页面
            [previewController.view removeFromSuperview];
            [previewController removeFromParentViewController];
        }
    });
}


@end
