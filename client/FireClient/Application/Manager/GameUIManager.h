#ifndef FireClient_GameUIManager_h
#define FireClient_GameUIManager_h

#include "nuiengine.h"
#include "CEGUI.h"
#include "Singleton.hpp"
#include "../Event.h"
#include "utils/Utils.h"

#ifdef TARGET_OS_IPHONE
#include "CCEditBoxImpl.h"
#endif

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "Utils2.h"
#endif

#include "XPRenderEffect.h"
#include "RendererModules/Cocos2D/CEGUICocos2DRenderer.h"
#include "ScriptingModules/LuaScriptModule/CEGUILua.h"
#include "CEGUIIMEDelegate.h"
#include "utils/FileUtil.h"
#include "GameApplication.h"
#include "LuaEffectNotify.h"
#include "LuaXPRenderEffect.h"
#define RED_COLOUR_TEXT L"[colrect='tl:FFFF0000 tr:FFFF0000 bl:FFFF0000 br:FFFF0000']"
#define BORDER_RED_COLOUR_TEXT L"[border='FF5F4100'][colrect='tl:FFFF0000 tr:FFFF0000 bl:FFFF0000 br:FFFF0000']"
#define GREEN_COLOUR_TEXT L"[colrect='tl:FF00FF00 tr:FF00FF00 bl:FF00FF00 br:FF00FF00']"
#define WHITE_COLOUR_TEXT L"[colrect='tl:FFFFFFFF tr:FFFFFFFF bl:FFFFFFFF br:FFFFFFFF']"
#define YELLOW_COLOUR_TEXT L"[colrect='tl:ffffff00 tr:ffffff00 bl:ffffff00 br:ffffff00']"
#define GRAY_COLOUR_TEXT L"[colrect='tl:ff696969 tr:ff696969 bl:ff696969 br:ff696969']"
#define PARENT_ITEM_COLOUR L"[border='FF5F4100'][colrect='tl:FFFFEAA9 tr:FFFFEAA9 bl:FFFFBA15 br:FFFFBA15']"

//#define HP_COLOUR_TEXT L"[colrect='tl:ff1c013e tr:ff1c013e bl:ff1c013e br:ff1c013e']"
#define HP_COLOUR_TEXT L"[colrect='tl:ffffff00 tr:ffffff00 bl:ffffff00 br:ffffff00']"

class Dialog;
class IEffect;
class XPRenderEffect;
class IEffectNotify;
class UISprite;
class UISpineSprite;
#ifdef TARGET_OS_IPHONE
class GameEditBox;
#endif

class UIEffectNotify : public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

class UIEffectWithExistXpRenderEffectNotify : public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

class SysMesBeginEffectNotify : public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

class SysMesFadeEffectNotify : public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

class ScreenEffectNotify : public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

struct stScreenEffect
{
	Nuclear::IEffect* pEffect;
	ScreenEffectNotify* pNotify;

	stScreenEffect& operator = (const stScreenEffect&x)
	{
		pEffect = x.pEffect;
		pNotify = x.pNotify;
		return *this;
	};
};

class SystemMsgRenderEffect : public XPRenderEffect
{
public:
	SystemMsgRenderEffect();
	~SystemMsgRenderEffect();

	void performPostRenderFunctions();
protected:
private:
};

class LastPoint {
public:
	float x;
	float y;
};

static std::vector<std::wstring> sEmptyParameters;

class TipsManager :public CSingleton < TipsManager >
{
public:
	void AddMsgTips(const int& msgid, const int& npcbaseid = 0, std::vector<std::wstring>& parameters = sEmptyParameters, bool bCheckSame = false)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(msgid);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(npcbaseid);

		lua_State * L = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->getLuaState();
		lua_newtable(L);
		for (size_t i = 0; i < parameters.size(); i++)
		{
			lua_pushstring(L, ws2s(parameters[i]).c_str());
			lua_rawseti(L, -2, i + 1);
		}

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(bCheckSame == false ? 0 : 1);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CChatManager.AddTipsMsg_", 4);
	}
};

inline TipsManager* GetTipsManager()
{
	if (TipsManager::GetInstance())
	{
		return TipsManager::GetInstance();
	}
	else
	{
		return TipsManager::NewInstance();
	}
}

class GameUImanager : public CSingleton<GameUImanager>
{
public:
	int m_iOnUI;

	GameUImanager();
	~GameUImanager();

	bool ChnInjectChar(CEGUI::utf32 code_point);
	void AddMessageTipById(int id);
	void AddSystemBoard(const std::wstring& message, bool bAddToChat = true, bool bforceshow = false);
	void AddMessageTip(const std::wstring& messageTip, bool bAddToChat = true, bool bEnalbe = true, bool bCheckSame = false);
	void InitMessageTip(CEGUI::MessageTip* & tip, const std::wstring& messageTip, CEGUI::TipType type, bool bAddToChat, bool bExistSysMes = false);
	bool IsHaveSameMessageTip(const std::wstring& messageTip);
	std::wstring GetFullPathFileName(std::wstring PathFileName);
	int QuickCommand(std::wstring Cmd, std::wstring& Param0, std::wstring& Param1, std::wstring& Param2, std::wstring& Param3);//LUA向C++发送和接受数据用接口
	int QuickCommandToLua(std::wstring Cmd, std::wstring Param0, std::wstring Param1, std::wstring Param2, std::wstring Param3);//C++向LUA发送和接受数据用接口
	void OnExitGameApp();
	void OnExitGameToSelectRole();
	void OnExitGameToLogin(int relogin = eExitToLoginType_ChangeLogin);
	void OnGameStart();
	void HandleEsc();
	void UnInitGameUI();
	void Run(int now, int delta);
	bool FloatSkillOnMouseButtonDown();
	void AddWndToRootWindow(CEGUI::Window* pWnd);
	CEGUI::Window* GetMainRootWnd();
	bool FloatItemOnMouseButtonDown();
	bool OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);
	void Draw();
	void initialiseDefaultResourceGroups();
	void initialiseResourceGroupDirectories();
	bool InitGameUIPostInit();
	bool InitGameUI();
	static LuaXPRenderEffect* createXPRenderEffect(int userid, int handler);
	static Nuclear::IEffectNotify* createNotify(int handler);

	std::string getClipboard();

	// 新手引导相关
	bool checkClickWhenGuide(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam);	// 新手引导过程中处理无效操作

	void asyncLoadWindowLayout(const char* szFilename, const char* szNamePrefix, const char* szLuaCallbackFunc);

private:
	std::map<Nuclear::IEffect*, float> m_mapBoundsEffect;
	float m_fBoundsEffectSpeed;

#if defined DEBUG || defined _DEBUG 
public:
	void ShowShowTexturePanel();
private:
	CEGUI::PushButton* m_pShowTextureBtn;
	CEGUI::Editbox* m_pTextureNameEdit;
	bool HandleShowTextureBtnClicked(const CEGUI::EventArgs& e);
#endif

private:
	CEGUI::Window* m_pRootWindow;
	LastPoint m_LastMousePoint;
	CEGUI::LuaScriptModule* m_pLuaScriptModule;
	CEGUI::ResourceProvider* m_pResourceProvider;
	CEGUI::Cocos2DRenderer* m_pCEGUICocos2DRender;

public:
	CEGUI::MessageTip* m_pSysBoardMes;
	std::vector<CEGUI::String> m_vecMessageTips;
	Nuclear::IEffect* m_pSysMesEndEffect;
	Nuclear::IEffect* m_pSysMesHoldEffect;
	CEGUI::uint m_iMessageTipNum;

	CEGUI::Window* GetCursorWindow();
	Dialog* getDialog(CEGUI::Window* pWin);
	void DrawSysMsgEffect();
	void DrawFPS();
	void OnSysMesEnd();
	void OnSysMesFade();
	void OnSysMesBegin();
	bool HandleSysMesFade(const CEGUI::EventArgs &e);
	bool HandleMessageTipOut(const CEGUI::EventArgs &e);

#ifdef XP_PERFORMANCE
#ifdef WIN32
	void DrawUIState();
#endif
#endif
#ifdef XP_PERFORMANCE
	void DrawPerformance();
#endif

private:
	bool m_bGameUIVisible;
	std::map<CEGUI::Window*, Dialog*> m_UIDialog;

public:
	CEGUI::LuaScriptModule* GetLuaScriptModule();
	bool IsBagMirrorDlgOpen();
	void CloseDialogBeforeMapChange();
	void CloseDialogBeforeBattle();
	bool OnUIDialogDestructionStarted(const CEGUI::EventArgs &args);
	void RemoveUIDialog(CEGUI::Window* window);
	void AddUIDialog(CEGUI::Window* window, Dialog* dialog);
	bool IsGameUIVisible();
	void SetGameUIVisible(bool b);

	static const CEGUI::String Chinese_Charset;
	static const CEGUI::String UnsignedIntegerValidator;

private:
	void makeMessageTipWnd(std::wstring& messageTip, bool bAddToChat, bool bEnable);
	void resetMessageTips();
	void updateMessageTips(float fDelta);
	bool moveFloatItem(float x, float y);
	bool isEffectWndVis(Nuclear::IEffect* pEffect);
	Nuclear::IEffect* GetEffectBySurface(CEGUI::RenderingSurface* pSurface);

public:
	bool SdkOutTimeOpen(){ m_blnSdkOutTime = true; return true; }
	bool SdkOutTimeClose(){ m_blnSdkOutTime = false; return true; }
	bool OnSpriteWndDestroy(const CEGUI::EventArgs& e);
	bool OnWndMove(const CEGUI::EventArgs& e);
	bool OnWndUpdata(const CEGUI::EventArgs& e);
	bool OnWndDestroy(const CEGUI::EventArgs& e);
	void RenderWindowSprite(XPRenderEffect* pRenderEffect);
	void RenderUIEffect(XPRenderEffect* pRenderEffect);
	bool IsWindowHaveEffect(CEGUI::Window* pWnd);
	void RemoveAllUIEffect();
	CEGUI::Window* GetWndByEffect(Nuclear::IEffect* pEffect);
	void RemoveWindowSpriteByWnd(CEGUI::Window* pWnd);
	void RemoveWindowSprite(UISprite* pSprite);
	void RemoveScreenEffect(Nuclear::IEffect* pEffect);
	void PlayScreenEffect(int effectid, float fx = 0.5, float fy = 0.5, bool delSame = true);
	void RemoveAllBoundsEffect();
	void RemoveBoundsEffect(Nuclear::IEffect* pEffect);
	void RemoveUIEffect(CEGUI::Window* pWnd);
	void RemoveUIEffect(Nuclear::IEffect* pEffect);
	UISprite* AddWindowSprite(CEGUI::Window* pWnd, int modleId, Nuclear::NuclearDirection dir = Nuclear::XPDIR_BOTTOMRIGHT, int x = 0, int y = 0, bool clip = false);
	Nuclear::IEffect* AddUIEffect(CEGUI::Window* pWnd, const std::wstring& strEffectName, bool bCycle = true, int x = 0, int y = 0, bool clip = false);
	void UpdateEffectBoundsPos(Nuclear::IEffect* pEffect, CEGUI::Window* pWnd);
	Nuclear::IEffect* AddParticalEffect(CEGUI::Window *pWnd, const std::wstring& strEffectName, bool bCycle = true, int x = 0, int y = 0, bool clip = false, bool bRoundBounds = false);

private:
	std::map<int, stScreenEffect> m_mapScreenEffect;
	std::map<UISprite*, Nuclear::NuclearLocation> m_mapSpritePos;
	std::map<UISprite*, CEGUI::Window*> m_mapWindowSprite;
	std::map<Nuclear::IEffect*, Nuclear::NuclearLocation> m_mapUIEffectLocation;      //特效位置
	std::map<Nuclear::IEffect*, CEGUI::Window*> m_mapUIEffect;

private:
	CEGUI::String m_sNpcSound;
	bool m_bSuperMapEnable; // 高级藏宝图标志（跳转地图用）
	bool m_bEnableDlgSound;
	int m_iCurItemId;
	int m_iCurNpcId;
	int m_iTreasureMapId;
	bool m_bUIInited;
	bool m_bUIPostInited;
	bool m_bShowGameUI;

public:
	void sdkInit();
	void sdkLogin();
	void sdkExit();

	//易接SDK登陆
	void JYSdkLogin();
	void JYSdkLogout();

	bool HandleConfirmEscapeCancel(const CEGUI::EventArgs& e);
	bool HandleConfirmInitEscape(const CEGUI::EventArgs& e);
	bool HandleConfirmEscape(const CEGUI::EventArgs& e);
	void setTreasureMapId(int id);
	int getTreasureMapId();
	void setCurrentItemId(int id);
	int getCurrentItemId();
	bool isSameNpcSoundPlaying(int iId);
	bool isNpcSoundPlaying();
	CEGUI::String& getCurNpcSound();
	int getCurNpcId();
	void setCurNpcId(int iId);
	void setCurNpcSound(const CEGUI::String& soundRes);
	void PlayNPCSound(const CEGUI::String& soundRes, const int iNpcId, bool bForcePlay = false);
	void PlayUISound(const CEGUI::String& soundRes);
	void EnableDlgSound(bool bEnable);
	bool isEnableDlgSound();
	void setSuperTreasureMapEnable(bool bEnable);
	bool isSuperTreasureMapEnable();
	void SetShowGameUI(bool bSHow);

private:
	std::list<UISprite*>		m_UISprites;
	std::list<UISpineSprite*>	m_UISpineSprites;

public:
	UISprite* FindUISprite(int iHandle);
	UISprite* GetUISprite(int iIndex);
	int GetUISpriteCount();
	void ClearUISprite();
	void RemoveUISprite(UISprite* pUISprite);
	void AddUISprite(UISprite* pUISprite);

	void HideAllUI(bool bHide);

	UISpineSprite* FindUISpineSprite(int iHandle);
	UISpineSprite* GetUISpineSprite(int iIndex);
	int  GetUISpineSpriteCount();
	void ClearUISpineSprite();
	void RemoveUISpineSprite(UISpineSprite* pUISpineSprite);
	bool AddUISpineSprite(UISpineSprite* pUISpineSprite);

public:
	void OnBackSpace();
	void InjectString(const char* pStr);
	void InjectChar(const char* pStr);
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	void AttachWp8IME(const CEGUI::String& str, const CEGUI::Rect& rect);
	void OnClickWp8EditArea(const CEGUI::String& str, const CEGUI::Rect& rect);
#endif
	void SetDetachIMEState();
	float GetCurEditBoxBottom(); //获得当前的编辑区的底部位置
	bool DetachIME();
	void AttachIME(const CEGUI::String& str);
	void OnClickEditArea(const CEGUI::String& str);

	void removeGameUpdateTextView();
	void showGameUpdateTextView();
	void hideGameUpdateTextView();
	void resumeGameUpdateTextView();

	void clearMessages();

	// yeqing 2015-12-14
	virtual void setLoginProgress(bool bLoginProgress) { m_bLoginProgress = bLoginProgress; }
	virtual bool getLoginProgress() { return m_bLoginProgress; }
    
#ifdef TARGET_OS_IPHONE
	cocos2d::extension::CCEditBoxImpl* GetEditBoxImpl() { return m_pEditBoxImpl; }
#endif

private:
	bool m_bAttachIME;
	GameIMEDelegate* m_pUIIMEDelegate;
#ifdef TARGET_OS_IPHONE
    //yangbin---调用ios输入控件
    cocos2d::extension::CCEditBoxImpl* m_pEditBoxImpl;
    GameEditBox* m_pEditBox;
#endif
    
private:
	void drawScreenEffect();
	void updateHideAllUI(unsigned int dtTime);//UI逐渐消失和动画的过程
	void CheckToolTipsRegularDlg(float x, float y, Dialog* Dialog);//new add , 2015-08-08
	bool CheckDialogAncestorInTarget(float x, float y, Dialog* Dialog, CEGUI::Window* mustRespoondAncestorWnd);//new add 2015-07-02
	void CheckNumInputDlg(float x, float y, Dialog* Dialog); //lua注册待定
	void CheckToolTips(float x, float y, Dialog* Dialog);
	void CheckToolTips(float x, float y, CEGUI::Window* pWnd);

	CEGUI::Point m_ptDragBenginPoint;

	CEGUI::Window* m_pFPSLabel;
#if (defined WIN32) && (defined XP_PERFORMANCE)
	CEGUI::Window* ma_pCEGUILabel[20];
#endif

	typedef struct stMessageStruct {
		std::wstring message;
		bool bAddToChat;
		bool bEnable;
	} stMessageStruct;
    
	bool m_bLoginProgress;
	//隐藏所有UI的时刻点
	unsigned int m_StartHideAllUITime;
	unsigned int m_ConsumeHideAllUITime;
	//是否开始显示/隐藏所有UI(不包括电池与wifi)
	bool m_bStartHideAllUI;
	//隐藏所有UI(不包括电池与wifi)
	bool m_bHideAllUI;

	float m_fSdkOutTime;
	bool m_blnSdkOutTime;
	float m_fTipDisplayTime;
	float m_fHeightFactor;
	float m_fMaxHeight;
	std::vector<stMessageStruct> m_vecMessages;

	unsigned int m_drawDeltaTime;
	unsigned int m_updateDeltaTime;
	CEGUI::String m_sKeyboardTargeWndName;
};

#ifdef TARGET_OS_IPHONE
class GameEditBox: public cocos2d::extension::CCEditBoxDelegate
{
public:
    void editBoxEditingDidBegin(cocos2d::extension::CCEditBox* editBox);
    void editBoxEditingDidEnd(cocos2d::extension::CCEditBox* editBox);
    void editBoxTextChanged(cocos2d::extension::CCEditBox* editBox, const std::string& text);
    void editBoxReturn(cocos2d::extension::CCEditBox* editBox);
};
#endif


#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
void ClickWp8EditArea(const CEGUI::String& str, const CEGUI::Rect& rect);
#else
void ClickEditArea(const CEGUI::String& str);
#endif

void GetEmotionParseInf(int id, bool& bExist, bool& bExitstNextChar);
void HandleLinkHttp(const CEGUI::String& httpaddress);
void RestoreXPRenderState(void);
void HandleLinkTextClick(int keyid);
bool HandleCheckShied(CEGUI::String& inText);
void HandleNPCPlaySound(const CEGUI::String& soundRes, const int iNpcId, bool bForcePlay = false);
void HandleUIPlaySound(const CEGUI::String& soundRes);
bool GetTextFromClipBord(CEGUI::String& text);
bool HandleCopyToBord(const CEGUI::String& text);
void HandleAddSelectEffectToItemCell(CEGUI::Window* wnd, bool isSelected);
void HandleAnswerQuestionLinkClick(int questionid, CEGUI::String& name, int roleid, int type);
void HandleJoinTeamLinkClick(int64_t leaderid);
void HandleRequestTeamLinkClick(int64_t teamid); //请求队伍信息 by yq 2016-03-25
bool OnEmotionFrameChange(const CEGUI::EventArgs& e);
void HandleTipsLinkClick(const CEGUI::String& name, int64_t roleID, int type, const CEGUI::String& key, int baseid, int64_t shopID, int counterID, const CEGUI::colour& nameColor, int bind, int64_t loseeffecttime);
void HandleShowRewardItemTips(const int& itemid, float xPos, float yPos);
void HandleGoToFunction(const int& mapid, const int& xPos, const int& yPos, const int& npcid, const int& chefu, int gotodirectly, const int64_t npckey);
void HandleCommonLinkClick(const CEGUI::String &arg);
void HandleOpenDialog(int64_t dialogID);
void HandleRequestAnotherQuest(int64_t questID, const CEGUI::String &title);





inline GameUImanager* gGetGameUIManager()
{
	return GameUImanager::GetInstance();
}

#endif
