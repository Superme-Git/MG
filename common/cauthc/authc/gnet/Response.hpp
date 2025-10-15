#ifndef __FIRENET_RESPONSE_HPP__
#define __FIRENET_RESPONSE_HPP__

#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_Response_.hpp"

namespace gnet
{ 

	class Response : public aio::Protocol
	{
	public:
		#include "rpcgen/gnet/Response.inc"

		virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

	};

} 

#endif