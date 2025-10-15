//  #import <UIKit/UIKit.h>.h
//  FireClient

#import <UIKit/UIKit.h>
#import "MQChatViewManager.h"
#import <Chartboost/Chartboost.h>
#import <ReplayKit/ReplayKit.h>
#import <StoreKit/StoreKit.h>

@interface FireClientViewController : UIViewController<UIWebViewDelegate, ChartboostDelegate, RPPreviewViewControllerDelegate,SKStoreProductViewControllerDelegate,UIAlertViewDelegate>
{
    UIWebView* webView;
    MQChatViewManager *_chatViewMgr;
}

@property (nonatomic, retain) UIWebView * webView;

-(void) OnBeginEditing:(float)top Boxheight:(float)height ViewAniTime:(float)aniTime;
-(void) OnEndEditing:(float)aniTime;

-(void) showWebView:(int)nX posY:(int)nY width:(int)nWidth height:(int)nHeigt;

-(void) closeWebView;

-(void) hideWebView;
-(void) resumeWebView;

-(void) delayShowView;

-(void) webViewDidStartLoad:(UIWebView *)webView;
-(void) webViewDidFinishLoad:(UIWebView *)webView;
-(void) webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error;

/**
 *  设置分享参数
 *
 *  @param text     文本
 *  @param images   图片集合,传入参数可以为单张图片信息，也可以为一个NSArray，数组元素可以为UIImage、NSString（图片路径）、NSURL（图片路径）、SSDKImage。 *  @param url      网页路径/应用路径
 *  @param title    标题
 *  @param type     分享类型
 */
- (void)oneKeyShareToPlatform:(NSInteger)shareSdk
                                shareType:(NSInteger)shareType
                                title:(NSString *)title
                                text:(NSString *)text
                                imagePath:(NSString *)imagePath
                                url:(NSString *)url;

-(void)showCSView;

-(void)sdkWillLogOut;

-(void)initChartBoostSdk;

-(void) startRecord;
-(void) stopRecord;

-(void)evaluate;

@end
