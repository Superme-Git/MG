// 测试环境下启用_SDK_DEBUG_
//#define _SDK_DEBUG_         //如果是正式环境 需要注销掉这行代码
// 渠道分配的appid
#define APP_ID @"10024"
// 渠道分配的appkey
#define APP_KEY @"8e55941bae2170d58170f8f3b85ed069"
// 公司平台分配的gameid
#define GAME_ID @"88"
// 公司平台分配的gamekey
#define GAME_KEY @"b18a26ffc632752987bd24a7bf0353f3"
// 屏幕方向 取值LandscapeLeft LandscapeRight Portrait PortraitUpsideDown
#define ORIENTATION @"LandscapeLeft"

// 以下的宏定义不要修改，使用默认值即可
#ifdef _SDK_DEBUG_

    #define LOGIN_VERIFY_URL @"http://testldsdk.locojoy.com/login"
    #define PAY_REQUEST_URL @"http://testldsdk.locojoy.com/pay"

#else

    #define LOGIN_VERIFY_URL @"http://ldsdk.locojoy.com/login"
    #define PAY_REQUEST_URL @"http://ldsdk.locojoy.com/pay"

#endif
