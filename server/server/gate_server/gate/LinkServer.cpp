
#include "LinkServer.hpp"
#include "ProviderServer.hpp"
#include "DeliveryClient.hpp"

#include "gnet/Challenge.hpp"
#include "gnet/StatusAnnounce.hpp"
#include "gnet/ErrorInfo.hpp"

#include "gnet/link/Dispatch.hpp"
#include "gnet/link/Send.hpp"
#include "gnet/link/LinkBroken.hpp"
#include "gnet/link/Broadcast.hpp"
#include "gnet/link/SetLogin.hpp"
#include "LinkBrokenCache.hpp"
#include "stat.hpp"

/*
#ifndef WIN32
	static GNET::ProtocolBinder GNET_ProtocolBinder(PROTOCOL_BINDER,65536);
	static GNET::CompressBinder GNET_CompressBinder(PROTOCOL_COMPRESSBINDER,65536);
#endif
*/

namespace glinkd { 

	log4cpp::Category& LinkServer::log = log4cpp::Category::getInstance(std::string("glinkd"));
	log4cpp::Category& LinkSessions::log = log4cpp::Category::getInstance(std::string("glinkd"));
  
	const GNET::Protocol::Manager::Session::State* LinkServer::GetInitState() const
	{
		return &gnet::state::LinkService;
	}

	void LinkServer::OnAddSession(Session::ID sid)
	{
		if (false == GetSessions().insert(sid))
		{
			Close(sid);
			log.warn("LinkServer:OnAddSession:duplicate sid=%d,closed!", sid);
			return;
		}
		log.debug("LinkServer:OnAddSession:add new client,sid=%d.", sid);

#ifdef ENABLE_FLASHPOLICY
		this->SendFlashPolicy(sid);
#endif
		gnet::Challenge challenge;
		challenge.Setup(16);
		challenge.version = version;
		challenge.serverattr = GetServerAttr(); 
		this->Send(sid, challenge);
		GetLinkSession(sid)->setChallenge(challenge.nonce);
	}

	void LinkServer::OnSetTransport(Session::ID sid,const GNET::SockAddr& local,const GNET::SockAddr& peer)
	{
		LinkSession *psession = GetLinkSession(sid);
		if(psession)
		{
			psession->setPeer(peer); 
		}
	}

	log4cpp::Category& ProviderMap::log = log4cpp::Category::getInstance(std::string("glinkd"));
	inline void broadcast(ProviderMap * pm, const GNET::Protocol & p)
	{
		for (ProviderMap::iterator it = pm->begin(), ie = pm->end(); it != ie; ++it)
		{
			if(!ProviderServer::GetInstance()->Send(it->second, p))
				LinkServer::log.warn("LinkServer:broadcast:proto=%d to provider failed!", GNET::Protocol::typeLow(p.GetType()));
			else
				LinkServer::log.debug("LinkServer:broadcast:proto=%d to provider.", GNET::Protocol::typeLow(p.GetType()));
		}
	}

	void LinkServer::OnDelSession(Session::ID sid) { }
	void LinkServer::OnDelSession(Session::ID sid,int state)
	{
		LinkSession * psession = GetLinkSession(sid);
		if(NULL == psession)
			return;
		int userid = psession->getUserId();
		int reason = GNET::Protocol::typeHigh(state & 0xf0000);	
		if(userid)
		{
			DeliveryClient::GetInstance()->SendProtocol(gnet::StatusAnnounce(userid,sid,0)); //for ForceLogin,deliver need to clean ForceLoginMap
			UseridMap::iterator itor = GetUseridMap().find(userid);
			if(itor != GetUseridMap().end() && (*itor).second == sid)
			{
				GetUseridMap().erase(userid);
				// broadcast LinkBroken to gs 
				if(psession->getLogin() == gnet::link::SetLogin::eLogin)
				{
				   gnet::link::LinkBroken lb(userid,sid,reason);
				   broadcast(psession->getProviders(), lb);
				   broadcast(GetSessions().getDefaultProviders(), lb);

				   LinkBrokenCache::GetInstance()->setBrokenInfo(sid,userid,reason);	
				}
				char strpeer[32];
				strcpy( strpeer,inet_ntoa(psession->getPeerAddr()));
				time_t now = time(NULL);
				log.info("LinkServer:OnDelSession:accountid=%d,account=%.*s,time=%d,peer=%s,reason=%d,mid=%.*s,logout!",userid,psession->getIdentity().size(),(char*)psession->getIdentity().begin(),(now-psession->getLogintime()),strpeer,reason,psession->getMid().size(),(char*)psession->getMid().begin());
			}
		}
		GetHalfLoginSet().erase(sid);
		GetSessions().erase(sid);
		alivetimemap.erase(sid);
		log.info("LinkServer:OnDelSession:close a client,userid=%d,sid=%d,reason=%d",userid,sid,reason);
		TriggerListen();
	}
	
	void LinkServer::TriggerListen()
	{
	   if ((!IsListening()) && UnderHalfloginLimit(halfloginset.size()) && UnderUserLimit(useridmap.size()))
	   {
		  log.warn("LinkServer:TriggerListen:halflogin=%d,online=%d,halflogin_limit=%d,online_limit=%d",halfloginset.size(),useridmap.size(),halflogin_limit,user_count_limit);
	      StartListen();
	   }
	}

	void LinkServer::SendErr(Session::ID sid, int errorcode, const char *info)
	{   
	   if(errorcode != GNET::ERR_SERVEROVERLOAD)
		  log.info("LinkServer:SendErr:linkid=%d,errcode=(%d,%s),sid=%d",ProviderServer::GetInstance()->getLinkId(),errorcode,info,sid);
	   SendErr(sid,errorcode,GNET::Octets(info,strlen(info)));
	}

	void LinkServer::SendErr(Session::ID sid, int errorcode, const GNET::Octets & info)
	{
	   Send(sid,gnet::ErrorInfo(errorcode,info));
	   ChangeState(sid,&gnet::state::Null);
	   OnDelSession(sid,GNET::CLOSE_ACTIVE);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//load unknown protocol type and size from sizepolicy.conf, CheckUnknownProtocol.Sizepolicy
	void LinkServer::ReloadSizePolicy()
	{
	   GNET::Conf conf("sizepolicy.conf");
	   std::vector<std::string> keys;
	   conf.getkeys("CheckUnknownProtocol", keys);

	   sizepolicy.clear();
	   for (std::vector<std::string>::iterator ik = keys.begin(), ie = keys.end(); ik != ie; ++ik)
	   {
	      std::string typemaxsize = conf.find("CheckUnknownProtocol", *ik);
	      std::string::size_type sep = typemaxsize.find(',');
	      if (std::string::npos == sep)
	      {
			  log.warn("LinkServer:ReloadSizePolicy:Error format.'%s=%s'", ik->c_str(), typemaxsize.c_str());
			  continue; // continue load
	      }
	      std::string type(typemaxsize, 0, sep);
	      std::string maxsize(typemaxsize, sep + 1);
	      sizepolicy.push_back(sizepolicy_t::value_type(::atoi(type.c_str()), ::atoi(maxsize.c_str())));
	   }
	   std::sort(sizepolicy.begin(), sizepolicy.end(), SizepolicyComparator());
	}

	bool LinkServer::CheckUnknownProtocol(Session::ID, GNET::Protocol::Type ptype, unsigned int psize)
	{
	   //开发时，不检查，发布时需要严格检查（通过配置文件配置）
	   if(!checkunknownprotocol)
	      return true;
	   sizepolicy_t::value_type val(ptype, psize);
	   sizepolicy_t::iterator it = std::lower_bound(sizepolicy.begin(), sizepolicy.end(), val, SizepolicyComparator());
	   return it != sizepolicy.end() && it->first == ptype && it->second >= psize;
	}

	//考虑在protocol.h增加直接发送type，octets的接口，可以避免一次拷贝
	struct ProtocolBox : public GNET::Protocol
   	{
		const GNET::Octets & data;
		ProtocolBox(Type type, const GNET::Octets & _data) : data(_data) { this->type = type; }
		virtual OctetsStream & marshal(OctetsStream & os) const { return os.push_byte((const char *)data.begin(), data.size()); }
		virtual const OctetsStream & unmarshal(const OctetsStream & os) { abort(); return os; }
		virtual void Process(GNET::Protocol::Manager*, unsigned int) { abort(); }
		virtual GNET::Protocol * Clone() const { abort(); return 0; }
	};

	void LinkServer::DispatchUnknownProtocol(Session::ID sid, GNET::Protocol::Type type, const GNET::Marshal::OctetsStream & os)
	{
	   LinkSession * psession = GetLinkSession(sid);
	   if (NULL == psession || psession->getUserId() == 0)
	   {
	      Close(sid);
	      return;
	   }
	   SetAliveTime(sid, GetKeepAliveInterval());
	   
	   ProviderId 		pvid 		= GNET::Protocol::typeHigh(type);
	   GNET::Protocol::Type real_type 	= GNET::Protocol::typeLow(type);
	   int userid 				= psession->getUserId();
	   
	   ProviderMap * pm 			= psession->getProviders(); 
	   ProviderMap::iterator itor;

	   if ((itor = pm->find(pvid)) == pm->end()) // find Provider in LinkSession
	   {
	      pm = GetSessions().getDefaultProviders();
	      if ((itor = pm->find(pvid)) == pm->end()) // find Default Provider错误处理需要知道哪个ProviderMap
	      {
			  //TODO根据配置决定是否严格检查
			  SendErr(sid,GNET::ERR_GS_NOT_CONNECT_LINK,"no useable provider.");
			  log.warn("LinkServer:DispatchUnknownProtocol:user(id=%d,sid=%d) no useable provider,pvid=%d,type=%d", 
					   userid, sid, pvid, real_type);
			  return;
	      }
	   }
	   if (ProviderServer::GetInstance()->Send(itor->second,gnet::link::Dispatch(sid,psession->getUserId(),type,os)))
	   {
			log.debug("LinkServer:DispatchUnknownProtocol:user(id=%d,sid=%d) send Dispatch ok,pvid=%d,type=%d",
					  userid, sid, pvid, real_type);
			if(stat_open)
			{
				ProtocolStat::GetInstance()->update_c2g_map(real_type);
				ProtocolStat::GetInstance()->update_c2g_num(os.size());
			}
			return;
	   }
	   else
	   {
		  log.warn("LinkServer:DispatchUnknownProtocol:user(id=%d,sid=%d) send Dispatch failed,pvid=%d,type=%d,close client",
				   userid, sid, pvid, real_type);
	      //xxx这里需要区分发送错误，provider_sid不存在才关闭，需要Protocol::Manager能返回这个信息
	      OnDelSession(sid,GNET::CLOSE_ACTIVE);
	      Close(sid);
	      return;
	   }
	}

	void LinkServer::Update()
	{
	   std::set<Broadcaster*> broadcasters_copy = this->broadcasters; // allow erase in loop
	   for (std::set<Broadcaster*>::iterator it = broadcasters_copy.begin()
		 , end = broadcasters_copy.end(); it != end; ++it)
	      (*it)->update(this);
	   if(1 == this->session_state_check)
	   {
	      static int checktimer = TIMER_INTERVAL;
	      if (checktimer--) return;
	      CheckSessionTimePolicy();
	      checktimer = TIMER_INTERVAL;
	   }
	}

	void LinkServer::Broadcast(int ptype, const GNET::Octets & pdata, int time)
	{

	   int onlines = this->sessions.size();
	   if (onlines <= 0) return; // nobody online. discard message
	   if (time <= 1000)
	   {
	      // broadcast now
	      const glinkd::ProtocolBox box(ptype, pdata);
	      for (LinkSessions::links_map::iterator it = this->sessions.begin(),
		    end = this->sessions.end(); it != end; ++it)
	      {
		 if (it->second->getLogin() == gnet::link::SetLogin::eLogin)
		 {
		    this->Send(it->first, box);
		 }
	      }
	   }
	   else
	   {
	      broadcasters.insert(new Broadcaster(ptype, pdata, time, sessions.begin()->first, onlines));
		  log.info("LinkServer:Broadcast:assign mem:ptype=%d,data.size=%d,time=%d", ptype, pdata.size(), time);
	   }
	   if(stat_open)
	   {
	      ProtocolStat::GetInstance()->update_g2c_map(ptype,onlines);
	      ProtocolStat::GetInstance()->update_g2c_num(pdata.size() * onlines);
	   }
	}

	void LinkServer::Broadcaster::update(LinkServer * ls)
	{
	   int elapsed = this->timer.Elapse_ms();
	   int count = this->remain_hint <= 0 ? ls->GetSessions().size() // newlogin: try broadcast all
	      : this->remain_time <= 0 ? this->remain_hint // timeout: try broadcast remain_hint
	      : (int)((float)elapsed / this->remain_time * this->remain_hint); // percent: at least 1

	   if (count <= 0) return; // update too fast
	   this->timer.Reset();

	   LinkSessions::links_map::iterator it = ls->GetSessions().lower_bound(sid_lower);
	   LinkSessions::links_map::iterator end = ls->GetSessions().end();
	   const glinkd::ProtocolBox box(this->ptype, this->pdata);
	   for (int i = 0; i < count && it != end; ++i, ++it)
	   {
	      if (it->second->getLogin() == gnet::link::SetLogin::eLogin)
		 ls->Send(it->first, box);
	   }

	   if (it != end)
	   {
	      this->sid_lower = it->first; // sid for next update

	      this->remain_time -= elapsed;
	      this->remain_hint -= count;
	   }
	   else
	   {
	      ls->broadcasters.erase(this);
	      delete this;
		  log.info("LinkServer:Broadcast:update:release mem:ptype=%d,data.size=%d", this->ptype, this->pdata.size());
	   }
	}

} 

namespace gnet
{  
	namespace link
	{
		void Send::Process(Manager *, Manager::Session::ID pvid)
		{
			glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
			const glinkd::ProtocolBox box(ptype, pdata);
			GNET::Protocol::Type real_type = GNET::Protocol::typeLow(ptype);
			int userid = 0;
			gnet::link::LinkBroken lb;

			for (std::set<int>::iterator it = linksids.begin(), ie = linksids.end(); it != ie;)
			{
				glinkd::LinkSession * psession = lsm->GetLinkSession(*it);
				if(psession)
				{
					userid = psession->getUserId();
					glinkd::LinkServer::log.debug("Send:Process:user(id=%d,sid=%d) type=%d", userid, *it, real_type);

					if(!lsm->Send(*it, box))
					{
						glinkd::LinkServer::log.info("Send:Process:user(id=%d,sid=%d) send failed,type=%d", userid, *it, real_type);
						lsm->OnDelSession(*it,GNET::CLOSE_ACTIVE);
						lsm->Close(*it);
					}
					it++;
				}
				else
				{
					if(false == lsm->GetLinkBrokenLimit().add(*it))
					{
						if(glinkd::LinkBrokenCache::GetInstance()->getBrokenInfo(*it,lb.userid,lb.reason))
						{
							lb.linksid = *it;
							glinkd::ProviderServer::GetInstance()->Send(pvid, lb);
							glinkd::LinkServer::log.info("Send:Process:user(id=%d,sid=%d,reason=%d) session not exist, send LinKBroken to provider!",lb.userid,*it,lb.reason);
						}
					}
					++it;
					glinkd::LinkServer::log.debug("Send:Process:user(id=%d,sid=%d) session not exist!",lb.userid,*it);
				}
			}
			if(lsm->GetStatOpen())
			{
				glinkd::ProtocolStat::GetInstance()->update_g2c_map(ptype,linksids.size());
				glinkd:: ProtocolStat::GetInstance()->update_g2c_num(pdata.size() * linksids.size());
			}
		}

		void Broadcast::Process(Manager *, Manager::Session::ID )
		{
			glinkd::LinkServer::log.debug("Broadcast:Process:type=%d", GNET::Protocol::typeLow(ptype));
			glinkd::LinkServer::GetInstance()->Broadcast(ptype,pdata,time);
		}

		void SetLogin::Process(Manager *, Manager::Session::ID )
		{
			glinkd::LinkServer::log.info("SetLogin::Process:roleid=%ld,linksid=%d,login=%d", roleid, linksid, login);
			glinkd::LinkSession * psession = glinkd::LinkServer::GetInstance()->GetLinkSession(linksid);
			if (psession)
			{
				//setLogintime when init LinkSession
				return psession->setLogin(login);
			}
		}
	}
}
