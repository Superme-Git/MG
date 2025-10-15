
#pragma once

#include <log4cpp/Category.hh>
#include "rpcgen.hpp"
#include "LinkServer.hpp"

namespace gnet { 

class AnnounceServerAttribute : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AnnounceServerAttribute.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
		log.info("AnnounceServerAttribute:Process:from deliver.");
		glinkd::LinkServer::GetInstance()->SetServerAttr(serverattr);	
	}
	
	static log4cpp::Category& log;
};
log4cpp::Category& AnnounceServerAttribute::log = log4cpp::Category::getInstance(std::string("glinkd"));
} 
