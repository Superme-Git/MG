#ifndef __GNET_CONV_CHARSET_H
#define __GNET_CONV_CHARSET_H

#include <iconv.h>
#include "octets.h"
namespace GNET
{
	class CharsetConverter
	{
			static size_t conv_charset(iconv_t& cd,const Octets& os_from, Octets& os_to)
			{
				if ((iconv_t)-1==cd) return (size_t) -1;
				if (0==os_from.size()) return (size_t)-1;

				size_t szFrom=os_from.size();
				size_t szTo=szFrom*4,old_szTo;

				char* from=(char*)os_from.begin();
				char* to=(char*)malloc(szTo);
				char* tmp_to=to;

				old_szTo=szTo;
				if ( ((size_t) -1)==iconv(cd,&from,&szFrom,&tmp_to,&szTo) )
				{
					free(to);
					return (size_t)-1;
				}
				os_to.replace(to,old_szTo-szTo);
				free(to);
				return old_szTo-szTo;

			}
			static iconv_t& GetU2G()
			{
				static iconv_t cd=iconv_open("GBK","UCS2");
				return cd;
			}
			static iconv_t& GetG2U()
			{
				static iconv_t cd=iconv_open("UCS2","GBK");
				return cd;
			}

		public:
			static size_t conv_charset_u2g(const Octets& os_from, Octets& os_to)
			{
				return conv_charset(GetU2G(),os_from,os_to);
			}
			static size_t conv_charset_g2u(const Octets& os_from, Octets& os_to)
			{
				return conv_charset(GetG2U(),os_from,os_to);
			}

	};
	
	/*
	static size_t conv_charset(const char* encode_from,const char* encode_to,const Octets& os_from, Octets& os_to)
	{
		
		os_to=os_from;
		return 0;
		
		
		iconv_t cd;
		if ( ((iconv_t) -1) == (cd=iconv_open(encode_to,encode_from)) ) return (size_t)-1;
		if (0==os_from.size()) return (size_t)-1;
		
		size_t szFrom=os_from.size();
		size_t szTo=szFrom*4,old_szTo;
		
		char* from=(char*)os_from.begin();
		char* to=(char*)malloc(szTo);
		char* tmp_to=to;

		old_szTo=szTo;
		if ( ((size_t) -1)==iconv(cd,&from,&szFrom,&tmp_to,&szTo) )
		{
			free(to);
			return (size_t)-1;
		}
		os_to.replace(to,old_szTo-szTo);
		free(to);
		iconv_close(cd);		
		return old_szTo-szTo;
		
	}
	*/
};
#endif	
