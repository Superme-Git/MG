
#import "FireClientViewController.h"
#import "FireClientAppDelegate.h"


// void WebViewController::showWebView(int nX,int nY, int nWidth ,int nHeigt)
//{
//    [g_viewController showWebView:nX posY:nY width:nWidth height:nHeigt];
//}
// void WebViewController::closeWebView()
//{
//    [g_viewController closeWebView];
//
//}

void showWebView(int nX,int nY, int nWidth ,int nHeigt)
{
    [g_viewController showWebView:nX - nWidth / 2 posY:nY - nHeigt / 2 width:nWidth height:nHeigt];
}
void closeWebView()
{
    [g_viewController closeWebView];
}
void hideWebView()
{
	[g_viewController hideWebView];
}
void resumeWebView()
{
	[g_viewController resumeWebView];
}