#include <string>
#include "GameSdk.h"
#include "LoginManager.h"
#include "MainRoleDataManager.h"
#import "PlatformSDK.h"
#include "StringCover.h"
#import <AdSupport/ASIdentifierManager.h>
#import "Reachability.h"
#import "ParameterSDK.h"
#include "GameUIManager.h"
#include "FireClientAppDelegate.h"
#include "MessageManager.h"
#import "ljstatistics.h"


@interface MyResult : IResultListener

@end

@implementation MyResult

-(void)onInitSuccess
{
    NSLog(@"MyResult onInitSuccess");
    gGetGameUIManager()->sdkLogin();
}


-(void)onInitFail
{
    NSLog(@"MyResult onInitFail");
    CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmInitEscape, gGetGameUIManager());
    CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, gGetGameUIManager());
    gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
}

-(void)onLoginSuccess:(NSString*)channelID
              withPID:(NSString*)platformID
             withSess:(NSString*)session
           withGameID:(NSString*)gameID
          withGameKey:(NSString*)gameKey
{
    NSLog(@"MyResult onLoginSuccess");
	
	NSString* cid = [[[NSBundle mainBundle] infoDictionary] valueForKey:@"CHANNEL_ID"];
	
    //关闭超时判断
    gGetGameUIManager()->SdkOutTimeClose();
    if (SDK::GameSdk::s_loginResultCallback != NULL)
        SDK::GameSdk::s_loginResultCallback(0, [channelID UTF8String], [platformID UTF8String], [session UTF8String]);
    NSString *deviceid =[[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    std::string strPlatformID = [platformID UTF8String];
    std::string strChannelID = [cid UTF8String];
    std::string strDeviceid = [deviceid UTF8String];
    char aAccount[256] = "";
    sprintf(aAccount,"%s@@%s,%s", strPlatformID.c_str(), strChannelID.c_str(), strDeviceid.c_str());
    gGetLoginManager()->SetAccountInfo(StringCover::to_wstring(aAccount));
    std::string strSession = [session UTF8String];
    gGetLoginManager()->SetPassword(StringCover::to_wstring(strSession));
    gGetLoginManager()->SaveAccount();
    gGetLoginManager()->SetDeviceid(StringCover::to_wstring(strDeviceid));
	gGetLoginManager()->SetChannelId(StringCover::to_wstring(strChannelID.c_str()));
   
	//cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeScriptFile(L"main.lua");

	if (gGetLoginManager()->getEnterMainStatus() == 0)
		gGetLoginManager()->setEnterMainStatus(1); // SDK登陆成功
	else if (gGetLoginManager()->getEnterMainStatus() == 3) {
		gGetLoginManager()->setEnterMainStatus(2); // 启动游戏成功
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("SelectServerEntry_EnableClick", true); // 设置选服界面按钮可点击
	}
    
}

-(void)onLoginFail
{
    NSLog(@"MyResult onLoginFail");
    //关闭超时判断
    gGetGameUIManager()->SdkOutTimeClose();
    CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmEscape, gGetGameUIManager());
    CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, gGetGameUIManager());
    gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
    if (SDK::GameSdk::s_loginResultCallback != NULL)
        SDK::GameSdk::s_loginResultCallback(1, [@"" UTF8String], [@"" UTF8String], [@"" UTF8String]);
}

-(void)onLogout
{
    NSLog(@"MyResult onLogout");
    if (SDK::GameSdk::s_logoutCallback != NULL)
        SDK::GameSdk::s_logoutCallback();
}

-(void)onPaySuccess:(NSString*)order
{
    NSLog(@"MyResult onPaySuccess");
    if (SDK::GameSdk::s_payCallback != NULL)
        SDK::GameSdk::s_payCallback(0, [order UTF8String]);
    SDK::GameSdk::s_payCallback = NULL;
}

-(void)onPayFail:(NSString*)order
{
    NSLog(@"MyResult onPayFail");
    if (SDK::GameSdk::s_payCallback != NULL)
        SDK::GameSdk::s_payCallback(1, [order UTF8String]);
    SDK::GameSdk::s_payCallback = NULL;
}

-(void)onExit
{
    NSLog(@"MyResult onExit");
    if (SDK::GameSdk::s_exitCallback != NULL)
        SDK::GameSdk::s_exitCallback();
    SDK::GameSdk::s_exitCallback = NULL;
}


@end

namespace SDK
{
    
    LoginCallback GameSdk::s_loginResultCallback = NULL;
    Callback GameSdk::s_logoutCallback = NULL;
    PayCallback GameSdk::s_payCallback = NULL;
    Callback GameSdk::s_exitCallback = NULL;
    bool GameSdk::init()
    {
        if([[Reachability reachabilityForInternetConnection] currentReachabilityStatus]!=NotReachable){
            [[PlatformSDK sharedInstance] setListener:[[MyResult alloc] init]];
            [[PlatformSDK sharedInstance] initSDK];
            return true;
        }
        return false;
        
    }
    void GameSdk::setLoginCallback(LoginCallback loginResultCallback)
    {
        GameSdk::s_loginResultCallback = loginResultCallback;
    }
    
    void GameSdk::setLogoutCallback(Callback logoutResultCallback)
    {
        GameSdk::s_logoutCallback = logoutResultCallback;
    }
    
    bool GameSdk::login(bool bAutoLogin)
    {
        //if([[Reachability reachabilityForInternetConnection] currentReachabilityStatus]!=NotReachable){
            [[PlatformSDK sharedInstance] login: bAutoLogin];
            return true;
        //}
        //return false;
        
    }
    void GameSdk::logout()
    {
        [[PlatformSDK sharedInstance] logout];
    }
    
    void GameSdk::pay(int number, int amount, const char *pszPid, const char *pszPname, const char *pszRoleId, const char *pszRoleName, const char *pszServerId, const char *pszCp, PayCallback payResultCallback)
    {
        NSLog(@"number:%d, amount:%d,pszPid:%s,pszPname:%s,pszRoleId:%s,pszRoleName:%s,pszServerId:%s,pszCp:%s", number, amount, pszPid,pszPname,pszRoleId,pszRoleName,pszServerId,pszCp );
        GameSdk::s_payCallback = payResultCallback;
        
        NSString *productId = [NSString stringWithUTF8String:pszPid];
        NSString *productName = [NSString stringWithUTF8String:pszPname];
        NSString *roleId = [NSString stringWithUTF8String:pszRoleId];
        NSString *roleName = [NSString stringWithUTF8String:pszRoleName];
        NSString *serverId = [NSString stringWithUTF8String:pszServerId];
        NSString *cp = [NSString stringWithUTF8String:pszCp];
        [[PlatformSDK sharedInstance] pay:amount withNum:number withPID:productId withPName:productName withCp:cp withAppUID:roleId withAppUName:roleName withAppSvrID:serverId];
        
    }
    
    void GameSdk::exit(Callback exitResultCallback)
    {
        GameSdk::s_exitCallback = exitResultCallback;
        [[PlatformSDK sharedInstance] exitSDK];
    }
    
    void GameSdk::openUserCenter()
    {
        [[PlatformSDK sharedInstance] openUserCenter];
    }
    
    void GameSdk::openBBS()
    {
        [[PlatformSDK sharedInstance] openBBS];
    }
    
    void GameSdk::createRoleStat(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName)
    {
        NSString *roleId = [NSString stringWithUTF8String:pszRoleId];
        NSString *roleName = [NSString stringWithUTF8String:pszRoleName];
        NSString *roleLevel = [NSString stringWithUTF8String:pszRoleLevel];
        NSString *serverId = [NSString stringWithUTF8String:pszServerId];
        NSString *serverName = [NSString stringWithUTF8String:pszServerName];
        [[PlatformSDK sharedInstance] enterGame:roleId withRName:roleName withRLevel:roleLevel withSvrId:serverId withSvrName:serverName];
    }
    
    void GameSdk::enterGameStat(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName)
    {
        NSString *roleId = [NSString stringWithUTF8String:pszRoleId];
        NSString *roleName = [NSString stringWithUTF8String:pszRoleName];
        NSString *roleLevel = [NSString stringWithUTF8String:pszRoleLevel];
        NSString *serverId = [NSString stringWithUTF8String:pszServerId];
        NSString *serverName = [NSString stringWithUTF8String:pszServerName];
        [[PlatformSDK sharedInstance] enterGame:roleId withRName:roleName withRLevel:roleLevel withSvrId:serverId withSvrName:serverName];
    }
    
    
    //统计sdk
    void GameSdk::LJStatistics_onRegister(const char *platformId, const char *roleId, const char *roleName, const char *serverId)
    {
        NSString *_platformId = [NSString stringWithUTF8String:platformId];
        NSString *_roleId = [NSString stringWithUTF8String:roleId];
        NSString *_roleName = [NSString stringWithUTF8String:roleName];
        NSString *_serverId = [NSString stringWithUTF8String:serverId];
        [[LJStatistics getInstance] onRegister:_platformId roleId:_roleId roleName:_roleName serverId:_serverId];
    }
    void GameSdk::LJStatistics_onLogin(const char *platformId, const char *roleId, const char *roleName, const char *serverId)
    {
        NSString *_platformId = [NSString stringWithUTF8String:platformId];
        NSString *_roleId = [NSString stringWithUTF8String:roleId];
        NSString *_roleName = [NSString stringWithUTF8String:roleName];
        NSString *_serverId = [NSString stringWithUTF8String:serverId];
        [[LJStatistics getInstance] onLogin:_platformId roleId:_roleId roleName:_roleName serverId:_serverId];
    }
    void GameSdk::LJStatistics_onPay(const char *platformId, const char *roleId, const char *roleName, const char *serverId, int PAY_TYPE, const char *appOrder, const char *channelOrder, const char *productId, const char *productPrice)
    {
        NSString *_platformId = [NSString stringWithUTF8String:platformId];
        NSString *_roleId = [NSString stringWithUTF8String:roleId];
        NSString *_roleName = [NSString stringWithUTF8String:roleName];
        NSString *_serverId = [NSString stringWithUTF8String:serverId];
        NSString *_appOrder = [NSString stringWithUTF8String:appOrder];
        NSString *_channelOrder = [NSString stringWithUTF8String:channelOrder];
        NSString *_productId = [NSString stringWithUTF8String:productId];
        NSString *_productPrice = [NSString stringWithUTF8String:productPrice];
        
        [[LJStatistics getInstance] onPay:_platformId roleId:_roleId roleName:_roleName serverId:_serverId payType:PAY_TYPE appOrder:_appOrder channelOrder:_channelOrder productId:_productId productPrice:_productPrice];
    }
    std::string GameSdk::getPasteBoard()
    {
        UIPasteboard *board = [UIPasteboard generalPasteboard];
		if (board.string) {
			return std::string([board.string UTF8String]);
		}
		return "";
    }
}

