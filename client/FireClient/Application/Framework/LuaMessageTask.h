#ifndef FireClient_LuaMessageTask_h
#define FireClient_LuaMessageTask_h

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "authc/os/windows/win32/share.hpp"
#else
#include "authc/os/ios/win32/share.hpp"
#endif

#include "include/protocol.h"
#include "nuiengine.h"

namespace Game
{
    class LuaMessageTask: public Nuclear::INuclearRunnable
    {
    private:
        aio::LuaProtocol* protocol;
    public:
		LuaMessageTask(aio::LuaProtocol* p);
        ~LuaMessageTask();
        virtual void Run();
    };    
}



#endif
