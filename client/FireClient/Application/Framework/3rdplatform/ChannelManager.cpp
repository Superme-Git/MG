//  MessageTask.cpp
//  FireClient
#include "stdafx.h"
#include "ChannelManager.h"
#include "MainRoleDataManager.h"
#include "LoginManager.h"
#include "utils/StringCover.h"
#include "DeviceInfo.h"
#include "GameUImanager.h"
#include "MessageManager.h"
#ifdef ANDROID
#include "androidcommon/ChannelPlatformInterface.h"
#include "androidcommon/ChannelPlatformUtil.h"
#include "MainCharacter.h"
#include "GameApplication.h"
#include "../../../common/platform/android/SDJniHelper.h"
#include <jni.h>
#endif

#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "ICSharpCallback.h"
#endif

#ifdef _LOCOJOY_SDK_
//#include "GameTable/fushi/CAddCashlua.h"
#ifdef ANDROID
#include "GameSdk.h"
#else
#import "../../../FireClient/FireClient/GameSdk.h"
#import "../../../FireClient/FireClient/TalkingData/TalkingDataSdk.h"
#endif
#endif

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

#include <cocos2d.h>
#include "network/HttpRequest.h"
#include "network/HttpResponse.h"
#include "SFGameNativeInterface.hpp"

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
#include "WinWebBrowser/WinSDK.h"
#endif

namespace MT3
{
	/*游戏登陆回调类*/
	class SFNativeOnlineLoginCallbackImpl : public SFNativeOnlineLoginCallback, cocos2d::CCObject{
	public:
		SFNativeOnlineLoginCallbackImpl() {
		}

		/*SDK账户登出，如有游戏有账户切换功能，可在此做再次登陆操作*/
		virtual void onLogout(const char* remain) {
#ifdef _YJ_SDK_
#ifdef ANDROID
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("Logout_CalledBySdk"); // 触发调用游戏内的“切换账号”
			LOGE("SFNativeOnlineLoginCallbackImpl onLogout begin");
			SFGameNativeInterface::login("login");
			LOGE("SFNativeOnlineLoginCallbackImpl onLogout end");
#else

#endif
#endif // _YJ_SDK_

		}

		/*SDK登陆成功回调，此回调为客户端返回，需要去游戏服务器验证是否真正登陆成功*/
		virtual void onLoginSuccess(SFNativeOnlineUser* user, const char* remain) {
#ifdef _YJ_SDK_
#ifdef ANDROID
			LOGE("SFNativeOnlineLoginCallbackImpl onLoginSuccess productCode=%s,channelId=%s,channelUserId=%s,token=%s",
				user->productCode, user->channelId, MT3::ChannelManager::urlencode(user->channelUserId).c_str(), MT3::ChannelManager::urlencode(user->token).c_str());
			LOGE("SFNativeOnlineLoginCallbackImpl onLoginSuccess gGetLoginManager()->getEnterMainStatus()=%d", gGetLoginManager()->getEnterMainStatus());
#endif
			// 兼容GameSDK
			int nChannelId = 0;
			MT3::JniMethodInfo methodInfo;
			if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
				"com/locojoy/mini/mt3/GameApp",
				"GetYJChannelId",
				"()I"))
			{
				nChannelId = methodInfo.env->CallStaticIntMethod(methodInfo.classID, methodInfo.methodID);
				methodInfo.env->DeleteLocalRef(methodInfo.classID);
			}

			//渠道用户ID对比
			// std::string  yjUserId = MT3::ChannelManager::urlencode(user->channelUserId);
			// std::string yjLUserId = ws2s(gGetLoginManager()->GetYJUserId());
			// if ((strcmp(yjUserId.c_str(), yjLUserId.c_str()) == 0) || yjLUserId.empty())
			// {
				std::string strDeviceID = DeviceInfo::sGetDeviceID();
				std::string strMacAddress = DeviceInfo::sGetMacAddress();
				gGetLoginManager()->SetPassword(StringCover::to_wstring("123"));
				char aAccount[128] = "";
				sprintf(aAccount, "%s,%s", MT3::ChannelManager::urlencode(user->channelUserId).c_str(), strMacAddress.c_str());
				gGetLoginManager()->SetAccountInfo(StringCover::to_wstring(aAccount));
				gGetLoginManager()->SaveAccount();
				gGetLoginManager()->SetChannelId(StringCover::intTowstring(nChannelId));
				gGetLoginManager()->SetDeviceid(StringCover::to_wstring(strMacAddress.empty() ? strDeviceID.c_str() : strMacAddress.c_str()));

				gGetLoginManager()->SetYJAppId(s2ws(user->productCode));
				gGetLoginManager()->SetYJChannelId(s2ws(user->channelId));
				gGetLoginManager()->SetYJUserId(s2ws(MT3::ChannelManager::urlencode(user->channelUserId)));
				gGetLoginManager()->SetYJToken(s2ws(MT3::ChannelManager::urlencode(user->token)));
				if (gGetLoginManager()->getEnterMainStatus() == eEnterMainStatus_None)
					gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_LoginSuccess); // SDK登陆成功
				else {
					gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_GamePlaying);
					cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("SelectServerEntry_EnableClick", true); // 设置选服界面按钮可点击
				}
			// }
			// else
			// {
			// cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("Logout_CalledBySdk"); // 触发调用游戏内的“切换账号”
			// gGetLoginManager()->SetYJUserId(s2ws(MT3::ChannelManager::urlencode(user->channelUserId)));
			// LOGE("SFNativeOnlineLoginCallbackImpl onLoginSuccess switch begin");
			// SFGameNativeInterface::login("login");
			// LOGE("SFNativeOnlineLoginCallbackImpl onLoginSuccess switch end");
			// }
#endif // _YJ_SDK_
		}

		/*登陆失败回调*/
		virtual void onLoginFailed(const char* r, const char* remain) {
#ifdef _YJ_SDK_
#ifdef ANDROID
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("Logout_CalledBySdk"); // 触发调用游戏内的“切换账号”
			LOGE("SFNativeOnlineLoginCallbackImpl onLoginFailed begin");
			SFGameNativeInterface::login("login");
			LOGE("SFNativeOnlineLoginCallbackImpl onLoginFailed end");

#endif
			//CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmEscape, gGetGameUIManager());
			//CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, gGetGameUIManager());
			//gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
#endif // _YJ_SDK_
		}

		void callLogin(float fDelta) {
#ifdef _YJ_SDK_
#ifdef ANDROID
			SFGameNativeInterface::login("login");
#else

#endif
#endif // _YJ_SDK_
		}
	};

	/*游戏退出回调类*/
	class SFNativeOnlineExitCallbackImpl : public SFNativeOnlineExitCallback {
		/*SDK没有退出界面时，走此方法，可以自定义退出界面*/
		virtual void onNoExiterProvide() {
#ifdef _YJ_SDK_
#ifdef ANDROID
			LOGE("SFNativeOnlineLoginCallbackImpl onNoExiterProvide begin");
			MT3::JniMethodInfo methodInfo;
			if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
				"com/locojoy/mini/mt3/GameApp",
				"showExitDialog",
				"()V"))
			{
				methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID);
				methodInfo.env->DeleteLocalRef(methodInfo.classID);
			}
			LOGE("SFNativeOnlineLoginCallbackImpl onNoExiterProvide end");
#else

#endif
#endif // _YJ_SDK_
			
		}

		/*SDK有退出界面时调用此方法
		* result
		* 		true：退出
		* 		false：取消退出*/
		virtual void onSDKExit(bool result) {
			if (!result)
				return;
#ifdef ANDROID
			LOGE("SFNativeOnlineExitCallbackImpl : onSDKExit ");
#endif
#ifdef _YJ_SDK_
#ifdef ANDROID
			SFGameNativeInterface::onDestroy();

			MT3::JniMethodInfo methodInfo;
			if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
				"com/locojoy/mini/mt3/GameApp",
				"ExitGame",
				"()V"))
			{
				methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID);
				methodInfo.env->DeleteLocalRef(methodInfo.classID);
			}
#else

#endif
#endif // _YJ_SDK_
		}

	};

	class SFNativeOnlinePayResultCallbackImpl : public SFNativeOnlinePayResultCallback {

		virtual void onFailed(const char* remain) {
			//stringstream ss;
			//ss << "支付失败:";
			//ss << remain << ';';
			//textPayInfo->setString(ss.str());
#ifdef _YJ_SDK_
#ifdef ANDROID
			LOGE("SFNativeOnlinePayResultCallbackImpl : onFailed");
#else

#endif
#endif // _YJ_SDK_

		}

		virtual void onSuccess(const char* remain) {

			//stringstream ss;
			//ss << "支付成功:";
			//ss << remain << ';';
			//textPayInfo->setString(ss.str());
#ifdef _YJ_SDK_
#ifdef ANDROID
			LOGE("SFNativeOnlinePayResultCallbackImpl : onSuccess");
#else

#endif
#endif // _YJ_SDK_

		}

		virtual void onOderNo(const char* orderNo) {
			//stringstream ss;
			//ss << "订单号:";
			//ss << orderNo << ';';
			//textPayInfo->setString(ss.str());
#ifdef _YJ_SDK_
#ifdef ANDROID
			LOGE("SFNativeOnlinePayResultCallbackImpl  onOderNo=%s", orderNo);
#else

#endif
#endif // _YJ_SDK_
		}

	};

	SFNativeOnlineExitCallbackImpl* exitCallback = new SFNativeOnlineExitCallbackImpl();
	SFNativeOnlinePayResultCallbackImpl* payCallback = new SFNativeOnlinePayResultCallbackImpl();
	SFNativeOnlineLoginCallbackImpl* loginCallback = new SFNativeOnlineLoginCallbackImpl();

	void ChannelManager::ExitChannelManager()
	{
		if (exitCallback)
		{
			delete exitCallback;
			exitCallback = NULL;
		}

		if (payCallback)
		{
			delete payCallback;
			payCallback = NULL;
		}

		if (loginCallback)
		{
			delete loginCallback;
			loginCallback = NULL;
		}
	}

	bool ChannelManager::isDefineSDK()
	{
#if defined(_LOCOJOY_SDK_) || defined(_YJ_SDK_)
		return true;
#else
		return false;
#endif
	}

	void ChannelManager::ChangeUserLogin()
	{
	}

    void ChannelManager::LogoutAndRelogin()
    {
    }

    int ChannelManager::SupportFeature(int feature) // return 1-true, 0-false
    {
		return 0;
	}

	int ChannelManager::StartBuyYuanbao(int64_t sn, std::wstring& name, int goodid, int goodnum, int price, int zoneid)
	{
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
		if (gGetGameApplication()->IsUseSDKInWindows()) {
			std::string strFoodID = "";
			cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
			if (pScriptEngine)
			{
				CallLuaUtil util;
				util.addArg(goodid);
				if (util.callLua(pScriptEngine->getLuaState(), "GetConsumeFoodId"))
				{
					strFoodID = util.getRetCString();
				}
			}
			WinSDK::getInstance()->openChargeUrl(strFoodID, goodnum);
			return 0;
		}
#endif

#ifdef _LOCOJOY_SDK_
        
        char cpinfo[512];
		sprintf(cpinfo, "%d|%s|%lld|%s|%s|%s|",gGetLoginManager()->getServerID(), ws2s(gGetLoginManager()->GetChannelId()).c_str(), GetMainRoleDataNumValue<int64_t>("roleid"),StringCover::to_string(gGetLoginManager()->GetIp()).c_str(),StringCover::to_string(gGetLoginManager()->GetDeviceid()).c_str(),StringCover::int64_tToString(sn).c_str());
		std::string strFoodID = "";
		std::string strFoodName = "";
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine)
		{
			CallLuaUtil util;
			util.addArg(goodid);
			if (util.callLua(pScriptEngine->getLuaState(), "GetConsumeFoodId"))
			{
				strFoodID = util.getRetCString();
			}
			if (util.callLua(pScriptEngine->getLuaState(), "GetConsumeFoodName"))
			{
				strFoodName = util.getRetCString();
			}
		}
		int sellpricenum = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetConsumeSellpricenum", goodid);
		char aserverid[128];
        sprintf(aserverid,"%d",gGetLoginManager()->getServerID());

		std::string strCharacterID = StringCover::int64_tToString(GetMainRoleDataNumValue<int64_t>("roleid"));
		std::string strCharactName = GetMainRoleDataStrValue("strName");
        std::string strServerID = StringCover::intToString(gGetLoginManager()->getServerID());
        
#ifdef ANDROID
		LOGE("StartBuyYuanbao... price = %d", price);
		if (getIsYingYongBao())
		{
			LOGE("StartBuyYuanbao... yingyongbao price = %d", price);
			pay("",
				"",
				"",
				price,
				0,
				0,
				"",
				"",
				0,
				0,
				aserverid,
				"",
				"");
		}
		else
		{
			LOGE("ChannelManager::StartBuyYuanbao strFoodID=%s, strFoodName=%s", strFoodID.c_str(), strFoodName.c_str());
			pay(strFoodID.c_str(),
				strFoodName.c_str(),
				strFoodName.c_str(),
				sellpricenum * 100,
				1,
				1,
				strCharacterID.c_str(),
				strCharactName.c_str(),
				GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL),
				0,
				aserverid,
				StringCover::to_string(gGetLoginManager()->GetSelectServer()).c_str(),
				cpinfo);
		}
#else
        char aprise[128];
		sprintf(aprise, "%d", sellpricenum * 100);
		SDK::GameSdk::pay(1, sellpricenum * 100, strFoodID.c_str(), strFoodID.c_str(), strCharacterID.c_str(), strCharactName.c_str(), aserverid, cpinfo, NULL);
		onpay_sta("",strCharacterID.c_str(), strCharactName.c_str(), strServerID.c_str(),1,"","",strFoodID.c_str(),aprise);
#endif
#endif

#ifdef _YJ_SDK_
#ifdef ANDROID
		LOGE("ChannelManager::StartBuyYuanbao _YJ_SDK_ begin");
		std::string strFoodID = "";
		std::string strFoodName = "";
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine)
		{
			CallLuaUtil util;
			util.addArg(goodid);
			if (util.callLua(pScriptEngine->getLuaState(), "GetConsumeFoodId"))
			{
				strFoodID = util.getRetCString();
			}
			if (util.callLua(pScriptEngine->getLuaState(), "GetConsumeFoodName"))
			{
				strFoodName = util.getRetCString();
			}
		}
		int sellpricenum = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetConsumeSellpricenum", goodid);
		LOGE("ChannelManager::StartBuyYuanbao strFoodID=%s, sellpricenum=%d", strFoodID.c_str(), sellpricenum);

		char cbi[512];
		sprintf(cbi, "%d|%s|%lld|%s|%s|88|%s|%s|",
			gGetLoginManager()->getServerID(),
			ws2s(gGetLoginManager()->GetChannelId()).c_str(),
			GetMainRoleDataNumValue<int64_t>("roleid"),
			StringCover::to_string(gGetLoginManager()->GetIp()).c_str(),
			StringCover::to_string(gGetLoginManager()->GetDeviceid()).c_str(),
			strFoodID.c_str(),
			StringCover::int64_tToString(sn).c_str());
		LOGE("ChannelManager::StartBuyYuanbao cbi=%s", cbi);

		std::string strCharacterID = StringCover::int64_tToString(GetMainRoleDataNumValue<int64_t>("roleid"));
		std::string strCharactName = GetMainRoleDataStrValue("strName");
		std::string strServerID = StringCover::intToString(gGetLoginManager()->getServerID());
		LOGE("ChannelManager::StartBuyYuanbao strCharacterID=%s, strCharactName=%s, strServerID=%s", strCharacterID.c_str(), strCharactName.c_str(), strServerID.c_str());

		SFGameNativeInterface::setPayResultCallback(payCallback);
		/* charge非定额计费接口
		* 	static void payExtend(int unitPrice, const char*  unitName,
		*const char*  itemCode, const char*  remain,int count, const char*  callBackInfo, const char* callBackUrl);
		* @param unitPrice        定额支付总金额，单位为人民币分
		* @param unitName     虚拟货币名称
		* @param count       购买虚拟货币数量
		* @param callBackInfo由游戏开发者定义传入的字符串，回与支付结果一同发送给游戏服务器，游戏服务器可通过该字段判断交易的详细内容（金额 角色等）
		* @param callBackUrl  该比支付结果通知给游戏服务器时的通知地址url，交易结束后，系统会向该url发送http请求，通知交易的结果金额callbackInfo等信息
		*/
		LOGE("SFGameNativeInterface::pay unitPrice=%d, unitName=%s, count=%d, callBackInfo=%s", sellpricenum * 100, strFoodID.c_str(), goodnum, cbi);
		SFGameNativeInterface::pay(sellpricenum * 100, strFoodName.c_str(), goodnum, cbi, "");
		LOGE("ChannelManager::StartBuyYuanbao _YJ_SDK_ end");
#else
#endif
#endif // _YJ_SDK_
		return 0;
	}

	void ChannelManager::EnterPlatformCenter()
	{
	}

	void ChannelManager::UserFeedBack()
	{

    }
    
    void ChannelManager::EnterBBS()
    {
    }

    int ChannelManager::HasPlatformCenter()
    {
		return 0;
    }
    
    int ChannelManager::HasPlatformForum()
    {
		return 0;		
    }
    
    int ChannelManager::HasFeedback()
    {
		return 0;
    }
    
    std::wstring ChannelManager::GetPlatformCenterName()
    {
		return L"";
    }
    
    std::wstring ChannelManager::GetPlatformForumName()
    {
		return L"WP8";
    }
    
    std::wstring ChannelManager::GetPlatformFeedbackName()
    {
		return L"";
    }
    
    std::wstring ChannelManager::GetPlatformLoginSuffix()
    {
        return StringCover::to_wstring("");
    }
    
    int ChannelManager::IsTrdPlatform()
    {
        return 0;
    }
    
    std::wstring ChannelManager::GetCur3rdPlatform()
    {
        return StringCover::to_wstring("");
    }
    
    int ChannelManager::IsAndroid()
    {
#ifdef ANDROID
        return 1;
#endif
        return 0;
    }
    
	int ChannelManager::IsSupportUserCenter()
	{
		{
#ifdef ANDROID
#ifdef _LOCOJOY_SDK_
			if (isFunctionSupported("enterPlatform"))
			{
				return isEnterPlatform();
			}
			return 0;
#endif
#endif
			return 0;
		}
	}

	void ChannelManager::openUserCenter()
	{
#ifdef ANDROID
#ifdef _LOCOJOY_SDK_
		enterPlatform();
#endif
#endif
	}

    void ChannelManager::RemoveRCPFiles(int day)
    {

    }
    
    void ChannelManager::EnterCustomerService()
    {

    }
    
    void ChannelManager::EnterGameHelp(std::wstring url)
    {

    }
    void ChannelManager::FacebookInvite(std::wstring roleid,int serverid)
    {
        if (! ChannelManager::IsAndroid()) {
 
        }
    }
    void ChannelManager::CommonShare(std::wstring title, std::wstring content, std::wstring picUrl, std::wstring link, std::wstring comment, std::wstring image)
    {
        if (!ChannelManager::IsAndroid()) {
        }
    }
    bool ChannelManager::IsKorea()
    {
        std::wstring suff = ChannelManager::GetPlatformLoginSuffix();
        if (suff ==  StringCover::to_wstring("krgp") || suff ==  StringCover::to_wstring("krts") || suff ==  StringCover::to_wstring("krnv") || suff ==  StringCover::to_wstring("krlg") || suff ==  StringCover::to_wstring("kris")){
            return true;
        }
        else{
            return false;
        }
        
    }
    void ChannelManager::CommonInterface(int p1,int p2,int p3,int p4,int p5,int p6, std::wstring s1, std::wstring s2, std::wstring s3, std::wstring s4, std::wstring s5, std::wstring s6)
    {
  
    }
    std::wstring ChannelManager::GetFlurryId()
    {
        return L"";
    }
	void ChannelManager::onRegister_sta(const char* roleid, const char* rolename)
	{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
	//onRegisterAndroid("",roleid, rolename, StringCover::intToString(gGetLoginManager()->getServerID()).c_str());
	int64_t rolecreatetime = 0;
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "GetRoleCreateTime"))
		{
			rolecreatetime = util.getRetInt();
		}
	}

	LOGE("createRoleStat...  servertime=%lld", rolecreatetime);
	createRoleStat(roleid,rolename,"1",StringCover::intToString(gGetLoginManager()->getServerID()).c_str(),StringCover::to_string(gGetLoginManager()->GetSelectServer()).c_str(),StringCover::int64_tToString(rolecreatetime).c_str());
	bool is_init = false;
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "Config_IsLocojoy"))
		{
			is_init =  util.getRetBool();
		}
	}
	if ( is_init ){
		LOGE("tdInitCpp");
		tdInitCpp();
	}
		
#elif defined(OS_IOS)
    SDK::GameSdk::LJStatistics_onRegister("",roleid, rolename, StringCover::intToString(gGetLoginManager()->getServerID()).c_str());
        TalkingDataSdk::init(StringCover::to_string(gGetLoginManager()->GetChannelId()).c_str());
#else


#endif
#endif //end of _LOCOJOY_SDK_
#ifdef _YJ_SDK_
#ifdef ANDROID
		setYJData_CreateRole("createrole",roleid,rolename);
#endif // ANDROID
#endif
    }
    void ChannelManager::onLogin_sta()
	{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
    	onEnterGameStat();
#elif defined(OS_IOS)
		SDK::GameSdk::LJStatistics_onLogin("", StringCover::int64_tToString(GetMainRoleDataNumValue<int64_t>("roleid")).c_str(), GetMainRoleDataStrValue("strName").c_str(), StringCover::intToString(gGetLoginManager()->getServerID()).c_str());
#else
#endif
#endif //end of _LOCOJOY_SDK_

#ifdef _YJ_SDK_
#ifdef ANDROID
		onEnterGameStat();
#else
#endif
#endif
    }
    void ChannelManager::onpay_sta(const char *platformId, const char *roleId, const char *roleName, const char *serverId, int PAY_TYPE, const char *appOrder, const char *channelOrder, const char *productId, const char *productPrice)
	{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
        
#elif defined(OS_IOS)
		SDK::GameSdk::LJStatistics_onPay(platformId, roleId, roleName, serverId, PAY_TYPE, appOrder, channelOrder, productId, productPrice);
#else
#endif
#endif //end of _LOCOJOY_SDK_
    }

	void ChannelManager::onEnterGameStat()
	{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
		//LOGE("onEnterGameStat");
		//LOGE("onEnterGameStat... roleId=%s, roleName = %s, roleLevel = %s, serverId = %s, serverName = %s",
		//		StringCover::intToString((int)GetMainRoleDataNumValue<int64_t>("roleid")).c_str(),
		//		GetMainRoleDataStrValue("strName").c_str(),
		//		StringCover::intToString((int)GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)).c_str(),
		//		StringCover::intToString((int)gGetLoginManager()->getServerID()).c_str(),
		//		StringCover::to_string(gGetLoginManager()->GetSelectServer()).c_str());
		int64_t rolecreatetime = 0;
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine)
		{
			CallLuaUtil util;
			if (util.callLua(pScriptEngine->getLuaState(), "GetRoleCreateTime"))
			{
				rolecreatetime = util.getRetInt();
			}
		}

		LOGE("onEnterGameStat...  servertime=%lld", rolecreatetime);
		enterGameStat(StringCover::int64_tToString(GetMainRoleDataNumValue<int64_t>("roleid")).c_str(),
				GetMainRoleDataStrValue("strName").c_str(),
				StringCover::intToString((int)GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)).c_str(),
				StringCover::intToString((int)gGetLoginManager()->getServerID()).c_str(),
				StringCover::to_string(gGetLoginManager()->GetSelectServer()).c_str(),
				StringCover::int64_tToString(rolecreatetime).c_str());
#elif defined(OS_IOS)
#endif // ANDROID

#endif // _LOCOJOY_SDK_

#ifdef _YJ_SDK_
#ifdef ANDROID
		LOGE("ChannelManager::onEnterGameStat() setRoleData begin");
		SFGameNativeInterface::setRoleData(StringCover::int64_tToString(GetMainRoleDataNumValue<int64_t>("roleid")).c_str(),
				GetMainRoleDataStrValue("strName").c_str(),
				StringCover::intToString((int)GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)).c_str(),
				StringCover::intToString((int)gGetLoginManager()->getServerID()).c_str(),
				StringCover::to_string(gGetLoginManager()->GetSelectServer()).c_str());
		LOGE("ChannelManager::onEnterGameStat() setRoleData end");
		//setYJData("createrole");
		setYJData("enterServer");
#endif // ANDROID
#endif
	}

	void ChannelManager::onRecordRoleInf()
	{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
		int64_t rolecreatetime = 0;
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine)
		{
			CallLuaUtil util;
			if (util.callLua(pScriptEngine->getLuaState(), "GetRoleCreateTime"))
			{
				rolecreatetime = util.getRetInt();
			}
		}

		LOGE("onRecordRoleInf...  servertime=%lld", rolecreatetime);
		recordRoleInf(StringCover::int64_tToString(GetMainRoleDataNumValue<int64_t>("roleid")).c_str(),
				GetMainRoleDataStrValue("strName").c_str(),
				StringCover::intToString((int)GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)).c_str(),
				StringCover::intToString((int)gGetLoginManager()->getServerID()).c_str(),
				StringCover::to_string(gGetLoginManager()->GetSelectServer()).c_str(),
				StringCover::int64_tToString(rolecreatetime).c_str());
#elif defined(OS_IOS)
#endif // ANDROID

#endif // _LOCOJOY_SDK_

#ifdef _YJ_SDK_
#ifdef ANDROID
		setYJData("levelup");
#endif // ANDROID
#endif

	}

	void ChannelManager::qqLogin()
	{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
		login();
#endif // ANDROID
#endif // _LOCOJOY_SDK_
	}

	void ChannelManager::wxLogin()
	{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
		wxYYBLogin();
#endif // ANDROID
#endif // _LOCOJOY_SDK_
	}

	static bool m_isYingyongbao = false;
	static bool m_isYYBLogined = false;

	void ChannelManager::setIsYingYongBao(bool isyingyongbao)
	{
		m_isYingyongbao = isyingyongbao;
		gGetLoginManager()->setIsYingYongBao(isyingyongbao);
	}

	bool ChannelManager::getIsYingYongBao()
	{
		return m_isYingyongbao;
	}

	void ChannelManager::setIsYYBLogined(bool isYYBLogined)
	{
		m_isYYBLogined = isYYBLogined;
	}

	bool ChannelManager::getIsYYBLogined()
	{
		return m_isYYBLogined;
	}

	std::string ChannelManager::urlencode(std::string str_source) {
		char const *in_str = str_source.c_str();
		int in_str_len = strlen(in_str);
		int out_str_len = 0;
		std::string out_str;
		register unsigned char c;
		unsigned char *to, *start;
		unsigned char const *from, *end;
		unsigned char hexchars[] = "0123456789ABCDEF";

		from = (unsigned char *)in_str;
		end = (unsigned char *)in_str + in_str_len;
		start = to = (unsigned char *)malloc(3 * in_str_len + 1);

		while (from < end) {
			c = *from++;

			if (c == ' ') {
				*to++ = '+';
			}
			else if ((c < '0' && c != '-' && c != '.') || (c < 'A' && c > '9')
				|| (c > 'Z' && c < 'a' && c != '_') || (c > 'z' && c != '~')) {
				to[0] = '%';
				to[1] = hexchars[c >> 4];
				to[2] = hexchars[c & 15];
				to += 3;
			}
			else {
				*to++ = c;
			}
		}
		*to = 0;

		out_str_len = to - start;
		out_str = (char *)start;
		free(start);
		return out_str;
	}

	//易接SDK登陆
	void ChannelManager::yj_sdkLogin()
	{
#ifdef _YJ_SDK_
#ifdef ANDROID
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("Config.setCur3rdPlatform", "app");
		LOGE("ChannelManager::yj_sdkLogin begin");
		/*设置登陆回调*/
		SFGameNativeInterface::setLoginCallback(loginCallback);
		/*调用登陆接口*/
		SFGameNativeInterface::login("login");
		LOGE("ChannelManager::yj_sdkLogin end");
#else

#endif
#endif // _YJ_SDK_
	}

	void ChannelManager::yj_loginWhenFailed()
	{
#ifdef _YJ_SDK_
#ifdef ANDROID
		LOGE("ChannelManager::yj_loginWhenFailed begin");
		SFGameNativeInterface::login("login");
		LOGE("ChannelManager::yj_loginWhenFailed end");
#else

#endif
#endif // _YJ_SDK_
	}

	//易接SDK登出
	void ChannelManager::yj_sdkLogout()
	{
#ifdef _YJ_SDK_
#ifdef ANDROID
		LOGE("ChannelManager::yj_sdkLogout begin");
		SFGameNativeInterface::logout("logout");
		LOGE("ChannelManager::yj_sdkLogout end");
#else

#endif
#endif // _YJ_SDK_
	}

	//易接SDK退出
	void ChannelManager::yj_sdkExit()
	{
#ifdef _YJ_SDK_
#ifdef ANDROID
		LOGE("ChannelManager::yj_sdkExit begin");
		/*设置退出回调*/
		SFGameNativeInterface::setExitCallback(exitCallback);
		/*调用SDK退出方法*/
		SFGameNativeInterface::exit();
		LOGE("ChannelManager::yj_sdkExit end");
#else

#endif
#endif // _YJ_SDK_
	}

	void ChannelManager::yj_onDestroy()
	{
#ifdef _YJ_SDK_
#ifdef ANDROID
		LOGE("ChannelManager::yj_onDestroy begin");
		SFGameNativeInterface::onDestroy();
		LOGE("ChannelManager::yj_onDestroy end");
#else

#endif
#endif // _YJ_SDK_
	}

	void ChannelManager::setYJData(const char * key)
	{
#ifdef _YJ_SDK_
#ifdef ANDROID
		LOGE("ChannelManager::setYJData setData begin");
		std::string dataString = "{";
		dataString += "\"roleId\":\"" + StringCover::int64_tToString(GetMainRoleDataNumValue<int64_t>("roleid")) + "\",";
		dataString += "\"roleName\":\"" +  GetMainRoleDataStrValue("strName") + "\",";
		dataString += "\"roleLevel\":\"" +   StringCover::intToString((int)GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)) + "\",";
		dataString += "\"zoneId\":\"" +  StringCover::intToString((int)gGetLoginManager()->getServerID()) + "\",";
		dataString += "\"zoneName\":\"" +  StringCover::to_string(gGetLoginManager()->GetSelectServer()) + "\",";
		dataString += "\"balance\":\"" +  std::string("0") + "\",";
		dataString += "\"vip\":\"" +  std::string("1") + "\",";
		dataString += "\"partyName\":\"" +  std::string("none") + "\",";
		dataString += "\"roleCTime\":\"" +  std::string("21322222") + "\",";
		dataString += "\"roleLevelMTime\":\"" +  std::string("54456556");
		dataString += "\"}";
		SFGameNativeInterface::setData(key, dataString.c_str());
		LOGE("ChannelManager::setYJData setData end");
#endif // ANDROID
#endif
	}

	void ChannelManager::setYJData_CreateRole(const char * key, const char * roleid, const char * rolename)
	{
#ifdef _YJ_SDK_
#ifdef ANDROID
		LOGE("ChannelManager::setYJData setData begin");
		std::string dataString = "{";
		dataString += "\"roleId\":\"" + std::string(roleid) + "\",";
		dataString += "\"roleName\":\"" + std::string(rolename) + "\",";
		dataString += "\"roleLevel\":\"" + StringCover::intToString((int)GetMainRoleDataAttr(fire::pb::attr::AttrType::LEVEL)) + "\",";
		dataString += "\"zoneId\":\"" + StringCover::intToString((int)gGetLoginManager()->getServerID()) + "\",";
		dataString += "\"zoneName\":\"" + StringCover::to_string(gGetLoginManager()->GetSelectServer()) + "\",";
		dataString += "\"balance\":\"" + std::string("0") + "\",";
		dataString += "\"vip\":\"" + std::string("1") + "\",";
		dataString += "\"partyName\":\"" + std::string("none") + "\",";
		dataString += "\"roleCTime\":\"" + std::string("21322222") + "\",";
		dataString += "\"roleLevelMTime\":\"" + std::string("54456556");
		dataString += "\"}";
		SFGameNativeInterface::setData(key, dataString.c_str());
		LOGE("ChannelManager::setYJData setData end");
#endif // ANDROID
#endif
	}
	std::string ChannelManager::getClipboardText()
	{
		std::string str = "";
	#ifdef ANDROID
		MT3::JniMethodInfo methodInfo;
		if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
			"com/locojoy/mini/mt3/GameApp",
			"getClipBoardText",
			"()Ljava/lang/String;"))
		{
			jstring text = (jstring)methodInfo.env->CallStaticObjectMethod(methodInfo.classID, methodInfo.methodID);
			methodInfo.env->DeleteLocalRef(methodInfo.classID);
			str = methodInfo.env->GetStringUTFChars(text, false);
		}
	#endif
		return str;
	}
}//namespace MT3

