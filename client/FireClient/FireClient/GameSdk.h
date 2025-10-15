#ifndef _GAMESDK_H_
#define _GAMESDK_H_
#include "stdafx.h"

namespace SDK
{

/*
 * 定义SDK通用的回调函数
 */
typedef void (*Callback)();

/*
 * 定义SDK登陆回掉函数
 * @param success 0-成功 1-失败
 * @param pszChannel	渠道标识，例如：uc 360 baidu，最大10字节
 * @param pszPlatformId	平台用户id，最大40字节
 * @param pszSession	验证用session，最大400字节
 */
typedef void (*LoginCallback)(int success, const char *pszChannel, const char *pszPlatformId, const char *pszSession);

/*
 * 定义SDK支付回掉函数
 * @param success 0-支付成功 1-支付失败
 * @param pszOrder 订单号,最大60字节
 */
typedef void (*PayCallback)(int success, const char *pszOrder);

/*
 * SDK访问接口，通过此类调用SDK的功能。
 */
class GameSdk
{
public:
    static bool init();
    static void currentReachabilityStatus();

    static void setLoginCallback(LoginCallback loginResultCallback);
	static void setLogoutCallback(Callback logoutResultCallback);

	static bool login(bool bAutoLogin);
	static void logout();

	// 退出SDK
	static void exit(Callback exitResultCallback);

	/*
	 * @param number 购买数目
	 * @param amount 商品总金额，单位分
	 * @param pszPid 商品id，最大60字节
	 * @param pszPname 商品名称，最大60字节
	 * @param pszUid 游戏内角色id，最大40字节
	 * @param pszUname 游戏内角色名，最大60字节
	 * @param pszServerId 游戏服务器id，最大20字节
	 * @param pszCp 自定义字符串，最大300字节
	 * @param payResultCallback 支付结果回调函数
	 */
	static void pay(int number, int amount, const char *pszPid, const char *pszPname, const char *pszRoleId, const char *pszRoleName, const char *pszServerId, const char *pszCp, PayCallback payResultCallback);

	static void openUserCenter();
	static void openBBS();

	/*
	 * @param pszRoleId 游戏内角色id（不是登陆返回的平台id，如果没有可以填写默认值），最大40字节
	 * @param pszRoleName 游戏内角色名 （不是登陆返回的平台名称，如果没有可以填写默认值），最大60字节
	 * @param pszRoleLevel 游戏内角色等级，最大10字节
	 * @param pszServerId 分配的游戏服务器id（如果没有可以填写默认值），最大10字节
	 * @param pszServerName 分配的游戏服务器名称（如果没有可以填写默认值），最大40字节
	 */
	static void createRoleStat(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName);
	
	/*
	 * @param pszRoleId 游戏内角色id（不是登陆返回的平台id，如果没有可以填写默认值），最大40字节
	 * @param pszRoleName 游戏内角色名 （不是登陆返回的平台名称，如果没有可以填写默认值），最大60字节
	 * @param pszRoleLevel 游戏内角色等级，最大10字节
	 * @param pszServerId 分配的游戏服务器id（如果没有可以填写默认值），最大10字节
	 * @param pszServerName 分配的游戏服务器名称（如果没有可以填写默认值），最大40字节
	 */
	static void enterGameStat(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName);
    
    
    
    
    
    
    //统计sdk
    //    3.1.2 角色注册统计接口
    //    此接口在玩家完成游戏角色创建后调用。
    //    调用方式：
    //    [[LJStatistics getInstance] onRegister:@"2" roleId:@"8" roleName:@"4" serverId:@"5"];
    //    参数说明：
    //    platformId
    //    渠道登陆返回的渠道用户ID，NSString。
    //    roleId
    //    玩家在游戏内的角色ID，NSString。
    //    roleName
    //    玩家在游戏内的角色名称，NSString。
    //    serverId
    //    玩家当前登录的服务器ID，NSString。
    static void LJStatistics_onRegister(const char *platformId, const char *roleId, const char *roleName, const char *serverId);
    //    3.1.3 角色登陆统计接口
    //    此接口在玩家游戏角色登陆服务器成功后调用。
    //    调用方式：
    //    [[LJStatistics getInstance] onLogin:@"2" roleId:@"8" roleName:@"4" serverId:@"5"];
    //    参数说明：
    //    platformId
    //    渠道登陆返回的渠道用户ID，NSString。
    //    roleId
    //    玩家在游戏内的角色ID，NSString。
    //    roleName
    //    玩家在游戏内的角色名称，NSString。
    //    serverId
    //    玩家当前登录的服务器ID，NSString。
    //    使用范例：
    static void LJStatistics_onLogin(const char *platformId, const char *roleId, const char *roleName, const char *serverId);
    //    3.1.5 支付统计接口
    //    此接口在游戏从后台切换到前台时调用。
    //    调用方式：
    //    [[LJStatistics getInstance] onPay:@"1" roleId:@"2" roleName:@"3" serverId:@"4" payType:8 appOrder:@"5" channelOrder:@"6" productId:@"7" productPrice:@"8"];
    //    参数说明：
    //    参数
    //    参数说明
    //    context
    //    应用上下文
    //    platformId
    //    渠道登陆返回的渠道用户ID，String。
    //    roleId
    //    玩家在游戏内的角色ID，String。
    //    roleName
    //    玩家在游戏内的角色名称，String。
    //    serverId
    //    玩家当前登录的服务器ID，String。
    //    PAY_TYPE
    //    支付类型，用于区分是否是应用宝支付，取值：PAY_TYPE.YINGYONGBAO、PAY_TYPE.OTHER
    //    appOrder
    //    支付时的订单号，String。
    //    channelOrder
    //    支付时渠道的订单号，String。没有可以填“”。
    //    productId
    //    商品id，String。
    //    productPrice
    //    商品价格，浮点数，String。

    static void LJStatistics_onPay(const char *platformId, const char *roleId, const char *roleName, const char *serverId, int PAY_TYPE, const char *appOrder, const char *channelOrder, const char *productId, const char *productPrice);
    
    static std::string getPasteBoard();
protected:

public:
	static LoginCallback s_loginResultCallback;
	static Callback s_logoutCallback;
	static PayCallback s_payCallback;
	static Callback s_exitCallback;
};
}
#endif
