#ifndef __VOICEMANAGER_H__
#define __VOICEMANAGER_H__ 

#include "network/HttpClient.h"
#include "network/HttpRequest.h"
#include "network/HttpResponse.h"

#include "elements/CEGUIAll.h"

class HttpOperator
{
public:
	bool Send(cocos2d::CCObject* obj);
public:
	cocos2d::extension::CCHttpRequest::HttpRequestType mType;
	cocos2d::extension::SEL_HttpResponse mResponse;
	std::string mStrUrl;
	std::vector<std::string> mVetHeader; // http 头.
	std::string mStrData;
	std::vector<std::string> mVetParam;
};

class VoiceManager : public cocos2d::CCObject
{
public:
	VoiceManager();
	~VoiceManager();
	bool initialize();
	void release();
	void tick(unsigned int deltaTime);

	void registerAutoClose(int fromType);																	// 注册自动停止录音
	void unregisterAutoClose();																				// 注销自动停止录音
	void showTip_RequirePermission();																		// 提示需要录音权限

	std::vector<short> readTempWavData();																	// 读取录音后保存的临时录音文件
	int SkipWaveHeader(FILE* fp);																			// skip wave header when read wave data
	int SkipCaffHander(FILE* fp);																			// skip caff header when read caff data
	std::vector<short> amplifyWavVolume(const std::vector<short>& datas);									// 将Wav音量放大到不失真的最大（噪音也同时放大）

	void ProcessVoiceDataForLua(unsigned int recordTime, int voiceChanelid, int extraData = 0);				// try send voice for lua
	void tryGetVoice(const std::string& strUuid, float fTime, const std::string& yuyinBtnName, bool bTouchPlay = false);	// try get voice

	void prepareSendVoiceMsg(const char* uuid, const char* time, int chanelId, int extraID);
	void SendVoiceDone(const char* uuid);
	void SendVoiceFail(const char* uuid);
	void GetTextDone(const char* uuid, const wchar_t* text);
	void GetTextFail(const char* uuid);

	void PauseMusicAndEffectForLua();
	void ResumeEffectForLua();
	void ResumeMusicForLua();
	void tryPlayEffect(const char* szFilePathName, const char* IconName, float effectWholeTime, bool bTouchPlay = false);
	void setForbiddenVoicePlay(bool bForbiddenVoicePlay);
	void finishPlayingEffect();
	void playEffectWithName(const char* szFilePathName, const char* IconName, float effectWholeTime);
	void resetOtherSound();

	void SendChatToPlatform(int type, CEGUI::String content, int rolelv, int rolefushi);
	void RoleAccusation(int roleid, int type, CEGUI::String content, int rolelv, int rolefushi, int aid, CEGUI::String aname, int alv, int afushi);
	void SendChatToPlatformCallBack(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void RoleAccusationCallBack(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);

	CEGUI::Window* getYuyinAniWnd();
private:
	bool mVoiceRecording;
	unsigned int mVoiceWaitTime;
	int mVoiceFromDialogType;	//标记语音来源哪个dialog

private:
	bool m_bForbiddenVoicePlay;
	bool m_bPlayingEffect;
	float m_PlayingTime;
	float m_EffectWholeTime;
	std::string m_StrFilePathName;
	CEGUI::String m_VoiceIconName_light;
	CEGUI::String m_VoiceIconName_dark;

	struct PlayEffectData
	{
		std::string strFilePathName;
		std::string strIconName;
		float fWholeTime;
	};
	std::vector<PlayEffectData> m_vPlayToEffect;

	float m_YuYinAniTime;
	std::string m_YuYinAniWndName;	//语音播放时的动画窗口名称

	int mExtraID;	// 好友聊天的m_ChatRoleID

	struct VoiceMsgForSend
	{
		std::string strUuid;
		int nChanelId;
		std::string strTimeText;
		int nExtraID; // 好友聊天的m_ChatRoleID
		std::wstring strVoiceText;
		bool bHasGetText;
		bool bHasSendVoice;
		float fTimeElapse;
		float fExpiresTime;
		VoiceMsgForSend() {
			strUuid = "";
			nChanelId = 0;
			strTimeText = "";
			nExtraID = 0;
			strVoiceText = L"";
			bHasGetText = false;
			bHasSendVoice = false;
			fTimeElapse = 0.f;
			fExpiresTime = 60.f;
		}
	};
	std::map<std::string, VoiceMsgForSend> m_mapVoiceMsgForSend;
};

VoiceManager* gGetVoiceManager();

/*****************************************************************/

class GetTextManager : public cocos2d::CCObject
{
public:
	GetTextManager();

	bool initialize();
	void tick(unsigned int deltaTime);
	void release();

	void tryRequestToken();

	void getTextFromXunFei(const char* uuid, const std::string& spxData, int recordTime, int voiceChanelid);	// 从讯飞服务器获取语音文本
	void onGetText(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);

private:
	void doGetTextResult(const std::string& strUuid, const std::string& result);
	void PopOperator_GetText();

	void requestTocken();
	void onTocken(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);

private:
	bool stoparam(std::string& type, std::string& param, const std::string& word);
	std::vector<std::string> stovs(const std::string& data);
	std::vector<std::string> stovs(const std::string& data, char c);
	const std::string getXunFeiServerUrl();
	const std::string getXunFeiAppId();

private:
	std::vector<HttpOperator*> mOper_GetText;
	std::vector<HttpOperator*> mFreeOper_GetText;
	bool mLock_GetText;	// 是否锁定（同时只允许发送一次HTTP请求）
	HttpOperator* mTockenOperator;	// 请求令牌的操作。

private:
	std::string mTocken;	// 令牌
	float mTockenExpires;	// 令牌持续时间
	int64_t mLastRequestTokenTime;	// 上一次请求令牌的时间
};

GetTextManager* gGetGetTextManager();

/*****************************************************************/

class SendVoiceManager : public cocos2d::CCObject
{
public:
	SendVoiceManager();

	bool initialize();
	void tick(unsigned int deltaTime);
	void release();

	// 上传语音文件到语音服务器
	void sendVoiceToServer(const char* uuid, const std::string& spxData, unsigned int recordTime);
	// 上传语音文件的response
	void onSendVoice(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);

private:
	void PopOperator_SendVoice();

private:
	std::vector<HttpOperator*> mOper_SendVoice;
	std::vector<HttpOperator*> mFreeOper_SendVoice;
	bool mLock_SendVoice;	// 是否锁定（同时只允许发送一次HTTP请求）
};

SendVoiceManager* gGetSendVoiceManager();

/*****************************************************************/

class GetVoiceManager : public cocos2d::CCObject
{
public:
	GetVoiceManager();

	bool initialize();
	void tick(unsigned int deltaTime);
	void release();

	// 从语音服务器下载语音文件
	void getVoiceFromServer(const char* uuid, float fTime, const char* yuyinBtnName, bool bTouchPlay);
	// 下载语音文件的response
	void onGetVoice(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);

private:
	void PopOperator_GetVoice();

private:
	std::vector<HttpOperator*> mOper_GetVoice;
	std::vector<HttpOperator*> mFreeOper_GetVoice;
	bool mLock_GetVoice;	// 是否锁定（同时只允许发送一次HTTP请求）
};

GetVoiceManager* gGetGetVoiceManager();

#endif
