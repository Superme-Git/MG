#include "LuaMessageTask.h"
#include "ProtocolLuaFunManager.h"

namespace Game
{
	LuaMessageTask::LuaMessageTask(aio::LuaProtocol* p)
		:protocol(p)
	{ }

	LuaMessageTask::~LuaMessageTask()
    {
        delete this->protocol;
    }
    
    void LuaMessageTask::Run()
    {
        try
        {
            if(this->protocol)
            {
                if(gGetProtocolLuaFunManager())
                {
                    gGetProtocolLuaFunManager()->LuaProtocolProcess(*this->protocol);
                }
            }
        } 
        catch (aio::ProtocolException & ex)
        {
			throw ex;
        }
        catch (std::exception & ex)
        {
			throw aio::ProtocolException(protocol->type, protocol->data.size(), ex.what());
        }
        catch (FireNet::Marshal::Exception & )
        {
			throw aio::ProtocolException(protocol->type, protocol->data.size(), "NuclearMarshal::Exception");
        }
    }

}//namespace Game
