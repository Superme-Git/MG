
#pragma once

#include "rpcgen.hpp"

namespace gnet { 
class ACCaptcha : public aio::Protocol
{
public:
	#include "ACCaptcha.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		if (captype == 0)
		{
			VMProtectBeginVirtualization
			unsigned char mask[32] = {
				0x12, 0x97, 0x34, 0x56, 0xe6, 0xae, 0x33, 0xfa,
				0x3f, 0x4a, 0x3e, 0x5b, 0x79, 0x80, 0x31, 0x23,
				0x1f, 0x90, 0x82, 0x99, 0x33, 0x23, 0x65, 0x7e,
				0xab, 0xef, 0x55, 0x78, 0x90, 0x90, 0x33, 0x44
			};
			unsigned char *com = (unsigned char*)captcha.begin();
			size_t comsize =  captcha.size();
			for(size_t n=0; n<comsize; n++)
			{
				unsigned char t = com[n];
				t = (((t & 0x07) << 5) | ((t & 0xf8) >> 3));
				com[n] = t ^ mask[n % 32];
			}

			int pos = 0;
			std::vector< unsigned char > src;
			for(size_t i=0; i<comsize; i+=2)
			{
				unsigned char color = com[i];
				unsigned char cnt = com[i+1];
				for(int j=0; j<cnt; j++)
					src.push_back(color);
			}
			Defence::GetIDefence()->OnCaptcha( captype, src );
			VMProtectEnd
		}
		else
		{

		}
	}

};

} 
