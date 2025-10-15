
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class Challenge : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/Challenge.inc"

	void Setup(size_t size)
        {
                GNET::Security *random = GNET::Security::Create(GNET::RANDOM);
                random->Update(nonce.resize(size));
                random->Destroy();
        }

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
	}

};

} 
