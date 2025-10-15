#include <jni.h>
#include <string>
#include "SDJniHelper.h"
#include "GameSdk.h"
#include "LoginManager.h"
#include "StringCover.h"
#include "DeviceInfo.h"
#include "GameUImanager.h"
#include "ConfigManager.h"
#include "MessageManager.h"
#include "ChannelManager.h"

#include "CallLuaUtil.h"

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

// 此方式供测试用，可以弹出一个alert dialog
void showDialog(const char *pszTitle, const char *pszContent)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "showDialog",
			"(Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring jTitle = t.env->NewStringUTF(pszTitle);
		jstring jContent = t.env->NewStringUTF(pszContent);

		t.env->CallStaticVoidMethod(t.classID, t.methodID, jTitle, jContent);

		t.env->DeleteLocalRef(jTitle);
		t.env->DeleteLocalRef(jContent);

		t.env->DeleteLocalRef(t.classID);
	}
}
void Java_com_locojoy_sdk_GameSDK_initCb(JNIEnv *env, jclass obj, jint flags, jstring channel)
{
	bool isInited = false;
	std::string strChannel = "";

	if (flags == 1)
		isInited = true;
	const char * pszChannel = env->GetStringUTFChars(channel, NULL);
	strChannel = pszChannel;
	env->ReleaseStringUTFChars(channel, pszChannel);

	LOGE("Java_com_locojoy_sdk_GameSDK_initCb flags=%d, pszChannel=%s", flags, pszChannel);

	// 做回调处理
	if (isInited)
	{
		// 初始化成功，可以调用其它
		//showDialog("init", "SUCCESS");
	}
	else
	{
		if (gGetMessageManager()) {
			CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmInitEscape, gGetGameUIManager());
			CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, gGetGameUIManager());
			gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
		}
	}

}

void Java_com_locojoy_activecode_ActiveCode_doActiveCodeResult(JNIEnv *env, jclass obj, jint flags, jstring platformId, jstring channel){
	LOGE("Java_com_locojoy_activecode_ActiveCode_doActiveCodeResult start... flags=%d, EnterMainStatus=%d, ", flags, gGetLoginManager()->getEnterMainStatus());

	if (flags == 1) {
		if (gGetLoginManager()->getEnterMainStatus() == eEnterMainStatus_None)
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_LoginSuccess); // SDK登陆成功
		else {
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_GamePlaying);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("SelectServerEntry_EnableClick", true); // 设置选服界面按钮可点击
		}
	}
	else{
		login();
	}

	LOGE("Java_com_locojoy_activecode_ActiveCode_doActiveCodeResult end... flags=%d, EnterMainStatus=%d, ", flags, gGetLoginManager()->getEnterMainStatus());
}

void Java_com_locojoy_sdk_GameSDK_loginCb(JNIEnv *env, jclass obj, jint flags, jstring channel, jstring platformid, jstring session)
{
	bool isLogined = false;
	std::string strChannel = "";
	std::string strPlatformId = "";
	std::string strSession = "";

	if (flags == 1)
		isLogined = true;

	const char *pszChannel = env->GetStringUTFChars(channel, NULL);
	const char *pszPlatformid = env->GetStringUTFChars(platformid, NULL);
	const char *pszSession = env->GetStringUTFChars(session, NULL);

	strChannel = pszChannel;
	strPlatformId = pszPlatformid;
	strSession = pszSession;

	env->ReleaseStringUTFChars(channel, pszChannel);
	env->ReleaseStringUTFChars(platformid, pszPlatformid);
	env->ReleaseStringUTFChars(session, pszSession);

	LOGE("Java_com_locojoy_sdk_GameSDK_loginCb flags=%d, pszChannel=%s, pszPlatformid=%s, pszSession=%s", flags, pszChannel, pszPlatformid, pszSession);

	LOGE("Java_com_locojoy_sdk_GameSDK_loginCb start... EnterMainStatus=%d, ", gGetLoginManager()->getEnterMainStatus());

	// 做回调处理
	if (isLogined)
	{
		// 登录成功
		//showDialog("login", std::string(std::string("SUCCESS:\n") + strChannel + std::string("\n") + strPlatformId + std::string("\n") + strSession).c_str());
		std::string strDeviceID = DeviceInfo::sGetDeviceID();
		std::string strMacAddress = DeviceInfo::sGetMacAddress();
		gGetLoginManager()->SetPassword(StringCover::to_wstring(strSession));
		char aAccount[128] = "";
		sprintf(aAccount, "%s@@%s,%s", strPlatformId.c_str(), strChannel.c_str(), strMacAddress.c_str());
		gGetLoginManager()->SetAccountInfo(StringCover::to_wstring(aAccount));
		gGetLoginManager()->SaveAccount();
		gGetLoginManager()->SetDeviceid(StringCover::to_wstring(strMacAddress.empty() ? strDeviceID.c_str() : strMacAddress.c_str()));
		LOGE("Java_com_locojoy_sdk_GameSDK_loginCb start... strMacAddress=%s, strDeviceID= %s", strMacAddress.c_str(), strDeviceID.c_str());
		gGetLoginManager()->SetChannelId(StringCover::to_wstring(strChannel.c_str()));

		//执行lua文件 无验证码逻辑
		if (gGetLoginManager()->getEnterMainStatus() == eEnterMainStatus_None)
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_LoginSuccess); // SDK登陆成功
		else {
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_GamePlaying);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("SelectServerEntry_EnableClick", true); // 设置选服界面按钮可点击
		}

		// 关闭启动时的快速登录界面（除应用宝和易接之外的安卓SDK登陆改版）
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LoginQuickDialog.DestroyDialog");
	}
	else
	{
		CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmEscape, gGetGameUIManager());
		CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, gGetGameUIManager());
		gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
	}

	LOGE("Java_com_locojoy_sdk_GameSDK_loginCb end... EnterMainStatus=%d, ", gGetLoginManager()->getEnterMainStatus());
}

// 应用宝登陆回调相关
static bool bYYBLoginCbCalled = false;
static bool isLogined_YYBLogin = false;
static std::string strChannel_YYBLogin = "";
static std::string strPlatformId_YYBLogin = "";
static std::string strSession_YYBLogin = "";
static std::string strOpenId_YYBLogin = "";
static std::string strOpenKey_YYBLogin = "";
static std::string strPayToken_YYBLogin = "";
static std::string strPf_YYBLogin = "";
static std::string strPfKey_YYBLogin = "";
static std::string strZoneId_YYBLogin = "";
static std::string strPlatformName_YYBLogin = "";

void Java_com_locojoy_sdk_GameSDK_YYBLoginCb(JNIEnv *env, jclass obj, jint flags,
		jstring channel, jstring platformid, jstring session, jstring openid,
		jstring openkey, jstring paytoken, jstring pf, jstring pfkey,
		jstring zoneid, jstring platformname)
{
	const char *pszChannel = env->GetStringUTFChars(channel, NULL);
	const char *pszPlatformid = env->GetStringUTFChars(platformid, NULL);
	const char *pszSession = env->GetStringUTFChars(session, NULL);
	const char *pszOpenid = env->GetStringUTFChars(openid, NULL);
	const char *pszOpenkey = env->GetStringUTFChars(openkey, NULL);
	const char *pszPaytoken = env->GetStringUTFChars(paytoken, NULL);
	const char *pszPf = env->GetStringUTFChars(pf, NULL);
	const char *pszPfkey = env->GetStringUTFChars(pfkey, NULL);
	const char *pszZoneid = env->GetStringUTFChars(zoneid, NULL);
	const char *pszPlatformname = env->GetStringUTFChars(platformname, NULL);

	isLogined_YYBLogin = false;
	if (flags == 1) {
		isLogined_YYBLogin = true;
	}
	strChannel_YYBLogin = pszChannel;
	strPlatformId_YYBLogin = pszPlatformid;
	strSession_YYBLogin = pszSession;
	strOpenId_YYBLogin = pszOpenid;
	strOpenKey_YYBLogin = pszOpenkey;
	strPayToken_YYBLogin = pszPaytoken;
	strPf_YYBLogin = pszPf;
	strPfKey_YYBLogin = pszPfkey;
	strZoneId_YYBLogin = pszZoneid;
	strPlatformName_YYBLogin = pszPlatformname;

	env->ReleaseStringUTFChars(channel, pszChannel);
	env->ReleaseStringUTFChars(platformid, pszPlatformid);
	env->ReleaseStringUTFChars(session, pszSession);
	env->ReleaseStringUTFChars(openid, pszOpenid);
	env->ReleaseStringUTFChars(openkey, pszOpenkey);
	env->ReleaseStringUTFChars(paytoken, pszPaytoken);
	env->ReleaseStringUTFChars(pf, pszPf);
	env->ReleaseStringUTFChars(pfkey, pszPfkey);
	env->ReleaseStringUTFChars(zoneid, pszZoneid);
	env->ReleaseStringUTFChars(platformname, pszPlatformname);

	bYYBLoginCbCalled = true;
	LOGE("Java_com_locojoy_sdk_GameSDK_YYBLoginCb flags=%d, pszChannel=%s, pszPlatformid=%s, pszSession=%s", flags, pszChannel, pszPlatformid, pszSession);
}

bool canDoYYBLoginCallback()
{
	return bYYBLoginCbCalled && gGetGameApplication()->isGameInitialized();
}

// 应用宝登陆回调的处理
void onYYBLoginCb()
{
	LOGE("GameSdk onYYBLoginCb isLogined_YYBLogin=%d, strChannel_YYBLogin=%s, strPlatformId_YYBLogin=%s, strSession_YYBLogin=%s,strOpenId_YYBLogin=%s,strOpenKey_YYBLogin=%s,strPayToken_YYBLogin=%s,strPf_YYBLogin=%s,strZoneId_YYBLogin=%s,strPlatformName_YYBLogin=%s",
		isLogined_YYBLogin, strChannel_YYBLogin.c_str(), strPlatformId_YYBLogin.c_str(), strSession_YYBLogin.c_str(), strOpenId_YYBLogin.c_str(), strOpenKey_YYBLogin.c_str(), strPayToken_YYBLogin.c_str(), strPf_YYBLogin.c_str(), strZoneId_YYBLogin.c_str(), strPlatformName_YYBLogin.c_str());

	if (isLogined_YYBLogin)
	{
		// 登录成功
		std::string strDeviceID = DeviceInfo::sGetDeviceID();
		std::string strMacAddress = DeviceInfo::sGetMacAddress();
		gGetLoginManager()->SetPassword(StringCover::to_wstring(strSession_YYBLogin));
		char aAccount[128] = "";
		sprintf(aAccount, "%s@@%s,%s", strPlatformId_YYBLogin.c_str(), strChannel_YYBLogin.c_str(), strMacAddress.c_str());
		gGetLoginManager()->SetAccountInfo(StringCover::to_wstring(aAccount));
		gGetLoginManager()->SaveAccount();
		gGetLoginManager()->SetDeviceid(StringCover::to_wstring(strMacAddress.empty() ? strDeviceID.c_str() : strMacAddress.c_str()));
		gGetLoginManager()->SetChannelId(StringCover::to_wstring(strChannel_YYBLogin.c_str()));

		LOGE("GameSdk onYYBLoginCb aAccount=%s, EnterMainStatus=%d ", aAccount, gGetLoginManager()->getEnterMainStatus());

		//执行lua文件 无验证码逻辑
		if (gGetLoginManager()->getEnterMainStatus() == eEnterMainStatus_None)
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_LoginSuccess); // SDK登陆成功
		else {
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_GamePlaying);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("SelectServerEntry_EnableClick", true); // 设置选服界面按钮可点击
		}

		//showDialog("login", std::string(std::string("SUCCESS:\n") + strChannel + std::string("\n") + strPlatformId + std::string("\n") + strSession + std::string("\n") + strOpenId + std::string("\n") + strOpenKey + std::string("\n") + strPayToken + std::string("\n") + strPf + std::string("\n") + strPfKey + std::string("\n") + strZoneId + std::string("\n") + strPlatformName).c_str());
		gGetLoginManager()->SetOpenId(StringCover::to_wstring(strOpenId_YYBLogin));
		gGetLoginManager()->SetOpenKey(StringCover::to_wstring(strOpenKey_YYBLogin));
		gGetLoginManager()->SetPayToken(StringCover::to_wstring(strPayToken_YYBLogin));
		gGetLoginManager()->SetPf(StringCover::to_wstring(strPf_YYBLogin));
		gGetLoginManager()->SetPfKey(StringCover::to_wstring(strPfKey_YYBLogin));
		gGetLoginManager()->SetZoneId(StringCover::to_wstring(strZoneId_YYBLogin));
		gGetLoginManager()->SetPlatformName(StringCover::to_wstring(strPlatformName_YYBLogin));
		MT3::ChannelManager::setIsYYBLogined(true);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SelectServerEntry_YingYongBaoHide");
	}
	else
	{
		//showDialog("login", "FAIL");
		LOGE("GameSdk onYYBLoginCb strZoneId_YYBLogin=%s,strPlatformName_YYBLogin = %s", strZoneId_YYBLogin.c_str(), strPlatformName_YYBLogin.c_str());
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SelectServerEntry_YingYongBaoShow"); // "鏄剧ず搴旂敤瀹濈殑鐣岄潰"
		if (strcmp(strZoneId_YYBLogin.c_str(), "6") == 0)
		{
			gGetMessageManager()->AddConfirmBox(eConfirmOK, MHSD_UTILS::GetTipsMsg(190062).c_str(), CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent),
											CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent));
		}
	}

	bYYBLoginCbCalled = false;
}


void Java_com_locojoy_sdk_GameSDK_logoutCb(JNIEnv *env, jclass obj, jint flags)
{
	bool isLogouted = false;

	if (flags == 1)
		isLogouted = true;

	// 做回调处理
	if (isLogouted)
	{
		//showDialog("logout", "SUCCESS");
		bool is_RongHe = false;
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine)
		{
			CallLuaUtil util;
			if (util.callLua(pScriptEngine->getLuaState(), "Config_IsRongHe"))
			{
				is_RongHe =  util.getRetBool();
			}
		}
		if (is_RongHe) 
		{
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_SwitchBySdk); // SDK界面切换账号回调
			login();
		}
	}
	else
	{
		//showDialog("logout", "FAIL");
	}
}

void Java_com_locojoy_sdk_GameSDK_switchAccountCb(JNIEnv *env, jclass obj, jint flags, jstring channel, jstring platformid, jstring session)
{
	bool isSwitched = false;
	std::string strChannel = "";
	std::string strPlatformId = "";
	std::string strSession = "";

	if (flags == 1)
		isSwitched = true;

	const char *pszChannel = env->GetStringUTFChars(channel, NULL);
	const char *pszPlatformid = env->GetStringUTFChars(platformid, NULL);
	const char *pszSession = env->GetStringUTFChars(session, NULL);

	strChannel = pszChannel;
	strPlatformId = pszPlatformid;
	strSession = pszSession;

	env->ReleaseStringUTFChars(channel, pszChannel);
	env->ReleaseStringUTFChars(platformid, pszPlatformid);
	env->ReleaseStringUTFChars(session, pszSession);

	LOGE("Java_com_locojoy_sdk_GameSDK_switchAccountCb flags=%d, pszChannel=%s, pszPlatformid=%s, pszSession=%s", flags, pszChannel, pszPlatformid, pszSession);

	LOGE("Java_com_locojoy_sdk_GameSDK_switchAccountCb start... EnterMainStatus=%d, ", gGetLoginManager()->getEnterMainStatus());

	// 做回调处理
	if (isSwitched)
	{
		//showDialog("switchAccount", std::string(std::string("SUCCESS\n") + strChannel + std::string("\n") + strPlatformId + std::string("\n") + strSession).c_str());
		std::string strDeviceID = DeviceInfo::sGetDeviceID();
		std::string strMacAddress = DeviceInfo::sGetMacAddress();
		gGetLoginManager()->SetPassword(StringCover::to_wstring(strSession));
		char aAccount[128] = "";
		sprintf(aAccount, "%s@@%s,%s", strPlatformId.c_str(), strChannel.c_str(), strMacAddress.c_str());
		gGetLoginManager()->SetAccountInfo(StringCover::to_wstring(aAccount));
		gGetLoginManager()->SaveAccount();
		gGetLoginManager()->SetDeviceid(StringCover::to_wstring(strMacAddress.empty() ? strDeviceID.c_str() : strMacAddress.c_str()));
		gGetLoginManager()->SetChannelId(StringCover::to_wstring(strChannel.c_str()));

		if (gGetLoginManager()->getEnterMainStatus() == eEnterMainStatus_None)
		{
			gGetMessageManager()->CloseConfirmBox(eConfirmLoginFail, false);
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_LoginSuccess); // SDK登陆成功
		}
		else
		{
			gGetLoginManager()->setEnterMainStatus(eEnterMainStatus_SwitchBySdk); // SDK界面切换账号回调
		}
	}
	else
	{
		//showDialog("switchAccount", "FAIL");
	    CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmEscape, gGetGameUIManager());
	    CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, gGetGameUIManager());
	    gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
	}

	LOGE("Java_com_locojoy_sdk_GameSDK_switchAccountCb end... EnterMainStatus=%d, ", gGetLoginManager()->getEnterMainStatus());
}

void Java_com_locojoy_sdk_GameSDK_payCb(JNIEnv *env, jclass obj, jint flags, jstring order)
{
	bool success = false;
	std::string strOrder = "";

	if (flags == 1)
		success = true;
	const char *pszOrder = env->GetStringUTFChars(order, NULL);
	strOrder = pszOrder;
	env->ReleaseStringUTFChars(order, pszOrder);

	LOGE("Java_com_locojoy_sdk_GameSDK_payCb ");
	// 做回调处理
	if (success)
	{
		//showDialog("pay", std::string(std::string("SUCCESS\n") + strOrder).c_str());
		LOGE("Java_com_locojoy_sdk_GameSDK_payCb success");
		#ifdef ANDROID
		if (gGetLoginManager()->getIsYingYongBao())
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("YYB_RequestFushi"); // "显示应用宝的界面"
		}
		#endif
	}
	else
	{
		LOGE("Java_com_locojoy_sdk_GameSDK_payCb fail");
		//showDialog("pay", "FAIL");
	}
}

void Java_com_locojoy_sdk_GameSDK_exitSdkCb(JNIEnv *env, jclass obj)
{
	// 做回调处理，一般是退出游戏
	//showDialog("exitSdk", "SUCCESS");
	//gGetLoginManager()->Exit();
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "ExitGame", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

int isFunctionSupported(const char *funcName)
{
	int ret = 0;
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "isFunctionSupported", "(Ljava/lang/String;)I"))
	{
		jstring jFuncName = t.env->NewStringUTF(funcName);

		ret = t.env->CallStaticIntMethod(t.classID, t.methodID, jFuncName);

		t.env->DeleteLocalRef(jFuncName);

		t.env->DeleteLocalRef(t.classID);
	}

	return ret;
}

int isEnterPlatform()
{
	int ret = 0;
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "isEnterPlatform", "()I"))
	{
		ret = t.env->CallStaticIntMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}

	return ret;
}

void login()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "login", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

void wxYYBLogin()
{
	LOGE("GameSdk wxLogin");
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "wxLogin", "()V"))
	{
		LOGE("GameSdk wxLogin11");
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

void init()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "init", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}
void logout()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "logout", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

void pay(const char *pszPid, const char *pszPname, const char *pszDesc, int price, int count, int rate,
		const char *pszRoleId, const char *pszRoleName, int roleLevel, int roleBalance, const char *pszServerId, const char *pszServerName, const char *pszCp)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "pay",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IIILjava/lang/String;Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring jPid = t.env->NewStringUTF(pszPid);
		jstring jPname = t.env->NewStringUTF(pszPname);
		jstring jPdesc = t.env->NewStringUTF(pszDesc);
		jstring jUid = t.env->NewStringUTF(pszRoleId);
		jstring jUname = t.env->NewStringUTF(pszRoleName);
		jstring jServerId = t.env->NewStringUTF(pszServerId);
		jstring jServerName = t.env->NewStringUTF(pszServerName);
		jstring jCp = t.env->NewStringUTF(pszCp);

		t.env->CallStaticVoidMethod(t.classID, t.methodID, jPid, jPname, jPdesc, price, count, rate, jUid, jUname, roleLevel, roleBalance, jServerId, jServerName,
				jCp);

		t.env->DeleteLocalRef(jPid);
		t.env->DeleteLocalRef(jPname);
		t.env->DeleteLocalRef(jPdesc);
		t.env->DeleteLocalRef(jUid);
		t.env->DeleteLocalRef(jUname);
		t.env->DeleteLocalRef(jServerId);
		t.env->DeleteLocalRef(jServerName);
		t.env->DeleteLocalRef(jCp);

		t.env->DeleteLocalRef(t.classID);
	}
}
void switchAccount()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "switchAccount", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

void exitSdk()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "exitSdk", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

void enterPlatform()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "enterPlatform", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

void createRoleStat(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName, const char *pszRoleTime)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "createRoleStat",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring jRoleId = t.env->NewStringUTF(pszRoleId);
		jstring jRolename = t.env->NewStringUTF(pszRoleName);
		jstring jRoleLevel = t.env->NewStringUTF(pszRoleLevel);
		jstring jServerId = t.env->NewStringUTF(pszServerId);
		jstring jServerName = t.env->NewStringUTF(pszServerName);
		jstring jRoleTime = t.env->NewStringUTF(pszRoleTime);

		t.env->CallStaticVoidMethod(t.classID, t.methodID, jRoleId,
				jRolename, jRoleLevel, jServerId, jServerName, jRoleTime);

		t.env->DeleteLocalRef(jRoleId);
		t.env->DeleteLocalRef(jRolename);
		t.env->DeleteLocalRef(jRoleLevel);
		t.env->DeleteLocalRef(jServerId);
		t.env->DeleteLocalRef(jServerName);
		t.env->DeleteLocalRef(jRoleTime);

		t.env->DeleteLocalRef(t.classID);
	}
}
void tdInitCpp(){
	LOGE("tdInit2");
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "tdInit", "()V")){
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}
void enterGameStat(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName, const char *pszRoleTime)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "enterGameStat",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
	{
		LOGE("GameSdk onEnterGameStat");
		jstring jRoleId = t.env->NewStringUTF(pszRoleId);
		jstring jRolename = t.env->NewStringUTF(pszRoleName);
		jstring jRoleLevel = t.env->NewStringUTF(pszRoleLevel);
		jstring jServerId = t.env->NewStringUTF(pszServerId);
		jstring jServerName = t.env->NewStringUTF(pszServerName);
		jstring jRoleTime = t.env->NewStringUTF(pszRoleTime);

		t.env->CallStaticVoidMethod(t.classID, t.methodID, jRoleId,
				jRolename, jRoleLevel, jServerId, jServerName, jRoleTime);

		t.env->DeleteLocalRef(jRoleId);
		t.env->DeleteLocalRef(jRolename);
		t.env->DeleteLocalRef(jRoleLevel);
		t.env->DeleteLocalRef(jServerId);
		t.env->DeleteLocalRef(jServerName);
		t.env->DeleteLocalRef(jRoleTime);

		t.env->DeleteLocalRef(t.classID);
	}
}

void recordRoleInf(const char *pszRoleId, const char *pszRoleName, const char *pszRoleLevel, const char *pszServerId, const char *pszServerName, const char *pszRoleTime)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "recordRoleInf",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring jRoleId = t.env->NewStringUTF(pszRoleId);
		jstring jRolename = t.env->NewStringUTF(pszRoleName);
		jstring jRoleLevel = t.env->NewStringUTF(pszRoleLevel);
		jstring jServerId = t.env->NewStringUTF(pszServerId);
		jstring jServerName = t.env->NewStringUTF(pszServerName);
		jstring jRoleTime = t.env->NewStringUTF(pszRoleTime);

		t.env->CallStaticVoidMethod(t.classID, t.methodID, jRoleId,
				jRolename, jRoleLevel, jServerId, jServerName, jRoleTime);

		t.env->DeleteLocalRef(jRoleId);
		t.env->DeleteLocalRef(jRolename);
		t.env->DeleteLocalRef(jRoleLevel);
		t.env->DeleteLocalRef(jServerId);
		t.env->DeleteLocalRef(jServerName);
		t.env->DeleteLocalRef(jRoleTime);

		t.env->DeleteLocalRef(t.classID);
	}
}


void showToolBar(int place)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "showToolBar", "(I)V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID, place);
		t.env->DeleteLocalRef(t.classID);
	}
}

void hideToolBar()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/sdk/GameSDK", "hideToolBar", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}
/*
void onRegisterAndroid(const char* platformId, const char* roleId, const char* roleName, const char* serverId ){
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "onRegistercb",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring jplatformId = t.env->NewStringUTF(platformId);
		jstring jroleId = t.env->NewStringUTF(roleId);
		jstring jroleName = t.env->NewStringUTF(roleName);
		jstring jserverId = t.env->NewStringUTF(serverId);

		t.env->CallStaticVoidMethod(t.classID, t.methodID, jplatformId, jroleId, jroleName, jserverId);

		t.env->DeleteLocalRef(jplatformId);
		t.env->DeleteLocalRef(jroleId);
		t.env->DeleteLocalRef(jroleName);
		t.env->DeleteLocalRef(jserverId);

		t.env->DeleteLocalRef(t.classID);
	}
}
void onLoginAndroid(const char* platformId, const char* roleId, const char* roleName, const char* serverId ){
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "onLogincb",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring jplatformId = t.env->NewStringUTF(platformId);
		jstring jroleId = t.env->NewStringUTF(roleId);
		jstring jroleName = t.env->NewStringUTF(roleName);
		jstring jserverId = t.env->NewStringUTF(serverId);

		t.env->CallStaticVoidMethod(t.classID, t.methodID, jplatformId, jroleId, jroleName, jserverId);

		t.env->DeleteLocalRef(jplatformId);
		t.env->DeleteLocalRef(jroleId);
		t.env->DeleteLocalRef(jroleName);
		t.env->DeleteLocalRef(jserverId);

		t.env->DeleteLocalRef(t.classID);
	}
}
void onPayAndroid(const char* platformId, const char* roleId, const char* roleName, const char* serverId,
		const char* appOrder, const char* channelOrder, const char* productId, const char* productPrice){
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "onLogincb",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring jplatformId = t.env->NewStringUTF(platformId);
		jstring jroleId = t.env->NewStringUTF(roleId);
		jstring jroleName = t.env->NewStringUTF(roleName);
		jstring jserverId = t.env->NewStringUTF(serverId);
		jstring jappOrder = t.env->NewStringUTF(appOrder);
		jstring jchannelOrder = t.env->NewStringUTF(channelOrder);
		jstring jproductId = t.env->NewStringUTF(productId);
		jstring jproductPrice = t.env->NewStringUTF(productPrice);

		t.env->CallStaticVoidMethod(t.classID, t.methodID, jplatformId, jroleId, jroleName, jserverId,
				jappOrder,  jchannelOrder,jproductId,jproductPrice);

		t.env->DeleteLocalRef(jplatformId);
		t.env->DeleteLocalRef(jroleId);
		t.env->DeleteLocalRef(jroleName);
		t.env->DeleteLocalRef(jserverId);
		t.env->DeleteLocalRef(jappOrder);
		t.env->DeleteLocalRef(jchannelOrder);
		t.env->DeleteLocalRef(jproductId);
		t.env->DeleteLocalRef(jproductPrice);

		t.env->DeleteLocalRef(t.classID);
	}
}
*/
