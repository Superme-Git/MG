
#include "DeliveryClient.hpp"
#include "common/timertask.h"

namespace glinkd { 

	DeliveryClient DeliveryClient::instance;
	
	log4cpp::Category& DeliveryClient::log = log4cpp::Category::getInstance(std::string("glinkd"));

	void DeliveryClient::Reconnect()
	{
		GNET::Thread::HouseKeeper::AddTimerTask(new GNET::ReconnectTask(this, 1), backoff);
		backoff *= 2;
		if (backoff > BACKOFF_DEADLINE) backoff = BACKOFF_DEADLINE;
	}

	const GNET::Protocol::Manager::Session::State* DeliveryClient::GetInitState() const
	{
		return &gnet::state::DeliveryClient;
	}

	void DeliveryClient::OnAddSession(Session::ID sid)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		if (conn_state)
		{
			Close(sid);
			return;
		}
		conn_state = true;
		this->sid = sid;
		backoff = BACKOFF_INIT;

		log.info("DeliveryClient:OnAddSession:connect to deliver success,sid=%d", sid);
	}

	void DeliveryClient::OnDelSession(Session::ID sid)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		conn_state = false;
		Reconnect();

		log.info("DeliveryClient:OnDelSession:disconnect from deliver,sid=%d", sid);
	}
	
	void DeliveryClient::OnDelSession(Session::ID sid,int state)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		conn_state = false;
		Reconnect();

		log.info("DeliveryClient:OnDelSession:disconnect from deliver,state=%d,sid=%d", state, sid);
	}

	void DeliveryClient::OnAbortSession(const GNET::SockAddr &sa)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		conn_state = false;
		Reconnect();

		log.info("DeliveryClient:OnAbortSession:connect to deliver failed,sid=%d", sid);
	}

	void DeliveryClient::OnCheckAddress(GNET::SockAddr & sa) const
	{
		// check or setup a new address
	}

} 
