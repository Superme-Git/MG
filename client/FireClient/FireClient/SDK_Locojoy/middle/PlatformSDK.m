//
//  PlatformSDK.m
//  SDKDemo_LOCOJOY
//
//  Created by locojoy on 14-12-9.
//  Copyright (c) 2014年 locojoy. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PlatformSDK.h"
#import "ActiveCodeViewController.h"
#import "HttpRequest.h"
#import "SDKUtils.h"


static PlatformSDK *gsharedInstance = nil;

@implementation PlatformSDK

+(PlatformSDK*)sharedInstance
{
    @synchronized(self)
    {
        if (gsharedInstance == nil)
        {
            gsharedInstance = [[self alloc] init];
        }
    }
    return gsharedInstance;
}

-(void)setViewController:(UIViewController*)vctrl{
    
    self.viewCtrl = vctrl;
}
-(void)initSDK
{
    [super initSDK];
    self.channelId = P_LOCOJOY;
    
    [[LocojoyPlatform sharedUserData]locojoyInit];
    [LocojoyPlatform sharedUserData].locojoyInitDelegate = self;
    [LocojoyPlatform sharedUserData].locojoyLoginDelegate = self;
    [LocojoyPlatform sharedUserData].locojoyPayDelegate= self;
    [LocojoyPlatform sharedUserData].locojoyCutDelegate= self;
    
    
}

-(void)login: (bool)bAutoLogin
{
    [[LocojoyPlatform sharedUserData]locojoyLogin: bAutoLogin];
    
    
   
}

-(void)logout
{
    if (self.listener != nil)
        [self.listener onLogout];
}

-(void)exitSDK
{
    [super exitSDK];
    
}

-(BOOL)supportUserCenter
{
    return YES;
}

-(void)openUserCenter
{
    
}

-(void)handleOpenURL:(NSURL *)url
{
    //  支付宝回调
    [[LocojoyPlatform sharedUserData] alpayOpenURL:url];
    
}

-(void)_doPay
{
    [[LocojoyPlatform sharedUserData]locojoyPayWithView:self.viewCtrl productId:self.productId extraData:self.order];
    //[[LocojoyPlatform sharedUserData] locojoyPayProdictId:self.productId extraData:self.order];
    
    
}

-(void) locojoyInitCallBack:(NSInteger)initState result:(NSDictionary *)data
{
    NSLog(@"locojoyInitCallBack:%zd",initState);
    if (initState == 1) {
        
        [self.listener onInitSuccess];
    }else{
        
        [self.listener onInitFail];
    }
}

-(void)locojoyLoginCallBack:(NSInteger)loginState result:(NSDictionary *)data{
    
    NSLog(@"locojoyLoginCallBack loginState:%zd",loginState);
    NSLog(@"locojoyLoginCallBack data:%@",data);
    
    if (loginState == 3) {
//        NSString*accountID = [data objectForKey:@"accountID"];
//        NSString *token = [data objectForKey:@"token"];
//        [self _doLoginVerify:accountID withSession:token];
        //[[LocojoyPlatform sharedUserData] locojoyShowToolBar:V2];
        [self checkActiveCode:data];
        
    }else{
        [self.listener onLoginFail];
        
    }
    
}
-(void)checkActiveCode:(NSDictionary*)dic
{
    NSString*accountID = [dic objectForKey:@"accountID"];
    NSString *token = [dic objectForKey:@"token"];
    //[self _doLoginVerify:accountID withSession:token];
    NSString*channelId = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"CHANNEL_ID"];;
    
    HttpRequest*req = [[HttpRequest alloc] init];
    
    NSTimeInterval time = [[NSDate date] timeIntervalSince1970];
    long long int timestamp = (long long int)time;
    NSString*version = @"1";
    
    NSString* gameId  = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"GAMEID"];
    NSString* gameKey = [[[NSBundle mainBundle]infoDictionary]objectForKey:@"GAMEKEY"];
    NSString* data = [NSString stringWithFormat:@"{\"PlatformId\":\"%@\",\"Channel\":\"%@\",\"Time\":\"%lld\"}",accountID,channelId,timestamp];
    NSString* strToBeSign = [NSString stringWithFormat:@"%@%@%@",gameKey,version,data];
    NSString* sign = [SDKUtils encryptMD5String:strToBeSign];
    data = [SDKUtils UrlEncode:data];
    NSString*url = @"";
    if([[[[NSBundle mainBundle]infoDictionary]objectForKey:@"SDK_Debug"] boolValue])
    {
        NSString*baseurl = @"http://testot.locojoy.com/api/ActiveCode/IsActive.ashx";
        url = [NSString stringWithFormat:@"%@?Version=%@&GameId=%@&Data=%@&Sign=%@",baseurl,version,gameId,data,sign];
    }
    else{
        NSString*baseurl = @"http://ot.locojoy.com/api/ActiveCode/IsActive.ashx";
        url = [NSString stringWithFormat:@"%@?Version=%@&GameId=%@&Data=%@&Sign=%@",baseurl,version,gameId,data,sign];
    }
    
    __weak PlatformSDK* weakSelf = self;
    [self showLoadingView];
    req.handler= ^(BOOL bSuccess, NSData *data,HttpRequest*req)
    {
        [weakSelf hideLoadingView];
        if(bSuccess)
        {
            NSError *error;
            NSDictionary *dict = [NSJSONSerialization
                                  JSONObjectWithData:data
                                  options:NSJSONReadingMutableLeaves error:&error];
            NSString *code = [dict objectForKey:@"Code"];
            //此用户已经激活过验证码，或者不需要激活
            if([code isEqualToString:@"1"])
            {
                 [weakSelf _doLoginVerify:accountID withSession:token];
            }
            else
            {
                
                ActiveCodeViewController*activeCodeVc = [[UIStoryboard storyboardWithName:@"ActiveCode" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"ActiveCodeViewController"];
                activeCodeVc.platformsdk = self;
                activeCodeVc.token = token;
                activeCodeVc.accountId = accountID;
                activeCodeVc.channelId = channelId;
                
                if ([[[UIDevice currentDevice] systemVersion] floatValue]>=8.0) {
                    
                    activeCodeVc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
                    activeCodeVc.view.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.6];
                    
                }else{
                    
                    activeCodeVc.view.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.6];
                    activeCodeVc.modalPresentationStyle=UIModalPresentationCurrentContext;
                    
                }
                [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:activeCodeVc animated:YES completion:nil];
            }
        }
        else{
            [weakSelf showLoadingView];
            //发生http错误了 重新连接
            [req start:url];
        }
        
    };
    [req start:url];
    
    /* */

    
}
-(void)locojoyPayCallBack:(NSInteger)payState result:(NSDictionary *)data{
    NSLog(@"locojoyPayCallBack loginState:%zd",payState);
    NSLog(@"locojoyPayCallBack data:%@",data);
    self.isPaying = NO;
    if (payState == 100) {
        
        [self.listener onPaySuccess:self.order];
    }else{
        [self.listener onPayFail:self.order];
    }
    
}
-(void)locojoyCutCallBack:(NSInteger)cutState result:(NSDictionary *)data
{
    if (cutState == 1 ) {
        if ([self.viewCtrl respondsToSelector:@selector(sdkWillLogOut)]) {
            [self.viewCtrl sdkWillLogOut];
        }
        //切换账号
        [[LocojoyPlatform sharedUserData] locojoyLogin: NO];
    }
        
}
//-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
//    
//    [self.viewCtrl.view endEditing:YES];
//}

@end