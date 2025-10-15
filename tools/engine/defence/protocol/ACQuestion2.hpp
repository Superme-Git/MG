
#pragma once

#include "rpcgen.hpp"
#include "ACAnswer2.hpp"
#include <sstream>

#include "../src/VMProtectSDK.h"


namespace gnet { 

class ACQuestion2 : public aio::Protocol
{
public:
	#include "ACQuestion2.inc"

	virtual void Process(Manager *manager, Manager::Session::ID sid ) 
	{ 
		VMProtectBeginVirtualization		
		if ( question.size() == 0)
		{
			Defence::GetIDefence()->Log( L"question size err\n");
			return;
		}
		if ( Defence::GetIDefence()->GetRoleID() != roleid)
		{
			Defence::GetIDefence()->Log( L"question roleid err: server=%llu, local=%llu\n", roleid, Defence::GetIDefence()->GetRoleID());
			return;
		}
		if ( 3 == qtype )
		{
			const Octets& data = question[0];
			size_t size =  data.size();
			static unsigned char mask[32] = {
				0x1f, 0x90, 0x82, 0x99, 0x33, 0x23, 0x65, 0x7e,
				0x78, 0x89, 0x12, 0x56, 0xe9, 0x8e, 0x32, 0xf1,
				0xf4, 0xe9, 0xe4, 0x3b, 0x88, 0x23, 0x11, 0xa5,
				0x3f, 0x4a, 0x3e, 0x5b, 0x79, 0x80, 0x31, 0x23
			};
			unsigned char *szInfo = (unsigned char*)data.begin();
			for(size_t n=0; n< size; n++)
			{
				unsigned char t = szInfo[n];
				szInfo[n] = (((t & 0x1f) << 3) | ((t & 0xe0) >> 5));
				szInfo[n] = szInfo[n] ^ mask[n % 32];
			}

			std::vector< std::vector<std::pair<unsigned char, unsigned char> > > pic;
			unsigned char* p = szInfo;
			int pathcnt = *(int *)p;
			p+=4;
			//printf("%d paths\n", pathcnt);
			for (int i=0; i<pathcnt; i++)
			{
				unsigned char pointcnt = *p++;
				std::vector< std::pair<unsigned char, unsigned char> > pts;
				//printf("%d path has point %d\n", i, pointcnt);
				for (unsigned char j=0; j<pointcnt; j++)
				{
					unsigned char x = *p++;
					unsigned char y = *p++;
					pts.push_back( std::pair<unsigned char, unsigned char>(x, y) );
				}
				pic.push_back(pts);
			}

			// 因为p可能不以0结尾，所以，这里需要指定长度copy
			std::wstring askedqus((wchar_t *)p, (szInfo+size-p)/2);
			//wprintf(L"%s\n", askedqus.c_str());
			Defence::GetIDefence()->OnQuestion(roleid, qtype, seq, reserved, askedqus, pic );

		}
		else if ( 4 == qtype )
		{
			unsigned char *capdat = (unsigned char*)question[0].begin();
			size_t capsize =  question[0].size();

			const int width = 240, height = 120;
			unsigned char pixels[width*height];
			memset(pixels, 255, width*height);
			for (size_t i=0, y=0; i<capsize; y++)
			{
				unsigned char lineblackcnt = capdat[i++];
				for(unsigned char j=0; j<lineblackcnt; j++)
				{
					unsigned char startx = capdat[i++];
					unsigned char lenx = capdat[i++];
					memset(pixels+y*width+startx, 0, lenx);
				}
			}

			Defence::GetIDefence()->OnHzQuestion(roleid, qtype, seq, reserved, pixels);
		}

		VMProtectEnd
		// 为了暴露问题，取消异常捕获代码
		//catch( ...)
		//{
		//	const int bsize = 8;
		//	wchar_t					buffer[8];
		//	std::wstringstream		wss;	
		//	size_t sz = question[0].size();
		//	unsigned char* data = (unsigned char*)question[0].begin();

		//	for(size_t i= 0; i<sz; i++)
		//	{
		//		swprintf_s( buffer, bsize, L"%02x ", data[i]);
		//		wss << buffer;
		//	}
		//	
		//	Defence::GetIDefence()->Log( L"question err : %s\n", wss.str().c_str());
		//}

	}

};

} 
