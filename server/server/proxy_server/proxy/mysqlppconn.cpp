
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

#include "mysqlppconn.h"
#include "rpcgen.hpp"

namespace gdeliverd {

	MysqlppConn::MysqlppConn()
	{
		memset(dbname,0,sizeof(dbname));
		memset(ip,0,sizeof(ip));
		port = 0;
		memset(user,0,sizeof(user));
		memset(passwd,0,sizeof(passwd));
		conntime = 0;
		interval = 0;
		isreconnect = false;
	}

	MysqlppConn::~MysqlppConn() {	mysqlppconn.disconnect();	}

	void MysqlppConn::InitEnv(char* _dbname,char* _ip,int _port,char* _user,char* _passwd,int _interval)
	{
		int dbnamelen = strlen(_dbname);
		int iplen = strlen(_ip);
		int userlen = strlen(_user);
		int passwdlen = strlen(_passwd);

		strncpy(dbname,_dbname,dbnamelen);
		strncpy(ip,_ip,iplen);
		port = _port;
		strncpy(user,_user,userlen);
		strncpy(passwd,_passwd,passwdlen);
		interval = _interval;
	}

	void MysqlppConn::ConnectToMysql(  )
	{
#ifndef USE_AUANY
        //just for compile error,no use
		mysqlppconn.set_option(new mysqlpp::MultiStatementsOption(CLIENT_MULTI_STATEMENTS));
#endif
		bool res = mysqlppconn.connect(dbname,ip,user,passwd,port);
		if(!res)
		{
			GNET::Log::log(LOG_ERR,"ConnectToMysql (dbname=%s ip=%s port=%d user=%s passwd=%s) Failed! ",dbname,ip,port,user,passwd);
			return;
		}
		GNET::Log::log(LOG_INFO,"ConnectToMysql (dbname=%s ip=%s port=%d user=%s passwd=%s) Success! ",dbname,ip,port,user,passwd);
		conntime = time(NULL);
	}

	int MysqlppConn::GetUserID(char* username)
	{

		if(IsNeedReconnect())
		{
			Disconnect();
			ConnectToMysql();
		}

		if(!mysqlppconn.connected())
		{
			GNET::Log::log(LOG_INFO,"Mysql is not connect! ");
			return -1;
		}

		mysqlpp::Query query = mysqlppconn.query();
		char sql[256];
		memset(sql,0,sizeof(sql));
		sprintf(sql,"select query_user(\"%s\")",username);

		mysqlpp::StoreQueryResult res = query.store(sql,strlen(sql));
		int userid = res[0][0];

		GNET::Log::log(LOG_WARNING,"GetUserID user(username=%s , userid=%d) ",username,userid);

		return userid;
	}

	bool MysqlppConn::IsNeedReconnect()
	{
		if(conntime == 0)
		{
			conntime = time(NULL);
			return false;
		}
		int now = time(NULL);
		if(now - conntime <  interval)
		{
			return false;
		}

		return true;
	}

	void MysqlppConn::Disconnect()
	{
		mysqlppconn.disconnect();
	}

}
