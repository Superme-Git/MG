
#pragma once

#include "rpcgen.hpp"
#include "ACReport2.hpp"

#include "../src/defence.h"
#include "../src/pp.h"
#include "../src/infocollection.h"

#include "../src/VMProtectSDK.h"

namespace gnet { 

class ACPP : public aio::Protocol
{
public:
	#include "ACPP.inc"

	virtual void Dispatch(Manager::Session::ID sid, Manager * manager)
	{
		Defence::PutTask(new Defence::Task(manager, sid, this));
	}

	virtual void Process(Manager *manager, Manager::Session::ID sid) 
	{
		VMProtectBeginVirtualization
		if (roleid != Defence::GetIDefence()->GetRoleID())
			return;

		PP::Check(roleid, pptype, seq, key);

		PBYTE buf = NULL;
		int len = Collector::GetInfoPP(&buf);
		Octets oct;

		if (buf && len > 0)
		{
			oct.replace( buf, len );
			ACReport2 report(roleid, oct);
			manager->Send(sid, report);
			delete[] buf;
		}
		VMProtectEnd
	}

};

} 
