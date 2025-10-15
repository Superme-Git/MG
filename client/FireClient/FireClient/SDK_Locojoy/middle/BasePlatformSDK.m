#import <UIKit/UIKit.h>
//#import "ParameterSDK.h"
#import "BasePlatformSDK.h"
#import "HttpRequest.h"
#import "SDKUtils.h"

#define kTag_MainScreenView        (2002)
#define kTag_ActivityIndicatoriew  (2001)
#define kTag_PromptView            (2000)

@implementation IResultListener

-(void)onInitSuccess
{
    
}


-(void)onInitFail
{
    
}

-(void)onLoginSuccess:(NSString*)channelID
                withPID:(NSString*)platformID
                withSess:(NSString*)session
                withGameID:(NSString*)gameID
                withGameKey:(NSString*)gameKey
{
    
}

-(void)onLoginFail
{
    
}

-(void)onLogout
{
    
}

-(void)onPaySuccess:(NSString*)order
{
    
}

-(void)onPayFail:(NSString*)order
{
    
}

-(void)onExit
{
    
}


@end
@interface BasePlatformSDK()
{
    NSString*loginVerifyUrl;
    NSString*payRequestUrl;
    NSString*appId;
    NSString*appKey;
    NSString*gameId;
    NSString*gameKey;
}

@end

@implementation BasePlatformSDK
{

}

-(id)init
{
    self = [super init];
    if (self)
    {
        
        if ([[[[NSBundle mainBundle]infoDictionary]objectForKey:@"SDK_Debug"] boolValue])
        {
            loginVerifyUrl = @"http://testldsdk.locojoy.com/login";
            payRequestUrl = @"http://testldsdk.locojoy.com/pay";
            
        }
        else{
            loginVerifyUrl = @"http://ldsdk.locojoy.com/login";
            payRequestUrl = @"http://ldsdk.locojoy.com/pay";
        }
        appId = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"APPID"];
        appKey = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"APPKEY"];
        gameId = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"GAMEID"];
        gameKey = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"GAMEKEY"];
        
        self.platformId = nil;
        self.isPaying = NO;
        self.channelId = nil;
        self.listener = nil;
    }
    return self;
}



-(void)initSDK
{
    self.platformId = nil;
    self.channelId = nil;
    /*
    _appId = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"app_id"];
    _appKey = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"app_key"];
    _gameId = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"game_id"];
    _gameKey = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"game_key"];
    _orientation = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"orientation"];
    _loginVerifyUrl = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"login_verify_url"];
    _payRequestUrl = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"pay_request_url"];
     */
}

-(void)login
{
    
}

-(void)logout
{
    
}
-(void)WillEnterForeground{
    
    
}
/*
 amount:支付金额，单位为分
 number:商品数量
 productID:商品id
 productName:商品名字
 cp:cpifo 格式参考接入文档
 appUID:  游戏角色id  没有填入@"1"
 appUname:游戏角色名字 没有填入@"r1"
 appSvrID:游戏服务器id 没有填入@"0"
 
 */
-(void)pay:(int)amount
            withNum:(int)number
            withPID:(NSString*)productID
            withPName:(NSString*)productName
            withCp:(NSString *)cp
            withAppUID:(NSString*)appUID
            withAppUName:(NSString*)appUname
            withAppSvrID:(NSString*)appSvrID
{
    if (self.isPaying)
        return;
    self.isPaying = YES;
    self.productAmount = amount;
    self.productNumber = number;
    self.productId = productID;
    self.productName = productName;
    self.roleId = appUID;
    self.roleName = appUname;
    self.serverId = appSvrID;
    
    NSString *signOri = [NSString stringWithFormat:@"%@%@%@%@%@%@",
                         self.channelId, appId, gameId, self.platformId, productID,
                         gameKey];
    NSString *sign = [SDKUtils encryptMD5String:signOri];
    
    NSString *cpB64 = [SDKUtils encodeBase64String:cp];
    NSString *cpB64U = [SDKUtils UrlEncode:cpB64];
    
    NSString *url = [NSString stringWithFormat:@"%@?channel=%@&appid=%@&gameid=%@&platformid=%@&productid=%@&productcount=%d&originalmoney=%d&money=%d&cpinfo=%@&sign=%@",
                     payRequestUrl, self.channelId, appId, gameId, self.platformId, productID, number, amount, amount, cpB64U, sign];
    
    NSLog(@"payrequest: %@", url);
    HttpRequest *request = [[HttpRequest alloc] init];
       [self showLoadingView];
        __weak BasePlatformSDK* weakself = self;
        request.handler = ^(BOOL bSuccess, NSData *data,HttpRequest*req){
        [weakself hideLoadingView];
        if (bSuccess) {
            NSError *error;
            NSDictionary *dict = [NSJSONSerialization
                                  JSONObjectWithData:data
                                  options:kNilOptions error:&error];
            self.order = [dict objectForKey:@"order"];
            if (self.order ==nil || [self.order isEqualToString:@""]) {
                NSLog(@"中间件订单获取失败!!");
                weakself.isPaying = NO;
                return;
            }
            NSLog(@"中间件订单获取成功 order: %@", self.order);
            [self _doPay];
        }
        else
        {
            NSLog(@"中间件订单获取失败");
            weakself.isPaying = NO;
            if (self.listener != nil)
                [self.listener onPayFail:@""];
        }
    };
    [request start:url];
}

-(void)exitSDK
{
    if (self.listener != nil)
        [self.listener onExit];
        self.listener = nil;
}

-(BOOL)supportUserCenter
{
    return NO;
}

-(BOOL)supportBBS
{
    return NO;
}

-(void)openUserCenter
{
    
}

-(void)openBBS
{
    
}

-(void)createRole:(NSString*)roleId
        withRName:(NSString*)roleName
       withRLevel:(NSString*)roleLevel
        withSvrId:(NSString*)serverId
      withSvrName:(NSString*)serverName
{
    
}


-(void)enterGame:(NSString*)roleId
       withRName:(NSString*)roleName
      withRLevel:(NSString*)roleLevel
       withSvrId:(NSString*)serverId
     withSvrName:(NSString*)serverName
{
    
}

-(void)onPause
{
    
}

-(void)onResume
{
    
}

-(BOOL)showToolBar:(int)place{
    return NO;
}

-(BOOL)hideToolBar{
    return NO;
}

-(BOOL)switchLogin{
    return NO;
}

-(void)handleOpenURL:(NSURL *)url
{
    
}
-(void)setViewController:(UIViewController *)viewController
{
    self.viewController = viewController;
    
}
-(void)openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    
}

-(void)_doLoginVerify:(NSString*)platformID
          withSession:(NSString*)session
{
    NSString *signOri = [NSString stringWithFormat:@"%@%@%@%@%@",
                         self.channelId, appId, gameId, platformID, gameKey];
    NSString *sign = [SDKUtils encryptMD5String:signOri];
    NSString *url = [NSString
                     stringWithFormat:@"%@?channel=%@&appid=%@&gameid=%@&uid=%@&name=&sessionid=%@&extra=&extra2=&sign=%@",
                     loginVerifyUrl,
                     self.channelId,
                     appId,
                     gameId,
                     platformID,
                     session,
                     sign];
    NSLog(@"login verify url: %@", url);
    
    HttpRequest *request = [[HttpRequest alloc] init];
    [self showLoadingView];
    __weak BasePlatformSDK* weakself = self;
    request.handler = ^(BOOL bSuccess, NSData *data,HttpRequest*req){
        [weakself hideLoadingView];
        if (bSuccess)
        {
            NSError *error;
            NSDictionary *dict = [NSJSONSerialization
                                  JSONObjectWithData:data
                                  options:NSJSONReadingMutableLeaves error:&error];
            NSNumber *code = [dict objectForKey:@"code"];
            if ([code intValue] != 1)
            {
                NSLog(@"登陆验证失败!!!!");
                if (self.listener != nil)
                    [self.listener onLoginFail];
            }
            else
            {
                NSLog(@"登陆验证成功!!!!");
                self.platformId = [dict objectForKey:@"platformid"];
                
                NSDictionary *tmpDict = [NSDictionary dictionaryWithObjectsAndKeys:
                                         self.channelId, @"channel",
                                         appId, @"appid",
                                         self.platformId, @"platformid",
                                         @"", @"name",
                                         @"", @"avatar",
                                         @"", @"sex",
                                         @"", @"nick",
                                         [dict objectForKey:@"logintime"], @"logintime",
                                         @"", @"cpinfo",
                                         [dict objectForKey:@"sign"], @"sign",
                                         nil];
                NSData *tmpData = [NSJSONSerialization dataWithJSONObject:tmpDict options:NSJSONWritingPrettyPrinted error:&error];
                NSString *tmpStrJson = [[NSString alloc] initWithData:tmpData encoding:NSUTF8StringEncoding];
                NSString *tmpSession = [SDKUtils encodeBase64String:tmpStrJson];
                NSLog(@"login success, %@", tmpStrJson);
                
                if (self.listener != nil)
                {
                    [self.listener onLoginSuccess:self.channelId
                                      withPID:self.platformId
                                     withSess:tmpSession
                                   withGameID:gameId
                                  withGameKey:gameKey];
                }
            }
        }
        else
        {
            NSLog(@"登陆验证失败!!!!");
            if (self.listener != nil)
                [self.listener onLoginFail];
        }
    };
    [request start:url];
}
-(void)_doPay
{
    
}

-(void) showLoadingView
{
    UIView * rootView = [[[[UIApplication sharedApplication] keyWindow] rootViewController] view];
    CGRect rect = [[UIScreen mainScreen] bounds];
    

    
    //NSLog(@"before x=%f y=%f w=%f h=%f",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
    if(rect.size.height > rect.size.width)
    {
        CGFloat tem = rect.size.height;
        rect.size.height = rect.size.width;
        rect.size.width = tem;
    }
    //NSLog(@"after x=%f y=%f w=%f h=%f",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
    UIView* mainScreenView = [[UIView alloc] initWithFrame:rect];
    if (nil == [rootView viewWithTag:kTag_MainScreenView])
    {
        
        mainScreenView.backgroundColor = [UIColor blackColor];
        mainScreenView.alpha = 0.5;
        mainScreenView.tag = kTag_MainScreenView;
        
        //[rootView addSubview:mainScreenView];
    }
    
    if (nil == [mainScreenView viewWithTag:kTag_ActivityIndicatoriew])
    {
        UIActivityIndicatorView* indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
        indicator.center = mainScreenView.center;
        indicator.tag = kTag_ActivityIndicatoriew;
        [indicator startAnimating];
        
        [mainScreenView addSubview:indicator];
        //[indicator release];
    }
    [rootView addSubview:mainScreenView];
    
}

-(void)hideLoadingView
{
    UIView * rootView = [[[[UIApplication sharedApplication] keyWindow] rootViewController] view];
    
    UIView* mainScreenView = [rootView viewWithTag:kTag_MainScreenView];
    
    
    UIActivityIndicatorView* indicator = nil;
    if(mainScreenView)
    {
        indicator = (UIActivityIndicatorView*)[mainScreenView viewWithTag:kTag_ActivityIndicatoriew];
    }
    
    if (indicator != nil) {
        [indicator stopAnimating];
        [indicator removeFromSuperview];
    }
    if (mainScreenView != nil) {
        [mainScreenView removeFromSuperview];
    }
    
}


@end

