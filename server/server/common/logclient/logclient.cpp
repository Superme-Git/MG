#include "rpcgen.hpp"
#include <iostream>
#include <unistd.h>
#include "LogClientManager.hpp"
#include "LogClientTcpManager.hpp"

using namespace GNET;

int main(int argc, char *argv[])
{
	if (argc != 2 || access(argv[1], R_OK) == -1)
	{
		std::cerr << "Usage: " << argv[0] << " configurefile" << std::endl;
		exit(-1);
	}

	Conf *conf = Conf::GetInstance(argv[1]);
	Log::setprogname("logclient");
	{
		logclient::LogClientManager *manager = logclient::LogClientManager::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		Protocol::Client(manager);
	}
	{
		logclient::LogClientTcpManager *manager = logclient::LogClientTcpManager::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		Protocol::Client(manager);
	}

	Thread::Pool::AddTask(PollIO::Task::GetInstance());
	Thread::Pool::Run();
	return 0;
}

