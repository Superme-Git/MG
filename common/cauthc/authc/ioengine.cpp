#if (defined WIN7_32)
#include <WS2tcpip.h>
#endif
#include "win32/share.hpp"
#include <errno.h>

#include "ioengine.h"
#include "activeio.h"
#include "../../platform/platform//thread.h"

namespace aio {
    
#ifdef OS_IOS
    static int getaddrinfo_compat(
                                   const char * hostname,
                                   const char * servname,
                                   const struct addrinfo * hints,
                                   struct addrinfo ** res
                                   ) {
        int    err;
        int    numericPort;
        
        // If we're given a service name and it's a numeric string, set `numericPort` to that,
        // otherwise it ends up as 0.
        
        numericPort = servname != NULL ? atoi(servname) : 0;
        
        // Call `getaddrinfo` with our input parameters.
        
        err = getaddrinfo(hostname, servname, hints, res);
        
        // Post-process the results of `getaddrinfo` to work around <rdar://problem/26365575>.
        
        if ( (err == 0) && (numericPort != 0) ) {
            for (const struct addrinfo * addr = *res; addr != NULL; addr = addr->ai_next) {
                in_port_t *    portPtr;
                
                switch (addr->ai_family) {
                    case AF_INET: {
                        portPtr = &((struct sockaddr_in *) addr->ai_addr)->sin_port;
                    } break;
                    case AF_INET6: {
                        portPtr = &((struct sockaddr_in6 *) addr->ai_addr)->sin6_port;
                    } break;
                    default: {
                        portPtr = NULL;
                    } break;
                }
                if ( (portPtr != NULL) && (*portPtr == 0) ) {
                    *portPtr = htons(numericPort);
                }
            }
        }
        return err;
    }
#endif

	class ConnectTask : public aio::Runnable
	{
		FireNet::Connector		m_param;
	public:
		ConnectTask( FireNet::Connector mParam)
			: m_param( mParam)
		{}
		virtual ~ConnectTask() {}
		virtual void run()
		{
			FireNet::SockAddr serveraddr;
			struct addrinfo hints;
			struct addrinfo *result;

			memset(&hints, 0, sizeof(struct addrinfo));
			hints.ai_family = AF_UNSPEC;
			hints.ai_socktype = SOCK_STREAM;

#ifdef OS_IOS
			int retcode = getaddrinfo_compat( m_param.host.c_str(), m_param.port.c_str(), &hints, &result);
			if( 0 == retcode && NULL != result)
			{
				serveraddr = FireNet::SockAddr( *result->ai_addr);
                FireNet::GetNetIO()->ConnectTo2(serveraddr, m_param, result);
                freeaddrinfo(result);
			}
			else
			{
                struct sockaddr_in6 inetaddr;
                memset(&inetaddr, 0, sizeof(struct sockaddr_in6));
                inetaddr.sin6_family = AF_INET;
                inet_pton(AF_INET, m_param.mHostAddress.c_str(), &inetaddr.sin6_addr);
                inetaddr.sin6_port = htons( atoi( m_param.port.c_str()));

				serveraddr = FireNet::SockAddr( inetaddr);
                FireNet::GetNetIO()->ConnectTo( serveraddr, m_param);
			}
#else
			int retcode = getaddrinfo( m_param.host.c_str(), m_param.port.c_str(), &hints, &result);
			if( 0 == retcode && NULL != result)
			{
				serveraddr = FireNet::SockAddr( *result->ai_addr);
				freeaddrinfo( result);
			}
			else
			{
				struct sockaddr_in inetaddr;
				memset(&inetaddr, 0, sizeof(struct sockaddr_in));
				inetaddr.sin_family = AF_INET;
				inetaddr.sin_addr.s_addr = inet_addr( m_param.mHostAddress.c_str());
				inetaddr.sin_port = htons( atoi( m_param.port.c_str()));

				serveraddr = FireNet::SockAddr( inetaddr);
			}

			FireNet::GetNetIO()->ConnectTo( serveraddr, m_param);
#endif
		}
	};

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	TaskQueue::TaskQueue()
		: exit(false)
	{
	}

	TaskQueue::~TaskQueue()
	{
		exit = true;
	}

	void TaskQueue::runTasks()
	{
		std::list<Runnable*> temp;
		{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
			//if (mutex.CheckValid())
#endif
			{
				FireNet::Mutex::Scoped scoped(mutex);
				temp.swap(tasks);
			}
		}
		std::for_each(temp.begin(), temp.end(), std::mem_fun(&Runnable::runAndDestroy));
	}

	void TaskQueue::clear()
	{
		std::list<Runnable*> temp;
		{
			FireNet::Mutex::Scoped scoped(mutex);
			temp.swap(tasks);
		}
		std::for_each(temp.begin(), temp.end(), std::mem_fun(&Runnable::destroy));
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	Engine::Engine()
		: exit(false)
	{
	}
	Engine::~Engine()
	{
		exit = true;
	}

	Engine & Engine::getInstance()
	{
		static Engine engine;
		return engine;
	}

	bool Engine::Startup()
	{
		Thread::Start();
		return true;
	}
	void Engine::Cleanup()
	{
		Thread::StopRunning();
		Thread::Join();
		taskqueue.clear();

		// yeqing 2015-10-20
		FireNet::PollIO::destroy();
	}

	void Engine::Connect(const FireNet::Connector & c)
	{
		taskqueue.addTask( new ConnectTask( c));
		FireNet::PollIO::WakeUp();
	}

	void Engine::Run()
	{
		while( this->IsRunningNow())
		{
			FireNet::PollIO::Poll( 1000);
			FireNet::Timer::UpdateSelf();
			taskqueue.runTasks();
		}
	}

} // namespace aio {
