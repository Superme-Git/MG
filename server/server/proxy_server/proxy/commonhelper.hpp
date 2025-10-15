#pragma once

#include "rpcgen.hpp"

namespace gdeliverd {

class CommonHelper
{
	public:
		CommonHelper() { }
		static int getIntPeer(const GNET::SockAddr& peer)
		{
			return ((const struct sockaddr_in*)peer)->sin_addr.s_addr;
		}

		static char * getStringPeer(const GNET::SockAddr& peer)
		{
			 return inet_ntoa(((const struct sockaddr_in*)peer)->sin_addr);
		}

};
}

