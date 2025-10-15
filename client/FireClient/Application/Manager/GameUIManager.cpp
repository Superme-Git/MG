#include "stdafx.h"
#include "GameUIManager.h"
#include "engine/nucocos2d_wraper.h"
#if defined WIN7_32
#include "engine/nusystemresourcemanager.h"
#endif
#include "shaders/CCShaderCache.h"
#include "shaders/CCGLProgram.h"
#include "GameApplication.h"
#include "GameStateManager.h"
#include "GameOperateState.h"
#include "TaskOnOffEffectManager.h"
#include "script_support/CCScriptSupport.h" 
#include "CCLuaEngine.h"
#include "IconManager.h"
#include "EmotionManager.h"
#include "NewRoleGuideManager.h"
#include "RoleItemManager.h"
#include "MainCharacter.h"
#include "CheckDistanceTimer.h"

#include "SceneMovieManager.h"
#include "GameScene.h"
#include "GameApplication.h"
#include "SimpleAudioEngine.h"
#include "ConfigManager.h"
#include "engine/nuengine.h"
#include "sprite/nuspritemanager.h"
#include "UISpineSprite.h"
#include "LoginManager.h"
#include "BattleManager.h"
#include "MessageManager.h"
#include "effect/nuspineeffect.h"
#include "TableDataManager.h"

#include "cocos2d.h"

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
#include "WinWebBrowser/WinSDK.h"
#endif

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
#include "WebViewController.h"
#endif

#include "ljfsfile.h"
#include "ljfszipfile.h"
#include "platform/platform_types.h"

#define NpcTalkDelayTime 0.2f
#define HideAllUITime	500

#ifdef ANDROID
#include "androidcommon/ChannelPlatformInterface.h"
#include "androidcommon/AndroidLoginDialog.h"
#include "SDJniHelper.h"
#include <jni.h>
#endif
#include "ChineseCode.h"

#include "ResolutionAdapter.h"

#ifdef XP_PERFORMANCE
#include "ljfmopen.h"
#include "ljfmimage.h"
#endif

#include "BattleReplayManager.h"

#define TIPSDISHEIGHT 4

const DWORD  s_dwLeftButtonClickTime = 250;			//鼠标按键的时间间�?
const unsigned int s_uiMaxUIUpdateTimePreFPS = 40;	//设置UI限帧每帧时间间隔�?0ms�?
const unsigned int s_uiMaxUIDrawTimePreFPS = 40;	//设置UI限帧每帧时间间隔�?0ms�?

float tipsStartYPos = 0.5f;

#define UISoundRes L"/sound/ui/"
#define NPCSoundRes L"/sound/chat/"

#include "GameTable/common/CCommon.h"

#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
extern "C"
{
	#include "GameSdk.h"
}
#else
	#import "GameSdk.h"
#endif

#endif
#include "UISprite.h"
#ifdef WIN7_32
#include "ReportManager_Win.h"
#elif defined ANDROID
#include "ReportManager_Android.h"
#else//IOS
#include "ReportManager_IOS.h"
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

#include "MusicSoundVolumeMixer.h"
#ifdef ANDROID
#include "ChannelManager.h"
#endif

/***********************************************************************
Static / Const data
*************************************************************************/

const CEGUI::String GameUImanager::UnsignedIntegerValidator("[1-9]?\\d*");    //!< Validator regex used for decimal integer mode.
const CEGUI::String GameUImanager::Chinese_Charset(L"[\u4e00-\u9fa5]*");
extern ResolutionAdapter g_adapter;

void UIEffectWithExistXpRenderEffectNotify::OnEnd(Nuclear::IEffect *pEffect)
{
	if (pEffect)
	{
		pEffect->RemoveNotify(this);
		delete this;
	}
}

void UIEffectWithExistXpRenderEffectNotify::OnDelete(Nuclear::IEffect *pEffect)
{
	delete this;
}

void UIEffectNotify::OnEnd(Nuclear::IEffect *pEffect)
{
    if (pEffect)
    {
        pEffect->RemoveNotify(this);
        gGetGameUIManager()->RemoveUIEffect(pEffect);
        delete this;
    }
}

void UIEffectNotify::OnDelete(Nuclear::IEffect *pEffect)
{
    delete this;
}

void SysMesBeginEffectNotify::OnEnd(Nuclear::IEffect *pEffect)
{
	if (this == NULL)
		return;

	if (pEffect)
	{
		pEffect->RemoveNotify(this);

		delete this;
	}
}

void SysMesBeginEffectNotify::OnDelete(Nuclear::IEffect *pEffect)
{
	delete this;
}

void SysMesFadeEffectNotify::OnEnd(Nuclear::IEffect *pEffect)
{
	if (pEffect)
	{
		pEffect->RemoveNotify(this);
		gGetGameUIManager()->OnSysMesEnd();

		delete this;
	}
}

void ScreenEffectNotify::OnDelete(Nuclear::IEffect *pEffect)
{
	delete this;
}

void ScreenEffectNotify::OnEnd(Nuclear::IEffect *pEffect)
{
	if (pEffect)
	{
		pEffect->RemoveNotify(this);
		gGetGameUIManager()->RemoveScreenEffect(pEffect);
		delete this;
	}
}

void SysMesFadeEffectNotify::OnDelete(Nuclear::IEffect *pEffect)
{
	delete this;
}

SystemMsgRenderEffect::SystemMsgRenderEffect()
{ }

SystemMsgRenderEffect::~SystemMsgRenderEffect()
{ }

void SystemMsgRenderEffect::performPostRenderFunctions()
{
	if (gGetGameUIManager())
	{
		gGetGameUIManager()->DrawSysMsgEffect();
	}
}

GameUImanager::GameUImanager() 
:m_pCEGUICocos2DRender(NULL)
,m_pSysBoardMes(NULL)
,m_iMessageTipNum(0)
,m_pSysMesHoldEffect(NULL)
,m_pSysMesEndEffect(NULL)
,m_bGameUIVisible(true)
,m_pResourceProvider(NULL)
,m_pRootWindow(NULL)
,m_bShowGameUI(true)
,m_bEnableDlgSound(false)
,m_bUIInited(false)
,m_bUIPostInited(false)
,m_pUIIMEDelegate(NULL)
,m_ptDragBenginPoint(0.0f, 0.0f)
,m_sNpcSound("")
,m_iCurNpcId(0)
,m_iCurItemId(-1)
,m_bSuperMapEnable(false)
,m_fMaxHeight(0)
,m_bHideAllUI(false) 
,m_bStartHideAllUI(false)
,m_ConsumeHideAllUITime(0)
#if defined DEBUG || defined _DEBUG
,m_pShowTextureBtn(NULL)
,m_pTextureNameEdit(NULL)
,m_fSdkOutTime(0.0)
,m_blnSdkOutTime(false)
, m_iOnUI(0)
#endif
{
	m_updateDeltaTime = 0;
	m_drawDeltaTime = 0;

	m_fBoundsEffectSpeed = StringCover::WStrToNum<float>(GameTable::common::GetCCommonTableInstance().getRecorder(169).value);
	m_fHeightFactor = StringCover::WStrToNum<float>(GameTable::common::GetCCommonTableInstance().getRecorder(179).value);
	m_fTipDisplayTime = StringCover::WStrToNum<float>(GameTable::common::GetCCommonTableInstance().getRecorder(178).value);

	m_bLoginProgress = false;	// ANDROID 解决进度条花屏处�?// ycl
    
#ifdef TARGET_OS_IPHONE
    m_pEditBox = new GameEditBox();
    float fx = (float)g_adapter.GetScreenWidth() / (float)g_adapter.GetLogicWidth();
    float fy = (float)g_adapter.GetScreenHeight() / (float)g_adapter.GetLogicHeight();
    m_pEditBoxImpl = cocos2d::extension::__createSystemEditBox(NULL);
    m_pEditBoxImpl->initWithSize(cocos2d::CCSize(180* fx, 30 * fy));
    m_pEditBoxImpl->setPosition(cocos2d::CCPoint(107* fx, 34 * fy));
    m_pEditBoxImpl->setMaxLength(5);
    m_pEditBoxImpl->setDelegate(m_pEditBox);
    
#endif
}

GameUImanager::~GameUImanager()
{
	if (NULL != m_pResourceProvider)
	{
		delete m_pResourceProvider;
	}

	if (NULL != m_pCEGUICocos2DRender)
	{
		CEGUI::Cocos2DRenderer::destroy(*m_pCEGUICocos2DRender);
	}
    
#ifdef TARGET_OS_IPHONE
    if (m_pEditBoxImpl) {
        delete m_pEditBoxImpl;
        m_pEditBoxImpl = NULL;
    }
    
    if (m_pEditBox) {
        delete m_pEditBox;
        m_pEditBox = NULL;
    }
    
#endif
}

void GetEmotionParseInf(int id, bool& bExist, bool& bExitstNextChar)
{
	bExist = false;
	bExitstNextChar = false;
	if (gGetEmotionManager())
	{
		bExist = gGetEmotionManager()->isEmotionExist(id);
		bExitstNextChar = gGetEmotionManager()->isNextCharEmotionExist(id);
	}
}

bool GameUImanager::isSameNpcSoundPlaying(int iId)
{
	if (m_iCurNpcId == iId)
	{
		return true;
	}
	return false;
}

bool GameUImanager::isNpcSoundPlaying()
{
	if (m_sNpcSound != "")
	{
		return CocosDenshion::SimpleAudioEngine::sharedEngine()->getEffectIsPlaying(m_sNpcSound.c_str());
	}
	return false;
}

int GameUImanager::getCurNpcId()
{
	return m_iCurNpcId;
}

void GameUImanager::setCurNpcId(int iId)
{
	m_iCurNpcId = iId;
}

CEGUI::String& GameUImanager::getCurNpcSound()
{
	return m_sNpcSound;
}

void GameUImanager::setCurNpcSound(const CEGUI::String& soundRes)
{
	m_sNpcSound = soundRes;
}

UISprite* GameUImanager::FindUISprite(int iHandle)
{
	for (std::list<UISprite*>::iterator it = m_UISprites.begin(); it != m_UISprites.end(); it++)
	{
		if ((*it)->GetSprite() == iHandle)
		{
			return (*it);
		}
	}
	return NULL;
}

UISprite* GameUImanager::GetUISprite(int iIndex)
{
	int i = 0;
	for (std::list<UISprite*>::iterator it = m_UISprites.begin(); it != m_UISprites.end(); it++)
	{
		if (i == iIndex)
		{
			return (*it);
		}
		i++;
	}
	return NULL;
}

int GameUImanager::GetUISpriteCount()
{
	return m_UISprites.size();
}

void GameUImanager::ClearUISprite()
{
	m_UISprites.clear();
}

void GameUImanager::RemoveUISprite(UISprite* pUISprite)
{
	for (std::list<UISprite*>::iterator it = m_UISprites.begin(); it != m_UISprites.end(); it++)
	{
		if ((*it) == pUISprite)
		{
			(*it) = NULL;
			m_UISprites.erase(it);
			break;
		}
	}
}

void GameUImanager::AddUISprite(UISprite* pUISprite)
{
	bool alreadyhave = false;
	for (std::list<UISprite*>::iterator it = m_UISprites.begin(); it != m_UISprites.end(); it++)
	{
		if ((*it) == pUISprite)
		{
			alreadyhave = true;
			break;
		}
	}
	if (!alreadyhave)
	{
		m_UISprites.push_back(pUISprite);
	}
}

UISpineSprite* GameUImanager::FindUISpineSprite(int iHandle)
{
	for (std::list<UISpineSprite*>::iterator it = m_UISpineSprites.begin(); it != m_UISpineSprites.end(); it++)
	{
		if ((*it)->GetSprite() == iHandle)
		{
			return (*it);
		}
	}
	return NULL;
}

UISpineSprite* GameUImanager::GetUISpineSprite(int iIndex)
{
	int i = 0;
	for (std::list<UISpineSprite*>::iterator it = m_UISpineSprites.begin(); it != m_UISpineSprites.end(); it++)
	{
		if (i == iIndex)
		{
			return (*it);
		}
		i++;
	}
	return NULL;
}

int GameUImanager::GetUISpineSpriteCount()
{
	return (int)m_UISpineSprites.size();
}

void GameUImanager::ClearUISpineSprite()
{
	m_UISpineSprites.clear();
}

void GameUImanager::RemoveUISpineSprite(UISpineSprite* pUISpineSprite)
{
	for (std::list<UISpineSprite*>::iterator it = m_UISpineSprites.begin(); it != m_UISpineSprites.end(); it++)
	{
		if ((*it) == pUISpineSprite)
		{
			(*it) = NULL;
			m_UISpineSprites.erase(it);
			break;
		}
	}
}

bool GameUImanager::AddUISpineSprite(UISpineSprite* pUISpineSprite)
{
	std::list<UISpineSprite*>::iterator it = std::find(m_UISpineSprites.begin(), m_UISpineSprites.end(), pUISpineSprite);
	if (it == m_UISpineSprites.end())
	{
		m_UISpineSprites.push_back(pUISpineSprite);
		return true;
	}

	return false;
}

bool HandleCheckShied(CEGUI::String& inText)
{
	std::wstring wText = CEGUI::PFSResourceProvider::GUIStringToWString(inText);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(wText).c_str());
	string tmp = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("CChatManager.ShiedText_", 1);

	if (tmp != "")
	{
		inText = s2ws(tmp);
	}
	return true;
}

void HandleUIPlaySound(const CEGUI::String& soundRes)
{
	if (gGetGameConfigManager() && gGetGameConfigManager()->isPlayEffect())
	{
		if (!soundRes.empty())
		{
			std::wstring strSoundRes = UISoundRes + StringCover::to_wstring(soundRes.c_str());
			std::string newSoundRes;
			std::wstring newws;
			newws = LJFM::LJFMF::GetFullPathFileName(strSoundRes);
			if (!newws.empty())
			{
				newSoundRes = StringCover::to_string(newws);
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
				// on iOS, effect will not be cached if use "playEffectWithTimes", it will create a new sound every time
				// when call, the method "playEffect" will cache the sound using srouce path as key, if play a same
				// effect multitimes at the same time, will only create one sound.
				// maybe Android need do something like this too. by lg
				//CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffectWithTimes(newSoundRes.c_str());
				CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffect(newSoundRes.c_str());
#else
				CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffect(newSoundRes.c_str());
#endif
			}
		}
	}
}

void HandleNPCPlaySound(const CEGUI::String& soundRes, const int iNpcId, bool bForcePlay)
{
	if (gGetGameConfigManager() && gGetGameConfigManager()->isPlayEffect())
	{
		if (!soundRes.empty())
		{
			std::wstring strSoundRes = NPCSoundRes + StringCover::to_wstring(soundRes.c_str());
			std::string newSoundRes;
			std::wstring newws;
			newws = LJFM::LJFMF::GetFullPathFileName(strSoundRes);
			if (!newws.empty())
			{
				newSoundRes = StringCover::to_string(newws);
				if (!gGetGameUIManager()->isNpcSoundPlaying())
				{
					CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffect(newSoundRes.c_str());
					gGetGameUIManager()->setCurNpcSound(newSoundRes);
					gGetGameUIManager()->setCurNpcId(iNpcId);
					if (MusicSoundVolumeMixer::GetInstance())
						MusicSoundVolumeMixer::GetInstance()->notifySoundStart(newSoundRes);
				}
				else
				{
					bool bPlay = false;
					if (bForcePlay)
					{
						bPlay = true;
					}
					else
					{
						if (iNpcId != gGetGameUIManager()->getCurNpcId())
						{
							bPlay = true;
						}
					}

					if (bPlay)
					{
						CocosDenshion::SimpleAudioEngine::sharedEngine()->stopEffectByPath(gGetGameUIManager()->getCurNpcSound().c_str());
						CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffect(newSoundRes.c_str());
						gGetGameUIManager()->setCurNpcSound(newSoundRes);
						gGetGameUIManager()->setCurNpcId(iNpcId);
						if (MusicSoundVolumeMixer::GetInstance())
							MusicSoundVolumeMixer::GetInstance()->notifySoundStart(newSoundRes);
					}

				}
			}
		}
	}
}

void GameUImanager::PlayNPCSound(const CEGUI::String& soundRes, const int iNpcId, bool bForcePlay)
{
	if (m_bEnableDlgSound)
	{
		HandleNPCPlaySound(soundRes, iNpcId, bForcePlay);
	}
}

void GameUImanager::PlayUISound(const CEGUI::String& soundRes)
{
	if (m_bEnableDlgSound)
	{
		HandleUIPlaySound(soundRes);
	}
}

void GameUImanager::EnableDlgSound(bool bEnable)
{
	m_bEnableDlgSound = bEnable;
}

bool GameUImanager::isEnableDlgSound()
{
	return m_bEnableDlgSound;
}

void GameUImanager::setSuperTreasureMapEnable(bool bEnable)
{
	m_bSuperMapEnable = bEnable;
}

bool GameUImanager::isSuperTreasureMapEnable()
{
	return m_bSuperMapEnable;
}

void GameUImanager::SetShowGameUI(bool bSHow)
{
	m_bShowGameUI = bSHow;
}

void GameUImanager::AddWndToRootWindow(CEGUI::Window* pWnd)
{
	if (m_pRootWindow)
	{
		m_pRootWindow->addChildWindow(pWnd);
	}
}

Nuclear::IEffect*  GameUImanager::GetEffectBySurface(CEGUI::RenderingSurface* pSurface)
{
	if (pSurface != NULL)
	{
		std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.begin();
		for (; it != m_mapUIEffect.end(); ++it)
		{
			CEGUI::Window* pWnd = it->second;
			if (pWnd->getRenderingSurface() == pSurface)
			{
				return it->first;
			}
		}
	}

	return NULL;
}

bool GameUImanager::OnWndMove(const CEGUI::EventArgs& e)
{
	const CEGUI::WindowEventArgs& wndArgs = static_cast<const CEGUI::WindowEventArgs&>(e);
	CEGUI::Window* pWnd = wndArgs.window;
	if (pWnd != NULL)
	{
		std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.begin();
		for (; it != m_mapUIEffect.end(); ++it)
		{
			if (it->second == pWnd)
			{
				Nuclear::IEffect* pEffect = it->first;
				Nuclear::NuclearLocation effectPos;


				std::map<Nuclear::IEffect*, Nuclear::NuclearLocation>::iterator locItor = m_mapUIEffectLocation.find(pEffect);
				if (locItor != m_mapUIEffectLocation.end())
				{
					effectPos = locItor->second;
					if (effectPos.x == 0 && effectPos.y == 0)
					{
						CEGUI::Point pt = pWnd->GetScreenPosOfCenter();
						effectPos.x = static_cast<int>(pt.d_x);
						effectPos.y = static_cast<int>(pt.d_y);
					}
					else
					{
						CEGUI::Point pt = pWnd->GetScreenPos();
						effectPos.x += static_cast<int>(pt.d_x);
						effectPos.y += static_cast<int>(pt.d_y);
					}
				}
				else
				{
					CEGUI::Point centerPt = pWnd->GetScreenPosOfCenter();
					effectPos.x = static_cast<int>(centerPt.d_x);
					effectPos.y = static_cast<int>(centerPt.d_y);
				}
				printf("location x = %d, y = %d\n", effectPos.x, effectPos.y);
				pEffect->SetLocation(effectPos);

			}
		}
	}

	return true;
}

bool GameUImanager::OnWndUpdata(const CEGUI::EventArgs& e)
{
	const CEGUI::WindowEventArgs& wndArgs = static_cast<const CEGUI::WindowEventArgs&>(e);
	CEGUI::Window* pWnd = wndArgs.window;
	if (pWnd)
	{
		pWnd->invalidate();
	}

	return true;
}

void GameUImanager::RemoveAllUIEffect()
{
	std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.begin();
	for (; it != m_mapUIEffect.end(); ++it)
	{
		CEGUI::Window* pWnd = it->second;
		if (pWnd)
		{
			std::map<Nuclear::IEffect*, Nuclear::NuclearLocation>::iterator locItor = m_mapUIEffectLocation.find(it->first);
			if (locItor != m_mapUIEffectLocation.end())
			{
				m_mapUIEffectLocation.erase(locItor);
			}

			Nuclear::GetEngine()->ReleaseEffect(it->first);
			pWnd->getGeometryBuffer()->setRenderEffect(0);
		}
	}
	m_mapUIEffect.clear();
}

bool GameUImanager::OnSpriteWndDestroy(const CEGUI::EventArgs& e)
{
	const CEGUI::WindowEventArgs& wndArgs = static_cast<const CEGUI::WindowEventArgs&>(e);
	CEGUI::Window* pWnd = wndArgs.window;
	if (pWnd != NULL)
	{
		std::map<UISprite*, CEGUI::Window*>::iterator it = m_mapWindowSprite.begin();
		for (; it != m_mapWindowSprite.end();)
		{
			if (it->second == pWnd)
			{
				std::map<UISprite*, Nuclear::NuclearLocation>::iterator locItor = m_mapSpritePos.find(it->first);
				if (locItor != m_mapSpritePos.end())
				{
					m_mapSpritePos.erase(locItor);
				}

				delete it->first;
				pWnd->getGeometryBuffer()->setRenderEffect(0);
				m_mapWindowSprite.erase(it++);
				break;
			}
			else
			{
				++it;
			}
		}
	}
	return true;
}

bool GameUImanager::OnWndDestroy(const CEGUI::EventArgs& e)
{
	const CEGUI::WindowEventArgs& wndArgs = static_cast<const CEGUI::WindowEventArgs&>(e);
	CEGUI::Window* pWnd = wndArgs.window;
	if (pWnd != NULL)
	{
		std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.begin();
		for (; it != m_mapUIEffect.end();)
		{
			if (it->second == pWnd)
			{
				if (gGetNewRoleGuideManager())
				{
					gGetNewRoleGuideManager()->RemoveGuidePaticleEffect(it->first);
				}
				RemoveBoundsEffect(it->first);

				std::map<Nuclear::IEffect*, Nuclear::NuclearLocation>::iterator locItor = m_mapUIEffectLocation.find(it->first);
				if (locItor != m_mapUIEffectLocation.end())
				{
					m_mapUIEffectLocation.erase(locItor);
				}

				Nuclear::GetEngine()->ReleaseEffect(it->first);
				pWnd->getGeometryBuffer()->setRenderEffect(0);
				m_mapUIEffect.erase(it++);
			}
			else
			{
				++it;
			}
		}
	}
	return true;

}

bool GameUImanager::isEffectWndVis(Nuclear::IEffect* pEffect)

{
	std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.find(pEffect);
	if (it != m_mapUIEffect.end())
	{
		CEGUI::Window* pWnd = it->second;
		if (pWnd->isVisible() && pWnd != NULL)
		{
			return true;
		}
	}
	return false;
}

void GameUImanager::RemoveWindowSpriteByWnd(CEGUI::Window* pWnd)
{
	if (pWnd)
	{
		std::map<UISprite*, CEGUI::Window*>::iterator it = m_mapWindowSprite.begin();
		for (; it != m_mapWindowSprite.end();)
		{
			if (it->second == pWnd)
			{
				std::map<UISprite*, Nuclear::NuclearLocation>::iterator locItor = m_mapSpritePos.find(it->first);
				if (locItor != m_mapSpritePos.end())
				{
					m_mapSpritePos.erase(locItor);
				}

				delete it->first;
				it->second->getGeometryBuffer()->setRenderEffect(0);
				m_mapWindowSprite.erase(it++);
			}
			else
			{
				++it;
			}
		}
	}
}

void GameUImanager::RemoveUIEffect(CEGUI::Window* pWnd)
{
	if (pWnd)
	{
		std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.begin();
		for (; it != m_mapUIEffect.end();)
		{
			CEGUI::Window* pEffectWnd = it->second;

			if (pEffectWnd == pWnd)
			{
				if (gGetNewRoleGuideManager())
				{
					gGetNewRoleGuideManager()->RemoveGuidePaticleEffect(it->first);
				}
				RemoveBoundsEffect(it->first);

				std::map<Nuclear::IEffect*, Nuclear::NuclearLocation>::iterator locItor = m_mapUIEffectLocation.find(it->first);
				if (locItor != m_mapUIEffectLocation.end())
				{
					m_mapUIEffectLocation.erase(locItor);
				}

				Nuclear::GetEngine()->ReleaseEffect(it->first);
				pEffectWnd->getGeometryBuffer()->setRenderEffect(0);
				m_mapUIEffect.erase(it++);
			}
			else
			{
				++it;
			}
		}
	}
}

bool GameUImanager::IsWindowHaveEffect(CEGUI::Window* pWnd)
{
	if (pWnd == NULL)
		return false;

	std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.begin();
	for (; it != m_mapUIEffect.end(); ++it)
	{
		if (it->second == pWnd)
		{
			return true;
		}
	}

	return false;
}

void GameUImanager::RemoveWindowSprite(UISprite* pSprite)
{
	if (pSprite)
	{
		std::map<UISprite*, Nuclear::NuclearLocation>::iterator locItor = m_mapSpritePos.find(pSprite);
		if (locItor != m_mapSpritePos.end())
		{
			m_mapSpritePos.erase(locItor);
		}

		std::map<UISprite*, CEGUI::Window*>::iterator it = m_mapWindowSprite.find(pSprite);
		if (it != m_mapWindowSprite.end())
		{
			delete pSprite;
			it->second->getGeometryBuffer()->setRenderEffect(0);
			m_mapWindowSprite.erase(it);
		}
	}
}

void GameUImanager::RemoveUIEffect(Nuclear::IEffect* pEffect)
{
	if (pEffect)
	{
		if (gGetNewRoleGuideManager())
		{
			gGetNewRoleGuideManager()->RemoveGuidePaticleEffect(pEffect);
		}

		RemoveBoundsEffect(pEffect);

		std::map<Nuclear::IEffect*, Nuclear::NuclearLocation>::iterator locItor = m_mapUIEffectLocation.find(pEffect);
		if (locItor != m_mapUIEffectLocation.end())
		{
			m_mapUIEffectLocation.erase(locItor);
		}

		CEGUI::Window* pWnd = NULL;
		std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.find(pEffect);
		if (it != m_mapUIEffect.end())
		{
			pWnd = it->second;
			m_mapUIEffect.erase(it);
			Nuclear::GetEngine()->ReleaseEffect(pEffect);
		}

		//C++处理特效播放完毕
		Dialog::OnEffectOver(pEffect);

		//LUA处理特效播放完毕
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("Dialog.OnEffectOver", pEffect, 0.0f, 0.0f);
	}
}

void GameUImanager::RemoveAllBoundsEffect()
{
	std::map<Nuclear::IEffect*, float> mapTemp = m_mapBoundsEffect;

	std::map<Nuclear::IEffect*, float>::iterator it = mapTemp.begin();
	while (it != mapTemp.end())
	{
		Nuclear::IEffect* pEffect = it->first;
		++it;
		if (pEffect)
		{
			RemoveUIEffect(pEffect);
		}
	}
}

void GameUImanager::RemoveBoundsEffect(Nuclear::IEffect* pEffect)
{
	std::map<Nuclear::IEffect*, float>::iterator it = m_mapBoundsEffect.find(pEffect);
	if (it == m_mapBoundsEffect.end())
	{
		return;
	}
	m_mapBoundsEffect.erase(it->first);
}

CEGUI::Window* GameUImanager::GetWndByEffect(Nuclear::IEffect* pEffect)
{
	if (pEffect)
	{
		std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.find(pEffect);
		if (it != m_mapUIEffect.end())
		{
			return it->second;
		}
	}
	return NULL;
}

void GameUImanager::RenderWindowSprite(XPRenderEffect* pRenderEffect)
{
	if (pRenderEffect)
	{
		if (m_pCEGUICocos2DRender)
		{
			m_pCEGUICocos2DRender->endRendering();
		}

		int     d_ScissorBox[4];
		GLboolean scissorEnable = glIsEnabled(GL_SCISSOR_TEST);
		if (pRenderEffect->m_bClip)
		{
			glEnable(GL_SCISSOR_TEST);

			glGetIntegerv(GL_SCISSOR_BOX, (GLint*)&d_ScissorBox[0]);
			glScissor(pRenderEffect->scissor_x,
				pRenderEffect->scissor_y,
				pRenderEffect->scissor_w,
				pRenderEffect->scissor_h);
		}


		std::map<UISprite*, CEGUI::Window*>::iterator it = m_mapWindowSprite.begin();
		for (; it != m_mapWindowSprite.end(); ++it)
		{
			CEGUI::Window* pWnd = it->second;
			if (pWnd->getGeometryBuffer()->getRenderEffect() == (CEGUI::RenderEffect*)pRenderEffect && pWnd->getEffectiveAlpha() >= 0.95f)
			{
				CEGUI::Point wndPos = pWnd->GetScreenPos();
				std::map<UISprite*, Nuclear::NuclearLocation>::iterator locItor = m_mapSpritePos.find(it->first);
				if (locItor != m_mapSpritePos.end())
				{
					Nuclear::NuclearLocation spritePos = locItor->second;
					spritePos.x += wndPos.d_x;
					spritePos.y += wndPos.d_y;
					it->first->SetUILocation(spritePos);
				}
				it->first->RenderUISprite();
			}
		}

		if (pRenderEffect->m_bClip)
		{
			if (!scissorEnable) {
				glDisable(GL_SCISSOR_TEST);
				glScissor(d_ScissorBox[0],
					d_ScissorBox[1],
					d_ScissorBox[2],
					d_ScissorBox[3]);
			}
		}

		if (m_pCEGUICocos2DRender)
		{
			m_pCEGUICocos2DRender->beginRendering();

		}
	}
}

void GameUImanager::UpdateEffectBoundsPos(Nuclear::IEffect* pEffect, CEGUI::Window* pWnd)
{
	std::map<Nuclear::IEffect*, float>::iterator it = m_mapBoundsEffect.find(pEffect);
	if (it == m_mapBoundsEffect.end())
	{
		return;
	}
	if (!pWnd)
	{
		return;
	}
	int x = 0;
	int y = 0;
	CEGUI::Point screenPt = pWnd->GetScreenPos();
	CEGUI::Size wndSize = pWnd->getPixelSize();

	int width = static_cast<int>(wndSize.d_width);
	int height = static_cast<int>(wndSize.d_height);
	float elapseTime = it->second;


	int totalLength = (int)(elapseTime*m_fBoundsEffectSpeed);
	int rectLength = (width + height) * 2;
	int length = totalLength%rectLength;
	if (length < width)
	{
		x = length;
		y = 0;
	}
	else if (length < (width + height))
	{
		x = width;
		y = length - width;
	}
	else if (length < (2 * width + height))
	{
		x = width - (length - width - height);
		y = height;
	}
	else
	{
		x = 0;
		y = height - (length - 2 * width - height);
	}
	x += (int)screenPt.d_x;
	y += (int)screenPt.d_y;
	if (pEffect)
	{
		pEffect->SetLocation(Nuclear::NuclearLocation(x, y));
	}

}

void GameUImanager::RenderUIEffect(XPRenderEffect* pRenderEffect)
{
	if (pRenderEffect)
	{
		if (m_pCEGUICocos2DRender)
		{
			m_pCEGUICocos2DRender->endRendering();
		}

		int     d_ScissorBox[4];
		GLboolean scissorEnable = glIsEnabled(GL_SCISSOR_TEST);
		if (pRenderEffect->m_bClip)
		{
			glEnable(GL_SCISSOR_TEST);

			glGetIntegerv(GL_SCISSOR_BOX, (GLint*)&d_ScissorBox[0]);
			glScissor(pRenderEffect->scissor_x,
				pRenderEffect->scissor_y,
				pRenderEffect->scissor_w,
				pRenderEffect->scissor_h);
		}

		std::map<Nuclear::IEffect*, CEGUI::Window*>::iterator it = m_mapUIEffect.begin();
		for (; it != m_mapUIEffect.end(); ++it)
		{
			CEGUI::Window* pWnd = it->second;
			CEGUI::RenderEffect* pCurRE = pWnd->getGeometryBuffer()->getRenderEffect();
			float fCurAlpha = pWnd->getEffectiveAlpha();
			if (pCurRE == (CEGUI::RenderEffect*)pRenderEffect && fCurAlpha >= 0.95f)
			{

				if (gGetNewRoleGuideManager())
				{
					gGetNewRoleGuideManager()->UpdateGuidePaticleEffect(it->first, pWnd);
				}

				UpdateEffectBoundsPos(it->first, pWnd);

				Nuclear::GetEngine()->DrawEffect(it->first);
			}
		}

		if (pRenderEffect->m_bClip)
		{
			if (!scissorEnable) {
				glDisable(GL_SCISSOR_TEST);
				glScissor(d_ScissorBox[0],
					d_ScissorBox[1],
					d_ScissorBox[2],
					d_ScissorBox[3]);
			}
		}

		if (m_pCEGUICocos2DRender)
		{
			m_pCEGUICocos2DRender->beginRendering();

		}
	}
}

UISprite* GameUImanager::AddWindowSprite(CEGUI::Window* pWnd, int modleId, Nuclear::NuclearDirection dir, int x, int y, bool clip)
{
	if (pWnd != NULL)
	{
		RemoveWindowSpriteByWnd(pWnd);
		UISprite* sprite = new UISprite(modleId);

		if (NULL != sprite)
		{
			Nuclear::NuclearLocation spritePos(x, y);
			if (x == 0 && y == 0)
			{
				CEGUI::Size centerPt = pWnd->getPixelSize();
				CEGUI::Point pt(centerPt.d_width / 2, centerPt.d_height / 2);
				spritePos.x = static_cast<int>(pt.d_x);
				spritePos.y = static_cast<int>(pt.d_y);
			}

			m_mapSpritePos[sprite] = spritePos;
			CEGUI::Point wndPos = pWnd->GetScreenPos();
			spritePos.x += wndPos.d_x;
			spritePos.y += wndPos.d_y;
			sprite->SetUILocation(spritePos);
			sprite->SetUIDirection(dir);

			CEGUI::GeometryBuffer* pGB = pWnd->getGeometryBuffer();
			if (pGB)
			{
				XPRenderEffect* pXPRenderEffect = new XPRenderEffect;
				pXPRenderEffect->setType(2);
				if (clip)
				{
					pXPRenderEffect->setClip(true);
				}
				pGB->setRenderEffect(pXPRenderEffect);

				pWnd->subscribeEvent(CEGUI::Window::EventDestructionStarted, CEGUI::Event::Subscriber(&GameUImanager::OnSpriteWndDestroy, this));
				m_mapWindowSprite[sprite] = pWnd;
			}
		}
		return sprite;
	}
	return NULL;
}

Nuclear::IEffect* GameUImanager::AddUIEffect(CEGUI::Window* pWnd, const std::wstring& strEffectName, bool bCycle, int x, int y, bool clip)
{
	if (pWnd != NULL)
	{
		RemoveUIEffect(pWnd);
		Nuclear::IEffect* pEffect = Nuclear::GetEngine()->CreateEffect(strEffectName, true);
		if (NULL != pEffect)
		{
			Nuclear::NuclearLocation effectPos(x, y);
			if (x == 0 && y == 0)
			{
				CEGUI::Size centerPt = pWnd->getPixelSize();
				CEGUI::Point pt = pWnd->GetScreenPosOfCenter();
				effectPos.x = static_cast<int>(pt.d_x);
				effectPos.y = static_cast<int>(pt.d_y);
			}
			else
			{
				CEGUI::Point pt = pWnd->GetScreenPos();
				effectPos.x = static_cast<int>(pt.d_x) + x;
				effectPos.y = static_cast<int>(pt.d_y) + y;
			}
			if (pEffect->m_isSpine) {
				Nuclear::SpineEffect* spineEffect = dynamic_cast<Nuclear::SpineEffect*>(pEffect);
				if (spineEffect)
				{
					spineEffect->SetIsUIEffect(true);
				}
			}

			pEffect->SetLocation(effectPos);


			if (!bCycle)
			{
				UIEffectNotify* pNotify = new UIEffectNotify;
				pEffect->AddNotify(pNotify);
			}

			CEGUI::GeometryBuffer* pGB = pWnd->getGeometryBuffer();
			if (pGB)
			{
				XPRenderEffect* pXPRenderEffect = new XPRenderEffect;
				pXPRenderEffect->setType(1);
				if (clip)
				{
					pXPRenderEffect->setClip(true);
				}
				pGB->setRenderEffect(pXPRenderEffect);
				pWnd->subscribeEvent(CEGUI::Window::EventDestructionStarted, CEGUI::Event::Subscriber(&GameUImanager::OnWndDestroy, this));
				pWnd->subscribeEvent(CEGUI::Window::EventScreenAreaChange, CEGUI::Event::Subscriber(&GameUImanager::OnWndMove, this));
				m_mapUIEffect[pEffect] = pWnd;
				m_mapUIEffectLocation[pEffect] = Nuclear::NuclearLocation(x, y);
			}
		}
		return pEffect;
	}
	return NULL;
}

Nuclear::IEffect* GameUImanager::AddParticalEffect(CEGUI::Window *pWnd, const std::wstring& strEffectName, bool bCycle, int x, int y, bool clip, bool bRoundBounds)
{
	if (!pWnd)
		return NULL;
	RemoveUIEffect(pWnd);

	Nuclear::IEffect* pEffect = gGetGameUIManager()->AddUIEffect(pWnd, strEffectName, bCycle, x, y, clip);//nEffectId 10068
	if (!pEffect)
	{
		return NULL;
	}
	if (bRoundBounds)
	{
		m_mapBoundsEffect[pEffect] = 0.0f;
		UpdateEffectBoundsPos(pEffect, pWnd);
	}

	Nuclear::IParticleEffect* pParticleEffect = pEffect->TryConvertToParticle();
	if (pParticleEffect)
	{
		pParticleEffect->SetCycleMode(Nuclear::XPPCM_ALWAY_EMISSION);
	}

	return pEffect;
}

bool GetTextFromClipBord(CEGUI::String& text)
{
	return false;
}

bool HandleCopyToBord(const CEGUI::String& text)
{
	return false;
}

void HandleAddSelectEffectToItemCell(CEGUI::Window* wnd, bool isSelected)
{
	if (isSelected)
	{
		if (((CEGUI::ItemCell*)wnd)->HasFloodLight())
		{
			((CEGUI::ItemCell*)wnd)->SetFloodLight(false);
		}
		float scale = wnd->getPixelSize().d_width / STANDARD_ITEMCELL_SIZE;
		Nuclear::IEffect *e = gGetGameUIManager()->AddUIEffect(wnd, MHSD_UTILS::GetEffectPath(11065), true, 0, 0, true);
		if (e) e->SetScale(scale);
	}
	else
	{
		gGetGameUIManager()->RemoveUIEffect(wnd);
	}
}

void HandleAnswerQuestionLinkClick(int questionid, CEGUI::String& name, int roleid, int type)
{
	//杨斌---判断点击聊天框打开聊天界面时，是否点击了连�?
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("CChatOutBoxOperatelDlg.SetOpenChatWnd", false);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatOutputDialog.ToHide_");
	//CChatOutputDialog::GetSingleton()->ToHide();
	CEGUI::String strQuestionid = StringCover::intToString(questionid);
	CEGUI::String strRoleid = StringCover::intToString(roleid);
	CEGUI::String strType = StringCover::intToString(type);
	int status = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("HuoDongManager.HandleAnswerQuestionLinkClick",
		strQuestionid.c_str(), name.c_str(), strRoleid.c_str(), strType.c_str());

}

void HandleJoinTeamLinkClick(int64_t leaderid)
{
	cocos2d::CCScriptEngineProtocol *engine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	engine->pushInt64ToLuaStack(leaderid);
	engine->executeGlobalFunction("TeamManager.HandleJoinTeamLinkClick", 1);
}

void HandleOpenDialog(int64_t dialogID)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("Openui.OpenUI", dialogID);
}

void HandleRequestAnotherQuest(int64_t questID, const CEGUI::String &title)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushInt64ToLuaStack(questID);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(title.c_str());
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("JinglingDlg.globalRequstQuest", 2);
}

void HandleRequestTeamLinkClick(int64_t teamid)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(intToString(teamid).c_str());
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("FireProtocol_COneKeyApplyTeamInfo", 1);
}

void HandleTipsLinkClick(const CEGUI::String& name, int64_t roleID, int type, const CEGUI::String& key, int baseid, int64_t shopID, int counterID, const CEGUI::colour& nameColor, int bind, int64_t loseeffecttime)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(name.c_str());
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushInt64ToLuaStack(roleID);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(type);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(key.c_str());
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(baseid);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushInt64ToLuaStack(shopID);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(counterID);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(nameColor.getARGB());
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(bind);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushInt64ToLuaStack(loseeffecttime);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatManager.HandleTipsLinkClick_", 10);
}

void HandleCommonLinkClick(const CEGUI::String &arg)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("HandleRichEditboxCommonLinkClicked", arg.c_str());
}

bool OnEmotionFrameChange(const CEGUI::EventArgs& e)
{
	const CEGUI::RichEditBoxCoponentEventArgs& Args = static_cast<const CEGUI::RichEditBoxCoponentEventArgs&>(e);
	CEGUI::RichEditboxEmotionComponent* pEmtionCpn = static_cast<CEGUI::RichEditboxEmotionComponent*>(Args.d_Component);

	if (pEmtionCpn != NULL)
	{
		int aniID = pEmtionCpn->GetAniID();
		float fElapse = pEmtionCpn->GetElapseTime();
		int frame = gGetEmotionManager()->GetFrameByElapseTime(aniID, fElapse);
		const CEGUI::Image* pImage = gGetIconManager()->GetEmotionImageByFrame(aniID, frame);
		if (pImage != NULL)
		{
			pEmtionCpn->setImage(pImage);
		}
	}
	return true;
}

void GameUImanager::CloseDialogBeforeMapChange()
{
	std::vector<CEGUI::Window*> tempCloseWindow;

	std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.begin();
	for (; iter != m_UIDialog.end(); iter++)
	{
		Dialog* dialog = (*iter).second;
		if (dialog->m_eDialogType & eDialogType_MapChangeClose)
		{
			tempCloseWindow.push_back((*iter).first);
		}
	}

	for (std::vector<CEGUI::Window*>::iterator it = tempCloseWindow.begin(); it != tempCloseWindow.end(); it++)
	{
		std::map<CEGUI::Window*, Dialog*>::iterator iterDilaog = m_UIDialog.find((*it));
		if (iterDilaog != m_UIDialog.end())
		{
			Dialog* dialog = (*iterDilaog).second;
			dialog->OnClose();
		}
	}
}

void GameUImanager::CloseDialogBeforeBattle()
{
	std::vector<CEGUI::Window*> tempCloseWindow;

	std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.begin();
	for (; iter != m_UIDialog.end(); iter++)
	{
		Dialog* dialog = (*iter).second;
		if (dialog->m_eDialogType & eDialogType_BattleClose)
		{
			tempCloseWindow.push_back((*iter).first);
		}
	}

	for (std::vector<CEGUI::Window*>::iterator it = tempCloseWindow.begin(); it != tempCloseWindow.end(); it++)
	{
		std::map<CEGUI::Window*, Dialog*>::iterator iterDilaog = m_UIDialog.find((*it));
		if (iterDilaog != m_UIDialog.end())
		{
			Dialog* dialog = (*iterDilaog).second;
			dialog->OnClose();
		}
	}
}

void GameUImanager::HandleEsc()
{

	CEGUI::Window* escWindow = NULL;

	escWindow = CEGUI::System::getSingleton().getModalTarget();
	if (escWindow == NULL)
	{
		escWindow = CEGUI::System::getSingleton().getGUISheet()->getEscCloseWindow();
	}
	else
	{
		return;
	}
}

CEGUI::LuaScriptModule* GameUImanager::GetLuaScriptModule()
{
	return m_pLuaScriptModule;
}

bool GameUImanager::IsBagMirrorDlgOpen()
{
	return false;
}

bool GameUImanager::OnUIDialogDestructionStarted(const CEGUI::EventArgs &args)
{
	CEGUI::Window *window = static_cast<const CEGUI::WindowEventArgs&>(args).window;
	std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.find(window);
	if (iter != m_UIDialog.end())
	{
		//It won't make any problem, just give a tip, this dialog didn't call OnClose. by lg
		//SDLOG_WARN(L"[UIDialog Destruction] %s\n", window->getName().c_str());
		m_UIDialog.erase(iter);
	}
	return true;
}

void GameUImanager::RemoveUIDialog(CEGUI::Window *window)
{
	std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.find(window);
	if (iter != m_UIDialog.end())
	{
		m_UIDialog.erase(iter);
	}
}

void GameUImanager::AddUIDialog(CEGUI::Window *window, Dialog *dialog)
{
	std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.find(window);
	if (iter == m_UIDialog.end())
	{
		m_UIDialog[window] = dialog;
		window->subscribeEvent(CEGUI::Window::EventDestructionStarted, CEGUI::Event::Subscriber(&GameUImanager::OnUIDialogDestructionStarted, this));
	}
}

void HandleShowRewardItemTips(const int& itemid, float xPos, float yPos)
{
}

void GameUImanager::OnExitGameApp()
{
	EnableDlgSound(false);
	if (SceneMovieManager::GetInstance())
		SceneMovieManager::GetInstance()->ExitMovieScene();

	for (std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.begin(); iter != m_UIDialog.end(); iter++)
	{
		Dialog* dialog = (*iter).second;
		dialog->RemoveWindowEvent();
	}

	for (std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.begin(); iter != m_UIDialog.end(); iter++)
	{
		Dialog* dialog = (*iter).second;
		dialog->OnClose2(false, false);
	}
	m_UIDialog.clear();


	RemoveAllUIEffect();

	CheckDisTimer::getInstance().CancelAll();

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LuaUIManager.Exit");

	CEGUI::System::getSingleton().getGUISheet()->cleanupChildren();
	if (m_pFPSLabel != NULL)
	{
		CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		winMgr.destroyWindow(m_pFPSLabel);
	}
	m_pFPSLabel = NULL;

	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
	CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
#if (defined WIN32) && (defined XP_PERFORMANCE)
	for (int i = 0; i < 20; ++i)
	{
		if (ma_pCEGUILabel[i])
		{
			winMgr.destroyWindow(ma_pCEGUILabel[i]);
			ma_pCEGUILabel[i] = NULL;
		}
	}
#endif
	std::map<int, stScreenEffect>::iterator locItor = m_mapScreenEffect.begin();
	while (locItor != m_mapScreenEffect.end())
	{
		stScreenEffect eInfo = static_cast<stScreenEffect>(locItor->second);

		eInfo.pEffect->RemoveNotify(eInfo.pNotify);
		delete eInfo.pNotify;
		eInfo.pNotify = NULL;

		Nuclear::GetEngine()->ReleaseEffect(eInfo.pEffect);
		eInfo.pEffect = NULL;
		m_mapScreenEffect.erase(locItor);
		locItor = m_mapScreenEffect.begin();
	}
	m_mapScreenEffect.clear();

	EmotionManager::RemoveInstance();
	IconManager::RemoveInstance();

	if (m_pUIIMEDelegate)
	{
		delete m_pUIIMEDelegate;	// yeqing 2015-10-20
		m_pUIIMEDelegate = NULL;
	}
	cocos2d::extension::CCHttpClient::destroyInstance();
}

void GameUImanager::OnExitGameToSelectRole()
{
	EnableDlgSound(false);

	SceneMovieManager::GetInstance()->ExitMovieScene();

	std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.begin();
	for (; iter != m_UIDialog.end(); iter++)
	{
		Dialog* dialog = iter->second;
		dialog->RemoveWindowEvent();
	}

	iter = m_UIDialog.begin();
	while (iter != m_UIDialog.end())
	{
		Dialog* dialog = iter->second;
		if (dialog->m_eDialogType & eDialogType_ImmeDel)
		{
			dialog->DestroyDialog();
			iter = m_UIDialog.begin();
		}
		else
		{
			m_UIDialog.erase(iter);
			iter = m_UIDialog.begin();
			if (!dialog->m_bRelease) {	// yeqing 2015-11-18 : only destroy dialog which has not been released
				dialog->DestroyDialog();
			}
		}
	}
	m_UIDialog.clear();

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("RemindUseItemDlg.ClearAllDialog");

	RemoveAllUIEffect();
	CEGUI::System::getSingleton().getGUISheet()->cleanupChildren();
}

void GameUImanager::OnExitGameToLogin(int relogin)
{
	EnableDlgSound(false);
	if (SceneMovieManager::GetInstance())
		SceneMovieManager::GetInstance()->ExitMovieScene();

	for (std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.begin(); iter != m_UIDialog.end(); iter++)
	{
		Dialog* dialog = (*iter).second;
		dialog->RemoveWindowEvent();
	}

	for (std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.begin(); iter != m_UIDialog.end(); iter++)
	{
		Dialog* dialog = (*iter).second;
		dialog->OnClose2(false, false);
	}
	m_UIDialog.clear();

	RemoveAllUIEffect();

	CheckDisTimer::getInstance().CancelAll();

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LuaUIManager.Exit");

	CEGUI::System::getSingleton().getGUISheet()->cleanupChildren();

	if (m_pFPSLabel != NULL)
	{
		CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		winMgr.destroyWindow(m_pFPSLabel);
	}
	m_pFPSLabel = NULL;

	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
	CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();

#if (defined WIN32) && (defined XP_PERFORMANCE)
	for (int i = 0; i < 20; ++i)
	{
		if (ma_pCEGUILabel[i])
		{
			winMgr.destroyWindow(ma_pCEGUILabel[i]);
		}
		ma_pCEGUILabel[i] = NULL;
	}
#endif
}

bool GameUImanager::IsGameUIVisible()
{
	return m_bGameUIVisible;
}

void GameUImanager::SetGameUIVisible(bool b)
{
	if (m_bGameUIVisible == b)
		return;

	m_bGameUIVisible = b;
	if (m_pRootWindow)
	{
		m_pRootWindow->setVisible(b);
	}
}

bool GameUImanager::ChnInjectChar(CEGUI::utf32 code_point)
{
	return CEGUI::System::getSingleton().injectChar(code_point);
}

void GameUImanager::initialiseDefaultResourceGroups()
{
	// set the default resource groups to be used
	CEGUI::Imageset::setDefaultResourceGroup("imagesets");
	CEGUI::Font::setDefaultResourceGroup("fonts");
	CEGUI::Scheme::setDefaultResourceGroup("schemes");
	CEGUI::WidgetLookManager::setDefaultResourceGroup("looknfeel");
	CEGUI::WindowManager::setDefaultResourceGroup("layouts");
	CEGUI::ScriptModule::setDefaultResourceGroup("lua_scripts");
	CEGUI::AnimationManager::setDefaultResourceGroup("animations");

#ifdef PUBLISHED_VERSION
	CEGUI::XMLParser* xmlparser = CEGUI::System::getSingleton().getXMLParser();
	if (xmlparser && xmlparser->isPropertyPresent("SchemaDefaultResourceGroup")) {
		xmlparser->setProperty("SchemaDefaultResourceGroup", "schemes");
	}
#endif
}

void GameUImanager::initialiseResourceGroupDirectories()
{
#ifdef PUBLISHED_VERSION
	CEGUI::PFSResourceProvider* rp = static_cast<CEGUI::PFSResourceProvider*>(CEGUI::System::getSingleton().getResourceProvider());

	rp->setResourceGroupDirectory("schemes", "/ui/schemes/");
	rp->setResourceGroupDirectory("imagesets", "/ui/imagesets/");
	rp->setResourceGroupDirectory("fonts", "/ui/fonts/");
	rp->setResourceGroupDirectory("layouts", "/ui/layouts/");
	rp->setResourceGroupDirectory("looknfeel", "/ui/looknfeel/");
	rp->setResourceGroupDirectory("lua_scripts", "/lua_scripts/");
	rp->setResourceGroupDirectory("animations", "/ui/animations/");
#else
	CEGUI::DefaultResourceProvider* rp = static_cast<CEGUI::DefaultResourceProvider*>(CEGUI::System::getSingleton().getResourceProvider());
#if defined WIN7_32 && defined NoPack
	std::string pathPrefix = (CFileUtil::GetRootDir() + "../../res1/").c_str();
#else
	std::string pathPrefix = (CFileUtil::GetRootDir() + "../../res/").c_str();
#endif

	rp->setResourceGroupDirectory("schemes", (pathPrefix + "ui/schemes/").c_str());
	rp->setResourceGroupDirectory("imagesets", (pathPrefix + "ui/imagesets/").c_str());
	rp->setResourceGroupDirectory("fonts", (pathPrefix + "ui/fonts/").c_str());
	rp->setResourceGroupDirectory("layouts", (pathPrefix + "ui/layouts/").c_str());
	rp->setResourceGroupDirectory("looknfeel", (pathPrefix + "ui/looknfeel/").c_str());
	rp->setResourceGroupDirectory("lua_scripts", (pathPrefix + "scripts/").c_str());
	rp->setResourceGroupDirectory("animations", (pathPrefix + "animations/").c_str());
#endif
}

void GameUImanager::UnInitGameUI()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatCellManager.Release_");
	//ChatCellManager::GetSingleton().Release();

	if (CEGUI::System::getSingletonPtr() != NULL)
	{
		CEGUI::System& guiSystem = CEGUI::System::getSingleton();
		guiSystem.destroy();
	}

	if (NULL != m_pResourceProvider)
	{
		delete m_pResourceProvider;
		m_pResourceProvider = NULL;
	}

	if (NULL != m_pCEGUICocos2DRender)
	{
		CEGUI::Cocos2DRenderer::destroy(*m_pCEGUICocos2DRender);
		m_pCEGUICocos2DRender = NULL;
	}

	if (NULL != m_pLuaScriptModule)
	{
		CEGUI::LuaScriptModule::destroy(*m_pLuaScriptModule);
		m_pLuaScriptModule = NULL;
	}

	m_bUIInited = false;
}

bool GameUImanager::moveFloatItem(float x, float y)
{
	return false;
}

CEGUI::Window* GameUImanager::GetMainRootWnd()
{
	return m_pRootWindow;
}

bool GameUImanager::FloatItemOnMouseButtonDown()
{
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	CEGUI::Window* pwindow = guiSystem.getTargetWindow(CEGUI::MouseCursor::getSingleton().getPosition(), false);
	if (pwindow && pwindow->getType() == "TaharezLook/ItemCellGeneral")
	{
		if (guiSystem.injectMouseButtonDown(CEGUI::LeftButton))
		{
			return true;
		}
		else
			return false;
	}
	if (pwindow && pwindow->getType() == "TaharezLook/ItemCell")
	{
		if (guiSystem.injectMouseButtonDown(CEGUI::LeftButton))
		{
			return true;
		}
		else
			return false;
	}
	else if (pwindow && pwindow->getType() == "TaharezLook/TabButton")
	{
		return true;
	}

	return false;
}

bool GameUImanager::FloatSkillOnMouseButtonDown()
{
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	CEGUI::Window* pwindow = guiSystem.getTargetWindow(CEGUI::MouseCursor::getSingleton().getPosition(), false);
	if (pwindow && pwindow->getType() == "TaharezLook/SkillBox")
	{
		if (guiSystem.injectMouseButtonDown(CEGUI::LeftButton))
		{
			return true;
		}
		else
			return false;
	}
	else if (pwindow && pwindow->getType() == "TaharezLook/ItemCellGeneral")
	{
		if (guiSystem.injectMouseButtonDown(CEGUI::LeftButton))
		{
			return true;
		}
		else
			return false;
	}
	else
	{
		return false;
	}
}

void GameUImanager::RemoveScreenEffect(Nuclear::IEffect* pEffect)
{
	if (pEffect == NULL)	return;

	std::map<int, stScreenEffect>::iterator iter = m_mapScreenEffect.begin();
	for (; iter != m_mapScreenEffect.end(); iter++)
	{
		stScreenEffect eInfo = iter->second;
		if (eInfo.pEffect == pEffect)
		{
			Nuclear::GetEngine()->ReleaseEffect(eInfo.pEffect);
			m_mapScreenEffect.erase(iter);
			break;
		}
	}
}

void GameUImanager::drawScreenEffect()
{
	if (GetBattleManager() && GetBattleManager()->IsInBattle())
		return;
	std::map<int, stScreenEffect>::iterator iter = m_mapScreenEffect.begin();
	for (; iter != m_mapScreenEffect.end(); iter++)
	{
		stScreenEffect eInfo = iter->second;
		if (eInfo.pEffect != NULL)
		{
			Nuclear::GetEngine()->DrawEffect(eInfo.pEffect);
		}
	}
}

void GameUImanager::DrawSysMsgEffect()
{
	if (gGetScene() && gGetSceneMovieManager())
	{
		CEGUI::System::getSingleton().getRenderer()->endRendering();
		if (m_pSysMesHoldEffect && gGetSceneMovieManager()->isOnSceneMovie() == false)
		{
			Nuclear::GetEngine()->DrawEffect(m_pSysMesHoldEffect);
		}
		else if (m_pSysMesEndEffect&& gGetSceneMovieManager()->isOnSceneMovie() == false)
		{
			Nuclear::GetEngine()->DrawEffect(m_pSysMesEndEffect);
		}

		CEGUI::System::getSingleton().getRenderer()->beginRendering();
	}
}

void GameUImanager::DrawFPS()
{
	if (m_pFPSLabel == NULL) return;
	if (!gGetGameApplication()->IsShowFPS())
	{
		m_pFPSLabel->setVisible(false);
		return;
	}
	else
	{
		if (m_pFPSLabel->isVisible(false))
		{
			m_pFPSLabel->setVisible(true);
		}
	}

	cocos2d::CCDirector* pDirector = cocos2d::CCDirector::sharedDirector();
	if (pDirector->isDisplayStats())
	{
		m_pFPSLabel->setVisible(true);

		float curFps = Nuclear::GetEngine()->GetFPS();
		char buffer[128];
		float workSize = Nuclear::GetEngine()->GetCurMemSize();
		Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());
		unsigned int nThreadExcCounter = pEngine->GetThreadExcCounter();
		sprintf(buffer, "FPS:%.1f,Mem:%.2fM,Thread:%d", curFps, workSize, nThreadExcCounter);
		if (nThreadExcCounter > 120)
		{
			SDLOG_INFO(L"[Thread Excute Counter: %d]", nThreadExcCounter);
		}

		m_pFPSLabel->setText(buffer);
		m_pFPSLabel->SetTextColor(0xffffffff);
		m_pFPSLabel->EnableAllowModalState(true);
		m_pFPSLabel->setTopMost(true);
	}
	else
	{
		m_pFPSLabel->setVisible(false);
	}
}

void GameUImanager::Draw()
{
	if (!m_bShowGameUI)
	{
		return;
	}

	if (gGetScene() && gGetScene()->isLoadMaping())
	{
		return;
	}

	Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());

	if (getLoginProgress())
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LoginImageAndBar.draw");
	}

	if (gGetScene() && gGetSceneMovieManager())
	{
		if (m_pSysMesHoldEffect
			&& gGetScene()->isOnDreamScene() == false
			&& gGetSceneMovieManager()->isOnSceneMovie() == false)
		{
			pEngine->DrawEffect(m_pSysMesHoldEffect);
		}
		else if (m_pSysMesEndEffect
			&& gGetScene()->isOnDreamScene() == false
			&& gGetSceneMovieManager()->isOnSceneMovie() == false)
		{
			pEngine->DrawEffect(m_pSysMesEndEffect);
		}
	}

	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	//guiSystem.signalRedraw();
	guiSystem.renderGUI();

	drawScreenEffect();

	if (TaskOnOffEffectManager::GetInstance())
	{
		TaskOnOffEffectManager::GetInstance()->DrawEffect();
	}

	DrawFPS();
#ifdef XP_PERFORMANCE
#ifdef WIN32
	DrawUIState();
#endif

	DrawPerformance();
#endif
}

std::wstring GameUImanager::GetFullPathFileName(std::wstring PathFileName)
{
	std::wstring wsPFN;
	std::wstring wsFPFN;
	wsPFN = PathFileName;
	wsFPFN = PathFileName;
	std::wstring wsTmp;
	wsTmp = LJFM::LJFMF::GetFullPathFileName(wsPFN);
	if (wsTmp != L"")
	{
		wsFPFN = wsTmp;
	}
	return wsFPFN;
}

int GameUImanager::QuickCommand(std::wstring Cmd, std::wstring& Param0, std::wstring& Param1, std::wstring& Param2, std::wstring& Param3)//LUA??C++?????????????y??
{
	bool bUseQuickCommand = false;
#ifdef WIN7_32
	bUseQuickCommand = true;
#else
	#if defined DEBUG || defined _DEBUG
		bUseQuickCommand = true;
	#endif
#endif
	if (bUseQuickCommand == true)
	{
		if (Cmd.compare(L"ShowWindow") == 0)
		{
			if (Param0.compare(L"CCharacterLabelDlg") == 0)
			{

			}
		}
		else if (Cmd.compare(L"DeltaRate") == 0)
		{
			float fDeltaRate = StringCover::StrToNum<float>(StringCover::to_string(Param0));
			Nuclear::GetEngine()->SetGameTimeSpeedScale(fDeltaRate);
		}
		else if (Cmd.compare(L"SmoothDeltaLimit") == 0)
		{
			unsigned int uiSmoothDeltaLimit = StringCover::StrToNum<unsigned int>(StringCover::to_string(Param0));
			Nuclear::GetEngine()->SetSmoothDeltaLimit(uiSmoothDeltaLimit);
		}
		else if (Cmd.compare(L"FlyScaleTgt") == 0 || Cmd.compare(L"FST") == 0)
		{
			int iFlyScaleTgt = StringCover::StrToNum<int>(StringCover::to_string(Param0));
			GetMainCharacter()->GetSprite()->SetFlyScaleTgt(iFlyScaleTgt);
		}
		else if (Cmd.compare(L"FlyScaleStep") == 0 || Cmd.compare(L"FSS") == 0)
		{
			int iFlyScaleStep = StringCover::StrToNum<int>(StringCover::to_string(Param0));
			GetMainCharacter()->GetSprite()->SetFlyScaleStep(iFlyScaleStep);
		}
		else if (Cmd.compare(L"FlyOffsetYTgt") == 0 || Cmd.compare(L"FOYT") == 0)
		{
			int iFlyOffsetYTgt = StringCover::StrToNum<int>(StringCover::to_string(Param0));
			GetMainCharacter()->GetSprite()->SetFlyOffsetYTgt(iFlyOffsetYTgt);
		}
		else if (Cmd.compare(L"FlyOffsetYStep") == 0 || Cmd.compare(L"FOYS") == 0)
		{
			int iFlyOffsetYStep = StringCover::StrToNum<int>(StringCover::to_string(Param0));
			GetMainCharacter()->GetSprite()->SetFlyOffsetYStep(iFlyOffsetYStep);
		}
		else if (Cmd.compare(L"YCS") == 0)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("YinCang.CShowAll");
		}
		else if (Cmd.compare(L"YCH") == 0)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("YinCang.CHideAll");
		}
		else if (Cmd.compare(L"WS") == 0)
		{
			float fScale = StringCover::StrToNum<float>(StringCover::to_string(Param0));
			Nuclear::GetEngine()->GetWorld()->SetScale(fScale);
		}
		else if (Cmd.compare(L"WST") == 0)
		{
			int iFlyOffsetYTgt = StringCover::StrToNum<int>(StringCover::to_string(Param0));
			Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYTgt(iFlyOffsetYTgt);
		}
		else if (Cmd.compare(L"WSS") == 0)
		{
			int iFlyOffsetYStep = StringCover::StrToNum<int>(StringCover::to_string(Param0));
			Nuclear::GetEngine()->GetWorld()->SetFlyOffsetYStep(iFlyOffsetYStep);
		}
		else if (Cmd.compare(L"UGZ") == 0)
		{
			GetBattleReplayManager()->PlayAndUnGZipReplay(Param0);
		}
		else if (Cmd.compare(L"UJ") == 0)
		{
			//UpdateJson::NewInstance()->RequestUpdateJson(L"http://192.168.32.2:8080/6688/", L"0.0.1", L"100");
		}
		else if (Cmd.compare(L"FLY") == 0)
		{
			int iFlyID = StringCover::StrToNum<int>(StringCover::to_string(Param0));
			GetMainCharacter()->StartAutoMove(iFlyID);
		}
	}
	if (Cmd.compare(L"RLOG") == 0 || Cmd.compare(L"rlog") == 0 || Cmd.compare(L"Rlog") == 0)
	{
		std::string sCustomZipFN = StringCover::to_string(Param0);
		if (sCustomZipFN != "")
		{
			if (sCustomZipFN.length() > 200)
			{
				sCustomZipFN.resize(200);
			}
			sCustomZipFN = StringCover::to_string(gGetLoginManager()->GetDeviceid()) + "_" + sCustomZipFN + ".zip";
		}
		gGetReportManager()->ReportLogByFTP(sCustomZipFN);
	}
	return 0;
}

int GameUImanager::QuickCommandToLua(std::wstring Cmd, std::wstring Param0, std::wstring Param1, std::wstring Param2, std::wstring Param3)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData(
		ws2s(Cmd).c_str(), ws2s(Param0).c_str(), ws2s(Param1).c_str(), ws2s(Param2).c_str(), ws2s(Param3).c_str());
	return 0;
}

void GameUImanager::OnSysMesEnd()
{
	if (m_pSysMesEndEffect)
	{
		Nuclear::GetEngine()->ReleaseEffect(m_pSysMesEndEffect);
		m_pSysMesEndEffect = NULL;
	}

	if (m_pSysMesHoldEffect)
	{
		Nuclear::GetEngine()->ReleaseEffect(m_pSysMesHoldEffect);
		m_pSysMesHoldEffect = NULL;
	}
}

void GameUImanager::OnSysMesFade()
{
	if (m_pSysMesHoldEffect)
	{
		Nuclear::GetEngine()->ReleaseEffect(m_pSysMesHoldEffect);
		m_pSysMesHoldEffect = NULL;

		if (m_pSysMesEndEffect)
		{
			m_pSysMesEndEffect->Play();
		}
		else
		{
			m_pSysMesEndEffect = Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(10174));
			if (NULL != m_pSysMesEndEffect)
			{
				Nuclear::NuclearDisplayMode mode = Nuclear::GetEngine()->GetRenderer()->GetDisplayMode();
				m_pSysMesEndEffect->SetLocation(Nuclear::NuclearLocation(mode.width / 2, mode.height / 6));

				SysMesFadeEffectNotify* pNotify = new SysMesFadeEffectNotify;
				m_pSysMesEndEffect->AddNotify(pNotify);
			}
		}
	}
}

void GameUImanager::OnSysMesBegin()
{
	m_pSysMesHoldEffect = Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(10172));
	if (NULL != m_pSysMesHoldEffect)
	{
		Nuclear::NuclearDisplayMode mode = Nuclear::GetEngine()->GetRenderer()->GetDisplayMode();
		m_pSysMesHoldEffect->SetLocation(Nuclear::NuclearLocation(mode.width / 2, mode.height / 6));
	}

	if (m_pSysMesEndEffect)
	{
		m_pSysMesEndEffect->Stop();
	}
}

bool GameUImanager::HandleSysMesFade(const CEGUI::EventArgs &e)
{
	if (m_pSysBoardMes)
	{
		OnSysMesFade();
	}
	return true;
}

bool GameUImanager::HandleMessageTipOut(const CEGUI::EventArgs &e)
{
	const CEGUI::WindowEventArgs& windowArgs = static_cast<const CEGUI::WindowEventArgs&>(e);
	CEGUI::MessageTip* tip = static_cast<CEGUI::MessageTip*>(windowArgs.window);
	if (tip == NULL)
		return true;

	if (tip->GetTipType() == CEGUI::eMsgTip)
	{
		std::vector<CEGUI::String>::iterator it = std::find(m_vecMessageTips.begin(), m_vecMessageTips.end(), tip->getName());
		if (it != m_vecMessageTips.end())
		{
			m_vecMessageTips.erase(it);
		}
	}

	else if (tip->GetTipType() == CEGUI::eSystemTip)
	{
		OnSysMesEnd();
		m_pSysBoardMes = NULL;
	}

	return true;
}

void GameUImanager::AddMessageTipById(int id)
{
	//AddMessageTip(MHSD_UTILS::GetTipsMsg(id));
}

void GameUImanager::AddMessageTip(const std::wstring& messageTip, bool bAddToChat, bool bEnalbe, bool bCheckSame)
{
	//if (messageTip.empty())
	//	return;

	//if (!m_vecMessageTips.empty() && (bCheckSame && IsHaveSameMessageTip(messageTip)))
	//	return;

	//CEGUI::String MsgParseString(messageTip.c_str());

	////if (CChatOutputDialog::GetSingleton() && CChatOutputDialog::GetSingleton()->GetInputBox())
	////{
	////	int pos = -1;

	////	do
	////	{
	////		//yangbin---这行是为了兼容策划填的表，策划所有颜色格式都是c='FFFFFFFF
	////		pos = MsgParseString.find("c='" + CChatOutputDialog::GetSingleton()->GetInputBox()->getProperty("NormalTextColour"));
	////		if (pos == -1)
	////		{
	////			pos = MsgParseString.find("c=\"" + CChatOutputDialog::GetSingleton()->GetInputBox()->getProperty("NormalTextColour"));
	////		}

	////		if (pos != String::npos)
	////		{
	////			MsgParseString.replace(pos + 3, 8, "FFFFF2DF");
	////		}

	////	} while (pos >= 0);
	////}

	//int pos = -1;

	//do
	//{
	//	//yangbin---这行是为了兼容策划填的表，策划所有颜色格式都是c='FFFFFFFF
	//	pos = MsgParseString.find("c='" + CEGUI::String("FF693F00"));
	//	if (pos == -1)
	//	{
	//		pos = MsgParseString.find("c=\"" + CEGUI::String("FF693F00"));
	//	}

	//	if (pos != CEGUI::String::npos)
	//	{
	//		MsgParseString.replace(pos + 3, 8, "FFFFF2DF");
	//	}

	//} while (pos >= 0);

	//std::wstring tmp = StringCover::to_wstring(MsgParseString.c_str());
	//makeMessageTipWnd(tmp, bAddToChat, bEnalbe);
}

bool GameUImanager::IsHaveSameMessageTip(const std::wstring& messageTip)
{
	//std::vector<CEGUI::String>::iterator iter = m_vecMessageTips.begin();
	//for (iter; iter != m_vecMessageTips.end(); iter++) {
	//	if (CEGUI::WindowManager::getSingleton().isWindowPresent(*iter)){
	//		CEGUI::MessageTip* messagetip = static_cast<CEGUI::MessageTip*>(CEGUI::WindowManager::getSingleton().getWindow(*iter));
	//		CEGUI::String strEditbox = messagetip->getRichEditbox()->GetPureText();
	//		if (strcmp(strEditbox.c_str(), ws2s(messageTip).c_str()) == 0) {
	//			return true;
	//		}
	//	}

	//}
	return false;
}

#if defined DEBUG || defined _DEBUG 
bool GameUImanager::HandleShowTextureBtnClicked(const CEGUI::EventArgs& e)
{
	static GLuint lastTex = -1;
	Nuclear::EngineLayer* layer = (Nuclear::EngineLayer*)Nuclear::GetEngine()->GetEngineLayer();
	CEGUI::String text = m_pTextureNameEdit->getText();
	bool close = false;
	if (!text.empty()) {
		GLuint texName = CEGUI::PropertyHelper::stringToInt(text);

		layer->setShowTextureName(texName);
		close = lastTex == texName;
		lastTex = texName;
	}

	layer->showTexture(!close);

	return true;
}
#endif

bool GameUImanager::InitGameUIPostInit()
{
	if (m_bUIPostInited)
		return true;

	m_bUIPostInited = true;
	CEGUI::SchemeManager::getSingleton().append("taharezlook2.scheme");

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatCellManager.Initialize_");
	return true;
}

#if defined DEBUG || defined _DEBUG 
void GameUImanager::ShowShowTexturePanel()
{
	if (m_pTextureNameEdit == NULL)
	{
		m_pTextureNameEdit = (CEGUI::Editbox*)CEGUI::WindowManager::getSingleton().createWindow("TaharezLook/Editbox");
		GetMainRootWnd()->addChildWindow(m_pTextureNameEdit);
		m_pTextureNameEdit->setSize(CEGUI::UVector2(CEGUI::UDim(0, 200), CEGUI::UDim(0, 50)));
		m_pTextureNameEdit->setPosition(CEGUI::UVector2(CEGUI::UDim(0.5, -100), CEGUI::UDim(0.5, -25)));
		m_pTextureNameEdit->setReadOnly(false);
		CEGUI::PushButton* pShowTextureBtn = m_pShowTextureBtn = (CEGUI::PushButton*)CEGUI::WindowManager::getSingleton().createWindow("TaharezLook/Button");
		GetMainRootWnd()->addChildWindow(pShowTextureBtn);
		pShowTextureBtn->setSize(CEGUI::UVector2(CEGUI::UDim(0, 200), CEGUI::UDim(0, 50)));
		pShowTextureBtn->setPosition(CEGUI::UVector2(CEGUI::UDim(0.5, -100), CEGUI::UDim(0.5, 35)));
		pShowTextureBtn->setMousePassThroughEnabled(false);
		pShowTextureBtn->subscribeEvent(CEGUI::PushButton::EventClicked, CEGUI::Event::Subscriber(&GameUImanager::HandleShowTextureBtnClicked, this));
		pShowTextureBtn->setText("???");
	}
	else
	{
		m_pTextureNameEdit->setVisible(!m_pTextureNameEdit->isVisible());
		m_pShowTextureBtn->setVisible(!m_pTextureNameEdit->isVisible());
	}
}
#endif

LuaXPRenderEffect* GameUImanager::createXPRenderEffect(int userid, int handler)
{
	return new LuaXPRenderEffect(userid, handler);
}

Nuclear::IEffectNotify* GameUImanager::createNotify(int handler)
{
	return new LuaEffectNotify(handler);
}

bool GameUImanager::checkClickWhenGuide(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
#ifndef WIN32
	if (msg != Nuclear::WM_LBUTTONUP && gGetNewRoleGuideManager() && gGetNewRoleGuideManager()->isClickInvalidWhenLockScreen((float)wParam, (float)lParam))
#else
	if (msg != WM_LBUTTONUP && gGetNewRoleGuideManager() && gGetNewRoleGuideManager()->isClickInvalidWhenLockScreen((float)wParam, (float)lParam))
#endif
	{
#ifndef WIN32
		if (Nuclear::WM_LBUTTONDOWN == msg)
#else
		if (WM_LBUTTONDOWN == msg)
#endif
		{
			Nuclear::NuclearFRectt rect = gGetNewRoleGuideManager()->GetClickRect(gGetNewRoleGuideManager()->getCurGuideId());
			//gGetGameUIManager()->AddMessageTip(GameTable::message::GetCMessageTipTableInstance().getRecorder(160208).msg);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(GameTable::message::GetCMessageTipTableInstance().getRecorder(160208).msg).c_str());
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushBooleanToLuaStack(false);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushBooleanToLuaStack(false);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushBooleanToLuaStack(true);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.AddMessageTip_", 4);

			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushFloatToLuaStack((float)wParam);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushFloatToLuaStack((float)lParam);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("RoleSkillManager_AddNewGuideEffect", 2);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("newguidebg.getInstanceAndShow");
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("newguidebg.init", rect.left, rect.right, rect.top, rect.bottom);
		}
		return true;
	}

	if (gGetNewRoleGuideManager() && gGetNewRoleGuideManager()->isPlayingAni())
	{
#ifndef WIN32
		if (Nuclear::WM_LBUTTONDOWN == msg)
#else
		if (WM_LBUTTONDOWN == msg)
#endif
		{
			//gGetGameUIManager()->AddMessageTip(GameTable::message::GetCMessageTipTableInstance().getRecorder(160208).msg);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(GameTable::message::GetCMessageTipTableInstance().getRecorder(160208).msg).c_str());
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushBooleanToLuaStack(false);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushBooleanToLuaStack(false);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushBooleanToLuaStack(true);
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.AddMessageTip_", 4);
		}
		return true;
	}

	return false;
}

bool GameUImanager::InitGameUI()
{
	Nuclear::EngineLayer* pEngineLayer = Nuclear::EngineLayer::GetEngineLayer();
	if (pEngineLayer == NULL)
	{
		core::Logger::flurryEvent(L"GameUIManager_InitGameUI_EngineLayer_null_fail");
		return false;
	}

	m_pCEGUICocos2DRender = &CEGUI::Cocos2DRenderer::bootstrapSystem(pEngineLayer, NULL);

#ifdef PUBLISHED_VERSION
	m_pResourceProvider = new CEGUI::PFSResourceProvider;
#else
	m_pResourceProvider = new CEGUI::DefaultResourceProvider;
#endif 

	{
		USING_NS_CC;
		lua_State* luastate = CCScriptEngineManager::sharedManager()->getScriptEngine()->getLuaState();
		m_pLuaScriptModule = &CEGUI::LuaScriptModule::create(luastate);
	}

	CEGUI::System::create(*m_pCEGUICocos2DRender, m_pResourceProvider, 0, 0, m_pLuaScriptModule, "", CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "CEGUI_ct.log").c_str());

	CEGUI::System::getSingleton().SetAdapter((CEGUI::IAdapter*)&g_adapter);
	m_pCEGUICocos2DRender->setDisplaySize(CEGUI::Size(g_adapter.GetLogicWidth(), g_adapter.GetLogicHeight()));

#if defined(_DEBUG) || defined(DEBUG)
	CEGUI::Logger::getSingleton().setLoggingLevel(CEGUI::Warnings);
#else
	CEGUI::Logger::getSingleton().setLoggingLevel(CEGUI::Warnings);
#endif

	initialiseResourceGroupDirectories();
	initialiseDefaultResourceGroups();

	m_pUIIMEDelegate = new GameIMEDelegate;

	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();

	CEGUI::SchemeManager::getSingleton().create("taharezlook.scheme");

	CEGUI::System::getSingleton().setDefaultMouseCursor("common", "common_biaoshi_cc");
	CEGUI::System::getSingleton().setDefaultTooltip("TaharezLook/Tooltip");
	CEGUI::System::getSingleton().setDefaultCompnenttip("TaharezLook/CompnentTip");
	CEGUI::Image* pUnderLineImage = (CEGUI::Image*)&CEGUI::ImagesetManager::getSingleton().get("common").getImage("common_biaoshi_cc");
	const CEGUI::Image* pTextBrushImage = (const CEGUI::Image*)&CEGUI::ImagesetManager::getSingleton().get("common").getImage("common_biaoshi_cc");
	CEGUI::System::getSingleton().SetTextBrushImage(pTextBrushImage);

	CEGUI::FontManager::getSingleton().SetUnderLineImage(pUnderLineImage);
	CEGUI::System::getSingleton().setDefaultFont("simhei-12");
	CEGUI::System::getSingleton().SetDefaultGoToFunction((CEGUI::GoToFunction*)(&HandleGoToFunction));
	CEGUI::System::getSingleton().SetDefaultShowItemTips((CEGUI::ShowItemTips*)(&HandleShowRewardItemTips));
	CEGUI::System::getSingleton().SetEmotionFrameChangeFunction((CEGUI::EmotionChangeFrameFunction*)(&OnEmotionFrameChange));
	CEGUI::System::getSingleton().SetTipsLinkClickFunc((CEGUI::TipsLinkClick*)(&HandleTipsLinkClick));
	CEGUI::System::getSingleton().SetDefaultJoinTeamLinkFunc((CEGUI::JoinTeamLinkClicked*)(&HandleJoinTeamLinkClick));
	CEGUI::System::getSingleton().SetDefaultRequestTeamLinkFunc((CEGUI::RequestTeamLinkClicked*)(&HandleRequestTeamLinkClick));
	CEGUI::System::getSingleton().SetDefaultAnswerQuestionLinkClicked((CEGUI::AnswerQuestionLinkClicked*)(&HandleAnswerQuestionLinkClick));
	CEGUI::System::getSingleton().SetCopyToClipBordFunc((CEGUI::OnCopyToClipBord*)(&HandleCopyToBord));
	CEGUI::System::getSingleton().SetPasteFromClipBordFunc((CEGUI::OnPasteFromClipBord*)(&GetTextFromClipBord));
	CEGUI::System::getSingleton().SetUIPlaySoundFunc((CEGUI::OnUIPlaySound*)(&HandleUIPlaySound));
	CEGUI::System::getSingleton().setDefaultOpenDialog((CEGUI::OpenDialog*)(&HandleOpenDialog));
	CEGUI::System::getSingleton().setDefaultRequestOtherQuest((CEGUI::RequestOtherQuest*)(&HandleRequestAnotherQuest));

	CEGUI::System::getSingleton().SetCheckShiedFunc((CEGUI::OnCheckShied*)(&HandleCheckShied));
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	CEGUI::System::getSingleton().SetOnClickWp8EditAreaFunc((CEGUI::OnClickWp8EditArea*)(&ClickWp8EditArea));
#else
	CEGUI::System::getSingleton().SetOnClickEditAreaFunc((CEGUI::OnClickEditArea*)(&ClickEditArea));
#endif
	CEGUI::System::getSingleton().SetEmotionParseInfFunc((CEGUI::EmotionParseInfFunc*)(&GetEmotionParseInf));
	CEGUI::System::getSingleton().SetEmotionNum(49);
	CEGUI::System::getSingleton().SetAddSelectEffectToItemCell((CEGUI::AddSelectEffectToItemCell*)&HandleAddSelectEffectToItemCell);
	CEGUI::System::getSingleton().SetDefaultCommonLinkLinkFunc((CEGUI::CommonLinkLinkClicked*)&HandleCommonLinkClick);

	CEGUI::AnimationManager::getSingleton().loadAnimationsFromXML("sample.xml");

	CEGUI::Window* pRootWindow = winMgr.createWindow("DefaultWindow", "root_wnd");

	CEGUI::System::getSingleton().setGUISheet(pRootWindow);
	m_pRootWindow = pRootWindow;
	pRootWindow->setMousePassThroughEnabled(true);
	pRootWindow->setDistributesCapturedInputs(true);


	m_bUIInited = true;

	GameStateManager::NewInstance();
#if (defined WIN32) && (defined XP_PERFORMANCE)
	for (int i = 0; i < 20; ++i)
	{
		ma_pCEGUILabel[i] = NULL;
	}
#endif
	m_pFPSLabel = NULL;

	cocos2d::CCDirector* pDirector = cocos2d::CCDirector::sharedDirector();
	if (pDirector)
	{
		pDirector->setDisplayStats(true);
	}

	return true;
}

void GameUImanager::InitMessageTip(CEGUI::MessageTip* & tip, const std::wstring& message, CEGUI::TipType type, bool bAddToChat, bool bExistSysMes)
{
	//cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(message).c_str());
	//string tmp = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("CChatManager.ReplaceColor", 1);
	//const std::wstring newMessage = s2ws(tmp);

	//tip->SetTipsType(type);

	//if (bExistSysMes == false)
	//{
	//	AddWndToRootWindow(tip);
	//}

	//if (type == CEGUI::eSystemTip)
	//{
	//	if (message.find(L"<T") == std::wstring::npos)
	//	{
	//		std::wstring strFormatMsg;
	//		StringCover::FormatString(strFormatMsg, L"<T t=\"%ls\" c=\"FFFFF2DF\"></T>", message.c_str());
	//		tip->setText(strFormatMsg);

	//		if (bAddToChat)
	//			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("CChatManager.AddMsg_SysChannel_", ws2s(strFormatMsg).c_str());
	//	}
	//	else
	//	{
	//		//添加颜色
	//		tip->SetTextColor(0xFFFFF2DF);
	//		tip->setText(newMessage);

	//		if (bAddToChat)
	//			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("CChatManager.AddMsg_SysChannel_", ws2s(message).c_str());
	//	}
	//}
	//else if (type == CEGUI::eMsgTip)
	//{
	//	if (message.find(L"<T") == std::wstring::npos)
	//	{
	//		std::wstring strFormatMsg(L"");
	//		StringCover::FormatString(strFormatMsg, L"<T t=\"%ls\" c=\"FFFFF2DF\"></T>", message.c_str());
	//		tip->setText(strFormatMsg);

	//		if (bAddToChat)
	//			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("CChatManager.AddMsg_Message_", ws2s(strFormatMsg).c_str());
	//	}
	//	else
	//	{

	//		tip->setText(newMessage);
	//		//添加颜色
	//		tip->SetTextColor(0xFFFFF2DF);
	//		if (bAddToChat)
	//			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("CChatManager.AddMsg_Message_", ws2s(message).c_str());
	//	}
	//}
	//int nLineCount = tip->getRichEditbox()->GetLineCount();
	//if (nLineCount == 1){
	//	tip->getRichEditbox()->setTextLineVertCenter(true);
	//}
	//else{
	//	tip->getRichEditbox()->setTextLineVertCenter(false);
	//}
	//if (m_iMessageTipNum > 1000)
	//{
	//	m_iMessageTipNum = 0;
	//}
	//tip->setID(m_iMessageTipNum);
}

void GameUImanager::AddSystemBoard(const std::wstring &systemBoard, bool bAddToChat, bool bforceshow)
{
	if (gGetScene() == NULL)
		return;

	if (gGetScene()->isLoadMaping() && !bforceshow)
		return;

	if (m_pSysMesHoldEffect == NULL)
	{
		OnSysMesBegin();
	}

	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();

	if (m_pSysBoardMes != NULL)
	{
		m_pSysBoardMes->InitSysMsgParamter();
		InitMessageTip(m_pSysBoardMes, systemBoard, CEGUI::eSystemTip, bAddToChat, true);
	}
	else
	{
		m_pSysBoardMes = static_cast<CEGUI::MessageTip*>(winMgr.createWindow("TaharezLook/MessageTip"));
		SystemMsgRenderEffect* pRenderEffect = new SystemMsgRenderEffect;
		m_pSysBoardMes->getGeometryBuffer()->setRenderEffect(pRenderEffect);

		InitMessageTip(m_pSysBoardMes, systemBoard, CEGUI::eSystemTip, bAddToChat);
	}

	float ParentHeight = m_pSysBoardMes->getParentPixelHeight();

	float xPos = (m_pSysBoardMes->getParentPixelWidth() - m_pSysBoardMes->getTextSize().d_width) / 2;

	m_pSysBoardMes->setPosition(CEGUI::UVector2(cegui_absdim(xPos), cegui_reldim(0.2f)));
	m_pSysBoardMes->subscribeEvent(CEGUI::MessageTip::EventStartFade, CEGUI::Event::Subscriber(&GameUImanager::HandleSysMesFade, this));
	m_pSysBoardMes->subscribeEvent(CEGUI::Window::EventDestructionStarted, CEGUI::Event::Subscriber(&GameUImanager::HandleMessageTipOut, this));

	float desHeight = m_pSysBoardMes->getHeight().d_offset / ParentHeight;
	if (desHeight < 0.08f)
	{
		m_pSysBoardMes->SetDestYPos(0.15f);
	}
	else
	{
		m_pSysBoardMes->SetDestYPos(0.135f);
	}
}

#ifdef XP_PERFORMANCE
void GameUImanager::DrawPerformance()
{
	if (!gGetGameApplication()->IsShowmPerformance())
		return;

	const int fheight = 20;
	RECT trect;
	trect.left = 0;
	trect.top = 180;
	trect.right = 500;
	trect.bottom = 180 + fheight;

	Nuclear::Renderer *pRenderer = Nuclear::GetEngine()->GetRenderer();
	Nuclear::NuclearFileIOManager* pFileIOMan = Nuclear::GetEngine()->GetFileIOManager();

	Nuclear::RenderPerformance& rp = pRenderer->GetPerformance();
	char buffer[64];
	std::wstring outText;

	sprintf(buffer, "ManagedTextureCount : %d", rp.m_iManagedTextureCount);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffff00);

	sprintf(buffer, "ManagedTextureMemory : %d M", rp.m_iManagedTextureMemory / (1024 * 1024));
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffeebb00);

	float fTmp = rp.m_iTotalPixelRenderedPerFrame / (float)(Nuclear::GetEngine()->GetWidth()*Nuclear::GetEngine()->GetHeight());
	int iTmp1 = (int)fTmp;
	int iTmp2 = (int)(10 * (fTmp - iTmp1));
	sprintf(buffer, "AverageTextureLayer : %d, %d", iTmp1, iTmp2);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffeebb00);

	fTmp = pFileIOMan ? pFileIOMan->GetAverageReadSpeed() : 0.f;
	iTmp1 = (int)fTmp;
	iTmp2 = (int)(10 * (fTmp - iTmp1));
	sprintf(buffer, "AverageReadSpeed  : %d.%d M/s", iTmp1, iTmp2);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffeebb00);

	sprintf(buffer, "DrawPrimitiveUp : %d	/Frame", rp.m_iDrawPrimitiveUPCountPerFrame);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffff00);

	sprintf(buffer, "DrawPrimitive : %d  /Frame", rp.m_iDrawPrimitiveCountPerFrame);
	trect.top += fheight;
	trect.bottom += fheight;
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffff00);

	sprintf(buffer, "DrawPicture : %d  /Frame", rp.m_iDrawPictureCountPerFrame);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffff00);

	sprintf(buffer, "TotalActiveParticles  : %d", pRenderer->GetParticleManager()->GetTotalNumActiveParticles());
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffff00);

	static int sTotalLoadTimePerFram;
	if (rp.m_iTotalLoadTimePerFrame != 0 && rp.m_iTotalLoadTimePerFrame != sTotalLoadTimePerFram)
	{
		sTotalLoadTimePerFram = rp.m_iTotalLoadTimePerFrame;
		rp.m_iTotalLoadTimePerFrame = 0;
	}
	static int sLoadXmlTimePerFrame;
	if (rp.m_iLoadXmlTimePerFrame != 0 && rp.m_iLoadXmlTimePerFrame != sLoadXmlTimePerFrame)
	{
		sLoadXmlTimePerFrame = rp.m_iLoadXmlTimePerFrame;
		rp.m_iLoadXmlTimePerFrame = 0;
	}
	static int sLoadTextureTimePerFrame;
	if (rp.m_iLoadTextureTimePerFrame != 0 && rp.m_iLoadTextureTimePerFrame != sLoadTextureTimePerFrame)
	{
		sLoadTextureTimePerFrame = rp.m_iLoadTextureTimePerFrame;
		rp.m_iLoadTextureTimePerFrame = 0;
	}

	sprintf(buffer, "TotalLoadTime : %d	/Frame", sTotalLoadTimePerFram);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xff0000ff);

	sprintf(buffer, "LoadXmlTime : %d /Frame", sLoadXmlTimePerFrame);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xff00ff00);

	sprintf(buffer, "LoadTextureTime : %d /Frame", sLoadTextureTimePerFrame);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffff0000);

	sprintf(buffer, "GameUpdateTime : %d ms/s", rp.m_iUpdateTimePerSecond);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffffff);

	sprintf(buffer, "GameTicketsTime : %d ms/s", rp.m_iGameTickTimePerSecond);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffffff);

	sprintf(buffer, "LuaTicketsTime : %d ms/s", rp.m_iLuaTickTimePerSecond);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffffff);

	sprintf(buffer, "GameRenderTime : %d ms/s", rp.m_iRenderTimePerSecond);
	trect.top += fheight;
	trect.bottom += fheight;
	outText = s2ws(buffer);
	pRenderer->DrawText(outText.c_str(), strlen(buffer), &trect, 0xffffffff);

	rp.ClearFrame();
}

#ifdef WIN32
void GameUImanager::DrawUIState()
{
	if (m_pFPSLabel == NULL) return;
	if (gGetGameApplication()->IsShowUIState())
	{
		CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
		const std::multimap<unsigned int, CEGUI::WindowManager::StatInfo>& stats = winMgr.getStatLayout();
		int windowCount = winMgr.getWindowCount();
		const char* lastWindowName = winMgr.getLastCreateWindow().c_str();

		char buffer[256];
		sprintf(buffer, "layout count =%d window count=%d last window =%s", stats.size(), windowCount, lastWindowName);

		std::vector<CEGUI::String> texts;
		texts.push_back(buffer);
		std::multimap<unsigned int, CEGUI::WindowManager::StatInfo>::const_iterator itStat = stats.begin();
		int i = 0;
		while (itStat != stats.end())
		{
			if (i > 19)
				break;

			unsigned int time = timeGetTime() - itStat->first;
			CEGUI::String name = itStat->second.name;
			CEGUI::Window* wnd = CEGUI::WindowManager::getSingleton().getWindow(name);
			if (wnd)//&&wnd->isVisible(false))
			{
				const char* layoutName = itStat->second.name.c_str();
				float memSize = itStat->second.size;
				char isShow = '-';
				if (wnd->isVisible(true))
					isShow = '+';
				if (time < 1000)//小于1�?
				{
					sprintf(buffer, "%clife time<1s mem=%.2fM name=%s", isShow, memSize, layoutName);
				}
				else if (time < 60 * 1000)//小于1分钟.
				{
					sprintf(buffer, "%clife time=%ds mem=%.2fM minute name = %s", isShow, time/(1000), memSize, layoutName);
				}
				else if (time < 60 * 60 * 1000)//小于1小时
				{
					sprintf(buffer, "%clife time=%dm mem=%.2fM hour name=%s", isShow, time/(1000*60), memSize, layoutName);
				}
				texts.push_back(buffer);
			}
			++itStat;
		}
		for (size_t i = 0; i < 20; i++)
		{
			if (ma_pCEGUILabel[i])
			{
				if (i < texts.size())
				{
					ma_pCEGUILabel[i]->setVisible(true);
					ma_pCEGUILabel[i]->setText(texts[i]);
					ma_pCEGUILabel[i]->SetTextColor(0xffffffff);
					ma_pCEGUILabel[i]->EnableAllowModalState(true);
					ma_pCEGUILabel[i]->setTopMost(true);
				}
				else
					ma_pCEGUILabel[i]->setVisible(false);
			}
		}
	}
	else
	{
		for(int i = 0; i < 20;i++)
		{
			if (ma_pCEGUILabel[i])
				ma_pCEGUILabel[i]->setVisible(false);
		}
	}
}
#endif
#endif

void GameUImanager::PlayScreenEffect(int effectid,float fx,float fy,bool delSame)
{
	Nuclear::NuclearDisplayMode mode = Nuclear::GetEngine()->GetRenderer()->GetDisplayMode();

	if (delSame)
	{
		std::map<int, stScreenEffect>::iterator locItor = m_mapScreenEffect.find(effectid);
		if (locItor != m_mapScreenEffect.end())
		{
			stScreenEffect eInfo = static_cast<stScreenEffect>(locItor->second);
			
			eInfo.pEffect->RemoveNotify(eInfo.pNotify);
			delete eInfo.pNotify;
			eInfo.pNotify = NULL;

			Nuclear::GetEngine()->ReleaseEffect(eInfo.pEffect);
			eInfo.pEffect = NULL;

			m_mapScreenEffect.erase(locItor);
		}
	}

	Nuclear::IEffect* pEffect = Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(effectid));
	if (pEffect == NULL)
		return;
	ScreenEffectNotify* pEffectNotify = new ScreenEffectNotify;
	pEffect->AddNotify(pEffectNotify);

	int posX = fx;
	int posY = fy;
	if (fx <= 1.0)
		posX = mode.width * fx;
	if (fy <= 1.0)
		posY = mode.height * fy;
	pEffect->SetLocation(Nuclear::NuclearLocation(posX, posY));
	stScreenEffect sInfo;
	sInfo.pEffect = pEffect;
	sInfo.pNotify = pEffectNotify;
	m_mapScreenEffect[effectid] = sInfo;
}

bool GameUImanager::OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	if (!m_bUIInited) return false;

	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	switch (msg)
	{
#ifndef WIN32
	case Nuclear::WM_LBUTTONDOWN:
#else
	case WM_LBUTTONDOWN:
#endif
	{
		float x = (float)wParam;
		float y = (float)lParam;
		m_LastMousePoint.x = x;
		m_LastMousePoint.y = y;
		if (CEGUI::WindowManager::getSingleton().isWindowPresent("QuackMemberSort/Back/btn"))
			CheckToolTips(x, y, CEGUI::WindowManager::getSingleton().getWindow("QuackMemberSort/Back/btn"));
		
		//杨斌------颜色框单独处�?
		//CheckToolTips(x, y, CInsetdialogDlg::GetSingleton());

		CEGUI::MouseCursor& mouse(CEGUI::MouseCursor::getSingleton());
		CEGUI::Point mousePt(x, y);
		mouse.setPosition(mousePt);

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CheckTipsWnd.OnLButtonDown");

		bool isDialogShow = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NpcDialog.isShow") > 0 ? true : false;

		if (isDialogShow && !CEGUI::System::getSingleton().getModalTarget())
		{
			//杨斌----关闭延时
			float ptime = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NpcDialog.getDialogPastTime");
			if (ptime < NpcTalkDelayTime)
			{
				return true;
			}

			if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CheckTipsWnd.CheckNpcDialogTouch"))
			{
				return true;
			}
		}

		guiSystem.injectMousePosition(x, y);
		if (guiSystem.injectMouseButtonDown(CEGUI::LeftButton, (int)hWnd) || isDialogShow)
		{
			return true;
		}
	}
	break;
	case Nuclear::WM_CLICK:
	{
		float x = (float)wParam;
		float y = (float)lParam;
		guiSystem.injectMousePosition(x, y);

		if (guiSystem.injectMouseButtonUp(CEGUI::LeftButton))
		{
			return true;
		}
	}
	break;
#ifndef WIN32
	case Nuclear::WM_LBUTTONUP:
#else
	case WM_LBUTTONUP:
#endif
	{
		float x = (float)wParam;
		float y = (float)lParam;
		guiSystem.injectMousePosition(x, y);
		if (guiSystem.injectMouseButtonUp(CEGUI::LeftButton, (int)hWnd))
		{
			if (gGetNewRoleGuideManager() && !gGetNewRoleGuideManager()->NeedLockScreen())
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CheckTipsWnd.OnLButtonUp");
			return true;
		}
		if (gGetNewRoleGuideManager() && !gGetNewRoleGuideManager()->NeedLockScreen())
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CheckTipsWnd.OnLButtonUp");
	}
	break;
	case Nuclear::WM_DOUBLECLICK:
	{
		float x = (float)wParam;
		float y = (float)lParam;
		guiSystem.injectMousePosition(x, y);
		if (guiSystem.injectMouseButtonUp(CEGUI::LeftButton))
		{
			return true;
		}
	}
	break;
#ifndef WIN32
	case Nuclear::WM_MOUSEMOVE:
#else
	case WM_MOUSEMOVE:
#endif
	{
		if (gGetNewRoleGuideManager() && gGetNewRoleGuideManager()->NeedLockScreen()) {
			return true;
		}

		int iwParam = wParam;
		if (wParam > 10000)
		{
			iwParam = (2 ^ 32 - wParam) * -1;
		}
		float x = (float)iwParam;
		float y = (float)lParam;
		float del_X = x - m_LastMousePoint.x;//oldPos.d_x;
		float del_Y = y - m_LastMousePoint.y;//oldPos.d_y;
		m_LastMousePoint.x = x;
		m_LastMousePoint.y = y;
		bool b = guiSystem.injectMouseMove(del_X, del_Y);

		CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
		CEGUI::Window* pWindow = guiSystem.getTargetWindow(CEGUI::Point(x, y), false);
		if (pWindow)
		{
			if (pWindow != winMgr.getWindow("root_wnd"))
			{
				m_iOnUI = 1;
			}
		}
		return b;
	}
	break;
	case Nuclear::WM_DRAG:
	{
		if (gGetNewRoleGuideManager() && gGetNewRoleGuideManager()->NeedLockScreen())
			return true;

		float x = (float)wParam;
		float y = (float)lParam;
		int state = (int)hWnd;

		if (state == 1)
		{
			m_ptDragBenginPoint.d_x = x;
			m_ptDragBenginPoint.d_y = y;
		}
		if (state != 3)
		{
			if (moveFloatItem(x, y))
				return true;
		}

		if (state == 3)
		{
			CEGUI::MouseCursor& mouse(CEGUI::MouseCursor::getSingleton());
			CEGUI::Point mousePt = mouse.getPosition();
			float x_offset = fabsf(mousePt.d_x - m_ptDragBenginPoint.d_x);
			float y_offset = fabsf(mousePt.d_y - m_ptDragBenginPoint.d_y);
			printf("drag offset:%3.2f %2.2f \n", x_offset, y_offset);
		}

		bool b = guiSystem.injectMouseDrag(state, x, y, 0.0f);
		if (b)
		{
			return true;
		}
	}
	break;
	case Nuclear::WM_SLIDE:
	{
		if (gGetNewRoleGuideManager() && gGetNewRoleGuideManager()->NeedLockScreen()) {
			return true;
		}
		float x = (float)wParam;
		float y = (float)lParam;

		int dir = (int)hWnd;
		bool b = guiSystem.injectMouseSlide(dir, x, y, 30.0f);
		if (b)
		{
			return true;
		}
	}
	break;
	case Nuclear::WM_LONGPRESS:
	{
		int state = (int)hWnd;
		float x = (float)wParam;
		float y = (float)lParam;
		guiSystem.injectMousePosition(x, y);
		if (state == 2) {
			if (moveFloatItem(x, y)) {
				return true;
			}
		}
		else if (state == 3)
		{
			CEGUI::Window* pWindow = guiSystem.getTargetWindow(CEGUI::MouseCursor::getSingleton().getPosition(), false);
			if (pWindow == NULL || pWindow->getType() != CEGUI::String("TaharezLook/ItemCell"))
			{
				if (gGetRoleItemManager()->DestroyItem())
					return true;
			}
		}
		if (guiSystem.injectLongPress(CEGUI::LeftButton, state))
		{
			return true;
		}
	}
	break;
	case 990:
	{
		m_iOnUI = 0;
	}
	break;
    case 998:
    case 999:
    {
		if (m_iOnUI != 0)
		{
			return false;
		}
		if (GetBattleManager())
		{
			if (GetBattleManager()->IsInBattle())
			{
				return false;
			}
		}
		if (guiSystem.getModalTarget() != NULL)
		{
			return false;
		}
		if (msg == 998)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("YinCang.CHideAll");
			return true;
		}
		else if (msg == 999)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("YinCang.CShowAll");
			return true;
		}
    }
    break;
	default:
		break;
	}

	if (guiSystem.isInModalState())
	{
		return true;
	}

	return false;
}

void GameUImanager::Run(int now, int delta)
{
	//UI限刷新频�?
	unsigned int tick = Nuclear::GetMilliSeconds();
	m_updateDeltaTime += delta;
	if (m_updateDeltaTime < s_uiMaxUIUpdateTimePreFPS)
	{
		return;
	}
	//UI限刷新频率结�?

	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	if (gGetGameApplication()->IsShowFPS() && m_pFPSLabel == NULL)
	{
		CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
		if (m_pFPSLabel == NULL){
			m_pFPSLabel = winMgr.createWindow("TaharezLook/StaticText", OnlyName);
		}

		pRootWindow->addChildWindow(m_pFPSLabel);
		m_pFPSLabel->setPosition(CEGUI::UVector2(CEGUI::UDim(0.5f, -200), CEGUI::UDim(0.0f, 150)));
		m_pFPSLabel->setSize(CEGUI::UVector2(CEGUI::UDim(0, 450), CEGUI::UDim(0, 25)));
		m_pFPSLabel->setMousePassThroughEnabled(true);
	}
	
#if (defined WIN32) && (defined XP_PERFORMANCE)
	if (gGetGameApplication()->IsShowUIState())
	{
		for (int i = 0; i < 20; ++i)
		{
			if (ma_pCEGUILabel[i] == NULL)
			{
				CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
				CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
				CEGUI::String OnlyName = winMgr.generateUniqueWindowName();
				if (ma_pCEGUILabel[i] == NULL){
					ma_pCEGUILabel[i] = winMgr.createWindow("TaharezLook/StaticText", OnlyName);
				}
				pRootWindow->addChildWindow(ma_pCEGUILabel[i]);
				ma_pCEGUILabel[i]->setPosition(CEGUI::UVector2(CEGUI::UDim(0.5f, -400), CEGUI::UDim(0.0f, 125 + i * 25)));
				ma_pCEGUILabel[i]->setSize(CEGUI::UVector2(CEGUI::UDim(0, 800), CEGUI::UDim(0, 25)));
				ma_pCEGUILabel[i]->setMousePassThroughEnabled(true);
			}
		}
	}
#endif

	// inject the time pulse
	float elapsed = static_cast<float>(m_updateDeltaTime) / 1000.0f;
	guiSystem.injectTimePulse(elapsed);

	if (gGetStateManager() && gGetStateManager()->isGameState(eGameStateRunning))
	{
		if (gGetEmotionManager())
		{
			gGetEmotionManager()->RefreshEmotionPackTime(m_updateDeltaTime);
		}

		gGetSceneMovieManager()->Updata(m_updateDeltaTime);

		std::map<Nuclear::IEffect*, float>::iterator it = m_mapBoundsEffect.begin();
		for (; it != m_mapBoundsEffect.end(); ++it)
		{
			(*it).second += elapsed;
		}
	}

	//updateMessageTips(elapsed);

	m_updateDeltaTime = 0;
}
void GameUImanager::OnGameStart()
{
	float pro = 33;
	float step = 2;
	cocos2d::CCDirector::sharedDirector()->SetSwapBuffer(true);
	

	if (gGetNewRoleGuideManager() && gGetNewRoleGuideManager()->isGuideFinish(30023))
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LogoInfoDialog.GetSingletonDialogAndShowIt");
	}
	else
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LogoInfoDialog.GetSingletonDialogAndShowIt");
	}
	gGetGameApplication()->DrawLoginBar(pro); pro += step;
	gGetGameApplication()->DrawLoginBar(pro); pro += step;
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatOutBoxOperatelDlg.getInstanceAndShow");
	gGetGameApplication()->DrawLoginBar(pro); pro += step;
	gGetGameApplication()->DrawLoginBar(pro); pro += step;
	gGetGameApplication()->DrawLoginBar(pro); pro += step;
	gGetGameApplication()->DrawLoginBar(pro); pro += step;
	gGetGameApplication()->DrawLoginBar(pro); pro += step;
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatOutputDialog.getInstanceNotShow");

	gGetGameApplication()->DrawLoginBar(pro); pro += step;
	gGetGameApplication()->DrawLoginBar(pro); pro += step;

	gGetGameApplication()->DrawLoginBar(pro); pro += step;

#ifdef XP_PERFORMANCE
	double beginSize = CFileUtil::GetUsedMemory();
#endif

	//预加载配置的LAYOUT.
	static bool hasPreloadLayout = false;
	if (!hasPreloadLayout)
	{
		hasPreloadLayout = true;
		CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
		float layoutCacheStepVolume = (100 - pro) / winMgr.getCacheLayout().size();
		std::set<CEGUI::String>::const_iterator it = winMgr.getCacheLayout().begin();
		while (it != winMgr.getCacheLayout().end())
		{
			winMgr.destroyWindow(winMgr.loadWindowLayout(it->c_str()));
			gGetGameApplication()->DrawLoginBar(pro);
			pro += layoutCacheStepVolume;
			++it;
		}
	}
	gGetGameApplication()->DrawLoginBar(100);

#ifdef XP_PERFORMANCE
	double endSize = CFileUtil::GetUsedMemory();
	double dtSize = endSize - beginSize;
#endif

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ShowHide.OnGameStart");

	gGetGameApplication()->DrawLoginBar(100);

	EnableDlgSound(true);

#ifdef ANDROID // yeqing 2015-12-14
	Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());
	setLoginProgress(false);
#endif // ANDROID

}

void HandleGoToFunction(const int &mapid, const int &xPos, const int &yPos, const int& npcid, const int& chefu, int gotodirectly, const int64_t npckey)
{
	if (GetBattleManager()->IsInBattle())
	{
		//gGetGameUIManager()->AddMessageTipById(141363);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(141363);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.AddMessageTipById_", 1);
		return;
	}

	if ((gGetScene()->GetMapID() != mapid) && gotodirectly)
	{
		GetMainCharacter()->FlyToPos(mapid, xPos, yPos, npckey, npcid);
	}
	else
	{
		GetMainCharacter()->m_MapWalker.SetTarget(xPos*g_logicGrid, yPos*g_logicGrid, mapid, npckey, chefu, false, true, npcid);
	}
}

void GameUImanager::removeGameUpdateTextView()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	closeWebView();
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "closeUpdateView", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);

		t.env->DeleteLocalRef(t.classID);
	}
#else
	WinSDK::getInstance()->closeWinWebView();
#endif
}

void GameUImanager::showGameUpdateTextView()
{
	CEGUI::Window* textFrame = CEGUI::WindowManager::getSingleton().getWindow("NewsWarn/main");
	float width = textFrame->getPixelSize().d_width;
	float height = textFrame->getPixelSize().d_height;
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	int factor = cocos2d::CCDirector::sharedDirector()->getContentScaleFactor();
	int sWidth = Nuclear::GetEngine()->GetScreenWidth() / factor;
	int sHeight = Nuclear::GetEngine()->GetScreenHeight() / factor;
	width = width / factor;
	height = height / factor;

    int logicWidth = Nuclear::GetEngine()->GetLogicWidth();
    int logicHeight = Nuclear::GetEngine()->GetLogicHeight();
    int sWidthAndroid = Nuclear::GetEngine()->GetScreenWidth();
    int sHeightAndroid = Nuclear::GetEngine()->GetScreenHeight();
    
    int widthFinal = (int)(width / logicWidth * sWidthAndroid * 1.0f);
    int heightFinal = (int)(height / logicHeight * sHeightAndroid * 1.0f);
    showWebView(sWidth / 2, sHeight / 2, (int)widthFinal, (int)heightFinal);
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	int logicWidth = Nuclear::GetEngine()->GetLogicWidth();
	int logicHeight = Nuclear::GetEngine()->GetLogicHeight();
	int sWidthAndroid = Nuclear::GetEngine()->GetScreenWidth();
	int sHeightAndroid = Nuclear::GetEngine()->GetScreenHeight();

	int widthFinal = (int)(width / logicWidth * sWidthAndroid * 1.0f);
	int heightFinal = (int)(height / logicHeight * sHeightAndroid * 1.0f);

	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "openUpdateview", "(II)V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID, (int)widthFinal, (int)heightFinal);

		t.env->DeleteLocalRef(t.classID);
	}
#else
	int logicWidth = Nuclear::GetEngine()->GetLogicWidth();
	int logicHeight = Nuclear::GetEngine()->GetLogicHeight();
	int screenWidth = Nuclear::GetEngine()->GetScreenWidth();
	int screenHeight = Nuclear::GetEngine()->GetScreenHeight();
	float finalWidth = width / logicWidth * screenWidth;
	float finalHeight = height / logicHeight * screenHeight;
	std::string strHttpNoticeUrl = GetServerInfo()->getHttpAdressByEnum(eHttpNoticeUrl) + "?r=x" + StringCover::intToString(rand());
	WinSDK::getInstance()->openWinWebView(strHttpNoticeUrl, finalWidth, finalHeight);
#endif
}

void GameUImanager::hideGameUpdateTextView()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	hideWebView();
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("void GameUImanager::hideGameUpdateTextView() android...");
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "hideUpdateview", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);

		t.env->DeleteLocalRef(t.classID);
	}
#else
	WinSDK::getInstance()->hideWinWebView();
#endif
}

void GameUImanager::resumeGameUpdateTextView()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	resumeWebView();
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	LOGE("void GameUImanager::resumeGameUpdateTextView() android...");
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "resumeUpdateview", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);

		t.env->DeleteLocalRef(t.classID);
	}
#else
	WinSDK::getInstance()->resumeWinWebView();
#endif
}

void GameUImanager::updateHideAllUI(unsigned int dtTime)
{
	if (m_bStartHideAllUI)
	{
		m_ConsumeHideAllUITime += dtTime;
		if (m_ConsumeHideAllUITime > HideAllUITime)
		{
			m_ConsumeHideAllUITime = 0;
			m_bStartHideAllUI = false;
		}
	}
}

void GameUImanager::HideAllUI(bool bHide)
{
	if (m_bHideAllUI != bHide)
	{
		m_bStartHideAllUI = true;
		m_bHideAllUI = bHide;
		m_StartHideAllUITime = Nuclear::GetMilliSeconds();
		m_ConsumeHideAllUITime = 0;
	}
}

bool GameUImanager::HandleConfirmEscapeCancel(const CEGUI::EventArgs& e)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "ExitGame", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
	else
	{
		exit(0);
	}
#else
	exit(0);
#endif
	return MessageManager::HandleDefaultCancelEvent(e);
}

bool GameUImanager::HandleConfirmInitEscape(const CEGUI::EventArgs& e)
{
	const CEGUI::WindowEventArgs& windowargs = static_cast<const CEGUI::WindowEventArgs&>(e);
	stConfirmBoxInfo* pConfirmBoxInfo = static_cast<stConfirmBoxInfo*>(windowargs.window->getUserData());
	gGetMessageManager()->RemoveConfirmBox(pConfirmBoxInfo);
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
	init();
#else
	if (SDK::GameSdk::init()){

	}
	else{
		CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmEscape, this);
		CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, this);
		gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
	}
#endif

#endif

	return true;
}

bool GameUImanager::HandleConfirmEscape(const CEGUI::EventArgs& e)
{
	const CEGUI::WindowEventArgs& windowargs = static_cast<const CEGUI::WindowEventArgs&>(e);
	stConfirmBoxInfo* pConfirmBoxInfo = static_cast<stConfirmBoxInfo*>(windowargs.window->getUserData());
	gGetMessageManager()->RemoveConfirmBox(pConfirmBoxInfo);

#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
	login();
#else
	if (SDK::GameSdk::login(false)){

	}
	else{
		CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmEscape, this);
		CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, this);
		gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
	}
#endif
#endif

#ifdef _YJ_SDK_
#ifdef ANDROID
	LOGE("GameUImanager::HandleConfirmEscape MT3::ChannelManager::yj_loginAgain() begin");
	MT3::ChannelManager::yj_loginWhenFailed();
	LOGE("GameUImanager::HandleConfirmEscape MT3::ChannelManager::yj_loginAgain() end");
#else

#endif
#endif // _YJ_SDK_

	return true;
}

void GameUImanager::JYSdkLogin()
{

#ifdef _YJ_SDK_
#ifdef ANDROID
	LOGE("GameUImanager::JYSdkLogin begin");
	MT3::ChannelManager::yj_sdkLogin();
	LOGE("GameUImanager::JYSdkLogin end");
#else

#endif
#endif // _YJ_SDK_

}

void GameUImanager::JYSdkLogout()
{
#ifdef _YJ_SDK_
#ifdef ANDROID
	MT3::ChannelManager::yj_sdkLogout();
#else

#endif
#endif // _YJ_SDK_
}

void GameUImanager::sdkInit(){

#ifdef _LOCOJOY_SDK_

#ifdef ANDROID
	init();
#else
	if (!SDK::GameSdk::init())
	{
		CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmInitEscape, this);
		CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, this);
		gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
	}
#endif

#endif
}

extern "C"
{
	void GameUImanager::sdkLogin(){
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
		if (gGetGameApplication()->IsUseSDKInWindows()) {
			WinSDK::getInstance()->openLoginUrl();
			return;
		}
#endif

#ifdef _LOCOJOY_SDK_
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("Config.setCur3rdPlatform", "app");
#ifdef ANDROID
		if (MT3::ChannelManager::getIsYingYongBao())
		{
			if (!MT3::ChannelManager::getIsYYBLogined()) {
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SelectServerEntry_YingYongBaoShow"); // "显示应用宝的界面"
			} else {
				MT3::ChannelManager::setIsYYBLogined(false);
			}
		}
		else
		{
			login();
		}
#else
		if (!SDK::GameSdk::login(true))
		{
			CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmEscape, this);
			CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, this);
			gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
		}
#endif

#endif
	}

	void GameUImanager::sdkExit()
	{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
		LOGE("GameUImanager::sdkExit()");
		//exitSdk();
#else
		if (!SDK::GameSdk::login(false))
		{
			CEGUI::Event::Subscriber s(&GameUImanager::HandleConfirmEscape, this);
			CEGUI::Event::Subscriber c(&GameUImanager::HandleConfirmEscapeCancel, this);
			gGetMessageManager()->AddConfirmBox(eConfirmLoginFail, MHSD_UTILS::GetTipsMsg(160167).c_str(), s, c);
		}
#endif

#endif
	}
}


//yangbin---reBuild MessageTips 20151229
void GameUImanager::updateMessageTips(float fDelta)
{
	//int MAXSHOW = 3;

	//CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
	//vector<CEGUI::String>::iterator it = m_vecMessageTips.begin();

	//for (int i = 0; it != m_vecMessageTips.end();)
	//{
	//	CEGUI::MessageTip* msgTip = NULL;
	//	if (winMgr.isWindowPresent(*it))
	//	{
	//		msgTip = static_cast<CEGUI::MessageTip*>(winMgr.getWindow(*it));

	//		if (i == 0 && !msgTip->isVisible())//yangbin----每次update都要检查第一条是不是隐藏的，某些界面设置模态，隐藏其他界面后，界面关闭会把tips所有条目隐�?
	//		{
	//			msgTip->setVisible(true);
	//		}


	//		if (msgTip->GetTextureIsLoading())
	//		{
	//			it++;
	//			i++;
	//			continue;
	//		}

	//		float ParentHeight = msgTip->getParentPixelHeight();
	//		int size = m_vecMessageTips.size();

	//		int loop = size - i > MAXSHOW - 1 ? MAXSHOW : size - i;
	//		float h = msgTip->getHeight().d_offset + TIPSDISHEIGHT;
	//		for (int j = 0; j < loop - 1; j++)//loop-1为了刨去本身
	//		{
	//			if (winMgr.isWindowPresent(m_vecMessageTips[i + j + 1]))
	//			{
	//				CEGUI::MessageTip* pNext = static_cast<CEGUI::MessageTip*>(winMgr.getWindow(m_vecMessageTips[i + j + 1]));
	//				h += pNext->getHeight().d_offset + TIPSDISHEIGHT;
	//			}
	//		}
	//		msgTip->SetDestYPos(tipsStartYPos - h / ParentHeight);//这个0.5是暂时默认从屏幕中间开始计算，因为每条tips的默认值是0.5，懒得起个变量了

	//		if (i > 0)
	//		{
	//			CEGUI::MessageTip* pPrev = static_cast<CEGUI::MessageTip*>(winMgr.getWindow(m_vecMessageTips[i - 1]));
	//			float a = pPrev->getYPosition().d_scale;
	//			float b = msgTip->getYPosition().d_scale;
	//			float c = (b - a) * ParentHeight;
	//			float d = pPrev->getHeight().d_offset + TIPSDISHEIGHT;
	//			if (c >= d &&  !msgTip->isVisible())
	//			{
	//				msgTip->setVisible(true);
	//				break;
	//			}
	//		}

	//		if (!msgTip->isVisible())
	//		{
	//			it++;
	//			i++;
	//			continue;
	//		}

	//		float WndH = m_fHeightFactor / 10;//这里需要一个恒定的值，来确保不同高度的窗口上升速率是一样的
	//		float speed = WndH / (m_fTipDisplayTime / 2 / fDelta);

	//		msgTip->SetStartYPos(msgTip->GetStartYPos() - speed);

	//		if (msgTip->GetStartYPos() <= msgTip->GetDestYPos())
	//		{
	//			if (size - i > MAXSHOW)//如果超过�?个，那么最先到达顶端的删掉
	//			{
	//				CEGUI::WindowManager::getSingleton().destroyWindow(msgTip);
	//				it = m_vecMessageTips.erase(it);
	//				continue;
	//			}
	//			else
	//			{
	//				msgTip->SetStartYPos(msgTip->GetDestYPos());
	//			}
	//		}

	//		msgTip->setYPosition(cegui_reldim(msgTip->GetStartYPos()));

	//		it++;
	//		i++;
	//	}
	//	else{
	//		it = m_vecMessageTips.erase(it);
	//	}
	//}
}

void GameUImanager::makeMessageTipWnd(std::wstring& messageTip, bool bAddToChat, bool bEnable)
{
	//CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
	//CEGUI::MessageTip* mes = static_cast<CEGUI::MessageTip*>(winMgr.createWindow("TaharezLook/MessageTip"));

	//InitMessageTip(mes, messageTip, CEGUI::eMsgTip, bAddToChat);

	//if (mes->getRichEditbox())
	//{
	//	mes->getRichEditbox()->SetHoriAutoCenter(true);
	//}

	//mes->setEnabled(bEnable);
	//mes->setAlwaysOnTop(true);
	//mes->moveToFront();
	//mes->setTopMost(true);
	//mes->setMousePassThroughEnabled(true);

	//float ParentHeight = mes->getParentPixelHeight();
	//float xPos = (mes->getParentPixelWidth() - mes->getWidth().d_offset) / 2;
	//mes->SetStartYPos(0.58f);
	//float yPos = tipsStartYPos = mes->GetStartYPos();
	//mes->setPosition(CEGUI::UVector2(cegui_absdim(xPos), cegui_reldim(yPos)));

	//m_vecMessageTips.push_back(mes->getName());

	//if (m_vecMessageTips.size() > 1)
	//{
	//	mes->setVisible(false);
	//}

	//mes->SetDestYPos(yPos - (mes->getHeight().d_offset + TIPSDISHEIGHT) / ParentHeight);//上升一个tips的高�?
	//mes->SetDisplayTime(m_fTipDisplayTime);
}

void GameUImanager::clearMessages()
{
	//if (!CEGUI::WindowManager::getSingletonPtr()) return;

	//CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
	//while (!m_vecMessageTips.empty())
	//{
	//	vector<CEGUI::String>::iterator it = m_vecMessageTips.begin();
	//	CEGUI::MessageTip* msgTip = static_cast<CEGUI::MessageTip*>(winMgr.getWindow(*it));
	//	CEGUI::WindowManager::getSingleton().destroyWindow(msgTip);
	//	it = m_vecMessageTips.erase(it);
	//}
	//m_vecMessageTips.clear();
}

void GameUImanager::setTreasureMapId(int id)
{
	m_iTreasureMapId = id;
}

int GameUImanager::getTreasureMapId()
{
	return m_iTreasureMapId;
}

void GameUImanager::setCurrentItemId(int id)
{
	m_iCurItemId = id;
}

int GameUImanager::getCurrentItemId()
{
	return m_iCurItemId;
}

CEGUI::Window* GameUImanager::GetCursorWindow()
{
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	CEGUI::Point pt = CEGUI::MouseCursor::getSingleton().getPosition();
	CEGUI::Window* pwindow = guiSystem.getTargetWindow(pt, false);
	return pwindow;
}

Dialog* GameUImanager::getDialog(CEGUI::Window *pWin)
{
	std::map<CEGUI::Window*, Dialog*>::iterator iter = m_UIDialog.find(pWin);
	if (iter != m_UIDialog.end()) {
		return iter->second;
	}
	return NULL;
}

void GameUImanager::CheckNumInputDlg(float x, float y, Dialog* Dialog)
{
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	if (Dialog && Dialog->IsVisible())
	{
		const CEGUI::Point pt(x, y);
		CEGUI::Window* pWindow = guiSystem.getTargetWindow(pt, false);
		if (pWindow == NULL || (pWindow != Dialog->GetWindow() && !Dialog->GetWindow()->isChild(pWindow)))
		{
			Dialog->OnClose();
		}
	}
}

//new add 2015-07-02
bool GameUImanager::CheckDialogAncestorInTarget(float x, float y, Dialog* Dialog, CEGUI::Window* ancestorWnd)
{
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	if (Dialog && Dialog->IsVisible()) {
		const CEGUI::Point pt(x, y);
		CEGUI::Window* pWindow = guiSystem.getTargetWindow(pt, false);
		if (pWindow != NULL
			&& (pWindow == Dialog->GetWindow() || pWindow->isAncestor(Dialog->GetWindow()))
			&& (ancestorWnd == pWindow)
			)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		return false;
	}
}

void GameUImanager::CheckToolTips(float x, float y, Dialog* Dialog) {
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	if (Dialog && Dialog->IsVisible()) {
		const CEGUI::Point pt(x, y);
		CEGUI::Window* pWindow = guiSystem.getTargetWindow(pt, false);
		if (pWindow == NULL
			|| (pWindow != Dialog->GetWindow() && !pWindow->isAncestor(Dialog->GetWindow()))
			)
		{
			Dialog->OnClose2(false, true);
		}
	}
}

void GameUImanager::CheckToolTipsRegularDlg(float x, float y, Dialog* Dialog)
{
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	if (Dialog && Dialog->IsVisible()) {
		const CEGUI::Point pt(x, y);
		CEGUI::Window* pWindow = guiSystem.getTargetWindow(pt, false);

		CEGUI::ItemCell *pItem = dynamic_cast<CEGUI::ItemCell*>(pWindow);
		if (pItem && pItem->getUserData())
		{
			return;
		}

		if (pWindow == NULL
			|| (pWindow != Dialog->GetWindow() && !pWindow->isAncestor(Dialog->GetWindow()))
			)
		{
			Dialog->OnClose2(false, true);
		}
	}
}

void GameUImanager::CheckToolTips(float x, float y, CEGUI::Window* pWnd)
{
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	if (pWnd->isVisible()) {
		const CEGUI::Point pt(x, y);
		CEGUI::Window* pWindow = guiSystem.getTargetWindow(pt, false);
		if (pWindow == NULL
			|| (pWindow != pWnd && !pWindow->isAncestor(pWnd))
			)
		{
			pWnd->setVisible(false);
		}
	}
}

void GameUImanager::SetDetachIMEState()
{
	m_bAttachIME = false;
}

float GameUImanager::GetCurEditBoxBottom()
{
	float bottom = 0.0f;
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	CEGUI::Window* pEditBox = guiSystem.getKeyboardTargetWindow();
	if (pEditBox != NULL) {
		float height = pEditBox->getPixelSize().d_height;
		float pos = pEditBox->GetScreenPos().d_y * g_adapter.GetScreenHeight() / g_adapter.GetLogicHeight();
		bottom = height + pos;
	}

	return bottom;
}

void GameUImanager::OnBackSpace()
{
	if (CEGUI::System::getSingletonPtr() != NULL)
	{
		CEGUI::System& guiSystem = CEGUI::System::getSingleton();
		guiSystem.injectKeyDown(0x0E);
	}
}

void GameUImanager::InjectString(const char* pStr)
{
	if (CEGUI::System::getSingletonPtr() != NULL && pStr != NULL)
	{
		std::wstring wstr = StringCover::to_wstring(pStr);
		CEGUI::System& guiSystem = CEGUI::System::getSingleton();
		guiSystem.injectString(wstr);
	}
}

void GameUImanager::InjectChar(const char* pStr)
{
	if (CEGUI::System::getSingletonPtr() != NULL && pStr != NULL)
	{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		std::wstring wstr = StringCover::to_wstring(pStr);

		CEGUI::System& guiSystem = CEGUI::System::getSingleton();
		guiSystem.injectChar(pStr);
#else
		std::wstring wstr = StringCover::to_wstring(pStr);

		CEGUI::System& guiSystem = CEGUI::System::getSingleton();
		guiSystem.injectChar(wstr);
#endif       
	}
}

bool GameUImanager::DetachIME()
{
	if (m_pUIIMEDelegate == NULL)
	{
		return false;
	}

	if (!m_bAttachIME)
	{
		return false;
	}

	if (m_pUIIMEDelegate->detachWithIME())
	{
		m_bAttachIME = false;
		m_pUIIMEDelegate->setInputText(NULL);
		return true;
	}

	return false;
}

void GameUImanager::AttachIME(const CEGUI::String& str)
{
	if (m_pUIIMEDelegate == NULL)
	{
		return;

	}
	m_pUIIMEDelegate->setInputText(str.c_str());

	if (m_pUIIMEDelegate->attachWithIME())
	{
		m_bAttachIME = true;
	}
}

void GameUImanager::OnClickEditArea(const CEGUI::String& str)
{
//#ifdef TARGET_OS_IPHONE
//    m_pEditBoxImpl->openKeyboard();
//#else
//    AttachIME(str);
//#endif

	AttachIME(str);
    
}

#ifdef TARGET_OS_IPHONE
void GameEditBox::editBoxEditingDidBegin(cocos2d::extension::CCEditBox* editBox)
{
    
}

void GameEditBox::editBoxEditingDidEnd(cocos2d::extension::CCEditBox* editBox)
{
    gGetGameUIManager()->InjectString(gGetGameUIManager()->GetEditBoxImpl()->getText());
    gGetGameUIManager()->GetEditBoxImpl()->setText("");
}

void GameEditBox::editBoxTextChanged(cocos2d::extension::CCEditBox* editBox, const std::string& text)
{
    
}

void GameEditBox::editBoxReturn(cocos2d::extension::CCEditBox* editBox)
{

}

#endif


#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
void ClickWp8EditArea(const CEGUI::String& str, const CEGUI::Rect& rect)
{
	if (gGetGameUIManager())
	{
		gGetGameUIManager()->OnClickWp8EditArea(str, rect);
	}
}

void GameUImanager::OnClickWp8EditArea(const CEGUI::String& str, const CEGUI::Rect& rect)
{
	AttachWp8IME(str, rect);
}

void GameUImanager::AttachWp8IME(const CEGUI::String& str, const CEGUI::Rect& rect)
{
	if (m_pUIIMEDelegate == NULL)
	{
		return;
	}
	m_pUIIMEDelegate->setInputText(str.c_str());
	m_pUIIMEDelegate->setInputRect(&rect);
	if (m_pUIIMEDelegate->attachWithIME())
	{
		m_bAttachIME = true;
	}
}
#else
void ClickEditArea(const CEGUI::String& str)
{
	if (gGetGameUIManager()) {
		gGetGameUIManager()->OnClickEditArea(str);
	}
}
#endif

// layout �첽�������

class LayoutReadTask : public Nuclear::NuclearFileIOManager::AsyncReadTask
{
private:
	CEGUI::String mFilename;
	CEGUI::String mNamePrefix;
	std::string mLuaCallbackFunc;
	bool mbCacheLayout;

	CEGUI::Window* mpWindow;

public:
	LayoutReadTask(const std::string& filename, const CEGUI::String& name_prefix, const std::string& strLuaCallbackFunc, bool bCacheLayout)
		: Nuclear::NuclearFileIOManager::AsyncReadTask(Nuclear::Engine::GetInstance().GetFileIOManager(), L"", 0, false)
		, mFilename(filename)
		, mNamePrefix(name_prefix)
		, mLuaCallbackFunc(strLuaCallbackFunc)
		, mbCacheLayout(bCacheLayout)

		, mpWindow(NULL)
	{
	}

	virtual void Execute()
	{
		CEGUI::WindowManager* pWinMgr = CEGUI::WindowManager::getSingletonPtr();
		if (pWinMgr)
		{
			pWinMgr->setLoading(true);
			if (mbCacheLayout)
			{
				CEGUI::Window* rootWnd = pWinMgr->loadWindowLayoutFromFile(mFilename, "@template@");
				if (rootWnd)
				{
					pWinMgr->addTemplateWindow(mFilename, rootWnd);
					mpWindow = pWinMgr->cloneWindow(rootWnd, mNamePrefix);
				}
			}
			else
			{
				mpWindow = pWinMgr->loadWindowLayoutFromFile(mFilename, mNamePrefix);
			}
			pWinMgr->setLoading(false);
		}
	}

	virtual void OnReady()
	{
		if (!mLuaCallbackFunc.empty())
		{
			CallLuaUtil clu;
			clu.addArg(mpWindow, "CEGUI::Window", false);
			clu.callLua(mLuaCallbackFunc.c_str());
		}

		delete this;
	}

	virtual void OnDiscard()
	{
		delete this;
	}
};

void GameUImanager::asyncLoadWindowLayout(const char* szFilename, const char* szNamePrefix, const char* szLuaCallbackFunc)
{
	CEGUI::WindowManager* pWinMgr = CEGUI::WindowManager::getSingletonPtr();
	if (pWinMgr)
	{
		std::string filename = szFilename;
		std::transform(filename.begin(), filename.end(), filename.begin(), ::tolower);

		CEGUI::String name_prefix = szNamePrefix;

		const std::set<CEGUI::String>& cachedLayoutNameSet = pWinMgr->getCacheLayout();
		bool bCacheLayout = cachedLayoutNameSet.find(filename) != cachedLayoutNameSet.end();
		if (bCacheLayout)
		{
			pWinMgr->setLoading(true);
			CEGUI::Window* pWindow = pWinMgr->cloneWindowFromTemplate(filename, name_prefix);
			pWinMgr->setLoading(false);
			if (pWindow)
			{
				if (szLuaCallbackFunc)
				{
					CallLuaUtil clu;
					clu.addArg(pWindow, "CEGUI::Window", false);
					clu.callLua(szLuaCallbackFunc);
				}

				return;
			}
		}

		LayoutReadTask* pTask = new LayoutReadTask(filename, name_prefix, szLuaCallbackFunc, bCacheLayout);
		if (pTask && !pTask->Submit())
		{
			delete pTask;
		}
	}
}
std::string GameUImanager::getClipboard()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	std::string str = SDK::GameSdk::getPasteBoard();
	return str;
#endif
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	std::string str = MT3::ChannelManager::getClipboardText();
	return str;
#endif
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
	char *url, *pData;
	size_t length;
	::OpenClipboard(NULL);
	HANDLE hData = ::GetClipboardData(CF_TEXT);
	if (hData == NULL)
	{
		::CloseClipboard();
		CloseClipboard();
		return "";
	}
	length = ::GlobalSize(hData);
	url = (char*)malloc(length + 1);
	pData = (char*)::GlobalLock(hData);
	strcpy(url, pData);
	::GlobalUnlock(hData);
	::CloseClipboard();
	url[length] = 0;
	return url;
#endif
}