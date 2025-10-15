#include <iostream>
#include <unistd.h>
#include <signal.h>
#include <syslog.h>

#include <log4cpp/Category.hh>
#include <log4cpp/PropertyConfigurator.hh>
//#include <mysql++/mysql++.h>
#include "rpcgen.hpp"
#include "mapforbid.h"
#include "keepalive.h"
#include "netplayer.h"
#include "mapusertype.h"
#include "DeliverServer.hpp"
#include "AuthClient.hpp"
#include "GameDBServer.hpp"
#include "CrossServer.hpp"
#include "CrossClient.hpp"
#include "crossdata.hpp"
#include "crossbroadcast.hpp"
#include "AuAnyClient.hpp"
#include "forcelogin.h"
#ifndef USE_AUANY
	#include "mysqlppconn.h"
#endif

using namespace GNET;
static void sig_handler(int);

int gdeliverd::global_userid = 1;

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
		std::string initFileName = "proxy.log4cpp.properties";
		log4cpp::PropertyConfigurator::configure(initFileName);
	}
	catch(log4cpp::ConfigureFailure& f)
	{
		std::cerr << "log4cpp configure problem " << f.what() << std::endl;
		exit(-1);
	}
	log4cpp::Category& root = log4cpp::Category::getRoot();
	log4cpp::Category& log = log4cpp::Category::getInstance(std::string("gdeliverd"));
	log.info("log4cpp init over!");

	Conf *conf = Conf::GetInstance(argv[1]);

#ifdef USE_LOGCLIENT
	Log::setprogname("gdeliverd");
#else
	Log::setprogname(LOG_LOCAL2,"gdeliverd");
#endif
	bool blneedconnAU = false;
#ifdef CROSS_SERVER
	blneedconnAU = false;
	log.info("==========gdeliverd_crossserver setup begin==========");
	{
		gdeliverd::CrossServer *manager = gdeliverd::CrossServer::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		manager->setBlCrossServer(true);
		if(NULL == Protocol::Server(manager))
		{
			log.warn("CrossServer init failed,reason:socket create or port bind failed!");
			log4cpp::Category::shutdown();
			exit(-1);
		}
	}
#else
	{
		log.info("==========gdeliverd setup begin==========");
		gdeliverd::CrossClient *manager = gdeliverd::CrossClient::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		int bl_open = 0;
		bl_open = atoi(conf->find(manager->Identification(),"bl_open").c_str());
		log.info("CrossClient.bl_open=%d",bl_open);
		if(bl_open > 0)
		{
			manager->setBlCrossClient(true);
			if(NULL == Protocol::Client(manager))
			{
				log.warn("CrossClient init failed,reason:socket create failed!");
				log4cpp::Category::shutdown();
				exit(-1);
			}
		}
	}
#endif

#ifdef USE_AUANY
	blneedconnAU = false;
	{
		gdeliverd::AuAnyClient *manager = gdeliverd::AuAnyClient::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		string s = conf->find(manager->Identification(),"serverid");
		manager->setServerId(s.c_str(),s.length());
		int plattype = atoi(conf->find(manager->Identification(),"plattype").c_str());
		manager->setPlatType(plattype);
		log.info("AuAnyClient.plattype=%d,serverid=%.*s",manager->getPlatType(),manager->getServerId().size(),manager->getServerId().begin());
		if(NULL == Protocol::Client(manager))
		{
			log.warn("AuAnyClient init failed,reason:socket create failed!");
			log4cpp::Category::shutdown();
			exit(-1);
		}
	}
#else
    log.info("AuAnyClient NOT pre define!use mysql!");
#endif
    //blneedconnAU = true;
	if(blneedconnAU)
	{
		gdeliverd::AuthClient *manager = gdeliverd::AuthClient::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		manager->SetCertPolicy(atoi(conf->find(manager->Identification(), "use_cert").c_str()));
		log.info("AuthClient.use_cert=%d",manager->GetCertPolicy());
		if(/*!gdeliverd::CrossServer::GetInstance()->getBlCrossServer() && */NULL == Protocol::Client(manager))
		{
			log.warn("AuthClient init failed,reason:socket create failed!");
			log4cpp::Category::shutdown();
			exit(-1);
		}
	}

#ifndef USE_AUANY
	{
		gdeliverd::MysqlppConn *manager = gdeliverd::MysqlppConn::GetInstance();
		manager->InitEnv(
			(char*)conf->find(manager->Identification(),"dbname").c_str(),
			(char*)conf->find(manager->Identification(),"ip").c_str(),
			atoi(conf->find(manager->Identification(),"port").c_str()),
			(char*)conf->find(manager->Identification(),"user").c_str(),
			(char*)conf->find(manager->Identification(),"passwd").c_str(),
			atoi(conf->find(manager->Identification(),"reconninterval").c_str())
			);
		manager->ConnectToMysql();
	}
#endif

	{
		gdeliverd::DeliverServer *manager = gdeliverd::DeliverServer::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		manager->SetZoneid(atoi(conf->find(manager->Identification(), "zoneid").c_str()));
		manager->SetAid(atoi(conf->find(manager->Identification(), "aid").c_str()));
		manager->LimitConfig(atoi(conf->find("SpeedLimit", "window").c_str()),atoi(conf->find("SpeedLimit","high").c_str()));
		manager->LockIpLimitConfig(atoi(conf->find("LockIPLimit", "window").c_str()),atoi(conf->find("LockIPLimit","high").c_str()));
		manager->CouponLimitConfig(atoi(conf->find("CouponLimit", "window").c_str()),atoi(conf->find("CouponLimit","high").c_str()));
		manager->CouponExchangeLimitConfig(atoi(conf->find("CouponExchangeLimit", "window").c_str()),atoi(conf->find("CouponExchangeLimit","high").c_str()));
		manager->AuRequsetLimitConfig(atoi(conf->find("AuRequestLimit", "window").c_str()),atoi(conf->find("AuRequestLimit","high").c_str()));
		int max_player_num = 0;
		int fake_max_player_num = 0;
		max_player_num = atoi(conf->find(manager->Identification(), "max_player_num").c_str());
		fake_max_player_num = atoi(conf->find(manager->Identification(), "fake_max_player_num").c_str());
		gdeliverd::UserContainer::GetInstance()->SetPlayerLimit( max_player_num,fake_max_player_num );
		gdeliverd::Passwd::GetInstance()->SetMaxSize(atoi(conf->find(manager->Identification(), "max_cache_num").c_str()));
		gdeliverd::UserType::GetInstance()->SetMaxSize(atoi(conf->find(manager->Identification(), "max_cache_num").c_str()));
		log.info("DeliverServer.max_player_num=%d",gdeliverd::UserContainer::GetInstance()->GetPlayerLimit());
		log.info("DeliverServer.fake_max_player_num=%d",gdeliverd::UserContainer::GetInstance()->GetFakePlayerLimit());
		log.info("DeliverServer.max_cache_num=%d",gdeliverd::Passwd::GetInstance()->GetCacheLimit());
		int forbid_client_autologin = 0;
		forbid_client_autologin = atoi(conf->find(manager->Identification(), "forbid_client_autologin").c_str());
		if(forbid_client_autologin > 0)
			manager->forbid_client_autologin = true;
		log.info("DeliverServer.forbid_client_autologin=%d",forbid_client_autologin);
		int allow_nickname_contain_account = 0;
		allow_nickname_contain_account = atoi(conf->find(manager->Identification(),"allow_nickname_contain_account").c_str());
		if(allow_nickname_contain_account > 0)
		   	manager->allow_nickname_contain_account = true;
		log.info("DeliverServer.allow_nickname_contain_account=%d",allow_nickname_contain_account);

		int support_forcelogin = 0;
		support_forcelogin = atoi(conf->find(manager->Identification(), "support_forcelogin").c_str());
		if(support_forcelogin > 0)
			manager->setSupportForceLogin(true);
		log.info("DeliverServer.support_forcelogin=%d",support_forcelogin);

		int forcelogin_timeout = 0;
		forcelogin_timeout = atoi(conf->find(manager->Identification(),"forcelogin_timeout").c_str());
		if(forcelogin_timeout > 0)
			manager->setForceLoginTimeout(forcelogin_timeout);
		log.info("DeliverServer.forcelogin_timeout=%d",manager->getForceLoginTimeout());
		if(NULL == Protocol::Server(manager))
		{
			log.warn("DeliverServer init failed,reason:socket create or port bind failed!");
			log4cpp::Category::shutdown();
			exit(-1);
		}
		manager->setBootTime(time(NULL));
		log.info("DeliverServer init success,ip=%s,port=%d", conf->find(manager->Identification(),"address").c_str(), atoi(conf->find(manager->Identification(),"port").c_str()));
	}

	{
		gdeliverd::GameDBServer *manager = gdeliverd::GameDBServer::GetInstance();
		manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
		manager->LimitConfig(atoi(conf->find("InstantAddCash", "window").c_str()),atoi(conf->find("InstantAddCash","high").c_str()));
		if(NULL == Protocol::Server(manager))
		{
			log.warn("GameDBServer init failed,reason:socket create or port bind failed!");
			log4cpp::Category::shutdown();
			exit(-1);
		}
		log.info("GameDBServer init success,ip=%s,port=%d", conf->find(manager->Identification(),"address").c_str(), atoi(conf->find(manager->Identification(),"port").c_str()));

	}

	Thread::Pool::AddTask(PollIO::Task::GetInstance());

	int interval = atoi(conf->find("Intervals","checkforbidmap_interval").c_str());
	interval = interval<60 ? 60 : interval;
	Thread::HouseKeeper::AddTimerTask(gdeliverd::CheckForbidTimer::GetInstance(interval),interval);
	log.info("gdeliverd.checkforbidmap_interval=%d", interval);

	interval = atoi(conf->find("Intervals","keepalive_interval").c_str());
	interval = interval<60 ? 60 : interval;
	Thread::HouseKeeper::AddTimerTask(gdeliverd::KeepAlive::GetInstance(interval),interval);
	log.info("gdeliverd.keepalive_interval=%d", interval);

	interval = atoi(conf->find("Intervals","cross_ticket_alive_second").c_str());
	interval = interval<10 ? 10 : interval;
	gdeliverd::UserIdentityCache::GetInstance()->setCacheMaxTime(interval);
	log.info("gdeliverd.cross_ticket_alive_second=%d", interval);


	GNET::Timer::Attach(gdeliverd::UserIdentityCache::GetInstance());
	GNET::Timer::Attach(gdeliverd::ForceLogin::GetInstance());
#ifdef ALL_GM
	log.info("----- this gdeliverd is gm version,every online user is GM! -----");
#endif

#ifdef CROSS_SERVER
	Thread::HouseKeeper::AddTimerTask(gdeliverd::CrossBroadcast::GetInstance(60),60);
	log.info("==========gdeliverd_crossserver setup success==========");
#else
	log.info("==========gdeliverd setup success==========");
#endif
	Thread::Pool::Run();
	log4cpp::Category::shutdown();
	return 0;
}
static void sig_handler(int signo)
{
	if (signo == SIGUSR2){
		log4cpp::Category::shutdown();
		exit(0);
	}
}

