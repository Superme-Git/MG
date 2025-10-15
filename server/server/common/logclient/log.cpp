#include <stdio.h>

#include "statistic.h"
#include "log.h"

#ifdef USE_LOG2
#include "logger.hpp"
#endif

#include "octets.h"
#include "conf.h"
#include "thread.h"
#include "LogClientManager.hpp"
#include "LogClientTcpManager.hpp"
#include "protocols.hpp"

namespace GNET
{
	std::string g_hostname;
	std::string g_progname;

	void Log::setprogname( const char * __name )
	{
		instance().m_progname = __name;
		instance().LogFacility = LOG_LOCAL0;

		openlog( __name, LOG_CONS | LOG_PID, LOG_LOCAL0 );

		char buffer[256];
		memset( buffer, 0, sizeof(buffer) );
		gethostname( buffer, sizeof(buffer)-1 );
		g_hostname = buffer;
		g_progname = __name;

		/*
		Conf *conf = Conf::GetInstance();
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
		*/
	#ifdef USE_LOG2
		LOG::Log_Init();
	#endif
	}

	void Log::vlog( int __priority, const char * __fmt, va_list ap )
	{
		char buffer[1024];
		int len = vsnprintf( buffer, sizeof(buffer)-1, __fmt, ap );
		if( len > 0 )
		{
			gnet::RemoteLog rmtlog( __priority, Octets(buffer,len), Octets(0), Octets(0));
			buffer[len] = 0;
			if( logclient::LogClientManager::GetInstance()->SendProtocol(rmtlog) )
			{
				if( Thread::Pool::Size() > 1 )
					PollIO::WakeUp();
			}
			else
			{
				Log::vsyslog( __priority, buffer, len );
			}
		#ifdef USE_LOG2
			LOG::Log(rmtlog, true);
		#endif
		}
	}

	void Log::vlogvital( int __priority, const char * __fmt, va_list ap )
	{
		char buffer[1024];
		int len = vsnprintf( buffer, sizeof(buffer)-1, __fmt, ap );
		if( len > 0 )
		{
			//gnet::RemoteLogVital rmtlog( __priority, Octets(buffer,len), Octets(g_hostname.c_str(),g_hostname.length()), Octets(g_progname.c_str(),g_progname.length()));
			gnet::RemoteLogVital rmtlog( __priority, Octets(buffer,len), Octets(0), Octets(0));
			buffer[len] = 0;
			if( logclient::LogClientTcpManager::GetInstance()->SendProtocol(rmtlog) )
			{
				if( Thread::Pool::Size() > 1 )
					PollIO::WakeUp();
			}
			else
			{
				Log::vsyslog( __priority, buffer, len );
			}
		#ifdef USE_LOG2
			LOG::Log(rmtlog, true);
		#endif
		}
	}

	void Log::vstatinfo( int __priority, const char * __fmt, va_list ap )
	{
		char buffer[1024];
		int len = vsnprintf( buffer, sizeof(buffer)-1, __fmt, ap );
		if( len > 0 )
		{
			gnet::StatInfoVital rmtlog( __priority, Octets(buffer,len), Octets(0), Octets(0));
			buffer[len] = 0;

			if( logclient::LogClientTcpManager::GetInstance()->SendProtocol(rmtlog) )
			{
				if( Thread::Pool::Size() > 1 )
					PollIO::WakeUp();
			}
			else
			{
				Log::vsyslog( __priority, buffer, len );
			}
		#ifdef USE_LOG2
			LOG::Log(rmtlog, true);
		#endif
		}
	}

}


