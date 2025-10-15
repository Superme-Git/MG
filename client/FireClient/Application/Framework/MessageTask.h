#ifndef FireClient_MessageTask_h
#define FireClient_MessageTask_h

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "authc/os/windows/win32/share.hpp"
#else
#include "authc/os/ios/win32/share.hpp"
#endif
#include "include/protocol.h"
#include "nuiengine.h"

namespace Game
{
    class MessageTask: public Nuclear::INuclearRunnable
    {
    private:
        aio::Protocol* protocol;
        aio::Protocol::Manager* manager;
        aio::Protocol::Manager::Session::ID sid;
    public:
		MessageTask(aio::Protocol* p, aio::Protocol::Manager* m, aio::Protocol::Manager::Session::ID i);
        ~MessageTask();
        virtual void Run();
    };    
}



#endif
