//  ChannelManager.h
//  FireClient
#ifndef FireClient_ChannelManager_h
#define FireClient_ChannelManager_h

#if (defined WIN7_32)
#include <stdint.h>
#endif

#include <string>
namespace MT3
{
	class ChannelManager
	{
    public:
		static void ExitChannelManager();

		static bool isDefineSDK();

		static void ChangeUserLogin();
        static void LogoutAndRelogin();
        
        static int SupportFeature(int feature); // return 1-true, 0-false

		static int StartBuyYuanbao(int64_t sn, std::wstring& name, int goodid, int goodnum, int price, int zoneid);

		static void EnterPlatformCenter();

		static void UserFeedBack();

		static void EnterBBS();
        
        static int HasPlatformCenter(); // 1-true, 0-false
        static int HasPlatformForum(); // 1-true, 0-false
        static int HasFeedback();//1-true, 0-false

        static std::wstring GetPlatformCenterName();
        static std::wstring GetPlatformForumName();
        static std::wstring GetPlatformFeedbackName();
        static std::wstring GetPlatformLoginSuffix();
        
        static int IsTrdPlatform();
        static std::wstring GetCur3rdPlatform();
        static int IsAndroid();
		//是否支持用户中心,只用于360融合SDK的判断
		static int IsSupportUserCenter();
		static void openUserCenter();
        
        static void RemoveRCPFiles(int day);
        static void EnterCustomerService();
        static void EnterGameHelp(std::wstring);
        
        static void FacebookInvite(std::wstring roleid,int serverid);
        static void CommonShare(std::wstring title, std::wstring content = L"", std::wstring picUrl = L"", std::wstring link = L"", std::wstring comment = L"", std::wstring image = L"");
        static bool IsKorea();
        static void CommonInterface(int p1=0,int p2=0,int p3=0,int p4=0,int p5=0,int p6=0, std::wstring s1 = L"", std::wstring s2 = L"", std::wstring s3 = L"", std::wstring s4 = L"", std::wstring s5 = L"", std::wstring s6 = L"");
        static std::wstring GetFlurryId();

        //统计sdk
        static void onRegister_sta(const char* roleid, const char* rolename);
		//进入游戏
		static void onLogin_sta();
		static void onEnterGameStat();
		//角色升级
		static void onRecordRoleInf();
		//支付
        static void onpay_sta(const char *platformId, const char *roleId, const char *roleName, const char *serverId, int PAY_TYPE, const char *appOrder, const char *channelOrder, const char *productId, const char *productPrice);

		//QQ登陆
		static void qqLogin();
		//WX登陆
		static void wxLogin();
		//是否是应用宝
		static void setIsYingYongBao(bool isyingyongbao);
		static bool getIsYingYongBao();
		//应用宝是否自动登陆成功
		static void setIsYYBLogined(bool isYYBLogined);
		static bool getIsYYBLogined();

		//易接渠道相关方法
		static void yj_sdkLogin();
		static void yj_loginWhenFailed();
		static void yj_sdkLogout();
		static void yj_sdkExit();
		static void yj_onDestroy();
		static std::string urlencode(std::string str_source);

		static void setYJData(const char * key);
		static void setYJData_CreateRole(const char * key, const char * roleid, const char * rolename);

		static std::string getClipboardText();

    };
}//namespace MT3


#endif
