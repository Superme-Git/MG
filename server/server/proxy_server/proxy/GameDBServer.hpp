
#pragma once

#include <log4cpp/Category.hh>
#include "rpcgen.hpp"
#include "speedlimit.h"
#include "mappasswd.h"
#include "rpcgen/gnet/MerchantDiscount.hpp"

namespace gdeliverd { 

class GameDBServer : public GNET::Protocol::Manager
{
public:
	static log4cpp::Category& log;
private:
	size_t 		accumulate_limit;
	Session::ID 	db_sid;

	typedef SpeedLimit<GNET::Octets,OctetsCompare>	OctetsLimit;	
	OctetsLimit	addcashlimit;//instant addcash input invalid passwd exceed limit

	typedef std::set<unsigned int>	 UIntSet;	
	typedef UIntSet::iterator 	 UIntIterator;
	UIntSet				 dbsidset;

	protected:
	virtual const Session::State *GetInitState() const;
	virtual bool OnCheckAccumulate(size_t size) const {
		return accumulate_limit == 0 || size < accumulate_limit;
	}
	virtual void OnAddSession(Session::ID sid);
	virtual void OnDelSession(Session::ID sid);
	virtual void OnDelSession(Session::ID sid,int state);
	public:
	
	GameDBServer() : accumulate_limit(0),is_db_connected(false) { }
	
	bool is_db_connected;	
	std::vector<gnet::MerchantDiscount> discount;
	
	static GameDBServer *GetInstance()
	{
	    static GameDBServer instance;
	    return &instance; 
	}
	virtual std::string Identification() const  	{ return "GameDBServer"; }
	void SetAccumulate(size_t size)     		{ accumulate_limit = size; }
	bool SendProtocol(const GNET::Protocol & p) 	{ return Send(db_sid, p); }
	bool SendProtocol(const GNET::Protocol * p) 	{ return Send(db_sid, p); }

	Session::ID GetSid() const {return db_sid;}
	OctetsLimit & GetAddcashLimit() 	{ return addcashlimit; }
	void LimitConfig(int window=300,int high=3)
	{
		addcashlimit.config(window,high);
		log.info("GameDBServer:LimitConfig:window=%d,high=%d", window, high);
	}
};

} 
