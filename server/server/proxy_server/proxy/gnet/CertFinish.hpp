
#pragma once

#include "rpcgen.hpp"
#include "AuthClient.hpp"
#include "DeliverServer.hpp"
#include "common/hardware.h"


namespace gnet { 

class CertFinish : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CertFinish.inc"

	virtual void Process(Manager *manager, Manager::Session::ID sid) 
	{
		 /* protocol handle */
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		GNET::Log::log(LOG_INFO,"=====end CertVerify:CertFinish,aid=%d,zoneid=%d,sid=%d",dsm->GetAid(),dsm->GetZoneid(),sid);
		gdeliverd::AuthClient *acm = gdeliverd::AuthClient::GetInstance();
		acm->SetOSecurity(sid,GNET::ARCFOURSECURITY,acm->osec_key);	
		gnet::AnnounceZoneid3 ann;
		ann.zoneid  = dsm->GetZoneid();
		ann.aid     = dsm->GetAid();
		ann.blreset = acm->GetBlreset();

		int ip1 = 0, ip2 = 0, ip3 = 0;
		std::vector<int>	ips;
		const GNET::IFConfig *ifconfig = GNET::IFConfig::GetInstance();
		std::map< const char *, struct in_addr > map = ifconfig->if2ipv4();
		for ( std::map<const char *, struct in_addr>::iterator it = map.begin(), ie = map.end(); it != ie; ++it )
		   ips.push_back( (int)(*it).second.s_addr ); 
		if( ips.size() >= 1 )	ip1 = ips[0];
		if( ips.size() >= 2 )	ip2 = ips[1];
		if( ips.size() >= 3 )	ip3 = ips[2];
		ann.ip1 = ip1;
		ann.ip2 = ip2;
		ann.ip3 = ip3;

		ann.version = 1;
		if(acm->SendProtocol(ann))
		{
		   GNET::Log::log(LOG_INFO,"send AnnounceZoneid3 to au ok,aid=%d,zoneid=%d",ann.aid,ann.zoneid);

		   gdeliverd::AuthClient::GetInstance()->SetAbroadPolicy(1);
		   acm->bl_keepalive = true;
		}
		else
		   GNET::Log::log(LOG_WARNING,"send AnnounceZoneid3 to au failed,aid=%d,zoneid=%d",ann.aid,ann.zoneid);

	}

};

} 
