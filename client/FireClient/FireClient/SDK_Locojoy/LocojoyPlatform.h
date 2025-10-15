//
//  LocojoyPlatform.h
//  ljsdk

#import <Foundation/Foundation.h>

//init
@protocol LocojoyPlatformInitDelegate <NSObject>
-(void)locojoyInitCallBack:(NSInteger) initState;
@end
//login
@protocol LocojoyPlatformLoginDelegate <NSObject>
-(void)locojoyLoginCallBack:(NSInteger) loginState result:(NSDictionary *)data;
@end
//pay
@protocol LocojoyPlatformPayDelegate <NSObject>
-(void)locojoyPayCallBack:(NSInteger)payState result:(NSDictionary *)data;
@end
//cut
@protocol LocojoyPlatformCutDelegate <NSObject>
-(void)locojoyCutCallBack:(NSInteger)payState result:(NSDictionary *)data;
@end

@interface LocojoyPlatform : NSObject

typedef NS_ENUM(NSInteger, LOCATION){
    V1,V2,V3,V4,
    H1,H2,H3,H4,
};

+(LocojoyPlatform *)sharedUserData;

/**
 @brief 初始化
 */
-(void)locojoyInit;

/**
 @brief 登陆
 */
-(void)locojoyLogin;

/**
 支付
 */
-(void)locojoyPayProdictId:(NSString*)productid extraData:(NSString*)extraData;

/**
 *  显示悬浮工具条
 */
- (void)locojoyShowToolBar:(LOCATION)tab;
/**
 *  Crash
 */
//-(void)crashHelp;
/**
 *  隐藏悬浮工具条
 */
- (void)locojoyHideToolBar;


@property (nonatomic,weak) id<LocojoyPlatformInitDelegate> locojoyInitDelegate;
@property (nonatomic,weak) id<LocojoyPlatformLoginDelegate> locojoyLoginDelegate;
@property (nonatomic,weak) id<LocojoyPlatformPayDelegate> locojoyPayDelegate;
@property (nonatomic,weak) id<LocojoyPlatformCutDelegate> locojoyCutDelegate;
@property(nonatomic) BOOL CanShowButton;

@end



