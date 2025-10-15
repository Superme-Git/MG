#include "rpcgen.hpp"
#include <iostream>
#include <unistd.h>
#include <signal.h>
#include <log4cpp/Category.hh>
#include <log4cpp/PropertyConfigurator.hh>
#include "LinkServer.hpp"
#include "ProviderServer.hpp"
#include "DeliveryClient.hpp"
#include "checktimer.hpp"
#include "stat.hpp"

using namespace GNET;
static void sig_handler(int);

int main(int argc, char *argv[])
{	
	if (argc != 2 || access(argv[1], R_OK) == -1)
	{
		std::cerr << "Usage: " << argv[0] << " configurefile" << std::endl;
		exit(-1);
	}
	if (signal(SIGUSR2, sig_handler) == SIG_ERR)
		fprintf(stderr, "can't register sig handler!\n");
	
	try
	{
		std::string initFileName = "gate.log4cpp.properties";
		log4cpp::PropertyConfigurator::configure(initFileName);
	}
	catch(log4cpp::ConfigureFailure& f)
	{
		std::cerr << "log4cpp configure problem " << f.what() << std::endl;
		exit(-1);
	}
	log4cpp::Category& root = log4cpp::Category::getRoot();
	log4cpp::Category& log = log4cpp::Category::getInstance(std::string("glinkd"));
	log.info("log4cpp init over!");
	
	Conf *conf = Conf::GetInstance(argv[1]);
	Log::setprogname("glinkd");
	{
		log.info("==========glinkd setup begin==========");
		glinkd::LinkServer *manager = glinkd::LinkServer::GetInstance();
		GNET::Timer::Attach(manager);//for linkserver.update
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		manager->SetVersion(atoi(conf->find(manager->Identification(),"version").c_str()));
		manager->SetSessionStateCheck(atoi(conf->find(manager->Identification(),"session_state_check").c_str()));
		manager->SetKeepAliveOpen(atoi(conf->find(manager->Identification(),"keepalive_open").c_str()));
		manager->SetKeepAliveInterval(atoi(conf->find(manager->Identification(),"keepalive_interval").c_str()));
		manager->SetForbidIpInterval(atoi(conf->find(manager->Identification(),"forbid_ip_interval").c_str()));
		manager->SetCheckPolicy(atoi(conf->find(manager->Identification(),"checkunknownprotocol").c_str()));
		manager->SetLogLevel(atoi(conf->find(manager->Identification(),"log_level").c_str()));
		manager->SetUserCountLimit(atoi(conf->find(manager->Identification(), "max_users").c_str()));
		manager->SetHalfLoginLimit(atoi(conf->find(manager->Identification(), "halflogin_users").c_str()));
		manager->LimitConfig(atoi(conf->find("SpeedLimit", "window").c_str()),atoi(conf->find("SpeedLimit","high").c_str()));
		manager->SetStatOpen(atoi(conf->find(manager->Identification(),"stat_open").c_str()));
		manager->SetListenPolicy(atoi(conf->find(manager->Identification(),"listen_port_open").c_str()));

		if(manager->GetCheckPolicy())
		{
			manager->ReloadSizePolicy();
			log.info("LinkServer load unknownprotocol from sizepolicy.conf");
		}
		log.info("LinkServer.checkunknownprotocol=%d,listen_port_open=%d", manager->GetCheckPolicy(), manager->GetListenPolicy());
		log.info("LinkServer.session_state_check=%d", manager->GetSessionStateCheck());
		log.info("LinkServer.keepalive_open=%d,keepalive_interval=%d", manager->GetKeepAliveOpen(), manager->GetKeepAliveInterval());
		log.info("LinkServer.max_user=%d,halflogin_limit=%d", manager->GetUserCountLimit(), manager->GetHalfLoginLimit());
		if(manager->GetListenPolicy())
		{
			manager->StartListen();
			log.info("LinkServer init success,ip=%s,port=%d.", conf->find(manager->Identification(),"address").c_str(), atoi(conf->find(manager->Identification(),"port").c_str()));
		}
	}
	
	{
		glinkd::ProviderServer *manager = glinkd::ProviderServer::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		if(Protocol::Server(manager) == NULL)
		{
			log.error("ProviderServer init failed,reason:socket create or port bind failed!");
			log4cpp::Category::shutdown();
			exit(-1);
		}
		log.info("ProviderServer init success,ip=%s,port=%d.",conf->find(manager->Identification(),"address").c_str(),atoi(conf->find(manager->Identification(),"port").c_str()));
		manager->setLinkId(atoi(conf->find(manager->Identification(), "linkid").c_str()));
	}
	
	{
		glinkd::DeliveryClient *manager = glinkd::DeliveryClient::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		if(Protocol::Client(manager) == NULL)
		{
			log.error("DeliveryClient init failed,reason:socket create failed!");
			log4cpp::Category::shutdown();
			exit(-1);
		}
		log.info("DeliveryClient init success.");
	}
	log.info("==========glinkd setup success==========");
	if(glinkd::LinkServer::GetInstance()->GetKeepAliveOpen())
		Thread::HouseKeeper::AddTimerTask(glinkd::CheckTimer::GetInstance(15),60);
	if(glinkd::LinkServer::GetInstance()->GetStatOpen())
		Thread::HouseKeeper::AddTimerTask(glinkd::Stat::GetInstance(100),100);
	Thread::Pool::AddTask(PollIO::Task::GetInstance());
	Thread::Pool::Run();
	log.info("==========glinkd exit==========");
	log4cpp::Category::shutdown();
	return 0;
}

static void sig_handler(int signo)
{
	if (signo == SIGUSR2)
	{
		GNET::Log::log(LOG_INFO,"glinkd exit because receive SIGUSR2 signal -------!");
		log4cpp::Category::shutdown();
		exit(0);
	}
} 
