#include "LuaTickerRegister.h"
#include "script_support/CCScriptSupport.h"


LuaTickerRegister::LuaTickerRegister():m_iLuaTickerHandler(0)
{ }

LuaTickerRegister::~LuaTickerRegister()
{ }

void LuaTickerRegister::LuaTick(int delta)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeFunctionWithIntegerData(m_iLuaTickerHandler, delta);
}

void LuaTickerRegister::RegisterTickerHandler(int handler)
{
	m_iLuaTickerHandler = handler;
}
