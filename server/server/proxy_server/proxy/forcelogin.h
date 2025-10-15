#pragma once

#include "rpcgen.hpp"
#include "netplayer.h"
#include "gnet/KickoutUser.hpp"
#include "gnet/UserLogin.hpp"
#include "gnet/UserLogin2.hpp"
#include "mapusbkey.h"
#include "mapphonetoken.h"

namespace gdeliverd
{

	class ForceLogin : public GNET::Timer::Observer 
	{
		private:	
			typedef std::map<int/*userid*/,UserInfo>	ForceLoginMap;
			typedef ForceLoginMap::iterator			Iterator;	
			ForceLoginMap 					map;
		public:
			static ForceLogin * GetInstance()
			{
				static ForceLogin instance;
				return &instance;
			}
			ForceLogin() {}

			size_t Size() 
			{
				 return map.size();
			}
			virtual void Update()
			{
				for (Iterator it = map.begin(); it != map.end();)
				{
					it->second.timeout--;
					if(it->second.timeout <= 0)
					{
						ProcessForceLogin(it->first,it->second);
						map.erase(it++);
					}
					else
						++it;
				}
			}
			
			UserInfo * GetForceLogin( int userid )
			{
				Iterator it = map.find( userid );
				if (it != map.end())
				{
					return &(it->second);
				}
				return NULL;
			}
			void PutForceLogin( int userid, int client_sid, int link_sid )
			{
				UserInfo ui(client_sid,link_sid,0,0,GNET::Octets());
				map[userid] = ui;
			}
			void RmvForceLogin( int userid )
			{
				map.erase(userid);
			}

			void ProcessForceLogin(int userid, UserInfo& uinfo)
			{
				UserInfo * old_uinfo = UserContainer::GetInstance()->find(userid);
				if(NULL != old_uinfo)
				{
					DeliverServer::GetInstance()->Send(old_uinfo->link_sid,gnet::KickoutUser(userid,old_uinfo->client_sid,GNET::ERR_LOGOUT_BY_BLKICK));
					UserContainer::GetInstance()->erase(userid);
					GNET::Log::log(LOG_WARNING,"user(id=%d) ForceLogin,old be Kick,old localsid=%d",userid,old_uinfo->client_sid);
				}
				gdeliverd::SessionInfo* sinfo = gdeliverd::DeliverServer::GetInstance()->GetSessionInfo(uinfo.link_sid,uinfo.client_sid);
				if(NULL == sinfo)
					return;
#ifdef USE_AUANY

				if(sinfo->logintype == gnet::AUANY_LOGIN)
				{
					GNET::Octets used_elec_number;
					gnet::UserLogin2Arg arg(userid,uinfo.client_sid,1,0,used_elec_number,0,GNET::Octets());
					gnet::UserLogin2 *rpc = (gnet::UserLogin2*)GNET::Rpc::Call(gnet::UserLogin2::PROTOCOL_TYPE,arg);
					rpc->link_sid = uinfo.link_sid;
					rpc->client_sid = uinfo.client_sid;
					rpc->is_trylogin = false;
					GNET::Log::log(LOG_WARNING,"user(id=%d) AuAny UserLogin.",userid);
					rpc->TryLogin(GNET::ERR_SUCCESS);
				}
				return;
#endif

				gdeliverd::AuthClient* acm = gdeliverd::AuthClient::GetInstance();
				if(sinfo->logintype == gnet::CROSS_LOGIN)
				{
					GNET::Octets used_elec_number;
					gnet::UserLogin2Arg arg(userid,uinfo.client_sid,1,0,used_elec_number,0,GNET::Octets());
					gnet::UserLogin2 *rpc = (gnet::UserLogin2*)GNET::Rpc::Call(gnet::UserLogin2::PROTOCOL_TYPE,arg);
					rpc->link_sid = uinfo.link_sid;
					rpc->client_sid = uinfo.client_sid;
					rpc->is_trylogin = false;
					GNET::Log::log(LOG_WARNING,"user(id=%d) Cross UserLogin.",userid);
					rpc->TryLogin(GNET::ERR_SUCCESS);
					return;
				}

				if(0 == gdeliverd::AuthClient::GetInstance()->GetAbroadPolicy())
				{
					GNET::Octets used_elec_number;
					if(sinfo->logintype == gnet::ELEC_LOGIN)
					{
						switch(sinfo->algorithm & gnet::ALGORITHM_MASK_HIGH)
						{
							case gnet::ALGORITHM_USBKEY2:
								{
									gdeliverd::UsbKeyCache::GetInstance()->GetUsedElecNumber(userid,used_elec_number);
									break;
								}
							case gnet::ALGORITHM_PHONETOKEN:
								{
									gdeliverd::PhoneTokenCache::GetInstance()->GetUsedElecNumber(userid,used_elec_number);
									break;
								}
							default:
								break;
						}
					}
					gnet::UserLogin2Arg arg(userid,uinfo.client_sid,1,0,used_elec_number,0,GNET::Octets());
					gnet::UserLogin2 *rpc = (gnet::UserLogin2*)GNET::Rpc::Call(gnet::UserLogin2::PROTOCOL_TYPE,arg);
					rpc->link_sid = uinfo.link_sid;
					rpc->client_sid = uinfo.client_sid;

					if(!acm->SendProtocol(rpc))
					{
						GNET::Log::log(LOG_WARNING,"user(id=%d) send UserLogin failed.",userid);
						rpc->TryLogin(GNET::ERR_NOT_CONNECT_AU);
					}
				}
				else
				{
					gnet::UserLoginArg arg(userid,uinfo.client_sid,1,0);
					gnet::UserLogin *rpc = (gnet::UserLogin*)GNET::Rpc::Call(gnet::UserLogin::PROTOCOL_TYPE,arg);
					rpc->link_sid = uinfo.link_sid;
					rpc->client_sid = uinfo.client_sid;

					if(!acm->SendProtocol(rpc))
					{
						GNET::Log::log(LOG_WARNING,"user(id=%d) send UserLogin failed.",userid);
						rpc->TryLogin(GNET::ERR_NOT_CONNECT_AU);
					}
				}	

				RmvForceLogin(userid);
			}
	};

	/* Check ForceLogin user map periodically, and remove timeout items
	 */  
/*
	class CheckForceLoginTimer : public GNET::Thread::Runnable
	{
		int update_time;
		CheckForceLoginTimer(int _time,int _proir=1) : Runnable(_proir),update_time(_time) { }
		public:
		static CheckForceLoginTimer* GetInstance(int _time=60,int _proir=1) {
			static CheckForceLoginTimer instance(_time,_proir);
			return &instance;
		}	

		void Run()
		{
			ForceLogin::GetInstance()->Update( update_time );
			GNET::Thread::HouseKeeper::AddTimerTask(this,update_time);
		}
	};
*/
};


