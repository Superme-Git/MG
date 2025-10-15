
#include "GameDBServer.hpp"
#include "AuthClient.hpp"
#include "AuAnyClient.hpp"
#include "gnet/DiscountAnnounce.hpp"
#include "openau/ServerIDResponse.hpp"
#include "rpcgen.hpp"
#include "DeliverServer.hpp"

namespace gdeliverd { 
 
	log4cpp::Category& GameDBServer::log = log4cpp::Category::getInstance(std::string("gdeliverd"));

	const GNET::Protocol::Manager::Session::State* GameDBServer::GetInitState() const
	{
		return &gnet::state::GameDBServer;
	}

	void GameDBServer::OnAddSession(Session::ID sid)
	{
		if(dbsidset.empty())
		{
			log.info("GameDBServer:OnAddSession:add a gs(db),sid=%d", sid);
		}
		else
		{
			Close(db_sid);//new session always replace old
			log.info("GameDBServer:OnAddSession:new replace old,old_sid=%d,new_sid=%d", db_sid, sid);
		}
		dbsidset.insert(sid);
		db_sid = sid;
		is_db_connected = true;

		if(AuthClient::GetInstance()->GetAbroadPolicy() == 0)
		{
			SendProtocol(gnet::DiscountAnnounce(discount,0,GNET::Octets()));
		}

#ifdef	USE_AUANY
		openau::ServerIDResponse response;
		response.plattype = AuAnyClient::GetInstance()->getPlatType();
		response.serverid = AuAnyClient::GetInstance()->getServerId();
		SendProtocol(response);
#endif

		//刷新服务器状态给link
		DeliverServer * dsm = DeliverServer::GetInstance();
		dsm->SendStatusToAllLink();

	}

	void GameDBServer::OnDelSession(Session::ID sid)
	{
		if(dbsidset.erase(sid) != 0)
		{
			if(dbsidset.size() == 0)
			{
				db_sid = 0;
				is_db_connected = false;
				//刷新服务器状态给link
				DeliverServer * dsm = DeliverServer::GetInstance();
				dsm->SendStatusToAllLink();
			}
			log.info("GameDBServer:OnDelSession:delete a gs(db),sid=%d", sid);
		}	
		
	}

	void GameDBServer::OnDelSession(Session::ID sid,int state)
	{
		if(dbsidset.erase(sid) != 0)
		{
			if(dbsidset.size() == 0)
			{
				db_sid = 0;
				is_db_connected = false;
				log.info("GameDBServer:OnDelSession:delete a gs(db),state=0x%x,sid=%d", state, sid);

				//刷新服务器状态给link
				DeliverServer * dsm = DeliverServer::GetInstance();
				dsm->SendStatusToAllLink();
			}
			else
				log.info("GameDBServer:OnDelSession:delete a invalid gs(db),state=0x%x,sid=%d",state,sid);
		}
	}

} 
