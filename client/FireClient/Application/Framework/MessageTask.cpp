#include "MessageTask.h"
#include "ProtocolLuaFunManager.h"

namespace Game
{
	MessageTask::MessageTask(aio::Protocol* p, aio::Protocol::Manager* m, aio::Protocol::Manager::Session::ID i)
		: protocol(p), manager(m), sid(i)
	{ }

	MessageTask::~MessageTask()
    {
        delete this->protocol;
    }
    
    void MessageTask::Run()
    {
        if(this->protocol)
        {
            bool bNeedCToProcess = true;
            if(gGetProtocolLuaFunManager())
            {
               bNeedCToProcess = gGetProtocolLuaFunManager()->ProtocolScriptProcess(*this->protocol);
            }
            if(bNeedCToProcess)
                this->protocol->Process(manager, sid);
        }
    }

}
