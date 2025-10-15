#include "stdafx.h"

#include "RoleItemManager.h"
#include "ProtoDef/rpcgen/fire/pb/game/MoneyType.hpp"

#include "CallLuaUtil.h"

ItemIDParameter::ItemIDParameter()
{
	itemkey = 0;
	itembaseid = 0;
	bagid = 0;
}

ItemIDParameter::ItemIDParameter(int key, int id, int bagtype)
{
	itemkey = key;
	itembaseid = id;
	bagid = bagtype;
}

RoleItemManager::RoleItemManager()
{
	//cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require 'logic.item.roleitemmanager'.getInstance()");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("RoleItemManager_GetInstance");
	m_pLuaItemNumChangeNotify = NULL;
};

RoleItemManager::~RoleItemManager()
{
	//cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require 'logic.item.roleitemmanager'.destroyInstance()");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("RoleItemManager_DestroyInstance");

	if (m_pLuaItemNumChangeNotify != NULL) {
		delete m_pLuaItemNumChangeNotify;
		m_pLuaItemNumChangeNotify = NULL;
	}
}

int64_t RoleItemManager::GetPackMoney()
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "RoleItemManager_GetPackMoney"))
		{
			return util.getRetInt();
		}
	}

	return 0;
}

int64_t RoleItemManager::GetReserveMoney()
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "RoleItemManager_GetReserveMoney"))
		{
			return util.getRetInt();
		}
	}

	return 0;
}

//判断包裹栏是否满了
bool RoleItemManager::IsBagFull(int badid)
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(badid);
		if (util.callLua(pScriptEngine->getLuaState(), "RoleItemManager_IsBagFull"))
		{
			return util.getRetBool();
		}
	}

	return true;
}

CEGUI::ItemCell* RoleItemManager::GetItemCellAtBag(int baseID)
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(baseID);
		if (util.callLua(pScriptEngine->getLuaState(), "RoleItemManager_GetItemCellAtBag"))
		{
			return (CEGUI::ItemCell*)util.getRetUsertype("CEGUI::ItemCell");
		}
	}

	return NULL;
}

CEGUI::ItemCell* RoleItemManager::GetItemCellAtQuestBag(int baseID)
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		util.addArg(baseID);
		if (util.callLua(pScriptEngine->getLuaState(), "RoleItemManager_GetItemCellAtQuestBag"))
		{
			return (CEGUI::ItemCell*)util.getRetUsertype("CEGUI::ItemCell");
		}
	}

	return NULL;
}

bool RoleItemManager::DestroyItem()
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine)
	{
		CallLuaUtil util;
		if (util.callLua(pScriptEngine->getLuaState(), "RoleItemManager_DestroyItem"))
		{
			return util.getRetBool();
		}
	}

	return false;
}

/*****************************************************************************/

void RoleItemManager::ClearBag(int bagid)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("RoleItemManager_ClearBag", bagid);
}

void RoleItemManager::CheckEquipEffect()
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("RoleItemManager_CheckEquipEffect");
}

/*****************************************************************************/

//物品数量变化
void RoleItemManager::FireItemNumChange(const ItemIDParameter &data)
{
	m_ItemNumChangeNotifySet.FireNotify(&ItemNumChangeNotify::OnItemNumChangeNotify, data);
}

bool RoleItemManager::InsertItemNumChangeNotify(ItemNumChangeNotify& notiy)
{
	return m_ItemNumChangeNotifySet.InsertNotify(notiy);
}

bool RoleItemManager::RemoveItemNumChangeNotify(ItemNumChangeNotify& notiy)
{
	return m_ItemNumChangeNotifySet.RemoveNotify(notiy);
}

int RoleItemManager::InsertLuaItemNumChangeNotify(int handler) {
	if (m_pLuaItemNumChangeNotify == NULL) {
		m_pLuaItemNumChangeNotify = new LuaItemNumChangeNotify;
	}
	return m_pLuaItemNumChangeNotify->InsertItemNumChangeNotify(handler);
}

void RoleItemManager::RemoveLuaItemNumChangeNotify(int handler) {
	if (m_pLuaItemNumChangeNotify) {
		m_pLuaItemNumChangeNotify->RemoveItemNumChangeNotify(handler);
	}
}

void RoleItemManager::ClearItemNumChangeNotify()
{
	m_ItemNumChangeNotifySet.Clear();
	if (m_pLuaItemNumChangeNotify != NULL) {
		delete m_pLuaItemNumChangeNotify;
		m_pLuaItemNumChangeNotify = NULL;
	}
}

void LuaItemNumChangeNotify::OnItemNumChangeNotify(const ItemIDParameter &data) {
	cocos2d::CCScriptEngineProtocol* engine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (engine) {
	for (std::vector<int>::iterator iter = m_handlers.begin(); iter != m_handlers.end(); iter++) {
			engine->executeFunctionWithParamsData(*iter, data.bagid, data.itemkey, data.itembaseid);
	}
	}
}

void LuaItemNumChangeNotify::RemoveItemNumChangeNotify(int handler) {
	for (std::vector<int>::iterator iter = m_handlers.begin(); iter != m_handlers.end(); iter++) {
		if (*iter == handler) {
			luaL_unref(cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->getLuaState(), LUA_REGISTRYINDEX, *iter);
			m_handlers.erase(iter);
			break;
		}
	}
}

int LuaItemNumChangeNotify::InsertItemNumChangeNotify(int handler) {
	m_handlers.push_back(handler);
	return handler;
}

LuaItemNumChangeNotify::~LuaItemNumChangeNotify() {
		for (std::vector<int>::iterator iter = m_handlers.begin(); iter != m_handlers.end(); iter++) {
		luaL_unref(cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->getLuaState(), LUA_REGISTRYINDEX, *iter);
		}
	m_handlers.clear();
}

ItemNumChangeNotify::ItemNumChangeNotify()
{
	gGetRoleItemManager()->InsertItemNumChangeNotify(*this);
}

ItemNumChangeNotify::~ItemNumChangeNotify()
{
	gGetRoleItemManager()->RemoveItemNumChangeNotify(*this);
}
