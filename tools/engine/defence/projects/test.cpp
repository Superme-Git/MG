#include <tchar.h>
#include <string>
#include <iostream>
#include <sstream>
#include <cstdlib>
#include <eh.h>


#include "win32/share.hpp"
#include "../idefence.h"
#include "../src/pp.h"

#include "PerfectProtector.h"





struct PROCESS_INFO{
	DWORD dwPid;
	DWORD dwPeb;
	DWORD dwExitStatus;
	char  ImageFileName[16];
};

struct SYSTEM_MODULE_INFO{
	DWORD EntrySize;
	DWORD DllBase;
	DWORD SizeOfImage;
	DWORD EntryPoint;
	DWORD CheckSum;
	DWORD TimeDateStamp;
	DWORD Flags;
	DWORD FullDllName;
};

struct DEBUG_INFO{
	DWORD dwTargetPid;
};

struct SINGLE_PROCESS_INFO{
	DWORD dwPid;
	DWORD lpEprocess;
	DWORD dwPeb;
	DWORD dwExitStatus;
	DWORD dwFid;
	DWORD szCmdLine;
	DWORD ImagePathName;
	DWORD dwNumberOfThreads;
	DWORD dwNumberOfModules;
	DWORD lpModuleInfo;	//指向SINGLE_Module_INFO结构
	//一系列SINGLE_THREAD_INFO结构
	//szCmdLine指向的unicode字符串
	//ImagePathName指向的unicode字符串
	//一系列SINGLE_Module_INFO结构
};

struct SINGLE_THREAD_INFO{
	DWORD dwTid;
	DWORD dwWin32StartAddr;
	DWORD dwTeb;
};

struct SINGLE_MODULE_INFO{
	DWORD dwBase;
	DWORD dwSize;
	DWORD szName;
};


//return value 1: checksum err, 2: size err, 0:ok
//;加密完：header|len|data|checksum|dwVar,		26个字节
//;header										4字节
//;len											1字节
//;其中data的明文是 时间戳|版本|发布号|较验数据	16字节
//;checksum										1字节
//;dwVar,用于解密								4字节
static int decryptPP(unsigned int serverkey, unsigned int roleid, unsigned char *buf, size_t bufsize, 
					 int &timestamp, int &version, int &checksum, int &last, int &tickcount1, int &tickcount2)
{
	static char decrptbuf[128] = {0};

	memset(decrptbuf, 0, 128 );
	const int HEADER = 0x3f193a51;
	if (bufsize < 74)
		return 2;

	unsigned int var = ( *(int *)&buf[70] ) ^ 0x20071224;
	unsigned int key = (serverkey ^ roleid) - var;
	key = (key << 8) +  ( (key >> 24));

	
	int head = (var<<3) ^ (*(int *)&buf[2]) ^ (key>>1) ^ HEADER;
	if (head) return 1; 

	unsigned char len = (buf[0]+4) ^ (key&0xff) ^  (var&0xff );
	if (len == 0) return 1;

	if (len != 64)
		return 2;

	unsigned char cksum = buf[1]; 
	buf[1] = 0;
	*(int *)&buf[6+len] = key;

	int rl = len + 10 ;
	for (int i=0; i<rl; i++)
	{
		unsigned char mrl = ( (rl-i)&0xff) % 8;
		unsigned char s = buf[i] ;
		s = (s<<mrl) + (s>>(8-mrl));
		cksum ^= s;
	}
	*(int *)&buf[6+len] = 0;
	if (cksum) return 1;

	int j=0;
	for (int i=len-1; i>=0; i--)
	{
		unsigned char s = buf[6+i] ^ (key&0xff); 
		key = (key << 8) +  ( (key >> 24) );
		j++;
		if (j==1)
			s ^= 0xdc;
		else if (j==2)
			s ^= 0x73;
		else if(j==3)
			s ^= 0x49;
		else
			s ^= decrptbuf[i+3];
		decrptbuf[i] = s;
	}

	int *dbuf = (int *)decrptbuf;
	timestamp = dbuf[0];
	version = dbuf[1];
	checksum = dbuf[2];
	last = dbuf[3];
	tickcount1 = dbuf[4];
	tickcount2 = dbuf[5];
	return 0;
}

void dumpPP()
{
	srand( (unsigned)time( NULL ) );
	union {
		struct{int rl; int rr;};
		int64_t r;
	} roleid;
	roleid.rl = rand();
	roleid.rr = rand();
	int pptype=1;
	int seq = rand();
	int key = rand();
	printf("pp check : rid:%lld, pptype:%d, seq:%d, key:%d\n", roleid.r,  pptype, seq, key);
	

	PP::Check(roleid.r, pptype, seq, key);
	GNET::Marshal::OctetsStream	os;
	PP::DumpResult( os );
	int rpptype=0;
	int rseq=0;
	GNET::Octets data;
	os >> rpptype >> rseq >> data;
	printf("pp answer: rpptype: %d, rseq: %d, datasize: %d, data: \n", rpptype, rseq, data.size());
	data.dump();

	GNET::Octets data2 = data;

	unsigned int rid = (unsigned int)(roleid.rl ^ roleid.rr);
	unsigned char* buf = (unsigned char *)data.begin();
	unsigned int var = ( *(int *)&buf[70] ) ^ 0x20071224;
	unsigned int skey = (key ^ rid) - var;
	unsigned int output[64]={0};
	int cret = 0;
	
	__asm{
		mov		ecx,skey
		rol		ecx,8
		mov		edx,var
		mov		esi,buf
		lea		edi,output
		Common_Decrypt(a)
		mov		cret,eax
	}
	
	printf("common_decrypt: ret: %d, timestamp: %08x, version: %08x, checksum: %08x, last: %08x tickcount1: %08x, tickcount2: %08x\n", cret, output[0], output[1], output[2], output[3], output[4], output[5]);
	int timestamp=0, version=0, checksum=0, last=0, tickcount1=0, tickcount2=0;
	int ret = decryptPP(key, (unsigned int)(roleid.rl ^ roleid.rr), (unsigned char *)data2.begin(), data2.size(), timestamp, version, checksum, last, tickcount1, tickcount2);
	printf("pp decrpted:    ret: %d, timestamp: %08x, version: %08x, checksum: %08x, last: %08x tickcount1: %08x, tickcount2: %08x\n", ret, timestamp, version, checksum, last, tickcount1, tickcount2);
	

	if (cret == 0) //cret 返回1表示正确
		printf("common_decrypt err!\n");
	else if (ret != 0)
		printf("decryptpp err!\n");
	else{
		if (output[0] == timestamp && output[1] == version && output[2] == checksum && output[3] == last && output[4] == tickcount1 && output[5] == tickcount2)
			printf("compare equal, ok!\n\n");
		else{
			printf("compare diff, err!\n\n");
		}
	}
}

void dumpChecksum()
{
	int rd=0;
	PP::PPService(11, &rd, sizeof(int), 11);
	printf("random: %d\n", rd);

	int rd2=0;
	PP::PPService(11, &rd2, sizeof(int), 1111);
	printf("random: %d\n", rd2);

	int csum[2];
	csum[0] = 0; 
	csum[1] = 0;
	PP::PPService(13, csum, 2*sizeof(int), 22);
	printf("checksum: %x, imagebase: %x\n", csum[0], csum[1]);

	int csum2[2];
	csum2[0] = 0; 
	csum2[1] = 0;
	PP::PPService(13, csum2, 2*sizeof(int), 2222);
	printf("checksum: %x, imagebase: %x\n", csum2[0], csum2[1]);

}

#define ALLOCSIZE 30000

void shell()
{
	printf("testing pp functions\nuse help | ?\n");
	static int attached_pid = GetCurrentProcessId();
	bool quit = false;
	while( !quit )
	{
		printf(">");
		std::string line;
		std::getline(std::cin, line) ;
		std::istringstream iss(line);
		std::vector<std::string> argv;
		std::string tmp;
		while ( iss >> tmp )
		{
			argv.push_back(tmp);
		}
		size_t argc = argv.size();
		if (argc == 0) continue;
		std::string cmd = argv[0];
			
		if ( cmd == "quit" || cmd == "q")
		{
			quit = true;
		}
		else if( cmd == "help" || cmd == "?" )
		{
			fprintf(stderr, "cmds:\n\tlm\n\ttlist [pid] [.]\n\tdb|dw|dd addr [len]\n\twb addr writestr\n\tssdt [servicename]\n\tpp\n\tcksum\n\tquit|q\n\thelp|?\n");
		}
		/*
		else if (cmd == "lm")
		{
			
			char buf[ALLOCSIZE];
			int r= PP::QuerySystemModule(buf, ALLOCSIZE);
			if ( 0 == r )
			{
				int cnt = *(PDWORD)buf;
				SYSTEM_MODULE_INFO* modinfo=(SYSTEM_MODULE_INFO*)(buf+4);
				printf("dllbase  imagsize entrypoint checksum timstamp  flags   fullDllName\n");
				while(cnt--)
				{
					wprintf(L"%8x %8x %8x %08x %08x %08x %s\n", modinfo->DllBase, modinfo->SizeOfImage, modinfo->EntryPoint, modinfo->CheckSum, modinfo->TimeDateStamp, modinfo->Flags, &(modinfo->FullDllName) );
					modinfo=(SYSTEM_MODULE_INFO*)((char *)modinfo + modinfo->EntrySize);
				}
			}else{
				printf("err %d\n", r);
			}
		}
		else if (cmd == "tlist")
		{
			if (argc == 1 )
			{
				char buf[ALLOCSIZE];
				int r = PP::QueryAllProcessInfo(buf, ALLOCSIZE);
				if ( 0 == r )
				{
					int cnt = *(PDWORD)buf;
					PROCESS_INFO* proinfo = (PROCESS_INFO*) (buf+4);
					printf("pid      peb      exitstate imagefilename\n");
					while(cnt--)
					{
						printf("%8d %08x %08x %s\n",proinfo->dwPid,proinfo->dwPeb,proinfo->dwExitStatus,proinfo->ImageFileName);
						proinfo++;
					}
				}
				else{
					printf("err %d\n", r);
				}
			}
			else if (argc == 2)
			{
				int pid = strtol(argv[1].c_str(), 0, 0);
				attached_pid = pid;
				//printf("req pid: %d\n", pid);
				char buf[ALLOCSIZE];
				int r = PP::QuerySingleProcessInfo(pid, buf, ALLOCSIZE);
				if ( 0 == r )
				{
					SINGLE_PROCESS_INFO* info = (SINGLE_PROCESS_INFO*)buf;
					wchar_t* cmdline = NULL;
					wchar_t* imgname = NULL;

					if(info->szCmdLine)
						cmdline = (wchar_t*)&buf[info->szCmdLine];
					if(info->ImagePathName)
						imgname = (wchar_t*)&buf[info->ImagePathName];
					printf("Pid:\t %d \nEproces:\t %x\nPeb:\t %x\nFid:\t %d\nExitStatus:\t %x\n",info->dwPid,info->lpEprocess,info->dwPeb,info->dwFid,info->dwExitStatus);
					wprintf(L"Cmdline:\t %s\nImagePath:\t %s\n", cmdline, imgname);

					printf("thread:\t %d\nmodule:\t %d\n", info->dwNumberOfThreads, info->dwNumberOfModules);
					printf("tid    win32startaddr teb\n");
					SINGLE_THREAD_INFO *tinfo=(SINGLE_THREAD_INFO *)(info+1);
					for(unsigned int i=0; i<info->dwNumberOfThreads; i++)
					{
						printf("%d %08x %08x\n",tinfo[i].dwTid,tinfo[i].dwWin32StartAddr,tinfo[i].dwTeb);
					}

					
					//显示模块
					printf("base    size    name\n");
					SINGLE_MODULE_INFO *lpModule=(SINGLE_MODULE_INFO *)&buf[info->lpModuleInfo];
					while(info->dwNumberOfModules--)
					{
						printf("%08x %08x %s\n",lpModule->dwBase,lpModule->dwSize,&lpModule->szName);
						lpModule=(SINGLE_MODULE_INFO *)((char *)lpModule+strlen((char*)&lpModule->szName)+9);
					}
				

				}
				else{
					printf("query pid %d err %d\n", pid, r);
				}
			}
			else
			{
				int pid = strtol(argv[1].c_str(), 0, 0);
				attached_pid = pid;
				//printf("req pid: %d\n", pid);
				char buf[ALLOCSIZE];
				int r = PP::QueryProcessModule(pid, buf, ALLOCSIZE);
				if ( 0 == r )
				{
					int modcnt = *(int *)buf;
					SINGLE_MODULE_INFO *lpModule=(SINGLE_MODULE_INFO *)&buf[4];
					printf("modcnt: %d\nbase    size    name\n", modcnt);
					while(modcnt--)
					{
						printf("%08x %08x %s\n",lpModule->dwBase,lpModule->dwSize,&lpModule->szName);
						lpModule=(SINGLE_MODULE_INFO *)((char *)lpModule+strlen((char*)&lpModule->szName)+9);
					}
				}
				else{
					printf("querymodule pid %d err %d\n", pid, r);
				}

			}
				
		}
		else if( cmd=="db" || cmd =="dw" || cmd=="dd" )
		{
			void* addr = NULL;
			int bcnt = cmd=="dd"?4:(cmd=="dw"?2:1);
			int len = 0;
			if (argc > 1) addr = reinterpret_cast<void *> ( strtoul(argv[1].c_str(), 0, 16) );
			if (argc > 2) len = strtol(argv[2].c_str(), 0, 0);
			if(len) len *= bcnt; else len=16;
			
			char *buf = (char *)malloc(len);
			if (NULL == buf) continue;
			int realread = 0;
			printf("read addr: %08p len: %d bcnt %d\n", addr, len, bcnt);
			int r = PP::ReadProMemory(attached_pid, addr, buf, len, &realread);
			if (0 == r)
			{
				if (bcnt == 1)
					for(int i = 0; i < realread; i++)
						fprintf(stderr, "%02x ", (unsigned char)buf[i]);
				else if(bcnt == 2)
					for(int i = 0; i < realread/2; i++)
						fprintf(stderr, "%04x ", *(PWORD)&buf[i*2]);
				else if(bcnt == 4)
					for(int i = 0; i < realread/4; i++)
						fprintf(stderr, "%08x ", *(PDWORD)&buf[i*4]);
				fprintf(stderr, "\n");
			}
			else{
				fprintf(stderr, "err %d\n", r);
			}

			free(buf);
		}
		else if (cmd=="wb")
		{
			void* addr = NULL;
			char writestr[1024] = {'\0'};
			if (argc > 2){ addr = (void *)strtoul(argv[1].c_str(), 0, 16); strncpy(writestr, argv[2].c_str(), 1023);}
			int realwrite = 0;
			int r = PP::WriteProMemory(attached_pid, addr, writestr, (int)strlen(writestr), &realwrite);
			if (0 == r)
			{
				fprintf(stderr, "realwrite: %d\n", realwrite);
			}
			else{
				fprintf(stderr, "err %d\n", r);
			}
		}
		else if( cmd == "ssdt" )
		{
			const char* funcname = NULL;
			if (argc > 1) funcname = argv[1].c_str();

			if (funcname)
			{
				int funcaddr = 0;
				int r = PP::GetServiceAddr(funcname, &funcaddr);
				if (0 == r)
				{
					fprintf(stderr, "%s\t : %08x ", funcname, funcaddr);
					fprintf(stderr, "\n");
				}
				else{
					fprintf(stderr, "err %d\n", r);
				}
			}
			else
			{
				printf("todo: show all ssdt\nu can ssdt keservicedescriptortable\n");
			}
		}
		*/
		else if( cmd == "pp" )
		{
			dumpPP();
		}
		else if( cmd == "cksum")
		{
			dumpChecksum();
		}
	}
}





class MyDefence : public Defence::IDefence
{
public:
	virtual void OnQuestion(int64_t roleid, int qtype, int seq, int reserved, const std::wstring &ask, const std::vector< std::vector<std::pair<unsigned char, unsigned char> > >  &pic )
	{

	}

	virtual void OnCaptcha(int captype, const std::vector< unsigned char > &pixels)
	{

	}
	virtual void OnShowMessageBox(const std::wstring &title, const std::wstring &content)
	{
		std::cout << "OnShowMessageBox " << std::endl;
		std::wcout.imbue(std::locale("chs"));
		std::wcout << title.c_str() << content.c_str() << std::endl;
	}
	virtual bool IsRoleInWorld()
	{
		return true;
	}
	virtual int64_t	 GetRoleID() 
	{
		return 1;
	}
	virtual bool SendProtocol(const aio::Protocol &protocol)
	{
		return true;
	}

	virtual const std::wstring GetCodeSegmentFilename() 
	{
		return L"abc";
	}
	virtual const std::string GetAdpaterDescription()
	{
		return "acard";
	}
	virtual void Log(const wchar_t* str, ...)
	{

	}
	virtual void OnHzQuestion( int64_t roleid, int qtype, int seq, int reserved, unsigned char* pixels )
	{

	}
};



//from <<progammer applications for microsoft windows>>
class CSE 
{
public:
	static void MapSEtoCE() { _se_translator_function old = _set_se_translator( TranslateSEtoCE ); }
	static void Restore() { _set_se_translator( old ); }
	operator DWORD() { return er.ExceptionCode; }

private:
	static _se_translator_function old;
	EXCEPTION_RECORD er;

	CSE(PEXCEPTION_POINTERS pep) : er(*pep->ExceptionRecord) {}
	static void _cdecl TranslateSEtoCE(unsigned int, PEXCEPTION_POINTERS pep)
	{
		throw CSE(pep);
	}
};

int main(int argc, char* argv[])
{

	TCHAR curdir[MAX_PATH] = {0};
	int len = GetCurrentDirectory(MAX_PATH, curdir);
	if ( 0 == len || len >= MAX_PATH-1 ) 
		return 1;

	curdir[len] = _T('\\');
	_tprintf(_T("curdir: %s\n"), curdir);

	if (int r = Protector::StartProtector(curdir))
	{
		printf("start pp err: %d\n", r);
		return 2;
	}

	
	//test seh
	CSE::MapSEtoCE();

	try{
		int a=0;
		a=10/a;
		printf("a=%d\n", a);
	}
	catch( CSE se ){
		int code = se;
		printf("exception code1: %x\n", code);
	}

	try{
		*(PBYTE)0 = 0;
	}
	catch( CSE se ){
		DWORD code = se;
		printf("exception code2: %x\n", code);
	}

	try{
		int a=1;
	}
	catch( CSE se ){
		DWORD code = se;
		printf("exception code3: %x\n", code);
	}

	

	printf("start pp ok\n");
	int r = 0;
	Defence::IDefence *id = new MyDefence();
	if ( Defence::Startup(id) )
	{
		printf("start defence ok\n\nrunning...\n\n");
		dumpPP();
		dumpChecksum();


		const char* szMsg = "测试wstring。";
		if(int wzlen = MultiByteToWideChar( CP_ACP, 0, szMsg, -1, NULL, 0 ))
		{
			wchar_t* wzMsg = new wchar_t[wzlen];
			if (MultiByteToWideChar(CP_ACP, 0, szMsg, -1, wzMsg, wzlen))
			{
				std::wstring content(wzMsg);
				id->OnShowMessageBox(std::wstring(), content);
			}
			delete[] wzMsg;
		}

		shell();

		Defence::Cleanup();
		printf("cleanup defence\n");
	}
	else
	{
		printf("start defence err\n");
		r = 3;
	}

	delete id;

	Protector::StopProtector();
	printf("stop pp\n");

	return r;
}