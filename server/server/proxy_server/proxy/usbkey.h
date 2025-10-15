#pragma once

#include "rpcgen.hpp"

namespace gdeliverd
{
#if defined BYTE_ORDER_BIG_ENDIAN
	inline unsigned int gd_byteorder_32(unsigned int x)
	{
		register unsigned int v;
		__asm__ ("bswapl %0" : "=r"(v) : "0"(x));
		return v;
	}
#else
	#define gd_byteorder_32(x)	(x)
#endif
	class UsbKey
	{
		GNET::Octets	seed;//
		GNET::Octets 	pin;//size can only be 0 or 4
		int		rtime;//divide au's currenttime by 32(when use cache,consider the time difference between au and delivery)
		public:
		UsbKey() {}	
		UsbKey(GNET::Octets& _seed,GNET::Octets& _pin,int _rtime):seed(_seed),pin(_pin),rtime(_rtime) {}
		UsbKey(const UsbKey& r):seed(r.seed),pin(r.pin),rtime(r.rtime) {}

		void GetElecNumber(unsigned char ret[6]/*ret[0] is the first elec number*/)
		{
			unsigned char text[8];
			((int*)text)[1] = ((int*)text)[0] = gd_byteorder_32(rtime);//use Little-Endian(intel-linux is LE)
			GNET::Octets re(text,8);
			GNET::HMAC_MD5Hash hash;
			GNET::Octets os;
			hash.SetParameter(seed);
			hash.Update(re); 
			hash.Final(os);

			const unsigned char *p = (const unsigned char*)os.begin();
			unsigned char pos = p[15] & 0xf;
			if(pos > 11)
				pos = 11;
			const unsigned char *p2 = p + pos;
			for(int i = 0; i < 3 ; ++i)
			{
				unsigned char c = p2[i];
				unsigned char c1 = (c & 0xf) & 7;
				if( p[14] & (1 << (2*i)) ) ++c1;
				if( p[13] & (1 << (2*i)) ) ++c1;
				unsigned char c2 = ((c >> 4) & 0xf) & 7;
				if( p[14] & (1 << (2*i + 1)) ) ++c2;
				if( p[13] & (1 << (2*i + 1)) ) ++c2;

				ret[i*2] = c1;
				ret[i*2+1] = c2;
			}
		}

		void CalcResponse(GNET::Octets& account,GNET::Octets& challenge,GNET::Octets& passwdmd5,GNET::Octets& response,GNET::Octets& used_elec_number)
		{
			if(seed.size() != 16)
			{
				GNET::Log::log(LOG_ERR,"USBKEY2's seed.size invalid,size=%d"+seed.size());
				return;
			}

			//pin can contain number and letter,so is different from elec number
			if(pin.size() != 4 && pin.size() != 0)
			{
				GNET::Log::log(LOG_ERR,"USBKEY2's pin.size invalid,size=%d"+pin.size());
				return;
			}

			GNET::Octets temp;
			if(pin.size() == 4)
			{
				temp.insert(temp.end(),pin.begin(),pin.size());
			}
			unsigned char ret[6];
			GetElecNumber(ret);
			for(int i = 0;i < 6;++i)
			{
				ret[i] += '0';
			}
			used_elec_number.replace(ret,sizeof(ret));
			temp.insert(temp.end(),ret,sizeof(ret));
			/*
			 * 1. first use MD5Hash
			 * 2. second use HMAC_MD5Hash
			 */
			GNET::MD5Hash md5;
			GNET::Octets passwd;
			md5.Update(account);
			md5.Update(temp);
			md5.Final(passwd);
			passwdmd5.replace(passwd.begin(),passwd.size());

			GNET::HMAC_MD5Hash hash;
			hash.SetParameter(passwd);
			hash.Update(challenge);
			hash.Final(response);
		}
	};
}
