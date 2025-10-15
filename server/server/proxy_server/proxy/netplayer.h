#pragma once

#include "rpcgen.hpp"
#include "DeliverServer.hpp"

#define MAX_PLAYER_NUM_DEFAULT  10000
#define FAKE_MAX_PLAYER_NUM_DEFAULT  2500
namespace gdeliverd
{
	typedef int UserId;
	struct UserInfo
	{
		int	client_sid;
		int	link_sid;
		int	loginip;
		int	algorithm;// now not use
		GNET::Octets account;//name remove @sso
		int	timeout; //for ForceLogin
		UserInfo(int _client_sid,int _link_sid,int _loginip,int _algorithm,GNET::Octets _account):client_sid(_client_sid),link_sid(_link_sid),loginip(_loginip),algorithm(_algorithm),account(_account)
		{
			timeout = DeliverServer::GetInstance()->getForceLoginTimeout();
		}

		UserInfo()
		{
			timeout = DeliverServer::GetInstance()->getForceLoginTimeout();
		}

	};

	class UserContainer
	{
		private:
			int  max_player_num;
			int  fake_max_player_num;

			typedef std::map<UserId,UserInfo> USER_MAP;
			typedef USER_MAP::iterator	Iterator;
			USER_MAP users;
			UserContainer():max_player_num(MAX_PLAYER_NUM_DEFAULT),fake_max_player_num(FAKE_MAX_PLAYER_NUM_DEFAULT){}
		public:
			static UserContainer * GetInstance()
			{
				static UserContainer instance;
				return &instance;
			}
			int Size() { return users.size();	}

		public:
			UserInfo* find(UserId userid) 
			{
				USER_MAP::iterator it = users.find(userid);
				return it != users.end() ? &(it->second) : NULL;
			}
			void erase(UserId userid)
			{
				users.erase(userid);
			}
			//when link disconnect,erase all user on this link_sid
			void erasebylinksid(int link_sid)
			{
				for(Iterator itor=users.begin();itor!=users.end();)
				{
					if((*itor).second.link_sid == link_sid)
					{
						users.erase(itor++);
					}
					else
						++itor;
				}	
			}

			bool addUser(UserId userid,int client_sid,int link_sid,int loginip,int algorithm,const GNET::Octets& account)
			{
				UserInfo ui(client_sid,link_sid,loginip,algorithm,account);
				return users.insert(std::make_pair(userid,ui)).second;
			}

			void SetPlayerLimit(int num,int fake_num)
			{
				if(0 == num)
					max_player_num = MAX_PLAYER_NUM_DEFAULT;
				else
					max_player_num = num; 	
				if(0 == fake_num)
					fake_max_player_num = FAKE_MAX_PLAYER_NUM_DEFAULT;
				else
					fake_max_player_num = fake_num;
			}

			int GetPlayerLimit() 
			{ 
				return max_player_num;
			}

			int GetFakePlayerLimit()
			{
				return fake_max_player_num;
			}

	};

}

