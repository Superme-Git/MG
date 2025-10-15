/*
 * NativeGLApplication.hpp
 *
 *  Created on: 2013-5-20
 *      Author: starlight
 */

#ifndef SFGAMENATIVEINTERFACE_HPP_
#define SFGAMENATIVEINTERFACE_HPP_

#include <sys/types.h>
extern "C" {
using namespace std;

class SFGameNativeInterface;

struct SFNativeOnlineUser {
	long long id;
	const char* channelId;
	const char* channelUserId;

	const char* userName;
	const char* token;
	const char* productCode;
};

class SFNativeOnlinePayResultCallback
{
public:
	SFNativeOnlinePayResultCallback() {};
   virtual ~SFNativeOnlinePayResultCallback() {};
   virtual void onFailed(const char* remain) = 0;
   virtual void onSuccess(const char* remain) = 0;
   virtual void onOderNo(const char* orderNo) = 0;
};

class SFNativeOnlineLoginCallback
{
public:
	SFNativeOnlineLoginCallback() {};
    virtual ~SFNativeOnlineLoginCallback() {};
    virtual void onLogout(const char* remain) = 0;
    virtual void onLoginSuccess(SFNativeOnlineUser* user, const char* remain) = 0;
    virtual void onLoginFailed(const char* r, const char* remain) = 0;
};

class SFNativeOnlineExitCallback
{
public:
	SFNativeOnlineExitCallback() {};
    virtual ~SFNativeOnlineExitCallback() {};
    virtual void onNoExiterProvide() = 0;
    virtual void onSDKExit(bool result) = 0;
};

class SFNativeOnlineExtendCallback
{
public:
	SFNativeOnlineExtendCallback() {};
    virtual ~SFNativeOnlineExtendCallback() {};
    virtual void onResponse(int index,const char* r, const char* remain) = 0;
};

class SFNativeOnlineInitCallback
{
public:
	SFNativeOnlineInitCallback() {};
    virtual ~SFNativeOnlineInitCallback(){};
    virtual void onResponse(const char* r, const char* remain) = 0;
};

class SFGameNativeInterface {
public:
	SFGameNativeInterface() {};
    virtual ~SFGameNativeInterface() {};
	static void setPayResultCallback(SFNativeOnlinePayResultCallback* callback);
	static void setLoginCallback(SFNativeOnlineLoginCallback* callback);
	static void setExitCallback(SFNativeOnlineExitCallback* callback);
	static void setExtendCallback(SFNativeOnlineExtendCallback* callback);
	static void setInitCallback(SFNativeOnlineInitCallback* callback);

	static void onCreate();
	static void onStop();
	static void onResume();
	static void onPause();
	static void onRestart();
	static void onDestroy();

	static void exit();
	static void login(const char* params);
	static void logout(const char* params);
	static void charge(const char* itemName, int unitPrice, int count,
			const char* callBackInfo, const char* callBackUrl);
	static void pay(int unitPrice, const char*  unitName,
			int count, const char*  callBackInfo, const char* callBackUrl);
	static void payExtend(int unitPrice, const char*  unitName,
			 const char*  itemCode, const char*  remain,int count, const char*  callBackInfo, const char* callBackUrl);
	static void setRoleData(const char* roleId, const char* roleName,
					const char* roleLevel, const char* zoneId, const char* zoneName);
	static bool isMusicEnabled();
	static void setData(const char* key, const char* value);
	const static char* extend(const char* key, int count);
};

}
#endif /* SFGAMENATIVEINTERFACE_HPP_ */
