/*
 * implementing log.
 */

#ifndef __GNET_LOG_H__
#define __GNET_LOG_H__

#ifdef _MSC_VER

#define LOG_EMERG       0       /* system is unusable */
#define LOG_ALERT       1       /* action must be taken immediately */
#define LOG_CRIT        2       /* critical conditions */
#define LOG_ERR         3       /* error conditions */
#define LOG_WARNING     4       /* warning conditions */
#define LOG_NOTICE      5       /* normal but significant condition */
#define LOG_INFO        6       /* informational */
#define LOG_DEBUG       7       /* debug-level messages */

namespace GNET
{
	class Log
	{
	public:
		static void log( int __priority, const char * __fmt, ... )
		{
		}
	};
}

#else

#include <stdio.h>
#include <syslog.h>
#include <stdarg.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/un.h>
#include <errno.h>

#include <ios>
#include <iostream>
#include <fstream>
#include <string>

#include "timer.h"
#include "statistic.h"
#include "octets.h"

//LOG_DEBUG=7
#define LOG_CHAT	8

namespace GNET
{
	class Log
	{
	private:
		const char * m_progname;
		bool	connected;
		int		LogFile;
		int		LogFacility;

		Log() : connected(false), LogFile(-1), LogFacility(LOG_USER) { }

		virtual ~Log()
		{
			if( connected )
				close( LogFile );

			closelog();
		}

		static Log & instance( )
		{
			static Log s_log;
			static Log::HouseKeeper s_housekeeper;
			return s_log;
		}

		static bool logstatistic( const std::string & __name, const Statistic * __pstat )
		{
			int priority = LOG_DEBUG;
			if( Statistic::hour == __pstat->m_interval )
				priority = LOG_INFO;
			else if( Statistic::day == __pstat->m_interval )
				priority = LOG_NOTICE;
			statinfo( priority, "STAT:%s=%lld:%lld:%lld:%lld:%lld",
					__name.c_str(), __pstat->m_max, __pstat->m_min,
					__pstat->m_cur, __pstat->m_cnt, __pstat->m_sum );
			return true;
		}

		static void statenumerate( Statistic::StatInterval __interval )
		{
			closelog();
			openlog( instance().m_progname, LOG_CONS | LOG_PID, LOG_LOCAL1 );
			instance().LogFacility = LOG_LOCAL1;

			Statistic::enumerate( logstatistic, __interval );

			closelog();
			openlog( instance().m_progname, LOG_CONS | LOG_PID, LOG_LOCAL0 );
			instance().LogFacility = LOG_LOCAL0;
		}

		static void statinfo( int __priority, const char * __fmt, ... )
		{
			va_list ap;
			va_start(ap,__fmt);
			Log::vstatinfo( __priority, __fmt, ap );
			va_end(ap);
		}

		class HouseKeeper : public Timer::Observer
		{
		private:
			Timer	timer;
			long	t_hour_last;
			long	t_day_last;

		public:
			HouseKeeper() : t_hour_last(-1), t_day_last(-1) { Timer::Attach(this); }
    
			void Update()
			{
				if( timer.Elapse() >= 299 )
				{
					int seconds = Timer::GetTime() % 300;
					if( seconds < 180 || seconds > 210 )
						return;
					timer.Reset();

					// min5
					statenumerate( Statistic::min5 );
					// cricket use COUNTER, needn't reset
					// Statistic::resetall( );

					// hour
					time_t now = Timer::GetTime();
					if( t_hour_last < 0 || now - t_hour_last > 3000 )
					{
						struct tm * t = localtime( &now );
						if( t->tm_min >= 0 && t->tm_min <= 8 )
						{
							statenumerate( Statistic::hour );
							statenumerate( Statistic::day );
							t_hour_last = now;
						}
					}
					/*
					// day
					if( t_day_last < 0 || now - t_day_last > 85000 )
					{
						struct tm * t = localtime( &now );
						if( 3 == t->tm_hour && t->tm_min >= 25 && t->tm_min <= 33 )
						{
							statenumerate( Statistic::day );
							t_day_last = now;
						}
					}
					*/
				}
			}
		};

		static void open_internal( )
		{
			if( !instance().connected )
			{
				struct sockaddr_un addr;
				memset(&addr,0,sizeof(addr));
				addr.sun_family = AF_UNIX;
				strncpy(addr.sun_path, "/dev/log", sizeof(addr.sun_path)-1 );

				if( -1 == (instance().LogFile = socket(PF_UNIX, SOCK_DGRAM, 0)) )
					return;

				int len = 256*1024;
				setsockopt( instance().LogFile, SOL_SOCKET, SO_SNDBUF, (const void*)&len, sizeof(len) );

				fcntl( instance().LogFile, F_SETFD, FD_CLOEXEC );
				if( -1 == connect(instance().LogFile, (const sockaddr*)&addr, sizeof(addr) ) )
				{
					close(instance().LogFile);
					return;
				}

				instance().connected = true;
			}
		}

		static void vsyslog( int pri, const char * fmt, va_list ap )
		{
			char buffer[1024];
			pri |= instance().LogFacility;
			int len1 = snprintf( buffer, sizeof(buffer)-1, "<%d>%s[%d]: ",
								 pri, instance().m_progname, getpid() );
			if( len1 <= 0 )	return;
			int len2 = vsnprintf( buffer+len1, sizeof(buffer)-1-len1, fmt, ap );
			if( len2 <= 0 )	return;
			int len = len1 + len2;
			buffer[len] = 0;

			/* connect and send */
			int flags = MSG_NOSIGNAL;
			if( pri > LOG_WARNING )
				flags |= MSG_DONTWAIT;
			open_internal( );
			if( send(instance().LogFile, buffer, len, flags ) == -1
				&& EPIPE == errno )
			{
				instance().connected = false;
				close(instance().LogFile);
				open_internal( );
				send(instance().LogFile, buffer, len, flags );
			}
		}

	public:
		static const char * prioritystring( int __priority )
		{
			static char * sz_emerg   = "emerg";
			static char * sz_alert   = "alert";
			static char * sz_crit    = "crit";
			static char * sz_err     = "err";
			static char * sz_warning = "warning";
			static char * sz_notice  = "notice";
			static char * sz_info    = "info";
			static char * sz_debug   = "debug";
			static char * sz_chat    = "chat";

			switch( __priority )
			{
			case LOG_CHAT:
				return sz_chat;

			case LOG_DEBUG:
				return sz_debug;
			case LOG_INFO:
				return sz_info;
			case LOG_NOTICE:
				return sz_notice;
			case LOG_WARNING:
				return sz_warning;
			case LOG_ERR:
				return sz_err;

			case LOG_CRIT:
				return sz_crit;
			case LOG_ALERT:
				return sz_alert;
			case LOG_EMERG:
				return sz_emerg;
			default:
				return sz_info;
			}
		}

#ifdef USE_LOGCLIENT
		static void setprogname( const char * __name );
		static void vlog( int __priority, const char * __fmt, va_list ap );
		static void vlogvital( int __priority, const char * __fmt, va_list ap );
		static void vstatinfo( int __priority, const char * __fmt, va_list ap );
#else
		static void setprogname( const char * __name )
		{
			instance().m_progname = __name;
			instance().LogFacility = LOG_LOCAL0;

			openlog( __name, LOG_CONS | LOG_PID, LOG_LOCAL0 );

			// static std::ofstream fs_log;
			// fs_log.open( "/tmp/gnet.log", std::ios::app );
			// std::cerr.rdbuf(fs_log.rdbuf());
			// std::cout.rdbuf(fs_log.rdbuf());

			// freopen( "/tmp/gnet.log", "a", stderr );
			// freopen( "/tmp/gnet.log", "a", stdout );
		}
		static void vlog( int __priority, const char * __fmt, va_list ap )
		{
			Log::vsyslog( __priority, __fmt, ap );
		}
		static void vlogvital( int __priority, const char * __fmt, va_list ap )
		{
			Log::vsyslog( __priority, __fmt, ap );
		}
		static void vstatinfo( int __priority, const char * __fmt, va_list ap )
		{
			Log::vsyslog( __priority, __fmt, ap );
		}
#endif

		static void log( int __priority, const char * __fmt, ... )
		{
			va_list ap;
			va_start(ap,__fmt);

			fprintf( stderr, "%s : ", Log::prioritystring(__priority) );
			vfprintf( stderr, __fmt, ap );

			size_t len = (__fmt ? strlen(__fmt) : 0);
			if( len > 0 && __fmt[len-1] != '\n' )
				fprintf( stderr, "\n" );

			Log::vlogvital( __priority, __fmt, ap );
			va_end(ap);
		}

		static void logvital( int __priority, const char * __fmt, ... )
		{
			va_list ap;
			va_start(ap,__fmt);

			fprintf( stderr, "%s : ", Log::prioritystring(__priority) );
			vfprintf( stderr, __fmt, ap );

			size_t len = (__fmt ? strlen(__fmt) : 0);
			if( len > 0 && __fmt[len-1] != '\n' )
				fprintf( stderr, "\n" );

			Log::vlogvital( __priority, __fmt, ap );
			va_end(ap);
		}

		static void trace( const char * __fmt, ... )
		{
			va_list ap;
			va_start(ap,__fmt);

			fprintf( stderr, "TRACE : " );
			vfprintf( stderr, __fmt, ap );

			size_t len = (__fmt ? strlen(__fmt) : 0);
			if( len > 0 && __fmt[len-1] != '\n' )
				fprintf( stderr, "\n" );

			Log::vlog( LOG_DEBUG, __fmt, ap );
			va_end(ap);
		}

	public:
		static void formatlog( const char * title, const char * __fmt, ... )
		{
			va_list ap;
			va_start(ap,__fmt);

			char buffer[1024];
			int len1 = snprintf(buffer, sizeof(buffer)-1, "formatlog:%s:", title );
			if( len1 <= 0 )	return;
			int len2 = vsnprintf( buffer+len1, sizeof(buffer)-1-len1, __fmt, ap );
			if( len2 < 0 )	return;
			int len = len1 + len2;
			buffer[len] = 0;

			Log::logvital( LOG_NOTICE, buffer );
			va_end(ap);
		}

		static void task( int roleid, int taskid, int type, const char * msg )
		{
			logvital( LOG_NOTICE, "formatlog:task:roleid=%d:taskid=%d:type=%d:msg=%s",
							roleid, taskid, type, msg );
		}

		static void accounting( int userid, int type )
		{
			logvital( LOG_NOTICE, "formatlog:accounting:userid=%d:type=%d", userid, type );
		}

		static void login( Octets account, int userid, unsigned int sid, std::string peer )
		{
			char buf[128];
			int len = std::min(account.size(),sizeof(buf)-1);
			memcpy( buf, (char*)account.begin(), len );
			buf[len] = 0;
			logvital( LOG_NOTICE, "formatlog:login:account=%s:userid=%d:sid=%u:peer=%s",
							buf, userid, sid, peer.c_str() );
		}

		static void logout( Octets account, int userid, unsigned int sid, std::string peer, int time )
		{
			char buf[128];
			int len = std::min(account.size(),sizeof(buf)-1);
			memcpy( buf, (char*)account.begin(), len );
			buf[len] = 0;
			logvital( LOG_NOTICE, "formatlog:logout:account=%s:userid=%d:sid=%u:peer=%s:time=%d",
							buf, userid, sid, peer.c_str(), time );
		}

		static void rolelogin( int roleid )
		{
			logvital( LOG_NOTICE, "formatlog:rolelogin:roleid=%d", roleid );
		}

		static void rolelogout( int roleid, int time )
		{
			logvital( LOG_NOTICE, "formatlog:rolelogout:roleid=%d:time=%d", roleid, time );
		}

		static void gmoperate( int userid, int type, std::string content )
		{
			logvital(LOG_NOTICE, "formatlog:gmoperate:userid=%d:type=%d:content=%s",
							userid, type, content.c_str());
		}

		struct trade_object
		{
			int id;
			int pos;
			int count;
			trade_object(int _id,int _pos,int _c) : id(_id), pos(_pos), count(_c) { }
		};
		typedef std::vector<struct trade_object>	ObjectVector;
		static void trade( int roleidA, int roleidB, unsigned int moneyA, unsigned int moneyB,
						const ObjectVector & objectsA, const ObjectVector & objectsB )
		{
			ObjectVector::const_iterator it;
			std::string oA, oB;
			for( it=objectsA.begin(); it!=objectsA.end(); ++it )
			{
				char buf[32];
				sprintf( buf, "%d,%d,%d;", (*it).id, (*it).pos, (*it).count );
				oA += buf; 
			}
			for( it=objectsB.begin(); it!=objectsB.end(); ++it )
			{
				char buf[32];
				sprintf( buf, "%d,%d,%d;", (*it).id, (*it).pos, (*it).count );
				oB += buf; 
			}
			logvital(LOG_NOTICE,
					"formatlog:trade:roleidA=%d:roleidB=%d:moneyA=%u:moneyB=%u:objectsA=%s:objectsB=%s",
					roleidA, roleidB, moneyA, moneyB, oA.c_str(), oB.c_str() );
		}

		static void upgrade( int roleid, int level, int money )
		{
			logvital( LOG_NOTICE, "formatlog:upgrade:roleid=%d:level=%d:money=%d", roleid, level, money );
		}

		static void die( int roleid, int type, int attacker )
		{
			logvital( LOG_NOTICE, "formatlog:die:roleid=%d:type=%d:attacker=%d", roleid, type, attacker );
		}

		static void keyobject( int id, int delta )
		{
			logvital( LOG_NOTICE, "formatlog:keyobject:id=%d:delta=%d", id, delta );
		}

	};

#ifdef _DEBUG
	#define LOG_TRACE	Log::trace
#else
	#define LOG_TRACE(...)
#endif

}

#endif //#ifdef _MSC_VER else

#endif


