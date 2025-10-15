
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SetServerAttr_.hpp"
#include "mapgameattr.h"

namespace gnet { 

class SetServerAttr : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/SetServerAttr.inc"
	
	void OnServer(Manager *, Manager::Session::ID )
	{
		gnet::SetServerAttrArg* arg = getArgument();
		gnet::SetServerAttrRes* res = getResult();
		GNET::Log::log(LOG_INFO,"-----SetServerAttr-----,gmuserid=%d,attribute=%d,value=%d",arg->gmuserid,arg->attribute,arg->value);
		//不局限于双倍**，可以任意倍
		if(gdeliverd::GameAttrMap::PutAttr(arg->attribute,arg->value))
		{
			res->retcode = GNET::ERR_SUCCESS;
			UpdateServerAttr(arg->attribute,arg->value);
		}
		else
			res->retcode = GNET::ERR_GENERAL;
	}
	
	void AnnounceLink()
	{
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		AnnounceServerAttribute attr;
		attr.serverattr.flags = dsm->GetServerAttr().GetFlags();
		attr.serverattr.load = dsm->GetServerAttr().GetLoad();
		dsm->BroadcastLink(attr);
	}

	void UpdateServerAttr( int attribute,int value)
	{
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		switch ( attribute )
		{
			case gdeliverd::ATTR_EXP:
				{
					dsm->GetServerAttr().SetDoubleExp(value);
					break;
				}
			case gdeliverd::ATTR_MONEY:
				{
					dsm->GetServerAttr().SetDoubleMoney(value);
					break;
				}
			case gdeliverd::ATTR_OBJECT:
				{
					dsm->GetServerAttr().SetDoubleObject(value);
					break;
				}
			case gdeliverd::ATTR_SP://skill point
				{
					dsm->GetServerAttr().SetDoubleSP(value);
					break;
				}
			case gdeliverd::ATTR_LAMBDA://并发连接数
				{
					dsm->GetServerAttr().SetLambda(value);
					break;
				}
			default:
				break;
		}//end of switch	
		AnnounceLink();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::SetServerAttrArg* arg = getArgument();
		//gnet::SetServerAttrRes* res = getResult();
	}

	void OnTimeout()
	{
		// client only
		//gnet::SetServerAttrArg* arg = getArgument();
	}

};

} 
