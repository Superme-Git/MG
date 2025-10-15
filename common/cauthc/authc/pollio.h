#ifndef __POLLIO_H__
#define __POLLIO_H__

#ifdef _WIN32
#include "os/windows/mutex.hpp"
#elif defined OS_IOS
#include "os/linux/mutex.hpp"
#endif

#ifdef OS_IOS

#define USE_SELECT

#ifndef POLLIN
#define POLLIN          0x001           /* There is data to read.  */
#define POLLOUT         0x004           /* Writing now will not block.  */
#define POLLERR			0x008
#endif

#ifndef max
#define max(a,b) (((a) > (b)) ? (a) : (b))
#endif

#elif defined OS_ANDROID

#define USE_EPOLL

#include <sys/time.h>
#include <sys/epoll.h>
#include <sys/poll.h>
#include <unistd.h>

#elif defined OS_WINDOWS

#define USE_SELECT
#if (defined WIN7_32)
#define POLLIN 0x001
#define POLLOUT 0x004
#define POLLERR	0x008
#else
#ifndef POLLIN
#define POLLIN 0x001
#define POLLOUT 0x004
#define POLLERR	0x008
#endif
#endif
#if !(defined NOMAXMIN)
#define max(a,b) (((a) > (b)) ? (a) : (b))
#endif
#elif defined OS_LINUX

#define USE_EPOLL

#include <sys/time.h>
#include <sys/epoll.h>
#include <sys/poll.h>
#include <unistd.h>

#endif

namespace FireNet {

	class PollIO
	{
		typedef std::map<int, PollIO*> IOMap;
		typedef std::map<PollIO*, int> EventMap;
		typedef std::set<PollIO*> EventSet;
		enum { POLLCLOSE = 0x80000000 };
		int  mEvent;
		int  mEventNew;
		bool mUpdate;
#if defined USE_EPOLL
		typedef std::vector<struct epoll_event> FDSet;
		static int ep;
#elif defined USE_SELECT
		typedef std::vector<int> FDSet;
		static fd_set rfds, wfds, all_rfds, all_wfds;
		static int maxfd;
#endif

		static IOMap iomap;
		static FDSet fdset;
		static IOMap ionew;
		static EventSet eventset;

		static Mutex locker_poll;
		static Mutex locker_event;
		static int Init();
		static bool wakeup_flag;

		virtual void PollIn() = 0;
		virtual void PollOut() { }
		virtual void PollClose() { }

#if defined USE_EPOLL
		static void LoadEvent(const EventMap::value_type event_pair)
		{
			PollIO *io = event_pair.first;
			int oldevent = event_pair.second;
			int mEventNew = io->mEvent;
			int fd = io->fd;

			struct epoll_event ev;
			if (mEventNew & POLLCLOSE)
			{
				if (oldevent)
					epoll_ctl(ep, EPOLL_CTL_DEL, fd, &ev);
				iomap.erase(fd);
				iomap[fd] = NULL;
				delete io;
			}
			else
			{
				int status = 0;
				if (mEventNew & POLLOUT) status |= EPOLLOUT;
				if (mEventNew & POLLIN)  status |= EPOLLIN;
				if (status)
				{
					ev.events = status;
					ev.data.ptr = io;
					epoll_ctl(ep, (oldevent & (POLLOUT | POLLIN)) ? EPOLL_CTL_MOD : EPOLL_CTL_ADD, io->fd, &ev);
				}
				else if (oldevent)
					epoll_ctl(ep, EPOLL_CTL_DEL, io->fd, &ev);
			}
		}

		static void UpdateEvent()
		{
			UpdateEventMap();
		}

		static void TriggerEvent(const struct epoll_event &ev)
		{
			PollIO *io = (PollIO *)ev.data.ptr;
			if (!io)
				return;
			if (ev.events & (EPOLLIN | EPOLLERR | EPOLLHUP))
				io->PollIn();
			if (ev.events & EPOLLOUT)
				io->PollOut();
			io->PollClose();
		}
#elif defined USE_SELECT
		static void LoadEvent(const EventMap::value_type event_pair)
		{
			PollIO *io = event_pair.first;
			int mEventNew = io->mEvent;
			int fd = io->fd;

			if (mEventNew & POLLCLOSE)
			{
				FD_CLR(fd, &all_rfds);
				FD_CLR(fd, &all_wfds);
				iomap.erase(fd);
				iomap[fd] = NULL;
				delete io;
			}
			else
			{
				if (mEventNew & POLLIN) FD_SET(fd, &all_rfds); else FD_CLR(fd, &all_rfds);
				if (mEventNew & POLLOUT) FD_SET(fd, &all_wfds); else FD_CLR(fd, &all_wfds);
				maxfd = max(maxfd, fd);
			}
		}

		static void UpdateEvent()
		{
			UpdateEventMap();
			rfds = all_rfds;
			wfds = all_wfds;
			fdset.clear();
			for (int i = 0; i <= maxfd; i++)
				if (FD_ISSET(i, &rfds) || FD_ISSET(i, &wfds))
					fdset.push_back(i);
		}

		static void TriggerEvent(int fd)
		{
			PollIO *io = iomap[fd];
			if (!io)
				return;
			if (FD_ISSET(fd, &rfds)) io->PollIn();
			if (FD_ISSET(fd, &wfds)) io->PollOut();
			io->PollClose();
		}
#endif

		static void UpdateEventMap()
		{
			EventMap map;
			{
				FireNet::Mutex::Scoped l(locker_event);
				for (IOMap::const_iterator it = ionew.begin(), ie = ionew.end(); it != ie; ++it)
				{
					PollIO *io = (*it).second;
					iomap[(*it).first] = io;
					if (io->mEventNew)
						eventset.insert(io);
				}
				ionew.clear();
				for (EventSet::iterator it = eventset.begin(), ie = eventset.end(); it != ie; ++it)
				{
					map.insert(std::make_pair(*it, (*it)->mEvent));
					(*it)->mUpdate = false;
					(*it)->mEvent = (*it)->mEventNew;
					(*it)->mEventNew = 0;
				}
				eventset.clear();
				wakeup_flag = true;
			}
			std::for_each(map.begin(), map.end(), std::ptr_fun(&LoadEvent));
		}

		void WakeUpEventSet()
		{
			eventset.insert(this);
			mUpdate = true;
			WakeUp();
		}

		void _PermitRecv()
		{
			if (mUpdate)
			{
				mEventNew |= POLLIN;
			}
			else if ((mEvent & POLLIN) == 0)
			{
				mEventNew = mEvent | POLLIN;
				WakeUpEventSet();
			}
		}

		void _PermitSend()
		{
			if (mUpdate)
			{
				mEventNew |= POLLOUT;
			}
			else if ((mEvent & POLLOUT) == 0)
			{
				mEventNew = mEvent | POLLOUT;
				WakeUpEventSet();
			}
		}
	protected:
		int fd;

		virtual ~PollIO()
		{
			if (0 != fd)
				closesocket(fd);
		}

		PollIO(int x)
			: mEvent(0), mEventNew(0), mUpdate(false), fd(x)
		{
#ifdef OS_WINDOWS
			u_long	ul = 1;
			ioctlsocket(fd, FIONBIO, &ul);
#else
			fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK);
#endif
		}

	public:
		static PollIO* Register(PollIO *io, bool init_permit_recv, bool init_permit_send)
		{
			FireNet::Mutex::Scoped l(locker_event);
			ionew[io->fd] = io;
			if (init_permit_recv)
				io->mEventNew |= POLLIN;
			if (init_permit_send)
				io->mEventNew |= POLLOUT;
			WakeUp();
			return io;
		}

		// yeqing 2015-10-20
		static void destroy()
		{
			FireNet::Mutex::Scoped l(locker_event);
			for (IOMap::const_iterator it = ionew.begin(), ie = ionew.end(); it != ie; ++it)
			{
				PollIO *io = (*it).second;
				delete io;
			}
			ionew.clear();

			FireNet::Mutex::Scoped lp(locker_poll);
			for (IOMap::const_iterator it = iomap.begin(), ie = iomap.end(); it != ie; ++it)
			{
				PollIO *io = (*it).second;
				delete io;
			}
			iomap.clear();
		}

		void PermitRecv()
		{
			FireNet::Mutex::Scoped l(locker_event);
			_PermitRecv();
		}

		void PermitSend()
		{
			FireNet::Mutex::Scoped l(locker_event);
			_PermitSend();
		}

		void ForbidRecv()
		{
			FireNet::Mutex::Scoped l(locker_event);
			if (mUpdate)
			{
				mEventNew &= ~POLLIN;
			}
			else if (mEvent & POLLIN)
			{
				mEventNew = mEvent & ~POLLIN;
				WakeUpEventSet();
			}
		}

		void ForbidSend()
		{
			FireNet::Mutex::Scoped l(locker_event);
			if (mUpdate)
			{
				mEventNew &= ~POLLOUT;
			}
			else if (mEvent & POLLOUT)
			{
				mEventNew = mEvent & ~POLLOUT;
				WakeUpEventSet();
			}
		}

		void Close()
		{
			FireNet::Mutex::Scoped l(locker_event);
			mEventNew |= POLLCLOSE;
			if (!mUpdate)
				WakeUpEventSet();
		}

		static int Poll(int timeout)
		{
			FireNet::Mutex::Scoped lp(locker_poll);

			static int __Init_dummy = PollIO::Init();
			UpdateEvent();
#if defined USE_EPOLL
			int maxevents = iomap.size();
			fdset.resize(maxevents);
			int nevents = epoll_wait(ep, &fdset[0], maxevents, timeout);
			wakeup_flag = false;
			if (nevents > 0)
				std::for_each(fdset.begin(), fdset.begin() + nevents, std::ptr_fun(&TriggerEvent));

#elif defined USE_SELECT
			int nevents;

#ifdef OS_WINDOWS
			{
				fd_set fsTemp = rfds;
				if (timeout < 0)
				{
					nevents = select(maxfd + 1, &rfds, &wfds, &fsTemp, 0);
				}
				else
				{
					struct timeval tv;
					tv.tv_sec = timeout / 1000;
					tv.tv_usec = (timeout - (tv.tv_sec * 1000)) * 1000;
					nevents = select(maxfd + 1, &rfds, &wfds, &fsTemp, &tv);
				}
				for (u_int i = 0; i < fsTemp.fd_count; i++)
					FD_SET(fsTemp.fd_array[i], &rfds);
			}
#else
			{
				fd_set fsTemp = rfds;
				if (timeout < 0)
				{
					nevents = select(maxfd + 1, &rfds, &wfds, &fsTemp, 0);
				}
				else
				{
					struct timeval tv;
					tv.tv_sec = timeout / 1000;
					tv.tv_usec = (timeout - (tv.tv_sec * 1000)) * 1000;
					nevents = select(maxfd + 1, &rfds, &wfds, &fsTemp, &tv);
				}
#ifdef OS_IOS
				int bytesize = sizeof(fsTemp.fds_bits) / sizeof(fsTemp.fds_bits[0]);
				int count = bytesize * __DARWIN_NFDBITS;
				for (int i = 0; i < count; ++i)
					if (FD_ISSET(i, &fsTemp))
						FD_SET(i, &rfds);
#endif
			}
#endif
			wakeup_flag = false;
			if (nevents > 0)
				std::for_each(fdset.begin(), fdset.end(), std::ptr_fun(&TriggerEvent));
			FD_ZERO(&rfds); FD_ZERO(&wfds);
#endif
			return __Init_dummy;
		}

		static void WakeUp();
	};

	class PollControl : public PollIO
	{
		friend class PollIO;
		static int writer;

		void PollIn()
		{
			for (char buff[256]; readsocket(fd, buff, 256) == 256;);
		}
		PollControl(int r, int w) : PollIO(r)
		{
			writer = w;
#ifdef OS_WINDOWS
			u_long	ul = 1;
			ioctlsocket(w, FIONBIO, &ul);
#else
			fcntl(w, F_SETFL, fcntl(w, F_GETFL) | O_NONBLOCK);
#endif
		}

		~PollControl()
		{
			closesocket(writer);
		}

		static void WakeUp()
		{
			writesocket(writer, "", 1);
		}

		static void Init()
		{
#ifdef _MSC_VER
			int			i;
			{
				WSADATA	Data;
				WSAStartup(MAKEWORD(2, 0), &Data);
			}
			SOCKET s = socket(AF_INET, SOCK_DGRAM, 0);

			struct sockaddr_in sin;
			sin.sin_family = AF_INET;
			sin.sin_addr.s_addr = inet_addr("127.0.0.1");
			sin.sin_port = htons(0);

			i = ::bind(s, (LPSOCKADDR)&sin, sizeof(sin));
			struct sockaddr addr;
			int len = sizeof(struct sockaddr);
			i = getsockname(s, &addr, &len);

			i = connect(s, (LPSOCKADDR)&addr, len);

			PollIO::Register(new PollControl((int)s, (int)s), true, false);
#else
			int pds[2];
			pipe(pds);
			PollIO::Register(new PollControl(pds[0], pds[1]), true, false);
#endif
		}
	};

	inline int PollIO::Init()
	{
#ifndef OS_WINDOWS
		signal(SIGPIPE, SIG_IGN);
#endif

#if defined USE_EPOLL
		ep = epoll_create(8192);
#elif defined USE_SELECT
		maxfd = 0;
		FD_ZERO(&rfds);
		FD_ZERO(&wfds);
#endif
		PollControl::Init();
		return 0;
	}

	inline void PollIO::WakeUp()
	{
		if (wakeup_flag)
		{
			wakeup_flag = false;
			PollControl::WakeUp();
		}
	}

} // namespace GNET {
#ifdef OS_WINDOWS
#if !(defined NOMAXMIN)
#undef max
#endif
#endif
#endif

