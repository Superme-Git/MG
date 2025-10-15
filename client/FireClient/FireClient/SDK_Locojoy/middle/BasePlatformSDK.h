#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#define P_91        @"91"
#define P_PP        @"pp"
#define P_KY        @"kuaiyong"
#define P_TBT       @"tongbutui"
#define P_ITOOLS    @"itools"
#define P_XY        @"xy"
#define P_HAIMA     @"haimanew"
#define P_IS        @"aisi"
#define P_IApple    @"aipingguo"
#define P_LOCOJOY   @"locojoy"


@interface IResultListener : NSObject

-(void)onInitSuccess;
-(void)onInitFail;

// 登陆成功回调接口
// @channelID  渠道标记，如uc\360
// @platformID 平台id
// @session    验证串，传递给公司平台
// @gameID     游戏id
// @gameKey    游戏key
-(void)onLoginSuccess:(NSString*)channelID
                withPID:(NSString*)platformID
                withSess:(NSString*)session
                withGameID:(NSString*)gameID
                withGameKey:(NSString*)gameKey;
-(void)onLoginFail;
-(void)onLogout;

-(void)onPaySuccess:(NSString*)order;
-(void)onPayFail:(NSString*)order;

-(void)onExit;

@end

@interface BasePlatformSDK : NSObject
@property(nonatomic,copy)NSString *channelId;
@property(nonatomic,copy)NSString *platformId;
@property(nonatomic,copy)NSString *productId;
@property(nonatomic,copy)NSString *productName;
@property(nonatomic,copy)NSString *cp;
@property(nonatomic,copy)NSString *roleId;
@property(nonatomic,copy)NSString *roleName;
@property(nonatomic,copy)NSString *serverId;
@property(nonatomic,copy)NSString *order;
@property(nonatomic,assign)BOOL isPaying;
@property(nonatomic,strong)IResultListener *listener;
@property(nonatomic)int productAmount;
@property(nonatomic)int productNumber;
@property(nonatomic,strong)UIViewController *viewController;


-(void)initSDK;
-(void)login: (bool)bAutoLogin;
-(void)logout;
-(void)setViewController:(UIViewController*)viewController;
// 支付接口
// @amount  支付总金额
// @number  购买道具数目，仅做参考，不会乘以amount
// @productID   虚拟道具ID
// @productName 虚拟道具名称
// @cp      自定义参数
// @appUID  游戏内角色id,如果没有填@"1"
// @appUname    游戏内角色名称，如果没有填@“r1”
// @appSvrID    游戏登陆的服务器id，如果没有填@“0”
-(void)pay:(int)amount
        withNum:(int)number
        withPID:(NSString*)productID
        withPName:(NSString*)productName
        withCp:(NSString *)cp
        withAppUID:(NSString*)appUID
        withAppUName:(NSString*)appUname

        withAppSvrID:(NSString*)appSvrID;

-(void)exitSDK;

-(BOOL)supportUserCenter;

-(BOOL)supportBBS;

-(void)openUserCenter;

-(void)openBBS;

-(BOOL)showToolBar:(int)place;//yes 设置成功  no 设置失败
-(BOOL)hideToolBar;//yes 设置成功  no 设置失败
-(BOOL)switchLogin;//yes 设置成功  no 设置失败

// 创建角色统计接口
// @roleID  游戏内角色id,如果没有填@"1"
// @roleName    游戏内角色名称，如果没有填@“r1”
// @roleLevel   游戏内角色等级，如果没有填@“0”
// @serverID    游戏登陆的服务器id，如果没有填@“0”
// @serverName  游戏登陆的服务器名称，如果没有填@“0”
-(void)createRole:(NSString*)roleId
            withRName:(NSString*)roleName
            withRLevel:(NSString*)roleLevel
            withSvrId:(NSString*)serverId
            withSvrName:(NSString*)serverName;

// 登陆角色统计接口
// @roleID  游戏内角色id,如果没有填@"1"
// @roleName    游戏内角色名称，如果没有填@“r1”
// @roleLevel   游戏内角色等级，如果没有填@“0”
// @serverID    游戏登陆的服务器id，如果没有填@“0”
// @serverName  游戏登陆的服务器名称，如果没有填@“0”
-(void)enterGame:(NSString*)roleId
        withRName:(NSString*)roleName
        withRLevel:(NSString*)roleLevel
        withSvrId:(NSString*)serverId
        withSvrName:(NSString*)serverName;

-(void)WillEnterForeground;
-(void)handleOpenURL:(NSURL*)url;
-(void)openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation;

// 以下为私有方法，不要外部调用
-(void)_doLoginVerify:(NSString*)platformID
          withSession:(NSString*)session;
-(void)_doPay;

-(void)hideLoadingView;
-(void)showLoadingView;
@end

