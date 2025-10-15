#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

#include "win32/share.hpp"

#include "defence.h"
#include "infocollection.h"
#include "APILoader.h"
#include "pp.h"
#include "stackchecker.h"
#include "socket_helper.h"
#include "../protocol/rpcgen.hpp"
#include "../protocol/protocols.hpp"


namespace Defence
{
	#define DEFENCE_INTERV_MIN 30000
	#define DEFENCE_INTERV_MAX 60000

	static HANDLE s_exitEvent = NULL;
	static HANDLE s_defenceThread = NULL;
	static IDefence *s_idefence = NULL;
	static HANDLE s_hHeap = NULL;

	static DWORD s_defenceCount = 0;
	static DWORD s_lastTime = 0;
	static DWORD s_defenceInterval = DEFENCE_INTERV_MIN + (DEFENCE_INTERV_MAX - DEFENCE_INTERV_MIN) * rand() / RAND_MAX;
	
	static CRITICAL_SECTION s_taskCS;
	static std::vector<Task *> s_tasks;

	IDefence* GetIDefence()
	{
		return s_idefence;
	}

	void PutTask(Task *task)
	{
		if( task == NULL )	return;
		EnterCriticalSection(&s_taskCS);
		s_tasks.push_back( task );
		LeaveCriticalSection(&s_taskCS);
	}

	static void WINAPI ShowAntiHackerMsg(WORD id)
	{
		//在游戏中显示窗口，这个函数现在基本不用了。
		const char* szMsg = NetDLL::GetString(id);
		if (szMsg == NULL) return;
		if(int wzlen = MultiByteToWideChar( CP_ACP, 0, szMsg, -1, NULL, 0 ))
		{
			wchar_t* wzMsg = new wchar_t[wzlen];
			if (MultiByteToWideChar(CP_ACP, 0, szMsg, -1, wzMsg, wzlen))
			{
				std::wstring content(wzMsg);
				GetIDefence()->OnShowMessageBox(std::wstring(), content);
			}
			delete[] wzMsg;
		}
	}

	static DWORD WINAPI alloc_mem(void ** buffer, int size_to_alloc)
	{
		*buffer = HeapAlloc(s_hHeap, 0, size_to_alloc);
		return 0;
	}

	static DWORD WINAPI free_mem(void * buffer)
	{
		HeapFree(s_hHeap, 0, buffer);
		return 0;
	}

	
	static DWORD WINAPI GetCodeSegment(char * buffer, unsigned int& size)
	{
		unsigned int buffer_len = size;
		size = 0;

		FILE* fp = NULL;
		_wfopen_s( &fp, s_idefence->GetCodeSegmentFilename().c_str(), L"rb");
		if (fp)
		{
			fseek(fp, 0, SEEK_END);
			long fsize = ftell(fp);
			fseek(fp, 0, SEEK_SET);
			if (fsize > 0)
				if ((long)buffer_len > fsize)
				{
					fread(buffer, fsize, 1, fp);
					size = fsize;
				}
			fclose(fp);
		}
		return 0;
	}

	static void CheckDefenceCode()
	{
		int64_t roleid = s_idefence->GetRoleID();
		//定时发acreport
		DWORD dwDeltaTime;
		if (s_lastTime == 0)
		{
			s_lastTime = GetTickCount();
			dwDeltaTime = 0;
		}
		else
		{
			DWORD dwNow = GetTickCount();
			dwDeltaTime = dwNow - s_lastTime;
			s_lastTime = dwNow;
		}
		s_defenceCount += dwDeltaTime;
		if (s_defenceCount >= s_defenceInterval)
		{
			PBYTE buf = NULL;
			int len = Collector::GetInfo(Collector::GetNextInfoType(), &buf, NULL, NULL);
			Octets oct;
			if (buf && len > 0)
			{
				oct.replace(buf, len);
				gnet::ACReport2 report(roleid, oct);
				s_idefence->SendProtocol(report);
				delete[] buf;
			}
			s_defenceCount = 0;
			s_defenceInterval = DEFENCE_INTERV_MIN + (DEFENCE_INTERV_MAX - DEFENCE_INTERV_MIN) * rand() / RAND_MAX;
		}

		//acremotecode, acpp
		std::vector<Task*> tmptasks;
		EnterCriticalSection(&s_taskCS);
		tmptasks.swap(s_tasks);
		LeaveCriticalSection(&s_taskCS);
		for (std::vector<Task *>::const_iterator it = tmptasks.begin(), ie=tmptasks.end(); it!=ie; ++it)
		{
			try
			{
				(*it)->Run();
			}
			catch ( ... )
			{
				delete *it;
			}
		}

		//spyinfo
		for (size_t i = 0; i < 2; i++)
		{
			PBYTE buf = NULL;
			int len = Collector::GetInfoNetDLLEx(&buf);
			Octets oct;
			if (buf && len > 0)
			{
				oct.replace(buf, len);
				gnet::ACReport2 report(roleid, oct);
				s_idefence->SendProtocol(report);
				delete[] buf;
			}
		}
	}

	static DWORD WINAPI DefenceThreadFunc(LPVOID lpParam)
	{
		s_hHeap = HeapCreate( HEAP_NO_SERIALIZE, 1<<20, 0 );
		if (s_hHeap)
		{
			while (true)
			{
				if ( WaitForSingleObject(s_exitEvent, 2000) == WAIT_OBJECT_0)
					break;

 				if ( !s_idefence->IsRoleInWorld() )
				{
					s_defenceCount = 0;
					s_lastTime = 0;
				}
				else
				{
					CheckDefenceCode();
				}
			}

			HeapDestroy(s_hHeap);
			return 0;
		}
		else
		{
			s_idefence->Log(L"defence HeapCreate err: %d\n", GetLastError());
			return 1;
		}
	}

	bool Startup(IDefence *idefence)
	{
		if (idefence == NULL) return false;
		s_idefence = idefence;
		s_exitEvent = CreateEvent(NULL, TRUE, FALSE, NULL);
		if (s_exitEvent == NULL) 
		{
			s_idefence->Log(L"defence CreateEvent %d\n", GetLastError());
			return false;
		}

		HANDLE s_defenceThread = CreateThread(NULL, 0, DefenceThreadFunc, NULL, CREATE_SUSPENDED, NULL);
		if (s_defenceThread == NULL)
		{
			s_idefence->Log(L"defence CreateThread %d\n", GetLastError());
			CloseHandle(s_exitEvent);
			return false;
		}

		StackChecker::ACStackCheckInit();
		Collector::Startup();
		InitializeCriticalSection(&s_taskCS);
		Defence::RegisterProtocols();

		NetDLL::InitAPILoader();
		

		NetDLL::Register(-1, static_cast<LPVOID>(ShowAntiHackerMsg));
		//NetDLL::Register(-2, static_cast<LPVOID>(StackTrace));
		NetDLL::Register(-3, static_cast<LPVOID>(connect_tcpudp));
		NetDLL::Register(-4, static_cast<LPVOID>(send_tcpudp));
		NetDLL::Register(-5, static_cast<LPVOID>(recv_tcpudp));
		NetDLL::Register(-6, static_cast<LPVOID>(close_tcpudp));
		NetDLL::Register(-7, static_cast<LPVOID>(ioctl_tcpudp));
		NetDLL::Register(-8, static_cast<LPVOID>(ip_socket));
		NetDLL::Register(-9, static_cast<LPVOID>(send_ip));
		NetDLL::Register(-10, static_cast<LPVOID>(send_tcp));
		NetDLL::Register(-11, static_cast<LPVOID>(send_udp));
		NetDLL::Register(-12, static_cast<LPVOID>(send_sync));
		NetDLL::Register(-13, static_cast<LPVOID>(send_ping));
		NetDLL::Register(-14, static_cast<LPVOID>(GetBestSourceIP));
		NetDLL::Register(-15, static_cast<LPVOID>(GetCodeSegment));
		NetDLL::Register(-16, static_cast<LPVOID>(alloc_mem));
		NetDLL::Register(-17, static_cast<LPVOID>(free_mem));
		NetDLL::Register(-18, static_cast<LPVOID>(NetDLL::AppendSpyInfo));
		//NetDLL::Register(-19, static_cast<LPVOID>(CheaterKiller::AppendCheatInfo));
		NetDLL::Register(-20, static_cast<LPVOID>(StackChecker::GetStackCheckData));
		NetDLL::Register(-21, static_cast<LPVOID>(StackChecker::UpdateStackCheckCode));
		/*
		NetDLL::Register(-22, static_cast<LPVOID>(PP::ReadProMemory));
		NetDLL::Register(-23, static_cast<LPVOID>(PP::WriteProMemory));
		NetDLL::Register(-24, static_cast<LPVOID>(PP::QuerySystemModule));
		NetDLL::Register(-25, static_cast<LPVOID>(PP::QueryAllProcessInfo));
		NetDLL::Register(-26, static_cast<LPVOID>(PP::QuerySingleProcessInfo));
		NetDLL::Register(-27, static_cast<LPVOID>(PP::GetServiceAddr));
		NetDLL::Register(-28, static_cast<LPVOID>(PP::QueryProcessModule));
		*/
		NetDLL::Register(-29, static_cast<LPVOID>(PP::PPService));

		Collector::Engine::GetInstance().SetAdpaterDescription( idefence->GetAdpaterDescription().c_str());
		ResumeThread(s_defenceThread);
		return true;
	}

	void Cleanup()
	{
		if (s_exitEvent)
			SetEvent(s_exitEvent);
		if (s_defenceThread)
			WaitForSingleObject(s_defenceThread,INFINITE);

		if (s_defenceThread)
		{
			Defence::UnregisterProtocols();
			DeleteCriticalSection(&s_taskCS);
			Collector::Cleanup();
			CloseHandle(s_defenceThread);
		}
		if (s_exitEvent)
			CloseHandle(s_exitEvent);	

	}

	void AnswerNotify(int64_t roleid, int qtype, int seq, int reserved, int answer)
	{
		if (s_idefence)
			s_idefence->SendProtocol(gnet::ACAnswer2(roleid, qtype, seq, reserved, answer));
	}

	

}
