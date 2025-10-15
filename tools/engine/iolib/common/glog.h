
#ifndef __GNET_GLOG_H__
#define __GNET_GLOG_H__

#include <stdarg.h>
#include <syslog.h>
namespace GLog
{

	#define	GLOG_EMERG		LOG_EMERG
	#define	GLOG_ALERT		LOG_ALERT
	#define	GLOG_CRIT		CRIT
	#define	GLOG_ERR		LOG_ERR
	#define	GLOG_WARNING		LOG_WARNING
	#define GLOG_INFO		LOG_INFO

	void init( );

	void log( int __priority, const char * __fmt, ... )
#ifdef __GNUC__	
		__attribute__ ((format (printf, 2, 3)))
#endif
	;

	void logvital( int __priority, const char * __fmt, ... )
#ifdef __GNUC__	
		__attribute__ ((format (printf, 2, 3)))
#endif
	;

	void trace( const char * __fmt, ... );

	void tasklog( int roleid, int taskid, int type, const char * msg );

	void formatlog( const char * title, const char * msg );

	void task( int roleid, int taskid, int type, const char * msg );

	void upgrade( int roleid, int level, int money );

	void die( int roleid, int type, int attacker = -1);

	void keyobject( int id, int delta );

	void refine( int roleid, int item, int level, int result, int cost );
	
	void formatlog(const char * __fmt, ...)
#ifdef __GNUC__	
		__attribute__ ((format (printf, 1, 2)))
#endif
	;
}

#endif


