#ifndef __PROTOCOLLUAFUNHANDLER_H
#define __PROTOCOLLUAFUNHANDLER_H

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "authc/os/windows/win32/share.hpp"
#else
#include "authc/os/ios/win32/share.hpp"
#endif

#include "include/protocol.h"

#include "Singleton.hpp"

namespace Game
{
    class ProtocolLuaFunManager:public CSingleton<ProtocolLuaFunManager>
    {
    public:
		ProtocolLuaFunManager();
		~ProtocolLuaFunManager();
        
    public:
		bool LuaProtocolProcess(aio::LuaProtocol& lp);
		void RegisterLuaProtocolHandler(unsigned int handler);
        
		bool ProtocolScriptProcess(aio::Protocol& p);
		void RegisterProtocolScriptHandler(unsigned int type, unsigned int handler);
		void UnRegisterProtocolScriptHandler(unsigned int type);

	private:
        unsigned int m_LuaProtocolHandler;
		std::map<unsigned int, unsigned int> m_MapProtocolScriptHandler;
	};
    
    inline ProtocolLuaFunManager* gGetProtocolLuaFunManager()
    {
        return ProtocolLuaFunManager::GetInstance();
    }
}

#endif
