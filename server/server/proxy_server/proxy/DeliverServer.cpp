
#include "DeliverServer.hpp"
#include "AuAnyClient.hpp"
#include "gnet/ErrorInfoRep.hpp"
#include "netplayer.h"
#include "gnet/AnnounceServerAttribute.hpp"
#include "mapgameattr.h"
#include "openau/ServerIDResponse.hpp"
#include "GameDBServer.hpp"

namespace gdeliverd { 

	log4cpp::Category& DeliverServer::log = log4cpp::Category::getInstance(std::string("gdeliverd"));

	const GNET::Protocol::Manager::Session::State* DeliverServer::GetInitState() const
	{
		return &gnet::state::DeliverService;
	}

	void DeliverServer::OnAddSession(Session::ID sid)
	{
		linksidset.insert(sid);	
		log.info("DeliverServer:OnAddSession:add a link,sid=%d", sid);
		SendStatusToAllLink();
#ifdef	USE_AUANY
		openau::ServerIDResponse response;
		response.plattype = AuAnyClient::GetInstance()->getPlatType();
		response.serverid = AuAnyClient::GetInstance()->getServerId();
		Send(sid,response);
#endif

	}

	void DeliverServer::OnDelSession(Session::ID sid)
	{
		linksidset.erase(sid);
	//	UserContainer::GetInstance()->erasebylinksid(sid);
		log.info("DeliverServer::OnDelSession:delete a link,sid=%d", sid);
		RemoveSessionInfoByLinkId(sid);
	}
	
	void DeliverServer::OnDelSession(Session::ID sid,int state)
	{
		//TODO delete all info about this link
		linksidset.erase(sid);
	//	UserContainer::GetInstance()->erasebylinksid(sid);
		log.info("DeliverServer::OnDelSession:delete a link,state=0x%x,sid=%d", state, sid);
		RemoveSessionInfoByLinkId(sid);
	}

	void DeliverServer::BroadcastLink(GNET::Protocol* p)
	{
		for (Sidset::const_iterator it=linksidset.begin(), ite=linksidset.end(); it!=ite; ++it )
		{
			Send((*it),p);
		}
	}
	void DeliverServer::BroadcastLink(const GNET::Protocol* p)
	{
		for (Sidset::const_iterator it=linksidset.begin(), ite=linksidset.end(); it!=ite; ++it )
		{
			Send((*it),p);
		}
	}

	void DeliverServer::SendErrorInfo(Session::ID sid,Session::ID client_sid, int errcode, const char *info)
	{
		Send(sid,gnet::ErrorInfoRep(errcode,GNET::Octets(info,strlen(info)),client_sid));
	}

	void DeliverServer::SessionError(int userid,Session::ID link_sid,Session::ID client_sid, int errcode, const char *info)
	{
		SendErrorInfo(link_sid,client_sid,errcode,info);
		log.warn("DeliverServer:SessionError:user(id=%d) error(code=%d,%s),link_sid=%d,client_sid=%d",userid,errcode,info,link_sid,client_sid);
		RemoveSessionInfo(link_sid,client_sid);
	}

	void DeliverServer::GetServerLoad()
	{
		gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
                unsigned int load = (unsigned int)((double)(uc->Size()*200)/(double)(uc->GetFakePlayerLimit()));
                char _load = 0;
                if (load > 200)
                        load = 200;
                if(load <= 160)
                        _load = 1;
                else if(load <=  190)
                        _load = 2;
                else
                        _load = 3;

			//Ôö¼ÓÎ¬»¤×´Ì¬
		   if(!GameDBServer::GetInstance()->is_db_connected)
		   		_load = 0;
		   
                serverAttr.SetLoad(_load);
	}
	
	//update server's attribute when user login and logout
	void DeliverServer::BroadcastStatus()
	{
		static time_t timer = 0;
		time_t now = time(NULL);

		if(now - timer < 10)
			return;
		timer = now;

		GetServerLoad();
		gnet::AnnounceServerAttribute attr;
		attr.serverattr.flags = serverAttr.GetFlags();
		attr.serverattr.load = serverAttr.GetLoad();
		GameAttrMap::GetAttrs(attr.serverattr.extra);
		BroadcastLink(attr);
		gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		log.info("DeliverServer:BroadcastStatus:online=%d,fakemax=%d,load=%d,flags=%d",
				 uc->Size(), uc->GetFakePlayerLimit(), serverAttr.GetLoad(), serverAttr.GetFlags());
	}
	
	void DeliverServer::SendStatusToAllLink()
        {
                GetServerLoad();
		gnet::AnnounceServerAttribute attr;
                attr.serverattr.flags = serverAttr.GetFlags();
                attr.serverattr.load = serverAttr.GetLoad();
                GameAttrMap::GetAttrs(attr.serverattr.extra);
                BroadcastLink(attr);
		gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		log.info("DeliverServer:SendStatusToAllLink:online=%d,fakemax=%d,load=%d,flags=%d",
				 uc->Size(), uc->GetFakePlayerLimit(), serverAttr.GetLoad(), serverAttr.GetFlags());
        }
} 
