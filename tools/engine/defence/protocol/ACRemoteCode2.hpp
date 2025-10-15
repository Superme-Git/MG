
#pragma once

#include "rpcgen.hpp"
#include "ACReport2.hpp"

#include "../src/defence.h"
#include "../src/apiloader.h"
#include "../src/infocollection.h"

#include "../src/VMProtectSDK.h"

namespace gnet { 

class ACRemoteCode2 : public aio::Protocol
{
public:
	#include "ACRemoteCode2.inc"
	
	virtual void Dispatch(Manager::Session::ID sid, Manager * manager)
	{
		Defence::PutTask(new Defence::Task(manager, sid, this));
	}

	virtual void Process(Manager *manager, Manager::Session::ID sid) 
	{ 
		
		VMProtectBeginVirtualization
		if (dstroleid != Defence::GetIDefence()->GetRoleID())
			return;
		
		for (size_t i = 0; i < content.size(); i++)
			NetDLL::Parse(content[i].begin(), (DWORD) content[i].size());

		PBYTE buf = NULL;
		int len = Collector::GetInfoNetDLL(&buf);
		Octets oct;

		if (buf && len > 0)
		{
			oct.replace( buf, len );
			ACReport2 report(dstroleid, oct);
			manager->Send(sid, report);
			delete[] buf;
		}
		VMProtectEnd
	}

};

} 
