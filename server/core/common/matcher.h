#ifndef __MATCHER_H
#define __MATCHER_H
#include <iconv.h>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>

#if __GNUC__ <4
	#include <pcre/pcre.h>
#else
	#include <pcre.h>
#endif

#include "thread.h"

namespace GNET
{
#define CDINVALID ((iconv_t)-1)
	using std::string;
	class Matcher
	{
		static Matcher instance;
		Thread::RWLock locker;
		iconv_t cinput;
		iconv_t cvalid;

		string pattern;
		pcre *regexp;

		void close()
		{
			if(cinput!=CDINVALID)
				iconv_close(cinput);
			if(cvalid!=CDINVALID)
				iconv_close(cvalid);
			if(regexp)
				pcre_free(regexp);
			cinput = CDINVALID;
			cvalid = CDINVALID;
			regexp = NULL;
		}

	public:
		Matcher() : locker("Matcher"), cinput(CDINVALID), cvalid(CDINVALID), regexp(NULL)
		{
		}

		~Matcher()
		{
			close();
		}
		int Load(const char* file,const char* in_code="UCS2",const char* check_code="GB2312",const char* table_code="GBK")
		{
			char *in,*out,*buf,*input;
			size_t ilen, olen, flen;
			string line;

			Thread::RWLock::WRScoped l(locker);

			if(!file || !file[0])
				file = "rolename.txt";
			if (access(file, R_OK) != 0)
				return -1;
			close();
			cinput = iconv_open("UTF8",in_code);
			if(cinput==CDINVALID)
				return -1;
			if(check_code && check_code[0])
			{
				cvalid = iconv_open(check_code,in_code);
				if(cvalid==CDINVALID)
					return -1;
			}

			iconv_t cv;
			if(table_code && table_code[0])
				cv = iconv_open("UTF8",table_code);
			else
				cv = iconv_open("UTF8","GBK");
			if(cv==CDINVALID)
				return -1;
			std::ifstream ifs;
			ifs.open(file, std::ios::binary);
			ifs.seekg (0, std::ios::end);
			flen = ifs.tellg();
			ifs.seekg (0, std::ios::beg);
			if(!(input = new char[flen]))
				return -1;
			ifs.read(input, flen);
			ifs.close();

			if(!(buf = new char[flen*4]))
				return -1;
			in = input;
			if(*(unsigned char*)in==0xFF && *(unsigned char*)(in+1)==0xFE)
				in += 2;
			out = buf;
			ilen = flen;
			olen = flen*4;
			int buflen = olen;
			if(iconv(cv,&in,&ilen,&out,&olen)>=0)
			{
				buf[buflen-olen] = 0;
				std::istringstream iss(buf,std::istringstream::in);
				pattern = "";
				while(std::getline(iss, line))
				{
					line.erase(std::find(line.begin(), line.end(), '\r'), line.end());
					if(line.size()>0)
						pattern += "(" + line + ")|";
				}

				if(pattern.size()>0)
				{
					const char *error;
					int erroffset;
					ilen = pattern.size()-1;
					pattern.assign(pattern.c_str(), ilen);
					regexp = pcre_compile(pattern.c_str(),PCRE_CASELESS|PCRE_UTF8,&error,&erroffset,0); 
				}
			}
			delete input;
			delete buf;
			iconv_close(cv);
			return regexp?0:-1;
		}
		static Matcher *GetInstance()
		{
			return &instance; 
		}
		int Match(char* str, int len)
		{
			char buf[256];
			char *in, *out;
			size_t ilen, olen;

			Thread::RWLock::RDScoped l(locker);
			if(!regexp)
				return -1;

			if(cvalid!=CDINVALID)
			{
				in = str;
				out = buf;
				ilen = len;
				olen = 256;
				if(iconv(cvalid,&in,&ilen,&out,&olen)==(size_t)(-1))
					return 2;
			}

			in = str;
			out = buf;
			ilen = len;
			olen = 256;
			iconv(cinput,&in,&ilen,&out,&olen);
			return pcre_exec( regexp, NULL, buf, 256-olen, 0,  PCRE_NO_UTF8_CHECK, NULL,0) >= 0;      
		}
	};

};
#endif
