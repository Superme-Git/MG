/***********************************************************************
	filename:	CallLuaUtil.cpp
	author:		Ñî´ºÀ×
***********************************************************************/
#include "stdafx.h"
#include "CallLuaUtil.h"
#include "script_support/CCScriptSupport.h"

void LuaArgUsertype::pushToLua(lua_State* pState) const
 {
	 tolua_pushusertype(pState, mpData, mType);
	 if (mbRegisterGC)
	 {
		 tolua_register_gc(pState, lua_gettop(pState));
	 }
}

CallLuaUtil::~CallLuaUtil()
{
	reset();
}

void CallLuaUtil::addArg(bool val)
{
	LuaArgBool* pArg = new LuaArgBool(val);
	mArgs.push_back(pArg);
}

void CallLuaUtil::addArg(int val)
{
	LuaArgInt* pArg = new LuaArgInt(val);
	mArgs.push_back(pArg);
}

void CallLuaUtil::addArg(int64_t val)
{
	LuaArgInt64* pArg = new LuaArgInt64(val);
	mArgs.push_back(pArg);
}

void CallLuaUtil::addArg(float val)
{
	LuaArgFloat* pArg = new LuaArgFloat(val);
	mArgs.push_back(pArg);
}

void CallLuaUtil::addArg(const char* val)
{
	LuaArgCString* pArg = new LuaArgCString(val);
	mArgs.push_back(pArg);
}

void CallLuaUtil::addArg(void* val, const char* szType, bool bRegGC)
{
	LuaArgUsertype* pArg = new LuaArgUsertype(val, szType, bRegGC);
	mArgs.push_back(pArg);
}

bool CallLuaUtil::callLua(lua_State* pState, const char* fname, int nresults)
{
	mpState = pState;

	mOriginalTop = lua_gettop(mpState);

	lua_getglobal(pState, fname);
	if (lua_isfunction(pState, -1))
	{
		int nArg = pushToLua(pState);
		if (0 == lua_pcall(pState, nArg, nresults, 0))
		{
			return true;
		}
		fprintf(stderr, "call lua func %s failed.\n", fname);
		lua_pop(pState, 1);
	}
	return false;
}

bool CallLuaUtil::callLua(const char* fname, int nresults)
{
	cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
	if (pScriptEngine && pScriptEngine->getLuaState())
	{
		return callLua(pScriptEngine->getLuaState(), fname, nresults);
	}
	return false;
}

bool CallLuaUtil::getRetBool(bool def)
{
	tolua_Error tolua_err;
	if (tolua_isboolean(mpState, -1, def, &tolua_err))
	{
		bool ret = 0 != tolua_toboolean(mpState, -1, def);
		lua_pop(mpState, 1);
		return ret;
	}
	return def;
}

int64_t CallLuaUtil::getRetInt(int64_t def)
{
	tolua_Error tolua_err;
	if (tolua_isnumber(mpState, -1, def, &tolua_err))
	{
		int64_t ret = tolua_tonumber(mpState, -1, def);
		lua_pop(mpState, 1);
		return ret;
	}
	return def;
}

float CallLuaUtil::getRetFloat(float def)
{
	tolua_Error tolua_err;
	if (tolua_isnumber(mpState, -1, def, &tolua_err))
	{
		float ret = tolua_tonumber(mpState, -1, def);
		lua_pop(mpState, 1);
		return ret;
	}
	return def;
}

const char* CallLuaUtil::getRetCString(const char* def)
{
	tolua_Error tolua_err;
	if (tolua_isstring(mpState, -1, 0, &tolua_err))
	{
		const char* ret = tolua_tostring(mpState, -1, def);
		lua_pop(mpState, 1);
		return ret;
	}
	return def;
}

void* CallLuaUtil::getRetUsertype(const char* szType)
{
	tolua_Error tolua_err;
	if (tolua_isusertype(mpState, -1, szType, 0, &tolua_err))
	{
		void* pRet = tolua_tousertype(mpState, -1, NULL);
		lua_pop(mpState, 1);
		return pRet;
	}
	return NULL;
}

void CallLuaUtil::reset()
{
	for (size_t i = 0; i < mArgs.size(); ++i)
	{
		LuaArg* pArg = mArgs[i];
		if (pArg) delete pArg;
	}
	mArgs.clear();

	if (mpState)
	{
		lua_settop(mpState, mOriginalTop);
	}

	mpState = NULL;
}

int CallLuaUtil::pushToLua(lua_State* pState)
{
	for (size_t i = 0; i < mArgs.size(); ++i)
	{
		LuaArg* pArg = mArgs[i];
		pArg->pushToLua(pState);
	}

	return (int)mArgs.size();
}
