#pragma warning( disable : 4786 )

#include <windows.h>
#include <stdio.h>

#include "win32/share.hpp"

#include "pp.h"
#include "defence.h"
#include "VMProtectSDK.h"
#include "PerfectProtector.h"


namespace PP
{

	static unsigned char buf[128];
	static int seq;
	static int pptype;

	void WINAPI Check(int64_t roleid, int pptp, int sq, int key)
	{
		seq = sq;
		pptype = pptp;
		memset(buf, 0, 128);
		int rid;
		VMProtectBeginVirtualization

		rid =  (unsigned int)((roleid >> 32)&0xffffffff ) ^ (unsigned int)(roleid & 0xffffffff);
		_CheckAlive_Client(key, rid, buf);
		VMProtectEnd
		return;
	}

	void WINAPI DumpResult( GNET::Marshal::OctetsStream & os )
	{
		os << pptype;
		os << seq;
		GNET::Octets data(buf, 74);
		os << data;
	}

	void WINAPI PPService(int srvid, void *buf, int bufsz, int key)
	{
		VMProtectBeginVirtualization
			DWORD *dbuf = (DWORD *)buf;
		GACD_Service(srvid, dbuf, bufsz, key);
		VMProtectEnd
	}

	/*
	void WINAPI DumpResult()
	{
		printf("pptype=%d seq=%d buf= ", pptype, seq);
		for (int i=0; i<26; i++)
			printf("%02x ", buf[i]);
		printf("\n");
	}


	
	#define ERR_PARAM 10
	#define ERR_NOMEM 11

	int WINAPI ReadProMemory(int pid, void* readaddr, void* outbuffer, int bufsize, int* realreadsize)
	{

		if (NULL == readaddr || NULL == outbuffer || 0 == bufsize)
			return ERR_PARAM;

		int* inputbuf = NULL;
		int inputsz;
		int key;
		int ret;

		VMProtectBeginVirtualization
		key= 0x189012ff;
		if( Defence::IDefence* idef = Defence::GetIDefence() )
			key = (unsigned int)((idef->GetRoleID() >> 25)&0xffffffff ) ^ (unsigned int)(idef->GetRoleID() & 0xffffffff) ^ key;
		inputsz = 4*sizeof(int) + bufsize;
		inputbuf = (int *)GlobalAlloc(GPTR, inputsz);

		if (inputbuf)
		{
			inputbuf[0] = pid;
			inputbuf[1] = reinterpret_cast<int>(readaddr);
			inputbuf[2] = bufsize;
			inputbuf[3] = reinterpret_cast<int>(&inputbuf[4]);

			GACD_Service(1,inputbuf, inputsz, key)

			ret = inputbuf[0];
			if( (ret==0 || ret==4) && inputbuf[1])
			{
				*realreadsize = inputbuf[1];
				memcpy(outbuffer, &inputbuf[4], inputbuf[1]);
			}
			GlobalFree(inputbuf);
		}
		else
			ret = ERR_NOMEM;
		VMProtectEnd
		
		if ( ret == 4 )
			return 0;
		return ret;
	}

	int WINAPI WriteProMemory(int pid, void* writeaddr, void* buffer, int size, int* realwritesize)
	{

		if (NULL == buffer || 0 == size)
			return ERR_PARAM;

		int* inputbuf = NULL;
		int ibuf[4];
		int key;
		int ret;

		VMProtectBeginVirtualization
		key= 0x198501ab;
		if( Defence::IDefence* idef = Defence::GetIDefence() )
			key = (unsigned int)((idef->GetRoleID() >> 25)&0xffffffff ) ^ (unsigned int)(idef->GetRoleID() & 0xffffffff) ^ key;
		ibuf[0] = pid;
		ibuf[1] = reinterpret_cast<int>(writeaddr);
		ibuf[2] = size;
		ibuf[3] = reinterpret_cast<int>(buffer);		
		inputbuf = &ibuf[0];

		GACD_Service(3,inputbuf,16,key)

		ret = inputbuf[0];
		if( (ret==0 || ret==4) && inputbuf[1])
		{
			*realwritesize = inputbuf[1];
		}
		VMProtectEnd

		if ( ret == 4 )
			return 0;
		return ret;
	}


	static void dump(void* vbuf, int sz)
	{
		FILE *fp= fopen("dmp.log", "wb");
		fwrite(vbuf, sz, 1, fp);
		fclose(fp);
	}
	
	int WINAPI QuerySystemModule(void* outbuffer, int bufsize)
	{
		if (NULL == outbuffer || 0 == bufsize)
			return ERR_PARAM;

		int* inputbuf = NULL;
		int inputsz;
		int key;
		int ret=0;
		
		VMProtectBeginVirtualization
		key= 0x13451281;
		if( Defence::IDefence* idef = Defence::GetIDefence() )
			key = (unsigned int)((idef->GetRoleID() >> 25)&0xffffffff ) ^ (unsigned int)(idef->GetRoleID() & 0xffffffff) ^ key;
		inputsz = 2*sizeof(int) + bufsize;
		inputbuf = (int *)GlobalAlloc(GPTR, inputsz);
		
		if (inputbuf)
		{
			inputbuf[0] = reinterpret_cast<int>(&inputbuf[2]);
			inputbuf[1] = bufsize;

			GACD_Service(5,inputbuf, inputsz, key)

			ret = inputbuf[0];
			//if (ret == 0)
				memcpy(outbuffer, &inputbuf[2], bufsize);
			GlobalFree(inputbuf);
		}
		else
		{
			ret = ERR_NOMEM;
		}
		VMProtectEnd

		return ret;
	}

	int WINAPI QueryAllProcessInfo(void* outbuffer, int bufsize)
	{
		if (NULL == outbuffer || 0 == bufsize)
			return ERR_PARAM;

		int* inputbuf = NULL;
		int inputsz;
		int key;
		int ret = 0;
		
		VMProtectBeginVirtualization
		key = 0x33334444;
		if( Defence::IDefence* idef = Defence::GetIDefence() )
			key = (unsigned int)((idef->GetRoleID() >> 25)&0xffffffff ) ^ (unsigned int)(idef->GetRoleID() & 0xffffffff) ^ key;
		inputsz = 2*sizeof(int) + bufsize;
		inputbuf = (int *)GlobalAlloc(GPTR, inputsz);
		if (inputbuf)
		{
			inputbuf[0] = reinterpret_cast<int>(&inputbuf[2]);
			inputbuf[1] = bufsize;

			GACD_Service(6,inputbuf, inputsz, key)

			ret = inputbuf[0];
			//if (ret == 0)
				memcpy(outbuffer, &inputbuf[2], bufsize);
			GlobalFree(inputbuf);
		}
		else
			ret = ERR_NOMEM;
		VMProtectEnd
		
		return ret;
	}

	int WINAPI QuerySingleProcessInfo(int pid, void* outbuffer, int bufsize)
	{
		if (NULL == outbuffer || 0 == bufsize)
			return ERR_PARAM;

		int* inputbuf = NULL;
		int inputsz;
		int key;
		int ret = 0;

		VMProtectBeginVirtualization
		key = 0x55554444;
		if( Defence::IDefence* idef = Defence::GetIDefence() )
			key = (unsigned int)((idef->GetRoleID() >> 25)&0xffffffff ) ^ (unsigned int)(idef->GetRoleID() & 0xffffffff) ^ key;
		inputsz = 3*sizeof(int) + bufsize;
		inputbuf = (int *)GlobalAlloc(GPTR, inputsz);
		

		if (inputbuf)
		{
			inputbuf[0] = pid;
			inputbuf[1] = reinterpret_cast<int>(&inputbuf[3]);
			inputbuf[2] = bufsize;

			GACD_Service(7,inputbuf, inputsz, key)

			ret = inputbuf[0];
			//if (ret == 0)
				memcpy(outbuffer, &inputbuf[3], bufsize);
			GlobalFree(inputbuf);
			
		}
		else
			ret = ERR_NOMEM;
		VMProtectEnd

		return ret;
	}

	int WINAPI GetServiceAddr(const char* service_name, int* outaddr)
	{
		if(NULL == service_name || NULL == outaddr)
			return ERR_PARAM;

		int ret = 0;
		size_t slen = 0;
		int inputsize = 0;
		int key;
		int* inputbuf = NULL;

		VMProtectBeginVirtualization

		key = 0x19830804;
		if( Defence::IDefence* idef = Defence::GetIDefence() )
			key = (unsigned int)((idef->GetRoleID() >> 25)&0xffffffff ) ^ (unsigned int)(idef->GetRoleID() & 0xffffffff) ^ key; 
		slen = strlen(service_name);
		inputsize = max(slen+1, 8);
		inputbuf = (int *)GlobalAlloc(GPTR, inputsize);
		if (inputbuf)
		{
			memcpy(inputbuf, service_name, slen);

			GACD_Service(8,inputbuf, inputsize, key)

			if ( -1 == inputbuf[0] )
				ret = 1;
			else if ( -2 == inputbuf[1] )
				ret = 2;
			else
				*outaddr = inputbuf[0];
			GlobalFree(inputbuf);
		}
		else
			ret = ERR_NOMEM;

		VMProtectEnd
		
		return ret;
	}

	int WINAPI QueryProcessModule(int pid, void* outbuffer, int bufsize)
	{
		if (NULL == outbuffer || 0 == bufsize)
			return ERR_PARAM;

		int* inputbuf = NULL;
		int inputsz;
		int key;
		int ret = 0;

		VMProtectBeginVirtualization
			key = 0x33994444;
		if( Defence::IDefence* idef = Defence::GetIDefence() )
			key = (unsigned int)((idef->GetRoleID() >> 25)&0xffffffff ) ^ (unsigned int)(idef->GetRoleID() & 0xffffffff) ^ key;
		inputsz = 3*sizeof(int) + bufsize;
		inputbuf = (int *)GlobalAlloc(GPTR, inputsz);
		if (inputbuf)
		{
			inputbuf[0] = pid;
			inputbuf[1] = reinterpret_cast<int>(&inputbuf[3]);
			inputbuf[2] = bufsize;

			GACD_Service(9,inputbuf, inputsz, key)

			ret = inputbuf[0];
			//if (ret == 0)
				memcpy(outbuffer, &inputbuf[3], bufsize);
			GlobalFree(inputbuf);

		}
		else
			ret = ERR_NOMEM;
		VMProtectEnd

		return ret;
	}
	*/


}
