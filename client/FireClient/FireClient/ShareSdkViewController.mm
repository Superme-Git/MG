
#import "ShareSdkViewController.h"
#import "FireClientViewController.h"
#import "FireClientAppDelegate.h"


void oneKeyShareToPlatform(int eShareSDK_, int eShareType_, const char* title, const char* text, const char* imgPath, const char* webUrl)
{
    NSInteger shareSdk = eShareSDK_;
    NSInteger shareType = eShareType_;
    NSString * shareTitle = [NSString stringWithCString:title encoding:NSUTF8StringEncoding];
    NSString * shareText = [NSString stringWithCString:text encoding:NSUTF8StringEncoding];
    NSString * imagePath = [NSString stringWithCString:imgPath encoding:NSUTF8StringEncoding];
    NSString * shareWebUrl = [NSString stringWithCString:webUrl encoding:NSUTF8StringEncoding];
    [g_viewController oneKeyShareToPlatform:shareSdk shareType:shareType title:shareTitle text:shareText imagePath:imagePath url:shareWebUrl];
}

void IosShowMQView()
{
    [g_viewController showCSView];
}

void IosCallChartBoost()
{
    [g_viewController initChartBoostSdk];
}

void IosCallEvaluate()
{
    [g_viewController evaluate];
}