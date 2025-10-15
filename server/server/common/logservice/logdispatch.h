
#ifndef __GNET_LOGDISPATCH_H__
#define __GNET_LOGDISPATCH_H__

#include <stdio.h>
#include <syslog.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/un.h>

#include <ios>
#include <iostream>
#include <fstream>
#include <string>

#include "conf.h"
#include "timer.h"
#include "log.h"

using std::string;

//#define LOG_EMERG       0       /* system is unusable */
//#define LOG_ALERT       1       /* action must be taken immediately */
//#define LOG_CRIT        2       /* critical conditions */
//#define LOG_ERR         3       /* error conditions */
//#define LOG_WARNING     4       /* warning conditions */
//#define LOG_NOTICE      5       /* normal but significant condition */
//#define LOG_INFO        6       /* informational */
//#define LOG_DEBUG       7       /* debug-level messages */
/*
LOG_EMERG
	A panic condition was reported to all processes. 
LOG_ALERT
	A condition that should be corrected immediately. 
LOG_CRIT
	A critical condition. 
LOG_ERR
	An error message. 
LOG_WARNING
	A warning message. 
LOG_NOTICE
	A condition requiring special handling. 
LOG_INFO
	A general information message. 
LOG_DEBUG
	A message useful for debugging programs. 
*/

//priority >=11;
/*
15 	 Ò»°ãÈÕÖ¾
fd_format	=	/export/logs/format.log
;16	  ¼ÆÊýÆ÷ÈÕÖ¾ 20 Í³¼ÆÈÕÖ¾
fd_stat		=	/export/logs/stat.log
;17	 GMÃüÁîÈÕÖ¾
fd_gm		=	/export/logs/gm.log
;18	 ÓÎÏ·±ÒÏà¹ØÈÕÖ¾
fd_money	=	/export/logs/money.log
;19	ÁÄÌìÏà¹ØÈÕÖ¾
fd_chat		= 	/export/logs/chat.log
;21	 ÐÐ³ÌÏà¹ØÈÕÖ¾
fd_xingcheng	=	/export/logs/xingcheng.log
;22	 ½»Ò×Ïà¹ØÈÕÖ¾
fd_trade	=	/export/logs/trade.log
*/


#define LOG_FORMAT 15
#define LOG_STAT 16
#define LOG_GM 17
#define LOG_MONEY 18
#define LOG_CHAT2 19
#define LOG_COUNTER 20
#define LOG_XINGCHENG 21
#define LOG_TRADE 22


namespace GNET
{

	class LogDispatch
	{
		enum argument {
			flags = O_WRONLY|O_CREAT|O_APPEND|O_NONBLOCK|O_LARGEFILE,
			mode  = S_IRUSR|S_IWUSR|S_IRGRP|S_IROTH
		};

		static int fd_format;
		static int fd_stat;
		static int fd_gm;
		static int fd_money;
		static int fd_chat;
		static int fd_xingcheng;
		static int fd_trade;

		static int fd_err;
		static int fd_log;
		static int fd_formatlog;
		static int fd_trace;

		static int fd_statinfom;
		static int fd_statinfoh;
		static int fd_statinfod;

		static int threshhold;

		static int writefile( int fd, int priority, Octets& msg, Octets& hostname, Octets& servicename )
		{
			char buffer[1024];
    
			struct tm now_tm;
			time_t now = Timer::GetTime();
			size_t pos = strftime(buffer, sizeof(buffer)-1, "%Y-%m-%d %H:%M:%S ", localtime_r(&now, &now_tm));
								// syslog format "%h %e %T "
    
			if( pos + hostname.size() + 5 < sizeof(buffer) )
			{
				strncpy( buffer+pos, (char*)hostname.begin(), hostname.size() );
				pos += hostname.size();
			}
			strncpy( buffer+pos, " ", 1 );
			pos ++;
    
			if( pos + servicename.size() + 5 < sizeof(buffer) )
			{
				strncpy( buffer+pos, (char*)servicename.begin(), servicename.size() );
				pos += servicename.size();
			}
			strncpy( buffer+pos, ": ", 2 );
			pos ++;
			pos ++;
    
			if( pos + 20 < sizeof(buffer) )
				pos += sprintf( buffer+pos, "%s : ", Log::prioritystring(priority) );
			strncpy( buffer+pos, (char*)msg.begin(), std::min(msg.size(),sizeof(buffer)-pos-1) );
			pos += std::min(msg.size(),sizeof(buffer)-pos-1);

			if( buffer[pos-1] != '\n' )
				buffer[pos++] = '\n';
			return write( fd, buffer, pos );
		}

	public:
		static int wwrite(GNET::Octets &o) {
			char file[] = "linkmodule.log";
			std::ofstream theout(file,std::ios::app);
			theout.imbue(std::locale(""));
			std::string ws((char*)o.begin(), o.size());
			theout << ws << std::endl;
			theout.close();
			return 0;
		}
	public:
		static void openfiles( )
		{
			Conf * conf = Conf::GetInstance();

			std::string format = conf->find("logservice","fd_format");
			std::string stat = conf->find("logservice","fd_stat");
			std::string gm = conf->find("logservice","fd_gm");
			std::string money = conf->find("logservice","fd_money");
			std::string chat = conf->find("logservice","fd_chat");
			std::string xingcheng = conf->find("logservice","fd_xingcheng");
			std::string trade = conf->find("logservice","fd_trade");

			std::string err = conf->find("logservice","fd_err");
			std::string log = conf->find("logservice","fd_log");
			std::string formatlog = conf->find("logservice","fd_formatlog");
			std::string trace = conf->find("logservice","fd_trace");

			std::string statinfom = conf->find("logservice","fd_statinfom");
			std::string statinfoh = conf->find("logservice","fd_statinfoh");
			std::string statinfod = conf->find("logservice","fd_statinfod");
			//std::string charset = Conf::GetInstance()->find("common","charset");

			if( format.length() <= 0 )
				format = "/var/log/format.log";
			if( stat.length() <= 0 )
				stat = "/var/log/stat.log";
			if( gm.length() <= 0 )
				gm = "/var/log/gm.log";
			if( money.length() <= 0 )
				money =	"/var/log/money.log";
			if( chat.length() <= 0 )
				chat = "var/log/chat.log";
			if( xingcheng.length() <= 0 )
				xingcheng = "/var/log/xingcheng.log";
			if( trade.length() <= 0 )
				trade =	"/var/log/trade.log";

			if( err.length() <= 0 )
				err = "/var/log/world2.err";
			if( log.length() <= 0 )
				log = "/var/log/world2.log";
			if( formatlog.length() <= 0 )
				formatlog = "/var/log/world2.formatlog";
			if( trace.length() <= 0 )
				trace = "/var/log/world2.trace";

			if( statinfom.length() <= 0 )
				statinfom = "/var/log/statinfom";
			if( statinfoh.length() <= 0 )
				statinfoh = "/var/log/statinfoh";
			if( statinfod.length() <= 0 )
				statinfod = "/var/log/statinfod";

			fd_format = open( format.c_str(), flags, mode );
			fd_stat = open( stat.c_str(), flags, mode );
			fd_gm = open( gm.c_str(), flags, mode );
			fd_money = open( money.c_str(), flags, mode );
			fd_chat = open( chat.c_str(), flags, mode );
			fd_xingcheng = open( xingcheng.c_str(), flags, mode );
			fd_trade = open( trade.c_str(), flags, mode );

			fd_err = open( err.c_str(), flags, mode );
			fd_log = open( log.c_str(), flags, mode );
			fd_formatlog = open( formatlog.c_str(), flags, mode );
			fd_trace = open( trace.c_str(), flags, mode );

			fd_statinfom = open( statinfom.c_str(), flags, mode );
			fd_statinfoh = open( statinfoh.c_str(), flags, mode );
			fd_statinfod = open( statinfod.c_str(), flags, mode );
		}

		static void closefiles( )
		{		

			close( fd_format );
			close( fd_stat );
			close( fd_gm );
			close( fd_money );
			close( fd_chat );
			close( fd_xingcheng );
			close( fd_trade );

			close( fd_err );
			close( fd_log );
			close( fd_formatlog );
			close( fd_trace );

			close( fd_statinfom );
			close( fd_statinfoh );
			close( fd_statinfod );

			fd_format = fd_stat = fd_gm = fd_money = fd_chat = fd_xingcheng = fd_trade = fd_err = fd_log = fd_formatlog = fd_trace = fd_statinfom = fd_statinfoh = fd_statinfod = -1;
		}

		static void set_threshhold( int __threshhold )
		{
			threshhold = __threshhold;
		}

		static void log( int priority, Octets& msg, Octets& hostname, Octets& servicename )
		{
			if( LOG_FORMAT == priority ) {
				writefile( fd_format, priority, msg, hostname, servicename );
				return;
			}

			if( LOG_STAT == priority || LOG_COUNTER == priority) {
				writefile( fd_stat, priority, msg, hostname, servicename );
				return;
			}
			if( LOG_GM == priority ) {
				writefile( fd_gm, priority, msg, hostname, servicename );
				return;
			}
			if( LOG_MONEY == priority ) {
				writefile( fd_money, priority, msg, hostname, servicename );
				return;
			}
			if( LOG_CHAT2 == priority ) {
				writefile( fd_chat, priority, msg, hostname, servicename );
				return;
			}
			if( LOG_XINGCHENG == priority ) {
				writefile( fd_xingcheng, priority, msg, hostname, servicename );
				return;
			}
			if( LOG_TRADE == priority ) {
				writefile( fd_trade, priority, msg, hostname, servicename );
				return;
			}

			if( LOG_WARNING >= priority )
			{
				writefile( fd_err, priority, msg, hostname, servicename );
			}
			else if( LOG_NOTICE == priority )
			{
				writefile( fd_formatlog, priority, msg, hostname, servicename );
			}

			if( priority > threshhold )
				return;

			if( LOG_INFO == priority )
			{
				writefile( fd_log, priority, msg, hostname, servicename );
			}
			else if( LOG_DEBUG == priority )
			{
				writefile( fd_trace, priority, msg, hostname, servicename );
			}
		}

		static void stat( int priority, Octets& msg, Octets& hostname, Octets& servicename )
		{
			if( LOG_DEBUG == priority )
			{
				writefile( fd_statinfom, priority, msg, hostname, servicename );
			}
			else if( LOG_INFO == priority )
			{
				writefile( fd_statinfoh, priority, msg, hostname, servicename );
			}
			else if( LOG_NOTICE == priority )
			{
				writefile( fd_statinfod, priority, msg, hostname, servicename );
			}
		}
	};
}

#endif


