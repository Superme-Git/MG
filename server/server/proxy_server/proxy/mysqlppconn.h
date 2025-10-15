
#pragma once

#include <mysql++/mysql++.h>

namespace gdeliverd { 

class MysqlppConn
{
	public:
	
	mysqlpp::Connection mysqlppconn;

	char dbname[32];
	char ip[32];
	int port;
	char user[64];
	char passwd[128];
	int interval;
	int conntime;
	bool isreconnect;

	public:
	
	MysqlppConn() ;
	virtual ~MysqlppConn();

	static MysqlppConn *GetInstance()
	{
	    static MysqlppConn instance;
	    return &instance; 
	};

	std::string Identification() const  	{ return "Mysql"; };

	void InitEnv(char* _dbname,char* _ip,int _port,char* _user,char* _passwd,int _interval);

	void ConnectToMysql();

	int GetUserID(char* username);

	void Disconnect();

	bool IsNeedReconnect();

};

} 
