#pragma once
#include "simplelru.hpp"

namespace glinkd 
{

class BrokenInfo
{
public:
        int 	    userid;
        int         reason;
public:
        BrokenInfo() { }
        BrokenInfo(int _userid, int _reason) : userid(_userid),reason(_reason)  {}
        BrokenInfo(const BrokenInfo& r) : userid(r.userid),reason(r.reason) {}
	void GetBrokenInfo(int &u, int &r)
	{
		u = this->userid;
		r = this->reason;
	}
};


class LinkBrokenCache
{
	private:
		typedef	GNET::simplelru<int,BrokenInfo,std::less<int> >		CacheLRUMap;
		CacheLRUMap							brokenmap;
	public:
		LinkBrokenCache():brokenmap(20000) { }
		static LinkBrokenCache* GetInstance()
		{
			static LinkBrokenCache instance;
			return &instance;
		}

		void setMaxSize(size_t size) 
		{
			if(size <= 20000)
				brokenmap.setMaxSize(20000);
			else 
				brokenmap.setMaxSize(size); 
		}

		bool getBrokenInfo(const int sid,int& userid,int& reason)
		{
			BrokenInfo info;
			if(brokenmap.find(sid,info))
			{
				info.GetBrokenInfo(userid,reason);
				return true;
			}
			return false;
		}

		void setBrokenInfo(int sid,int userid,int reason)
		{
			brokenmap.put(sid,BrokenInfo(userid,reason));
		}
	
		void delBrokenInfo(int sid)
	   	{
			brokenmap.del(sid);
		}	   
};

}


