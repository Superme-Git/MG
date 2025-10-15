#include "CCPlatformMacros.h"
#if CC_TARGET_PLATFORM==CC_PLATFORM_IOS
#include <AudioToolbox/CAFFile.h>
#include "sys/types.h"
#include "sys/sysctl.h"
#define MAKEWORD(a, b)      ((WORD)(((BYTE)(((DWORD_PTR)(a)) & 0xff)) | ((WORD)((BYTE)(((DWORD_PTR)(b)) & 0xff))) << 8))
#endif 

#include "VoiceManager.h"
#include "GameApplication.h"
#include "Utils/base64.h"
#include "Utils/Voice.h"
#include "Singleton.hpp"
#include "utils/StringCover.h"
#include "utils/FileUtil.h"
#include "SimpleAudioEngine.h"
#include "Utils/WavRecorder.h"

#include "DeviceInfo.h"
#include "ConfigManager.h"
#include "GameTable/message/CMessageTip.h"
#include "MainRoleDataManager.h"
#include "LoginManager.h"
#include "GameScene.h"
#include "ConnectGetServerInfo.h"

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

#define OPERATE_TIMEOUT 3000		// 语音操作的超时时间是3秒 // yeqing 2015-12-03
#define VOICE_TEXT_TIMEOUT 30000	// 获取语音文本的超时时间是30秒 // yeqing 2015-12-03

std::string UrlEnCode(const std::string& str);
std::string UrlEncodeFormat(const unsigned char cValue);
std::string DecimalToHexString(unsigned int nValue);
std::string createUUID();	// 生成UUID
std::string getFilePathName(const char* uuid);
std::string getChatServerUrl();

/*****************************************************************/

bool HttpOperator::Send(cocos2d::CCObject* obj)
{
	cocos2d::extension::CCHttpRequest* request = new cocos2d::extension::CCHttpRequest;
	request->setUrl(mStrUrl.c_str());
	if (mVetHeader.size()) {
		request->setHeaders(mVetHeader);
	}
	if (mStrData.size()) {
		request->setRequestData(mStrData.c_str(), mStrData.length());
	}
	request->setRequestType(mType);
	request->setResponseCallback(obj, mResponse);

	cocos2d::extension::CCHttpClient* client = cocos2d::extension::CCHttpClient::getInstance();
	client->setTimeoutForConnect(20);
	client->send(request);
	request->release();
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("HttpOperator::Send mStrUrl=%s, mStrData=%s", mStrUrl.c_str(), mStrData.c_str());
#endif
	return true;
}

std::string swapWavAmrFileName(const std::string& strFileName, bool bWav)
{
	std::string outFileName;
	if (bWav)
	{
		size_t dotPos = strFileName.find_last_of(".");
		outFileName = strFileName.substr(0, dotPos);
		outFileName += ".amr";
	}
	else
	{
		size_t dotPos = strFileName.find_last_of(".");
		outFileName = strFileName.substr(0, dotPos);
		outFileName += ".wav";
	}
	return outFileName;
}

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
bool DeleteWinWaveFiles(const std::wstring& dir)
{
	WIN32_FIND_DATA fd;
	std::wstring searchpath = dir + L"/*.wav";
	HANDLE hFind = ::FindFirstFile(searchpath.c_str(), &fd);

	if (hFind != INVALID_HANDLE_VALUE)
	{
		do{
			if (0 == (fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))//目录
			{
				if (fd.cFileName[0] != L'.')
				{
					std::wstring fn = dir + L"/" + fd.cFileName;
					DeleteFile(fn.c_str());
				}
			}
		} while (::FindNextFile(hFind, &fd));

		::FindClose(hFind);
	}

	searchpath = dir + L"/*.amr";
	hFind = ::FindFirstFile(searchpath.c_str(), &fd);

	if (hFind != INVALID_HANDLE_VALUE)
	{
		do{
			if (0 == (fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))//目录
			{
				if (fd.cFileName[0] != L'.')
				{
					std::wstring fn = dir + L"/" + fd.cFileName;
					DeleteFile(fn.c_str());
				}
			}
		} while (::FindNextFile(hFind, &fd));

		::FindClose(hFind);
	}
	return true;
}
#endif

/*****************************************************************/

VoiceManager::VoiceManager()
	: mVoiceRecording(false)
	, mVoiceWaitTime(0)
	, mVoiceFromDialogType(0)
	, m_bForbiddenVoicePlay(false)
	, m_bPlayingEffect(false)
	, m_PlayingTime(0.f)
	, m_EffectWholeTime(0.f)
	, m_StrFilePathName()
	, m_VoiceIconName_light("")
	, m_VoiceIconName_dark("")

	, m_YuYinAniTime(0.5f)
	, m_YuYinAniWndName()

	, mExtraID(0)
{
	m_mapVoiceMsgForSend.clear();
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	std::string strFilePath = gGetWavRecorder()->getTempFilePath();
	DeleteWinWaveFiles(s2ws(strFilePath));
#endif
}

VoiceManager::~VoiceManager()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	std::string strFilePath = gGetWavRecorder()->getTempFilePath();
	DeleteWinWaveFiles(s2ws(strFilePath));
#endif
}

bool VoiceManager::initialize()
{
	gGetGetTextManager()->initialize();
	gGetSendVoiceManager()->initialize();
	gGetGetVoiceManager()->initialize();
	return true;
}

void VoiceManager::release()
{
	gGetGetTextManager()->release();
	gGetSendVoiceManager()->release();
	gGetGetVoiceManager()->release();

	m_vPlayToEffect.clear();

	resetOtherSound();
}

void VoiceManager::tick(unsigned int deltaTime)
{
	gGetGetTextManager()->tick(deltaTime);
	gGetSendVoiceManager()->tick(deltaTime);
	gGetGetVoiceManager()->tick(deltaTime);

	// 录音超时后自动停止
	if (mVoiceRecording)
	{
		if (deltaTime < mVoiceWaitTime)
		{
			mVoiceWaitTime -= deltaTime;
		}
		else
		{
			CEGUI::EventArgs eVoiceEnd;
			eVoiceEnd.handled = 2;
			cocos2d::gGetScriptEngine()->pushIntegerToLuaStack(mVoiceFromDialogType);
			cocos2d::gGetScriptEngine()->pushUserDataToLuaStack(&eVoiceEnd);
			cocos2d::gGetScriptEngine()->executeGlobalFunction("VoiceRecordTimeOver", 2);
		}
	}

	// 处理语音继续播放或停止
	if (m_bPlayingEffect)
	{
		m_PlayingTime += (float)deltaTime / 1000.f;
		if (m_PlayingTime > m_EffectWholeTime)
		{
			finishPlayingEffect();
		}

		CEGUI::Window* yuyinAni = getYuyinAniWnd();
		if (yuyinAni)
		{
			if (m_YuYinAniTime >= 0.f) {
				m_YuYinAniTime += (float)deltaTime / 1000.f;
				if (m_YuYinAniTime >= 1.f) {
					yuyinAni->setProperty("Image", m_VoiceIconName_dark);
					m_YuYinAniTime = -0.5f;
				}
			}
			else {
				m_YuYinAniTime -= (float)deltaTime / 1000.f;
				if (m_YuYinAniTime <= -1.f) {
					yuyinAni->setProperty("Image", m_VoiceIconName_light);
					m_YuYinAniTime = 0.5f;
				}
			}
		}
	}

	// 发送语音聊天消息到聊天服务器
	std::map<std::string, VoiceMsgForSend>::iterator it;
	for (it = m_mapVoiceMsgForSend.begin(); it != m_mapVoiceMsgForSend.end();) {
		VoiceMsgForSend& vmfs = it->second;
		vmfs.fTimeElapse += deltaTime * 0.001f;
		if (vmfs.bHasGetText && vmfs.bHasSendVoice) {
			std::string voiceText = ws2s(vmfs.strVoiceText.c_str());
			std::string resultText = "voice link=\"";
			resultText += vmfs.strUuid;
			resultText += "\" time=";
			resultText += vmfs.strTimeText;
			resultText += " text=";
			resultText += voiceText;
			resultText += "&";
			if (vmfs.nExtraID != 0) {
				cocos2d::gGetScriptEngine()->pushIntegerToLuaStack(vmfs.nExtraID);
				cocos2d::gGetScriptEngine()->pushStringToLuaStack(resultText.c_str());
				cocos2d::gGetScriptEngine()->pushStringToLuaStack(voiceText.c_str());
				cocos2d::gGetScriptEngine()->executeGlobalFunction("FriendDialog.SendVoiceMsgToFriend_", 3);
			}
			else {
				cocos2d::gGetScriptEngine()->pushStringToLuaStack(resultText.c_str());
				cocos2d::gGetScriptEngine()->pushIntegerToLuaStack(0);
				cocos2d::gGetScriptEngine()->pushStringToLuaStack(voiceText.c_str());
				cocos2d::gGetScriptEngine()->pushIntegerToLuaStack(vmfs.nChanelId);
				cocos2d::gGetScriptEngine()->executeGlobalFunction("CChatOutputDialog.SendVoiceMsgToServer_", 4);
			}
			m_mapVoiceMsgForSend.erase(it++);
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
			LOGE("CChatOutputDialog.SendVoiceMsgToServer_ resultText=%s, voiceText=%s", resultText.c_str(), voiceText.c_str());
#endif
		}
		else if (vmfs.fTimeElapse >= vmfs.fExpiresTime) {
			m_mapVoiceMsgForSend.erase(it++);
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
			LOGE("VoiceManager::tick vmfs.fTimeElapse=%.1f > vmfs.fExpiresTime=%.1f", vmfs.fTimeElapse, vmfs.fExpiresTime);
#endif
		}
		else {
			++it;
		}
	}
}

// 注册自动停止录音
void VoiceManager::registerAutoClose(int fromType)
{
	mVoiceRecording = true;
	mVoiceWaitTime = 30000;
	mVoiceFromDialogType = fromType;
}

// 注销自动停止录音
void VoiceManager::unregisterAutoClose()
{
	mVoiceRecording = false;
	mVoiceWaitTime = 0;
}

// 提示需要录音权限
void VoiceManager::showTip_RequirePermission()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	const GameTable::message::CMessageTip& tip = GameTable::message::GetCMessageTipTableInstance().getRecorder(160234);
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	const GameTable::message::CMessageTip& tip = GameTable::message::GetCMessageTipTableInstance().getRecorder(160236);
#else
	const GameTable::message::CMessageTip& tip = GameTable::message::GetCMessageTipTableInstance().getRecorder(160234);
#endif

	cocos2d::gGetScriptEngine()->pushStringToLuaStack(ws2s(tip.msg).c_str());
	cocos2d::gGetScriptEngine()->executeGlobalFunction("CChatManager.AddMsg_PoP_", 1);
}

// 读取录音后保存的临时录音文件
std::vector<short> VoiceManager::readTempWavData()
{
	std::vector<short> datas;

	FILE* fp = fopen(gGetWavRecorder()->getTempFileName(), "rb");
	if (fp)
	{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32 || CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
		gGetVoiceManager()->SkipWaveHeader(fp);
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
		gGetVoiceManager()->SkipCaffHander(fp);
#endif
		while (true)
		{
			short in[1024];
			size_t read_count = fread(in, sizeof(short), 1024, fp);
			if (feof(fp))
				break;
			for (size_t i = 0; i < read_count; i++)
			{
				datas.push_back(in[i]);
			}
		}
		fclose(fp);
	}

	std::vector<short> amplifiedData = amplifyWavVolume(datas);
	return amplifiedData;
}

// skip wave header when read wave data
int VoiceManager::SkipWaveHeader(FILE* fp)
{
	fseek(fp, 40, 0);
	unsigned int dataLength;
	fread(&dataLength, 4, 1, fp);

	return dataLength;
}

// skip caff header when read caff data
int VoiceManager::SkipCaffHander(FILE* fp)
{
	assert(fp);
	fseek(fp, 0, 2);
	int dataLength = ftell(fp) - 4092;
	fseek(fp, 4092, 0);
	return dataLength;
}

// 将Wav音量放大到不失真的最大（噪音也同时放大）
std::vector<short> VoiceManager::amplifyWavVolume(const std::vector<short>& datas)
{
	std::vector<short> amplifiedData;
	std::vector<short>::const_iterator it;

	// 统计最大振幅
	short maxAmplitude = 1;
	for (it = datas.begin(); it != datas.end(); ++it)
	{
		short curAmplitude = *it;
		if (curAmplitude > maxAmplitude) {
			maxAmplitude = curAmplitude;
		}
	}

	// 计算不失真的最大放大倍数
	float multiple = 0x7fff / (float)maxAmplitude;

	// 将振幅统一放大
	for (it = datas.begin(); it != datas.end(); ++it)
	{
		short resultAmplitude = (*it) * multiple;
		if (resultAmplitude > 0x7fff) {
			resultAmplitude = 0x7fff;
		}
		amplifiedData.push_back(resultAmplitude);
	}

	return amplifiedData;
}

// try send voice for lua
void VoiceManager::ProcessVoiceDataForLua(unsigned int recordTime, int voiceChanelid, int extraData)
{
	// wav数据
	std::vector<short> voiceDatas = gGetVoiceManager()->readTempWavData();

	if (!voiceDatas.empty())
	{
		// 将Wav格式转成Speex格式
		std::vector<char> outDatas = encodeWAV2SPEEX(voiceDatas);
		std::string spxData = Base64::YYEncode((const unsigned char*)&outDatas[0], (unsigned long)outDatas.size());

		// amr数据
		std::string strWavPath = gGetWavRecorder()->getTempFileName();
		std::string strAmrPath = swapWavAmrFileName(strWavPath, true);
		encodeWAVE2AMR(strWavPath.c_str(), strAmrPath.c_str(), 1, 16);
		unsigned long nSize = 0;
		unsigned char* pBuffer = cocos2d::CCFileUtils::sharedFileUtils()->getFileData(strAmrPath.c_str(), "rb", &nSize);
		if (!pBuffer) return;

		// amr数据 编码BASE64
		std::string amrData = Base64::YYEncode(pBuffer, nSize);

		if (!spxData.empty() && !amrData.empty())
		{
			std::string strUuid = createUUID();
			std::string strTime = StringCover::floatToString(recordTime * 0.001f);

			// 精灵的语音
			if (voiceChanelid == cocos2d::gGetScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", "VOICE_JINGLING_CHANNEL")) {
				gGetGetTextManager()->getTextFromXunFei(strUuid.c_str(), spxData, recordTime, voiceChanelid);
			}
			// 语音聊天
			else {
				prepareSendVoiceMsg(strUuid.c_str(), strTime.c_str(), voiceChanelid, extraData);
				gGetGetTextManager()->getTextFromXunFei(strUuid.c_str(), spxData, recordTime, voiceChanelid);
				gGetSendVoiceManager()->sendVoiceToServer(strUuid.c_str(), amrData, recordTime);
			}
		}
		CC_SAFE_DELETE_ARRAY(pBuffer);
	}
}

// try get voice
void VoiceManager::tryGetVoice(const std::string& strUuid, float fTime, const std::string& yuyinBtnName, bool bTouchPlay)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("VoiceManager::tryGetVoice strUuid=%s, fTime=%f, yuyinBtnName=%s, bTouchPlay=%d",
		strUuid.c_str(), fTime, yuyinBtnName.c_str(), bTouchPlay);
#endif
	gGetGetVoiceManager()->getVoiceFromServer(strUuid.c_str(), fTime, yuyinBtnName.c_str(), bTouchPlay);
}

void VoiceManager::prepareSendVoiceMsg(const char* uuid, const char* time, int chanelId, int extraID)
{
	VoiceMsgForSend vmfs;
	vmfs.strUuid = uuid;
	vmfs.nChanelId = chanelId;
	vmfs.strTimeText = time;
	vmfs.nExtraID = extraID;
	m_mapVoiceMsgForSend[vmfs.strUuid] = vmfs;
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("VoiceManager::prepareSendVoiceMsg vmfs.strUuid=%s, vmfs.nChanelId=%d, vmfs.strTimeText=%s, vmfs.nExtraID=%d",
		vmfs.strUuid.c_str(), vmfs.nChanelId, vmfs.strTimeText.c_str(), vmfs.nExtraID);
#endif

}

void VoiceManager::SendVoiceDone(const char* uuid)
{
	std::string strUuid = uuid;
	std::map<std::string, VoiceMsgForSend>::iterator it = m_mapVoiceMsgForSend.find(strUuid);
	if (it != m_mapVoiceMsgForSend.end()) {
		VoiceMsgForSend& vmfs = it->second;
		vmfs.bHasSendVoice = true;
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		LOGE("VoiceManager::SendVoiceDone vmfs.strUuid=%s, vmfs.bHasSendVoice=%d",
			vmfs.strUuid.c_str(), vmfs.bHasSendVoice);
#endif
	}
}

void VoiceManager::SendVoiceFail(const char* uuid)
{
	std::string strUuid = uuid;
	std::map<std::string, VoiceMsgForSend>::iterator it = m_mapVoiceMsgForSend.find(strUuid);
	if (it != m_mapVoiceMsgForSend.end()) {
		VoiceMsgForSend& vmfs = it->second;
		vmfs.fTimeElapse = vmfs.fExpiresTime;
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		LOGE("VoiceManager::SendVoiceFail vmfs.strUuid=%s", vmfs.strUuid.c_str());
#endif
	}
}

void VoiceManager::GetTextDone(const char* uuid, const wchar_t* text)
{
	std::string strUuid = uuid;
	std::map<std::string, VoiceMsgForSend>::iterator it = m_mapVoiceMsgForSend.find(strUuid);
	if (it != m_mapVoiceMsgForSend.end()) {
		VoiceMsgForSend& vmfs = it->second;
		vmfs.strVoiceText = text;
		vmfs.bHasGetText = true;
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		LOGE("VoiceManager::GetTextDone vmfs.strUuid=%s, vmfs.strVoiceText=%s, vmfs.bHasGetText=%d",
			vmfs.strUuid.c_str(), ws2s(vmfs.strVoiceText.c_str()).c_str(), vmfs.bHasGetText);
#endif
	}
}

void VoiceManager::GetTextFail(const char* uuid)
{
	std::string strUuid = uuid;
	std::map<std::string, VoiceMsgForSend>::iterator it = m_mapVoiceMsgForSend.find(strUuid);
	if (it != m_mapVoiceMsgForSend.end()) {
		VoiceMsgForSend& vmfs = it->second;
		vmfs.fTimeElapse = vmfs.fExpiresTime;
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		LOGE("VoiceManager::GetTextFail vmfs.strUuid=%s", vmfs.strUuid.c_str());
#endif
	}
}

void VoiceManager::PauseMusicAndEffectForLua()
{
	CocosDenshion::SimpleAudioEngine::sharedEngine()->stopAllEffects();
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(false);
	CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
}

void VoiceManager::ResumeEffectForLua()
{
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(true);
}

void VoiceManager::ResumeMusicForLua()
{
	CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
}

void VoiceManager::tryPlayEffect(const char* szFilePathName, const char* IconName, float effectWholeTime, bool bTouchPlay)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("VoiceManager::tryPlayEffect szFilePathName=%s, IconName=%s, effectWholeTime=%f, bTouchPlay=%d", szFilePathName, IconName, effectWholeTime, bTouchPlay);
#endif
	// 当录音时，禁止播放语音
	if (m_bForbiddenVoicePlay) {
		return;
	}
	
	// 点击播放
	if (bTouchPlay)
	{
		m_vPlayToEffect.clear();

		//  当前有语音正在播放，则
		if (m_bPlayingEffect) {
			// 点击播放与当前播放不同的语音，则直接播放
			if (m_StrFilePathName.compare(szFilePathName) != 0) {
				playEffectWithName(szFilePathName, IconName, effectWholeTime);
			}
			// 点击播放与当前播放相同的语音，则停止播放
			else {
				resetOtherSound();
			}
		}
		// 当前没有语音正在播放，则直接播放
		else {
			playEffectWithName(szFilePathName, IconName, effectWholeTime);
		}
	}
	// 自动播放
	else
	{
		// 当前有语音正在播放，则加入自动播放队列
		if (m_bPlayingEffect)
		{
			PlayEffectData effectData;
			effectData.strFilePathName = szFilePathName;
			effectData.fWholeTime = effectWholeTime + 0.5;
			effectData.strIconName = IconName;
			m_vPlayToEffect.push_back(effectData);
		}
		// 当前没有语音正在播放，则直接播放
		else
		{
			playEffectWithName(szFilePathName, IconName, effectWholeTime);
		}
	}
}

void VoiceManager::setForbiddenVoicePlay(bool bForbiddenVoicePlay)
{
	if (m_bForbiddenVoicePlay != bForbiddenVoicePlay)
	{
		m_bForbiddenVoicePlay = bForbiddenVoicePlay;
		if (m_bForbiddenVoicePlay)
		{
			m_vPlayToEffect.clear();
			CocosDenshion::SimpleAudioEngine* pAudioEngine = CocosDenshion::SimpleAudioEngine::sharedEngine();
			if (pAudioEngine) {
				pAudioEngine->stopAllEffects();
				m_bPlayingEffect = false;
				m_StrFilePathName = "";
			}
		}
	}
}

void VoiceManager::finishPlayingEffect()
{
	if ((int)m_vPlayToEffect.size() > 0) {
		PlayEffectData effectData = m_vPlayToEffect[0];
		playEffectWithName(effectData.strFilePathName.c_str(), effectData.strIconName.c_str(), effectData.fWholeTime);
		m_vPlayToEffect.erase(m_vPlayToEffect.begin());
	}
	else {
		resetOtherSound();
	}
}

void VoiceManager::playEffectWithName(const char* szFilePathName, const char* IconName, float effectWholeTime)
{
	m_bPlayingEffect = true;
	m_PlayingTime = 0.f;
	m_EffectWholeTime = effectWholeTime + 0.5f;
	m_StrFilePathName = szFilePathName;

	CEGUI::Window* yuyinAni = getYuyinAniWnd();
	if (yuyinAni) {
		yuyinAni->setProperty("Image", m_VoiceIconName_dark);
		m_YuYinAniWndName = "";
	}

	string strIconName(IconName);
	if (strIconName.size() > 0) {
		strIconName += "/icon";

		if (strIconName.find("cell2") != string::npos)
		{
			m_VoiceIconName_light = "set:liaotian image:liaotian_yuyin_biaoshi4";
			m_VoiceIconName_dark = "set:liaotian image:liaotian_yuyin_biaoshi3";
		}
		else{
			m_VoiceIconName_light = "set:liaotian image:liaotian_yuyin_biaoshi";
			m_VoiceIconName_dark = "set:liaotian image:liaotian_yuyin_biaoshi2";
		}

		m_YuYinAniWndName = strIconName;
		yuyinAni = getYuyinAniWnd();
		if (yuyinAni) {
			yuyinAni->setProperty("Image", m_VoiceIconName_light);
			m_YuYinAniTime = 0.5f;
		}
	}

	CocosDenshion::SimpleAudioEngine* pAudioEngine = CocosDenshion::SimpleAudioEngine::sharedEngine();
	pAudioEngine->stopAllEffects();
	pAudioEngine->pauseBackgroundMusic();
	pAudioEngine->setEffectsVolume(1.f);
	pAudioEngine->setEffectEnable(true);
	pAudioEngine->playEffect(szFilePathName);
	pAudioEngine->setEffectEnable(false);
}

void VoiceManager::resetOtherSound()
{
	CocosDenshion::SimpleAudioEngine* pAudioEngine = CocosDenshion::SimpleAudioEngine::sharedEngine();
	if (!pAudioEngine) return;

	pAudioEngine->stopAllEffects();
	pAudioEngine->setEffectEnable(true);
	if (gGetGameConfigManager() && gGetGameConfigManager()->GetConfigValue(L"sound") != 0)
	{
		pAudioEngine->resumeBackgroundMusic();
	}

	if (gGetGameConfigManager())
	{
		int BackMusicValue = gGetGameConfigManager()->GetConfigValue(L"soundvalue");
		pAudioEngine->setEffectsVolume((float)(BackMusicValue / 255.0f));
	}

	m_bPlayingEffect = false;
	m_StrFilePathName = "";

	CEGUI::Window* yuyinAni = getYuyinAniWnd();
	if (yuyinAni)
	{
		yuyinAni->setProperty("Image", m_VoiceIconName_dark);
		m_YuYinAniWndName = "";
	}
}

void VoiceManager::SendChatToPlatform(int type, CEGUI::String content, int rolelv, int rolefushi)
{
	int GameID = 88;
	int RoleID = GetMainRoleDataNumValue<int64_t>("roleid");
	int ServerID = gGetLoginManager()->getServerID();
	std::wstring RoleName = s2ws(GetMainRoleDataStrValue("strName"));
	int ContentType = type;
	std::string ChatContent = StringCover::to_string(s2ws(content));
	int64_t ServerTime = gGetServerTime();

#ifdef _WIN32
	SECURITY_WIN32::MD5Hash md5;
#else
	SECURITY_LINUX::MD5Hash md5;
#endif
	std::string GameKey = "b18a26ffc632752987bd24a7bf0353f3";
	std::string toMD5 = GameKey + StringCover::intToString(GameID) + StringCover::intToString(RoleID) + StringCover::intToString(ServerID) + StringCover::int64_tToString(ServerTime);
	md5.UpdateSelf((const unsigned char*)toMD5.c_str(), (unsigned int)toMD5.length());
	const unsigned char* dest = md5.Final();

	char szmd5[64] = {};
	for (int i = 0; i < 16; i++)
	{
		sprintf(szmd5, "%s%02x", szmd5, dest[i]);
	}
	std::string md5Str = szmd5;

	std::string strUrl = "GameId=";
	strUrl += StringCover::intToString(GameID) + "&RoleId=";
	strUrl += StringCover::intToString(RoleID) + "&ServerId=";
	strUrl += StringCover::intToString(ServerID) + "&RoleName=";
	strUrl += UrlEnCode(ws2s(RoleName)) + "&ContentType=";
	strUrl += StringCover::intToString(ContentType) + "&ChatContent=";
	strUrl += UrlEnCode(Base64::YYEncode((const unsigned char*)ChatContent.c_str(), ChatContent.length())) + "&RoleLevel=";
	strUrl += StringCover::intToString(rolelv) + "&FuShi=";
	strUrl += StringCover::intToString(rolefushi) + "&Time=";
	strUrl += StringCover::int64_tToString(ServerTime) + "&Sign=";
	strUrl += md5Str;

	cocos2d::extension::CCHttpRequest* request = new cocos2d::extension::CCHttpRequest;
	request->setUrl(GetServerInfo()->getHttpAdressByEnum(eHttpServerInfoUrl).c_str());
	request->setRequestType(cocos2d::extension::CCHttpRequest::kHttpPost);
	request->setRequestData(strUrl.c_str(), strUrl.length());
	request->setResponseCallback((cocos2d::CCObject*)this, httpresponse_selector(VoiceManager::SendChatToPlatformCallBack));

	cocos2d::extension::CCHttpClient* client = cocos2d::extension::CCHttpClient::getInstance();
	client->setTimeoutForConnect(10);
	client->send(request);
	request->release();
}

void VoiceManager::RoleAccusation(int roleid, int type, CEGUI::String content, int rolelv, int rolefushi, int aid, CEGUI::String aname, int alv, int afushi)
{
	int GameID = 88;
	int RoleID = roleid;
	int ServerID = gGetLoginManager()->getServerID();
	std::wstring RoleName = L"";
	int64_t ServerTime = gGetServerTime();
	std::string ChatContent = StringCover::to_string(s2ws(content));

	if (gGetScene()->FindCharacterByID(roleid))
	{
		RoleName = gGetScene()->FindCharacterByID(roleid)->GetName();
	}
	else if (RoleID != 0)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(RoleID);
		string tmp = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("FriendManager.getRoleNameByID", 1);
		RoleName = s2ws(tmp);
	}
	else{
		cocos2d::gGetScriptEngine()->pushIntegerToLuaStack(190038);
		cocos2d::gGetScriptEngine()->executeGlobalFunction("CChatManager.AddTipsMsg_", 1);
		return;
	}

#ifdef _WIN32
	SECURITY_WIN32::MD5Hash md5;
#else
	SECURITY_LINUX::MD5Hash md5;
#endif
	std::string GameKey = "b18a26ffc632752987bd24a7bf0353f3";
	std::string toMD5 = GameKey + StringCover::intToString(GameID) + StringCover::intToString(RoleID) + StringCover::intToString(ServerID) + StringCover::int64_tToString(ServerTime);
	md5.UpdateSelf((const unsigned char*)toMD5.c_str(), (unsigned int)toMD5.length());
	const unsigned char* dest = md5.Final();

	char szmd5[64] = {};
	for (int i = 0; i < 16; i++)
	{
		sprintf(szmd5, "%s%02x", szmd5, dest[i]);
	}
	std::string md5Str = szmd5;

	std::string strUrl = GetServerInfo()->getHttpAdressByEnum(eHttpHorseRunUrl) + "?GameId=";
	strUrl += StringCover::intToString(GameID) + "&RoleId=";
	strUrl += StringCover::intToString(RoleID) + "&ServerId=";
	strUrl += StringCover::intToString(ServerID) + "&RoleName=";
	strUrl += UrlEnCode(ws2s(RoleName)) + "&Type=";
	strUrl += StringCover::intToString(type) + "&Content=";
	strUrl += UrlEnCode(Base64::YYEncode((const unsigned char*)ChatContent.c_str(), ChatContent.length())) + "&RoleLevel=";
	strUrl += StringCover::intToString(rolelv) + "&FuShi=";
	strUrl += StringCover::intToString(rolefushi) + "&TipRoleId=";
	strUrl += StringCover::intToString(aid) + "&TipRoleName=";
	strUrl += UrlEnCode(aname) + "&TipRoleLevel=";
	strUrl += StringCover::intToString(alv) + "&TipFuShi=";
	strUrl += StringCover::intToString(afushi) + "&Time=";
	strUrl += StringCover::int64_tToString(ServerTime) + "&Sign=";
	strUrl += md5Str;

	cocos2d::extension::CCHttpRequest* request = new cocos2d::extension::CCHttpRequest;
	request->setUrl(strUrl.c_str());
	request->setRequestType(cocos2d::extension::CCHttpRequest::kHttpGet);
	request->setResponseCallback((cocos2d::CCObject*)this, httpresponse_selector(VoiceManager::RoleAccusationCallBack));

	cocos2d::extension::CCHttpClient* client = cocos2d::extension::CCHttpClient::getInstance();
	client->setTimeoutForConnect(10);
	client->send(request);
	request->release();
}

void VoiceManager::SendChatToPlatformCallBack(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response)
{
	int retCode = response->getResponseCode();
	std::string type = "-1";
	int pos1 = 0;
	int pos2 = 0;
	if (retCode == 200)
	{
		std::vector<char>* res = response->getResponseData();
		for (size_t i = 0; i < (*res).size(); i++)
		{
			if ((*res)[i] == ':')
			{
				if (i + 2 < (*res).size() && i + 4 < (*res).size())
				{
					if ((*res)[i + 4] == '"')
					{
						type += (*res)[i + 3];
					}
					else{
						type += (*res)[i + 3];
						type += (*res)[i + 4];
					}
				}
			}
		}

		switch (StringCover::StrToNum<int>(type))
		{
		case 1://成功
			break;
		case 2://Sign校验失败
			break;
		case 3://GameId格式不正确
			break;
		case 4://游戏不存在或未配置
			break;
		case 5://RoleId未填或格式不正确
			break;
		case 6://ServerId未填或格式不正确
			break;
		case 7://RoleName未填或格式不正确
			break;
		case 8://ContentType 未填或格式不正确
			break;
		case 9://ChatContent未填或格式不正确
			break;
		case 10://Time未填或格式不正确
			break;
		case 99://其他错误
			break;
		}
	}
}

void VoiceManager::RoleAccusationCallBack(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response)
{
	int retCode = response->getResponseCode();
	std::string type = "-1";
	int pos1 = 0;
	int pos2 = 0;
	if (retCode == 200)
	{
		std::vector<char>* res = response->getResponseData();
		for (size_t i = 0; i < (*res).size(); i++)
		{
			if ((*res)[i] == ':')
			{
				if (i + 2 < (*res).size() && i + 4 < (*res).size())
				{
					if ((*res)[i + 4] == '"')
					{
						type += (*res)[i + 3];
					}
					else{
						type += (*res)[i + 3];
						type += (*res)[i + 4];
					}
				}
			}
		}

		switch (StringCover::StrToNum<int>(type))
		{
		case 1://成功
			break;
		case 2://Sign校验失败
			break;
		case 3://GameId格式不正确
			break;
		case 4://游戏不存在或未配置
			break;
		case 5://RoleId未填或格式不正确
			break;
		case 6://ServerId未填或格式不正确
			break;
		case 7://RoleName未填或格式不正确
			break;
		case 8://Time未填或格式不正确
			break;
		case 99://其他错误
			break;
		}
	}
}

CEGUI::Window* VoiceManager::getYuyinAniWnd()
{
	if (CEGUI::WindowManager::getSingleton().isWindowPresent(m_YuYinAniWndName)) {
		return CEGUI::WindowManager::getSingleton().getWindow(m_YuYinAniWndName);
	}
	return NULL;
}

VoiceManager* gGetVoiceManager()
{
	static VoiceManager mgr;
	return &mgr;
}

/*****************************************************************/

GetTextManager::GetTextManager()
	: mLock_GetText(false)
	, mTockenOperator(NULL)

	, mTocken()
	, mTockenExpires(0.f)
	, mLastRequestTokenTime(0)
{
}

bool GetTextManager::initialize()
{
	return true;
}

void GetTextManager::tick(unsigned int deltaTime)
{
	// 释放操作
	while (mFreeOper_GetText.size())
	{
		delete mFreeOper_GetText[0];
		mFreeOper_GetText.erase(mFreeOper_GetText.begin());
	}

	// 令牌倒计时
	if (mTockenExpires > 0.f) {
		mTockenExpires -= (float)deltaTime / 1000.f;
	}

	// 尝试请求讯飞的Token
	tryRequestToken();

	// 获取令牌中，直接跳过
	if (mTockenOperator) {
		return;
	}

	// 只允许发送一次HTTP请求
	if (mLock_GetText) {
		return;
	}

	// 发送请求
	if (mOper_GetText.size())
	{
		mOper_GetText[0]->Send(this);
		mLock_GetText = true;
	}
}

void GetTextManager::release()
{
	while (mFreeOper_GetText.size())
	{
		delete mFreeOper_GetText[0];
		mFreeOper_GetText.erase(mFreeOper_GetText.begin());
	}

	while (mOper_GetText.size())
	{
		delete mOper_GetText[0];
		mOper_GetText.erase(mOper_GetText.begin());
	}

	if (mTockenOperator) {
		delete mTockenOperator;
		mTockenOperator = NULL;
	}

	mLock_GetText = false;
}

// 尝试请求讯飞的Token
void GetTextManager::tryRequestToken()
{
	// 每次请求讯飞Token的时间间隔不得少于15分钟
	int64_t nowServerTime = gGetServerTime();
	if (nowServerTime - mLastRequestTokenTime > 15 * 60.f * 1000.f) {
		// 令牌未申请成功，则申请；令牌申请成功，则1小时后申请
		if (mTocken.size() == 0 || mTockenExpires < 60 * 60.0f) {
			requestTocken();
			mLastRequestTokenTime = nowServerTime;
		}
	}
}

// 从讯飞服务器获取语音文本
void GetTextManager::getTextFromXunFei(const char* uuid, const std::string& spxData, int recordTime, int voiceChanelid)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetTextManager::getTextFromXunFei uuid=%s, recordTime=%d, voiceChanelid=%d, spxData=%s", uuid, recordTime, voiceChanelid, spxData.c_str());
#endif
	HttpOperator* oper = new HttpOperator();
	oper->mStrUrl = getXunFeiServerUrl();
	oper->mStrUrl += "/webapi/index.htm?";
	oper->mStrUrl += "svc=iat&";
	oper->mStrUrl += "token=" + mTocken + "&";
	oper->mStrUrl += "auf=audio/L16;rate=8000&";
	oper->mStrUrl += "aue=speex-wb&";
	oper->mStrUrl += "ent=sms8k";
	oper->mVetHeader.push_back("Content-Type:binary");
	char parText[256] = {};
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	sprintf(parText, "mac=%s&appid=%s", DeviceInfo::sGetNoColonMacAddress(), getXunFeiAppId().c_str());
#else
	sprintf(parText, "mac=%s&appid=%s", DeviceInfo::sGetMacAddress(), getXunFeiAppId().c_str());
#endif
	std::string par = "X-Par:";
	par += Base64::YYEncode((unsigned char*)parText, strlen(parText));
	oper->mStrData = spxData;
	oper->mResponse = httpresponse_selector(GetTextManager::onGetText);
	oper->mType = cocos2d::extension::CCHttpRequest::kHttpPost;
	oper->mVetParam.push_back(uuid);
	char paramText[256] = {};
	sprintf(paramText, "%.1f", recordTime * 0.001f);
	oper->mVetParam.push_back(paramText);
	sprintf(paramText, "%d", voiceChanelid);
	oper->mVetParam.push_back(paramText);
	mOper_GetText.push_back(oper);
}

void GetTextManager::onGetText(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response)
{
	// release后不再处理无效的response
	if (!mLock_GetText) {
		return;
	}

	assert(mOper_GetText.size());
	assert(mOper_GetText[0]->mVetParam.size() == 3);

	std::string strUuid = mOper_GetText[0]->mVetParam[0];
	std::string strTime = mOper_GetText[0]->mVetParam[1];
	int chanelId = atoi(mOper_GetText[0]->mVetParam[2].c_str());

	int retCode = response->getResponseCode();
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetTextManager::onGetText retCode=%d", retCode);
#endif
	if (retCode == 200)
	{
		std::vector<char>* data = response->getResponseData();
		int outByte;
		std::string retData = Base64::YYDecode(data->data(), data->size(), outByte);
		std::vector<std::string> vs = this->stovs(retData);
		int ret = -1;
		std::string result;
		for (size_t i = 0; i < vs.size(); ++i)
		{
			std::string type, param;
			stoparam(type, param, vs[i]);
			if (type == "ret")
				ret = atoi(param.c_str());
			else if (type == "result")
				result = param;
		}
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		LOGE("GetTextManager::onGetText ret=%d", ret);
#endif
		if (ret == 0)
		{
			// 精灵的语音
			if (chanelId == cocos2d::gGetScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", "VOICE_JINGLING_CHANNEL")) {
				cocos2d::gGetScriptEngine()->executeGlobalFunctionWithStringData("JinglingDlg.globalSetSearchText", result.c_str());
			}
			// 语音聊天
			else {
				doGetTextResult(strUuid, result);
			}
		}
		else
		{
			doGetTextResult(strUuid, "");
			//gGetVoiceManager()->GetTextFail(strUuid.c_str());
		}
	}
	else
	{
		doGetTextResult(strUuid, "");
	}

	PopOperator_GetText();
}

void GetTextManager::doGetTextResult(const std::string& strUuid, const std::string& result)
{
	std::wstring content = L"<T t=\"" + s2ws(result.c_str()) + L"\" c=\"FF693F00\"></T>";
	gGetVoiceManager()->GetTextDone(strUuid.c_str(), content.c_str());
}

void GetTextManager::PopOperator_GetText()
{
	mFreeOper_GetText.push_back(*mOper_GetText.begin());
	mOper_GetText.erase(mOper_GetText.begin());
	mLock_GetText = false;
}

void GetTextManager::requestTocken()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetTextManager::requestTocken mTocken=%s, mTockenExpires=%f", mTocken.c_str(), mTockenExpires);
#endif
	if (mTockenOperator)
	{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		LOGE("GetTextManager::requestTocken delete mTockenOperator");
#endif
		delete mTockenOperator;
		mTockenOperator = NULL;
	}

	mTockenOperator = new HttpOperator;
	mTockenOperator->mStrUrl = getXunFeiServerUrl();
	mTockenOperator->mStrUrl += "/webapi/index.htm?svc=token";
	char text[256] = {};
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	sprintf(text, "mac=%s&appid=%s", DeviceInfo::sGetNoColonMacAddress(), getXunFeiAppId().c_str());
#else
	sprintf(text, "mac=%s&appid=%s", DeviceInfo::sGetMacAddress(), getXunFeiAppId().c_str());
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetTextManager::requestTocken mTockenOperator->mStrUrl=%s, text=%s", mTockenOperator->mStrUrl.c_str(), text);
#endif
	std::string par = "{X-Par:";
	par += Base64::YYEncode((unsigned char*)text, strlen(text));
	par += "}";
	mTockenOperator->mVetHeader.push_back(par);
	mTockenOperator->mResponse = httpresponse_selector(GetTextManager::onTocken);
	mTockenOperator->mType = cocos2d::extension::CCHttpRequest::kHttpGet;
	mTockenOperator->Send(this);
}


void GetTextManager::onTocken(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetTextManager::onTocken start....");
#endif
	// release后不再处理无效的response
	if (!mTockenOperator) {
		return;
	}

	int retCode = response->getResponseCode();
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetTextManager::onTocken retCode=%d", retCode);
#endif
	if (retCode == 200)
	{
		std::vector<char>* data = response->getResponseData();
		int outByte;
		std::string retData = Base64::YYDecode(data->data(), data->size(), outByte);

		std::vector<std::string> vs = stovs(retData);
		int ret = -1;
		std::string token;
		int expires = -1;
		for (size_t i = 0; i < vs.size(); ++i)
		{
			std::string type, param;
			stoparam(type, param, vs[i]);
			if (type == "ret")
				ret = atoi(param.c_str());
			else if (type == "token")
				token = param.c_str();
			else if (type == "expires")
				expires = atoi(param.c_str());
		}
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		LOGE("GetTextManager::onTocken ret=%d", ret);
#endif
		if (ret == 0)// 登陆成功.
		{
			mTocken = token;
			mTockenExpires = expires;
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
			LOGE("GetTextManager::onGetText mTocken=%s, mTockenExpires=%f", mTocken.c_str(), mTockenExpires);
#endif
		}
	}

	if (mTockenOperator)
	{
		delete mTockenOperator;
		mTockenOperator = NULL;
	}
}

GetTextManager* gGetGetTextManager()
{
	static GetTextManager mgr;
	return &mgr;
}

/*****************************************************************/

bool GetTextManager::stoparam(std::string& type, std::string& param, const std::string& word)
{
	bool isParam = false;

	type.clear();
	param.clear();

	for (size_t i = 0; i < word.size(); ++i)
	{
		if (word[i] == ':')
		{
			isParam = true;
			continue;
		}
		if (word[i] != '\"')
		{
			if (isParam)
				param.push_back(word[i]);
			else
				type.push_back(word[i]);
		}
	}

	return true;
}

std::vector<std::string> GetTextManager::stovs(const std::string& data, char c)
{
	std::vector<std::string> vs;
	std::string s;
	for (size_t i = 0; i < data.size(); ++i)
	{
		if (data[i] == c)
		{
			vs.push_back(s);
			s.clear();
		}
		else
			s += data[i];
	}
	if (s.size())
		vs.push_back(s);
	return vs;
}

std::vector<std::string> GetTextManager::stovs(const std::string& data)
{
	std::vector<std::string> vs;
	std::string s;
	for (size_t i = 1; i < data.size() - 1; ++i)
	{
		if (data[i] == '\r' || data[i] == '\n')
			continue;
		if (data[i] == ',')
		{
			vs.push_back(s);
			s.clear();
		}
		else
		{
			s += data[i];
		}
	}
	if (s.size())
		vs.push_back(s);

	return vs;
}

const std::string GetTextManager::getXunFeiServerUrl()
{
	std::string url = gGetGameApplication()->GetXunFeiServerAddress();
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetTextManager::getXunFeiServerUrl() url=%s", url.c_str());
#endif
	return url;
}

const std::string GetTextManager::getXunFeiAppId()
{
	std::string appid = gGetGameApplication()->GetXunFeiAppId();
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetTextManager::getXunFeiAppId() appid=%s", appid.c_str());
#endif
	return appid;
}

/*****************************************************************/

SendVoiceManager::SendVoiceManager()
	: mLock_SendVoice(false)
{
}

bool SendVoiceManager::initialize()
{
	return true;
}

void SendVoiceManager::tick(unsigned int deltaTime)
{
	// 释放操作
	while (mFreeOper_SendVoice.size())
	{
		delete mFreeOper_SendVoice[0];
		mFreeOper_SendVoice.erase(mFreeOper_SendVoice.begin());
	}

	// 只允许发送一次HTTP请求
	if (mLock_SendVoice) {
		return;
	}

	// 发送请求
	if (mOper_SendVoice.size())
	{
		mOper_SendVoice[0]->Send(this);
		mLock_SendVoice = true;
	}
}

void SendVoiceManager::release()
{
	while (mFreeOper_SendVoice.size())
	{
		delete mFreeOper_SendVoice[0];
		mFreeOper_SendVoice.erase(mFreeOper_SendVoice.begin());
	}

	while (mOper_SendVoice.size())
	{
		delete mOper_SendVoice[0];
		mOper_SendVoice.erase(mOper_SendVoice.begin());
	}

	mLock_SendVoice = false;
}

void SendVoiceManager::PopOperator_SendVoice()
{
	mFreeOper_SendVoice.push_back(*mOper_SendVoice.begin());
	mOper_SendVoice.erase(mOper_SendVoice.begin());
	mLock_SendVoice = false;
}

// 上传语音文件到语音服务器
void SendVoiceManager::sendVoiceToServer(const char* uuid, const std::string& spxData, unsigned int recordTime)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("SendVoiceManager::sendVoiceToServer uuid=%s, recordTime=%d, spxData=%s", uuid, recordTime, spxData.c_str());
#endif
	HttpOperator* oper = new HttpOperator();

	oper->mStrUrl = getChatServerUrl();

	oper->mStrUrl += "transServer/trans";

	std::string text = "{\"svc\":\"iat\",";
	char timeText[256] = {};
	sprintf(timeText, "\"sec\":%.1f,", recordTime * 0.001f);
	text += timeText;
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	text += "\"mac\":" + std::string(DeviceInfo::sGetNoColonMacAddress()) + ",";
#else
	text += "\"mac\":" + std::string(DeviceInfo::sGetMacAddress()) + ",";
#endif
	text += "\"auf\":\"audio/L16;rate=8000\",";
	text += "\"ent\":\"sms8k\",";
	text += "\"aue\":\"speex-wb\",";
	text += "\"uuid\":\"" + std::string(uuid) + "\",";
	text += "\"speech\":\"" + spxData;
	text += "\"}";
	oper->mStrData = text;
	oper->mVetParam.push_back(uuid);

	oper->mResponse = httpresponse_selector(SendVoiceManager::onSendVoice);

	oper->mType = cocos2d::extension::CCHttpRequest::kHttpPost;

	mOper_SendVoice.push_back(oper);
}

// 上传语音文件的response
void SendVoiceManager::onSendVoice(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response)
{
	// release后不再处理无效的response
	if (!mLock_SendVoice) {
		return;
	}

	std::string strUuid = mOper_SendVoice[0]->mVetParam[0];

	int retCode = response->getResponseCode();
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("SendVoiceManager::onSendVoice strUuid=%s, retCode=%d", strUuid.c_str(), retCode);
#endif
	if (retCode == 200)
	{
		// 上传数据成功
		gGetVoiceManager()->SendVoiceDone(strUuid.c_str());
	}
	else
	{
		gGetVoiceManager()->SendVoiceFail(strUuid.c_str());
	}

	PopOperator_SendVoice();
}

SendVoiceManager* gGetSendVoiceManager()
{
	static SendVoiceManager mgr;
	return &mgr;
}

/*****************************************************************/

GetVoiceManager::GetVoiceManager()
	: mLock_GetVoice(false)
{
}

bool GetVoiceManager::initialize()
{
	return true;
}

void GetVoiceManager::tick(unsigned int deltaTime)
{
	// 释放操作
	while (mFreeOper_GetVoice.size())
	{
		delete mFreeOper_GetVoice[0];
		mFreeOper_GetVoice.erase(mFreeOper_GetVoice.begin());
	}

	// 只允许发送一次HTTP请求
	if (mLock_GetVoice) {
		return;
	}

	// 发送请求
	if (mOper_GetVoice.size())
	{
		mOper_GetVoice[0]->Send(this);
		mLock_GetVoice = true;
	}
}

void GetVoiceManager::release()
{
	while (mFreeOper_GetVoice.size())
	{
		delete mFreeOper_GetVoice[0];
		mFreeOper_GetVoice.erase(mFreeOper_GetVoice.begin());
	}

	while (mOper_GetVoice.size())
	{
		delete mOper_GetVoice[0];
		mOper_GetVoice.erase(mOper_GetVoice.begin());
	}

	mLock_GetVoice = false;
}

// 从语音服务器下载语音文件
void GetVoiceManager::getVoiceFromServer(const char* uuid, float fTime, const char* yuyinBtnName, bool bTouchPlay)
{
	// 如果语音已经存在，则直接播放
	std::string strFilePathName = getFilePathName(uuid);
	FILE* fp = fopen(strFilePathName.c_str(), "rb");
	if (fp)
	{
		fclose(fp);
		gGetVoiceManager()->tryPlayEffect(strFilePathName.c_str(), yuyinBtnName, fTime, bTouchPlay);
		return;
	}

	// 除了正在操作的语音请求，其余的语音请求全部删除.
	if (mOper_GetVoice.size())
	{
		std::vector<HttpOperator*>::iterator it = mOper_GetVoice.begin();
		if (mLock_GetVoice)
		{
			it++;
		}
		while (it != mOper_GetVoice.end())
		{
			if ((*it)->mStrUrl == getChatServerUrl())
				mOper_GetVoice.erase(it++);
			else
				++it;
		}
	}

#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetVoiceManager::getVoiceFromServer uuid=%s, yuyinBtnName=%s, bTouchPlay=%d", uuid, yuyinBtnName, bTouchPlay);
#endif
	// 发送新的语音请求.
	HttpOperator* oper = new HttpOperator;
	oper->mStrUrl = getChatServerUrl();
	oper->mStrUrl += "iat/";
	oper->mStrUrl += uuid;
	oper->mType = cocos2d::extension::CCHttpRequest::kHttpGet;
	oper->mResponse = httpresponse_selector(GetVoiceManager::onGetVoice);
	oper->mVetParam.push_back(uuid);
	oper->mVetParam.push_back(yuyinBtnName);
	oper->mVetParam.push_back(StringCover::floatToString(fTime));
	if (bTouchPlay)
		oper->mVetParam.push_back("true");
	else
		oper->mVetParam.push_back("false");
	mOper_GetVoice.push_back(oper);
}

void GetVoiceManager::onGetVoice(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response)
{
	// release后不再处理无效的response
	if (!mLock_GetVoice) {
		return;
	}

	int retCode = response->getResponseCode();
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("GetVoiceManager::onGetVoice retCode=%d", retCode);
#endif
	if (retCode == 200)
	{
		std::string strUuid = mOper_GetVoice[0]->mVetParam[0];
		std::string yuyinBtnName = mOper_GetVoice[0]->mVetParam[1];
		float fTime = CEGUI::PropertyHelper::stringToFloat(mOper_GetVoice[0]->mVetParam[2]);
		bool bTouchPlay = false;
		if (mOper_GetVoice[0]->mVetParam[3] == "true")
			bTouchPlay = true;

		// 下载数据,含BASE64
		std::vector<char>* data = response->getResponseData();

		// 解BASE64
		int outByte;
		std::string voiceData = Base64::YYDecode(data->data(), data->size(), outByte);
		std::string strFilePathName = getFilePathName(strUuid.c_str());

		std::string strAmrPath = swapWavAmrFileName(strFilePathName, true);
		FILE* fpamr = fopen(strAmrPath.c_str(), "wb");
		if (fpamr)
		{
			fwrite(&voiceData[0], voiceData.size(), 1, fpamr);
			fclose(fpamr);
		}

		// 由amr文件转换成wav文件
		int nFrameCount = decodeAMR2WAV(strAmrPath.c_str(), strFilePathName.c_str());
		if (nFrameCount > 0)
		{
			// 播放声音
			gGetVoiceManager()->tryPlayEffect(strFilePathName.c_str(), yuyinBtnName.c_str(), fTime, bTouchPlay);
		}
	}

	PopOperator_GetVoice();
}

void GetVoiceManager::PopOperator_GetVoice()
{
	mFreeOper_GetVoice.push_back(*mOper_GetVoice.begin());
	mOper_GetVoice.erase(mOper_GetVoice.begin());
	mLock_GetVoice = false;
}

GetVoiceManager* gGetGetVoiceManager()
{
	static GetVoiceManager mgr;
	return &mgr;
}

/*****************************************************************/

std::string UrlEnCode(const std::string& str)
{
	string encodeUrl = "";
	int len = str.length();
	if (len == 0)
	{
		return encodeUrl;
	}

	std::string sDontEncode = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_~.";

	for (int i = 0; i < len; i++)
	{
		char cToFind = str.at(i);
		if (-1 == sDontEncode.find(cToFind, 0))
		{
			std::string tmp = UrlEncodeFormat(cToFind);
			encodeUrl.append(tmp);
		}
		else if (cToFind == ' ')
		{
			encodeUrl += '+';
		}
		else{
			encodeUrl += cToFind;
		}
	}

	return encodeUrl;
}

std::string UrlEncodeFormat(const unsigned char cValue)
{
	string tmp;
	tmp.append("%");
	unsigned int nDiv = cValue / 16;
	unsigned int nMod = cValue % 16;
	tmp.append(DecimalToHexString(nDiv));
	tmp.append(DecimalToHexString(nMod));

	return tmp;
}

std::string DecimalToHexString(unsigned int nValue)
{
	std::string tmp;
	if (nValue < 10)
	{
		tmp += ((char)nValue + 48);
	}
	else{
		switch (nValue)
		{
		case 10:tmp.append("A"); break;
		case 11:tmp.append("B"); break;
		case 12:tmp.append("C"); break;
		case 13:tmp.append("D"); break;
		case 14:tmp.append("E"); break;
		case 15:tmp.append("F"); break;
		default:break;

		}
	}
	return tmp;
}

// 生成UUID
std::string createUUID()
{
	char uuid[256] = {};

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	SYSTEMTIME sm;
	GetSystemTime(&sm);
	sprintf(uuid, "%s_%d_%d_%d_%d_%d_%d_%d", (unsigned char*)DeviceInfo::sGetNoColonMacAddress(),
		sm.wYear, sm.wMonth, sm.wDay, sm.wHour, sm.wMinute, sm.wSecond, sm.wMilliseconds);
#else
	time_t timep;
	time(&timep);
	struct tm* p = gmtime(&timep);
	sprintf(uuid, "%s_%d_%d_%d_%d_%d_%d_%d", (unsigned char*)DeviceInfo::sGetMacAddress(),
		p->tm_year, p->tm_mon, p->tm_wday, p->tm_hour, p->tm_min, p->tm_sec, (unsigned int)clock());
#endif

	std::string strUuid = uuid;
	return strUuid;
}

std::string getFilePathName(const char* uuid)
{
	std::string strFilePath = gGetWavRecorder()->getTempFilePath();
	std::string strFilePathName;

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	strFilePathName = strFilePath + "/" + uuid + ".wav";
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	strFilePathName = strFilePath + uuid + ".wav";
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	strFilePathName = strFilePath + uuid + ".wav";
#endif

	return strFilePathName;
}

std::string getChatServerUrl()
{
	std::string strUrl = gGetGameApplication()->GetVoiceServerAddress();
	if (strUrl.size() == 0) {
		return "http://42.62.98.140:9862/"; // 外网测试服务器
	}
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("getChatServerUrl() strUrl=%s", strUrl.c_str());
#endif
	return strUrl;
}
