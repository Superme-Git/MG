#pragma once

#include <map>
#include "simplelru.hpp"

namespace gdeliverd
{
class UserTypeEntry
{
	int func;
	int funcparm;
	char gender;//0-female,1-male,2-unknown
	GNET::Octets nickname;
public:
	UserTypeEntry() 
	{
	}
	UserTypeEntry(int _func,int _funcparm,char _gender,const GNET::Octets& _nickname):func(_func),funcparm(_funcparm),gender(_gender),nickname(_nickname) {}
	void GetUserType(int& _func,int& _funcparm,char& _gender,GNET::Octets& _nickname)
	{
		_func = func;
		_funcparm = funcparm;
		_gender = gender;
		_nickname = nickname;
	}
};

class UserType
{
	typedef	GNET::simplelru<int,UserTypeEntry,std::less<int> > LRUMap;
	LRUMap lrumap;
	public:
	UserType():lrumap(100000) {  }
	~UserType() { }
	static UserType * GetInstance() 
	{ 
		static UserType instance; 
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

	bool GetUserType( int userid, int& func,int& funcparm,char& gender,GNET::Octets& nickname )
	{
		UserTypeEntry ut;
		if(lrumap.find( userid,ut ))
		{
			ut.GetUserType(func,funcparm,gender,nickname);
			return true;
		}

		return true;	//changning
	}
/*	void SetUserType( int userid,int func,int funcparm,char gender,GNET::Octets& nickname )
	{
		lrumap.put(userid,UserTypeEntry(func,funcparm,gender,nickname));
	}
*/
	void SetUserType( int userid,int func,int funcparm,char gender,const GNET::Octets& nickname )
	{
		lrumap.put(userid,UserTypeEntry(func,funcparm,gender,nickname));
	}

};
}
