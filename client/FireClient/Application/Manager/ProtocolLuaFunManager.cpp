#include "stdafx.h"
#include "ProtocolLuaFunManager.h"
#include "script_support/CCScriptSupport.h"


namespace Game
{

	ProtocolLuaFunManager::ProtocolLuaFunManager()
		:m_LuaProtocolHandler(0)
	{ }

	ProtocolLuaFunManager::~ProtocolLuaFunManager()
	{ }

	bool ProtocolLuaFunManager::LuaProtocolProcess(aio::LuaProtocol& lp)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeLuaProtocolHandler(m_LuaProtocolHandler, lp);
		return true;
	}

	void ProtocolLuaFunManager::RegisterLuaProtocolHandler(unsigned int handler)
	{
		m_LuaProtocolHandler = handler;
	}

	bool ProtocolLuaFunManager::ProtocolScriptProcess(aio::Protocol& p)
	{
		std::map<unsigned int, unsigned int>::iterator iter = m_MapProtocolScriptHandler.find(p.getType());
		if (iter != m_MapProtocolScriptHandler.end()) {
			return cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeProtocolHandler(iter->second, p);
		}
		return true;
	}

	void ProtocolLuaFunManager::UnRegisterProtocolScriptHandler(unsigned int type)
	{
		m_MapProtocolScriptHandler.erase(type);
	}

	void ProtocolLuaFunManager::RegisterProtocolScriptHandler(unsigned int type, unsigned int handler)
	{
		m_MapProtocolScriptHandler[type] = handler;
	}

}
