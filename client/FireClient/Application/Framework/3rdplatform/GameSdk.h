#ifndef _GAMESDK_H_
#define _GAMESDK_H_

#include <jni.h>
#include <string>

extern "C"
{

enum ToolBarPlace {
	TopLeft = 0,
	TopRight,
	BottomLeft,
	BottomRight,
	MiddleLeft,
	MiddleRight
};

/**
 * 用于检查渠道是否支持logout、switchAccount、enterPlatform、showToolBar、hideToolBar
 * @funcName 函数名称
 * @return 1-支持 0-不支持
 */
int isFunctionSupported(const char *funcName);

//融合的判断用户中心接口
int isEnterPlatform();

// 此方式供测试用，可以弹出一个alert dialog
void showDialog(const char *pszTitle, const char *pszContent);

// 登陆、登出
void login();
void wxYYBLogin();
void logout();
void init();

// 切换账号
void switchAccount();

// 退出SDK
void exitSdk();

// 应用宝登陆回调相关
bool canDoYYBLoginCallback();
void onYYBLoginCb();

/*
 *
 *
 * @param pszPid 商品id，最大60字节
 * @param pszPname 商品名称，最大60字节
 * @param pszDesc 商品描述，最大200字节
 * @param price 商品总金额，单位分
 * @param count 购买数目
 * @param rate 兑换比率，比如1元兑换10个商品，一般填写1
 * @param pszUid 游戏内角色id，最大40字节
 * @param pszUname 游戏内角色名，最大60字节
 * @param roleLevel 角色等级
 * @param roleBalance 角色虚拟币数量，一般填0
 * @param pszServerId 游戏服务器id，最大20字节
 * @param pszServerName 游戏服务器名称，最大60字节
 * @param pszCp 自定义字符串，最大300字节
 */
void pay(const char *pszPid, const char *pszPname, const char *pszDesc, int price, int count, int rate,
		const char *pszRoleId, const char *pszRoleName, int roleLevel, int roleBalance, const char *pszServerId, const char *pszServerName, const char *pszCp);

// 打开用户中心
void enterPlatform();

/*
 * 创建角色数据统计
 * @param pszRoleId 游戏内角色id（不是登陆返回的平台id，如果没有可以填写默认值），最大40字节
 * @param pszRoleName 游戏内角色名 （不是登陆返回的平台名称，如果没有可以填写默认值），最大60字节
 * @param pszRoleLevel 游戏内角色等级，最大10字节
 * @param pszServerId 分配的游戏服务器id（如果没有可以填写默认值），最大10字节
 * @param pszServerName 分配的游戏服务器名称（如果没有可以填写默认值），最大40字节
 * @param pszRoleTime 角色创建时间 
 */
void createRoleStat(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName, const char *pszRoleTime);

/*
 * 角色登陆数据统计
 * @param pszRoleId 游戏内角色id（不是登陆返回的平台id，如果没有可以填写默认值），最大40字节
 * @param pszRoleName 游戏内角色名 （不是登陆返回的平台名称，如果没有可以填写默认值），最大60字节
 * @param pszRoleLevel 游戏内角色等级，最大10字节
 * @param pszServerId 分配的游戏服务器id（如果没有可以填写默认值），最大10字节
 * @param pszServerName 分配的游戏服务器名称（如果没有可以填写默认值），最大40字节
 * @param pszRoleTime 角色创建时间 
 */
void enterGameStat(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName, const char *pszRoleTime);

/*
 * 角色升级数据统计
 * @param pszRoleId 游戏内角色id（不是登陆返回的平台id，如果没有可以填写默认值），最大40字节
 * @param pszRoleName 游戏内角色名 （不是登陆返回的平台名称，如果没有可以填写默认值），最大60字节
 * @param pszRoleLevel 游戏内角色等级，最大10字节
 * @param pszServerId 分配的游戏服务器id（如果没有可以填写默认值），最大10字节
 * @param pszServerName 分配的游戏服务器名称（如果没有可以填写默认值），最大40字节
 * @param pszRoleTime 角色创建时间 
 */
void recordRoleInf(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName, const char *pszRoleTime);

// 显示悬浮框
// @param place 悬浮框位置，必须是enum ToolBarPlace中一个值
void showToolBar(int place);

// 隐藏悬浮框
void hideToolBar();

/**
 * 初始化回调
 * @param flags 1-成功 0-失败
 * @param channel 分配的渠道号, 最大10字节
 */
JNIEXPORT void JNICALL Java_com_locojoy_sdk_GameSDK_initCb(JNIEnv *env, jclass obj, jint flags, jstring channel);

/**
 * 登陆结果回调
 * @param flags 1-成功 0-失败
 * @param channel 分配的渠道号, 最大10字节
 * @param platformid 渠道id, 最大40字节，失败时为""
 * @param session 去平台验证用的session, 最大400字节，失败时为""
 */
JNIEXPORT void JNICALL Java_com_locojoy_sdk_GameSDK_loginCb(JNIEnv *env, jclass obj, jint flags, jstring channel, jstring platformid, jstring session);

/**
 * 应用宝登陆结果回调
 * @param flags 1-成功 0-失败
 * @param channel 分配的渠道号, 最大10字节
 * @param platformid 渠道id, 最大40字节，失败时为""
 * @param session 去平台验证用的session, 最大400字节，失败时为""
 * @param openid 从手Q登录态或微信登录态中获取的openid的值
 * @param openkey 从手Q登录态或微信登录态中获取的access_token 的值
 * @param paytoken 从手Q登录态中获取的pay_token的值; 微信登录时特别注意该参数传空。
 * @param pf 平台来源，$平台-$渠道-$版本-$业务标识。
 * @param pfkey 跟平台来源和openkey根据规则生成的一个密钥串。
 * @param zoneid 账户分区ID_角色。
 * @param platformname 手Q用户：qq  微信用户：weixin
 */
JNIEXPORT void JNICALL Java_com_locojoy_sdk_GameSDK_YYBLoginCb(JNIEnv *env, jclass obj, jint flags, jstring channel, jstring platformid, jstring session, jstring openid, jstring openkey, jstring paytoken, jstring pf, jstring pfkey, jstring zoneid, jstring platformname);

/**
 * 登出结果回调
 * @param flags 1-成功 0-失败
 */
JNIEXPORT void JNICALL Java_com_locojoy_sdk_GameSDK_logoutCb(JNIEnv *env, jclass obj, jint flags);

/**
 * 切换账号结果回调
 * @param flags 1-成功 0-失败
 * @param channel 分配的渠道号, 最大10字节
 * @param platformid 渠道id, 最大40字节，失败时为""
 * @param session 去平台验证用的session, 最大400字节，失败时为""
 */
JNIEXPORT void JNICALL Java_com_locojoy_sdk_GameSDK_switchAccountCb(JNIEnv *env, jclass obj, jint flags, jstring channel, jstring platformid, jstring session);

/**
 * 支付结果回调
 * @param flags 1-成功 0-失败
 * @param order 本次支付订单号，32字节
 */
JNIEXPORT void JNICALL Java_com_locojoy_sdk_GameSDK_payCb(JNIEnv *env, jclass obj, jint flags, jstring order);

/**
 * 退出sdk
 */
JNIEXPORT void JNICALL Java_com_locojoy_sdk_GameSDK_exitSdkCb(JNIEnv *env, jclass obj);

/**
 * 用户中心
 */
JNIEXPORT void JNICALL Java_com_locojoy_sdk_GameSDK_enterPlatformCb(JNIEnv *env, jclass obj, jint flags, jstring channel);

JNIEXPORT void JNICALL Java_com_locojoy_activecode_ActiveCode_doActiveCodeResult(JNIEnv *env, jclass obj, jint flags, jstring platformId, jstring channel);
/*
void onLoginAndroid(const char* platformId, const char* roleId, const char* roleName, const char* serverId );
void onPayAndroid(const char* platformId, const char* roleId, const char* roleName, const char* serverId,
		const char* appOrder, const char* channelOrder, const char* productId, const char* productPrice);
void onRegisterAndroid(const char* platformId, const char* roleId, const char* roleName, const char* serverId );
*/
}
void tdInitCpp();
/*
 td初始化
 *
 */

#endif
