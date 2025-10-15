#include "stdafx.h"
#include "NewRoleGuideManager.h"
#include "MessageManager.h"
#include "RoleItemManager.h"
#include "MainRoleDataManager.h"

#include "CallLuaUtil.h"

LockEffect::LockEffect()
{ }

LockEffect::~LockEffect()
{ }

void LockEffect::performPostRenderFunctions()
{
    if (!gGetNewRoleGuideManager()->CheckTargetWindow()) {
        gGetNewRoleGuideManager()->FailGuide();
        return;
    }
    gGetNewRoleGuideManager()->ShowLockEffect();
}


NewRoleGuideManager::NewRoleGuideManager()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_GetInstance");
}

NewRoleGuideManager::~NewRoleGuideManager()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_DestroyInstance");
}

bool NewRoleGuideManager::isPlayingAni()
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_IsPlayingAni"))
		{
			return util.getRetBool();
		}
	}

	return false;
}

// 判断战斗中引导是否通过
bool NewRoleGuideManager::isBattleGuideFinsh(int battleId, int roundId)
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(battleId);
		util.addArg(roundId);
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_IsBattleGuideFinsh"))
		{
			return util.getRetBool();
		}
	}

	return true;
}

// 战斗相关引导
void NewRoleGuideManager::GuideStartBattle(int battleId, int roundId)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(battleId);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(roundId);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_GuideStartBattle", 2);
}

//每一级需要的引导
void NewRoleGuideManager::GuideLevel(int level)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(level);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_GuideLevel", 1);
}

bool NewRoleGuideManager::CheckTargetWindow()
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_CheckTargetWindow"))
		{
			return util.getRetBool();
		}
	}

	return false;
}

Nuclear::NuclearFRectt NewRoleGuideManager::GetClickRect(int id)
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(id);
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_GetClickRect"))
		{
			return *(Nuclear::NuclearFRectt*)util.getRetUsertype("Nuclear::NuclearFRectt");
		}
	}

	return Nuclear::NuclearFRectt(0, 0, 0, 0);
}

int NewRoleGuideManager::getCurGuideId()
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_GetCurGuideId"))
		{
			return util.getRetInt();
		}
	}

	return 0;
}

bool NewRoleGuideManager::NeedLockScreen()
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_NeedLockScreen"))
		{
			return util.getRetBool();
		}
	}

	return false;
}

void NewRoleGuideManager::UpdateGuidePaticleEffect(Nuclear::IEffect* pEffect, CEGUI::Window* pWnd)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushUserDataToLuaStack(pEffect);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushUserDataToLuaStack(pWnd);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_UpdateGuidePaticleEffect", 2);
}

void NewRoleGuideManager::RemoveGuidePaticleEffect(Nuclear::IEffect* pEffect)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushUserDataToLuaStack(pEffect);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_RemoveGuidePaticleEffect", 1);
}

void NewRoleGuideManager::FailGuide()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_FailGuide");
}

void NewRoleGuideManager::FinishGuideInBattle()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_FinishGuideInBattle");
}

//锁屏引导完成
void NewRoleGuideManager::FinishGuide()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_FinishGuide");
}

int NewRoleGuideManager::getGuideModel()
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_GetGuideModel"))
		{
			return util.getRetInt();
		}
	}

	return -1;
}

// 锁屏引导是点击无效
bool NewRoleGuideManager::isClickInvalidWhenLockScreen(float x, float y)
{
	bool bClickInvalid = false;
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(x);
		util.addArg(y);
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_IsClickInvalidWhenLockScreen"))
		{
			bClickInvalid = util.getRetBool();
		}
	}

	return bClickInvalid && !gGetMessageManager()->isHaveConfirmBox() && !gGetMessageManager()->isHaveMessageBox();
}

//某个新手引导是否已经完成
bool NewRoleGuideManager::isGuideFinish(int id)
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(id);
		if (util.callLua(pScriptEngine->getLuaState(), "NewRoleGuideManager_IsGuideFinish"))
		{
			return util.getRetBool();
		}
	}

	return false;
}

void NewRoleGuideManager::ShowLockEffect()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("NewRoleGuideManager_ShowLockEffect");
}
