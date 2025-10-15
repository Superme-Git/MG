#pragma once

#ifndef USE_AUANY
  #include "mysqlppconn.h"
#endif
#include "simplelru.hpp"

using namespace std;

namespace gdeliverd
{

extern int global_userid;

class PasswdEntry
{
        int    userid;
        int    algorithm;
        GNET::Octets password;
        GNET::Octets matrix;
public:
        PasswdEntry() { }
        PasswdEntry(int u, int algo, const GNET::Octets& p,const GNET::Octets& m) : userid(u),algorithm(algo),password(p),matrix(m)  {}
        PasswdEntry(const PasswdEntry& r) : userid(r.userid),algorithm(r.algorithm),password(r.password),matrix(r.matrix)  {}

        void GetPassword(int& uid, int& algo, GNET::Octets& p, GNET::Octets& m)
        {
                uid  = userid;
                algo = algorithm;
                p = password;
                m = matrix;
        }
};

//std::less
struct OctetsCompare
{
	bool operator () (const GNET::Octets & lh,const GNET::Octets & rh) const
	{
		if (lh.size() < rh.size())
		{
			return true;
		}
		if(lh.size() > rh.size())
		{
			return false;
		}
		int a = memcmp(lh.begin(),rh.begin(),lh.size());
		if(a < 0)
			return true;
		else if(a >= 0)
			return false;
		return false;
	}
};

class Passwd
{
	typedef	GNET::simplelru<GNET::Octets,PasswdEntry,OctetsCompare> LRUMap;
	LRUMap lrumap;

	public:
	Passwd():lrumap(100000)
	{

	}
	~Passwd() { }
	static Passwd * GetInstance()
	{
		static Passwd instance;
		return &instance;
	}


//	size_t Size() { return lrumap.size();}
	size_t GetCacheLimit() { return lrumap.getMaxSize();}
	void SetMaxSize(size_t size)
	{
		if(size <= 100000)
			lrumap.setMaxSize(100000);
		else
			lrumap.setMaxSize(size);
	}
	bool GetPasswd( GNET::Octets username, int& userid, int& algorithm, GNET::Octets& passwd, GNET::Octets& matrix )
	{
		PasswdEntry pe;
		if(lrumap.find( username,pe ))
		{
			pe.GetPassword(userid, algorithm, passwd, matrix);
			return true;
		}
		algorithm = 0;
#ifndef USE_AUANY
		char usernamestr[64];
		memset(usernamestr,0,sizeof(usernamestr));
		int strsize = 0;
        if(username.size() > 63)
            strsize = 63;
        else
            strsize = username.size();
		strncpy(usernamestr,(char*)username.begin(),strsize);
		usernamestr[63] = '\0';
		userid = gdeliverd::MysqlppConn::GetInstance()->GetUserID(usernamestr);
		if(userid <= 0)
			return false;
#endif
		return true;		//changning mod for debug
	}
	void SetPasswd( GNET::Octets username, int userid, int algorithm, GNET::Octets& passwd, GNET::Octets& matrix )
	{
		lrumap.put(username,PasswdEntry(userid,algorithm,passwd,matrix));
	}

	void DelPasswd( GNET::Octets username )
	{
	   	lrumap.del(username);
	}
};
}


