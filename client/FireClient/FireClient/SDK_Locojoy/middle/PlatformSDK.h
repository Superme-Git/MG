#import "BasePlatformSDK.h"
#import <LJSDKFW/LocojoyPlatform.h>
@interface PlatformSDK : BasePlatformSDK <LocojoyPlatformInitDelegate,LocojoyPlatformLoginDelegate,LocojoyPlatformPayDelegate,LocojoyPlatformCutDelegate>


+(PlatformSDK*)sharedInstance;
-(void)setViewController:(UIViewController*)vctrl;
@property(nonatomic,strong)UIViewController *viewCtrl;
@end

