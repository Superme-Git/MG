
#include "stdafx.h"
#include "MainRoleDataManager.h"
#include "CCLuaEngine.h"

//这里战斗宠物属性附加值
void CMainRoleDataManager::UpdateMainPetAttribute(std::map<int, int>& attribute)
{
	lua_State* L = cocos2d::gGetScriptEngine()->getLuaState();
	lua_newtable(L);
	int top = lua_gettop(L);
	for (std::map<int, int>::iterator it = attribute.begin(); it != attribute.end(); it++)
	{
		lua_pushinteger(L, it->first);
		lua_pushinteger(L, it->second);
		lua_settable(L, top);
	}
	cocos2d::gGetScriptEngine()->executeGlobalFunction("MainRoleData_UpdateMainPetAttribute", 1);
}

void CMainRoleDataManager::UpdateMainBattlerAttribute(std::map<int, int>& attribute)
{
	lua_State* L = cocos2d::gGetScriptEngine()->getLuaState();
	lua_newtable(L);
	int top = lua_gettop(L);
	for (std::map<int, int>::iterator it = attribute.begin(); it != attribute.end(); it++)
	{
		lua_pushinteger(L, it->first);
		lua_pushinteger(L, it->second);
		lua_settable(L, top);
	}
	cocos2d::gGetScriptEngine()->executeGlobalFunction("MainRoleData_UpdateMainBattlerAttribute", 1);
}



//attrType: fire::pb::attr::AttrType
int GetMainRoleDataAttr(int attrType)
{
	return cocos2d::gGetScriptEngine()->executeGlobalFunctionWithIntegerData("MainRoleData_getAttr", attrType);
}

std::string GetMainRoleDataStrValue(const char *name)
{
	CallLuaUtil util;
	util.addArg(name);
	if (util.callLua("MainRoleData_getStrValue"))
	{
		return util.getRetCString();
	}
	return "";
}