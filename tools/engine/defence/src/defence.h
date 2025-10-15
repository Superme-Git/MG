#pragma once

#include <vector>
#include <string>

#include "win32/share.hpp"
#include "../idefence.h"

namespace Defence
{

	class Task
	{
		aio::Protocol::Manager *manager;
		aio::Protocol::Manager::Session::ID sid;
		aio::Protocol *protocol;
	public:
		~Task () { delete protocol; }
		Task(aio::Protocol::Manager *m, aio::Protocol::Manager::Session::ID id, aio::Protocol *p) : manager(m), sid(id), protocol(p) { }
		void Run()
		{
			protocol->Process(manager, sid);
			delete this;
		}
	};

	void PutTask(Task* task);
	IDefence* GetIDefence(void);

}


